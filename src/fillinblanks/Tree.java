/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fillinblanks;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
        List<Character> in = new LinkedList<Character>();
        //in.add('$');
        
        for(char name: input) {
            in.add(name);
        }
        
        AStar(in);
        //HillClimbing(in);
        //SimmulatedAnnealing(in);
    }
    
    public void AStar(List<Character> input) {        
        List<Node> frontier = new LinkedList<Node>();
        List<String> explored = new LinkedList<String>();
        Map<String, Double> heuristic = getHeuristicFromFile();
        SortedSet<String> dictionary = getDictionaryFromFile();
        
        //Print add init no Frontier
        System.out.println("Log:\n");
        frontier.add(init);
        System.out.println("   Add "+" "+ init.getName()+" no Frontier\n");
        
        
        AStar(frontier, explored, input, heuristic, dictionary);
                
    }
    
    private void AStar(List<Node> frontier, List<String> explored, List<Character> input, Map<String, Double> heuristic, SortedSet<String> dictionary) {        
    
        if(frontier.isEmpty()) {
            System.out.println("FALHA\n"); //Testa se já foi visto todos os casos do frontier.
            
            System.out.println("Explored:");
            for (int i = 0; i < explored.size(); i++) {
                System.out.println(explored.get(i)); //Imprime explored.
            }
        }
        else {
            Node node = frontier.remove(0); //Pop no nó melhor avaliado do frontier
            
            if(node.getCost() == 0) {
                System.out.println("Matriz Completa:\n"+node); //Se chegou no objetivo, imprime matriz completa.
            }
            else {
                char nameDad;
                if(node.getDad() == null) 
                    nameDad = '/';                
                else
                    nameDad = node.getDad().getName();
                Character name = node.getName();

                if(!explored.contains(nameDad+"->"+name)) {
                    //Print Add no Explored e Remove do Input
                    System.out.println(node.getIndex()+"- Add "+nameDad+" -> "+node.getName()+" no Explored\n   Remove "+node.getName()+" do Input\n");

                    explored.add(nameDad+"->"+name); //Senão, adiciona nó no explored
                    //input.remove(name); //Remove do input                    
                    
                    if(node.getDad() == null)
                        node.setIndex(1);
                    else
                        node.setIndex(node.getDad().getIndex()+1);
                    
                    System.out.println("Tamanho do Explored = "+node.getIndex()+"\n");
                }
                
                boolean isChanged = sortedAdd(node.getIndex(), node, frontier, explored, input, heuristic, dictionary); //adiciona filhos no frontier
                
                if(!isChanged) {
                    input.add(name);                    
                    node.decIndex();
                    System.out.println("Não mudou frontier! Index = "+node.getIndex());
                }
                
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

    //Add values that was sorted by evaluated function on frontier.
    private boolean sortedAdd(int index, Node dad, List<Node> frontier, List<String> explored, List<Character> input, Map<String, Double> heuristic, SortedSet<String> dictionary) {
        /* col = index - (lin - 1)*3 = 
         *     = 1 - (1 - 1)*3 = 1 - 0 = 1
         *     = 4 - (2 - 1)*3 = 4 - 3 = 1
         *     = 7 - (3 - 1)*3 = 7 - 6 = 1
         */
        
        boolean changed = false;
        for(char name: input) {
            int lin = (int) (index - 1)/ 3 + 1;
            int col = index - (lin - 1)* 3;
            
            Node n = new Node(name);            
            n.setDad(dad);
            n.setCost(9.0f - index);
            n.setIndex(dad.getIndex()+1);
            //n.setMatrix(n.getDad().getMatrix());
            
            char[][] matrix = updateMatrix(index, n, lin, col);            
                                    
            double function = n.getCost() + heuristic.get(matrix[lin][col - 1] + " " + n.getName()); //Evaluate function
            n.setFunction(function);
                        
            //Test if node was explored.
            if(!explored.contains(n.getDad().getName()+"->"+n.getName())) { 
                //Test if node is valid, i.e. bigram heuristic > 0 or word is contained in dictionary.
                boolean aux = isValid(n, lin, col, heuristic, dictionary);
                if(aux){ 
                    boolean inseriu = false;
                    changed = true; 

                    for (int i = 0; i < frontier.size(); i++) {
                        //if(frontier.get(i).getName() == n.getName() && frontier.get(i).getFunction() < n.getFunction()) {
                        if(frontier.get(i).getName() == n.getName()){
                            frontier.remove(i);
                            frontier = sortedAdd(n, frontier);//Modify node with same name if function of new node to be higher.
                            inseriu = true;
                            System.out.println("   Set "+n.getName()+" no Frontier");
                            break;
                        }
                    }

                    if(!inseriu) {//Add new node                        
                        frontier = sortedAdd(n, frontier);   
                        //Print add no Frontier
                        System.out.println("   Add "+n.getDad().getName()+" -> "+n.getName()+" no Frontier");
                    }
                    //Print function
                    System.out.println("   F(n) = "+function + '\n');            
                }
            }
                            
        }
        
        return changed;
    }

    private List<Node> sortedAdd(Node n, List<Node> list) {
        int i = 0; boolean inseriu = false;

        if (!list.isEmpty()) { //Add de maneira que a lista fique em ordem decrescente de function.
            while(i < list.size() && !inseriu) {
                Node aux = list.get(i);                    
                if(aux.getFunction() < n.getFunction()) {
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
        
        keyL = matrix[lin][col - 1] + " " + matrix[lin][col]; //Bigram Line
        keyC = matrix[lin - 1][col] + " " + matrix[lin][col]; //Bigram Column
        keyD = matrix[lin - 1][col - 1] + " " + matrix[lin][col]; //Bigram Diagonal
        
        if(col == 3) { //Test if it is the last line character.
            String word = new String(matrix[lin]).substring(1);
            lineValid = dictionary.contains(word);
            
            colValid = (heuristic.get(keyC) > 0.0f);
            
            if(lin == 3) { //Test if it is the last diagonal character too.
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
                String word = new String(matrix[col]).substring(1);
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
        
        //Print teste de validez do nó
        System.out.println(" -Node: "+n.getName()+"\n  "+lineValid +"^"+ colValid +"^"+ diagonalValid+'\n');             
        
        boolean aux = (lineValid ^ colValid) ^ diagonalValid;
        return aux;
    }

    private char[][] updateMatrix(int index, Node n, int lin, int col) {
        
        char[][] matrix = new char[4][4];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(n.getDad().getMatrix()[i], 0, matrix[i], 0, matrix.length);
        }
        
        matrix[lin][col] = n.getName();
        if (index > 1)
            if(index%3 == 1) 
                matrix[lin - 1][3] = n.getDad().getName();
            else
                matrix[lin][col - 1] = n.getDad().getName();

        n.setMatrix(matrix);
        
        //Print matriz do nó
        System.out.println("Matrix: "+n.getName()+"\n"+n);
        System.out.println("Dad: "+n.getDad().getName()+"\n"+n.getDad());
        return matrix;
    }

//    private Node updateNode(int .getIndex(), Node n, Node dad, int lin, int col, double function) {
//        n.setDad(dad);
//        n.setCost(9.0f - .getIndex());
//        n.setMatrix(updateMatrix(.getIndex(), n, lin, col));                    
//        n.setFunction(function);
//        
//        return n;
//    }

//    private boolean isChanged(List<Node> list, List<Node> anotherList) {
//        
//        if(list.size() == anotherList.size()) {
//            for (int i = 0; i < anotherList.size(); i++) {                
//                Node a = list.get(i), b = anotherList.get(i);
//                if(a.getName() != b.getName() || a.getFunction() != a.getFunction() || a.getDad() != b.getDad()){
//                    return true;
//                }
//            }
//            return false;
//        }
//        else
//            return true;
//    }
    
}
