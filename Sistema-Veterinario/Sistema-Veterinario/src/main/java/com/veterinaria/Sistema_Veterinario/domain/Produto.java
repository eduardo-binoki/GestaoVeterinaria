package com.veterinaria.Sistema_Veterinario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CategoriaProduto categoria = CategoriaProduto.OUTRO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer quantidadeEstoque = 0;

    @Column(nullable = false)
    private Integer estoqueMinimo = 0;

    @Column(length = 60)
    private String lote;

    private java.time.LocalDate validade;

    @Column(nullable = false)
    private boolean ativo = true;
}
