package br.com.lg.smb;

import java.io.FileInputStream;
import java.io.InputStream;

import br.com.lg.smb.utils.Utilitario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));

			Scene scene = new Scene(root);

//			InputStream is = new FileInputStream(Utilitario.diretorioLocal() + ".\\assets\\logo.png");
			InputStream is = new FileInputStream(Utilitario.diretorioLocal() + "\\src\\assets\\logo.png");
			primaryStage.getIcons().add(new Image(is));
			primaryStage.setTitle("Conversor arquivos ADP - Consultoria SMB");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
