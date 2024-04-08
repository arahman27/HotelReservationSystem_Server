module HotelReservationSystem_Server {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.logging;
	requires java.sql;
	requires org.xerial.sqlitejdbc;
	
	opens application to javafx.graphics, javafx.fxml;
}
