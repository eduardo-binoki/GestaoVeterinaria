package com.veterinaria.Sistema_Veterinario.dto;

import com.veterinaria.Sistema_Veterinario.domain.CategoriaProduto;
import com.veterinaria.Sistema_Veterinario.domain.Produto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class ProdutoDtos {

    private ProdutoDtos() {
    }

    public record ProdutoRequest(
            @NotBlank(message = "Nome é obrigatório") String nome,
            String descricao,
            @NotNull(message = "Categoria é obrigatória") CategoriaProduto categoria,
            @NotNull(message = "Preço é obrigatório") @DecimalMin(value = "0.0", message = "Preço não pode ser negativo") BigDecimal preco,
            @NotNull(message = "Quantidade em estoque é obrigatória") @Min(value = 0, message = "Estoque não pode ser negativo") Integer quantidadeEstoque,
            @NotNull(message = "Estoque mínimo é obrigatório") @Min(value = 0, message = "Estoque mínimo não pode ser negativo") Integer estoqueMinimo,
            String lote,
            java.time.LocalDate validade
    ) {
    }

    public record ProdutoResponse(
            Long id,
            String nome,
            String descricao,
            CategoriaProduto categoria,
            BigDecimal preco,
            Integer quantidadeEstoque,
            Integer estoqueMinimo,
            String lote,
            java.time.LocalDate validade,
            boolean estoqueBaixo,
            boolean vencido,
            boolean ativo,
            LocalDateTime criadoEm
    ) {
        public static ProdutoResponse from(Produto produto) {
            return new ProdutoResponse(
                    produto.getId(),
                    produto.getNome(),
                    produto.getDescricao(),
                    produto.getCategoria(),
                    produto.getPreco(),
                    produto.getQuantidadeEstoque(),
                    produto.getEstoqueMinimo(),
                    produto.getLote(),
                    produto.getValidade(),
                    produto.getQuantidadeEstoque() <= produto.getEstoqueMinimo(),
                    produto.getValidade() != null && produto.getValidade().isBefore(java.time.LocalDate.now()),
                    produto.isAtivo(),
                    produto.getCriadoEm()
            );
        }
    }
}
