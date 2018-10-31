package dockerphase1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class DockerPhase1 {

    public static void main(String[] args) throws IOException {

        Scanner s = new Scanner(System.in);

        System.out.println("Enter your C code to execute: ");
        System.out.println("To finish, type *end* on a single line");

        File file = new File("main.c");
        file.createNewFile();

        FileWriter fileWriter = new FileWriter(file.getName());
        PrintWriter printWriter = new PrintWriter(fileWriter);

        String input = s.nextLine();
        while (!input.equals("*end*")) {
            printWriter.println(input);
            input = s.nextLine();
        }

        printWriter.close();

        Runtime rt = Runtime.getRuntime();
        Process prBuild = rt.exec("docker build -t mycode .");

        BufferedReader buildReader = new BufferedReader(new InputStreamReader(prBuild.getErrorStream()));
        BufferedReader buildOutput = new BufferedReader(new InputStreamReader(prBuild.getInputStream()));

        String str = buildOutput.readLine();
        while (str != null) {
            System.out.println(str);
            str = buildOutput.readLine();
        }
        str = buildReader.readLine();
        if (str != null) {
            while (str != null) {
                System.out.println(str);
                str = buildReader.readLine();
            }
        } else {

            Process pr = rt.exec("docker run mycode");

            BufferedReader r = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            BufferedReader e = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

            str = e.readLine();
            while (str != null) {
                System.out.println(str);
                str = e.readLine();
            }

            str = r.readLine();
            while (str != null) {
                System.out.println(str);
                str = r.readLine();
            }    
        }
          
        rt.exec("docker rmi -f mycode");
        
        file.delete();
    }
}
