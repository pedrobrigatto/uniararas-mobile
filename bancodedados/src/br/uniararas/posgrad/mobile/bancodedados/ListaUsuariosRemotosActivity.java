package br.uniararas.posgrad.mobile.bancodedados;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import br.uniararas.posgrad.mobile.bancodedados.model.Usuario;
import br.uniararas.posgrad.mobile.bancodedados.model.service.HttpUtils;

public class ListaUsuariosRemotosActivity extends ListActivity {
	
	private ProgressDialog dialogo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		dialogo = new ProgressDialog(this);
		dialogo.setTitle(getResources().getString(R.string.lista_remoto_progresso_titulo));
		dialogo.setMessage(getResources().getString(R.string.lista_remoto_progresso_mensagem));
		new ListaUsuarios().execute("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.lista_usuarios_remotos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class ListaUsuarios extends AsyncTask<String, Void, List<Usuario>> {
		
		@Override
		protected void onPreExecute() {
			dialogo.show();
		}

		@Override
		protected void onPostExecute(List<Usuario> result) {
			setListAdapter(new AdaptadorUsuarios(ListaUsuariosRemotosActivity.this, result));
			dialogo.dismiss();
		}

		@SuppressWarnings("unchecked")
		@Override
		protected List<Usuario> doInBackground(String... params) {
			if (params != null && params.length == 1) {
				return HttpUtils.buscarRegistros(Usuario.class, params[0]);
			}
			return new ArrayList<>();
		}
	}
}
