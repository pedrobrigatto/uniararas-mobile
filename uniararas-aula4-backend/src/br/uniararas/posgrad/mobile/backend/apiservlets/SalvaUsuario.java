package br.uniararas.posgrad.mobile.backend.apiservlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.uniararas.posgrad.mobile.backend.model.Cache;
import br.uniararas.posgrad.mobile.backend.model.Usuario;

import com.google.gson.Gson;

/**
 * Servlet implementation class SalvaUsuario
 */
public class SalvaUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SalvaUsuario() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String nome = request.getParameter("nome");
		String senha = request.getParameter("senha");
		
		System.out.println(String.format("Usuario: %s, %s, %s", username, nome, senha));
		
		if (username == null || "".equals(username) || senha == null || "".equals(senha)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			Usuario resposta = Cache.salvarUsuario(new Usuario(username, nome, senha));
			if (resposta != null) {
				response.getWriter().write(new Gson().toJson(resposta));
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		
		response.getWriter().flush();
	}
}
