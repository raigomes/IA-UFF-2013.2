/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import io.Log;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author rai
 */
public class Tree extends Observable{
    private Node init;
    
    private final String path = "/home/rai/Documentos/IA 2013.2/Trabalho/Trab2/trab2/";
    private final String heuristicFileName = "bigram.txt";
    private final String dicFileName = "3.txt";

    public Tree() {
        init = new Node('$');                
        this.addObserver(new Log());
        this.setChanged();
        this.notifyObservers("Log:\n\n");
    }
    
    public Node getInit() {        
        return init;
    }
    
    public void search(char[] input) {
        List<Character> in = new LinkedList<Character>();        
        
        for(char name: input) {
            in.add(name);
        }
        
        AStar(in);
        //HillClimbing(in);
        //SimmulatedAnnealing(in);
    }
        
    private void AStar(List<Character> input) {        
        List<Node> frontier = new LinkedList<Node>();
        List<Node> explored = new LinkedList<Node>();
        Map<String, Double> heuristic = getHeuristicFromFile();
        SortedSet<String> dictionary = getDictionaryFromFile();
        
        //Print add init no Frontier
        init.setChild(input);
        frontier.add(init);
        updateLog("AStar Algorithm:\n\n"+"   Add "+ init.getName()+" no Frontier\n\n");
                
        AStar(frontier, explored, input, heuristic, dictionary);
        
        this.updateLog("END");
    }        

    private void HillClimbing(List<Character> input) {
        List<Node> explored = new LinkedList<Node>();
        Map<String, Double> heuristic = getHeuristicFromFile();
        SortedSet<String> dictionary = getDictionaryFromFile();
        
        init.setChild(input);
        explored.add(init);
        
        
    }
        
    private void SimmulatedAnnealing(List<Character> input) {
        throw new UnsupportedOperationException("Not yet implemented");
    }   
    
    private void AStar(List<Node> frontier, List<Node> explored, List<Character> input, Map<String, Double> heuristic, SortedSet<String> dictionary) {        
    
        //Testa se já foi visto todos os casos do frontier.
        if(frontier.isEmpty()) {
            System.out.println("Frontier vazio!\n\nFALHA");
            //Imprime explored.
            updateLog("FALHA\n\nExplored:");
            for (int i = 0; i < explored.size(); i++) {
                updateLog("Node = "+explored.get(i).getName()+"\n     "+explored.get(i)+"\n");
//                System.out.println("Node = "+explored.get(i).getName()+"\n\nMatrix: ");
//                System.out.println(explored.get(i)); 
            }
        }
        //Se há nós no frontier.
        else {
            //Pop no nó melhor avaliado do frontier
            Node node = frontier.remove(0);
            
            //Se chegou no objetivo, imprime matriz completa.
            if(node.getCost() == 0) {
                System.out.println("Matriz Completa:\n\n"+node+"\n\nStrings");
                updateLog("SUCESSO!"); 
                printWords(node.getMatrix(), dictionary);                
            }
            
            else {                
                //Se nó não foi explorado
                if(!containsNode(node, explored)) {                                   
                    
                    //Print Add no Explored e Atualiza posição
                    if(node.getDad() != null) {
                        updateLog(node.getPosition()+"- Add "+ node.getDad().getName()+" -> "+node.getName()+" no Explored\n\n");
                        node.setPosition(node.getDad().getPosition()+1);
                    }
                    else {
                        updateLog(node.getPosition()+"- Add "+ node.getName()+" no Explored\n\n");
                        node.setPosition(0);
                    }

                    //Add nó no explored.
                    explored.add(node);                                                                                    
                    
                    //updateLog("Tamanho do Explored = "+node.getPosition()+"\n\n");
                }
                
                //Add filhos no frontier
                boolean isChanged = addInFrontier(node, frontier, explored, input, heuristic, dictionary); //adiciona filhos no frontier
                
                if(!isChanged) {
                    //input.add(name);                    
                    node.decPosition();
                    updateLog("Não mudou frontier!");
                }
                
                AStar(frontier, explored, input, heuristic, dictionary); 
                
            }
        }
        
    }
    
    public Map<String, Double> getHeuristicFromFile() {//Get all of heuristic values.
        
        
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
                updateLog("Heuristica vazia!");
                return null;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.println("Exceção!");
            return null;
        }
    }

    public SortedSet<String> getDictionaryFromFile() {//Get all of 3-gram words.
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
                updateLog("Dicionário vazio!");
                return null;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.println("Exceção!");
            return null;
        }
    }

    //Add values that was sorted by evaluated function on frontier.
    private boolean addInFrontier(Node dad, List<Node> frontier, List<Node> explored, List<Character> input, Map<String, Double> heuristic, SortedSet<String> dictionary) {
        /* col = pos - (lin - 1)*3 = 
         *     = 1 - (1 - 1)*3 = 1 - 0 = 1
         *     = 4 - (2 - 1)*3 = 4 - 3 = 1
         *     = 7 - (3 - 1)*3 = 7 - 6 = 1
         */
        int index = dad.getPosition()+1;
        boolean changed = false; 
        for(char name: dad.getChild()) {
            
            int lin = (int) (index - 1)/ 3 + 1;
            int col = index - (lin - 1)* 3;
            
            updateLog("Lin = "+lin+"/ Col = "+col+"\n\n");
            Node n = createNode(name, dad, lin, col, heuristic, input);                        
                        
            //Test if node was explored.
            if(!containsNode(n, explored)) { 
                //Test if node is valid, i.e. bigram heuristic > 0 or word is contained in dictionary.
                boolean aux = isValid(n, lin, col, heuristic, dictionary);
                
                if(aux){ 
                    boolean hasEqualNode = false;
                    changed = true; 

                    for (int i = 0; i < frontier.size(); i++) {
                        //if(frontier.get(i).getName() == n.getName() && frontier.get(i).getFunction() < n.getFunction()) {
                        if(frontier.get(i).compareTo(n) == 0){
                            hasEqualNode = true;
                            if(frontier.get(i).getCost() > n.getCost()) {
                                frontier.remove(i);
                                frontier = sortedAdd(n, frontier);//Modify node with same name if function of new node to be higher.
                                updateLog("   Set "+n.getName()+" no Frontier"+"   F(n) = "+n.getFunction() + "\n\n");
                                break;
                            }
                        }
                    }

                    if(!hasEqualNode) {//Add new node                        
                        frontier = sortedAdd(n, frontier);   
                        //Print add no Frontier and function
                        updateLog("   Add "+n.getDad().getName()+" -> "+n.getName()+" no Frontier"+"   F(n) = "+n.getFunction() + "\n\n");
                    }
                }
            }
                            
        }
        
        return changed;
    }

    //Add de maneira que a lista fique em ordem decrescente de function.
    private List<Node> sortedAdd(Node n, List<Node> list) {
        int i = 0; boolean inseriu = false;

        if (!list.isEmpty()) { 
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

    //Test if Node n can be added in matrix.
    private boolean isValid(Node n, int lin, int col, Map<String, Double> heuristic, SortedSet<String> dictionary) {
        String keyL, keyC, keyD;
        boolean lineValid = false, colValid = false, diagonalValid = false;
        char[][] matrix = n.getMatrix();
        
        keyL = matrix[lin][col - 1] + " " + matrix[lin][col]; //Bigram Line
        keyC = matrix[lin - 1][col] + " " + matrix[lin][col]; //Bigram Column
        keyD = matrix[lin - 1][col - 1] + " " + matrix[lin][col]; //Bigram Diagonal
        
        if(lin == 3) { //Test if it is the last column character
            String l = "", c = "", d = ""; 
            
            for (int i = 1; i < matrix.length; i++) {                
                c += matrix[i][col];
            }
            colValid = dictionary.contains(c);
                        
            if(col == 1) { //Test if it is the last diagonal character too.
                //d = "";
                lineValid = (heuristic.get(keyL) > 0.0f);
                
                for (int i = 1; i < matrix.length; i++) {                    
                    d += matrix[i][matrix.length - i];                                                                
                }
                diagonalValid = dictionary.contains(d);
            }
            else 
                if(col == 3) {
                    //word = "";                    
                    l = new String(matrix[lin]).substring(1);
                    lineValid = dictionary.contains(l);
                    
                    for (int i = 1; i < matrix.length; i++) {                    
                            d += matrix[i][i];                                                                
                    }
                    diagonalValid = dictionary.contains(d);
                }
                else {
                    lineValid = (heuristic.get(keyL) > 0.0f);
                    diagonalValid = true;
                }
        }
        
        else {
            if(col == 3) { //Test if it is the last line character.
                String word = new String(matrix[lin]).substring(1);
                
                lineValid = dictionary.contains(word);
                colValid = (heuristic.get(keyC) > 0.0f);
                diagonalValid = true;
            }        
            else { //Case of there aren't search in dictionary.
                lineValid = (heuristic.get(keyL) > 0.0f);
                colValid = (heuristic.get(keyC) > 0.0f);
                diagonalValid = true;
            }
        }
        
        //Print teste de validez do nó
        updateLog(" -Node: "+n.getName()+"\n\n  Evaluate Node (line ^ col ^ diagonal): "+lineValid +"^"+ colValid +"^"+ diagonalValid+" => ");             
        boolean aux = (lineValid && colValid && diagonalValid);
        updateLog("  Valid = "+aux+"\n\n");
        return aux;
    }

    //UpdateMatrix.
    private char[][] updateMatrix(Node n, int lin, int col) {
        
        n.setMatrix(n.getDad().getMatrix());
        char[][] matrix = n.getMatrix();
        
        
        matrix[lin][col] = n.getName();
        if (n.getPosition() > 1)
            if(n.getPosition()%3 == 1) 
                matrix[lin - 1][3] = n.getDad().getName();
            else
                matrix[lin][col - 1] = n.getDad().getName();

        n.setMatrix(matrix);
        
        //Print matriz do nó
        updateLog("Matrix: "+n.getName()+"\n\n"+n);
        updateLog("Dad: "+n.getDad().getName()+"\n\n"+n.getDad());
        return matrix;
    }
    
    
    private Node createNode(char name, Node dad, int lin, int col, Map<String, Double> heuristic, List<Character> input) {
            Node n, aux;
            n = new Node(name);            
            aux = n;
            
            n.setDad(dad);
            n.setCost(dad.getCost() - 1);
            n.setPosition(dad.getPosition()+1);
            
            char[][] matrix = updateMatrix(n, lin, col);            
            
            double function = n.getCost();
            if(dad.getPosition() % 3 != 0 || dad.getPosition() == 0)
                function += heuristic.get(dad.getName() + " " + n.getName()); //Evaluate function
            
            n.setFunction(function);
            n.setChild(input);
                        
            while(aux != null) {                
                n.getChild().remove(aux.getName());
                aux = aux.getDad();
            }
            
            
            return n;
    }
    
    private boolean isChanged(List<Node> list, List<Node> anotherList) {
        
        if(list.size() == anotherList.size()) {
            for (int i = 0; i < anotherList.size(); i++) {                
                Node a = list.get(i), b = anotherList.get(i);
                if(a.getName() != b.getName() || a.getFunction() != a.getFunction() || a.getDad() != b.getDad()){
                    return true;
                }
            }
            return false;
        }
        else
            return true;
    }

    private boolean containsNode(Node node, List<Node> list) {
        for(Node n : list) {
            if(node.compareTo(n) == 0)
                return true;
        }
        
        return false;
    }

    public void printWords(char[][] matrix, SortedSet<String> dictionary) {        
        String h, v, d1 = "", d2 = "";

        for (int i = 1; i < matrix.length; i++) {
            h = new String(matrix[i]).substring(1);
            System.out.println(h+" -> "+dictionary.contains(h));
        }
        for (int i = 1; i < matrix.length; i++) {
            v = matrix[1][i] + "" + matrix[2][i] + "" + matrix[3][i];
            System.out.println(v+" -> "+dictionary.contains(v));
        }
        for (int i = 1; i < matrix.length; i++) {
            d1 += matrix[i][i];
            d2 += matrix[i][4-i];
        }

        System.out.println(d1+" -> "+dictionary.contains(d1));
        System.out.println(d2+" -> "+dictionary.contains(d2));
    }

    private void updateLog(String text) {
        this.setChanged();
        if(!text.equals("END"))
            this.notifyObservers(text);
        else
            this.notifyObservers();
    }
            
}
