package sample;


import java.util.ArrayList;
import java.util.Random;


public class GenerateRectangleDataSet {
    int minAnyDimension=20;
    int index;

    ArrayList<Simple2DBox> BoxArray = new ArrayList<Simple2DBox>();

    public GenerateRectangleDataSet(int maxW, int maxH) {
        Simple2DBox ourBox=new Simple2DBox(maxW,maxH);
        SliceBox(ourBox);
        System.out.println(BoxArray.size());
    }

    boolean sliceW=false;
    void SliceBox(Simple2DBox box){
        int newW;
        int newH;
        Simple2DBox A,B;
        index ++;
        if (sliceW && (box.w)>minAnyDimension*2){
            newH=box.h;
            //newW= (int)((Math.random() * (box.w-minAnyDimension*2) + minAnyDimension));
            newW = getIntegerBetweenValues(minAnyDimension, box.w - minAnyDimension);
            A = new Simple2DBox(newW,newH);
            B = new Simple2DBox(box.w-newW,newH);
            sliceW=!sliceW;
            SliceBox(A);
            SliceBox(B);
        }else if (!sliceW && (box.h)>minAnyDimension*2){
            newH = getIntegerBetweenValues(minAnyDimension, box.h - minAnyDimension);
            //newH=(int)((Math.random() * (box.h-minAnyDimension*2) + minAnyDimension));
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

    private int getIntegerBetweenValues(int lower, int upper ) {
        Random r = new Random();
        return r.nextInt(upper-lower) + lower;
    }

    public SequencePairs generateSeq() {
        ArrayList<Integer> positive = new ArrayList<>();
        ArrayList<Integer> negative = new ArrayList<>();
        ArrayList<Module> modules = new ArrayList<>();

        for (int i = 0; i < BoxArray.size() ; i ++) {
            positive.add(i + 1);
            negative.add(i + 1);
            modules.add(new Module(i+1, BoxArray.get(i).w, BoxArray.get(i).h));
        }

        SequencePairs sequencePairs = new SequencePairs(positive, negative, modules);

        return  sequencePairs;
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


    @Override
    public String toString() {
        return "Simple2DBox{" +
                "w=" + w +
                ", h=" + h +
                '}';
    }
}