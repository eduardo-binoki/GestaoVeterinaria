package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VacinaRepository extends JpaRepository<Vacina, Long> {

    List<Vacina> findAllByOrderByDataAplicacaoDesc();

    List<Vacina> findByPetIdOrderByDataAplicacaoDesc(Long petId);

    List<Vacina> findByProximaDoseBetweenOrderByProximaDoseAsc(LocalDate inicio, LocalDate fim);
}
