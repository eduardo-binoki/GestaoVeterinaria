package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.Pet;
import com.veterinaria.Sistema_Veterinario.domain.Vacina;
import com.veterinaria.Sistema_Veterinario.domain.Veterinario;
import com.veterinaria.Sistema_Veterinario.dto.VacinaDtos.VacinaRequest;
import com.veterinaria.Sistema_Veterinario.dto.VacinaDtos.VacinaResponse;
import com.veterinaria.Sistema_Veterinario.exception.BusinessException;
import com.veterinaria.Sistema_Veterinario.exception.ResourceNotFoundException;
import com.veterinaria.Sistema_Veterinario.repository.PetRepository;
import com.veterinaria.Sistema_Veterinario.repository.VacinaRepository;
import com.veterinaria.Sistema_Veterinario.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacinaService {

    private final VacinaRepository vacinaRepository;
    private final PetRepository petRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final RegistroAtividadeService atividadeService;

    @Transactional(readOnly = true)
    public List<VacinaResponse> listar(Long petId) {
        if (petId != null) {
            if (!petRepository.existsById(petId)) {
                throw ResourceNotFoundException.de("Pet", petId);
            }
            return vacinaRepository.findByPetIdOrderByDataAplicacaoDesc(petId).stream()
                    .map(VacinaResponse::from)
                    .toList();
        }
        return vacinaRepository.findAllByOrderByDataAplicacaoDesc().stream()
                .map(VacinaResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VacinaResponse> listarProximasDoses(int dias) {
        LocalDate hoje = LocalDate.now();
        return vacinaRepository.findByProximaDoseBetweenOrderByProximaDoseAsc(hoje, hoje.plusDays(dias)).stream()
                .map(VacinaResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public VacinaResponse buscar(Long id) {
        return VacinaResponse.from(buscarEntidade(id));
    }

    @Transactional
    public VacinaResponse criar(VacinaRequest request) {
        Vacina vacina = new Vacina();
        preencher(vacina, request);
        Vacina salva = vacinaRepository.save(vacina);
        atividadeService.registrarAtual("CRIAR", "Vacina", salva.getId(),
                salva.getNome() + " - " + salva.getPet().getNome());
        return VacinaResponse.from(salva);
    }

    @Transactional
    public VacinaResponse atualizar(Long id, VacinaRequest request) {
        Vacina vacina = buscarEntidade(id);
        preencher(vacina, request);
        Vacina salva = vacinaRepository.save(vacina);
        atividadeService.registrarAtual("ATUALIZAR", "Vacina", salva.getId(),
                salva.getNome() + " - " + salva.getPet().getNome());
        return VacinaResponse.from(salva);
    }

    @Transactional
    public void excluir(Long id) {
        Vacina vacina = buscarEntidade(id);
        String descricao = vacina.getNome() + " - " + vacina.getPet().getNome();
        vacinaRepository.delete(vacina);
        atividadeService.registrarAtual("EXCLUIR", "Vacina", id, descricao);
    }

    private void preencher(Vacina vacina, VacinaRequest request) {
        Pet pet = petRepository.findById(request.petId())
                .orElseThrow(() -> ResourceNotFoundException.de("Pet", request.petId()));
        if (!pet.isAtivo()) {
            throw new BusinessException("Pet informado está inativo");
        }
        vacina.setPet(pet);

        if (request.veterinarioId() != null) {
            Veterinario veterinario = veterinarioRepository.findById(request.veterinarioId())
                    .orElseThrow(() -> ResourceNotFoundException.de("Veterinário", request.veterinarioId()));
            vacina.setVeterinario(veterinario);
        } else {
            vacina.setVeterinario(null);
        }

        vacina.setNome(request.nome().trim());
        vacina.setFabricante(request.fabricante());
        vacina.setLote(request.lote());
        vacina.setDataAplicacao(request.dataAplicacao());
        if (request.status() != null) {
            vacina.setStatus(request.status());
        } else if (vacina.getStatus() == null) {
            vacina.setStatus(com.veterinaria.Sistema_Veterinario.domain.StatusVacina.APLICADA);
        }
        LocalDate proximaDose = request.proximaDose();
        if (proximaDose == null && vacina.getId() == null
                && vacina.getStatus() == com.veterinaria.Sistema_Veterinario.domain.StatusVacina.APLICADA) {
            proximaDose = request.dataAplicacao().plusYears(1);
        }
        if (proximaDose == null && vacina.getId() != null) {
            proximaDose = vacina.getProximaDose();
        }
        if (proximaDose != null && proximaDose.isBefore(request.dataAplicacao())) {
            throw new BusinessException("Próxima dose não pode ser anterior à data de aplicação");
        }
        vacina.setProximaDose(proximaDose);
        vacina.setDose(request.dose());
        vacina.setReforco(Boolean.TRUE.equals(request.reforco()));
        vacina.setObservacoes(request.observacoes());
    }

    private Vacina buscarEntidade(Long id) {
        return vacinaRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Vacina", id));
    }
}
