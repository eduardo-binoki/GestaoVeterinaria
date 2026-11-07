package com.veterinaria.Sistema_Veterinario.dto;

import com.veterinaria.Sistema_Veterinario.domain.Prescricao;
import com.veterinaria.Sistema_Veterinario.domain.PrescricaoItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public final class PrescricaoDtos {

    private PrescricaoDtos() {
    }

    public record ItemRequest(
            @NotBlank(message = "Medicamento é obrigatório") String medicamento,
            @NotBlank(message = "Dosagem é obrigatória") String dosagem,
            String frequencia,
            String duracao,
            String viaAdministracao,
            String observacoes
    ) {
    }

    public record PrescricaoRequest(
            @NotNull(message = "Pet é obrigatório") Long petId,
            Long consultaId,
            Long veterinarioId,
            LocalDateTime dataPrescricao,
            String orientacoes,
            Boolean ativo,
            @NotEmpty(message = "Informe ao menos um item") List<@Valid ItemRequest> itens
    ) {
    }

    public record ItemResponse(
            Long id,
            String medicamento,
            String dosagem,
            String frequencia,
            String duracao,
            String viaAdministracao,
            String observacoes
    ) {
        public static ItemResponse from(PrescricaoItem item) {
            return new ItemResponse(item.getId(), item.getMedicamento(), item.getDosagem(),
                    item.getFrequencia(), item.getDuracao(), item.getViaAdministracao(),
                    item.getObservacoes());
        }
    }

    public record PrescricaoResponse(
            Long id,
            Long petId,
            String petNome,
            Long consultaId,
            Long veterinarioId,
            String veterinarioNome,
            LocalDateTime dataPrescricao,
            String orientacoes,
            boolean ativo,
            List<ItemResponse> itens,
            LocalDateTime criadoEm
    ) {
        public static PrescricaoResponse from(Prescricao prescricao) {
            return new PrescricaoResponse(
                    prescricao.getId(),
                    prescricao.getPet().getId(),
                    prescricao.getPet().getNome(),
                    prescricao.getConsulta() != null ? prescricao.getConsulta().getId() : null,
                    prescricao.getVeterinario() != null ? prescricao.getVeterinario().getId() : null,
                    prescricao.getVeterinario() != null ? prescricao.getVeterinario().getNome() : null,
                    prescricao.getDataPrescricao(),
                    prescricao.getOrientacoes(),
                    prescricao.isAtivo(),
                    prescricao.getItens().stream().map(ItemResponse::from).toList(),
                    prescricao.getCriadoEm()
            );
        }
    }
}
