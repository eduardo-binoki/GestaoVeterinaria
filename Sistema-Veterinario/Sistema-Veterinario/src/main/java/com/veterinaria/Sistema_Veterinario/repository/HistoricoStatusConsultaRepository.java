package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.HistoricoStatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoStatusConsultaRepository extends JpaRepository<HistoricoStatusConsulta, Long> {

    List<HistoricoStatusConsulta> findByConsultaIdOrderByDataHoraAsc(Long consultaId);
}
