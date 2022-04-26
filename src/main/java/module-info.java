module ru.gb.gbchat1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ru.gb.gbchat1 to javafx.fxml;
    exports ru.gb.gbchat1;
}