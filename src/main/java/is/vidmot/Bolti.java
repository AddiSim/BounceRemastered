package is.vidmot;

import javafx.beans.binding.Bindings;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;


public class Bolti extends ImageView implements Leikhlutur {

    private static final double HRATT = 10;

    private Stefna stefna;

    private Pallur pallur;


    public Bolti() {
        FXML_Lestur.lesa(this, "Bolti-view.fxml");
        setLayoutX(getImage().getWidth() / 2);
        setStefna(Stefna.NIDUR);
        binda();
    }

    /**
     * Binda boltann við clip sem afmarkar hring
     */
    private void binda() {
        double r = ((Circle) getClip()).getRadius();
        ((Circle) getClip()).centerYProperty().bind(
                Bindings.createDoubleBinding(() -> this.yProperty().get() + r,
                        this.yProperty()));
        ((Circle) getClip()).centerXProperty().bind(
                Bindings.createDoubleBinding(() -> this.xProperty().get() + r,
                        this.xProperty()));
    }

    /**
     * Boltinn færist áfram. Ef farið er í gegn um vinstri bound á Leikbordi
     * þá birtist hann á hægri bound og öfugt
     */
    public void afram() {
        Leikbord parent = (Leikbord) this.getParent();
        double newX = getX() + Math.cos(Math.toRadians(getStefnaGradur())) * HRATT;
        double newY = getY() + Math.sin(Math.toRadians(90)) * HRATT;
        double width = parent.getWidth();
        double imageWidth = getImage().getWidth();
        setX((int) ((newX < 0) ? width - imageWidth : ((newX > width - imageWidth) ? 0 : newX)));
        setY((int) newY % parent.getHeight());
    }




    /**
     * Færir boltann til vinstri eða hægri á pallinum.
     */
    public void aframAPalli() {
        if (getStefna() != Stefna.NIDUR) {
            Leikbord parent = (Leikbord) this.getParent();
            setX(getX() < 0 ? parent.getWidth() : (int) (getX() + Math.cos(Math.toRadians(getStefnaGradur())) * HRATT) % parent.getWidth());
            setStefna(Stefna.NIDUR);
        }
    }

    /**
     * Segir til um hvort pallur hefur dottið á botninn á leikborðinu
     *
     * @param l leikborðið
     * @return satt ef bolti féll á botninn
     */
    public boolean erBoltiABotni(Leikbord l) {
        return yProperty().get() + ((Circle) getClip()).getRadius() * 2 > l.getHeight();
    }

    /**
     * Skilar true ef bolti er á palli p
     *
     * @param p pallur
     * @return satt ef bolti er á palli p
     */
    public boolean erAPalli(Pallur p) {
        return p == pallur;
    }


    public double getStefnaGradur() {
        return stefna.getGradur();
    }

    public void setStefna(Stefna stefna) {
        this.stefna = stefna;
    }

    public Stefna getStefna() {
        return stefna;
    }


    public Pallur getPallur() {
        return pallur;
    }

    /**
     * Bolti færist á pall p
     *
     * @param p pallur
     */
    public void setPallur(Pallur p) {
        pallur = p;
    }
}
