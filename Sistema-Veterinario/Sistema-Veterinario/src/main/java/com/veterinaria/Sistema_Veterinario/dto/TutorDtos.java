package com.veterinaria.Sistema_Veterinario.dto;

import com.veterinaria.Sistema_Veterinario.domain.Tutor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class TutorDtos {

    private TutorDtos() {
    }

    public record TutorRequest(
            @NotBlank(message = "Nome é obrigatório") String nome,
            @NotBlank(message = "CPF é obrigatório") String cpf,
            @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,
            @NotBlank(message = "Telefone é obrigatório") String telefone,
            String endereco,
            String cidade,
            @Size(max = 2, message = "UF deve ter 2 caracteres") String uf
    ) {
    }

    public record TutorResponse(
            Long id,
            String nome,
            String cpf,
            String email,
            String telefone,
            String endereco,
            String cidade,
            String uf,
            boolean ativo,
            LocalDateTime criadoEm
    ) {
        public static TutorResponse from(Tutor tutor) {
            return new TutorResponse(
                    tutor.getId(),
                    tutor.getNome(),
                    tutor.getCpf(),
                    tutor.getEmail(),
                    tutor.getTelefone(),
                    tutor.getEndereco(),
                    tutor.getCidade(),
                    tutor.getUf(),
                    tutor.isAtivo(),
                    tutor.getCriadoEm()
            );
        }
    }
}
