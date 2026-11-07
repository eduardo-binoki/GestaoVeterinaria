const API_CONFIG = Object.freeze({
  baseUrl: 'http://localhost:8080',
  endpoints: Object.freeze({
    login: '/api/auth/login',
    me: '/api/auth/me',
    alterarSenha: '/api/auth/senha'
  })
});

(function () {
  const TIPOS_IGNORADOS = [
    'checkbox',
    'radio',
    'hidden',
    'submit',
    'button',
    'reset',
    'file',
    'range',
    'color'
  ];

  const TIPOS_COM_AUTOFILL = [
    'text',
    'email',
    'tel',
    'search',
    'url',
    'password'
  ];

  function tipoDoCampo(campo) {
    return (campo.getAttribute('type') || 'text').toLowerCase();
  }

  function prepararCampo(campo) {
    if (!campo || campo.hasAttribute('data-autofill-decoy')) {
      return;
    }
    if (campo.dataset.autofillPreparado === '1') {
      return;
    }
    const tipo = tipoDoCampo(campo);
    if (TIPOS_IGNORADOS.includes(tipo)) {
      return;
    }
    campo.dataset.autofillPreparado = '1';
    campo.setAttribute('autocomplete', tipo === 'password' ? 'new-password' : 'off');

    if (TIPOS_COM_AUTOFILL.includes(tipo)) {
      campo.setAttribute('readonly', 'readonly');
      const liberar = () => campo.removeAttribute('readonly');
      campo.addEventListener('focus', liberar, { once: true });
      campo.addEventListener('pointerdown', liberar, { once: true });
      campo.addEventListener('keydown', liberar, { once: true });
      campo.addEventListener('beforeinput', liberar, { once: true });
      campo.addEventListener('paste', liberar, { once: true });
      campo.addEventListener('drop', liberar, { once: true });
    }
  }

  function prepararFormulario(form) {
    if (!form || form.dataset.autofillPreparado === '1') {
      return;
    }
    form.dataset.autofillPreparado = '1';
    form.setAttribute('autocomplete', 'off');
    form.setAttribute('data-form-type', 'other');

    const isca = document.createElement('input');
    isca.type = 'text';
    isca.setAttribute('autocomplete', 'nope');
    isca.setAttribute('data-autofill-decoy', '');
    isca.setAttribute('tabindex', '-1');
    isca.setAttribute('aria-hidden', 'true');
    isca.style.cssText =
      'position:absolute;left:-9999px;width:1px;height:1px;opacity:0;pointer-events:none;';
    form.appendChild(isca);

    form.addEventListener(
      'submit',
      () => {
        form.querySelectorAll('[readonly]').forEach((campo) => {
          campo.removeAttribute('readonly');
        });
      },
      true
    );

    form.querySelectorAll('input, textarea, select').forEach(prepararCampo);
  }

  function preparar(raiz) {
    const escopo = raiz && raiz.querySelectorAll ? raiz : document;
    escopo.querySelectorAll('form').forEach(prepararFormulario);
    escopo.querySelectorAll('input, textarea, select').forEach((campo) => {
      const form = campo.closest('form');
      if (form) {
        prepararFormulario(form);
      }
      prepararCampo(campo);
    });
  }

  function iniciar() {
    preparar(document);

    const observador = new MutationObserver((mutacoes) => {
      mutacoes.forEach((mutacao) => {
        mutacao.addedNodes.forEach((no) => {
          if (no.nodeType !== 1) {
            return;
          }
          if (no.matches('form')) {
            prepararFormulario(no);
          } else if (no.matches('input, textarea, select')) {
            const form = no.closest('form');
            if (form) {
              prepararFormulario(form);
            }
            prepararCampo(no);
          }
          if (no.querySelectorAll) {
            preparar(no);
          }
        });
      });
    });

    observador.observe(document.documentElement, {
      childList: true,
      subtree: true
    });
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', iniciar);
  } else {
    iniciar();
  }
})();
