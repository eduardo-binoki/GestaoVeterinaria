package com.veterinaria.Sistema_Veterinario.dto;

import java.time.LocalDateTime;

public final class NotificacaoDtos {

    private NotificacaoDtos() {
    }

    public record NotificacaoResponse(
            Long id,
            String tipo,
            String acao,
            String entidade,
            Long entidadeId,
            String mensagem,
            String usuarioEmail,
            LocalDateTime dataHora,
            Boolean lida
    ) {
    }
}
