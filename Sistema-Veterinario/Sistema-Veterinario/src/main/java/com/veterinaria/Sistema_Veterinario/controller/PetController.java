package com.veterinaria.Sistema_Veterinario.controller;

import com.veterinaria.Sistema_Veterinario.dto.PetDtos.PetRequest;
import com.veterinaria.Sistema_Veterinario.dto.PetDtos.PetResponse;
import com.veterinaria.Sistema_Veterinario.service.PetService;
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
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','VETERINARIO','RECEPCAO')")
public class PetController {

    private final PetService petService;

    @GetMapping
    public List<PetResponse> listar(@RequestParam(required = false) Long tutorId,
                                    @RequestParam(required = false) String nome) {
        return petService.listar(tutorId, nome);
    }

    @GetMapping("/{id}")
    public PetResponse buscar(@PathVariable Long id) {
        return petService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetResponse criar(@Valid @RequestBody PetRequest request) {
        return petService.criar(request);
    }

    @PutMapping("/{id}")
    public PetResponse atualizar(@PathVariable Long id, @Valid @RequestBody PetRequest request) {
        return petService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        petService.desativar(id);
    }
}
