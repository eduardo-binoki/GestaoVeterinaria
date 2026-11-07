package com.veterinaria.Sistema_Veterinario.controller;

import com.veterinaria.Sistema_Veterinario.dto.ProntuarioDtos.ProntuarioRequest;
import com.veterinaria.Sistema_Veterinario.dto.ProntuarioDtos.ProntuarioResponse;
import com.veterinaria.Sistema_Veterinario.service.ProntuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/prontuarios")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','VETERINARIO')")
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    @GetMapping
    public List<ProntuarioResponse> listar(@RequestParam(required = false) Long petId) {
        return prontuarioService.listar(petId);
    }

    @GetMapping("/{id}")
    public ProntuarioResponse buscar(@PathVariable Long id) {
        return prontuarioService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProntuarioResponse criar(@Valid @RequestBody ProntuarioRequest request) {
        return prontuarioService.criar(request);
    }

    @PutMapping("/{id}")
    public ProntuarioResponse atualizar(@PathVariable Long id,
                                        @Valid @RequestBody ProntuarioRequest request) {
        return prontuarioService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void excluir(@PathVariable Long id) {
        prontuarioService.excluir(id);
    }
}
