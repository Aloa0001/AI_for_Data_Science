module com.example.ai_for_data_science {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires commons.math3;


    opens com.example.ai_for_data_science to javafx.fxml;
    exports com.example.ai_for_data_science;
}