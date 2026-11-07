package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.Consulta;
import com.veterinaria.Sistema_Veterinario.domain.HistoricoStatusConsulta;
import com.veterinaria.Sistema_Veterinario.domain.Pet;
import com.veterinaria.Sistema_Veterinario.domain.StatusConsulta;
import com.veterinaria.Sistema_Veterinario.domain.Veterinario;
import com.veterinaria.Sistema_Veterinario.dto.ConsultaDtos.AlterarStatusConsultaRequest;
import com.veterinaria.Sistema_Veterinario.dto.ConsultaDtos.ConsultaRequest;
import com.veterinaria.Sistema_Veterinario.dto.ConsultaDtos.ConsultaResponse;
import com.veterinaria.Sistema_Veterinario.dto.ConsultaDtos.HistoricoStatusResponse;
import com.veterinaria.Sistema_Veterinario.exception.BusinessException;
import com.veterinaria.Sistema_Veterinario.exception.ResourceNotFoundException;
import com.veterinaria.Sistema_Veterinario.repository.AusenciaVeterinarioRepository;
import com.veterinaria.Sistema_Veterinario.repository.ConsultaRepository;
import com.veterinaria.Sistema_Veterinario.repository.DisponibilidadeVeterinarioRepository;
import com.veterinaria.Sistema_Veterinario.repository.HistoricoStatusConsultaRepository;
import com.veterinaria.Sistema_Veterinario.repository.PetRepository;
import com.veterinaria.Sistema_Veterinario.repository.ProntuarioRepository;
import com.veterinaria.Sistema_Veterinario.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PetRepository petRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final ProntuarioRepository prontuarioRepository;
    private final HistoricoStatusConsultaRepository historicoRepository;
    private final DisponibilidadeVeterinarioRepository disponibilidadeRepository;
    private final AusenciaVeterinarioRepository ausenciaRepository;
    private final FinanceiroService financeiroService;
    private final RegistroAtividadeService atividadeService;

    @Transactional(readOnly = true)
    public List<ConsultaResponse> listar(Long petId, StatusConsulta status) {
        List<Consulta> consultas;
        if (petId != null) {
            consultas = consultaRepository.findByPetIdOrderByDataHoraDesc(petId);
            if (status != null) {
                consultas = consultas.stream().filter(c -> c.getStatus() == status).toList();
            }
        } else if (status != null) {
            consultas = consultaRepository.findByStatusOrderByDataHoraDesc(status);
        } else {
            consultas = consultaRepository.findAll(Sort.by(Sort.Direction.DESC, "dataHora"));
        }
        return consultas.stream().map(ConsultaResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponse> listarAgenda(LocalDateTime inicio, LocalDateTime fim) {
        return consultaRepository.findByDataHoraBetweenOrderByDataHoraAsc(inicio, fim).stream()
                .map(ConsultaResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConsultaResponse buscar(Long id) {
        return ConsultaResponse.from(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<HistoricoStatusResponse> historico(Long id) {
        buscarEntidade(id);
        return historicoRepository.findByConsultaIdOrderByDataHoraAsc(id).stream()
                .map(HistoricoStatusResponse::from)
                .toList();
    }

    @Transactional
    public ConsultaResponse criar(ConsultaRequest request) {
        Pet pet = buscarPetAtivo(request.petId());
        Veterinario veterinario = buscarVeterinarioAtivo(request.veterinarioId());
        StatusConsulta status = request.status() != null ? request.status() : StatusConsulta.AGENDADA;
        validarAgenda(veterinario.getId(), request.dataHora(), status, null, request.consultaOrigemId());

        Consulta consulta = new Consulta();
        preencher(consulta, request, pet, veterinario, status);
        Consulta salva = consultaRepository.save(consulta);
        registrarHistorico(salva, null, status, null, "Consulta criada");
        aplicarEfeitosStatus(salva, status);
        atividadeService.registrarAtual("CRIAR", "Consulta", salva.getId(),
                "Pet " + salva.getPet().getNome());
        return ConsultaResponse.from(salva);
    }

    @Transactional
    public ConsultaResponse atualizar(Long id, ConsultaRequest request) {
        Consulta consulta = buscarEntidade(id);
        StatusConsulta statusAnterior = consulta.getStatus();
        Pet pet = buscarPetAtivo(request.petId());
        Veterinario veterinario = buscarVeterinarioAtivo(request.veterinarioId());
        StatusConsulta status = request.status() != null ? request.status() : consulta.getStatus();

        if (status != statusAnterior) {
            validarTransicao(statusAnterior, status);
            validarJustificativaCancelamento(status, request.justificativaCancelamento());
            validarProntuarioParaRealizada(consulta.getId(), status);
        }
        validarAgenda(veterinario.getId(), request.dataHora(), status, id, request.consultaOrigemId());

        preencher(consulta, request, pet, veterinario, status);
        Consulta salva = consultaRepository.save(consulta);
        if (status != statusAnterior) {
            registrarHistorico(salva, statusAnterior, status, null, request.justificativaCancelamento());
            aplicarEfeitosStatus(salva, status);
        }
        atividadeService.registrarAtual("ATUALIZAR", "Consulta", salva.getId(),
                "Pet " + salva.getPet().getNome());
        return ConsultaResponse.from(salva);
    }

    @Transactional
    public ConsultaResponse alterarStatus(Long id, AlterarStatusConsultaRequest request, String usuarioEmail) {
        Consulta consulta = buscarEntidade(id);
        StatusConsulta statusAnterior = consulta.getStatus();
        StatusConsulta novoStatus = request.status();

        if (novoStatus == statusAnterior) {
            return ConsultaResponse.from(consulta);
        }
        validarTransicao(statusAnterior, novoStatus);
        validarJustificativaCancelamento(novoStatus, request.justificativa());
        validarProntuarioParaRealizada(consulta.getId(), novoStatus);

        consulta.setStatus(novoStatus);
        if (novoStatus == StatusConsulta.CANCELADA) {
            consulta.setJustificativaCancelamento(request.justificativa());
        }
        consultaRepository.save(consulta);
        registrarHistorico(consulta, statusAnterior, novoStatus, usuarioEmail, request.justificativa());
        aplicarEfeitosStatus(consulta, novoStatus);
        atividadeService.registrar(usuarioEmail, "ALTERAR_STATUS", "Consulta", consulta.getId(),
                "Status alterado para " + novoStatus);
        return ConsultaResponse.from(consulta);
    }

    @Transactional
    public ConsultaResponse cancelar(Long id) {
        Consulta consulta = buscarEntidade(id);
        StatusConsulta statusAnterior = consulta.getStatus();
        validarTransicao(statusAnterior, StatusConsulta.CANCELADA);
        consulta.setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);
        registrarHistorico(consulta, statusAnterior, StatusConsulta.CANCELADA, null,
                "Cancelamento solicitado");
        atividadeService.registrarAtual("CANCELAR", "Consulta", consulta.getId(),
                "Pet " + consulta.getPet().getNome());
        return ConsultaResponse.from(consulta);
    }

    @Transactional
    public void excluir(Long id) {
        Consulta consulta = buscarEntidade(id);
        if (prontuarioRepository.existsByConsultaId(id)) {
            throw new BusinessException("Não é possível excluir uma consulta que possui prontuário");
        }
        consultaRepository.delete(consulta);
        atividadeService.registrarAtual("EXCLUIR", "Consulta", id,
                "Pet " + consulta.getPet().getNome());
    }

    private void preencher(Consulta consulta, ConsultaRequest request, Pet pet,
                           Veterinario veterinario, StatusConsulta status) {
        consulta.setPet(pet);
        consulta.setVeterinario(veterinario);
        consulta.setDataHora(request.dataHora());
        consulta.setTipo(request.tipo());
        consulta.setStatus(status);
        consulta.setMotivo(request.motivo().trim());
        consulta.setDiagnostico(request.diagnostico());
        consulta.setObservacoes(request.observacoes());
        consulta.setValor(request.valor());
        if (request.justificativaCancelamento() != null) {
            consulta.setJustificativaCancelamento(request.justificativaCancelamento());
        }
        if (request.consultaOrigemId() != null) {
            Consulta origem = buscarEntidade(request.consultaOrigemId());
            if (origem.getId().equals(consulta.getId())) {
                throw new BusinessException("Consulta não pode ter ela mesma como origem");
            }
            consulta.setConsultaOrigem(origem);
        }
    }

    private void aplicarEfeitosStatus(Consulta consulta, StatusConsulta status) {
        if (status == StatusConsulta.EM_ATENDIMENTO && consulta.getIniciadaEm() == null) {
            consulta.setIniciadaEm(LocalDateTime.now());
            consultaRepository.save(consulta);
        }
        if (status == StatusConsulta.REALIZADA) {
            if (consulta.getIniciadaEm() == null) {
                consulta.setIniciadaEm(LocalDateTime.now());
            }
            if (consulta.getFinalizadaEm() == null) {
                consulta.setFinalizadaEm(LocalDateTime.now());
            }
            consultaRepository.save(consulta);
            financeiroService.registrarReceitaConsulta(consulta);
        }
    }

    private void validarTransicao(StatusConsulta atual, StatusConsulta novo) {
        Set<StatusConsulta> permitidos = switch (atual) {
            case AGENDADA -> Set.of(StatusConsulta.CONFIRMADA, StatusConsulta.EM_ATENDIMENTO,
                    StatusConsulta.CANCELADA, StatusConsulta.NAO_COMPARECEU);
            case CONFIRMADA -> Set.of(StatusConsulta.EM_ATENDIMENTO, StatusConsulta.CANCELADA,
                    StatusConsulta.NAO_COMPARECEU);
            case EM_ATENDIMENTO -> Set.of(StatusConsulta.REALIZADA, StatusConsulta.CANCELADA);
            case REALIZADA, CANCELADA, NAO_COMPARECEU -> Set.of();
        };
        if (!permitidos.contains(novo)) {
            throw new BusinessException("Transição de status inválida: " + atual + " para " + novo);
        }
    }

    private void validarJustificativaCancelamento(StatusConsulta status, String justificativa) {
        if (status == StatusConsulta.CANCELADA && (justificativa == null || justificativa.isBlank())) {
            throw new BusinessException("Justificativa é obrigatória para cancelar a consulta");
        }
    }

    private void validarProntuarioParaRealizada(Long consultaId, StatusConsulta status) {
        if (status == StatusConsulta.REALIZADA && !prontuarioRepository.existsByConsultaId(consultaId)) {
            throw new BusinessException("Registre o prontuário antes de finalizar a consulta");
        }
    }

    private void validarAgenda(Long veterinarioId, LocalDateTime dataHora, StatusConsulta status,
                               Long consultaIgnoradaId, Long consultaOrigemId) {
        if (status == StatusConsulta.CANCELADA || status == StatusConsulta.NAO_COMPARECEU) {
            return;
        }
        boolean conflito = consultaRepository.buscarConflitoAgenda(veterinarioId, dataHora).stream()
                .anyMatch(c -> consultaIgnoradaId == null || !c.getId().equals(consultaIgnoradaId));
        if (conflito) {
            throw new BusinessException("Veterinário já possui uma consulta neste horário");
        }
        validarAusencia(veterinarioId, dataHora);
        validarDisponibilidade(veterinarioId, dataHora);
    }

    private void validarAusencia(Long veterinarioId, LocalDateTime dataHora) {
        boolean ausente = !ausenciaRepository
                .findByVeterinarioIdAndInicioLessThanEqualAndFimGreaterThanEqual(
                        veterinarioId, dataHora, dataHora)
                .isEmpty();
        if (ausente) {
            throw new BusinessException("Veterinário está ausente no horário informado");
        }
    }

    private void validarDisponibilidade(Long veterinarioId, LocalDateTime dataHora) {
        var disponibilidades = disponibilidadeRepository
                .findByVeterinarioIdAndDiaSemanaAndAtivoTrue(veterinarioId, dataHora.getDayOfWeek());
        if (disponibilidades.isEmpty()) {
            return;
        }
        boolean dentroDoHorario = disponibilidades.stream().anyMatch(d ->
                !dataHora.toLocalTime().isBefore(d.getHoraInicio())
                        && dataHora.toLocalTime().isBefore(d.getHoraFim()));
        if (!dentroDoHorario) {
            throw new BusinessException("Horário fora da disponibilidade do veterinário");
        }
    }

    private void registrarHistorico(Consulta consulta, StatusConsulta anterior, StatusConsulta novo,
                                    String usuarioEmail, String justificativa) {
        HistoricoStatusConsulta historico = new HistoricoStatusConsulta();
        historico.setConsulta(consulta);
        historico.setStatusAnterior(anterior);
        historico.setStatusNovo(novo);
        historico.setUsuarioEmail(usuarioEmail);
        historico.setJustificativa(justificativa);
        historicoRepository.save(historico);
    }

    private Pet buscarPetAtivo(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Pet", id));
        if (!pet.isAtivo()) {
            throw new BusinessException("Pet informado está inativo");
        }
        return pet;
    }

    private Veterinario buscarVeterinarioAtivo(Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Veterinário", id));
        if (!veterinario.isAtivo()) {
            throw new BusinessException("Veterinário informado está inativo");
        }
        return veterinario;
    }

    private Consulta buscarEntidade(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Consulta", id));
    }
}
