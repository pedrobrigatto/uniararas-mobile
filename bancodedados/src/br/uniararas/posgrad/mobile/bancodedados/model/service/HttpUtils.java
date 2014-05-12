package br.uniararas.posgrad.mobile.bancodedados.model.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.uniararas.posgrad.mobile.bancodedados.model.Usuario;
import br.uniararas.posgrad.mobile.bancodedados.model.exceptions.ModelException;

/**
 * Utilit�rio HTTP para comunica��o remota, chamando servi�os
 * dispon�veis no back-end.
 * 
 * @author pedrobrigatto
 */
public class HttpUtils {
	
	// Use este para quando estiver rodando a solu��o web no mesmo computador que a aplica��o mobile
//	private static final String URL_BACKEND = "http://10.0.2.2:8080/uniararas-aula4-backend";
	private static final String URL_BACKEND = "http://uniararas-backend.pedrobrigatto.cloudbees.net";
	
	private static final String SALVA_USUARIO = "/SalvaUsuario";
	private static final String LISTA_USUARIOS = "/ListaUsuarios";

	private static HttpClient httpClient;

	/**
	 * Busca registros de um tipo espec�fico no sistema de informa��es
	 * da solu��o. 
	 * 
	 * @param tipo Tipo de registro a ser pesquisado
	 * @param criterios Filtro para ser usado na pesquisa
	 * 
	 * @return Uma cole��o de registros que atendem ao filtro da pesquisa realizada
	 */
	@SuppressWarnings("rawtypes")
	public static List buscarRegistros(Class tipo, String ... criterios) {
		httpClient = new DefaultHttpClient();
		String parametroPesquisa = montarFiltro(tipo, criterios);
		HttpGet get = null;

		try {
			
			if (tipo == Usuario.class) {
				get = new HttpGet(URL_BACKEND + LISTA_USUARIOS + parametroPesquisa);
				// Mando executar um GET via HTTP no servidor, para recuperar usu�rios
				HttpResponse resposta = httpClient.execute(get);
				// Guardo uma refer�ncia para o conte�do da resposta HTTP enviada pelo servidor
				InputStream dadosResposta = resposta.getEntity().getContent();
				String respostaJSON = lerResposta(dadosResposta);
				return montarResposta(tipo, respostaJSON);
			}
			return new ArrayList();			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return new ArrayList();
	}

	/**
	 * Monta a pesquisa complementando a URL base com filtros a serem usados
	 * na sele��o de registros no back-end.
	 * 
	 * @param tipo Tipo de registro que ser� pesquisado no back-end 
	 * @param criterios Dados que devem ser usados na defini��o do filtro da pesquisa
	 * 
	 * @return A por��o da URL contendo filtros a serem usados na pesquisa
	 */
	@SuppressWarnings("rawtypes")
	private static String montarFiltro(Class tipo, String[] criterios) {
		
		if (criterios == null || criterios.length == 0) {
			return "";
		}
		
		StringBuilder filtro = new StringBuilder().append("");

		if (tipo == Usuario.class && criterios.length == 1 && !"".equals(criterios[0])) { 
			// Filtro de usu�rios pelo username ou pelo nome
			filtro = new StringBuilder().append("&");
			filtro.append("username=");
			filtro.append(criterios[0]);			
		}
		return filtro.toString();
	}

	/**
	 * Monta a resposta a ser enviada por quem solicitou o consumo de dados 
	 * do back-end da solu��o. 
	 * 
	 * @param tipo O tipo de registro sendo solicitado
	 * @param respostaJSON A resposta j� enviada do servidor contendo os dados
	 * 
	 * @return Uma cole��o de registros compat�vel com o tipo informado 
	 */
	@SuppressWarnings({ "rawtypes" })
	private static List montarResposta(Class tipo, String respostaJSON) {		
		if (tipo == Usuario.class) {
			return converterParaUsuarios(respostaJSON);
		}
		return new ArrayList();
	}

	/**
	 * Usado para converter um conjunto de usu�rios representada no formato JSON em 
	 * uma representa��o em objeto deste mesmo conjunto de dados, de acordo com a 
	 * representa��o do modelo de dados da solu��o.
	 *
	 * @param respostaJSON Resposta obtida pelo servidor quando a opera��o for conclu�da
	 * @return Uma cole��o de usu�rios j� na representa��o do modelo de dados da solu��o
	 */
	private static List<Usuario> converterParaUsuarios(String respostaJSON) {

		List<Usuario> usuarios = new ArrayList<Usuario>();

		// Leitura do JSON e convers�o para objetos
		try  {
			JSONArray colecao = new JSONArray(respostaJSON);
			Usuario usuario = null;
			JSONObject objetoJsonAtual;

			// Processando usu�rios, um a um.
			for (int pos = 0; pos < colecao.length(); pos++) {

				objetoJsonAtual = colecao.getJSONObject(pos);

				// Criando o objeto usu�rio
				usuario = new Usuario();
				usuario.setNome(objetoJsonAtual.getString("nome"));
				usuario.setUsername(objetoJsonAtual.getString("username"));
				usuarios.add(usuario);
			}
		} catch (JSONException excecao) {
			return new ArrayList<Usuario>();
		}
		return usuarios;
	}

	/**
	 * Recupera o conte�do JSON contido na resposta enviada pelo servidor. 
	 * 
	 * @param stream Stream de entrada enviada pelo servidor. 
	 * @return Os dados encapsulados na resposta, formatados em JSON
	 * @throws IOException Se qualquer problema de comunica��o com o backend 
	 *         ocorrer ou formatos inesperados forem encontrados no conte�do da resposta
	 */
	private static String lerResposta(InputStream stream) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int tamanhoLido = 0;

		while (( tamanhoLido = stream.read(buffer) ) > 0) {
			// Passar o conte�do lido para a output stream
			outputStream.write(buffer, 0, tamanhoLido);
		}

		String valorTextual = new String(outputStream.toByteArray());
		return valorTextual;
	}

	/**
	 * Usado para salvar um novo usu�rio no sistema de informa��o da solu��o, mantido 
	 * no lado servidor e manipulado por uma aplica��o de back-end.
	 *
	 * @param usuario O usu�rio a ser salvo no sistema
	 * @return O mesmo objeto que foi enviado, caso a opera��o seja bem sucedida.
	 * @throws ModelException caso qualquer erro ocorra na tentativa de salvamento do registro
	 */
	public static Usuario salvarUsuario(Usuario usuario) throws ModelException {

		HttpClient cliente = new DefaultHttpClient();
		HttpPost post = new HttpPost(URL_BACKEND + SALVA_USUARIO);

		// Preparando a requisi��o post, come�ando pelos dados que devem ser enviados
		List<NameValuePair> parametrosForm = new ArrayList<NameValuePair>();

		// Preenchendo a lista de par�metros
		parametrosForm.add(new BasicNameValuePair("username", usuario.getUsername()));
		parametrosForm.add(new BasicNameValuePair("nome", usuario.getNome()));
		parametrosForm.add(new BasicNameValuePair("senha", usuario.getSenha()));

		try {
			// Salvando a lista de valores no corpo da requisi��o
			post.setEntity(new UrlEncodedFormEntity(parametrosForm, "utf-8"));
			HttpResponse resposta = cliente.execute(post);
			StatusLine status = resposta.getStatusLine();

			if (status.getStatusCode() == HttpStatus.SC_OK) {
				return usuario;
			}
		} catch (UnsupportedEncodingException excecaoEncoding) {
			excecaoEncoding.printStackTrace();
			throw new ModelException("Problemas associados ao encoding utilizado na comunica��o");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new ModelException("Problemas associados ao protocolo utilizado na comunica��o");
		} catch (IOException e) {
			e.printStackTrace();
			throw new ModelException("Problemas gen�ricos associados a entrada e sa�da");
		}
		return null;
	}
}
