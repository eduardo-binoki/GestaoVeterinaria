package com.veterinaria.Sistema_Veterinario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "evolucoes_internacao")
public class EvolucaoInternacao extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "internacao_id", nullable = false)
    private Internacao internacao;

    @Column(nullable = false)
    private LocalDateTime dataHora = LocalDateTime.now();

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Column(length = 500)
    private String sinaisVitais;

    @Column(length = 1000)
    private String medicamentos;

    @Column(length = 1000)
    private String alimentacaoCuidados;

    @Column(length = 150)
    private String usuarioEmail;
}
