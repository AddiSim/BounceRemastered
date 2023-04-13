package is.vidmot;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class BounceApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Scene s = new Scene(new Pane());
        ViewSwitcher.setScene (s);
        ViewSwitcher.switchTo(View.BOUNCING);
        BounceController bounceController = (BounceController) ViewSwitcher.lookup(View.BOUNCING);
        stage.setTitle("Bounce ball");
        stage.setScene(s);
        stage.show();

        bounceController.orvatakkar();    // setur upp örvatakka
        bounceController.hefjaLeik();     // byrjar leikinn
    }


    public static void main(String[] args) {
        launch();
    }
}
