package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.Internacao;
import com.veterinaria.Sistema_Veterinario.domain.StatusInternacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternacaoRepository extends JpaRepository<Internacao, Long> {

    List<Internacao> findAllByOrderByDataEntradaDesc();

    List<Internacao> findByStatusOrderByDataEntradaDesc(StatusInternacao status);

    List<Internacao> findByPetIdOrderByDataEntradaDesc(Long petId);

    boolean existsByPetIdAndStatus(Long petId, StatusInternacao status);

    long countByStatus(StatusInternacao status);
}
