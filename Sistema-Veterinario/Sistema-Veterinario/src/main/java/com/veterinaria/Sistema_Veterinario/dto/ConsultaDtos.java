package com.veterinaria.Sistema_Veterinario.dto;

import com.veterinaria.Sistema_Veterinario.domain.Consulta;
import com.veterinaria.Sistema_Veterinario.domain.HistoricoStatusConsulta;
import com.veterinaria.Sistema_Veterinario.domain.StatusConsulta;
import com.veterinaria.Sistema_Veterinario.domain.TipoConsulta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class ConsultaDtos {

    private ConsultaDtos() {
    }

    public record ConsultaRequest(
            @NotNull(message = "Pet é obrigatório") Long petId,
            @NotNull(message = "Veterinário é obrigatório") Long veterinarioId,
            @NotNull(message = "Data e hora são obrigatórias") LocalDateTime dataHora,
            @NotNull(message = "Tipo é obrigatório") TipoConsulta tipo,
            @NotBlank(message = "Motivo é obrigatório") String motivo,
            String diagnostico,
            String observacoes,
            @PositiveOrZero(message = "Valor não pode ser negativo") BigDecimal valor,
            StatusConsulta status,
            String justificativaCancelamento,
            Long consultaOrigemId
    ) {
    }

    public record AlterarStatusConsultaRequest(
            @NotNull(message = "Status é obrigatório") StatusConsulta status,
            String justificativa
    ) {
    }

    public record HistoricoStatusResponse(
            Long id,
            StatusConsulta statusAnterior,
            StatusConsulta statusNovo,
            LocalDateTime dataHora,
            String usuarioEmail,
            String justificativa
    ) {
        public static HistoricoStatusResponse from(HistoricoStatusConsulta historico) {
            return new HistoricoStatusResponse(
                    historico.getId(),
                    historico.getStatusAnterior(),
                    historico.getStatusNovo(),
                    historico.getDataHora(),
                    historico.getUsuarioEmail(),
                    historico.getJustificativa()
            );
        }
    }

    public record ConsultaResponse(
            Long id,
            Long petId,
            String petNome,
            Long tutorId,
            String tutorNome,
            Long veterinarioId,
            String veterinarioNome,
            LocalDateTime dataHora,
            TipoConsulta tipo,
            StatusConsulta status,
            String motivo,
            String diagnostico,
            String observacoes,
            BigDecimal valor,
            String justificativaCancelamento,
            LocalDateTime iniciadaEm,
            LocalDateTime finalizadaEm,
            LocalDateTime criadoEm
    ) {
        public static ConsultaResponse from(Consulta consulta) {
            return new ConsultaResponse(
                    consulta.getId(),
                    consulta.getPet().getId(),
                    consulta.getPet().getNome(),
                    consulta.getPet().getTutor().getId(),
                    consulta.getPet().getTutor().getNome(),
                    consulta.getVeterinario().getId(),
                    consulta.getVeterinario().getNome(),
                    consulta.getDataHora(),
                    consulta.getTipo(),
                    consulta.getStatus(),
                    consulta.getMotivo(),
                    consulta.getDiagnostico(),
                    consulta.getObservacoes(),
                    consulta.getValor(),
                    consulta.getJustificativaCancelamento(),
                    consulta.getIniciadaEm(),
                    consulta.getFinalizadaEm(),
                    consulta.getCriadoEm()
            );
        }
    }
}
