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
    private char value;
    private int cost;
    private char[][] matrix;
    private Node son, dad;

    public Node() {
        cost = 9;
        value = '$';
        matrix = new char[4][4];
        for (int i = 0; i < matrix.length; i++) {
            matrix[0][i] = '$';
            matrix[i][0] = '$';
        }
        son = null; dad = null;
    }

    public Node(char value, int cost, char[][] matrix, Node son) {
        this.value = value;
        this.cost = cost;
        this.matrix = matrix;
        this.son = son;
    }
        

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }
    

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public char[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(char[][] matrix) {
        this.matrix = matrix;
    }
    
    
    
    
}
