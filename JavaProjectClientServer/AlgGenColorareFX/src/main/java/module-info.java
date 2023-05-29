module com.example.alggencolorarefx {
    requires javafx.controls;
    requires javafx.fxml;
    requires unirest.java;

    //needed for JSON
    requires java.sql;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;

    opens com.example.alggencolorarefx to javafx.fxml;
    opens com.example.alggencolorarefx.graph to com.google.gson;
    exports com.example.alggencolorarefx;
    exports com.example.alggencolorarefx.graph;
}