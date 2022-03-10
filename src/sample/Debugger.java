package sample;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Debugger implements Runnable{
    private static Debugger debugger;
    private boolean printInConsole = true;
    private static Queue<String> stringQueue = new LinkedList<>();
    public Thread debugThread;


    protected Debugger() {

    }



    public static Debugger getDebugger() {
        if(debugger == null) {
            debugger = new Debugger();
        } return debugger;
    }


    @Override
    public void run() {
        while(printInConsole){
            if(stringQueue.size() == 0) {
                try {
                    debugThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                String currentLine = stringQueue.poll();
                System.out.println(currentLine);
            }
        }
    }

    public static void addToQueue(String s){
        stringQueue.add(s);
        getDebugger().debugThread.notify();
    }
}

class Handler{


}