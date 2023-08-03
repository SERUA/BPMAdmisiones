package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.rest.api.DAO.ListadoDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utilities.FileDownload
import com.anahuac.rest.api.DB.Statements

import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.SimpleDateFormat
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.IndexedColorMap
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.document.Document


class ImportacionPAADAO {
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
	
	
	public Result postGuardarUsuario(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result dataResult = new Result();
		Boolean closeCon = false;
		Boolean executar = false;
		String errorLog = "";
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				object.each{
					executar = true;
					con.setAutoCommit(false)
					if(it.update) {
						pstm = con.prepareStatement(Statements.UPDATE_IMPORTACIONPAA, Statement.RETURN_GENERATED_KEYS)
						pstm.setString(1,it.PAAN);
						pstm.setString(2,it.LA1);
						pstm.setString(3,it.LA2);
						pstm.setString(4,it.LA3);
						pstm.setString(5,it.LA4);
						pstm.setString(6,it.PG1);
						pstm.setString(7,it.PG2);
						pstm.setString(8,it.PG3);
						pstm.setString(9,it.PG4);
						pstm.setString(10,it.PG5);
						pstm.setString(11,it.PV1);
						pstm.setString(12,it.PV4);
						pstm.setString(13,it.PAAV);
						pstm.setString(14,it.LEO1);
						pstm.setString(15,it.LEO3);
						pstm.setString(16,it.LEO4);
						pstm.setString(17,it.LEO5);
						pstm.setString(18,it.CIT1);
						pstm.setString(19,it.CIT2);
						pstm.setString(20,it.PARA);
						pstm.setString(21,it.HI1);
						pstm.setString(22,it.HI2);
						pstm.setString(23,it.HI3);
						pstm.setString(24,it.HI4);
						pstm.setString(25,it.HI5);
						pstm.setString(26,it.HI6);
						pstm.setString(27,it.Total);
						if(it.tipoExamen.equals("KP")) {
							pstm.setString(28,it.MLEX);
							pstm.setString(29,it.CLEX);
							pstm.setString(30,it.HLEX);
							pstm.setString(31,it.LEXIUM_Total);
						}else {
							pstm.setString(28,"");
							pstm.setString(29,"");
							pstm.setString(30,"");
							pstm.setString(31,"");
						}
						pstm.setString(32,it.tipoExamen);
						pstm.setString(33,it.INVP);
						pstm.setString(34,it.fechaExamen);
						pstm.setLong(35,Long.parseLong(it.PERSISTENCEID));
						pstm.setString(36,it.IDBANNER);
						pstm.setString(37,it.caseId);
						pstm.executeUpdate();
						
					}else {
							pstm = con.prepareStatement(Statements.INSERT_IMPORTACIONPAA, Statement.RETURN_GENERATED_KEYS);
							pstm.setString(1,it.IDBANNER);
							pstm.setString(2,it.PAAN);
							pstm.setString(3,it.LA1);
							pstm.setString(4,it.LA2);
							pstm.setString(5,it.LA3);
							pstm.setString(6,it.LA4);
							pstm.setString(7,it.PG1);
							pstm.setString(8,it.PG2);
							pstm.setString(9,it.PG3);
							pstm.setString(10,it.PG4);
							pstm.setString(11,it.PG5);
							pstm.setString(12,it.PV1);
							pstm.setString(13,it.PV4);
							pstm.setString(14,it.PAAV);
							pstm.setString(15,it.LEO1);
							pstm.setString(16,it.LEO3);
							pstm.setString(17,it.LEO4);
							pstm.setString(18,it.LEO5);
							pstm.setString(19,it.CIT1);
							pstm.setString(20,it.CIT2);
							pstm.setString(21,it.PARA);
							pstm.setString(22,it.HI1);
							pstm.setString(23,it.HI2);
							pstm.setString(24,it.HI3);
							pstm.setString(25,it.HI4);
							pstm.setString(26,it.HI5);
							pstm.setString(27,it.HI6);
							pstm.setString(28,it.Total);
							pstm.setString(29,it.fechaExamen);
							if(it.tipoExamen.equals("KP")) {
								pstm.setString(30,it.MLEX);
								pstm.setString(31,it.CLEX);
								pstm.setString(32,it.HLEX);
								pstm.setString(33,it.LEXIUM_Total);
							}else {
								pstm.setString(30,"");
								pstm.setString(31,"");
								pstm.setString(32,"");
								pstm.setString(33,"");
							}
							
							pstm.setString(34,it.tipoExamen);
							pstm.setString(35,it.INVP);
							pstm.setString(36,it.IdSesion)
							pstm.setString(37,it.caseId);
							pstm.executeUpdate();
							dataResult = asistenciaCollegeBoard(it.IDBANNER,it.IdSesion,it.username,it.caseId,context);
					}
					
				}
				
				if(executar) {
					con.commit();
					/*Result resultado2 = new Result();
					resultado2 = subirDatosBannerEthos(jsonData,context);
					errorLog += "INTEGRACION:"+resultado2.isSuccess()+"ERROR:"+resultado2.getError()+"ERROR_INFO:"+resultado2.getError_info();*/
					//
				}
				resultado.setSuccess(true);
				//resultado.setData(estatus)
				resultado.setError_info(dataResult.toString());
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
	
	public Result asistenciaCollegeBoard(String idbanner,idsesion,username,caseid, RestAPIContext context) {
		Result resultado = new Result();
		Result dataResult = new Result();
		String errorLog = "";
		try {
			String  prueba="",username2 = "";
			errorLog+="1";
			pstm = con.prepareStatement("Select sda.caseid, ap.prueba_pid, ap.username FROM solicituddeadmision AS SDA INNER JOIN detallesolicitud AS DS ON DS.caseid = SDA.caseid::varchar AND DS.idbanner = '${idbanner}' INNER JOIN aspirantespruebas AS AP ON AP.username = SDA.correoelectronico AND AP.catTipoPrueba_pid = 4 and AP.sesiones_pid = ${idsesion} ")
			rs= pstm.executeQuery();
			if(rs.next()) {
				//caseid = rs.getString("caseid");
				prueba = rs.getString("prueba_pid");
				username2 = rs.getString("username");
			}
			errorLog+="2";
			username2 = username2.replace(" (rechazado)","");
			if(!prueba.equals("") && !prueba.equals("null") && prueba != null ){
				boolean update = false;
				errorLog+="3";
				pstm = con.prepareStatement(" SELECT * FROM paselista WHERE prueba_pid = ${prueba} and username = '${username2}'")
				rs= pstm.executeQuery();
				if(rs.next()) {
					update = true;
				}
				errorLog+="4";
				String jsdonPaseLista = "{\"prueba\":${prueba},\"username\":\"${username2}\",\"asistencia\":true,\"usuarioPaseLista\":\"${username}\"}";
				if(update) {
					errorLog+="5";
					dataResult = new SesionesDAO().updatePaseLista(jsdonPaseLista,context);
				}else {
					errorLog+="6";
					dataResult = new SesionesDAO().insertPaseLista(jsdonPaseLista,context);
				}
				errorLog+="7";
				ProcessAPI processAPI = context.getApiClient().getProcessAPI();
				Map<String, Serializable> rows = new HashMap<String, Serializable>();
				
				rows.put("asistenciaCollegeBoard", true);
				processAPI.updateProcessDataInstances(Long.parseLong(caseid),rows)
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
	
	public Result subirDatosBannerEthos(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
		List<Map<String,Object>> machine = new ArrayList <Map<String,Object>> ()
		Map<String,Object> coins =  new HashMap < String, Object > ();
		
		

		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			
			//resultado = new BannerDAO().integracionBannerEthosEAC(context, jsonData)
			//errorLog += "INTEGRACION SUBIDA:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
			
			object.each{
				
				String fecha =  it.fechaExamen.substring(6, 10)+"-"+it.fechaExamen.substring(3, 5)+"-"+it.fechaExamen.substring(0, 2);
				
				//resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "PAAV", it.PAAV, fecha);
				coins =  new HashMap < String, Object > ();
				coins.put("context", context)
				coins.put("idBanner", it.IDBANNER)
				coins.put("codeScore", "PAAV")
				coins.put("score", it.PAAV)
				coins.put("fecha", fecha)
				machine.add(coins)
				//errorLog += "INTEGRACION SUBIDA PAAV:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
				
				//resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "PAAN", it.PAAN, fecha);
				coins =  new HashMap < String, Object > ();
				coins.put("context", context)
				coins.put("idBanner", it.IDBANNER)
				coins.put("codeScore", "PAAN")
				coins.put("score", it.PAAN)
				coins.put("fecha", fecha)
				machine.add(coins)
				//errorLog += ", INTEGRACION SUBIDA PAAN:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
				
				//resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "PARA", it.PARA, fecha);
				coins =  new HashMap < String, Object > ();
				coins.put("context", context)
				coins.put("idBanner", it.IDBANNER)
				coins.put("codeScore", "PARA")
				coins.put("score", it.PARA)
				coins.put("fecha", fecha)
				machine.add(coins)
				//errorLog += ", INTEGRACION SUBIDA PARA:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
				
				if(it.tipoExamen.toString().equals("KP")) {
					//resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "MLEX", it.MLEX, fecha);
					coins =  new HashMap < String, Object > ();
					coins.put("context", context)
					coins.put("idBanner", it.IDBANNER)
					coins.put("codeScore", "MLEX")
					coins.put("score", it.MLEX)
					coins.put("fecha", fecha)
					machine.add(coins)
					//errorLog += ", INTEGRACION SUBIDA MLEX:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					//resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "CLEX", it.CLEX, fecha);
					coins =  new HashMap < String, Object > ();
					coins.put("context", context)
					coins.put("idBanner", it.IDBANNER)
					coins.put("codeScore", "CLEX")
					coins.put("score", it.CLEX)
					coins.put("fecha", fecha)
					machine.add(coins)
					//errorLog += ", INTEGRACION SUBIDA CLEX:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					//resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "HLEX", it.HLEX, fecha);
					coins =  new HashMap < String, Object > ();
					coins.put("context", context)
					coins.put("idBanner", it.IDBANNER)
					coins.put("codeScore", "HLEX")
					coins.put("score", it.HLEX)
					coins.put("fecha", fecha)
					machine.add(coins)
					//errorLog += ", INTEGRACION SUBIDA HLEX:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					/*resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "LA01", it.LA1, fecha);
					errorLog += ", INTEGRACION SUBIDA LA1:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "LA02", it.LA2, fecha);
					errorLog += ", INTEGRACION SUBIDA LA2:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "LA03", it.LA3, fecha);
					errorLog += ", INTEGRACION SUBIDA LA3:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "LA04", it.LA4, fecha);
					errorLog += ", INTEGRACION SUBIDA LA4:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "PG01", it.PG1, fecha);
					errorLog += ", INTEGRACION SUBIDA PG1:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "PG02", it.PG2, fecha);
					errorLog += ", INTEGRACION SUBIDA PG2:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "PG03", it.PG3, fecha);
					errorLog += ", INTEGRACION SUBIDA PG3:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "PG04", it.PG4, fecha);
					errorLog += ", INTEGRACION SUBIDA PG4:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "PG05", it.PG5, fecha);
					errorLog += ", INTEGRACION SUBIDA PG5:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "PV01", it.PV1, fecha);
					errorLog += ", INTEGRACION SUBIDA PV1:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "PV04", it.PV4, fecha);
					errorLog += ", INTEGRACION SUBIDA PV4:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "LEO1", it.LEO1, fecha);
					errorLog += ", INTEGRACION SUBIDA LEO1:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "LEO3", it.LEO3, fecha);
					errorLog += ", INTEGRACION SUBIDA LEO3:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "LEO4", it.LEO4, fecha);
					errorLog += ", INTEGRACION SUBIDA LEO4:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "LEO5", it.LEO5, fecha);
					errorLog += ", INTEGRACION SUBIDA LEO5:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "CIT1", it.CIT1, fecha);
					errorLog += ", INTEGRACION SUBIDA CIT1:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "CIT2", it.CIT2, fecha);
					errorLog += ", INTEGRACION SUBIDA CIT2:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "HI01", it.HI1, fecha);
					errorLog += ", INTEGRACION SUBIDA HI1:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "HI02", it.HI2, fecha);
					errorLog += ", INTEGRACION SUBIDA HI2:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "HI03", it.HI3, fecha);
					errorLog += ", INTEGRACION SUBIDA HI3:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "HI04", it.HI4, fecha);
					errorLog += ", INTEGRACION SUBIDA HI4:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "HI05", it.HI5, fecha);
					errorLog += ", INTEGRACION SUBIDA HI5:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "HI06", it.HI6, fecha);
					errorLog += ", INTEGRACION SUBIDA HI6:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();*/
				}
				resultado.setSuccess(true);
				
				
			}
			resultado = new BannerDAO().multiThread(machine)
			
		}catch(Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}
		
		return resultado;
	}
	
	
	public Result postUpdateINVP(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Boolean executar = false;
		
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				
				con.setAutoCommit(false)
				pstm = con.prepareStatement(Statements.UPDATE_INVP, Statement.RETURN_GENERATED_KEYS)
				
				pstm.setString(1,object.invp);
				pstm.setString(2,object.fecha);
				pstm.setString(3,object.IDBANNER);
				pstm.executeUpdate();
					
				resultado.setSuccess(true)
				con.commit();
				//resultado.setData(estatus)
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
	
	
	
	public Result postValidarUsuario(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				
				List<Map<String, Object>> estatus = new ArrayList<Map<String, Object>>();
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				
				 String[] idBanner = object.IDBANNER.split(",");
				 String[] fecha = object.FECHA.split(",");
				 String[] idSesion = object.IDSESION.split(",");
				 for(int j = 0; j < idBanner.size(); ++j) {
					 pstm = con.prepareStatement(Statements.GET_EXISTE_Y_DATOS_DUPLICADOS.replace("[VALOR]",idBanner[j]).replace("[FECHA]", fecha[j]).replace("[IDSESION]", idSesion[j]))
					 rs= pstm.executeQuery();
					 columns = new LinkedHashMap<String, Object>();
					 columns.put("idBanner", idBanner[j] );
					 columns.put("Registrado",false);
					 columns.put("Existe",false);
					 //columns.put("EstaEnCarga",false);
					 columns.put("mismaFecha",false);
					 columns.put("AA",false);
					 columns.put("puede",false);
					 columns.put("sc", false);
					 columns.put("caseId", '');
					 if(rs.next()) {
						 columns.put("Registrado",isNullOrEmpty(rs.getString("idbanner")))
						 columns.put("Existe",isNullOrEmpty(rs.getString("dsbanner")))
						 //columns.put("EstaEnCarga",rs.getBoolean("CC"))
						 columns.put("mismaFecha",(rs.getInt("mismafecha") == 1?true:false))
						 columns.put("AA",(rs.getBoolean("AA")))
						 columns.put("puede",(rs.getBoolean("puede")))
						 columns.put("sc",(rs.getBoolean("SC")))
						 
					 }
					 pstm = con.prepareStatement("SELECT ds.caseid FROM detallesolicitud as ds INNER JOIN solicitudDeAdmision as sda ON sda.caseid = ds.caseid::integer WHERE sda.correoelectronico NOT LIKE '%(rechazado)%' and  ds.idbanner = ${idBanner[j]} limit 1");
					 rs= pstm.executeQuery();
					 if(rs.next()) {
						 columns.put("caseId", rs.getString("caseid"));
					 }
					 estatus.add(columns)
					 
				 }
				
				
				resultado.setSuccess(true)
				resultado.setData(estatus)
				
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
	
	
	public Result postGuardarBitacoraErrores(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Boolean existe = false;
		String errorLog = "";
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				
				List<Map<String, Object>> estatus = new ArrayList<Map<String, Object>>();
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				
				
				//Saca la fecha para la consulta
				Calendar cal = Calendar.getInstance();
				Date date = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				String sDate = formatter.format(date);
				
				 
				 object.each{
					 //revisar si el idBanner esta registrado o no
					 pstm = con.prepareStatement(Statements.GET_BITACORA_ERRORES_EXISTE)
					 pstm.setString(1, it.idBanner);
					 rs= pstm.executeQuery();
					 if(rs.next()) {
						 existe = true;
					 }
					 
					 con.setAutoCommit(false)
					 //segun lo resultado se crea o actualiza el dato del error
					 pstm = con.prepareStatement((existe?Statements.UPDATE_BITACORA_ERRORES_PAA.replace("[IDBANNER]", it.idBanner):Statements.INSERT_BITACORA_ERRORES_PAA), Statement.RETURN_GENERATED_KEYS)
					 pstm.setString(1, it.Error);
					 pstm.setString(2, it.idBanner)
					 pstm.setString(3, it.fechaExamen)
					 pstm.setString(4, sDate);
					 pstm.executeUpdate();
				 }
				
				
				con.commit();
				resultado.setSuccess(true)
				resultado.setData(estatus)
				
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
	
	

	public Boolean isNullOrEmpty(String text) {
		
		if(text?.equals("null") || text?.equals("") || text?.equals(" ") || text?.length() < 1) {
			return false
		}
		return true
	}
	
	//todos los aspirantes que tengan el status de carga y consulta de resultados

	public Result getAspirantesSinPAA(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", bachillerato="", campus="", programa="", ingreso="", estado ="", tipoalumno ="", orderby="ORDER BY ", errorlog=""
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			
			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(CatCampus rowGrupo : lstCatCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}
			
			assert object instanceof Map;
			where+=" WHERE sda.iseliminado=false and (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado = false ) AND ((select count(persistenceid) from IMPORTACIONPAA WHERE idbanner = da.idbanner and sesion_pid::INTEGER = sesiones.persistenceid ) = 0 )  "
			if(object.campus != null){
				where+=" AND LOWER(campus.grupoBonita) = LOWER('"+object.campus+"') "
			}			

			where+=" AND (sda.ESTATUSSOLICITUD = 'Carga y consulta de resultados' OR sda.ESTATUSSOLICITUD = 'Elección de pruebas calendarizado' OR sda.ESTATUSSOLICITUD = 'Ya se imprimió su credencial')"				
				
			
			if(lstGrupo.size()>0) {
				campus+=" AND ("
			}
			for(Integer i=0; i<lstGrupo.size(); i++) {
				String campusMiembro=lstGrupo.get(i);
				campus+="campus.descripcion='"+campusMiembro+"'"
				if(i==(lstGrupo.size()-1)) {
					campus+=") "	
				}
				else {
					campus+=" OR "
				}
			}
			
			errorlog+="campus" + campus;
				errorlog+="object.lstFiltro" +object.lstFiltro
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				
				String SSA = "";
				pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
				rs= pstm.executeQuery();
				if(rs.next()) {
					SSA = rs.getString("valor")
				}
				
				String consulta = Statements.GET_ASPIRANTES_SIN_PAA
				
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
                    errorlog=consulta+" 1";
					switch(filtro.get("columna")) {
					
					case "NOMBRE,EMAIL,CURP":
						errorlog+="NOMBRE,EMAIL,CURP"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROGRAMA,PERÍODO DE INGRESO,CAMPUS INGRESO":
						errorlog+="PROGRAMA, PERÍODO DE INGRESO, CAMPUS INGRESO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;
						
					case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
						errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						/*where +=" ( LOWER(estado.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						*/
						where +="( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +="  OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ULTIMA MODIFICACION":
						errorlog+="FECHAULTIMAMODIFICACION"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" (LOWER(fechaultimamodificacion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where +=" OR to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') ";
						where+="LIKE LOWER('%[valor]%'))";

						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
				//filtrado utilizado en lista roja y rechazado
					case "NOMBRE,EMAIL,CURP":
						errorlog+="NOMBRE,EMAIL,CURP"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CAMPUS,PROGRAMA,INGRESO":
						errorlog+="PROGRAMA,INGRESO,CAMPUS"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;
						
					case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
						errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +="( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						/*
						where +=" OR LOWER(sda.estadoextranjero) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						*/
						where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ESTATUS,TIPO":
						errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(R.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "INDICADORES":
						errorlog+="INDICADORES"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						
						where +=" ( LOWER(R.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(TA.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(TAL.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;
						
					// filtrados normales	
					case "NÚMERO DE SOLICITUD":
                        errorlog+="SOLICITUD"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(CAST(sda.caseid AS varchar)) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
						
					case "IDBANNER":
						errorlog+="IDBANNER"
						tipoalumno +=" AND LOWER(da.idbanner) ";
						if(filtro.get("operador").equals("Igual a")) {
							tipoalumno+="=LOWER('[valor]')"
						}else {
							tipoalumno+="LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ID BANNER":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(da.idbanner) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ID,SESION":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(SESIONES.nombre) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(SESIONES.persistenceid||'') like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						
					
					} 
					
				}
				errorlog=consulta+" 2";
				switch(object.orderby) {
					case "RESIDEICA":
					orderby+="residensia";
					break;
					case "TIPODEADMISION":
					orderby+="tipoadmision";
					break;
					case "TIPODEALUMNO":
					orderby+="tipoDeAlumno";
					break;
					case "FECHAULTIMAMODIFICACION":
					orderby+="sda.fechaultimamodificacion";
					break;
					case "NOMBRE":
					orderby+="sda.apellidopaterno";
					break;
					case "EMAIL":
					orderby+="sda.correoelectronico";
					break;
					case "CURP":
					orderby+="sda.curp";
					break;
					case "CAMPUS":
					orderby+="campus.DESCRIPCION"
					break;
					case "PREPARATORIA":
					orderby+="prepa.DESCRIPCION"
					break;
					case "PROGRAMA":
					orderby+="gestionescolar.NOMBRE"
					break;
					case "INGRESO":
					orderby+="periodo.DESCRIPCION"
					break;
					case "PROCEDENCIA":
					orderby +="CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END";
					break;
					case "PROMEDIO":
					orderby+="sda.PROMEDIOGENERAL";
					break;
					case "ESTATUS":
					orderby+="sda.ESTATUSSOLICITUD";
					break;
					case "TIPO":
					orderby+="da.TIPOALUMNO";
					break;
					case "TELEFONO":
					orderby+="sda.telefonocelular";
					break;
					case "IDBANNER":
					orderby+="da.idbanner";
					break;
					case "SESION":
					orderby+="SESIONES.nombre";
					break;
					
					default:
					orderby+="sda.persistenceid"
					break;
				}
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[CAMPUS]", campus)
				consulta=consulta.replace("[PROGRAMA]", programa)
				consulta=consulta.replace("[INGRESO]", ingreso)
				consulta=consulta.replace("[ESTADO]", estado)
				consulta=consulta.replace("[BACHILLERATO]", bachillerato)
				consulta=consulta.replace("[TIPOALUMNO]", tipoalumno)
				where+=" "+campus +" "+programa +" " + ingreso + " " + estado +" "+bachillerato +" "+tipoalumno
				
				consulta=consulta.replace("[WHERE]", where);
				errorlog=consulta+" 5";
				pstm = con.prepareStatement(consulta.replace("sesiones.persistenceid as id,CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso,periodo.fechafin as periodofin, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid,  da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura,SESIONES.nombre,da.cbcoincide as Lexium", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("GROUP BY prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion,periodo.fechafin, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,da.tipoalumno,sda.caseid,sda.telefonocelular,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid, SESIONES.nombre, SESIONES.persistenceid,da.cbcoincide",""))
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				errorlog=consulta+" 8";
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
							String encoded = "";
							try {
								String urlFoto = rs.getString("urlfoto");
								if(urlFoto != null && !urlFoto.isEmpty()) {
									columns.put("fotografiab64", base64Imagen((rs.getString("urlfoto") + SSA)) );
									//columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
								}else {
									List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
									for(Document doc : doc1) {
										encoded = "../API/formsDocumentImage?document="+doc.getId();
										columns.put("fotografiab64", encoded);
									}	
								}
								
							}catch(Exception e) {
								columns.put("fotografiab64", "");
								errorlog+= ""+e.getMessage();
							}
						}
					}
	
					rows.add(columns);
				}
				errorlog=consulta+" 9";
				resultado.setSuccess(true)
				
				
				resultado.setData(rows)
				
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
	
	
	public Result getAspirantePAA(String idBanner,String persistenceid, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			
			closeCon = validarConexion();
			
			errorlog+=(persistenceid.equals("") || persistenceid.equals(null) )  ?Statements.GET_PAA_BY_IDBANNER_SIN_PERSISTENCE:Statements.GET_PAA_BY_IDBANNER
			pstm = con.prepareStatement( (persistenceid.equals("") || persistenceid.equals(null) )  ?Statements.GET_PAA_BY_IDBANNER_SIN_PERSISTENCE:Statements.GET_PAA_BY_IDBANNER);
			pstm.setString(1, idBanner)
			if(!(persistenceid.equals("") || persistenceid.equals(null) )) {
				errorlog+="el valor es:"+persistenceid+" validacion"
				pstm.setLong(2, Long.parseLong(persistenceid))
			}
			rs= pstm.executeQuery();
			
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				info.add(columns)
			}
			
			resultado.setSuccess(true);
			resultado.setData(info);
			
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

	
	// todos los aspirantes que tenga cargado el PAA
	public Result postListaAspirantePAA ( Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", bachillerato="", campus="", programa="", ingreso="", estado ="", tipoalumno ="", orderby="ORDER BY ", errorlog=""
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			
			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(CatCampus rowGrupo : lstCatCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}
			
			assert object instanceof Map;
			where+=" WHERE sda.iseliminado=false and PAA.idBanner is not null and (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado = false ) ";
			if(object.campus != null){
				where+=" AND LOWER(campus.grupoBonita) = LOWER('"+object.campus+"') "
			}			

			//where+=" AND (sda.ESTATUSSOLICITUD = 'Carga y consulta de resultados' OR sda.ESTATUSSOLICITUD = 'Elección de pruebas calendarizado' OR sda.ESTATUSSOLICITUD = 'Ya se imprimió su credencial' )"				
			if(object.completos) {
				where += " AND (PAA.INVP IS NOT NULL AND PAA.INVP <> '') "
			}else {
				where += " AND (PAA.INVP IS NULL OR PAA.INVP = '')"
			}
			
			if(lstGrupo.size()>0) {
				campus+=" AND ("
			}
			for(Integer i=0; i<lstGrupo.size(); i++) {
				String campusMiembro=lstGrupo.get(i);
				campus+="campus.descripcion='"+campusMiembro+"'"
				if(i==(lstGrupo.size()-1)) {
					campus+=") "	
				}
				else {
					campus+=" OR "
				}
			}
			
			errorlog+="campus" + campus;
				errorlog+="object.lstFiltro" +object.lstFiltro
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				
				String SSA = "";
				pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
				rs= pstm.executeQuery();
				if(rs.next()) {
					SSA = rs.getString("valor")
				}
				
				String consulta= Statements.GET_ASPIRANTES_CON_PAA;
				
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
                    errorlog=consulta+" 1";
					switch(filtro.get("columna")) {
					
					case "NOMBRE,EMAIL,CURP":
						errorlog+="NOMBRE,EMAIL,CURP"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROGRAMA,PERÍODO DE INGRESO,CAMPUS INGRESO":
						errorlog+="PROGRAMA, PERÍODO DE INGRESO, CAMPUS INGRESO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;
						
					case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
						errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						/*where +=" ( LOWER(estado.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						*/
						where +="( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +="  OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ULTIMA MODIFICACION":
						errorlog+="FECHAULTIMAMODIFICACION"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" (LOWER(fechaultimamodificacion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where +=" OR to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') ";
						where+="LIKE LOWER('%[valor]%'))";

						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PUNTUACIONES":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +="( LOWER(PAA.PAAN) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +="  OR LOWER(PAA.PAAV) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(PAA.PARA) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(PAA.INVP) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
						
						case "FECHA DEL EXAMEN, FECHA ULTIMA MODIFICACION":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +="( LOWER(PAA.fechaRegistro) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +="OR LOWER(PAA.fechaexamen) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;
				//filtrado utilizado en lista roja y rechazado
					case "NOMBRE,EMAIL,CURP":
						errorlog+="NOMBRE,EMAIL,CURP"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CAMPUS,PROGRAMA,INGRESO":
						errorlog+="PROGRAMA,INGRESO,CAMPUS"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;
						
					case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
						errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +="( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						/*
						where +=" OR LOWER(sda.estadoextranjero) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						*/
						where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ESTATUS,TIPO":
						errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(R.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "INDICADORES":
						errorlog+="INDICADORES"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						
						where +=" ( LOWER(R.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(TA.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(TAL.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;
						
					// filtrados normales	
					case "NÚMERO DE SOLICITUD":
                        errorlog+="SOLICITUD"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(CAST(sda.caseid AS varchar)) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
						
					case "IDBANNER":
						errorlog+="IDBANNER"
						tipoalumno +=" AND LOWER(da.idbanner) ";
						if(filtro.get("operador").equals("Igual a")) {
							tipoalumno+="=LOWER('[valor]')"
						}else {
							tipoalumno+="LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ID BANNER":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(da.idbanner) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ID,SESION":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
							where +=" ( LOWER(sesion.nombre) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
							
							where +=" OR LOWER(sesion.persistenceid||'') like lower('%[valor]%')) ";
							where = where.replace("[valor]", filtro.get("valor"))
					break;
					
					} 
					
					
				
				
					
				}
				errorlog=consulta+" 2";
				switch(object.orderby) {
					case "RESIDEICA":
					orderby+="residensia";
					break;
					case "TIPODEADMISION":
					orderby+="tipoadmision";
					break;
					case "TIPODEALUMNO":
					orderby+="tipoDeAlumno";
					break;
					case "FECHAULTIMAMODIFICACION":
					orderby+="sda.fechaultimamodificacion";
					break;
					case "NOMBRE":
					orderby+="sda.apellidopaterno";
					break;
					case "EMAIL":
					orderby+="sda.correoelectronico";
					break;
					case "CURP":
					orderby+="sda.curp";
					break;
					case "CAMPUS":
					orderby+="campus.DESCRIPCION"
					break;
					case "PREPARATORIA":
					orderby+="prepa.DESCRIPCION"
					break;
					case "PROGRAMA":
					orderby+="gestionescolar.NOMBRE"
					break;
					case "INGRESO":
					orderby+="periodo.DESCRIPCION"
					break;
					case "PROCEDENCIA":
					orderby +="CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END";
					break;
					case "PROMEDIO":
					orderby+="sda.PROMEDIOGENERAL";
					break;
					case "ESTATUS":
					orderby+="sda.ESTATUSSOLICITUD";
					break;
					case "TIPO":
					orderby+="da.TIPOALUMNO";
					break;
					case "TELEFONO":
					orderby+="sda.telefonocelular";
					break;
					case "IDBANNER":
					orderby+="da.idbanner";
					break;
					case "PAAN":
					orderby+="PAA.PAAN";
					break;
					case "PAAV":
					orderby+="PAA.PAAV";
					break;
					case "PARA":
					orderby+="PAA.PARA";
					break;
					case "INVP":
					orderby+="PAA.INVP";
					break;
					case "FECHARESULTADO":
					orderby+="TO_DATE(PAA.fechaExamen,'DD-MM-YYYY')";
					break;
					case "FECHAULTIMA":
					orderby+="TO_DATE(PAA.fechaRegistro,'DD-MM-YYYY')";
					break;
					default:
					orderby+="TO_DATE(PAA.fechaRegistro,'DD-MM-YYYY')";
					break;
				}
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[CAMPUS]", campus)
				consulta=consulta.replace("[PROGRAMA]", programa)
				consulta=consulta.replace("[INGRESO]", ingreso)
				consulta=consulta.replace("[ESTADO]", estado)
				consulta=consulta.replace("[BACHILLERATO]", bachillerato)
				consulta=consulta.replace("[TIPOALUMNO]", tipoalumno)
				where+=" "+campus +" "+programa +" " + ingreso + " " + estado +" "+bachillerato +" "+tipoalumno
				
				consulta=consulta.replace("[WHERE]", where);
				errorlog=consulta.replace("sesion.persistenceid as id,sesion.nombre as sesion,CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso,periodo.fechafin AS periodofin, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura, PAA.PARA,PAA.PAAV,PAA.PAAN,PAA.fechaRegistro,PAA.INVP,PAA.fechaExamen,PAA.persistenceid,PAA.LEXIUMPAAN,PAA.LEXIUMPAAV,PAA.LEXIUMPARA,da.cbcoincide as Lexium", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("GROUP BY prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion,periodo.fechafin, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,da.tipoalumno,sda.caseid,sda.telefonocelular,da.observacioneslistaroja,da.observacionesrechazo,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid, PAA.PARA,PAA.PAAV,PAA.PAAN,PAA.fechaRegistro,PAA.INVP,PAA.fechaExamen,PAA.persistenceid,sesion.persistenceid,sesion.nombre,PAA.LEXIUMPAAN,PAA.LEXIUMPAAV,PAA.LEXIUMPARA,da.cbcoincide","")+"¡¡¿¿¿"
				pstm = con.prepareStatement(consulta.replace("sesion.persistenceid as id,sesion.nombre as sesion,CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso,periodo.fechafin AS periodofin, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura, PAA.PARA,PAA.PAAV,PAA.PAAN,PAA.fechaRegistro,PAA.INVP,PAA.fechaExamen,PAA.persistenceid,PAA.LEXIUMPAAN,PAA.LEXIUMPAAV,PAA.LEXIUMPARA,da.cbcoincide as Lexium", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("GROUP BY prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion,periodo.fechafin, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,da.tipoalumno,sda.caseid,sda.telefonocelular,da.observacioneslistaroja,da.observacionesrechazo,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid, PAA.PARA,PAA.PAAV,PAA.PAAN,PAA.fechaRegistro,PAA.INVP,PAA.fechaExamen,PAA.persistenceid,sesion.persistenceid,sesion.nombre,PAA.LEXIUMPAAN,PAA.LEXIUMPAAV,PAA.LEXIUMPARA,da.cbcoincide",""))
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]"," LIMIT ? OFFSET ?")
				errorlog=consulta+" 7";
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				errorlog=consulta+" 8";
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
							String encoded = "";
							try {
								String urlFoto = rs.getString("urlfoto");
								if(urlFoto != null && !urlFoto.isEmpty()) {
									columns.put("fotografiab64", base64Imagen((rs.getString("urlfoto") + SSA)) );
									//columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
								}else {
									List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
									for(Document doc : doc1) {
										encoded = "../API/formsDocumentImage?document="+doc.getId();
										columns.put("fotografiab64", encoded);
									}	
								}
								
							}catch(Exception e) {
								columns.put("fotografiab64", "");
								errorlog+= ""+e.getMessage();
							}
						}
					}
	
					rows.add(columns);
				}
				errorlog=consulta+" 9";
				resultado.setSuccess(true)
				
				
				resultado.setData(rows)
				
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
	
	
	
	public Result postExcelAspirantesPAA(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams;
			//String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("ImportacionEAC");
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER)
			
			dataResult = postListaAspirantePAA(0,0,jsonData,context);
			if (dataResult.success) {
				lstParams = dataResult.getData();
				
			} else {
				errorLog += ""+dataResult
				throw new Exception("No encontro datos");
			}
			//titulos que manejan la mayoria de los exceles
			Row titleRow = sheet.createRow(++rowCount);
			
			Cell cellReporte = titleRow.createCell(1);
			cellReporte.setCellValue("Reporte:");
			cellReporte.setCellStyle(style);
			
			Cell cellTitle = titleRow.createCell(2);
			cellTitle.setCellValue("ASPIRANTES CON PUNTUACIÓN");
			
			Cell cellFecha = titleRow.createCell(4);
			cellFecha.setCellValue("Fecha:");
			cellFecha.setCellStyle(style);
			Calendar cal = Calendar.getInstance();
			Date date = cal.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

			String sDate = formatter.format(date);
			Cell cellFechaData = titleRow.createCell(5);
			cellFechaData.setCellValue(sDate);

			Row blank = sheet.createRow(++rowCount);

			Cell cellusuario = blank.createCell(4);
			cellusuario.setCellValue("Usuario:");
			cellusuario.setCellStyle(style);
			Cell cellusuarioData = blank.createCell(5);
			cellusuarioData.setCellValue(object.usuarioNombre);
			
			++rowCount;
			Row espacio = sheet.createRow(++rowCount);
			
			def titulos = ["Id Banner","Nombre","Email","Curp","Campus","Programa","Periodo","Procedencia","Preparatoria","Promedio","Id sesión","Sesión","MLEX","PAAN","CLEX","PAAV","HLEX","PARA","INVP","Fecha del examen","Fecha ultima modificación"]
			Row headersRow = sheet.createRow(rowCount);
			++rowCount;
			List<Cell> header = new ArrayList<Cell>();
			for(int i = 0; i < titulos.size(); ++i) {
				header.add(headersRow.createCell(i))
				header[i].setCellValue(titulos.get(i))
				header[i].setCellStyle(style)
			}
			
			CellStyle bodyStyle = workbook.createCellStyle();
			bodyStyle.setWrapText(true);
			bodyStyle.setAlignment(HorizontalAlignment.CENTER);
			
			
			def info = ["idbanner","nombre","correoelectronico","curp","campus","licenciatura","ingreso","procedencia","preparatoria","promediogeneral","id","sesion","lexiumpaan","paan","lexiumpaav","paav","lexiumpara","para","invp","fechaexamen","fecharegistro"]
			List<Cell> body;
			for (int i = 0; i < lstParams.size(); ++i){
				Row row = sheet.createRow(rowCount);
				++rowCount;
				body = new ArrayList<Cell>()
				for(int j=0;  j < info.size(); ++j) {
					body.add(row.createCell(j))
					if(info.get(j).equals("nombre")){
						body[j].setCellValue(lstParams[i].apellidopaterno + " " + lstParams[i].apellidomaterno+ " " + lstParams[i].primernombre + " " + lstParams[i].segundonombre)
					}else{
						body[j].setCellValue(lstParams[i][info.get(j)].toString())
					}
					body[j].setCellStyle(bodyStyle);
					
				}
				
			}
			
			for(int i=0; i<=20; ++i) {
					sheet.autoSizeColumn(i);
			}
			
			
			FileOutputStream outputStream = new FileOutputStream("ReportImportacionEAC.xls");
			workbook.write(outputStream);

			List < Object > lstResultado = new ArrayList < Object > ();
			lstResultado.add( new ListadoDAO().encodeFileToBase64Binary("ReportImportacionEAC.xls"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			
			
		}catch(Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	public Result PostUpdateDeleteCatEscalaEAC(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				pstm = con.prepareStatement(Statements.UPDATE_CATESCALAEAC, Statement.RETURN_GENERATED_KEYS)
				pstm.setString(1, object.escala);
				pstm.setString(2, object.equivalenteKP);
				pstm.setBoolean(3,object.isEliminado);
				pstm.setInt(4,Integer.valueOf(object.persistenceId));
				
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
	
	public Result getCatEscalaEAC(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", orderby = "ORDER BY ", errorLog = ""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			//assert object instanceof List;
			String consulta = Statements.GET_CATESCALAEAC
			closeCon = validarConexion();
			where += " WHERE isEliminado = false";
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {

				switch (filtro.get("columna")) {

					case "USUARIO CREACION":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(USUARIOCREACION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					case "FECHA CREACIÓN":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(FECHACREACION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					
					case "ESCALA":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(escala) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "EQUIVALENTE":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(equivalentekp) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "EQUIVALENTEKP":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(equivalentekp) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "EQUIVALENTE KP":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(equivalentekp) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;

				}
			}
			switch (object.orderby) {
				case "ISELIMINADO":
					orderby += "isEliminado";
					break;
				case "USUARIOCREACION":
					orderby += "usuarioCreacion";
					break;
				case "EQUIVALENTE":
					orderby += "equivalenteKP";
					break;
				case "ESCALA":
					orderby += "escala";
					break;
				case "FECHA CREACIÓN":
					orderby += "fechaCreacion";
					break;
				default:
					orderby += "persistenceid"

					break;
			}
			errorLog += "orderby"
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);
			pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			
			errorLog += consulta
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)
			rs = pstm.executeQuery()
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()) {
				
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
					columns.put("escala", rs.getString("escala"));
					columns.put("equivalenteKP", rs.getString("equivalenteKP"));
					columns.put("isEliminado", rs.getBoolean("isEliminado"));
					columns.put("fechaCreacion", rs.getString("fechaCreacion"));
					columns.put("usuarioCreacion", rs.getString("usuarioCreacion"));
					columns.put("persistenceId", rs.getString("persistenceId"));
					columns.put("persistenceVersion", rs.getString("persistenceVersion"));
					
					rows.add(columns);
			}
			
			errorLog += " paso el listado";
			resultado.setSuccess(true)
			resultado.setError(errorLog)
			resultado.setData(rows)

		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result cargarEACBANNER( RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
		Boolean closeCon = false;
		try {
			
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_EAC_BANNER);
			rs= pstm.executeQuery();
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				
				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toUpperCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			Result resultado2 = new Result();
			resultado2 = subirEAC_BannerEthos(rows,context);
			errorLog +=" ||Resultado BannerEthos:"+ resultado2+resultado2.isSuccess()
			if(resultado2.isSuccess()) {
				
				resultado2 = updateEAC(rows,resultado2.getAdditional_data(),context);
				errorLog +="||Resultado update:"+ resultado2+" succes:"+resultado2.isSuccess();
				
			}
			resultado.setSuccess(true)
			resultado.setError(errorLog);
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError(errorLog);
			resultado.setError_info(e.getMessage())
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result cargarEACBANNER_IDBANNER(String idbanner, RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
		Boolean closeCon = false;
		try {
			closeCon = validarConexion();
		
			pstm = con.prepareStatement(Statements.GET_EAC_BANNER_ESPECIFICOS);
			pstm.setString(1, idbanner)
			rs= pstm.executeQuery();
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				
				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toUpperCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			Result resultado2 = new Result();
			resultado2 = subirEAC_BannerEthos(rows,context);
			errorLog +=" ||Resultado BannerEthos:"+ resultado2+resultado2.isSuccess()
			if(resultado2.isSuccess()) {
				
				resultado2 = updateEAC(rows,resultado2.getAdditional_data(),context);
				errorLog +="||Resultado update:"+ resultado2+" succes:"+resultado2.isSuccess();
				
			}
			resultado.setSuccess(true)
			resultado.setError(errorLog);
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError(errorLog);
			resultado.setError_info(e.getMessage())
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result subirEAC_BannerEthos(List<Map<String, Object>> list, RestAPIContext context) {
		
		Result resultado = new Result();
		String errorLog = "";
		List<String> idbanner = new ArrayList<>();
		try {
			
			
			for (Map<String, Object> it : list) {
				String fecha =  it.FECHAEXAMEN.substring(6, 10)+"-"+it.FECHAEXAMEN.substring(3, 5)+"-"+it.FECHAEXAMEN.substring(0, 2);
				//fecha +="T12:00:00+00:00";
				resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "PAAV", it.PAAV, fecha);
				//errorLog += "PAAV:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
				
				resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "PAAN", it.PAAN, fecha);
				//errorLog += " , PAAN:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
				
				resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "PARA", it.PARA, fecha);
				errorLog += " , PARA:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
				
				if(it.TIPOEXAMEN.toString().equals("KP")) {
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "MLEX", it.MLEX, fecha);
					//errorLog += ", MLEX:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "CLEX", it.CLEX, fecha);
					//errorLog += ", CLEX:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
					resultado = new BannerDAO().integracionBannerEthos(context, it.IDBANNER, "HLEX", it.HLEX, fecha);
					//errorLog += ", HLEX:"+resultado.isSuccess()+"ERROR:"+resultado.getError()+"ERROR_INFO:"+resultado.getError_info();
					
				}
				
				if(!resultado.isSuccess()) {
					errorLog+=" || Entro ||"
					idbanner.add("${it.IDBANNER}");
				}
				//idbanner.add("${resultado?.getInfo()}");
				
			}
			
			resultado.setSuccess(true);
			resultado.setError_info(errorLog)
			resultado.setAdditional_data(idbanner)
			
		}catch(Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog)
			resultado.setAdditional_data(idbanner)
		}
		
		return resultado;
	}
	
	
	
	public Result updateEAC(List<Map<String, Object>> list,List<?> list2,RestAPIContext context) {
		Result resultado = new Result();
		Result dataResult = new Result();
		String errorLog = "";
		Boolean closeCon = false,rollback = false;
		try {
			
			if(list.size() > 0) {
				rollback = true;
				closeCon = validarConexion();
				con.setAutoCommit(false);
				String ids="";
				
				for (Map<String, Object> it : list) {
					Boolean actualizar = true;
					for (String it2 : list2) {
						if(it2 == it.IDBANNER && actualizar) {
							actualizar=false;
							errorLog+=" ||NO:"+it.IDBANNER
						}
					}
					if(actualizar) {
						errorLog+="||SI:"+it.IDBANNER
						ids+= (ids.length() == 0?"":",") + it.PERSISTENCEID;
						
						pstm = con.prepareStatement(Statements.INSERT_BITACORA_INTEGRACION_EAC)
						pstm.setLong(1,Long.valueOf(it.PERSISTENCEID));
						pstm.setLong(2,Long.valueOf(it.CASEID));
						pstm.setString(3,it.ESTATUSSOLICITUD);
						pstm.setString(4,it.PARA);
						pstm.setString(5,it.PAAV);
						pstm.setString(6,it.PAAN);
						pstm.setString(7,it.MLEX);
						pstm.setString(8,it.CLEX);
						pstm.setString(9,it.HLEX);
						pstm.setString(10,context.getApiSession().getUserName());
						pstm.executeUpdate();
					}
					
				}
				if(ids.length() > 0) {
					pstm = con.prepareStatement(Statements.UPDATE_IMPORTACIONPAA_BANNER.replace('[VALOR]', "${ids}"))
					pstm.executeUpdate();					
				}
				
				con.commit();
			}
			
			resultado.setSuccess(true)
			resultado.setError(errorLog);
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError(errorLog);
			resultado.setError_info(e.getMessage())
			
			if(rollback) {
				con.rollback();				
			}
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result bitacoraIntegracionEAC ( Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", bachillerato="", campus="", programa="", ingreso="", estado ="", tipoalumno ="", orderby="ORDER BY ", errorlog="",consulta="";
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			assert object instanceof Map;
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			
			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(CatCampus rowGrupo : lstCatCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}
			
			where+=" WHERE sda.iseliminado=false and (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado = false ) ";
			if(object.completos) {
				consulta= Statements.GET_ASPIRANTES_INTEGRADOS_BANNER
			}else {
				consulta= Statements.GET_ASPIRANTES_SIN_INTEGRADOS_BANNER;
				where +=" AND (PAA.inBanner is NULL OR PAA.inBanner is false )"
			}
			
			if(object.campus != null){
				where+=" AND LOWER(campus.grupoBonita) = LOWER('"+object.campus+"') "
			}
			
			if(lstGrupo.size()>0) {
				campus+=" AND ("
			}
			for(Integer i=0; i<lstGrupo.size(); i++) {
				String campusMiembro=lstGrupo.get(i);
				campus+="campus.descripcion='"+campusMiembro+"'"
				if(i==(lstGrupo.size()-1)) {
					campus+=") "
				}
				else {
					campus+=" OR "
				}
			}
			
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs= pstm.executeQuery();
			if(rs.next()) {
				SSA = rs.getString("valor")
			}
			
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				errorlog=consulta+" 1";
				switch(filtro.get("columna")) {
				
				case "NOMBRE,EMAIL,CURP":
					errorlog+="NOMBRE,EMAIL,CURP"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(sda.curp) like lower('%[valor]%') ) ";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "PROGRAMA,PERÍODO DE INGRESO,CAMPUS INGRESO":
					errorlog+="PROGRAMA, PERÍODO DE INGRESO, CAMPUS INGRESO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					
					break;
					
				case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
					errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					/*where +=" ( LOWER(estado.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					*/
					where +="( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +="  OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				case "ULTIMA MODIFICACION":
					errorlog+="FECHAULTIMAMODIFICACION"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" (LOWER(fechaultimamodificacion) ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=LOWER('[valor]')"
					}else {
						where+="LIKE LOWER('%[valor]%')"
					}
					where +=" OR to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') ";
					where+="LIKE LOWER('%[valor]%'))";

					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "PUNTUACIONES":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +="( LOWER(PAA.PAAN) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +="  OR LOWER(PAA.PAAV) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(PAA.PARA) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(PAA.INVP) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
					
				case "FECHA DEL EXAMEN, FECHA ULTIMA MODIFICACION":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +="( LOWER(PAA.fechaRegistro) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +="OR LOWER(PAA.fechaexamen) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					
					break;
					
				case "FECHA DE REGISTRO EN EAC":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" LOWER(PAA.fechaRegistro) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					break;
			//filtrado utilizado en lista roja y rechazado
				case "NOMBRE,EMAIL,CURP":
					errorlog+="NOMBRE,EMAIL,CURP"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(sda.curp) like lower('%[valor]%') ) ";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "CAMPUS,PROGRAMA,INGRESO":
					errorlog+="PROGRAMA,INGRESO,CAMPUS"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					
					break;
					
				case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
					errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +="( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					/*
					where +=" OR LOWER(sda.estadoextranjero) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					*/
					where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "ESTATUS,TIPO":
					errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(R.descripcion) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "FECHA DE INTEGRACION, USUARIO QUE INTEGRO":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(Bitacora.fechaSubida) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(bitacora.usuarioSubio) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "INDICADORES":
					errorlog+="INDICADORES"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					
					where +=" ( LOWER(R.descripcion) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(TA.descripcion) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(TAL.descripcion) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					
					break;
					
				// filtrados normales
				case "NÚMERO DE SOLICITUD":
					errorlog+="SOLICITUD"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" LOWER(CAST(sda.caseid AS varchar)) ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=LOWER('[valor]')"
					}else {
						where+="LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "IDBANNER":
					errorlog+="IDBANNER"
					tipoalumno +=" AND LOWER(da.idbanner) ";
					if(filtro.get("operador").equals("Igual a")) {
						tipoalumno+="=LOWER('[valor]')"
					}else {
						tipoalumno+="LIKE LOWER('%[valor]%')"
					}
					tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
					break;
					
				case "ID BANNER":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" LOWER(da.idbanner) ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=LOWER('[valor]')"
					}else {
						where+="LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				case "ID,SESION":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
						where +=" ( LOWER(sesion.nombre) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sesion.persistenceid||'') like lower('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
				break;
				
				}
				
				
			
			
				
			}
			
			switch(object.orderby) {
				case "RESIDEICA":
				orderby+="residensia";
				break;
				case "TIPODEADMISION":
				orderby+="tipoadmision";
				break;
				case "TIPODEALUMNO":
				orderby+="tipoDeAlumno";
				break;
				case "FECHAULTIMAMODIFICACION":
				orderby+="sda.fechaultimamodificacion";
				break;
				case "NOMBRE":
				orderby+="sda.apellidopaterno";
				break;
				case "EMAIL":
				orderby+="sda.correoelectronico";
				break;
				case "CURP":
				orderby+="sda.curp";
				break;
				case "CAMPUS":
				orderby+="campus.DESCRIPCION"
				break;
				case "PREPARATORIA":
				orderby+="prepa.DESCRIPCION"
				break;
				case "PROGRAMA":
				orderby+="gestionescolar.NOMBRE"
				break;
				case "INGRESO":
				orderby+="periodo.DESCRIPCION"
				break;
				case "PROCEDENCIA":
				orderby +="CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END";
				break;
				case "PROMEDIO":
				orderby+="sda.PROMEDIOGENERAL";
				break;
				case "ESTATUS":
				orderby+="sda.ESTATUSSOLICITUD";
				break;
				case "TIPO":
				orderby+="da.TIPOALUMNO";
				break;
				case "TELEFONO":
				orderby+="sda.telefonocelular";
				break;
				case "IDBANNER":
				orderby+="da.idbanner";
				break;
				case "PAAN":
				orderby+="PAA.PAAN";
				break;
				case "PAAV":
				orderby+="PAA.PAAV";
				break;
				case "PARA":
				orderby+="PAA.PARA";
				break;
				case "INVP":
				orderby+="PAA.INVP";
				break;
				case "FECHARESULTADO":
				orderby+="TO_DATE(PAA.fechaExamen,'DD-MM-YYYY')";
				break;
				case "FECHAULTIMA":
				orderby+="TO_DATE(PAA.fechaRegistro,'DD-MM-YYYY')";
				break;
				default:
				orderby+="TO_DATE(PAA.fechaRegistro,'DD-MM-YYYY')";
				break;
			}
			
				consulta=consulta.replace("[CAMPUS]", campus)
				consulta=consulta.replace("[PROGRAMA]", programa)
				consulta=consulta.replace("[INGRESO]", ingreso)
				consulta=consulta.replace("[ESTADO]", estado)
				consulta=consulta.replace("[BACHILLERATO]", bachillerato)
				consulta=consulta.replace("[TIPOALUMNO]", tipoalumno)
				where+=" "+campus +" "+programa +" " + ingreso + " " + estado +" "+bachillerato +" "+tipoalumno
				
				consulta=consulta.replace("[WHERE]", where);
				
				if(object.completos) {
					pstm = con.prepareStatement(consulta.replace("sesion.persistenceid as id,sesion.nombre as sesion, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso,periodo.fechafin AS periodofin, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, sda.ESTATUSSOLICITUD, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura, Bitacora.PARA,Bitacora.PAAV,Bitacora.PAAN,Bitacora.fechaSubida,PAA.INVP,Bitacora.fechaSubida,PAA.persistenceid,Bitacora.mlex,Bitacora.clex,Bitacora.hlex,da.cbcoincide as Lexium,paa.inBanner, paa.fechaBanner, Bitacora.usuariosubio", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				}else {
					pstm = con.prepareStatement(consulta.replace("sesion.persistenceid as id,sesion.nombre as sesion,sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso,periodo.fechafin AS periodofin, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, sda.ESTATUSSOLICITUD, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura, PAA.PARA,PAA.PAAV,PAA.PAAN,PAA.fechaRegistro,PAA.INVP,PAA.fechaExamen,PAA.persistenceid,PAA.LEXIUMPAAN,PAA.LEXIUMPAAV,PAA.LEXIUMPARA,da.cbcoincide as Lexium,paa.inBanner, paa.fechaBanner", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				}
				
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]"," LIMIT ? OFFSET ?")
				errorlog=consulta+" 7";
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				errorlog=consulta+" 8";
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
							String encoded = "";
							try {
								String urlFoto = rs.getString("urlfoto");
								if(urlFoto != null && !urlFoto.isEmpty()) {
									columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
								}else {
									List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
									for(Document doc : doc1) {
										encoded = "../API/formsDocumentImage?document="+doc.getId();
										columns.put("fotografiab64", encoded);
									}
								}
								
							}catch(Exception e) {
								columns.put("fotografiab64", "");
								errorlog+= ""+e.getMessage();
							}
						}
					}
	
					rows.add(columns);
				}
				errorlog=consulta+" 9";
				resultado.setSuccess(true)
				
				
				resultado.setData(rows)
				
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
	
	public String base64Imagen(String url)  throws Exception {
		String b64 = "";
		if(url.toLowerCase().contains(".jpeg")) {
				b64 = ( "data:image/jpeg;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".png")) {
				b64 = ( "data:image/png;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".jpg")) {
				b64 = ( "data:image/jpg;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".jfif")) {
				b64 = ( "data:image/jfif;base64, "+(new FileDownload().b64Url(url)));
			}
		return  b64
	}
	
}
