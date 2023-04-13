package is.vidmot;


public class Pallur1 extends Pallur {

    public Pallur1() {
        FXML_Lestur.lesa(this, "Pallur-view2.fxml");
    }

    @Override
    public void afram() {
        Leikbord leikbord = (Leikbord) getParent(); // ná í foreldrið
        Bolti bolti = leikbord.getBolti(); // ná í boltann á leikborðinu
        if (getY() < 0) {   // er pallurinn kominn efst
            athugaBoltinnEfst(leikbord, bolti);
        } else
            setY(getY() - 3); // færa pallinn upp um eitt skref
    }

    /**
     * Pallurinn er efst. Ef bolti fylgir pallinum
     * @param l
     * @param bolti boltinn sem verið er að athuga hvort er á palli
     */
    private void athugaBoltinnEfst(Leikbord l, Bolti bolti) {
        if (bolti.erAPalli(this)) { // Ef boltinn á þessum palli
            l.hendaBoltaAfPalli(this); // henda boltanum af palli
        }
        double platformWidth = getFitWidth();
        double boardWidth = l.getWidth();
        double randomX = Math.random() * (boardWidth - platformWidth); // generate a random x-coordinate
        setX(randomX); // set the x-coordinate of the platform
        setY(l.getHeight() - getFitHeight()); // staðsetja pallinn neðst
    }
}
