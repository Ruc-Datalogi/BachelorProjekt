package sample;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class GenerateRectangleDataSet {
    int minAnyDimension=15;
    int index;

    ArrayList<Simple2DBox> BoxArray = new ArrayList<Simple2DBox>();
    ArrayList<Explicit2DBox> BoxTest = new ArrayList<Explicit2DBox>();

    public GenerateRectangleDataSet(int maxW, int maxH) {
        Simple2DBox ourBox=new Simple2DBox(maxW,maxH);

        SliceBox(ourBox);
        int totalArea=0;

        for (Simple2DBox B : BoxArray){
            totalArea+=B.w*B.h;
        }
        if(totalArea!=maxW*maxH){
            System.out.println("ERROR!: We generated boxes with " + totalArea +". But expected " + maxW*maxH);
        }

        System.out.println(BoxArray.size());
    }
    public void GenerateTestExplicitBoxes(int maxW,int maxH){
        Explicit2DBox startBox=new Explicit2DBox(maxW,maxH,0,0);
        SliceExplicitBox(startBox);
        System.out.println(BoxTest);
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

        return new SequencePairs(positive, negative, modules);
    }

    public DivideAndConquer generateDivideAndConquer() {
        ArrayList<Integer> positive = new ArrayList<>();
        ArrayList<Integer> negative = new ArrayList<>();
        ArrayList<Module> modules = new ArrayList<>();

        for (int i = 0; i < BoxArray.size() ; i ++) {
            positive.add(i + 1);
            negative.add(i + 1);
            modules.add(new Module(i+1, BoxArray.get(i).w, BoxArray.get(i).h));
        }

        return new DivideAndConquer(positive, negative, modules);
    }

    boolean sliceW=false;
    void SliceBox(Simple2DBox box){
        index ++;
        boolean canSliceW=box.w>minAnyDimension*2;
        boolean canSliceH=box.h>minAnyDimension*2;
        //If we can do both cuts then pick a random one to vary the boxes
        //If we can't do both the do the one we can
        //If we can't do any then add the box to the arraylist

        if(canSliceW){
            if(canSliceH) {
                //Pick a random one
                if(new Random().nextBoolean()){
                    SliceWidth(box);
                }else{
                    SliceHeight(box);
                }
            }else{
                //Slice W since sliceH not possible
                SliceWidth(box);
            }
        }else{
            //We can't slice W
            if(canSliceH){
                //We can still slice so we slice H
                SliceHeight(box);
            }else{
                //No slices possible so we add box to array
                BoxArray.add(box);
            }
        }
        /*
        if (box.w>minAnyDimension*2){
            SliceWidth(box);
        }else if (box.h>minAnyDimension*2){
            SliceHeight(box);
        }else{
            //We cannot or will not slice this box any further so we add it to the tally
            BoxArray.add(box);

        }*/
    }

    private void SliceHeight(Simple2DBox box) {

        int newW;
        int newH;
        newH = getIntegerBetweenValues(minAnyDimension, box.h - minAnyDimension);
        //newH=(int)((Math.random() * (box.h-minAnyDimension*2) + minAnyDimension));
        newW= box.w;
        Simple2DBox A = new Simple2DBox(newW,newH);
        Simple2DBox B = new Simple2DBox(newW, box.h-newH);
        SliceBox(A);
        SliceBox(B);
    }

    private void SliceWidth(Simple2DBox box) {
        int newW;
        int newH;
        newH= box.h;
        newW = getIntegerBetweenValues(minAnyDimension, box.w - minAnyDimension);
        Simple2DBox A = new Simple2DBox(newW,newH);
        Simple2DBox B = new Simple2DBox(box.w-newW,newH);
        SliceBox(A);
        SliceBox(B);
    }

    void SliceExplicitBox(Explicit2DBox box){
        int newW;
        int newH;
        Explicit2DBox A,B;
        index ++;

        if ((box.w)>minAnyDimension*2){
            newH=box.h;
            //newW= (int)((Math.random() * (box.w-minAnyDimension*2) + minAnyDimension));
            newW = getIntegerBetweenValues(minAnyDimension, box.w - minAnyDimension);
            //We slice in the X dimension, so A's x is box.x1
            A = new Explicit2DBox(newW,newH,box.x1,box.y1);
            //Box B's x is box.x1+newW
            B = new Explicit2DBox(box.w-newW,newH,box.x1+newW,box.y1);
            sliceW=!sliceW;
            SliceExplicitBox(A);
            SliceExplicitBox(B);
        }else if ((box.h)>minAnyDimension*2){
            newH = getIntegerBetweenValues(minAnyDimension, box.h - minAnyDimension);
            //newH=(int)((Math.random() * (box.h-minAnyDimension*2) + minAnyDimension));
            newW= box.w;
            //We slice along Y axis so box A will have box.y1
            A = new Explicit2DBox(newW,newH,box.x1,box.y1);
            //Box B will have box.y1+newH
            B = new Explicit2DBox(newW,box.h-newH,box.x1,box.y1+newH);
            sliceW=!sliceW;
            SliceExplicitBox(A);
            SliceExplicitBox(B);
        }else{
            //We cannot or will not slice this box any further so we add it to the tally
            BoxTest.add(box);

        }
    }

    private int getIntegerBetweenValues(int lower, int upper ) {
        Random r = new Random();
        return r.nextInt(upper-lower) + lower;
    }

    public static void generateDatasets(int n) throws Exception {
        for (int i = 0; i < n; i++) {
            GenerateRectangleDataSet rectangleDataSet = new GenerateRectangleDataSet(200,200);
            int rectangleAmnt = rectangleDataSet.BoxArray.size();


            int totalArea = 0;
            for (Simple2DBox box : rectangleDataSet.BoxArray) {
                totalArea += box.h*box.w;
            }

            if (totalArea != 40000) throw new Exception();

            StringBuilder csv = new StringBuilder();
            for (Simple2DBox box : rectangleDataSet.BoxArray) {
                csv.append(box.w).append(",").append(box.h).append("\n");
            }

            if (new File("src/TestSet/").listFiles().length < 50) CSVWriter.getCsvWriter().createAndWrite("src/TestSet/","TestSet_" + i + "_" + rectangleAmnt + ".csv", String.valueOf(csv));
        }
    }
}


class Explicit2DBox{
    int x1,x2,y1,y2;
    int w,h;
    Explicit2DBox(int w,int h,int deltaX, int deltaY){
        //Geneating a box with dimensions w & h
        this.w=w;
        this.h=h;
        this.x1=deltaX;
        this.x2=w+deltaX;
        this.y1=deltaY;
        this.y2=h+deltaY;

    }
    //When slicing we create two "childs" from parents dimensions
    //We either slice on the width or the height


    @Override
    public String toString() {
        return "\nExplicit2DBox{" +
                "(" + x1 +  "," + y1 +")\n"+
                "(" + x1 +  "," + y2 +")\n"+
                "(" + x2 +  "," + y2 +")\n"+
                "(" + x2 +  "," + y1 +")\n";
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