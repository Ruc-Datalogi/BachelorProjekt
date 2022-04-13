package sample;


import java.util.ArrayList;


public class GenerateRectangleDataSet {
    int maxW,maxH;
    int minAnyDimension=20;

    ArrayList<Simple2DBox> BoxArray = new ArrayList<Simple2DBox>();

    public GenerateRectangleDataSet(int maxH, int maxW) {
        Simple2DBox ourBox=new Simple2DBox(maxW,maxH);
        SliceBox(ourBox);
        System.out.println("For ");
        System.out.println(BoxArray);

    }

    boolean sliceW=false;
    void SliceBox(Simple2DBox box){
        int newW;
        int newH;
        Simple2DBox A,B;
        if (sliceW & (box.w*2)>minAnyDimension){
            newH=box.h;
            newW= (int)(Math.random() * (box.w-minAnyDimension));
            A = new Simple2DBox(newW,newH);
            B = new Simple2DBox(box.w-newW,newH);
            sliceW=!sliceW;
            SliceBox(A);
            SliceBox(B);
        }else if (!sliceW & (box.h*2)>minAnyDimension){
            newH=(int)(Math.random() * (box.h-minAnyDimension));
            newW= box.w;
            A = new Simple2DBox(newW,newH);
            B = new Simple2DBox(newW,box.h-newH);
            sliceW=!sliceW;
            SliceBox(A);
            SliceBox(B);
        }else{
            //We cannot or will not slice this box any further so we add it to the tally
            BoxArray.add(box);
        }



    }
}
class Simple2DBox{
    int w;
    int h;
    Simple2DBox(int w,int h){
        //Geneating a box with dimensions w & h
        this.w=w;
        this.h=h;
    }
    //When slicing we create two "childs" from parents dimensions
    //We either slice on the width or the height


}