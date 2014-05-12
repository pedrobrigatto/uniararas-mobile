package br.uniararas.posgrad.mobile.bancodedados.model.exceptions;

/**
 * Simboliza, genericamente, problemas que ocorram durante a execução de
 * operações de negócio no aplicativo.
 * 
 * @author pedrobrigatto
 */
public class ModelException extends Exception {
	
	private static final long serialVersionUID = -7945526621818267535L;

	public ModelException (String mensagem) {
		super(mensagem);
	}
}
