package sample;

import java.util.ArrayList;

public class Bin2D {
    int w,l;
    Point2D iteratorPoint;
    ArrayList<Box2D> containedBoxes = new ArrayList<Box2D>();
    public Bin2D(int width, int length) {
        this.w = width;
        this.l = length;
        this.iteratorPoint =new Point2D(0,length);
    }

    //First we check if the box can be placed on top of the iterator point
    //We check
    boolean canContain(Box2D box){
        //First check if the box can be empty Bin first:
        if(box.l>this.l || box.w>this.l){
            //Box cannot be inside bin ever
            return false;
        }else if()
        return false;
    }
    void addBox(Box2D box){
        containedBoxes.add(box);

    }
}
