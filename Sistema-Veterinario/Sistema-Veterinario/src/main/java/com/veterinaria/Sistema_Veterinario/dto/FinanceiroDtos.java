package com.veterinaria.Sistema_Veterinario.dto;

import com.veterinaria.Sistema_Veterinario.domain.CategoriaFinanceira;
import com.veterinaria.Sistema_Veterinario.domain.ContaFinanceira;
import com.veterinaria.Sistema_Veterinario.domain.FormaPagamento;
import com.veterinaria.Sistema_Veterinario.domain.PagamentoFinanceiro;
import com.veterinaria.Sistema_Veterinario.domain.StatusContaFinanceira;
import com.veterinaria.Sistema_Veterinario.domain.TipoContaFinanceira;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class FinanceiroDtos {

    private FinanceiroDtos() {
    }

    public record CategoriaRequest(
            @NotBlank(message = "Nome é obrigatório") String nome,
            @NotNull(message = "Tipo é obrigatório") TipoContaFinanceira tipo
    ) {
    }

    public record CategoriaResponse(
            Long id,
            String nome,
            TipoContaFinanceira tipo,
            boolean ativo
    ) {
        public static CategoriaResponse from(CategoriaFinanceira categoria) {
            return new CategoriaResponse(categoria.getId(), categoria.getNome(),
                    categoria.getTipo(), categoria.isAtivo());
        }
    }

    public record ContaRequest(
            @NotBlank(message = "Descrição é obrigatória") String descricao,
            @NotNull(message = "Tipo é obrigatório") TipoContaFinanceira tipo,
            Long categoriaId,
            @NotNull(message = "Valor é obrigatório")
            @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero") BigDecimal valorPrevisto,
            LocalDate dataEmissao,
            @NotNull(message = "Vencimento é obrigatório") LocalDate vencimento,
            Long consultaId,
            String observacoes
    ) {
    }

    public record ContaResponse(
            Long id,
            String descricao,
            TipoContaFinanceira tipo,
            Long categoriaId,
            String categoriaNome,
            BigDecimal valorPrevisto,
            BigDecimal valorPago,
            BigDecimal saldo,
            LocalDate dataEmissao,
            LocalDate vencimento,
            StatusContaFinanceira status,
            boolean atrasada,
            Long consultaId,
            String observacoes,
            LocalDateTime criadoEm
    ) {
        public static ContaResponse from(ContaFinanceira conta) {
            BigDecimal saldo = conta.getValorPrevisto().subtract(conta.getValorPago());
            boolean atrasada = conta.getStatus() != StatusContaFinanceira.PAGA
                    && conta.getStatus() != StatusContaFinanceira.CANCELADA
                    && conta.getVencimento().isBefore(LocalDate.now());
            return new ContaResponse(
                    conta.getId(),
                    conta.getDescricao(),
                    conta.getTipo(),
                    conta.getCategoria() != null ? conta.getCategoria().getId() : null,
                    conta.getCategoria() != null ? conta.getCategoria().getNome() : null,
                    conta.getValorPrevisto(),
                    conta.getValorPago(),
                    saldo,
                    conta.getDataEmissao(),
                    conta.getVencimento(),
                    conta.getStatus(),
                    atrasada,
                    conta.getConsulta() != null ? conta.getConsulta().getId() : null,
                    conta.getObservacoes(),
                    conta.getCriadoEm()
            );
        }
    }

    public record PagamentoRequest(
            @NotNull(message = "Valor é obrigatório")
            @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero") BigDecimal valor,
            LocalDate dataPagamento,
            @NotNull(message = "Forma de pagamento é obrigatória") FormaPagamento formaPagamento,
            String observacoes
    ) {
    }

    public record PagamentoResponse(
            Long id,
            Long contaId,
            String contaDescricao,
            BigDecimal valor,
            LocalDate dataPagamento,
            FormaPagamento formaPagamento,
            String observacoes,
            boolean cancelado
    ) {
        public static PagamentoResponse from(PagamentoFinanceiro pagamento) {
            return new PagamentoResponse(
                    pagamento.getId(),
                    pagamento.getConta().getId(),
                    pagamento.getConta().getDescricao(),
                    pagamento.getValor(),
                    pagamento.getDataPagamento(),
                    pagamento.getFormaPagamento(),
                    pagamento.getObservacoes(),
                    pagamento.isCancelado()
            );
        }
    }

    public record ResumoFinanceiroResponse(
            LocalDate inicio,
            LocalDate fim,
            BigDecimal receitasPrevistas,
            BigDecimal receitasPagas,
            BigDecimal despesasPrevistas,
            BigDecimal despesasPagas,
            BigDecimal saldoPrevisto,
            BigDecimal saldoRealizado
    ) {
    }
}
