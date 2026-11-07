package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.Consulta;
import com.veterinaria.Sistema_Veterinario.domain.Pet;
import com.veterinaria.Sistema_Veterinario.domain.Prescricao;
import com.veterinaria.Sistema_Veterinario.domain.PrescricaoItem;
import com.veterinaria.Sistema_Veterinario.domain.Veterinario;
import com.veterinaria.Sistema_Veterinario.dto.PrescricaoDtos.ItemRequest;
import com.veterinaria.Sistema_Veterinario.dto.PrescricaoDtos.PrescricaoRequest;
import com.veterinaria.Sistema_Veterinario.dto.PrescricaoDtos.PrescricaoResponse;
import com.veterinaria.Sistema_Veterinario.exception.BusinessException;
import com.veterinaria.Sistema_Veterinario.exception.ResourceNotFoundException;
import com.veterinaria.Sistema_Veterinario.repository.ConsultaRepository;
import com.veterinaria.Sistema_Veterinario.repository.PetRepository;
import com.veterinaria.Sistema_Veterinario.repository.PrescricaoRepository;
import com.veterinaria.Sistema_Veterinario.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescricaoService {

    private final PrescricaoRepository prescricaoRepository;
    private final PetRepository petRepository;
    private final ConsultaRepository consultaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final RegistroAtividadeService atividadeService;

    @Transactional(readOnly = true)
    public List<PrescricaoResponse> listar(Long petId, Long consultaId) {
        List<Prescricao> prescricoes;
        if (consultaId != null) {
            prescricoes = prescricaoRepository.findByConsultaIdOrderByDataPrescricaoDesc(consultaId);
        } else if (petId != null) {
            prescricoes = prescricaoRepository.findByPetIdOrderByDataPrescricaoDesc(petId);
        } else {
            prescricoes = prescricaoRepository.findAllByOrderByDataPrescricaoDesc();
        }
        return prescricoes.stream().map(PrescricaoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PrescricaoResponse buscar(Long id) {
        return PrescricaoResponse.from(buscarEntidade(id));
    }

    @Transactional
    public PrescricaoResponse criar(PrescricaoRequest request, String usuarioEmail) {
        Prescricao prescricao = new Prescricao();
        preencher(prescricao, request);
        Prescricao salva = prescricaoRepository.save(prescricao);
        atividadeService.registrar(usuarioEmail, "CRIAR", "Prescricao", salva.getId(),
                "Prescrição para " + salva.getPet().getNome());
        return PrescricaoResponse.from(salva);
    }

    @Transactional
    public PrescricaoResponse atualizar(Long id, PrescricaoRequest request, String usuarioEmail) {
        Prescricao prescricao = buscarEntidade(id);
        preencher(prescricao, request);
        Prescricao salva = prescricaoRepository.save(prescricao);
        atividadeService.registrar(usuarioEmail, "ATUALIZAR", "Prescricao", salva.getId(),
                "Prescrição para " + salva.getPet().getNome());
        return PrescricaoResponse.from(salva);
    }

    @Transactional
    public void excluir(Long id, String usuarioEmail) {
        Prescricao prescricao = buscarEntidade(id);
        prescricaoRepository.delete(prescricao);
        atividadeService.registrar(usuarioEmail, "EXCLUIR", "Prescricao", id,
                "Prescrição para " + prescricao.getPet().getNome());
    }

    private void preencher(Prescricao prescricao, PrescricaoRequest request) {
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
        if (request.itens() == null || request.itens().isEmpty()) {
            throw new BusinessException("Informe ao menos um item na prescrição");
        }

        prescricao.setPet(pet);
        prescricao.setConsulta(consulta);
        prescricao.setVeterinario(veterinario);
        prescricao.setDataPrescricao(request.dataPrescricao() != null
                ? request.dataPrescricao() : LocalDateTime.now());
        prescricao.setOrientacoes(request.orientacoes());
        prescricao.setAtivo(request.ativo() == null || request.ativo());

        prescricao.getItens().clear();
        for (ItemRequest itemRequest : request.itens()) {
            PrescricaoItem item = new PrescricaoItem();
            item.setPrescricao(prescricao);
            item.setMedicamento(itemRequest.medicamento().trim());
            item.setDosagem(itemRequest.dosagem().trim());
            item.setFrequencia(itemRequest.frequencia());
            item.setDuracao(itemRequest.duracao());
            item.setViaAdministracao(itemRequest.viaAdministracao());
            item.setObservacoes(itemRequest.observacoes());
            prescricao.getItens().add(item);
        }
    }

    private Prescricao buscarEntidade(Long id) {
        return prescricaoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Prescrição", id));
    }
}
