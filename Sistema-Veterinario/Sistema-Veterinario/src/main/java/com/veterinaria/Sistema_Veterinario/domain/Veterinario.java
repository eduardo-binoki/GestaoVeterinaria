package com.veterinaria.Sistema_Veterinario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "veterinarios")
public class Veterinario extends BaseEntity {

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 30)
    private String crmv;

    @Column(length = 100)
    private String especialidade;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false)
    private boolean ativo = true;
}
