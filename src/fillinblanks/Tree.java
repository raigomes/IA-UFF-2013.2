/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fillinblanks;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author rai
 */
public class Tree {
    private Node init;
    
    private final String path = "";
    private final String heuristicFileName = "";

    public Tree() {
        init = new Node();
    }
    
    public Node getInit() {
        return init;
    }
    
    public void AEstrela() {
        List<Node> frontier = new LinkedList<Node>();
        Map<String, Float> heuristic = getHeuristicFromFile();
        
        
        
    }

    private Map<String, Float> getHeuristicFromFile() {
        Map<String, Float> heuristic = new HashMap<String, Float>();
        Scanner file = new Scanner(path + heuristicFileName);
        
        while(file.hasNextLine()) {
            String line = file.nextLine();
            String key = line.substring(0, 2);
            float value = Float.parseFloat(line.substring(4));
            
            heuristic.put(key, value);
        }
        
        file.close();
        
        if(heuristic.size() > 0){            
            return heuristic;
        }
        else{
            System.out.println("Heuristica vazia!");
            return null;
        }
            
    }
}
