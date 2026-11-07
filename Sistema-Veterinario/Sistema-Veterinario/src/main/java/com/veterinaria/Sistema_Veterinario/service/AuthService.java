package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.config.JwtService;
import com.veterinaria.Sistema_Veterinario.domain.Usuario;
import com.veterinaria.Sistema_Veterinario.dto.UsuarioDtos.LoginRequest;
import com.veterinaria.Sistema_Veterinario.dto.UsuarioDtos.LoginResponse;
import com.veterinaria.Sistema_Veterinario.dto.UsuarioDtos.UsuarioResponse;
import com.veterinaria.Sistema_Veterinario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.senha()));
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("E-mail ou senha inválidos");
        }

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new BadCredentialsException("E-mail ou senha inválidos"));

        usuario.setUltimoAcesso(java.time.LocalDateTime.now());
        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario);
        return new LoginResponse(token, "Bearer", jwtService.getExpirationMs(), UsuarioResponse.from(usuario));
    }
}
