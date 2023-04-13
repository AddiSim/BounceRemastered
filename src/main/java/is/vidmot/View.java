package is.vidmot;

public enum View {
    LEIKBORD("Leikbord-view.fxml"),

    BOUNCING("bounce-view.fxml");


    private final String fileName;

    View(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
