package sample;

import java.util.ArrayList;

public class Bin2D {
    int w,h;
    Point2D iteratorPoint;
    int furthestW=0;
    ArrayList<Box2D> containedBoxes = new ArrayList<Box2D>();
    public Bin2D(int width, int height) {
        this.w = width;
        this.h = height;
        this.iteratorPoint =new Point2D(0,height);
    }

    //First we check if the box can be placed on top of the iterator point
    //We check
    boolean canContain(Box2D box){
        //First check if the box can be empty Bin first:
        if(box.h>this.h || box.w>this.h){
            //Box cannot be inside bin ever
            return false;

            //Check if box can be placed above iteratorPoint

        }else if(pointInsideAnyBox(new Point2D(iteratorPoint.getX()+box.w, iteratorPoint.getY()))){
            return false;
        }
        return true;
    }

    //Not optimal way to do this but we check if a point is contained in ALL the boxes inside this bin.
    boolean pointInsideAnyBox(Point2D p){
        for(Box2D box : containedBoxes){
            if (box.containsPoint(p)){
                return true;
            }
        }
        return false;
    }

    boolean addBox(Box2D box){

        containedBoxes.add(box);
        return true;


    }
}
