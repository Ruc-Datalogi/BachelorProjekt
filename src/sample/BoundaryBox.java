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
    boolean containsPoint(Point2D p){
        //TODO test if logic for contains point works.
        return p.getX() >= this.getMinX() && p.getX() <= this.getMaxX() && p.getY() >= this.getMinY() && p.getY() <= this.getMaxY();
    }
    boolean hasOverlap(BoundaryBox other){
        //TODO test if logic for overlap is accurate
        /*

        if(ax1 > bx2 || bx1 > ax2 || ay1 > by2 || by1 > ay2)

         */
        if (this.getMinX() > other.getMaxX() ||
                other.getMinX()> this.getMaxX() ||
                this.getMinY()> other.getMaxY() ||
                other.getMinY()>this.getMaxY()){
            return true;
        }

        return false;

    }
}
