package br.com.lg.smb.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.lg.smb.exception.MsgRetorno;
import br.com.lg.smb.exception.PadraoException;

public class ConexaoDiretaBanco {

	private String usuario;
	private String senha;
	private String strConexao;
	private Connection conexao;

	@SuppressWarnings("resource")
	public ConexaoDiretaBanco() throws PadraoException, IOException {
		super();

		// Buscar dentro da Pasta ArquivosConfiguracao o arquivo Config.ini
		// !!!!!! IMPORTANTE !!!!!!!!
		// A ordem dos dados deve seguir o padrão:
		// servidor|banco|user|password
		String diretorio = Utilitario.diretorioLocal() + File.separator + "config.ini";

		FileReader arq = new FileReader(diretorio, Charset.forName("UTF-8"));
		BufferedReader lerArq = new BufferedReader(arq);
		String linha = lerArq.readLine();

		if (linha == null) {
			throw new PadraoException(new MsgRetorno("Parâmetros não configurados.", MsgRetorno.RETORNO_ERRO));
		}

		String[] split = linha.split("\\|", -1);

		// Atribuindo as variaveis os dados
		String servidor = split[0];
		String banco = split[1];
		usuario = split[2];
		senha = split[3];
		String porta = "1433";
		String instancia = null;

		if (Utilitario.StringNullOuVaziaComTrim(servidor) || Utilitario.StringNullOuVaziaComTrim(banco)
				|| Utilitario.StringNullOuVaziaComTrim(usuario) || Utilitario.StringNullOuVaziaComTrim(senha)
				|| Utilitario.StringNullOuVaziaComTrim(porta)) {

			MsgRetorno m = new MsgRetorno("Parâmetros do servidor não configurados.", MsgRetorno.RETORNO_ALERTA);
			throw new PadraoException(m);
		}

		strConexao = "jdbc:jtds:sqlserver://" + servidor + ":" + porta + "/" + banco;

		if (!Utilitario.StringNullOuVaziaComTrim(instancia)) {
			strConexao += ";instance=" + instancia;
		}
	}

	public void conectar() throws PadraoException {
		if (conexao != null)
			return;

		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			conexao = DriverManager.getConnection(strConexao, usuario, senha);

		} catch (Exception e) {
			throw new PadraoException(e);
		}
	}

	public void desconectar() throws PadraoException {
		try {
			if (conexao == null)
				return;

			conexao.close();
			conexao = null;

		} catch (Exception e) {
			throw new PadraoException(e);
		}
	}

	public List<Object[]> obterTodosComSqlLivre(String sql, boolean conectarDesconectarAutomatico) throws PadraoException {

		if (conexao == null) {
			if (conectarDesconectarAutomatico) {
				conectar();
			} else {
				MsgRetorno m = new MsgRetorno("Não conectado.", MsgRetorno.RETORNO_ALERTA);
				throw new PadraoException(m);
			}
		}

		List<Object[]> retorno = null;

		Statement st = null;
		ResultSet rs = null;
		try {

			st = conexao.createStatement();
			rs = st.executeQuery(sql);

			if (rs == null) {
				if (conectarDesconectarAutomatico)
					desconectar();

				return null;
			}

			retorno = new ArrayList<Object[]>();
			ResultSetMetaData metadata = rs.getMetaData();
			int numCols = metadata.getColumnCount();

			while (rs.next()) {
				Object[] row = new Object[numCols];
				for (int i = 0; i < numCols; i++) {
					row[i] = rs.getObject(i + 1);
				}

				retorno.add(row);
			}
			rs.close();

			if (conectarDesconectarAutomatico)
				desconectar();

			return retorno;

		} catch (Exception e) {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			if (conectarDesconectarAutomatico)
				desconectar();

			throw new PadraoException(e);

		}

	}

	public void executaSql(String sql, boolean conectarDesconectarAutomatico) throws PadraoException {

		if (conexao == null) {
			if (conectarDesconectarAutomatico) {
				conectar();
			} else {
				MsgRetorno m = new MsgRetorno("Não conectado.", MsgRetorno.RETORNO_ALERTA);
				throw new PadraoException(m);
			}
		}

		try {
			PreparedStatement pstmt = conexao.prepareStatement(sql);
			pstmt.execute();

			if (conectarDesconectarAutomatico)
				desconectar();

		} catch (Exception e) {
			if (conectarDesconectarAutomatico)
				desconectar();

			throw new PadraoException(e);
		}
	}

}
