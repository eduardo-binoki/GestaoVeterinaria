package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.DisponibilidadeVeterinario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface DisponibilidadeVeterinarioRepository extends JpaRepository<DisponibilidadeVeterinario, Long> {

    List<DisponibilidadeVeterinario> findByVeterinarioIdOrderByDiaSemanaAscHoraInicioAsc(Long veterinarioId);

    List<DisponibilidadeVeterinario> findByVeterinarioIdAndDiaSemanaAndAtivoTrue(Long veterinarioId, DayOfWeek diaSemana);
}
