package com.veterinaria.Sistema_Veterinario.dto;

import com.veterinaria.Sistema_Veterinario.domain.Vacina;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class VacinaDtos {

    private VacinaDtos() {
    }

    public record VacinaRequest(
            @NotNull(message = "Pet é obrigatório") Long petId,
            Long veterinarioId,
            @NotBlank(message = "Nome da vacina é obrigatório") String nome,
            String fabricante,
            String lote,
            @NotNull(message = "Data de aplicação é obrigatória") LocalDate dataAplicacao,
            LocalDate proximaDose,
            Integer dose,
            Boolean reforco,
            com.veterinaria.Sistema_Veterinario.domain.StatusVacina status,
            String observacoes
    ) {
    }

    public record VacinaResponse(
            Long id,
            Long petId,
            String petNome,
            Long veterinarioId,
            String veterinarioNome,
            String nome,
            String fabricante,
            String lote,
            LocalDate dataAplicacao,
            LocalDate proximaDose,
            Integer dose,
            boolean reforco,
            com.veterinaria.Sistema_Veterinario.domain.StatusVacina status,
            String observacoes,
            LocalDateTime criadoEm
    ) {
        public static VacinaResponse from(Vacina vacina) {
            return new VacinaResponse(
                    vacina.getId(),
                    vacina.getPet().getId(),
                    vacina.getPet().getNome(),
                    vacina.getVeterinario() != null ? vacina.getVeterinario().getId() : null,
                    vacina.getVeterinario() != null ? vacina.getVeterinario().getNome() : null,
                    vacina.getNome(),
                    vacina.getFabricante(),
                    vacina.getLote(),
                    vacina.getDataAplicacao(),
                    vacina.getProximaDose(),
                    vacina.getDose(),
                    vacina.isReforco(),
                    vacina.getStatus(),
                    vacina.getObservacoes(),
                    vacina.getCriadoEm()
            );
        }
    }
}
