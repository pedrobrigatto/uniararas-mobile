package br.uniararas.posgrad.mobile.bancodedados.model.db.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Utilitário usado para recuperar acesso ao banco de dados local do aplicativo.
 * 
 * @author pedrobrigatto
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final int VERSAO_DB = 1;
	
	private static final String DB_NAME = "uniararasdb";
	private static final String TABELA_USUARIO = "usuario";
	
	private final String CRIA_TABELA_USUARIO = 
			"create table " + TABELA_USUARIO + "(username text not null primary key, nome text, senha text not null)";
	private final String DELETA_USUARIO = "drop table " + TABELA_USUARIO + ";";

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSAO_DB);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CRIA_TABELA_USUARIO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DELETA_USUARIO);
		onCreate(db);
	}
}
