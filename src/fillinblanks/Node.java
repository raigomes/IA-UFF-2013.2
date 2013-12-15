/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fillinblanks;

/**
 *
 * @author rai
 */
public class Node {
    private char name;
    private double cost, function;
    private char[][] matrix;
    private Node dad;

    public Node(char name) {
        cost = 9.0d; function = 0.0d;
        this.name = name;
        matrix = new char[4][4];
        for (int i = 0; i < matrix.length; i++) {
            matrix[0][i] = '$';
            matrix[i][0] = '$';
        }
        dad = null;
    }   
        

    public char getName() {
        return name;
    }

    public void setValue(char value) {
        this.name = value;
    }
    

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getFunction() {
        return function;
    }

    public void setFunction(double function) {
        this.function = function;
    }
    
    public Node getDad() {
        return dad;
    }

    public void setDad(Node dad) {
        this.dad = dad;
    }
    

//    public Node getSon() {
//        return son;
//    }
//
//    public void setSon(Node son) {
//        this.son = son;
//    }
    
    
    public char[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(char[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public String toString() {
        StringBuilder answer = new StringBuilder();
        for (int i = 1; i < matrix.length; i++) {
            answer.append("{");
            for (int j = 1; j < matrix[0].length; j++) {
                answer.append(matrix[i][j]).append(" ");                
            }            
            answer.append("}\n");
        }
        
        return answer.toString();
    }
    
    
    
    
}
