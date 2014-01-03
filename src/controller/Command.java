/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import src.Tree;
import ui.UserInterface;

/**
 *
 * @author rai
 */
public class Command {
    private int type;    
    private String label, input;
    private static String path = "/home/rai/Documentos/IA 2013.2/Trabalho/Trab2/trab2/";
    
    public static final int EXIT = 0;
    public static final int ENTRY = 1;
    public static final int SEARCH = 2;
    public static final int PRINT = 3;

    public Command(String label, int type) {
        this.label = label;
        this.type = type;          
        input = "AEOPRRSWY";        
    }

    public Command(String label, int type, String input, String path) {
        this.label = label;
        this.type = type;
        this.input = input;
        Command.path = path;        
    }        

    public void execute() {
        switch(type) {
            case EXIT: 
                UserInterface.credits();
                System.exit(0);
            case ENTRY:
                setInput();
                break;
            case SEARCH:                
                search();
                break;
            case PRINT:
                print();
                break;
        }
    }

    public static String getPath() {
        return path;
    }        

    public String getInput() {
        return input;
    }    
    
    private void setInput() {        
        if(label.equals("Pre"))
            input = "AEOPRRSWY";
        else {
            if(label.equals("Aleatoria")) {
                String s = "";
                for (int i = 0; i < 9; i++) {            
                    char value = (char) (65 + (Math.random() * 25));
                    s += new Character(value);
                }
                input = s;
            }                               
            
            input = input.toUpperCase();
        }
        
    }

    private void search() {
        Tree t = new Tree();
        t.setPath(path);
        
        if(label.equals("AStar")) 
            t.search(input.toCharArray(), 0);
        
        else 
            if(label.equals("Hill"))
                t.search(input.toCharArray(), 1);
            else
                t.search(input.toCharArray(), 2);
    }
        
    public void print() {
        UserInterface.update(input);
    }   
    
}
