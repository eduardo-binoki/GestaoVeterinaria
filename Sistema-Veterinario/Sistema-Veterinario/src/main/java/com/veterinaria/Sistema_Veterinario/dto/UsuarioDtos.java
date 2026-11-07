package com.veterinaria.Sistema_Veterinario.dto;

import com.veterinaria.Sistema_Veterinario.domain.Role;
import com.veterinaria.Sistema_Veterinario.domain.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class UsuarioDtos {

    private UsuarioDtos() {
    }

    public record CriarUsuarioRequest(
            @NotBlank(message = "Nome é obrigatório") String nome,
            @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,
            @NotBlank(message = "Senha é obrigatória") @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres") String senha,
            @NotNull(message = "Perfil é obrigatório") Role role
    ) {
    }

    public record AtualizarUsuarioRequest(
            @NotBlank(message = "Nome é obrigatório") String nome,
            @NotNull(message = "Perfil é obrigatório") Role role,
            Boolean ativo,
            @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres") String senha
    ) {
    }

    public record AlterarSenhaRequest(
            @NotBlank(message = "Senha atual é obrigatória") String senhaAtual,
            @NotBlank(message = "Nova senha é obrigatória") @Size(min = 6, message = "Nova senha deve ter pelo menos 6 caracteres") String novaSenha
    ) {
    }

    public record UsuarioResponse(
            Long id,
            String nome,
            String email,
            Role role,
            boolean ativo,
            LocalDateTime ultimoAcesso,
            LocalDateTime criadoEm
    ) {
        public static UsuarioResponse from(Usuario usuario) {
            return new UsuarioResponse(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getRole(),
                    usuario.isAtivo(),
                    usuario.getUltimoAcesso(),
                    usuario.getCriadoEm()
            );
        }
    }

    public record LoginRequest(
            @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,
            @NotBlank(message = "Senha é obrigatória") String senha
    ) {
    }

    public record LoginResponse(
            String token,
            String tipo,
            long expiraEmMs,
            UsuarioResponse usuario
    ) {
    }
}
