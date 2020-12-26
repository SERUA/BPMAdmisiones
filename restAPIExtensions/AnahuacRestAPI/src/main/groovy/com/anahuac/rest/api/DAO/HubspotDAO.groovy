package com.anahuac.rest.api.DAO

import java.text.DateFormat
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
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatRegistro
import com.anahuac.catalogos.CatRegistroDAO
import com.anahuac.model.PadresTutor
import com.anahuac.model.PadresTutorDAO
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.Entity.HubSpotData
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class HubspotDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(HubspotDAO.class);

	//def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
	public Result createOrUpdateRegistro(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1)
			LOGGER.error "lstCatRegistro";
			LOGGER.error "email: "+object.email;
			if(lstCatRegistro != null) {
				LOGGER.error "lstCatRegistro in join";
				if(!lstCatRegistro.empty) {
					LOGGER.error "size: "+lstCatRegistro.size();
					objHubSpotData = new HashMap<String, String>();
					objHubSpotData.put("nombre_completo", lstCatRegistro.get(0).getPrimernombre()+" "+(lstCatRegistro.get(0).getSegundonombre() == null ? "" : lstCatRegistro.get(0).getSegundonombre()+" ")+lstCatRegistro.get(0).getApellidopaterno()+" "+lstCatRegistro.get(0).getApellidomaterno());
					objHubSpotData.put("firstname", lstCatRegistro.get(0).getPrimernombre());
					objHubSpotData.put("lastname", lstCatRegistro.get(0).getApellidopaterno());
					objHubSpotData.put("nombre", lstCatRegistro.get(0).getPrimernombre());
					objHubSpotData.put("apellido_paterno", lstCatRegistro.get(0).getApellidopaterno());
					objHubSpotData.put("apellido_materno", lstCatRegistro.get(0).getApellidomaterno());
					objHubSpotData.put("tipo_de_alumno", "N");
					resultado = createOrUpdateHubspot(object.email, objHubSpotData);
				}
			}
			LOGGER.error "listo";
			resultado.setError_info(strError);
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateEnviada(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		List<PadresTutor> lstPadresTutor = new ArrayList<PadresTutor>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def objPadresTutorDAO = context.apiClient.getDAO(PadresTutorDAO.class);
			
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1)
			LOGGER.error "lstCatRegistro";
			LOGGER.error "email: "+object.email;
			
			//Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
			Date fechaNacimiento = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd");
			
			if(lstSolicitudDeAdmision != null) {
				LOGGER.error "lstCatRegistro in join";
				if(!lstSolicitudDeAdmision.empty) {
					
					objHubSpotData = new HashMap<String, String>();
					lstPadresTutor = objPadresTutorDAO.findByCaseId(lstSolicitudDeAdmision.get(0).getCaseId(), 0, 999);
					if(lstPadresTutor != null) {
						if(!lstPadresTutor.empty) {
							for(PadresTutor objPadresTutor : lstPadresTutor) {
								if(objPadresTutor.isIsTutor()) {
									strError = strError + ", ENTRO A TUTOR"
									objHubSpotData.put("apellido_tutor", objPadresTutor.getApellidos() == null ? "" : objPadresTutor.getApellidos());
									objHubSpotData.put("celular_de_tutor", objPadresTutor.getTelefono() == null ? "" : objPadresTutor.getTelefono());
									objHubSpotData.put("correo_tutor", objPadresTutor.getCorreoElectronico() == null ? "" : objPadresTutor.getCorreoElectronico());
									objHubSpotData.put("nombre_de_tutor", objPadresTutor.getNombre() == null ? "" : objPadresTutor.getNombre());
									objHubSpotData.put("telefono_tutor", objPadresTutor.getTelefono() == null ? "" : objPadresTutor.getTelefono());
								}
							}
						}
					}
					
					
					
					LOGGER.error "size: "+lstSolicitudDeAdmision.size();
					
					objHubSpotData.put("nombre_completo", lstSolicitudDeAdmision.get(0).getPrimerNombre()+" "+(lstSolicitudDeAdmision.get(0).getSegundoNombre() == null ? "" : lstSolicitudDeAdmision.get(0).getSegundoNombre()+" ")+lstSolicitudDeAdmision.get(0).getApellidoPaterno()+" "+lstSolicitudDeAdmision.get(0).getApellidoMaterno());
					objHubSpotData.put("firstname", lstSolicitudDeAdmision.get(0).getPrimerNombre() == null ? "" : lstSolicitudDeAdmision.get(0).getPrimerNombre());
					objHubSpotData.put("lastname", lstSolicitudDeAdmision.get(0).getApellidoPaterno() == null ? "" : lstSolicitudDeAdmision.get(0).getApellidoPaterno());
					objHubSpotData.put("nombre", lstSolicitudDeAdmision.get(0).getPrimerNombre() == null ? "" : lstSolicitudDeAdmision.get(0).getPrimerNombre());
					objHubSpotData.put("apellido_paterno", lstSolicitudDeAdmision.get(0).getApellidoPaterno() == null ? "" : lstSolicitudDeAdmision.get(0).getApellidoPaterno());
					objHubSpotData.put("apellido_materno", lstSolicitudDeAdmision.get(0).getApellidoMaterno() == null ? "" : lstSolicitudDeAdmision.get(0).getApellidoMaterno());
					objHubSpotData.put("tipo_de_alumno", "N");
					
					strError = strError + ", INFORMACION DE REGISTRO";
					
					//objHubSpotData.put("celular_de_la_madre", lstSolicitudDeAdmision.get(0).getPrimerNombre());
					//objHubSpotData.put("correo_de_la_madre", lstSolicitudDeAdmision.get(0).getPrimerNombre());
					//objHubSpotData.put("nombre_de_la_madre", lstSolicitudDeAdmision.get(0).getPrimerNombre());
					//objHubSpotData.put("telefono_de_la_madre", lstSolicitudDeAdmision.get(0).getPrimerNombre());
					
					//objHubSpotData.put("celular_del_padre", lstSolicitudDeAdmision.get(0).getPrimerNombre());
					//objHubSpotData.put("correo_del_padre", lstSolicitudDeAdmision.get(0).getPrimerNombre());
					//objHubSpotData.put("nombre_del_padre", lstSolicitudDeAdmision.get(0).getPrimerNombre());
					//objHubSpotData.put("telefono_del_padre", lstSolicitudDeAdmision.get(0).getPrimerNombre());
					
					objHubSpotData.put("address", lstSolicitudDeAdmision.get(0).getCalle() == null ? "" : lstSolicitudDeAdmision.get(0).getCalle());
					//objHubSpotData.put("campus_destino", lstSolicitudDeAdmision.get(0).getPrimerNombre());
					objHubSpotData.put("celular", lstSolicitudDeAdmision.get(0).getTelefonoCelular() == null ? "" : lstSolicitudDeAdmision.get(0).getTelefonoCelular());
					objHubSpotData.put("city", lstSolicitudDeAdmision.get(0).getCiudad() == null ? "" : lstSolicitudDeAdmision.get(0).getCiudad());
					objHubSpotData.put("ciudad", lstSolicitudDeAdmision.get(0).getCiudad() == null ? "" : lstSolicitudDeAdmision.get(0).getCiudad());
					objHubSpotData.put("correo_electrnico", lstSolicitudDeAdmision.get(0).getCorreoElectronico() == null ? "" : lstSolicitudDeAdmision.get(0).getCorreoElectronico());
					objHubSpotData.put("country", lstSolicitudDeAdmision.get(0).getCatPais().getDescripcion() == null ? "" : lstSolicitudDeAdmision.get(0).getCatPais().getDescripcion());
					strError = strError + ", ADDRESS-COUNTRY";
					/*STRING*/
					fechaNacimiento = Date.from(lstSolicitudDeAdmision.get(0).getFechaNacimiento().atZone(ZoneId.systemDefault()).toInstant());
					strError = strError + ", FECHA DE NACIMIENTO";
					objHubSpotData.put("date_of_birth", dfSalida.format(fechaNacimiento));
					strError = strError + ", FECHA DE NACIMIENTO FORMAT: "+dfSalida.format(fechaNacimiento);
					/*DATE*/
					//objHubSpotData.put("fecha_de_nacimiento", lstSolicitudDeAdmision.get(0).getPrimerNombre());
					objHubSpotData.put("gender", lstSolicitudDeAdmision.get(0).getCatSexo().getDescripcion() == null ? "" : lstSolicitudDeAdmision.get(0).getCatSexo().getDescripcion());
					objHubSpotData.put("phone", lstSolicitudDeAdmision.get(0).getTelefono() == null ? "" : lstSolicitudDeAdmision.get(0).getTelefono());
					objHubSpotData.put("promedio", lstSolicitudDeAdmision.get(0).getPromedioGeneral() == null ? "" : lstSolicitudDeAdmision.get(0).getPromedioGeneral());
					objHubSpotData.put("promedio_de_preparatoria", lstSolicitudDeAdmision.get(0).getPromedioGeneral() == null ? "" : lstSolicitudDeAdmision.get(0).getPromedioGeneral());
					objHubSpotData.put("relationship_status", lstSolicitudDeAdmision.get(0).getCatEstadoCivil().getDescripcion() == null ? "" : lstSolicitudDeAdmision.get(0).getCatEstadoCivil().getDescripcion());
					objHubSpotData.put("state", lstSolicitudDeAdmision.get(0).getCatEstado().getDescripcion() == null ? "" : lstSolicitudDeAdmision.get(0).getCatEstado().getDescripcion());
					objHubSpotData.put("twitterhandle", lstSolicitudDeAdmision.get(0).getUsiarioTwitter() == null ? "" : lstSolicitudDeAdmision.get(0).getUsiarioTwitter());
					//objHubSpotData.put("ua_vpd", lstSolicitudDeAdmision.get(0).getPrimerNombre());
					objHubSpotData.put("zip", lstSolicitudDeAdmision.get(0).getCodigoPostal() == null ? "" : lstSolicitudDeAdmision.get(0).getCodigoPostal());
					strError = strError + ", GENDER-ZIP";
										
					resultado = createOrUpdateHubspot(object.email, objHubSpotData);
					strError = strError + (resultado.getError_info() == NULL ? "NULL INFO" : "|" + resultado.getError_info() + "|"); 
				}
			}
			LOGGER.error "listo";
			//resultado.setError_info(strError);
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			//resultado.setError_info(strError);
			//resultado.setSuccess(false);
			//resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateHubspot(String email, Map<String, String> objHubSpotData) {
		Result resultado = new Result();
		String data="8b-.-.-.b0-.-.-.a-.-.-.1ac-df-.-.-.54-40-.-.-.bf-b5-.-.-.69-40-.-.-.e8-.-.-.7f-.-.-.90-.-.-.c0-.-.-.99"
		String targetURL = "https://api.hubapi.com/contacts/v1/contact/createOrUpdate/email/[EMAIL]/?hapikey="+data.replace("-.-.-.", "");
		String jsonInputString = "{\"properties\":[{\"property\":\"firstname\",\"value\":\"java\"},{\"property\":\"nombre\",\"value\":\"Arturo\"},{\"property\":\"lastname\",\"value\":\"Zamorano\"},{\"property\":\"nombre_completo\",\"value\":\"Arturo Zamorano\"},{\"property\":\"correo_electrnico\",\"value\":\"jasz189@hotmail.com\"},{\"property\":\"date_of_birth\",\"value\":\"2020-11-30T23:51:03.309Z\"},{\"property\":\"fecha_de_nacimiento\",\"value\":\"654307200000\"},{\"property\":\"twitterhandle\",\"value\":\"arturoZCZ\"},{\"property\":\"gender\",\"value\":\"Masculino\"},{\"property\":\"country\",\"value\":\"México\"},{\"property\":\"state\",\"value\":\"Sonora\"},{\"property\":\"ciudad\",\"value\":\"Navojoa\"},{\"property\":\"city\",\"value\":\"Navojoa\"},{\"property\":\"address\",\"value\":\"Callejon 3\"},{\"property\":\"celular\",\"value\":\"6421344161\"},{\"property\":\"phone\",\"value\":\"6421344161\"},{\"property\":\"zip\",\"value\":\"85890\"},{\"property\":\"promedio\",\"value\":\"9.5\"},{\"property\":\"promedio_de_preparatoria\",\"value\":\"9.5\"},{\"property\":\"relationship_status\",\"value\":\"Casado\"},{\"property\":\"nombre_de_tutor\",\"value\":\"Arturo\"},{\"property\":\"apellido_tutor\",\"value\":\"Zamorano\"},{\"property\":\"celular_de_tutor\",\"value\":\"6421344161\"},{\"property\":\"correo_tutor\",\"value\":\"arturo.zamorano@gmail.com\"},{\"property\":\"telefono_tutor\",\"value\":\"6421344161\"},{\"property\":\"nombre_del_padre\",\"value\":\"Arturo\"},{\"property\":\"apellido_paterno\",\"value\":\"Zamorano\"},{\"property\":\"celular_del_padre\",\"value\":\"6421344161\"},{\"property\":\"correo_del_padre\",\"value\":\"arturo.zamorano@gmail.com\"},{\"property\":\"telefono_del_padre\",\"value\":\"6421344161\"},{\"property\":\"nombre_de_la_madre\",\"value\":\"Guadalupe\"},{\"property\":\"apellido_materno\",\"value\":\"Sainz\"},{\"property\":\"celular_de_la_madre\",\"value\":\"6421344161\"},{\"property\":\"correo_de_la_madre\",\"value\":\"eva.sainz@gmail.com\"},{\"property\":\"telefono_de_la_madre\",\"value\":\"6421344161\"},{\"property\":\"ua_vpd\",\"value\":\"UAM\"},{\"property\":\"campus_destino\",\"value\":\"AMAY\"},{\"property\":\"tipo_de_alumno\",\"value\":\"N\"}]}";
		String strError = "";
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		JSONObject jsonItem = new JSONObject();
		JSONObject jsonProperties = new JSONObject();
		JSONArray jsonList = new JSONArray();
		
		try {
			strError = strError + ", INICIO";
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
			strError = strError + ", "+jsonProperties.toString();
			LOGGER.error jsonList.toString();
			LOGGER.error jsonProperties.toString();
			strError = strError + ", EMAIL: "+email;
			targetURL = targetURL.replace("[EMAIL]", email);
			HttpPost request = new HttpPost(targetURL);
			StringEntity params = new StringEntity(jsonProperties.toString(), "UTF-8");
			request.setHeader("content-type", "application/json");
			request.setHeader("Accept-Encoding", "UTF-8");
			request.setEntity(params);
			
			CloseableHttpResponse response = httpClient.execute(request);
			strError = strError + ", "+ response.getEntity().getContentType().getName();
			strError = strError + ", "+ response.getEntity().getContentType().getValue();
			strError = strError + ", "+ EntityUtils.toString(response.getEntity(), "UTF-8");
			
			resultado.setError_info(strError);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setError_info(strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
}
