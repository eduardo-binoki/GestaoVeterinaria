package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.AusenciaVeterinario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AusenciaVeterinarioRepository extends JpaRepository<AusenciaVeterinario, Long> {

    List<AusenciaVeterinario> findByVeterinarioIdOrderByInicioDesc(Long veterinarioId);

    List<AusenciaVeterinario> findByVeterinarioIdAndInicioLessThanEqualAndFimGreaterThanEqual(
            Long veterinarioId, LocalDateTime fim, LocalDateTime inicio);
}
