const Sessao = (() => {
  const TOKEN_KEY = 'vet.token';
  const USER_KEY = 'vet.usuario';

  function stores() {
    return [localStorage, sessionStorage];
  }

  function ler(chave) {
    for (const store of stores()) {
      const valor = store.getItem(chave);
      if (valor) {
        return valor;
      }
    }
    return null;
  }

  function limpar() {
    for (const store of stores()) {
      store.removeItem(TOKEN_KEY);
      store.removeItem(USER_KEY);
    }
  }

  return {
    salvar(loginResponse, lembrar) {
      limpar();
      const store = lembrar ? localStorage : sessionStorage;
      store.setItem(TOKEN_KEY, loginResponse.token);
      store.setItem(USER_KEY, JSON.stringify(loginResponse.usuario));
    },
    getToken() {
      return ler(TOKEN_KEY);
    },
    getUsuario() {
      const bruto = ler(USER_KEY);
      if (!bruto) {
        return null;
      }
      try {
        return JSON.parse(bruto);
      } catch {
        limpar();
        return null;
      }
    },
    autenticado() {
      return Boolean(this.getToken());
    },
    limpar
  };
})();
