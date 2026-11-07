package com.veterinaria.Sistema_Veterinario.service;

import com.veterinaria.Sistema_Veterinario.domain.ContaFinanceira;
import com.veterinaria.Sistema_Veterinario.domain.Vacina;
import com.veterinaria.Sistema_Veterinario.exception.BusinessException;
import com.veterinaria.Sistema_Veterinario.repository.ConsultaRepository;
import com.veterinaria.Sistema_Veterinario.repository.ContaFinanceiraRepository;
import com.veterinaria.Sistema_Veterinario.repository.ProdutoRepository;
import com.veterinaria.Sistema_Veterinario.repository.VacinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final ConsultaRepository consultaRepository;
    private final ContaFinanceiraRepository contaRepository;
    private final ProdutoRepository produtoRepository;
    private final VacinaRepository vacinaRepository;

    @Transactional(readOnly = true)
    public String consultas(LocalDate inicio, LocalDate fim) {
        LocalDate inicioEfetivo = inicio != null ? inicio : LocalDate.now().withDayOfMonth(1);
        LocalDate fimEfetivo = fim != null ? fim : LocalDate.now();
        validarPeriodo(inicioEfetivo, fimEfetivo);

        StringBuilder csv = new StringBuilder();
        csv.append(linha("ID", "Data/Hora", "Pet", "Tutor", "Veterinario", "Tipo", "Status", "Valor", "Motivo"));
        consultaRepository.findByDataHoraBetweenOrderByDataHoraAsc(
                        inicioEfetivo.atStartOfDay(), fimEfetivo.atTime(23, 59, 59))
                .forEach(c -> csv.append(linha(
                        c.getId(),
                        c.getDataHora(),
                        c.getPet().getNome(),
                        c.getPet().getTutor().getNome(),
                        c.getVeterinario().getNome(),
                        c.getTipo(),
                        c.getStatus(),
                        c.getValor(),
                        c.getMotivo()
                )));
        return csv.toString();
    }

    @Transactional(readOnly = true)
    public String financeiro(LocalDate inicio, LocalDate fim) {
        LocalDate inicioEfetivo = inicio != null ? inicio : LocalDate.now().withDayOfMonth(1);
        LocalDate fimEfetivo = fim != null ? fim : LocalDate.now();
        validarPeriodo(inicioEfetivo, fimEfetivo);

        StringBuilder csv = new StringBuilder();
        csv.append(linha("ID", "Descricao", "Tipo", "Categoria", "Valor previsto", "Valor pago",
                "Vencimento", "Status", "Consulta"));
        contaRepository.findAllByOrderByVencimentoAsc().stream()
                .filter(ContaFinanceira::isAtivo)
                .filter(c -> !c.getVencimento().isBefore(inicioEfetivo) && !c.getVencimento().isAfter(fimEfetivo))
                .forEach(c -> csv.append(linha(
                        c.getId(),
                        c.getDescricao(),
                        c.getTipo(),
                        c.getCategoria() != null ? c.getCategoria().getNome() : "",
                        c.getValorPrevisto(),
                        c.getValorPago(),
                        c.getVencimento(),
                        c.getStatus(),
                        c.getConsulta() != null ? c.getConsulta().getId() : ""
                )));
        return csv.toString();
    }

    @Transactional(readOnly = true)
    public String estoque() {
        StringBuilder csv = new StringBuilder();
        csv.append(linha("ID", "Produto", "Categoria", "Preco", "Estoque", "Estoque minimo",
                "Lote", "Validade", "Estoque baixo", "Vencido"));
        produtoRepository.findByAtivoTrueOrderByNomeAsc().forEach(p -> csv.append(linha(
                p.getId(),
                p.getNome(),
                p.getCategoria(),
                p.getPreco(),
                p.getQuantidadeEstoque(),
                p.getEstoqueMinimo(),
                p.getLote(),
                p.getValidade(),
                p.getQuantidadeEstoque() <= p.getEstoqueMinimo() ? "SIM" : "NAO",
                p.getValidade() != null && p.getValidade().isBefore(LocalDate.now()) ? "SIM" : "NAO"
        )));
        return csv.toString();
    }

    @Transactional(readOnly = true)
    public String vacinas(LocalDate inicio, LocalDate fim) {
        LocalDate inicioEfetivo = inicio != null ? inicio : LocalDate.now();
        LocalDate fimEfetivo = fim != null ? fim : LocalDate.now().plusDays(30);
        validarPeriodo(inicioEfetivo, fimEfetivo);

        StringBuilder csv = new StringBuilder();
        csv.append(linha("ID", "Pet", "Tutor", "Vacina", "Fabricante", "Lote", "Aplicacao",
                "Proxima dose", "Dose", "Reforco", "Status"));
        List<Vacina> vacinas = vacinaRepository.findAllByOrderByDataAplicacaoDesc();
        vacinas.stream()
                .filter(v -> v.getProximaDose() != null)
                .filter(v -> !v.getProximaDose().isBefore(inicioEfetivo) && !v.getProximaDose().isAfter(fimEfetivo))
                .forEach(v -> csv.append(linha(
                        v.getId(),
                        v.getPet().getNome(),
                        v.getPet().getTutor().getNome(),
                        v.getNome(),
                        v.getFabricante(),
                        v.getLote(),
                        v.getDataAplicacao(),
                        v.getProximaDose(),
                        v.getDose(),
                        v.isReforco() ? "SIM" : "NAO",
                        v.getStatus()
                )));
        return csv.toString();
    }

    private void validarPeriodo(LocalDate inicio, LocalDate fim) {
        if (fim.isBefore(inicio)) {
            throw new BusinessException("Data final não pode ser anterior à data inicial");
        }
    }

    private String linha(Object... valores) {
        StringBuilder linha = new StringBuilder();
        for (int i = 0; i < valores.length; i++) {
            if (i > 0) {
                linha.append(';');
            }
            linha.append(escapar(valores[i]));
        }
        return linha.append('\n').toString();
    }

    private String escapar(Object valor) {
        if (valor == null) {
            return "";
        }
        String texto = valor instanceof BigDecimal bd ? bd.toPlainString() : valor.toString();
        if (texto.contains(";") || texto.contains("\"") || texto.contains("\n")) {
            return '"' + texto.replace("\"", "\"\"") + '"';
        }
        return texto;
    }
}
