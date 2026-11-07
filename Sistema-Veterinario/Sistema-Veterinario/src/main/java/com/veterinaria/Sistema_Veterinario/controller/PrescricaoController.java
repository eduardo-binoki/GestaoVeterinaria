package com.veterinaria.Sistema_Veterinario.controller;

import com.veterinaria.Sistema_Veterinario.dto.PrescricaoDtos.PrescricaoRequest;
import com.veterinaria.Sistema_Veterinario.dto.PrescricaoDtos.PrescricaoResponse;
import com.veterinaria.Sistema_Veterinario.service.PrescricaoService;
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
@RequestMapping("/api/prescricoes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','VETERINARIO','RECEPCAO')")
public class PrescricaoController {

    private final PrescricaoService prescricaoService;

    @GetMapping
    public List<PrescricaoResponse> listar(@RequestParam(required = false) Long petId,
                                           @RequestParam(required = false) Long consultaId) {
        return prescricaoService.listar(petId, consultaId);
    }

    @GetMapping("/{id}")
    public PrescricaoResponse buscar(@PathVariable Long id) {
        return prescricaoService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','VETERINARIO')")
    public PrescricaoResponse criar(@Valid @RequestBody PrescricaoRequest request,
                                    Authentication authentication) {
        return prescricaoService.criar(request, authentication.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VETERINARIO')")
    public PrescricaoResponse atualizar(@PathVariable Long id,
                                        @Valid @RequestBody PrescricaoRequest request,
                                        Authentication authentication) {
        return prescricaoService.atualizar(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void excluir(@PathVariable Long id, Authentication authentication) {
        prescricaoService.excluir(id, authentication.getName());
    }
}
