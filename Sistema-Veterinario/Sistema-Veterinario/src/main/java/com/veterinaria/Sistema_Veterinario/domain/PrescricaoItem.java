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

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "prescricao_itens")
public class PrescricaoItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prescricao_id", nullable = false)
    private Prescricao prescricao;

    @Column(nullable = false, length = 150)
    private String medicamento;

    @Column(nullable = false, length = 100)
    private String dosagem;

    @Column(length = 100)
    private String frequencia;

    @Column(length = 100)
    private String duracao;

    @Column(length = 80)
    private String viaAdministracao;

    @Column(length = 500)
    private String observacoes;
}
