package com.veterinaria.Sistema_Veterinario.controller;

import com.veterinaria.Sistema_Veterinario.dto.TutorDtos.TutorRequest;
import com.veterinaria.Sistema_Veterinario.dto.TutorDtos.TutorResponse;
import com.veterinaria.Sistema_Veterinario.service.TutorService;
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
@RequestMapping("/api/tutores")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','VETERINARIO','RECEPCAO')")
public class TutorController {

    private final TutorService tutorService;

    @GetMapping
    public List<TutorResponse> listar(@RequestParam(required = false) String nome) {
        return tutorService.listar(nome);
    }

    @GetMapping("/{id}")
    public TutorResponse buscar(@PathVariable Long id) {
        return tutorService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TutorResponse criar(@Valid @RequestBody TutorRequest request) {
        return tutorService.criar(request);
    }

    @PutMapping("/{id}")
    public TutorResponse atualizar(@PathVariable Long id, @Valid @RequestBody TutorRequest request) {
        return tutorService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        tutorService.desativar(id);
    }
}
