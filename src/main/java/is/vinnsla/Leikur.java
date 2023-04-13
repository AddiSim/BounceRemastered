package is.vinnsla;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Map;
import java.util.HashMap;

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

    public void setHighScore(String name, int points) {
        Map.Entry<String, Integer> entry = new HashMap.SimpleEntry<>(name, points);
        stigatafla.add(entry);
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
