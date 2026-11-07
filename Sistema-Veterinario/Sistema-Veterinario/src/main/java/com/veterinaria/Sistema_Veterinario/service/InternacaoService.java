package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.EvolucaoInternacao;
import com.veterinaria.Sistema_Veterinario.domain.Internacao;
import com.veterinaria.Sistema_Veterinario.domain.Pet;
import com.veterinaria.Sistema_Veterinario.domain.StatusInternacao;
import com.veterinaria.Sistema_Veterinario.domain.Veterinario;
import com.veterinaria.Sistema_Veterinario.dto.InternacaoDtos.AltaRequest;
import com.veterinaria.Sistema_Veterinario.dto.InternacaoDtos.EvolucaoRequest;
import com.veterinaria.Sistema_Veterinario.dto.InternacaoDtos.EvolucaoResponse;
import com.veterinaria.Sistema_Veterinario.dto.InternacaoDtos.InternacaoRequest;
import com.veterinaria.Sistema_Veterinario.dto.InternacaoDtos.InternacaoResponse;
import com.veterinaria.Sistema_Veterinario.exception.BusinessException;
import com.veterinaria.Sistema_Veterinario.exception.ResourceNotFoundException;
import com.veterinaria.Sistema_Veterinario.repository.EvolucaoInternacaoRepository;
import com.veterinaria.Sistema_Veterinario.repository.InternacaoRepository;
import com.veterinaria.Sistema_Veterinario.repository.PetRepository;
import com.veterinaria.Sistema_Veterinario.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternacaoService {

    private final InternacaoRepository internacaoRepository;
    private final EvolucaoInternacaoRepository evolucaoRepository;
    private final PetRepository petRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final RegistroAtividadeService atividadeService;

    @Transactional(readOnly = true)
    public List<InternacaoResponse> listar(StatusInternacao status, Long petId) {
        List<Internacao> internacoes;
        if (petId != null) {
            internacoes = internacaoRepository.findByPetIdOrderByDataEntradaDesc(petId);
        } else if (status != null) {
            internacoes = internacaoRepository.findByStatusOrderByDataEntradaDesc(status);
        } else {
            internacoes = internacaoRepository.findAllByOrderByDataEntradaDesc();
        }
        if (petId != null && status != null) {
            internacoes = internacoes.stream().filter(i -> i.getStatus() == status).toList();
        }
        return internacoes.stream().map(InternacaoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public InternacaoResponse buscar(Long id) {
        return InternacaoResponse.from(buscarEntidade(id));
    }

    @Transactional
    public InternacaoResponse criar(InternacaoRequest request, String usuarioEmail) {
        Internacao internacao = new Internacao();
        preencher(internacao, request);
        if (internacaoRepository.existsByPetIdAndStatus(internacao.getPet().getId(), StatusInternacao.ATIVA)) {
            throw new BusinessException("Pet já possui uma internação ativa");
        }
        Internacao salva = internacaoRepository.save(internacao);
        atividadeService.registrar(usuarioEmail, "CRIAR", "Internacao", salva.getId(),
                "Internação de " + salva.getPet().getNome());
        return InternacaoResponse.from(salva);
    }

    @Transactional
    public InternacaoResponse atualizar(Long id, InternacaoRequest request, String usuarioEmail) {
        Internacao internacao = buscarEntidade(id);
        if (internacao.getStatus() != StatusInternacao.ATIVA) {
            throw new BusinessException("Apenas internações ativas podem ser alteradas");
        }
        preencher(internacao, request);
        Internacao salva = internacaoRepository.save(internacao);
        atividadeService.registrar(usuarioEmail, "ATUALIZAR", "Internacao", salva.getId(),
                "Internação de " + salva.getPet().getNome());
        return InternacaoResponse.from(salva);
    }

    @Transactional
    public InternacaoResponse darAlta(Long id, AltaRequest request, String usuarioEmail) {
        Internacao internacao = buscarEntidade(id);
        if (internacao.getStatus() != StatusInternacao.ATIVA) {
            throw new BusinessException("Somente internação ativa pode receber alta");
        }
        internacao.setStatus(StatusInternacao.ALTA);
        internacao.setDataAlta(LocalDateTime.now());
        internacao.setMotivoAlta(request.motivoAlta().trim());
        Internacao salva = internacaoRepository.save(internacao);
        atividadeService.registrar(usuarioEmail, "ALTA", "Internacao", id,
                "Alta de " + salva.getPet().getNome());
        return InternacaoResponse.from(salva);
    }

    @Transactional
    public void excluir(Long id, String usuarioEmail) {
        Internacao internacao = buscarEntidade(id);
        if (!evolucaoRepository.findByInternacaoIdOrderByDataHoraDesc(id).isEmpty()) {
            throw new BusinessException("Internação com evoluções registradas não pode ser excluída");
        }
        internacaoRepository.delete(internacao);
        atividadeService.registrar(usuarioEmail, "EXCLUIR", "Internacao", id,
                "Internação de " + internacao.getPet().getNome());
    }

    @Transactional(readOnly = true)
    public List<EvolucaoResponse> listarEvolucoes(Long internacaoId) {
        buscarEntidade(internacaoId);
        return evolucaoRepository.findByInternacaoIdOrderByDataHoraDesc(internacaoId).stream()
                .map(EvolucaoResponse::from)
                .toList();
    }

    @Transactional
    public EvolucaoResponse registrarEvolucao(Long internacaoId, EvolucaoRequest request, String usuarioEmail) {
        Internacao internacao = buscarEntidade(internacaoId);
        if (internacao.getStatus() != StatusInternacao.ATIVA) {
            throw new BusinessException("Somente internação ativa pode receber evolução");
        }
        EvolucaoInternacao evolucao = new EvolucaoInternacao();
        evolucao.setInternacao(internacao);
        evolucao.setDescricao(request.descricao().trim());
        evolucao.setSinaisVitais(request.sinaisVitais());
        evolucao.setMedicamentos(request.medicamentos());
        evolucao.setAlimentacaoCuidados(request.alimentacaoCuidados());
        evolucao.setUsuarioEmail(usuarioEmail);
        EvolucaoInternacao salva = evolucaoRepository.save(evolucao);
        atividadeService.registrar(usuarioEmail, "EVOLUCAO", "Internacao", internacaoId,
                "Evolução registrada para " + internacao.getPet().getNome());
        return EvolucaoResponse.from(salva);
    }

    @Transactional
    public void excluirEvolucao(Long id, String usuarioEmail) {
        EvolucaoInternacao evolucao = evolucaoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Evolução", id));
        evolucaoRepository.delete(evolucao);
        atividadeService.registrar(usuarioEmail, "EXCLUIR_EVOLUCAO", "Internacao",
                evolucao.getInternacao().getId(), "Evolução removida");
    }

    private void preencher(Internacao internacao, InternacaoRequest request) {
        Pet pet = petRepository.findById(request.petId())
                .orElseThrow(() -> ResourceNotFoundException.de("Pet", request.petId()));
        if (!pet.isAtivo()) {
            throw new BusinessException("Pet informado está inativo");
        }
        Veterinario veterinario = veterinarioRepository.findById(request.veterinarioId())
                .orElseThrow(() -> ResourceNotFoundException.de("Veterinário", request.veterinarioId()));
        if (!veterinario.isAtivo()) {
            throw new BusinessException("Veterinário informado está inativo");
        }
        internacao.setPet(pet);
        internacao.setVeterinario(veterinario);
        internacao.setMotivo(request.motivo().trim());
        internacao.setDataEntrada(request.dataEntrada() != null ? request.dataEntrada() : LocalDateTime.now());
        internacao.setObservacoes(request.observacoes());
    }

    private Internacao buscarEntidade(Long id) {
        return internacaoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Internação", id));
    }
}
