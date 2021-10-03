package com.example.ai_for_data_science;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        new Dashboard().run();
    }

    public static void main(String[] args) {
        if (args != null) launch(args);
    }
}
