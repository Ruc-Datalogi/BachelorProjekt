package sample;

import java.util.ArrayList;

public class State {
    public Dimension selectedDimension = Dimension.ONEDIMENSION;
    public Algorithms selectedAlgorithm = Algorithms.FIRST_FIT;
    public ArrayList<Module> modules = new ArrayList<>();
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
