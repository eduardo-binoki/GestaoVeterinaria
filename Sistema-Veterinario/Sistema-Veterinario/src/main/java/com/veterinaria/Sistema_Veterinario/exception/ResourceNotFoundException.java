package com.veterinaria.Sistema_Veterinario.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }

    public static ResourceNotFoundException de(String recurso, Long id) {
        return new ResourceNotFoundException(recurso + " não encontrado(a) para o id " + id);
    }
}
