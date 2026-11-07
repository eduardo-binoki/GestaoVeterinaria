document.addEventListener('DOMContentLoaded', () => {
  if (Sessao.autenticado()) {
    window.location.replace('dashboard.html');
    return;
  }

  const form = document.getElementById('login-form');
  const emailInput = document.getElementById('email');
  const senhaInput = document.getElementById('senha');
  const lembrarInput = document.getElementById('lembrar');
  const alertBox = document.getElementById('login-alert');
  const btnEntrar = document.getElementById('btn-entrar');
  const toggleSenha = document.getElementById('toggle-senha');

  const campos = {
    email: document.getElementById('email-error'),
    senha: document.getElementById('senha-error')
  };

  const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  function limparErros() {
    alertBox.hidden = true;
    alertBox.textContent = '';
    emailInput.classList.remove('input-invalid');
    senhaInput.classList.remove('input-invalid');
    Object.values(campos).forEach((el) => {
      el.textContent = '';
    });
  }

  function mostrarErroCampo(nome, mensagem) {
    if (campos[nome]) {
      campos[nome].textContent = mensagem;
    }
    const input = nome === 'email' ? emailInput : senhaInput;
    input.classList.add('input-invalid');
  }

  function mostrarAlerta(mensagem) {
    alertBox.textContent = mensagem;
    alertBox.hidden = false;
  }

  function validar() {
    let valido = true;
    const email = emailInput.value.trim();
    const senha = senhaInput.value;

    if (!email) {
      mostrarErroCampo('email', 'Informe seu e-mail.');
      valido = false;
    } else if (!EMAIL_REGEX.test(email)) {
      mostrarErroCampo('email', 'Informe um e-mail válido.');
      valido = false;
    }

    if (!senha) {
      mostrarErroCampo('senha', 'Informe sua senha.');
      valido = false;
    }

    return valido;
  }

  function definirCarregando(carregando) {
    btnEntrar.disabled = carregando;
    btnEntrar.classList.toggle('loading', carregando);
    emailInput.disabled = carregando;
    senhaInput.disabled = carregando;
  }

  toggleSenha.addEventListener('click', () => {
    const visivel = senhaInput.type === 'text';
    senhaInput.type = visivel ? 'password' : 'text';
    toggleSenha.classList.toggle('ativo', !visivel);
    toggleSenha.setAttribute('aria-label', visivel ? 'Mostrar senha' : 'Ocultar senha');
  });

  [emailInput, senhaInput].forEach((input) => {
    input.addEventListener('input', () => {
      input.classList.remove('input-invalid');
      campos[input.id].textContent = '';
      alertBox.hidden = true;
    });
  });

  form.addEventListener('submit', async (event) => {
    event.preventDefault();
    limparErros();

    if (!validar()) {
      return;
    }

    definirCarregando(true);

    try {
      const resposta = await Api.login(
        emailInput.value.trim(),
        senhaInput.value
      );
      Sessao.salvar(resposta, lembrarInput.checked);
      window.location.replace('dashboard.html');
    } catch (error) {
      if (error instanceof ApiError && error.campos) {
        Object.entries(error.campos).forEach(([campo, mensagem]) => {
          if (campos[campo]) {
            mostrarErroCampo(campo, mensagem);
          }
        });
      }
      mostrarAlerta(error.message);
      definirCarregando(false);
    }
  });
});
