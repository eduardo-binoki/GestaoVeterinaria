package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.MovimentacaoEstoque;
import com.veterinaria.Sistema_Veterinario.domain.Produto;
import com.veterinaria.Sistema_Veterinario.domain.TipoMovimentacao;
import com.veterinaria.Sistema_Veterinario.dto.MovimentacaoDtos.MovimentacaoRequest;
import com.veterinaria.Sistema_Veterinario.dto.MovimentacaoDtos.MovimentacaoResponse;
import com.veterinaria.Sistema_Veterinario.dto.ProdutoDtos.ProdutoRequest;
import com.veterinaria.Sistema_Veterinario.dto.ProdutoDtos.ProdutoResponse;
import com.veterinaria.Sistema_Veterinario.exception.BusinessException;
import com.veterinaria.Sistema_Veterinario.exception.ResourceNotFoundException;
import com.veterinaria.Sistema_Veterinario.repository.MovimentacaoEstoqueRepository;
import com.veterinaria.Sistema_Veterinario.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final RegistroAtividadeService atividadeService;

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listar(String nome, boolean apenasEstoqueBaixo) {
        List<Produto> produtos;
        if (apenasEstoqueBaixo) {
            produtos = produtoRepository.buscarEstoqueBaixo();
        } else if (nome != null && !nome.isBlank()) {
            produtos = produtoRepository.findByAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(nome.trim());
        } else {
            produtos = produtoRepository.findByAtivoTrueOrderByNomeAsc();
        }
        return produtos.stream().map(ProdutoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarVencidos() {
        return produtoRepository.buscarVencidos(LocalDate.now()).stream()
                .map(ProdutoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarVencendo(int dias) {
        LocalDate hoje = LocalDate.now();
        return produtoRepository.buscarVencendo(hoje, hoje.plusDays(dias)).stream()
                .map(ProdutoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscar(Long id) {
        return ProdutoResponse.from(buscarEntidade(id));
    }

    @Transactional
    public ProdutoResponse criar(ProdutoRequest request) {
        Produto produto = new Produto();
        preencher(produto, request);
        Produto salvo = produtoRepository.save(produto);

        if (salvo.getQuantidadeEstoque() > 0) {
            registrarMovimentacao(salvo, TipoMovimentacao.ENTRADA, salvo.getQuantidadeEstoque(),
                    "Estoque inicial", "Cadastro do produto", null, 0, salvo.getQuantidadeEstoque());
        }
        atividadeService.registrarAtual("CRIAR", "Produto", salvo.getId(), salvo.getNome());
        return ProdutoResponse.from(salvo);
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = buscarEntidade(id);
        int estoqueAnterior = produto.getQuantidadeEstoque();
        preencher(produto, request);
        int estoquePosterior = produto.getQuantidadeEstoque();
        Produto salvo = produtoRepository.save(produto);

        if (estoquePosterior != estoqueAnterior) {
            int diferenca = Math.abs(estoquePosterior - estoqueAnterior);
            TipoMovimentacao tipo = estoquePosterior > estoqueAnterior
                    ? TipoMovimentacao.ENTRADA
                    : TipoMovimentacao.SAIDA;
            registrarMovimentacao(salvo, tipo, diferenca, "Ajuste de inventário",
                    "Quantidade alterada no cadastro do produto", null,
                    estoqueAnterior, estoquePosterior);
        }
        atividadeService.registrarAtual("ATUALIZAR", "Produto", salvo.getId(), salvo.getNome());
        return ProdutoResponse.from(salvo);
    }

    @Transactional
    public void desativar(Long id) {
        Produto produto = buscarEntidade(id);
        produto.setAtivo(false);
        produtoRepository.save(produto);
        atividadeService.registrarAtual("DESATIVAR", "Produto", produto.getId(), produto.getNome());
    }

    @Transactional
    public MovimentacaoResponse movimentar(Long produtoId, MovimentacaoRequest request, String responsavel) {
        Produto produto = buscarEntidade(produtoId);
        if (!produto.isAtivo()) {
            throw new BusinessException("Produto informado está inativo");
        }

        int estoqueAnterior = produto.getQuantidadeEstoque();
        int novoEstoque = calcularNovoEstoque(estoqueAnterior, request.tipo(), request.quantidade());

        produto.setQuantidadeEstoque(novoEstoque);
        produtoRepository.save(produto);

        MovimentacaoEstoque movimentacao = registrarMovimentacao(produto, request.tipo(), request.quantidade(),
                request.motivo(), request.justificativa(), responsavel, estoqueAnterior, novoEstoque);
        atividadeService.registrarAtual("CRIAR", "MovimentacaoEstoque", movimentacao.getId(),
                request.tipo() + " de " + request.quantidade() + " - " + produto.getNome());
        return MovimentacaoResponse.from(movimentacao);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponse> listarMovimentacoes(Long produtoId) {
        if (produtoId != null) {
            if (!produtoRepository.existsById(produtoId)) {
                throw ResourceNotFoundException.de("Produto", produtoId);
            }
            return movimentacaoRepository.findByProdutoIdOrderByCriadoEmDesc(produtoId).stream()
                    .map(MovimentacaoResponse::from)
                    .toList();
        }
        return movimentacaoRepository.findAllByOrderByCriadoEmDesc().stream()
                .map(MovimentacaoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MovimentacaoResponse buscarMovimentacao(Long id) {
        return MovimentacaoResponse.from(buscarMovimentacaoEntidade(id));
    }

    @Transactional
    public MovimentacaoResponse atualizarMovimentacao(Long id, MovimentacaoRequest request) {
        MovimentacaoEstoque movimentacao = buscarMovimentacaoEntidade(id);
        Produto produto = movimentacao.getProduto();

        int estoqueBase = reverter(produto.getQuantidadeEstoque(),
                movimentacao.getTipo(), movimentacao.getQuantidade());
        int novoEstoque = calcularNovoEstoque(estoqueBase, request.tipo(), request.quantidade());

        produto.setQuantidadeEstoque(novoEstoque);
        produtoRepository.save(produto);

        movimentacao.setTipo(request.tipo());
        movimentacao.setQuantidade(request.quantidade());
        movimentacao.setMotivo(request.motivo());
        movimentacao.setJustificativa(request.justificativa());
        movimentacao.setEstoqueAnterior(estoqueBase);
        movimentacao.setEstoquePosterior(novoEstoque);

        MovimentacaoEstoque salva = movimentacaoRepository.save(movimentacao);
        atividadeService.registrarAtual("ATUALIZAR", "MovimentacaoEstoque", salva.getId(),
                salva.getProduto().getNome());
        return MovimentacaoResponse.from(salva);
    }

    @Transactional
    public void excluirMovimentacao(Long id) {
        MovimentacaoEstoque movimentacao = buscarMovimentacaoEntidade(id);
        Produto produto = movimentacao.getProduto();

        int novoEstoque = reverter(produto.getQuantidadeEstoque(),
                movimentacao.getTipo(), movimentacao.getQuantidade());
        if (novoEstoque < 0) {
            throw new BusinessException("Não é possível excluir a movimentação: o estoque ficaria negativo");
        }

        produto.setQuantidadeEstoque(novoEstoque);
        produtoRepository.save(produto);
        movimentacaoRepository.delete(movimentacao);
        atividadeService.registrarAtual("EXCLUIR", "MovimentacaoEstoque", movimentacao.getId(),
                movimentacao.getProduto().getNome());
    }

    private MovimentacaoEstoque registrarMovimentacao(Produto produto, TipoMovimentacao tipo, int quantidade,
                                                      String motivo, String justificativa, String responsavel,
                                                      int estoqueAnterior, int estoquePosterior) {
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setTipo(tipo);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setMotivo(motivo);
        movimentacao.setJustificativa(justificativa);
        movimentacao.setResponsavel(responsavel);
        movimentacao.setEstoqueAnterior(estoqueAnterior);
        movimentacao.setEstoquePosterior(estoquePosterior);
        return movimentacaoRepository.save(movimentacao);
    }

    private int calcularNovoEstoque(int estoqueAtual, TipoMovimentacao tipo, int quantidade) {
        int novoEstoque = tipo == TipoMovimentacao.ENTRADA
                ? estoqueAtual + quantidade
                : estoqueAtual - quantidade;
        if (novoEstoque < 0) {
            throw new BusinessException("Estoque insuficiente para realizar a operação");
        }
        return novoEstoque;
    }

    private int reverter(int estoqueAtual, TipoMovimentacao tipo, int quantidade) {
        return tipo == TipoMovimentacao.ENTRADA
                ? estoqueAtual - quantidade
                : estoqueAtual + quantidade;
    }

    private MovimentacaoEstoque buscarMovimentacaoEntidade(Long id) {
        return movimentacaoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Movimentação de estoque", id));
    }

    private void preencher(Produto produto, ProdutoRequest request) {
        if (request.quantidadeEstoque() < 0) {
            throw new BusinessException("Estoque não pode ser negativo");
        }
        produto.setNome(request.nome().trim());
        produto.setDescricao(request.descricao());
        produto.setCategoria(request.categoria());
        produto.setPreco(request.preco());
        produto.setQuantidadeEstoque(request.quantidadeEstoque());
        produto.setEstoqueMinimo(request.estoqueMinimo());
        produto.setLote(request.lote());
        produto.setValidade(request.validade());
        produto.setAtivo(true);
    }

    private Produto buscarEntidade(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Produto", id));
    }
}
