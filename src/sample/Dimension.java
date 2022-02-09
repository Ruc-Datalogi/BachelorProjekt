package sample;

public enum Dimension {
    ONEDIMENSION("One Dimension"), TWODIMENSION("Two Dimension"), THREEDIMENSION("Three Dimension");


    private String label;

    Dimension(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
