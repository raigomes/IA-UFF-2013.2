/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fillinblanks;

/**
 *
 * @author rai
 */
public class FillinBlanks {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String s = "AEOPRRSWY"; 
        char[] vector = s.toUpperCase().toCharArray();
        Tree t = new Tree();
        t.search(vector);
        
    }
}
