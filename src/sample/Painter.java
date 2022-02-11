package sample;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Painter {
    Canvas canvas;
    GraphicsContext gc;
    final private int BOX1D_WIDTH = 32;
    final private int BOX1D_HEIGHT = 32;

    public Painter(Canvas c) {
        this.canvas = c;
        this.gc = c.getGraphicsContext2D();
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

}
