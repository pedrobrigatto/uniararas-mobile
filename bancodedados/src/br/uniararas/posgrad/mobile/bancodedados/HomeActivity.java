package br.uniararas.posgrad.mobile.bancodedados;

import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import br.uniararas.posgrad.mobile.bancodedados.model.Usuario;
import br.uniararas.posgrad.mobile.bancodedados.model.db.impl.UsuarioDao;

/**
 * Tela na qual podem ser visualizados usuários cadastrados no banco de dados local.
 * 
 * @author pedrobrigatto
 */
public class HomeActivity extends ActionBarActivity {
	
	private EditText txtPesquisa;
	private ImageButton btnPesquisa;
	private ListView listaUsuarios;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		txtPesquisa = (EditText) findViewById(R.id.home_txt_pesquisa);
		btnPesquisa = (ImageButton) findViewById(R.id.home_btn_pesquisa);
		listaUsuarios = (ListView) findViewById(R.id.home_lista);
		
		btnPesquisa.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				carregarUsuarios();
			}
		});
	}
	
	private void carregarUsuarios() {
		
		// Primeiro passo: ler o que o usuário forneceu como filtro
		String filtro = txtPesquisa.getText().toString();
		
		// Segundo passo: chamar o DAO para realizar a pesquisa no banco local
		UsuarioDao dao = new UsuarioDao(this);
		List<Usuario> resultados = dao.listar(filtro);
		
		// Terceiro passo: carregar a lista com os dados retornados
		AdaptadorUsuarios adaptador = new AdaptadorUsuarios(this, resultados);
		listaUsuarios.setAdapter(adaptador);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
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
}
