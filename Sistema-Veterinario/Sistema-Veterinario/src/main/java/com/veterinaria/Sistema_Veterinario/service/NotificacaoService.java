package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.RegistroAtividade;
import com.veterinaria.Sistema_Veterinario.dto.NotificacaoDtos.NotificacaoResponse;
import com.veterinaria.Sistema_Veterinario.repository.RegistroAtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final RegistroAtividadeRepository atividadeRepository;

    @Transactional(readOnly = true)
    public List<NotificacaoResponse> listar() {
        return atividadeRepository.buscarNaoLidas(PageRequest.of(0, 50)).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public long contar() {
        return atividadeRepository.contarNaoLidas();
    }

    @Transactional
    public void limpar() {
        atividadeRepository.marcarTodasLidas();
    }

    private NotificacaoResponse toResponse(RegistroAtividade registro) {
        return new NotificacaoResponse(
                registro.getId(),
                tipo(registro.getAcao()),
                registro.getAcao(),
                registro.getEntidade(),
                registro.getEntidadeId(),
                montarMensagem(registro),
                registro.getUsuarioEmail(),
                registro.getDataHora(),
                Boolean.TRUE.equals(registro.getLida())
        );
    }

    private String tipo(String acao) {
        if (acao == null) {
            return "alteracao";
        }
        if (acao.startsWith("CRIAR")) {
            return "cadastro";
        }
        if (acao.startsWith("EXCLUIR")) {
            return "exclusao";
        }
        return "alteracao";
    }

    private String montarMensagem(RegistroAtividade registro) {
        String entidade = nomeEntidade(registro.getEntidade());
        String detalhe = registro.getDetalhes() == null || registro.getDetalhes().isBlank()
                ? "" : ": " + registro.getDetalhes();
        String acao = registro.getAcao() == null ? "ALTERACAO" : registro.getAcao();

        if (acao.startsWith("CRIAR")) {
            return entidade + " cadastrado(a)" + detalhe;
        }
        if (acao.startsWith("EXCLUIR")) {
            return entidade + " excluído(a)" + detalhe;
        }
        if (acao.startsWith("DESATIVAR")) {
            return entidade + " desativado(a)" + detalhe;
        }
        if (acao.startsWith("PAGAR")) {
            return "Pagamento registrado" + detalhe;
        }
        if (acao.startsWith("CANCELAR_PAGAMENTO")) {
            return "Pagamento cancelado" + detalhe;
        }
        if (acao.startsWith("CANCELAR")) {
            return entidade + " cancelado(a)" + detalhe;
        }
        if (acao.startsWith("ALTA")) {
            return "Alta registrada" + detalhe;
        }
        if (acao.startsWith("EVOLUCAO")) {
            return "Evolução registrada" + detalhe;
        }
        return entidade + " alterado(a)" + detalhe;
    }

    private String nomeEntidade(String entidade) {
        if (entidade == null) {
            return "Registro";
        }
        return switch (entidade) {
            case "Tutor" -> "Tutor";
            case "Pet" -> "Pet";
            case "Veterinario" -> "Veterinário";
            case "Consulta" -> "Consulta";
            case "Prontuario" -> "Prontuário";
            case "Vacina" -> "Vacina";
            case "Produto" -> "Produto";
            case "MovimentacaoEstoque" -> "Movimentação de estoque";
            case "Exame" -> "Exame";
            case "Prescricao" -> "Prescrição";
            case "Internacao" -> "Internação";
            case "ContaFinanceira" -> "Conta financeira";
            case "CategoriaFinanceira" -> "Categoria financeira";
            case "PagamentoFinanceiro" -> "Pagamento";
            case "Usuario" -> "Usuário";
            default -> entidade;
        };
    }
}
