package com.veterinaria.Sistema_Veterinario.dto;

import com.veterinaria.Sistema_Veterinario.domain.RegistroAtividade;
import com.veterinaria.Sistema_Veterinario.dto.ConsultaDtos.ConsultaResponse;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.ResumoFinanceiroResponse;
import com.veterinaria.Sistema_Veterinario.dto.ProdutoDtos.ProdutoResponse;
import com.veterinaria.Sistema_Veterinario.dto.VacinaDtos.VacinaResponse;

import java.time.LocalDateTime;
import java.util.List;

public final class DashboardDtos {

    private DashboardDtos() {
    }

    public record AtividadeResponse(
            Long id,
            String usuarioEmail,
            String acao,
            String entidade,
            Long entidadeId,
            String detalhes,
            LocalDateTime dataHora
    ) {
        public static AtividadeResponse from(RegistroAtividade registro) {
            return new AtividadeResponse(
                    registro.getId(),
                    registro.getUsuarioEmail(),
                    registro.getAcao(),
                    registro.getEntidade(),
                    registro.getEntidadeId(),
                    registro.getDetalhes(),
                    registro.getDataHora()
            );
        }
    }

    public record DashboardResponse(
            long totalTutores,
            long totalPets,
            long consultasHoje,
            long consultasAgendadas,
            long internacoesAtivas,
            ResumoFinanceiroResponse resumoFinanceiro,
            List<ConsultaResponse> agendaHoje,
            List<ProdutoResponse> estoqueBaixo,
            List<ProdutoResponse> produtosVencidos,
            List<VacinaResponse> vacinasProximas,
            List<AtividadeResponse> atividadesRecentes
    ) {
    }
}
