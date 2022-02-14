package sample;

public class BoundaryBox {
    //opposite corners, A: topleft, B: bottomright
    private Point2D cornerA,cornerB;

    //C and D gets generated if needed
    private Point2D cornerC,cornerD;

    public BoundaryBox(Point2D cornerA, Point2D cornerB) {
        this.cornerA = cornerA;
        this.cornerB = cornerB;
    }
    private boolean hasExtraCorners(){
        return cornerC!=null;
    }
    int getMinX() {
        if (hasExtraCorners()){
            return Math.min(Math.min(cornerA.getX(),cornerB.getX()),Math.min(cornerC.getX(),cornerD.getX()));
        }
        return Math.min(cornerA.getX(),cornerB.getX());
    }

    int getMaxX() {
        if (hasExtraCorners()){
            return Math.max(Math.max(cornerA.getX(),cornerB.getX()),Math.max(cornerC.getX(),cornerD.getX()));
        }
        return Math.max(cornerA.getX(),cornerB.getX());
    }

    int getMinY() {
        if (hasExtraCorners()){
            return Math.min(Math.min(cornerA.getY(),cornerB.getY()),Math.min(cornerC.getY(),cornerD.getY()));
        }
        return Math.min(cornerA.getY(),cornerB.getY());
    }

    int getMaxY() {
        if (hasExtraCorners()){
            return Math.max(Math.max(cornerA.getY(),cornerB.getY()),Math.max(cornerC.getY(),cornerD.getY()));
        }
        return Math.max(cornerA.getY(),cornerB.getY());
    }
    boolean hasOverlap(BoundaryBox other){
        //TODO add logic for boundary box overlap
        return false;

    }
}
