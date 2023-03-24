package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.LoginSesion
import com.anahuac.rest.api.Entity.Result

import groovy.json.JsonSlurper

class LoginSesionesDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginSesionesDAO.class);
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

	public Result getSesionLogin(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		String horafinal = "";
		try {
			Long campus_pid = 0L;
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			LoginSesion row = new LoginSesion();
			List<LoginSesion> rows = new ArrayList<LoginSesion>();
			closeCon = validarConexion();
			String consulta = Statements.GET_CAT_CAMPUS_PID_BY_CORREO;
			pstm = con.prepareStatement(consulta);
			pstm.setString(1, object.username);
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				errorlog = " | " + errorlog + " | "
				campus_pid = rs.getLong("catcampus_pid");
			}
			
			errorlog = " | campus_pid " + campus_pid + " | "
			consulta = Statements.GET_SESION_LOGIN;
			pstm = con.prepareStatement(consulta);
//			pstm.setString(1, object.aplicacion);
			pstm.setString(1, object.username);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				row = new LoginSesion()
				row.setPersistenceId(rs.getLong("idsesion"));
				row.setNombre_sesion(rs.getString("nombresesion"));
				row.setDescripcion(rs.getString("descripcion"))
				row.setNombre_prueba(rs.getString("nombre_prueba"));
				row.setId_prueba(rs.getLong("id_prueba"));
				try {
					row.setAplicacion(rs.getString("aplicacion"));
				} catch (Exception e) {
					errorlog += e.getMessage();
				}
				row.setEntrada(rs.getString("entrada"));
				row.setSalida(rs.getString("salida"));
				row.setUsername(rs.getString("username"));
				rows.add(row);
			} else {
				consulta = Statements.GET_SESION_LOGIN_TEMPORAL;
				pstm = con.prepareStatement(consulta);
				pstm.setString(1, object.username);
				pstm.setString(2, object.aplicacion);
				rs = pstm.executeQuery();
				
				if(rs.next()) {
					row = new LoginSesion();
					row.setNombre_sesion(rs.getString("nombresesion"));
					row.setDescripcion(rs.getString("descripcionsesion"))
					row.setNombre_prueba("(Temporal)");
					row.setId_prueba(rs.getLong("idprueba"));
					try {
						row.setAplicacion(rs.getString("fechainiciosesion"));
					} catch (Exception e) {
						errorlog += e.getMessage();
					}
					row.setEntrada(rs.getString("horainiciosesion"));
					row.setSalida(rs.getString("horafinsesion"));
					row.setUsername(rs.getString("username"));
					rows.add(row);
				}
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updateUsuarioSesion(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		Boolean success = false;
		String error_log = "";
		String success_log = "";
		Long resultReq = 0;
		Boolean havesesion = false;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			pstm = con.prepareStatement(Statements.GET_SESION_USUARIO);
			pstm.setString(1, object.username);
			rs = pstm.executeQuery();
			Boolean exists = false;
			if (rs.next()) {
				exists = true;
				havesesion = rs.getBoolean("havesesion");
			} 
			
			if(!exists) {
				pstm = con.prepareStatement(Statements.INSERT_BLOQUEO_USUARIO);
				pstm.setBoolean(1, Boolean.valueOf(object.havesesion));
				pstm.setString(2, object.username);
				rs = pstm.executeQuery();
				
				if(rs.next()) {
					resultReq = rs.getLong("persistenceid")
				}
				
				success = true;
				con.commit();
			}else {
				pstm = con.prepareStatement(Statements.UPDATE_SESION_USUARIO);
				pstm.setBoolean(1, Boolean.valueOf(object.havesesion));
				pstm.setString(2, object.username);
				pstm.executeUpdate();
				con.commit();
				
				success = true;
			}
			
			resultado.setSuccess(true)
			resultado.setError_info(errorlog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getSesionActiva(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		Boolean isTemporal = false;
		Boolean examenReiniciado = false;
		try {
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
			List<String> additional_data = new ArrayList<String>();
			Map<String,Object> row = new HashMap<String,Object>(); 
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_SESION_USUARIO);
			pstm.setString(1, object.username);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				isTemporal = rs.getBoolean("istemporal");
				examenReiniciado = rs.getBoolean("examenreiniciado");
				row = new HashMap<String,Object>();
				Result checkBloqueado = new UsuariosDAO().checkBloqueado(object.username);
				Boolean bloqueado = false;
				Boolean tieneTolerancia = false;
				
				if(checkBloqueado.isSuccess()) {
					errorlog += " | 1 ";
					bloqueado = (Boolean) checkBloqueado.getData().get(0);
					
					if(bloqueado) {
						errorlog += " | 2 ";
						additional_data.add("block");
						row.put("havesesion", true);
					} else {
						errorlog += " | 3 ";
						Result checkTolerancia = new Result();
						if(isTemporal == true) {
							errorlog += " | 4 ";
							checkTolerancia = new UsuariosDAO().checkToleranciaTemp(object.username);
						} else {
							errorlog += " | 5 ";
							checkTolerancia = new UsuariosDAO().checkTolerancia(object.username);
						}
						
						if(checkTolerancia.isSuccess()) {
							errorlog += " | 6 ";
							tieneTolerancia = (Boolean) checkTolerancia.getData().get(0);
						} else {
							errorlog += " | 7 ";
							throw new Exception("no_sesion_asignada");
						}
						
						if(!tieneTolerancia) {
							errorlog += " | 8 ";
							additional_data.add("toler");
							row.put("havesesion", true);
						} else {
							errorlog += " | 9 ";
							row.put("havesesion", false);
						}
					}
				} else {
					errorlog += " | 10 ";
					row.put("havesesion", false);
				}
				 
				rows.add(row)
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setAdditional_data(additional_data);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result insertterminado(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		Boolean success = false;
		String error_log = "";
		String success_log = "";
		Long resultReq = 0;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.INSERT_TERMINADO_EXAMEN);
			pstm.setString(1, object.username);
			pstm.setBoolean(2, object.terminado);
			
			pstm.executeUpdate();
			/*rs = pstm.executeQuery();
			if(rs.next()) {
				resultReq = rs.getLong("persistenceid")
			}
			
			success = true;
			if(resultReq > 0) {
				error_log = resultReq + " Exito! query INSERT_TERMINADO_EXAMEN"
			} else {
				error_log = resultReq + " Error! query INSERT_TERMINADO_EXAMEN"
			}*/
			con.commit();
			
			resultado.setSuccess(true)
			resultado.setError_info(errorlog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updateterminado(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		Boolean success = false;
		String error_log = "";
		String success_log = "";
		Long resultReq = 0;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.UPDATE_TERMINADO_EXAMEN);
			pstm.setBoolean(1, object.terminado);
			pstm.setString(2, object.username);
			pstm.executeUpdate();
			con.commit();
			
			success = true;
			if(resultReq > 0) {
				error_log = resultReq + " Exito! query UPDATE_TERMINADO_EXAMEN"
			} else {
				error_log = resultReq + " Error! query UPDATE_TERMINADO_EXAMEN"
			}
			
			resultado.setSuccess(true)
			resultado.setError_info(errorlog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getTotalPreguntasContestadas(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		try {
			Map<String,Integer> row = new HashMap<String,Integer>();
			List<Map<String,Integer>> rows = new ArrayList<Map<String,Integer>>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_COUNT_PREGUNTASCONTESTADAS_BY_USERNAME);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			while (rs.next()) {
				row = new HashMap<String,Integer>();
				row.put("totalPreguntas", rs.getInt("totalpreguntas"))
				rows.add(row)
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getTerminadoExamen(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		try {
			Map<String,Boolean> row = new HashMap<String,Boolean>();
			List<Map<String,Integer>> rows = new ArrayList<Map<String,Integer>>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_TERMINADO_EXAMEN);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			while (rs.next()) {
				row = new HashMap<String,Integer>();
				row.put("terminado", rs.getBoolean("terminado"))
				rows.add(row)
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getDatosSesionLogin(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		String horafinal = "";
		try {
			Long campus_pid = 0L;
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			LoginSesion row = new LoginSesion();
			List<LoginSesion> rows = new ArrayList<LoginSesion>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_DATOS_SESION_LOGIN);
			pstm.setString(1, object.username);
			//pstm.setLong(3, campus_pid);
			rs = pstm.executeQuery();
			while (rs.next()) {
				errorlog += " dentro de la consulta " + " | "
				row = new LoginSesion()
				row.setPersistenceId(rs.getLong("idsesion"));
				row.setNombre_sesion(rs.getString("nombresesion"));
				
				//tipoexamen
				row.setDescripcion(rs.getString("descripcion"))
				row.setNombre_prueba(rs.getString("nombre_prueba"));
				row.setId_prueba(rs.getLong("id_prueba"));
				try {
					row.setAplicacion(rs.getString("aplicacion"))
				} catch (Exception e) {
					LOGGER.error "[ERROR] " + e.getMessage();
					errorlog += e.getMessage()
				}
				row.setEntrada(rs.getString("entrada"));
				row.setSalida(rs.getString("salida"));
				row.setUsername(rs.getString("username"));
				rows.add(row)
			}
			
			
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getidbanner(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		try {
			Map<String,Boolean> row = new HashMap<String,Boolean>();
			List<Map<String,Integer>> rows = new ArrayList<Map<String,Integer>>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_ID_BANNER_BY_CORREO);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			while (rs.next()) {
				row = new HashMap<String,Integer>();
				row.put("idbanner", rs.getInt("idbanner"))
				row.put("telefono", rs.getString("telefono"));
				row.put("telefonocelular", rs.getString("telefonocelular"));
				rows.add(row)
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getcelularusuariotemporal(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		try {
			Map<String,Boolean> row = new HashMap<String,Boolean>();
			List<Map<String,Integer>> rows = new ArrayList<Map<String,Integer>>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_CELULAR_BY_USERNAME);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			while (rs.next()) {
				row = new HashMap<String,Integer>();
				row.put("telefonocelular", rs.getString("telefonocelular"));
				rows.add(row)
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getExamenTerminado(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		
		try {
			Map<String,Integer> row = new HashMap<String,Integer>();
			List<Map<String,Integer>> rows = new ArrayList<Map<String,Integer>>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_EXAMEN_TERMINADO);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			
			row = new HashMap<String,Integer>();
			if (rs.next()) {
				row.put("examenIniciado", true);
				row.put("examenTerminado", rs.getBoolean("terminado"));
			} else {
				row.put("examenIniciado", false);
				row.put("examenTerminado", false);
			}
			
			rows.add(row);
			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getSalidaPrueba(Long idsesion) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		
		try {
			Map<String,Boolean> row = new HashMap<String,Boolean>();
			List<Map<String,Integer>> rows = new ArrayList<Map<String,Integer>>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_SESION_FECHA_SALIDA);
			pstm.setLong(1, idsesion);
			rs = pstm.executeQuery();
			while (rs.next()) {
				row = new HashMap<String,Integer>();
				row.put("aplicacion_salida", rs.getString("aplicacion_salida"));
				rows.add(row);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result getTerminoPrueba(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		
		try {
			Map<String,Boolean> row = new HashMap<String,Boolean>();
			List<Long> rows = new ArrayList<Long>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_FECHA_TERMINO_BY_USERNAME);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				row = new HashMap<String,Integer>();
				if(rs.getString("horafin_temp") != null) {
					rows.add(rs.getTimestamp("horafin_temp").getTime());
				} else {
					rows.add(rs.getTimestamp("horafin").getTime());
				}
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
}
