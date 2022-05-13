package com.prova1.prova;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SerieDAO {
    Conexao minhaConexao;
    private static final String RELATORIOSERIE = "select * from \"serie\"";
    private static final String BUSCASERIE = "select * from \"serie\" where \"id\"=?";
    private static final String QTDSERIE = "select count(*) from \"serie\"";
    private static final String INCLUIRSERIE = "insert into \"serie\" (\"id\", \"nome\", \"descricao\", \"categoria\", \"temporadas\",  \"localOriem\") values (?,?,?,?,?,?)";

    public SerieDAO() {
        minhaConexao = new Conexao("jdbc:postgresql://localhost:5432/BDSerie", "postgres", "eryc");
    }

    public ArrayList<Serie> BuscarGeral() {
        ArrayList<Serie> lista_serie = new ArrayList<>();
        Serie s = null;
        try {
            minhaConexao.conectar();
            Statement instrucao = minhaConexao.getConexao().createStatement();
            ResultSet rs = instrucao.executeQuery(RELATORIOSERIE);
            while (rs.next()) {
                s = new Serie(rs.getInt("id"), rs.getString("nome"), rs.getString("descricao"),
                        rs.getString("categoria"), rs.getInt("temporadas"), rs.getString("localOriem"));
                lista_serie.add(s);
            }
            minhaConexao.desconectar();
        } catch (SQLException e) {
            System.out.println("Erro na busca geral: " + e.getMessage());
        }
        return lista_serie;
    }

    public Serie buscarSerie(Integer idSerie) {

        Serie serie = null;
        try {
            minhaConexao.conectar();
            PreparedStatement instrucao = minhaConexao.getConexao().prepareStatement(BUSCASERIE);
            instrucao.setInt(1, idSerie);
            ResultSet rs = instrucao.executeQuery();
            if (rs.next()) {
                serie = new Serie(rs.getInt("id"), rs.getString("nome"), rs.getString("descricao"),
                        rs.getString("categoria"), rs.getInt("temporadas"), rs.getString("localOriem"));
            }
            minhaConexao.desconectar();
        } catch (SQLException e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
        return serie;
    }

    public int qtdSerie() {
        int qtd = 0;
        try {
            minhaConexao.conectar();
            Statement instrucao = minhaConexao.getConexao().createStatement();
            ResultSet rs = instrucao.executeQuery(QTDSERIE);
            if (rs.next()) {
                qtd = rs.getInt("count");
            }
            minhaConexao.desconectar();
        } catch (SQLException e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
        return qtd;
    }

    public void incluirSerie(Serie serie) {
        try {
            minhaConexao.conectar();
            PreparedStatement instrucao = minhaConexao.getConexao().prepareStatement(INCLUIRPRODUTO);
            instrucao.setInt(1, serie.getId());
            instrucao.setString(2, serie.getNome());
            instrucao.setString(3, serie.getDescricao());
            instrucao.setString(4, serie.getCategoria());
            instrucao.setInt(5, serie.getTemporadas());
            instrucao.setString(6, serie.getLocalOrigem());
            instrucao.execute();
            minhaConexao.desconectar();
        } catch (SQLException e) {
            System.out.println("Erro na Inclusão: " + e.getMessage());
        }
    }
}
