package br.com.lg.smb.validacao.enumerator;

public enum ConceitoMigracaoEnum {

	/* ===================================================== */
	/* ENUMS */
	/* ===================================================== */
	EMPRESA(1, "Empresa"), 
	ESTABELECIMENTO(2, "Estabelecimento"), 
	LOTACAO_TRIBUTARIA(3, "Empresa"), 
	BANCO(4, "Banco"), 
	AGENCIA(4, "Agência"), 
	CARGO(5, "Cargo"), 
	CENTRO_DE_CUSTO(6, "Centro de Custo"), 
	SINDICATO(7, "Sindicato"),
	INSTITUICAO_ENSINO(8, "Instituição de Ensino"), 
	AGENTE_DE_INTEGRACAO(9, "Agente de Integração"), 
	COLABORADOR(10, "Colaborador"), 
	DEPENDENTE(11, "Dependente"), 
	PENSIONISTA(12, "Pensionista"),
	MOV_CARGOS(13, "Movimentação de Cargo"), 
	MOV_SALARIO(14, "Movimentação de Salário"), 
	MOV_CENTRO_CUSTO(15, "Movimentação de Centro de Custo"),
	MOV_ESTABELECIMENTO(16, "Movimentação de Estabelecimento"), 
	MOV_SINDICATO(17, "Movimentação de Sindicato"), 
	AFASTAMENTO(18, "Afastamento"), 
	FERIAS(19, "Férias"), 
	RECESSO(20, "Recesso"),
	ESTABILIDADE(21, "Estabilidade"), 
	APOSENTADORIA(22, "Aposentadoria"), 
	AVISO_PREVIO(23, "Aviso Prévio"), 
	RECLAMATORIA(24, "Reclamatória Trabalhista"),
	OUTRAS_OCORRENCIAS(25, "Outras Ocorrências"), 
	RESCISAO(26, "Rescisão"), 
	RESCISAO_COMPLEMENTAR(27, "Rescisão Complementar"),
	MOV_EMPRESA(27, "Movimentação de Empresa"),
	CALCULO_RETROATIVO(28, "Cálculo Retroativo"),
	FICHA_FINANCEIRA(29, "Ficha Financeira");

	/* ===================================================== */
	/* CONSTRUCTOR */
	/* ===================================================== */
	ConceitoMigracaoEnum(int id, String description) {
		this.id = id;	
		this.description = description;
	}

	/* ===================================================== */
	/* ______________________ ATRIBUTOS ____________________ */
	/* ===================================================== */
	private Integer id;
	private String description;

	/* ===================================================== */
	/* ________________________ METODOS ____________________ */
	/* ===================================================== */
	public static ConceitoMigracaoEnum getById(int id) {
		for (ConceitoMigracaoEnum enums : ConceitoMigracaoEnum.values()) {
			if (enums.getId() == id) {
				return enums;
			}
		}
		return null;
	}

	public static ConceitoMigracaoEnum getByNomeEvento(String description) {
		for (ConceitoMigracaoEnum enums : ConceitoMigracaoEnum.values()) {
			if (enums.getDescription().equals(description)) {
				return enums;
			}
		}
		return null;
	}

	/* ===================================================== */
	/* _________________ GETTERS AND SETTERS _______________ */
	/* ===================================================== */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
