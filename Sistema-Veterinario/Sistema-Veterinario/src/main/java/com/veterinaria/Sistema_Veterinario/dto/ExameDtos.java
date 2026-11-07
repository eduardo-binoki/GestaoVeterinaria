package com.veterinaria.Sistema_Veterinario.dto;

import com.veterinaria.Sistema_Veterinario.domain.Exame;
import com.veterinaria.Sistema_Veterinario.domain.StatusExame;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ExameDtos {

    private ExameDtos() {
    }

    public record ExameRequest(
            @NotNull(message = "Pet é obrigatório") Long petId,
            Long consultaId,
            Long veterinarioId,
            @NotBlank(message = "Tipo é obrigatório") String tipo,
            LocalDate dataSolicitacao,
            LocalDate dataColeta,
            LocalDate dataResultado,
            StatusExame status,
            String resultado,
            String observacoes,
            String anexoNome,
            String anexoUrl
    ) {
    }

    public record ExameResponse(
            Long id,
            Long petId,
            String petNome,
            Long tutorId,
            String tutorNome,
            Long consultaId,
            Long veterinarioId,
            String veterinarioNome,
            String tipo,
            LocalDate dataSolicitacao,
            LocalDate dataColeta,
            LocalDate dataResultado,
            StatusExame status,
            String resultado,
            String observacoes,
            String anexoNome,
            String anexoUrl,
            LocalDateTime criadoEm
    ) {
        public static ExameResponse from(Exame exame) {
            return new ExameResponse(
                    exame.getId(),
                    exame.getPet().getId(),
                    exame.getPet().getNome(),
                    exame.getPet().getTutor().getId(),
                    exame.getPet().getTutor().getNome(),
                    exame.getConsulta() != null ? exame.getConsulta().getId() : null,
                    exame.getVeterinario() != null ? exame.getVeterinario().getId() : null,
                    exame.getVeterinario() != null ? exame.getVeterinario().getNome() : null,
                    exame.getTipo(),
                    exame.getDataSolicitacao(),
                    exame.getDataColeta(),
                    exame.getDataResultado(),
                    exame.getStatus(),
                    exame.getResultado(),
                    exame.getObservacoes(),
                    exame.getAnexoNome(),
                    exame.getAnexoUrl(),
                    exame.getCriadoEm()
            );
        }
    }
}
