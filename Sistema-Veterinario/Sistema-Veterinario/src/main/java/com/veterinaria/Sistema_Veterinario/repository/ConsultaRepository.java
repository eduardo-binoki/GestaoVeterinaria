package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.Consulta;
import com.veterinaria.Sistema_Veterinario.domain.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByPetIdOrderByDataHoraDesc(Long petId);

    List<Consulta> findByVeterinarioIdOrderByDataHoraDesc(Long veterinarioId);

    List<Consulta> findByStatusOrderByDataHoraDesc(StatusConsulta status);

    List<Consulta> findByDataHoraBetweenOrderByDataHoraAsc(LocalDateTime inicio, LocalDateTime fim);

    @Query("""
            select c from Consulta c
            where c.veterinario.id = :veterinarioId
              and c.dataHora = :dataHora
              and c.status not in (
                    com.veterinaria.Sistema_Veterinario.domain.StatusConsulta.CANCELADA,
                    com.veterinaria.Sistema_Veterinario.domain.StatusConsulta.NAO_COMPARECEU
              )
            """)
    List<Consulta> buscarConflitoAgenda(@Param("veterinarioId") Long veterinarioId,
                                        @Param("dataHora") LocalDateTime dataHora);

    boolean existsByPetId(Long petId);

    boolean existsByVeterinarioId(Long veterinarioId);

    long countByStatus(StatusConsulta status);

    long countByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
}
