package com.prova1.prova;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Rotas {
    Serie serie = new Serie();
    SerieDAO serieDAO = new SerieDAO();
    private static final String CRIARTABELA = "CREATE TABLE IF NOT EXISTS serie (id INTEGER PRIMARY KEY, nome VARCHAR(55), descricao VARCHAR(55), categoria VARCHAR(55), temporadas Integer, localOriem VARCHAR(55))";
    private static final String CRIAREGISTROS = "insert into serie (\"id\",\"nome\", \"descricao\", \"categoria\", \"temporadas\", \"localOriem\") values\n"
            + "(1, 'Rainha do sul', 'serie de trafico de drogas', 'acao', 5, 'Mexico'),\n"
            + "(2, 'The good doctor', 'hospital', 'documentorio', 2, 'EUA'),\n"
            + "(3, 'justiceiro', 'serie de guerra nas ruas', 'acao', 6, 'EUA'),\n"
            + "(4, 'Arcanjo renegado', 'serie policial', 'acao policial', 3, 'Brasil'),\n"
            + "(5, 'CSI', 'serie policial', 'investigacao', 8, 'EUA')";
    Conexao minhaConexao;

    public Rotas() {
        minhaConexao = new Conexao("jdbc:postgresql://localhost:5432/BDSerie", "postgres", "eryc");
    }

    @RequestMapping(value = "/config")
    public void addSerie(HttpServletRequest request, HttpServletResponse response) {
        try {
            minhaConexao.conectar();
            PreparedStatement instrucao = minhaConexao.getConexao().prepareStatement(CRIARTABELA);
            instrucao.execute();
            minhaConexao.desconectar();
        } catch (SQLException e) {
            System.out.println("Erro na criacao de tabelas: " + e.getMessage());
        }
        try {
            minhaConexao.conectar();
            PreparedStatement instrucao = minhaConexao.getConexao().prepareStatement(CRIAREGISTROS);
            instrucao.execute();
            minhaConexao.desconectar();
        } catch (SQLException e) {
            System.out.println("Erro na Inclusão: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/cliente")
    public void doTelaCliente(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<Serie> lista_serie = new ArrayList();
        lista_serie.addAll(serieDAO.BuscarGeral());

        var i = 0;

        response.getWriter().println("<html>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1> Seja bem vindo ao portal do cliente Sr(a) Cliente </h1>");
        response.getWriter().println("<br>");
        response.getWriter().println("<h2> Lista de Produtos Disponiveis</h2>");
        response.getWriter().println("<table>");
        for (i = 0; i < lista_serie.size(); i++) {
            response.getWriter().println("<tr>");
            response.getWriter().println("<td>" + "Nome: " + lista_serie.get(i).getNome() + "    |    " + "</td>");
            response.getWriter()
                    .println("<td>" + "Descricao: " + lista_serie.get(i).getDescricao() + "    |    " + "</td>");
            response.getWriter()
                    .println("<td>" + "Categoria: " + lista_serie.get(i).getCategoria() + "    |    " + "</td>");
            response.getWriter()
                    .println("<td>" + "Temporadas: " + lista_serie.get(i).getTemporadas() + "    |    " + "</td>");
            response.getWriter()
                    .println(
                            "<td>" + "Local de Origem: " + lista_serie.get(i).getLocalOrigem() + "    |    " + "</td>");
            response.getWriter().println("<td>");
            response.getWriter()
                    .println("<a href='/AddItemCarrinho?idSerie=" + lista_serie.get(i).getId() + "'>+ ADD</a>");
            response.getWriter().println("</td>");

            response.getWriter().println("</tr>");
        }
        response.getWriter().println("</table>");
        response.getWriter().println("<a href='/verCarrinho>Ver carrinho</a>");
        response.getWriter().println("</body>");
        response.getWriter().println("<html>");
    }

    @RequestMapping(value = "/verCarrinho")
    public void doVerCarrinho(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie carrinhoCompras = new Cookie("carrinhoCompras", "");
        carrinhoCompras.setMaxAge(60 * 60);
        Cookie[] requestCookies = request.getCookies();
        boolean achouCarrinho = false;

        if (requestCookies != null) {
            for (var c : requestCookies) {
                if (c.getName().equals("carrinhoCompras")) {
                    achouCarrinho = true;
                    carrinhoCompras = c;
                    break;
                }
            }
        }
        SerieDAO produtoDAO = new SerieDAO();
        Serie serie = null;
        var i = 0;
        ArrayList<Serie> lista_serie = new ArrayList();
        Double compra = 0.0;
        if (achouCarrinho == true) {
            StringTokenizer tokenizer = new StringTokenizer(carrinhoCompras.getValue(), "|");
            while (tokenizer.hasMoreTokens()) {
                serie = produtoDAO.buscarSerie(Integer.parseInt(tokenizer.nextToken()));
                lista_serie.add(serie);
            }
            response.getWriter().println("<html>");
            response.getWriter().println("<body>");
            response.getWriter().println("<h1>Seja bem vindo ao seu carrinho de compras</h1>");
            response.getWriter().println("<br>");
            response.getWriter().println("<table>");
            for (i = 0; i < lista_serie.size(); i++) {
                response.getWriter().println("<tr>");
                response.getWriter().println("<td>" + "Nome: " + lista_serie.get(i).getNome() + "    |    " + "</td>");
                response.getWriter()
                        .println("<td>" + "Descricao: " + lista_serie.get(i).getDescricao() + "    |    " + "</td>");
                response.getWriter()
                        .println("<td>" + "Categoria: " + lista_serie.get(i).getCategoria() + "    |    " + "</td>");
                response.getWriter()
                        .println("<td>" + "Temporadas: " + lista_serie.get(i).getTemporadas() + "    |    " + "</td>");
                response.getWriter()
                        .println(
                                "<td>" + "Local de Origem: " + lista_serie.get(i).getLocalOrigem() + "    |    "
                                        + "</td>");
                response.getWriter().println("<td>");
                response.getWriter().println("</td>");

                response.getWriter().println("</tr>");
            }
            response.getWriter().println("</table>");
            response.getWriter().println("</body>");
            response.getWriter().println("<html>");
        } else {
            response.getWriter().println("<html>");
            response.getWriter().println("<body>");
            response.getWriter().println("<h1>Você não possui compras, caso deseje comprar vá ao menu principal</h1>");
            response.getWriter().println("</body>");
            response.getWriter().println("<html>");
        }
    }

    @RequestMapping("/AddItemCarrinho")
    public void doAdicionaItem(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var idSerie = Integer.parseInt(request.getParameter("idProd"));
        SerieDAO prodDAO = new SerieDAO();
        var serie = prodDAO.buscarSerie(idSerie);

        Cookie carrinhoCompras = new Cookie("carrinhoCompras", "");
        carrinhoCompras.setMaxAge(60 * 60);
        Cookie[] requestCookies = request.getCookies();
        boolean achouCarrinho = false;

        if (requestCookies != null) {
            for (var c : requestCookies) {
                if (c.getName().equals("carrinhoCompras")) {
                    achouCarrinho = true;
                    carrinhoCompras = c;
                    break;
                }
            }
        }
        Serie produtoEscolhido = null;

        if (serie != null) {
            produtoEscolhido = serie;
            if (achouCarrinho == true) {
                String value = carrinhoCompras.getValue();
                carrinhoCompras.setValue(value + produtoEscolhido.getId() + "|");
            } else {
                carrinhoCompras.setValue(produtoEscolhido.getId() + "|");
            }
        } else {
            response.addCookie(carrinhoCompras);
        }
        response.addCookie(carrinhoCompras);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/telaCliente");
        dispatcher.forward(request, response);
    }

    @RequestMapping("/admin")
    public void doAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.sendRedirect("http://localhost:8080/indexCadastra.html");
    }

    @RequestMapping("/cadastra")
    public void doCadastra(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var nome = request.getParameter("nome");
        var descricao = request.getParameter("descricao");
        var categoria = request.getParameter("categoria");
        var temporadas = Integer.parseInt(request.getParameter("temporadas"));
        var pais = request.getParameter("pais");

        serie.setNome(nome);
        serie.setDescricao(descricao);
        serie.setCategoria(categoria);
        serie.setTemporadas(temporadas);
        serie.setLocalOrigem(pais);
        serie.setId(serieDAO.qtdSerie() + 1);
        serieDAO.incluirSerie(serie);
        response.sendRedirect("http://localhost:8080/admin");
    }

    @RequestMapping("/finazarCompra")
    public void finazarCompra(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("http://localhost:8080/index.html");
    }

}
