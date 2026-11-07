package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {

    Optional<Prontuario> findByConsultaId(Long consultaId);

    List<Prontuario> findAllByOrderByDataRegistroDesc();

    List<Prontuario> findByConsultaPetIdOrderByDataRegistroDesc(Long petId);

    boolean existsByConsultaId(Long consultaId);
}
