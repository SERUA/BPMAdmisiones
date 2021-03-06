package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

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
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error "[ERROR]" + e.getMessage();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
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
		} catch (Exception e) {
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
		} catch (Exception e) {
			LOGGER.error "[ERROR]" + e.getMessage();
			e.printStackTrace();
		}
		return resultado.toString();
	}

	private Result getConsumeJSON(String jsonResultado, RestAPIContext context, String operacion, String barrerToken) {
		Result resultado = new Result();

		Integer indexAddress = null;

		JSONObject objJson = null;
		JSONObject objJsonAddressesData = null;
		JSONObject objJsonAddressExtended = null;
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
		JSONArray lstJson = null;

		JSONParser parser = new JSONParser();

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
											//{
												//"addressLines": [
													//"Av. Xalapa 73."
												//],
												//"id": "3b23dc9c-3cab-4919-9b16-9dcff1ad672f",
												//"place": {
													//"country": {
														//"code": "MEX",
														//"locality": "Veracruz",
														//"postalCode": "91130",
														//"postalTitle": "MEXICO",
														//"region": {
															//"title": "Veracruz"
														//},
														//"subRegion": {
															//"title": "Xalapa"
														//},
														//"title": "México"
													//}
												//},
												//"addressExtended": [
													//{
														//"streetLine1": "Av. Xalapa 73.",
														//"streetLine2": null,
														//"streetLine3": null,
														//"nationCode": "99",
														//"stateCode": "M30",
														//"countyCode": "30087"
													//}
												//]
											//}
											objJsonContent = (JSONObject) parser.parse(resultAddresses);
											
											objJsonPlace = (JSONObject) objJsonContent.get("place");
											lstAddressExtended = (JSONArray) objJsonContent.get("addressExtended");
											objJsonCountry = (JSONObject) objJsonPlace.get("country");
											objJsonRegion = (JSONObject) objJsonCountry.get("region");
					
											errorLog = errorLog + " | " + ("idDireccion: " + objJsonContent.get("id").toString());
											errorLog = errorLog + " | " + ("pais: " + objJsonCountry.get("title").toString());
											if(objJsonRegion != null) {
												errorLog = errorLog + " | " + ("Estado: " + objJsonRegion.get("title").toString());
											}
											errorLog = errorLog + " | " + ("ciudad: " + objJsonCountry.get("locality").toString());
											errorLog = errorLog + " | " + ("idDireccion: " + objJsonContent.get("id").toString());
											errorLog = errorLog + " | " + ("pais: " + (objJsonCountry.get("title")==null ? "" : objJsonCountry.get("title").toString()));
											if(objJsonRegion != null) {
												errorLog = errorLog + " | " + ("Estado: " + (objJsonRegion.get("title")==null ? "" : objJsonRegion.get("title").toString()));
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
						objJsonCountry = (JSONObject) objJsonPlace.get("country");
						objJsonRegion = (JSONObject) objJsonCountry.get("region");

						errorLog = errorLog + " | " + ("idDireccion: " + objJsonContent.get("id").toString());
						errorLog = errorLog + " | " + ("pais: " + (objJsonCountry.get("title")==null ? "" : objJsonCountry.get("title").toString()));
						if(objJsonRegion != null) {
							errorLog = errorLog + " | " + ("Estado: " + (objJsonRegion.get("title")==null ? "" : objJsonRegion.get("title").toString()));
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
						lstAddresses.add(objAddresses);
					break;
					default:
						errorLog = errorLog + " | " + (objJsonResource.get("name").toString());
					break;
				}

			}
			errorLog = errorLog + " | " + ("--------------------------------------------------------");

			/*for(CatBachillerato row : lstEducationalInstitutions) {
				objCatBachillerato= new CatBachillerato();
				objCatBachillerato.setIdBachillerato(row.getIdBachillerato());
				objCatBachillerato.setDescripcion(row.getDescripcion());
				objCatBachillerato.setUsuarioBanner(row.getUsuarioBanner());
				objCatBachillerato.setFechaImportacion(row.getFechaImportacion());
				objCatBachillerato.setFechaCreacion(row.getFechaCreacion());
				objCatBachillerato.setOperation(row.getOperation());
				objCatBachillerato.setClave(row.getClave());
				objCatBachillerato.setIsEliminado(false);
				objCatBachillerato.setIsEnabled(true);
				objCatBachillerato.setPerteneceRed(false);
				objCatBachillerato.setTypeInd(row.getTypeInd())
				
				objCatBachillerato.setStreetLine1(row.getStreetLine1())
				objCatBachillerato.setStreetLine2(row.getStreetLine2())
				objCatBachillerato.setStreetLine3(row.getStreetLine3())
				objCatBachillerato.setNationCode(row.getNationCode())
				objCatBachillerato.setStateCode(row.getStateCode())
				objCatBachillerato.setCountyCode(row.getCountyCode())
				objCatBachillerato.setIdDireccion(row.getIdDireccion());
		
				errorLog = errorLog + " | row.getIdBachillerato(): "+(row.getIdBachillerato());
				errorLog = errorLog + " | row.getDescripcion(): "+(row.getDescripcion());
				errorLog = errorLog + " | row.getUsuarioBanner(): "+(row.getUsuarioBanner());
				errorLog = errorLog + " | row.getFechaImportacion(): "+(row.getFechaImportacion());
				errorLog = errorLog + " | row.getFechaCreacion(): "+(row.getFechaCreacion());
				errorLog = errorLog + " | row.getOperation(): "+(row.getOperation());
				errorLog = errorLog + " | row.getClave(): "+(row.getClave());
				objCatBachillerato.setPais("");
				objCatBachillerato.setEstado("");
				objCatBachillerato.setCiudad("");
				lstCatBachillerato.add(objCatBachillerato);
			}*/

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
			def catBachilleratosDAO = context.getApiClient().getDAO(CatBachilleratosDAO.class);
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
								
								errorLog = errorLog + " | getStreetLine1: " + objRow.getStreetLine1()
								errorLog = errorLog + " | getStreetLine3: " + objRow.getStreetLine3()
								isMexicoOk = (isNationCodeOk && isStateCodeOk && isStreetLineOk);
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
				for (CatBachilleratos objRow: lstCatBachilleratos) {
					//if (!objRow.isIsEliminado()) {

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
					/*} else {

						errorLog = errorLog + " | PersistenceId:" + objRow.getPersistenceId();
						errorLog = errorLog + " | Descripcion:" + objRow.getDescripcion();
						errorLog = errorLog + " | is eliminado";
						errorLog = errorLog + " | ================================================================== | ";

					}*/
				}
				/*}
				else {
				  errorLog = errorLog + " | ELSE ADDRESSES";
				  errorLog = errorLog + " | objLstAddresses.getOperation():" + (objLstAddresses.getOperation());
				}*/
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
}