package br.com.lg.smb.validacao.fpw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.lg.smb.exception.PadraoException;
import br.com.lg.smb.utils.ConexaoDiretaBanco;
import br.com.lg.smb.utils.Utilitario;
import br.com.lg.smb.validacao.enumerator.ConceitoMigracaoEnum;

public class FPW_Quantitativo {

	/* ===================================================== */
	/* Variables */
	/* ===================================================== */
	List<HashMap<String, Object>> returnList;
	StringBuilder allSQL;
	ConexaoDiretaBanco con;
	String database;
	String codEmp;
	Boolean queryDone;

	/* ===================================================== */
	/* Constructor */
	/* ===================================================== */
	public FPW_Quantitativo(String database, String codEmp, ConexaoDiretaBanco con) throws PadraoException, IOException {

		this.returnList = new ArrayList<HashMap<String, Object>>();
		this.con = con;
		this.database = database;
		this.codEmp = codEmp;
		this.allSQL = new StringBuilder();
		this.queryDone = false;
	}

	/* ===================================================== */
	/* MAIN */
	/* ===================================================== */
	public List<HashMap<String, Object>> getCountTables() throws PadraoException {

		empresa();
		agencia();
		cargo();
		centroDeCusto();
		sindicato();
		instituicaoEnsino();
		colaborador();
		dependente();
		pensionista();
		movCargos();
		movSalarios();
		movCentroCusto();
		movEstabelecimento();
		outrasOcorrencias();
		afastamento();
		ferias();
		recesso();
		estabilidade();
		aposentadoria();
		avisoPrevio();
		reclamatoriaTrabalhista();
		rescisao();
		rescisaoComplementar();
		fichaFinanceira();

		this.queryDone = true;

		return this.returnList;
	}

	public String getSQL() throws PadraoException {

		if (!queryDone) {
			getCountTables();
		}

		return this.allSQL.toString();
	}

	/* ===================================================== */
	/* Auxiliary Methods */
	/* ===================================================== */
	private void empresa() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM \n");
			sql.append("	EMPRESAS;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	EMPRESAS\n");
			sql.append("WHERE\n");
			sql.append("	EMCODEMP IN (").append(this.codEmp).append(")\n");
		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.EMPRESA.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.EMPRESA.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void agencia() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM \n");
			sql.append("	BANCOSAG;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(B.BACODAGENC)\n");
			sql.append("FROM\n");
			sql.append("	BANCOSAG B\n");
			sql.append("WHERE\n");
			sql.append("	EXISTS(\n");
			sql.append("		\n");
			sql.append("		SELECT\n");
			sql.append("			F.FUCODAGEPG\n");
			sql.append("		FROM\n");
			sql.append("			FUNCIONA F\n");
			sql.append("		WHERE\n");
			sql.append("			B.BACODAGENC = F.FUCODAGEPG\n");
			sql.append("			AND B.BACODBANCO = F.FUCODBCOPG\n");
			sql.append("			AND F.FUCODEMP IN (").append(this.codEmp).append(")\n");
			sql.append("	);\n");
		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.AGENCIA.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.AGENCIA.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void cargo() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	CARGOS;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	CARGOS\n");
			sql.append("WHERE\n");
			sql.append("	CACODEMP IN (").append(this.codEmp).append(")\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.CARGO.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.CARGO.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void centroDeCusto() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	CENCUSTO;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	CENCUSTO\n");
			sql.append("WHERE\n");
			sql.append("	CCCODEMP IN (").append(this.codEmp).append(")\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.CENTRO_DE_CUSTO.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.CENTRO_DE_CUSTO.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void sindicato() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	SINDICAT;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	SINDICAT sind\n");
			sql.append("WHERE\n");
			sql.append("	EXISTS (\n");
			sql.append("	\n");
			sql.append("		SELECT\n");
			sql.append("			*\n");
			sql.append("		FROM\n");
			sql.append("			FUNCIONA fun\n");
			sql.append("		WHERE\n");
			sql.append("			fun.FUCODSIND = sind.SICODIGO\n");
			sql.append("			AND fun.FUCODEMP IN (").append(this.codEmp).append(")\n");
			sql.append("	);\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.SINDICATO.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.SINDICATO.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void instituicaoEnsino() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	INSTITUICAO_ENSINO;\n");

		} else {

			sql.append("SELECT \n");
			sql.append("	COUNT(*) \n");
			sql.append("FROM \n");
			sql.append("	INSTITUICAO_ENSINO insEns\n");
			sql.append("WHERE\n");
			sql.append("	EXISTS (\n");
			sql.append("	\n");
			sql.append("		SELECT\n");
			sql.append("			*\n");
			sql.append("		FROM\n");
			sql.append("			FUNCIONA fun\n");
			sql.append("		WHERE\n");
			sql.append("			fun.FUCNPJINSTITUICAO = insEns.INSENCODIGO\n");
			sql.append("			AND fun.FUCODEMP IN (").append(this.codEmp).append(")\n");
			sql.append("	);\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.INSTITUICAO_ENSINO.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.INSTITUICAO_ENSINO.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void colaborador() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	FUNCIONA;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	FUNCIONA\n");
			sql.append("WHERE\n");
			sql.append("	FUCODEMP IN (").append(this.codEmp).append(")\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.COLABORADOR.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.COLABORADOR.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void dependente() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	DEPSFIR;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	DEPSFIR\n");
			sql.append("WHERE\n");
			sql.append("	DPCODEMP IN (").append(this.codEmp).append(")\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.DEPENDENTE.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.DEPENDENTE.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void pensionista() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	PENSIONI;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	PENSIONI\n");
			sql.append("WHERE\n");
			sql.append("	PECODEMP IN (").append(this.codEmp).append(")\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.PENSIONISTA.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.PENSIONISTA.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void movCargos() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1006;\n");

		} else {

			sql.append("SELECT \n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM \n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1006\n");
			sql.append("	AND OFCODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.MOV_CARGOS.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.MOV_CARGOS.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void movSalarios() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR IN (01009, 10091, 10092, \n");
			sql.append("					10093,10094, 10095, \n");
			sql.append("					10096, 10097, 10098, \n");
			sql.append("					10099, 01010, 10101, \n");
			sql.append("					10102, 10103, 10104, \n");
			sql.append("					10105, 10106, 10107, 10108, 10109);\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR IN (01009, 10091, 10092, \n");
			sql.append("					10093,10094, 10095, \n");
			sql.append("					10096, 10097, 10098, \n");
			sql.append("					10099, 01010, 10101, \n");
			sql.append("					10102, 10103, 10104, \n");
			sql.append("					10105, 10106, 10107, 10108, 10109)\n");
			sql.append("	AND OFCODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.MOV_SALARIO.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.MOV_SALARIO.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void movCentroCusto() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1013;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1013\n");
			sql.append("	AND OFCODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.MOV_CENTRO_CUSTO.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.MOV_CENTRO_CUSTO.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void movEstabelecimento() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1003;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1003\n");
			sql.append("	AND OFCODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.MOV_ESTABELECIMENTO.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.MOV_ESTABELECIMENTO.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void outrasOcorrencias() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR <= 999;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR <= 999\n");
			sql.append("	AND OFCODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.OUTRAS_OCORRENCIAS.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.OUTRAS_OCORRENCIAS.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void afastamento() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1002;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1002\n");
			sql.append("	AND OFCODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.AFASTAMENTO.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.AFASTAMENTO.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void ferias() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR IN (1001, 1007);\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR IN (1001, 1007)\n");
			sql.append("	AND OFCODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.FERIAS.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.FERIAS.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void recesso() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1033;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1033\n");
			sql.append("	AND OFCODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.RECESSO.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.RECESSO.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void estabilidade() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1028;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1028\n");
			sql.append("	AND OFCODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.ESTABILIDADE.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.ESTABILIDADE.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void aposentadoria() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1016;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1016\n");
			sql.append("	AND OFCODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.APOSENTADORIA.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.APOSENTADORIA.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void avisoPrevio() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1036;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1036\n");
			sql.append("	AND OFCODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.AVISO_PREVIO.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.AVISO_PREVIO.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void reclamatoriaTrabalhista() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1021;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1021\n");
			sql.append("	AND OFCODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.RECLAMATORIA.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.RECLAMATORIA.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void rescisao() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1004;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1004\n");
			sql.append("	AND OFCODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.RESCISAO.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.RESCISAO.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void rescisaoComplementar() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1027;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	OCORFUNC\n");
			sql.append("WHERE\n");
			sql.append("	OFCODOCORR = 1027\n");
			sql.append("	AND OFCODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.RESCISAO_COMPLEMENTAR.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.RESCISAO_COMPLEMENTAR.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}

	private void fichaFinanceira() throws PadraoException {

		StringBuilder sql = new StringBuilder("");

		sql.append("\n\nUSE ").append(this.database).append(";\n\n");

		if (Utilitario.StringNullOuVaziaComTrim(this.codEmp)) {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	VALANO;\n");

		} else {

			sql.append("SELECT\n");
			sql.append("	COUNT(*)\n");
			sql.append("FROM\n");
			sql.append("	VALANO\n");
			sql.append("WHERE\n");
			sql.append("	VACODEMP IN (").append(this.codEmp).append(");\n");

		}

		List<Object[]> retList = con.obterTodosComSqlLivre(sql.toString(), Boolean.TRUE);
		allSQL.append(sql);

		if (!Utilitario.listaVazia(retList)) {

			for (Object[] objects : retList) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("table", ConceitoMigracaoEnum.FICHA_FINANCEIRA.getDescription());
				map.put("count", objects[0]);

				this.returnList.add(map);
			}

		} else {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("table", ConceitoMigracaoEnum.FICHA_FINANCEIRA.getDescription());
			map.put("count", 0);

			this.returnList.add(map);
		}
	}
}
