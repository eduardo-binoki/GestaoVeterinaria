package com.veterinaria.Sistema_Veterinario.controller;

import com.veterinaria.Sistema_Veterinario.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','VETERINARIO','RECEPCAO')")
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/consultas")
    public ResponseEntity<byte[]> consultas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return csv("relatorio-consultas.csv", relatorioService.consultas(inicio, fim));
    }

    @GetMapping("/financeiro")
    public ResponseEntity<byte[]> financeiro(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return csv("relatorio-financeiro.csv", relatorioService.financeiro(inicio, fim));
    }

    @GetMapping("/estoque")
    public ResponseEntity<byte[]> estoque() {
        return csv("relatorio-estoque.csv", relatorioService.estoque());
    }

    @GetMapping("/vacinas")
    public ResponseEntity<byte[]> vacinas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return csv("relatorio-vacinas.csv", relatorioService.vacinas(inicio, fim));
    }

    private ResponseEntity<byte[]> csv(String nome, String conteudo) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nome)
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(conteudo.getBytes(StandardCharsets.UTF_8));
    }
}
