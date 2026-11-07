package com.veterinaria.Sistema_Veterinario.repository;

import com.veterinaria.Sistema_Veterinario.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByAtivoTrueOrderByNomeAsc();

    List<Produto> findByAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    @Query("select p from Produto p where p.ativo = true and p.quantidadeEstoque <= p.estoqueMinimo order by p.nome")
    List<Produto> buscarEstoqueBaixo();

    @Query("""
            select p from Produto p
            where p.ativo = true and p.validade is not null and p.validade < :hoje
            order by p.validade
            """)
    List<Produto> buscarVencidos(@Param("hoje") LocalDate hoje);

    @Query("""
            select p from Produto p
            where p.ativo = true and p.validade is not null
              and p.validade between :hoje and :limite
            order by p.validade
            """)
    List<Produto> buscarVencendo(@Param("hoje") LocalDate hoje, @Param("limite") LocalDate limite);
}
