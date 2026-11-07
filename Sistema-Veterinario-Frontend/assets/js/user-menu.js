document.addEventListener('DOMContentLoaded', () => {
  const wrapper = document.getElementById('user-menu');
  const toggle = document.getElementById('user-menu-toggle');
  const dropdown = document.getElementById('user-dropdown');
  if (!wrapper || !toggle || !dropdown) {
    return;
  }

  const elNome = document.getElementById('usuario-nome');
  const elEmail = document.getElementById('usuario-email');
  const elAvatar = document.getElementById('usuario-avatar');
  const dropdownNome = document.getElementById('dropdown-nome');
  const dropdownEmail = document.getElementById('dropdown-email');
  const logoutBtn = document.getElementById('user-logout');

  const usuario = Sessao.getUsuario();

  function iniciais(nome) {
    if (!nome) {
      return '--';
    }
    const partes = nome.trim().split(/\s+/);
    const primeira = partes[0] ? partes[0][0] : '';
    const ultima = partes.length > 1 ? partes[partes.length - 1][0] : '';
    return (primeira + ultima).toUpperCase();
  }

  function preencher(usuarioAtual) {
    if (!usuarioAtual) {
      return;
    }

    if (elNome) {
      elNome.textContent = usuarioAtual.nome;
    }
    if (elEmail) {
      elEmail.textContent = usuarioAtual.email;
    }
    if (elAvatar) {
      elAvatar.textContent = iniciais(usuarioAtual.nome);
    }
    if (dropdownNome) {
      dropdownNome.textContent = usuarioAtual.nome;
    }
    if (dropdownEmail) {
      dropdownEmail.textContent = usuarioAtual.email;
    }
  }

  function abrir() {
    dropdown.hidden = false;
    toggle.setAttribute('aria-expanded', 'true');
    const primeiroItem = dropdown.querySelector('.user-dropdown-item');
    if (primeiroItem) {
      primeiroItem.focus();
    }
  }

  function fechar(devolverFoco) {
    dropdown.hidden = true;
    toggle.setAttribute('aria-expanded', 'false');
    if (devolverFoco) {
      toggle.focus();
    }
  }

  toggle.addEventListener('click', () => {
    if (dropdown.hidden) {
      abrir();
    } else {
      fechar(false);
    }
  });

  toggle.addEventListener('keydown', (event) => {
    if (event.key === 'ArrowDown' && dropdown.hidden) {
      event.preventDefault();
      abrir();
    }
  });

  document.addEventListener('pointerdown', (event) => {
    if (!wrapper.contains(event.target)) {
      fechar(false);
    }
  });

  document.addEventListener('keydown', (event) => {
    if (event.key === 'Escape' && !dropdown.hidden) {
      fechar(true);
    }
  });

  dropdown.addEventListener('keydown', (event) => {
    if (event.key === 'Escape') {
      event.preventDefault();
      fechar(true);
    }
  });

  dropdown.addEventListener('click', (event) => {
    if (event.target.closest('.user-dropdown-item')) {
      fechar(false);
    }
  });

  if (logoutBtn) {
    logoutBtn.addEventListener('click', () => {
      Sessao.limpar();
      window.location.replace('index.html');
    });
  }

  preencher(usuario);

  window.addEventListener('usuario-atualizado', (event) => {
    preencher(event.detail);
  });
});
