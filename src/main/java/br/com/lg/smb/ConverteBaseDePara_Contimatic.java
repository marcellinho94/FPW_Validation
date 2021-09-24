package br.com.lg.smb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class ConverteBaseDePara_Contimatic {

	private static final String ARQUIVO_ENTRADA = "C://ADP/ARQUIVO_ORIGEM.txt";
	private static final String ARQUIVO_SAIDA = "C://ADP/ARQUIVO_DESTINO.txt";

	public static void main(String[] args) {
		try {

			boolean mat = false;

			// Preparando arquivo de retorno
			criarArquivoDestino();
			StringBuilder sb = new StringBuilder();
			sb.append("SET @REFINICIAL = '201901';\n");
			sb.append("SET @REFFINAL   = '202105';\n\n\n");

			// Lendo arquivo de origem
			File fileArqCompleto = new File(ARQUIVO_ENTRADA);
			String strArqCompleto = new String(Files.readAllBytes(fileArqCompleto.toPath()));

			// Split por linhas
			String[] arrLinhas = strArqCompleto.replaceAll("\"", "").replaceAll(" \\|", "\\|").split("\r\n");

			// Percorrendo as linhas
			for (int i = 0; i < arrLinhas.length; i++) {

				// Pegando as info �teis de cada Colunas
				String[] colunas = arrLinhas[i].split("\t");

				// 1� INFO - TABELA
				String tabela = "";
				switch (colunas[1]) {
				case "8":
					tabela = "base13p1";
					break;
				case "9":
					tabela = "base13p2";
					break;
				case "10":
					tabela = "baseadto";
					break;
				case "11":
					tabela = "baseres";
					break;
				case "12":
					tabela = "basefun";
					break;
				case "13":
					tabela = "baseptlc";
					break;
				case "14":
					tabela = "basefer";
					break;
				case "16":
					tabela = "bsrescc";
					break;
				}

				// 2� INFO - CAMPO
				String campo = colunas[4];

				// 3� INFO - FOLHA DESTINO
				String folha = colunas[8];

				// 4� INFO - EVENTO DESTINO
				String evento = colunas[10];

				// -----------------------------------------------------------
				// Criando estrutura base do select
				if (i != 0) {
					sb.append("\n\n\tUNION ALL\n\n\n");
				}

				// Script exceto base de f�rias
				if (!tabela.equals("basefer")) {

					sb.append("SELECT\n");
					sb.append("	'3' AS QuandoRegistroExistir,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	(SELECT \n");
					sb.append("		dp.Para_CodEmpresa \n");
					sb.append("	FROM \n");
					sb.append("		lg_depara_empresas dp \n");
					sb.append("	WHERE \n");
					sb.append("		TRIM(dp.De_Apelido) = TRIM(fic.zSysEmpId)\n");
					sb.append("	) AS CodigoEmpresa,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	(SELECT\n");
					sb.append("		CAST(dp.Para_Matricula AS INT)\n");
					sb.append("	FROM\n");
					sb.append("		lg_depara_matriculas dp\n");
					sb.append("	WHERE\n");
					sb.append("		dp.De_Matricula = fic.Func\n");
					sb.append("		AND dp.De_CodEmp = fic.zSysEmpId\n");
					sb.append("	) AS Matricula,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	'PARA_CODFOLHA' AS CodigoFolha,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	CONCAT(\n");
					sb.append("		RIGHT(CONCAT('0', fic.Mes), 2),\n");
					sb.append("		TRIM(fic.Ano)\n");
					sb.append("	) AS MesReferencia,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	'PARA_CODEVENTO' AS CodigoEvento,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	'' AS TotalParcelas,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	'' AS NParcelaAtual,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	CAST(fic.CAMPO AS DECIMAL(18,2)) AS Valor\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("FROM\n");
					sb.append("	TABELA fic\n");
					sb.append("WHERE\n");
					sb.append("	fic.CAMPO IS NOT NULL\n");
					sb.append("	AND fic.CAMPO > 0\n");
					sb.append("	AND CONCAT(TRIM(fic.Ano), RIGHT(CONCAT('0', fic.Mes), 2)) >= @REFINICIAL\n");
					sb.append("	AND CONCAT(TRIM(fic.Ano), RIGHT(CONCAT('0', fic.Mes), 2)) <= @REFFINAL\n");

					if (mat) {
						sb.append("	AND (SELECT\n");
						sb.append("		CAST(dp.Para_Matricula AS INT)\n");
						sb.append("	FROM\n");
						sb.append("		lg_depara_matriculas dp\n");
						sb.append("	WHERE\n");
						sb.append("		dp.De_Matricula = fic.Func\n");
						sb.append("		AND dp.De_CodEmp = fic.zSysEmpId\n");
						sb.append("	) IN (0300199,0300655,0300882,0301129,0301316,0301388,0301566,0301891,0302442,0500066,0300034,0300934,0301064,0301151,1000052, 1000059, 1000060, 1000043, 1000058)\n");
					}

				} else {

					// Script base de ferias
					sb.append("SELECT DISTINCT\n");
					sb.append("	'3' AS QuandoRegistroExistir,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	(SELECT \n");
					sb.append("		dp.Para_CodEmpresa \n");
					sb.append("	FROM \n");
					sb.append("		lg_depara_empresas dp \n");
					sb.append("	WHERE \n");
					sb.append("		TRIM(dp.De_Apelido) = TRIM(fic.zSysEmpId)\n");
					sb.append("	) AS CodigoEmpresa,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	(SELECT\n");
					sb.append("		CAST(dp.Para_Matricula AS INT)\n");
					sb.append("	FROM\n");
					sb.append("		lg_depara_matriculas dp\n");
					sb.append("	WHERE\n");
					sb.append("		dp.De_Matricula = fic.Func\n");
					sb.append("		AND dp.De_CodEmp = fic.zSysEmpId\n");
					sb.append("	) AS Matricula,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	'PARA_CODFOLHA' AS CodigoFolha,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	(SELECT DISTINCT\n");
					sb.append("	    CASE fic.GozoNumero\n");
					sb.append("	 	   WHEN 1 THEN CONCAT(LEFT(RIGHT(fer.InicioGozo1, 5), 2), LEFT(fer.InicioGozo1, 4))\n");
					sb.append("		   WHEN 2 THEN CONCAT(LEFT(RIGHT(fer.InicioGozo2, 5), 2), LEFT(fer.InicioGozo2, 4))\n");
					sb.append("		   WHEN 3 THEN CONCAT(LEFT(RIGHT(fer.InicioGozo3, 5), 2), LEFT(fer.InicioGozo3, 4))\n");
					sb.append("	    END\n");
					sb.append("  	FROM\n");
					sb.append("		feriafun fer\n");
					sb.append("	WHERE\n");
					sb.append("		fic.Func = fer.Func \n");
					sb.append("		AND fic.AqsId = fer.AqsId \n");
					sb.append("		AND fic.zSysEmpId = fer.zSysEmpId\n");
					sb.append("		AND fer.InicioGozo1 IS NOT NULL\n");
					sb.append("	) AS MesReferencia,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	'PARA_CODEVENTO' AS CodigoEvento,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	'' AS TotalParcelas,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	'' AS NParcelaAtual,\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("	CAST(fic.CAMPO AS DECIMAL(18,2)) AS Valor\n");
					sb.append("	/* --------------------------------------------------- */\n");
					sb.append("FROM\n");
					sb.append("	TABELA fic\n");
					sb.append("WHERE\n");
					sb.append("	fic.CAMPO IS NOT NULL\n");
					sb.append("	AND fic.CAMPO > 0\n");
					sb.append("	AND (SELECT DISTINCT\n");
					sb.append("		    CASE fic.GozoNumero\n");
					sb.append("		 	   WHEN 1 THEN CONCAT(LEFT(fer.InicioGozo1, 4), LEFT(RIGHT(fer.InicioGozo1, 5), 2))\n");
					sb.append("			   WHEN 2 THEN CONCAT(LEFT(fer.InicioGozo2, 4), LEFT(RIGHT(fer.InicioGozo2, 5), 2))\n");
					sb.append("			   WHEN 3 THEN CONCAT(LEFT(fer.InicioGozo3, 4), LEFT(RIGHT(fer.InicioGozo3, 5), 2))\n");
					sb.append("		    END\n");
					sb.append("	  	FROM\n");
					sb.append("			feriafun fer\n");
					sb.append("		WHERE\n");
					sb.append("			fic.Func = fer.Func\n");
					sb.append("			AND fic.AqsId = fer.AqsId\n");
					sb.append("			AND fic.zSysEmpId = fer.zSysEmpId\n");
					sb.append("			AND fer.InicioGozo1 IS NOT NULL\n");
					sb.append("		) >= @REFINICIAL\n");
					sb.append("	AND (SELECT DISTINCT\n");
					sb.append("		    CASE fic.GozoNumero\n");
					sb.append("		 	   WHEN 1 THEN CONCAT(LEFT(fer.InicioGozo1, 4), LEFT(RIGHT(fer.InicioGozo1, 5), 2))\n");
					sb.append("			   WHEN 2 THEN CONCAT(LEFT(fer.InicioGozo2, 4), LEFT(RIGHT(fer.InicioGozo2, 5), 2))\n");
					sb.append("			   WHEN 3 THEN CONCAT(LEFT(fer.InicioGozo3, 4), LEFT(RIGHT(fer.InicioGozo3, 5), 2))\n");
					sb.append("		    END\n");
					sb.append("	  	FROM\n");
					sb.append("			feriafun fer\n");
					sb.append("		WHERE\n");
					sb.append("			fic.Func = fer.Func\n");
					sb.append("			AND fic.AqsId = fer.AqsId\n");
					sb.append("			AND fic.zSysEmpId = fer.zSysEmpId\n");
					sb.append("			AND fer.InicioGozo1 IS NOT NULL\n");
					sb.append("		) <= @REFFINAL\n");

					if (mat) {
						sb.append("	AND (SELECT\n");
						sb.append("		CAST(dp.Para_Matricula AS INT)\n");
						sb.append("	FROM\n");
						sb.append("		lg_depara_matriculas dp\n");
						sb.append("	WHERE\n");
						sb.append("		dp.De_Matricula = fic.Func\n");
						sb.append("		AND dp.De_CodEmp = fic.zSysEmpId\n");
						sb.append("	) IN (0300199,0300655,0300882,0301129,0301316,0301388,0301566,0301891,0302442,0500066,0300034,0300934,0301064,0301151,1000052, 1000059, 1000060, 1000043,1000058)\n");
					}

				}

				// -----------------------------------------------------------
				// Substituindo as colunas no script
				String ret = sb.toString();
				ret = ret.replace("TABELA", tabela);
				ret = ret.replace("CAMPO", campo);
				ret = ret.replace("PARA_CODFOLHA", folha);
				ret = ret.replace("PARA_CODEVENTO", evento);

				salvarArquivoDestino(ret);
				sb = new StringBuilder();
			}

		} catch (Exception e) {
			System.out.println("\n\n---------------------\n\nErro no programa, segue o log: \n\n");
			e.printStackTrace();
		}
	}

	private static void criarArquivoDestino() throws IOException {

		// Define nome de destino
		File arquivo = new File(ARQUIVO_SAIDA);

		// Criando ou recriando o arquivo
		if (arquivo.exists()) {
			arquivo.delete();
			arquivo.createNewFile();
		} else {
			arquivo.createNewFile();
		}
	}

	private static void salvarArquivoDestino(String str) throws IOException {

		// Escrevendo no arquivo
		File arquivo = new File(ARQUIVO_SAIDA);
		FileWriter fw = new FileWriter(arquivo, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(str);

		// Fechando conex�es
		bw.close();
		fw.close();
	}
}
