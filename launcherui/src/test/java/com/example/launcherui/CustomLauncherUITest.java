package com.example.launcherui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CustomLauncherUITest extends Application {

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        stage.setScene(new Scene(root));

        CustomLauncherUI ui = new CustomLauncherUI();
        ui.init(stage);

        Parent updater = ui.createLoader();
        root.getChildren().addAll(updater);

        stage.show();
    }
}
