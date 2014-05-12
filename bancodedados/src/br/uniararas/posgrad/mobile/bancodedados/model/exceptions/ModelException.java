package br.uniararas.posgrad.mobile.bancodedados.model.exceptions;

/**
 * Simboliza, genericamente, problemas que ocorram durante a execu��o de
 * opera��es de neg�cio no aplicativo.
 * 
 * @author pedrobrigatto
 */
public class ModelException extends Exception {
	
	private static final long serialVersionUID = -7945526621818267535L;

	public ModelException (String mensagem) {
		super(mensagem);
	}
}
