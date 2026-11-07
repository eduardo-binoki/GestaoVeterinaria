package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.AusenciaVeterinario;
import com.veterinaria.Sistema_Veterinario.domain.DisponibilidadeVeterinario;
import com.veterinaria.Sistema_Veterinario.domain.Veterinario;
import com.veterinaria.Sistema_Veterinario.dto.VeterinarioDtos.AusenciaRequest;
import com.veterinaria.Sistema_Veterinario.dto.VeterinarioDtos.AusenciaResponse;
import com.veterinaria.Sistema_Veterinario.dto.VeterinarioDtos.DisponibilidadeRequest;
import com.veterinaria.Sistema_Veterinario.dto.VeterinarioDtos.DisponibilidadeResponse;
import com.veterinaria.Sistema_Veterinario.dto.VeterinarioDtos.VeterinarioRequest;
import com.veterinaria.Sistema_Veterinario.dto.VeterinarioDtos.VeterinarioResponse;
import com.veterinaria.Sistema_Veterinario.exception.BusinessException;
import com.veterinaria.Sistema_Veterinario.exception.ResourceNotFoundException;
import com.veterinaria.Sistema_Veterinario.repository.AusenciaVeterinarioRepository;
import com.veterinaria.Sistema_Veterinario.repository.ConsultaRepository;
import com.veterinaria.Sistema_Veterinario.repository.DisponibilidadeVeterinarioRepository;
import com.veterinaria.Sistema_Veterinario.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;
    private final ConsultaRepository consultaRepository;
    private final DisponibilidadeVeterinarioRepository disponibilidadeRepository;
    private final AusenciaVeterinarioRepository ausenciaRepository;
    private final RegistroAtividadeService atividadeService;

    @Transactional(readOnly = true)
    public List<VeterinarioResponse> listar() {
        return veterinarioRepository.findByAtivoTrueOrderByNomeAsc().stream()
                .map(VeterinarioResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public VeterinarioResponse buscar(Long id) {
        return VeterinarioResponse.from(buscarEntidade(id));
    }

    @Transactional
    public VeterinarioResponse criar(VeterinarioRequest request) {
        String crmv = normalizarCrmv(request.crmv());
        if (veterinarioRepository.existsByCrmv(crmv)) {
            throw new BusinessException("Já existe um veterinário cadastrado com este CRMV");
        }
        Veterinario veterinario = new Veterinario();
        preencher(veterinario, request, crmv);
        Veterinario salvo = veterinarioRepository.save(veterinario);
        atividadeService.registrarAtual("CRIAR", "Veterinario", salvo.getId(), salvo.getNome());
        return VeterinarioResponse.from(salvo);
    }

    @Transactional
    public VeterinarioResponse atualizar(Long id, VeterinarioRequest request) {
        Veterinario veterinario = buscarEntidade(id);
        String crmv = normalizarCrmv(request.crmv());
        if (veterinarioRepository.existsByCrmvAndIdNot(crmv, id)) {
            throw new BusinessException("Já existe um veterinário cadastrado com este CRMV");
        }
        preencher(veterinario, request, crmv);
        Veterinario salvo = veterinarioRepository.save(veterinario);
        atividadeService.registrarAtual("ATUALIZAR", "Veterinario", salvo.getId(), salvo.getNome());
        return VeterinarioResponse.from(salvo);
    }

    @Transactional
    public void desativar(Long id) {
        Veterinario veterinario = buscarEntidade(id);
        if (consultaRepository.existsByVeterinarioId(id)) {
            throw new BusinessException("Não é possível desativar um veterinário que possui consultas registradas");
        }
        veterinario.setAtivo(false);
        veterinarioRepository.save(veterinario);
        atividadeService.registrarAtual("DESATIVAR", "Veterinario", veterinario.getId(), veterinario.getNome());
    }

    @Transactional(readOnly = true)
    public List<DisponibilidadeResponse> listarDisponibilidades(Long veterinarioId) {
        buscarEntidade(veterinarioId);
        return disponibilidadeRepository
                .findByVeterinarioIdOrderByDiaSemanaAscHoraInicioAsc(veterinarioId).stream()
                .map(DisponibilidadeResponse::from)
                .toList();
    }

    @Transactional
    public DisponibilidadeResponse criarDisponibilidade(Long veterinarioId, DisponibilidadeRequest request) {
        Veterinario veterinario = buscarEntidade(veterinarioId);
        validarIntervalo(request);
        validarSobreposicaoDisponibilidade(veterinarioId, request, null);

        DisponibilidadeVeterinario disponibilidade = new DisponibilidadeVeterinario();
        disponibilidade.setVeterinario(veterinario);
        preencherDisponibilidade(disponibilidade, request);
        return DisponibilidadeResponse.from(disponibilidadeRepository.save(disponibilidade));
    }

    @Transactional
    public DisponibilidadeResponse atualizarDisponibilidade(Long id, DisponibilidadeRequest request) {
        DisponibilidadeVeterinario disponibilidade = disponibilidadeRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Disponibilidade", id));
        validarIntervalo(request);
        validarSobreposicaoDisponibilidade(disponibilidade.getVeterinario().getId(), request, id);
        preencherDisponibilidade(disponibilidade, request);
        return DisponibilidadeResponse.from(disponibilidadeRepository.save(disponibilidade));
    }

    @Transactional
    public void excluirDisponibilidade(Long id) {
        DisponibilidadeVeterinario disponibilidade = disponibilidadeRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Disponibilidade", id));
        disponibilidadeRepository.delete(disponibilidade);
    }

    @Transactional(readOnly = true)
    public List<AusenciaResponse> listarAusencias(Long veterinarioId) {
        buscarEntidade(veterinarioId);
        return ausenciaRepository.findByVeterinarioIdOrderByInicioDesc(veterinarioId).stream()
                .map(AusenciaResponse::from)
                .toList();
    }

    @Transactional
    public AusenciaResponse criarAusencia(Long veterinarioId, AusenciaRequest request) {
        Veterinario veterinario = buscarEntidade(veterinarioId);
        if (!request.inicio().isBefore(request.fim())) {
            throw new BusinessException("Início da ausência deve ser anterior ao fim");
        }
        boolean temConsulta = consultaRepository.findByVeterinarioIdOrderByDataHoraDesc(veterinarioId).stream()
                .filter(c -> c.getStatus() != com.veterinaria.Sistema_Veterinario.domain.StatusConsulta.CANCELADA)
                .anyMatch(c -> !c.getDataHora().isBefore(request.inicio())
                        && !c.getDataHora().isAfter(request.fim()));
        if (temConsulta) {
            throw new BusinessException("Existem consultas agendadas no período de ausência");
        }

        AusenciaVeterinario ausencia = new AusenciaVeterinario();
        ausencia.setVeterinario(veterinario);
        ausencia.setInicio(request.inicio());
        ausencia.setFim(request.fim());
        ausencia.setMotivo(request.motivo());
        return AusenciaResponse.from(ausenciaRepository.save(ausencia));
    }

    @Transactional
    public void excluirAusencia(Long id) {
        AusenciaVeterinario ausencia = ausenciaRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Ausência", id));
        ausenciaRepository.delete(ausencia);
    }

    private void validarIntervalo(DisponibilidadeRequest request) {
        if (!request.horaInicio().isBefore(request.horaFim())) {
            throw new BusinessException("Hora inicial deve ser anterior à hora final");
        }
    }

    private void validarSobreposicaoDisponibilidade(Long veterinarioId, DisponibilidadeRequest request,
                                                    Long ignoradaId) {
        boolean sobrepoe = disponibilidadeRepository
                .findByVeterinarioIdAndDiaSemanaAndAtivoTrue(veterinarioId, request.diaSemana()).stream()
                .filter(d -> ignoradaId == null || !d.getId().equals(ignoradaId))
                .anyMatch(d -> request.horaInicio().isBefore(d.getHoraFim())
                        && request.horaFim().isAfter(d.getHoraInicio()));
        if (sobrepoe) {
            throw new BusinessException("Já existe disponibilidade neste intervalo");
        }
    }

    private void preencherDisponibilidade(DisponibilidadeVeterinario disponibilidade,
                                          DisponibilidadeRequest request) {
        disponibilidade.setDiaSemana(request.diaSemana());
        disponibilidade.setHoraInicio(request.horaInicio());
        disponibilidade.setHoraFim(request.horaFim());
        disponibilidade.setAtivo(request.ativo() == null || request.ativo());
    }

    private void preencher(Veterinario veterinario, VeterinarioRequest request, String crmv) {
        veterinario.setNome(request.nome().trim());
        veterinario.setCrmv(crmv);
        veterinario.setEspecialidade(request.especialidade());
        veterinario.setEmail(request.email());
        veterinario.setTelefone(request.telefone());
        veterinario.setAtivo(true);
    }

    private Veterinario buscarEntidade(Long id) {
        return veterinarioRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Veterinário", id));
    }

    private String normalizarCrmv(String crmv) {
        String valor = crmv.trim().toUpperCase();
        if (valor.isBlank()) {
            throw new BusinessException("CRMV é obrigatório");
        }
        return valor;
    }
}
