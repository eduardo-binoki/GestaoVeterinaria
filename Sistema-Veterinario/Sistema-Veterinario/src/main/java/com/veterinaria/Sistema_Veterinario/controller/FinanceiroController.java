package com.veterinaria.Sistema_Veterinario.controller;

import com.veterinaria.Sistema_Veterinario.domain.StatusContaFinanceira;
import com.veterinaria.Sistema_Veterinario.domain.TipoContaFinanceira;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.CategoriaRequest;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.CategoriaResponse;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.ContaRequest;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.ContaResponse;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.PagamentoRequest;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.PagamentoResponse;
import com.veterinaria.Sistema_Veterinario.dto.FinanceiroDtos.ResumoFinanceiroResponse;
import com.veterinaria.Sistema_Veterinario.service.FinanceiroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/financeiro")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','RECEPCAO','VETERINARIO')")
public class FinanceiroController {

    private final FinanceiroService financeiroService;

    @GetMapping("/categorias")
    public List<CategoriaResponse> listarCategorias(@RequestParam(required = false) TipoContaFinanceira tipo) {
        return financeiroService.listarCategorias(tipo);
    }

    @PostMapping("/categorias")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public CategoriaResponse criarCategoria(@Valid @RequestBody CategoriaRequest request,
                                            Authentication authentication) {
        return financeiroService.criarCategoria(request, authentication.getName());
    }

    @GetMapping("/contas")
    public List<ContaResponse> listarContas(
            @RequestParam(required = false) TipoContaFinanceira tipo,
            @RequestParam(required = false) StatusContaFinanceira status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return financeiroService.listarContas(tipo, status, inicio, fim);
    }

    @GetMapping("/contas/{id}")
    public ContaResponse buscarConta(@PathVariable Long id) {
        return financeiroService.buscarConta(id);
    }

    @PostMapping("/contas")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCAO')")
    public ContaResponse criarConta(@Valid @RequestBody ContaRequest request, Authentication authentication) {
        return financeiroService.criarConta(request, authentication.getName());
    }

    @PutMapping("/contas/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCAO')")
    public ContaResponse atualizarConta(@PathVariable Long id,
                                        @Valid @RequestBody ContaRequest request,
                                        Authentication authentication) {
        return financeiroService.atualizarConta(id, request, authentication.getName());
    }

    @PostMapping("/contas/{id}/cancelar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCAO')")
    public void cancelarConta(@PathVariable Long id, Authentication authentication) {
        financeiroService.cancelarConta(id, authentication.getName());
    }

    @DeleteMapping("/contas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void excluirConta(@PathVariable Long id, Authentication authentication) {
        financeiroService.excluirConta(id, authentication.getName());
    }

    @GetMapping("/pagamentos")
    public List<PagamentoResponse> listarPagamentos(@RequestParam(required = false) Long contaId) {
        return financeiroService.listarPagamentos(contaId);
    }

    @PostMapping("/contas/{id}/pagamentos")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCAO')")
    public PagamentoResponse registrarPagamento(@PathVariable Long id,
                                                @Valid @RequestBody PagamentoRequest request,
                                                Authentication authentication) {
        return financeiroService.registrarPagamento(id, request, authentication.getName());
    }

    @PostMapping("/pagamentos/{id}/cancelar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void cancelarPagamento(@PathVariable Long id, Authentication authentication) {
        financeiroService.cancelarPagamento(id, authentication.getName());
    }

    @GetMapping("/resumo")
    public ResumoFinanceiroResponse resumo(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return financeiroService.resumo(inicio, fim);
    }
}
