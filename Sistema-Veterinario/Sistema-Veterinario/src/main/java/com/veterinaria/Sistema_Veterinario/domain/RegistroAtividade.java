package com.veterinaria.Sistema_Veterinario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "registros_atividade")
public class RegistroAtividade extends BaseEntity {

    @Column(length = 150)
    private String usuarioEmail;

    @Column(nullable = false, length = 120)
    private String acao;

    @Column(length = 80)
    private String entidade;

    private Long entidadeId;

    @Column(length = 1000)
    private String detalhes;

    @Column(nullable = false)
    private LocalDateTime dataHora = LocalDateTime.now();

    private Boolean lida = false;
}
