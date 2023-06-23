package com.mycompany.trabalhoa3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClienteSocket {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClienteSocket(String host, Integer port) throws IOException{
        this.socket = new Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(),true);
    }

    public void enviar(String msg){
        out.println(msg);
    }

    public String receber() throws IOException{
        return in.readLine();
    }

    public void fechar() throws IOException{
        in.close();
        out.close();
    }
}
