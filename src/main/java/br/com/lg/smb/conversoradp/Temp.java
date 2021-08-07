package br.com.lg.smb.conversoradp;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import br.com.lg.smb.utils.ConexaoDiretaBanco;
import br.com.lg.smb.utils.Utilitario;

public class Temp {

	public static void convertFolha(File folha, ConexaoDiretaBanco con) throws Exception {

		// Converting File to String
		String fullStringFile = new String(Files.readAllBytes(folha.toPath()), StandardCharsets.UTF_8);

		// Variables
		StringBuilder sb = new StringBuilder();
		int maxRowsInsert = 0;

		// Split by rows
		String[] arrRows = fullStringFile.replaceAll(" \\|", "\\|").split("\r\n");

		// For by rows
		for (int i = 0; i < arrRows.length; i++) {

			// Header
			if (i == 0) {

				String createTableQuery = ImportArquivosADP_Banco.createTableQuery(arrRows[i], folha.getName(), 1000);
				con.executaSql(createTableQuery, true);

			} else { // Rows

				// Split by columns
				String[] columns = Utilitario.textToColumns(arrRows[i]);

				// count how many rows will be
				int qtdItens = 0;
				for (int j = 0; j < columns.length; j++) {

					if (columns[j].contains("|")) {

						// Count |
						String[] itens = columns[j].split("\\|");

						// Getting the column with more pipes
						if (itens.length > qtdItens) {
							qtdItens = itens.length;
						}
					}
				}

				// If does not exists itens
				if (qtdItens == 0) {

					sb.append(ImportArquivosADP_Banco.insertQuery(columns));

				} else {

					// Duplicando as linhas, na quantidade de vezes necessárias - de acordo com os pipes
					String[] auxColumns = new String[columns.length];
					for (int j = 0; j < qtdItens; j++) {

						// Percorrendo as colunas
						for (int k = 0; k < columns.length; k++) {

							// Adicionando a coluna ou o item
							if (columns[k].contains("|")) {

								String[] itens = columns[k].split("\\|");

								if (itens.length > j) {
									auxColumns[k] = itens[j];
								}

							} else {

								auxColumns[k] = columns[k];
							}
						}

						sb.append(ImportArquivosADP_Banco.insertQuery(auxColumns));
						auxColumns = new String[columns.length];
					}
				}

				// Insert database
				maxRowsInsert++;
				if ((i + 1) == arrRows.length || maxRowsInsert == 995) {

					String header = "INSERT INTO \n\t[" + folha.getName() + "]\nVALUES\n";
					con.executaSql(header + sb.toString().substring(0, sb.toString().length() - 1), true);

					maxRowsInsert = 0;
					sb = new StringBuilder();
				}
			}
		}
	}
}