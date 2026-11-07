package com.veterinaria.Sistema_Veterinario.dto;

import com.veterinaria.Sistema_Veterinario.domain.AusenciaVeterinario;
import com.veterinaria.Sistema_Veterinario.domain.DisponibilidadeVeterinario;
import com.veterinaria.Sistema_Veterinario.domain.Veterinario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class VeterinarioDtos {

    private VeterinarioDtos() {
    }

    public record DisponibilidadeRequest(
            @NotNull(message = "Dia da semana é obrigatório") DayOfWeek diaSemana,
            @NotNull(message = "Hora inicial é obrigatória") LocalTime horaInicio,
            @NotNull(message = "Hora final é obrigatória") LocalTime horaFim,
            Boolean ativo
    ) {
    }

    public record DisponibilidadeResponse(
            Long id,
            Long veterinarioId,
            String veterinarioNome,
            DayOfWeek diaSemana,
            LocalTime horaInicio,
            LocalTime horaFim,
            boolean ativo
    ) {
        public static DisponibilidadeResponse from(DisponibilidadeVeterinario disponibilidade) {
            return new DisponibilidadeResponse(
                    disponibilidade.getId(),
                    disponibilidade.getVeterinario().getId(),
                    disponibilidade.getVeterinario().getNome(),
                    disponibilidade.getDiaSemana(),
                    disponibilidade.getHoraInicio(),
                    disponibilidade.getHoraFim(),
                    disponibilidade.isAtivo()
            );
        }
    }

    public record AusenciaRequest(
            @NotNull(message = "Início é obrigatório") LocalDateTime inicio,
            @NotNull(message = "Fim é obrigatório") LocalDateTime fim,
            String motivo
    ) {
    }

    public record AusenciaResponse(
            Long id,
            Long veterinarioId,
            String veterinarioNome,
            LocalDateTime inicio,
            LocalDateTime fim,
            String motivo
    ) {
        public static AusenciaResponse from(AusenciaVeterinario ausencia) {
            return new AusenciaResponse(
                    ausencia.getId(),
                    ausencia.getVeterinario().getId(),
                    ausencia.getVeterinario().getNome(),
                    ausencia.getInicio(),
                    ausencia.getFim(),
                    ausencia.getMotivo()
            );
        }
    }

    public record VeterinarioRequest(
            @NotBlank(message = "Nome é obrigatório") String nome,
            @NotBlank(message = "CRMV é obrigatório") String crmv,
            String especialidade,
            @Email(message = "E-mail inválido") String email,
            String telefone
    ) {
    }

    public record VeterinarioResponse(
            Long id,
            String nome,
            String crmv,
            String especialidade,
            String email,
            String telefone,
            boolean ativo,
            LocalDateTime criadoEm
    ) {
        public static VeterinarioResponse from(Veterinario veterinario) {
            return new VeterinarioResponse(
                    veterinario.getId(),
                    veterinario.getNome(),
                    veterinario.getCrmv(),
                    veterinario.getEspecialidade(),
                    veterinario.getEmail(),
                    veterinario.getTelefone(),
                    veterinario.isAtivo(),
                    veterinario.getCriadoEm()
            );
        }
    }
}
