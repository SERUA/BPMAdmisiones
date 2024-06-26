package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result
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
import org.bonitasoft.engine.bpm.document.Document


class ResultadoComiteDAO {
	
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
	
	public Result postEliminarResultado(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			
			// REVISO SI EL REGISTRO SE ENCUENTRA ACTIVO, AL ENCONTRAR QUE ESTA ACTIVO BUSCA SI HAY OTRO REGISTRO EXCLUYENDO EL REGISTRO, SI LO ENCUENTRA SIGNIFICA QUE ESE REGISTRO PASARA DE DESACTIVADO A ACTIVADO
			con.setAutoCommit(false);
			
			if(!object.desactivado) {
				String comando = Statements.GET_LAST_RESULTADO_COMITE+" WHERE IDBANNER = '"+object.idBanner+"' AND PERSISTENCEID <> ? ORDER BY PERSISTENCEID DESC LIMIT 1";
				pstm = con.prepareStatement(comando);
				pstm.setLong(1,Long.parseLong(object.persistenceid));
				rs= pstm.executeQuery();
				String nuevoPrincipal = "";
				if(rs.next()) {
					nuevoPrincipal = rs.getString("persistenceid");
				}
				
				if(isNullOrEmpty(nuevoPrincipal)) {
					//SI ENCONTRO OTRO REGISTRO ESTE SE CONVIERTE EN EL NUEVO PRINCIPAL
					comando = Statements.UPDATE_RESULTADO_COMITE_REACTIVAR_BY_PERSISTENCEID;
					pstm = con.prepareStatement(comando);
					pstm.setLong(1,Long.parseLong(nuevoPrincipal));
					pstm.executeUpdate();
					
				}
								
								
			}
			
			//SE ELIMINA EL REGISTRO 
			pstm = con.prepareStatement(Statements.DELETE_RESULTADO_COMITE)
			pstm.setLong(1,Long.parseLong(object.persistenceid));
			pstm.executeUpdate();
			
			
			
			con.commit();
			resultado.setSuccess(true)
		}catch(Exception e) {
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
	
	public Result postDesactivarResultado(String persistenceid, String usuario, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
			closeCon = validarConexion();
			con.setAutoCommit(false)
			pstm = con.prepareStatement(Statements.DELETE_BITACORA_ERRORES_RC, Statement.RETURN_GENERATED_KEYS)
			pstm.executeUpdate();
			con.commit();
			resultado.setSuccess(true)
		}catch(Exception e) {
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
	
	public Result getLimpiarBitacoraErrores(RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
			closeCon = validarConexion();
			con.setAutoCommit(false)
			pstm = con.prepareStatement(Statements.DELETE_BITACORA_ERRORES_RC, Statement.RETURN_GENERATED_KEYS)
			pstm.executeUpdate();
			con.commit();
			resultado.setSuccess(true)
		}catch(Exception e) {
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
	
	
	public Result postGuardarUsuario(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				
				object.each{
				//List<Map<String, Object>> estatus = new ArrayList<Map<String, Object>>();
					con.setAutoCommit(false)
					
					//SE OBTINE LA CARRERA Y LA SESION A LA QUE ESTARA VINCULADO EL RESULTADO
					pstm = con.prepareStatement(Statements.GET_IDS_PARA_RESULTADOCOMITE);
					pstm.setString(1, it.IDBANNER);
					
					rs= pstm.executeQuery();
					Long gestionescolar = 0L, sesion = 0L, countRechazos = 0L;
					while(rs.next()) {
						gestionescolar = rs.getLong("catgestionescolar_pid");
						sesion = rs.getLong("SESIONES_PID");
						countRechazos = rs.getLong("countrechazos");
					}
					
					//DESACTUVA TODOS LOS RESULTADOS QUE TENGA ESE IDBANNER
					pstm = con.prepareStatement(Statements.UPDATE_RESULTADO_COMITE_DESACTIVAR.replace("[IDBANNER]", it.IDBANNER));
					pstm.executeUpdate();
					
					// SE AGREGA EL NUEVO RESULTADO O SE ACTUALIZA UN RESULTADO
					pstm = con.prepareStatement(it.update? Statements.INSERT_RESULTADO_COMITE_MODIFICACION:Statements.INSERT_RESULTADO_COMITE, Statement.RETURN_GENERATED_KEYS);
					
					pstm.setString(1,it.IDBANNER);
					pstm.setString(2,it.decision);
					pstm.setString(3,it.PDP_1);
					pstm.setString(4,it.PDU_1);
					pstm.setString(5,it.SSE_1);
					pstm.setString(6,it.PCDA_1);
					pstm.setString(7,it.PCA_1);
					pstm.setString(8,it.observaciones);
					pstm.setString(9,(it.Periodo));
					pstm.setBoolean(10, it.isAdmitido);
					pstm.setLong(11, gestionescolar);
					pstm.setLong(12, sesion);
					pstm.setString(13,it.usuariocreacion);
					pstm.setBoolean(14, it.isPropedeutico)
					pstm.setLong(15, countRechazos);
					
					
					pstm.executeUpdate();
				}
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
	
	
	
	public Result postGuardarBitacoraErroresRC(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Boolean existe = false;
		Boolean execucion = false;
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
					 //existe es una variable que se asigna en pantalla la cual mientras sea diferente de true, es un error que tiene que ser guardado
					 if(it.existe != true) {
						 errorLog+= "Entro"
						 //revisar si el idBanner esta registrado o no
						 pstm = con.prepareStatement(Statements.GET_BITACORA_ERRORES_RC_EXISTE)
						 pstm.setString(1, it.idBanner);
						 rs= pstm.executeQuery();
						 if(rs.next()) {
							 existe = true;
						 }
						 errorLog+=" existe:"+existe
						 con.setAutoCommit(false)
						 execucion = true;
						 //segun lo resultado se crea o actualiza el dato del error
						 errorLog+=" consulta:"+(existe?Statements.UPDATE_BITACORA_ERRORES_RC.replace("[IDBANNER]", it.idBanner):Statements.INSERT_BITACORA_ERRORES_RC)
						 pstm = con.prepareStatement((existe?Statements.UPDATE_BITACORA_ERRORES_RC.replace("[IDBANNER]", it.idBanner):Statements.INSERT_BITACORA_ERRORES_RC), Statement.RETURN_GENERATED_KEYS)
						 pstm.setString(1, it.Error);
						 pstm.setString(2, it.idBanner)
						 pstm.setString(3, it.nombre)
						 pstm.setString(4, sDate);
						 pstm.executeUpdate();
						 
					 }
				 }
				
				 if(execucion) {					 
					 con.commit();
				 }
				
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
				
				// RESIVO UN TEXTO SEPARADO POR COMAS DE LOS IDBANNER Y LOS PERIODOS ("VALOR1,VALOR2")
				 String[] idBanner = object.IDBANNER.split(",");
				 String[] periodo = object.PERIODO.split(",");
				 for(int j = 0; j < idBanner.size(); ++j) {
					 errorLog += Statements.GET_EXISTE_Y_DATOS_DUPLICADOS_RC.replace("[VALOR]",idBanner[j]).replace("[PERIODO]",periodo[j])
					 //REVISO SI HAY VALORES QUE CONCUEDEN CON EL IDBANNER Y  EL PERIODO YA REGISTRADOS
					 pstm = con.prepareStatement(Statements.GET_EXISTE_Y_DATOS_DUPLICADOS_RC.replace("[VALOR]",idBanner[j]).replace("[PERIODO]",periodo[j]) )
					 rs= pstm.executeQuery();
					 columns = new LinkedHashMap<String, Object>();
					 // ESTO ES PARA QUE SIEMPRE REGRESE VALORES POR SI NO EXISTE
					 columns.put("idBanner", idBanner[j] )
					 columns.put("Registrado",false)
					 columns.put("Existe",false)
					 columns.put("EstaEnCarga",false)
					 columns.put("puedePeriodo",false)
					 columns.put("cantidadIntentos",false)
					 if(rs.next()) {
						 columns.put("Registrado",isNullOrEmpty(rs.getString("idbanner")))
						 columns.put("Existe",isNullOrEmpty(rs.getString("dsbanner")))
						 columns.put("EstaEnCarga",isNullOrEmpty(rs.getString("primernombre")))
						 columns.put("puedePeriodo",isNullOrEmpty(rs.getString("puedePeriodo")))
						 columns.put("cantidadIntentos",isNullOrEmpty(rs.getString("cantidadIntentos")))
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
	
	
	public Result postValidarUsuarioCantidadIntento(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				
				List<Map<String, Object>> estatus = new ArrayList<Map<String, Object>>();
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				
				//REVISO SI HAY VALORES QUE CONCUEDEN CON EL IDBANNER Y  EL PERIODO YA REGISTRADOS
				pstm = con.prepareStatement(Statements.GET_CANTIDAD_INTENTOS_RC)
				pstm.setString(1, object.IDBANNER)
				rs= pstm.executeQuery();
				columns = new LinkedHashMap<String, Object>();
				// ESTO ES PARA QUE SIEMPRE REGRESE VALORES POR SI NO EXISTE
				columns.put("idBanner", object.IDBANNER )
				columns.put("cantidadIntentos",0)
				if(rs.next()) {
					columns.put("cantidadIntentos",isNullOrEmpty(rs.getString("cantidadIntentos")))
					
				}
				estatus.add(columns)
				
				
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
	
	
	
	public Result getListaBitacoraRC( RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			
				//def jsonSlurper = new JsonSlurper();
				//def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				//Map<String, Object> columns = new LinkedHashMap<String, Object>();
				//SE OBTIENE EL LISTADO DE LA BITACORRA DE ERRORES
				pstm = con.prepareStatement(Statements.GET_BITACORA_ERRRORES_RC)
				rs= pstm.executeQuery();
				errorLog+="consulta";
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
					rows.add(columns);
				}
				errorLog+="termino la consulta";
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
	
	//se utiliza para optener el listado de resultados que tiene un aspirante
	public Result getAspiranteRC(String idBanner, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			
			closeCon = validarConexion();
			
			
			pstm = con.prepareStatement(Statements.GET_REGISTRO_RESULTADO_COMITE_BY_IDBANNER);
			pstm.setString(1, idBanner);
			
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
	
	
	public Result getAspiranteRC_Expecifico(String persistenceid, String idBanner, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			
			closeCon = validarConexion();
			
			
			pstm = con.prepareStatement(Statements.GET_RC_BY_IDBANNER);
			pstm.setString(1, idBanner);
			pstm.setLong(2, Long.parseLong(persistenceid));
			
			errorlog+= "persistenceid:"+persistenceid;
			
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
	
	
	
	public Result postUpdateLicenciaturaPeriodo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			
			closeCon = validarConexion();
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			con.setAutoCommit(false);
			
			pstm = con.prepareStatement(Statements.UPDATE_USER_LICENCIATURA_AND_PERIODO);
			pstm.setLong(1, Long.parseLong(object.idlicenciatura.toString()));
			pstm.setLong(2, Long.parseLong(object.idPeriodo.toString()));
			pstm.setString(3, object.correo.toString());
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
	
	
	public Boolean isNullOrEmpty(String text) {
		
		if(text?.equals("null") || text?.equals("") || text?.equals(" ") || text?.length() < 1) {
			return false
		}
		return true
	}
	
	//SE OBTIENE LOS ASPIRANTES QUE NO TIENE RESULTADOS	
	public Result getAspirantesSinRC(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
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
			where+=" WHERE sda.iseliminado=false and (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado = false ) and RC.idBanner is NULL "
			if(object.campus != null){
				where+=" AND LOWER(campus.grupoBonita) = LOWER('"+object.campus+"') "
			}

			where+=" AND (sda.ESTATUSSOLICITUD = 'Resultado final del comité' OR sda.ESTATUSSOLICITUD = 'Carga y consulta de resultados')"
				
			
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
				
				String consulta = Statements.GET_ASPIRANTES_SIN_RC
				
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
						
					case "SESION":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(SESIONES.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
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
				pstm = con.prepareStatement(consulta.replace("CASE WHEN prepa.descripcion = 'Otro'THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, periodo.clave AS claveIngreso , CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro'THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, sda.caseid, da.idbanner, campus.grupoBonita, TA.descripcion AS tipoadmision , R.descripcion AS residensia, TAL.descripcion AS tipoDeAlumno, catcampus.descripcion AS transferencia, campusEstudio.clave AS claveCampus, gestionescolar.clave  AS claveLicenciatura, SESIONES.nombre", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("GROUP BY prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,periodo.clave,sda.caseid,sda.telefonocelular,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid, SESIONES.nombre",""))
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
	
	
	
	// CONSULTA PARA OBTENER LOS ASPIRANTES QUE TIENE RESULTADOS
	public Result postListaAspiranteRC ( Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", bachillerato="", campus="", programa="", ingreso="", estado ="", tipoalumno ="", orderby="ORDER BY da.idbanner,", errorlog="" ;
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
			where+=" WHERE sda.iseliminado=false and (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado = false ) "
			if(object.campus != null){
				where+=" AND LOWER(campus.grupoBonita) = LOWER('"+object.campus+"') "
			}

			where+=" AND (sda.ESTATUSSOLICITUD = 'Resultado final del comité' OR sda.ESTATUSSOLICITUD = 'Carga y consulta de resultados') AND RC.decision IS NOT NULL AND RC.desactivado IS NOT TRUE"
			//where += " AND (RC.decision = '"+object.estatus +"') "
			
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
				
				String consulta = Statements.GET_ASPIRANTES_CON_RC
				
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
						
						
						case "FECHA DE CARGA DE RESULTADOS":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +="LOWER(PAA.fechaRegistro) like lower('%[valor]%') ";
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
						
					case "DECISION":
						errorlog+="DECISION"
						where +=" AND LOWER(RC.DECISION) ";
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
					case "DECISION":
					orderby+="RC.DECISION";
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
				errorlog=consulta.replace("distinct on (da.idbanner) da.idbanner,CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, periodo.clave AS claveingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, sda.caseid, sda.telefonocelular, da.idbanner, campus.grupoBonita, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura, RC.decision, gestionescolar.persistenceid as idlicenciatura", " COUNT(distinct sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,periodo.clave,sda.caseid,sda.telefonocelular,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid, RC.decision, gestionescolar.persistenceid","")+" 4"
				pstm = con.prepareStatement(consulta.replace("distinct on (da.idbanner) da.idbanner,CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, periodo.clave AS claveingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, sda.caseid, sda.telefonocelular, da.idbanner, campus.grupoBonita, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura, RC.decision, gestionescolar.persistenceid as idlicenciatura", " COUNT(distinct sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("GROUP BY prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,periodo.clave,sda.caseid,sda.telefonocelular,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid, RC.decision, gestionescolar.persistenceid",""));
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
	
	
	
}
