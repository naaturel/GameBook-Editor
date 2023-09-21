module org.helmo {
	requires javafx.controls;
	requires java.desktop;
	requires javafx.graphics;
	requires com.google.gson;
    requires mysql.connector.java;
	requires java.sql;
	exports org.helmo.gbeditor;
	exports org.helmo.gbeditor.models to com.google.gson;
	opens org.helmo.gbeditor.models to com.google.gson;
    exports org.helmo.gbeditor.infrastructure to com.google.gson;
    opens org.helmo.gbeditor.infrastructure to com.google.gson;
}