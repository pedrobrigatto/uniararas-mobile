package br.uniararas.posgrad.mobile.backend.model;

/**
 * Representa��o de usu�rio no sistema. Note que esta representa��o � id�ntica a
 * de usu�rios no lado cliente (mobile). Logo, conv�m que coloquemos essas 
 * defini��es em uma biblioteca a parte.
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
