package br.uniararas.posgrad.mobile.bancodedados.model.db.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.uniararas.posgrad.mobile.bancodedados.model.Usuario;
import br.uniararas.posgrad.mobile.bancodedados.model.db.IDao;

/**
 * Objeto de acesso a dados para manipulação de usuários.
 * Essa manipulação é realizada no banco de dados local do aplicativo.
 * 
 * @author pedrobrigatto
 */
public class UsuarioDao implements IDao<Usuario> {

	private Context contexto;
	private DatabaseHelper dbHelper;

	public UsuarioDao(Context contexto) {
		this.contexto = contexto;
	}

	@Override
	public boolean salvar(Usuario registro) {

		// Trabalhando com banco de dados
		if (dbHelper == null) {
			dbHelper = new DatabaseHelper(contexto);
		}

		SQLiteDatabase bd = dbHelper.getWritableDatabase();

		// Preenchendo os valores que devem ser considerados na inserção do registro
		ContentValues valoresInsert = new ContentValues();
		valoresInsert.put("username", registro.getUsername());
		valoresInsert.put("nome", registro.getNome());
		valoresInsert.put("senha", registro.getSenha());

		long resultado = bd.insert("usuario", null, valoresInsert);
		return (resultado > 0);
	}

	@Override
	public boolean atualizar(Usuario registro) {
		return false;
	}

	@Override
	public boolean excluir(Usuario registro) {
		return false;
	}

	@Override
	public List<Usuario> listar(String... criterios) {
		
		Cursor cursor = null;
		List<Usuario> usuarios = new ArrayList<>();
		
		if (dbHelper == null) {
			dbHelper = new DatabaseHelper(contexto);
		}
		
		if (criterios == null || criterios.length == 0 || "".equals(criterios[0])) {  // listar todos
			
			cursor = dbHelper.getReadableDatabase().
					query(true, "usuario", null, null, null, null, null, null, null);
			
		} else if (criterios.length == 1) {  // pesquisa feita por username
			
			cursor = dbHelper.getReadableDatabase().
					query(true, "usuario", null, "username=?", 
					new String[]{criterios[0]}, null, null, null, null);
		}

		// De usuário a usuário retornado, alimento a lista com mais um objeto
		while (cursor.moveToNext()) {
			String username = cursor.getString(cursor.getColumnIndex("username"));
			String nome = cursor.getString(cursor.getColumnIndex("nome"));
			String senha = cursor.getString(cursor.getColumnIndex("senha"));
			usuarios.add(new Usuario(username, nome, senha));
		}
		
		return usuarios;
	}
}
