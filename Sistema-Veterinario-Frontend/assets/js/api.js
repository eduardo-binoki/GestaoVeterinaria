class ApiError extends Error {
  constructor(message, status, campos) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
    this.campos = campos || null;
  }
}

const Api = (() => {
  async function request(path, { method = 'GET', body, auth = true } = {}) {
    const headers = { 'Content-Type': 'application/json' };

    if (auth) {
      const token = Sessao.getToken();
      if (token) {
        headers.Authorization = `Bearer ${token}`;
      }
    }

    let response;
    try {
      response = await fetch(`${API_CONFIG.baseUrl}${path}`, {
        method,
        headers,
        body: body ? JSON.stringify(body) : undefined
      });
    } catch {
      throw new ApiError(
        'Não foi possível conectar ao servidor. Verifique se o backend está rodando.',
        0
      );
    }

    if (response.status === 204) {
      if (method !== 'GET') {
        window.dispatchEvent(new CustomEvent('notificacoes-atualizadas'));
      }
      return null;
    }

    let data = null;
    try {
      data = await response.json();
    } catch {
      data = null;
    }

    if (!response.ok) {
      const mensagem =
        (data && data.message) ||
        'Ocorreu um erro ao processar a solicitação. Tente novamente.';
      throw new ApiError(mensagem, response.status, data ? data.campos : null);
    }

    if (method !== 'GET') {
      window.dispatchEvent(new CustomEvent('notificacoes-atualizadas'));
    }

    return data;
  }

  return {
    login(email, senha) {
      return request(API_CONFIG.endpoints.login, {
        method: 'POST',
        body: { email, senha },
        auth: false
      });
    },
    me() {
      return request(API_CONFIG.endpoints.me);
    },
    alterarSenha(senhaAtual, novaSenha) {
      return request(API_CONFIG.endpoints.alterarSenha, {
        method: 'PUT',
        body: { senhaAtual, novaSenha }
      });
    },
    request
  };
})();
