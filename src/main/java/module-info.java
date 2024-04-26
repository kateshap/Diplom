module com.example.diploma.controllers {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires com.google.gson;
    requires AnimateFX;
    requires javafx.swing;
//    requires javafx.swing;


    opens com.example.diploma to javafx.fxml, com.google.gson;
    opens com.example.diploma.controllers to com.google.gson, javafx.fxml;
    opens request to javafx.fxml, com.google.gson;
    opens Server to javafx.fxml, com.google.gson;

    exports com.example.diploma;
    exports com.example.diploma.controllers;
    exports request;


}