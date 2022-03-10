package sample;

//A 2 dimensional box with a width and a height
public class Box2D extends BoundaryBox{
    int w,h, id;
    public Box2D(int width, int height) {
        super(new Point2D(0,0),new Point2D(width,height));
        w=width;
        h=height;

    }
    public Box2D(int x, int y, int width, int height){
        super(new Point2D(x,y),new Point2D(width+x,height+y));
        w=width;
        h=height;
    }

    public void setId(int id) {
        this.id = id;
    }

}
