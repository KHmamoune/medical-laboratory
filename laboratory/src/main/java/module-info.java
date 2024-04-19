module com.example.laboratory {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.laboratory to javafx.fxml;
    exports com.example.laboratory;
}