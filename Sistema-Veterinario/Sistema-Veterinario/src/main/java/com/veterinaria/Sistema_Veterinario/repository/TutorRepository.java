package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    long countByAtivoTrue();

    List<Tutor> findByAtivoTrueOrderByNomeAsc();

    List<Tutor> findByAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(String nome);
}
