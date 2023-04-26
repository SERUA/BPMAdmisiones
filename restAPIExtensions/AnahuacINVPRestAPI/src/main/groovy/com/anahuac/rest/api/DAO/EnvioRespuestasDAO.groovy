package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.DateFormat
import java.text.SimpleDateFormat
import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.identity.UserNotFoundException
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.invp.RespuestasExamen
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.custom.CatPreguntasCustomFiltro
import com.anahuac.rest.api.Utilities.LoadParametros
import groovy.json.JsonSlurper

class EnvioRespuestasDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RespuestasExamenDAO.class);
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
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true
		}
		return retorno;
	}
	
	public Result PostMasivoRespuestaSesion( String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		boolean roll = false;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			String idBannerList = "";
			
			object.each{
				if(!it.idbanner.equals("") && !it.idbanner.equals("null") && !it.idbanner.equals(null)) {
					pstm = con.prepareStatement(Statements.GET_RESULTADOS_INVP_BY_USERNAME)
					pstm.setString(1, it.username);
					rs = pstm.executeQuery();
					
					List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
					String respuestas = "";
					while (rs.next()) {
						respuestas = rs.getString("resultados");
					}
					
					def str = "{\"respuestas\":\"${respuestas}\",\"idbanner\":\"${it.idbanner}\",\"id_sesion\":${it.sesion} }";
					
					resultado = insertRespuesta(str);
					
					if(resultado.isSuccess()) {
						idBannerList += idBannerList.length()>1?",":"";
						idBannerList += it.idbanner	;
						roll = true;
						con.setAutoCommit(false)
						pstm = con.prepareStatement(Statements.UPDATE_RESULTADOENVIADO);
						pstm.setString(1, it.username)
						pstm.executeUpdate();
						con.commit();
					}
				}
				
			}
			if(idBannerList.length() == 0) {
				resultado.setSuccess(false);
				resultado.setInfo("No se encontraron aspirante para guardar");
			}else {
				resultado.setSuccess(true);
				resultado.setInfo(idBannerList);
			}
			
			
			
		} catch (Exception e) {
			String es = e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(es);
			if(roll) {con.rollback();}
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result PostRespuestaSesion( String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		boolean roll = false;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			if(!object.idbanner.equals("") && !object.idbanner.equals("null") && !object.idbanner.equals(null)) {
				pstm = con.prepareStatement(Statements.GET_RESULTADOS_INVP_BY_USERNAME)
				pstm.setString(1, object.username);
				rs = pstm.executeQuery();
				
				List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
				String respuestas = "";
				while (rs.next()) {
					respuestas = rs.getString("resultados");
				}
				
				def str = "{\"respuestas\":\"${respuestas}\",\"idbanner\":\"${object.idbanner}\",\"id_sesion\":${object.sesion} }";
				//def str = "{\"campus\": \""+object.campus+"\", \"correo\":\"" + object.nombreusuario + "\", \"codigo\": \"registrar\", \"isEnviar\":false }"
				resultado = insertRespuesta(str);
						
				//resultado.setSuccess(true);
				
				if(resultado.isSuccess()){
					roll = true;
					con.setAutoCommit(false)
					pstm = con.prepareStatement(Statements.UPDATE_RESULTADOENVIADO);
					pstm.setString(1, object.username)
					pstm.executeUpdate();
					con.commit();
				}
			}else {
				resultado.setSuccess(false);
				resultado.setError("El aspirante tiene que tener un id banner para poder enviar su resultado");
			}
			
			
			
		} catch (Exception e) {
			String es = e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(es);
			if(roll) {con.rollback();}
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	
	public Result insertRespuesta(String jsonData) {
		def jsonSlurper = new JsonSlurper();
		def object = jsonSlurper.parseText(jsonData);
		Result resultado = new Result()
		Boolean closeCon = false
		String where ="",errorLog = "";
		String idbanner=object.idbanner;;
		String respuesta = object.respuestas;
		String sesiones_pid=object.id_sesion;
		List < Map < String, Object >> respuestas = new ArrayList < Map < String, Object >> ();
		List < Map < String, String >> additionalData = new ArrayList < Map < String, String >> ();
		List < Map < String, Integer >> rows = new ArrayList < Map < String, Integer >> ();
		Map<String,Integer> respuestainvp= new HashMap  < String, Integer >()
		Map<String,String> aData= new HashMap  < String, String >()
		aData.put("idbanner", idbanner);
		aData.put("respuestas",respuesta);
		aData.put("id_sesion",sesiones_pid);
		try {
			Result resultado2 = new Result();
			resultado2 = insertBitacoraComentarios(jsonData);
			errorLog = resultado2.getError();
			closeCon = validarConexion()
			pstm = con.prepareStatement(Statements.GET_RESPUESTAS_INVP);
			pstm.setString(1, idbanner)
			rs = pstm.executeQuery()
			respuestas = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				respuestas.add(columns);
			}
			int count = 0;
			for (int i = 0; i < respuesta.length(); i++) {
				if (respuesta.charAt(i) == "*") {
					count++;
				}
			}
			
			respuestainvp.put("?",count);
			
			for(int i=0; i<respuestas.size(); i++) {
				if(i > respuesta.length()) {continue;}
				if (respuesta.charAt((Integer.parseInt(respuestas.get(i).get("pregunta"))-1)) != "*") {
					if((respuesta.charAt(Integer.parseInt(respuestas.get(i).get("pregunta"))-1)=='C')==(respuestas.get(i).get("respuesta")=='t')) {
						if(respuestainvp.get(respuestas.get(i).get("escala"))==null) {
							respuestainvp.put(respuestas.get(i).get("escala"), Integer.parseInt(respuestas.get(i).get("puntuacion")))
						}else {
							respuestainvp.put(respuestas.get(i).get("escala"), respuestainvp.get(respuestas.get(i).get("escala"))+Integer.parseInt(respuestas.get(0).get("puntuacion")))
						}
						
					}else {
						
						if(respuestainvp.get(respuestas.get(i).get("escala"))==null) {
							respuestainvp.put(respuestas.get(i).get("escala"), 0)
						}
					}
				}else {
					if(respuestainvp.get(respuestas.get(i).get("escala"))==null) {
						respuestainvp.put(respuestas.get(i).get("escala"), 0)
					}
				}
				
			}
			errorLog+="llego aqui 2";
			String caseId = "";
			try {
				pstm = con.prepareStatement(Statements.GET_CASEID_BY_IDBANNER);
				pstm.setString(1, idbanner)
				rs= pstm.executeQuery();
				if(rs.next()) {
					caseId = rs.getString("caseid");
				}
				 
			} catch(Exception cd) {
				errorLog+=", fallo en traer el caseid";
				caseId = "0";
			}
			try {
				errorLog +=" entro 1 || "+caseId;;
				TimeZone tz = TimeZone.getTimeZone("UTC")
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS") 
				df.setTimeZone(tz)
				String nowAsISO = df.format(new Date())
				pstm = con.prepareStatement(Statements.GET_RESULTADO_INVP);
				pstm.setString(1, idbanner)
				pstm.setLong(2, Long.parseLong(sesiones_pid))
				//errorLog+=" || "+idbanner + "|| "+sesiones_pid + "||"+nowAsISO;
				rs = pstm.executeQuery()
				if(!rs.next()) {
					errorLog+=" || "+respuestainvp;
					for (Map.Entry<String,Integer> entry: respuestainvp) {
						errorLog+=" || "+entry.getKey()+" || "+entry.getValue()
						pstm = con.prepareStatement(Statements.INSERT_RESULTADO_INVP);
						pstm.setString(1, idbanner)
						pstm.setString(2, entry.getKey())
						pstm.setInt(3, entry.getValue())
						pstm.setLong(4, Long.parseLong(sesiones_pid))
						pstm.setString(5, nowAsISO)
						pstm.setLong(6, Long.parseLong(caseId))
						pstm.execute();
					}
					
				}else {
					resultado.setError("Error");
					resultado.setError_info("Ya hay un aspirante con esa sesion")
				}
			}
			catch(Exception test) {
				resultado.setError("Error")
				resultado.setError_info(test.getMessage()+" -- " +errorLog)
			}
			
			if(resultado.getError().equals("Error")){
				resultado.setSuccess(false);
			}else {
				Result asistencia = asistenciaINVP(idbanner,sesiones_pid);
				rows.add(respuestainvp)
				additionalData.add(aData)
				resultado.setSuccess(true)
				resultado.setData(rows)
				resultado.setAdditional_data(additionalData+"error:"+errorLog+" asistencia:"+asistencia.isSuccess()+"error_Asistencia:"+asistencia.getError())
			}
			
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError(e.getMessage())
			resultado.setError_info(errorLog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result asistenciaINVP(String idbanner,String idsesion) {
		Result resultado = new Result();
		Result dataResult = new Result();
		String errorLog = "";
		try {
			String caseid = "", prueba="",username2 = "",username="servicio INVP";
			errorLog+="1";
			pstm = con.prepareStatement(Statements.GET_PRUEBA_CASEID_USERNAME_BY_BANNER_AND_SESION)
			pstm.setString(1, idbanner);
			pstm.setInt(2, idsesion.toInteger());
			rs= pstm.executeQuery();
			if(rs.next()) {
				caseid = rs.getString("caseid");
				prueba = rs.getString("prueba_pid");
				username2 = rs.getString("username");
			}
			errorLog+="2";
			if(!prueba.equals("") && !prueba.equals("null") && prueba != null ){
				boolean update = false;
				errorLog+="3";
				pstm = con.prepareStatement(Statements.GET_USER_REGISTRADO_EN_PRUEBA);
				pstm.setInt(1, prueba.toInteger());
				pstm.setString(2, username2);
				rs= pstm.executeQuery();
				if(rs.next()) {
					update = true;
				}
				errorLog+="4";
				String jsdonPaseLista = "{\"prueba\":${prueba},\"username\":\"${username2}\",\"asistencia\":true,\"usuarioPaseLista\":\"${username}\"}";
				if(update) {
					errorLog+="5";
					dataResult = updatePaseLista(jsdonPaseLista);
				}else {
					errorLog+="6";
					dataResult = insertPaseLista(jsdonPaseLista);
				}
				errorLog+="7";
				
				String usuario = "";
				String password = "";
				
				/*-------------------------------------------------------------*/
				LoadParametros objLoad = new LoadParametros();
				PropertiesEntity objProperties = objLoad.getParametros();
				usuario = objProperties.getUsuario();
				password = objProperties.getPassword();
				/*-------------------------------------------------------------*/
				
				org.bonitasoft.engine.api.APIClient apiClient = new APIClient() //context.getApiClient();
				apiClient.login(usuario, password);
				//context.getApiClient().getProcessAPI();
				ProcessAPI processAPI = apiClient.getProcessAPI();
				Map<String, Serializable> rows = new HashMap<String, Serializable>();
				
				rows.put("asistenciaPsicometrico", true);
				processAPI.updateProcessDataInstances(Long.parseLong(caseid),rows)
				errorLog +="8";
			}
			
			
			resultado.setSuccess(true)
			resultado.setError_info(dataResult.toString())
			resultado.setError(errorLog);
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError(errorLog);
			resultado.setError_info(e.getMessage())
		}
		return resultado
	}
	
	public Result updatePaseLista(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				pstm = con.prepareStatement(Statements.UPDATE_PASEDELISTA);
				pstm.setBoolean(1,object.asistencia);
				pstm.setString(2,object.usuarioPaseLista);
				pstm.setLong(3, object.prueba)
				pstm.setString(4, object.username)
				pstm.executeUpdate();
				
				con.commit();
				Result dataResult = updateAspirantesPruebas(jsonData);
				Result dataResult2 = updateBitacoraAspirantesPruebas(jsonData);
				
				resultado.setSuccess(true)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result insertPaseLista( String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				pstm = con.prepareStatement(Statements.INSERT_PASEDELISTA);
				pstm.setLong(1, object.prueba);
				pstm.setString(2, object.username);
				pstm.setBoolean(3,object.asistencia);
				pstm.setString(4,object.usuarioPaseLista);
				
				pstm.executeUpdate();
				
				con.commit();
				
				Result dataResult = updateAspirantesPruebas(jsonData);
				Result dataResult2 = updateBitacoraAspirantesPruebas(jsonData);
				
				resultado.setSuccess(true)
			} catch (Exception e) {
			String es = e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(es);
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result insertBitacoraComentarios( String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				closeCon = validarConexion();
				
				Calendar cal = Calendar.getInstance();
				Date date = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				String sDate = formatter.format(date);
				
				con.setAutoCommit(false)
				pstm = con.prepareStatement(Statements.INSET_BITACORA_RESPUESTAS);
				pstm.setString(1, object.respuestas);
				pstm.setString(2, object.idbanner);
				pstm.setString(3,"Administrador");
				pstm.setString(4,"INVP");
				pstm.setString(5,sDate);
				
				pstm.executeUpdate();
				
				con.commit();
				
				resultado.setSuccess(true)
			} catch (Exception e) {
			String es = e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(es);
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updateAspirantesPruebas(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				
				pstm = con.prepareStatement(Statements.UPDATE_ASPIRANTESPRUEBAS);
				pstm.setBoolean(1,object.asistencia);
				pstm.setLong(2,object.prueba);
				pstm.setString(3,object.username);
				
				
				pstm.executeUpdate();
				con.commit();
				resultado.setSuccess(true)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updateBitacoraAspirantesPruebas(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				
				pstm = con.prepareStatement(Statements.UPDATE_BITACORAASPIRANTESPRUEBAS);
				pstm.setBoolean(1,object.asistencia);
				pstm.setLong(2,object.prueba);
				pstm.setString(3,object.username);
				
				
				pstm.executeUpdate();
				con.commit();
				resultado.setSuccess(true)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
}
