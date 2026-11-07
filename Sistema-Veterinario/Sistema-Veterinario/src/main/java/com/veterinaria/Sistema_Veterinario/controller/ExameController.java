package com.veterinaria.Sistema_Veterinario.controller;

import com.veterinaria.Sistema_Veterinario.domain.StatusExame;
import com.veterinaria.Sistema_Veterinario.dto.ExameDtos.ExameRequest;
import com.veterinaria.Sistema_Veterinario.dto.ExameDtos.ExameResponse;
import com.veterinaria.Sistema_Veterinario.service.ExameService;
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
@RequestMapping("/api/exames")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','VETERINARIO','RECEPCAO')")
public class ExameController {

    private final ExameService exameService;

    @GetMapping
    public List<ExameResponse> listar(@RequestParam(required = false) Long petId,
                                      @RequestParam(required = false) StatusExame status) {
        return exameService.listar(petId, status);
    }

    @GetMapping("/{id}")
    public ExameResponse buscar(@PathVariable Long id) {
        return exameService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','VETERINARIO')")
    public ExameResponse criar(@Valid @RequestBody ExameRequest request, Authentication authentication) {
        return exameService.criar(request, authentication.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VETERINARIO')")
    public ExameResponse atualizar(@PathVariable Long id, @Valid @RequestBody ExameRequest request,
                                   Authentication authentication) {
        return exameService.atualizar(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void excluir(@PathVariable Long id, Authentication authentication) {
        exameService.excluir(id, authentication.getName());
    }
}
