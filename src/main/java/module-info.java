module is.vidmot {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;


    exports is.vidmot;
    exports is.vinnsla;
    opens is.vidmot to javafx.fxml;
}

