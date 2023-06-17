package com.mycompany.laboratorio;

public class Servidor extends Tipo {
    public Servidor(String porta, String nome){
        super(porta, nome);
    }
    
    @Override
    public void run(){
        this.iniciarConexao();
    } 
}