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
 * Utilitário HTTP para comunicação remota, chamando serviços
 * disponíveis no back-end.
 * 
 * @author pedrobrigatto
 */
public class HttpUtils {
	
	// Use este para quando estiver rodando a solução web no mesmo computador que a aplicação mobile
//	private static final String URL_BACKEND = "http://10.0.2.2:8080/uniararas-aula4-backend";
	private static final String URL_BACKEND = "http://uniararas-backend.pedrobrigatto.cloudbees.net";
	
	private static final String SALVA_USUARIO = "/SalvaUsuario";
	private static final String LISTA_USUARIOS = "/ListaUsuarios";

	private static HttpClient httpClient;

	/**
	 * Busca registros de um tipo específico no sistema de informações
	 * da solução. 
	 * 
	 * @param tipo Tipo de registro a ser pesquisado
	 * @param criterios Filtro para ser usado na pesquisa
	 * 
	 * @return Uma coleção de registros que atendem ao filtro da pesquisa realizada
	 */
	@SuppressWarnings("rawtypes")
	public static List buscarRegistros(Class tipo, String ... criterios) {
		httpClient = new DefaultHttpClient();
		String parametroPesquisa = montarFiltro(tipo, criterios);
		HttpGet get = null;

		try {
			
			if (tipo == Usuario.class) {
				get = new HttpGet(URL_BACKEND + LISTA_USUARIOS + parametroPesquisa);
				// Mando executar um GET via HTTP no servidor, para recuperar usuários
				HttpResponse resposta = httpClient.execute(get);
				// Guardo uma referência para o conteúdo da resposta HTTP enviada pelo servidor
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
	 * na seleção de registros no back-end.
	 * 
	 * @param tipo Tipo de registro que será pesquisado no back-end 
	 * @param criterios Dados que devem ser usados na definição do filtro da pesquisa
	 * 
	 * @return A porção da URL contendo filtros a serem usados na pesquisa
	 */
	@SuppressWarnings("rawtypes")
	private static String montarFiltro(Class tipo, String[] criterios) {
		
		if (criterios == null || criterios.length == 0) {
			return "";
		}
		
		StringBuilder filtro = new StringBuilder().append("");

		if (tipo == Usuario.class && criterios.length == 1 && !"".equals(criterios[0])) { 
			// Filtro de usuários pelo username ou pelo nome
			filtro = new StringBuilder().append("&");
			filtro.append("username=");
			filtro.append(criterios[0]);			
		}
		return filtro.toString();
	}

	/**
	 * Monta a resposta a ser enviada por quem solicitou o consumo de dados 
	 * do back-end da solução. 
	 * 
	 * @param tipo O tipo de registro sendo solicitado
	 * @param respostaJSON A resposta já enviada do servidor contendo os dados
	 * 
	 * @return Uma coleção de registros compatível com o tipo informado 
	 */
	@SuppressWarnings({ "rawtypes" })
	private static List montarResposta(Class tipo, String respostaJSON) {		
		if (tipo == Usuario.class) {
			return converterParaUsuarios(respostaJSON);
		}
		return new ArrayList();
	}

	/**
	 * Usado para converter um conjunto de usuários representada no formato JSON em 
	 * uma representação em objeto deste mesmo conjunto de dados, de acordo com a 
	 * representação do modelo de dados da solução.
	 *
	 * @param respostaJSON Resposta obtida pelo servidor quando a operação for concluída
	 * @return Uma coleção de usuários já na representação do modelo de dados da solução
	 */
	private static List<Usuario> converterParaUsuarios(String respostaJSON) {

		List<Usuario> usuarios = new ArrayList<Usuario>();

		// Leitura do JSON e conversão para objetos
		try  {
			JSONArray colecao = new JSONArray(respostaJSON);
			Usuario usuario = null;
			JSONObject objetoJsonAtual;

			// Processando usuários, um a um.
			for (int pos = 0; pos < colecao.length(); pos++) {

				objetoJsonAtual = colecao.getJSONObject(pos);

				// Criando o objeto usuário
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
	 * Recupera o conteúdo JSON contido na resposta enviada pelo servidor. 
	 * 
	 * @param stream Stream de entrada enviada pelo servidor. 
	 * @return Os dados encapsulados na resposta, formatados em JSON
	 * @throws IOException Se qualquer problema de comunicação com o backend 
	 *         ocorrer ou formatos inesperados forem encontrados no conteúdo da resposta
	 */
	private static String lerResposta(InputStream stream) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int tamanhoLido = 0;

		while (( tamanhoLido = stream.read(buffer) ) > 0) {
			// Passar o conteúdo lido para a output stream
			outputStream.write(buffer, 0, tamanhoLido);
		}

		String valorTextual = new String(outputStream.toByteArray());
		return valorTextual;
	}

	/**
	 * Usado para salvar um novo usuário no sistema de informação da solução, mantido 
	 * no lado servidor e manipulado por uma aplicação de back-end.
	 *
	 * @param usuario O usuário a ser salvo no sistema
	 * @return O mesmo objeto que foi enviado, caso a operação seja bem sucedida.
	 * @throws ModelException caso qualquer erro ocorra na tentativa de salvamento do registro
	 */
	public static Usuario salvarUsuario(Usuario usuario) throws ModelException {

		HttpClient cliente = new DefaultHttpClient();
		HttpPost post = new HttpPost(URL_BACKEND + SALVA_USUARIO);

		// Preparando a requisição post, começando pelos dados que devem ser enviados
		List<NameValuePair> parametrosForm = new ArrayList<NameValuePair>();

		// Preenchendo a lista de parâmetros
		parametrosForm.add(new BasicNameValuePair("username", usuario.getUsername()));
		parametrosForm.add(new BasicNameValuePair("nome", usuario.getNome()));
		parametrosForm.add(new BasicNameValuePair("senha", usuario.getSenha()));

		try {
			// Salvando a lista de valores no corpo da requisição
			post.setEntity(new UrlEncodedFormEntity(parametrosForm, "utf-8"));
			HttpResponse resposta = cliente.execute(post);
			StatusLine status = resposta.getStatusLine();

			if (status.getStatusCode() == HttpStatus.SC_OK) {
				return usuario;
			}
		} catch (UnsupportedEncodingException excecaoEncoding) {
			excecaoEncoding.printStackTrace();
			throw new ModelException("Problemas associados ao encoding utilizado na comunicação");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new ModelException("Problemas associados ao protocolo utilizado na comunicação");
		} catch (IOException e) {
			e.printStackTrace();
			throw new ModelException("Problemas genéricos associados a entrada e saída");
		}
		return null;
	}
}
