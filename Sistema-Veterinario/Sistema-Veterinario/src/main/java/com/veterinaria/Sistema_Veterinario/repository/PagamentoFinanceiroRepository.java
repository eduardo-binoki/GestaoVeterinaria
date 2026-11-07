package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.PagamentoFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoFinanceiroRepository extends JpaRepository<PagamentoFinanceiro, Long> {

    List<PagamentoFinanceiro> findByContaIdAndCanceladoFalseOrderByDataPagamentoAsc(Long contaId);

    List<PagamentoFinanceiro> findAllByOrderByDataPagamentoDesc();
}
