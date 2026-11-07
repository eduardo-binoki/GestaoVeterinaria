package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.StatusConsulta;
import com.veterinaria.Sistema_Veterinario.domain.StatusInternacao;
import com.veterinaria.Sistema_Veterinario.dto.ConsultaDtos.ConsultaResponse;
import com.veterinaria.Sistema_Veterinario.dto.DashboardDtos.AtividadeResponse;
import com.veterinaria.Sistema_Veterinario.dto.DashboardDtos.DashboardResponse;
import com.veterinaria.Sistema_Veterinario.exception.BusinessException;
import com.veterinaria.Sistema_Veterinario.repository.ConsultaRepository;
import com.veterinaria.Sistema_Veterinario.repository.InternacaoRepository;
import com.veterinaria.Sistema_Veterinario.repository.PetRepository;
import com.veterinaria.Sistema_Veterinario.repository.RegistroAtividadeRepository;
import com.veterinaria.Sistema_Veterinario.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TutorRepository tutorRepository;
    private final PetRepository petRepository;
    private final ConsultaRepository consultaRepository;
    private final InternacaoRepository internacaoRepository;
    private final RegistroAtividadeRepository atividadeRepository;
    private final ProdutoService produtoService;
    private final VacinaService vacinaService;
    private final FinanceiroService financeiroService;

    @Transactional(readOnly = true)
    public DashboardResponse carregar() {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicioDia = hoje.atStartOfDay();
        LocalDateTime fimDia = hoje.atTime(23, 59, 59);

        var agenda = consultaRepository.findByDataHoraBetweenOrderByDataHoraAsc(inicioDia, fimDia).stream()
                .map(ConsultaResponse::from)
                .toList();
        var atividades = atividadeRepository.findTop100ByOrderByDataHoraDesc().stream()
                .limit(10)
                .map(AtividadeResponse::from)
                .toList();

        long agendadas = consultaRepository.countByStatus(StatusConsulta.AGENDADA)
                + consultaRepository.countByStatus(StatusConsulta.CONFIRMADA);

        return new DashboardResponse(
                tutorRepository.countByAtivoTrue(),
                petRepository.countByAtivoTrue(),
                agenda.size(),
                agendadas,
                internacaoRepository.countByStatus(StatusInternacao.ATIVA),
                financeiroService.resumo(hoje.withDayOfMonth(1), hoje),
                agenda,
                produtoService.listar(null, true),
                produtoService.listarVencidos(),
                vacinaService.listarProximasDoses(30),
                atividades
        );
    }

    @Transactional(readOnly = true)
    public List<AtividadeResponse> atividadesRecentes() {
        return atividadeRepository.findTop100ByOrderByDataHoraDesc().stream()
                .limit(50)
                .map(AtividadeResponse::from)
                .toList();
    }
}
