package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.Prescricao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescricaoRepository extends JpaRepository<Prescricao, Long> {

    List<Prescricao> findAllByOrderByDataPrescricaoDesc();

    List<Prescricao> findByPetIdOrderByDataPrescricaoDesc(Long petId);

    List<Prescricao> findByConsultaIdOrderByDataPrescricaoDesc(Long consultaId);

    boolean existsByConsultaId(Long consultaId);
}
