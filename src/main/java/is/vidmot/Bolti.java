package is.vidmot;

import javafx.beans.binding.Bindings;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;


public class Bolti extends ImageView implements Leikhlutur {

    private static final double HRATT = 10;
    private final double hradi = HRATT;

    private Stefna stefna; // stefna boltans

    private Pallur pallur; // ef boltinn er á palli, annars null


    public Bolti() {
        FXML_Lestur.lesa(this, "Bolti-view.fxml");
        setLayoutX(getImage().getWidth() / 2);
        setStefna(Stefna.NIDUR);
        bindaVidClip();
    }

    /**
     * Binda boltann við clip sem afmarkar hring
     */
    private void bindaVidClip() {
        double r = ((Circle) getClip()).getRadius();
        ((Circle) getClip()).centerYProperty().bind(
                Bindings.createDoubleBinding(() -> this.yProperty().get() + r,
                        this.yProperty()));
        ((Circle) getClip()).centerXProperty().bind(
                Bindings.createDoubleBinding(() -> this.xProperty().get() + r,
                        this.xProperty()));
    }

    /**
     * Boltinn færist áfram. Ef komið er að vinstri jaðrinum kemstu ekki lengra. Ef komið er að hægri jaðrinum
     * ferðu hringinn að vinstri jaðrinum.
     */
    public void afram() {
        Leikbord parent = (Leikbord) this.getParent();
        double newX = getX() + Math.cos(Math.toRadians(getStefnaGradur())) * hradi;
        double newY = getY() + Math.sin(Math.toRadians(90)) * hradi ;
        setX((int) (newX < 0 || newX > parent.getWidth() ? 0 : newX) % (parent.getWidth() - getImage().getWidth()));
        setY((int) newY % parent.getHeight());
    }



    public double getImageWidth() {
        return getImage().getWidth() * getScaleX();
    }

    public double getImageHeight() {
        return getImage().getHeight() * getScaleY();
    }


    public boolean erArekstur(Pallur d) {
        boolean intersects = getBoundsInParent().intersects(d.getBoundsInParent());
        if (intersects) {
            double ballBottom = getBoundsInParent().getMaxY();
            double platformTop = d.getBoundsInParent().getMinY();
            double platformLeft = d.getBoundsInParent().getMinX();
            double platformRight = d.getBoundsInParent().getMaxX();
            if (ballBottom >= platformTop && getBoundsInParent().getCenterX() >= platformLeft && getBoundsInParent().getCenterX() <= platformRight) {
                setY(getY() - 10); // færir myndina upp um 10 pixla ef hún lendir ofan á platform
            } else {
                // lagar myndina til að varða frá því að hún fara í gegn eða ýtist upp á pallinn
                double ballLeft = getBoundsInParent().getMinX();
                double ballRight = getBoundsInParent().getMaxX();
                if (ballRight >= platformLeft && ballLeft <= platformRight) {
                    if (getBoundsInParent().getCenterY() < platformTop) {
                        setY(platformTop - getImageHeight());
                    } else if (getBoundsInParent().getCenterY() > platformTop + d.getBoundsInParent().getHeight()) {
                        setY(platformTop + d.getBoundsInParent().getHeight());
                    }
                } else if (getBoundsInParent().getCenterX() < platformLeft) {
                    setX(platformLeft - getImageWidth());
                } else if (getBoundsInParent().getCenterX() > platformRight) {
                    setX(platformRight);
                }
            }
        }
        return intersects;
    }

    /**
     * færir boltann til vinstri eða hægri á pallinum.
     */
    public void aframAPalli() {
        if (getStefna() != Stefna.NIDUR) {
            Leikbord parent = (Leikbord) this.getParent();
            setX(getX() < 0 ? parent.getWidth() : (int) (getX() + Math.cos(Math.toRadians(getStefnaGradur())) * hradi) % parent.getWidth());
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

    // Ákvað hér að snúa (rotate()) ekki hlutnum því ég er með bolta - en það mætti fara þá leið ef vill
    // getterar og setterar
    public double getStefnaGradur() {
        return stefna.getGradur();
    }

    // Ákvað hér að snúa (rotate()) ekki hlutnum því ég er með bolta - en það mætti fara þá leið ef vill
    // Hér væri þá setRotate()  og svo getRotate() í getStefna
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
