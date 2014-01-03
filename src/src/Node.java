/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author rai
 */
public class Node implements Comparable<Node> {
    private int position;
    private Character name;
    private double cost, function;
    private char[][] matrix;
    private Node dad;
    private List<Character> child;

    public Node(char name) {
        position = 0;
        cost = 0.0d; function = 0.0d;
        this.name = name;
        matrix = new char[4][4];
        for (int i = 0; i < matrix.length; i++) {
            matrix[0][i] = '$';
            matrix[i][0] = '$';
        }
        dad = null;
        child = new LinkedList<Character>();
    }        

    public void incPosition() {
        position++;
    }
    
    public void decPosition() {
        position--;
    }
    
    public int getPosition() {
        return position;
    }

    public void setPosition(int newPosition) {
        this.position = newPosition;
    }            
    
    public Character getName() {
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

    public List<Character> getChild() {
        return child;
    }

    public void setChild(List<Character> child) {
        for(char c: child) {
            this.child.add(c);
        }
    }    
      
    public char[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, this.getMatrix()[i], 0, matrix.length);
        }
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

    @Override
    public int compareTo(Node n) {
        if(this.name == n.name) {
            if(this.dad == n.dad)
                return 0;
            else
                return 1;
        }
        else {
            return -1;
        }
    }                
}