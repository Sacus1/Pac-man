public interface Observer {
    void update(Observable o, Object arg);
    default void update(Observable o){
        update(o, null);
    }
}
