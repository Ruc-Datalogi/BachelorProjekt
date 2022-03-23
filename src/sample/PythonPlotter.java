package sample;

import java.lang.ProcessBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PythonPlotter {

        public void runPython(String xCor, String yCor) {

            String[] cmd = {
                    "python",
                    ".\\src\\sample\\graph_python.py",
                    xCor,
                    yCor
            };

            System.out.println(Arrays.stream(cmd).toList());
            try {
                System.out.println(xCor +"\n" + yCor);
                Runtime.getRuntime().exec(cmd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}

