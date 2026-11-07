package com.veterinaria.Sistema_Veterinario.controller;

import com.veterinaria.Sistema_Veterinario.domain.StatusConsulta;
import com.veterinaria.Sistema_Veterinario.dto.ConsultaDtos.AlterarStatusConsultaRequest;
import com.veterinaria.Sistema_Veterinario.dto.ConsultaDtos.ConsultaRequest;
import com.veterinaria.Sistema_Veterinario.dto.ConsultaDtos.ConsultaResponse;
import com.veterinaria.Sistema_Veterinario.dto.ConsultaDtos.HistoricoStatusResponse;
import com.veterinaria.Sistema_Veterinario.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','VETERINARIO','RECEPCAO')")
public class ConsultaController {

    private final ConsultaService consultaService;

    @GetMapping
    public List<ConsultaResponse> listar(@RequestParam(required = false) Long petId,
                                         @RequestParam(required = false) StatusConsulta status) {
        return consultaService.listar(petId, status);
    }

    @GetMapping("/agenda")
    public List<ConsultaResponse> agenda(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return consultaService.listarAgenda(inicio, fim);
    }

    @GetMapping("/{id}")
    public ConsultaResponse buscar(@PathVariable Long id) {
        return consultaService.buscar(id);
    }

    @GetMapping("/{id}/historico")
    public List<HistoricoStatusResponse> historico(@PathVariable Long id) {
        return consultaService.historico(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultaResponse criar(@Valid @RequestBody ConsultaRequest request) {
        return consultaService.criar(request);
    }

    @PutMapping("/{id}")
    public ConsultaResponse atualizar(@PathVariable Long id,
                                      @Valid @RequestBody ConsultaRequest request) {
        return consultaService.atualizar(id, request);
    }

    @PatchMapping("/{id}/cancelar")
    public ConsultaResponse cancelar(@PathVariable Long id) {
        return consultaService.cancelar(id);
    }

    @PatchMapping("/{id}/status")
    public ConsultaResponse alterarStatus(@PathVariable Long id,
                                          @Valid @RequestBody AlterarStatusConsultaRequest request,
                                          Authentication authentication) {
        return consultaService.alterarStatus(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void excluir(@PathVariable Long id) {
        consultaService.excluir(id);
    }
}
