import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TicTacToe extends Application {
	Server serverConnection;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("T^3 start");
		
		/*___________________________________________*/
		BorderPane portPane = new BorderPane();
		portPane.setPrefSize(800,800);
		portPane.setPadding(new Insets(200));
		TextField portText = new TextField("Port #");
		portText.setPrefSize(50, 10);
		portText.setAlignment(Pos.CENTER);
		portText.setPadding(new Insets(1, 1, 1, 1));
		Button startServer = new Button("Start Server");
		VBox portBox = new VBox();
		portBox.getChildren().addAll(portText, startServer);
		portBox.setAlignment(Pos.CENTER);
		portBox.setPrefSize(300, 300);
		portPane.setCenter(portBox);
		portPane.setMinSize(600, 600);
		Scene portScene = new Scene(portPane);
		
		/*__________________________________________*/
		
		BorderPane serverPane = new BorderPane();
		serverPane.setPadding(new Insets(70));
		ListView<String> serverList = new ListView<String>();
		VBox serverBox = new VBox();
		serverBox.getChildren().add(serverList);
		serverPane.setCenter(serverBox);
		serverBox.setAlignment(Pos.CENTER);
		serverBox.setPrefSize(300,300);
		serverPane.setPrefSize(800,800);
		Scene serverScene = new Scene(serverPane);
		
		/*_________________________________________*/
		
		startServer.setOnAction(e->{ primaryStage.setScene(serverScene);
		primaryStage.setTitle("This is the Server");
			this.serverConnection = new Server( Integer.parseInt(portText.getText()));//end of serverConnection
		
		});//end of startServer
		
		
		/*___________________________________________*/
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		primaryStage.setScene(portScene);
		primaryStage.show();
	}

}
