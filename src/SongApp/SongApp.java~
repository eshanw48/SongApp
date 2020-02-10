
//class for actual app driving logic

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class SongApp{

	

	@Override
	public void start(Stage primaryStage) throws Exception{
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass().getResource("/SongApp/SongUI/SongUI.fxml"));
			
		//AnchorPane for root is a resizable Node
		AnchorPane root = (AnchorPane) loader.load();

		//getting controller
		ViewController viewController = loader.getController();
		ViewController.start();
		Scene window = new Scene(root);

		primaryStage.setScene(window);
		primaryStage.show();
	}	

	public static void main(String[] args){
		launch(args);
	}
	



}
