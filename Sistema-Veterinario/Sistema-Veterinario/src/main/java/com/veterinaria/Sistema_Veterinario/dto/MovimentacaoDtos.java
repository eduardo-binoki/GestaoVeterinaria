package com.veterinaria.Sistema_Veterinario.dto;

import com.veterinaria.Sistema_Veterinario.domain.MovimentacaoEstoque;
import com.veterinaria.Sistema_Veterinario.domain.TipoMovimentacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public final class MovimentacaoDtos {

    private MovimentacaoDtos() {
    }

    public record MovimentacaoRequest(
            @NotNull(message = "Tipo é obrigatório") TipoMovimentacao tipo,
            @NotNull(message = "Quantidade é obrigatória") @Positive(message = "Quantidade deve ser maior que zero") Integer quantidade,
            String motivo,
            String justificativa
    ) {
    }

    public record CriarMovimentacaoRequest(
            @NotNull(message = "Produto é obrigatório") Long produtoId,
            @NotNull(message = "Tipo é obrigatório") TipoMovimentacao tipo,
            @NotNull(message = "Quantidade é obrigatória") @Positive(message = "Quantidade deve ser maior que zero") Integer quantidade,
            String motivo,
            String justificativa
    ) {
        public MovimentacaoRequest paraRequest() {
            return new MovimentacaoRequest(tipo, quantidade, motivo, justificativa);
        }
    }

    public record MovimentacaoResponse(
            Long id,
            Long produtoId,
            String produtoNome,
            TipoMovimentacao tipo,
            Integer quantidade,
            Integer estoqueAnterior,
            Integer estoquePosterior,
            String motivo,
            String justificativa,
            String responsavel,
            LocalDateTime dataHora
    ) {
        public static MovimentacaoResponse from(MovimentacaoEstoque movimentacao) {
            return new MovimentacaoResponse(
                    movimentacao.getId(),
                    movimentacao.getProduto().getId(),
                    movimentacao.getProduto().getNome(),
                    movimentacao.getTipo(),
                    movimentacao.getQuantidade(),
                    movimentacao.getEstoqueAnterior(),
                    movimentacao.getEstoquePosterior(),
                    movimentacao.getMotivo(),
                    movimentacao.getJustificativa(),
                    movimentacao.getResponsavel(),
                    movimentacao.getCriadoEm()
            );
        }
    }
}
