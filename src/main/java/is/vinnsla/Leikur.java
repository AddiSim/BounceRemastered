package is.vinnsla;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import java.util.Map;


public class Leikur {

    private final ObservableList<Map.Entry<String, Integer>> stigatafla = FXCollections.observableArrayList();
    private final IntegerProperty stigin = new SimpleIntegerProperty();

    public IntegerProperty stiginProperty() {
        return stigin;
    }

    public int getStigin() {
        return stigin.get();
    }

    public void haekkaStigin() {
        stigin.setValue(stigin.getValue() + 1);
    }

    public void nyrLeikur() {
        stigin.setValue(0);
    }

    public ObservableList<Map.Entry<String, Integer>> getTotalStig() {
        return stigatafla;
    }

    public String getHighScoreName() {
        String highScoreName = "";
        int highScore = 0;
        for (Map.Entry<String, Integer> entry : stigatafla) {
            int score = entry.getValue();
            if (score > highScore) {
                highScore = score;
                highScoreName = entry.getKey();
            }
        }
        return highScoreName;
    }

    public void setHighScore(String name, int points, Label fxHighscore) {
        boolean isNewHighScore = false;

        // Check if the points are higher than the current high score
        String highScoreName;
        int highScore;
        if (points > getHighScore()) {
            isNewHighScore = true;
            highScoreName = name;
            highScore = points;
        } else {
            highScoreName = getHighScoreName();
            highScore = getHighScore();
        }

        // Update the highscore label if it's a new high score
        if (isNewHighScore) {
            fxHighscore.setText(name + " " + points);
        } else {
            fxHighscore.setText(highScoreName + " " + highScore);
        }

        // Add the name and points to the stigatafla map regardless of whether it's a new high score or not
        stigatafla.add(Map.entry(name, points));
    }



    public int getHighScore() {
        int highScore = 0;
        for (Map.Entry<String, Integer> entry : stigatafla) {
            int score = entry.getValue();
            if (score > highScore) {
                highScore = score;
            }
        }
        return highScore;
    }
}
