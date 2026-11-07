document.addEventListener('DOMContentLoaded', () => {
  const drawer = document.getElementById('app-drawer');
  if (!drawer) {
    return;
  }

  const overlay = document.getElementById('drawer-overlay');
  const toggle = document.getElementById('menu-toggle');
  const nav = document.getElementById('drawer-nav');
  const logoutBtn = document.getElementById('drawer-logout');
  const topbar = document.querySelector('.topbar');

  function ajustarAlturaTopbar() {
    if (topbar) {
      document.documentElement.style.setProperty(
        '--topbar-altura',
        `${topbar.offsetHeight}px`
      );
    }
  }

  ajustarAlturaTopbar();
  window.addEventListener('resize', ajustarAlturaTopbar);
  if ('ResizeObserver' in window && topbar) {
    new ResizeObserver(ajustarAlturaTopbar).observe(topbar);
  }

  const ICONES = {
    home: '<path d="M3 10.5 12 3l9 7.5"/><path d="M5 9.5V21h14V9.5"/><path d="M9.5 21v-6h5v6"/>',
    tutor: '<circle cx="12" cy="8" r="4"/><path d="M4.5 21c0-4 3.4-6.5 7.5-6.5s7.5 2.5 7.5 6.5"/>',
    pet: '<circle cx="7" cy="8" r="1.8"/><circle cx="12" cy="6" r="1.8"/><circle cx="17" cy="8" r="1.8"/><path d="M12 11c-3 0-5 2-5 4a2 2 0 0 0 2 2c1 0 2-.5 3-.5s2 .5 3 .5a2 2 0 0 0 2-2c0-2-2-4-5-4Z"/>',
    vet: '<path d="M6 3v4M18 3v4"/><path d="M4 7h16v3a8 8 0 0 1-16 0V7Z"/><path d="M12 18v3"/><path d="M9 21h6"/>',
    consulta: '<rect x="3" y="5" width="18" height="16" rx="2"/><path d="M16 3v4M8 3v4M3 10h18"/><path d="M8 14h3M8 17h6"/>',
    vacina: '<path d="m18 2 4 4M17 7l-9 9-4 1 1-4 9-9Z"/><path d="m14 10 4 4"/>',
    prontuario: '<path d="M7 3h10a2 2 0 0 1 2 2v16l-7-4-7 4V5a2 2 0 0 1 2-2Z"/><path d="M9 8h6M9 11h6"/>',
    exame: '<path d="M9 3h6"/><path d="M10 3v6l-4 7a3 3 0 0 0 2.7 4.3h6.6A3 3 0 0 0 18 16l-4-7V3"/><path d="M8 14h8"/>',
    prescricao: '<path d="M7 3h10a2 2 0 0 1 2 2v16l-7-4-7 4V5a2 2 0 0 1 2-2Z"/><path d="M9 8h6M12 5v6"/>',
    internacao: '<path d="M4 21V8l8-5 8 5v13"/><path d="M9 21v-6h6v6"/><path d="M12 8v3M10.5 9.5h3"/>',
    financeiro: '<rect x="3" y="6" width="18" height="13" rx="2"/><path d="M3 10h18"/><path d="M7 15h4"/>',
    relatorio: '<path d="M6 3h9l4 4v14H6z"/><path d="M15 3v4h4"/><path d="M9 13h6M9 17h6"/>',
    produto: '<path d="m21 8-9-5-9 5 9 5 9-5Z"/><path d="M3 8v8l9 5 9-5V8"/><path d="M12 13v8"/>',
    movimentacao: '<path d="M4 7h13l-3-3"/><path d="M20 17H7l3 3"/>',
    usuario: '<circle cx="12" cy="8" r="4"/><path d="M4.5 21c0-4 3.4-6.5 7.5-6.5s7.5 2.5 7.5 6.5"/><path d="M17 3.5 18.5 5 21 2.5"/>',
    config: '<circle cx="12" cy="12" r="3"/><path d="M12 2v3M12 19v3M4.2 4.2l2.1 2.1M17.7 17.7l2.1 2.1M2 12h3M19 12h3M4.2 19.8l2.1-2.1M17.7 6.3l2.1-2.1"/>'
  };

  const usuario = Sessao.getUsuario();
  const role = usuario ? usuario.role : null;
  const params = new URLSearchParams(window.location.search);
  const paginaAtual = window.location.pathname.split('/').pop() || 'index.html';

  let ultimoFoco = null;

  function podeAcessar(item) {
    return !item.roles || (role && item.roles.includes(role));
  }

  function icone(nome) {
    const conteudo = ICONES[nome] || ICONES.home;
    return `<svg class="drawer-icon" viewBox="0 0 24 24" aria-hidden="true">${conteudo}</svg>`;
  }

  function linkAtivo(href) {
    return href.split('?')[0] === paginaAtual && !params.get('recurso');
  }

  function criarLink(href, rotulo, ativo, nomeIcone) {
    const classe = ativo ? 'drawer-link ativo' : 'drawer-link';
    const aria = ativo ? ' aria-current="page"' : '';
    return `<a class="${classe}" href="${href}"${aria}>${icone(nomeIcone)}<span class="drawer-label">${rotulo}</span></a>`;
  }

  function criarGrupo(item) {
    const rotinas = (item.rotinas || []).filter(podeAcessar);
    if (rotinas.length === 0) {
      return '';
    }

    const principal = rotinas.find((rotina) => rotina.rotina === 'listar') || rotinas[0];
    const href = `modulo.html?recurso=${encodeURIComponent(principal.recurso)}&rotina=${encodeURIComponent(principal.rotina)}`;
    const ativo = params.get('recurso') === principal.recurso;

    return criarLink(href, item.rotulo, ativo, item.icone);
  }

  function renderizar() {
    const html = MENU_SECTIONS.map((secao) => {
      const itens = secao.itens.filter(podeAcessar);
      if (itens.length === 0) {
        return '';
      }

      const conteudo = itens
        .map((item) => {
          if (item.href) {
            return criarLink(item.href, item.rotulo, linkAtivo(item.href), item.icone);
          }
          return criarGrupo(item);
        })
        .join('');

      if (!conteudo.trim()) {
        return '';
      }

      return `
        <div class="drawer-section">
          <p class="drawer-section-title">${secao.titulo}</p>
          ${conteudo}
        </div>
      `;
    }).join('');

    nav.innerHTML = html || '<p class="drawer-vazio">Nenhuma rotina disponível para o seu perfil.</p>';
  }

  function abrirMenu() {
    ultimoFoco = document.activeElement;
    overlay.hidden = false;
    document.body.classList.add('menu-aberto');
    drawer.classList.add('aberto');
    drawer.setAttribute('aria-hidden', 'false');
    toggle.setAttribute('aria-expanded', 'true');
    window.requestAnimationFrame(() => overlay.classList.add('visivel'));
    window.setTimeout(() => {
      const primeiroItem = drawer.querySelector('.drawer-link');
      if (primeiroItem) {
        primeiroItem.focus();
      }
    }, 60);
  }

  function fecharMenu() {
    overlay.classList.remove('visivel');
    drawer.classList.remove('aberto');
    drawer.setAttribute('aria-hidden', 'true');
    toggle.setAttribute('aria-expanded', 'false');
    document.body.classList.remove('menu-aberto');
    window.setTimeout(() => {
      overlay.hidden = true;
    }, 280);
    if (ultimoFoco) {
      ultimoFoco.focus();
    }
  }

  toggle.addEventListener('click', () => {
    if (drawer.classList.contains('aberto')) {
      fecharMenu();
    } else {
      abrirMenu();
    }
  });

  overlay.addEventListener('click', fecharMenu);

  document.addEventListener('keydown', (event) => {
    if (event.key === 'Escape' && drawer.classList.contains('aberto')) {
      fecharMenu();
    }
  });

  if (logoutBtn) {
    logoutBtn.addEventListener('click', () => {
      Sessao.limpar();
      window.location.replace('index.html');
    });
  }

  renderizar();
});
