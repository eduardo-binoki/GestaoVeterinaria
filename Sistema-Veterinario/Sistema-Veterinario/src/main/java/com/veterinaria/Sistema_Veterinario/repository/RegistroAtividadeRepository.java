package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.RegistroAtividade;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistroAtividadeRepository extends JpaRepository<RegistroAtividade, Long> {

    List<RegistroAtividade> findAllByOrderByDataHoraDesc();

    List<RegistroAtividade> findTop100ByOrderByDataHoraDesc();

    List<RegistroAtividade> findByEntidadeAndEntidadeIdOrderByDataHoraDesc(String entidade, Long entidadeId);

    @Query("""
            select r from RegistroAtividade r
            where r.lida is null or r.lida = false
            order by r.dataHora desc
            """)
    List<RegistroAtividade> buscarNaoLidas(Pageable pageable);

    @Query("select count(r) from RegistroAtividade r where r.lida is null or r.lida = false")
    long contarNaoLidas();

    @Modifying
    @Query("update RegistroAtividade r set r.lida = true where r.lida is null or r.lida = false")
    int marcarTodasLidas();
}
