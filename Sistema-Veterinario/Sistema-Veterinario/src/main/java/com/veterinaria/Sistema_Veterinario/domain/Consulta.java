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
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "consultas")
public class Consulta extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoConsulta tipo = TipoConsulta.ROTINA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusConsulta status = StatusConsulta.AGENDADA;

    @Column(nullable = false, length = 300)
    private String motivo;

    @Column(length = 1000)
    private String diagnostico;

    @Column(length = 1000)
    private String observacoes;

    @Column(precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(length = 500)
    private String justificativaCancelamento;

    private LocalDateTime iniciadaEm;

    private LocalDateTime finalizadaEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_origem_id")
    private Consulta consultaOrigem;
}
