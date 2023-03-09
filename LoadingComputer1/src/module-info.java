module LoadingComputer1 {

requires javafx.controls;
requires javafx.fxml;
requires javafx.base;
requires javafx.graphics;
requires java.logging;
requires java.base;
requires json.simple;

opens application to javafx.graphics, javafx.fxml, javafx.base; 

}