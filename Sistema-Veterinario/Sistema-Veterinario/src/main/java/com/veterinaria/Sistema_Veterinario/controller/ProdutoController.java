package com.veterinaria.Sistema_Veterinario.controller;

import com.veterinaria.Sistema_Veterinario.dto.MovimentacaoDtos.MovimentacaoRequest;
import com.veterinaria.Sistema_Veterinario.dto.MovimentacaoDtos.MovimentacaoResponse;
import com.veterinaria.Sistema_Veterinario.dto.ProdutoDtos.ProdutoRequest;
import com.veterinaria.Sistema_Veterinario.dto.ProdutoDtos.ProdutoResponse;
import com.veterinaria.Sistema_Veterinario.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','VETERINARIO','RECEPCAO')")
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    public List<ProdutoResponse> listar(@RequestParam(required = false) String nome,
                                        @RequestParam(defaultValue = "false") boolean estoqueBaixo) {
        return produtoService.listar(nome, estoqueBaixo);
    }

    @GetMapping("/estoque-baixo")
    public List<ProdutoResponse> estoqueBaixo() {
        return produtoService.listar(null, true);
    }

    @GetMapping("/vencidos")
    public List<ProdutoResponse> vencidos() {
        return produtoService.listarVencidos();
    }

    @GetMapping("/vencendo")
    public List<ProdutoResponse> vencendo(@RequestParam(defaultValue = "30") int dias) {
        return produtoService.listarVencendo(dias);
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscar(@PathVariable Long id) {
        return produtoService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','VETERINARIO')")
    public ProdutoResponse criar(@Valid @RequestBody ProdutoRequest request) {
        return produtoService.criar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VETERINARIO')")
    public ProdutoResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request) {
        return produtoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void desativar(@PathVariable Long id) {
        produtoService.desativar(id);
    }

    @GetMapping("/{id}/movimentacoes")
    public List<MovimentacaoResponse> listarMovimentacoes(@PathVariable Long id) {
        return produtoService.listarMovimentacoes(id);
    }

    @PostMapping("/{id}/movimentacoes")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','VETERINARIO')")
    public MovimentacaoResponse movimentar(@PathVariable Long id,
                                           @Valid @RequestBody MovimentacaoRequest request,
                                           Authentication authentication) {
        return produtoService.movimentar(id, request, authentication.getName());
    }
}
