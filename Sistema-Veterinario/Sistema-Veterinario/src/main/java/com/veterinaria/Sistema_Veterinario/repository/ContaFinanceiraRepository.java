package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.ContaFinanceira;
import com.veterinaria.Sistema_Veterinario.domain.StatusContaFinanceira;
import com.veterinaria.Sistema_Veterinario.domain.TipoContaFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ContaFinanceiraRepository extends JpaRepository<ContaFinanceira, Long> {

    List<ContaFinanceira> findAllByOrderByVencimentoAsc();

    List<ContaFinanceira> findByTipoOrderByVencimentoAsc(TipoContaFinanceira tipo);

    List<ContaFinanceira> findByStatusOrderByVencimentoAsc(StatusContaFinanceira status);

    List<ContaFinanceira> findByTipoAndStatusOrderByVencimentoAsc(TipoContaFinanceira tipo,
                                                                  StatusContaFinanceira status);

    List<ContaFinanceira> findByVencimentoBetweenAndAtivoTrueOrderByVencimentoAsc(LocalDate inicio, LocalDate fim);

    boolean existsByConsultaId(Long consultaId);

    @Query("""
            select coalesce(sum(c.valorPrevisto), 0) from ContaFinanceira c
            where c.tipo = :tipo and c.ativo = true
              and c.status <> com.veterinaria.Sistema_Veterinario.domain.StatusContaFinanceira.CANCELADA
              and c.vencimento between :inicio and :fim
            """)
    BigDecimal somarPrevisto(@Param("tipo") TipoContaFinanceira tipo,
                             @Param("inicio") LocalDate inicio,
                             @Param("fim") LocalDate fim);

    @Query("""
            select coalesce(sum(c.valorPago), 0) from ContaFinanceira c
            where c.tipo = :tipo and c.ativo = true
              and c.status <> com.veterinaria.Sistema_Veterinario.domain.StatusContaFinanceira.CANCELADA
              and c.vencimento between :inicio and :fim
            """)
    BigDecimal somarPago(@Param("tipo") TipoContaFinanceira tipo,
                         @Param("inicio") LocalDate inicio,
                         @Param("fim") LocalDate fim);
}
