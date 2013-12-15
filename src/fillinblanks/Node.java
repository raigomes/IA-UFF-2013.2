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
    private float cost, function;
    private char[][] matrix;
    private Node son, dad;

    public Node() {
        cost = 9.0f; function = 0.0f;
        name = '$';
        matrix = new char[4][4];
        for (int i = 0; i < matrix.length; i++) {
            matrix[0][i] = '$';
            matrix[i][0] = '$';
        }
        son = null; dad = null;
    }

    public Node(char name, int cost, char[][] matrix, Node son) {
        this.name = name;
        this.cost = cost;
        this.matrix = matrix;
        this.son = son;
    }
        

    public char getName() {
        return name;
    }

    public void setValue(char value) {
        this.name = value;
    }
    

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getFunction() {
        return function;
    }

    public void setFunction(float function) {
        this.function = function;
    }
    
    public Node getDad() {
        return dad;
    }

    public void setDad(Node dad) {
        this.dad = dad;
    }
    

    public Node getSon() {
        return son;
    }

    public void setSon(Node son) {
        this.son = son;
    }

    
    
    public char[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(char[][] matrix) {
        this.matrix = matrix;
    }
    
    
    
    
}
