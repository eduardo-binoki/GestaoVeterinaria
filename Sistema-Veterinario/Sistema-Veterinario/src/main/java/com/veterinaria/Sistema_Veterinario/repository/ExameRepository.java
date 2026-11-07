package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.Exame;
import com.veterinaria.Sistema_Veterinario.domain.StatusExame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExameRepository extends JpaRepository<Exame, Long> {

    List<Exame> findAllByOrderByDataSolicitacaoDesc();

    List<Exame> findByPetIdOrderByDataSolicitacaoDesc(Long petId);

    List<Exame> findByStatusOrderByDataSolicitacaoDesc(StatusExame status);

    List<Exame> findByDataSolicitacaoBetweenOrderByDataSolicitacaoAsc(LocalDate inicio, LocalDate fim);
}
