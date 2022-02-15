package sample;

public class State {
    public Dimension selectedDimension = Dimension.ONEDIMENSION;
    public Algorithms selectedAlgorithm = Algorithms.FIRST_FIT;
    public String energyList;
    public String iterList;
    static State state;

    public static State getState() {
        if(state == null){
            state = new State();
        }
            return state;
    }

    private State() {

    }
}
