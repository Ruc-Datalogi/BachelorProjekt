package sample;

public enum AlgorithmsTwoDimension {
    TESTER("TESTER");

    private String label;

    AlgorithmsTwoDimension(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
