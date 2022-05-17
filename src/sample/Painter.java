package sample;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Painter {
    Canvas canvas;
    GraphicsContext gc;
    final private int BOX1D_WIDTH = 32;
    final private int BOX1D_HEIGHT = 32;

    public Painter(Canvas c) {
        this.canvas = c;
        this.gc = c.getGraphicsContext2D();
        fillBlank();
    }

    public void fillBlank() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
    }

    public void drawBox1D(int x, int y, Bin1D bin1D) {
        gc.setFill(Color.LIGHTGREY);
        gc.fillRect(x, y, BOX1D_WIDTH, BOX1D_HEIGHT);
        gc.setFill(Color.WHITE);
        gc.fillRect(x+1, y+1, BOX1D_WIDTH-2, BOX1D_HEIGHT-2);
        gc.setFill(Color.BLACK);
        gc.fillText(String.valueOf(bin1D.capacity), x + BOX1D_WIDTH/4 , y + BOX1D_HEIGHT/2);
    }

    public void drawBox2D(ArrayList<NormModules> normModules){
        int iterator=0;
        for(NormModules module : normModules){
            gc.setFill(Color.BLACK);
            gc.fillRect(module.getX(), module.getY(),module.width,module.height);
            gc.setFill(Color.hsb(iterator,1,1));
            iterator+=55;
            gc.fillRect(module.x + 1,module.y+1,module.width-2,module.height-2);
            gc.setFill(Color.BLACK);
        }
        //gc.fillText(String.valueOf(bin.capacity), x + BOX1D_WIDTH/4 , y + BOX1D_HEIGHT/2);
    }
    public void drawBoxesInBin(Bin2D bin){
        int iterator=0;
        for(Box2D box : bin.containedBoxes){
            gc.setFill(Color.BLACK);
            gc.fillRect(box.getCornerA().x,box.getCornerA().y,box.w,box.h);
            gc.setFill(Color.hsb(iterator,1,1));
            iterator+=55;
            gc.fillRect(box.getCornerA().x + 1,box.getCornerA().y+1,box.w-2,box.h-2);
            gc.setFill(Color.BLACK);
            String s = "[ id: "  + box.id + "]" +"\n" + "[" + box.w + "," + box.h + "]";
            String s_id = "[ id: "  + box.id + "]";
            gc.fillText(s_id ,box.getCornerA().x + box.w/3, box.getCornerA().y + box.h/3);
        }
    }

    public void drawBoxes(AdjanceyGraph tH, AdjanceyGraph tV) {
        ArrayList<Integer> x_cor = new ArrayList<>();

    }

    public void drawGraph(ArrayList<Integer> positive, ArrayList<Integer> negative){
        /*
        gc.setFill(Color.BLACK);
        gc.setLineWidth(5);
        int scale = 250;
        gc.strokeLine(canvas.getWidth()/2+scale,canvas.getHeight()/2,canvas.getWidth()/2,canvas.getHeight()/2+scale);
        gc.strokeLine(canvas.getWidth()/2-scale,canvas.getHeight()/2,canvas.getWidth()/2,canvas.getHeight()/2-scale);
        gc.strokeLine(canvas.getWidth()/2+scale,canvas.getHeight()/2,canvas.getWidth()/2,canvas.getHeight()/2-scale);
        gc.strokeLine(canvas.getWidth()/2-scale,canvas.getHeight()/2,canvas.getWidth()/2,canvas.getHeight()/2+scale);
*/

/*

        for (int i = 0; i < positive.size()-1; i++) {
            for (int j = 0; j < negative.size()-1; j++) {
                if(positive.get(i) == negative.get(j)){


                }

            }
        }

 */
    }

}
