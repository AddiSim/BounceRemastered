package is.vidmot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import is.vinnsla.Leikur;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class BounceController {
    private static int INTERVAL = 50;
    // Býr til beinan aðgang frá KeyCode og í heiltölu. Hægt að nota til að fletta upp
    // heiltölu fyrir KeyCode
    private static final HashMap<KeyCode, Stefna> map = new HashMap<KeyCode, Stefna>();

    // viðmóts tilviksbreytur
    @FXML
    public Label fxHighscore;
    @FXML
    private ListView fxStigin;
    @FXML
    private Label fxStig;
    @FXML
    private Leikbord fxLeikbord;

    private Timeline t; // tímalínan
    Leikur leikur; // vinnslan

    /**
     * Frumstilla controller
     */
    public void initialize() {
        leikur = new Leikur();
        fxStig.textProperty().bind(leikur.stiginProperty().asString());
        fxStigin.setItems(leikur.getTotalStig());
        fxStigin.setFocusTraversable(false);
        fxHighscore.setText("High Score: " + leikur.getHighScore());
    }


    /**
     * Tengir örvatakka við fall sem á að keyra í controller
     **/
    public void orvatakkar() {
        map.put(KeyCode.RIGHT, Stefna.HAEGRI);
        map.put(KeyCode.LEFT, Stefna.VINSTRI);
        // lambda fall - event er parameter
        fxStig.getScene().addEventFilter(KeyEvent.ANY,      //KeyEvents eru sendar á Scene
                this::adgerdLykill); // tilvísun í aðferðina (method reference) - kallað verður á aðferðina adgerdLykill
    }

    private void adgerdLykill(KeyEvent event) {
        try {
            if (map.get(event.getCode()) == null)
                event.consume(); // rangur lykill
            else
                fxLeikbord.getBolti().setStefna(map.get(event.getCode())); // flettum upp horninu fyrir KeyCode í map
        } catch (NullPointerException e) {
            event.consume();        // Ef rangur lykill er sleginn inn þá höldum við áfram
        }
    }

    /**
     * Stillir upp nýjum leik og byrjar hann
     */
    public void nyrLeikur() {
        // Display a dialog to get the player's name
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Game Over");
        dialog.setHeaderText("Enter your name:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();

        // If the player entered a name, add the name and points to the stigatafla map
        if (result.isPresent()) {
            String name = result.get();
            int points = leikur.getStigin();
            leikur.setHighScore(name, points);
            fxHighscore.setText("High Score: " + leikur.getHighScore());
        }
        INTERVAL = 50;
        leikur.nyrLeikur();
        fxLeikbord.nyrLeikur();
        t.play();
    }



    /**
     * Setur upp Animation fyrir leikinn og setur upp leikjalykkjuna
     */
    public void hefjaLeik() throws IOException {
        KeyFrame k = new KeyFrame(Duration.millis(INTERVAL), e -> {
            fxLeikbord.afram(); // boltinn færist áfram
            fxLeikbord.aframPallar(); // pallar færast áfram
            leikur.haekkaStigin(); // stigin eru hækkað
            if (fxLeikbord.erBoltiABotni()) { // athuga hvort boltinn hefur fallið á botninn
                leikLokid("Boltinn fór útaf ");
            } else if (leikur.getStigin() % 500 == 0 && INTERVAL >= 50) { // athuga hvort stigin eru í fjölda af 500
                INTERVAL *= 0.5; // setja nýja millisekúndutíðni sem er helmingur af núverandi
                t.stop(); // stoppa tímalínu
                t.getKeyFrames().set(0, new KeyFrame(Duration.millis(INTERVAL), e1 -> {
                    fxLeikbord.afram(); // boltinn færist áfram
                    fxLeikbord.aframPallar(); // pallar færast áfram
                    leikur.haekkaStigin(); // stigin eru hækkað
                    if (fxLeikbord.erBoltiABotni()) { // athuga hvort boltinn hefur fallið á botninn
                        leikLokid("Boltinn fór útaf ");
                    }
                    // check if the ball collides with the special Pallur1 object
                    if (leikur.getStigin() % 10 == 0 && leikur.getStigin() % 100 != 0) {
                        Pallur1 pallur1 = fxLeikbord.getSpecialPallur1();
                        if (pallur1 != null && pallur1.isVisible() && fxLeikbord.getBolti().erAPalli(pallur1)) {
                            leikLokid("Leik lokið! Þú tapaðir.");
                        }
                    }
                })); // uppfæra fyrstu lykilramma í tímalínu
                t.play(); // spila tímalínu aftur með nýju millisekúndutíðni
            }
        });
        t = new Timeline(k);    // búin til tímalína fyrir leikinn
        t.setCycleCount(Timeline.INDEFINITE);   // leikurinn leikur endalaust
        t.play();
    }




    /**
     * Leik er lokið eð skilaboðum til notanda sem segir ástæðuna.
     * Leikmaður spurður hvort hann vilji leika annan leik
     *
     * @param skilabod ástæðan fyrir að leik lauk
     */
    public void leikLokid(String skilabod) {
        t.stop();
        Platform.runLater(() -> synaAlert(skilabod));
    }





    /**
     * Spyr notanda hvort hann vilji leika annan leik. Hefur nýjan leik ef svo er
     *
     * @param s skilaboð
     */
    private void synaAlert(String s) {
        Alert a = new AdvorunDialog("", "Bouncing Ball", s + " Viltu leika annan leik? ");
        Optional<ButtonType> u = a.showAndWait();
        if (u.isPresent() && !u.get().getButtonData().isCancelButton())
            nyrLeikur();
        else
            System.exit(0);
    }

}
