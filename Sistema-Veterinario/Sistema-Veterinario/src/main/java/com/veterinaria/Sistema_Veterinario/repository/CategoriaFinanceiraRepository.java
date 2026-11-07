package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.CategoriaFinanceira;
import com.veterinaria.Sistema_Veterinario.domain.TipoContaFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaFinanceiraRepository extends JpaRepository<CategoriaFinanceira, Long> {

    List<CategoriaFinanceira> findByAtivoTrueOrderByNomeAsc();

    List<CategoriaFinanceira> findByTipoAndAtivoTrueOrderByNomeAsc(TipoContaFinanceira tipo);

    Optional<CategoriaFinanceira> findFirstByNomeIgnoreCaseAndTipo(String nome, TipoContaFinanceira tipo);

    boolean existsByNomeIgnoreCaseAndTipo(String nome, TipoContaFinanceira tipo);
}
