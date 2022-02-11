package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

public enum Algorithms {
    FIRST_FIT("First Fit ", Dimension.ONEDIMENSION ), WORST_FIT("Worst Fit", Dimension.ONEDIMENSION), TESTER("TESTER", Dimension.TWODIMENSION);

    private String label;
    private Dimension dimension;

    Algorithms(String label, Dimension d) {
        this.label = label;
        this.dimension = d;
    }

    public String toString() {
        return label;
    }

    public static ArrayList<Algorithms> getAlgorithms (Dimension d){
        ArrayList<Algorithms> output = new ArrayList<>();
        Arrays.asList(Algorithms.values()).forEach(a -> {
            if(a.dimension == d){
                output.add(a);
            }
        });
        System.out.println(output.toString());
        return output;
    }
}
