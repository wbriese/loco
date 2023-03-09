package application;

import java.util.Locale;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {

		// set Layout 
		double width=Screen.getPrimary().getVisualBounds().getWidth();
		double height=Screen.getPrimary().getVisualBounds().getHeight()-20;
		
			try {
				
			Locale.setDefault(new Locale("en", "US"));				
			BorderPane root=new BorderPane();
			Navigation navigator=new Navigation(root);
			navigator.createStartPage();
					
			Scene scene = new Scene(root,width,height);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Loading Computer");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
			
			
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
