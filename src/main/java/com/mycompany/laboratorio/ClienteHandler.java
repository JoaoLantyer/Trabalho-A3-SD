package com.mycompany.laboratorio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;

public class ClienteHandler implements Runnable {

    private Socket socket;
    private Integer identificador;

    private Processo me;

    public ClienteHandler(Socket socket, Integer identificador, Processo me) {
        this.socket = socket;
        this.identificador = identificador;
        this.me = me;
    }

    @Override
    public void run() {
        try {
            System.out.println("Cliente conectado: " + socket.getInetAddress().getHostAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String msg = in.readLine();
            System.out.println("Mensagem recebida: " + msg);
            String[] protocolo = msg.split("\\|");
            switch (protocolo[0]) {
                case "01":
                    out.println("Oi! E eu sou o processo " + this.me.getIdentificador());
                    break;
                case "02":
                    if (me.isLider()) {
                        out.println("Eu sou o líder! Processo " + this.me.getIdentificador());
                    } else {
                        out.println("Eu não sou o líder, eu sou o processo " + this.me.getIdentificador());
                    }
                    break;
                case "03":
                    out.println("OK");
                    Eleicao.getInstance().checkEleicao(protocolo[1]);
                    break;
                case "04":
                    out.println("OK");
                    Eleicao.getInstance().atualizarLider(Integer.valueOf(protocolo[1]));
                    break;
                case "05":
                    if (protocolo.length >= 5) {
                        int quantidade = Integer.parseInt(protocolo[1]);
                        String nomeProduto = protocolo[2];
                        String nomeVendedor = protocolo[3];
                        String dataVenda = protocolo[4];

                        boolean vendaRealizada = realizarVenda(nomeVendedor, nomeProduto, quantidade, dataVenda);

                        if (vendaRealizada){
                            out.println("OK");
                        } else {
                            out.println("ERRO");
                        }
                    }
                    break;
                default:
                    System.out.println("codigo: " + protocolo[0]);
                    out.println("09|Error");
                    break;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean realizarVenda(String nomeVendedor, String nomeProduto, int quantidade, String dataVenda) {
        boolean vendaRealizada = false;
        try {
            Connection connection = null;
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            String selectVendedorQuery = "SELECT id FROM vendedores WHERE nome = ?";
            PreparedStatement selectVendedorStmt = connection.prepareStatement(selectVendedorQuery);
            selectVendedorStmt.setString(1, nomeVendedor);
            ResultSet rsVendedor = selectVendedorStmt.executeQuery();

            int vendedorId = -1;
            if (rsVendedor.next()) {
                vendedorId = rsVendedor.getInt("id");
            }

            String selectProdutoQuery = "SELECT id FROM produtos WHERE nome = ?";
            PreparedStatement selectProdutoStmt = connection.prepareStatement(selectProdutoQuery);
            selectProdutoStmt.setString(1, nomeProduto);
            ResultSet rsProduto = selectProdutoStmt.executeQuery();

            int produtoId = -1;
            if (rsProduto.next()) {
                produtoId = rsProduto.getInt("id");
            }

            if (vendedorId != -1 || produtoId != -1) {
                vendaRealizada = true;
            }

            String insertVendaQuery = "INSERT INTO vendas (id_vendedor, id_produto, quantidade, data_venda) VALUES (?, ?, ?, ?)";
            PreparedStatement insertVendaStmt = connection.prepareStatement(insertVendaQuery);

            insertVendaStmt.setInt(1, vendedorId);
            insertVendaStmt.setInt(2, produtoId);
            insertVendaStmt.setInt(3, quantidade);
            insertVendaStmt.setString(4, dataVenda);
            insertVendaStmt.executeUpdate();

        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        return vendaRealizada;
    }
}
