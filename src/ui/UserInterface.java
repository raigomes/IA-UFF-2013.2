/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import controller.Command;
import java.util.Scanner;

/**
 *
 * @author rai
 */
public class UserInterface {
        
    public static void show() {
        String input;
        int opcao;
        
        System.out.println("Universidade Federal Fluminense\nTrabalho de IA 2013.2: Fill-in blanks\nProfª.: Aline Paes");        
        
        Scanner scn = new Scanner(System.in);        
        
        String path = setPath(scn);
        
        do {
            input = chooseEntryType(scn);         
            if(input != null)
                opcao = chooseSearchType(scn, input, path);                
            else 
                opcao = 0;
        }while(opcao != 0);
    }

    private static boolean isLetter(String msg) {
        for (int i = 0; i < msg.length(); i++) {
            char c = msg.charAt(i);
            if(c < 65 || (c > 90 && c < 97) || c > 122) {
                return false;
            }
        }
        return true;
    }

    private static String setPath(Scanner scn) {
        String msg;
        int opcao;
        
        try{
            System.out.println("\nDiretório default dos arquivos de entrada: "+Command.getPath()+"\nGostaria de mudar?");
            System.out.println("1) Sim");
            System.out.println("2) Não\n");
            System.out.println("0) Sair");
            
            msg = scn.nextLine();
            opcao = Integer.parseInt(msg);
             
            switch(opcao) {
                case 0:
                    exit();                    
                    break;
                case 1:
                    System.out.println("\nInsira o novo caminho para o diretório:");
                    msg = scn.nextLine();
                    break;
                case 2:
                    msg = Command.getPath();
                    break;
                default:
                    System.out.println("Opção inválida!");
                    msg = setPath(scn);
                    break;
            }            
            return msg;                        
            
        }
        catch(NumberFormatException ex) {
            System.out.println("Opção inválida!");
            return setPath(scn);
        }
    }
    
    private static int chooseSearchType(Scanner scn, String input, String path) {        
        Command cmd;
        String msg;
        int opcao;
        
        try {
            System.out.println("\nQual(is) busca(s) você deseja avaliar?");
            System.out.println("1) Busca A*");
            System.out.println("2) Hill Climbing");
            System.out.println("3) Simulated Annealing\n");
            System.out.println("0) Sair");
            //System.out.println("9) Voltar");
            System.out.println("Digite a opcao e pressione ENTER:");
            msg = scn.nextLine();
            opcao = Integer.parseInt(msg);

            switch(opcao) {
                case 0: 
                    exit();
                    break;
                case 1:
                    cmd = new Command("AStar", Command.SEARCH, input, path);
                    cmd.print();
                    cmd.execute();
                    break;
                case 2:
                    cmd = new Command("Hill", Command.SEARCH, input, path);
                    cmd.print();
                    cmd.execute();
                    break;
                case 3:
                    cmd = new Command("Simulated", Command.SEARCH, input, path);
                    cmd.print();
                    cmd.execute();
                    break;  
//                case 9:
//                    chooseEntryType(scn);
//                    break;
                default:
                    System.out.println("Opção inválida!");
                    chooseSearchType(scn, input, path);
            }
            return opcao;
        }
        
        catch(NumberFormatException ex) {
            System.out.println("Opção Inválida");
            return chooseSearchType(scn, input, path);
        }
    }

    private static String chooseEntryType(Scanner scn) {
        String msg;
        Command cmd;
        int opcao;
        
        try {
            System.out.println("\nEscolha um tipo de entrada:");
            System.out.println("1) Pré-definida");
            System.out.println("2) Manual");
            System.out.println("3) Aleatória\n");
            System.out.println("0) Sair");
            //System.out.println("9) Voltar");
            System.out.println("Digite a opcao e pressione ENTER:");
            msg = scn.nextLine();
            opcao = Integer.parseInt(msg);

            switch(opcao) {
                case 0: 
                    exit();
                    msg = null;
                    break;
                case 1:
                    cmd = new Command("Pre", Command.ENTRY);
                    cmd.execute();                    
                    msg = cmd.getInput();
                    break;
                case 2:
                    System.out.println("Adicione a entrada do algoritmo (8 letras):");
                    msg = scn.nextLine();

                    if(msg.length() >= 9 && isLetter(msg)){
                        msg = msg.substring(0, 9);                        
                    }
                    else {
                        System.out.println("Entrada inválida");
                        return chooseEntryType(scn);
                    }

                    cmd = new Command("Manual", Command.ENTRY, msg, Command.getPath());
                    cmd.execute();
                    msg = cmd.getInput();
                    break;
                case 3:
                    cmd = new Command("Aleatoria", Command.ENTRY);
                    cmd.execute();
                    msg = cmd.getInput();
                    break;       
//                case 9:
//                    setPath(scn);
//                    break;
                default:
                    System.out.println("Opção inválida!");
                    chooseEntryType(scn);
            }
            return msg;
        }
        catch(NumberFormatException e) {
            System.out.println("Opção inválida!");
            return chooseEntryType(scn);
        }
    }
    
    public static void credits(){
        System.out.println("\nCréditos:\nGisele Alves\nRafael Morett\nRaí Gomes\nTaiane Manhães");
    }

    public static void update(String input) {
        System.out.println("\n"+input);
    }

    private static void exit() {
        Command cmd = new Command("Sair", Command.EXIT);                    
        cmd.execute();
    }
    
}
