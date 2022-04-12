package br.com.lg.smb.controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import br.com.lg.smb.exception.PadraoException;
import br.com.lg.smb.utils.ConexaoDiretaBanco;
import br.com.lg.smb.utils.Utilitario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import smb.conversoradp.ImportArquivosADP_Banco;

public class MainController implements Initializable {

	@FXML
	private ChoiceBox<String> cboxDatabase;
	@FXML
	private TextField txtServer;
	@FXML
	private TextField txtUser;
	@FXML
	private PasswordField pswPassword;
	@FXML
	private Button btnMigration;
	@FXML
	private Button btnConnection;
	@FXML
	private TextArea txtaConsole;
	@FXML
	private CheckBox cbxCreateTable;

	private ConexaoDiretaBanco con;

	public void connect() {
		try {

			// Clean console
			txtaConsole.setText("");

			// Validation on input data
			if (Utilitario.StringNullOuVaziaComTrim(txtServer.getText()) || Utilitario.StringNullOuVaziaComTrim(txtUser.getText())
					|| Utilitario.StringNullOuVaziaComTrim(pswPassword.getText())) {
				txtaConsole.setText("Par�metros n�o preenchidos.\nT� de sacanagem, n�? Tu n�o � usu�rio comum.");
				return;
			}

			// Database connection
			con = new ConexaoDiretaBanco(txtServer.getText(), "master", txtUser.getText(), pswPassword.getText());

			List<String> listDatabase = new ArrayList<String>();
			StringBuilder sql = new StringBuilder("");
			sql.append("SELECT \n");
			sql.append("	dat.name\n");
			sql.append("FROM \n");
			sql.append("	sys.databases dat\n");
			sql.append("WHERE\n");
			sql.append("	dat.name NOT IN ('master', 'tempdb', 'model', 'msdb')\n");
			sql.append("ORDER BY\n");
			sql.append("	dat.create_date DESC;\n");

			List<Object[]> result = con.obterTodosComSqlLivre(sql.toString(), true);

			// Getting result from database
			if (!Utilitario.listaVazia(result)) {
				result.forEach(e -> {
					listDatabase.add(e[0].toString());
				});
			} else {
				txtaConsole.setText("N�o existe banco de dados.\n1� Crie o banco\n2� Use o migrador");
				return;
			}

			// Enable and disable itens
			cboxDatabase.setDisable(false);
			btnMigration.setDisable(false);
			cbxCreateTable.setDisable(false);

			txtServer.setDisable(true);
			pswPassword.setDisable(true);
			txtUser.setDisable(true);
			btnConnection.setDisable(true);

			// Return to user
			cboxDatabase.getItems().addAll(listDatabase);
			txtaConsole.setText("Conex�o feita com sucesso.\n");

		} catch (Exception e) {
			txtaConsole.setText("Falha de conex�o.\nVerifique os dados preenchidos.");
			e.printStackTrace();
		}
	}

	public void migration() {
		try {
			if (Utilitario.StringNullOuVazia(cboxDatabase.getValue())) {
				txtaConsole.setText("Base n�o selecionada.\nT� de sacanagem, n�? Tu n�o � usu�rio comum.");
				return;
			}

			con = new ConexaoDiretaBanco(txtServer.getText(), cboxDatabase.getValue(), txtUser.getText(), pswPassword.getText());

			// Initial feedback
			String startDate = "\n\nIn�cio - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
			txtaConsole.setText(txtaConsole.getText() + startDate);

			// Migration
			String folder = Utilitario.diretorioLocal() + File.separator + "BASE_AGRUPADA";
			ImportArquivosADP_Banco.iterationFiles(con, folder, cbxCreateTable.isSelected());

			// End feedback
			String endDate = "\nFim - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
			txtaConsole.setText(txtaConsole.getText() + endDate);

			// Disable all app
			btnMigration.setDisable(true);
			cboxDatabase.setDisable(true);

		} catch (PadraoException e) {
			txtaConsole.setText(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			txtaConsole.setText("Falha de conex�o.\nVerifique os dados preenchidos.");
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		cboxDatabase.setDisable(true);
		btnMigration.setDisable(true);
		cbxCreateTable.setDisable(true);
		cbxCreateTable.setSelected(false);
		txtaConsole.setEditable(false);
	}
}
