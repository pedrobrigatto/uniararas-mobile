package br.uniararas.posgrad.mobile.bancodedados.model.db;

import java.util.List;

public interface IDao<T> {
	
	/**
	 * 
	 * @param registro
	 * @return
	 */
	boolean salvar(T registro);
	
	boolean atualizar(T registro);
	
	boolean excluir (T registro);
	
	List<T> listar(String ... criterios);
}
