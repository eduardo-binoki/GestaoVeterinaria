package com.veterinaria.Sistema_Veterinario.controller;

import com.veterinaria.Sistema_Veterinario.dto.UsuarioDtos.AlterarSenhaRequest;
import com.veterinaria.Sistema_Veterinario.dto.UsuarioDtos.LoginRequest;
import com.veterinaria.Sistema_Veterinario.dto.UsuarioDtos.LoginResponse;
import com.veterinaria.Sistema_Veterinario.dto.UsuarioDtos.UsuarioResponse;
import com.veterinaria.Sistema_Veterinario.service.AuthService;
import com.veterinaria.Sistema_Veterinario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UsuarioResponse me(Authentication authentication) {
        return usuarioService.buscarPorEmail(authentication.getName());
    }

    @PutMapping("/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@Valid @RequestBody AlterarSenhaRequest request,
                             Authentication authentication) {
        usuarioService.alterarSenha(authentication.getName(), request);
    }
}
