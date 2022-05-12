package sample;

import java.util.*;

public class SequencePairs extends Algorithm {
    ArrayList<Integer> positiveSequence;
    ArrayList<Integer> negative;
    ArrayList<Module> modules;
    HashMap<Integer, Integer> mapPostive = new HashMap<>();
    HashMap<Integer, Integer> mapNegative = new HashMap<>();
    boolean rotate = false;
    int worstIdHorizontal;
    int iterationsSinceBest = 0;
    int bestDist = Integer.MAX_VALUE;
    float bestOptimazitionFactor = Integer.MAX_VALUE;
    public Bin2D bestBin = new Bin2D();
    Bin2D testBin;
    HashSet<ArrayList<ArrayList<Integer>>> solutionSet = new HashSet<>();

    public SequencePairs(ArrayList<Integer> positive, ArrayList<Integer> negative, ArrayList<Module> modules) {
        this.positiveSequence = positive;
        this.negative = negative;
        this.modules = modules;
        for(int i = 0 ; i < positive.size() ; i++) {
            mapPostive.put(positive.get(i), i);
        }
        for(int i = 0 ; i < negative.size() ; i++) {
            mapNegative.put(negative.get(i), i);
        }
    }
    public void semiNormalizePlacements(){
        // we have two extreme modules: s and t
        //s represents the module placed at (0,infinite)
        //t represents the module placed at (infinite,0)
        NormModules source = new NormModules(modules.size()+1, 0,Integer.MAX_VALUE);
        NormModules target = new NormModules(modules.size()+2,Integer.MAX_VALUE,0);
        System.out.println("SourceID: " + source.id);
        System.out.println("TargetID: " +target.id);
        ArrayList<NormModules> normiesOut = new ArrayList<>();
        int[] moduleMapping = new int[modules.size()+4];

        int placement=0;
        for(Module mod : modules){
            //we make normModules which has coordinates :)

            normiesOut.add(new NormModules(mod.id,mod.width,mod.height));
            moduleMapping[mod.id]=placement;
            //System.out.println("adding id:" + mod.id + " at pos:" + placement);
            placement++;

        }

        normiesOut.add(source);
        moduleMapping[source.id]=placement;
        normiesOut.add(target);
        moduleMapping[target.id]=placement+1;


        //We have two sequences, the positive and the negative.
        //In this example we call the positive list for A, and the negative list for B
        ArrayList<Integer> normA = (ArrayList<Integer>) positiveSequence.clone();
        ArrayList<Integer> normB = (ArrayList<Integer>) negative.clone();

        //We add s and t to our normA
        //s gets added at the first value, and t is at the end of the arraylist
        normA.add(0,source.id);
        normA.add(target.id);

        //We then have a list of extreme modules, which initially only contains s and t
        LinkedList<Integer> extremed = new LinkedList<>();
        extremed.add(source.id);
        extremed.add(target.id);

        ArrayList<Integer> extremes = new ArrayList<>();
        extremes.add(source.id);
        extremes.add(target.id);

        //We go through each of the elements in the negative list
        for(Integer h : normB){
            //We consider this of this element to be H and we need to find the element that is positioned before H in normA

            //Tempoarily add h to extremes :)
            //Find the elements we have in common between the extreme list and the normA
            List<Integer> intersects= CommonFunctions.getIntersect(normA,extremes,h);
            int h_A = intersects.indexOf(h);
            //List<Integer> extremeCommon=CommonFunctions.getCommon()
            //List<Integer> prev =intersects.subList(0,h_A);
            //List<Integer> after=intersects.subList(h_A,intersects.size());
            int prevId,afterId;
            if(h_A==-1){
                /*System.out.println("h id is: " + h);
                System.out.println("Extremes: " + extremes);
                System.out.println("normA: " + normA);
                System.out.println("Intersex: " + intersects);

                 */
                prevId=intersects.get(0);
                afterId=intersects.get(intersects.size()-1);
            }else {
                //System.out.println(intersects);
                //System.out.println("h's position in intersects is: " + h_A+ " h is:" +h);
                prevId = intersects.get(h_A - 1);
                afterId = intersects.get(h_A + 1);
            }
            //We now need a x & y based on the id's weights

            //We get the module with id h from our normModules

            NormModules hMod = normiesOut.get(moduleMapping[h]);
            //We get the previous module for setting the x value
            NormModules prevNormMod=normiesOut.get(moduleMapping[prevId]);
            hMod.setX(prevNormMod.getX()+prevNormMod.width);
            //We get the after module to set the y value
            NormModules afterNormMod=normiesOut.get(moduleMapping[afterId]);
            hMod.setY(afterNormMod.getY()+afterNormMod.height);
            //System.out.println("Now mod" + h + " has dimensions:" + hMod.x + "," + hMod.y);
            prevNormMod.PrintDimensions();
            afterNormMod.PrintDimensions();
            //Add h to extremes after prevId's position
            extremes.add(extremes.indexOf(prevId)+1,h);

            //Remove extremes that are no longer extremes (shadowed)
            //That means they are no next to one of the two modules s or t in the extreme list
            //So in essence we remove the middle element of extremes when extremes is of size 5
            if(extremes.size()==5){
                extremes.remove(2);
            }


        }
        NormModules widthExtreme= normiesOut.get(moduleMapping[extremes.get(extremes.size()-2)]);
        NormModules heightExtreme= normiesOut.get(moduleMapping[extremes.get(1)]);
        System.out.println(extremes);
        int thisWidth=widthExtreme.getX()+widthExtreme.width;
        System.out.println("wId:"+widthExtreme.id);
        System.out.println("hId:"+ heightExtreme.id);
        System.out.println("widthX:" + widthExtreme.getX());
        System.out.println("heightY:"+ heightExtreme.getY());
        int thisHeight= heightExtreme.getY()+ heightExtreme.height;
        System.out.println("Our square is:" + thisWidth +","+thisHeight+ " awea:" + thisWidth*thisHeight);
    }


    public void calculatePlacementTable(){
        for(Module mod : modules){
            if(mod.id == worstIdHorizontal){
                rotate = false;
                mod.rotate();
            }

            int posiPosition = mapPostive.get(mod.id);
            mod.setPositiveIndex(posiPosition);
            int negiPositon = mapNegative.get(mod.id);
            mod.setNegativeIndex(negiPositon);

            List<Integer> leftPosSlice   = positiveSequence.subList(0, posiPosition);
            List<Integer> rightPosSlice  = positiveSequence.subList(posiPosition+1, positiveSequence.size() );
            List<Integer> leftNegiSlice  = negative.subList(0, negiPositon);
            List<Integer> rightNegiSlice = negative.subList(negiPositon+1, negative.size() );

            mod.leftOf  = CommonFunctions.getCommon(leftPosSlice, leftNegiSlice);    //TODO this is slow
            mod.rightOf = CommonFunctions.getCommon(rightPosSlice, rightNegiSlice);
            mod.above   = CommonFunctions.getCommon(rightNegiSlice,leftPosSlice);
            mod.below   = CommonFunctions.getCommon(leftNegiSlice,rightPosSlice);
        }

        AdjanceyGraph thcg = new AdjanceyGraph();
        AdjanceyGraph tvcg = new AdjanceyGraph();

        Vertex sourceH = new Vertex(0,-1);
        Vertex targetH = new Vertex(0, -2);
        Vertex sourceV = new Vertex(0, -1);
        Vertex targetV = new Vertex(0, -2);

        //construct the graph according to the positive and negative sequences
        for(Module mod : modules){
            Vertex vertexH = new Vertex(mod.width, mod.id);
            thcg.vertices.add(vertexH);

            Vertex vertexV = new Vertex(mod.height, mod.id);
            tvcg.vertices.add(vertexV);
        }

        Collections.sort(modules); // Sort the modules to make sure the id matches with the index :)
        createTempGraph(thcg, modules, sourceH, targetH, true);
        createTempGraph(tvcg, modules, sourceV, targetV, false);

        thcg.vertices.add(sourceH);
        thcg.vertices.add(targetH);
        tvcg.vertices.add(sourceV);
        tvcg.vertices.add(targetV);

        int dist1=sourceH.DFS_New();
        int dist2 = sourceV.DFS_New();
        super.optimizationFactor = dist2*dist1; // the variable we optimise for.

        iterationsSinceBest++;

        if (optimizationFactor < bestDist) {
            testBin = TEMPgenerateCoordinatesForModules(thcg, tvcg, dist1, dist2);
            //bestBin = testBin;
            bestDist = (int) optimizationFactor;
            //Please do not spam my console @Mads
            //System.out.println("Best (" + dist1 + "," + dist2 +" iterations: " + iterationsSinceBest + ") = " + dist1 *dist2);
            iterationsSinceBest = 0;
            PrimaryWindow.changeDebugMessage("Best (" + dist1 + "," + dist2 +" iterations: " + iterationsSinceBest + ") = " + dist1 *dist2 +"\n" + "Hori " + thcg.toString() + "\n" + "Verti" + tvcg.toString());
        }
    }


    private int worstRoute(Vertex input) {
        Vertex biggest = input;
        Vertex currentVertex = input;
        ArrayList<Vertex> list = new ArrayList<>();
        Random r = new Random();
        while (currentVertex.previousVertex != null) {
            currentVertex = currentVertex.previousVertex;
            if(biggest.weight < currentVertex.weight && currentVertex.id > 0) {
                biggest = currentVertex;
                list.add(currentVertex);
            }
        }
        return list.get(r.nextInt(list.size())).id;
    }


    //TODO Slow
    public static void createTempGraph(AdjanceyGraph graph, ArrayList<Module> modules, Vertex source, Vertex target, boolean isHorizontal) {
        for (Vertex v : graph.vertices) {
            Module thisMod = modules.get(v.id - 1);

            if (isHorizontal) {
                if (thisMod.leftOf.size() == 0) {
                    source.neighbors.add(v);
                }
                if (thisMod.rightOf.size() == 0) {
                    v.neighbors.add(target);
                } else {
                    thisMod.rightOf.forEach(index -> v.neighbors.add(graph.vertices.get(index - 1)));
                }
            } else {
                if (thisMod.below.size() == 0) {
                    source.neighbors.add(v);
                }
                if (thisMod.above.size() == 0) {
                    v.neighbors.add(target);
                } else {
                    thisMod.above.forEach(index -> v.neighbors.add(graph.vertices.get(index - 1)));
                }
            }
        }
    }

    Bin2D TEMPgenerateCoordinatesForModules(AdjanceyGraph horizontal, AdjanceyGraph vertical, int wH, int wV){
        Bin2D bin = new Bin2D(5000,5000); //TODO don't hardcode fucking values
        int scalar = 800/Math.max(wH, wV);

        for(Vertex vy : vertical.vertices){
            for(Vertex vx : horizontal.vertices){
                if(vx.id == vy.id && vx.id > 0){
                    Module currentMod = modules.get(vx.id-1);

                    Box2D currentBox = new Box2D((wH-vx.distToTarget-vx.weight)*scalar, (wV-vy.distToTarget-vy.weight)*scalar , currentMod.width*scalar, currentMod.height*scalar);
                    currentBox.setId(vx.id);
                    bin.addBox(currentBox);
                }
            }
        }
        return bin;
    }

    int DFSIt=0;
    private int DFS(Vertex input) {
        DFSIt=0;
        int dfsDist=DFSExplore(input, 0, 0);
        return dfsDist;
    }

    private int DFSExplore(Vertex input, int depth, int maxDepth) {
        DFSIt++;
        for(Vertex v : input.neighbors) {
            if (depth + v.weight > maxDepth) {
                maxDepth = depth + v.weight;
            }
            if (v.maxDepth < depth) {
                v.setPreviousVertex(input);
                v.setMaxDepth(depth);
            }
            maxDepth = DFSExplore(v, depth + v.weight, maxDepth);
        }
        return maxDepth;
    }

    private void swapInMap(HashMap<Integer, Integer> map, int id1 , int id2) {
        Integer tempValue = map.get(id1);
        map.put(id1, map.get(id2));
        map.put(id2, tempValue);
    }

    @Override
    void execute() {

        //int swap1;
        Random random = new Random();

        int randomIndex1 = random.nextInt(positiveSequence.size());
        int randomIndex2 = random.nextInt(positiveSequence.size());

        int id1 = positiveSequence.get(randomIndex1);
        int id2 = positiveSequence.get(randomIndex2);

        switch (random.nextInt(0, 2)) {
            case 0 -> { // Dual swap
                Collections.swap(positiveSequence, randomIndex1, randomIndex2);
                swapInMap(mapPostive, id1, id2);
                Collections.swap(negative, mapNegative.get(id1), mapNegative.get(id2));
                swapInMap(mapNegative, id1, id2);
            }
            case 1 -> { // Single Swap Positive
                Collections.swap(positiveSequence, randomIndex1, randomIndex2);
                swapInMap(mapPostive, id1, id2);
            }
            case 2 -> { // Single Swap Negative
                Collections.swap(negative, randomIndex1, randomIndex2);
                swapInMap(mapNegative, id1, id2);
            }
            case 5 -> // Rotate
                    rotate = true;
        }

        ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();
        solutions.add(positiveSequence);
        solutions.add(negative);
        this.solution = solutions;

        if (solutionSet.add(solutions)) {
            this.calculatePlacementTable(); // clean the table //TODO figure out if it needs to be earlier
        }
    }
}

class Module implements Comparable<Module>{
    int id;
    int width;
    int height;
    int positiveIndex=-1;
    int negativeIndex=-1;
    int realdId = - 1;
    SubProblem subProblem = new SubProblem();
    ArrayList<Module> subModules = new ArrayList<>();

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", width=" + width +
                ", height=" + height +
                ", RightOf=" + rightOf +
                ", LeftOf=" + leftOf +
                ", Above=" + above +
                ", Below=" + below +
                '}';
    }

    List<Integer> rightOf = new ArrayList<>();
    List<Integer> leftOf  = new ArrayList<>();
    List<Integer> above   = new ArrayList<>();
    List<Integer> below   = new ArrayList<>();

    public Module(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public void setPositiveIndex(int positiveIndex) {
        this.positiveIndex = positiveIndex;
    }

    public void setNegativeIndex(int negativeIndex) {
        this.negativeIndex = negativeIndex;
    }

    public void rotate(){
        int temp = this.width;
        this.width = this.height;
        this.height = temp;
    }

    @Override
    public int compareTo(Module otherMod) {
        if(this.id>otherMod.id){
            return 1;
        }else if (this.id<otherMod.id){
            return -1;
        }
        return 0;
    }

}
class NormModules extends Module{
    int x,y;
    public NormModules(int id, int width, int height) {
        super(id, width, height);
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public void PrintDimensions(){
        System.out.println("Dims of " + this.id + ": " + this.width + "," + this.height);
    }
}