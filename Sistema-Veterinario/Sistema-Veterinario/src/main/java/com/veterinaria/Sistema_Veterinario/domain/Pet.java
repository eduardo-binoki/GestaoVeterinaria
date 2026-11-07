package com.veterinaria.Sistema_Veterinario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pets")
public class Pet extends BaseEntity {

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 60)
    private String especie;

    @Column(length = 80)
    private String raca;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SexoPet sexo = SexoPet.INDEFINIDO;

    private LocalDate dataNascimento;

    @Column(precision = 8, scale = 3)
    private BigDecimal peso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    @Column(length = 500)
    private String observacoes;

    @Column(length = 1000)
    private String alergias;

    @Column(length = 1000)
    private String doencasPreexistentes;

    @Column(nullable = false)
    private boolean ativo = true;
}
