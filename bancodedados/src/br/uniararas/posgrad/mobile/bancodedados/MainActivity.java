package br.uniararas.posgrad.mobile.bancodedados;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.uniararas.posgrad.mobile.bancodedados.model.Usuario;
import br.uniararas.posgrad.mobile.bancodedados.model.db.impl.UsuarioDao;
import br.uniararas.posgrad.mobile.bancodedados.model.exceptions.ModelException;
import br.uniararas.posgrad.mobile.bancodedados.model.service.HttpUtils;

/**
 * Tela na qual é possível cadastrar novos usuários no aplicativo.
 * 
 * @author pedrobrigatto
 */
public class MainActivity extends ActionBarActivity {

	private EditText txtUsername;
	private EditText txtSenha;
	private EditText txtNome;
	private Button btnSalvar;
	private Button btnListar;
	private Button btnSalvarRemoto;
	private Button btnListarRemoto;
	
	private ProgressDialog dialogoProgresso;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtUsername = (EditText) findViewById(R.id.main_txt_username);
		txtSenha = (EditText) findViewById(R.id.main_txt_senha);
		txtNome = (EditText) findViewById(R.id.main_txt_nome);
		btnSalvar = (Button) findViewById(R.id.main_btn_salvar);
		btnListar = (Button) findViewById(R.id.main_btn_listar);
		btnSalvarRemoto = (Button) findViewById(R.id.main_btn_salvar_remoto);
		btnListarRemoto = (Button) findViewById(R.id.main_btn_listar_remoto);
		
		dialogoProgresso = new ProgressDialog(this);
		dialogoProgresso.setTitle(getResources().getString(R.string.main_progresso_titulo));
		dialogoProgresso.setMessage(getResources().getString(R.string.main_progresso_mensagem));

		btnSalvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				salvarUsuarioLocal();
			}
		});

		btnListar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listarUsuarios();
			}
		});

		btnSalvarRemoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				salvarUsuarioRemoto();
			}
		});
		
		btnListarRemoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listarUsuarioRemoto();
			}
		});
	}

	private void listarUsuarios() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}
	
	private void listarUsuarioRemoto() {
		startActivity(new Intent("LISTAR_USUARIOS_REMOTOS"));
	}

	private void salvarUsuarioLocal() {

		UsuarioDao dao = new UsuarioDao(this);

		if (camposPreenchidos()) {
			
			// Criar um usuário com dados fornecidos pelo formulário
			String nome = txtNome.getText().toString();
			String username = txtUsername.getText().toString();
			String senha = txtSenha.getText().toString();

			// Quando eu quiser salvar um usuário, é assim que se faz agora:
			if (dao.salvar(new Usuario(username, nome, senha))) {
				Toast.makeText(this, "Registro inserido", Toast.LENGTH_LONG).show();
				limparCampos();
			}
		} else {
			Toast.makeText(this, 
					getResources().getString(R.string.main_notificacao_campos_vazios), 
					Toast.LENGTH_LONG).show();
		}
	}

	private void salvarUsuarioRemoto() {
		if (camposPreenchidos()) {
			// Criar um usuário com dados fornecidos pelo formulário
			String nome = txtNome.getText().toString();
			String username = txtUsername.getText().toString();
			String senha = txtSenha.getText().toString();
			new SalvarUsuario().execute(new Usuario(username, nome, senha));
		}		
	}

	private void limparCampos() {
		this.txtNome.setText("");
		this.txtUsername.setText("");
		this.txtSenha.setText("");
	}

	private boolean camposPreenchidos() {
		return (txtUsername.getText().toString() != null &&
				!"".equals(txtUsername.getText().toString()) &&
				txtNome.getText().toString() != null &&
				!"".equals(txtNome.getText().toString()) &&
				txtSenha.getText().toString() != null &&
				!"".equals(txtSenha.getText().toString()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
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

	/**
	 * Tarefa usada para salvar um usuário no sistema, remotamente.
	 * 
	 * @author pedrobrigatto
	 */
	private class SalvarUsuario extends AsyncTask<Usuario, String, Usuario> {

		@Override
		protected void onPreExecute() {
			Log.i("[uniararas-mobile]", "Preparativos para salvar um usuário remotamente");
			dialogoProgresso.show();
		}

		@Override
		protected void onPostExecute(Usuario result) {
			
			if (result != null) {
				Log.i("[uniararas-mobile]", "Finalizando atividades, cadastro realizado");
				limparCampos();
				Toast.makeText(MainActivity.this, 
						getResources().getString(R.string.main_notificacao_usuario_cadastrado), 
						Toast.LENGTH_LONG).show();
			} else {
				Log.i("[uniararas-mobile]", "Finalizando atividades, cadastro não realizado");
				Toast.makeText(MainActivity.this, 
						getResources().getString(R.string.main_notificacao_usuario_falha_cadastro), 
						Toast.LENGTH_LONG).show();
			}
			dialogoProgresso.dismiss();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			if (values != null && values.length == 1) {
				Log.i("[uniararas-mobile]", "Progresso: " + values[0]);
			}	
		}

		@Override
		protected Usuario doInBackground(Usuario... params) {
			try {
				if (HttpUtils.salvarUsuario(params[0]) != null) {
					return params[0];
				}
			} catch (ModelException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	
}
