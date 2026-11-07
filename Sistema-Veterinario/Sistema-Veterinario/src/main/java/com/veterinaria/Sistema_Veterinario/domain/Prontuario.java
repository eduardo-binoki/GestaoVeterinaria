package com.veterinaria.Sistema_Veterinario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "prontuarios")
public class Prontuario extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consulta_id", nullable = false, unique = true)
    private Consulta consulta;

    @Column(nullable = false, length = 2000)
    private String anamnese;

    @Column(length = 2000)
    private String exameFisico;

    @Column(length = 2000)
    private String diagnostico;

    @Column(length = 2000)
    private String prescricao;

    @Column(length = 1000)
    private String observacoes;

    @Column(length = 2000)
    private String evolucao;

    @Column(length = 2000)
    private String procedimentos;

    @Column(length = 1000)
    private String sinaisVitais;

    @Column(precision = 8, scale = 3)
    private java.math.BigDecimal peso;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean finalizado = false;

    @Column(nullable = false)
    private LocalDateTime dataRegistro = LocalDateTime.now();
}
