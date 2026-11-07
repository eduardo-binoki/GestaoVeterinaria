package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.CategoriaFinanceira;
import com.veterinaria.Sistema_Veterinario.domain.Consulta;
import com.veterinaria.Sistema_Veterinario.domain.ContaFinanceira;
import com.veterinaria.Sistema_Veterinario.domain.PagamentoFinanceiro;
import com.veterinaria.Sistema_Veterinario.domain.StatusContaFinanceira;
import com.veterinaria.Sistema_Veterinario.domain.TipoContaFinanceira;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.CategoriaRequest;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.CategoriaResponse;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.ContaRequest;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.ContaResponse;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.PagamentoRequest;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.PagamentoResponse;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.ResumoFinanceiroResponse;
import com.veterinaria.Sistema_Veterinario.exception.BusinessException;
import com.veterinaria.Sistema_Veterinario.exception.ResourceNotFoundException;
import com.veterinaria.Sistema_Veterinario.repository.CategoriaFinanceiraRepository;
import com.veterinaria.Sistema_Veterinario.repository.ConsultaRepository;
import com.veterinaria.Sistema_Veterinario.repository.ContaFinanceiraRepository;
import com.veterinaria.Sistema_Veterinario.repository.PagamentoFinanceiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceiroService {

    private final ContaFinanceiraRepository contaRepository;
    private final CategoriaFinanceiraRepository categoriaRepository;
    private final PagamentoFinanceiroRepository pagamentoRepository;
    private final ConsultaRepository consultaRepository;
    private final RegistroAtividadeService atividadeService;

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarCategorias(TipoContaFinanceira tipo) {
        List<CategoriaFinanceira> categorias = tipo == null
                ? categoriaRepository.findByAtivoTrueOrderByNomeAsc()
                : categoriaRepository.findByTipoAndAtivoTrueOrderByNomeAsc(tipo);
        return categorias.stream().map(CategoriaResponse::from).toList();
    }

    @Transactional
    public CategoriaResponse criarCategoria(CategoriaRequest request, String usuarioEmail) {
        String nome = request.nome().trim();
        if (categoriaRepository.existsByNomeIgnoreCaseAndTipo(nome, request.tipo())) {
            throw new BusinessException("Já existe uma categoria com este nome e tipo");
        }
        CategoriaFinanceira categoria = new CategoriaFinanceira();
        categoria.setNome(nome);
        categoria.setTipo(request.tipo());
        CategoriaFinanceira salva = categoriaRepository.save(categoria);
        atividadeService.registrar(usuarioEmail, "CRIAR", "CategoriaFinanceira", salva.getId(), nome);
        return CategoriaResponse.from(salva);
    }

    @Transactional(readOnly = true)
    public List<ContaResponse> listarContas(TipoContaFinanceira tipo, StatusContaFinanceira status,
                                            LocalDate inicio, LocalDate fim) {
        return contaRepository.findAllByOrderByVencimentoAsc().stream()
                .filter(ContaFinanceira::isAtivo)
                .filter(c -> tipo == null || c.getTipo() == tipo)
                .filter(c -> status == null || c.getStatus() == status)
                .filter(c -> inicio == null || !c.getVencimento().isBefore(inicio))
                .filter(c -> fim == null || !c.getVencimento().isAfter(fim))
                .map(ContaResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContaResponse buscarConta(Long id) {
        return ContaResponse.from(buscarContaEntidade(id));
    }

    @Transactional
    public ContaResponse criarConta(ContaRequest request, String usuarioEmail) {
        return ContaResponse.from(criarContaEntidade(request, usuarioEmail));
    }

    @Transactional
    public ContaResponse atualizarConta(Long id, ContaRequest request, String usuarioEmail) {
        ContaFinanceira conta = buscarContaEntidade(id);
        if (conta.getStatus() == StatusContaFinanceira.CANCELADA) {
            throw new BusinessException("Conta cancelada não pode ser alterada");
        }
        if (request.valorPrevisto().compareTo(conta.getValorPago()) < 0) {
            throw new BusinessException("Valor previsto não pode ser menor que o valor já pago");
        }
        CategoriaFinanceira categoria = buscarCategoria(request.categoriaId(), request.tipo());
        Consulta consulta = buscarConsulta(request.consultaId());

        conta.setDescricao(request.descricao().trim());
        conta.setTipo(request.tipo());
        conta.setCategoria(categoria);
        conta.setValorPrevisto(request.valorPrevisto());
        conta.setDataEmissao(request.dataEmissao() != null ? request.dataEmissao() : conta.getDataEmissao());
        conta.setVencimento(request.vencimento());
        conta.setConsulta(consulta);
        conta.setObservacoes(request.observacoes());
        atualizarStatusPorValores(conta);

        ContaFinanceira salva = contaRepository.save(conta);
        atividadeService.registrar(usuarioEmail, "ATUALIZAR", "ContaFinanceira", salva.getId(),
                salva.getDescricao());
        return ContaResponse.from(salva);
    }

    @Transactional
    public void cancelarConta(Long id, String usuarioEmail) {
        ContaFinanceira conta = buscarContaEntidade(id);
        if (conta.getValorPago().compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException("Conta com pagamentos registrados não pode ser cancelada");
        }
        conta.setStatus(StatusContaFinanceira.CANCELADA);
        contaRepository.save(conta);
        atividadeService.registrar(usuarioEmail, "CANCELAR", "ContaFinanceira", id, conta.getDescricao());
    }

    @Transactional
    public void excluirConta(Long id, String usuarioEmail) {
        ContaFinanceira conta = buscarContaEntidade(id);
        if (conta.getValorPago().compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException("Conta com pagamentos registrados não pode ser excluída");
        }
        conta.setAtivo(false);
        contaRepository.save(conta);
        atividadeService.registrar(usuarioEmail, "EXCLUIR", "ContaFinanceira", id, conta.getDescricao());
    }

    @Transactional
    public PagamentoResponse registrarPagamento(Long contaId, PagamentoRequest request, String usuarioEmail) {
        ContaFinanceira conta = buscarContaEntidade(contaId);
        if (!conta.isAtivo() || conta.getStatus() == StatusContaFinanceira.CANCELADA) {
            throw new BusinessException("Conta não está disponível para pagamento");
        }
        BigDecimal saldo = conta.getValorPrevisto().subtract(conta.getValorPago());
        if (request.valor().compareTo(saldo) > 0) {
            throw new BusinessException("Valor do pagamento maior que o saldo da conta");
        }

        PagamentoFinanceiro pagamento = new PagamentoFinanceiro();
        pagamento.setConta(conta);
        pagamento.setValor(request.valor());
        pagamento.setDataPagamento(request.dataPagamento() != null ? request.dataPagamento() : LocalDate.now());
        pagamento.setFormaPagamento(request.formaPagamento());
        pagamento.setObservacoes(request.observacoes());
        pagamentoRepository.save(pagamento);

        conta.setValorPago(conta.getValorPago().add(request.valor()));
        atualizarStatusPorValores(conta);
        contaRepository.save(conta);

        atividadeService.registrar(usuarioEmail, "PAGAR", "ContaFinanceira", conta.getId(),
                "Pagamento de " + request.valor() + " na conta " + conta.getDescricao());
        return PagamentoResponse.from(pagamento);
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponse> listarPagamentos(Long contaId) {
        if (contaId != null) {
            buscarContaEntidade(contaId);
            return pagamentoRepository.findByContaIdAndCanceladoFalseOrderByDataPagamentoAsc(contaId).stream()
                    .map(PagamentoResponse::from)
                    .toList();
        }
        return pagamentoRepository.findAllByOrderByDataPagamentoDesc().stream()
                .map(PagamentoResponse::from)
                .toList();
    }

    @Transactional
    public void cancelarPagamento(Long id, String usuarioEmail) {
        PagamentoFinanceiro pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Pagamento", id));
        if (pagamento.isCancelado()) {
            throw new BusinessException("Pagamento já está cancelado");
        }
        ContaFinanceira conta = pagamento.getConta();
        conta.setValorPago(conta.getValorPago().subtract(pagamento.getValor()));
        atualizarStatusPorValores(conta);
        contaRepository.save(conta);

        pagamento.setCancelado(true);
        pagamentoRepository.save(pagamento);
        atividadeService.registrar(usuarioEmail, "CANCELAR_PAGAMENTO", "PagamentoFinanceiro", id,
                "Cancelamento de pagamento da conta " + conta.getDescricao());
    }

    @Transactional(readOnly = true)
    public ResumoFinanceiroResponse resumo(LocalDate inicio, LocalDate fim) {
        LocalDate inicioEfetivo = inicio != null ? inicio : LocalDate.now().withDayOfMonth(1);
        LocalDate fimEfetivo = fim != null ? fim : LocalDate.now();
        if (fimEfetivo.isBefore(inicioEfetivo)) {
            throw new BusinessException("Data final não pode ser anterior à data inicial");
        }

        BigDecimal receitasPrevistas = contaRepository.somarPrevisto(TipoContaFinanceira.RECEITA, inicioEfetivo, fimEfetivo);
        BigDecimal despesasPrevistas = contaRepository.somarPrevisto(TipoContaFinanceira.DESPESA, inicioEfetivo, fimEfetivo);
        BigDecimal receitasPagas = contaRepository.somarPago(TipoContaFinanceira.RECEITA, inicioEfetivo, fimEfetivo);
        BigDecimal despesasPagas = contaRepository.somarPago(TipoContaFinanceira.DESPESA, inicioEfetivo, fimEfetivo);

        return new ResumoFinanceiroResponse(
                inicioEfetivo,
                fimEfetivo,
                receitasPrevistas,
                receitasPagas,
                despesasPrevistas,
                despesasPagas,
                receitasPrevistas.subtract(despesasPrevistas),
                receitasPagas.subtract(despesasPagas)
        );
    }

    @Transactional
    public void registrarReceitaConsulta(Consulta consulta) {
        if (consulta.getValor() == null || consulta.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        if (contaRepository.existsByConsultaId(consulta.getId())) {
            return;
        }

        CategoriaFinanceira categoria = categoriaRepository
                .findFirstByNomeIgnoreCaseAndTipo("Consultas", TipoContaFinanceira.RECEITA)
                .orElse(null);

        ContaFinanceira conta = new ContaFinanceira();
        conta.setDescricao("Consulta #" + consulta.getId() + " - " + consulta.getPet().getNome());
        conta.setTipo(TipoContaFinanceira.RECEITA);
        conta.setCategoria(categoria);
        conta.setValorPrevisto(consulta.getValor());
        conta.setDataEmissao(LocalDate.now());
        conta.setVencimento(LocalDate.now());
        conta.setConsulta(consulta);
        conta.setObservacoes("Receita gerada automaticamente ao finalizar a consulta");
        contaRepository.save(conta);
    }

    private ContaFinanceira criarContaEntidade(ContaRequest request, String usuarioEmail) {
        if (request.consultaId() != null && request.tipo() == TipoContaFinanceira.RECEITA
                && contaRepository.existsByConsultaId(request.consultaId())) {
            throw new BusinessException("Já existe uma receita vinculada a esta consulta");
        }
        CategoriaFinanceira categoria = buscarCategoria(request.categoriaId(), request.tipo());
        Consulta consulta = buscarConsulta(request.consultaId());

        ContaFinanceira conta = new ContaFinanceira();
        conta.setDescricao(request.descricao().trim());
        conta.setTipo(request.tipo());
        conta.setCategoria(categoria);
        conta.setValorPrevisto(request.valorPrevisto());
        conta.setDataEmissao(request.dataEmissao() != null ? request.dataEmissao() : LocalDate.now());
        conta.setVencimento(request.vencimento());
        conta.setConsulta(consulta);
        conta.setObservacoes(request.observacoes());
        atualizarStatusPorValores(conta);

        ContaFinanceira salva = contaRepository.save(conta);
        atividadeService.registrar(usuarioEmail, "CRIAR", "ContaFinanceira", salva.getId(),
                salva.getDescricao());
        return salva;
    }

    private void atualizarStatusPorValores(ContaFinanceira conta) {
        if (conta.getStatus() == StatusContaFinanceira.CANCELADA) {
            return;
        }
        if (conta.getValorPago().compareTo(BigDecimal.ZERO) <= 0) {
            conta.setStatus(StatusContaFinanceira.PENDENTE);
        } else if (conta.getValorPago().compareTo(conta.getValorPrevisto()) >= 0) {
            conta.setStatus(StatusContaFinanceira.PAGA);
        } else {
            conta.setStatus(StatusContaFinanceira.PARCIAL);
        }
    }

    private CategoriaFinanceira buscarCategoria(Long categoriaId, TipoContaFinanceira tipo) {
        if (categoriaId == null) {
            return null;
        }
        CategoriaFinanceira categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> ResourceNotFoundException.de("Categoria financeira", categoriaId));
        if (!categoria.isAtivo() || categoria.getTipo() != tipo) {
            throw new BusinessException("Categoria financeira incompatível com o tipo da conta");
        }
        return categoria;
    }

    private Consulta buscarConsulta(Long consultaId) {
        if (consultaId == null) {
            return null;
        }
        return consultaRepository.findById(consultaId)
                .orElseThrow(() -> ResourceNotFoundException.de("Consulta", consultaId));
    }

    private ContaFinanceira buscarContaEntidade(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Conta financeira", id));
    }
}
