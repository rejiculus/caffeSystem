package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserIOHelper {
    
    private static BufferedReader br;
    private static UserIOHelper instance;

    private UserIOHelper(){
        br = new BufferedReader(new InputStreamReader(System.in));
    }
    public static UserIOHelper getInstance(){
        if(instance==null)
            instance = new UserIOHelper();
        return instance;
    }

    public String input(String message){
        System.out.print(message);
        String res = "";
        try{
            res = br.readLine();
        } catch(IOException e){
            System.out.println(e);
        }
        return res;
    }
   

    public void out(String message){
        System.out.print(message);
    }
    public void close(){
        try {
            if(br!=null && br.ready())
                br.close();
        } catch (IOException e) {
            System.out.println(e);
        }
            
    }

}
