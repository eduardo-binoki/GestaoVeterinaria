document.addEventListener('DOMContentLoaded', () => {
  if (!Sessao.autenticado()) {
    window.location.replace('index.html');
    return;
  }

  const usuario = Sessao.getUsuario();

  const ROTULOS_ROLE = {
    ADMIN: 'Administrador',
    VETERINARIO: 'Veterinário',
    RECEPCAO: 'Recepção'
  };

  const ENUM_LABELS = {
    MACHO: 'Macho',
    FEMEA: 'Fêmea',
    INDEFINIDO: 'Indefinido',
    ROTINA: 'Rotina',
    RETORNO: 'Retorno',
    EMERGENCIA: 'Emergência',
    CIRURGIA: 'Cirurgia',
    VACINACAO: 'Vacinação',
    AGENDADA: 'Agendada',
    CONFIRMADA: 'Confirmada',
    EM_ATENDIMENTO: 'Em atendimento',
    REALIZADA: 'Realizada',
    CANCELADA: 'Cancelada',
    NAO_COMPARECEU: 'Não compareceu',
    SOLICITADO: 'Solicitado',
    COLETADO: 'Coletado',
    EM_ANALISE: 'Em análise',
    CONCLUIDO: 'Concluído',
    CANCELADO: 'Cancelado',
    APLICADA: 'Aplicada',
    PROGRAMADA: 'Programada',
    ATIVA: 'Ativa',
    ALTA: 'Alta',
    PENDENTE: 'Pendente',
    PARCIAL: 'Parcial',
    PAGA: 'Paga',
    RECEITA: 'Receita',
    DESPESA: 'Despesa',
    DINHEIRO: 'Dinheiro',
    PIX: 'Pix',
    CARTAO_CREDITO: 'Cartão de crédito',
    CARTAO_DEBITO: 'Cartão de débito',
    TRANSFERENCIA: 'Transferência',
    CHEQUE: 'Cheque',
    MEDICAMENTO: 'Medicamento',
    VACINA: 'Vacina',
    RACAO: 'Ração',
    HIGIENE: 'Higiene',
    ACESSORIO: 'Acessório',
    OUTRO: 'Outro',
    ENTRADA: 'Entrada',
    SAIDA: 'Saída',
    PERDA: 'Perda',
    ADMIN: 'Administrador',
    VETERINARIO: 'Veterinário',
    RECEPCAO: 'Recepção'
  };

  const PROP_LABELS = {
    id: 'ID',
    petId: 'ID do pet',
    tutorId: 'ID do tutor',
    veterinarioId: 'ID do veterinário',
    consultaId: 'ID da consulta',
    produtoId: 'ID do produto',
    petNome: 'Pet',
    tutorNome: 'Tutor',
    veterinarioNome: 'Veterinário',
    produtoNome: 'Produto',
    dataNascimento: 'Data de nascimento',
    dataAplicacao: 'Data de aplicação',
    proximaDose: 'Próxima dose',
    dataHora: 'Data e hora',
    dataRegistro: 'Data do registro',
    dataEmissao: 'Emissão',
    dataSolicitacao: 'Solicitação',
    dataColeta: 'Coleta',
    dataResultado: 'Resultado',
    dataEntrada: 'Entrada',
    dataAlta: 'Alta',
    dataPrescricao: 'Data da prescrição',
    dataPagamento: 'Data do pagamento',
    vencimento: 'Vencimento',
    criadoEm: 'Criado em',
    quantidadeEstoque: 'Estoque',
    estoqueMinimo: 'Estoque mínimo',
    estoqueBaixo: 'Estoque baixo',
    quantidade: 'Quantidade',
    motivo: 'Motivo',
    motivoAlta: 'Motivo da alta',
    responsavel: 'Responsável',
    role: 'Perfil',
    ativo: 'Situação',
    cpf: 'CPF',
    crmv: 'CRMV',
    uf: 'UF',
    preco: 'Preço',
    valor: 'Valor',
    valorPrevisto: 'Valor previsto',
    valorPago: 'Valor pago',
    saldo: 'Saldo',
    tipo: 'Tipo',
    status: 'Status',
    sexo: 'Sexo',
    raca: 'Raça',
    especie: 'Espécie',
    nome: 'Nome',
    descricao: 'Descrição',
    email: 'E-mail',
    telefone: 'Telefone',
    endereco: 'Endereço',
    cidade: 'Cidade',
    fabricante: 'Fabricante',
    lote: 'Lote',
    anamnese: 'Anamnese',
    exameFisico: 'Exame físico',
    diagnostico: 'Diagnóstico',
    prescricao: 'Prescrição',
    orientacoes: 'Orientações',
    resultado: 'Resultado',
    anexoNome: 'Anexo',
    anexoUrl: 'URL do anexo',
    categoriaId: 'Categoria',
    categoriaNome: 'Categoria',
    atrasada: 'Atrasada',
    formaPagamento: 'Forma de pagamento',
    contaDescricao: 'Conta',
    itens: 'Itens',
    sinaisVitais: 'Sinais vitais',
    medicamentos: 'Medicamentos',
    alimentacaoCuidados: 'Alimentação e cuidados',
    usuarioEmail: 'Usuário',
    medicamento: 'Medicamento',
    dosagem: 'Dosagem',
    frequencia: 'Frequência',
    duracao: 'Duração',
    viaAdministracao: 'Via de administração',
    observacoes: 'Observações'
  };

  const OPCOES = {
    sexo: [
      { value: 'MACHO', label: 'Macho' },
      { value: 'FEMEA', label: 'Fêmea' },
      { value: 'INDEFINIDO', label: 'Indefinido' }
    ],
    tipoConsulta: [
      { value: 'ROTINA', label: 'Rotina' },
      { value: 'RETORNO', label: 'Retorno' },
      { value: 'EMERGENCIA', label: 'Emergência' },
      { value: 'CIRURGIA', label: 'Cirurgia' },
      { value: 'VACINACAO', label: 'Vacinação' }
    ],
    statusConsulta: [
      { value: 'AGENDADA', label: 'Agendada' },
      { value: 'CONFIRMADA', label: 'Confirmada' },
      { value: 'EM_ATENDIMENTO', label: 'Em atendimento' },
      { value: 'REALIZADA', label: 'Realizada' },
      { value: 'CANCELADA', label: 'Cancelada' },
      { value: 'NAO_COMPARECEU', label: 'Não compareceu' }
    ],
    statusExame: [
      { value: 'SOLICITADO', label: 'Solicitado' },
      { value: 'COLETADO', label: 'Coletado' },
      { value: 'EM_ANALISE', label: 'Em análise' },
      { value: 'CONCLUIDO', label: 'Concluído' },
      { value: 'CANCELADO', label: 'Cancelado' }
    ],
    statusInternacao: [
      { value: 'ATIVA', label: 'Ativa' },
      { value: 'ALTA', label: 'Alta' },
      { value: 'CANCELADA', label: 'Cancelada' }
    ],
    tipoConta: [
      { value: 'RECEITA', label: 'Receita' },
      { value: 'DESPESA', label: 'Despesa' }
    ],
    statusConta: [
      { value: 'PENDENTE', label: 'Pendente' },
      { value: 'PARCIAL', label: 'Parcial' },
      { value: 'PAGA', label: 'Paga' },
      { value: 'CANCELADA', label: 'Cancelada' }
    ],
    formaPagamento: [
      { value: 'DINHEIRO', label: 'Dinheiro' },
      { value: 'PIX', label: 'Pix' },
      { value: 'CARTAO_CREDITO', label: 'Cartão de crédito' },
      { value: 'CARTAO_DEBITO', label: 'Cartão de débito' },
      { value: 'TRANSFERENCIA', label: 'Transferência' },
      { value: 'CHEQUE', label: 'Cheque' },
      { value: 'OUTRO', label: 'Outro' }
    ],
    categoriaProduto: [
      { value: 'MEDICAMENTO', label: 'Medicamento' },
      { value: 'VACINA', label: 'Vacina' },
      { value: 'RACAO', label: 'Ração' },
      { value: 'HIGIENE', label: 'Higiene' },
      { value: 'ACESSORIO', label: 'Acessório' },
      { value: 'OUTRO', label: 'Outro' }
    ],
    tipoMovimentacao: [
      { value: 'ENTRADA', label: 'Entrada' },
      { value: 'SAIDA', label: 'Saída' },
      { value: 'PERDA', label: 'Perda' }
    ],
    role: [
      { value: 'ADMIN', label: 'Administrador' },
      { value: 'VETERINARIO', label: 'Veterinário' },
      { value: 'RECEPCAO', label: 'Recepção' }
    ]
  };

  const LOOKUPS = {
    tutores: {
      endpoint: '/api/tutores',
      label: (tutor) => `${tutor.nome} — ${tutor.cpf}`
    },
    pets: {
      endpoint: '/api/pets',
      label: (pet) => `${pet.nome} (${pet.especie}) — Tutor: ${pet.tutorNome}`
    },
    veterinarios: {
      endpoint: '/api/veterinarios',
      label: (vet) => `${vet.nome} — ${vet.crmv}`
    },
    consultas: {
      endpoint: '/api/consultas',
      label: (consulta) =>
        `#${consulta.id} · ${consulta.petNome} · ${formatarDataHora(consulta.dataHora)}`
    },
    produtos: {
      endpoint: '/api/produtos',
      label: (produto) => `${produto.nome} — ${produto.quantidadeEstoque} em estoque`
    },
    categoriasFinanceiras: {
      endpoint: '/api/financeiro/categorias',
      label: (categoria) => `${categoria.nome} — ${rotuloEnum(categoria.tipo)}`
    }
  };

  const RECURSOS = {
    tutores: {
      api: '/api/tutores',
      singular: 'tutor',
      descricao: 'Cadastro e gestão dos tutores responsáveis pelos pets.',
      deleteRoutine: 'desativar',
      fields: [
        { name: 'nome', label: 'Nome', type: 'text', required: true, maxLength: 120 },
        { name: 'cpf', label: 'CPF', type: 'text', required: true, maxLength: 14 },
        { name: 'email', label: 'E-mail', type: 'email', required: true, maxLength: 150 },
        { name: 'telefone', label: 'Telefone', type: 'tel', required: true, maxLength: 20 },
        { name: 'endereco', label: 'Endereço', type: 'text', maxLength: 200, full: true },
        { name: 'cidade', label: 'Cidade', type: 'text', maxLength: 100 },
        { name: 'uf', label: 'UF', type: 'text', maxLength: 2, placeholder: 'SP' }
      ],
      filters: [],
      columns: [
        { key: 'id', label: 'ID' },
        { key: 'nome', label: 'Nome' },
        { key: 'cpf', label: 'CPF' },
        { key: 'email', label: 'E-mail' },
        { key: 'telefone', label: 'Telefone' },
        { key: 'cidade', label: 'Cidade' },
        { key: 'uf', label: 'UF' },
        { key: 'ativo', label: 'Situação', badge: badgeAtivo }
      ]
    },

    pets: {
      api: '/api/pets',
      singular: 'pet',
      descricao: 'Cadastro dos pacientes, com tutor, espécie, raça e observações.',
      deleteRoutine: 'desativar',
      fields: [
        { name: 'nome', label: 'Nome', type: 'text', required: true, maxLength: 120 },
        { name: 'especie', label: 'Espécie', type: 'text', required: true, maxLength: 60 },
        { name: 'raca', label: 'Raça', type: 'text', maxLength: 80 },
        {
          name: 'sexo',
          label: 'Sexo',
          type: 'select',
          options: OPCOES.sexo,
          defaultValue: 'INDEFINIDO'
        },
        { name: 'dataNascimento', label: 'Data de nascimento', type: 'date' },
        { name: 'peso', label: 'Peso (kg)', type: 'number', step: '0.001', min: '0' },
        {
          name: 'tutorId',
          label: 'Tutor',
          type: 'select',
          lookup: 'tutores',
          required: true
        },
        {
          name: 'observacoes',
          label: 'Observações',
          type: 'textarea',
          maxLength: 500,
          rows: 3,
          full: true
        }
      ],
      filters: [],
      columns: [
        { key: 'id', label: 'ID' },
        { key: 'nome', label: 'Nome' },
        { key: 'especie', label: 'Espécie' },
        { key: 'raca', label: 'Raça' },
        { key: 'sexo', label: 'Sexo', enum: true },
        { key: 'tutorNome', label: 'Tutor' },
        { key: 'ativo', label: 'Situação', badge: badgeAtivo }
      ]
    },

    veterinarios: {
      api: '/api/veterinarios',
      singular: 'veterinário',
      descricao: 'Equipe veterinária, CRMV, especialidade e contatos.',
      deleteRoutine: 'desativar',
      fields: [
        { name: 'nome', label: 'Nome', type: 'text', required: true, maxLength: 120 },
        { name: 'crmv', label: 'CRMV', type: 'text', required: true, maxLength: 30 },
        { name: 'especialidade', label: 'Especialidade', type: 'text', maxLength: 100 },
        { name: 'email', label: 'E-mail', type: 'email', maxLength: 150 },
        { name: 'telefone', label: 'Telefone', type: 'tel', maxLength: 20 }
      ],
      filters: [],
      columns: [
        { key: 'id', label: 'ID' },
        { key: 'nome', label: 'Nome' },
        { key: 'crmv', label: 'CRMV' },
        { key: 'especialidade', label: 'Especialidade' },
        { key: 'email', label: 'E-mail' },
        { key: 'telefone', label: 'Telefone' },
        { key: 'ativo', label: 'Situação', badge: badgeAtivo }
      ]
    },

    consultas: {
      api: '/api/consultas',
      singular: 'consulta',
      searchName: 'petNome',
      descricao: 'Agendamento e acompanhamento das consultas da clínica.',
      deleteRoutine: 'excluir',
      fields: [
        {
          name: 'petId',
          label: 'Pet',
          type: 'select',
          lookup: 'pets',
          required: true
        },
        {
          name: 'veterinarioId',
          label: 'Veterinário',
          type: 'select',
          lookup: 'veterinarios',
          required: true
        },
        { name: 'dataHora', label: 'Data e hora', type: 'datetime-local', required: true },
        {
          name: 'tipo',
          label: 'Tipo',
          type: 'select',
          options: OPCOES.tipoConsulta,
          required: true,
          defaultValue: 'ROTINA'
        },
        { name: 'motivo', label: 'Motivo', type: 'text', required: true, maxLength: 300 },
        {
          name: 'diagnostico',
          label: 'Diagnóstico',
          type: 'textarea',
          maxLength: 1000,
          rows: 3,
          full: true
        },
        {
          name: 'observacoes',
          label: 'Observações',
          type: 'textarea',
          maxLength: 1000,
          rows: 3,
          full: true
        },
        { name: 'valor', label: 'Valor (R$)', type: 'number', step: '0.01', min: '0' },
        {
          name: 'status',
          label: 'Status',
          type: 'select',
          options: OPCOES.statusConsulta,
          required: true,
          defaultValue: 'AGENDADA'
        }
      ],
      columns: [
        { key: 'id', label: 'ID' },
        { key: 'petNome', label: 'Pet' },
        { key: 'tutorNome', label: 'Tutor' },
        { key: 'veterinarioNome', label: 'Veterinário' },
        { key: 'dataHora', label: 'Data e hora', type: 'datetime' },
        { key: 'tipo', label: 'Tipo', enum: true },
        { key: 'status', label: 'Status', badge: badgeStatus },
        { key: 'valor', label: 'Valor', type: 'money' }
      ]
    },

    vacinas: {
      api: '/api/vacinas',
      singular: 'vacina',
      descricao: 'Registro de vacinas aplicadas e controle das próximas doses.',
      deleteRoutine: 'excluir',
      fields: [
        {
          name: 'petId',
          label: 'Pet',
          type: 'select',
          lookup: 'pets',
          required: true
        },
        {
          name: 'veterinarioId',
          label: 'Veterinário',
          type: 'select',
          lookup: 'veterinarios'
        },
        { name: 'nome', label: 'Vacina', type: 'text', required: true, maxLength: 120 },
        { name: 'fabricante', label: 'Fabricante', type: 'text', maxLength: 120 },
        { name: 'lote', label: 'Lote', type: 'text', maxLength: 60 },
        {
          name: 'dataAplicacao',
          label: 'Data de aplicação',
          type: 'date',
          required: true
        },
        { name: 'proximaDose', label: 'Próxima dose', type: 'date' },
        {
          name: 'observacoes',
          label: 'Observações',
          type: 'textarea',
          maxLength: 500,
          rows: 3,
          full: true
        }
      ],
      columns: [
        { key: 'id', label: 'ID' },
        { key: 'petNome', label: 'Pet' },
        { key: 'nome', label: 'Vacina' },
        { key: 'fabricante', label: 'Fabricante' },
        { key: 'lote', label: 'Lote' },
        { key: 'dataAplicacao', label: 'Aplicação', type: 'date' },
        { key: 'proximaDose', label: 'Próxima dose', type: 'date' }
      ]
    },

    prontuarios: {
      api: '/api/prontuarios',
      singular: 'prontuário',
      searchName: 'petNome',
      descricao: 'Histórico clínico dos pacientes atendidos.',
      deleteRoutine: 'excluir',
      fields: [
        {
          name: 'consultaId',
          label: 'Consulta',
          type: 'select',
          lookup: 'consultas',
          required: true
        },
        {
          name: 'anamnese',
          label: 'Anamnese',
          type: 'textarea',
          required: true,
          maxLength: 2000,
          rows: 4,
          full: true
        },
        {
          name: 'exameFisico',
          label: 'Exame físico',
          type: 'textarea',
          maxLength: 2000,
          rows: 3,
          full: true
        },
        {
          name: 'diagnostico',
          label: 'Diagnóstico',
          type: 'textarea',
          maxLength: 2000,
          rows: 3,
          full: true
        },
        {
          name: 'prescricao',
          label: 'Prescrição',
          type: 'textarea',
          maxLength: 2000,
          rows: 3,
          full: true
        },
        {
          name: 'observacoes',
          label: 'Observações',
          type: 'textarea',
          maxLength: 1000,
          rows: 3,
          full: true
        }
      ],
      columns: [
        { key: 'id', label: 'ID' },
        { key: 'petNome', label: 'Pet' },
        { key: 'consultaId', label: 'Consulta' },
        { key: 'dataRegistro', label: 'Registrado em', type: 'datetime' }
      ]
    },

    produtos: {
      api: '/api/produtos',
      singular: 'produto',
      descricao: 'Produtos da clínica, preços e controle de estoque.',
      deleteRoutine: 'desativar',
      fields: [
        { name: 'nome', label: 'Nome', type: 'text', required: true, maxLength: 150 },
        {
          name: 'descricao',
          label: 'Descrição',
          type: 'textarea',
          maxLength: 500,
          rows: 3,
          full: true
        },
        {
          name: 'categoria',
          label: 'Categoria',
          type: 'select',
          options: OPCOES.categoriaProduto,
          required: true,
          defaultValue: 'OUTRO'
        },
        {
          name: 'preco',
          label: 'Preço (R$)',
          type: 'number',
          required: true,
          step: '0.01',
          min: '0'
        },
        {
          name: 'quantidadeEstoque',
          label: 'Estoque atual',
          type: 'number',
          required: true,
          step: '1',
          min: '0'
        },
        {
          name: 'estoqueMinimo',
          label: 'Estoque mínimo',
          type: 'number',
          required: true,
          step: '1',
          min: '0'
        }
      ],
      filters: [],
      columns: [
        { key: 'id', label: 'ID' },
        { key: 'nome', label: 'Nome' },
        { key: 'categoria', label: 'Categoria', enum: true },
        { key: 'preco', label: 'Preço', type: 'money' },
        { key: 'quantidadeEstoque', label: 'Estoque' },
        { key: 'estoqueMinimo', label: 'Mín.' },
        { key: 'estoqueBaixo', label: 'Alerta', badge: badgeEstoque },
        { key: 'ativo', label: 'Situação', badge: badgeAtivo }
      ]
    },

    movimentacoes: {
      api: '/api/movimentacoes',
      singular: 'movimentação',
      searchName: 'produtoNome',
      descricao: 'Entradas e saídas de produtos no estoque.',
      deleteRoutine: 'excluir',
      fields: [
        {
          name: 'produtoId',
          label: 'Produto',
          type: 'select',
          lookup: 'produtos',
          required: true
        },
        {
          name: 'tipo',
          label: 'Tipo',
          type: 'select',
          options: OPCOES.tipoMovimentacao,
          required: true,
          defaultValue: 'ENTRADA'
        },
        {
          name: 'quantidade',
          label: 'Quantidade',
          type: 'number',
          required: true,
          step: '1',
          min: '1'
        },
        { name: 'motivo', label: 'Motivo', type: 'text', maxLength: 300, full: true }
      ],
      editFields: [
        {
          name: 'tipo',
          label: 'Tipo',
          type: 'select',
          options: OPCOES.tipoMovimentacao,
          required: true
        },
        {
          name: 'quantidade',
          label: 'Quantidade',
          type: 'number',
          required: true,
          step: '1',
          min: '1'
        },
        { name: 'motivo', label: 'Motivo', type: 'text', maxLength: 300, full: true }
      ],
      columns: [
        { key: 'id', label: 'ID' },
        { key: 'produtoNome', label: 'Produto' },
        { key: 'tipo', label: 'Tipo', badge: badgeMovimentacao },
        { key: 'quantidade', label: 'Quantidade' },
        { key: 'motivo', label: 'Motivo' },
        { key: 'responsavel', label: 'Responsável' },
        { key: 'dataHora', label: 'Data e hora', type: 'datetime' }
      ]
    },

    exames: {
      api: '/api/exames',
      singular: 'exame',
      searchName: 'petNome',
      descricao: 'Solicitações, coletas e resultados dos exames laboratoriais.',
      deleteRoutine: 'excluir',
      fields: [
        { name: 'petId', label: 'Pet', type: 'select', lookup: 'pets', required: true },
        { name: 'consultaId', label: 'Consulta', type: 'select', lookup: 'consultas' },
        { name: 'veterinarioId', label: 'Veterinário', type: 'select', lookup: 'veterinarios' },
        { name: 'tipo', label: 'Tipo do exame', type: 'text', required: true, maxLength: 120 },
        { name: 'dataSolicitacao', label: 'Data da solicitação', type: 'date' },
        { name: 'dataColeta', label: 'Data da coleta', type: 'date' },
        { name: 'dataResultado', label: 'Data do resultado', type: 'date' },
        {
          name: 'status',
          label: 'Status',
          type: 'select',
          options: OPCOES.statusExame,
          required: true,
          defaultValue: 'SOLICITADO'
        },
        {
          name: 'resultado',
          label: 'Resultado',
          type: 'textarea',
          maxLength: 3000,
          rows: 4,
          full: true
        },
        {
          name: 'observacoes',
          label: 'Observações',
          type: 'textarea',
          maxLength: 1000,
          rows: 3,
          full: true
        },
        { name: 'anexoNome', label: 'Nome do anexo', type: 'text', maxLength: 150 },
        { name: 'anexoUrl', label: 'URL do anexo', type: 'url', maxLength: 500 }
      ],
      filters: [
        {
          name: 'status',
          label: 'Status',
          type: 'select',
          options: OPCOES.statusExame,
          placeholder: 'Todos'
        },
        {
          name: 'petId',
          label: 'Pet',
          type: 'select',
          lookup: 'pets',
          placeholder: 'Todos os pets'
        }
      ],
      columns: [
        { key: 'id', label: 'ID' },
        { key: 'petNome', label: 'Pet' },
        { key: 'tutorNome', label: 'Tutor' },
        { key: 'tipo', label: 'Tipo' },
        { key: 'veterinarioNome', label: 'Veterinário' },
        { key: 'dataSolicitacao', label: 'Solicitação', type: 'date' },
        { key: 'status', label: 'Status', badge: badgeStatus }
      ]
    },

    prescricoes: {
      api: '/api/prescricoes',
      singular: 'prescrição',
      searchName: 'petNome',
      descricao: 'Receitas e orientações de medicamentos para os pacientes.',
      deleteRoutine: 'excluir',
      fields: [],
      filters: [],
      columns: [
        { key: 'id', label: 'ID' },
        { key: 'petNome', label: 'Pet' },
        { key: 'veterinarioNome', label: 'Veterinário' },
        { key: 'dataPrescricao', label: 'Data', type: 'datetime' },
        {
          key: 'itens',
          label: 'Itens',
          badge: (valor) => ({
            label: `${Array.isArray(valor) ? valor.length : 0} item(ns)`,
            tone: 'neutro'
          })
        },
        { key: 'ativo', label: 'Situação', badge: badgeAtivo }
      ]
    },

    internacoes: {
      api: '/api/internacoes',
      singular: 'internação',
      searchName: 'petNome',
      descricao: 'Acompanhamento de pacientes internados e evolução clínica.',
      deleteRoutine: 'excluir',
      fields: [
        { name: 'petId', label: 'Pet', type: 'select', lookup: 'pets', required: true },
        {
          name: 'veterinarioId',
          label: 'Veterinário',
          type: 'select',
          lookup: 'veterinarios',
          required: true
        },
        { name: 'motivo', label: 'Motivo', type: 'text', required: true, maxLength: 300 },
        { name: 'dataEntrada', label: 'Data de entrada', type: 'datetime-local' },
        {
          name: 'observacoes',
          label: 'Observações',
          type: 'textarea',
          maxLength: 2000,
          rows: 4,
          full: true
        }
      ],
      filters: [
        {
          name: 'status',
          label: 'Status',
          type: 'select',
          options: OPCOES.statusInternacao,
          placeholder: 'Todas'
        }
      ],
      columns: [
        { key: 'id', label: 'ID' },
        { key: 'petNome', label: 'Pet' },
        { key: 'tutorNome', label: 'Tutor' },
        { key: 'veterinarioNome', label: 'Veterinário' },
        { key: 'motivo', label: 'Motivo' },
        { key: 'dataEntrada', label: 'Entrada', type: 'datetime' },
        { key: 'status', label: 'Status', badge: badgeStatus }
      ]
    },

    'financeiro-contas': {
      api: '/api/financeiro/contas',
      singular: 'conta',
      searchName: 'descricao',
      descricao: 'Contas a receber e a pagar da clínica.',
      deleteRoutine: 'excluir',
      fields: [
        { name: 'descricao', label: 'Descrição', type: 'text', required: true, maxLength: 200 },
        {
          name: 'tipo',
          label: 'Tipo',
          type: 'select',
          options: OPCOES.tipoConta,
          required: true,
          defaultValue: 'RECEITA'
        },
        {
          name: 'categoriaId',
          label: 'Categoria',
          type: 'select',
          lookup: 'categoriasFinanceiras'
        },
        {
          name: 'valorPrevisto',
          label: 'Valor previsto (R$)',
          type: 'number',
          required: true,
          step: '0.01',
          min: '0.01'
        },
        { name: 'dataEmissao', label: 'Data de emissão', type: 'date' },
        { name: 'vencimento', label: 'Vencimento', type: 'date', required: true },
        { name: 'consultaId', label: 'Consulta vinculada', type: 'select', lookup: 'consultas' },
        {
          name: 'observacoes',
          label: 'Observações',
          type: 'textarea',
          maxLength: 1000,
          rows: 3,
          full: true
        }
      ],
      filters: [
        {
          name: 'tipo',
          label: 'Tipo',
          type: 'select',
          options: OPCOES.tipoConta,
          placeholder: 'Todos'
        },
        {
          name: 'status',
          label: 'Status',
          type: 'select',
          options: OPCOES.statusConta,
          placeholder: 'Todos'
        },
        { name: 'inicio', label: 'Vencimento de', type: 'date' },
        { name: 'fim', label: 'Vencimento até', type: 'date' }
      ],
      columns: [
        { key: 'id', label: 'ID' },
        { key: 'descricao', label: 'Descrição' },
        { key: 'tipo', label: 'Tipo', enum: true },
        { key: 'categoriaNome', label: 'Categoria' },
        { key: 'valorPrevisto', label: 'Previsto', type: 'money' },
        { key: 'valorPago', label: 'Pago', type: 'money' },
        { key: 'saldo', label: 'Saldo', type: 'money' },
        { key: 'vencimento', label: 'Vencimento', type: 'date' },
        { key: 'status', label: 'Status', badge: badgeStatus },
        {
          key: 'atrasada',
          label: 'Atraso',
          badge: (valor) => ({
            label: valor ? 'Atrasada' : 'Em dia',
            tone: valor ? 'perigo' : 'sucesso'
          })
        }
      ]
    },

    'financeiro-categorias': {
      api: '/api/financeiro/categorias',
      singular: 'categoria',
      descricao: 'Categorias para classificar receitas e despesas.',
      fields: [
        { name: 'nome', label: 'Nome', type: 'text', required: true, maxLength: 120 },
        {
          name: 'tipo',
          label: 'Tipo',
          type: 'select',
          options: OPCOES.tipoConta,
          required: true,
          defaultValue: 'RECEITA'
        }
      ],
      filters: [
        {
          name: 'tipo',
          label: 'Tipo',
          type: 'select',
          options: OPCOES.tipoConta,
          placeholder: 'Todos'
        }
      ],
      columns: [
        { key: 'id', label: 'ID' },
        { key: 'nome', label: 'Nome' },
        { key: 'tipo', label: 'Tipo', enum: true },
        { key: 'ativo', label: 'Situação', badge: badgeAtivo }
      ]
    },

    relatorios: {
      api: '/api/relatorios',
      singular: 'relatório',
      descricao: 'Exportação de relatórios operacionais e financeiros em CSV.',
      fields: [],
      filters: [],
      columns: []
    },

    usuarios: {
      api: '/api/usuarios',
      singular: 'usuário',
      descricao: 'Usuários do sistema, perfis de acesso e situação.',
      deleteRoutine: 'desativar',
      fields: [
        { name: 'nome', label: 'Nome', type: 'text', required: true, maxLength: 120 },
        { name: 'email', label: 'E-mail', type: 'email', required: true, maxLength: 150 },
        {
          name: 'senha',
          label: 'Senha',
          type: 'password',
          required: true,
          minLength: 6,
          autocomplete: 'new-password'
        },
        {
          name: 'role',
          label: 'Perfil',
          type: 'select',
          options: OPCOES.role,
          required: true
        }
      ],
      editFields: [
        { name: 'nome', label: 'Nome', type: 'text', required: true, maxLength: 120 },
        {
          name: 'role',
          label: 'Perfil',
          type: 'select',
          options: OPCOES.role,
          required: true
        },
        {
          name: 'senha',
          label: 'Nova senha',
          type: 'password',
          minLength: 6,
          autocomplete: 'new-password',
          omitIfEmpty: true,
          help: 'Deixe em branco para manter a senha atual.'
        },
        {
          name: 'ativo',
          label: 'Ativo',
          type: 'checkbox',
          defaultValue: true,
          help: 'Usuários inativos não conseguem entrar no sistema.'
        }
      ],
      filters: [],
      columns: [
        { key: 'id', label: 'ID' },
        { key: 'nome', label: 'Nome' },
        { key: 'email', label: 'E-mail' },
        { key: 'role', label: 'Perfil', enum: true },
        { key: 'ativo', label: 'Situação', badge: badgeAtivo }
      ]
    }
  };

  Object.entries(RECURSOS).forEach(([chave, definicao]) => {
    definicao.key = chave;
  });

  const ROTINAS = {
    listar: {
      metodo: 'GET',
      sufixo: '',
      descricao: 'Lista os registros cadastrados, com filtros opcionais.'
    },
    novo: {
      metodo: 'POST',
      sufixo: '',
      descricao: 'Formulário para cadastrar um novo registro.'
    },
    buscar: {
      metodo: 'GET',
      sufixo: '/{id}',
      descricao: 'Consulta um registro específico pelo identificador.'
    },
    editar: {
      metodo: 'PUT',
      sufixo: '/{id}',
      descricao: 'Atualiza os dados de um registro existente.'
    },
    desativar: {
      metodo: 'DELETE',
      sufixo: '/{id}',
      descricao: 'Desativa o registro sem removê-lo do banco de dados.'
    },
    excluir: {
      metodo: 'DELETE',
      sufixo: '/{id}',
      descricao: 'Remove definitivamente o registro do sistema.'
    },
    agenda: {
      metodo: 'GET',
      sufixo: '/agenda',
      descricao: 'Lista as consultas dentro de um intervalo de datas.'
    },
    cancelar: {
      metodo: 'PATCH',
      sufixo: '/{id}/cancelar',
      descricao: 'Altera o status da consulta para cancelada.'
    },
    'proximas-doses': {
      metodo: 'GET',
      sufixo: '/proximas-doses',
      descricao: 'Lista as vacinas com doses previstas para os próximos dias.'
    },
    'estoque-baixo': {
      metodo: 'GET',
      sufixo: '/estoque-baixo',
      descricao: 'Lista os produtos abaixo do estoque mínimo.'
    },
    movimentacoes: {
      metodo: 'GET',
      sufixo: '/{id}/movimentacoes',
      descricao: 'Lista as movimentações de estoque de um produto.'
    },
    movimentar: {
      metodo: 'POST',
      sufixo: '/{id}/movimentacoes',
      descricao: 'Registra uma entrada ou saída de estoque para o produto.'
    }
  };

  const params = new URLSearchParams(window.location.search);
  const recursoChave = params.get('recurso');
  const rotinaChave = params.get('rotina');

  let encontrada = null;
  for (const secao of MENU_SECTIONS) {
    for (const item of secao.itens) {
      const rotina = (item.rotinas || []).find(
        (r) => r.recurso === recursoChave && r.rotina === rotinaChave
      );
      if (rotina) {
        encontrada = { secao, grupo: item, rotina };
        break;
      }
    }
    if (encontrada) {
      break;
    }
  }

  const elTitulo = document.getElementById('modulo-titulo');
  const conteudo = document.getElementById('modulo-conteudo');

  const lookupCache = new Map();

  function escapeHtml(valor) {
    return String(valor === null || valor === undefined ? '' : valor)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#039;');
  }

  function formatarData(valor) {
    if (!valor) {
      return '—';
    }
    const texto = String(valor).slice(0, 10);
    const partes = texto.split('-');
    if (partes.length === 3 && partes[0].length === 4) {
      return `${partes[2]}/${partes[1]}/${partes[0]}`;
    }
    return texto;
  }

  function formatarDataHora(valor) {
    if (!valor) {
      return '—';
    }
    const data = new Date(valor);
    if (Number.isNaN(data.getTime())) {
      return String(valor);
    }
    return data.toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' });
  }

  function formatarMoeda(valor) {
    if (valor === null || valor === undefined || valor === '') {
      return '—';
    }
    const numero = Number(valor);
    if (Number.isNaN(numero)) {
      return String(valor);
    }
    return numero.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }

  function rotuloEnum(valor) {
    if (valor === true) {
      return 'Sim';
    }
    if (valor === false) {
      return 'Não';
    }
    return ENUM_LABELS[valor] || String(valor || '').replace(/_/g, ' ');
  }

  function rotuloPropriedade(chave) {
    if (PROP_LABELS[chave]) {
      return PROP_LABELS[chave];
    }
    return chave
      .replace(/([A-Z])/g, ' $1')
      .replace(/^./, (letra) => letra.toUpperCase());
  }

  function tomEnum(valor) {
    const tons = {
      AGENDADA: 'info',
      CONFIRMADA: 'info',
      EM_ATENDIMENTO: 'info',
      REALIZADA: 'sucesso',
      CANCELADA: 'perigo',
      NAO_COMPARECEU: 'aviso',
      SOLICITADO: 'info',
      COLETADO: 'aviso',
      EM_ANALISE: 'aviso',
      CONCLUIDO: 'sucesso',
      CANCELADO: 'perigo',
      APLICADA: 'sucesso',
      PROGRAMADA: 'info',
      ATIVA: 'aviso',
      ALTA: 'sucesso',
      PENDENTE: 'aviso',
      PARCIAL: 'aviso',
      PAGA: 'sucesso',
      RECEITA: 'sucesso',
      DESPESA: 'perigo',
      ENTRADA: 'sucesso',
      SAIDA: 'aviso',
      PERDA: 'perigo'
    };
    return tons[valor] || 'neutro';
  }

  function badgeAtivo(valor) {
    return valor
      ? { label: 'Ativo', tone: 'sucesso' }
      : { label: 'Inativo', tone: 'neutro' };
  }

  function badgeEstoque(valor) {
    return valor
      ? { label: 'Baixo', tone: 'perigo' }
      : { label: 'OK', tone: 'sucesso' };
  }

  function badgeStatus(valor) {
    return { label: rotuloEnum(valor), tone: tomEnum(valor) };
  }

  function badgeMovimentacao(valor) {
    return { label: rotuloEnum(valor), tone: tomEnum(valor) };
  }

  function badgeHtml(label, tone) {
    return `<span class="crud-badge ${tone || 'neutro'}">${escapeHtml(label)}</span>`;
  }

  function hrefRotina(recurso, rotina, id) {
    const base = `modulo.html?recurso=${encodeURIComponent(recurso)}&rotina=${encodeURIComponent(rotina)}`;
    return id ? `${base}&id=${encodeURIComponent(id)}` : base;
  }

  function buscarRotina(recurso, rotina) {
    for (const secao of MENU_SECTIONS) {
      for (const item of secao.itens) {
        const achada = (item.rotinas || []).find(
          (r) => r.recurso === recurso && r.rotina === rotina
        );
        if (achada) {
          return achada;
        }
      }
    }
    return null;
  }

  function podeRotina(recurso, rotina) {
    const registro = buscarRotina(recurso, rotina);
    if (!registro) {
      return false;
    }
    return !registro.roles || (usuario && registro.roles.includes(usuario.role));
  }

  function rotuloRotina(recurso, rotina, fallback) {
    const registro = buscarRotina(recurso, rotina);
    return registro ? registro.rotulo : fallback;
  }

  function lerId(valor) {
    const numero = Number(String(valor === null || valor === undefined ? '' : valor).trim());
    if (!Number.isInteger(numero) || numero <= 0) {
      return null;
    }
    return numero;
  }

  function montarQuery(valores) {
    const query = new URLSearchParams();
    Object.entries(valores).forEach(([chave, valor]) => {
      if (valor !== '' && valor !== null && valor !== undefined) {
        query.set(chave, valor);
      }
    });
    const texto = query.toString();
    return texto ? `?${texto}` : '';
  }

  function carregandoHtml(texto) {
    return `<div class="crud-loading"><span class="spinner"></span><span>${escapeHtml(texto || 'Carregando...')}</span></div>`;
  }

  function mensagemErro(erro) {
    if (erro && erro.status === 401) {
      Sessao.limpar();
      window.location.replace('index.html');
      return 'Sessão expirada. Faça login novamente.';
    }
    if (erro && erro.status === 403) {
      return 'Você não tem permissão para realizar esta operação.';
    }
    if (erro && erro.message) {
      return erro.message;
    }
    return 'Não foi possível concluir a operação. Tente novamente.';
  }

  function alertaErroHtml(erro) {
    return `<div class="alert" role="alert">${escapeHtml(mensagemErro(erro))}</div>`;
  }

  function carregarLookup(nome) {
    if (lookupCache.has(nome)) {
      return Promise.resolve(lookupCache.get(nome));
    }
    return Api.request(LOOKUPS[nome].endpoint).then((itens) => {
      lookupCache.set(nome, itens);
      return itens;
    });
  }

  function opcoesDeLookup(nome) {
    const itens = lookupCache.get(nome) || [];
    return itens.map((item) => ({
      value: item.id,
      label: LOOKUPS[nome].label(item)
    }));
  }

  function prepararLookups(campos) {
    const nomes = [
      ...new Set(campos.filter((campo) => campo.lookup).map((campo) => campo.lookup))
    ];
    return Promise.all(nomes.map(carregarLookup));
  }

  function camposDoModo(def, modo) {
    if (modo === 'editar' && def.editFields) {
      return def.editFields;
    }
    return def.fields;
  }

  function valorCampo(campo, valores) {
    let valor;
    if (valores && Object.prototype.hasOwnProperty.call(valores, campo.name)) {
      valor = valores[campo.name];
    } else {
      valor = campo.defaultValue;
    }
    if (valor === null || valor === undefined) {
      return '';
    }
    if (campo.type === 'datetime-local') {
      return String(valor).slice(0, 16);
    }
    if (campo.type === 'date') {
      return String(valor).slice(0, 10);
    }
    return String(valor);
  }

  function campoHtml(campo, valores, idPrefix) {
    const prefixo = idPrefix || 'campo';
    const id = `${prefixo}-${campo.name}`;
    const full = campo.full ? ' full' : '';
    const obrigatorio = campo.required
      ? '<span class="crud-obrigatorio">*</span>'
      : '';
    const ajuda = campo.help
      ? `<small class="crud-help">${escapeHtml(campo.help)}</small>`
      : '';

    const atributos = [];
    if (campo.required) {
      atributos.push('required');
    }
    if (campo.maxLength) {
      atributos.push(`maxlength="${campo.maxLength}"`);
    }
    if (campo.minLength) {
      atributos.push(`minlength="${campo.minLength}"`);
    }
    if (campo.min !== undefined) {
      atributos.push(`min="${campo.min}"`);
    }
    if (campo.max !== undefined) {
      atributos.push(`max="${campo.max}"`);
    }
    if (campo.step !== undefined) {
      atributos.push(`step="${campo.step}"`);
    }
    if (campo.placeholder) {
      atributos.push(`placeholder="${escapeHtml(campo.placeholder)}"`);
    }
    if (campo.autocomplete) {
      atributos.push(`autocomplete="${campo.autocomplete}"`);
    } else {
      atributos.push('autocomplete="off"');
    }

    const valor = valorCampo(campo, valores);
    const attrs = atributos.join(' ');

    if (campo.type === 'checkbox') {
      const marcado = valores
        ? Boolean(valores[campo.name])
        : campo.defaultValue === true;
      return `<div class="field${full}">
        <label class="crud-check" for="${id}">
          <input type="checkbox" id="${id}" name="${campo.name}" autocomplete="off"${marcado ? ' checked' : ''}>
          <span>
            <strong>${escapeHtml(campo.label)}</strong>
            ${campo.help ? `<small>${escapeHtml(campo.help)}</small>` : ''}
          </span>
        </label>
        <span class="field-error" data-erro="${campo.name}"></span>
      </div>`;
    }

    let controle = '';

    if (campo.type === 'textarea') {
      controle = `<textarea class="control" id="${id}" name="${campo.name}" rows="${campo.rows || 3}" ${attrs}>${escapeHtml(valor)}</textarea>`;
    } else if (campo.type === 'select') {
      const opcoes = [];
      if (campo.placeholder) {
        opcoes.push(
          `<option value="">${escapeHtml(campo.placeholder)}</option>`
        );
      } else if (!campo.required) {
        opcoes.push('<option value="">Selecione...</option>');
      }

      const lista = campo.lookup ? opcoesDeLookup(campo.lookup) : campo.options || [];
      for (const opcao of lista) {
        const selecionado =
          String(opcao.value) === String(valor) ? ' selected' : '';
        opcoes.push(
          `<option value="${escapeHtml(opcao.value)}"${selecionado}>${escapeHtml(opcao.label)}</option>`
        );
      }

      controle = `<select class="control" id="${id}" name="${campo.name}" ${attrs}>${opcoes.join('')}</select>`;
    } else {
      controle = `<input class="control" id="${id}" name="${campo.name}" type="${campo.type || 'text'}" value="${escapeHtml(valor)}" ${attrs}>`;
    }

    return `<div class="field${full}">
      <label for="${id}">${escapeHtml(campo.label)} ${obrigatorio}</label>
      ${controle}
      ${ajuda}
      <span class="field-error" data-erro="${campo.name}"></span>
    </div>`;
  }

  function formularioHtml(def, modo, valores) {
    const campos = camposDoModo(def, modo);
    const camposHtml = campos.map((campo) => campoHtml(campo, valores)).join('');
    const rotuloBotao = modo === 'editar' ? 'Salvar alterações' : 'Inserir';

    return `<form class="crud-form" novalidate autocomplete="off">
      <div class="crud-form-grid">${camposHtml}</div>
      <div class="crud-feedback" hidden></div>
      <div class="crud-form-footer">
        <button type="submit" class="btn-primary crud-submit">
          <span class="spinner"></span>
          <span class="btn-label">${rotuloBotao}</span>
        </button>
        <a class="btn-outline" href="${hrefRotina(def.key, 'listar')}">Cancelar</a>
      </div>
    </form>`;
  }

  function coletarPayload(form, campos) {
    const dados = new FormData(form);
    const payload = {};

    for (const campo of campos) {
      if (campo.type === 'checkbox') {
        payload[campo.name] = dados.has(campo.name);
        continue;
      }

      let valor = dados.get(campo.name);
      if (typeof valor === 'string' && campo.type !== 'password') {
        valor = valor.trim();
      }

      if (valor === '' || valor === null) {
        if (campo.omitIfEmpty) {
          delete payload[campo.name];
        } else {
          payload[campo.name] = null;
        }
        continue;
      }

      if (campo.type === 'number') {
        payload[campo.name] = Number(valor);
      } else {
        payload[campo.name] = valor;
      }
    }

    return payload;
  }

  function limparErros(form) {
    form.querySelectorAll('.field-error').forEach((elemento) => {
      elemento.textContent = '';
    });
    form.querySelectorAll('.control.invalido').forEach((elemento) => {
      elemento.classList.remove('invalido');
      elemento.removeAttribute('aria-invalid');
    });
  }

  function tratarErroFormulario(feedback, form, erro) {
    feedback.hidden = false;
    feedback.classList.add('erro');
    feedback.textContent = mensagemErro(erro);

    const campos = erro && erro.campos;
    if (!campos) {
      return;
    }

    Object.entries(campos).forEach(([nome, mensagem]) => {
      const erroCampo = form.querySelector(`[data-erro="${nome}"]`);
      if (erroCampo) {
        erroCampo.textContent = mensagem;
      }
      const input = form.querySelector(`[name="${nome}"]`);
      if (input) {
        input.classList.add('invalido');
        input.setAttribute('aria-invalid', 'true');
      }
    });
  }

  function mostrarSucesso(feedback, mensagem, links) {
    feedback.hidden = false;
    feedback.classList.remove('erro');

    const linksHtml = (links || [])
      .filter(Boolean)
      .map(
        (link) =>
          `<a href="${link.href}">${escapeHtml(link.label)}</a>`
      )
      .join(' · ');

    feedback.innerHTML = `<span>${escapeHtml(mensagem)}</span>${
      linksHtml
        ? `<span class="crud-feedback-links">${linksHtml}</span>`
        : ''
    }`;
  }

  function definirCarregando(botao, ativo) {
    if (!botao) {
      return;
    }
    botao.disabled = ativo;
    botao.classList.toggle('loading', ativo);
  }

  function configurarFormulario(form, def, modo, registro) {
    const feedback = form.querySelector('.crud-feedback');
    const botao = form.querySelector('.crud-submit');
    const campos = camposDoModo(def, modo);

    form.addEventListener('submit', async (evento) => {
      evento.preventDefault();
      limparErros(form);

      if (!form.reportValidity()) {
        return;
      }

      const payload = coletarPayload(form, campos);
      definirCarregando(botao, true);

      try {
        let resposta;
        if (modo === 'editar') {
          resposta = await Api.request(`${def.api}/${registro.id}`, {
            method: 'PUT',
            body: payload
          });
        } else {
          resposta = await Api.request(def.api, {
            method: 'POST',
            body: payload
          });
        }

        const mensagem =
          modo === 'editar'
            ? 'Registro atualizado com sucesso.'
            : 'Registro cadastrado com sucesso.';

        const links = [];
        if (resposta && resposta.id) {
          links.push({
            href: hrefRotina(def.key, 'buscar', resposta.id),
            label: 'Ver registro'
          });
        }
        links.push({
          href: hrefRotina(def.key, 'listar'),
          label: 'Ir para a listagem'
        });

        mostrarSucesso(feedback, mensagem, links);
      } catch (erro) {
        tratarErroFormulario(feedback, form, erro);
      } finally {
        definirCarregando(botao, false);
      }
    });
  }

  function formatarCelula(coluna, item) {
    const valor = item[coluna.key];
    if (valor === null || valor === undefined || valor === '') {
      return '—';
    }
    if (coluna.type === 'date') {
      return formatarData(valor);
    }
    if (coluna.type === 'datetime') {
      return formatarDataHora(valor);
    }
    if (coluna.type === 'money') {
      return formatarMoeda(valor);
    }
    if (coluna.enum) {
      return rotuloEnum(valor);
    }
    return String(valor);
  }

  function celulaHtml(coluna, item) {
    if (coluna.badge) {
      const resultado = coluna.badge(item[coluna.key], item);
      return badgeHtml(resultado.label, resultado.tone);
    }
    return escapeHtml(formatarCelula(coluna, item));
  }

  const ICONE_ENGRENAGEM = `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="3"></circle><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-4 0v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1 0-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 2.83-2.83l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"></path></svg>`;

  function fecharMenus() {
    if (!conteudo) {
      return;
    }

    document.querySelectorAll('.crud-menu-dropdown').forEach((menu) => {
      if (!menu.hidden) {
        menu.hidden = true;
        menu.removeAttribute('style');
      }
      if (menu._menuOrigem && menu.parentElement !== menu._menuOrigem) {
        menu._menuOrigem.appendChild(menu);
      }
    });

    conteudo.querySelectorAll('[data-menu-toggle]').forEach((botao) => {
      botao.setAttribute('aria-expanded', 'false');
    });
  }

  function posicionarMenu(botao, menu) {
    const margem = 8;
    const espaco = 6;
    const rect = botao.getBoundingClientRect();

    menu._menuOrigem = botao.parentElement;
    if (menu.parentElement !== document.body) {
      document.body.appendChild(menu);
    }

    menu.hidden = false;
    menu.style.visibility = 'hidden';

    const largura = menu.offsetWidth;
    const altura = menu.offsetHeight;

    let left = rect.left;
    let top = rect.bottom + espaco;

    if (left + largura > window.innerWidth - margem) {
      left = window.innerWidth - largura - margem;
    }
    if (left < margem) {
      left = margem;
    }
    if (top + altura > window.innerHeight - margem) {
      top = rect.top - altura - espaco;
    }
    if (top < margem) {
      top = margem;
    }

    menu.style.left = `${left}px`;
    menu.style.top = `${top}px`;
    menu.style.visibility = '';
  }

  function alternarMenu(botao) {
    const menu = botao.parentElement.querySelector('.crud-menu-dropdown');
    if (!menu) {
      return;
    }

    const aberto = !menu.hidden;
    fecharMenus();

    if (aberto) {
      return;
    }

    botao.setAttribute('aria-expanded', 'true');
    posicionarMenu(botao, menu);
  }

  conteudo.addEventListener('click', (evento) => {
    const botao = evento.target.closest('[data-menu-toggle]');
    if (botao) {
      evento.preventDefault();
      alternarMenu(botao);
    }
  });

  document.addEventListener('click', (evento) => {
    if (!evento.target.closest('.crud-menu, .crud-menu-dropdown')) {
      fecharMenus();
    }
  });

  document.addEventListener('keydown', (evento) => {
    if (evento.key === 'Escape') {
      fecharMenus();
    }
  });

  document.addEventListener('scroll', fecharMenus, true);
  window.addEventListener('resize', fecharMenus);

  function acoesLinha(def, item) {
    const acoes = [];

    if (podeRotina(def.key, 'buscar')) {
      acoes.push({
        href: hrefRotina(def.key, 'buscar', item.id),
        label: 'Visualizar'
      });
    }

    if (podeRotina(def.key, 'editar')) {
      acoes.push({
        href: hrefRotina(def.key, 'editar', item.id),
        label: 'Alterar'
      });
    }

    if (
      def.key === 'consultas' &&
      item.status !== 'CANCELADA' &&
      podeRotina('consultas', 'cancelar')
    ) {
      acoes.push({
        href: hrefRotina('consultas', 'cancelar', item.id),
        label: 'Cancelar consulta'
      });
    }

    if (def.key === 'produtos' && podeRotina('produtos', 'movimentacoes')) {
      acoes.push({
        href: hrefRotina('produtos', 'movimentacoes', item.id),
        label: 'Movimentações'
      });
    }

    if (
      def.key === 'internacoes' &&
      item.status === 'ATIVA' &&
      podeRotina('internacoes', 'alta')
    ) {
      acoes.push({
        href: hrefRotina('internacoes', 'alta', item.id),
        label: 'Dar alta'
      });
    }

    if (def.key === 'internacoes' && podeRotina('internacoes', 'evolucoes')) {
      acoes.push({
        href: hrefRotina('internacoes', 'evolucoes', item.id),
        label: 'Evoluções'
      });
    }

    if (
      def.key === 'financeiro-contas' &&
      item.status !== 'CANCELADA' &&
      item.status !== 'PAGA' &&
      podeRotina('financeiro-contas', 'registrar-pagamento')
    ) {
      acoes.push({
        href: hrefRotina('financeiro-contas', 'registrar-pagamento', item.id),
        label: 'Registrar pagamento'
      });
    }

    if (
      def.key === 'financeiro-contas' &&
      item.status !== 'PAGA' &&
      podeRotina('financeiro-contas', 'cancelar-conta')
    ) {
      acoes.push({
        href: hrefRotina('financeiro-contas', 'cancelar-conta', item.id),
        label: 'Cancelar conta'
      });
    }

    if (def.key === 'financeiro-contas' && podeRotina('financeiro-contas', 'pagamentos')) {
      acoes.push({
        href: hrefRotina('financeiro-contas', 'pagamentos', item.id),
        label: 'Pagamentos'
      });
    }

    if (def.deleteRoutine && podeRotina(def.key, def.deleteRoutine)) {
      acoes.push({
        href: hrefRotina(def.key, def.deleteRoutine, item.id),
        label: def.deleteRoutine === 'desativar' ? 'Desativar' : 'Excluir',
        perigo: true
      });
    }

    if (!acoes.length) {
      return '<span class="crud-muted">—</span>';
    }

    return `<div class="crud-menu">
      <button type="button" class="crud-menu-toggle" data-menu-toggle aria-haspopup="menu" aria-expanded="false" aria-label="Ações do registro">${ICONE_ENGRENAGEM}</button>
      <div class="crud-menu-dropdown" role="menu" hidden>
        ${acoes
          .map(
            (acao) =>
              `<a class="crud-menu-item${acao.perigo ? ' perigo' : ''}" role="menuitem" href="${acao.href}">${acao.label}</a>`
          )
          .join('')}
      </div>
    </div>`;
  }

  function montarTabela(def, itens) {
    if (!Array.isArray(itens) || itens.length === 0) {
      return '<div class="crud-empty">Nenhum registro encontrado.</div>';
    }

    const colunas = def.columns;

    return `<div class="crud-table-wrap">
      <table class="crud-table">
        <thead>
          <tr>
            <th scope="col" class="crud-col-acoes">Ações</th>
            ${colunas
              .map((coluna) => `<th scope="col">${escapeHtml(coluna.label)}</th>`)
              .join('')}
          </tr>
        </thead>
        <tbody>
          ${itens
            .map(
              (item) => `<tr>
                <td class="crud-col-acoes">
                  <div class="crud-row-actions">${acoesLinha(def, item)}</div>
                </td>
                ${colunas
                  .map(
                    (coluna) =>
                      `<td data-label="${escapeHtml(coluna.label)}">${celulaHtml(coluna, item)}</td>`
                  )
                  .join('')}
              </tr>`
            )
            .join('')}
        </tbody>
      </table>
    </div>`;
  }

  function detalheValor(chave, valor) {
    if (valor === null || valor === undefined || valor === '') {
      return '—';
    }
    if (typeof valor === 'boolean') {
      return badgeHtml(valor ? 'Sim' : 'Não', valor ? 'sucesso' : 'neutro');
    }
    if (
      chave === 'dataHora' ||
      chave === 'dataRegistro' ||
      chave === 'dataEntrada' ||
      chave === 'dataAlta' ||
      chave === 'dataPrescricao' ||
      chave === 'criadoEm'
    ) {
      return escapeHtml(formatarDataHora(valor));
    }
    if (
      chave === 'dataNascimento' ||
      chave === 'dataAplicacao' ||
      chave === 'proximaDose' ||
      chave === 'dataSolicitacao' ||
      chave === 'dataColeta' ||
      chave === 'dataResultado' ||
      chave === 'dataEmissao' ||
      chave === 'dataPagamento' ||
      chave === 'vencimento'
    ) {
      return escapeHtml(formatarData(valor));
    }
    if (
      chave === 'preco' ||
      chave === 'valor' ||
      chave === 'valorPrevisto' ||
      chave === 'valorPago' ||
      chave === 'saldo'
    ) {
      return escapeHtml(formatarMoeda(valor));
    }
    if (Array.isArray(valor)) {
      return escapeHtml(`${valor.length} item(ns)`);
    }
    if (typeof valor === 'string' && ENUM_LABELS[valor]) {
      return badgeHtml(rotuloEnum(valor), tomEnum(valor));
    }
    return escapeHtml(String(valor));
  }

  function acoesDetalhe(def, item) {
    const acoes = [
      `<a class="btn-outline" href="${hrefRotina(def.key, 'listar')}">Voltar à listagem</a>`
    ];

    if (podeRotina(def.key, 'editar')) {
      acoes.push(
        `<a class="btn-outline" href="${hrefRotina(def.key, 'editar', item.id)}">Editar</a>`
      );
    }

    if (
      def.key === 'consultas' &&
      item.status !== 'CANCELADA' &&
      podeRotina('consultas', 'cancelar')
    ) {
      acoes.push(
        `<a class="btn-outline" href="${hrefRotina('consultas', 'cancelar', item.id)}">Cancelar consulta</a>`
      );
    }

    if (def.key === 'produtos' && podeRotina('produtos', 'movimentacoes')) {
      acoes.push(
        `<a class="btn-outline" href="${hrefRotina('produtos', 'movimentacoes', item.id)}">Movimentações</a>`
      );
    }

    if (
      def.key === 'internacoes' &&
      item.status === 'ATIVA' &&
      podeRotina('internacoes', 'alta')
    ) {
      acoes.push(
        `<a class="btn-outline" href="${hrefRotina('internacoes', 'alta', item.id)}">Dar alta</a>`
      );
    }

    if (def.key === 'internacoes' && podeRotina('internacoes', 'evolucoes')) {
      acoes.push(
        `<a class="btn-outline" href="${hrefRotina('internacoes', 'evolucoes', item.id)}">Evoluções</a>`
      );
    }

    if (
      def.key === 'financeiro-contas' &&
      item.status !== 'CANCELADA' &&
      item.status !== 'PAGA' &&
      podeRotina('financeiro-contas', 'registrar-pagamento')
    ) {
      acoes.push(
        `<a class="btn-outline" href="${hrefRotina('financeiro-contas', 'registrar-pagamento', item.id)}">Registrar pagamento</a>`
      );
    }

    if (
      def.key === 'financeiro-contas' &&
      item.status !== 'PAGA' &&
      podeRotina('financeiro-contas', 'cancelar-conta')
    ) {
      acoes.push(
        `<a class="btn-outline" href="${hrefRotina('financeiro-contas', 'cancelar-conta', item.id)}">Cancelar conta</a>`
      );
    }

    if (def.key === 'financeiro-contas' && podeRotina('financeiro-contas', 'pagamentos')) {
      acoes.push(
        `<a class="btn-outline" href="${hrefRotina('financeiro-contas', 'pagamentos', item.id)}">Pagamentos</a>`
      );
    }

    if (def.deleteRoutine && podeRotina(def.key, def.deleteRoutine)) {
      const rotulo = def.deleteRoutine === 'desativar' ? 'Desativar' : 'Excluir';
      acoes.push(
        `<a class="btn-outline perigo" href="${hrefRotina(def.key, def.deleteRoutine, item.id)}">${rotulo}</a>`
      );
    }

    return `<div class="crud-detail-actions">${acoes.join('')}</div>`;
  }

  function detalheHtml(def, item, opcoes) {
    const config = opcoes || {};
    const linhas = Object.entries(item)
      .filter(([chave]) => chave !== 'senha')
      .map(
        ([chave, valor]) => `<div class="crud-detail-row">
          <dt>${escapeHtml(rotuloPropriedade(chave))}</dt>
          <dd>${detalheValor(chave, valor)}</dd>
        </div>`
      )
      .join('');

    return `<div class="crud-detail-wrap">
      <dl class="crud-detail">${linhas}</dl>
      ${config.acoes === false ? '' : acoesDetalhe(def, item)}
    </div>`;
  }

  function renderizar(html) {
    fecharMenus();
    conteudo.innerHTML = html;
  }

  function renderizarCarregando(texto) {
    renderizar(carregandoHtml(texto));
  }

  function mostrarAlertaResultado(elemento, mensagem) {
    elemento.innerHTML = `<div class="alert" role="alert">${escapeHtml(mensagem)}</div>`;
  }

  function renderBuscaId(config) {
    renderizar(`<form class="crud-id-form" novalidate autocomplete="off">
      <div class="field">
        <label for="crud-id">ID do registro</label>
        <input class="control" id="crud-id" name="id" type="number" inputmode="numeric" min="1" step="1" required placeholder="Ex.: 1" autocomplete="off" value="${config.idInicial ? escapeHtml(String(config.idInicial)) : ''}">
      </div>
      <button type="submit" class="btn-primary crud-submit">
        <span class="spinner"></span>
        <span class="btn-label">${escapeHtml(config.rotulo)}</span>
      </button>
    </form>
    <div class="crud-resultado" id="crud-resultado"></div>`);

    const form = conteudo.querySelector('.crud-id-form');
    const resultado = conteudo.querySelector('#crud-resultado');

    form.addEventListener('submit', (evento) => {
      evento.preventDefault();
      const id = lerId(new FormData(form).get('id'));
      if (!id) {
        mostrarAlertaResultado(
          resultado,
          'Informe um ID válido (número inteiro maior que zero).'
        );
        return;
      }
      config.aoCarregar(id, resultado);
    });

    const idInicial = lerId(config.idInicial);
    if (idInicial) {
      config.aoCarregar(idInicial, resultado);
    }
  }

  function renderListar(def) {
    renderizarCarregando('Carregando registros...');
    const filtros = def.filters || [];
    const chaveNome = def.searchName || 'nome';

    prepararLookups(filtros)
      .then(() => {
        const filtrosHtml = filtros
          .map((filtro) =>
            campoHtml(
              Object.assign({}, filtro, {
                required: false,
                placeholder:
                  filtro.placeholder ||
                  (filtro.type === 'select' ? 'Todos' : undefined)
              }),
              null,
              'filtro'
            )
          )
          .join('');

        const buscaHtml = `<div class="field crud-search-field">
          <label for="filtro-busca">Buscar</label>
          <div class="crud-search">
            <select class="control" id="filtro-busca-tipo" aria-label="Filtrar por">
              <option value="nome">Nome</option>
              <option value="id">Código</option>
            </select>
            <input class="control" id="filtro-busca" type="text" placeholder="Nome" autocomplete="off">
            <button type="submit" class="btn-outline crud-search-btn" aria-label="Filtrar" title="Filtrar">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M22 3H2l8 9.46V19l4 2v-8.54L22 3z"/></svg>
            </button>
          </div>
        </div>`;

        const novo = podeRotina(def.key, 'novo')
          ? `<a class="btn-primary btn-compact" href="${hrefRotina(def.key, 'novo')}"><span class="btn-label">Inserir</span></a>`
          : '';

        renderizar(`<form class="crud-filters" id="crud-filtros" autocomplete="off">
            ${buscaHtml}
            ${filtrosHtml}
          </form>
        ${
          novo
            ? `<div class="crud-section-head"><div class="crud-actions">${novo}</div></div>`
            : ''
        }
        <div id="crud-lista"></div>`);

        const lista = conteudo.querySelector('#crud-lista');
        const formFiltros = conteudo.querySelector('#crud-filtros');
        const buscaTipo = formFiltros.querySelector('#filtro-busca-tipo');
        const buscaInput = formFiltros.querySelector('#filtro-busca');

        function atualizarBusca() {
          const porCodigo = buscaTipo.value === 'id';
          buscaInput.placeholder = porCodigo ? 'Código' : 'Nome';
          buscaInput.inputMode = porCodigo ? 'numeric' : 'text';
        }

        function filtrarBusca(itens) {
          const termo = buscaInput.value.trim();
          if (!termo) {
            return itens;
          }
          const porCodigo = buscaTipo.value === 'id';
          const alvo = termo.toLowerCase();
          return itens.filter((item) => {
            if (porCodigo) {
              return String(item.id) === termo;
            }
            return String(item[chaveNome] || '').toLowerCase().includes(alvo);
          });
        }

        async function carregar() {
          lista.innerHTML = carregandoHtml('Carregando registros...');
          try {
            const valores = coletarFiltros(formFiltros, filtros);
            const itens = filtrarBusca(
              await Api.request(def.api + montarQuery(valores))
            );
            lista.innerHTML = `<p class="crud-count">${itens.length} registro(s) encontrado(s)</p>${montarTabela(def, itens)}`;
          } catch (erro) {
            lista.innerHTML = alertaErroHtml(erro);
          }
        }

        buscaTipo.addEventListener('change', atualizarBusca);
        atualizarBusca();

        formFiltros.addEventListener('submit', (evento) => {
          evento.preventDefault();
          carregar();
        });

        carregar();
      })
      .catch((erro) => {
        renderizar(alertaErroHtml(erro));
      });
  }

  function coletarFiltros(form, filtros) {
    const dados = new FormData(form);
    const valores = {};
    for (const filtro of filtros) {
      let valor = dados.get(filtro.name);
      if (typeof valor === 'string') {
        valor = valor.trim();
      }
      if (valor !== '' && valor !== null) {
        valores[filtro.name] = valor;
      }
    }
    return valores;
  }

  function renderNovo(def) {
    const campos = camposDoModo(def, 'criar');
    renderizarCarregando('Preparando formulário...');

    prepararLookups(campos)
      .then(() => {
        renderizar(`${formularioHtml(def, 'criar', null)}`);

        configurarFormulario(conteudo.querySelector('form'), def, 'criar', null);
      })
      .catch((erro) => {
        renderizar(alertaErroHtml(erro));
      });
  }

  function renderBuscar(def, idInicial) {
    renderBuscaId({
      titulo: rotuloExibicaoRotina(),
      texto: 'Informe o ID para visualizar todos os dados do registro.',
      rotulo: 'Buscar',
      idInicial,
      aoCarregar: async (id, resultado) => {
        resultado.innerHTML = carregandoHtml('Buscando registro...');
        try {
          const item = await Api.request(`${def.api}/${id}`);
          resultado.innerHTML = detalheHtml(def, item);
        } catch (erro) {
          resultado.innerHTML = alertaErroHtml(erro);
        }
      }
    });
  }

  function renderEditar(def, idInicial) {
    renderBuscaId({
      titulo: rotuloExibicaoRotina(),
      texto: 'Informe o ID para carregar o formulário de edição.',
      rotulo: 'Carregar',
      idInicial,
      aoCarregar: async (id, resultado) => {
        resultado.innerHTML = carregandoHtml('Carregando registro...');
        try {
          const item = await Api.request(`${def.api}/${id}`);
          const campos = camposDoModo(def, 'editar');
          await prepararLookups(campos);
          resultado.innerHTML = formularioHtml(def, 'editar', item);
          configurarFormulario(resultado.querySelector('form'), def, 'editar', item);
        } catch (erro) {
          resultado.innerHTML = alertaErroHtml(erro);
        }
      }
    });
  }

  function renderExcluir(def, rotina, idInicial) {
    const acao = rotina === 'desativar' ? 'Desativar' : 'Excluir';
    const textoConfirmacao =
      rotina === 'desativar'
        ? 'O registro será marcado como inativo, sem ser removido do banco de dados.'
        : 'O registro será removido permanentemente do sistema. Esta ação não pode ser desfeita.';

    renderBuscaId({
      titulo: rotuloExibicaoRotina(),
      texto: `Informe o ID do registro que deseja ${acao.toLowerCase()}.`,
      rotulo: 'Carregar registro',
      idInicial,
      aoCarregar: async (id, resultado) => {
        resultado.innerHTML = carregandoHtml('Carregando registro...');
        try {
          const item = await Api.request(`${def.api}/${id}`);
          resultado.innerHTML = `${detalheHtml(def, item, { acoes: false })}
            <div class="crud-confirm">
              <h3>Confirmar ${acao.toLowerCase()}</h3>
              <p>${escapeHtml(textoConfirmacao)}</p>
              <div class="crud-confirm-actions">
                <button type="button" class="btn-danger" id="crud-confirmar">${acao}</button>
                <a class="btn-outline" href="${hrefRotina(def.key, 'buscar', id)}">Cancelar</a>
              </div>
              <div class="crud-feedback" hidden></div>
            </div>`;

          const botao = resultado.querySelector('#crud-confirmar');
          const feedback = resultado.querySelector('.crud-feedback');

          botao.addEventListener('click', async () => {
            botao.disabled = true;
            try {
              await Api.request(`${def.api}/${id}`, { method: 'DELETE' });
              mostrarSucesso(
                feedback,
                rotina === 'desativar'
                  ? 'Registro desativado com sucesso.'
                  : 'Registro excluído com sucesso.',
                [{ href: hrefRotina(def.key, 'listar'), label: 'Ir para a listagem' }]
              );
              botao.remove();
            } catch (erro) {
              feedback.hidden = false;
              feedback.classList.add('erro');
              feedback.textContent = mensagemErro(erro);
              botao.disabled = false;
            }
          });
        } catch (erro) {
          resultado.innerHTML = alertaErroHtml(erro);
        }
      }
    });
  }

  function paraInputDataHora(data) {
    const pad = (numero) => String(numero).padStart(2, '0');
    return `${data.getFullYear()}-${pad(data.getMonth() + 1)}-${pad(data.getDate())}T${pad(data.getHours())}:${pad(data.getMinutes())}`;
  }

  function renderAgenda() {
    const inicio = new Date();
    inicio.setMinutes(0, 0, 0);
    const fim = new Date(inicio);
    fim.setDate(fim.getDate() + 7);

    renderizar(`<form class="crud-filters" id="crud-agenda-form" autocomplete="off">
      <div class="field">
        <label for="agenda-inicio">Início</label>
        <input class="control" id="agenda-inicio" name="inicio" type="datetime-local" required autocomplete="off" value="${paraInputDataHora(inicio)}">
      </div>
      <div class="field">
        <label for="agenda-fim">Fim</label>
        <input class="control" id="agenda-fim" name="fim" type="datetime-local" required autocomplete="off" value="${paraInputDataHora(fim)}">
      </div>
      <div class="crud-filter-botoes">
        <button type="submit" class="btn-outline">Buscar agenda</button>
      </div>
    </form>
    <div id="crud-lista"></div>`);

    const form = conteudo.querySelector('#crud-agenda-form');
    const lista = conteudo.querySelector('#crud-lista');

    async function carregar() {
      const dados = new FormData(form);
      const inicioValor = dados.get('inicio');
      const fimValor = dados.get('fim');

      if (!inicioValor || !fimValor) {
        mostrarAlertaResultado(lista, 'Informe as datas de início e fim.');
        return;
      }

      lista.innerHTML = carregandoHtml('Carregando agenda...');
      try {
        const itens = await Api.request(
          `/api/consultas/agenda?inicio=${encodeURIComponent(inicioValor)}&fim=${encodeURIComponent(fimValor)}`
        );
        lista.innerHTML = `<p class="crud-count">${itens.length} consulta(s) no período</p>${montarTabela(RECURSOS.consultas, itens)}`;
      } catch (erro) {
        lista.innerHTML = alertaErroHtml(erro);
      }
    }

    form.addEventListener('submit', (evento) => {
      evento.preventDefault();
      carregar();
    });

    carregar();
  }

  function renderProximasDoses() {
    renderizar(`<form class="crud-filters" id="crud-doses-form" autocomplete="off">
      <div class="field">
        <label for="doses-dias">Próximos (dias)</label>
        <input class="control" id="doses-dias" name="dias" type="number" min="1" step="1" required autocomplete="off" value="30">
      </div>
      <div class="crud-filter-botoes">
        <button type="submit" class="btn-outline">Buscar</button>
      </div>
    </form>
    <div id="crud-lista"></div>`);

    const form = conteudo.querySelector('#crud-doses-form');
    const lista = conteudo.querySelector('#crud-lista');

    async function carregar() {
      const dias = Number(new FormData(form).get('dias'));
      if (!Number.isInteger(dias) || dias <= 0) {
        mostrarAlertaResultado(lista, 'Informe um número de dias válido.');
        return;
      }

      lista.innerHTML = carregandoHtml('Buscando próximas doses...');
      try {
        const itens = await Api.request(`/api/vacinas/proximas-doses?dias=${dias}`);
        lista.innerHTML = `<p class="crud-count">${itens.length} vacina(s) encontrada(s)</p>${montarTabela(RECURSOS.vacinas, itens)}`;
      } catch (erro) {
        lista.innerHTML = alertaErroHtml(erro);
      }
    }

    form.addEventListener('submit', (evento) => {
      evento.preventDefault();
      carregar();
    });

    carregar();
  }

  function renderEstoqueBaixo() {
    renderizar(`<div id="crud-lista"></div>`);

    const lista = conteudo.querySelector('#crud-lista');

    async function carregar() {
      lista.innerHTML = carregandoHtml('Carregando produtos...');
      try {
        const itens = await Api.request('/api/produtos/estoque-baixo');
        lista.innerHTML = `<p class="crud-count">${itens.length} produto(s) com estoque baixo</p>${montarTabela(RECURSOS.produtos, itens)}`;
      } catch (erro) {
        lista.innerHTML = alertaErroHtml(erro);
      }
    }

    carregar();
  }

  function renderMovimentacoesProduto(idInicial) {
    renderBuscaId({
      titulo: rotuloExibicaoRotina(),
      texto: 'Informe o ID do produto para listar as movimentações de estoque.',
      rotulo: 'Carregar',
      idInicial,
      aoCarregar: async (id, resultado) => {
        resultado.innerHTML = carregandoHtml('Carregando movimentações...');
        try {
          const produto = await Api.request(`/api/produtos/${id}`);
          const itens = await Api.request(`/api/produtos/${id}/movimentacoes`);
          const extra = podeRotina('produtos', 'movimentar')
            ? `<a class="btn-primary btn-compact" href="${hrefRotina('produtos', 'movimentar', id)}"><span class="btn-label">Registrar movimentação</span></a>`
            : '';

          resultado.innerHTML = `<div class="crud-subhead">
            <h3>${escapeHtml(produto.nome)} <span class="crud-muted">#${produto.id}</span></h3>
            <div class="crud-actions">${extra}</div>
          </div>
          <p class="crud-count">${itens.length} movimentação(ões) encontrada(s)</p>
          ${montarTabela(RECURSOS.movimentacoes, itens)}`;
        } catch (erro) {
          resultado.innerHTML = alertaErroHtml(erro);
        }
      }
    });
  }

  function renderMovimentar(idProduto) {
    const def = RECURSOS.movimentacoes;
    const campos = camposDoModo(def, 'criar');
    renderizarCarregando('Preparando formulário...');

    prepararLookups(campos)
      .then(() => {
        const valores = idProduto ? { produtoId: idProduto } : null;
        renderizar(`${formularioHtml(def, 'criar', valores)}`);

        configurarFormulario(conteudo.querySelector('form'), def, 'criar', null);
      })
      .catch((erro) => {
        renderizar(alertaErroHtml(erro));
      });
  }

  function renderCancelar(idInicial) {
    const def = RECURSOS.consultas;

    renderBuscaId({
      titulo: rotuloExibicaoRotina(),
      texto: 'Informe o ID da consulta que deseja cancelar.',
      rotulo: 'Carregar',
      idInicial,
      aoCarregar: async (id, resultado) => {
        resultado.innerHTML = carregandoHtml('Carregando consulta...');
        try {
          const consulta = await Api.request(`/api/consultas/${id}`);
          const jaCancelada = consulta.status === 'CANCELADA';

          resultado.innerHTML = `${detalheHtml(def, consulta, { acoes: false })}
            <div class="crud-confirm">
              <h3>Cancelar consulta</h3>
              <p>${
                jaCancelada
                  ? 'Esta consulta já está cancelada.'
                  : 'A consulta será marcada como cancelada. Deseja continuar?'
              }</p>
              <div class="crud-confirm-actions">
                ${
                  jaCancelada
                    ? ''
                    : '<button type="button" class="btn-danger" id="crud-confirmar">Cancelar consulta</button>'
                }
                <a class="btn-outline" href="${hrefRotina('consultas', 'buscar', id)}">Voltar</a>
              </div>
              <div class="crud-feedback" hidden></div>
            </div>`;

          const botao = resultado.querySelector('#crud-confirmar');
          if (!botao) {
            return;
          }

          const feedback = resultado.querySelector('.crud-feedback');

          botao.addEventListener('click', async () => {
            botao.disabled = true;
            try {
              await Api.request(`/api/consultas/${id}/cancelar`, { method: 'PATCH' });
              mostrarSucesso(feedback, 'Consulta cancelada com sucesso.', [
                { href: hrefRotina('consultas', 'buscar', id), label: 'Ver consulta' },
                { href: hrefRotina('consultas', 'listar'), label: 'Ir para a listagem' }
              ]);
              botao.remove();
            } catch (erro) {
              feedback.hidden = false;
              feedback.classList.add('erro');
              feedback.textContent = mensagemErro(erro);
              botao.disabled = false;
            }
          });
        } catch (erro) {
          resultado.innerHTML = alertaErroHtml(erro);
        }
      }
    });
  }

  function itemPrescricaoHtml(item, sequencia) {
    const valores = item || {};
    const prefixo = `prescricao-item-${sequencia}`;

    return `<div class="crud-item-card" data-item>
      <div class="crud-item-card-head">
        <strong>Item da prescrição</strong>
        <button type="button" class="btn-outline btn-compact" data-remove-item>Remover</button>
      </div>
      <div class="crud-form-grid">
        ${campoHtml(
          {
            name: 'medicamento',
            label: 'Medicamento',
            type: 'text',
            required: true,
            maxLength: 150
          },
          valores,
          prefixo
        )}
        ${campoHtml(
          {
            name: 'dosagem',
            label: 'Dosagem',
            type: 'text',
            required: true,
            maxLength: 100
          },
          valores,
          prefixo
        )}
        ${campoHtml({ name: 'frequencia', label: 'Frequência', type: 'text', maxLength: 100 }, valores, prefixo)}
        ${campoHtml({ name: 'duracao', label: 'Duração', type: 'text', maxLength: 100 }, valores, prefixo)}
        ${campoHtml(
          { name: 'viaAdministracao', label: 'Via de administração', type: 'text', maxLength: 100 },
          valores,
          prefixo
        )}
        ${campoHtml(
          {
            name: 'observacoes',
            label: 'Observações do item',
            type: 'textarea',
            maxLength: 500,
            rows: 2,
            full: true
          },
          valores,
          prefixo
        )}
      </div>
    </div>`;
  }

  function renderPrescricaoForm(idInicial) {
    const editar = Boolean(idInicial);
    renderizarCarregando('Preparando formulário...');

    Promise.all([
      carregarLookup('pets'),
      carregarLookup('veterinarios'),
      carregarLookup('consultas')
    ])
      .then(async () => {
        const prescricao = editar
          ? await Api.request(`/api/prescricoes/${idInicial}`)
          : null;
        const valores = prescricao || { ativo: true };
        const campos = [
          { name: 'petId', label: 'Pet', type: 'select', lookup: 'pets', required: true },
          { name: 'consultaId', label: 'Consulta', type: 'select', lookup: 'consultas' },
          { name: 'veterinarioId', label: 'Veterinário', type: 'select', lookup: 'veterinarios' },
          { name: 'dataPrescricao', label: 'Data da prescrição', type: 'datetime-local' },
          {
            name: 'orientacoes',
            label: 'Orientações',
            type: 'textarea',
            maxLength: 2000,
            rows: 3,
            full: true
          },
          {
            name: 'ativo',
            label: 'Prescrição ativa',
            type: 'checkbox',
            defaultValue: true
          }
        ];

        renderizar(`<form class="crud-form" id="prescricao-form" novalidate autocomplete="off">
          <div class="crud-form-grid">
            ${campos.map((campo) => campoHtml(campo, valores)).join('')}
          </div>
          <div class="crud-section-head">
            <div>
              <h2>Itens da prescrição</h2>
              <p>Informe ao menos um medicamento com dosagem.</p>
            </div>
            <div class="crud-actions">
              <button type="button" class="btn-outline btn-compact" id="adicionar-item">Adicionar item</button>
            </div>
          </div>
          <div id="prescricao-itens"></div>
          <div class="crud-feedback" hidden></div>
          <div class="crud-form-footer">
            <button type="submit" class="btn-primary crud-submit">
              <span class="spinner"></span>
              <span class="btn-label">${editar ? 'Salvar alterações' : 'Inserir'}</span>
            </button>
            <a class="btn-outline" href="${hrefRotina('prescricoes', 'listar')}">Cancelar</a>
          </div>
        </form>`);

        const form = conteudo.querySelector('#prescricao-form');
        const itensContainer = conteudo.querySelector('#prescricao-itens');
        let sequencia = 0;

        function adicionarItem(item) {
          itensContainer.insertAdjacentHTML(
            'beforeend',
            itemPrescricaoHtml(item, sequencia)
          );
          sequencia += 1;
        }

        const itensIniciais =
          prescricao && Array.isArray(prescricao.itens) && prescricao.itens.length
            ? prescricao.itens
            : [{}];
        itensIniciais.forEach(adicionarItem);

        itensContainer.addEventListener('click', (evento) => {
          const botao = evento.target.closest('[data-remove-item]');
          if (!botao) {
            return;
          }
          if (itensContainer.querySelectorAll('[data-item]').length <= 1) {
            return;
          }
          botao.closest('[data-item]').remove();
        });

        conteudo
          .querySelector('#adicionar-item')
          .addEventListener('click', () => adicionarItem({}));

        const feedback = form.querySelector('.crud-feedback');
        const botao = form.querySelector('.crud-submit');

        form.addEventListener('submit', async (evento) => {
          evento.preventDefault();
          limparErros(form);

          if (!form.reportValidity()) {
            return;
          }

          const obterValor = (nome) => {
            const elemento = form.querySelector(`[name="${nome}"]`);
            return elemento ? elemento.value.trim() : '';
          };

          const itens = [...itensContainer.querySelectorAll('[data-item]')].map(
            (card) => {
              const valorDe = (nome) => {
                const elemento = card.querySelector(`[name="${nome}"]`);
                return elemento ? elemento.value.trim() : '';
              };
              return {
                medicamento: valorDe('medicamento'),
                dosagem: valorDe('dosagem'),
                frequencia: valorDe('frequencia') || null,
                duracao: valorDe('duracao') || null,
                viaAdministracao: valorDe('viaAdministracao') || null,
                observacoes: valorDe('observacoes') || null
              };
            }
          );

          const payload = {
            petId: Number(obterValor('petId')),
            consultaId: obterValor('consultaId') ? Number(obterValor('consultaId')) : null,
            veterinarioId: obterValor('veterinarioId')
              ? Number(obterValor('veterinarioId'))
              : null,
            dataPrescricao: obterValor('dataPrescricao') || null,
            orientacoes: obterValor('orientacoes') || null,
            ativo: form.querySelector('[name="ativo"]').checked,
            itens
          };

          definirCarregando(botao, true);

          try {
            const resposta = editar
              ? await Api.request(`/api/prescricoes/${idInicial}`, {
                  method: 'PUT',
                  body: payload
                })
              : await Api.request('/api/prescricoes', {
                  method: 'POST',
                  body: payload
                });

            mostrarSucesso(
              feedback,
              editar
                ? 'Prescrição atualizada com sucesso.'
                : 'Prescrição cadastrada com sucesso.',
              [
                {
                  href: hrefRotina('prescricoes', 'buscar', resposta.id),
                  label: 'Ver prescrição'
                },
                { href: hrefRotina('prescricoes', 'listar'), label: 'Ir para a listagem' }
              ]
            );
          } catch (erro) {
            tratarErroFormulario(feedback, form, erro);
          } finally {
            definirCarregando(botao, false);
          }
        });
      })
      .catch((erro) => {
        renderizar(alertaErroHtml(erro));
      });
  }

  async function renderPrescricaoDetalhe(id, resultado) {
    resultado.innerHTML = carregandoHtml('Buscando prescrição...');

    try {
      const prescricao = await Api.request(`/api/prescricoes/${id}`);
      const itens = Array.isArray(prescricao.itens) ? prescricao.itens : [];
      const linhasItens = itens.length
        ? itens
            .map(
              (item) => `<tr>
                <td>${escapeHtml(item.medicamento)}</td>
                <td>${escapeHtml(item.dosagem)}</td>
                <td>${escapeHtml(item.frequencia || '—')}</td>
                <td>${escapeHtml(item.duracao || '—')}</td>
                <td>${escapeHtml(item.viaAdministracao || '—')}</td>
                <td>${escapeHtml(item.observacoes || '—')}</td>
              </tr>`
            )
            .join('')
        : '<tr><td colspan="6">Nenhum item informado.</td></tr>';

      const acoes = [
        `<a class="btn-outline" href="${hrefRotina('prescricoes', 'listar')}">Voltar à listagem</a>`
      ];
      if (podeRotina('prescricoes', 'editar')) {
        acoes.push(
          `<a class="btn-outline" href="${hrefRotina('prescricoes', 'editar', id)}">Editar</a>`
        );
      }
      if (podeRotina('prescricoes', 'excluir')) {
        acoes.push(
          `<a class="btn-outline perigo" href="${hrefRotina('prescricoes', 'excluir', id)}">Excluir</a>`
        );
      }

      resultado.innerHTML = `${detalheHtml(RECURSOS.prescricoes, prescricao, {
        acoes: false
      })}
        <h3>Itens da prescrição</h3>
        <div class="crud-table-wrap">
          <table class="crud-table">
            <thead>
              <tr>
                <th scope="col">Medicamento</th>
                <th scope="col">Dosagem</th>
                <th scope="col">Frequência</th>
                <th scope="col">Duração</th>
                <th scope="col">Via</th>
                <th scope="col">Observações</th>
              </tr>
            </thead>
            <tbody>${linhasItens}</tbody>
          </table>
        </div>
        <div class="crud-detail-actions">${acoes.join('')}</div>`;
    } catch (erro) {
      resultado.innerHTML = alertaErroHtml(erro);
    }
  }

  function renderPrescricaoBuscar(idInicial) {
    renderBuscaId({
      titulo: rotuloExibicaoRotina(),
      texto: 'Informe o ID da prescrição para visualizar os dados e os itens.',
      rotulo: 'Buscar',
      idInicial,
      aoCarregar: (id, resultado) => renderPrescricaoDetalhe(id, resultado)
    });
  }

  function renderPrescricaoEditar(idInicial) {
    if (idInicial) {
      renderPrescricaoForm(idInicial);
      return;
    }

    renderBuscaId({
      titulo: rotuloExibicaoRotina(),
      texto: 'Informe o ID da prescrição que deseja editar.',
      rotulo: 'Carregar',
      aoCarregar: (id) => renderPrescricaoForm(id)
    });
  }

  function renderInternacaoAlta(idInicial) {
    renderBuscaId({
      titulo: rotuloExibicaoRotina(),
      texto: 'Informe o ID da internação que deseja finalizar com alta.',
      rotulo: 'Carregar',
      idInicial,
      aoCarregar: async (id, resultado) => {
        resultado.innerHTML = carregandoHtml('Carregando internação...');
        try {
          const internacao = await Api.request(`/api/internacoes/${id}`);
          const ativa = internacao.status === 'ATIVA';

          resultado.innerHTML = `${detalheHtml(RECURSOS.internacoes, internacao, {
            acoes: false
          })}
            <div class="crud-confirm">
              <h3>Dar alta</h3>
              <p>${
                ativa
                  ? 'Informe o motivo da alta. A internação será encerrada.'
                  : 'Somente internações ativas podem receber alta.'
              }</p>
              ${
                ativa
                  ? `<form class="crud-form" id="alta-form" novalidate autocomplete="off">
                      <div class="crud-form-grid">
                        ${campoHtml({
                          name: 'motivoAlta',
                          label: 'Motivo da alta',
                          type: 'textarea',
                          required: true,
                          maxLength: 500,
                          rows: 3,
                          full: true
                        })}
                      </div>
                      <div class="crud-feedback" hidden></div>
                      <div class="crud-form-footer">
                        <button type="submit" class="btn-primary crud-submit">
                          <span class="spinner"></span>
                          <span class="btn-label">Confirmar alta</span>
                        </button>
                        <a class="btn-outline" href="${hrefRotina('internacoes', 'buscar', id)}">Voltar</a>
                      </div>
                    </form>`
                  : `<a class="btn-outline" href="${hrefRotina('internacoes', 'buscar', id)}">Voltar</a>`
              }
            </div>`;

          const form = resultado.querySelector('#alta-form');
          if (!form) {
            return;
          }

          const feedback = form.querySelector('.crud-feedback');
          const botao = form.querySelector('.crud-submit');

          form.addEventListener('submit', async (evento) => {
            evento.preventDefault();
            limparErros(form);
            if (!form.reportValidity()) {
              return;
            }

            const motivoAlta = form.querySelector('[name="motivoAlta"]').value.trim();
            definirCarregando(botao, true);

            try {
              await Api.request(`/api/internacoes/${id}/alta`, {
                method: 'PATCH',
                body: { motivoAlta }
              });
              mostrarSucesso(feedback, 'Alta registrada com sucesso.', [
                { href: hrefRotina('internacoes', 'buscar', id), label: 'Ver internação' },
                { href: hrefRotina('internacoes', 'listar'), label: 'Ir para a listagem' }
              ]);
              botao.remove();
            } catch (erro) {
              tratarErroFormulario(feedback, form, erro);
            } finally {
              definirCarregando(botao, false);
            }
          });
        } catch (erro) {
          resultado.innerHTML = alertaErroHtml(erro);
        }
      }
    });
  }

  async function carregarEvolucoes(id, resultado) {
    resultado.innerHTML = carregandoHtml('Carregando evoluções...');

    try {
      const [internacao, evolucoes] = await Promise.all([
        Api.request(`/api/internacoes/${id}`),
        Api.request(`/api/internacoes/${id}/evolucoes`)
      ]);
      const podeRegistrar =
        internacao.status === 'ATIVA' &&
        usuario &&
        ['ADMIN', 'VETERINARIO'].includes(usuario.role);
      const podeDarAlta =
        internacao.status === 'ATIVA' && podeRotina('internacoes', 'alta');
      const lista = Array.isArray(evolucoes) ? evolucoes : [];
      const linhas = lista.length
        ? lista
            .map(
              (evolucao) => `<tr>
                <td>${escapeHtml(formatarDataHora(evolucao.dataHora))}</td>
                <td>${escapeHtml(evolucao.descricao)}</td>
                <td>${escapeHtml(evolucao.sinaisVitais || '—')}</td>
                <td>${escapeHtml(evolucao.medicamentos || '—')}</td>
                <td>${escapeHtml(evolucao.alimentacaoCuidados || '—')}</td>
                <td>${escapeHtml(evolucao.usuarioEmail || '—')}</td>
              </tr>`
            )
            .join('')
        : '<tr><td colspan="6">Nenhuma evolução registrada.</td></tr>';

      const formulario = podeRegistrar
        ? `<form class="crud-form" id="evolucao-form" novalidate autocomplete="off">
            <div class="crud-form-grid">
              ${campoHtml({
                name: 'descricao',
                label: 'Descrição',
                type: 'textarea',
                required: true,
                maxLength: 2000,
                rows: 3,
                full: true
              })}
              ${campoHtml({
                name: 'sinaisVitais',
                label: 'Sinais vitais',
                type: 'textarea',
                maxLength: 1000,
                rows: 2,
                full: true
              })}
              ${campoHtml({
                name: 'medicamentos',
                label: 'Medicamentos',
                type: 'textarea',
                maxLength: 1000,
                rows: 2,
                full: true
              })}
              ${campoHtml({
                name: 'alimentacaoCuidados',
                label: 'Alimentação e cuidados',
                type: 'textarea',
                maxLength: 1000,
                rows: 2,
                full: true
              })}
            </div>
            <div class="crud-feedback" hidden></div>
            <div class="crud-form-footer">
              <button type="submit" class="btn-primary crud-submit">
                <span class="spinner"></span>
                <span class="btn-label">Registrar evolução</span>
              </button>
            </div>
          </form>`
        : '';

      const acoes = [
        `<a class="btn-outline" href="${hrefRotina('internacoes', 'listar')}">Voltar à listagem</a>`
      ];
      if (podeDarAlta) {
        acoes.push(
          `<a class="btn-outline" href="${hrefRotina('internacoes', 'alta', id)}">Dar alta</a>`
        );
      }

      resultado.innerHTML = `${detalheHtml(RECURSOS.internacoes, internacao, {
        acoes: false
      })}
        <div class="crud-section-head">
          <div>
            <h2>Evoluções</h2>
            <p>${lista.length} evolução(ões) registrada(s).</p>
          </div>
          <div class="crud-actions">${acoes.join('')}</div>
        </div>
        ${formulario}
        <div class="crud-table-wrap">
          <table class="crud-table">
            <thead>
              <tr>
                <th scope="col">Data</th>
                <th scope="col">Descrição</th>
                <th scope="col">Sinais vitais</th>
                <th scope="col">Medicamentos</th>
                <th scope="col">Alimentação/cuidados</th>
                <th scope="col">Usuário</th>
              </tr>
            </thead>
            <tbody>${linhas}</tbody>
          </table>
        </div>`;

      const form = resultado.querySelector('#evolucao-form');
      if (!form) {
        return;
      }

      const feedback = form.querySelector('.crud-feedback');
      const botao = form.querySelector('.crud-submit');

      form.addEventListener('submit', async (evento) => {
        evento.preventDefault();
        limparErros(form);
        if (!form.reportValidity()) {
          return;
        }

        const valorDe = (nome) => {
          const elemento = form.querySelector(`[name="${nome}"]`);
          return elemento ? elemento.value.trim() : '';
        };
        const payload = {
          descricao: valorDe('descricao'),
          sinaisVitais: valorDe('sinaisVitais') || null,
          medicamentos: valorDe('medicamentos') || null,
          alimentacaoCuidados: valorDe('alimentacaoCuidados') || null
        };

        definirCarregando(botao, true);
        try {
          await Api.request(`/api/internacoes/${id}/evolucoes`, {
            method: 'POST',
            body: payload
          });
          await carregarEvolucoes(id, resultado);
        } catch (erro) {
          tratarErroFormulario(feedback, form, erro);
          definirCarregando(botao, false);
        }
      });
    } catch (erro) {
      resultado.innerHTML = alertaErroHtml(erro);
    }
  }

  function renderEvolucoes(idInicial) {
    renderBuscaId({
      titulo: rotuloExibicaoRotina(),
      texto: 'Informe o ID da internação para ver e registrar evoluções.',
      rotulo: 'Carregar',
      idInicial,
      aoCarregar: (id, resultado) => carregarEvolucoes(id, resultado)
    });
  }

  function renderPagamentosConta(idInicial) {
    renderBuscaId({
      titulo: rotuloExibicaoRotina(),
      texto: 'Informe o ID da conta para listar os pagamentos.',
      rotulo: 'Carregar',
      idInicial,
      aoCarregar: async (id, resultado) => {
        resultado.innerHTML = carregandoHtml('Carregando pagamentos...');
        try {
          const [conta, pagamentos] = await Promise.all([
            Api.request(`/api/financeiro/contas/${id}`),
            Api.request(`/api/financeiro/pagamentos?contaId=${id}`)
          ]);
          const lista = Array.isArray(pagamentos) ? pagamentos : [];
          const linhas = lista.length
            ? lista
                .map(
                  (pagamento) => `<tr>
                    <td>${escapeHtml(formatarData(pagamento.dataPagamento))}</td>
                    <td>${escapeHtml(formatarMoeda(pagamento.valor))}</td>
                    <td>${escapeHtml(rotuloEnum(pagamento.formaPagamento))}</td>
                    <td>${escapeHtml(pagamento.observacoes || '—')}</td>
                  </tr>`
                )
                .join('')
            : '<tr><td colspan="4">Nenhum pagamento registrado.</td></tr>';

          const podePagar =
            conta.status !== 'PAGA' &&
            conta.status !== 'CANCELADA' &&
            podeRotina('financeiro-contas', 'registrar-pagamento');
          const extra = podePagar
            ? `<a class="btn-primary btn-compact" href="${hrefRotina(
                'financeiro-contas',
                'registrar-pagamento',
                id
              )}"><span class="btn-label">Registrar pagamento</span></a>`
            : '';

          resultado.innerHTML = `${detalheHtml(RECURSOS['financeiro-contas'], conta, {
            acoes: false
          })}
            <div class="crud-section-head">
              <div>
                <h2>Pagamentos</h2>
                <p>${lista.length} pagamento(s) registrado(s).</p>
              </div>
              <div class="crud-actions">${extra}</div>
            </div>
            <div class="crud-table-wrap">
              <table class="crud-table">
                <thead>
                  <tr>
                    <th scope="col">Data</th>
                    <th scope="col">Valor</th>
                    <th scope="col">Forma</th>
                    <th scope="col">Observações</th>
                  </tr>
                </thead>
                <tbody>${linhas}</tbody>
              </table>
            </div>
            <div class="crud-detail-actions">
              <a class="btn-outline" href="${hrefRotina('financeiro-contas', 'listar')}">Voltar à listagem</a>
            </div>`;
        } catch (erro) {
          resultado.innerHTML = alertaErroHtml(erro);
        }
      }
    });
  }

  function renderRegistrarPagamento(idInicial) {
    renderBuscaId({
      titulo: rotuloExibicaoRotina(),
      texto: 'Informe o ID da conta que receberá o pagamento.',
      rotulo: 'Carregar',
      idInicial,
      aoCarregar: async (id, resultado) => {
        resultado.innerHTML = carregandoHtml('Carregando conta...');
        try {
          const conta = await Api.request(`/api/financeiro/contas/${id}`);
          if (conta.status === 'PAGA' || conta.status === 'CANCELADA') {
            resultado.innerHTML = `${detalheHtml(RECURSOS['financeiro-contas'], conta, {
              acoes: false
            })}<div class="alert" role="alert">Esta conta não está disponível para pagamento.</div>`;
            return;
          }

          const campos = [
            {
              name: 'valor',
              label: 'Valor (R$)',
              type: 'number',
              required: true,
              step: '0.01',
              min: '0.01'
            },
            { name: 'dataPagamento', label: 'Data do pagamento', type: 'date' },
            {
              name: 'formaPagamento',
              label: 'Forma de pagamento',
              type: 'select',
              options: OPCOES.formaPagamento,
              required: true,
              defaultValue: 'DINHEIRO'
            },
            {
              name: 'observacoes',
              label: 'Observações',
              type: 'textarea',
              maxLength: 500,
              rows: 3,
              full: true
            }
          ];

          resultado.innerHTML = `<div class="crud-subhead">
            <h3>${escapeHtml(conta.descricao)} <span class="crud-muted">#${conta.id}</span></h3>
            <p class="crud-muted">Saldo em aberto: ${escapeHtml(formatarMoeda(conta.saldo))}</p>
          </div>
          <form class="crud-form" id="pagamento-form" novalidate autocomplete="off">
            <div class="crud-form-grid">
              ${campos.map((campo) => campoHtml(campo, null)).join('')}
            </div>
            <div class="crud-feedback" hidden></div>
            <div class="crud-form-footer">
              <button type="submit" class="btn-primary crud-submit">
                <span class="spinner"></span>
                <span class="btn-label">Registrar pagamento</span>
              </button>
              <a class="btn-outline" href="${hrefRotina('financeiro-contas', 'pagamentos', id)}">Ver pagamentos</a>
            </div>
          </form>`;

          const form = resultado.querySelector('#pagamento-form');
          const feedback = form.querySelector('.crud-feedback');
          const botao = form.querySelector('.crud-submit');

          form.addEventListener('submit', async (evento) => {
            evento.preventDefault();
            limparErros(form);
            if (!form.reportValidity()) {
              return;
            }

            const valorDe = (nome) => {
              const elemento = form.querySelector(`[name="${nome}"]`);
              return elemento ? elemento.value.trim() : '';
            };
            const payload = {
              valor: Number(valorDe('valor')),
              dataPagamento: valorDe('dataPagamento') || null,
              formaPagamento: valorDe('formaPagamento'),
              observacoes: valorDe('observacoes') || null
            };

            definirCarregando(botao, true);
            try {
              await Api.request(`/api/financeiro/contas/${id}/pagamentos`, {
                method: 'POST',
                body: payload
              });
              mostrarSucesso(feedback, 'Pagamento registrado com sucesso.', [
                { href: hrefRotina('financeiro-contas', 'pagamentos', id), label: 'Ver pagamentos' },
                { href: hrefRotina('financeiro-contas', 'listar'), label: 'Ir para a listagem' }
              ]);
              botao.remove();
            } catch (erro) {
              tratarErroFormulario(feedback, form, erro);
            } finally {
              definirCarregando(botao, false);
            }
          });
        } catch (erro) {
          resultado.innerHTML = alertaErroHtml(erro);
        }
      }
    });
  }

  function renderCancelarConta(idInicial) {
    renderBuscaId({
      titulo: rotuloExibicaoRotina(),
      texto: 'Informe o ID da conta que deseja cancelar.',
      rotulo: 'Carregar',
      idInicial,
      aoCarregar: async (id, resultado) => {
        resultado.innerHTML = carregandoHtml('Carregando conta...');
        try {
          const conta = await Api.request(`/api/financeiro/contas/${id}`);
          const bloqueada =
            conta.status === 'CANCELADA' || Number(conta.valorPago) > 0;

          resultado.innerHTML = `${detalheHtml(RECURSOS['financeiro-contas'], conta, {
            acoes: false
          })}
            <div class="crud-confirm">
              <h3>Cancelar conta</h3>
              <p>${
                bloqueada
                  ? 'Esta conta não pode ser cancelada porque já está cancelada ou possui pagamentos.'
                  : 'A conta será marcada como cancelada. Deseja continuar?'
              }</p>
              <div class="crud-confirm-actions">
                ${
                  bloqueada
                    ? ''
                    : '<button type="button" class="btn-danger" id="crud-confirmar">Cancelar conta</button>'
                }
                <a class="btn-outline" href="${hrefRotina('financeiro-contas', 'buscar', id)}">Voltar</a>
              </div>
              <div class="crud-feedback" hidden></div>
            </div>`;

          const botao = resultado.querySelector('#crud-confirmar');
          if (!botao) {
            return;
          }

          const feedback = resultado.querySelector('.crud-feedback');
          botao.addEventListener('click', async () => {
            botao.disabled = true;
            try {
              await Api.request(`/api/financeiro/contas/${id}/cancelar`, {
                method: 'POST'
              });
              mostrarSucesso(feedback, 'Conta cancelada com sucesso.', [
                { href: hrefRotina('financeiro-contas', 'buscar', id), label: 'Ver conta' },
                { href: hrefRotina('financeiro-contas', 'listar'), label: 'Ir para a listagem' }
              ]);
              botao.remove();
            } catch (erro) {
              feedback.hidden = false;
              feedback.classList.add('erro');
              feedback.textContent = mensagemErro(erro);
              botao.disabled = false;
            }
          });
        } catch (erro) {
          resultado.innerHTML = alertaErroHtml(erro);
        }
      }
    });
  }

  function renderRelatorios() {
    const hoje = new Date();
    const inicioPadrao = new Date(hoje.getFullYear(), hoje.getMonth(), 1);
    const paraInputData = (data) => data.toISOString().slice(0, 10);

    renderizar(`<form class="crud-filters" id="relatorios-form" autocomplete="off">
      <div class="field">
        <label for="relatorio-inicio">Início</label>
        <input class="control" id="relatorio-inicio" name="inicio" type="date" value="${paraInputData(inicioPadrao)}">
      </div>
      <div class="field">
        <label for="relatorio-fim">Fim</label>
        <input class="control" id="relatorio-fim" name="fim" type="date" value="${paraInputData(hoje)}">
      </div>
    </form>
    <div class="crud-section-head">
      <div>
        <h2>Exportar relatórios</h2>
        <p>Os arquivos são gerados em formato CSV com os dados atuais do sistema.</p>
      </div>
      <div class="crud-actions">
        <button type="button" class="btn-primary btn-compact" data-relatorio="consultas">Consultas</button>
        <button type="button" class="btn-primary btn-compact" data-relatorio="financeiro">Financeiro</button>
        <button type="button" class="btn-primary btn-compact" data-relatorio="vacinas">Vacinas</button>
        <button type="button" class="btn-primary btn-compact" data-relatorio="estoque">Estoque</button>
      </div>
    </div>
    <div class="crud-feedback" id="relatorios-feedback" hidden></div>`);

    const form = conteudo.querySelector('#relatorios-form');
    const feedback = conteudo.querySelector('#relatorios-feedback');

    const nomes = {
      consultas: 'relatorio-consultas.csv',
      financeiro: 'relatorio-financeiro.csv',
      vacinas: 'relatorio-vacinas.csv',
      estoque: 'relatorio-estoque.csv'
    };

    async function baixar(tipo) {
      feedback.hidden = false;
      feedback.classList.remove('erro');
      feedback.textContent = 'Gerando relatório...';

      const dados = new FormData(form);
      const params = new URLSearchParams();
      if (tipo !== 'estoque') {
        const inicio = dados.get('inicio');
        const fim = dados.get('fim');
        if (inicio) {
          params.set('inicio', inicio);
        }
        if (fim) {
          params.set('fim', fim);
        }
      }

      const token = Sessao.getToken();
      const url = `${API_CONFIG.baseUrl}/api/relatorios/${tipo}${
        params.toString() ? `?${params.toString()}` : ''
      }`;

      try {
        const resposta = await fetch(url, {
          headers: token ? { Authorization: `Bearer ${token}` } : {}
        });

        if (!resposta.ok) {
          let mensagem = 'Não foi possível gerar o relatório.';
          try {
            const erro = await resposta.json();
            mensagem = erro.message || mensagem;
          } catch {
            mensagem = 'Não foi possível gerar o relatório.';
          }
          throw new Error(mensagem);
        }

        const blob = await resposta.blob();
        const link = document.createElement('a');
        const objectUrl = URL.createObjectURL(blob);
        link.href = objectUrl;
        link.download = nomes[tipo];
        document.body.appendChild(link);
        link.click();
        link.remove();
        window.setTimeout(() => URL.revokeObjectURL(objectUrl), 1000);
        feedback.textContent = 'Download iniciado.';
      } catch (erro) {
        feedback.classList.add('erro');
        feedback.textContent = erro.message || 'Não foi possível gerar o relatório.';
      }
    }

    conteudo.querySelectorAll('[data-relatorio]').forEach((botao) => {
      botao.addEventListener('click', () => baixar(botao.dataset.relatorio));
    });
  }

  function rotuloExibicaoRotina() {
    return rotinaChave === 'novo' ? 'Inserir' : encontrada.grupo.rotulo;
  }

  function atualizarCabecalho() {
    elTitulo.textContent = rotuloExibicaoRotina();
    document.title = `Gestão Veterinário · ${rotuloExibicaoRotina()}`;
  }

  function preencherUsuario() {
    if (!usuario) {
      return;
    }

    const nome = document.getElementById('usuario-nome');
    const email = document.getElementById('usuario-email');
    const avatar = document.getElementById('usuario-avatar');

    const partes = usuario.nome.trim().split(/\s+/);
    const iniciais = (
      (partes[0] ? partes[0][0] : '') +
      (partes.length > 1 ? partes[partes.length - 1][0] : '')
    ).toUpperCase();

    if (nome) {
      nome.textContent = usuario.nome;
    }
    if (email) {
      email.textContent = usuario.email;
    }
    if (avatar) {
      avatar.textContent = iniciais;
    }

    window.dispatchEvent(
      new CustomEvent('usuario-atualizado', { detail: usuario })
    );
  }

  preencherUsuario();

  if (!encontrada) {
    elTitulo.textContent = 'Rotina não encontrada';
    document.title = 'Gestão Veterinário · Rotina não encontrada';
    renderizar(
      '<div class="alert" role="alert">Rotina não encontrada. Volte ao painel e selecione uma rotina no menu.</div>'
    );
    return;
  }

  const def = RECURSOS[recursoChave];

  if (!def) {
    elTitulo.textContent = 'Rotina não encontrada';
    document.title = 'Gestão Veterinário · Rotina não encontrada';
    renderizar(
      '<div class="alert" role="alert">Recurso não encontrado. Volte ao painel e selecione uma rotina no menu.</div>'
    );
    return;
  }

  atualizarCabecalho();

  if (!podeRotina(recursoChave, rotinaChave)) {
    renderizar(
      '<div class="alert" role="alert">Você não tem permissão para acessar esta rotina.</div>'
    );
    return;
  }

  const idUrl = params.get('id');

  function iniciar() {
    if (recursoChave === 'consultas' && rotinaChave === 'agenda') {
      renderAgenda();
      return;
    }

    if (recursoChave === 'consultas' && rotinaChave === 'cancelar') {
      renderCancelar(idUrl);
      return;
    }

    if (recursoChave === 'vacinas' && rotinaChave === 'proximas-doses') {
      renderProximasDoses();
      return;
    }

    if (recursoChave === 'produtos' && rotinaChave === 'estoque-baixo') {
      renderEstoqueBaixo();
      return;
    }

    if (recursoChave === 'produtos' && rotinaChave === 'movimentacoes') {
      renderMovimentacoesProduto(idUrl);
      return;
    }

    if (recursoChave === 'produtos' && rotinaChave === 'movimentar') {
      renderMovimentar(lerId(idUrl));
      return;
    }

    if (recursoChave === 'prescricoes' && rotinaChave === 'novo') {
      renderPrescricaoForm(null);
      return;
    }

    if (recursoChave === 'prescricoes' && rotinaChave === 'buscar') {
      renderPrescricaoBuscar(idUrl);
      return;
    }

    if (recursoChave === 'prescricoes' && rotinaChave === 'editar') {
      renderPrescricaoEditar(idUrl);
      return;
    }

    if (recursoChave === 'internacoes' && rotinaChave === 'alta') {
      renderInternacaoAlta(idUrl);
      return;
    }

    if (recursoChave === 'internacoes' && rotinaChave === 'evolucoes') {
      renderEvolucoes(idUrl);
      return;
    }

    if (recursoChave === 'financeiro-contas' && rotinaChave === 'pagamentos') {
      renderPagamentosConta(idUrl);
      return;
    }

    if (
      recursoChave === 'financeiro-contas' &&
      rotinaChave === 'registrar-pagamento'
    ) {
      renderRegistrarPagamento(idUrl);
      return;
    }

    if (recursoChave === 'financeiro-contas' && rotinaChave === 'cancelar-conta') {
      renderCancelarConta(idUrl);
      return;
    }

    if (recursoChave === 'relatorios' && rotinaChave === 'relatorios') {
      renderRelatorios();
      return;
    }

    if (rotinaChave === 'listar') {
      renderListar(def);
      return;
    }

    if (rotinaChave === 'novo') {
      renderNovo(def);
      return;
    }

    if (rotinaChave === 'buscar') {
      renderBuscar(def, idUrl);
      return;
    }

    if (rotinaChave === 'editar') {
      renderEditar(def, idUrl);
      return;
    }

    if (rotinaChave === 'desativar' || rotinaChave === 'excluir') {
      renderExcluir(def, rotinaChave, idUrl);
      return;
    }

    renderizar(
      '<div class="alert" role="alert">Esta rotina ainda não está disponível.</div>'
    );
  }

  try {
    iniciar();
  } catch (erro) {
    renderizar(alertaErroHtml(erro));
  }
});
