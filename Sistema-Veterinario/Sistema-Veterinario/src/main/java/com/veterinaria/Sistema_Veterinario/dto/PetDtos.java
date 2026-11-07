package com.veterinaria.Sistema_Veterinario.dto;

import com.veterinaria.Sistema_Veterinario.domain.Pet;
import com.veterinaria.Sistema_Veterinario.domain.SexoPet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class PetDtos {

    private PetDtos() {
    }

    public record PetRequest(
            @NotBlank(message = "Nome é obrigatório") String nome,
            @NotBlank(message = "Espécie é obrigatória") String especie,
            String raca,
            SexoPet sexo,
            LocalDate dataNascimento,
            @PositiveOrZero(message = "Peso não pode ser negativo") BigDecimal peso,
            @NotNull(message = "Tutor é obrigatório") Long tutorId,
            String observacoes,
            String alergias,
            String doencasPreexistentes
    ) {
    }

    public record PetResponse(
            Long id,
            String nome,
            String especie,
            String raca,
            SexoPet sexo,
            LocalDate dataNascimento,
            BigDecimal peso,
            Long tutorId,
            String tutorNome,
            String observacoes,
            String alergias,
            String doencasPreexistentes,
            boolean ativo,
            LocalDateTime criadoEm
    ) {
        public static PetResponse from(Pet pet) {
            return new PetResponse(
                    pet.getId(),
                    pet.getNome(),
                    pet.getEspecie(),
                    pet.getRaca(),
                    pet.getSexo(),
                    pet.getDataNascimento(),
                    pet.getPeso(),
                    pet.getTutor().getId(),
                    pet.getTutor().getNome(),
                    pet.getObservacoes(),
                    pet.getAlergias(),
                    pet.getDoencasPreexistentes(),
                    pet.isAtivo(),
                    pet.getCriadoEm()
            );
        }
    }
}
