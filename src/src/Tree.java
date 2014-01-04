/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import controller.*;
import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.management.timer.Timer;

/**
 *
 * @author rai
 */
public class Tree extends Observable{
    private Node init;
    
    private String path = "/home/rai/Documentos/IA 2013.2/Trabalho/Trab2/trab2/";
    private static final String heuristicFileName = "bigram.txt";
    private static final String dicFileName = "3.txt";

    public Tree() {
        init = new Node('$');         
        this.addObserver(new Log());
        this.setChanged();
        this.notifyObservers("Log:\n\n");
    }
    
    public void setPath(String newPath) {
        path = newPath;
    }
    
    private Node getInitialNode(List<Character> input, Map<String, Double> heuristic, SortedSet<String> dictionary) {                
        int index = (int)(Math.random()*9);
        Node in = createNode(input.get(index), init, input, heuristic, dictionary);        
        return in;
    }
    
    public void search(char[] input, int type) {
        List<Character> in = new LinkedList<Character>();                
        long t0, t;
        
        for(char name: input) {
            in.add(name);
        }
        
        t0 = System.currentTimeMillis();
        switch(type) {
            case 0: 
                AStar(in);
                break;
            case 1:
                HillClimbing(in);
                break;
            case 2:
                SimulatedAnnealing(in);
                break;
        }                                
        t = System.currentTimeMillis();
        
        System.out.println("Tempo decorrido: "+(t-t0)+"\n");
        updateLog("Tempo decorrido: "+(t-t0)+"\n");
        updateLog("END");
    }
        
    private void AStar(List<Character> input) {        
        List<Node> frontier = new LinkedList<Node>();
        List<Node> explored = new LinkedList<Node>();
        Map<String, Double> heuristic = getHeuristicFromFile();
        SortedSet<String> dictionary = getDictionaryFromFile();
        
        //Print add init no Frontier
        init.setChild(input);
        frontier.add(init);
        updateLog("AStar Algorithm:\n\n"
                +"   Add "+ init.getName()+" no Frontier\n\n");
                
        Node answer = AStar(frontier, explored, input, heuristic, dictionary);
        if(answer != null)
            printWords(answer.getMatrix(), dictionary);        
    }        

    private Node AStar(List<Node> frontier, List<Node> explored, List<Character> input, Map<String, Double> heuristic, SortedSet<String> dictionary) {        
    
        //Testa se já foi visto todos os casos do frontier.
        if(frontier.isEmpty()) {
            System.out.println("Frontier vazio!\n\nFALHA");
            //Imprime explored.
            updateLog("FALHA\n\nExplored:");
            for (int i = 0; i < explored.size(); i++) {
                updateLog("Node = "+explored.get(i).getName()+"\n     "+explored.get(i)+"\n");
            }
            return null;
        }
        //Se há nós no frontier.
        else {
            //Pop no nó melhor avaliado do frontier
            Node node = frontier.remove(0);
            
            //Se chegou no objetivo, imprime matriz completa.
            if(node.getCost() >= 9.0) {
                System.out.println("Matriz Completa:\n\n"+node+"\nWords:");
                updateLog("SUCESSO!\n" 
                        + "\nMatriz Completa:\n\n"+node);
                return node;                
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
                }
                
                //Add filhos no frontier
                boolean isChanged = addInFrontier(node, frontier, explored, input, heuristic, dictionary); //adiciona filhos no frontier
                
                if(!isChanged) {                    
                    node.decPosition();
                    updateLog("Não mudou frontier!");
                }
                
                return AStar(frontier, explored, input, heuristic, dictionary); 
                
            }
        }
        
    }
    
    //Random-restart hill climbing
    private void HillClimbing(List<Character> input) {
        List<Node> explored = new LinkedList<Node>();
        Map<String, Double> heuristic = getHeuristicFromFile();
        SortedSet<String> dictionary = getDictionaryFromFile();                
        
        
        updateLog("Hill Climbing Algorithm:\n\n");
        Node bestNode = null;
        int bestSize = 0;
        
        for(char first : input) { 
            //Escolha o nó inicial e adiciona no Explored.        
            //Node firstNode = getInitialNode(input, heuristic, dictionary);        
            Node firstNode = createNode(first, init, input, heuristic, dictionary);
            
            explored.add(firstNode);
            
            updateLog("First Node = "+firstNode.getName()+"\n"
                    +"\nAdd "+firstNode.getName()+" in Explored"
                    +"\nF(n) = "+firstNode.getFunction()+"\n");

            Node answer = HillClimbing(firstNode, input, explored, heuristic, dictionary);
            
            int count = countNodes(answer.getMatrix());
            if(count == input.size()) {
                printWords(answer.getMatrix(), dictionary);
                break;
            }
            else {
                if(count > bestSize) {
                    bestSize = count;
                    bestNode = answer;
                }
            }                
        }
        
        System.out.println("FALHA\nMelhor Resposta:\n"+ bestNode);        
    }
    
    private Node HillClimbing(Node current, List<Character> input, List<Node> explored, Map<String, Double> heuristic, SortedSet<String> dictionary) {
        if(current.getPosition() == input.size()) {
            updateLog("Matriz Completa:\n\n"+current+"\n");
            System.out.println("Matriz Completa:\n\n"+current+"\nWords:");
            return current;
        }
        else{                        
            //Escolha o melhor vizinho
            Node bestNeighbor = getBestNeighbor(current, heuristic, dictionary, input);
            
            
            if(current.getFunction() <= bestNeighbor.getFunction() && bestNeighbor.getFunction() > bestNeighbor.getCost()) {
                explored.add(bestNeighbor);
                updateLog("Add "+bestNeighbor.getName()+" in Explored"
                        +"F(n) = "+bestNeighbor.getFunction()+"\n");                

                return HillClimbing(bestNeighbor, input, explored, heuristic, dictionary);
            }
            else{                
//                int index = (int)Math.random()*current.getChild().size();
//                bestNeighbor = createNode(current.getChild().get(index), current, input, heuristic, dictionary);
//                return HillClimbing(bestNeighbor, input, explored, heuristic, dictionary);
                updateLog("FALHA!\n"
                        +"\nMatriz:\n"+current+"\n");
                return current;
            }
        }   
    }        
    
    private void SimulatedAnnealing(List<Character> input) {
        List<Node> explored = new LinkedList<Node>();
        Map<String, Double> heuristic = getHeuristicFromFile();
        SortedSet<String> dictionary = getDictionaryFromFile();
        
        updateLog("Simulated Annealing Algorithm:\n\n");
        
        //Escolha o nó inicial e adiciona no Explored.
        Node firstNode = getInitialNode(input, heuristic, dictionary);
        firstNode.setChild(input);
        explored.add(firstNode);
        
        updateLog("Add "+firstNode.getName()+" in Explored" 
                + "F(n) = "+firstNode.getFunction()+"\n");
        
        Node answer = SimulatedAnnealing(firstNode, input, explored, heuristic, dictionary);
        if(answer != null)
            printWords(answer.getMatrix(), dictionary);        
    }   
    
    private Node SimulatedAnnealing(Node current, List<Character> input, List<Node> explored, Map<String, Double> heuristic, SortedSet<String> dictionary) {          
            double T, deltaE, p;
            int index, k = 0;
            char name;
            Node next;
            
            T = input.size();
            while(k < input.size()) {
                if (T == 0 || current.getChild().isEmpty()) {
                    System.out.println("Matriz:\n"+current);
                    updateLog("Matriz:\n"+current+"\n");
                    return current;
                }
                
                index = (int)Math.random() * current.getChild().size();     
                name = current.getChild().get(index);
                next = createNode(name, current, input, heuristic, dictionary);
                deltaE = next.getFunction() - current.getFunction() - 1;
                
                p = Math.pow(Math.E, deltaE / T);
                
                if(deltaE > 0 || p >= 0.7){
                    explored.add(current);
                    current = next;        
                }        
                else
                    break;
                
                updateLog("Add "+current.getName()+" in Explored\n" 
                        + "F(n) = "+current.getFunction()+"\n");
                T--; k++;
            }           
            
            updateLog("FALHA!\n\nMatriz:\n"+current);
            System.out.println("FALHA!\n\nMatriz:\n"+current);
            return null;
    }            
    
    //Get all of heuristic values.                
    public Map<String, Double> getHeuristicFromFile() {
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

    //Get all of 3-gram words.
    public SortedSet<String> getDictionaryFromFile() {
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
        boolean changed = false; 
        for(char name: dad.getChild()) {                        
                        
            Node n = createNode(name, dad, input, heuristic, dictionary);                        
                        
            //Test if node was explored.
            if(!containsNode(n, explored)) { 
                //Test if node is valid, i.e. bigram heuristic > 0 or word is contained in dictionary.
                boolean aux = isValid(n, heuristic, dictionary);
                
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
    private boolean isValid(Node n, Map<String, Double> heuristic, SortedSet<String> dictionary) {
        String keyL, keyC, keyD;
        int index, lin, col;
        
        index = n.getPosition();
        lin = (int) (index - 1)/ 3 + 1;
        col = index - (lin - 1)* 3;
        
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
                
                if(lin == col)
                    diagonalValid = (heuristic.get(keyD) > 0.0f);                
                else
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
    private void updateMatrix(Node n, int lin, int col) {
        
        n.setMatrix(n.getDad().getMatrix());
        char[][] matrix = n.getMatrix();
    
        updateLog("Lin = "+lin+"/ Col = "+col+"\n\n");
        
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
        //return matrix;
    }
        
    private Node createNode(char name, Node dad, List<Character> input, Map<String, Double> heuristic, SortedSet<String> dictionary) {
        Node n, aux;
        int index, lin, col;
        
        n = new Node(name);                    
        index = dad.getPosition()+1;
        lin = (int) (index - 1)/ 3 + 1;
        col = index - (lin - 1)* 3;
        
        n.setDad(dad);
        n.setCost(dad.getCost() + 1);
        n.setPosition(index);

        updateMatrix(n, lin, col);            

        double function = n.getCost();    
        //double function = 0.0;
        if(index % 3 == 0) {
            char[][] matrix = n.getMatrix();
            String word = new String(matrix[lin]).substring(1);
            if(dictionary.contains(word))
                function += 0.1; //Palavra conhecida
        }
        else
            if(dad.getPosition() % 3 == 0 && dad.getPosition() != 0)
                function += heuristic.get("$ " + n.getName());
            else 
                function += heuristic.get(dad.getName() + " " + n.getName()); //Evaluate function
            
        
        n.setFunction(function);
        n.setChild(input);

        aux = n;
        while(aux != null) {                
            n.getChild().remove(aux.getName());
            aux = aux.getDad();
        }

        return n;
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
        StringBuilder ans = new StringBuilder();
        
        for (int i = 1; i < matrix.length; i++) {
            h = new String(matrix[i]).substring(1);
            ans.append(h).append(" -> ").append(dictionary.contains(h));
            ans.append("\n");
        }
        for (int i = 1; i < matrix.length; i++) {
            v = matrix[1][i] + "" + matrix[2][i] + "" + matrix[3][i];
            ans.append(v).append(" -> ").append(dictionary.contains(v));
            ans.append("\n");
        }
        for (int i = 1; i < matrix.length; i++) {
            d1 += matrix[i][i];
            d2 += matrix[i][4-i];
        }

        ans.append(d1).append(" -> ").append(dictionary.contains(d1));
        ans.append("\n");
        ans.append(d2).append(" -> ").append(dictionary.contains(d2));
        
        System.out.println(ans.toString()+"\n");
    }

    private void updateLog(String text) {
        this.setChanged();
        if(!text.equals("END"))
            this.notifyObservers(text);
        else
            this.notifyObservers();
    }    

    private Node getBestNeighbor(Node current, Map<String, Double> heuristic, SortedSet<String> dictionary, List<Character> input) {                
        Node best = new Node(current.getName());
        
        for(char name : current.getChild()) {
            Node node = createNode(name, current, input, heuristic, dictionary);            
            if(isValid(node, heuristic, dictionary))
                if(best.getFunction() < node.getFunction() && node.getFunction() != 0) 
                    best = node;                
        }
        
        return best;
    }

    private int countNodes(char[][] matrix) {
        int count = 0;
        for (int i = 1; i < matrix.length; i++) {            
            for (int j = 1; j < matrix.length; j++) {
                if(matrix[i][j] < 65 || matrix[i][j] > 90)
                    break;
                else
                    count++;
            }
        }
        return count;
    }
}