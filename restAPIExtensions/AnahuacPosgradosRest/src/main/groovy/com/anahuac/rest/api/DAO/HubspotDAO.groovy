
package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.ZoneId

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.json.JSONArray
import org.json.JSONObject
import org.json.simple.parser.JSONParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.text.NumberFormat

import com.anahuac.catalogos.CatConfiguracion
import com.anahuac.catalogos.CatConfiguracionDAO
import com.anahuac.catalogos.CatRegistro
import com.anahuac.catalogos.CatRegistroDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.model.PadresTutor
import com.anahuac.model.PadresTutorDAO
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.posgrados.catalog.PSGRCatEstados
import com.anahuac.posgrados.catalog.PSGRCatEstadosDAO
import com.anahuac.posgrados.catalog.PSGRCatPais
import com.anahuac.posgrados.catalog.PSGRCatPaisDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.HubSpotData
import com.anahuac.rest.api.Entity.HubspotConfig
import com.anahuac.rest.api.Entity.HubspotProperties
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class HubspotDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(HubspotDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	Map<String,String> estatusMap = new HashMap<String, String>() {{
		put("solicitud_rechazada_admin", "solicitud_archivada");
		put("solicitud_archivada_area_academica", "solicitud_archivada");
		put("solicitud_archivada_reagendacion", "solicitud_rechazada");
		put("solicitud_archivada_dictamen", "solicitud_archivada");
		put("solicitud_completada", "solicitud_completada");
		put("modificaciones_realizadas", "solicitud_completada");
		put("solicitud_reactivada", "solicitud_completada");
		put("solicitud_pase_lista_esperando_validacion", "solicitud_pase_lista_esperando_validacion");
		put("esperando_agendacion_cita", "no_asistio_entrevista");
		put("entrevista_reagendada", "entrevista_reagendada");
		put("transferencia", "transferencia");
		
	}};

	Map<String,String> mapTipoBecas = new HashMap<String, String>() {{
		put("Beca Artística", "Artística");
	}};

	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		return retorno
	}

	public List<String> getLstValueProperties(String campo, String apikeyHubspot) throws Exception {
		List<String> lstResultado = new ArrayList<String>();
		String jsonData = "";
		//		String data="8b-.-.-.b0-.-.-.a-.-.-.1ac-df-.-.-.54-40-.-.-.bf-b5-.-.-.69-40-.-.-.e8-.-.-.7f-.-.-.90-.-.-.c0-.-.-.99";
		//		String urlParaVisitar = "https://api.hubapi.com/properties/v1/contacts/properties/named/"+campo+"?hapikey="+data.replace("-.-.-.", "");
		String urlParaVisitar = "https://api.hubapi.com/properties/v1/contacts/properties/named/" + campo ;
		// + "?hapikey=" + apikeyHubspot.replace("-.-.-.", "")
		StringBuilder resultado = new StringBuilder();

		URL url = new URL(urlParaVisitar);
		HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		conexion.setRequestMethod("GET");
		conexion.setRequestProperty("content-type", "application/json");
		conexion.setRequestProperty("Accept-Encoding", "UTF-8");
		conexion.setRequestProperty("Authorization", "Bearer "+ apikeyHubspot.replace("-.-.-.", ""));
		BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
		String linea;
		while ((linea = rd.readLine()) != null) {
			resultado.append(linea);
		}
		rd.close();


		jsonData = resultado.toString()

		def jsonSlurper = new JsonSlurper();
		def object = jsonSlurper.parseText(jsonData);
		assert object instanceof Map;
		if(object.options != null) {
			assert object.options instanceof List;
		}

		for(def row: object.options) {
			assert row instanceof Map;
			if(!row.value.equals("")){
				lstResultado.add(row.value);
			}
		}

		return lstResultado;
	}

	public List<HubspotProperties> getLstHubspotProperties(String campo, String apikeyHubspot) throws Exception {
		List<HubspotProperties> lstResultado = new ArrayList<HubspotProperties>();
		HubspotProperties objHubspotProperties =  new HubspotProperties();
		String jsonData = "";

		//		String data="8b-.-.-.b0-.-.-.a-.-.-.1ac-df-.-.-.54-40-.-.-.bf-b5-.-.-.69-40-.-.-.e8-.-.-.7f-.-.-.90-.-.-.c0-.-.-.99"
		//		String urlParaVisitar = "https://api.hubapi.com/properties/v1/contacts/properties/named/"+campo+"?hapikey="+data.replace("-.-.-.", "");
		String urlParaVisitar = "https://api.hubapi.com/properties/v1/contacts/properties/named/" + campo ;
		//		+ "?hapikey=" + apikeyHubspot.replace("-.-.-.", "")
		StringBuilder resultado = new StringBuilder();

		URL url = new URL(urlParaVisitar);
		HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		conexion.setRequestMethod("GET");
		conexion.setRequestProperty("content-type", "application/json");
		conexion.setRequestProperty("Accept-Encoding", "UTF-8");
		conexion.setRequestProperty("Authorization", "Bearer "+apikeyHubspot.replace("-.-.-.", ""));
		BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
		String linea;
		while ((linea = rd.readLine()) != null) {
			resultado.append(linea);
		}
		rd.close();


		jsonData = resultado.toString()

		def jsonSlurper = new JsonSlurper();
		def object = jsonSlurper.parseText(jsonData);
		assert object instanceof Map;
		if(object.options != null) {
			assert object.options instanceof List;
		}


		for(def row: object.options) {
			assert row instanceof Map;
			if(!row.value.equals("")){

				objHubspotProperties =  new HubspotProperties();
				objHubspotProperties.setHidden(Boolean.valueOf(row.hidden == null ? "false" : row.hidden.toString()));
				objHubspotProperties.setReadOnly(Boolean.valueOf(row.readOnly == null ? "false" : row.readOnly.toString()));
				objHubspotProperties.setLabel(String.valueOf(row.label == null ? "" : row.label.toString()));
				objHubspotProperties.setDescription(String.valueOf(row.label == null ? "" : row.label.toString()));
				objHubspotProperties.setDisplayOrder(Long.valueOf(row.displayOrder == null ? "0" : row.displayOrder.toString()));
				objHubspotProperties.setValue(String.valueOf(row.value == null ? "" : row.value.toString()));

				lstResultado.add(objHubspotProperties);
			}
		}



		return lstResultado;
	}

	public Result createOrUpdateHubspot(String email, String apikeyHubspot, Map<String, String> objHubSpotData) {
		Result resultado = new Result();
		//String data="8b-.-.-.b0-.-.-.a-.-.-.1ac-df-.-.-.54-40-.-.-.bf-b5-.-.-.69-40-.-.-.e8-.-.-.7f-.-.-.90-.-.-.c0-.-.-.99"
		String targetURL = "https://api.hubapi.com/contacts/v1/contact/createOrUpdate/email/[EMAIL]/"
		String jsonInputString = "{\"properties\":[{\"property\":\"firstname\",\"value\":\"java\"},{\"property\":\"nombre\",\"value\":\"Arturo\"},{\"property\":\"lastname\",\"value\":\"Zamorano\"},{\"property\":\"nombre_completo\",\"value\":\"Arturo Zamorano\"},{\"property\":\"correo_electrnico\",\"value\":\"jasz189@hotmail.com\"},{\"property\":\"date_of_birth\",\"value\":\"2020-11-30T23:51:03.309Z\"},{\"property\":\"fecha_de_nacimiento\",\"value\":\"654307200000\"},{\"property\":\"twitterhandle\",\"value\":\"arturoZCZ\"},{\"property\":\"gender\",\"value\":\"Masculino\"},{\"property\":\"country\",\"value\":\"México\"},{\"property\":\"state\",\"value\":\"Sonora\"},{\"property\":\"ciudad\",\"value\":\"Navojoa\"},{\"property\":\"city\",\"value\":\"Navojoa\"},{\"property\":\"address\",\"value\":\"Callejon 3\"},{\"property\":\"celular\",\"value\":\"6421344161\"},{\"property\":\"phone\",\"value\":\"6421344161\"},{\"property\":\"zip\",\"value\":\"85890\"},{\"property\":\"promedio\",\"value\":\"9.5\"},{\"property\":\"promedio_de_preparatoria\",\"value\":\"9.5\"},{\"property\":\"relationship_status\",\"value\":\"Casado\"},{\"property\":\"nombre_de_tutor\",\"value\":\"Arturo\"},{\"property\":\"apellido_tutor\",\"value\":\"Zamorano\"},{\"property\":\"celular_de_tutor\",\"value\":\"6421344161\"},{\"property\":\"correo_tutor\",\"value\":\"arturo.zamorano@gmail.com\"},{\"property\":\"telefono_tutor\",\"value\":\"6421344161\"},{\"property\":\"nombre_del_padre\",\"value\":\"Arturo\"},{\"property\":\"apellido_paterno\",\"value\":\"Zamorano\"},{\"property\":\"celular_del_padre\",\"value\":\"6421344161\"},{\"property\":\"correo_del_padre\",\"value\":\"arturo.zamorano@gmail.com\"},{\"property\":\"telefono_del_padre\",\"value\":\"6421344161\"},{\"property\":\"nombre_de_la_madre\",\"value\":\"Guadalupe\"},{\"property\":\"apellido_materno\",\"value\":\"Sainz\"},{\"property\":\"celular_de_la_madre\",\"value\":\"6421344161\"},{\"property\":\"correo_de_la_madre\",\"value\":\"eva.sainz@gmail.com\"},{\"property\":\"telefono_de_la_madre\",\"value\":\"6421344161\"},{\"property\":\"ua_vpd\",\"value\":\"UAM\"},{\"property\":\"campus_destino\",\"value\":\"AMAY\"},{\"property\":\"tipo_de_alumno_bpm\",\"value\":\"N\"}]}";
		String strError = "";

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		JSONObject jsonItem = new JSONObject();
		JSONObject jsonProperties = new JSONObject();
		JSONArray jsonList = new JSONArray();

		try {
			strError = strError + ", INICIO";
			strError = strError + "| ==============================================";
			//strError = strError + "| apikeyHubspot: "+apikeyHubspot;
			strError = strError + "| ==============================================";
			Iterator it = objHubSpotData.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				jsonItem = new JSONObject();
				jsonItem.put("property", pair.getKey());
				jsonItem.put("value", pair.getValue() == null ? "" : pair.getValue());
				jsonList.put(jsonItem);
				it.remove();
			}
			jsonProperties.put("properties", jsonList);
			strError = strError + "| "+jsonProperties.toString();

			strError = strError + "| EMAIL: "+email;
			targetURL = targetURL.replace("[EMAIL]", email);

			HttpPost request = new HttpPost(targetURL);
			StringEntity params = new StringEntity(jsonProperties.toString(), "UTF-8");
			request.setHeader("content-type", "application/json");
			request.setHeader("Accept-Encoding", "UTF-8");
			request.setHeader("Authorization", "Bearer "+apikeyHubspot);
			request.setEntity(params);

			CloseableHttpResponse response = httpClient.execute(request);
			strError = strError + " | "+ response.getEntity().getContentType().getName();
			strError = strError + " | "+ response.getEntity().getContentType().getValue();
			strError = strError + " | "+ EntityUtils.toString(response.getEntity(), "UTF-8");

			strError += "| statusCode:" + response.getStatusLine().getStatusCode()

			if(response.getStatusLine().getStatusCode()!=200) {
				throw new Exception(EntityUtils.toString(response.getEntity(), "UTF-8"))
			}
			resultado.setError_info(strError);
			resultado.setSuccess(true);
			new LogDAO().insertTransactionLog("POST", "CORRECTO", targetURL, "Log:"+strError, jsonList.toString())
		} catch (Exception e) {
			String mError = "Problema detectado en el usuario: ${email} \r\nERROR: "+e.getMessage()+"\r\n"+"Log: "+strError;
			resultado.setError_info(mError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
			new LogDAO().insertTransactionLog("POST", "FALLIDO", targetURL, "Log:"+strError, mError);


		}
		return resultado
		//"<br>" +
	}

	public Result getApikeyHubspot(String claveCampus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String strError = "";

		List<String> lstResultado = new ArrayList<String>();

		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_HUBSPOT_APIKEY);
			pstm.setString(1, claveCampus);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				lstResultado.add(rs.getString("valor"));
			}
			
			resultado.setData(lstResultado);
			resultado.setError_info(strError);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setError_info(strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}

	public Result getApiCrispChat() {
		Result resultado = new Result();
		Boolean closeCon = false;

		String errorlog="";
		String apikey = "";
		List<String> lstResultado = new ArrayList<String>();

		try {
			closeCon = validarConexion();

			errorlog+="| consulta: "+Statements.GET_APIKEY_CRISP;
			pstm = con.prepareStatement(Statements.GET_APIKEY_CRISP);
			rs = pstm.executeQuery();
			while(rs.next()) {
				apikey = rs.getString("crispchat");
			}
			lstResultado.add(apikey);

			resultado.setSuccess(true);
			resultado.setData(lstResultado);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());

		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}

	public Result enviarCorreoError(String titulo, String msjNF,String Error,String campus,RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;

		String errorlog="";
		String apikey = "";
		List<String> lstResultado = new ArrayList<String>();

		try {
			closeCon = validarConexion();

			String correo = "";
			pstm = con.prepareStatement(Statements.GET_CORREO_BY_CLAVE)
			pstm.setString(1, "EmailRegistro")
			rs = pstm.executeQuery()
			while(rs.next()) {
				correo = rs.getString("valor");
			}

			MailGunDAO mgd = new MailGunDAO();
			resultado = mgd.sendEmailPlantilla(correo, titulo, msjNF + "<br><br>" + Error, "",campus, context)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}

	public List<String> getLstValueProperties2(String campo, String apikeyHubspot,String campus,RestAPIContext context) throws Exception {
		List<String> lstResultado = new ArrayList<String>();
		String jsonData = "";
		//		String data="8b-.-.-.b0-.-.-.a-.-.-.1ac-df-.-.-.54-40-.-.-.bf-b5-.-.-.69-40-.-.-.e8-.-.-.7f-.-.-.90-.-.-.c0-.-.-.99";
		//		String urlParaVisitar = "https://api.hubapi.com/properties/v1/contacts/properties/named/"+campo+"?hapikey="+data.replace("-.-.-.", "");
		String urlParaVisitar = "https://api.hubapi.com/properties/v1/contacts/properties/named/" + campo ;
		StringBuilder resultado = new StringBuilder();
		// + "?hapikey=" + apikeyHubspot.replace("-.-.-.", "")
		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestMethod("GET");
			conexion.setRequestProperty("content-type", "application/json");
			conexion.setRequestProperty("Accept-Encoding", "UTF-8");
			conexion.setRequestProperty("Authorization", "Bearer "+apikeyHubspot.replace("-.-.-.", ""));
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
		}catch(IOException e) {
			new LogDAO().insertTransactionLog("GET", "FALLIDO", urlParaVisitar, "Log: ${e.getMessage()}", e.getMessage() )
			String error = "";
			error += (e.getMessage().contains("401")?"http 401 unauthorized favor de revisar las credenciales":"");
			enviarCorreoError("Error en la consulta", "${error}", e.getMessage().replace(urlParaVisitar,""), campus, context)
			throw new Exception( e.getMessage() )
		}


		jsonData = resultado.toString()

		def jsonSlurper = new JsonSlurper();
		def object = jsonSlurper.parseText(jsonData);
		assert object instanceof Map;
		if(object.options != null) {
			assert object.options instanceof List;
		}

		for(def row: object.options) {
			assert row instanceof Map;
			if(!row.value.equals("")){
				lstResultado.add(row.value);
			}
		}

		return lstResultado;
	}

	public List<HubspotProperties> getLstHubspotProperties2(String campo, String apikeyHubspot,String campus,RestAPIContext context) throws Exception {
		List<HubspotProperties> lstResultado = new ArrayList<HubspotProperties>();
		HubspotProperties objHubspotProperties =  new HubspotProperties();
		String jsonData = "";
		String urlParaVisitar = "https://api.hubapi.com/properties/v1/contacts/properties/named/" + campo ;
		StringBuilder resultado = new StringBuilder();

		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestMethod("GET");
			conexion.setRequestProperty("content-type", "application/json");
			conexion.setRequestProperty("Accept-Encoding", "UTF-8");
			conexion.setRequestProperty("Authorization", "Bearer "+apikeyHubspot.replace("-.-.-.", ""));
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
		}catch(IOException e) {
			new LogDAO().insertTransactionLog("GET", "FALLIDO", urlParaVisitar, "Log: ${e.getMessage()}", e.getMessage() );
			String error = "";
			error += (e.getMessage().contains("401")?"http 401 unauthorized favor de revisar las credenciales":"");
			enviarCorreoError("Error en la cosulta", " ${error}", e.getMessage().replace(urlParaVisitar,""), campus, context);
			throw new Exception( e.getMessage() )
		}


		jsonData = resultado.toString()

		def jsonSlurper = new JsonSlurper();
		def object = jsonSlurper.parseText(jsonData);
		assert object instanceof Map;
		if(object.options != null) {
			assert object.options instanceof List;
		}

		for(def row: object.options) {
			assert row instanceof Map;
			if(!row.value.equals("")){

				objHubspotProperties =  new HubspotProperties();
				objHubspotProperties.setHidden(Boolean.valueOf(row.hidden == null ? "false" : row.hidden.toString()));
				objHubspotProperties.setReadOnly(Boolean.valueOf(row.readOnly == null ? "false" : row.readOnly.toString()));
				objHubspotProperties.setLabel(String.valueOf(row.label == null ? "" : row.label.toString()));
				objHubspotProperties.setDescription(String.valueOf(row.label == null ? "" : row.label.toString()));
				objHubspotProperties.setDisplayOrder(Long.valueOf(row.displayOrder == null ? "0" : row.displayOrder.toString()));
				objHubspotProperties.setValue(String.valueOf(row.value == null ? "" : row.value.toString()));

				lstResultado.add(objHubspotProperties);
			}
		}

		return lstResultado;
	}

	public Result replicarProperties(String nombreUsuario, String correoElectronico, String apikeyHubspot) {
		Result resultado = new Result();
		List<String> lstNoHubspot = new ArrayList<String>();

		String jsonData = "";
		String urlParaVisitar = "https://api.hubapi.com/contacts/v1/contact/email/[EMAIL]/profile";
		StringBuilder strResultado = new StringBuilder();
		String linea = "";
		String errorlog = "";

		def jsonSlurper = new JsonSlurper();
		def propiedades = null;

		JSONParser parser = new JSONParser();

		org.json.simple.JSONObject objContacto = null;
		org.json.simple.JSONObject objPropiedades = null;
		org.json.simple.JSONObject objColumna = null;

		Map<String, String> objHubSpotData = new HashMap<String, String>();

		try {
			errorlog = errorlog + " | --------------------------------------------------------------------------";
			lstNoHubspot.add("hs_analytics_revenue");
			lstNoHubspot.add("createdate");
			lstNoHubspot.add("hs_analytics_num_page_views");
			lstNoHubspot.add("hs_lifecyclestage_subscriber_date");
			lstNoHubspot.add("hs_analytics_average_page_views");
			lstNoHubspot.add("hs_analytics_num_event_completions");
			lstNoHubspot.add("hs_is_unworked");
			lstNoHubspot.add("hs_social_num_broadcast_clicks");
			lstNoHubspot.add("hs_analytics_num_visits");
			lstNoHubspot.add("hs_social_linkedin_clicks");
			lstNoHubspot.add("hs_marketable_until_renewal");
			lstNoHubspot.add("hs_marketable_status");
			lstNoHubspot.add("lastmodifieddate");
			lstNoHubspot.add("hs_analytics_first_timestamp");
			lstNoHubspot.add("hs_social_google_plus_clicks");
			lstNoHubspot.add("hs_social_facebook_clicks");
			lstNoHubspot.add("hs_social_twitter_clicks");
			lstNoHubspot.add("hs_analytics_source_data_1");
			lstNoHubspot.add("num_unique_conversion_events");
			lstNoHubspot.add("hs_all_contact_vids");
			lstNoHubspot.add("hs_is_contact");
			lstNoHubspot.add("num_conversion_events");
			lstNoHubspot.add("hs_object_id");
			lstNoHubspot.add("hs_searchable_calculated_phone_number");
			lstNoHubspot.add("hs_email_domain");

			errorlog = errorlog + " | list load";

			//READ JSON
			URL url = new URL(urlParaVisitar.replace("[EMAIL]", nombreUsuario));
			//.replace("[APIKEY]", apikeyHubspot)
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestMethod("GET");
			conexion.setRequestProperty("content-type", "application/json");
			conexion.setRequestProperty("Accept-Encoding", "UTF-8");
			conexion.setRequestProperty("Authorization", "Bearer "+apikeyHubspot);
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));

			while ((linea = rd.readLine()) != null) {
				strResultado.append(linea);
			}
			rd.close();
			jsonData = strResultado.toString()

			//PARSE JSON
			objContacto    = (org.json.simple.JSONObject) parser.parse(jsonData);
			objPropiedades = (org.json.simple.JSONObject) objContacto.get("properties");

			errorlog = errorlog + " | --------------------------------------------------------------------------";

			objHubSpotData = new HashMap<String, String>();
			for(Object key: objPropiedades.keySet()){
				objColumna = (org.json.simple.JSONObject) objPropiedades.get(key.toString());

				if(!lstNoHubspot.contains(key)) {
					errorlog = errorlog + " | key: " + key.toString()+" ----------- Valor: "+objColumna.get("value");
					objHubSpotData.put(key.toString(), objColumna.get("value"));
				}
			}

			errorlog = errorlog + " | --------------------------------------------------------------------------"
			resultado = createOrUpdateHubspot(correoElectronico, apikeyHubspot, objHubSpotData);
			resultado.setError_info(errorlog +" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
		}
		catch(Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());

		}

		return resultado;
	}


	public Result insertUpdateEmail(HubspotConfig config) {
		Result result = new Result();
		Boolean closeCon=false;
		try {
			closeCon = validarConexion();

			verifyAndInsertOrUpdate("EmailAutodescripcion", config.emailHubspotAutodescripcion, "Correo para envío de fallos Hubspot Autodescripción")
			verifyAndInsertOrUpdate("EmailEnviada", config.emailHubspotEnviada,"")
			verifyAndInsertOrUpdate("EmailEsperaResultado", config.emailHubspotEsperaResultado,"")
			verifyAndInsertOrUpdate("EmailGenerarCredencial", config.emailHubspotGenerarCredencial,"")
			verifyAndInsertOrUpdate("EmailModificar", config.emailHubspotModificar,"")
			verifyAndInsertOrUpdate("EmailNoAsistioPruebas", config.emailHubspotNoAsistioPruebas,"")
			verifyAndInsertOrUpdate("EmailPago", config.emailHubspotPago,"")
			verifyAndInsertOrUpdate("EmailRechazoLRoja", config.emailHubspotRechazoLRoja,"")
			verifyAndInsertOrUpdate("EmailRegistro", config.emailHubspotRegistro,"")
			verifyAndInsertOrUpdate("EmailRestaurarRechazoLRoja", config.emailHubspotRestaurarRechazoLRoja,"")
			verifyAndInsertOrUpdate("EmailSeleccionoFechaExamen", config.emailHubspotSeleccionoFechaExamen,"")
			verifyAndInsertOrUpdate("EmailUsuarioRegistrado", config.emailHubspotUsuarioRegistrado,"")
			verifyAndInsertOrUpdate("EmailValidar", config.emailHubspotValidar,"")
			verifyAndInsertOrUpdate("EmailTransferirAspirante", config.emailHubspotTransferirAspirante,"")
			result.setSuccess(true)
		}catch(Exception e) {
			result.setSuccess(false)
			result.setError("Can't set config Hubspot email")
			result.setError_info(e.getMessage())
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}

		return result;
	}

	public Result getEmailHubspotConfig() {
		Result result = new Result();
		List < HubspotConfig > data = new ArrayList < HubspotConfig > ();
		HubspotConfig row = new HubspotConfig();
		Boolean closeCon = false;
		try {
			closeCon = validarConexion();

			pstm = con.prepareStatement(HubspotConfig.CONFIGURACIONES)
			rs = pstm.executeQuery();

			while (rs.next()) {
				switch (rs.getString("clave")) {
					case "EmailAutodescripcion":
						row.setEmailHubspotAutodescripcion(rs.getString("valor"))
						break;
					case "EmailEnviada":
						row.setEmailHubspotEnviada(rs.getString("valor"))
						break;
					case "EmailEsperaResultado":
						row.setEmailHubspotEsperaResultado(rs.getString("valor"))
						break;
					case "EmailGenerarCredencial":
						row.setEmailHubspotGenerarCredencial(rs.getString("valor"))
						break;
					case "EmailModificar":
						row.setEmailHubspotModificar(rs.getString("valor"))
						break;
					case "EmailNoAsistioPruebas":
						row.setEmailHubspotNoAsistioPruebas(rs.getString("valor"))
						break;
					case "EmailModificar":
						row.setEmailHubspotModificar(rs.getString("valor"))
						break;
					case "EmailPago":
						row.setEmailHubspotPago(rs.getString("valor"))
						break;
					case "EmailRegistro":
						row.setEmailHubspotRegistro(rs.getString("valor"))
						break;
					case "EmailRestaurarRechazoLRoja":
						row.setEmailHubspotRestaurarRechazoLRoja(rs.getString("valor"))
						break;
					case "EmailSeleccionoFechaExamen":
						row.setEmailHubspotSeleccionoFechaExamen(rs.getString("valor"))
						break;
					case "EmailUsuarioRegistrado":
						row.setEmailHubspotUsuarioRegistrado(rs.getString("valor"))
						break;
					case "EmailValidar":
						row.setEmailHubspotValidar(rs.getString("valor"))
						break;
					case "EmailTransferirAspirante":
						row.setEmailHubspotTransferirAspirante(rs.getString("valor"))
						break;
					case "EmailRechazoLRoja":
						row.setEmailHubspotRechazoLRoja(rs.getString("valor"))
						break;

				}
			}
			data.add(row)
			result.setSuccess(true);
			result.setData(data)
		} catch (Exception exception) {
			result.setSuccess(false)
			result.setError(exception.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return result;
	}
	private void verifyAndInsertOrUpdate(String key, String value, String description) {
		Boolean closeCon=false;

		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(HubspotConfig.GET_CONFIGURACIONES_CLAVE)
			pstm.setString(1, key)
			rs = pstm.executeQuery()
			if (rs.next()) {
				pstm = con.prepareStatement(HubspotConfig.UPDATE_CONFIGURACIONES)
				pstm.setString(1, value)
				pstm.setString(2, key)
				pstm.executeUpdate()
			} else {
				pstm = con.prepareStatement(HubspotConfig.INSERT_CONFIGURACIONES)
				pstm.setString(1, key)
				pstm.setString(2, value)
				pstm.setString(3, description)
				pstm.executeUpdate()
			}


		}catch(Exception e) {
			throw new Exception (e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}

	}

	private Map<String, Object> getSolicitudByCaseid(Long caseid){
		Boolean closeCon=false;
		Map<String, Object> solicitud = new HashMap<String, Object>();
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_SOLICITUD_BY_CASEID);
			pstm.setLong(1, caseid);

			rs = pstm.executeQuery();

			if(rs.next()) {
				solicitud.put("correo_electronico", rs.getString("correo_electronico"));
				solicitud.put("is_transferido", rs.getBoolean("is_transferido"));
				solicitud.put("id_banner_validacion", rs.getString("id_banner_validacion"));
				solicitud.put("mensaje_comite_admision", rs.getString("mensaje_comite_admision"));
				solicitud.put("fecha_envio_solicitud", rs.getString("fecha_envio_solicitud"));
				solicitud.put("estatus_solicitud", rs.getString("estatus_solicitud"));
				solicitud.put("mensaje_admin_escolar", rs.getString("mensaje_admin_escolar"));
				solicitud.put("aprobado_area_academic", rs.getBoolean("aprobado_area_academic"));
				solicitud.put("aprobado_comite_admision", rs.getBoolean("aprobado_comite_admision"));
				solicitud.put("campus", rs.getString("campus"));
				solicitud.put("clave_campus", rs.getString("clave_campus"));
				solicitud.put("grupo_bonita", rs.getString("grupo_bonita"));
				solicitud.put("carrera", rs.getString("carrera"));
				solicitud.put("clave_carrera", rs.getString("clave_carrera"));
				solicitud.put("apellido_paterno", rs.getString("apellido_paterno"));
				solicitud.put("apellido_materno", rs.getString("apellido_materno"));
				solicitud.put("nombre", rs.getString("nombre"));
				solicitud.put("curp", rs.getString("curp"));
				solicitud.put("pasaporte", rs.getString("pasaporte"));
				solicitud.put("lugar_nacimiento_ciudad", rs.getString("lugar_nacimiento_ciudad"));
				solicitud.put("lugar_nacimiento_pais", rs.getString("lugar_nacimiento_pais"));
				solicitud.put("lugar_nacimiento_estado", rs.getString("lugar_nacimiento_estado"));
				solicitud.put("fecha_nacimiento", rs.getString("fecha_nacimiento"));
				solicitud.put("nacionalidad", rs.getString("nacionalidad"));
				solicitud.put("estado_civil", rs.getString("estado_civil"));
				solicitud.put("estudiara_programa_otra_un", rs.getString("estudiara_programa_otra_un"));
				solicitud.put("clave_periodo", rs.getString("clave_periodo"));
				solicitud.put("clave_posgrado", rs.getString("clave_posgrado"));
			}
		} catch(Exception e) {
			throw new Exception (e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}

		return solicitud;
	}
	
	
	private Map<String, Object> getEscuelasByCaseid(Long caseid, Map<String, Object> objHubSpotData){
		Boolean closeCon = false;
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_ESCUELAS_SOLICITUD);
			pstm.setLong(1, caseid);

			rs = pstm.executeQuery();

			while(rs.next()) {
				if(rs.getString("grado_clave").equals("DOC")) {
					objHubSpotData.put("grado_escolar_doctor_posgrado_bpm", rs.getString("grado"));
					objHubSpotData.put("programa_grado_doctor_posgrado_bpm", rs.getString("programa"));
					objHubSpotData.put("inst_doc_posgrado_bpm", rs.getString("institucion"));
				} else if (rs.getString("grado_clave").equals("MAT")) {
					objHubSpotData.put("grado_escolar_maestria_posgrado_bpm", rs.getString("grado"));
					objHubSpotData.put("programa_grado_maestria_posgrado_bpm", rs.getString("programa"));
					objHubSpotData.put("inst_maestria_posgrado_bpm", rs.getString("institucion"));
				} else if (rs.getString("grado_clave").equals("ESP")) {
					objHubSpotData.put("grado_escolar_espec_posgrado_bpm", rs.getString("grado"));
					objHubSpotData.put("programa_grado_espec_posgrado_bpm", rs.getString("programa"));
					objHubSpotData.put("inst_espec_posgrado_bpm", rs.getString("institucion"));
				} else if (rs.getString("grado_clave").equals("LIC")) {
					objHubSpotData.put("grado_escolar_lic_posgrado_bpm", rs.getString("grado"));
					objHubSpotData.put("programa_grado_lic_posgrado_bpm", rs.getString("programa"));
					objHubSpotData.put("inst_lic_posgrado_bpm", rs.getString("institucion"));
				}
			}
		} catch(Exception e) {
			throw new Exception (e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}

		return objHubSpotData;
	}
	
	private Map<String, Object> getTrabajosByCaseid(Long caseid, Map<String, Object> objHubSpotData){
		Boolean closeCon = false;
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_TRABAJOS_SOLICITUD);
			pstm.setLong(1, caseid);

			rs = pstm.executeQuery();

			if(rs.next()) {
				objHubSpotData.put("nombre_empresa_empleado_posgrado_bpm", rs.getString("nombre_empresa"));
				objHubSpotData.put("puesto_empleado_posgrado_bpm", rs.getString("puesto_trabajo"));
				objHubSpotData.put("telefono_empleado_posgrado_bpm", rs.getString("telefono_empresa"));
				objHubSpotData.put("fecha_inicio_empleo_posgrado_bpm", rs.getString("fecha_inicio"));
			}
		} catch(Exception e) {
			throw new Exception (e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}

		return objHubSpotData;
	}
	
	private Map<String, Object> getMediosByCaseid(Long caseid, Map<String, Object> objHubSpotData){
		Boolean closeCon = false;
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_MEDIOS_ENTERASTE_SOLICITUD);
			pstm.setLong(1, caseid);

			rs = pstm.executeQuery();

			if(rs.next()) {
				objHubSpotData.put("medio_enteraste_posgrado_bpm", rs.getString("clave_medio"));	
				objHubSpotData.put("comentarios_medios_posgrado_bpm", rs.getString("especifique"));	
			}
		} catch(Exception e) {
			throw new Exception (e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}

		return objHubSpotData;
	}
	
	private Map<String, Object> getHorarioByCaseid(Long caseid, Map<String, Object> objHubSpotData, org.bonitasoft.web.extension.rest.RestAPIContext context){
		Boolean closeCon = false;
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_HORARIO_SOLICITUD);
			pstm.setLong(1, caseid);

			rs = pstm.executeQuery();

			if(rs.next()) {
				if(rs.getString("hora_inicio") != null) {
					DateFormat dfEntrevista = new SimpleDateFormat("yyyy-MM-dd");
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(dfEntrevista.parse(rs.getString("fecha_entrevista")));
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					TimeZone timeZone = TimeZone.getTimeZone("UTC");
					calendar.setTimeZone(timeZone);
					
					objHubSpotData.put("fecha_entrevista_posgrado_bpm", calendar.getTime().getTime());
					objHubSpotData.put("horario_entrevista_posgrado_bpm", rs.getString("hora_inicio") + " - " + rs.getString("hora_fin"));
					objHubSpotData.put("responsable_entrevista_posgrado_bpm", context.apiClient.identityAPI.getUser(rs.getLong("responsable_id")).getUserName());
				}
			} 
		} catch(Exception e) {
			throw new Exception ("No se ha podido cargar la cita de entrevista ");
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}

		return objHubSpotData;
	}
	
	private Boolean getIsReagendadoByCaseid(Long caseid){
		Boolean closeCon = false;
		Boolean isReagendado = false;
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_REAGENDADO);
			pstm.setLong(1, caseid);

			rs = pstm.executeQuery();

			if(rs.next()) {
				isReagendado = rs.getBoolean("reagendado");
			} 
		} catch(Exception e) {
			throw new Exception ("No se ha podido cargar la cita de entrevista ");
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}

		return isReagendado;
	}
	
	private Map<String, Object> getTransferenciaByCaseid(Long caseid, Map<String, Object> objHubSpotData){
		Boolean closeCon = false;
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_INFO_TRANSFERENCIA);
			pstm.setLong(1, caseid);

			rs = pstm.executeQuery();

			if(rs.next()) {
				objHubSpotData.put("campus_origen_posgrado_bpm", rs.getString("clave_campus_origen"));	
				objHubSpotData.put("campus_destino_posgrado_bpm", rs.getString("clave_campus_destino"));
			}
		} catch(Exception e) {
			throw new Exception (e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}

		return objHubSpotData;
	}

	public Result createOrUpdatePosgrado(Long caseid, org.bonitasoft.web.extension.rest.RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		Boolean closeCon = false;
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		Boolean noSol = false;
		String msjNF = "";
		String errorLog = "";
		String apikeyHubspot ="";
		Date fecha = new Date();
		DateFormat dfDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		DateFormat dfNoTime = new SimpleDateFormat("dd/MM/yyyy");
		Map<String, Object> solicitud;
		
		try {
			LOGGER.error "[createOrUpdatePosgrado] 1";
			solicitud = getSolicitudByCaseid(caseid);
			LOGGER.error "[createOrUpdatePosgrado] 2";
			resultadoApiKey = getApikeyHubspot(solicitud.get("grupo_bonita"));
			apikeyHubspot = (String) resultadoApiKey.getData().get(0);
			Date ultimaMod = new Date();
			objHubSpotData.put("fecha_actualizacion_posgrado_bpm", df.format(ultimaMod));
			LOGGER.error "[createOrUpdatePosgrado] 3";
			String estatusNuevo = estatusMap.get(solicitud.get("estatus_solicitud"));
			LOGGER.error "[createOrUpdatePosgrado] 4";
			if(!solicitud.get("estatus_solicitud").equals("aspirante_registrado")) {
				//Si la solicitud ya avanzo de este estatus quiere decir que ya existe esta información
				objHubSpotData = getEscuelasByCaseid(caseid, objHubSpotData);
				objHubSpotData = getTrabajosByCaseid(caseid, objHubSpotData);
				objHubSpotData = getMediosByCaseid(caseid, objHubSpotData);
				objHubSpotData = getHorarioByCaseid(caseid, objHubSpotData, context);
			}
			
			LOGGER.error "[createOrUpdatePosgrado] 5 :: " + solicitud.get("estatus_solicitud");
			if(solicitud.get("estatus_solicitud").equals("aspirante_registrado")) {
				LOGGER.error "[createOrUpdatePosgrado] 5.1";
				ultimaMod = new Date();
				objHubSpotData.put("fecha_actualizacion_posgrado_bpm", df.format(ultimaMod));
				objHubSpotData.put("email", solicitud.get("correo_electronico"));
				objHubSpotData.put("estatus_posgrado_admision_bpm", solicitud.get("estatus_solicitud"));
			} else if(solicitud.get("estatus_solicitud").equals("solicitud_completada") || solicitud.get("estatus_solicitud").equals("modificaciones_realizadas") ) {
				LOGGER.error "[createOrUpdatePosgrado] 5.2";
				ultimaMod = new Date();
				objHubSpotData.put("fecha_actualizacion_posgrado_bpm", df.format(ultimaMod));
				objHubSpotData.put("ffecha_nacimiento_posgrado_bpm", dfNoTime.format(dfDate.parse(solicitud.get("fecha_nacimiento"))));
				objHubSpotData.put("nacionalidad_posgrado_bpm", solicitud.get("nacionalidad"));
				objHubSpotData.put("estado_civil_posgrado_bpm", solicitud.get("estado_civil"));
				objHubSpotData.put("ciudad_posgrado_bpm", solicitud.get("lugar_nacimiento_ciudad"));
				String estudia_programa_opcion = solicitud.get("estudiara_programa_otra_un");
				objHubSpotData.put("estudiar_programa_como_opcion_otra_universidad_bpm", estudia_programa_opcion.equals("No") ? "No" : "Si");
				objHubSpotData.put("programa_posgrado_bpm", solicitud.get("clave_carrera"));
				objHubSpotData.put("periodo_ingreso_posgrado_bpm", solicitud.get("clave_periodo"));
				objHubSpotData.put("firstname", solicitud.get("nombre"));
				objHubSpotData.put("lastname", solicitud.get("apellido_paterno") + " " + solicitud.get("apellido_materno"));
				objHubSpotData.put("campus_posgrado_bpm", solicitud.get("clave_campus"));
				objHubSpotData.put("grado_estudiar_posgrado_bpm", solicitud.get("clave_posgrado"));
				
				PSGRCatPaisDAO paisDAO = context.apiClient.getDAO(PSGRCatPaisDAO.class);
				List<PSGRCatPais> paisResult = paisDAO.findByDescripcion(solicitud.get("lugar_nacimiento_pais"), 0, 1);
				
				if(!paisResult.isEmpty()) {
					objHubSpotData.put("pais_posgrado_bpm", paisResult.get(0).getDescripcion());
				}
				
				PSGRCatEstadosDAO estadoDAO = context.apiClient.getDAO(PSGRCatEstadosDAO.class);
				List<PSGRCatEstados> estadoResult = estadoDAO.findByDescripcion(solicitud.get("lugar_nacimiento_estado"), 0, 1);
				
				if(!estadoResult.isEmpty()) {
					objHubSpotData.put("estado_posgrado_bpm", estadoResult.get(0).getClave());
				}
				
//				Boolean reagendado = getIsReagendadoByCaseid(caseid);
//				if(reagendado) {
//					objHubSpotData.put("estatus_posgrado_admision_bpm", estatusMap.get("entrevista_reagendada"));
//				} else {
//					
//				}
				
				objHubSpotData.put("estatus_posgrado_admision_bpm", estatusMap.get(solicitud.get("estatus_solicitud")));
			} else if(solicitud.get("estatus_solicitud").equals("solicitud_aprobada_admin") || solicitud.get("estatus_solicitud").equals("modificaciones_solicitadas")) {
				LOGGER.error "[createOrUpdatePosgrado] 5.3";
				ultimaMod = new Date();
				objHubSpotData.put("fecha_actualizacion_posgrado_bpm", df.format(ultimaMod));
				LOGGER.error "[createOrUpdatePosgrado] 5.3.1";
				if(solicitud.get("id_banner_validacion") != null) {
					LOGGER.error "[createOrUpdatePosgrado] 5.3.2";
					objHubSpotData.put("id_banner_posgrado_bpm", solicitud.get("id_banner_validacion"));
				}
				LOGGER.error "[createOrUpdatePosgrado] 5.3.3";
				objHubSpotData.put("mensaje_posgrado_bpm", solicitud.get("mensaje_admin_escolar"));
				LOGGER.error "[createOrUpdatePosgrado] 5.3.4";
				objHubSpotData.put("estatus_posgrado_admision_bpm", solicitud.get("estatus_solicitud"));
				LOGGER.error "[createOrUpdatePosgrado] 5.3.5";
			} else if (solicitud.get("estatus_solicitud").equals("solicitud_rechazada_admin")) {
				LOGGER.error "[createOrUpdatePosgrado] 5.4";
				errorLog += "| " + solicitud.get("estatus_solicitud") ;
				ultimaMod = new Date();
				objHubSpotData.put("fecha_actualizacion_posgrado_bpm", df.format(ultimaMod));
				errorLog += "| " + df.format(ultimaMod);
				objHubSpotData.put("mensaje_posgrado_bpm", solicitud.get("mensaje_admin_escolar"));
				errorLog += "| " + solicitud.get("mensaje_admin_escolar");
				objHubSpotData.put("estatus_posgrado_admision_bpm", "solicitud_archivada");
				errorLog += "| " + estatusMap.get(solicitud.get("estatus_solicitud"));
			} else if(solicitud.get("estatus_solicitud").equals("solicitud_pase_lista_esperando_validacion")) {
				LOGGER.error "[createOrUpdatePosgrado] 5.5";
				ultimaMod = new Date();
				objHubSpotData.put("fecha_actualizacion_posgrado_bpm", df.format(ultimaMod));
				objHubSpotData.put("estatus_posgrado_admision_bpm", "solicitud_pase_lista_esperando_validacion");
			} else if(solicitud.get("estatus_solicitud").equals("esperando_agendacion_cita")) {
				LOGGER.error "[createOrUpdatePosgrado] 5.6";
				ultimaMod = new Date();
				objHubSpotData.put("fecha_actualizacion_posgrado_bpm", df.format(ultimaMod));
				objHubSpotData.put("estatus_posgrado_admision_bpm", estatusMap.get(solicitud.get("estatus_solicitud")));
			} else if(solicitud.get("estatus_solicitud").equals("esperando_agendacion_cita")) {
				LOGGER.error "[createOrUpdatePosgrado] 5.7";
				ultimaMod = new Date();
				objHubSpotData.put("fecha_actualizacion_posgrado_bpm", df.format(ultimaMod));
				objHubSpotData.put("estatus_posgrado_admision_bpm", estatusMap.get(solicitud.get("estatus_solicitud")));
			} else if(solicitud.get("estatus_solicitud").equals("solicitud_admitida")) {
				ultimaMod = new Date();
				objHubSpotData.put("fecha_actualizacion_posgrado_bpm", df.format(ultimaMod));
				objHubSpotData.put("estatus_posgrado_admision_bpm", solicitud.get("estatus_solicitud"));
			} else if(solicitud.get("estatus_solicitud").equals("solicitud_admitida")) {
				ultimaMod = new Date();
				objHubSpotData.put("fecha_actualizacion_posgrado_bpm", df.format(ultimaMod));
				objHubSpotData.put("estatus_posgrado_admision_bpm", solicitud.get("estatus_solicitud"));
			} else if(solicitud.get("estatus_solicitud").equals("solicitud_no_admitida")) {
				ultimaMod = new Date();
				objHubSpotData.put("fecha_actualizacion_posgrado_bpm", df.format(ultimaMod));
				objHubSpotData.put("estatus_posgrado_admision_bpm", solicitud.get("estatus_solicitud"));
				objHubSpotData.put("mensaje_posgrado_bpm", solicitud.get("aprobado_area_academic"));
			} else if(solicitud.get("estatus_solicitud").equals("solicitud_reactivada")) {
				LOGGER.error "[createOrUpdatePosgrado] 5.8";
				ultimaMod = new Date();
				objHubSpotData.put("fecha_actualizacion_posgrado_bpm", df.format(ultimaMod));
				objHubSpotData.put("estatus_posgrado_admision_bpm", "solicitud_completada");
			} 
			
			LOGGER.error "[createOrUpdatePosgrado] 6";
			resultado = createOrUpdateHubspotPosgrado(solicitud.get("correo_electronico"), apikeyHubspot, objHubSpotData);
			LOGGER.error "[createOrUpdatePosgrado] 7";
		} catch (Exception e) {
			LOGGER.error (errorLog + " | " + (resultado.getError_info() == null ? "" : resultado.getError_info()));
			
			resultado.setError_info(errorLog + " | " + (resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
			new LogDAO().insertTransactionLog("POST", "FALLIDO", "Error inesperado", "Log:"+errorLog, e.getMessage())
		} finally {
			resultado.setError_info(resultado.getError_info() + errorLog);
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}

		return resultado;
	}
	
	public Result transferenciaHubspot(Long caseid, org.bonitasoft.web.extension.rest.RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		Boolean closeCon = false;
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		Boolean noSol = false;
		String msjNF = "";
		String errorLog = "";
		String apikeyHubspot ="";
		Date fecha = new Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Map<String, Object> solicitud;
		
		try {
			solicitud = getSolicitudByCaseid(caseid);
			resultadoApiKey = getApikeyHubspot(solicitud.get("grupo_bonita"));
			apikeyHubspot = (String) resultadoApiKey.getData().get(0);
			Date ultimaMod = new Date();
			objHubSpotData.put("fecha_actualizacion_posgrado_bpm", df.format(ultimaMod));
			objHubSpotData.put("fecha_transferencia_posgrado_bpm", df.format(ultimaMod).toString());
			objHubSpotData = getTransferenciaByCaseid(caseid, objHubSpotData);
			resultado = createOrUpdateHubspotPosgrado(solicitud.get("correo_electronico"), apikeyHubspot, objHubSpotData);
		} catch (Exception e) {
			resultado.setError_info(errorLog + " | " + (resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
			new LogDAO().insertTransactionLog("POST", "FALLIDO", "Error inesperado", "Log:"+errorLog, e.getMessage())
		} finally {
			resultado.setError_info(resultado.getError_info() + errorLog);
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}

		return resultado;
	}
	
	public Result paseListaHubspot(Long caseid, org.bonitasoft.web.extension.rest.RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		Boolean closeCon = false;
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		Boolean noSol = false;
		String msjNF = "";
		String errorLog = "";
		String apikeyHubspot ="";
		Date fecha = new Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Map<String, Object> solicitud;
		
		try {
			solicitud = getSolicitudByCaseid(caseid);
			resultadoApiKey = getApikeyHubspot(solicitud.get("grupo_bonita"));
			apikeyHubspot = (String) resultadoApiKey.getData().get(0);
			Date ultimaMod = new Date();
			objHubSpotData.put("fecha_actualizacion_posgrado_bpm", df.format(ultimaMod));
			objHubSpotData = getTransferenciaByCaseid(caseid, objHubSpotData);
			resultado = createOrUpdateHubspotPosgrado(solicitud.get("correo_electronico"), apikeyHubspot, objHubSpotData);
		} catch (Exception e) {
			resultado.setError_info(errorLog + " | " + (resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
			new LogDAO().insertTransactionLog("POST", "FALLIDO", "Error inesperado", "Log:"+errorLog, e.getMessage())
		} finally {
			resultado.setError_info(resultado.getError_info() + errorLog);
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}

		return resultado;
	}
	
	public Result createOrUpdateHubspotPosgrado(String email, String apikeyHubspot, Map<String, String> objHubSpotData) {
		Result resultado = new Result();
		String targetURL = "https://api.hubapi.com/contacts/v1/contact/createOrUpdate/email/[EMAIL]/"
		String strError = "";

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		JSONObject jsonItem = new JSONObject();
		JSONObject jsonProperties = new JSONObject();
		JSONArray jsonList = new JSONArray();

		try {
			Iterator it = objHubSpotData.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				jsonItem = new JSONObject();
				jsonItem.put("property", pair.getKey());
				jsonItem.put("value", pair.getValue() == null ? "" : pair.getValue());
				jsonList.put(jsonItem);
				it.remove();
			}
			
			jsonProperties.put("properties", jsonList);
			targetURL = targetURL.replace("[EMAIL]", email);
			HttpPost request = new HttpPost(targetURL);
			StringEntity params = new StringEntity(jsonProperties.toString(), "UTF-8");
			request.setHeader("content-type", "application/json");
			request.setHeader("Accept-Encoding", "UTF-8");
			request.setHeader("Authorization", "Bearer "+apikeyHubspot);
			request.setEntity(params);

			CloseableHttpResponse response = httpClient.execute(request);

			if(response.getStatusLine().getStatusCode() != 200) {
				throw new Exception(EntityUtils.toString(response.getEntity(), "UTF-8"));
			}
			
			resultado.setError_info(strError);
			resultado.setSuccess(true);
			new LogDAO().insertTransactionLog("POST", "CORRECTO", targetURL, "Log:"+strError, jsonList.toString())
		} catch (Exception e) {
			String mError = "Problema detectado en el usuario: ${email} \r\nERROR: "+e.getMessage()+"\r\n"+"Log: "+strError;
			resultado.setError_info(mError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
			LOGGER.error "[createOrUpdateHubspotPosgrado] 1" + strError;
			new LogDAO().insertTransactionLog("POST", "FALLIDO", targetURL, "Log:"+strError, mError);
		}
		
		return resultado;
	}
}
