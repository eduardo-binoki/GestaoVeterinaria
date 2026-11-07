package com.veterinaria.Sistema_Veterinario.dto;

import com.veterinaria.Sistema_Veterinario.domain.EvolucaoInternacao;
import com.veterinaria.Sistema_Veterinario.domain.Internacao;
import com.veterinaria.Sistema_Veterinario.domain.StatusInternacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public final class InternacaoDtos {

    private InternacaoDtos() {
    }

    public record InternacaoRequest(
            @NotNull(message = "Pet é obrigatório") Long petId,
            @NotNull(message = "Veterinário é obrigatório") Long veterinarioId,
            @NotBlank(message = "Motivo é obrigatório") String motivo,
            LocalDateTime dataEntrada,
            String observacoes
    ) {
    }

    public record AltaRequest(
            @NotBlank(message = "Motivo da alta é obrigatório") String motivoAlta
    ) {
    }

    public record EvolucaoRequest(
            @NotBlank(message = "Descrição é obrigatória") String descricao,
            String sinaisVitais,
            String medicamentos,
            String alimentacaoCuidados
    ) {
    }

    public record EvolucaoResponse(
            Long id,
            Long internacaoId,
            LocalDateTime dataHora,
            String descricao,
            String sinaisVitais,
            String medicamentos,
            String alimentacaoCuidados,
            String usuarioEmail
    ) {
        public static EvolucaoResponse from(EvolucaoInternacao evolucao) {
            return new EvolucaoResponse(
                    evolucao.getId(),
                    evolucao.getInternacao().getId(),
                    evolucao.getDataHora(),
                    evolucao.getDescricao(),
                    evolucao.getSinaisVitais(),
                    evolucao.getMedicamentos(),
                    evolucao.getAlimentacaoCuidados(),
                    evolucao.getUsuarioEmail()
            );
        }
    }

    public record InternacaoResponse(
            Long id,
            Long petId,
            String petNome,
            Long tutorId,
            String tutorNome,
            Long veterinarioId,
            String veterinarioNome,
            String motivo,
            LocalDateTime dataEntrada,
            LocalDateTime dataAlta,
            String motivoAlta,
            StatusInternacao status,
            String observacoes,
            LocalDateTime criadoEm
    ) {
        public static InternacaoResponse from(Internacao internacao) {
            return new InternacaoResponse(
                    internacao.getId(),
                    internacao.getPet().getId(),
                    internacao.getPet().getNome(),
                    internacao.getPet().getTutor().getId(),
                    internacao.getPet().getTutor().getNome(),
                    internacao.getVeterinario().getId(),
                    internacao.getVeterinario().getNome(),
                    internacao.getMotivo(),
                    internacao.getDataEntrada(),
                    internacao.getDataAlta(),
                    internacao.getMotivoAlta(),
                    internacao.getStatus(),
                    internacao.getObservacoes(),
                    internacao.getCriadoEm()
            );
        }
    }
}
