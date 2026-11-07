package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.Pet;
import com.veterinaria.Sistema_Veterinario.domain.SexoPet;
import com.veterinaria.Sistema_Veterinario.domain.Tutor;
import com.veterinaria.Sistema_Veterinario.dto.PetDtos.PetRequest;
import com.veterinaria.Sistema_Veterinario.dto.PetDtos.PetResponse;
import com.veterinaria.Sistema_Veterinario.exception.BusinessException;
import com.veterinaria.Sistema_Veterinario.exception.ResourceNotFoundException;
import com.veterinaria.Sistema_Veterinario.repository.ConsultaRepository;
import com.veterinaria.Sistema_Veterinario.repository.PetRepository;
import com.veterinaria.Sistema_Veterinario.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final TutorRepository tutorRepository;
    private final ConsultaRepository consultaRepository;
    private final RegistroAtividadeService atividadeService;

    @Transactional(readOnly = true)
    public List<PetResponse> listar(Long tutorId, String nome) {
        boolean temNome = nome != null && !nome.isBlank();
        List<Pet> pets;
        if (tutorId != null && temNome) {
            pets = petRepository.findByTutorIdAndAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(tutorId, nome.trim());
        } else if (tutorId != null) {
            pets = petRepository.findByTutorIdAndAtivoTrueOrderByNomeAsc(tutorId);
        } else if (temNome) {
            pets = petRepository.findByAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(nome.trim());
        } else {
            pets = petRepository.findByAtivoTrueOrderByNomeAsc();
        }
        return pets.stream().map(PetResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PetResponse buscar(Long id) {
        return PetResponse.from(buscarEntidade(id));
    }

    @Transactional
    public PetResponse criar(PetRequest request) {
        Pet pet = new Pet();
        preencher(pet, request);
        Pet salvo = petRepository.save(pet);
        atividadeService.registrarAtual("CRIAR", "Pet", salvo.getId(), salvo.getNome());
        return PetResponse.from(salvo);
    }

    @Transactional
    public PetResponse atualizar(Long id, PetRequest request) {
        Pet pet = buscarEntidade(id);
        preencher(pet, request);
        Pet salvo = petRepository.save(pet);
        atividadeService.registrarAtual("ATUALIZAR", "Pet", salvo.getId(), salvo.getNome());
        return PetResponse.from(salvo);
    }

    @Transactional
    public void desativar(Long id) {
        Pet pet = buscarEntidade(id);
        if (consultaRepository.existsByPetId(id)) {
            throw new BusinessException("Não é possível desativar um pet que possui consultas registradas");
        }
        pet.setAtivo(false);
        petRepository.save(pet);
        atividadeService.registrarAtual("DESATIVAR", "Pet", pet.getId(), pet.getNome());
    }

    private void preencher(Pet pet, PetRequest request) {
        Tutor tutor = tutorRepository.findById(request.tutorId())
                .orElseThrow(() -> ResourceNotFoundException.de("Tutor", request.tutorId()));
        if (!tutor.isAtivo()) {
            throw new BusinessException("Tutor informado está inativo");
        }
        pet.setNome(request.nome().trim());
        pet.setEspecie(request.especie().trim());
        pet.setRaca(request.raca());
        pet.setSexo(request.sexo() != null ? request.sexo() : SexoPet.INDEFINIDO);
        pet.setDataNascimento(request.dataNascimento());
        pet.setPeso(request.peso());
        pet.setTutor(tutor);
        pet.setObservacoes(request.observacoes());
        pet.setAlergias(request.alergias());
        pet.setDoencasPreexistentes(request.doencasPreexistentes());
        pet.setAtivo(true);
    }

    private Pet buscarEntidade(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Pet", id));
    }
}
