package com.veterinaria.Sistema_Veterinario.controller;

import com.veterinaria.Sistema_Veterinario.dto.VacinaDtos.VacinaRequest;
import com.veterinaria.Sistema_Veterinario.dto.VacinaDtos.VacinaResponse;
import com.veterinaria.Sistema_Veterinario.service.VacinaService;
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
@RequestMapping("/api/vacinas")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','VETERINARIO','RECEPCAO')")
public class VacinaController {

    private final VacinaService vacinaService;

    @GetMapping
    public List<VacinaResponse> listar(@RequestParam(required = false) Long petId) {
        return vacinaService.listar(petId);
    }

    @GetMapping("/proximas-doses")
    public List<VacinaResponse> proximasDoses(@RequestParam(defaultValue = "30") int dias) {
        return vacinaService.listarProximasDoses(dias);
    }

    @GetMapping("/{id}")
    public VacinaResponse buscar(@PathVariable Long id) {
        return vacinaService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VacinaResponse criar(@Valid @RequestBody VacinaRequest request) {
        return vacinaService.criar(request);
    }

    @PutMapping("/{id}")
    public VacinaResponse atualizar(@PathVariable Long id, @Valid @RequestBody VacinaRequest request) {
        return vacinaService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','VETERINARIO')")
    public void excluir(@PathVariable Long id) {
        vacinaService.excluir(id);
    }
}
