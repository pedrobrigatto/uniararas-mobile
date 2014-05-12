package br.uniararas.posgrad.mobile.backend.model;

/**
 * Representação de usuário no sistema. Note que esta representação é idêntica a
 * de usuários no lado cliente (mobile). Logo, convém que coloquemos essas 
 * definições em uma biblioteca a parte.
 * 
 * @author pedrobrigatto
 */
public class Usuario {
	
	private String username;
	private String nome;
	private String senha;
	
	public Usuario() {}
	
	public Usuario(String username, String nome, String senha) {
		super();
		this.username = username;
		this.nome = nome;
		this.senha = senha;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
