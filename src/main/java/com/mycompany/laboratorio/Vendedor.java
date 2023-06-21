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
        while(true) {


                if (!Eleicao.getInstance().isEleicaoIniciada()) {
                    Processo processo = Processos.getInstance().getRandomProcesso();
                    if (!processo.isLider()) {
                        try {
                            ClienteSocket socket = new ClienteSocket(processo.getHost(), processo.getPort());

                            ExibirTabelas();

                            System.out.println("\nREALIZAR UMA VENDA:");

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

                            String resposta = socket.receber();
                            if (resposta.equals("OK") || resposta.equals("ERRO")) {
                                System.out.println("Resposta do servidor: " + resposta);
                            }

                            Thread.sleep(1000 * 5);
                        } catch (IOException ex) {
                            Logger.getLogger(MultiPrograma.class.getName()).log(Level.SEVERE, "Erro na conexao com " + processo.getIdentificador() + ": " + ex.getMessage());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        this.checkLider();
                        try {
                            Thread.sleep(1000 * 5);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        }
    }

    public void ExibirTabelas() {
        try {
            if (!Processos.getInstance().checkServidor().equals("PING")) {
                Eleicao.getInstance().callEleicao();
            }
            Connection connection = null;
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();

            ResultSet rsVendedores = statement1.executeQuery("SELECT * FROM vendedores");
            ResultSet rsProdutos = statement2.executeQuery("SELECT * FROM produtos");

            System.out.println("NOME DO VENDEDORES:");
            boolean primeiroVendedor = true;
            while (rsVendedores.next()) {
                if (!primeiroVendedor) {
                    System.out.print(", ");
                }
                System.out.print(rsVendedores.getString("nome"));
                primeiroVendedor = false;
            }

            System.out.println("\nNOME DO PRODUTOS:");
            boolean primeiroProduto = true;
            while (rsProdutos.next()) {
                if (!primeiroProduto) {
                    System.out.print(", ");
                }
                System.out.print(rsProdutos.getString("nome"));

                primeiroProduto = false;
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

}
