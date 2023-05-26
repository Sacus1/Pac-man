import java.util.ArrayList;

public interface Observable {
    ArrayList<Observer> observers = new ArrayList<>();

    default void addObserver(Observer o) {
        observers.add(o);
    }
    default void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }
}
