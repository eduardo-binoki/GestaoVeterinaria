package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.RegistroAtividade;
import com.veterinaria.Sistema_Veterinario.repository.RegistroAtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistroAtividadeService {

    private final RegistroAtividadeRepository repository;

    @Transactional
    public void registrar(String usuarioEmail, String acao, String entidade, Long entidadeId, String detalhes) {
        RegistroAtividade registro = new RegistroAtividade();
        registro.setUsuarioEmail(usuarioEmail);
        registro.setAcao(acao);
        registro.setEntidade(entidade);
        registro.setEntidadeId(entidadeId);
        registro.setDetalhes(detalhes);
        registro.setLida(false);
        repository.save(registro);
    }

    @Transactional
    public void registrarAtual(String acao, String entidade, Long entidadeId, String detalhes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication != null && authentication.isAuthenticated()
                ? authentication.getName() : null;
        registrar(email, acao, entidade, entidadeId, detalhes);
    }
}
