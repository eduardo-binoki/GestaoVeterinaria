package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.Consulta;
import com.veterinaria.Sistema_Veterinario.domain.Exame;
import com.veterinaria.Sistema_Veterinario.domain.Pet;
import com.veterinaria.Sistema_Veterinario.domain.StatusExame;
import com.veterinaria.Sistema_Veterinario.domain.Veterinario;
import com.veterinaria.Sistema_Veterinario.dto.ExameDtos.ExameRequest;
import com.veterinaria.Sistema_Veterinario.dto.ExameDtos.ExameResponse;
import com.veterinaria.Sistema_Veterinario.exception.BusinessException;
import com.veterinaria.Sistema_Veterinario.exception.ResourceNotFoundException;
import com.veterinaria.Sistema_Veterinario.repository.ConsultaRepository;
import com.veterinaria.Sistema_Veterinario.repository.ExameRepository;
import com.veterinaria.Sistema_Veterinario.repository.PetRepository;
import com.veterinaria.Sistema_Veterinario.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExameService {

    private final ExameRepository exameRepository;
    private final PetRepository petRepository;
    private final ConsultaRepository consultaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final RegistroAtividadeService atividadeService;

    @Transactional(readOnly = true)
    public List<ExameResponse> listar(Long petId, StatusExame status) {
        List<Exame> exames;
        if (petId != null) {
            exames = exameRepository.findByPetIdOrderByDataSolicitacaoDesc(petId);
        } else if (status != null) {
            exames = exameRepository.findByStatusOrderByDataSolicitacaoDesc(status);
        } else {
            exames = exameRepository.findAllByOrderByDataSolicitacaoDesc();
        }
        if (petId != null && status != null) {
            exames = exames.stream().filter(e -> e.getStatus() == status).toList();
        }
        return exames.stream().map(ExameResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ExameResponse buscar(Long id) {
        return ExameResponse.from(buscarEntidade(id));
    }

    @Transactional
    public ExameResponse criar(ExameRequest request, String usuarioEmail) {
        Exame exame = new Exame();
        preencher(exame, request);
        Exame salvo = exameRepository.save(exame);
        atividadeService.registrar(usuarioEmail, "CRIAR", "Exame", salvo.getId(), salvo.getTipo());
        return ExameResponse.from(salvo);
    }

    @Transactional
    public ExameResponse atualizar(Long id, ExameRequest request, String usuarioEmail) {
        Exame exame = buscarEntidade(id);
        preencher(exame, request);
        Exame salvo = exameRepository.save(exame);
        atividadeService.registrar(usuarioEmail, "ATUALIZAR", "Exame", salvo.getId(), salvo.getTipo());
        return ExameResponse.from(salvo);
    }

    @Transactional
    public void excluir(Long id, String usuarioEmail) {
        Exame exame = buscarEntidade(id);
        exameRepository.delete(exame);
        atividadeService.registrar(usuarioEmail, "EXCLUIR", "Exame", id, exame.getTipo());
    }

    private void preencher(Exame exame, ExameRequest request) {
        Pet pet = petRepository.findById(request.petId())
                .orElseThrow(() -> ResourceNotFoundException.de("Pet", request.petId()));
        if (!pet.isAtivo()) {
            throw new BusinessException("Pet informado está inativo");
        }
        Consulta consulta = null;
        if (request.consultaId() != null) {
            consulta = consultaRepository.findById(request.consultaId())
                    .orElseThrow(() -> ResourceNotFoundException.de("Consulta", request.consultaId()));
            if (!consulta.getPet().getId().equals(pet.getId())) {
                throw new BusinessException("Consulta informada pertence a outro pet");
            }
        }
        Veterinario veterinario = null;
        if (request.veterinarioId() != null) {
            veterinario = veterinarioRepository.findById(request.veterinarioId())
                    .orElseThrow(() -> ResourceNotFoundException.de("Veterinário", request.veterinarioId()));
        }
        StatusExame status = request.status() != null ? request.status() : StatusExame.SOLICITADO;
        if (status == StatusExame.CONCLUIDO && (request.resultado() == null || request.resultado().isBlank())) {
            throw new BusinessException("Resultado é obrigatório para concluir o exame");
        }

        exame.setPet(pet);
        exame.setConsulta(consulta);
        exame.setVeterinario(veterinario);
        exame.setTipo(request.tipo().trim());
        exame.setDataSolicitacao(request.dataSolicitacao() != null ? request.dataSolicitacao() : LocalDate.now());
        exame.setDataColeta(request.dataColeta());
        exame.setDataResultado(status == StatusExame.CONCLUIDO && request.dataResultado() == null
                ? LocalDate.now() : request.dataResultado());
        exame.setStatus(status);
        exame.setResultado(request.resultado());
        exame.setObservacoes(request.observacoes());
        exame.setAnexoNome(request.anexoNome());
        exame.setAnexoUrl(request.anexoUrl());
    }

    private Exame buscarEntidade(Long id) {
        return exameRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Exame", id));
    }
}
