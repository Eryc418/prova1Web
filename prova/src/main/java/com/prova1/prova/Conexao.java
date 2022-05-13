package com.prova1.prova;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {
    private String caminho;
    private String usuario;
    private String senha;
    private Connection conexao;

    public Conexao(String c, String u, String s) {
        this.caminho = c;
        this.usuario = u;
        this.senha = s;
    }

    public void conectar() {
        try {
            Class.forName("org.postgresql.Driver");
            conexao = DriverManager.getConnection(caminho, usuario, senha);
        } catch (Exception e) {
            System.out.println("Erro ao conectar ao conectar ao banco");
        }
    }

    public void desconectar() {
        try {
            conexao.close(); // fecha a conexão com o PostgreSQL
        } catch (Exception e) {
            System.out.println("Erro na desconexão: " + e.getMessage());
        }
    }

    public Connection getConexao() {
        return conexao;
    }
}
