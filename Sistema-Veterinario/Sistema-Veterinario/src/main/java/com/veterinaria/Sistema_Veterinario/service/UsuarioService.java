package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.Usuario;
import com.veterinaria.Sistema_Veterinario.dto.UsuarioDtos.AlterarSenhaRequest;
import com.veterinaria.Sistema_Veterinario.dto.UsuarioDtos.AtualizarUsuarioRequest;
import com.veterinaria.Sistema_Veterinario.dto.UsuarioDtos.CriarUsuarioRequest;
import com.veterinaria.Sistema_Veterinario.dto.UsuarioDtos.UsuarioResponse;
import com.veterinaria.Sistema_Veterinario.exception.BusinessException;
import com.veterinaria.Sistema_Veterinario.exception.ResourceNotFoundException;
import com.veterinaria.Sistema_Veterinario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegistroAtividadeService atividadeService;

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream().map(UsuarioResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscar(Long id) {
        return UsuarioResponse.from(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return UsuarioResponse.from(usuario);
    }

    @Transactional
    public UsuarioResponse criar(CriarUsuarioRequest request) {
        String email = request.email().trim().toLowerCase();
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new BusinessException("Já existe um usuário com este e-mail");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setRole(request.role());
        usuario.setAtivo(true);
        Usuario salvo = usuarioRepository.save(usuario);
        atividadeService.registrarAtual("CRIAR", "Usuario", salvo.getId(), salvo.getNome());
        return UsuarioResponse.from(salvo);
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, AtualizarUsuarioRequest request) {
        Usuario usuario = buscarEntidade(id);
        usuario.setNome(request.nome().trim());
        usuario.setRole(request.role());
        if (request.ativo() != null) {
            usuario.setAtivo(request.ativo());
        }
        if (request.senha() != null && !request.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.senha()));
        }
        Usuario salvo = usuarioRepository.save(usuario);
        atividadeService.registrarAtual("ATUALIZAR", "Usuario", salvo.getId(), salvo.getNome());
        return UsuarioResponse.from(salvo);
    }

    @Transactional
    public void alterarSenha(String email, AlterarSenhaRequest request) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        if (!passwordEncoder.matches(request.senhaAtual(), usuario.getSenha())) {
            throw new BusinessException("Senha atual incorreta");
        }
        if (passwordEncoder.matches(request.novaSenha(), usuario.getSenha())) {
            throw new BusinessException("A nova senha deve ser diferente da senha atual");
        }
        usuario.setSenha(passwordEncoder.encode(request.novaSenha()));
        usuarioRepository.save(usuario);
        atividadeService.registrarAtual("ALTERAR_SENHA", "Usuario", usuario.getId(), usuario.getNome());
    }

    @Transactional
    public void desativar(Long id) {
        Usuario usuario = buscarEntidade(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
        atividadeService.registrarAtual("DESATIVAR", "Usuario", usuario.getId(), usuario.getNome());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
        return User.withUsername(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(usuario.getRole().name())
                .disabled(!usuario.isAtivo())
                .build();
    }

    private Usuario buscarEntidade(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Usuário", id));
    }
}
