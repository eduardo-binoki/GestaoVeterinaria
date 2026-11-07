(function () {
  function formatarQuando(iso) {
    const data = new Date(iso);
    if (Number.isNaN(data.getTime())) {
      return '';
    }

    const minutos = Math.floor((Date.now() - data.getTime()) / 60000);
    if (minutos < 1) {
      return 'agora';
    }
    if (minutos < 60) {
      return `há ${minutos} min`;
    }

    const horas = Math.floor(minutos / 60);
    if (horas < 24) {
      return `há ${horas} h`;
    }

    return data.toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  function escapar(valor) {
    const mapa = {
      '&': '&amp;',
      '<': '&lt;',
      '>': '&gt;',
      '"': '&quot;',
      "'": '&#39;'
    };
    return String(valor == null ? '' : valor).replace(/[&<>"']/g, (caractere) => mapa[caractere]);
  }

  document.addEventListener('DOMContentLoaded', () => {
    const topbarUser = document.querySelector('.topbar-user');
    const userToggle = document.getElementById('user-menu-toggle');
    if (!topbarUser || !userToggle || typeof Api === 'undefined') {
      return;
    }

    const wrapper = document.createElement('div');
    wrapper.className = 'notif';
    wrapper.id = 'notif-menu';
    wrapper.innerHTML = `
      <button
        type="button"
        class="notif-button"
        id="notif-toggle"
        aria-haspopup="true"
        aria-expanded="false"
        aria-controls="notif-dropdown"
        aria-label="Notificações"
      >
        <svg viewBox="0 0 24 24" aria-hidden="true">
          <path d="M18 8a6 6 0 0 0-12 0c0 7-3 9-3 9h18s-3-2-3-9"/>
          <path d="M13.7 21a2 2 0 0 1-3.4 0"/>
        </svg>
        <span class="notif-badge" id="notif-badge" hidden>0</span>
      </button>
      <div class="notif-dropdown" id="notif-dropdown" hidden>
        <div class="notif-header">
          <strong>Notificações</strong>
          <button type="button" class="notif-clear" id="notif-clear">Limpar</button>
        </div>
        <ul class="notif-list" id="notif-list"></ul>
        <p class="notif-empty" id="notif-empty" hidden>Nenhuma notificação.</p>
      </div>`;

    topbarUser.insertBefore(wrapper, userToggle);

    const toggle = document.getElementById('notif-toggle');
    const dropdown = document.getElementById('notif-dropdown');
    const badge = document.getElementById('notif-badge');
    const lista = document.getElementById('notif-list');
    const vazio = document.getElementById('notif-empty');
    const limparBtn = document.getElementById('notif-clear');

    let itens = [];
    let carregando = false;

    function renderizar() {
      const total = itens.length;

      badge.hidden = total === 0;
      badge.textContent = total > 99 ? '99+' : String(total);
      toggle.setAttribute(
        'aria-label',
        total === 0 ? 'Notificações' : `Notificações (${total})`
      );
      limparBtn.disabled = total === 0 || carregando;

      vazio.hidden = total !== 0;
      lista.hidden = total === 0;
      vazio.textContent = 'Nenhuma notificação.';
      lista.innerHTML = itens
        .map(
          (item) => `<li class="notif-item notif-item--${escapar(item.tipo)}">
            <span class="notif-dot" aria-hidden="true"></span>
            <span class="notif-texto">
              <span class="notif-mensagem">${escapar(item.mensagem)}</span>
              <span class="notif-quando">${escapar(formatarQuando(item.dataHora))}</span>
            </span>
          </li>`
        )
        .join('');
    }

    async function carregar() {
      if (!Sessao.autenticado() || carregando) {
        return;
      }
      carregando = true;
      renderizar();
      try {
        const resposta = await Api.request('/api/notificacoes');
        itens = Array.isArray(resposta) ? resposta : [];
      } catch (erro) {
        itens = [];
        vazio.textContent = 'Não foi possível carregar as notificações.';
      } finally {
        carregando = false;
        renderizar();
      }
    }

    async function limpar() {
      if (limparBtn.disabled) {
        return;
      }
      limparBtn.disabled = true;
      try {
        await Api.request('/api/notificacoes', { method: 'DELETE' });
        itens = [];
      } finally {
        renderizar();
      }
    }

    function fecharUsuario() {
      const userDropdown = document.getElementById('user-dropdown');
      if (userDropdown) {
        userDropdown.hidden = true;
      }
      userToggle.setAttribute('aria-expanded', 'false');
    }

    function abrir() {
      fecharUsuario();
      dropdown.hidden = false;
      toggle.setAttribute('aria-expanded', 'true');
      carregar();
    }

    function fechar() {
      dropdown.hidden = true;
      toggle.setAttribute('aria-expanded', 'false');
    }

    toggle.addEventListener('click', () => {
      if (dropdown.hidden) {
        abrir();
      } else {
        fechar();
      }
    });

    document.addEventListener('pointerdown', (event) => {
      if (!wrapper.contains(event.target)) {
        fechar();
      }
    });

    document.addEventListener('keydown', (event) => {
      if (event.key === 'Escape' && !dropdown.hidden) {
        fechar();
        toggle.focus();
      }
    });

    limparBtn.addEventListener('click', limpar);

    window.addEventListener('fechar-notificacoes', fechar);
    window.addEventListener('notificacoes-atualizadas', carregar);
    window.addEventListener('focus', carregar);

    window.Notificacoes = {
      listar: () => itens,
      contar: () => itens.length,
      recarregar: carregar,
      limpar
    };

    renderizar();
    carregar();
    window.setInterval(carregar, 30000);
  });
})();
