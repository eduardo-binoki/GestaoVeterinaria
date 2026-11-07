package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.EvolucaoInternacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvolucaoInternacaoRepository extends JpaRepository<EvolucaoInternacao, Long> {

    List<EvolucaoInternacao> findByInternacaoIdOrderByDataHoraDesc(Long internacaoId);
}
