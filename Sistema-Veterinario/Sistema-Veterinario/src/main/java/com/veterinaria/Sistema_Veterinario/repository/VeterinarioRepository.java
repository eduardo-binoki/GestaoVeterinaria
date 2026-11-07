package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    boolean existsByCrmv(String crmv);

    boolean existsByCrmvAndIdNot(String crmv, Long id);

    List<Veterinario> findByAtivoTrueOrderByNomeAsc();
}
