package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    private final int SCREEN_WIDTH = 1425;
    private final int SCREEN_HEIGHT = 550;

    @Override
    public void start(Stage primaryStage) throws Exception{
        GUI gui1 = new GUI(primaryStage, SCREEN_WIDTH, SCREEN_HEIGHT);
        GUI gui2 = new GUI(primaryStage, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setTitle("Cell Society");
        GridPane gridPane = new GridPane();
        gridPane.add(gui1.getRoot(), 0, 0);
        gridPane.add(gui2.getRoot(), 1, 0);
        primaryStage.setScene(new Scene(gridPane, SCREEN_WIDTH, SCREEN_HEIGHT, Color.WHITE));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
