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
        Set<Character> explored = new HashSet<Character>();
        Map<String, Float> heuristic = getHeuristicFromFile();
        SortedSet<String> dictionary = getDictionaryFromFile();
        
        frontier.add(init);
        //frontier = sortedAdd(frontier, input, heuristic);
        
        AStar(frontier, explored, input, heuristic, dictionary);
                
    }
    
    private void AStar(List<Node> frontier, Set<Character> explored, List<Node> input, Map<String, Float> heuristic, SortedSet<String> dictionary) {        
    
        if(frontier.isEmpty()) {
            System.out.println("FALHA"); //Testa se já foi visto todos os casos
        }
        else {
            Node node = frontier.remove(0); //Pop no nó melhor avaliado do frontier
            
            if(node.getCost() == 0) {
                System.out.println(node); //Se chegou no objetivo, imprime matriz completa.
            }
            else {                
                explored.add(node.getName()); //Senão, adiciona nó no explored
                input.remove(node); //Remove do input
                
                frontier = sortedAdd(explored.size(), frontier, explored, input, heuristic); //adiciona filhos no frontier
                
                AStar(frontier, explored, input, heuristic, dictionary); 
            }
        }
        
    }

    private void HillClimbing(List<Node> input) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    
    
    private void SimmulatedAnnealing(List<Node> input) {
        throw new UnsupportedOperationException("Not yet implemented");
    }   
    
    
    
    private Map<String, Float> getHeuristicFromFile() {//Get all of heuristic values.
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

    private SortedSet<String> getDictionaryFromFile() {//Get all of 3-gram words.
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

    //
    private List<Node> sortedAdd(int index, List<Node> frontier, Set<Character> explored, List<Node> input, Map<String, Float> heuristic) {
        
        for(Node n: input) {
            int lin = (int) (index / 3) + 1;
            int col = index - 1;
            char[][] matrix = n.getMatrix();
            
            float function = n.getCost() + heuristic.get(matrix[lin][col] + " " + n.getName()); //Evaluate function
            n.setFunction(function);        
            
            if(!explored.contains(n.getName())) {
                boolean inseriu = false;
                
                for (int i = 0; i < frontier.size(); i++) {
                    if(frontier.get(i).getName() == n.getName() && frontier.get(i).getFunction() < n.getFunction()) {
                        frontier.set(i, n); //Modifica nó de mesmo nome, se function do nó novo for maior
                        inseriu = true;
                    }
                }
                
                if(!inseriu) //Add nó novo
                    frontier = sortedAdd(n, frontier); 
            }
                            
        }
        
        return frontier;
    }

    private List<Node> sortedAdd(Node n, List<Node> list) {
        int i = 0; boolean inseriu = false;

        if (!list.isEmpty()) { //Add de maneira que a lista fique em ordem decrescente de function.
            while(i < list.size() && !inseriu) {
                Node aux = list.get(i);                    
                if(aux.getFunction() > n.getFunction()) {
                    list.add(i, n);
                    inseriu = true;
                }
                else
                    i++;
            }
        }

        if(!inseriu) //lista vazia ou function maior da lista, add no fim.
            list.add(n);
        
        return list;
    }
    
}
