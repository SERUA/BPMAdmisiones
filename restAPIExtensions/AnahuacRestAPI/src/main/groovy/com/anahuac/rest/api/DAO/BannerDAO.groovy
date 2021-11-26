package com.anahuac.rest.api.DAO

import java.nio.charset.StandardCharsets
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatBachilleratos
import com.anahuac.catalogos.CatBachilleratosDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.CatBachillerato
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.AzureConfig
import com.bonitasoft.engine.api.ProcessAPI
import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper

class BannerDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(BannerDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	public Boolean validarConexion() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno = true
		}
		return retorno
	}

	public Result buscarCambiosBannerPreparatoria(RestAPIContext context, String operacion) {
		Result resultado = new Result();
		Result resultadoGetConsumeJSON = new Result();

		String errorLog = "";
		String barrerToken = "";
		String jsonResultado = "";
		String strGetConsumeJSON = "";
		try {
			errorLog += " | " + ("START JSON======================================");
			barrerToken = getBarreToken();
			//errorLog += " | " + barrerToken;
			errorLog += " | " + ("================================================");

			jsonResultado = getConsumePrepa(barrerToken);
			
			//errorLog += " | " + jsonResultado;
			//CREAR-------------------------------------------------------
			//jsonResultado = "[{\"id\":\"81\",\"published\":\"2021-05-31 18:30:53.865019+00\",\"resource\":{\"name\":\"educational-institutions\",\"id\":\"ba22c5ad-ab30-4d13-9fb2-3f7a8999375c\",\"version\":\"application/vnd.hedtech.integration.v6+json\"},\"operation\":\"created\",\"contentType\":\"resource-representation\",\"content\":{\"homeInstitution\":\"external\",\"id\":\"ba22c5ad-ab30-4d13-9fb2-3f7a8999375c\",\"title\":\"Instituto Curie\",\"type\":\"secondarySchool\",\"code\":\"9345\",\"typeInd\":\"H\"},\"publisher\":{\"id\":\"c9d2d963-68db-445d-a874-c9c103aa32ba\",\"applicationName\":\"RUAD INTEGRATION API (Shared Data)\",\"tenant\":{\"id\":\"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\":\"uatest\",\"name\":\"Universidad Anahuac\",\"environment\":\"Test\"}}},{\"id\":\"82\",\"published\":\"2021-05-31 18:31:56.227161+00\",\"resource\":{\"name\":\"addresses\",\"id\":\"7baa7116-e698-488f-b630-b3d14bbe9314\",\"version\":\"application/vnd.hedtech.integration.v11.1.0+json\"},\"operation\":\"created\",\"contentType\":\"resource-representation\",\"content\":{\"addressLines\":[\"AV. Montevideo\"],\"id\":\"7baa7116-e698-488f-b630-b3d14bbe9314\",\"place\":{\"country\":{\"code\":\"MEX\",\"locality\":\"CDMX\",\"postalCode\":\"07730\",\"postalTitle\":\"MEXICO\",\"region\":{\"title\":\"Ciudad de M\u00e9xico\"},\"subRegion\":{\"title\":\"Gustavo A. Madero\"},\"title\":\"M\u00e9xico\"}},\"addressExtended\":[{\"streetLine1\":\"AV. Montevideo\",\"streetLine2\":null,\"streetLine3\":\"calle 3\",\"nationCode\":\"99\",\"stateCode\":\"M16\",\"countyCode\":\"09005\"}]},\"publisher\":{\"id\":\"a216d744-fb37-413e-8430-7f187c223bda\",\"applicationName\":\"RUAD INTEGRATION API-UAN\",\"tenant\":{\"id\":\"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\":\"uatest\",\"name\":\"Universidad Anahuac\",\"environment\":\"Test\"}}}]";
			
			//EDITAR------------------------------------------------------
			//jsonResultado = "[{\"id\":\"30\",\"published\":\"2021-04-22 18:26:29.274756+00\",\"resource\":{\"name\":\"addresses\",\"id\":\"fe9eda15-74e3-4f1c-b249-e413c86bf49f\",\"version\":\"application/vnd.hedtech.integration.v11.1.0+json\"},\"operation\":\"replaced\",\"contentType\":\"resource-representation\",\"content\":{\"addressLines\":[\"Blvd. La Mirada 3050\",\"Los Angeles\"],\"id\":\"fe9eda15-74e3-4f1c-b249-e413c86bf49f\",\"place\":{\"country\":{\"code\":\"MEX\",\"locality\":\"Culiac\u00e1n 25006\",\"postalCode\":\"80014\",\"postalTitle\":\"MEXICO\",\"region\":{\"title\":\"Sinaloa\"},\"subRegion\":{\"title\":\"Culiac\u00e1n\"},\"title\":\"M\u00e9xico\"}}},\"publisher\":{\"id\":\"a216d744-fb37-413e-8430-7f187c223bda\",\"applicationName\":\"RUAD INTEGRATION API-UAN\",\"tenant\":{\"id\":\"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\":\"uatest\",\"name\":\"Universidad Anahuac\",\"environment\":\"Test\"}}}]";
			
			//DELETE------------------------------------------------------
			//jsonResultado = "[{\"id\": \"77\",\"published\": \"2021-05-31 18:07:49.688346+00\",\"resource\":{\"name\": \"educational-institutions\",\"id\": \"ba22c5ad-ab30-4d13-9fb2-3f7a8999375c\"},\"operation\": \"deleted\",\"contentType\": \"empty\",\"content\":{\"guid\": \"ba22c5ad-ab30-4d13-9fb2-3f7a8999375c\"},\"publisher\":{\"id\": \"c9d2d963-68db-445d-a874-c9c103aa32ba\",\"applicationName\": \"RUAD INTEGRATION API (Shared Data)\",\"tenant\":{\"id\": \"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\": \"uatest\",\"name\": \"Universidad Anahuac\",\"environment\": \"Test\"}}}]"
			
			//PROBLEMA
			//jsonResultado = "[{\"id\":\"132\",\"published\":\"2021-06-17 18:36:38.890122+00\",\"resource\":{\"name\":\"educational-institutions\",\"id\":\"efe85af3-95b3-49c6-823d-e86af029f8e5\",\"version\":\"application/vnd.hedtech.integration.v6+json\"},\"operation\":\"created\",\"contentType\":\"resource-representation\",\"content\":{\"addresses\":[{\"address\":{\"id\":\"f1a6ad1e-9ed1-4692-92fa-6df7582650b1\"},\"type\":{\"addressType\":\"school\"}}],\"homeInstitution\":\"external\",\"id\":\"efe85af3-95b3-49c6-823d-e86af029f8e5\",\"title\":\"Instituto Americano\",\"type\":\"secondarySchool\",\"code\":\"9680\",\"typeInd\":\"H\"},\"publisher\":{\"id\":\"c9d2d963-68db-445d-a874-c9c103aa32ba\",\"applicationName\":\"RUAD INTEGRATION API (Shared Data)\",\"tenant\":{\"id\":\"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\":\"uatest\",\"name\":\"Universidad Anahuac\",\"environment\":\"Test\"}}}]";
			
			errorLog += " | jsonResultado: " + jsonResultado;
			errorLog += " | " + ("END JSON========================================");

			resultadoGetConsumeJSON = getConsumeJSON(jsonResultado, context, operacion, barrerToken);
			errorLog += " | " + strGetConsumeJSON;
			//resultadoGetConsumeJSON.setSuccess(true);
			resultadoGetConsumeJSON.setError_info(errorLog + resultadoGetConsumeJSON.getError_info());
		} catch (Exception e) {
			errorLog += " | " + e.getMessage();
			resultadoGetConsumeJSON.setError_info(errorLog);
			e.printStackTrace()
		}

		return resultadoGetConsumeJSON;
	}
	
	public Result cambiosBannerPreparatoria(RestAPIContext context, String operacion, String jsonResultado) {
		Result resultado = new Result();
		Result resultadoGetConsumeJSON = new Result();

		String errorLog = "";
		String barrerToken = "";
		
		String strGetConsumeJSON = "";
		try {
			errorLog += " | " + ("START JSON======================================");
			barrerToken = getBarreToken();
			//errorLog += " | " + barrerToken;
			errorLog += " | " + ("================================================");

			//jsonResultado = getConsumePrepa(barrerToken);
			
			//errorLog += " | " + jsonResultado;
			//CREAR-------------------------------------------------------
			//jsonResultado = "[{\"id\":\"81\",\"published\":\"2021-05-31 18:30:53.865019+00\",\"resource\":{\"name\":\"educational-institutions\",\"id\":\"ba22c5ad-ab30-4d13-9fb2-3f7a8999375c\",\"version\":\"application/vnd.hedtech.integration.v6+json\"},\"operation\":\"created\",\"contentType\":\"resource-representation\",\"content\":{\"homeInstitution\":\"external\",\"id\":\"ba22c5ad-ab30-4d13-9fb2-3f7a8999375c\",\"title\":\"Instituto Curie\",\"type\":\"secondarySchool\",\"code\":\"9345\",\"typeInd\":\"H\"},\"publisher\":{\"id\":\"c9d2d963-68db-445d-a874-c9c103aa32ba\",\"applicationName\":\"RUAD INTEGRATION API (Shared Data)\",\"tenant\":{\"id\":\"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\":\"uatest\",\"name\":\"Universidad Anahuac\",\"environment\":\"Test\"}}},{\"id\":\"82\",\"published\":\"2021-05-31 18:31:56.227161+00\",\"resource\":{\"name\":\"addresses\",\"id\":\"7baa7116-e698-488f-b630-b3d14bbe9314\",\"version\":\"application/vnd.hedtech.integration.v11.1.0+json\"},\"operation\":\"created\",\"contentType\":\"resource-representation\",\"content\":{\"addressLines\":[\"AV. Montevideo\"],\"id\":\"7baa7116-e698-488f-b630-b3d14bbe9314\",\"place\":{\"country\":{\"code\":\"MEX\",\"locality\":\"CDMX\",\"postalCode\":\"07730\",\"postalTitle\":\"MEXICO\",\"region\":{\"title\":\"Ciudad de M\u00e9xico\"},\"subRegion\":{\"title\":\"Gustavo A. Madero\"},\"title\":\"M\u00e9xico\"}},\"addressExtended\":[{\"streetLine1\":\"AV. Montevideo\",\"streetLine2\":null,\"streetLine3\":\"calle 3\",\"nationCode\":\"99\",\"stateCode\":\"M16\",\"countyCode\":\"09005\"}]},\"publisher\":{\"id\":\"a216d744-fb37-413e-8430-7f187c223bda\",\"applicationName\":\"RUAD INTEGRATION API-UAN\",\"tenant\":{\"id\":\"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\":\"uatest\",\"name\":\"Universidad Anahuac\",\"environment\":\"Test\"}}}]";
			
			//EDITAR------------------------------------------------------
			//jsonResultado = "[{\"id\":\"30\",\"published\":\"2021-04-22 18:26:29.274756+00\",\"resource\":{\"name\":\"addresses\",\"id\":\"fe9eda15-74e3-4f1c-b249-e413c86bf49f\",\"version\":\"application/vnd.hedtech.integration.v11.1.0+json\"},\"operation\":\"replaced\",\"contentType\":\"resource-representation\",\"content\":{\"addressLines\":[\"Blvd. La Mirada 3050\",\"Los Angeles\"],\"id\":\"fe9eda15-74e3-4f1c-b249-e413c86bf49f\",\"place\":{\"country\":{\"code\":\"MEX\",\"locality\":\"Culiac\u00e1n 25006\",\"postalCode\":\"80014\",\"postalTitle\":\"MEXICO\",\"region\":{\"title\":\"Sinaloa\"},\"subRegion\":{\"title\":\"Culiac\u00e1n\"},\"title\":\"M\u00e9xico\"}}},\"publisher\":{\"id\":\"a216d744-fb37-413e-8430-7f187c223bda\",\"applicationName\":\"RUAD INTEGRATION API-UAN\",\"tenant\":{\"id\":\"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\":\"uatest\",\"name\":\"Universidad Anahuac\",\"environment\":\"Test\"}}}]";
			
			//DELETE------------------------------------------------------
			//jsonResultado = "[{\"id\": \"77\",\"published\": \"2021-05-31 18:07:49.688346+00\",\"resource\":{\"name\": \"educational-institutions\",\"id\": \"ba22c5ad-ab30-4d13-9fb2-3f7a8999375c\"},\"operation\": \"deleted\",\"contentType\": \"empty\",\"content\":{\"guid\": \"ba22c5ad-ab30-4d13-9fb2-3f7a8999375c\"},\"publisher\":{\"id\": \"c9d2d963-68db-445d-a874-c9c103aa32ba\",\"applicationName\": \"RUAD INTEGRATION API (Shared Data)\",\"tenant\":{\"id\": \"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\": \"uatest\",\"name\": \"Universidad Anahuac\",\"environment\": \"Test\"}}}]"
			
			//PROBLEMA
			//jsonResultado = "[{\"id\":\"132\",\"published\":\"2021-06-17 18:36:38.890122+00\",\"resource\":{\"name\":\"educational-institutions\",\"id\":\"efe85af3-95b3-49c6-823d-e86af029f8e5\",\"version\":\"application/vnd.hedtech.integration.v6+json\"},\"operation\":\"created\",\"contentType\":\"resource-representation\",\"content\":{\"addresses\":[{\"address\":{\"id\":\"f1a6ad1e-9ed1-4692-92fa-6df7582650b1\"},\"type\":{\"addressType\":\"school\"}}],\"homeInstitution\":\"external\",\"id\":\"efe85af3-95b3-49c6-823d-e86af029f8e5\",\"title\":\"Instituto Americano\",\"type\":\"secondarySchool\",\"code\":\"9680\",\"typeInd\":\"H\"},\"publisher\":{\"id\":\"c9d2d963-68db-445d-a874-c9c103aa32ba\",\"applicationName\":\"RUAD INTEGRATION API (Shared Data)\",\"tenant\":{\"id\":\"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\":\"uatest\",\"name\":\"Universidad Anahuac\",\"environment\":\"Test\"}}}]";
			
			errorLog += " | jsonResultado: " + jsonResultado;
			errorLog += " | " + ("END JSON========================================");

			resultadoGetConsumeJSON = getConsumeJSON(jsonResultado, context, operacion, barrerToken);
			errorLog += " | " + strGetConsumeJSON;
			//resultadoGetConsumeJSON.setSuccess(true);
			resultadoGetConsumeJSON.setError_info(errorLog + resultadoGetConsumeJSON.getError_info());
		} catch (Exception e) {
			errorLog += " | " + e.getMessage();
			resultadoGetConsumeJSON.setError_info(errorLog);
			e.printStackTrace()
		}

		return resultadoGetConsumeJSON;
	}
	
	private String getBarreToken() {
		String urlParaVisitar = "https://integrate.elluciancloud.com/auth";
		String barrerKey = "Bearer ";
		StringBuilder resultado = new StringBuilder();
		Boolean closeCon = false;
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(AzureConfig.GET_CONFIGURACIONES_CLAVE)
			pstm.setString(1, "BannerToken")
			rs = pstm.executeQuery()
			if (rs.next()) {

				barrerKey += rs.getString("valor")
			}

			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization", barrerKey.replace("-.-.-", ""));
			conexion.setRequestMethod("POST");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
			//System.out.println(urlParaVisitar);
			//System.out.println(resultado.toString());
			new LogDAO().insertTransactionLog("POST", "CORRECTO", "https://integrate.elluciancloud.com/auth", resultado.toString(), "Autenticarse para obtener el token de acceso, BannerToken:" + barrerKey.replace("-.-.-", ""))
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error "[ERROR]" + e.getMessage();
			new LogDAO().insertTransactionLog("POST", "FALLIDO", "https://integrate.elluciancloud.com/auth", e.getMessage(), "Autenticarse para obtener el token de acceso, BannerToken:" + barrerKey.replace("-.-.-", ""))
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado.toString();
	}
	private String getBarreToken(String token) {
		String urlParaVisitar = "https://integrate.elluciancloud.com/auth";
		String barrerKey = "Bearer " + token;
		StringBuilder resultado = new StringBuilder();
		Boolean closeCon = false;
		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization", barrerKey.replace("-.-.-", ""));
			conexion.setRequestMethod("POST");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
			//System.out.println(urlParaVisitar);
			//System.out.println(resultado.toString());
			new LogDAO().insertTransactionLog("POST", "CORRECTO", "https://integrate.elluciancloud.com/auth", resultado.toString(), "Autenticarse para obtener el token de acceso, BannerToken:" + barrerKey.replace("-.-.-", ""))
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error "[ERROR]" + e.getMessage();
			new LogDAO().insertTransactionLog("POST", "FALLIDO", "https://integrate.elluciancloud.com/auth", e.getMessage(), "Autenticarse para obtener el token de acceso, BannerToken:" + barrerKey.replace("-.-.-", ""))
		} finally {
			
		}
		return resultado.toString();
	}

	private String getAddresses(String barrerToken, String idDireccion) {
		String urlParaVisitar = "https://integrate.elluciancloud.com/api/addresses/"+idDireccion;
		StringBuilder resultado = new StringBuilder();

		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization", "Bearer " + barrerToken);
			conexion.setRequestProperty("Accept", "application/vnd.hedtech.integration.v11+json");
			conexion.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
			new LogDAO().insertTransactionLog("GET", "CORRECTO", urlParaVisitar, resultado.toString(), "Obtiene dirección apartir de un id, idDireccion:" + idDireccion)
		} catch (Exception e) {
			new LogDAO().insertTransactionLog("GET", "FALLIDO", urlParaVisitar, e.getMessage(), "Obtiene dirección apartir de un id, idDireccion:" + idDireccion)
			LOGGER.error "[ERROR]" + e.getMessage();
			e.printStackTrace();
		}
		return resultado.toString();
	}
	
	private String getConsumePrepa(String barrerToken) {
		String urlParaVisitar = "https://integrate.elluciancloud.com/consume";
		StringBuilder resultado = new StringBuilder();

		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization", "Bearer " + barrerToken);
			conexion.setRequestProperty("Accept", "application/vnd.hedtech.change-notifications.v2+json");
			conexion.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
			//System.out.println(urlParaVisitar);
			//System.out.println(resultado.toString());
			new LogDAO().insertTransactionLog("GET", "CORRECTO", "https://integrate.elluciancloud.com/consume", resultado.toString(), "Obtener preparatorias")
		} catch (Exception e) {
			LOGGER.error "[ERROR]" + e.getMessage();
			e.printStackTrace();
			new LogDAO().insertTransactionLog("GET", "FALLIDO", "https://integrate.elluciancloud.com/consume", e.getMessage(), "Obtener preparatorias")
		}
		return resultado.toString();
	}

	private Result getConsumeJSON(String jsonResultado, RestAPIContext context, String operacion, String barrerToken) {
		Result resultado = new Result();

		Integer indexAddress = null;

		JSONObject objJson = null;
		JSONObject objJsonAddressesData = null;
		JSONObject objJsonAddressExtended = null;
		JSONObject objJsoneducationalInstitutionsExtended= null;
		JSONObject objJsonAddressesDataAddress = null;
		JSONObject objJsonResource = null;
		JSONObject objJsonContent = null;
		JSONObject objJsonPlace = null;
		JSONObject objJsonCountry = null;
		JSONObject objJsonRegion = null;
		JSONObject objJsonPublisher = null;
		JSONObject objJsonAddressData = null;

		JSONArray objJsonAddresses = null;
		JSONArray lstAddressExtended = null;
		JSONArray lstEducationalInstitutionsExtended = null;
		JSONArray lstJson = null;

		JSONParser parser = new JSONParser();
		
		def jsonSlurper = new JsonSlurper();
		def object = jsonSlurper.parseText(jsonResultado);
		
		assert object instanceof List<Map>;
		
		CatBachillerato objEducationalInstitutions = new CatBachillerato();
		List < CatBachillerato > lstEducationalInstitutions = new ArrayList < CatBachillerato > ();

		CatBachillerato objAddresses = new CatBachillerato();
		List < CatBachillerato > lstAddresses = new ArrayList < CatBachillerato > ();

		CatBachillerato objCatBachillerato = new CatBachillerato();
		//List<CatBachillerato> lstCatBachillerato= new ArrayList<CatBachillerato>();

		DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		String errorLog = "";
		String strCountyCode = "";
		String strStateCode = "";
		String strNationCode = "";
		String resultEducationalInstitutions = "";
		String resultAddresses = "";

		Boolean isCountyCodeOk = false;
		Boolean isStateCodeOk = false;
		Boolean isStreetLineOk = false;
		Boolean isNationCodeOk = false;
		Boolean isNationCodeLetterOk = false;
		Boolean isMexicoOk = false;
		Boolean isUsaOk = false;
		Boolean isOtroPaisOk = false;
		Boolean closeCon = false;

		List < CatBachilleratos > lstCatBachilleratos = new ArrayList < CatBachilleratos > ();

		Long processId = null;
		Long processIdCrear = null;

		ProcessInstance processInstance = null;
		ProcessAPI processAPI = null;

		Map < String, Serializable > contracto = new HashMap < String, Serializable > ();
		Map < String, Serializable > objCatBachilleratosInput = new HashMap < String, Serializable > ();
		List < Map < String, Serializable >> lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
		SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
		List < ProcessDeploymentInfo > lstProcessDeploymentInfo = new ArrayList < ProcessDeploymentInfo > ();
		SearchOptions searchOptionsProccess = null;

		Pattern patternEstado = Pattern.compile("^m\\d\\d");
		Pattern pattern = Pattern.compile("[a-zA-Z]+");

		Matcher matcher = null;

		try {
			def catBachilleratosDAO = context.getApiClient().getDAO(CatBachilleratosDAO.class);
			errorLog = errorLog + " | jsonResultado: " + jsonResultado;
			lstJson = (org.json.simple.JSONArray) parser.parse(jsonResultado);

			Iterator < JSONObject > iterator = lstJson.iterator();

			errorLog = errorLog + " | " + "--------------------------------------------------------";
			while (iterator.hasNext()) {

				objJson = iterator.next();
				objJsonResource = (JSONObject) objJson.get("resource");
				objJsonContent = (JSONObject) objJson.get("content");
				objJsonPublisher = (JSONObject) objJson.get("publisher");

				errorLog = errorLog + " | " + ("idBachillerato: " + objJsonContent.get("id").toString());
				errorLog = errorLog + " | " + ("operation: " + objJson.get("operation").toString());
				
				objEducationalInstitutions = new CatBachillerato();
				objAddresses = new CatBachillerato();

				switch (objJsonResource.get("name").toString()) {
					case "educational-institutions":
					if (objJson.get("operation").toString().equals("deleted")||objJson.get("operation").toString().equals("replaced") ) {
						if(objJsonContent.get("id") != null) {
							lstCatBachilleratos = catBachilleratosDAO.findById(objJsonContent.get("id").toString(), 0, 100);
						}
						else {
							lstCatBachilleratos = catBachilleratosDAO.findById(objJsonContent.get("guid").toString(), 0, 100);
						}
						
						if(lstCatBachilleratos.size()==0) {
							objJson.put("operation", "created")
						}
					}
						
						if (objJson.get("operation").toString().equals("deleted")) {
							errorLog = errorLog + " | " + "IF DELETE--------------------------------------------------------";
							errorLog = errorLog + " | " + objJsonContent.get("id");
							errorLog = errorLog + " | " + objJsonContent.get("guid");
							errorLog = errorLog + " | " + objJsonContent.toString();
							if(objJsonContent.get("id") != null) {
								objEducationalInstitutions.setIdBachillerato(objJsonContent.get("id").toString());
							}
							else {
								objEducationalInstitutions.setIdBachillerato(objJsonContent.get("guid").toString());
							}
							objEducationalInstitutions.setOperation(objJson.get("operation").toString());
						}
						else {
							errorLog = errorLog + " | educational-institutions--------------------------------------------------";
							errorLog = errorLog + " | " + ("IdBachillerato" +(objJsonContent.get("id").toString()));
							errorLog = errorLog + " | " + ("Descripcion" +(objJsonContent.get("title").toString()));
							errorLog = errorLog + " | " + ("UsuarioBanner" +(objJsonPublisher.get("applicationName").toString()));
							errorLog = errorLog + " | " + ("FechaImportacion" +(objJson.get("published").toString()));
							errorLog = errorLog + " | " + ("FechaCreacion" +(objJson.get("published").toString()));
							errorLog = errorLog + " | " + ("Operation" +(objJson.get("operation").toString()));
							errorLog = errorLog + " | " + ("Clave" +(objJsonContent.get("code").toString()));
							errorLog = errorLog + " | " + ("TypeInd" +(objJsonContent.get("typeInd").toString()));

							objEducationalInstitutions.setIdBachillerato(objJsonContent.get("id").toString());
							objEducationalInstitutions.setDescripcion(objJsonContent.get("title").toString());
							objEducationalInstitutions.setUsuarioBanner(objJsonPublisher.get("applicationName").toString());
							objEducationalInstitutions.setFechaImportacion(objJson.get("published").toString());
							objEducationalInstitutions.setFechaCreacion(objJson.get("published").toString());
							objEducationalInstitutions.setOperation(objJson.get("operation").toString());
							objEducationalInstitutions.setClave(objJsonContent.get("code").toString());
							objEducationalInstitutions.setTypeInd(objJsonContent.get("typeInd").toString());
							
							errorLog = errorLog + " | " + "objEducationalInstitutions.setStreetLine1";
	
							objEducationalInstitutions.setStreetLine1("");
							objEducationalInstitutions.setStreetLine2("");
							objEducationalInstitutions.setStreetLine3("");
							objEducationalInstitutions.setNationCode("");
							objEducationalInstitutions.setStateCode("");
							objEducationalInstitutions.setCountyCode("");
							errorLog = errorLog + " | " + "objEducationalInstitutions.setCountyCode";
							objEducationalInstitutions.setIsEliminado(false);
							objEducationalInstitutions.setIsEnabled(true);
							objEducationalInstitutions.setPerteneceRed(false);
							objCatBachillerato.setPais("");
							objCatBachillerato.setEstado("");
							objCatBachillerato.setCiudad("");

							if (!objJson.get("operation").toString().equals("created")) {
								errorLog = errorLog + " | " + "IF CREATED--------------------------------------------------------";
								objJsonAddresses = (JSONArray) objJsonContent.get("addresses");
								Iterator < JSONObject > iteratorAddresses = objJsonAddresses.iterator();
								while (iteratorAddresses.hasNext()) {
									objJsonAddressesData = iteratorAddresses.next();
									objJsonAddressesDataAddress = (JSONObject) objJsonAddressesData.get("address");
									errorLog = errorLog + " | " + ("idDireccion: " + objJsonAddressesDataAddress.get("id").toString());
									objEducationalInstitutions.setIdDireccion(objJsonAddressesDataAddress.get("id").toString());
								}
							} else {
								errorLog = errorLog + " | objEducationalInstitutions.getIdBachillerato(): " + (objEducationalInstitutions.getIdBachillerato());
								errorLog = errorLog + " | objEducationalInstitutions.getDescripcion(): " + (objEducationalInstitutions.getDescripcion());
								errorLog = errorLog + " | objEducationalInstitutions.getUsuarioBanner(): " + (objEducationalInstitutions.getUsuarioBanner());
								errorLog = errorLog + " | objEducationalInstitutions.getFechaImportacion(): " + (objEducationalInstitutions.getFechaImportacion());
								errorLog = errorLog + " | objEducationalInstitutions.getFechaCreacion(): " + (objEducationalInstitutions.getFechaCreacion());
								errorLog = errorLog + " | objEducationalInstitutions.getOperation(): " + (objEducationalInstitutions.getOperation());
								errorLog = errorLog + " | barrerToken: " + (barrerToken);
								errorLog = errorLog + " | objJsonContent.get(id).toString(): " + (objJsonContent.get("id").toString());
	
								resultEducationalInstitutions = getConsumeEducationalInstitutions(barrerToken, objJsonContent.get("id").toString());
								errorLog = errorLog + " | " + ("getConsumeEducationalInstitutions ====================================");
								errorLog = errorLog + " |-" + (resultEducationalInstitutions == null ? "resultEducationalInstitutions is null" : (resultEducationalInstitutions.equals("")? "resultEducationalInstitutions is vacio" : resultEducationalInstitutions) )+"-";
								
								if(resultEducationalInstitutions == null ? false : (!resultEducationalInstitutions.equals(""))) {
									objJsonAddressData = (JSONObject) parser.parse(resultEducationalInstitutions);
									objJsonAddresses = (JSONArray) objJsonAddressData.get("addresses");
									Iterator < JSONObject > iteratorAddresses = objJsonAddresses.iterator();
									while (iteratorAddresses.hasNext()) {
										objJsonAddressesData = iteratorAddresses.next();
										objJsonAddressesDataAddress = (JSONObject) objJsonAddressesData.get("address");
										errorLog = errorLog + " | " + ("idDireccion: " + objJsonAddressesDataAddress.get("id").toString());
										objEducationalInstitutions.setIdDireccion(objJsonAddressesDataAddress.get("id").toString());
										
										resultAddresses = getAddresses(barrerToken, objJsonAddressesDataAddress.get("id").toString());
										errorLog = errorLog + " | " + ("resultAddresses: " + resultAddresses);
										if(!resultAddresses.equals("")) {
											errorLog = errorLog + " | " + ("resultAddresses: " + resultAddresses);
											
											objJsonContent = (JSONObject) parser.parse(resultAddresses);
											
											objJsonPlace = (JSONObject) objJsonContent.get("place");
											lstAddressExtended = (JSONArray) objJsonContent.get("addressExtended");
											objJsonCountry = (JSONObject) objJsonPlace.get("country");
											objJsonRegion = (JSONObject) objJsonCountry.get("region");
					
											errorLog = errorLog + " | " + ("idDireccion: " + objJsonContent.get("id").toString());
											errorLog = errorLog + " | " + ("pais: " + objJsonCountry.get("title").toString());
											if(objJsonRegion != null) {
												errorLog = errorLog + " | " + ("1.Estado: " + objJsonRegion.get("title").toString());
												objEducationalInstitutions.setEstado(objJsonRegion.get("title").toString())
												/*1*/objAddresses.setEstado(objJsonRegion.get("title").toString())
												}
											errorLog = errorLog + " | " + ("ciudad: " + objJsonCountry.get("locality").toString());
											errorLog = errorLog + " | " + ("idDireccion: " + objJsonContent.get("id").toString());
											errorLog = errorLog + " | " + ("pais: " + (objJsonCountry.get("title")==null ? "" : objJsonCountry.get("title").toString()));
											if(objJsonRegion != null) {
												errorLog = errorLog + " | " + ("2.Estado: " + (objJsonRegion.get("title")==null ? "" : objJsonRegion.get("title").toString()));
												objEducationalInstitutions.setEstado((objJsonRegion.get("title")==null ? "" : objJsonRegion.get("title").toString()))
												objAddresses.setEstado((objJsonRegion.get("title")==null ? "" : objJsonRegion.get("title").toString()))
											}
											errorLog = errorLog + " | " + ("ciudad: " + (objJsonCountry.get("locality")==null ? "" : objJsonCountry.get("locality").toString()));
											objAddresses.setIdDireccion(objJsonContent.get("id").toString());
					
											objAddresses.setOperation(objJson.get("operation").toString());
											
											if (objJsonCountry.get("title").toString().equals("México")) {
												objAddresses.setPais(objJsonCountry.get("title").toString());
												if(objJsonRegion != null) {
													objAddresses.setEstado((objJsonRegion.get("title")==null ? "" : objJsonRegion.get("title").toString()));
												}
												objAddresses.setCiudad(objJsonCountry.get("locality").toString());
											} else {
												objAddresses.setPais(objJsonCountry.get("title").toString());
												if(objJsonRegion != null) {
													objAddresses.setEstado((objJsonRegion.get("title")==null ? "" : objJsonRegion.get("title").toString()).equals("Estado Extranjero") ? "" : (objJsonRegion.get("title")==null ? "" : objJsonRegion.get("title").toString()));
												}
												objAddresses.setCiudad(objJsonCountry.get("locality").toString().equals("000000") ? "" : objJsonCountry.get("locality").toString());
											}
					
											Iterator < JSONObject > iteratorAddressExtended = lstAddressExtended.iterator();
											while (iteratorAddressExtended.hasNext()) {
												objJsonAddressExtended = iteratorAddressExtended.next();
												objAddresses.setStreetLine1(objJsonAddressExtended.get("streetLine1").toString().equals("null") ? null : (objJsonAddressExtended.get("streetLine1").toString()));
												objAddresses.setStreetLine2(objJsonAddressExtended.get("streetLine2").toString().equals("null") ? null : (objJsonAddressExtended.get("streetLine2").toString()));
												objAddresses.setStreetLine3(objJsonAddressExtended.get("streetLine3").toString().equals("null") ? null : (objJsonAddressExtended.get("streetLine3").toString()));
												objAddresses.setNationCode(objJsonAddressExtended.get("nationCode").toString().equals("null") ? null : (objJsonAddressExtended.get("nationCode").toString()));
												objAddresses.setStateCode(objJsonAddressExtended.get("stateCode").toString().equals("null") ? null : (objJsonAddressExtended.get("stateCode").toString()));
												objAddresses.setCountyCode(objJsonAddressExtended.get("countyCode").toString().equals("null") ? null : (objJsonAddressExtended.get("countyCode").toString()));
											}
											Iterator < JSONObject > iteratoreducationalInstitutionsExtended = lstEducationalInstitutionsExtended.iterator();
											while(iteratoreducationalInstitutionsExtended.hasNext()) {
												objJsoneducationalInstitutionsExtended = iteratoreducationalInstitutionsExtended.next();
												objAddresses.setIdBachillerato(objJsoneducationalInstitutionsExtended.get("id").toString().equals("null") ? null : (objJsoneducationalInstitutionsExtended.get("id").toString()))
											}
											lstAddresses.add(objAddresses);
											
											
										}
										else {
											errorLog = errorLog + " | " + ("ELSE resultAddresses: " + resultAddresses);
										}
									}
								}
								else {
									errorLog = errorLog + " | " + ("ELSE resultEducationalInstitutions ====================================");
									objEducationalInstitutions.setOperation("deleted");
								}
							}
							
						}
						
						lstEducationalInstitutions.add(objEducationalInstitutions);

					break;
					case "addresses":
						errorLog = errorLog + " | addresses";
						objJsonPlace = (JSONObject) objJsonContent.get("place");
						lstAddressExtended = (JSONArray) objJsonContent.get("addressExtended");
						lstEducationalInstitutionsExtended = (JSONArray) objJsonContent.get("educationalInstitutionsExtended");
						objJsonCountry = (JSONObject) objJsonPlace.get("country");
						objJsonRegion = (JSONObject) objJsonCountry.get("region");

						errorLog = errorLog + " | " + ("idDireccion: " + objJsonContent.get("id").toString());
						errorLog = errorLog + " | " + ("pais: " + (objJsonCountry.get("title")==null ? "" : objJsonCountry.get("title").toString()));
						if(objJsonRegion != null) {
							errorLog = errorLog + " | " + ("3.Estado: " + (objJsonRegion.get("title")==null ? "" : objJsonRegion.get("title").toString()));
							try {
							objAddresses.setEstado(objJsonRegion.get("title").toString())
							}catch(Exception e) {
								errorLog = errorLog + " | 3.Estado: " + e.getMessage()
							}
						}
						errorLog = errorLog + " | " + ("ciudad: " + (objJsonCountry.get("locality")==null ? "" : objJsonCountry.get("locality").toString()));
						objAddresses.setIdDireccion(objJsonContent.get("id").toString());

						objAddresses.setOperation(objJson.get("operation").toString());
						
						if (objJsonCountry.get("title").toString().equals("México")) {
							objAddresses.setPais(objJsonCountry.get("title").toString());
							if(objJsonRegion != null) {
								objAddresses.setEstado((objJsonRegion.get("title")==null ? "" : objJsonRegion.get("title").toString()));
							}
							objAddresses.setCiudad(objJsonCountry.get("locality").toString());
						} else {
							objAddresses.setPais(objJsonCountry.get("title").toString());
							if(objJsonRegion != null) {
								//objAddresses.setEstado((objJsonRegion.get("title")==null ? "" : objJsonRegion.get("title").toString()).equals("Estado Extranjero") ? "" : (objJsonRegion.get("title")==null ? "" : objJsonRegion.get("title").toString()));
								try {
									objAddresses.setEstado(objJsonRegion.get("title").toString())
								}catch(Exception e) {
									errorLog = errorLog + " | 4.Estado: " + e.getMessage()
								}
							}
							objAddresses.setCiudad(objJsonCountry.get("locality").toString().equals("000000") ? "" : objJsonCountry.get("locality").toString());
						}

						Iterator < JSONObject > iteratorAddressExtended = lstAddressExtended.iterator();
						
						while (iteratorAddressExtended.hasNext()) {
							objJsonAddressExtended = iteratorAddressExtended.next();
							objAddresses.setStreetLine1(objJsonAddressExtended.get("streetLine1").toString().equals("null") ? null : (objJsonAddressExtended.get("streetLine1").toString()));
							objAddresses.setStreetLine2(objJsonAddressExtended.get("streetLine2").toString().equals("null") ? null : (objJsonAddressExtended.get("streetLine2").toString()));
							objAddresses.setStreetLine3(objJsonAddressExtended.get("streetLine3").toString().equals("null") ? null : (objJsonAddressExtended.get("streetLine3").toString()));
							objAddresses.setNationCode(objJsonAddressExtended.get("nationCode").toString().equals("null") ? null : (objJsonAddressExtended.get("nationCode").toString()));
							objAddresses.setStateCode(objJsonAddressExtended.get("stateCode").toString().equals("null") ? null : (objJsonAddressExtended.get("stateCode").toString()));
							objAddresses.setCountyCode(objJsonAddressExtended.get("countyCode").toString().equals("null") ? null : (objJsonAddressExtended.get("countyCode").toString()));
						}
						Iterator < JSONObject > iteratoreducationalInstitutionsExtended = lstEducationalInstitutionsExtended.iterator();
						while(iteratoreducationalInstitutionsExtended.hasNext()) {
							objJsoneducationalInstitutionsExtended = iteratoreducationalInstitutionsExtended.next();
							objAddresses.setIdBachillerato(objJsoneducationalInstitutionsExtended.get("id").toString().equals("null") ? null : (objJsoneducationalInstitutionsExtended.get("id").toString()))
						}
						lstAddresses.add(objAddresses);
					break;
					default:
						errorLog = errorLog + " | " + (objJsonResource.get("name").toString());
					break;
				}

			}
			errorLog = errorLog + " | " + ("--------------------------------------------------------");
			processInstance = null;
			processAPI = context.getApiClient().getProcessAPI();

			contracto = new HashMap < String, Serializable > ();
			objCatBachilleratosInput = new HashMap < String, Serializable > ();
			lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();

			searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Editar Bachilleratos");
			searchOptionsProccess = searchBuilderProccess.done();
			SearchResult < ProcessDeploymentInfo > SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			for (ProcessDeploymentInfo objProcessDeploymentInfo: lstProcessDeploymentInfo) {
				if (objProcessDeploymentInfo.getActivationState().toString().equals("ENABLED")) {
					processId = objProcessDeploymentInfo.getProcessId();
				}
			}

			searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Agregar Bachilleratos");
			searchOptionsProccess = searchBuilderProccess.done();
			SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			for (ProcessDeploymentInfo objProcessDeploymentInfo: lstProcessDeploymentInfo) {
				if (objProcessDeploymentInfo.getActivationState().toString().equals("ENABLED")) {
					processIdCrear = objProcessDeploymentInfo.getProcessId();
				}
			}

			/*--------------------------------------------------------------------------------------------------------------*/

			errorLog = errorLog + " | " + ("====================================");
			errorLog = errorLog + " | lstEducationalInstitutions.size():" + (lstEducationalInstitutions.size());
			//def catBachilleratosDAO = context.getApiClient().getDAO(CatBachilleratosDAO.class);
			for (CatBachillerato row: lstEducationalInstitutions) {
				errorLog = errorLog + " | row.getOperation(): " + (row.getOperation());
				errorLog = errorLog + " | row.getUsuarioBanner(): " + (row.getUsuarioBanner());
				errorLog = errorLog + " | row.getEstado(): " + (row.getEstado());
				errorLog = errorLog + " | row.getCiudad(): " + (row.getCiudad());
				errorLog = errorLog + " | row.getPais(): " + (row.getPais());
				errorLog = errorLog + " | row.getIdBachillerato(): " + (row.getIdBachillerato());
				errorLog = errorLog + " | row.getOperation(): " + (row.getOperation());
				errorLog = errorLog + " | row.getClave(): " + (row.getClave());
				errorLog = errorLog + " | row.getDescripcion(): " + (row.getDescripcion());

				if (row.getOperation().equals("replaced")) {
					errorLog = errorLog + " | " + row.getOperation();

					lstCatBachilleratos = catBachilleratosDAO.findById(row.getIdBachillerato(), 0, 100);
					if (lstCatBachilleratos != null) {
						for (CatBachilleratos objRow: lstCatBachilleratos) {
							
							isMexicoOk = false;
							isUsaOk = false;
							isOtroPaisOk = false;
							isStateCodeOk = false;
							isNationCodeOk = false;
							isCountyCodeOk = false;
							isNationCodeLetterOk = false;
							
							if (objRow.getPais().equals("México")) {
								strStateCode = objRow.getStateCode() == null ? "" : objRow.getStateCode();
								if (!strStateCode.equals("")) {
									matcher = patternEstado.matcher(strStateCode.toLowerCase());
									isStateCodeOk = matcher.matches();
								}
								strNationCode = objRow.getNationCode() == null ? "" : objRow.getNationCode();
								
								if (!strNationCode.equals("")) {
									isNationCodeOk = strNationCode.equals("99")
								}
								
								isStreetLineOk = (objRow.getStreetLine1() != null && objRow.getStreetLine3() != null);
								errorLog = errorLog + " | isStreetLineOk: " + (isStreetLineOk);
								errorLog = errorLog + " | isStateCodeOk: " + (isStateCodeOk);
								errorLog = errorLog + " | isStreetLineOk: " + (isStreetLineOk);
								errorLog = errorLog + " | getStreetLine1: " + objRow.getStreetLine1()
								errorLog = errorLog + " | getStreetLine3: " + objRow.getStreetLine3()
								isMexicoOk = (isNationCodeOk && isStateCodeOk && isStreetLineOk);
								errorLog = errorLog + " | isMexicoOk: " + (isMexicoOk);
							} else {
								if (objRow.getPais().equals("Estados Unidos de América")) {
									strNationCode = objRow.getNationCode() == null ? "" : objRow.getNationCode();
									
									if (!strNationCode.equals("")) {
										isNationCodeOk = strNationCode.equals("157")
										matcher = pattern.matcher(strNationCode)
										isNationCodeLetterOk = !matcher.find();
									}
							
									strCountyCode = objRow.getCountyCode() == null ? "" : objRow.getCountyCode();
									
									if (!strCountyCode.equals("")) {
										isCountyCodeOk = strCountyCode.equals("20000")
									}
									isUsaOk = (isNationCodeOk && isCountyCodeOk && isNationCodeLetterOk);
								} else {
									strNationCode = objRow.getNationCode() == null ? "" : objRow.getNationCode();
									
									if (!strNationCode.equals("")) {
										isNationCodeOk = !strNationCode.equals("157") && !strNationCode.equals("99");
										matcher = pattern.matcher(strNationCode)
										isNationCodeLetterOk = !matcher.find();
									}
									strCountyCode = objRow.getCountyCode() == null ? "" : objRow.getCountyCode();
									
									if (!strCountyCode.equals("")) {
										isCountyCodeOk = strCountyCode.equals("20000")
									}
									
									strStateCode = objRow.getStateCode() == null ? "" : objRow.getStateCode();
									if (!strStateCode.equals("")) {
										isStateCodeOk = strStateCode.toLowerCase().equals("fr")
									}
									
									isOtroPaisOk = (isNationCodeOk && isCountyCodeOk && isNationCodeLetterOk && isStateCodeOk);

								}
							}
						
							//if (!objRow.isIsEliminado()) {
								if (row.getTypeInd().equals("H")) {
									lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
									objCatBachilleratosInput = new HashMap < String, Serializable > ();
									contracto = new HashMap < String, Serializable > ();
									errorLog = errorLog + " | CON H ---------------------------------------------------------";
									
									errorLog = errorLog + " | persistenceId: " + objRow.getPersistenceId();
									errorLog = errorLog + " | persistenceVersion: " + objRow.getPersistenceVersion();
									errorLog = errorLog + " | perteneceRed: " + false;
									errorLog = errorLog + " | region: " + null;
									errorLog = errorLog + " | caseId: " + null;
									errorLog = errorLog + " | clave: " + objRow.getClave();
									errorLog = errorLog + " | fechaImportacion: " + null;
									errorLog = errorLog + " | fechaCreacion: " + null;
									errorLog = errorLog + " | usuarioCreacion: " + "Administrador";
									errorLog = errorLog + " | descripcion: " + row.getDescripcion();
									errorLog = errorLog + " | usuarioBanner: " + row.getUsuarioBanner();
									errorLog = errorLog + " | estado: " + objRow.getEstado();
									errorLog = errorLog + " | ciudad: " + objRow.getCiudad();
									errorLog = errorLog + " | pais: " + objRow.getPais();
									errorLog = errorLog + " | id: " + row.getIdBachillerato();
									errorLog = errorLog + " | streetLine1: " + objRow.getStreetLine1();
									errorLog = errorLog + " | streetLine2: " + objRow.getStreetLine2();
									errorLog = errorLog + " | streetLine3: " + objRow.getStreetLine3();
									errorLog = errorLog + " | nationCode: " + objRow.getNationCode();
									errorLog = errorLog + " | stateCode: " + objRow.getStateCode();
									errorLog = errorLog + " | countyCode: " + objRow.getCountyCode();
									errorLog = errorLog + " | typeInd: " + row.getTypeInd();
									errorLog = errorLog + " | isEliminado: " + !(row.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) );
									errorLog = errorLog + " | CON H ---------------------------------------------------------";
									
									/*CONSTRUCCION DE CONTRATO=====================================================================*/
									objCatBachilleratosInput.put("persistenceId", objRow.getPersistenceId());
									objCatBachilleratosInput.put("persistenceVersion", objRow.getPersistenceVersion());
									objCatBachilleratosInput.put("isEliminado", !(row.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ));
									objCatBachilleratosInput.put("isEnabled", true);
									objCatBachilleratosInput.put("todelete", (row.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) ? "false" : "true");
									objCatBachilleratosInput.put("perteneceRed", false);
									objCatBachilleratosInput.put("region", null);
									objCatBachilleratosInput.put("caseId", null);
									objCatBachilleratosInput.put("clave", row.getClave());
									objCatBachilleratosInput.put("fechaImportacion", null);
									objCatBachilleratosInput.put("fechaCreacion", null);
									objCatBachilleratosInput.put("usuarioCreacion", "Administrador");
									objCatBachilleratosInput.put("descripcion", row.getDescripcion());
									objCatBachilleratosInput.put("usuarioBanner", row.getUsuarioBanner());
									objCatBachilleratosInput.put("estado", objRow.getEstado());
									objCatBachilleratosInput.put("ciudad", objRow.getCiudad());
									objCatBachilleratosInput.put("pais", objRow.getPais());
									objCatBachilleratosInput.put("id", row.getIdBachillerato());

									objCatBachilleratosInput.put("streetLine1", objRow.getStreetLine1());
									objCatBachilleratosInput.put("streetLine2", objRow.getStreetLine2());
									objCatBachilleratosInput.put("streetLine3", objRow.getStreetLine3());
									objCatBachilleratosInput.put("nationCode", objRow.getNationCode());
									objCatBachilleratosInput.put("stateCode", objRow.getStateCode());
									objCatBachilleratosInput.put("countyCode", objRow.getCountyCode());
									objCatBachilleratosInput.put("typeInd", row.getTypeInd());

									lstCatBachilleratosInput.add(objCatBachilleratosInput);
									contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
									processInstance = processAPI.startProcessWithInputs(processId, contracto);
								} else {
									lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
									objCatBachilleratosInput = new HashMap < String, Serializable > ();
									contracto = new HashMap < String, Serializable > ();
									/*CONSTRUCCION DE CONTRATO====================================================================*/
									errorLog = errorLog + " | SIN H ---------------------------------------------------------";
									errorLog = errorLog + " | persistenceId " + objRow.getPersistenceId();
									errorLog = errorLog + " | persistenceVersion " + objRow.getPersistenceVersion();
									errorLog = errorLog + " | isEliminado " + true;
									errorLog = errorLog + " | isEnabled " + true;
									errorLog = errorLog + " | todelete " + "true";
									errorLog = errorLog + " | perteneceRed " + false;
									errorLog = errorLog + " | region " + null;
									errorLog = errorLog + " | caseId " + null;
									errorLog = errorLog + " | clave " + row.getClave();
									errorLog = errorLog + " | fechaImportacion " + null;
									errorLog = errorLog + " | fechaCreacion " + null;
									errorLog = errorLog + " | usuarioCreacion " + "Administrador";
									errorLog = errorLog + " | descripcion " + row.getDescripcion();
									errorLog = errorLog + " | usuarioBanner " + row.getUsuarioBanner();
									errorLog = errorLog + " | estado " + row.getEstado();
									errorLog = errorLog + " | ciudad " + row.getCiudad();
									errorLog = errorLog + " | pais " + row.getPais();
									errorLog = errorLog + " | id " + row.getIdBachillerato();
									errorLog = errorLog + " | streetLine1 " + objRow.getStreetLine1();
									errorLog = errorLog + " | streetLine2 " + objRow.getStreetLine2();
									errorLog = errorLog + " | streetLine3 " + objRow.getStreetLine3();
									errorLog = errorLog + " | nationCode " + objRow.getNationCode();
									errorLog = errorLog + " | stateCode " + objRow.getStateCode();
									errorLog = errorLog + " | countyCode " + objRow.getCountyCode();
									errorLog = errorLog + " | typeInd " + row.getTypeInd();
									errorLog = errorLog + " | SIN H ---------------------------------------------------------";
									
									objCatBachilleratosInput.put("persistenceId", objRow.getPersistenceId());
									objCatBachilleratosInput.put("persistenceVersion", objRow.getPersistenceVersion());
									objCatBachilleratosInput.put("isEliminado", true);
									objCatBachilleratosInput.put("isEnabled", true);
									objCatBachilleratosInput.put("todelete", "true");
									objCatBachilleratosInput.put("perteneceRed", false);
									objCatBachilleratosInput.put("region", null);
									objCatBachilleratosInput.put("caseId", null);
									objCatBachilleratosInput.put("clave", row.getClave());
									objCatBachilleratosInput.put("fechaImportacion", null);
									objCatBachilleratosInput.put("fechaCreacion", null);
									objCatBachilleratosInput.put("usuarioCreacion", "Administrador");
									objCatBachilleratosInput.put("descripcion", row.getDescripcion());
									objCatBachilleratosInput.put("usuarioBanner", row.getUsuarioBanner());
									objCatBachilleratosInput.put("estado", row.getEstado());
									objCatBachilleratosInput.put("ciudad", row.getCiudad());
									objCatBachilleratosInput.put("pais", row.getPais());
									objCatBachilleratosInput.put("id", row.getIdBachillerato());

									objCatBachilleratosInput.put("streetLine1", objRow.getStreetLine1());
									objCatBachilleratosInput.put("streetLine2", objRow.getStreetLine2());
									objCatBachilleratosInput.put("streetLine3", objRow.getStreetLine3());
									objCatBachilleratosInput.put("nationCode", objRow.getNationCode());
									objCatBachilleratosInput.put("stateCode", objRow.getStateCode());
									objCatBachilleratosInput.put("countyCode", objRow.getCountyCode());
									objCatBachilleratosInput.put("typeInd", row.getTypeInd());

									lstCatBachilleratosInput.add(objCatBachilleratosInput);
									contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
									processInstance = processAPI.startProcessWithInputs(processId, contracto);

								}
							//}
						}
					}
				} else {
					if (row.getOperation().equals("created")) {
						errorLog = errorLog + " | created===================================================================================================";
						lstCatBachilleratos = catBachilleratosDAO.findById(row.getIdBachillerato(), 0, 100);

						if (lstCatBachilleratos != null && lstCatBachilleratos.size() > 0) {
							errorLog = errorLog + " | IF BACHILLERATO NULL======================================================================================";
							for (CatBachilleratos objRow: lstCatBachilleratos) {
								if (!objRow.isIsEliminado()) {
									if (row.getTypeInd().equals("H")) {
										errorLog = errorLog + " | IF IS ELIMANDO ===========================================================================================";
										errorLog = errorLog + " | PersistenceId:" + objRow.getPersistenceId();
										errorLog = errorLog + " | Descripcion:" + row.getDescripcion();
										errorLog = errorLog + " | ================================================================== | ";
										
										isMexicoOk = false;
										isUsaOk = false;
										isOtroPaisOk = false;
										isStateCodeOk = false;
										isNationCodeOk = false;
										isCountyCodeOk = false;
										isNationCodeLetterOk = false;
										
										errorLog = errorLog + " | " + ("====================================");
										errorLog = errorLog + " | " + ("Validar Pais y estado");
										if (row.getPais().equals("México")) {
											errorLog = errorLog + " | " + (row.getPais());
										
											strStateCode = row.getStateCode() == null ? "" : row.getStateCode();
											errorLog = errorLog + " | strStateCode: " + (strStateCode);
											if (!strStateCode.equals("")) {
												matcher = patternEstado.matcher(strStateCode.toLowerCase());
												isStateCodeOk = matcher.matches();
											}
										
											strNationCode = row.getNationCode() == null ? "" : row.getNationCode();
											errorLog = errorLog + " | strNationCode: " + (strNationCode);
											if (!strNationCode.equals("")) {
												isNationCodeOk = strNationCode.equals("99")
											}
										
											isStreetLineOk = (objRow.getStreetLine1() != null && objRow.getStreetLine3() != null);
											errorLog = errorLog + " | isStreetLineOk: " + (isStreetLineOk);
											
											errorLog = errorLog + " | getStreetLine1: " + objRow.getStreetLine1()
											errorLog = errorLog + " | getStreetLine3: " + objRow.getStreetLine3()
											isMexicoOk = (isNationCodeOk && isStateCodeOk && isStreetLineOk);
										} else {
											if (row.getPais().equals("Estados Unidos de América")) {
												errorLog = errorLog + " | " + (row.getPais());
												strNationCode = row.getNationCode() == null ? "" : row.getNationCode();
												errorLog = errorLog + " | strNationCode: " + (strNationCode);
												if (!strNationCode.equals("")) {
													isNationCodeOk = strNationCode.equals("157")
													matcher = pattern.matcher(strNationCode)
													isNationCodeLetterOk = !matcher.find();
												}
										
												strCountyCode = row.getCountyCode() == null ? "" : row.getCountyCode();
												errorLog = errorLog + " | strCountyCode: " + (strCountyCode);
												if (!strCountyCode.equals("")) {
													isCountyCodeOk = strCountyCode.equals("20000")
												}
												isUsaOk = (isNationCodeOk && isCountyCodeOk && isNationCodeLetterOk);
											} else {
												errorLog = errorLog + " | " + (row.getPais());
												strNationCode = row.getNationCode() == null ? "" : row.getNationCode();
												errorLog = errorLog + " | strNationCode: " + (strNationCode);
												if (!strNationCode.equals("")) {
													isNationCodeOk = !strNationCode.equals("157") && !strNationCode.equals("99");
													matcher = pattern.matcher(strNationCode)
													isNationCodeLetterOk = !matcher.find();
												}
												strCountyCode = row.getCountyCode() == null ? "" : row.getCountyCode();
												errorLog = errorLog + " | strCountyCode: " + (strCountyCode);
												if (!strCountyCode.equals("")) {
													isCountyCodeOk = strCountyCode.equals("20000")
												}
												strStateCode = row.getStateCode() == null ? "" : row.getStateCode();
												if (!strStateCode.equals("")) {
													isStateCodeOk = strStateCode.toLowerCase().equals("fr")
												}
												
												isOtroPaisOk = (isNationCodeOk && isCountyCodeOk && isNationCodeLetterOk && isStateCodeOk);
											}
										}
										
										
										
										
										lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
										objCatBachilleratosInput = new HashMap < String, Serializable > ();
										contracto = new HashMap < String, Serializable > ();
										/*CONSTRUCCION DE CONTRATO=====================================================================*/
										objCatBachilleratosInput.put("persistenceId", objRow.getPersistenceId());
										objCatBachilleratosInput.put("persistenceVersion", objRow.getPersistenceVersion());
										objCatBachilleratosInput.put("isEliminado", !(row.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ));
										objCatBachilleratosInput.put("isEnabled", true);
										objCatBachilleratosInput.put("todelete", (row.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) ? "false" : "true");
										objCatBachilleratosInput.put("perteneceRed", false);
										objCatBachilleratosInput.put("region", null);
										objCatBachilleratosInput.put("caseId", null);
										objCatBachilleratosInput.put("clave", row.getClave());
										objCatBachilleratosInput.put("fechaImportacion", null);
										objCatBachilleratosInput.put("fechaCreacion", null);
										objCatBachilleratosInput.put("usuarioCreacion", "Administrador");
										objCatBachilleratosInput.put("descripcion", row.getDescripcion());
										objCatBachilleratosInput.put("usuarioBanner", row.getUsuarioBanner());
										objCatBachilleratosInput.put("estado", row.getEstado());
										objCatBachilleratosInput.put("ciudad", row.getCiudad());
										objCatBachilleratosInput.put("pais", row.getPais());
										objCatBachilleratosInput.put("id", row.getIdBachillerato());

										objCatBachilleratosInput.put("streetLine1", row.getStreetLine1());
										objCatBachilleratosInput.put("streetLine2", row.getStreetLine2());
										objCatBachilleratosInput.put("streetLine3", row.getStreetLine3());
										objCatBachilleratosInput.put("nationCode", row.getNationCode());
										objCatBachilleratosInput.put("stateCode", row.getStateCode());
										objCatBachilleratosInput.put("countyCode", row.getCountyCode());
										objCatBachilleratosInput.put("typeInd", row.getTypeInd());

										lstCatBachilleratosInput.add(objCatBachilleratosInput);

										contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
										processInstance = processAPI.startProcessWithInputs(processId, contracto);
									} else {
										errorLog = errorLog + " | CREATED DELETE";

										errorLog = errorLog + " | PersistenceId:" + objRow.getPersistenceId();
										errorLog = errorLog + " | Descripcion:" + objRow.getDescripcion();
										errorLog = errorLog + " | ================================================================== | ";
										lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
										objCatBachilleratosInput = new HashMap < String, Serializable > ();
										contracto = new HashMap < String, Serializable > ();
										/*CONSTRUCCION DE CONTRATO====================================================================*/
										objCatBachilleratosInput.put("persistenceId", objRow.getPersistenceId());
										objCatBachilleratosInput.put("persistenceVersion", objRow.getPersistenceVersion());
										objCatBachilleratosInput.put("isEliminado", true);
										objCatBachilleratosInput.put("isEnabled", true);
										objCatBachilleratosInput.put("todelete", "true");
										objCatBachilleratosInput.put("perteneceRed", false);
										objCatBachilleratosInput.put("region", null);
										objCatBachilleratosInput.put("caseId", null);
										objCatBachilleratosInput.put("clave", row.getClave());
										objCatBachilleratosInput.put("fechaImportacion", null);
										objCatBachilleratosInput.put("fechaCreacion", null);
										objCatBachilleratosInput.put("usuarioCreacion", "Administrador");
										objCatBachilleratosInput.put("descripcion", row.getDescripcion());
										objCatBachilleratosInput.put("usuarioBanner", row.getUsuarioBanner());
										objCatBachilleratosInput.put("estado", row.getEstado());
										objCatBachilleratosInput.put("ciudad", row.getCiudad());
										objCatBachilleratosInput.put("pais", row.getPais());
										objCatBachilleratosInput.put("id", row.getIdBachillerato());

										objCatBachilleratosInput.put("streetLine1", objRow.getStreetLine1());
										objCatBachilleratosInput.put("streetLine2", objRow.getStreetLine2());
										objCatBachilleratosInput.put("streetLine3", objRow.getStreetLine3());
										objCatBachilleratosInput.put("nationCode", objRow.getNationCode());
										objCatBachilleratosInput.put("stateCode", objRow.getStateCode());
										objCatBachilleratosInput.put("countyCode", objRow.getCountyCode());
										objCatBachilleratosInput.put("typeInd", row.getTypeInd());

										lstCatBachilleratosInput.add(objCatBachilleratosInput);
										contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
										processInstance = processAPI.startProcessWithInputs(processId, contracto);

									}
								}
							}
						} else {
							if (row.getTypeInd().equals("H")) {
								errorLog = errorLog + " | ELSE CREAR DATA ==========================================================================================";
								errorLog = errorLog + " | " + row.getOperation();
								errorLog = errorLog + " | processIdCrear: " + processIdCrear;
								errorLog = errorLog + " | TypeInd: " + row.getTypeInd()
								lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
								objCatBachilleratosInput = new HashMap < String, Serializable > ();
								contracto = new HashMap < String, Serializable > ();
								/*CONSTRUCCION DE CONTRATO=====================================================================*/
								objCatBachilleratosInput.put("isEliminado", true);
								objCatBachilleratosInput.put("isEnabled", true);
								objCatBachilleratosInput.put("todelete", "true");
								objCatBachilleratosInput.put("perteneceRed", false);
								objCatBachilleratosInput.put("region", null);
								objCatBachilleratosInput.put("caseId", null);
								objCatBachilleratosInput.put("clave", row.getClave());
								objCatBachilleratosInput.put("fechaImportacion", null);
								objCatBachilleratosInput.put("fechaCreacion", null);
								objCatBachilleratosInput.put("usuarioCreacion", "Administrador");
								objCatBachilleratosInput.put("descripcion", row.getDescripcion());
								objCatBachilleratosInput.put("usuarioBanner", row.getUsuarioBanner());
								objCatBachilleratosInput.put("estado", row.getEstado());
								objCatBachilleratosInput.put("ciudad", row.getCiudad());
								objCatBachilleratosInput.put("pais", row.getPais());
								objCatBachilleratosInput.put("id", row.getIdBachillerato());
								objCatBachilleratosInput.put("idDireccion", row.getIdDireccion());

								/*eddressExtended--------------------------------------------------------------------------------*/
								objCatBachilleratosInput.put("streetLine1", row.getStreetLine1());
								objCatBachilleratosInput.put("streetLine2", row.getStreetLine2());
								objCatBachilleratosInput.put("streetLine3", row.getStreetLine3());
								objCatBachilleratosInput.put("nationCode", row.getNationCode());
								objCatBachilleratosInput.put("stateCode", row.getStateCode());
								objCatBachilleratosInput.put("countyCode", row.getCountyCode());
								objCatBachilleratosInput.put("typeInd", row.getTypeInd());

								lstCatBachilleratosInput.add(objCatBachilleratosInput);
								contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
								processInstance = processAPI.startProcessWithInputs(processIdCrear, contracto);
								errorLog = errorLog + " | processInstance: " + processInstance;
							} else {
								errorLog = errorLog + " | CREATED DELETEeeeeeeeeeeeeeeeeeee";
								errorLog = errorLog + " | TypeInd: " + row.getTypeInd()
								errorLog = errorLog + " | ELSE CREAR DATA DELETE ==========================================================================================";
								errorLog = errorLog + " | " + row.getOperation();
								errorLog = errorLog + " | processIdCrear: " + processIdCrear;
								lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
								objCatBachilleratosInput = new HashMap < String, Serializable > ();
								contracto = new HashMap < String, Serializable > ();
								/*CONSTRUCCION DE CONTRATO=====================================================================*/
								objCatBachilleratosInput.put("isEliminado", true);
								objCatBachilleratosInput.put("isEnabled", true);
								objCatBachilleratosInput.put("todelete", "true");
								objCatBachilleratosInput.put("perteneceRed", false);
								objCatBachilleratosInput.put("region", null);
								objCatBachilleratosInput.put("caseId", null);
								objCatBachilleratosInput.put("clave", row.getClave());
								objCatBachilleratosInput.put("fechaImportacion", null);
								objCatBachilleratosInput.put("fechaCreacion", null);
								objCatBachilleratosInput.put("usuarioCreacion", "Administrador");
								objCatBachilleratosInput.put("descripcion", row.getDescripcion());
								objCatBachilleratosInput.put("usuarioBanner", row.getUsuarioBanner());
								objCatBachilleratosInput.put("estado", row.getEstado());
								objCatBachilleratosInput.put("ciudad", row.getCiudad());
								objCatBachilleratosInput.put("pais", row.getPais());
								objCatBachilleratosInput.put("id", row.getIdBachillerato());
								objCatBachilleratosInput.put("idDireccion", row.getIdDireccion());

								/*eddressExtended--------------------------------------------------------------------------------*/
								objCatBachilleratosInput.put("streetLine1", row.getStreetLine1());
								objCatBachilleratosInput.put("streetLine2", row.getStreetLine2());
								objCatBachilleratosInput.put("streetLine3", row.getStreetLine3());
								objCatBachilleratosInput.put("nationCode", row.getNationCode());
								objCatBachilleratosInput.put("stateCode", row.getStateCode());
								objCatBachilleratosInput.put("countyCode", row.getCountyCode());
								objCatBachilleratosInput.put("typeInd", row.getTypeInd());
								
								errorLog = errorLog + " | ---------------------------------------------------------";
								errorLog = errorLog + " | " + ("isEliminado: "+ objCatBachilleratosInput.get("isEliminado"));
								errorLog = errorLog + " | " + ("isEnabled: "+ objCatBachilleratosInput.get("isEnabled"));
								errorLog = errorLog + " | " + ("todelete: "+ objCatBachilleratosInput.get("todelete"));
								errorLog = errorLog + " | " + ("perteneceRed: "+ objCatBachilleratosInput.get("perteneceRed"));
								errorLog = errorLog + " | " + ("region: "+ objCatBachilleratosInput.get("region"));
								errorLog = errorLog + " | " + ("caseId: "+ objCatBachilleratosInput.get("caseId"));
								errorLog = errorLog + " | " + ("clave: "+ objCatBachilleratosInput.get("clave"));
								errorLog = errorLog + " | " + ("fechaImportacion: "+ objCatBachilleratosInput.get("fechaImportacion"));
								errorLog = errorLog + " | " + ("fechaCreacion: "+ objCatBachilleratosInput.get("fechaCreacion"));
								errorLog = errorLog + " | " + ("usuarioCreacion: "+ objCatBachilleratosInput.get("usuarioCreacion"));
								errorLog = errorLog + " | " + ("descripcion: "+ objCatBachilleratosInput.get("descripcion"));
								errorLog = errorLog + " | " + ("usuarioBanner: "+ objCatBachilleratosInput.get("usuarioBanner"));
								errorLog = errorLog + " | " + ("estado: "+ objCatBachilleratosInput.get("estado"));
								errorLog = errorLog + " | " + ("ciudad: "+ objCatBachilleratosInput.get("ciudad"));
								errorLog = errorLog + " | " + ("pais: "+ objCatBachilleratosInput.get("pais"));
								errorLog = errorLog + " | " + ("id: "+ objCatBachilleratosInput.get("id"));
								errorLog = errorLog + " | " + ("idDireccion: "+ objCatBachilleratosInput.get("idDireccion"));
								errorLog = errorLog + " | " + ("streetLine1: "+ objCatBachilleratosInput.get("streetLine1"));
								errorLog = errorLog + " | " + ("streetLine2: "+ objCatBachilleratosInput.get("streetLine2"));
								errorLog = errorLog + " | " + ("streetLine3: "+ objCatBachilleratosInput.get("streetLine3"));
								errorLog = errorLog + " | " + ("nationCode: "+ objCatBachilleratosInput.get("nationCode"));
								errorLog = errorLog + " | " + ("stateCode: "+ objCatBachilleratosInput.get("stateCode"));
								errorLog = errorLog + " | " + ("countyCode: "+ objCatBachilleratosInput.get("countyCode"));
								
								errorLog = errorLog + " | ---------------------------------------------------------";
								
								lstCatBachilleratosInput.add(objCatBachilleratosInput);
								contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
								processInstance = processAPI.startProcessWithInputs(processIdCrear, contracto);
								errorLog = errorLog + " | CREATED DELETE SUCCESS";
							}
						}
					} else {
						if (row.getOperation().equals("deleted")) {
							//if(operacion.equals("deleted")) {
							errorLog = errorLog + " | " + row.getOperation();
							errorLog = errorLog + " | " + row.getIdBachillerato();
							
							closeCon = validarConexion();
							pstm = con.prepareStatement(Statements.DELETE_BACHILLERATO_BY_ID)
							pstm.setString(1, "ELIMINADO");
							pstm.setString(2, "ELIMINADO");
							pstm.setString(3, "ELIMINADO");
							pstm.setString(4, row.getIdBachillerato());
							pstm.executeUpdate();
						}
					}
				}
			}

			errorLog = errorLog + " | " + ("====================================");
			errorLog = errorLog + " | lstAddresses.size():" + (lstAddresses.size());
			for (CatBachillerato objLstAddresses: lstAddresses) {
				//if(objLstAddresses.getOperation().equals("replaced")) {
				errorLog = errorLog + " | objLstAddresses.getOperation():" + (objLstAddresses.getOperation());
				errorLog = errorLog + " | objLstAddresses.getIdDireccion():" + (objLstAddresses.getIdDireccion());

				isMexicoOk = false;
				isUsaOk = false;
				isOtroPaisOk = false;
				isStateCodeOk = false;
				isNationCodeOk = false;
				isCountyCodeOk = false;
				isStreetLineOk = false;
				
				errorLog = errorLog + " | " + ("====================================");
				errorLog = errorLog + " | " + ("Validar Pais y estado");
				if (objLstAddresses.getPais().equals("México")) {
					errorLog = errorLog + " | " + (objLstAddresses.getPais());

					strStateCode = objLstAddresses.getStateCode() == null ? "" : objLstAddresses.getStateCode();
					errorLog = errorLog + " | strStateCode: " + (strStateCode);
					if (!strStateCode.equals("")) {
						matcher = patternEstado.matcher(strStateCode.toLowerCase());
						isStateCodeOk = matcher.matches();
					}

					strNationCode = objLstAddresses.getNationCode() == null ? "" : objLstAddresses.getNationCode();
					errorLog = errorLog + " | strNationCode: " + (strNationCode);
					if (!strNationCode.equals("")) {
						isNationCodeOk = strNationCode.equals("99")
					}

					isStreetLineOk = (objLstAddresses.getStreetLine1() != null && objLstAddresses.getStreetLine3() != null);
					errorLog = errorLog + " | isStreetLineOk: " + (isStreetLineOk);
					
					errorLog = errorLog + " | getStreetLine1: " + objLstAddresses.getStreetLine1()
					errorLog = errorLog + " | getStreetLine3: " + objLstAddresses.getStreetLine3()
					isMexicoOk = (isNationCodeOk && isStateCodeOk && isStreetLineOk);
				} else {
					if (objLstAddresses.getPais().equals("Estados Unidos de América")) {
						errorLog = errorLog + " | " + (objLstAddresses.getPais());
						strNationCode = objLstAddresses.getNationCode() == null ? "" : objLstAddresses.getNationCode();
						errorLog = errorLog + " | strNationCode: " + (strNationCode);
						if (!strNationCode.equals("")) {
							isNationCodeOk = strNationCode.equals("157")
							matcher = pattern.matcher(strNationCode)
							isNationCodeLetterOk = !matcher.find();
						}

						strCountyCode = objLstAddresses.getCountyCode() == null ? "" : objLstAddresses.getCountyCode();
						errorLog = errorLog + " | strCountyCode: " + (strCountyCode);
						if (!strCountyCode.equals("")) {
							isCountyCodeOk = strCountyCode.equals("20000")
						}
						isUsaOk = (isNationCodeOk && isCountyCodeOk && isNationCodeLetterOk);
					} else {
						errorLog = errorLog + " | " + (objLstAddresses.getPais());
						strNationCode = objLstAddresses.getNationCode() == null ? "" : objLstAddresses.getNationCode();
						errorLog = errorLog + " | strNationCode: " + (strNationCode);
						if (!strNationCode.equals("")) {
							isNationCodeOk = !strNationCode.equals("157") && !strNationCode.equals("99");
							matcher = pattern.matcher(strNationCode)
							isNationCodeLetterOk = !matcher.find();
						}
						strCountyCode = objLstAddresses.getCountyCode() == null ? "" : objLstAddresses.getCountyCode();
						errorLog = errorLog + " | strCountyCode: " + (strCountyCode);
						if (!strCountyCode.equals("")) {
							isCountyCodeOk = strCountyCode.equals("20000")
						}
						strStateCode = objLstAddresses.getStateCode() == null ? "" : objLstAddresses.getStateCode();
						if (!strStateCode.equals("")) {
							isStateCodeOk = strStateCode.toLowerCase().equals("fr")
						}
						
						isOtroPaisOk = (isNationCodeOk && isCountyCodeOk && isNationCodeLetterOk && isStateCodeOk);
					}
				}

				lstCatBachilleratos = catBachilleratosDAO.findByIdDireccion(objLstAddresses.getIdDireccion(), 0, 100);
				if(lstCatBachilleratos.size()==0) {
					errorLog = errorLog + " |lstCatBachilleratos.size()=0, IDBACHILLERATO de busqueda " + objLstAddresses.getIdBachillerato();
					lstCatBachilleratos = catBachilleratosDAO.findById(objLstAddresses.getIdBachillerato(), 0, 100);
				}
				if(lstCatBachilleratos.size()>0){
					for (CatBachilleratos objRow: lstCatBachilleratos) {
							for(int i=0; i<object.size(); i++) {
								if(object.get(i).content.educationalInstitutionsExtended.get(0).code.equals(objRow.getClave())) {
									objRow.setDescripcion(object.get(i).content.educationalInstitutionsExtended.get(0).title)
								}
							}
							errorLog = errorLog + " | PersistenceId:" + objRow.getPersistenceId();
							errorLog = errorLog + " | Descripcion:" + objRow.getDescripcion();
							errorLog = errorLog + " | ================================================================== | ";
	
							lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
							objCatBachilleratosInput = new HashMap < String, Serializable > ();
							contracto = new HashMap < String, Serializable > ();
							/*CONSTRUCCION DE CONTRATO=====================================================================*/
							objCatBachilleratosInput.put("persistenceId", objRow.getPersistenceId());
							objCatBachilleratosInput.put("persistenceVersion", objRow.getPersistenceVersion());
	
							objCatBachilleratosInput.put("isEliminado", !(objRow.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) );
							objCatBachilleratosInput.put("isEnabled", objRow.isIsEnabled());
							objCatBachilleratosInput.put("todelete", (objRow.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) ? "false" : "true");
	
							objCatBachilleratosInput.put("perteneceRed", objRow.isPerteneceRed());
							objCatBachilleratosInput.put("region", null);
							objCatBachilleratosInput.put("caseId", null);
							objCatBachilleratosInput.put("clave", objRow.getClave());
							objCatBachilleratosInput.put("fechaImportacion", null);
							objCatBachilleratosInput.put("fechaCreacion", null);
							objCatBachilleratosInput.put("usuarioCreacion", "Administrador");
							objCatBachilleratosInput.put("descripcion", objRow.getDescripcion());
							objCatBachilleratosInput.put("usuarioBanner", objRow.getUsuarioBanner());
							objCatBachilleratosInput.put("estado", objLstAddresses.getEstado());
							objCatBachilleratosInput.put("ciudad", objLstAddresses.getCiudad());
							objCatBachilleratosInput.put("pais", objLstAddresses.getPais());
							objCatBachilleratosInput.put("id", objRow.getId());
							/*eddressExtended--------------------------------------------------------------------------------*/
							objCatBachilleratosInput.put("streetLine1", objLstAddresses.getStreetLine1());
							objCatBachilleratosInput.put("streetLine2", objLstAddresses.getStreetLine2());
							objCatBachilleratosInput.put("streetLine3", objLstAddresses.getStreetLine3());
							objCatBachilleratosInput.put("nationCode", objLstAddresses.getNationCode());
							objCatBachilleratosInput.put("stateCode", objLstAddresses.getStateCode());
							objCatBachilleratosInput.put("countyCode", objLstAddresses.getCountyCode());
							objCatBachilleratosInput.put("typeInd", objRow.getTypeInd());
	
							
							
							errorLog = errorLog + " | DIRECCION==========================================================================================";
							errorLog = errorLog + " | " + ("persistenceId - " + objRow.getPersistenceId());
							errorLog = errorLog + " | " + ("persistenceVersion - " + objRow.getPersistenceVersion());
							errorLog = errorLog + " | " + ("isEliminado - " + !( objRow.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) );
							errorLog = errorLog + " | " + ("isEnabled - " + objRow.isIsEnabled());
							errorLog = errorLog + " | " + ("todelete - " + ( ( objRow.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) ? "false" : "true" ) );
							errorLog = errorLog + " | " + ("perteneceRed - " + objRow.isPerteneceRed());
							errorLog = errorLog + " | " + ("region - " + null);
							errorLog = errorLog + " | " + ("caseId - " + null);
							errorLog = errorLog + " | " + ("clave - " + objRow.getClave());
							errorLog = errorLog + " | " + ("fechaImportacion - " + null);
							errorLog = errorLog + " | " + ("fechaCreacion - " + null);
							errorLog = errorLog + " | " + ("usuarioCreacion - " + "Administrador");
							errorLog = errorLog + " | " + ("descripcion - " + objRow.getDescripcion());
							errorLog = errorLog + " | " + ("usuarioBanner - " + objRow.getUsuarioBanner());
							errorLog = errorLog + " | " + ("estado - " + objLstAddresses.getEstado());
							errorLog = errorLog + " | " + ("ciudad - " + objLstAddresses.getCiudad());
							errorLog = errorLog + " | " + ("pais - " + objLstAddresses.getPais());
							errorLog = errorLog + " | " + ("id - " + objRow.getId());
							errorLog = errorLog + " | " + ("streetLine1 - " + objLstAddresses.getStreetLine1());
							errorLog = errorLog + " | " + ("streetLine2 - " + objLstAddresses.getStreetLine2());
							errorLog = errorLog + " | " + ("streetLine3 - " + objLstAddresses.getStreetLine3());
							errorLog = errorLog + " | " + ("nationCode - " + objLstAddresses.getNationCode());
							errorLog = errorLog + " | " + ("stateCode - " + objLstAddresses.getStateCode());
							errorLog = errorLog + " | " + ("countyCode - " + objLstAddresses.getCountyCode());
							errorLog = errorLog + " | DIRECCION==========================================================================================";
	
							lstCatBachilleratosInput.add(objCatBachilleratosInput);
							contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
							processInstance = processAPI.startProcessWithInputs(processId, contracto);
					}
				}else {
					for(int i=0; i<object.size(); i++) {
						if(object.get(i).content.educationalInstitutionsExtended.get(0).id != null) {
							lstCatBachilleratos = catBachilleratosDAO.findById(object.get(i).content.educationalInstitutionsExtended.get(0).id.toString(), 0, 100);
						}
						else {
							lstCatBachilleratos = catBachilleratosDAO.findById(object.get(i).content.educationalInstitutionsExtended.get(0).guid.toString(), 0, 100);
						}
						
						if(lstCatBachilleratos.size()==0 && object.get(i).operation.equals("replaced")) {
							lstCatBachilleratosInput.add(generateContract(object.get(i), isMexicoOk, isUsaOk, isOtroPaisOk))
						}
						
					}
					contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
					errorLog += "|| else -> " + lstCatBachilleratosInput.toString()
					processInstance = processAPI.startProcessWithInputs(processIdCrear, contracto);
				}
			}

			resultado.setData(lstEducationalInstitutions);

			resultado.setSuccess(true);
			resultado.setError_info(errorLog);

		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError_info(errorLog);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	private static String getConsumeEducationalInstitutions(String barrerToken, String idBachillerato) {
		String urlParaVisitar = "https://integrate.elluciancloud.com/api/educational-institutions/" + idBachillerato;
		StringBuilder resultado = new StringBuilder();

		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization", "Bearer " + barrerToken);
			conexion.setRequestProperty("Accept", "application/vnd.hedtech.integration.v6+json");
			conexion.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultado.toString();
	}
	private Map < String, Serializable > generateContract(Object object, boolean isMexicoOk,boolean isUsaOk, boolean isOtroPaisOk){
		Map < String, Serializable > objCatBachilleratosInput = new HashMap < String, Serializable > ();

			
		objCatBachilleratosInput.put("descripcion",object.content.educationalInstitutionsExtended.get(0).title)
		objCatBachilleratosInput.put("persistenceId", null);
		objCatBachilleratosInput.put("persistenceVersion", null);

		objCatBachilleratosInput.put("isEliminado", !(object.content.educationalInstitutionsExtended.get(0).typeInd.equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) );
		objCatBachilleratosInput.put("isEnabled", true);
		objCatBachilleratosInput.put("todelete", (object.content.educationalInstitutionsExtended.get(0).typeInd.equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) ? "false" : "true");

		objCatBachilleratosInput.put("perteneceRed", null);
		objCatBachilleratosInput.put("region", null);
		objCatBachilleratosInput.put("caseId", null);
		objCatBachilleratosInput.put("clave", object.content.educationalInstitutionsExtended.get(0).code);
		objCatBachilleratosInput.put("fechaImportacion", null);
		objCatBachilleratosInput.put("fechaCreacion", null);
		objCatBachilleratosInput.put("usuarioCreacion", "Administrador");
		objCatBachilleratosInput.put("usuarioBanner", object.publisher.applicationName);
		objCatBachilleratosInput.put("estado", object.content.place.country.region.title);
		objCatBachilleratosInput.put("ciudad", object.content.place.country.subRegion.title);
		objCatBachilleratosInput.put("pais", object.content.place.country.title);
		objCatBachilleratosInput.put("id",  object.content.educationalInstitutionsExtended.get(0).id);
		/*eddressExtended--------------------------------------------------------------------------------*/
		objCatBachilleratosInput.put("streetLine1", object.content.addressExtended.get(0).streetLine1);
		objCatBachilleratosInput.put("streetLine2", object.content.addressExtended.get(0).streetLine2);
		objCatBachilleratosInput.put("streetLine3", object.content.addressExtended.get(0).streetLine3);
		objCatBachilleratosInput.put("nationCode", object.content.addressExtended.get(0).nationCode);
		objCatBachilleratosInput.put("stateCode", object.content.addressExtended.get(0).stateCode);
		objCatBachilleratosInput.put("countyCode", object.content.addressExtended.get(0).countyCode);
		objCatBachilleratosInput.put("typeInd", object.content.educationalInstitutionsExtended.get(0).typeInd);
		objCatBachilleratosInput.put("idDireccion", object.resource.id);
			
		
		return objCatBachilleratosInput
	}
	/**
	 * 1.Obtiene id para necesario para el metodo student-aptitude-assessments POST y PUT
	 * 
	 * @param barrerToken BPM Match Person
	 * @param idbanner Parametro para encontrar el aspirante
	 * 
	 * @return [{"credentials": [{"type": "bannerSourcedId", "value": ""}, {"type": "bannerUserName", "value": ""}, {"type": "bannerUdcId", "value": ""}, {"type": "bannerId", "value": ""} ], "id": ""} ]
	 * */
	private String personsCredentials(String barrerToken, String idbanner) {
		String urlParaVisitar = "https://integrate.elluciancloud.com/api/persons-credentials?criteria=";
		StringBuilder resultado = new StringBuilder();
		String param="{\"credentials\":[{\"type\":\"bannerId\",\"value\":\""+idbanner+"\"}]}"
		urlParaVisitar+= URLEncoder.encode(param,StandardCharsets.UTF_8)
		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization", "Bearer " + barrerToken);
			conexion.setRequestProperty("Accept", "application/vnd.hedtech.integration.v11+json");
			conexion.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
			new LogDAO().insertTransactionLog("GET", "CORRECTO", urlParaVisitar, resultado.toString(), "Obtener id aspirante con idBanner:" + idbanner)
		} catch (Exception e) {
			LOGGER.error "[ERROR]" + e.getMessage();
			e.printStackTrace();
			new LogDAO().insertTransactionLog("GET", "FALLIDO", urlParaVisitar, e.getMessage(), "Obtener id de aspirante con idBanner: "+ idbanner)
		}
		return resultado.toString();
	}
	
	/**
	 * 2.Obtiene la lista de examenes
	 *
	 * @param barrerToken BPM Match Person
	 *
	 * @return [{"code": "", "id": "", "title": "", "validScores": {"range": {"increment": 0, "maximum": 0, "minimum": 0 } } }... ]
	 * */
	private String aptitudeAssessments(String barrerToken) {
		String urlParaVisitar = "https://integrate.elluciancloud.com/api/aptitude-assessments";
		StringBuilder resultado = new StringBuilder();
		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization", "Bearer " + barrerToken);
			conexion.setRequestProperty("Accept", "application/vnd.hedtech.integration.v6+json");
			conexion.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
			new LogDAO().insertTransactionLog("GET", "CORRECTO", urlParaVisitar, resultado.toString(), "Obtener aptitudeAssessments")
		} catch (Exception e) {
			LOGGER.error "[ERROR]" + e.getMessage();
			e.printStackTrace();
			new LogDAO().insertTransactionLog("GET", "FALLIDO", urlParaVisitar, e.getMessage(), "Obtener aptitudeAssessments ")
		}
		return resultado.toString();
	}
	/**
	 * 3.Obtiene la lista de assessment-special-circumstances
	 *
	 * @param barrerToken BPM Match Person
	 *
	 * @return [{"code": "", "id": "", "title": ""}... ]
	 * */
	private String assessmentSpecialCircumstances(String barrerToken) {
		String urlParaVisitar = "https://integrate.elluciancloud.com/api/assessment-special-circumstances";
		StringBuilder resultado = new StringBuilder();
		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization", "Bearer " + barrerToken);
			conexion.setRequestProperty("Accept", "application/vnd.hedtech.integration.v6+json");
			conexion.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
			new LogDAO().insertTransactionLog("GET", "CORRECTO", urlParaVisitar, resultado.toString(), "Obtener la lista de assessmentSpecialCircumstances")
		} catch (Exception e) {
			LOGGER.error "[ERROR]" + e.getMessage();
			e.printStackTrace();
			new LogDAO().insertTransactionLog("GET", "FALLIDO", urlParaVisitar, e.getMessage(), "Obtener la lista de assessmentSpecialCircumstances")
		}
		return resultado.toString();
	}
	/**
	 * 4.Obtiene la lista de sources, es necesario para el source id para student-aptitude-assessments POST o PUT
	 *
	 * @param barrerToken BPM Match Person
	 *
	 * @return [{"code": "", "id": "", "status": "", "title": ""}... ]
	 * */
	private String sources(String barrerToken) {
		String urlParaVisitar = "https://integrate.elluciancloud.com/api/sources";
		StringBuilder resultado = new StringBuilder();
		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization", "Bearer " + barrerToken);
			conexion.setRequestProperty("Accept", "application/vnd.hedtech.integration.v6+json");
			conexion.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
			new LogDAO().insertTransactionLog("GET", "CORRECTO", urlParaVisitar, resultado.toString(), "Obtener la lista de sources")
		} catch (Exception e) {
			LOGGER.error "[ERROR]" + e.getMessage();
			e.printStackTrace();
			new LogDAO().insertTransactionLog("GET", "FALLIDO", urlParaVisitar, e.getMessage(), "Obtener la lista de sources")
		}
		return resultado.toString();
	}
	/**
	 * 5.Obtiene la lista de scores registrados para el usuario para saber si se va ejecutar  student-aptitude-assessments POST o un PUT
	 *
	 * @param barrerToken BPM UNIVERSIDAD (UAN, UAM, etc...)
	 * @param studentId Parametro se encuentra en el API de persons-credentials
	 *
	 * @return [{"assessedOn": "", "assessment": {"id": ""}, "form": {"name": "Test Form A", "number": "TA"}, "id": "", "preference": "primary", "reported": "official", "score": {"type": "numeric", "value": 0 }, "source": {"id": ""}, "specialCircumstances": [{"id": ""} ], "status": "active", "student": {"id": ""}, "update": "original"}... ]
	 * */
	private String studentAptitudeAssessments(String barrerToken, String studentId) {
		String urlParaVisitar = "https://integrate.elluciancloud.com/api/student-aptitude-assessments?criteria=";
		StringBuilder resultado = new StringBuilder();
		String param="{\"student\":{\"id\": \""+studentId+"\"}}"
		urlParaVisitar+= URLEncoder.encode(param,StandardCharsets.UTF_8)
		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization", "Bearer " + barrerToken);
			conexion.setRequestProperty("Accept", "application/vnd.hedtech.integration.v16+json");
			conexion.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
			new LogDAO().insertTransactionLog("GET", "CORRECTO", urlParaVisitar, resultado.toString(), "Obtener studentAptitudeAssessments")
		} catch (Exception e) {
			LOGGER.error "[ERROR]" + e.getMessage();
			e.printStackTrace();
			new LogDAO().insertTransactionLog("GET", "FALLIDO", urlParaVisitar, e.getMessage(), "Obtener studentAptitudeAssessments")
		}
		return resultado.toString();
	}
	/**
	 * 6.Agrega registro a BANNER
	 * 
	 * @param barrerToken BPM UNIVERSIDAD (UAN, UAM, etc...)
	 * @param studentId Parametro se encuentra en el API de persons-credentials
	 * @param assessmentId Parametro se encuentra en el API de aptitude-assessments es a quien se le dará el score
	 * @param score valor de score
	 * @param specialCircumstancesId Parametro se encuentra en el API de assessment-special-circumstances
	 * @param sourceId Parametro se encuentra en el API de sources
	 * 
	 * @return {"assessedOn": "", "assessment": {"id": ""}, "form": {"name": "Test Form A", "number": "TA"}, "id": "", "preference": "primary", "reported": "official", "score": {"type": "numeric", "value": 0 }, "source": {"id": ""}, "specialCircumstances": [{"id": ""} ], "status": "active", "student": {"id": ""}, "update": "original"}
	 * */
	private Result insertStudentAptitudeAssessments(String barrerToken,String studentId, String assessmentId, Integer score, String fecha, Boolean mmpi) {
		Result resultado = new Result();
		String targetURL = "https://integrate.elluciancloud.com/api/student-aptitude-assessments"
		/*String jsonInputString = (mmpi)?"{\"id\": \"00000000-0000-0000-0000-000000000000\", \"student\": {\"id\": \""+studentId+"\"}, \"assessment\": {\"id\": \""+assessmentId+"\"}, \"assessedOn\": \""+fecha+"\", \"score\": {\"type\": \"numeric\", \"value\": "+score+" }, \"form\": {\"number\": \"TA\", \"name\": \"Test Form A\"}, \"specialCircumstances\": [{\"id\": \""+specialCircumstancesId+"\"} ], \"update\": \"original\", \"preference\": \"primary\", \"source\": {\"id\": \""+sourceId+"\"}, \"status\": \"active\", \"reported\": \"official\"}":
		"{\"id\": \"00000000-0000-0000-0000-000000000000\", \"student\": {\"id\": \""+studentId+"\"}, \"assessment\": {\"id\": \""+assessmentId+"\"}, \"assessedOn\": \""+fecha+"\", \"score\": {\"type\": \"literal\", \"value\": \""+((10>score)?"0"+score:score)+"\" }, \"form\": {\"number\": \"TA\", \"name\": \"Test Form A\"}, \"specialCircumstances\": [{\"id\": \""+specialCircumstancesId+"\"} ], \"update\": \"original\", \"preference\": \"primary\", \"source\": {\"id\": \""+sourceId+"\"}, \"status\": \"active\", \"reported\": \"official\"}"*/
		String jsonInputString = (mmpi)?'{"id": "00000000-0000-0000-0000-000000000000", "student": {"id": "'+studentId+'"}, "assessment": {"id": "'+assessmentId+'"}, "assessedOn": "'+fecha+'", "score": {"type": "numeric", "value": '+score+' } }': '{"id": "00000000-0000-0000-0000-000000000000", "student": {"id": "'+studentId+'"}, "assessment": {"id": "'+assessmentId+'"}, "assessedOn": "'+fecha+'", "score": {"type": "numeric", "literal": '+((10>score)?"0"+score:score)+' } }';
		String strError = "";
		
		JSONObject jsonProperties =  (JSONObject) new JSONParser().parse(jsonInputString)
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		
		try {		
			HttpPost request = new HttpPost(targetURL);
			StringEntity params = new StringEntity(jsonProperties.toString(), "UTF-8");
			request.setHeader("Authorization", "Bearer " + barrerToken);
			request.setHeader("Content-Type", "application/vnd.hedtech.integration.v16+json");
			request.setHeader("Accept", "application/vnd.hedtech.integration.v16+json");
			request.setHeader("Accept-Encoding", "UTF-8");
			request.setEntity(params);
			
			CloseableHttpResponse response = httpClient.execute(request);
			strError = strError + " | "+ response.getEntity().getContentType().getName();
			strError = strError + " | "+ response.getEntity().getContentType().getValue();
			strError = strError + " | "+ EntityUtils.toString(response.getEntity(), "UTF-8");
			try {
				strError = strError + " | " + response.getStatusLine()
			} catch (Exception e) {
				e.printStackTrace()
			}
			
			resultado.setError_info(strError);
			resultado.setSuccess(true);
			new LogDAO().insertTransactionLog("POST", "CORRECTO", targetURL, "Log:"+strError, jsonInputString)
		} catch (Exception e) {
			resultado.setError_info(strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
			new LogDAO().insertTransactionLog("POST", "FALLIDO", targetURL, "Log:"+strError, e.getMessage())
		}
		return resultado
	}
	/**
	 * 7.Actualiza un registro de BANNER
	 *
	 * @param barrerToken BPM UNIVERSIDAD (UAN, UAM, etc...)
	 * @param studentId Parametro se encuentra en el API de persons-credentials
	 * @param assessmentId Parametro se encuentra en el API de aptitude-assessments es a quien se le dará el score
	 * @param score valor de score
	 * @param specialCircumstancesId Parametro se encuentra en el API de assessment-special-circumstances
	 * @param sourceId Parametro se encuentra en el API de sources
	 * @param soatestId es el Id unico de la prueba se obtiene de la lista de pruebas con el studentId
	 * @param fecha es la fecha de la prueba se obtiene de la lista de pruebas con el studentId
	 *
	 * @return {"assessedOn": "", "assessment": {"id": ""}, "form": {"name": "Test Form A", "number": "TA"}, "id": "", "preference": "primary", "reported": "official", "score": {"type": "numeric", "value": 0 }, "source": {"id": ""}, "specialCircumstances": [{"id": ""} ], "status": "active", "student": {"id": ""}, "update": "original"}
	 * */
	private Result updateStudentAptitudeAssessments(String barrerToken,String studentId, String assessmentId, Integer score, String fecha,String soatestId, Boolean mmpi) {
		Result resultado = new Result();
		String targetURL = "https://integrate.elluciancloud.com/api/student-aptitude-assessments/"+soatestId
		String jsonInputString = (mmpi)?"{\"assessedOn\": \""+fecha+"\", \"assessment\": {\"id\": \""+assessmentId+"\"}, \"score\": {\"type\": \"numeric\", \"value\": "+score+" }, \"status\": \"active\", \"student\": {\"id\": \""+studentId+"\"}, \"update\": \"original\"}":
        "{\"assessedOn\": \""+fecha+"\", \"assessment\": {\"id\": \""+assessmentId+"\"}, \"score\": {\"type\": \"literal\", \"value\": \""+((10>score)?"0"+score:score)+"\" }, \"status\": \"active\", \"student\": {\"id\": \""+studentId+"\"}, \"update\": \"original\"}"
		String strError = "";
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		JSONObject jsonProperties =  (JSONObject) new JSONParser().parse(jsonInputString)
		try {
			HttpPut request = new HttpPut(targetURL);
			StringEntity params = new StringEntity(jsonProperties.toString(), "UTF-8");
			request.setHeader("Authorization", "Bearer " + barrerToken);
			request.setHeader("Content-Type", "application/vnd.hedtech.integration.v16+json");
			request.setHeader("Accept", "application/vnd.hedtech.integration.v16+json");
			request.setHeader("Accept-Encoding", "UTF-8");
			request.setEntity(params);
			
			CloseableHttpResponse response = httpClient.execute(request);
			strError = strError + " | "+ response.getEntity().getContentType().getName();
			strError = strError + " | "+ response.getEntity().getContentType().getValue();
			strError = strError + " | "+ EntityUtils.toString(response.getEntity(), "UTF-8");
			
			try {
				strError = strError + " | " + response.getStatusLine()
			} catch (Exception e) {
				e.printStackTrace()
			}
			
			
			resultado.setError_info(strError);
			resultado.setSuccess(true);
			new LogDAO().insertTransactionLog("PUT", "CORRECTO", targetURL, "Log:"+strError, jsonInputString)
		} catch (Exception e) {
			resultado.setError_info(strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
			new LogDAO().insertTransactionLog("PUT", "FALLIDO", targetURL, "Log:"+strError, e.getMessage())
		}
		return resultado
	}
	/**
	 * Inserta o actualiza puntuacion de aspirantes en banner
	 * @author Juan Esquer
	 * @param fecha yyyy-MM-dd es la fecha del examen
	 * */
	public Result integracionBannerEthos(RestAPIContext context, String idBanner, String codeScore, String score, String fecha) {
		Result resultado = new Result();
		Boolean closeCon =false
		String errorLog = "";
		String barrerToken = "";
		String bannerMatchPerson =""
		String tokenUniversidad=""
		String tokenMatchPerson=""
		String resultPersonsCredentials =""
		String resultAptitudeAssessments=""
		String resultSources=""
		String resultStudentAptitudeAssessments=""
		String resultAssessmentSpecialCircumstances=""
		
		def jsonSlurper = new JsonSlurper();
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT ck.tokenbanner, (SELECT valor from catconfiguracion where clave='bannerMatchPerson' limit 1) as bannerMatchPerson  FROM catapikey ck inner join solicituddeadmision sda on sda.catcampus_pid=ck.campus_pid inner join detallesolicitud ds on ds.caseid::bigint=sda.caseid and ds.idbanner=? limit 1")
			pstm.setString(1, idBanner);
			rs = pstm.executeQuery();
			if(rs.next()) {
				barrerToken = rs.getString("tokenbanner")
				bannerMatchPerson = rs.getString("bannerMatchPerson")
			}
			
			if(barrerToken==null) {
				throw new Exception("La universidad no cuenta con token Banner Ethos")
			}
			tokenUniversidad = getBarreToken(barrerToken);
			tokenMatchPerson = getBarreToken(bannerMatchPerson)
			
			resultPersonsCredentials = personsCredentials(tokenMatchPerson, idBanner)
			def personsCredentials = jsonSlurper.parseText(resultPersonsCredentials)
			assert personsCredentials instanceof List<Map>;
			if(personsCredentials.size()==0) {
				throw new Exception("No se encontró aspirante con idBanner: " + idBanner)
			}
			
			resultAptitudeAssessments = aptitudeAssessments(tokenMatchPerson)
			def aptitudeAssessments = jsonSlurper.parseText(resultAptitudeAssessments)
			assert aptitudeAssessments instanceof List<Map>;
			
			/*resultAssessmentSpecialCircumstances = assessmentSpecialCircumstances(tokenMatchPerson)
			def assessmentSpecialCircumstances = jsonSlurper.parseText(resultAssessmentSpecialCircumstances)
			assert assessmentSpecialCircumstances instanceof List<Map>;*/
			
			/*resultSources = sources(tokenMatchPerson)
			def sources = jsonSlurper.parseText(resultSources)
			assert sources instanceof List<Map>;*/
			
			resultStudentAptitudeAssessments = studentAptitudeAssessments(tokenUniversidad, personsCredentials.get(0).id)
			def studentAptitudeAssessments = jsonSlurper.parseText(resultStudentAptitudeAssessments)
			assert studentAptitudeAssessments instanceof List<Map>;
			
			def aptitude = null
			for(int i=0; i<aptitudeAssessments.size();i++) {
				if(aptitudeAssessments.get(i).code.equals(codeScore)) {
					aptitude = aptitudeAssessments.get(i)
				}
			}
			
			Boolean update=false;
			String soatestId = "";
			/*String fecha=""
			
			pstm = con.prepareStatement("select to_char(now(), 'YYYY-MM-DD') fechahoraservidor")
			rs = pstm.executeQuery();
			if(rs.next()){
				fecha = rs.getString("fechahoraservidor")
			}*/
			
			errorLog += "| studentAptitudeAssessments.size()="+studentAptitudeAssessments.size()
			for(int i=0; i<studentAptitudeAssessments.size(); i++) {
				if(studentAptitudeAssessments.get(i).assessment.id==aptitude.id && studentAptitudeAssessments.get(i).assessedOn == fecha ) {
					soatestId = studentAptitudeAssessments.get(i).id
					//fecha=studentAptitudeAssessments.get(i).assessedOn
					update=true
				}
			}
			
			
			
			errorLog+="|barrerToken="+tokenUniversidad
			errorLog+="|studentId="+personsCredentials.get(0).id
			errorLog+="|assessmentId="+aptitude.id
			errorLog+="|score="+score+""
			errorLog+="|fecha="+fecha
			
			if(update) {
				updateStudentAptitudeAssessments(tokenUniversidad, personsCredentials.get(0).id, aptitude.id, Integer.parseInt(score+""), fecha,soatestId,codeScore.equals("MMPI"))
			}else {
				insertStudentAptitudeAssessments(tokenUniversidad, personsCredentials.get(0).id, aptitude.id, Integer.parseInt(score+""), fecha,codeScore.equals("MMPI"))
			}
			resultado.setSuccess(true);
			resultado.setError_info(errorLog)
			//resultadoGetConsumeJSON.setSuccess(true);
		} catch (Exception e) {
			errorLog += " | " + e.getMessage();
			e.printStackTrace()
			resultado.setSuccess(false)
			resultado.setError(e.getMessage())
			resultado.setError_info(errorLog)
		}finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}

		return resultado;
	}
	
	public Result integracionBannerEthosEAC(RestAPIContext context,String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String campus_pid = "";
		String campus2 = "";
		String campus3 = "";
		String errorLog = "";
		String barrerToken = "";
		String bannerMatchPerson =""
		String tokenUniversidad=""
		String tokenMatchPerson=""
		String resultPersonsCredentials =""
		String resultAptitudeAssessments=""
		String resultSources=""
		String resultStudentAptitudeAssessments=""
		String resultAssessmentSpecialCircumstances=""
		
		def jsonSlurper = new JsonSlurper();
		def str_studentAptitudeAssessments;
		def str_sources;
		def str_assessmentSpecialCircumstances ;
		def str_aptitudeAssessments;
		def str_personsCredentials;
		def aptitude = null;
		try {
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			
			object.each{
			   String fecha =  it.fechaExamen.substring(6, 10)+"-"+it.fechaExamen.substring(3, 5)+"-"+it.fechaExamen.substring(0, 2);

				pstm = con.prepareStatement("SELECT ck.tokenbanner, (SELECT valor from catconfiguracion where clave='bannerMatchPerson' limit 1) as bannerMatchPerson, sda.catcampus_pid FROM catapikey ck inner join solicituddeadmision sda on sda.catcampus_pid=ck.campus_pid inner join detallesolicitud ds on ds.caseid::bigint=sda.caseid and ds.IDBANNER=? limit 1")
				pstm.setString(1, it.IDBANNER);
				rs = pstm.executeQuery();
				if(rs.next()) {
					barrerToken = rs.getString("tokenbanner")
					bannerMatchPerson = rs.getString("bannerMatchPerson")
					campus_pid = rs.getString("catcampus_pid")
				}
				errorLog += "|IDBANNER : "+it.IDBANNER;
				if(barrerToken==null) {
					campus_pid = "0";
					errorLog += "La universidad no cuenta con token Banner Ethos ";
					//throw new Exception("La universidad no cuenta con token Banner Ethos")
				}
				errorLog += "1";
				if(!campus_pid.equals("0")) {
					Boolean update=false;
					String soatestId = "";
					Boolean existeIdBanner = true;
					errorLog += "2";
					if(!campus_pid.equals(campus2)) {
						errorLog += "3";
						tokenUniversidad = getBarreToken(barrerToken);
						tokenMatchPerson = getBarreToken(bannerMatchPerson)
						campus2 = campus_pid;
					}
					errorLog += "4";
					resultPersonsCredentials = personsCredentials(tokenMatchPerson, it.IDBANNER+"")
					str_personsCredentials = jsonSlurper.parseText(resultPersonsCredentials)
					assert str_personsCredentials instanceof List<Map>;
					errorLog += "5";
					if(str_personsCredentials.size()==0) {
						existeIdBanner = false;
						errorLog += ", No se encontró aspirante con IDBANNER: " + it.IDBANNER;
						//throw new Exception("No se encontró aspirante con it.IDBANNER: " + it.IDBANNER)
					}
					if(existeIdBanner) {
						errorLog += "6";
						if(!campus_pid.equals(campus3)) {
							errorLog += "7";
							resultAptitudeAssessments = aptitudeAssessments(tokenMatchPerson)
							str_aptitudeAssessments = jsonSlurper.parseText(resultAptitudeAssessments)
							assert str_aptitudeAssessments instanceof List<Map>;
							errorLog += "8";
							resultAssessmentSpecialCircumstances = assessmentSpecialCircumstances(tokenMatchPerson)
							str_assessmentSpecialCircumstances = jsonSlurper.parseText(resultAssessmentSpecialCircumstances)
							assert str_assessmentSpecialCircumstances instanceof List<Map>;
							errorLog += "9";
							resultSources = sources(tokenMatchPerson)
							str_sources = jsonSlurper.parseText(resultSources)
							assert str_sources instanceof List<Map>;
							errorLog += "10";
							campus3 = campus_pid;
						}
						errorLog += "11";
							
						resultStudentAptitudeAssessments = studentAptitudeAssessments(tokenUniversidad, str_personsCredentials.get(0).id)
						str_studentAptitudeAssessments = jsonSlurper.parseText(resultStudentAptitudeAssessments)
						assert str_studentAptitudeAssessments instanceof List<Map>;
						errorLog += "12";
						aptitude = null
						def titulos = null;
						if(it.tipoExamen.toString().equals("KP")){
							errorLog += "13";
						   titulos = ["PAAV","PAAN","PARA","MLEX","CLEX","HLEX","LA01","LA02","LA03","LA04","PG01","PG02","PG03","PG04","PG05","PV01","PV04","LEO1","LEO3","LEO4","LEO5","CIT1","CIT2","HI01","HI02","HI03","HI04","HI05","HI06"]
						}else{
						   titulos = ["PAAV","PAAN","PARA"]
						   errorLog += "14";
						}
						for(int i=0; i<str_aptitudeAssessments.size();i++) {
						   for(int j=0; j<titulos.size(); ++j){
							  if(str_aptitudeAssessments.get(i).code.equals(titulos[j])) {
								aptitude = str_aptitudeAssessments.get(i);

								errorLog += "| studentAptitudeAssessments.size()="+str_studentAptitudeAssessments.size()
								update=false;
								for(int e=0; i<str_studentAptitudeAssessments.size(); e++) {
									if(str_studentAptitudeAssessments.get(i).assessment.id==aptitude.id && str_studentAptitudeAssessments.get(e).assessedOn == fecha ) {
										soatestId = str_studentAptitudeAssessments.get(e).id
										update=true
									}
								}
								 
								errorLog+="|barrerToken="+tokenUniversidad
								errorLog+="|studentId="+str_personsCredentials.get(0).id
								errorLog+="|assessmentId="+aptitude.id
								errorLog+="|score="+it[titulos[i].replace('0','')+""]+""
								errorLog+="|specialCircumstancesId="+str_assessmentSpecialCircumstances.get(0).id
								errorLog+="|sourceId="+str_sources.get(0).id
								errorLog+="|fecha="+fecha
								
								/*if(update) {
								   updateStudentAptitudeAssessments(tokenUniversidad, str_personsCredentials.get(0).id, aptitude.id, Integer.parseInt(it[titulos[i].replace('0','')+""]+''), str_assessmentSpecialCircumstances.get(0).id, str_sources.get(0).id,fecha,soatestId,titulos[i].equals("MMPI"));
								}else {
								   insertStudentAptitudeAssessments(tokenUniversidad, str_personsCredentials.get(0).id, aptitude.id, Integer.parseInt(it[titulos[i].replace('0','')+""]+''), str_assessmentSpecialCircumstances.get(0).id, str_sources.get(0).id,fecha,soatestId,titulos[i].equals("MMPI"))
								}*/
							  }
						   }
							
						}
						
						
						
					}
				}
				
			}
			
			
			
			resultado.setSuccess(true);
			resultado.setError_info(errorLog)
			
			//resultadoGetConsumeJSON.setSuccess(true);
		} catch (Exception e) {
			errorLog += " | " + e.getMessage();
			e.printStackTrace()
			resultado.setSuccess(false)
			resultado.setError(e.getMessage())
			resultado.setError_info(errorLog)
		}finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}

		return resultado;
	}
	
	
}