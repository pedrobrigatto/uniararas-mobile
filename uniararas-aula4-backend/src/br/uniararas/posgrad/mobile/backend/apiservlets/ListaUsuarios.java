package br.uniararas.posgrad.mobile.backend.apiservlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.uniararas.posgrad.mobile.backend.model.Cache;

import com.google.gson.Gson;

/**
 * Servlet implementation class ListaUsuarios
 */
public class ListaUsuarios extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ListaUsuarios() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filtro = request.getParameter("username");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF8");
		response.getWriter().write(new Gson().toJson(Cache.pesquisarUsuarios(filtro)));
	}
}
