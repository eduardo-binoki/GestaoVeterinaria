document.addEventListener('DOMContentLoaded', async () => {
  if (!Sessao.autenticado()) {
    window.location.replace('index.html');
    return;
  }

  const ROTULOS_ROLE = {
    ADMIN: 'Administrador',
    VETERINARIO: 'Veterinário',
    RECEPCAO: 'Recepção'
  };

  const elNome = document.getElementById('cfg-nome');
  const elEmail = document.getElementById('cfg-email');
  const elRole = document.getElementById('cfg-role');
  const elStatus = document.getElementById('cfg-status');
  const elCriado = document.getElementById('cfg-criado');
  const elLembrar = document.getElementById('cfg-lembrar');
  const elFeedback = document.getElementById('cfg-feedback');
  const btnLogout = document.getElementById('cfg-logout');

  let feedbackTimer = null;

  function preencher(usuario) {
    elNome.textContent = usuario.nome;
    elEmail.textContent = usuario.email;
    elRole.textContent = ROTULOS_ROLE[usuario.role] || usuario.role;
    elStatus.textContent = usuario.ativo ? 'Ativo' : 'Inativo';
    elCriado.textContent = usuario.criadoEm
      ? new Date(usuario.criadoEm).toLocaleString('pt-BR')
      : '—';

    elLembrar.checked = Boolean(localStorage.getItem('vet.token'));

    window.dispatchEvent(
      new CustomEvent('usuario-atualizado', { detail: usuario })
    );
  }

  function sair() {
    Sessao.limpar();
    window.location.replace('index.html');
  }

  elLembrar.addEventListener('change', () => {
    const token = Sessao.getToken();
    const usuario = Sessao.getUsuario();
    if (!token || !usuario) {
      return;
    }

    Sessao.salvar({ token, usuario }, elLembrar.checked);

    elFeedback.hidden = false;
    if (feedbackTimer) {
      window.clearTimeout(feedbackTimer);
    }
    feedbackTimer = window.setTimeout(() => {
      elFeedback.hidden = true;
    }, 2500);
  });

  btnLogout.addEventListener('click', sair);

  const configTabs = Array.from(document.querySelectorAll('.config-tab'));
  const configPanels = Array.from(document.querySelectorAll('.config-panel'));

  function ativarAba(aba) {
    configTabs.forEach((tab) => {
      const ativo = tab === aba;
      tab.classList.toggle('ativo', ativo);
      tab.setAttribute('aria-selected', String(ativo));
      tab.tabIndex = ativo ? 0 : -1;
    });

    configPanels.forEach((painel) => {
      painel.hidden = painel.id !== `panel-${aba.dataset.tab}`;
    });

    if (window.history.replaceState) {
      try {
        const url = `${window.location.pathname}${window.location.search}#${aba.dataset.tab}`;
        window.history.replaceState(null, '', url);
      } catch {
        // Ignora navegadores que bloqueiam replaceState em file://.
      }
    }
  }

  configTabs.forEach((aba) => {
    aba.addEventListener('click', () => ativarAba(aba));

    aba.addEventListener('keydown', (event) => {
      if (!['ArrowLeft', 'ArrowRight', 'Home', 'End'].includes(event.key)) {
        return;
      }

      event.preventDefault();
      let indice = configTabs.indexOf(aba);

      if (event.key === 'ArrowLeft') {
        indice = (indice - 1 + configTabs.length) % configTabs.length;
      } else if (event.key === 'ArrowRight') {
        indice = (indice + 1) % configTabs.length;
      } else if (event.key === 'Home') {
        indice = 0;
      } else {
        indice = configTabs.length - 1;
      }

      const novaAba = configTabs[indice];
      ativarAba(novaAba);
      novaAba.focus();
    });
  });

  const abaInicial =
    configTabs.find((aba) => `#${aba.dataset.tab}` === window.location.hash) ||
    configTabs[0];
  if (abaInicial) {
    ativarAba(abaInicial);
  }

  const senhaForm = document.getElementById('senha-form');
  const senhaAtualInput = document.getElementById('senha-atual');
  const novaSenhaInput = document.getElementById('nova-senha');
  const confirmarSenhaInput = document.getElementById('confirmar-senha');
  const senhaAlert = document.getElementById('senha-alert');
  const senhaFeedback = document.getElementById('senha-feedback');
  const btnAlterarSenha = document.getElementById('btn-alterar-senha');

  const camposSenha = {
    senhaAtual: {
      input: senhaAtualInput,
      erro: document.getElementById('senha-atual-error')
    },
    novaSenha: {
      input: novaSenhaInput,
      erro: document.getElementById('nova-senha-error')
    },
    confirmarSenha: {
      input: confirmarSenhaInput,
      erro: document.getElementById('confirmar-senha-error')
    }
  };

  const senhaInputs = Object.values(camposSenha).map(({ input }) => input);

  function armarProtecaoAutofill() {
    senhaInputs.forEach((input) => {
      input.setAttribute('readonly', 'readonly');
    });
  }

  senhaInputs.forEach((input) => {
    const liberar = () => input.removeAttribute('readonly');
    input.addEventListener('focus', liberar);
    input.addEventListener('pointerdown', liberar);
  });

  armarProtecaoAutofill();

  function limparErrosSenha() {
    senhaAlert.hidden = true;
    senhaAlert.textContent = '';
    senhaFeedback.hidden = true;
    Object.values(camposSenha).forEach(({ input, erro }) => {
      input.classList.remove('input-invalid');
      erro.textContent = '';
    });
  }

  function mostrarErroSenha(campo, mensagem) {
    const alvo = camposSenha[campo];
    if (!alvo) {
      return;
    }
    alvo.input.classList.add('input-invalid');
    alvo.erro.textContent = mensagem;
  }

  function validarSenha() {
    let valido = true;
    const atual = senhaAtualInput.value;
    const nova = novaSenhaInput.value;
    const confirmar = confirmarSenhaInput.value;

    if (!atual) {
      mostrarErroSenha('senhaAtual', 'Informe sua senha atual.');
      valido = false;
    }

    if (!nova) {
      mostrarErroSenha('novaSenha', 'Informe a nova senha.');
      valido = false;
    } else if (nova.length < 6) {
      mostrarErroSenha('novaSenha', 'A nova senha deve ter pelo menos 6 caracteres.');
      valido = false;
    } else if (nova === atual) {
      mostrarErroSenha('novaSenha', 'A nova senha deve ser diferente da atual.');
      valido = false;
    }

    if (!confirmar) {
      mostrarErroSenha('confirmarSenha', 'Confirme a nova senha.');
      valido = false;
    } else if (confirmar !== nova) {
      mostrarErroSenha('confirmarSenha', 'As senhas não coincidem.');
      valido = false;
    }

    return valido;
  }

  function definirCarregandoSenha(carregando) {
    btnAlterarSenha.disabled = carregando;
    btnAlterarSenha.classList.toggle('loading', carregando);
    Object.values(camposSenha).forEach(({ input }) => {
      input.disabled = carregando;
    });
  }

  document.querySelectorAll('[data-toggle-senha]').forEach((botao) => {
    const input = document.getElementById(botao.dataset.toggleSenha);
    if (!input) {
      return;
    }

    botao.addEventListener('click', () => {
      const revelada = input.classList.toggle('revelada');
      botao.classList.toggle('ativo', revelada);
      botao.setAttribute('aria-label', revelada ? 'Ocultar senha' : 'Mostrar senha');
    });
  });

  Object.values(camposSenha).forEach(({ input, erro }) => {
    input.addEventListener('input', () => {
      input.classList.remove('input-invalid');
      erro.textContent = '';
      senhaAlert.hidden = true;
      senhaFeedback.hidden = true;
    });
  });

  senhaForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    limparErrosSenha();

    if (!validarSenha()) {
      return;
    }

    definirCarregandoSenha(true);

    try {
      await Api.alterarSenha(senhaAtualInput.value, novaSenhaInput.value);
      senhaForm.reset();
      if (!senhaInputs.includes(document.activeElement)) {
        armarProtecaoAutofill();
      }
      senhaFeedback.textContent = 'Senha alterada com sucesso.';
      senhaFeedback.hidden = false;
    } catch (error) {
      if (error instanceof ApiError && error.campos) {
        Object.entries(error.campos).forEach(([campo, mensagem]) => {
          if (camposSenha[campo]) {
            mostrarErroSenha(campo, mensagem);
          }
        });
      }
      senhaAlert.textContent = error.message;
      senhaAlert.hidden = false;
    } finally {
      definirCarregandoSenha(false);
    }
  });

  const emCache = Sessao.getUsuario();
  if (emCache) {
    preencher(emCache);
  }

  try {
    const usuario = await Api.me();
    Sessao.salvar(
      { token: Sessao.getToken(), usuario },
      Boolean(localStorage.getItem('vet.token'))
    );
    preencher(usuario);
  } catch (error) {
    if (error instanceof ApiError && (error.status === 401 || error.status === 403)) {
      sair();
    }
  }
});
