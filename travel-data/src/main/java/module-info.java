// use pom.xml artifact-ids for new requires/imports
module sweng861 {
  requires javafx.controls;
  requires javafx.fxml;
  requires amadeus.java;

  opens sweng861 to javafx.fxml;
  exports sweng861;
}
