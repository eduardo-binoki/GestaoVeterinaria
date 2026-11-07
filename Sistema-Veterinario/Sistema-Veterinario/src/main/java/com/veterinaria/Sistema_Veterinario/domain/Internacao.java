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

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "internacoes")
public class Internacao extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    @Column(nullable = false, length = 500)
    private String motivo;

    @Column(nullable = false)
    private LocalDateTime dataEntrada = LocalDateTime.now();

    private LocalDateTime dataAlta;

    @Column(length = 500)
    private String motivoAlta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusInternacao status = StatusInternacao.ATIVA;

    @Column(length = 1000)
    private String observacoes;
}
