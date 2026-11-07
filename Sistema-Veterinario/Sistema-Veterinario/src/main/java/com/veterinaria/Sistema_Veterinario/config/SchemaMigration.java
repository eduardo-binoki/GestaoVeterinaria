package com.veterinaria.Sistema_Veterinario.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class SchemaMigration implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conexao = dataSource.getConnection()) {
            String banco = conexao.getMetaData().getDatabaseProductName();
            if (!"H2".equalsIgnoreCase(banco)) {
                log.info("SchemaMigration ignorado para o banco {}", banco);
                return;
            }
        }

        garantirValorEnum("CONSULTAS", "STATUS", "CONFIRMADA",
                "ENUM('AGENDADA','CONFIRMADA','EM_ATENDIMENTO','REALIZADA','CANCELADA','NAO_COMPARECEU')");
        garantirValorEnum("MOVIMENTACOES_ESTOQUE", "TIPO", "PERDA",
                "ENUM('ENTRADA','SAIDA','PERDA')");
    }

    private void garantirValorEnum(String tabela, String coluna, String valor, String definicao) {
        Integer existente = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.ENUM_VALUES " +
                        "WHERE OBJECT_SCHEMA = 'PUBLIC' AND OBJECT_NAME = ? AND VALUE_NAME = ?",
                Integer.class, tabela, valor);

        if (existente != null && existente > 0) {
            return;
        }

        jdbcTemplate.execute("ALTER TABLE " + tabela + " ALTER COLUMN " + coluna + " " + definicao);
        log.info("Enum atualizado: {}.{} com o valor {}", tabela, coluna, valor);
    }
}
