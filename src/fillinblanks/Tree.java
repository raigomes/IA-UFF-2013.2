/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fillinblanks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author rai
 */
public class Tree {
    private Node init;
    
    private final String path = "";
    private final String heuristicFileName = "";
    private final String dicFileName = "";

    public Tree() {
        init = new Node();
    }
    
    public Node getInit() {
        return init;
    }
    
    public void search(List<Node> input) {
        AStar(input);
        HillClimbing(input);
        SimmulatedAnnealing(input);
    }
    
    public void AStar(List<Node> input) {        
        List<Node> frontier = new LinkedList<Node>();
        Set<String> explored = new HashSet<String>();
        Map<String, Float> heuristic = getHeuristicFromFile();
        SortedSet<String> dictionary = getDictionaryFromFile();
        
        frontier = sortedAdd(frontier, input, heuristic);
        
        AStar(init, frontier, explored, heuristic, dictionary);
                
    }
    
    private void AStar(Node node, List<Node> frontier, Set<String> explored, Map<String, Float> heuristic, SortedSet<String> dictionary) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void HillClimbing(List<Node> input) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void SimmulatedAnnealing(List<Node> input) {
        throw new UnsupportedOperationException("Not yet implemented");
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

    private SortedSet<String> getDictionaryFromFile() {
        SortedSet<String> dic = new TreeSet<String>();
        Scanner file = new Scanner(path + dicFileName);
        
        while(file.hasNextLine()) {
            String line = file.nextLine();
            dic.add(line);
        }
        
        file.close();
        
        if (dic.size() > 0) {
            return dic;
        }
        else {
            System.out.println("Dicionário vazio!");
            return null;
        }
    }

    private List<Node> sortedAdd(List<Node> frontier, List<Node> input, Map<String, Float> heuristic) {
        
        for(Node n: input) {
            float function = n.getCost() + heuristic.get("$ "+n.getName()); //Evaluate function
            n.setFunction(function);
            int i = 0; boolean inseriu = false;
            
            if (!frontier.isEmpty()) {
                while(i < frontier.size() && !inseriu) {
                    Node aux = frontier.get(i);                    
                    if(aux.getFunction() > function) {
                        frontier.add(i, n);
                    }
                    else
                        i++;
                }
            }
            
            if(!inseriu)
                frontier.add(n);
        }
        
        return frontier;
    }
    
}
