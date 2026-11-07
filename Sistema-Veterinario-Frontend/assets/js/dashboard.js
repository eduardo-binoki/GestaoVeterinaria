document.addEventListener('DOMContentLoaded', async () => {
  if (!Sessao.autenticado()) {
    window.location.replace('index.html');
    return;
  }

  const elNome = document.getElementById('usuario-nome');
  const elEmail = document.getElementById('usuario-email');
  const elAvatar = document.getElementById('usuario-avatar');
  const elBoasVindas = document.getElementById('boas-vindas');

  function iniciais(nome) {
    if (!nome) {
      return '--';
    }
    const partes = nome.trim().split(/\s+/);
    const primeira = partes[0] ? partes[0][0] : '';
    const ultima = partes.length > 1 ? partes[partes.length - 1][0] : '';
    return (primeira + ultima).toUpperCase();
  }

  function preencher(usuario) {
    elNome.textContent = usuario.nome;
    elEmail.textContent = usuario.email;
    elAvatar.textContent = iniciais(usuario.nome);
    elBoasVindas.textContent = 'Bem-vindo de volta!';

    window.dispatchEvent(
      new CustomEvent('usuario-atualizado', { detail: usuario })
    );
  }

  function sair() {
    Sessao.limpar();
    window.location.replace('index.html');
  }

  function paraInputDataHora(data) {
    const pad = (numero) => String(numero).padStart(2, '0');
    return `${data.getFullYear()}-${pad(data.getMonth() + 1)}-${pad(data.getDate())}T${pad(data.getHours())}:${pad(data.getMinutes())}`;
  }

  function formatarHora(valor) {
    const data = valor instanceof Date ? valor : new Date(valor);
    if (Number.isNaN(data.getTime())) {
      return '--:--';
    }
    return data.toLocaleTimeString('pt-BR', {
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  function escaparHtml(valor) {
    const mapa = {
      '&': '&amp;',
      '<': '&lt;',
      '>': '&gt;',
      '"': '&quot;',
      "'": '&#39;'
    };
    return String(valor == null ? '' : valor).replace(/[&<>"']/g, (caractere) => mapa[caractere]);
  }

  const STATUS_LABELS = {
    AGENDADA: 'Agendada',
    REALIZADA: 'Realizada',
    CANCELADA: 'Cancelada',
    NAO_COMPARECEU: 'Não compareceu'
  };

  const TIPO_LABELS = {
    ROTINA: 'Rotina',
    RETORNO: 'Retorno',
    EMERGENCIA: 'Emergência',
    CIRURGIA: 'Cirurgia',
    VACINACAO: 'Vacinação'
  };

  function buscarAgendaDoDia() {
    const inicio = new Date();
    inicio.setHours(0, 0, 0, 0);
    const fim = new Date();
    fim.setHours(23, 59, 59, 0);
    return Api.request(
      `/api/consultas/agenda?inicio=${encodeURIComponent(paraInputDataHora(inicio))}&fim=${encodeURIComponent(paraInputDataHora(fim))}`
    );
  }

  const agendaDoDia = buscarAgendaDoDia();
  const estoqueBaixo = Api.request('/api/produtos/estoque-baixo');

  const RESUMO = [
    {
      contador: 'stat-consultas',
      info: 'stat-consultas-info',
      buscar() {
        return agendaDoDia;
      }
    },
    {
      contador: 'stat-pets',
      info: 'stat-pets-info',
      buscar() {
        return Api.request('/api/pets');
      }
    },
    {
      contador: 'stat-tutores',
      info: 'stat-tutores-info',
      buscar() {
        return Api.request('/api/tutores');
      }
    },
    {
      contador: 'stat-estoque',
      info: 'stat-estoque-info',
      buscar() {
        return estoqueBaixo;
      }
    }
  ];

  async function carregarResumo() {
    const resultados = await Promise.allSettled(
      RESUMO.map((item) => item.buscar())
    );

    resultados.forEach((resultado, indice) => {
      const item = RESUMO[indice];
      const contador = document.getElementById(item.contador);
      const info = document.getElementById(item.info);
      if (!contador) {
        return;
      }

      if (resultado.status === 'fulfilled') {
        const total = Array.isArray(resultado.value)
          ? resultado.value.length
          : 0;
        contador.textContent = total;
        if (info) {
          info.textContent = '';
          info.hidden = true;
        }
        return;
      }

      const erro = resultado.reason;
      contador.textContent = '—';
      if (info) {
        info.hidden = false;
        if (erro instanceof ApiError && erro.status === 403) {
          info.textContent = 'Sem permissão de acesso';
        } else if (erro instanceof ApiError && erro.status === 401) {
          info.textContent = 'Sessão expirada';
        } else {
          info.textContent = 'Não foi possível carregar';
        }
      }
    });
  }

  function renderAgendaDoDia(itens) {
    const container = document.getElementById('agenda-dia');
    if (!container) {
      return;
    }

    const ordenados = [...itens].sort(
      (a, b) => new Date(a.dataHora) - new Date(b.dataHora)
    );

    if (!ordenados.length) {
      container.innerHTML =
        '<p class="agenda-dia-vazio">Nenhuma consulta agendada para hoje.</p>';
      return;
    }

    container.innerHTML = `<ul class="agenda-dia-lista">${ordenados
      .map((item) => {
        const status = item.status || '';
        const tipo = TIPO_LABELS[item.tipo] || item.tipo || '';
        const detalhe = [tipo, item.tutorNome, item.veterinarioNome]
          .filter(Boolean)
          .map(escaparHtml)
          .join(' • ');

        return `<li class="agenda-dia-item">
          <span class="agenda-dia-hora">${formatarHora(item.dataHora)}</span>
          <span class="agenda-dia-info">
            <strong>${escaparHtml(item.petNome || 'Pet')}</strong>
            <small>${detalhe || 'Sem detalhes'}</small>
          </span>
          <span class="agenda-dia-tag agenda-dia-tag--${escaparHtml(
            status.toLowerCase()
          )}">${escaparHtml(STATUS_LABELS[status] || status)}</span>
        </li>`;
      })
      .join('')}</ul>`;
  }

  async function carregarAgendaDoDia() {
    const container = document.getElementById('agenda-dia');
    if (!container) {
      return;
    }

    try {
      const itens = await agendaDoDia;
      renderAgendaDoDia(Array.isArray(itens) ? itens : []);
    } catch (erro) {
      if (erro instanceof ApiError && erro.status === 403) {
        container.innerHTML =
          '<p class="agenda-dia-vazio">Sem permissão de acesso.</p>';
      } else if (erro instanceof ApiError && erro.status === 401) {
        container.innerHTML =
          '<p class="agenda-dia-vazio">Sessão expirada.</p>';
      } else {
        container.innerHTML =
          '<p class="agenda-dia-vazio">Não foi possível carregar a agenda.</p>';
      }
    }
  }

  function renderAlertas(itens) {
    const container = document.getElementById('alertas-notificacoes');
    if (!container) {
      return;
    }

    if (!itens.length) {
      container.innerHTML =
        '<p class="agenda-dia-vazio">Nenhum alerta no momento.</p>';
      return;
    }

    container.innerHTML = `<ul class="alertas-notificacoes-lista">${itens
      .map(
        (item) => `<li class="alertas-notificacoes-item">
          <span class="alertas-notificacoes-info">
            <strong>${escaparHtml(item.nome || 'Produto')}</strong>
            <small>${escaparHtml(item.quantidadeEstoque)} em estoque • mínimo ${escaparHtml(
              item.estoqueMinimo
            )}</small>
          </span>
          <span class="alertas-notificacoes-tag">Estoque baixo</span>
        </li>`
      )
      .join('')}</ul>`;
  }

  async function carregarAlertas() {
    const container = document.getElementById('alertas-notificacoes');
    if (!container) {
      return;
    }

    try {
      const itens = await estoqueBaixo;
      renderAlertas(Array.isArray(itens) ? itens : []);
    } catch (erro) {
      if (erro instanceof ApiError && erro.status === 403) {
        container.innerHTML =
          '<p class="agenda-dia-vazio">Sem permissão de acesso.</p>';
      } else if (erro instanceof ApiError && erro.status === 401) {
        container.innerHTML =
          '<p class="agenda-dia-vazio">Sessão expirada.</p>';
      } else {
        container.innerHTML =
          '<p class="agenda-dia-vazio">Não foi possível carregar os alertas.</p>';
      }
    }
  }

  const emCache = Sessao.getUsuario();
  if (emCache) {
    preencher(emCache);
  }

  carregarResumo();
  carregarAgendaDoDia();
  carregarAlertas();

  try {
    const usuario = await Api.me();
    Sessao.salvar(
      {
        token: Sessao.getToken(),
        usuario
      },
      Boolean(localStorage.getItem('vet.token'))
    );
    preencher(usuario);
  } catch (error) {
    if (error instanceof ApiError && (error.status === 401 || error.status === 403)) {
      sair();
      return;
    }
    if (!emCache) {
      elNome.textContent = 'Não foi possível carregar';
      elEmail.textContent = error.message;
    }
  }
});
