package br.uniararas.posgrad.mobile.bancodedados;

import java.util.List;

import br.uniararas.posgrad.mobile.bancodedados.model.Usuario;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adaptador para dados de usuários em telas que utilizem componentes
 * que trabalham com coleções de dados, como listas ou caixas de seleção.
 * 
 * @author pedrobrigatto
 */
public class AdaptadorUsuarios extends BaseAdapter {
	
	private List<Usuario> usuarios;
	private Context contexto;
	
	public AdaptadorUsuarios (Context contexto, List<Usuario> usuarios) {
		this.usuarios = usuarios;
		this.contexto = contexto;
	}

	@Override
	public int getCount() {
		return usuarios.size();
	}

	@Override
	public Object getItem(int position) {
		return usuarios.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView label = new TextView(contexto);
		label.setText(usuarios.get(position).getUsername() + 
				" ---> " + usuarios.get(position).getNome());
		return label;
	}
}
