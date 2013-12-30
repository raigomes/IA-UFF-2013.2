/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author rai
 */
public class FillinBlanks {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        String s = "WESOPRYAR"; 
//        String s = "AEEIKLLPY";         
//        String s = "APEILKLYE";
        
        String s = "";
        
        for (int i = 0; i < 9; i++) {            
            char value = (char) (65 + (Math.random() * 25));
            s += new Character(value);
        }
        System.out.println("Input = "+ s);
        
        char[] vector = s.toUpperCase().toCharArray();
        Tree t = new Tree();
        t.search(vector);
        
        
//        char[][] matrix = new char[4][4];
//        
//        for (int i = 0; i < matrix.length; i++) {            
//            matrix[0][i] = '$';
//            if(i > 0) {
//                matrix[i] = ("$"+ s.substring((i-1)*3, i*3)).toCharArray();
//            }
//        }
//        
//        SortedSet<String> dic = t.getDictionaryFromFile();
//        t.printWords(matrix, dic);
//        t.search(vector);
    }
}
