/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fillinblanks;

import java.io.File;
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
    
    private final String path = "/home/rai/Documentos/IA 2013.2/Trabalho/Trab2/trab2/";
    private final String heuristicFileName = "bigram.txt";
    private final String dicFileName = "3.txt";

    public Tree() {
        init = new Node('$');
    }
    
    public Node getInit() {
        return init;
    }
    
    public void search(char[] input) {
        List<Node> in = new LinkedList<Node>();
        
        for(char name: input) {
            Node aux = new Node(name);
            in.add(aux);
        }
        
        AStar(in);
        //HillClimbing(in);
        //SimmulatedAnnealing(in);
    }
    
    public void AStar(List<Node> input) {        
        List<Node> frontier = new LinkedList<Node>();
        Set<Character> explored = new HashSet<Character>();
        Map<String, Double> heuristic = getHeuristicFromFile();
        SortedSet<String> dictionary = getDictionaryFromFile();
        
        frontier.add(init);
        
        AStar(frontier, explored, input, heuristic, dictionary);
                
    }
    
    private void AStar(List<Node> frontier, Set<Character> explored, List<Node> input, Map<String, Double> heuristic, SortedSet<String> dictionary) {        
    
        if(frontier.isEmpty()) {
            System.out.println("FALHA"); //Testa se já foi visto todos os casos do frontier.
        }
        else {
            Node node = frontier.remove(0); //Pop no nó melhor avaliado do frontier
            
            if(node.getCost() == 0) {
                System.out.println(node); //Se chegou no objetivo, imprime matriz completa.
            }
            else {
                if(!explored.contains(node.getName())) {
                    explored.add(node.getName()); //Senão, adiciona nó no explored
                    input.remove(node); //Remove do input
                }
                
                frontier = sortedAdd(explored.size(), node, frontier, explored, input, heuristic, dictionary); //adiciona filhos no frontier
                
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
    
    
    
    private Map<String, Double> getHeuristicFromFile() {//Get all of heuristic values.
        
        
        try {
            Scanner file = new Scanner(new File(path + heuristicFileName));
            Map<String, Double> heuristic = new HashMap<String, Double>();

            while(file.hasNextLine()) {
                String line = file.nextLine();
                String key = line.substring(0, 3);            
                double value = Double.parseDouble(line.substring(4));
                
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
        catch(Exception e) {
            e.printStackTrace();
            System.err.println("Exceção!");
            return null;
        }
    }

    private SortedSet<String> getDictionaryFromFile() {//Get all of 3-gram words.
        try {
            Scanner file = new Scanner(new File(path + dicFileName));
            SortedSet<String> dic = new TreeSet<String>();

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
        catch(Exception e) {
            e.printStackTrace();
            System.err.println("Exceção!");
            return null;
        }
    }

    //
    private List<Node> sortedAdd(int index, Node dad, List<Node> frontier, Set<Character> explored, List<Node> input, Map<String, Double> heuristic, SortedSet<String> dictionary) {
        /* col = index - (lin - 1)*3 = 
         *     = 1 - (1 - 1)*3 = 1 - 0 = 1
         *     = 4 - (2 - 1)*3 = 4 - 3 = 1
         *     = 7 - (3 - 1)*3 = 7 - 6 = 1
         */
        for(Node n: input) {
            int lin = (int) (index - 1)/ 3 + 1;
            int col = index - (lin - 1)* 3;
            
            char[][] matrix = n.getMatrix();
            matrix[lin][col] = n.getName();
            
            System.out.println(index+" "+dad.getName()+" "+n.getName());
            
            n.setCost(9.0f - index);
            double function = n.getCost() + heuristic.get(matrix[lin][col - 1] + " " + n.getName()); //Evaluate function
            System.out.println(" "+function + '\n');
            n.setFunction(function);
            n.setDad(dad);
                        
            //Test if node was explored.
            if(!explored.contains(n.getName())) { 
                //Test if node is valid, i.e. bigram heuristic > 0 or word is contained in dictionary.
                if(isValid(n, lin, col, heuristic, dictionary)){ 
                    boolean inseriu = false;

                    for (int i = 0; i < frontier.size(); i++) {
                        if(frontier.get(i).getName() == n.getName() && frontier.get(i).getFunction() < n.getFunction()) {
                            frontier.set(i, n); //Modify node with same name if function of new node to be higher.
                            inseriu = true;
                        }
                    }

                    if(!inseriu) {//Add new node
                        
                        frontier = sortedAdd(n, frontier); 
                    }
                }
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

    private boolean isValid(Node n, int lin, int col, Map<String, Double> heuristic, SortedSet<String> dictionary) {
        String keyL, keyC, keyD;
        boolean lineValid = false, colValid = false, diagonalValid = false;
        char[][] matrix = n.getMatrix();
        
        keyL = matrix[lin - 1][col] + " " + matrix[lin][col]; //Bigram Line
        keyC = matrix[lin][col - 1] + " " + matrix[lin][col]; //Bigram Column
        keyD = matrix[lin - 1][col - 1] + " " + matrix[lin][col]; //Bigram Diagonal
        
        if(col == 3) { //Test if it is the last line character.
            String word = new String(matrix[lin]);
            lineValid = dictionary.contains(word);
            
            colValid = (heuristic.get(keyC) > 0.0f);
            
            if(lin == 1) { //Test if it is the last diagonal character too.
                word = "";
                for (int i = 1; i < matrix.length; i++) {                    
                        word += matrix[i][i];                                                                
                }
                diagonalValid = dictionary.contains(word);
            }
            
            else 
                diagonalValid = (heuristic.get(keyD) > 0.0f);
        }
        
        else {
            if(lin == 3) { //Test if it is the last column character
                String word = new String(matrix[col]);
                colValid = dictionary.contains(word);

                lineValid = (heuristic.get(keyL) > 0.0f);

                if(col == 1) { //Test if it is the last diagonal character too.
                    word = "";
                    for (int i = 1; i < matrix.length; i++) {                    
                            word += matrix[i][matrix.length - i];                                                                
                    }
                    diagonalValid = dictionary.contains(word);
                }

                else 
                    diagonalValid = (heuristic.get(keyD) > 0.0f);
            }
            else { //Case of there aren't search in dictionary.
                lineValid = (heuristic.get(keyL) > 0.0f);
                colValid = (heuristic.get(keyC) > 0.0f);
                diagonalValid = (heuristic.get(keyD) > 0.0f);
            }
        }
        
        return lineValid ^ colValid ^ diagonalValid;
    }

    
}
