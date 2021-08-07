package br.com.lg.smb.conversoradp;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.com.lg.smb.utils.ConexaoDiretaBanco;
import br.com.lg.smb.utils.Utilitario;

public class ImportArquivosADP_Banco {

	public static void main(String[] args) {
		try {

			System.out.print("\n\n\tInício - ");
			System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

			// Conection with database
			ConexaoDiretaBanco con = new ConexaoDiretaBanco();
			iterationFiles(con);

			System.out.print("\n\n\tConcluido - ");
			System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void iterationFiles(ConexaoDiretaBanco con) throws Exception {

		// Reading the directory
		File file = new File(Utilitario.diretorioLocal() + File.separator + "BASE_AGRUPADA");

		// Read all files
		File[] listFiles = file.listFiles();

		// Iteration by file
		for (int i = 0; i < listFiles.length; i++) {

			String fileName = listFiles[i].getName().replaceAll(".d", "").replaceAll(".txt", "");

			System.out.println("\tComeço: " + fileName);

			// Alguns precisam de tratamento especial
			if (fileName.equalsIgnoreCase("folha")) {

				// Reading the file
				convertFolha(listFiles[i], con, fileName);

			} else if (fileName.equalsIgnoreCase("estab-empresa")) {

			} else {

				// Reading the file
				String fullFile = new String(Files.readAllBytes(listFiles[i].toPath()), StandardCharsets.ISO_8859_1);

				// Split by rows
				String[] arrLinhas = fullFile.split("\r\n");

				Integer maxColumnLength = 0;
				// Iteration - COUNT MAX COLUMN LENGTH
				for (int j = 0; j < arrLinhas.length; j++) {

					String[] columns = Utilitario.textToColumns(arrLinhas[j]);
					for (int k = 0; k < columns.length; k++) {

						if (columns[k].length() > maxColumnLength) {
							maxColumnLength = columns[k].length();
						}
					}
				}

				int maxRowsInsert = 0;
				StringBuilder sb = new StringBuilder();
				// Iteration - CREATE AND INSERT
				for (int j = 0; j < arrLinhas.length; j++) {

					// First row
					if (j == 0) {

						String createTableQuery = createTableQuery(arrLinhas[j], fileName, maxColumnLength);
						con.executaSql(createTableQuery, true);

					} else { // Other rows

						if ((j + 1) == arrLinhas.length || maxRowsInsert == 995) {

							String header = "INSERT INTO \n\t[" + fileName + "]\nVALUES\n";
							sb.append(insertQuery(arrLinhas[j]));

							con.executaSql(header + sb.toString().substring(0, sb.toString().length() - 1), true);

							maxRowsInsert = 0;
							sb = new StringBuilder();

						} else {

							sb.append(insertQuery(arrLinhas[j]));
							maxRowsInsert++;
						}

					}
				}
			}

			System.out.println("\tFim: " + fileName);
		}
	}

	private static void convertFolha(File folha, ConexaoDiretaBanco con, String fileName) throws Exception {

		String fullStringFile = new String(Files.readAllBytes(folha.toPath()), StandardCharsets.ISO_8859_1);

		// Variables
		StringBuilder sb = new StringBuilder();
		int maxRowsInsert = 0;

		// Split by rows
		String[] arrRows = fullStringFile.replaceAll(" \\|", "\\|").split("\r\n");

		fullStringFile = null;

		// For by rows
		for (int i = 0; i < arrRows.length; i++) {

			// Header
			if (i == 0) {

				String createTableQuery = createTableQuery(arrRows[i], fileName, 150);
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

					sb.append(insertQuery(columns));

					// Insert database
					maxRowsInsert++;
					if ((i + 1) == arrRows.length || maxRowsInsert == 990) {

						Utilitario.copiaTextoClipBoard(sb.toString().substring(0, sb.toString().length() - 1));

						String header = "INSERT INTO \n\t[" + fileName + "]\nVALUES\n";
						con.executaSql(header + sb.toString().substring(0, sb.toString().length() - 1), true);

						maxRowsInsert = 0;
						sb = new StringBuilder();
					}

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

						sb.append(insertQuery(auxColumns));
						auxColumns = new String[columns.length];

						// Insert database
						maxRowsInsert++;
						if ((i + 1) == arrRows.length || maxRowsInsert == 990) {

							Utilitario.copiaTextoClipBoard(sb.toString().substring(0, sb.toString().length() - 1));

							String header = "INSERT INTO \n\t[" + fileName + "]\nVALUES\n";
							con.executaSql(header + sb.toString().substring(0, sb.toString().length() - 1), true);

							maxRowsInsert = 0;
							sb = new StringBuilder();
						}
					}
				}

			}
		}
	}

	public static String createTableQuery(String row, String fileName, Integer maxColumnLength) throws Exception {

		String[] columns = Utilitario.textToColumns(row);

		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE [").append(fileName).append("] (\n\n");

		for (int i = 0; i < columns.length; i++) {

			if ((i + 1) == columns.length) {

				sb.append("\t[").append(columns[i]).append("] VARCHAR(").append(maxColumnLength).append(") NULL\n");

			} else {

				sb.append("\t[").append(columns[i]).append("] VARCHAR(").append(maxColumnLength).append(") NULL,\n");
			}
		}

		sb.append(");");

		return sb.toString();
	}

	public static String insertQuery(String row) throws Exception {

		String[] columns = Utilitario.textToColumns(row);

		StringBuilder sb = new StringBuilder();
		sb.append("\t(");

		for (int i = 0; i < columns.length; i++) {

			String aux = columns[i].replaceAll("\"", "").replaceAll("'", "''");

			if ((i + 1) == columns.length) {

				sb.append("'").append(aux).append("'");

			} else {

				sb.append("'").append(aux).append("', ");
			}
		}
		sb.append("),");

		return sb.toString();
	}

	public static String insertQuery(String[] columns) throws Exception {

		StringBuilder sb = new StringBuilder();
		sb.append("\t(");

		for (int i = 0; i < columns.length; i++) {

			String aux = columns[i].replaceAll("\"", "").replaceAll("'", "''");

			if ((i + 1) == columns.length) {

				sb.append("'").append(aux).append("'");

			} else {

				sb.append("'").append(aux).append("', ");
			}
		}
		sb.append("),");

		return sb.toString();
	}
}
