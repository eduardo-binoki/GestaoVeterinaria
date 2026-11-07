package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.Tutor;
import com.veterinaria.Sistema_Veterinario.dto.TutorDtos.TutorRequest;
import com.veterinaria.Sistema_Veterinario.dto.TutorDtos.TutorResponse;
import com.veterinaria.Sistema_Veterinario.exception.BusinessException;
import com.veterinaria.Sistema_Veterinario.exception.ResourceNotFoundException;
import com.veterinaria.Sistema_Veterinario.repository.PetRepository;
import com.veterinaria.Sistema_Veterinario.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TutorService {

    private final TutorRepository tutorRepository;
    private final PetRepository petRepository;
    private final RegistroAtividadeService atividadeService;

    @Transactional(readOnly = true)
    public List<TutorResponse> listar(String nome) {
        List<Tutor> tutores = (nome == null || nome.isBlank())
                ? tutorRepository.findByAtivoTrueOrderByNomeAsc()
                : tutorRepository.findByAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(nome.trim());
        return tutores.stream().map(TutorResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public TutorResponse buscar(Long id) {
        return TutorResponse.from(buscarEntidade(id));
    }

    @Transactional
    public TutorResponse criar(TutorRequest request) {
        String cpf = normalizarCpf(request.cpf());
        if (tutorRepository.existsByCpf(cpf)) {
            throw new BusinessException("Já existe um tutor cadastrado com este CPF");
        }
        Tutor tutor = new Tutor();
        preencher(tutor, request, cpf);
        Tutor salvo = tutorRepository.save(tutor);
        atividadeService.registrarAtual("CRIAR", "Tutor", salvo.getId(), salvo.getNome());
        return TutorResponse.from(salvo);
    }

    @Transactional
    public TutorResponse atualizar(Long id, TutorRequest request) {
        Tutor tutor = buscarEntidade(id);
        String cpf = normalizarCpf(request.cpf());
        if (tutorRepository.existsByCpfAndIdNot(cpf, id)) {
            throw new BusinessException("Já existe um tutor cadastrado com este CPF");
        }
        preencher(tutor, request, cpf);
        Tutor salvo = tutorRepository.save(tutor);
        atividadeService.registrarAtual("ATUALIZAR", "Tutor", salvo.getId(), salvo.getNome());
        return TutorResponse.from(salvo);
    }

    @Transactional
    public void desativar(Long id) {
        Tutor tutor = buscarEntidade(id);
        if (petRepository.existsByTutorIdAndAtivoTrue(id)) {
            throw new BusinessException("Não é possível desativar um tutor que possui pets ativos");
        }
        tutor.setAtivo(false);
        tutorRepository.save(tutor);
        atividadeService.registrarAtual("DESATIVAR", "Tutor", tutor.getId(), tutor.getNome());
    }

    private void preencher(Tutor tutor, TutorRequest request, String cpf) {
        tutor.setNome(request.nome().trim());
        tutor.setCpf(cpf);
        tutor.setEmail(request.email().trim().toLowerCase());
        tutor.setTelefone(request.telefone().trim());
        tutor.setEndereco(request.endereco());
        tutor.setCidade(request.cidade());
        tutor.setUf(request.uf() != null ? request.uf().trim().toUpperCase() : null);
        tutor.setAtivo(true);
    }

    private Tutor buscarEntidade(Long id) {
        return tutorRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Tutor", id));
    }

    private String normalizarCpf(String cpf) {
        String digitos = cpf.replaceAll("\\D", "");
        if (digitos.length() != 11) {
            throw new BusinessException("CPF deve conter 11 dígitos");
        }
        return digitos;
    }
}
