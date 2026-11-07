package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    boolean existsByTutorIdAndAtivoTrue(Long tutorId);

    long countByAtivoTrue();

    List<Pet> findByAtivoTrueOrderByNomeAsc();

    List<Pet> findByAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    List<Pet> findByTutorIdAndAtivoTrueOrderByNomeAsc(Long tutorId);

    List<Pet> findByTutorIdAndAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(Long tutorId, String nome);
}
