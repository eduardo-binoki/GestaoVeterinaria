package com.veterinaria.Sistema_Veterinario.controller;

import com.veterinaria.Sistema_Veterinario.domain.StatusInternacao;
import com.veterinaria.Sistema_Veterinario.dto.InternacaoDtos.AltaRequest;
import com.veterinaria.Sistema_Veterinario.dto.InternacaoDtos.EvolucaoRequest;
import com.veterinaria.Sistema_Veterinario.dto.InternacaoDtos.EvolucaoResponse;
import com.veterinaria.Sistema_Veterinario.dto.InternacaoDtos.InternacaoRequest;
import com.veterinaria.Sistema_Veterinario.dto.InternacaoDtos.InternacaoResponse;
import com.veterinaria.Sistema_Veterinario.service.InternacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/internacoes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','VETERINARIO','RECEPCAO')")
public class InternacaoController {

    private final InternacaoService internacaoService;

    @GetMapping
    public List<InternacaoResponse> listar(@RequestParam(required = false) StatusInternacao status,
                                           @RequestParam(required = false) Long petId) {
        return internacaoService.listar(status, petId);
    }

    @GetMapping("/{id}")
    public InternacaoResponse buscar(@PathVariable Long id) {
        return internacaoService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','VETERINARIO')")
    public InternacaoResponse criar(@Valid @RequestBody InternacaoRequest request,
                                    Authentication authentication) {
        return internacaoService.criar(request, authentication.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VETERINARIO')")
    public InternacaoResponse atualizar(@PathVariable Long id,
                                        @Valid @RequestBody InternacaoRequest request,
                                        Authentication authentication) {
        return internacaoService.atualizar(id, request, authentication.getName());
    }

    @PatchMapping("/{id}/alta")
    @PreAuthorize("hasAnyRole('ADMIN','VETERINARIO')")
    public InternacaoResponse darAlta(@PathVariable Long id,
                                      @Valid @RequestBody AltaRequest request,
                                      Authentication authentication) {
        return internacaoService.darAlta(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void excluir(@PathVariable Long id, Authentication authentication) {
        internacaoService.excluir(id, authentication.getName());
    }

    @GetMapping("/{id}/evolucoes")
    public List<EvolucaoResponse> listarEvolucoes(@PathVariable Long id) {
        return internacaoService.listarEvolucoes(id);
    }

    @PostMapping("/{id}/evolucoes")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','VETERINARIO')")
    public EvolucaoResponse registrarEvolucao(@PathVariable Long id,
                                              @Valid @RequestBody EvolucaoRequest request,
                                              Authentication authentication) {
        return internacaoService.registrarEvolucao(id, request, authentication.getName());
    }

    @DeleteMapping("/evolucoes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void excluirEvolucao(@PathVariable Long id, Authentication authentication) {
        internacaoService.excluirEvolucao(id, authentication.getName());
    }
}
