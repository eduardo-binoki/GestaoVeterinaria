document.addEventListener('DOMContentLoaded', () => {
  const input = document.getElementById('busca-rotinas');
  const lista = document.getElementById('busca-resultados');
  if (!input || !lista) {
    return;
  }

  const container = input.closest('.topbar-search');
  const usuario = Sessao.getUsuario();
  const role = usuario ? usuario.role : null;
  const LIMITE = 8;

  function podeAcessar(item) {
    return !item.roles || (role && item.roles.includes(role));
  }

  function normalizar(texto) {
    return texto
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .toLowerCase()
      .trim();
  }

  function urlRotina(rotina) {
    return `modulo.html?recurso=${encodeURIComponent(rotina.recurso)}&rotina=${encodeURIComponent(rotina.rotina)}`;
  }

  const indice = [];

  MENU_SECTIONS.forEach((secao) => {
    secao.itens.forEach((item) => {
      if (!podeAcessar(item)) {
        return;
      }

      if (item.href) {
        indice.push({
          rotulo: item.rotulo,
          contexto: secao.titulo,
          href: item.href,
          busca: normalizar(`${item.rotulo} ${secao.titulo}`)
        });
        return;
      }

      const rotinas = (item.rotinas || []).filter(podeAcessar);
      if (rotinas.length === 0) {
        return;
      }

      const principal =
        rotinas.find((rotina) => rotina.rotina === 'listar') || rotinas[0];

      indice.push({
        rotulo: item.rotulo,
        contexto: secao.titulo,
        href: urlRotina(principal),
        busca: normalizar(`${item.rotulo} ${secao.titulo} listar`)
      });

      rotinas.forEach((rotina) => {
        if (rotina.rotina === 'listar') {
          return;
        }
        indice.push({
          rotulo: rotina.rotulo,
          contexto: `${secao.titulo} · ${item.rotulo}`,
          href: urlRotina(rotina),
          busca: normalizar(
            `${rotina.rotulo} ${item.rotulo} ${secao.titulo}`
          )
        });
      });
    });
  });

  let resultados = [];
  let indiceAtivo = -1;

  function buscar(termo) {
    const tokens = normalizar(termo).split(/\s+/).filter(Boolean);
    if (tokens.length === 0) {
      return [];
    }

    return indice
      .map((item) => {
        let pontuacao = 0;

        for (const token of tokens) {
          if (!item.busca.includes(token)) {
            return null;
          }

          const palavras = item.busca.split(/\s+/);
          pontuacao += palavras.some((palavra) => palavra.startsWith(token)) ? 2 : 1;

          if (normalizar(item.rotulo).startsWith(token)) {
            pontuacao += 3;
          }
        }

        return { item, pontuacao };
      })
      .filter(Boolean)
      .sort(
        (a, b) =>
          b.pontuacao - a.pontuacao ||
          a.item.rotulo.localeCompare(b.item.rotulo, 'pt-BR')
      )
      .slice(0, LIMITE)
      .map((resultado) => resultado.item);
  }

  function fechar() {
    lista.hidden = true;
    lista.innerHTML = '';
    input.setAttribute('aria-expanded', 'false');
    input.removeAttribute('aria-activedescendant');
    resultados = [];
    indiceAtivo = -1;
  }

  function definirAtivo(novoIndice) {
    const itens = Array.from(lista.querySelectorAll('.search-item'));
    if (itens.length === 0) {
      return;
    }

    indiceAtivo = (novoIndice + itens.length) % itens.length;

    itens.forEach((elemento, indice) => {
      const ativo = indice === indiceAtivo;
      elemento.classList.toggle('ativo', ativo);
      elemento.setAttribute('aria-selected', String(ativo));
      if (ativo) {
        elemento.scrollIntoView({ block: 'nearest' });
      }
    });

    input.setAttribute('aria-activedescendant', itens[indiceAtivo].id);
  }

  function renderizar(termo) {
    resultados = buscar(termo);
    indiceAtivo = -1;
    input.removeAttribute('aria-activedescendant');

    if (resultados.length === 0) {
      lista.innerHTML = '<p class="search-vazio">Nenhuma rotina encontrada.</p>';
      lista.hidden = false;
      input.setAttribute('aria-expanded', 'true');
      return;
    }

    lista.innerHTML = resultados
      .map(
        (resultado, indice) => `
          <a
            class="search-item"
            id="busca-item-${indice}"
            href="${resultado.href}"
            data-index="${indice}"
            role="option"
            aria-selected="false"
          >
            <strong>${resultado.rotulo}</strong>
            <small>${resultado.contexto}</small>
          </a>
        `
      )
      .join('');

    lista.hidden = false;
    input.setAttribute('aria-expanded', 'true');
  }

  input.addEventListener('input', () => {
    const termo = input.value.trim();
    if (!termo) {
      fechar();
      return;
    }
    renderizar(termo);
  });

  input.addEventListener('focus', () => {
    const termo = input.value.trim();
    if (termo) {
      renderizar(termo);
    }
  });

  input.addEventListener('keydown', (event) => {
    if (event.key === 'Escape') {
      fechar();
      return;
    }

    if (lista.hidden) {
      return;
    }

    if (event.key === 'ArrowDown') {
      event.preventDefault();
      definirAtivo(indiceAtivo + 1);
      return;
    }

    if (event.key === 'ArrowUp') {
      event.preventDefault();
      definirAtivo(indiceAtivo - 1);
      return;
    }

    if (event.key === 'Enter') {
      const itens = lista.querySelectorAll('.search-item');
      if (itens.length === 0) {
        return;
      }

      event.preventDefault();
      const alvo =
        indiceAtivo >= 0 && itens[indiceAtivo] ? itens[indiceAtivo] : itens[0];
      window.location.href = alvo.getAttribute('href');
    }
  });

  document.addEventListener('pointerdown', (event) => {
    if (container && !container.contains(event.target)) {
      fechar();
    }
  });
});
