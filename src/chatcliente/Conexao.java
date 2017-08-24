/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatcliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class Conexao {

    private String host, nick;
    private final int porta;
    private Socket cliente;
    private Scanner teclado; //entrada de texto do teclado
    private PrintStream saida;//saida de texto na tela
    
    public Conexao(String host, String nick, int porta) {
        this.host = host;
        this.nick = nick;
        this.porta = porta;
    }

    public void executa() throws UnknownHostException, IOException {
        try {
            //TelaChat tc =new TelaChat(nick);
            //tc.setVisible(true);
            cliente = new Socket(this.host, this.porta);
            //tc.campoPrincipal.setText("O cliente " + nick +" se conectou ao servidor!");
            // thread para receber mensagens do servidor
            Recebedor r = new Recebedor(cliente.getInputStream());
            new Thread(r).start();
            
            // lê msgs do teclado e manda pro servidor
            teclado = new Scanner(System.in);
            saida = new PrintStream(cliente.getOutputStream());
            while (teclado.hasNextLine()) {
                saida.println(nick + " : " + teclado.nextLine());
            }
            System.out.println("sucesso");
        } catch (Exception e) {
            System.out.println("Não foi possivel se conectar ao servidor");
        } finally {
            try {
                saida.close();
                teclado.close();
                cliente.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    
    public String getNick(){
      return this.nick;  
    }

}
