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
        while(!input.equals("*end*")) {
            printWriter.println(input);
            input = s.nextLine();
        }
        
        printWriter.close();
        
        Runtime rt = Runtime.getRuntime();
        rt.exec("docker build -t mycode .");
        Process pr = rt.exec("docker run mycode");
        
        BufferedReader r = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        
        BufferedReader e = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
      
        
        String str = e.readLine(); 
        while(str != null) {
            System.out.println(str);
            str = e.readLine();
        }
        
        str = r.readLine();
        while(str != null) {
            System.out.println(str);
            str = r.readLine();
        }   
        rt.exec("docker stop mycode");
    } 
}
