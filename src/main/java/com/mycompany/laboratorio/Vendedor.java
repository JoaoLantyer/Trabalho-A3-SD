package com.mycompany.laboratorio;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class Vendedor extends Tipo {
    Scanner scanner = new Scanner(System.in);
    public Vendedor(String porta, String nome){
        super(porta, nome);
    }
    
    @Override
    public void run(){
        while(true){
            if(!Eleicao.getInstance().isEleicaoIniciada()){
                Processo processo = Processos.getInstance().getRandomProcesso();
                if(!processo.isLider()){
                    try {
                        ClienteSocket socket = new ClienteSocket(processo.getHost(), processo.getPort());  

                        ExibirTabelas();

                        System.out.println("REALIZAR UMA VENDA:");

                        System.out.print("Digite o nome do vendedor: ");
                        String nomeVendedor = scanner.nextLine();

                        System.out.print("Digite o nome do produto: ");
                        String nomeProduto = scanner.nextLine();

                        System.out.print("Digite a quantidade de " + nomeProduto + "s vendidos: ");
                        int quantidade = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Digite a data da venda (no formato AAAA-MM-DD): ");
                        String dataVenda = scanner.nextLine();


                        socket.enviar("05|" + quantidade + "|" + nomeProduto + "|" + nomeVendedor + "|" + dataVenda);


                        Thread.sleep(1000*5);
                    } catch (IOException ex) {
                        Logger.getLogger(MultiPrograma.class.getName()).log(Level.SEVERE,"Erro na conexao com " + processo.getIdentificador() + ": " + ex.getMessage());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    this.checkLider();
                    try {
                        Thread.sleep(1000*5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public void ExibirTabelas(){
        try {
            Connection connection = null;
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();
            Statement statement3 = connection.createStatement();

            ResultSet rsVendedores = statement1.executeQuery("SELECT * FROM vendedores");
            ResultSet rsProdutos = statement2.executeQuery("SELECT * FROM produtos");
            ResultSet rsVendas = statement3.executeQuery("SELECT * FROM vendas");

            while(rsVendedores.next()) {
                System.out.println("NOME DO VENDEDOR  : " + rsVendedores.getString("nome"));
            }
            while(rsProdutos.next()) {
                System.out.println("NOME DO PRODUTO  : " + rsProdutos.getString("nome"));
            }
            while(rsVendas.next()) {
                System.out.println("VENDAS  : " + rsVendas.getInt("id_vendedor"));
            }

        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}