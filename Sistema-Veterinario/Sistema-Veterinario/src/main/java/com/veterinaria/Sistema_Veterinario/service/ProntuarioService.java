package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.Consulta;
import com.veterinaria.Sistema_Veterinario.domain.Prontuario;
import com.veterinaria.Sistema_Veterinario.domain.RegistroPeso;
import com.veterinaria.Sistema_Veterinario.domain.StatusConsulta;
import com.veterinaria.Sistema_Veterinario.dto.ProntuarioDtos.ProntuarioRequest;
import com.veterinaria.Sistema_Veterinario.dto.ProntuarioDtos.ProntuarioResponse;
import com.veterinaria.Sistema_Veterinario.exception.BusinessException;
import com.veterinaria.Sistema_Veterinario.exception.ResourceNotFoundException;
import com.veterinaria.Sistema_Veterinario.repository.ConsultaRepository;
import com.veterinaria.Sistema_Veterinario.repository.PetRepository;
import com.veterinaria.Sistema_Veterinario.repository.ProntuarioRepository;
import com.veterinaria.Sistema_Veterinario.repository.RegistroPesoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final ConsultaRepository consultaRepository;
    private final PetRepository petRepository;
    private final RegistroPesoRepository registroPesoRepository;
    private final RegistroAtividadeService atividadeService;

    @Transactional(readOnly = true)
    public List<ProntuarioResponse> listar(Long petId) {
        if (petId != null) {
            if (!petRepository.existsById(petId)) {
                throw ResourceNotFoundException.de("Pet", petId);
            }
            return prontuarioRepository.findByConsultaPetIdOrderByDataRegistroDesc(petId).stream()
                    .map(ProntuarioResponse::from)
                    .toList();
        }
        return prontuarioRepository.findAllByOrderByDataRegistroDesc().stream()
                .map(ProntuarioResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProntuarioResponse buscar(Long id) {
        return ProntuarioResponse.from(buscarEntidade(id));
    }

    @Transactional
    public ProntuarioResponse criar(ProntuarioRequest request) {
        Consulta consulta = consultaRepository.findById(request.consultaId())
                .orElseThrow(() -> ResourceNotFoundException.de("Consulta", request.consultaId()));
        if (prontuarioRepository.existsByConsultaId(consulta.getId())) {
            throw new BusinessException("Já existe prontuário para esta consulta");
        }
        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new BusinessException("Não é possível registrar prontuário de consulta cancelada");
        }

        Prontuario prontuario = new Prontuario();
        prontuario.setConsulta(consulta);
        preencher(prontuario, request);
        prontuario.setDataRegistro(LocalDateTime.now());

        consulta.setStatus(StatusConsulta.REALIZADA);
        consultaRepository.save(consulta);

        Prontuario salvo = prontuarioRepository.save(prontuario);
        registrarPeso(consulta, request.peso());
        atividadeService.registrarAtual("CRIAR", "Prontuario", salvo.getId(),
                "Pet " + salvo.getConsulta().getPet().getNome());
        return ProntuarioResponse.from(salvo);
    }

    @Transactional
    public ProntuarioResponse atualizar(Long id, ProntuarioRequest request) {
        Prontuario prontuario = buscarEntidade(id);
        if (prontuario.isFinalizado()) {
            throw new BusinessException("Prontuário finalizado não pode ser alterado");
        }
        preencher(prontuario, request);
        Prontuario salvo = prontuarioRepository.save(prontuario);
        registrarPeso(prontuario.getConsulta(), request.peso());
        atividadeService.registrarAtual("ATUALIZAR", "Prontuario", salvo.getId(),
                "Pet " + salvo.getConsulta().getPet().getNome());
        return ProntuarioResponse.from(salvo);
    }

    @Transactional
    public void excluir(Long id) {
        Prontuario prontuario = buscarEntidade(id);
        String petNome = prontuario.getConsulta().getPet().getNome();
        prontuarioRepository.delete(prontuario);
        atividadeService.registrarAtual("EXCLUIR", "Prontuario", id, "Pet " + petNome);
    }

    private void preencher(Prontuario prontuario, ProntuarioRequest request) {
        prontuario.setAnamnese(request.anamnese().trim());
        prontuario.setExameFisico(request.exameFisico());
        prontuario.setDiagnostico(request.diagnostico());
        prontuario.setPrescricao(request.prescricao());
        prontuario.setObservacoes(request.observacoes());
        prontuario.setEvolucao(request.evolucao());
        prontuario.setProcedimentos(request.procedimentos());
        prontuario.setSinaisVitais(request.sinaisVitais());
        prontuario.setPeso(request.peso());
        prontuario.setFinalizado(Boolean.TRUE.equals(request.finalizado()));
    }

    private void registrarPeso(Consulta consulta, java.math.BigDecimal peso) {
        if (peso == null || Objects.equals(consulta.getPet().getPeso(), peso)) {
            return;
        }
        consulta.getPet().setPeso(peso);
        petRepository.save(consulta.getPet());

        RegistroPeso registro = new RegistroPeso();
        registro.setPet(consulta.getPet());
        registro.setPeso(peso);
        registro.setObservacoes("Atualizado pelo prontuário da consulta #" + consulta.getId());
        registroPesoRepository.save(registro);
    }

    private Prontuario buscarEntidade(Long id) {
        return prontuarioRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Prontuário", id));
    }
}
