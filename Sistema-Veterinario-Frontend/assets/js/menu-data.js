const MENU_ROLES = Object.freeze({
  TODOS: Object.freeze(['ADMIN', 'VETERINARIO', 'RECEPCAO']),
  ADMIN: Object.freeze(['ADMIN']),
  ADMIN_VET: Object.freeze(['ADMIN', 'VETERINARIO']),
  ADMIN_RECEPCAO: Object.freeze(['ADMIN', 'RECEPCAO'])
});

const MENU_SECTIONS = Object.freeze([
  {
    titulo: 'Geral',
    itens: [
      {
        rotulo: 'Painel',
        href: 'dashboard.html',
        icone: 'home',
        roles: MENU_ROLES.TODOS
      }
    ]
  },
  {
    titulo: 'Cadastros',
    itens: [
      {
        rotulo: 'Tutores',
        icone: 'tutor',
        roles: MENU_ROLES.TODOS,
        rotinas: [
          { rotulo: 'Listar tutores', recurso: 'tutores', rotina: 'listar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Novo tutor', recurso: 'tutores', rotina: 'novo', roles: MENU_ROLES.TODOS },
          { rotulo: 'Buscar por ID', recurso: 'tutores', rotina: 'buscar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Editar tutor', recurso: 'tutores', rotina: 'editar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Desativar tutor', recurso: 'tutores', rotina: 'desativar', roles: MENU_ROLES.TODOS }
        ]
      },
      {
        rotulo: 'Pets',
        icone: 'pet',
        roles: MENU_ROLES.TODOS,
        rotinas: [
          { rotulo: 'Listar pets', recurso: 'pets', rotina: 'listar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Novo pet', recurso: 'pets', rotina: 'novo', roles: MENU_ROLES.TODOS },
          { rotulo: 'Buscar por ID', recurso: 'pets', rotina: 'buscar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Editar pet', recurso: 'pets', rotina: 'editar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Desativar pet', recurso: 'pets', rotina: 'desativar', roles: MENU_ROLES.TODOS }
        ]
      },
      {
        rotulo: 'Veterinários',
        icone: 'vet',
        roles: MENU_ROLES.TODOS,
        rotinas: [
          { rotulo: 'Listar veterinários', recurso: 'veterinarios', rotina: 'listar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Novo veterinário', recurso: 'veterinarios', rotina: 'novo', roles: MENU_ROLES.ADMIN },
          { rotulo: 'Buscar por ID', recurso: 'veterinarios', rotina: 'buscar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Editar veterinário', recurso: 'veterinarios', rotina: 'editar', roles: MENU_ROLES.ADMIN },
          { rotulo: 'Desativar veterinário', recurso: 'veterinarios', rotina: 'desativar', roles: MENU_ROLES.ADMIN }
        ]
      }
    ]
  },
  {
    titulo: 'Atendimentos',
    itens: [
      {
        rotulo: 'Consultas',
        icone: 'consulta',
        roles: MENU_ROLES.TODOS,
        rotinas: [
          { rotulo: 'Agenda de consultas', recurso: 'consultas', rotina: 'agenda', roles: MENU_ROLES.TODOS },
          { rotulo: 'Listar consultas', recurso: 'consultas', rotina: 'listar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Nova consulta', recurso: 'consultas', rotina: 'novo', roles: MENU_ROLES.TODOS },
          { rotulo: 'Buscar por ID', recurso: 'consultas', rotina: 'buscar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Editar consulta', recurso: 'consultas', rotina: 'editar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Cancelar consulta', recurso: 'consultas', rotina: 'cancelar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Excluir consulta', recurso: 'consultas', rotina: 'excluir', roles: MENU_ROLES.ADMIN }
        ]
      },
      {
        rotulo: 'Vacinas',
        icone: 'vacina',
        roles: MENU_ROLES.TODOS,
        rotinas: [
          { rotulo: 'Listar vacinas', recurso: 'vacinas', rotina: 'listar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Próximas doses', recurso: 'vacinas', rotina: 'proximas-doses', roles: MENU_ROLES.TODOS },
          { rotulo: 'Nova vacina', recurso: 'vacinas', rotina: 'novo', roles: MENU_ROLES.TODOS },
          { rotulo: 'Buscar por ID', recurso: 'vacinas', rotina: 'buscar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Editar vacina', recurso: 'vacinas', rotina: 'editar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Excluir vacina', recurso: 'vacinas', rotina: 'excluir', roles: MENU_ROLES.ADMIN_VET }
        ]
      },
      {
        rotulo: 'Prontuários',
        icone: 'prontuario',
        roles: MENU_ROLES.ADMIN_VET,
        rotinas: [
          { rotulo: 'Listar prontuários', recurso: 'prontuarios', rotina: 'listar', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Novo prontuário', recurso: 'prontuarios', rotina: 'novo', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Buscar por ID', recurso: 'prontuarios', rotina: 'buscar', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Editar prontuário', recurso: 'prontuarios', rotina: 'editar', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Excluir prontuário', recurso: 'prontuarios', rotina: 'excluir', roles: MENU_ROLES.ADMIN }
        ]
      },
      {
        rotulo: 'Exames',
        icone: 'exame',
        roles: MENU_ROLES.TODOS,
        rotinas: [
          { rotulo: 'Listar exames', recurso: 'exames', rotina: 'listar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Novo exame', recurso: 'exames', rotina: 'novo', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Buscar por ID', recurso: 'exames', rotina: 'buscar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Editar exame', recurso: 'exames', rotina: 'editar', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Excluir exame', recurso: 'exames', rotina: 'excluir', roles: MENU_ROLES.ADMIN }
        ]
      },
      {
        rotulo: 'Prescrições',
        icone: 'prescricao',
        roles: MENU_ROLES.TODOS,
        rotinas: [
          { rotulo: 'Listar prescrições', recurso: 'prescricoes', rotina: 'listar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Nova prescrição', recurso: 'prescricoes', rotina: 'novo', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Buscar por ID', recurso: 'prescricoes', rotina: 'buscar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Editar prescrição', recurso: 'prescricoes', rotina: 'editar', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Excluir prescrição', recurso: 'prescricoes', rotina: 'excluir', roles: MENU_ROLES.ADMIN }
        ]
      },
      {
        rotulo: 'Internações',
        icone: 'internacao',
        roles: MENU_ROLES.TODOS,
        rotinas: [
          { rotulo: 'Listar internações', recurso: 'internacoes', rotina: 'listar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Nova internação', recurso: 'internacoes', rotina: 'novo', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Buscar por ID', recurso: 'internacoes', rotina: 'buscar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Editar internação', recurso: 'internacoes', rotina: 'editar', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Evoluções', recurso: 'internacoes', rotina: 'evolucoes', roles: MENU_ROLES.TODOS },
          { rotulo: 'Dar alta', recurso: 'internacoes', rotina: 'alta', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Excluir internação', recurso: 'internacoes', rotina: 'excluir', roles: MENU_ROLES.ADMIN }
        ]
      }
    ]
  },
  {
    titulo: 'Estoque',
    itens: [
      {
        rotulo: 'Produtos',
        icone: 'produto',
        roles: MENU_ROLES.TODOS,
        rotinas: [
          { rotulo: 'Listar produtos', recurso: 'produtos', rotina: 'listar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Estoque baixo', recurso: 'produtos', rotina: 'estoque-baixo', roles: MENU_ROLES.TODOS },
          { rotulo: 'Novo produto', recurso: 'produtos', rotina: 'novo', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Buscar por ID', recurso: 'produtos', rotina: 'buscar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Editar produto', recurso: 'produtos', rotina: 'editar', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Desativar produto', recurso: 'produtos', rotina: 'desativar', roles: MENU_ROLES.ADMIN },
          { rotulo: 'Movimentações do produto', recurso: 'produtos', rotina: 'movimentacoes', roles: MENU_ROLES.TODOS },
          { rotulo: 'Registrar movimentação', recurso: 'produtos', rotina: 'movimentar', roles: MENU_ROLES.ADMIN_VET }
        ]
      },
      {
        rotulo: 'Movimentações',
        icone: 'movimentacao',
        roles: MENU_ROLES.TODOS,
        rotinas: [
          { rotulo: 'Listar movimentações', recurso: 'movimentacoes', rotina: 'listar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Buscar por ID', recurso: 'movimentacoes', rotina: 'buscar', roles: MENU_ROLES.TODOS },
          { rotulo: 'Registrar movimentação', recurso: 'movimentacoes', rotina: 'novo', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Editar movimentação', recurso: 'movimentacoes', rotina: 'editar', roles: MENU_ROLES.ADMIN_VET },
          { rotulo: 'Excluir movimentação', recurso: 'movimentacoes', rotina: 'excluir', roles: MENU_ROLES.ADMIN_VET }
        ]
      }
    ]
  },
  {
    titulo: 'Financeiro',
    itens: [
      {
        rotulo: 'Contas financeiras',
        icone: 'financeiro',
        roles: MENU_ROLES.ADMIN_RECEPCAO,
        rotinas: [
          { rotulo: 'Listar contas', recurso: 'financeiro-contas', rotina: 'listar', roles: MENU_ROLES.ADMIN_RECEPCAO },
          { rotulo: 'Nova conta', recurso: 'financeiro-contas', rotina: 'novo', roles: MENU_ROLES.ADMIN_RECEPCAO },
          { rotulo: 'Buscar por ID', recurso: 'financeiro-contas', rotina: 'buscar', roles: MENU_ROLES.ADMIN_RECEPCAO },
          { rotulo: 'Editar conta', recurso: 'financeiro-contas', rotina: 'editar', roles: MENU_ROLES.ADMIN_RECEPCAO },
          { rotulo: 'Cancelar conta', recurso: 'financeiro-contas', rotina: 'cancelar-conta', roles: MENU_ROLES.ADMIN_RECEPCAO },
          { rotulo: 'Pagamentos da conta', recurso: 'financeiro-contas', rotina: 'pagamentos', roles: MENU_ROLES.ADMIN_RECEPCAO },
          { rotulo: 'Registrar pagamento', recurso: 'financeiro-contas', rotina: 'registrar-pagamento', roles: MENU_ROLES.ADMIN_RECEPCAO },
          { rotulo: 'Excluir conta', recurso: 'financeiro-contas', rotina: 'excluir', roles: MENU_ROLES.ADMIN }
        ]
      },
      {
        rotulo: 'Categorias financeiras',
        icone: 'financeiro',
        roles: MENU_ROLES.ADMIN_RECEPCAO,
        rotinas: [
          { rotulo: 'Listar categorias', recurso: 'financeiro-categorias', rotina: 'listar', roles: MENU_ROLES.ADMIN_RECEPCAO },
          { rotulo: 'Nova categoria', recurso: 'financeiro-categorias', rotina: 'novo', roles: MENU_ROLES.ADMIN }
        ]
      }
    ]
  },
  {
    titulo: 'Relatórios',
    itens: [
      {
        rotulo: 'Relatórios',
        icone: 'relatorio',
        roles: MENU_ROLES.TODOS,
        rotinas: [
          { rotulo: 'Relatórios CSV', recurso: 'relatorios', rotina: 'relatorios', roles: MENU_ROLES.TODOS }
        ]
      }
    ]
  },
  {
    titulo: 'Administração',
    itens: [
      {
        rotulo: 'Usuários',
        icone: 'usuario',
        roles: MENU_ROLES.ADMIN,
        rotinas: [
          { rotulo: 'Listar usuários', recurso: 'usuarios', rotina: 'listar', roles: MENU_ROLES.ADMIN },
          { rotulo: 'Novo usuário', recurso: 'usuarios', rotina: 'novo', roles: MENU_ROLES.ADMIN },
          { rotulo: 'Buscar por ID', recurso: 'usuarios', rotina: 'buscar', roles: MENU_ROLES.ADMIN },
          { rotulo: 'Editar usuário', recurso: 'usuarios', rotina: 'editar', roles: MENU_ROLES.ADMIN },
          { rotulo: 'Desativar usuário', recurso: 'usuarios', rotina: 'desativar', roles: MENU_ROLES.ADMIN }
        ]
      }
    ]
  },
  {
    titulo: 'Sistema',
    itens: [
      {
        rotulo: 'Configurações',
        href: 'configuracoes.html',
        icone: 'config',
        roles: MENU_ROLES.TODOS
      }
    ]
  }
]);
