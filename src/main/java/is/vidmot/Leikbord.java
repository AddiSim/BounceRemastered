package is.vidmot;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import java.util.List;


public class Leikbord extends Pane implements Leikhlutur {
    public boolean isNewHighScore;
    @FXML
    private Pallur fxPallur;
    @FXML
    private Bolti fxBolti;


    private ObservableList<Pallur> pallar = FXCollections.observableArrayList();

    public Leikbord() {
        upphafsstilla();
    }

    private void upphafsstilla() {
        FXML_Lestur.lesa(this, "Leikbord-view.fxml"); // lesa fxml skrána
        for (int i = 1; i < getChildren().size(); i++) {
            pallar.add((Pallur) getChildren().get(i));
        }
        fxBolti.setPallur(null); // boltinn er ekki á neinum palli
    }


    /**
     * Færir boltann áfram. Ef boltinn er á palli má færa sig til vinstri eða hægri
     */
    public void afram() {
        if (fxBolti.getPallur() == null) { // bolti er ekki á neinum palli
            fxBolti.afram(); // eitt skref vinstri eða hægri en svo ...
        } else {
            fxBolti.aframAPalli(); // má færa bolta til vinstri eða hægri
        }

    }

    /**
     * Færir palla upp á y ás og athugar hvort bolti er á einhverjum palli
     */
    public void aframPallar() {
        for (Pallur p : pallar) { // pallar færast áfram
            ((Leikhlutur) p).afram();
            athugaBoltiAPalli(p); // athuga hvort boltinn er á palli
        }
    }

    /**
     * Athuga hvort bolti kemst á pall p og ef svo bindur boltann við pallinn.
     * Ef bolti var á palli p en ekki lengur er honum hent af palli
     *
     * @param p pallur
     */
    private void athugaBoltiAPalli(Pallur p) {
        if (fxBolti.getBoundsInParent().intersects(p.getBoundsInParent())) {
            if (!fxBolti.erAPalli(p)) { // skoðar hvort bolli er a palli
                setjaBoltaAPall(p); // setja bolta á pall
            }
        } else if (fxBolti.erAPalli(p)) { // fór af pallinum
            hendaBoltaAfPalli(p);   // henda bolta af palli
        }
    }

    /**
     * Setja bolta á pall. Tengir boltann við pallinn og bolti er á einhverjum palli
     * Stefnan er niður
     *
     * @param p pallur
     */
    private void setjaBoltaAPall(Pallur p) {
        fxBolti.setPallur(p);
        fxBolti.yProperty().bind(Bindings
                .createDoubleBinding(() -> p.yProperty().get() - 25,
                        p.yProperty()));

    }

    /**
     * Henda bolta af palli. Bolti aftengdur palli. Bolti er ekki á neinum palli
     *
     * @param p pallur
     */
    public void hendaBoltaAfPalli(Node p) {
        fxBolti.setPallur(null);
        fxBolti.yProperty().unbind();
    }


    public Bolti getBolti() {
        return fxBolti;
    }

    /**
     * Athuga hvort boltinn er fallinn á botninn
     *
     * @return true ef boltinn er fallinn á botninn
     */
    public boolean erBoltiABotni() {
        return fxBolti.erBoltiABotni(this);
    }

    /**
     * Nýr leikur. Pöllum eytt og upphafsstillt
     */
    public void nyrLeikur() {
        getChildren().clear();
        pallar = FXCollections.observableArrayList();
        upphafsstilla();
    }
}

