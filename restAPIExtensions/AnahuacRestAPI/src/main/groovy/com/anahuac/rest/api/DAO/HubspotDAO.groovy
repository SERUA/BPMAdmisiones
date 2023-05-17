
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
	Map<String,String> estatusMap = new HashMap<String, String>() {{put("Registro","Registro");
        put("Solicitud enviada","Envío de solicitud");
        put("Solicitud a modificar","Solicitud cambios");
        put("Solicitud rechazada","Solicitud rechazada");
        put("Lista Roja","Lista Roja");
        put("Solicitud en espera de pago","Validado");
        put("Autodescripción en proceso","Pagó examen de admisión");
        put("Autodescripción concluida","Autodescripción");
        put("Elección de pruebas calendarizado","Seleccionó fecha de examen");
        put("Ya se imprimió su credencial","Credencial Generada");
        put("Solicitud recibida","Envío de solicitud");
        put("Solicitud en proceso","Registro");
        put("Aspirantes registrados con validación de cuenta","Registro");
        put("Carga y consulta de resultados","Credencial Generada");
        put("Solicitud en espera de pago","Validado");
        put("Validación pago condonado","Pagó examen de admisión");
        put("Validación descuento 100%","Pagó examen de admisión");
        put("Autodescripción de proceso","Pagó examen de admisión");
        put("Carga y consulta de resultados","En espera de resultados");
        put("Resultado final del comité","En espera de resultados");
        put("Rechazado por comité","En espera de resultados");}};
	
	Map<String,String> estatusMapBecas = new HashMap<String, String>() {{
		put("Esperando Pre-Autorización", "Envío de solicitud beca");
		put("Correcciones realizadas", "Envío de solicitud beca");
		put("En espera de resultado", "Validado");
		put("Solicitud Rechazada", "Rechazo solicitud beca");
		put("Solicitud no autorizada", "Rechazo solicitud beca");
		put("Solicitud rechazada por comité de finanzas", "Rechazo solicitud beca");
		put("Solicitud rechazada por finanzas", "Rechazo solicitud beca");
		put("Pre-autorización solicita modificaciones", "Solicitud cambios");
		put("En espera de pago", "En espera de pago");
		put("En espera de validación de pago", "En espera de pago");
		put("En espera de autorización", "En espera de resultado de beca");
		put("Solicitud autorizada", "Solicitud de beca autorizada");
	}};

	Map<String,String> mapTipoBecas = new HashMap<String, String>() {{
		put("Beca artística", "Artística");
		put("Beca deportiva", "Deportiva");
		put("Beca académica", "Académica");
		put("Beca SEP(promedio mínimo 9.0)", "Académica");
		put("Convenio", "Académica");
		put("Beca de excelencia", "Académica");
		put("Convenio", "Académica");
		
	}};

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
		Boolean closeCon = false;
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
		Boolean nfcarrera = false;
		Boolean nffp = false;
		Boolean nffi = false;
		String msjNF = "";

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
					//strError = strError + " | apikeyHubspot: "+apikeyHubspot;
					objHubSpotData = new HashMap<String, String>();
					objHubSpotData.put("campus_admision_bpm", lstSolicitudDeAdmision.get(0).getCatCampusEstudio().getClave());
					
					if(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave() != null) {
						strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave():------- "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
						if(!lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave().equals("")) {
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
//							lstValueProperties = getLstValueProperties("carrera");
							lstValueProperties = getLstValueProperties2("carrera", apikeyHubspot,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
							strError = strError + " | lstValueProperties.size() "+lstValueProperties.size();
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave())) {
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
								objHubSpotData.put("carrera", lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave());
							}else{
								nfcarrera = true;
								msjNF = "No se encontro la propiedad carrera";
							}
						}
					}
					
					if(lstSolicitudDeAdmision.get(0).getCatPropedeutico() != null) {
						strError = strError + " | tiene propedeutico";
						if(lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave() != null) {
							strError = strError + " | tiene clave";
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave();
							lstValueProperties = getLstValueProperties2("periodo_propedeutico_bpm", apikeyHubspot,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave())) {
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave();
								objHubSpotData.put("periodo_propedeutico_bpm", lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave());
							}else{
								nffp = true;
								if(nfcarrera){
									msjNF = msjNF + ", periodo_propedeutico_bpm";	
								}else{
									msjNF = "No se encontro la propiedad periodo_propedeutico_bpm";	
								}
							}
						}
					}
					
					if(lstSolicitudDeAdmision.get(0).getCatPeriodo() != null) {
						strError = strError + " | tiene periodo";
						if(lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave() != null) {
							strError = strError + " | tiene clave";
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave();
							lstValueProperties = getLstValueProperties2("periodo_de_ingreso_bpm", apikeyHubspot,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave())) {
								strError = strError + " | entro al if";
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave();
								objHubSpotData.put("periodo_de_ingreso_bpm", lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave());
							}else{
								nffi = true;
								if(nfcarrera || nffp){
									msjNF = msjNF + ", periodo_de_ingreso_bpm";	
								}else{
									msjNF = "No se encontro la propiedad periodo_de_ingreso_bpm";	
								}
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
					objHubSpotData.put("phone", lstCatRegistro.get(0).getNumeroContacto());
					
					resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
				}
				else {
					strError = strError + " | ------------------------------------------";
					strError = strError + " | lstSolicitudDeAdmision.empty: "+lstSolicitudDeAdmision.empty;
					strError = strError + " | lstCatRegistro.empty: "+lstCatRegistro.empty;
					strError = strError + " | ------------------------------------------";
				}
				
			}
			
			if (!resultado.success) {
				//def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				String correo = "";
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_CORREO_BY_CLAVE)
				pstm.setString(1, "EmailRegistro")
				rs = pstm.executeQuery()
				while(rs.next()) {
					correo = rs.getString("valor");
				}
			
				//List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailRegistro",0,1)
				MailGunDAO mgd = new MailGunDAO();
				Result correoenviado =mgd.sendEmailPlantilla(correo, "Hubspot Registro Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context)
				strError += strError + correoenviado.isSuccess().toString() + " | " + correoenviado.getInfo();
				//resultado.error+="|email:"+correo+"|response:"+mgd.sendEmailPlantilla(correo, "Hubspot Registro Error", resultado.getError(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context).getData().get(0)
			}

			if (nfcarrera || nffp || nffi) {
				//def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				String correo = "";
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_CORREO_BY_CLAVE)
				pstm.setString(1, "EmailRegistro")
				rs = pstm.executeQuery()
				while(rs.next()) {
					correo = rs.getString("valor");
				}
				//List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailRegistro",0,1)
				msjNF += " en el catalogo de HubSpot"
				MailGunDAO mgd = new MailGunDAO();
				Result correoenviado = mgd.sendEmailPlantilla(correo, "Hubspot Registro Error - Propiedad no encotrada", msjNF + "<br><br>" + resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context)
				
				strError += strError + correoenviado.isSuccess().toString() + " | " + correoenviado.getInfo();
			}
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
			new LogDAO().insertTransactionLog("POST", "FALLIDO", "Error inesperado", "Log:"+strError, e.getMessage())
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado
	}
	
	public Result createOrUpdateEnviada(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		Boolean closeCon=false;
		
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
		Boolean nfcarrera = false;
		Boolean nffp = false;
		Boolean nffi = false;
		String msjNF = "";
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
			List < HubspotProperties > lstHubspotProperties = getLstHubspotProperties2("importacion_estados", apikeyHubspot,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
	
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
					//strError = strError + " | apikeyHubspot: " + apikeyHubspot;
					objHubSpotData = new HashMap < String, String > ();
					if (lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave() != null) {
						strError = strError + " | ";
						if (!lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave().equals("")) {
							strError = strError + " | !vacio";
							lstValueProperties = getLstValueProperties2("carrera", apikeyHubspot,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
							//strError = strError + " | "+lstValueProperties.join("--");
							if (lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave())) {
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): " + lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
								objHubSpotData.put("carrera", lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave());
								strError = strError + " | objHubSpotData: " + objHubSpotData.get("carrera");
							}else{
								nfcarrera = true;
								msjNF = "No se encontro la propiedad carrera";
							}
						}
					}
	
					if (lstSolicitudDeAdmision.get(0).getCatPropedeutico() != null) {
						strError = strError + " | tiene propedeutico";
						if (lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave() != null) {
							strError = strError + " | tiene clave";
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave(): " + lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave();
							lstValueProperties = getLstValueProperties2("periodo_propedeutico_bpm", apikeyHubspot,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
							if (lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave())) {
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave(): " + lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave();
								objHubSpotData.put("periodo_propedeutico_bpm", lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave());
							}else{
								nffp = true;
								if(nfcarrera){
									msjNF = msjNF + ", periodo_propedeutico_bpm";	
								}else{
									msjNF = "No se encontro la propiedad periodo_propedeutico_bpm";	
								}
							}
						}
					}
	
					if(lstSolicitudDeAdmision.get(0).getCatPeriodo() != null) {
						strError = strError + " | tiene periodo";
						if(lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave() != null) {
							strError = strError + " | tiene clave";
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave();
							lstValueProperties = getLstValueProperties2("periodo_de_ingreso_bpm", apikeyHubspot,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave())) {
								strError = strError + " | entro al if";
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave();
								objHubSpotData.put("periodo_de_ingreso_bpm", lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave());
							}else{
								nffi = true;
								if(nfcarrera || nffp){
									msjNF = msjNF + ", periodo_de_ingreso_bpm";	
								}else{
									msjNF = "No se encontro la propiedad periodo_de_ingreso_bpm";	
								}
							}
						}
					}
					
					if (lstSolicitudDeAdmision.get(0).getCatEstado() != null) {
						strError = strError + " | tiene estado";
						if(lstSolicitudDeAdmision.get(0).getCatEstado().getClave() != null) {
							strError = strError + " | tiene clave";
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatEstado().getClave(): "+lstSolicitudDeAdmision.get(0).getCatEstado().getClave();
							lstValueProperties = getLstValueProperties2("importacion_estados", apikeyHubspot,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
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
	
					objHubSpotData.put("pais", lstSolicitudDeAdmision.get(0).getCatPais().getDescripcion())
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
					//AQUI EMPIEZA LO QUE HIZO JUSQUER
					closeCon = validarConexion()
					List<Map<String, Object>> lstContactoEmergencias = new ArrayList<Map<String, Object>>();
					pstm = con.prepareStatement("SELECT * FROM contactoEmergencias where caseid=?");
					pstm.setLong(1, lstSolicitudDeAdmision.get(0).getCaseId())
					rs= pstm.executeQuery();
					
					
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();
					
					while(rs.next()) {
						Map<String, Object> columns = new LinkedHashMap<String, Object>();
		
						for (int i = 1; i <= columnCount; i++) {
							columns.put(metaData.getColumnLabel(i), rs.getString(i));
						}
						lstContactoEmergencias.add(columns)
					}
					for(Map<String,Object> contactoEmergencias:lstContactoEmergencias) {
						objHubSpotData.put("nombre_contacto_emergencia_bpm", contactoEmergencias.get("nombre"));
						objHubSpotData.put("numero_contacto_emergencia_bpm", contactoEmergencias.get("telefonocelular"));
					}	
					objHubSpotData.put("genero_bpm",lstSolicitudDeAdmision.get(0).getCatSexo().clave)
					//AQUI TERMINO LO QUE HA AGREGADO JUSQUER
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
			
			/*if (nfcarrera || nffp || nffi) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailRegistro",0,1)
				MailGunDAO mgd = new MailGunDAO();
				resultado.error+="|email:"+config.get(0).getValor()+"|response:"+mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot Registro Error - Propiedad no encotrada", msjNF + "<br>" + resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context).getData().get(0)
			}

			if (!resultado.success) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailEnviada",0,1)
				MailGunDAO mgd = new MailGunDAO();
				mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot Enviada Error", resultado.getError(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context);
			}*/
			
			if (!resultado.success) {
				//def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				String correo = "";
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_CORREO_BY_CLAVE)
				pstm.setString(1, "EmailEnviada")
				rs = pstm.executeQuery()
				while(rs.next()) {
					correo = rs.getString("valor");
				}
			
				//List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailRegistro",0,1)
				MailGunDAO mgd = new MailGunDAO();
				Result correoenviado =mgd.sendEmailPlantilla(correo, "Hubspot Solicitud enviada Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context)
				strError += strError + correoenviado.isSuccess().toString() + " | " + correoenviado.getInfo();
				//resultado.error+="|email:"+correo+"|response:"+mgd.sendEmailPlantilla(correo, "Hubspot Registro Error", resultado.getError(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context).getData().get(0)
			}

			if (nfcarrera || nffp || nffi) {
				//def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				String correo = "";
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_CORREO_BY_CLAVE)
				pstm.setString(1, "EmailEnviada")
				rs = pstm.executeQuery()
				while(rs.next()) {
					correo = rs.getString("valor");
				}
				//List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailRegistro",0,1)
				msjNF += " en el catalogo de HubSpot"
				MailGunDAO mgd = new MailGunDAO();
				Result correoenviado = mgd.sendEmailPlantilla(correo, "Hubspot Solicitud enviada Error - Propiedad no encotrada", msjNF + "<br><br>" + resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context)
				
				strError += strError + correoenviado.isSuccess().toString() + " | " + correoenviado.getInfo();
			}
			resultado.setError_info(strError);
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: " + e.getMessage();
			resultado.setError_info(strError + " | " + (resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
			new LogDAO().insertTransactionLog("POST", "FALLIDO", "Error inesperado", "Log:"+strError, e.getMessage())
		}
		finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
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
					//strError = strError + " | apikeyHubspot: "+apikeyHubspot;
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
			if (!resultado.success) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailModificar",0,1)
				MailGunDAO mgd = new MailGunDAO();
				mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot Modificar Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context);
			}
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
			new LogDAO().insertTransactionLog("POST", "FALLIDO", "Error inesperado", "Log:"+strError, e.getMessage())
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
							//strError = strError + " | apikeyHubspot: "+apikeyHubspot;
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
			
			if (!resultado.success) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailValidar",0,1)
				MailGunDAO mgd = new MailGunDAO();
				mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot Validar Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context);
			}
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
			new LogDAO().insertTransactionLog("POST", "FALLIDO", "Error inesperado", "Log:"+strError, e.getMessage())
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
					//strError = strError + " | apikeyHubspot: "+apikeyHubspot;
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
			if (!resultado.success) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailRechazoLRoja",0,1)
				MailGunDAO mgd = new MailGunDAO();
				mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot Rechazo Lista Roja Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context);
			}
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
			//strError = strError + " | apikeyHubspot: "+apikeyHubspot;
			Date fecha = new Date();
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
			objHubSpotData.put("estatus_admision_bpm", "Envío de solicitud");
			resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
			strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
			
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			if (!resultado.success) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailRestaurarRechazoLRoja",0,1)
				MailGunDAO mgd = new MailGunDAO();
				mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot Restaurar Rechazo Lista Roja Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context);
			}
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
							//strError = strError + " | apikeyHubspot: "+apikeyHubspot;
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
			if (!resultado.success) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailPago",0,1)
				MailGunDAO mgd = new MailGunDAO();
				mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot Pago Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context);
			}
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
					//strError = strError + " | apikeyHubspot: "+apikeyHubspot;
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
			if (!resultado.success) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailAutodescripcion",0,1)
				MailGunDAO mgd = new MailGunDAO();
				mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot Autodescripción Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context);
			}
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
					//strError = strError + " | apikeyHubspot: "+apikeyHubspot;
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
			if (!resultado.success) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailGenerarCredencial",0,1)
				MailGunDAO mgd = new MailGunDAO();
				mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot Generar Credencial Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context);
			}
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
					//strError = strError + " | apikeyHubspot: "+apikeyHubspot;
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
			if (!resultado.success) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailEsperaResultado",0,1)
				MailGunDAO mgd = new MailGunDAO();
				mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot Espera de resultado Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context);
			}
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
					//strError = strError + " | apikeyHubspot: "+apikeyHubspot;
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
			LOGGER.error "dataResult: "+dataResult.getError();
			
			dataResult = new Result();
			dataResult = new TransferenciasDAO().ejecutarGenerarCredencial(object.email, context);
			
			LOGGER.error "dataResult: "+dataResult.getError();
			if (!resultado.success) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailNoAsistioPruebas",0,1)
				MailGunDAO mgd = new MailGunDAO();
				mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot No Asistio Pruebas Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context);
			}
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
					
					
					//strError = strError + " | apikeyHubspot: "+apikeyHubspot;
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
			if (!resultado.success) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailSeleccionoFechaExamen",0,1)
				MailGunDAO mgd = new MailGunDAO();
				mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot Selecciono Fecha Examen Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context);
			}
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
		Boolean nfcarrera = false;
		Boolean nffp = false;
		Boolean nffi = false;
		Boolean closeCon = false;
		String msjNF = "";
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
					//strError = strError + " | apikeyHubspot: "+apikeyHubspot;

					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);

					objHubSpotData = new HashMap<String, String>();
					objHubSpotData.put("campus_admision_bpm", lstSolicitudDeAdmision.get(0).getCatCampusEstudio().getClave());

					if(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave() != null) {
						strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave():------- "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
						if(!lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave().equals("")) {
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
							lstValueProperties = getLstValueProperties2("carrera", apikeyHubspot,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
							strError = strError + " | lstValueProperties.size() "+lstValueProperties.size();
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave())) {
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
								objHubSpotData.put("carrera", lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave());
							}else{
								nfcarrera = true;
								msjNF = "No se encontro la propiedad carrera";
							}
						}
					}
					
					if(lstSolicitudDeAdmision.get(0).getCatPropedeutico() != null) {
						strError = strError + " | tiene propedeutico";
						if(lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave() != null) {
							strError = strError + " | tiene clave";
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave();
							lstValueProperties = getLstValueProperties2("periodo_propedeutico_bpm", apikeyHubspot,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave())) {
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave();
								objHubSpotData.put("periodo_propedeutico_bpm", lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave());
							}else{
								nffp = true;
								if(nfcarrera){
									msjNF = msjNF + ", periodo_propedeutico_bpm";	
								}else{
									msjNF = "No se encontro la propiedad periodo_propedeutico_bpm";	
								}
							}
						}
					}
					
					if(lstSolicitudDeAdmision.get(0).getCatPeriodo() != null) {
						strError = strError + " | tiene periodo";
						if(lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave() != null) {
							strError = strError + " | tiene clave";
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave();
							lstValueProperties = getLstValueProperties2("periodo_de_ingreso_bpm", apikeyHubspot,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave())) {
								strError = strError + " | entro al if";
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave();
								objHubSpotData.put("periodo_de_ingreso_bpm", lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave());
							}else{
								nffi = true;
								if(nfcarrera || nffp){
									msjNF = msjNF + ", periodo_de_ingreso_bpm";	
								}else{
									msjNF = "No se encontro la propiedad periodo_de_ingreso_bpm";	
								}
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
					
//					objHubSpotData.put("promedio_bpm", lstSolicitudDeAdmision.get(0).getPromedioGeneral() == null ? "" : lstSolicitudDeAdmision.get(0).getPromedioGeneral());
					objHubSpotData.put("porcentaje_de_descuento_bpm", lstDetalleSolicitud.get(0).getDescuento()==null ? (lstDetalleSolicitud.get(0).getCatDescuentos() == null ? "0" : lstDetalleSolicitud.get(0).getCatDescuentos().getDescuento()):lstDetalleSolicitud.get(0).getDescuento());
					objHubSpotData.put("tipo_de_alumno_bpm", lstDetalleSolicitud.get(0).getCatTipoAlumno().getClave());
					objHubSpotData.put("tipo_de_admision_bpm", tipoAdmision);
					objHubSpotData.put("residencia_bpm", residencia);
					
					if (lstSolicitudDeAdmision.get(0).getCatBachilleratos().getClave().toLowerCase().equals("otro")) {
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
			
			/*if (nfcarrera || nffp || nffi) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailRegistro",0,1)
				MailGunDAO mgd = new MailGunDAO();
				resultado.error+="|email:"+config.get(0).getValor()+"|response:"+mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot Registro Error - Propiedad no encotrada", msjNF + "<br>" + resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context).getData().get(0)
			}

			if (!resultado.success) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailUsuarioRegistrado",0,1)
				MailGunDAO mgd = new MailGunDAO();
				mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot Usuario Registrado Error", resultado.getError(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context);
			}*/
			
			if (!resultado.success) {
				//def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				String correo = "";
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_CORREO_BY_CLAVE)
				pstm.setString(1, "EmailRegistro")
				rs = pstm.executeQuery()
				while(rs.next()) {
					correo = rs.getString("valor");
				}
			
				//List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailRegistro",0,1)
				MailGunDAO mgd = new MailGunDAO();
				Result correoenviado =mgd.sendEmailPlantilla(correo, "Hubspot Registro Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context)
				strError += strError + correoenviado.isSuccess().toString() + " | " + correoenviado.getInfo();
				//resultado.error+="|email:"+correo+"|response:"+mgd.sendEmailPlantilla(correo, "Hubspot Registro Error", resultado.getError(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context).getData().get(0)
			}

			if (nfcarrera || nffp || nffi) {
				//def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				String correo = "";
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_CORREO_BY_CLAVE)
				pstm.setString(1, "EmailRegistro")
				rs = pstm.executeQuery()
				while(rs.next()) {
					correo = rs.getString("valor");
				}
				//List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailRegistro",0,1)
				msjNF += " en el catalogo de HubSpot"
				MailGunDAO mgd = new MailGunDAO();
				Result correoenviado = mgd.sendEmailPlantilla(correo, "Hubspot Registro Error - Propiedad no encotrada", msjNF + "<br><br>" + resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context)
				
				strError += strError + correoenviado.isSuccess().toString() + " | " + correoenviado.getInfo();
			}
			
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));

		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
			new LogDAO().insertTransactionLog("POST", "FALLIDO", "Error inesperado", "Log:"+strError, e.getMessage())
		}
		return resultado
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

//		String data="8b-.-.-.b0-.-.-.a-.-.-.1ac-df-.-.-.54-40-.-.-.bf-b5-.-.-.69-40-.-.-.e8-.-.-.7f-.-.-.90-.-.-.c0-.-.-.99"
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
  public Result createOrUpdateUsuarioRegistrado(String jsonData) {
	  Result resultado = new Result();
	  Result resultadoApiKey = new Result();

	  List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
	  List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
	  List<String> lstValueProperties = new ArrayList<String>();
	  
	  Map<String, String> objHubSpotData = new HashMap<String, String>();
	  Boolean closeCon = false;
	  String strError = "";
	  String nombreCompleto = "";
	  String catLugarExamenDescripcion = "";
	  String lugarExamen = "";
	  String estadoExamen = "";
	  String ciudadExamen ="";
	  String apikeyHubspot ="";
	  
	  Boolean nfcarrera = false;
	  Boolean nffp = false;
	  Boolean nffi = false;
	  String msjNF = "";
	  
	  Date fecha = new Date();
	  
	  DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	  DateFormat dfSalidaFN = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      Date fechaCreacion = new Date();
      DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
	  
	  try {
		  def jsonSlurper = new JsonSlurper();
		  def object = jsonSlurper.parseText(jsonData);
		  
		  assert object instanceof Map;
		  
		  resultadoApiKey = getApikeyHubspot(object.catCampus.clave);
		  strError+="apikeyhubspot";
		  apikeyHubspot = (String) resultadoApiKey.getData().get(0);
		  strError+="si apikeyhubspot";
		  
		  objHubSpotData.put("firstname", object.primernombre + (object.segundonombre?.toString().trim()==""?"":" "+object.segundonombre?.toString().trim()) );
		  objHubSpotData.put("lastname", object.apellidopaterno + (object.apellidomaterno?.toString().trim()==""?"":" "+object.apellidomaterno?.toString().trim()) );
		  objHubSpotData.put("campus_vpd_bpm", object.catCampus.clave);
		  objHubSpotData.put("email", object.correoelectronico);
		  objHubSpotData.put("campus_admision_bpm",object.catCampusEstudio.clave);
		  
		  if(object.catGestionEscolar != null) {
			  if(!object.catGestionEscolar.clave.equals("")) {
				  lstValueProperties = getLstValueProperties2("carrera", apikeyHubspot,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
				  strError = strError + " | lstValueProperties.size() "+lstValueProperties.size();
				  if(lstValueProperties.contains(object.catGestionEscolar.clave)) {
					  objHubSpotData.put("carrera", object.catGestionEscolar.clave);
				  }else{
					nfcarrera = true;
					msjNF = "No se encontro la propiedad carrera";
				}
			  }
		  }
		  
		  //objHubSpotData.put("carrera",object.catGestionEscolar.clave);
		  
		  if(object.catPeriodo != null) {
			  lstValueProperties = getLstValueProperties2("periodo_de_ingreso_bpm", apikeyHubspot,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
			  strError+= " | lstValueProperties:"+lstValueProperties
			  if(lstValueProperties.contains(object.catPeriodo.clave)) {
				  strError = strError + " | entro al if";
				  strError = strError + " | object.catPeriodo.clave: "+object.catPeriodo.clave;
				  objHubSpotData.put("periodo_de_ingreso_bpm", object.catPeriodo.clave);
			  }
		  }
		  
		  
		  //objHubSpotData.put("periodo_de_ingreso_bpm",object.catPeriodo.clave);
		  
		  if(object.catPropedeutico !=null && object.propedeutico != null) {
			  lstValueProperties = getLstValueProperties2("periodo_propedeutico_bpm", apikeyHubspot,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
			  if(lstValueProperties.contains(object.catPropedeutico.clave)) {
				  objHubSpotData.put("periodo_propedeutico_bpm", object.catPropedeutico.clave);
			  }else{
					nffp = true;
				if(nfcarrera){
					msjNF = msjNF + ", periodo_propedeutico_bpm";	
				}else{
					msjNF = "No se encontro la propiedad periodo_propedeutico_bpm";	
				}
			}
		  }
		  
		 /* if(object.catPropedeutico !=null && object.propedeutico != null) {
			  objHubSpotData.put("periodo_propedeutico_bpm",object.catPropedeutico.clave);
		  }else {
			  objHubSpotData.put("periodo_propedeutico_bpm","");
		  }*/
		  
		  objHubSpotData.put("genero_bpm",object.catSexo.clave);
		  // formateo de la fecha
		  fechaCreacion = dfSalidaFN.parse(object.fechanacimiento.toString().replace("t","T"))
		  
		  objHubSpotData.put("fecha_nacimiento_bpm",dformat.format(fechaCreacion));
		  objHubSpotData.put("promedio_bpm",object.promedio);
		  if (object.catBachilleratos.clave.toLowerCase().equals("otro")) {
			  objHubSpotData.put("preparatoria_bpm", object.nombrebachillerato);
		  } else {
			  objHubSpotData.put("preparatoria_bpm", object.catBachilleratos.descripcion);
		  }
		  objHubSpotData.put("residencia_bpm",object.catResidencia?.clave);
		  objHubSpotData.put("tipo_de_alumno_bpm",object.catTipoAlumno?.clave);
		  objHubSpotData.put("tipo_de_admision_bpm",object.catTipoAdmision?.clave);
		  objHubSpotData.put("lugar_de_examen_bpm",object.catLugarExamen?.descripcion);
		  objHubSpotData.put("porcentaje_de_descuento_bpm",object.descuento == null? "": object.descuento);
		  objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
		  objHubSpotData.put("email", object.correoelectronico);
		  objHubSpotData.put("phone", object.telefonoCelular);
		  
		  
		  resultado = createOrUpdateHubspot(object.correoelectronico, apikeyHubspot, objHubSpotData);
		  strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
		  
		  if (!resultado.success) {
			  //def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
			  String correo = "";
			  closeCon = validarConexion();
			  pstm = con.prepareStatement(Statements.GET_CORREO_BY_CLAVE)
			  pstm.setString(1, "EmailRegistro")
			  rs = pstm.executeQuery()
			  while(rs.next()) {
				  correo = rs.getString("valor");
			  }
		  
			  //List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailRegistro",0,1)
			  MailGunDAO mgd = new MailGunDAO();
			  Result correoenviado =mgd.sendEmailPlantilla(correo, "Hubspot Usuario registrado Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context)
			  strError += strError + correoenviado.isSuccess().toString() + " | " + correoenviado.getInfo();
			  //resultado.error+="|email:"+correo+"|response:"+mgd.sendEmailPlantilla(correo, "Hubspot Registro Error", resultado.getError(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context).getData().get(0)
		  }

		  if (nfcarrera || nffp || nffi) {
			  //def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
			  String correo = "";
			  closeCon = validarConexion();
			  pstm = con.prepareStatement(Statements.GET_CORREO_BY_CLAVE)
			  pstm.setString(1, "EmailRegistro")
			  rs = pstm.executeQuery()
			  while(rs.next()) {
				  correo = rs.getString("valor");
			  }
			  //List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailRegistro",0,1)
			  msjNF += " en el catalogo de HubSpot"
			  MailGunDAO mgd = new MailGunDAO();
			  Result correoenviado = mgd.sendEmailPlantilla(correo, "Hubspot Usuario registrado Error - Propiedad no encotrada", msjNF + "<br><br>" + resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context)
			  
			  strError += strError + correoenviado.isSuccess().toString() + " | " + correoenviado.getInfo();
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
 public Result createOrUpdateTransferirAspirante(String valorcambio, String valororginal, String correoElectronico,  RestAPIContext context) {
      Result resultado = new Result();
      Result resultadoApiKeyCambio = new Result();
      Result resultadoApiKeyOriginal = new Result();
      Result resultadoCDAO = new Result();
      
      List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
      List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
      List < PadresTutor > lstPadresTutor = new ArrayList < PadresTutor > ();
      List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
      List<Map<String, String>> lstOrderDetails = new ArrayList<Map<String, String>>();
      
      List<String> lstValueProperties = new ArrayList<String>();
      
      Map<String, String> objHubSpotData = new HashMap<String, String>();
      List<Map<String, String>> data = new ArrayList<Map<String, String>>();
      String strError = "";
      String nombreCompleto = "";
      String catLugarExamenDescripcion = "";
      String lugarExamen = "";
      String estadoExamen = "";
      String ciudadExamen ="";
      String apikeyHubspotCambio ="";
      String apikeyHubspotOriginal ="";
      String residencia = "";
      String tipoAdmision = "";
      String descuento = "";
      String catDescuento = ""
      String claveCambio=""
      String claveOriginal=""
      String jsonPago = "{\"order_id\":\"[ORDERID]\",\"campus_id\":\"[CAMPUSID]\"}";
      Boolean closeCon=false
      Date fecha = new Date();
      Date fechaNacimiento = new Date();
      DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      dfSalida.setTimeZone(TimeZone.getTimeZone("America/Mexico_City"));
      DateFormat dfSalidaFN = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      dfSalidaFN.setTimeZone(TimeZone.getTimeZone("America/Mexico_City"));
      Date fechaCreacion = new Date();
      DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
      DateFormat dfEntradaConekta = new SimpleDateFormat("dd/MM/yyyy");
      Date fechaConekta = new Date();
      ConektaDAO cDAO = new ConektaDAO();
      DecimalFormat df = new DecimalFormat("0");
	  Boolean nfcarrera = false;
	  Boolean nffp = false;
	  Boolean nffi = false;
	  String msjNF = "";
      try {
          def jsonSlurper = new JsonSlurper();
          def objectCambio = jsonSlurper.parseText(valorcambio);
          def objectOriginal = jsonSlurper.parseText(valororginal);
          
          assert objectCambio instanceof Map;
          assert objectOriginal instanceof Map;
          closeCon = validarConexion()
          String consulta="SELECT clave FROM catcampus where descripcion=? and iseliminado=false"
          pstm=con.prepareStatement(consulta)
          pstm.setString(1, objectCambio.campus)
          rs=pstm.executeQuery()
          if(rs.next()) {
              claveCambio=rs.getString("clave")
          }
          
          pstm=con.prepareStatement(consulta)
          pstm.setString(1, objectOriginal.campus)
          rs=pstm.executeQuery()
          if(rs.next()) {
              claveOriginal=rs.getString("clave")
          }
          strError +=" | 1._ clave campos por descripcion = " + claveCambio + ", " + claveOriginal
          
          resultadoApiKeyCambio = getApikeyHubspot(claveCambio);
          apikeyHubspotCambio = (String) resultadoApiKeyCambio.getData().get(0);
          
          resultadoApiKeyOriginal = getApikeyHubspot(claveOriginal);
          apikeyHubspotOriginal = (String) resultadoApiKeyOriginal.getData().get(0);
          
          objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
          objHubSpotData.put("fecha_transferencia_bpm",dfSalida.format(fecha));
          objHubSpotData.put("origen_vpd_bpm", claveOriginal);
          objHubSpotData.put("destino_vpd_bpm", claveCambio);
          objHubSpotData.put("estatus_admision_bpm","Transferencia a otro campus")
          strError += "| 2._ Hubspot original " + objHubSpotData.toString()
          resultado = createOrUpdateHubspot(correoElectronico, apikeyHubspotOriginal, objHubSpotData);
          
          def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
          def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
          def objPadresTutorDAO = context.apiClient.getDAO(PadresTutorDAO.class);
          def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
          
          lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(correoElectronico, 0, 1);
          lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(correoElectronico, 0, 1);
          
          if(lstCatRegistro != null) {
              //TODO
              Date fechaFF = null;
              Date fechaE = null;
              Date fechaAA = null;
              Date fechaPS = null;
              DateFormat dfSalidaNT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
              
              DateFormat dfSalidaFF = new SimpleDateFormat("yyyy-MM-dd");
              dfSalida.setTimeZone(TimeZone.getTimeZone("GMT"));
              strError += "| 3._ lstCatRegistro"
                
                strError = strError + " | lstSolicitudDeAdmision.size: "+lstSolicitudDeAdmision.size();
                strError = strError + " | lstSolicitudDeAdmision.empty: "+lstSolicitudDeAdmision.empty;
                strError = strError + " | lstCatRegistro.size: "+lstCatRegistro.size();
                strError = strError + " | lstCatRegistro.empty: "+lstCatRegistro.empty;

                
                if(!lstCatRegistro.empty && !lstSolicitudDeAdmision.empty) {
                    
                    //AQUI EMPIEZA LO QUE HIZO JUSQUER
                    List<Map<String, Object>> lstContactoEmergencias = new ArrayList<Map<String, Object>>();
                    pstm = con.prepareStatement("SELECT * FROM contactoEmergencias where caseid=?");
                    pstm.setLong(1, lstSolicitudDeAdmision.get(0).getCaseId())
                    rs= pstm.executeQuery();
                    
                    
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    
                    while(rs.next()) {
                        Map<String, Object> columns = new LinkedHashMap<String, Object>();
        
                        for (int i = 1; i <= columnCount; i++) {
                            columns.put(metaData.getColumnLabel(i), rs.getString(i));
                        }
                        lstContactoEmergencias.add(columns)
                    }
                    for(Map<String,Object> contactoEmergencias:lstContactoEmergencias) {
                        objHubSpotData.put("nombre_contacto_emergencia_bpm", contactoEmergencias.get("nombre"));
                        objHubSpotData.put("numero_contacto_emergencia_bpm", contactoEmergencias.get("telefonocelular"));
                    }
                    objHubSpotData.put("genero_bpm",lstSolicitudDeAdmision.get(0).getCatSexo().clave)
                    if(lstSolicitudDeAdmision.get(0).getEstatusSolicitud().equals("Transferido")) {
                        pstm = con.prepareStatement("SELECT * from CATBITACORATRANSFERENCIAS where correoaspirante=? order by persistenceid desc limit 1  ");
                        pstm.setString(1, lstSolicitudDeAdmision.get(0).getCorreoElectronico())
                        rs= pstm.executeQuery();
                        if(rs.next()) {
                            objHubSpotData.put("estatus_admision_bpm",estatusMap.get(rs.getString("estatus")))
                            pstm = con.prepareStatement("UPDATE solicituddeadmision set estatussolicitud=? where correoelectronico=?")
                            pstm.setString(1,rs.getString("estatus"))
                            pstm.setString(2,lstSolicitudDeAdmision.get(0).getCorreoElectronico())
                            pstm.executeUpdate()
                        }
                    }else {
                        objHubSpotData.put("estatus_admision_bpm",estatusMap.get(lstSolicitudDeAdmision.get(0).getEstatusSolicitud()))
                    }
                    
                    Result resultadoFirstFecha = new Result();
                    Result resultadoFechasSesiones = new Result();
                    List<Map<String, Object>> lstResultadoFF = new ArrayList<Map<String, Object>>();
                    List<Map<String, Object>> lstResultadoFS = new ArrayList<Map<String, Object>>();
                    resultadoFechasSesiones = new SesionesDAO().getDatosSesionUsername(correoElectronico);
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
                        if(fechaE != null) {
                            objHubSpotData.put("fecha_entrevista_bpm", fechaE.getTime());
                        }
                        if(fechaAA != null) {
                            objHubSpotData.put("fecha_de_examen_bpm", fechaAA.getTime());
                        }
                        if(fechaPS != null) {
                            objHubSpotData.put("fecha_psicometrico_bpm", fechaPS.getTime());
                        }
                        strError = strError + " | fechaE " + fechaE + " | fechaAA " + fechaAA + " | fechaPSString " + fechaPS;
                    }
                    
                    //AQUI TERMINO LO QUE HA AGREGADO JUSQUER
                    objHubSpotData.put("campus_admision_bpm", lstSolicitudDeAdmision.get(0).getCatCampusEstudio().getClave());
                    
                    if(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave() != null) {
                        strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave():------- "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
                        if(!lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave().equals("")) {
                            strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
//                          lstValueProperties = getLstValueProperties("carrera");
                            lstValueProperties = getLstValueProperties2("carrera", apikeyHubspotOriginal,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
                            strError = strError + " | lstValueProperties.size() "+lstValueProperties.size();
                            if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave())) {
                                strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
                                objHubSpotData.put("carrera", lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave());
                            }else{
								nfcarrera = true;
								msjNF = "No se encontro la propiedad carrera";
							}
                        }
                    }
                    
                    if(lstSolicitudDeAdmision.get(0).getCatPropedeutico() != null) {
                        strError = strError + " | tiene propedeutico";
                        if(lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave() != null) {
                            strError = strError + " | tiene clave";
                            strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave();
                            lstValueProperties = getLstValueProperties2("periodo_propedeutico_bpm", apikeyHubspotOriginal,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
                            if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave())) {
                                strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave();
                                objHubSpotData.put("periodo_propedeutico_bpm", lstSolicitudDeAdmision.get(0).getCatPropedeutico().getClave());
                            }else{
								nffp = true;
								if(nfcarrera){
									msjNF = msjNF + ", periodo_propedeutico_bpm";	
								}else{
									msjNF = "No se encontro la propiedad periodo_propedeutico_bpm";	
								}
							}
                        }
                    }
                    
                    if(lstSolicitudDeAdmision.get(0).getCatPeriodo() != null) {
                        strError = strError + " | tiene periodo";
                        if(lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave() != null) {
                            strError = strError + " | tiene clave";
                            strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave();
                            lstValueProperties = getLstValueProperties2("periodo_de_ingreso_bpm", apikeyHubspotOriginal,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
                            if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave())) {
                                strError = strError + " | entro al if";
                                strError = strError + " | lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave(): "+lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave();
                                objHubSpotData.put("periodo_de_ingreso_bpm", lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave());
                            }else{
								nffi = true;
								if(nfcarrera || nffp){
									msjNF = msjNF + ", periodo_de_ingreso_bpm";	
								}else{
									msjNF = "No se encontro la propiedad periodo_de_ingreso_bpm";	
								}
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
                    
                     if (lstSolicitudDeAdmision.get(0).getCatEstado() != null) {
                    strError = strError + " | tiene estado";
                    if(lstSolicitudDeAdmision.get(0).getCatEstado().getClave() != null) {
                        strError = strError + " | tiene clave";
                        strError = strError + " | lstSolicitudDeAdmision.get(0).getCatEstado().getClave(): "+lstSolicitudDeAdmision.get(0).getCatEstado().getClave();
                        lstValueProperties = getLstValueProperties2("importacion_estados", apikeyHubspotOriginal,lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context);
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

                objHubSpotData.put("pais", lstSolicitudDeAdmision.get(0).getCatPais().getDescripcion())
                objHubSpotData.put("municipio_bpm", lstSolicitudDeAdmision.get(0).getCiudad());
                strError = strError + " | ----------------------------- ";
                
                objHubSpotData.put("campus_admision_bpm", lstSolicitudDeAdmision.get(0).getCatCampusEstudio().getClave());
                //objHubSpotData.put("periodo_de_ingreso_bpm", lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave());
                objHubSpotData.put("campus_vpd_bpm", lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
                objHubSpotData.put("firstname", lstSolicitudDeAdmision.get(0).getPrimerNombre() + " " + (lstSolicitudDeAdmision.get(0).getSegundoNombre() == null ? "" : lstSolicitudDeAdmision.get(0).getSegundoNombre()));
                objHubSpotData.put("lastname", lstSolicitudDeAdmision.get(0).getApellidoPaterno() + " " + lstSolicitudDeAdmision.get(0).getApellidoMaterno());
                objHubSpotData.put("fecha_nacimiento_bpm", dformat.format(fechaNacimiento));
                //objHubSpotData.put("gender", lstSolicitudDeAdmision.get(0).getCatSexo().getClave() == null ? "" : lstSolicitudDeAdmision.get(0).getCatSexo().getClave());
                objHubSpotData.put("promedio_bpm", lstSolicitudDeAdmision.get(0).getPromedioGeneral() == null ? "" : lstSolicitudDeAdmision.get(0).getPromedioGeneral());
                    
                    
                    
                                        
                objHubSpotData.put("lugar_de_examen_bpm", lugarExamen);
                
                objHubSpotData.put("campus_vpd_bpm", lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
                objHubSpotData.put("firstname", lstCatRegistro.get(0).getPrimernombre()+" "+(lstCatRegistro.get(0).getSegundonombre() == null ? "" : lstCatRegistro.get(0).getSegundonombre()));
                objHubSpotData.put("lastname", lstCatRegistro.get(0).getApellidopaterno()+" "+lstCatRegistro.get(0).getApellidomaterno());
                objHubSpotData.put("email", correoElectronico);
                
                objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
                objHubSpotData.put("apoyo_ov_bpm", lstSolicitudDeAdmision.get(0).isNecesitoAyuda());
                objHubSpotData.put("phone", lstCatRegistro.get(0).getNumeroContacto());
                
                if(lstSolicitudDeAdmision.get(0).getCatBachilleratos().getClave().toLowerCase().equals("otro")) {
                    objHubSpotData.put("preparatoria_bpm", lstSolicitudDeAdmision.get(0).getBachillerato());
                }
                else {
                    objHubSpotData.put("preparatoria_bpm", lstSolicitudDeAdmision.get(0).getCatBachilleratos().getDescripcion());
                }
                lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);
                
                strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
                strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
                
                if(lstDetalleSolicitud != null) {
                    if(!lstDetalleSolicitud.empty) {
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
                        
                        
                        objHubSpotData.put("mensaje_bpm", lstDetalleSolicitud.get(0).getObservacionesCambio());
                        objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
                        descuento = ""+lstDetalleSolicitud.get(0).getDescuento();
                        catDescuento = ""+(lstDetalleSolicitud.get(0).getCatDescuentos()== null ? "" : lstDetalleSolicitud.get(0).getCatDescuentos().getDescuento());
                        
                        strError = strError + " | descuento: "+"descuento.toString()";
                        strError = strError + " | getOrdenPago: "+lstDetalleSolicitud.get(0).getOrdenPago()//lstDetalleSolicitud.size()>0 ? (lstDetalleSolicitud.get(0).getOrdenPago() == null ? "NULO OP" : "lstDetalleSolicitud.get(0).getOrdenPago()") : "NULL";
                        strError = strError + " | getCatCampus().getPersistenceId: " + lstSolicitudDeAdmision.get(0).getCatCampus().getPersistenceId()
                        strError = strError + " | jsonPago: " +jsonPago.replace("[ORDERID]", String.valueOf(lstDetalleSolicitud.get(0).getOrdenPago())).replace("[CAMPUSID]", String.valueOf(lstSolicitudDeAdmision.get(0).getCatCampus().getPersistenceId()));
                        if(lstDetalleSolicitud.get(0).getOrdenPago() == null || lstDetalleSolicitud.get(0).getOrdenPago() == "") {
                            
                        }else {
                            resultadoCDAO = cDAO.getOrderDetails(0, 999, jsonPago.replace("[ORDERID]", String.valueOf(lstDetalleSolicitud.get(0).getOrdenPago())).replace("[CAMPUSID]", String.valueOf(lstSolicitudDeAdmision.get(0).getCatCampus().getPersistenceId())), context);
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
                                
                                strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
                            }
                            else {
                                //throw new Exception(resultadoCDAO.getError());
                            }
                        }
                        
                    }
                }
                }
                else {
                    strError = strError + " | ------------------------------------------";
                    strError = strError + " | lstSolicitudDeAdmision.empty: "+lstSolicitudDeAdmision.empty;
                    strError = strError + " | lstCatRegistro.empty: "+lstCatRegistro.empty;
                    strError = strError + " | ------------------------------------------";
                }
                
            }
            pstm = con.prepareStatement("select to_char(now(), 'YYYY-MM-DD HH24:MI') fechahoraservidor")
            rs = pstm.executeQuery();
            if(rs.next()){
                fecha = dfSalida.parse(rs.getString("fechahoraservidor"))
            }
            objHubSpotData.put("fecha_actualizacion_bpm", dfSalida.format(fecha));
            objHubSpotData.put("fecha_transferencia_bpm",dfSalida.format(fecha));
            objHubSpotData.put("origen_vpd_bpm",  claveOriginal);
            objHubSpotData.put("destino_vpd_bpm", claveCambio);
            Map<String, String> objHubSpotDataPreview = new HashMap<String, String>(objHubSpotData)
            data.add(objHubSpotDataPreview)
        
            resultado = createOrUpdateHubspot(correoElectronico, apikeyHubspotCambio, objHubSpotData);
            
			/*if (!resultado.success) {
				def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				
				List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailTransferirAspirante",0,1)
				MailGunDAO mgd = new MailGunDAO();
				mgd.sendEmailPlantilla(config.get(0).getValor(), "Hubspot Transferir Aspirante Error", resultado.getError(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getClave(), context);
			}
*/
			if (!resultado.success) {
				//def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				String correo = "";
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_CORREO_BY_CLAVE)
				pstm.setString(1, "EmailTransferirAspirante")
				rs = pstm.executeQuery()
				while(rs.next()) {
					correo = rs.getString("valor");
				}
			
				//List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailRegistro",0,1)
				MailGunDAO mgd = new MailGunDAO();
				Result correoenviado =mgd.sendEmailPlantilla(correo, "Hubspot Transferir Aspirante Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context)
				strError += strError + correoenviado.isSuccess().toString() + " | " + correoenviado.getInfo();
				//resultado.error+="|email:"+correo+"|response:"+mgd.sendEmailPlantilla(correo, "Hubspot Registro Error", resultado.getError(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context).getData().get(0)
			}

			if (nfcarrera || nffp || nffi) {
				//def objCatConfiguracionDAO = context.apiClient.getDAO(CatConfiguracionDAO.class)
				String correo = "";
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_CORREO_BY_CLAVE)
				pstm.setString(1, "EmailTransferirAspirante")
				rs = pstm.executeQuery()
				while(rs.next()) {
					correo = rs.getString("valor");
				}
				//List<CatConfiguracion> config =objCatConfiguracionDAO.findByClave("EmailRegistro",0,1)
				msjNF += " en el catalogo de HubSpot"
				MailGunDAO mgd = new MailGunDAO();
				Result correoenviado = mgd.sendEmailPlantilla(correo, "Hubspot Transferir Aspirante Error - Propiedad no encotrada", msjNF + "<br><br>" + resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context)
				
				strError += strError + correoenviado.isSuccess().toString() + " | " + correoenviado.getInfo();
			}

          resultado.setData(data)
          resultado.setSuccess(true);
      } catch (Exception e) {
          LOGGER.error "e: "+e.getMessage();
          resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
          resultado.setSuccess(false);
          resultado.setError(e.getMessage());
          e.printStackTrace();
      }finally {
          if(closeCon) {
              new DBConnect().closeObj(con, stm, rs, pstm)
          }
      }
      return resultado
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
		  rs = pstm.executeQuery()
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

	  //TO-DO
	  public Result createOrUpdateBeca(String jsonData, RestAPIContext context) {
	        Result resultado = new Result();
	        Result resultadoApiKey = new Result();
	        Boolean closeCon = false;
//	        List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
	        List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
	        List<String> lstValueProperties = new ArrayList<String>();
	        Map<String, String> objHubSpotData = new HashMap<String, String>();
	        Boolean noSol = false;
			String msjNF = "";
	        String strError = "";
	        String apikeyHubspot ="";
	        Date fecha = new Date();
	        DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
	        try {
				  
	            def jsonSlurper = new JsonSlurper();
	            def object = jsonSlurper.parseText(jsonData);
	            
				def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
				lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
				
	            assert object instanceof Map;
	            strError += " | try";
				
				resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
				apikeyHubspot = (String) resultadoApiKey.getData().get(0);
				ServiciosBecasDAO becas = new ServiciosBecasDAO();
				Result resultBEcas = becas.getSolicitudApoyoByCaseId(Integer.valueOf(object.caseid), context);
				String email = object.email;
				strError += " | jsonData.caseid" + object.caseid;
				
				if(resultBEcas.isSuccess()){
					Map < String, Object > map = new LinkedHashMap < String, Object > ();
					map = (Map < String, Object >) resultBEcas.getData().get(0);
//					objHubSpotData.put("email", email);
					
					objHubSpotData.put("beca_otorgada_bpm", "");
					objHubSpotData.put("estatus_beca_bpm",  estatusMapBecas.get(map.get("estatussolicitud")));
					objHubSpotData.put("finan_otorgada_bpm", "");
					objHubSpotData.put("mensaje_becas_bpm", "");
//					objHubSpotData.put("periodo_de_ingreso_becas_bpm", map.get("ingreso"));
					objHubSpotData.put("porcentaje_beca_solicitado_bpm",  map.get("porcentajebeca"));
					objHubSpotData.put("porcentaje_finan_solicitado_bpm",  map.get("porcentajefinanciamiento"));
					objHubSpotData.put("propuesta_beca_bpm", "");
					objHubSpotData.put("tipo_beca_bpm", mapTipoBecas.get(map.get("tipoapoyo")));
//					objHubSpotData.put("fecha_actualizacion_becas_bpm", dfSalida.format(map.get("fechaultimamodificacion")));
					
					resultado = createOrUpdateHubspotBecas(email, apikeyHubspot, objHubSpotData);
				} else {
					strError = strError + " | ------------------------------------------";
					strError = strError + " | lstSolicitudDeAdmision.empty: "+lstSolicitudDeAdmision.empty;
					strError = strError + " | lstCatRegistro.empty: "+lstCatRegistro.empty;
					strError = strError + " | ------------------------------------------";
				}
				
	            if (!resultado.success) {
	                String correo = "";
	                closeCon = validarConexion();
	                pstm = con.prepareStatement(Statements.GET_CORREO_BY_CLAVE)
	                pstm.setString(1, "EmailRegistro")
	                rs = pstm.executeQuery()
	                while(rs.next()) {
	                    correo = rs.getString("valor");
	                }
	            
	                MailGunDAO mgd = new MailGunDAO();
	                Result correoenviado = mgd.sendEmailPlantilla(correo, "Hubspot Registro Error", resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context)
	                strError += strError + correoenviado.isSuccess().toString() + " | " + correoenviado.getInfo();
	            }
	
	            if (noSol == true) {
	                String correo = "";
	                closeCon = validarConexion();
	                pstm = con.prepareStatement(Statements.GET_CORREO_BY_CLAVE)
	                pstm.setString(1, "EmailRegistro")
	                rs = pstm.executeQuery()
	                while(rs.next()) {
	                    correo = rs.getString("valor");
	                }
					
	                msjNF += " en el catalogo de HubSpot"
	                MailGunDAO mgd = new MailGunDAO();
	                Result correoenviado = mgd.sendEmailPlantilla(correo, "Hubspot Registro Error - Propiedad no encotrada", msjNF + "<br><br>" + resultado.getError_info(), "",lstSolicitudDeAdmision.get(0).getCatCampus().getGrupoBonita(), context)
	                
	                strError += strError + correoenviado.isSuccess().toString() + " | " + correoenviado.getInfo();
	            }
	
	            resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
	            
	        } catch (Exception e) {
	            LOGGER.error "e: "+e.getMessage();
	            resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
	            resultado.setSuccess(false);
	            resultado.setError(e.getMessage());
	            e.printStackTrace();
	            new LogDAO().insertTransactionLog("POST", "FALLIDO", "Error inesperado", "Log:"+strError, e.getMessage())
	        } finally {
	            if(closeCon) {
	                new DBConnect().closeObj(con, stm, rs, pstm)
	            }
	        }
			  
			return resultado
	  }
	  
	  public Result createOrUpdateHubspotBecas(String email, String apikeyHubspot, Map<String, String> objHubSpotData) {
		  Result resultado = new Result();
		  //String data="8b-.-.-.b0-.-.-.a-.-.-.1ac-df-.-.-.54-40-.-.-.bf-b5-.-.-.69-40-.-.-.e8-.-.-.7f-.-.-.90-.-.-.c0-.-.-.99"
		  String targetURL = "https://api.hubapi.com/contacts/v1/contact/createOrUpdate/email/[EMAIL]/"
//		  String jsonInputString = "{\"properties\":[{\"property\":\"firstname\",\"value\":\"java\"},{\"property\":\"nombre\",\"value\":\"Arturo\"},{\"property\":\"lastname\",\"value\":\"Zamorano\"},{\"property\":\"nombre_completo\",\"value\":\"Arturo Zamorano\"},{\"property\":\"correo_electrnico\",\"value\":\"jasz189@hotmail.com\"},{\"property\":\"date_of_birth\",\"value\":\"2020-11-30T23:51:03.309Z\"},{\"property\":\"fecha_de_nacimiento\",\"value\":\"654307200000\"},{\"property\":\"twitterhandle\",\"value\":\"arturoZCZ\"},{\"property\":\"gender\",\"value\":\"Masculino\"},{\"property\":\"country\",\"value\":\"México\"},{\"property\":\"state\",\"value\":\"Sonora\"},{\"property\":\"ciudad\",\"value\":\"Navojoa\"},{\"property\":\"city\",\"value\":\"Navojoa\"},{\"property\":\"address\",\"value\":\"Callejon 3\"},{\"property\":\"celular\",\"value\":\"6421344161\"},{\"property\":\"phone\",\"value\":\"6421344161\"},{\"property\":\"zip\",\"value\":\"85890\"},{\"property\":\"promedio\",\"value\":\"9.5\"},{\"property\":\"promedio_de_preparatoria\",\"value\":\"9.5\"},{\"property\":\"relationship_status\",\"value\":\"Casado\"},{\"property\":\"nombre_de_tutor\",\"value\":\"Arturo\"},{\"property\":\"apellido_tutor\",\"value\":\"Zamorano\"},{\"property\":\"celular_de_tutor\",\"value\":\"6421344161\"},{\"property\":\"correo_tutor\",\"value\":\"arturo.zamorano@gmail.com\"},{\"property\":\"telefono_tutor\",\"value\":\"6421344161\"},{\"property\":\"nombre_del_padre\",\"value\":\"Arturo\"},{\"property\":\"apellido_paterno\",\"value\":\"Zamorano\"},{\"property\":\"celular_del_padre\",\"value\":\"6421344161\"},{\"property\":\"correo_del_padre\",\"value\":\"arturo.zamorano@gmail.com\"},{\"property\":\"telefono_del_padre\",\"value\":\"6421344161\"},{\"property\":\"nombre_de_la_madre\",\"value\":\"Guadalupe\"},{\"property\":\"apellido_materno\",\"value\":\"Sainz\"},{\"property\":\"celular_de_la_madre\",\"value\":\"6421344161\"},{\"property\":\"correo_de_la_madre\",\"value\":\"eva.sainz@gmail.com\"},{\"property\":\"telefono_de_la_madre\",\"value\":\"6421344161\"},{\"property\":\"ua_vpd\",\"value\":\"UAM\"},{\"property\":\"campus_destino\",\"value\":\"AMAY\"},{\"property\":\"tipo_de_alumno_bpm\",\"value\":\"N\"}]}";
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
  
}

