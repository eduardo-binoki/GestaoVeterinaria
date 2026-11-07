package com.veterinaria.Sistema_Veterinario.controller;

import com.veterinaria.Sistema_Veterinario.dto.DashboardDtos.AtividadeResponse;
import com.veterinaria.Sistema_Veterinario.dto.DashboardDtos.DashboardResponse;
import com.veterinaria.Sistema_Veterinario.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','VETERINARIO','RECEPCAO')")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse carregar() {
        return dashboardService.carregar();
    }

    @GetMapping("/atividades")
    public List<AtividadeResponse> atividades() {
        return dashboardService.atividadesRecentes();
    }
}
