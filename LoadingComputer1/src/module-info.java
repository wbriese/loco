module LoadingComputer1 {

requires javafx.controls;
requires javafx.fxml;
requires javafx.base;
requires javafx.graphics;
requires java.logging;
requires json.simple;
requires java.base;

opens application to javafx.graphics, javafx.fxml, javafx.base; 

}