module ru.gb.gbchat1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j;
    requires org.testng;
    requires junit;


    opens ru.gb.gbchat1 to javafx.fxml;
    exports ru.gb.gbchat1;
}