/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author rai
 */
public class Log implements Observer{
    private StringBuilder log;
    private final String fileName = "log.txt";

    public Log() {
        log = new StringBuilder();        
    }        
    
    public void write() {
        try {
            FileWriter fw = new FileWriter(new File(fileName));
            BufferedWriter buff = new BufferedWriter(fw);
            
            buff.write(log.toString());
            
            buff.close();
        } 
        catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg != null)
            log.append(arg.toString());         
        else
            write();
    }
    
}
