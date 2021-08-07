package br.com.lg.smb.validacao.enumerator;

public enum ConceitoMigracaoEnum {

	/* ===================================================== */
	/* ENUMS */
	/* ===================================================== */
	EMPRESA(1, "Empresa"), AGENCIA(2, "Agência"), CARGO(3, "Cargo"), CENTRO_DE_CUSTO(4, "Centro de Custo"), SINDICATO(5, "Sindicato"),
	INSTITUICAO_ENSINO(6, "Instituição de Ensino"), COLABORADOR(7, "Colaborador"), DEPENDENTE(8, "Dependente"),
	PENSIONISTA(9, "Pensionista"), MOV_CARGOS(10, "Movimentação de Cargo"), MOV_SALARIO(11, "Movimentação de Salário"),
	MOV_CENTRO_CUSTO(12, "Movimentação de Centro de Custo"), MOV_ESTABELECIMENTO(13, "Movimentação de Estabelecimento"),
	AFASTAMENTO(14, "Afastamento"), FERIAS(15, "Férias"), RECESSO(16, "Recesso"), ESTABILIDADE(17, "Estabilidade"),
	APOSENTADORIA(18, "Aposentadoria"), AVISO_PREVIO(19, "Aviso Prévio"), RECLAMATORIA(20, "Reclamatória Trabalhista"),
	OUTRAS_OCORRENCIAS(21, "Outras Ocorrências"), RESCISAO(22, "Rescisão"), RESCISAO_COMPLEMENTAR(23, "Rescisão Complementar"),
	FICHA_FINANCEIRA(24, "Ficha Financeira");

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
