package com.veterinaria.Sistema_Veterinario.dto;

import com.veterinaria.Sistema_Veterinario.domain.Prontuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public final class ProntuarioDtos {

    private ProntuarioDtos() {
    }

    public record ProntuarioRequest(
            @NotNull(message = "Consulta é obrigatória") Long consultaId,
            @NotBlank(message = "Anamnese é obrigatória") String anamnese,
            String exameFisico,
            String diagnostico,
            String prescricao,
            String observacoes,
            String evolucao,
            String procedimentos,
            String sinaisVitais,
            java.math.BigDecimal peso,
            Boolean finalizado
    ) {
    }

    public record ProntuarioResponse(
            Long id,
            Long consultaId,
            Long petId,
            String petNome,
            String anamnese,
            String exameFisico,
            String diagnostico,
            String prescricao,
            String observacoes,
            String evolucao,
            String procedimentos,
            String sinaisVitais,
            java.math.BigDecimal peso,
            boolean finalizado,
            LocalDateTime dataRegistro
    ) {
        public static ProntuarioResponse from(Prontuario prontuario) {
            return new ProntuarioResponse(
                    prontuario.getId(),
                    prontuario.getConsulta().getId(),
                    prontuario.getConsulta().getPet().getId(),
                    prontuario.getConsulta().getPet().getNome(),
                    prontuario.getAnamnese(),
                    prontuario.getExameFisico(),
                    prontuario.getDiagnostico(),
                    prontuario.getPrescricao(),
                    prontuario.getObservacoes(),
                    prontuario.getEvolucao(),
                    prontuario.getProcedimentos(),
                    prontuario.getSinaisVitais(),
                    prontuario.getPeso(),
                    prontuario.isFinalizado(),
                    prontuario.getDataRegistro()
            );
        }
    }
}
