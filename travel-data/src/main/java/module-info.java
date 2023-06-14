// use pom.xml artifact-ids for new requires/imports
module sweng861 {
  requires javafx.controls;
  requires javafx.fxml;
  requires transitive amadeus.java;
  requires com.google.gson;

  opens sweng861 to javafx.fxml;
  exports sweng861;
}
