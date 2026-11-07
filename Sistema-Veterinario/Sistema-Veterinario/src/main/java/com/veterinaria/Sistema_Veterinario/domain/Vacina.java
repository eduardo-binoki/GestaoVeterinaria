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

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vacinas")
public class Vacina extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_id")
    private Veterinario veterinario;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 120)
    private String fabricante;

    @Column(length = 60)
    private String lote;

    @Column(nullable = false)
    private LocalDate dataAplicacao;

    private LocalDate proximaDose;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusVacina status = StatusVacina.APLICADA;

    private Integer dose;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean reforco = false;

    @Column(length = 500)
    private String observacoes;
}
