
package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
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


import com.anahuac.catalogos.CatRegistro
import com.anahuac.catalogos.CatRegistroDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.model.PadresTutor
import com.anahuac.model.PadresTutorDAO
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.HubSpotData
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
	public Boolean validarConexion() {
		  Boolean retorno=false
		  if (con == null || con.isClosed()) {
				con = new DBConnect().getConnection();
				retorno=true
		  }
		  return retorno
	}
	
	public Result createOrUpdateRegistro(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		List<String> lstValueProperties = new ArrayList<String>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		String catLugarExamenDescripcion = "";
		String lugarExamen = "";
		String estadoExamen = "";
		String ciudadExamen ="";
		String apikeyHubspot ="";
		
		Date fecha = new Date();
		
		DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			
			strError = strError + " | try"
			
			if(lstCatRegistro != null) {
				
				strError = strError + " | object.email: "+object.email;
				
				strError = strError + " | lstSolicitudDeAdmision.size: "+lstSolicitudDeAdmision.size();
				strError = strError + " | lstSolicitudDeAdmision.empty: "+lstSolicitudDeAdmision.empty;
				strError = strError + " | lstCatRegistro.size: "+lstCatRegistro.size();
				strError = strError + " | lstCatRegistro.empty: "+lstCatRegistro.empty;
				resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
				apikeyHubspot = (String) resultadoApiKey.getData().get(0);
				
				if(!lstCatRegistro.empty && !lstSolicitudDeAdmision.empty) {
					
					resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					apikeyHubspot = (String) resultadoApiKey.getData().get(0);
					strError = strError + " | apikeyHubspot: "+apikeyHubspot;
					objHubSpotData = new HashMap<String, String>();
					objHubSpotData.put("campus_admision_bpm", lstSolicitudDeAdmision.get(0).getCatCampusEstudio().getClave());
					
					if(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave() != null) {
						strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave():------- "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
						if(!lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave().equals("")) {
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
//							lstValueProperties = getLstValueProperties("carrera");
							lstValueProperties = getLstValueProperties("carrera", apikeyHubspot);
							strError = strError + " | lstValueProperties.size() "+lstValueProperties.size();
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave())) {
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
								objHubSpotData.put("carrera", lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave());
							}
						}
					}
					
					if(lstSolicitudDeAdmision.get(0).getCatPropedeutico() != null) {
						strError = strError + " | tiene propedeutico";
						if(lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave() != null) {
							strError = strError + " | tiene clave";
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave();
							lstValueProperties = getLstValueProperties("periodo_propedeutico_bpm", apikeyHubspot);
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave())) {
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave();
								objHubSpotData.put("periodo_propedeutico_bpm", lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave());
							}
						}
					}
					
					if(lstSolicitudDeAdmision.get(0).getCatPeriodo() != null) {
						strError = strError + " | tiene periodo";
						if(lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave() != null) {
							strError = strError + " | tiene clave";
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave();
							lstValueProperties = getLstValueProperties("periodo_de_ingreso_bpm", apikeyHubspot);
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave())) {
								strError = strError + " | entro al if";
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave();
								objHubSpotData.put("periodo_de_ingreso_bpm", lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave());
							}
						}
					}
					
					
					
					
					
					
					catLugarExamenDescripcion = lstSolicitudDeAdmision.get(0).getCatLugarExamen().descripcion;
					
					if(catLugarExamenDescripcion.equals("En un estado")){
						lugarExamen = "México, "+lstSolicitudDeAdmision.get(0).getCatEstadoExamen().getDescripcion()+", "+lstSolicitudDeAdmision.get(0).getCiudadExamen().getDescripcion();
					}
					if(catLugarExamenDescripcion.equals("En el extranjero (solo si vives fuera de México)")){
						lugarExamen = lstSolicitudDeAdmision.get(0).getCatPaisExamen().getDescripcion()+", "+lstSolicitudDeAdmision.get(0).getCiudadExamenPais().getDescripcion();
					}
					if(catLugarExamenDescripcion.equals("En el mismo campus en donde realizaré mi licenciatura")){
						lugarExamen = lstSolicitudDeAdmision.get(0).getCatCampus().getDescripcion();
					}
					
					strError = strError + " | catLugarExamenDescripcion: "+catLugarExamenDescripcion;
					strError = strError + " | lugarExamen: "+lugarExamen;
					
					
					
										
					objHubSpotData.put("lugar_de_examen_bpm", lugarExamen);
					
					objHubSpotData.put("campus_vpd_bpm", lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					objHubSpotData.put("firstname", lstCatRegistro.get(0).getPrimernombre()+" "+(lstCatRegistro.get(0).getSegundonombre() == null ? "" : lstCatRegistro.get(0).getSegundonombre()));
					objHubSpotData.put("lastname", lstCatRegistro.get(0).getApellidopaterno()+" "+lstCatRegistro.get(0).getApellidomaterno());
					objHubSpotData.put("email", object.email);
					objHubSpotData.put("estatus_admision_bpm", "Registro");
					objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
					objHubSpotData.put("apoyo_ov_bpm", lstSolicitudDeAdmision.get(0).isNecesitoAyuda());
					//
					
					resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
				}
				else {
					strError = strError + " | ------------------------------------------";
					strError = strError + " | lstSolicitudDeAdmision.empty: "+lstSolicitudDeAdmision.empty;
					strError = strError + " | lstCatRegistro.empty: "+lstCatRegistro.empty;
					strError = strError + " | ------------------------------------------";
				}
				
			}
			
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateEnviada(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
	
		List < SolicitudDeAdmision > lstSolicitudDeAdmision = new ArrayList < SolicitudDeAdmision > ();
		List < PadresTutor > lstPadresTutor = new ArrayList < PadresTutor > ();
		List < CatRegistro > lstCatRegistro = new ArrayList < CatRegistro > ();
		List < String > lstValueProperties = new ArrayList < String > ();
	
		Map < String, String > objHubSpotData = new HashMap < String, String > ();
	
		String strError = "";
		String nombreCompleto = "";
		String catLugarExamenDescripcion = "";
		String lugarExamen = "";
		String apikeyHubspot = "";
		try {
			strError = strError + " | try 1";
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			assert object instanceof Map;
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def objPadresTutorDAO = context.apiClient.getDAO(PadresTutorDAO.class);
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
	
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			strError = strError + " | try 2";
			//			List<HubspotProperties> lstHubspotProperties = getLstHubspotProperties("importacion_estados");
			resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
			apikeyHubspot = (String) resultadoApiKey.getData().get(0);
			strError = strError + " | try 3";
			List < HubspotProperties > lstHubspotProperties = getLstHubspotProperties("importacion_estados", apikeyHubspot);
	
			//Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
			Date fechaNacimiento = new Date();
			Date fechaSC = new Date();
	
			DateFormat dfSalidaSC = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd");
			strError = strError + " | data";
			if (lstSolicitudDeAdmision != null) {
				strError = strError + " | !null";
				if (!lstSolicitudDeAdmision.empty) {
					strError = strError + " | !empty";
					resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					apikeyHubspot = (String) resultadoApiKey.getData().get(0);
					strError = strError + " | apikeyHubspot: " + apikeyHubspot;
					objHubSpotData = new HashMap < String, String > ();
					if (lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave() != null) {
						strError = strError + " | ";
						if (!lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave().equals("")) {
							strError = strError + " | !vacio";
							lstValueProperties = getLstValueProperties("carrera", apikeyHubspot);
							//strError = strError + " | "+lstValueProperties.join("--");
							if (lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave())) {
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): " + lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
								objHubSpotData.put("carrera", lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave());
								strError = strError + " | objHubSpotData: " + objHubSpotData.get("carrera");
							}
						}
					}
	
					if (lstSolicitudDeAdmision.get(0).getCatPropedeutico() != null) {
						strError = strError + " | tiene propedeutico";
						if (lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave() != null) {
							strError = strError + " | tiene clave";
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave(): " + lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave();
							lstValueProperties = getLstValueProperties("periodo_propedeutico_bpm", apikeyHubspot);
							if (lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave())) {
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave(): " + lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave();
								objHubSpotData.put("periodo_propedeutico_bpm", lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave());
							}
						}
					}
	
					if(lstSolicitudDeAdmision.get(0).getCatPeriodo() != null) {
						strError = strError + " | tiene periodo";
						if(lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave() != null) {
							strError = strError + " | tiene clave";
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave();
							lstValueProperties = getLstValueProperties("periodo_de_ingreso_bpm", apikeyHubspot);
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave())) {
								strError = strError + " | entro al if";
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave();
								objHubSpotData.put("periodo_de_ingreso_bpm", lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave());
							}
						}
					}
					
					if (lstSolicitudDeAdmision.get(0).getCatEstado() != null) {
						strError = strError + " | tiene estado";
						if(lstSolicitudDeAdmision.get(0).getCatEstado().getClave() != null) {
							strError = strError + " | tiene clave";
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatEstado().getClave(): "+lstSolicitudDeAdmision.get(0).getCatEstado().getClave();
							lstValueProperties = getLstValueProperties("importacion_estados", apikeyHubspot);
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatEstado().getClave())) {
								strError = strError + " | entro al if";
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatEstado().getClave(): "+lstSolicitudDeAdmision.get(0).getCatEstado().getClave();
								objHubSpotData.put("importacion_estados", lstSolicitudDeAdmision.get(0).getCatEstado().getClave());
							}
						}
					}
					
					lstPadresTutor = objPadresTutorDAO.findByCaseId(lstSolicitudDeAdmision.get(0).getCaseId(), 0, 999);
					if (lstPadresTutor != null) {
						if (!lstPadresTutor.empty) {
							for (PadresTutor objPadresTutor: lstPadresTutor) {
								if (objPadresTutor.isIsTutor()) {
									strError = strError + "| ENTRO A TUTOR"
									objHubSpotData.put("correo_tutor", objPadresTutor.getCorreoElectronico() == null ? "" : objPadresTutor.getCorreoElectronico());
									objHubSpotData.put("nombre_de_tutor", (objPadresTutor.getNombre() == null ? "" : objPadresTutor.getNombre()) + (objPadresTutor.getApellidos() == null ? "" : " " + objPadresTutor.getApellidos()));
									objHubSpotData.put("telefono_tutor", objPadresTutor.getTelefono());
	
								}
								if (objPadresTutor.getCatParentezco().getDescripcion().equals("Padre")) {
									objHubSpotData.put("nombre_del_padre", (objPadresTutor.getNombre() == null ? "" : objPadresTutor.getNombre()) + (objPadresTutor.getApellidos() == null ? "" : " " + objPadresTutor.getApellidos()));
									objHubSpotData.put("correo_del_padre", objPadresTutor.getCorreoElectronico());
									objHubSpotData.put("telefono_del_padre", objPadresTutor.getTelefono());
								}
								if (objPadresTutor.getCatParentezco().getDescripcion().equals("Madre")) {
									objHubSpotData.put("nombre_de_la_madre", (objPadresTutor.getNombre() == null ? "" : objPadresTutor.getNombre()) + (objPadresTutor.getApellidos() == null ? "" : " " + objPadresTutor.getApellidos()));
									objHubSpotData.put("correo_de_la_madre", objPadresTutor.getCorreoElectronico());
									objHubSpotData.put("telefono_de_la_madre", objPadresTutor.getTelefono());
								}
							}
						}
					}
	
					catLugarExamenDescripcion = lstSolicitudDeAdmision.get(0).getCatLugarExamen().descripcion;
	
					if (catLugarExamenDescripcion.equals("En un estado")) {
						lugarExamen = "México, " + (lstSolicitudDeAdmision.get(0).getCatEstadoExamen() == null ? "" : lstSolicitudDeAdmision.get(0).getCatEstadoExamen().getDescripcion() + ", ") + (lstSolicitudDeAdmision.get(0).getCiudadExamen() == null ? "" : lstSolicitudDeAdmision.get(0).getCiudadExamen().getDescripcion());
						strError = strError + " | lugarExamen: " + lugarExamen;
					}
					if (catLugarExamenDescripcion.equals("En el extranjero (solo si vives fuera de México)")) {
						lugarExamen = (lstSolicitudDeAdmision.get(0).getCatPaisExamen() == null ? "" : lstSolicitudDeAdmision.get(0).getCatPaisExamen().getDescripcion() + ", ") + (lstSolicitudDeAdmision.get(0).getCiudadExamenPais() == null ? "" : lstSolicitudDeAdmision.get(0).getCiudadExamenPais().getDescripcion());
						strError = strError + " | lugarExamen: " + lugarExamen;
					}
					if (catLugarExamenDescripcion.equals("En el mismo campus en donde realizaré mi licenciatura")) {
						lugarExamen = lstSolicitudDeAdmision.get(0).getCatCampus() == null ? "" : lstSolicitudDeAdmision.get(0).getCatCampus().getDescripcion();
						strError = strError + " | lugarExamen: " + lugarExamen;
					}
					//getLstValueProperties("importacion_estados")
					strError = strError + " | catLugarExamenDescripcion: " + catLugarExamenDescripcion;
					strError = strError + " | lugarExamen: " + lugarExamen;
	
					objHubSpotData.put("lugar_de_examen_bpm", lugarExamen);
					fechaNacimiento = Date.from(lstSolicitudDeAdmision.get(0).getFechaNacimiento().atZone(ZoneId.systemDefault()).toInstant());
	
					objHubSpotData.put("pais", lstSolicitudDeAdmision.get(0).getCatPais().descripcion);
					
					strError = strError + " | ----------------------------- ";
					
					objHubSpotData.put("campus_admision_bpm", lstSolicitudDeAdmision.get(0).getCatCampusEstudio().getClave());
					//objHubSpotData.put("periodo_de_ingreso_bpm", lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave());
					objHubSpotData.put("campus_vpd_bpm", lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					objHubSpotData.put("firstname", lstSolicitudDeAdmision.get(0).getPrimerNombre() + " " + (lstSolicitudDeAdmision.get(0).getSegundoNombre() == null ? "" : lstSolicitudDeAdmision.get(0).getSegundoNombre()));
					objHubSpotData.put("lastname", lstSolicitudDeAdmision.get(0).getApellidoPaterno() + " " + lstSolicitudDeAdmision.get(0).getApellidoMaterno());
					objHubSpotData.put("email", object.email);
					objHubSpotData.put("fecha_nacimiento_bpm", dfSalida.format(fechaNacimiento));
					//objHubSpotData.put("gender", lstSolicitudDeAdmision.get(0).getCatSexo().getClave() == null ? "" : lstSolicitudDeAdmision.get(0).getCatSexo().getClave());
					objHubSpotData.put("promedio_bpm", lstSolicitudDeAdmision.get(0).getPromedioGeneral() == null ? "" : lstSolicitudDeAdmision.get(0).getPromedioGeneral());
					objHubSpotData.put("estatus_admision_bpm", "Envío de solicitud");
	
					objHubSpotData.put("fecha_actualizacion_bpm", dfSalidaSC.format(fechaSC));
					//objHubSpotData.put("app_estatus_de_contacto", "Standby");
	
					if (lstSolicitudDeAdmision.get(0).getCatBachilleratos().getClave().equals("otro")) {
						objHubSpotData.put("preparatoria_bpm", lstSolicitudDeAdmision.get(0).getBachillerato());
					} else {
						objHubSpotData.put("preparatoria_bpm", lstSolicitudDeAdmision.get(0).getCatBachilleratos().getDescripcion());
					}
	
					
	
					objHubSpotData.put("municipio_bpm", lstSolicitudDeAdmision.get(0).getCiudad());
	
					/*if(lstHubspotProperties.contains(lstSolicitudDeAdmision.get(0).getCatEstado().getDescripcion())) {
						objHubSpotData.put("importacion_estados", lstSolicitudDeAdmision.get(0).getCatEstado().getClave());
					}*/
					objHubSpotData.put("phone", lstSolicitudDeAdmision.get(0).getTelefonoCelular());
	
	
					strError = strError + "| INFORMACION DE REGISTRO";
					strError = strError + "| GENDER-ZIP";
	
				
					resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
					//createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
					strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
				}
				else {
					strError = strError + " | empty";
				}
			}
			else {
				strError = strError + " | nulo";
			}
	
			resultado.setError_info(strError);
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: " + e.getMessage();
			resultado.setError_info(strError + " | " + (resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateModificar(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		String apikeyHubspot="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			
			Date fecha = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			if(lstCatRegistro != null) {
				if(!lstCatRegistro.empty) {
					resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					apikeyHubspot = (String) resultadoApiKey.getData().get(0);
					strError = strError + " | apikeyHubspot: "+apikeyHubspot;
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+lstCatRegistro.get(0).getCaseId();
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+(lstCatRegistro.get(0).getCaseId() == null ? "" : lstCatRegistro.get(0).getCaseId().toString());
					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);
					
					strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
					strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
					
					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							objHubSpotData.put("mensaje_bpm", lstDetalleSolicitud.get(0).getObservacionesCambio());
							objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
							objHubSpotData.put("estatus_admision_bpm", "Solicitud cambios");
							resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
							strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
						}
					}
					
				}
			}
			
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateValidar(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String residencia = "";
		String nombreCompleto = "";
		String tipoAdmision = "";
		String descuento = "";
		String catDescuento = "";
		String apikeyHubspot = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			
			
			Date fecha = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			if(lstCatRegistro != null) {
				if(!lstCatRegistro.empty) {
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+lstCatRegistro.get(0).getCaseId();
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+(lstCatRegistro.get(0).getCaseId() == null ? "" : lstCatRegistro.get(0).getCaseId().toString());
					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);
					lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
					strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
					strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
					
					
					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
							apikeyHubspot = (String) resultadoApiKey.getData().get(0);
							strError = strError + " | apikeyHubspot: "+apikeyHubspot;
							residencia = lstDetalleSolicitud.get(0).getCatResidencia().getClave().equals("F") ? "F" : (lstDetalleSolicitud.get(0).getCatResidencia().getClave().equals("R") ? "R" : "E");
							tipoAdmision = lstDetalleSolicitud.get(0).getCatTipoAdmision().getClave();
							
							strError = strError + " | residencia: "+residencia;
							strError = strError + " | tipoAdmision: "+tipoAdmision;
							strError = strError + " | getDescuento: "+lstDetalleSolicitud.get(0).getDescuento();
							
							descuento = ""+lstDetalleSolicitud.get(0).getDescuento();
							catDescuento = ""+(lstDetalleSolicitud.get(0).getCatDescuentos()== null ? "" : lstDetalleSolicitud.get(0).getCatDescuentos().getDescuento());
							
							strError = strError + " | descuento: "+descuento;
							strError = strError + " | catDescuento: "+catDescuento;
							
							objHubSpotData.put("tipo_de_alumno_bpm", lstDetalleSolicitud.get(0).getCatTipoAlumno().getClave());
							objHubSpotData.put("porcentaje_de_descuento_bpm", lstDetalleSolicitud.get(0).getDescuento()==null ? (lstDetalleSolicitud.get(0).getCatDescuentos() == null ? "0" : lstDetalleSolicitud.get(0).getCatDescuentos().getDescuento()):lstDetalleSolicitud.get(0).getDescuento());
							objHubSpotData.put("id_banner_bpm", lstDetalleSolicitud.get(0).getIdBanner());
							objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
							objHubSpotData.put("tipo_de_admision_bpm", tipoAdmision);
							objHubSpotData.put("residencia_bpm", residencia);
							objHubSpotData.put("estatus_admision_bpm", "Validado");
							
							if(lstSolicitudDeAdmision.get(0).getCatBachilleratos().getClave().equals("otro")) {
								objHubSpotData.put("preparatoria_bpm", lstSolicitudDeAdmision.get(0).getBachillerato());
							}
							else {
								objHubSpotData.put("preparatoria_bpm", lstSolicitudDeAdmision.get(0).getCatBachilleratos().getDescripcion());
							}
							
							resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
							strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
						}
					}
					
				}
			}
			
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateRechazoLRoja(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String mensaje = "";
		String estatus = "";
		String nombreCompleto = "";
		String apikeyHubspot = "";
		
		Boolean isRechazo=false;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			isRechazo=object.isRechazo;
			
			Date fecha = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			if(lstCatRegistro != null) {
				if(!lstCatRegistro.empty) {
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+lstCatRegistro.get(0).getCaseId();
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+(lstCatRegistro.get(0).getCaseId() == null ? "" : lstCatRegistro.get(0).getCaseId().toString());
					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);
					
					strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
					strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
					
					resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					apikeyHubspot = (String) resultadoApiKey.getData().get(0);
					strError = strError + " | apikeyHubspot: "+apikeyHubspot;
					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							if(isRechazo) {
								mensaje = lstDetalleSolicitud.get(0).getObservacionesRechazo() == null ? "" : lstDetalleSolicitud.get(0).getObservacionesRechazo();
								estatus = "Solicitud rechazada";
							}
							else {
								mensaje = lstDetalleSolicitud.get(0).getObservacionesListaRoja() == null ? "" : lstDetalleSolicitud.get(0).getObservacionesListaRoja();
								estatus = "Lista Roja";
							}
							objHubSpotData.put("mensaje_bpm", mensaje);
							objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
							objHubSpotData.put("estatus_admision_bpm", estatus);
							resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
							strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
						}
					}
					
				}
			}
			
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateRestaurarRechazoLRoja(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String apikeyHubspot = "";
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
			apikeyHubspot = (String) resultadoApiKey.getData().get(0);
			strError = strError + " | apikeyHubspot: "+apikeyHubspot;
			Date fecha = new Date();
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
			objHubSpotData.put("estatus_admision_bpm", "Envío de solicitud");
			resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
			strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
			
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdatePago(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoCDAO = new Result();
		Result resultadoApiKey = new Result();
				
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		List<Map<String, String>> lstOrderDetails = new ArrayList<Map<String, String>>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String mensaje = "";
		String estatus = "";
		String descuento = "";
		String catDescuento = "";
		String nombreCompleto = "";
		String apikeyHubspot = "";
		String jsonPago = "{\"order_id\":\"[ORDERID]\",\"campus_id\":\"[CAMPUSID]\"}";
		
		Date fechaConekta = new Date();
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			
			Date fecha = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			DateFormat dfSalidaHS = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat dfEntradaConekta = new SimpleDateFormat("dd/MM/yyyy");
			
			DecimalFormat df = new DecimalFormat("0");
			
			ConektaDAO cDAO = new ConektaDAO();
			
			NumberFormat formatNumber = NumberFormat.getCurrencyInstance();
			def pesoSigno = '$';
			if(lstCatRegistro != null) {
				if(!lstCatRegistro.empty) {
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+(lstCatRegistro.get(0).getCaseId() == null ? "" : lstCatRegistro.get(0).getCaseId().toString());
					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);
					
					strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
					strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();

					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							
							resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
							apikeyHubspot = (String) resultadoApiKey.getData().get(0);
							strError = strError + " | apikeyHubspot: "+apikeyHubspot;
							descuento = ""+lstDetalleSolicitud.get(0).getDescuento();
							catDescuento = ""+(lstDetalleSolicitud.get(0).getCatDescuentos()== null ? "" : lstDetalleSolicitud.get(0).getCatDescuentos().getDescuento());
							
							strError = strError + " | descuento: "+"descuento.toString()";
							strError = strError + " | getOrdenPago: "+lstDetalleSolicitud.get(0).getOrdenPago()//lstDetalleSolicitud.size()>0 ? (lstDetalleSolicitud.get(0).getOrdenPago() == null ? "NULO OP" : "lstDetalleSolicitud.get(0).getOrdenPago()") : "NULL";
							strError = strError + " | getCatCampus().getPersistenceId: " + lstSolicitudDeAdmision.get(0).getCatCampus().getPersistenceId()
							strError = strError + " | jsonPago: " +jsonPago.replace("[ORDERID]", String.valueOf(lstDetalleSolicitud.get(0).getOrdenPago())).replace("[CAMPUSID]", String.valueOf(lstSolicitudDeAdmision.get(0).getCatCampus().getPersistenceId()));
							if(lstDetalleSolicitud.get(0).getOrdenPago() == null || lstDetalleSolicitud.get(0).getOrdenPago() == "") {
								objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
								objHubSpotData.put("estatus_admision_bpm", "Pagó examen de admisión");
								resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
								strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
							}else {
								resultadoCDAO = cDAO.getOrderDetails(parameterP, parameterC, jsonPago.replace("[ORDERID]", String.valueOf(lstDetalleSolicitud.get(0).getOrdenPago())).replace("[CAMPUSID]", String.valueOf(lstSolicitudDeAdmision.get(0).getCatCampus().getPersistenceId())), context);
								if(resultadoCDAO.isSuccess()) {
									lstOrderDetails = (List<Map<String, String>>) resultadoCDAO.getData();
									strError = strError + " | speiBank: " +lstOrderDetails.get(0).get("speiBank");
									strError = strError + " | CLABE: " +lstOrderDetails.get(0).get("CLABE");
									strError = strError + " | amount: " +lstOrderDetails.get(0).get("amount");
									strError = strError + " | id: " +lstOrderDetails.get(0).get("id");
									strError = strError + " | createdAtDate: " +lstOrderDetails.get(0).get("createdAtDate");
									strError = strError + " | createdAtTime: " +lstOrderDetails.get(0).get("createdAtTime");
									strError = strError + " | type: " +lstOrderDetails.get(0).get("type");
									strError = strError + " | referencia: " +lstOrderDetails.get(0).get("referencia");
									strError = strError + " | cardNumber: " +lstOrderDetails.get(0).get("cardNumber");
									strError = strError + " | authorizationCode: " +lstOrderDetails.get(0).get("authorizationCode");
									strError = strError + " | name: " +lstOrderDetails.get(0).get("name");
									
									strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
									estatus = "Pagó examen de admisión";
									if(lstOrderDetails.get(0).get("createdAtDate") != null) {
										fechaConekta = dfEntradaConekta.parse(lstOrderDetails.get(0).get("createdAtDate"));
										objHubSpotData.put("monto_pago_bpm", dfSalida.format(fechaConekta));
									}
									if(lstOrderDetails.get(0).get("amount") != null) {
										Float monto=Float.parseFloat(lstOrderDetails.get(0).get("amount").toString().replace(pesoSigno, "").replace(" MXN", "").replace("MXN", ""));
										
										objHubSpotData.put("monto_pago_bpm", df.format(monto));
									}
									objHubSpotData.put("porcentaje_de_descuento_bpm", lstDetalleSolicitud.get(0).getDescuento()==null ? (lstDetalleSolicitud.get(0).getCatDescuentos() == null ? "0" : lstDetalleSolicitud.get(0).getCatDescuentos().getDescuento()):lstDetalleSolicitud.get(0).getDescuento());
									objHubSpotData.put("pago_examen_bpm", dfSalida.format(fecha));
									objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
									objHubSpotData.put("estatus_admision_bpm", estatus);
									resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
									strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
								}
								else {
									throw new Exception(resultadoCDAO.getError());
								}
							}
						}
					}
					
				}
			}

			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateAutodescripcion(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		String apikeyHubspot="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			
			Date fecha = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			if(lstCatRegistro != null) {
				if(!lstCatRegistro.empty) {
					resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					apikeyHubspot = (String) resultadoApiKey.getData().get(0);
					strError = strError + " | apikeyHubspot: "+apikeyHubspot;
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+lstCatRegistro.get(0).getCaseId();
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+(lstCatRegistro.get(0).getCaseId() == null ? "" : lstCatRegistro.get(0).getCaseId().toString());
					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);
					
					strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
					strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
					
					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
							objHubSpotData.put("estatus_admision_bpm", "Autodescripción");
							resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
							strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
						}
					}
					
				}
			}
			
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateGenerarCredencial(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		String apikeyHubspot="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			
			Date fecha = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			if(lstCatRegistro != null) {
				if(!lstCatRegistro.empty) {
					resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					apikeyHubspot = (String) resultadoApiKey.getData().get(0);
					strError = strError + " | apikeyHubspot: "+apikeyHubspot;
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+lstCatRegistro.get(0).getCaseId();
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+(lstCatRegistro.get(0).getCaseId() == null ? "" : lstCatRegistro.get(0).getCaseId().toString());
					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);
					
					strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
					strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
					
					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
							objHubSpotData.put("estatus_admision_bpm", "Credencial Generada");
							resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
							strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
						}
					}
					
				}
			}
			
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateEsperaResultado(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		String apikeyHubspot="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			
			Date fecha = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			if(lstCatRegistro != null) {
				if(!lstCatRegistro.empty) {
					resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					apikeyHubspot = (String) resultadoApiKey.getData().get(0);
					strError = strError + " | apikeyHubspot: "+apikeyHubspot;
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+lstCatRegistro.get(0).getCaseId();
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+(lstCatRegistro.get(0).getCaseId() == null ? "" : lstCatRegistro.get(0).getCaseId().toString());
					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);
					
					strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
					strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
					
					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
							objHubSpotData.put("estatus_admision_bpm", "En espera de resultados");
							resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
							strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
						}
					}
					
				}
			}
			
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateNoAsistioPruebas(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		String apikeyHubspot="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			
			Date fecha = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			if(lstCatRegistro != null) {
				if(!lstCatRegistro.empty) {
					resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					apikeyHubspot = (String) resultadoApiKey.getData().get(0);
					strError = strError + " | apikeyHubspot: "+apikeyHubspot;
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+lstCatRegistro.get(0).getCaseId();
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+(lstCatRegistro.get(0).getCaseId() == null ? "" : lstCatRegistro.get(0).getCaseId().toString());
					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);
					
					strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
					strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
					
					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
							objHubSpotData.put("estatus_admision_bpm", "No asistió a pruebas");
							resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
							strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
						}
					}
					
				}
			}
			
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			
			Result dataResult = new Result();
			dataResult = new TransferenciasDAO().GuardarFaltas(object.email);
			LOGGER.error "dataResult: "+dataResult.getError()
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	
	public Result createOrUpdateSeleccionoFechaExamen(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		Result resultadoFirstFecha = new Result();
		Result resultadoFechasSesiones = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		List<Map<String, Object>> lstResultadoFF = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lstResultadoFS = new ArrayList<Map<String, Object>>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		Map<String, Object> objResultadoFF = new HashMap<String, Object>();
		
		String strError = "";
		String nombreCompleto = "";
		String apikeyHubspot="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			
			Date fecha = new Date();
			Date fechaFF = null;
			Date fechaE = null;
			Date fechaAA = null;
			Date fechaPS = null;
			DateFormat dfSalidaNT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			DateFormat dfSalidaFF = new SimpleDateFormat("yyyy-MM-dd");
			dfSalida.setTimeZone(TimeZone.getTimeZone("GMT"));
			if(lstCatRegistro != null) {
				if(!lstCatRegistro.empty) {
					
					resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					apikeyHubspot = (String) resultadoApiKey.getData().get(0);
					
					resultadoFechasSesiones = new SesionesDAO().getDatosSesionUsername(object.email);
					strError = strError + " | resultadoFechasSesiones.getError_info: "+resultadoFechasSesiones.getError_info();
					lstResultadoFS = (List<Map<String, Object>>)resultadoFechasSesiones.getData();
					if(lstResultadoFS.size()>0) {
						for(Map<String, Object> fechassesiones : lstResultadoFS) {
							strError = strError + " " + fechassesiones.get("descripcion");
							strError = strError + " " + fechassesiones.get("aplicacion");
							String fechaobj = String.valueOf(fechassesiones.get("aplicacion"));
							strError = strError + " " + fechaobj;
							if(fechassesiones.get("descripcion").equals("Entrevista")) {
								fechaE = dfSalida.parse(fechaobj + " 00:00");
							}else if(fechassesiones.get("descripcion").equals("Examen de aptitudes y conocimientos")) {
								fechaAA = dfSalida.parse(fechaobj+ " 00:00");
							}else if(fechassesiones.get("descripcion").equals("Examen Psicométrico")) {
								fechaPS  = dfSalida.parse(fechaobj + " 00:00");
							}
						}
						
						strError = strError + " | fechaE " + fechaE + " | fechaAA " + fechaAA + " | fechaPSString " + fechaPS;			
					}
					
					
					strError = strError + " | apikeyHubspot: "+apikeyHubspot;
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+lstCatRegistro.get(0).getCaseId();
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+(lstCatRegistro.get(0).getCaseId() == null ? "" : lstCatRegistro.get(0).getCaseId().toString());
					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);
					
					strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
					strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
					
					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							objHubSpotData.put("fecha_actualizacion_bpm", dfSalidaNT.format(fecha));
							objHubSpotData.put("estatus_admision_bpm", "Seleccionó fecha de examen");
							/*if(fechaFF != null) {
								objHubSpotData.put("fecha_de_examen_bpm", fechaFF.getTime());
							}*/
							if(fechaE != null) {
								objHubSpotData.put("fecha_entrevista_bpm", fechaE.getTime());
							}
							if(fechaAA != null) {
								objHubSpotData.put("fecha_de_examen_bpm", fechaAA.getTime());
							}
							if(fechaPS != null) {
								objHubSpotData.put("fecha_psicometrico_bpm", fechaPS.getTime());
							}
							
							resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
							strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
						}
					}
				}
			}
			
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result getFirstFechaExamenByUsername(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		String errorlog="";
		String fechaFinalStr="";
		
		List<Map<String, Object>> lstResultado = new ArrayList<Map<String, Object>>();
		Map<String, Object> objResultado = new HashMap<String, Object>();
		
		Integer contador = 1;
		
		DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar calendario = new GregorianCalendar();
		try {
						
			closeCon = validarConexion();
						
			errorlog+="| consulta: "+Statements.GET_FIRST_FECHA_EXAMEN;
			pstm = con.prepareStatement(Statements.GET_FIRST_FECHA_EXAMEN);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			while(rs.next()) {
				fechaFinalStr = (rs.getString("fecha_inicio") == null ? "" : rs.getString("fecha_inicio"))+" "+(rs.getString("entrada")==null ? "" : rs.getString("entrada"));
				if(!fechaFinalStr.equals(" ")) {
					objResultado = new HashMap<String, Object>();
					calendario.setTime(dfSalida.parse(fechaFinalStr));
										
					objResultado.put("tiempo", calendario.getTime());
					objResultado.put("descripcion", rs.getString("descripcion"));
					lstResultado.add(objResultado);
				}
			}
			
			resultado.setError_info(errorlog);
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public List<String> getLstValueProperties(String campo, String apikeyHubspot) throws Exception {
		List<String> lstResultado = new ArrayList<String>();
		String jsonData = "";
//		String data="8b-.-.-.b0-.-.-.a-.-.-.1ac-df-.-.-.54-40-.-.-.bf-b5-.-.-.69-40-.-.-.e8-.-.-.7f-.-.-.90-.-.-.c0-.-.-.99";
//		String urlParaVisitar = "https://api.hubapi.com/properties/v1/contacts/properties/named/"+campo+"?hapikey="+data.replace("-.-.-.", "");
		String urlParaVisitar = "https://api.hubapi.com/properties/v1/contacts/properties/named/" + campo + "?hapikey=" + apikeyHubspot.replace("-.-.-.", "");

		StringBuilder resultado = new StringBuilder();
		
		URL url = new URL(urlParaVisitar);
		HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		conexion.setRequestMethod("GET");
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
		String urlParaVisitar = "https://api.hubapi.com/properties/v1/contacts/properties/named/" + campo + "?hapikey=" + apikeyHubspot.replace("-.-.-.", "");

		StringBuilder resultado = new StringBuilder();
		
		URL url = new URL(urlParaVisitar);
		HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		conexion.setRequestMethod("GET");
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
	
	public Result createOrUpdateUsuariosRegistrados(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		Result resultadoReplicarProperties = new Result();
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		List<String> lstValueProperties = new ArrayList<String>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		String catLugarExamenDescripcion = "";
		String estadoExamen = "";
		String ciudadExamen ="";
		String apikeyHubspot ="";
		String tipoAdmision = "";
		String residencia = "";
		String nombreUsuario = "";
		String correoElectronico = "";
		
		Date fecha = new Date();
		Date fechaNacimiento = new Date();
		
		DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			if(lstCatRegistro.size() <= 0) {
				lstCatRegistro = objCatRegistroDAO.findByNombreusuario(object.email, 0, 1);
			}
						
			strError = strError + " | try"
			
			if(lstCatRegistro != null) {
				lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(lstCatRegistro.get(0).getCaseId(), 0, 1);
				
				nombreUsuario = lstCatRegistro.get(0).getNombreusuario();
				correoElectronico = lstCatRegistro.get(0).getCorreoelectronico();
				
				strError = strError + " | object.email: "+object.email;
				strError = strError + " | lstSolicitudDeAdmision.size: "+lstSolicitudDeAdmision.size();
				strError = strError + " | lstSolicitudDeAdmision.empty: "+lstSolicitudDeAdmision.empty;
				strError = strError + " | lstCatRegistro.size: "+lstCatRegistro.size();
				strError = strError + " | lstCatRegistro.empty: "+lstCatRegistro.empty;
				resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
				apikeyHubspot = (String) resultadoApiKey.getData().get(0);
				
				strError = strError + " | nombreUsuario: " + nombreUsuario;
				strError = strError + " | correoElectronico: " + correoElectronico;
				
				if(!nombreUsuario.equals(correoElectronico)) {
					resultadoReplicarProperties = replicarProperties(nombreUsuario, correoElectronico, apikeyHubspot);
					strError = strError + " | replicar Data" + resultadoReplicarProperties.getError_info();
				} else {
					resultadoReplicarProperties = replicarProperties(object.email, nombreUsuario, apikeyHubspot);
					strError = strError + " | replicar Data else" + resultadoReplicarProperties.getError_info();
				}

				if(!lstCatRegistro.empty && !lstSolicitudDeAdmision.empty) {

					resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					apikeyHubspot = (String) resultadoApiKey.getData().get(0);
					strError = strError + " | apikeyHubspot: "+apikeyHubspot;

					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);

					objHubSpotData = new HashMap<String, String>();
					objHubSpotData.put("campus_admision_bpm", lstSolicitudDeAdmision.get(0).getCatCampusEstudio().getClave());

					if(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave() != null) {
						strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave():------- "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
						if(!lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave().equals("")) {
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
							lstValueProperties = getLstValueProperties("carrera", apikeyHubspot);
							strError = strError + " | lstValueProperties.size() "+lstValueProperties.size();
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave())) {
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
								objHubSpotData.put("carrera", lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave());
							}
						}
					}
					
					if(lstSolicitudDeAdmision.get(0).getCatPropedeutico() != null) {
						strError = strError + " | tiene propedeutico";
						if(lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave() != null) {
							strError = strError + " | tiene clave";
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave();
							lstValueProperties = getLstValueProperties("periodo_propedeutico_bpm", apikeyHubspot);
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave())) {
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave();
								objHubSpotData.put("periodo_propedeutico_bpm", lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave());
							}
						}
					}
					
					if(lstSolicitudDeAdmision.get(0).getCatPeriodo() != null) {
						strError = strError + " | tiene periodo";
						if(lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave() != null) {
							strError = strError + " | tiene clave";
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave();
							lstValueProperties = getLstValueProperties("periodo_de_ingreso_bpm", apikeyHubspot);
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave())) {
								strError = strError + " | entro al if";
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave();
								objHubSpotData.put("periodo_de_ingreso_bpm", lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave());
							}
						}
					}
	
					catLugarExamenDescripcion = lstSolicitudDeAdmision.get(0).getCatLugarExamen().descripcion;
										
					fechaNacimiento = Date.from(lstSolicitudDeAdmision.get(0).getFechaNacimiento().atZone(ZoneId.systemDefault()).toInstant());
					residencia = lstDetalleSolicitud.get(0).getCatResidencia().getClave().equals("F") ? "F" : (lstDetalleSolicitud.get(0).getCatResidencia().getClave().equals("R") ? "R" : "E");
					tipoAdmision = lstDetalleSolicitud.get(0).getCatTipoAdmision().getClave();
					
					strError = strError + " | catLugarExamenDescripcion: "+catLugarExamenDescripcion;
	
					objHubSpotData.put("firstname", lstSolicitudDeAdmision.get(0).getPrimerNombre()+" "+(lstSolicitudDeAdmision.get(0).getSegundoNombre() == null ? "" : lstSolicitudDeAdmision.get(0).getSegundoNombre()));
					objHubSpotData.put("lastname", lstSolicitudDeAdmision.get(0).getApellidoPaterno()+" "+lstSolicitudDeAdmision.get(0).getApellidoMaterno());
					objHubSpotData.put("email", lstSolicitudDeAdmision.get(0).getCorreoElectronico());
					//objHubSpotData.put("estatus_admision_bpm", "Registro");
					objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
					objHubSpotData.put("fecha_nacimiento_bpm", dfSalida.format(fechaNacimiento));
					objHubSpotData.put("promedio_bpm", lstSolicitudDeAdmision.get(0).getPromedioGeneral() == null ? "" : lstSolicitudDeAdmision.get(0).getPromedioGeneral());
					objHubSpotData.put("porcentaje_de_descuento_bpm", lstDetalleSolicitud.get(0).getDescuento()==null ? (lstDetalleSolicitud.get(0).getCatDescuentos() == null ? "0" : lstDetalleSolicitud.get(0).getCatDescuentos().getDescuento()):lstDetalleSolicitud.get(0).getDescuento());
					objHubSpotData.put("tipo_de_alumno_bpm", lstDetalleSolicitud.get(0).getCatTipoAlumno().getClave());
					objHubSpotData.put("tipo_de_admision_bpm", tipoAdmision);
					objHubSpotData.put("residencia_bpm", residencia);
					
					if (lstSolicitudDeAdmision.get(0).getCatBachilleratos().getClave().equals("otro")) {
						objHubSpotData.put("preparatoria_bpm", lstSolicitudDeAdmision.get(0).getBachillerato());
					} else {
						objHubSpotData.put("preparatoria_bpm", lstSolicitudDeAdmision.get(0).getCatBachilleratos().getDescripcion());
					}

					resultado = createOrUpdateHubspot(correoElectronico, apikeyHubspot, objHubSpotData);
				}
				else {
					strError = strError + " | ------------------------------------------";
					strError = strError + " | lstSolicitudDeAdmision.empty: "+lstSolicitudDeAdmision.empty;
					strError = strError + " | lstCatRegistro.empty: "+lstCatRegistro.empty;
					strError = strError + " | ------------------------------------------";
				}
				
			}
			
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));

		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateHubspot(String email, String apikeyHubspot, Map<String, String> objHubSpotData) {
		Result resultado = new Result();
		//String data="8b-.-.-.b0-.-.-.a-.-.-.1ac-df-.-.-.54-40-.-.-.bf-b5-.-.-.69-40-.-.-.e8-.-.-.7f-.-.-.90-.-.-.c0-.-.-.99"
		String targetURL = "https://api.hubapi.com/contacts/v1/contact/createOrUpdate/email/[EMAIL]/?hapikey="+apikeyHubspot;
		String jsonInputString = "{\"properties\":[{\"property\":\"firstname\",\"value\":\"java\"},{\"property\":\"nombre\",\"value\":\"Arturo\"},{\"property\":\"lastname\",\"value\":\"Zamorano\"},{\"property\":\"nombre_completo\",\"value\":\"Arturo Zamorano\"},{\"property\":\"correo_electrnico\",\"value\":\"jasz189@hotmail.com\"},{\"property\":\"date_of_birth\",\"value\":\"2020-11-30T23:51:03.309Z\"},{\"property\":\"fecha_de_nacimiento\",\"value\":\"654307200000\"},{\"property\":\"twitterhandle\",\"value\":\"arturoZCZ\"},{\"property\":\"gender\",\"value\":\"Masculino\"},{\"property\":\"country\",\"value\":\"México\"},{\"property\":\"state\",\"value\":\"Sonora\"},{\"property\":\"ciudad\",\"value\":\"Navojoa\"},{\"property\":\"city\",\"value\":\"Navojoa\"},{\"property\":\"address\",\"value\":\"Callejon 3\"},{\"property\":\"celular\",\"value\":\"6421344161\"},{\"property\":\"phone\",\"value\":\"6421344161\"},{\"property\":\"zip\",\"value\":\"85890\"},{\"property\":\"promedio\",\"value\":\"9.5\"},{\"property\":\"promedio_de_preparatoria\",\"value\":\"9.5\"},{\"property\":\"relationship_status\",\"value\":\"Casado\"},{\"property\":\"nombre_de_tutor\",\"value\":\"Arturo\"},{\"property\":\"apellido_tutor\",\"value\":\"Zamorano\"},{\"property\":\"celular_de_tutor\",\"value\":\"6421344161\"},{\"property\":\"correo_tutor\",\"value\":\"arturo.zamorano@gmail.com\"},{\"property\":\"telefono_tutor\",\"value\":\"6421344161\"},{\"property\":\"nombre_del_padre\",\"value\":\"Arturo\"},{\"property\":\"apellido_paterno\",\"value\":\"Zamorano\"},{\"property\":\"celular_del_padre\",\"value\":\"6421344161\"},{\"property\":\"correo_del_padre\",\"value\":\"arturo.zamorano@gmail.com\"},{\"property\":\"telefono_del_padre\",\"value\":\"6421344161\"},{\"property\":\"nombre_de_la_madre\",\"value\":\"Guadalupe\"},{\"property\":\"apellido_materno\",\"value\":\"Sainz\"},{\"property\":\"celular_de_la_madre\",\"value\":\"6421344161\"},{\"property\":\"correo_de_la_madre\",\"value\":\"eva.sainz@gmail.com\"},{\"property\":\"telefono_de_la_madre\",\"value\":\"6421344161\"},{\"property\":\"ua_vpd\",\"value\":\"UAM\"},{\"property\":\"campus_destino\",\"value\":\"AMAY\"},{\"property\":\"tipo_de_alumno_bpm\",\"value\":\"N\"}]}";
		String strError = "";
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		JSONObject jsonItem = new JSONObject();
		JSONObject jsonProperties = new JSONObject();
		JSONArray jsonList = new JSONArray();
		
		try {
			strError = strError + ", INICIO";
			strError = strError + "| ==============================================";
			strError = strError + "| apikeyHubspot: "+apikeyHubspot;
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
			request.setEntity(params);
			
			CloseableHttpResponse response = httpClient.execute(request);
			strError = strError + " | "+ response.getEntity().getContentType().getName();
			strError = strError + " | "+ response.getEntity().getContentType().getValue();
			strError = strError + " | "+ EntityUtils.toString(response.getEntity(), "UTF-8");
			
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
	
	public Result getApikeyHubspot(String claveCampus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String strError = "";

		List<String> lstResultado = new ArrayList<String>();

		try {
			  closeCon = validarConexion();
			  pstm = con.prepareStatement(Statements.GET_HUBSPOT_KEY_BY_CAMPUS)
			  pstm.setString(1, claveCampus)
			  rs = pstm.executeQuery()
			  if (rs.next()) {
					lstResultado.add(rs.getString("hubspotkey"));
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
		  resultado.setError_info(errorlog);
		  resultado.setSuccess(true);
		  resultado.setData(lstResultado);
	  } catch (Exception e) {
		  resultado.setSuccess(false);
		  resultado.setError(e.getMessage());
		  resultado.setError_info(errorlog)
	  }finally {
		  if(closeCon) {
			  new DBConnect().closeObj(con, stm, rs, pstm)
		  }
	  }
	  return resultado
  }
  
  public Result replicarProperties(String nombreUsuario, String correoElectronico, String apikeyHubspot) {
	  Result resultado = new Result();
	  List<String> lstNoHubspot = new ArrayList<String>();
	  
	  String jsonData = "";
	  String urlParaVisitar = "https://api.hubapi.com/contacts/v1/contact/email/[EMAIL]/profile?hapikey=[APIKEY]";
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
		  URL url = new URL(urlParaVisitar.replace("[EMAIL]", nombreUsuario).replace("[APIKEY]", apikeyHubspot));
		  HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		  conexion.setRequestMethod("GET");
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
		  resultado.setError_info(errorlog)
	  }
	  
	  return resultado;
  }
  
}

