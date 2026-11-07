package com.veterinaria.Sistema_Veterinario.controller;

import com.veterinaria.Sistema_Veterinario.dto.VeterinarioDtos.AusenciaRequest;
import com.veterinaria.Sistema_Veterinario.dto.VeterinarioDtos.AusenciaResponse;
import com.veterinaria.Sistema_Veterinario.dto.VeterinarioDtos.DisponibilidadeRequest;
import com.veterinaria.Sistema_Veterinario.dto.VeterinarioDtos.DisponibilidadeResponse;
import com.veterinaria.Sistema_Veterinario.dto.VeterinarioDtos.VeterinarioRequest;
import com.veterinaria.Sistema_Veterinario.dto.VeterinarioDtos.VeterinarioResponse;
import com.veterinaria.Sistema_Veterinario.service.VeterinarioService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/veterinarios")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','VETERINARIO','RECEPCAO')")
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    @GetMapping
    public List<VeterinarioResponse> listar() {
        return veterinarioService.listar();
    }

    @GetMapping("/{id}")
    public VeterinarioResponse buscar(@PathVariable Long id) {
        return veterinarioService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public VeterinarioResponse criar(@Valid @RequestBody VeterinarioRequest request) {
        return veterinarioService.criar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VeterinarioResponse atualizar(@PathVariable Long id, @Valid @RequestBody VeterinarioRequest request) {
        return veterinarioService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void desativar(@PathVariable Long id) {
        veterinarioService.desativar(id);
    }

    @GetMapping("/{id}/disponibilidades")
    public List<DisponibilidadeResponse> listarDisponibilidades(@PathVariable Long id) {
        return veterinarioService.listarDisponibilidades(id);
    }

    @PostMapping("/{id}/disponibilidades")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCAO')")
    public DisponibilidadeResponse criarDisponibilidade(@PathVariable Long id,
                                                        @Valid @RequestBody DisponibilidadeRequest request) {
        return veterinarioService.criarDisponibilidade(id, request);
    }

    @PutMapping("/disponibilidades/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCAO')")
    public DisponibilidadeResponse atualizarDisponibilidade(@PathVariable Long id,
                                                            @Valid @RequestBody DisponibilidadeRequest request) {
        return veterinarioService.atualizarDisponibilidade(id, request);
    }

    @DeleteMapping("/disponibilidades/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCAO')")
    public void excluirDisponibilidade(@PathVariable Long id) {
        veterinarioService.excluirDisponibilidade(id);
    }

    @GetMapping("/{id}/ausencias")
    public List<AusenciaResponse> listarAusencias(@PathVariable Long id) {
        return veterinarioService.listarAusencias(id);
    }

    @PostMapping("/{id}/ausencias")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCAO')")
    public AusenciaResponse criarAusencia(@PathVariable Long id,
                                          @Valid @RequestBody AusenciaRequest request) {
        return veterinarioService.criarAusencia(id, request);
    }

    @DeleteMapping("/ausencias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCAO')")
    public void excluirAusencia(@PathVariable Long id) {
        veterinarioService.excluirAusencia(id);
    }
}
