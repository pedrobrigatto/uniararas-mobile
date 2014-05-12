package br.uniararas.posgrad.mobile.backend.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Cache para manter, em memória, alguns registros para exercitar a comunicação
 * remota do lado cliente (Android).
 * 
 * @author pedrobrigatto
 */
public class Cache {

	private static List<Usuario> USUARIOS = new ArrayList<Usuario>();
	private static final int QTDD_INICIAL_USUARIOS = 20;

	static {
		for (int i=0; i < QTDD_INICIAL_USUARIOS; i++) {
			USUARIOS.add(new Usuario("uniararas_" + i, "Usuário No. " + i, "uniararas123"));
		}
	}

	/**
	 * Usado para verificar usuários cadastrados no sistema.
	 * 
	 * @return Os usuários disponíveis em memória.
	 */
	public static List<Usuario> getUsuarios() {
		return USUARIOS;
	}

	/**
	 * Usado para pesquisar usuários em memória.
	 * 
	 * @param filtro restringe a pesquisa a um determinado username
	 * @return A lista de usuários que atende ao filtro fornecido
	 */
	public static List<Usuario> pesquisarUsuarios(String filtro) {
		
		System.out.println(String.format(">>>>>>>>>>>> Filtro usado foi %s", filtro));

		if (filtro != null && !"".equals(filtro)) {
			for (int i=0; i < QTDD_INICIAL_USUARIOS; i++) {
				if (USUARIOS.get(i).getUsername().equals(filtro)) {
					List<Usuario> resultado = new ArrayList<Usuario>();
					resultado.add(USUARIOS.get(i));
					return resultado;
				}
			}
			return null;
		}
		return USUARIOS;
	}
	
	public static Usuario salvarUsuario(Usuario usuario) {
		
		if (pesquisarUsuarios(usuario.getUsername()) == null) {
			if (USUARIOS.add(usuario)) {
				return usuario;
			}
		}
		return null;
	}
}
