package br.com.lg.smb.validacao.fpw;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

import br.com.lg.smb.exception.PadraoException;
import br.com.lg.smb.utils.ConexaoDiretaBanco;
import br.com.lg.smb.utils.Utilitario;

public class FPW {

	public static void main(String[] args) {

		try {
			String databaseName = "ORIGEM_MAZARS";
			String codEmp = "";

			ConexaoDiretaBanco con = new ConexaoDiretaBanco(codEmp, databaseName, databaseName, codEmp, null);

			qualitativo(databaseName, codEmp, con);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void qualitativo(String databaseName, String codEmp, ConexaoDiretaBanco con) throws PadraoException, IOException {

		// Executing the query
		FPW_Quantitativo qtd = new FPW_Quantitativo(databaseName, codEmp, con);
		List<HashMap<String, Object>> countTables = qtd.getCountTables();
		String sql = qtd.getSQL();

		// Building CSV body
		StringBuilder sb = new StringBuilder();

		// Head
		sb.append("Banco:;").append(databaseName);
		sb.append("\n");
		if (!Utilitario.StringNullOuVaziaComTrim(codEmp)) {
			sb.append("Empresas:;" + codEmp);
			sb.append("\n");
		}
		sb.append("\nTabela;Quantidade\n");

		// Body
		for (HashMap<String, Object> map : countTables) {

			sb.append(map.get("table"));
			sb.append(";");
			sb.append(map.get("count"));
			sb.append("\n");
		}

		// CSV File
		File countFile = new File(Utilitario.diretorioLocal() + File.separatorChar + "00 - " + databaseName
				+ (Utilitario.StringNullOuVazia(codEmp) ? "_Completo" : "_PorEmpresas") + ".csv");

		Utilitario.saveFile(sb.toString().getBytes(Charset.forName("Cp1252")), countFile);

		// Query
		File countFileSQL = new File(Utilitario.diretorioLocal() + File.separatorChar + "01 - Quantitativo.sql");

		Utilitario.saveFile(sql.toString().getBytes(Charset.forName("Cp1252")), countFileSQL);

	}
}
