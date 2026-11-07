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
@Table(name = "exames")
public class Exame extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_id")
    private Veterinario veterinario;

    @Column(nullable = false, length = 120)
    private String tipo;

    @Column(nullable = false)
    private LocalDate dataSolicitacao = LocalDate.now();

    private LocalDate dataColeta;

    private LocalDate dataResultado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusExame status = StatusExame.SOLICITADO;

    @Column(length = 4000)
    private String resultado;

    @Column(length = 1000)
    private String observacoes;

    @Column(length = 255)
    private String anexoNome;

    @Column(length = 500)
    private String anexoUrl;
}
