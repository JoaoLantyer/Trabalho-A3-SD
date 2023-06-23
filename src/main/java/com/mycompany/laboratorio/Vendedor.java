package com.mycompany.laboratorio;

import java.io.IOException;
import java.sql.*;
import java.util.InputMismatchException;
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

                if(!Processos.getInstance().checkServidor() || Processos.getInstance().getLider() != Processos.getInstance().getProcessoByIdentificador(5)){
                    Eleicao.getInstance().callEleicao();
                }

                if(Processos.getInstance().getMe() == Processos.getInstance().getLider()){
                    System.out.println("Servidor indisponível, executando servidor temporário: ");
                }

                Processo processoLider = Processos.getInstance().getLider();


                    try {

                        Thread.sleep(500);

                        ClienteSocket socket = new ClienteSocket(processoLider.getHost(), processoLider.getPort());

                        ExibirTabelas();

                        try {

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


                            socket.enviar("05|" + nomeVendedor + "|" + nomeProduto + "|" + quantidade + "|" + dataVenda);

                        } catch (InputMismatchException e) {
                            System.out.println("ERRO! VALOR INVALIDO!");
                            run();
                            return;
                        }

                        String resposta = socket.receber();
                        if (resposta.equals("OK - venda realizada") || resposta.equals("ERRO - dados inválidos tente novamente")) {
                            System.out.println("Resposta do servidor: " + resposta);
                        }

                        Thread.sleep(1000 * 5);
                    } catch (IOException ex) {
                        Logger.getLogger(MultiPrograma.class.getName()).log(Level.SEVERE, "Erro na conexao com " + processoLider.getIdentificador() + ": " + ex.getMessage());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    public void ExibirTabelas() {
        try {
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
