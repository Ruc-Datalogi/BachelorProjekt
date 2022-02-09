package sample;

public enum AlgorithmsOneDimension {
    FIRST_FIT("First Fit "), WORST_FIT("Worst Fit");

    private String label;

    AlgorithmsOneDimension(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
