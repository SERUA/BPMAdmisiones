package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.text.SimpleDateFormat
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.bonitasoft.engine.exception.BonitaException
import org.bonitasoft.web.extension.rest.RestAPIContext
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
	
	public Result loginINVP(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		String horafinal = "";
		
		try {
			Long campus_pid = 0L;
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			context.getApiClient().login(object.username, object.password);
			errorlog += " | 1 ";
			Result resultadoExamenTerminado = getTerminadoExamenLogin(object.username);
			
			if(!resultadoExamenTerminado.isSuccess()) {
				errorlog += " | 2 ";
				throw new Exception(resultadoExamenTerminado.getError());	
			} else {
				errorlog += " | 3 ";
				Result resultSesionLogin = getSesionLogin(jsonData);
				if(!resultSesionLogin.isSuccess()) {
					errorlog += " | 4 ";
					throw new Exception(resultSesionLogin.getError());
				} else {
					errorlog += " | 5 ";
					Result resultSesionActiva = getSesionActivaLogin(jsonData);
					Result resultadoSesionTerminada = getSesionTerminada(object.username);
					
					if(!resultSesionActiva.isSuccess()) {
						errorlog += " | 6 " + resultSesionActiva.getError_info();
						throw new Exception(resultSesionActiva.getError());
					} else {
						errorlog += " | 7 ";
						Result checkBloqueado = new UsuariosDAO().checkBloqueado(object.username);
						
						if(!checkBloqueado.isSuccess()) {
							errorlog += " | 8 ";
							throw new Exception(checkBloqueado.getError());
						} else {
							if((Boolean) checkBloqueado.getData().get(0)) {
								errorlog += " | 10 ";
								throw new Exception("usuario_bloqueado");
							}
							
							errorlog += " | 11 ";
							Result resultBloquear = new UsuariosDAO().bloquearAspiranteDef(object.username);
							if(!resultBloquear.isSuccess()) {
								errorlog += " | 12 ";
								throw new Exception(resultBloquear.getError());
							} else {
								errorlog += " | 13 ";
								resultado = resultBloquear;
							}
						}
						
						resultado.setError_info(errorlog);
					}
				}
			}
			
			
//			resultado.setData(rows);
//			resultado.setSuccess(true);
//			resultado.setError_info(errorlog);
		} catch (BonitaException  e) {
			resultado.setSuccess(false);
			resultado.setError("user_password_incorrect");
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} catch (Exception e) {
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
			LoginSesion rowAdd = new LoginSesion();
			List<LoginSesion> rows = new ArrayList<LoginSesion>();
			List<LoginSesion> addData = new ArrayList<LoginSesion>();
			closeCon = validarConexion();
			String consulta = Statements.GET_SESION_LOGIN_LOGIN;
			pstm = con.prepareStatement(consulta);
			pstm.setString(1, object.username);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				row = new LoginSesion();
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
				
				if(!rs.getBoolean("sesion_iniciada")) {
					String mensaje = rs.getString("aplicacion") + " " + rs.getString("entrada");
					throw new Exception("sesion_no_iniciada|" + mensaje);
				}
			} else {
				consulta = Statements.GET_SESION_LOGIN_TEMPORAL_LOGIN;
				pstm = con.prepareStatement(consulta);
//				pstm.setString(1, object.aplicacion);
				pstm.setString(1, object.username);
				rs = pstm.executeQuery();
				
				if(rs.next()) {
					if(!rs.getBoolean("sesion_iniciada") && !rs.getBoolean("sesion_finalizada")) {
						String mensaje = rs.getString("fechainiciosesion") + " " + rs.getString("horainiciosesion");
						throw new Exception("sesion_no_iniciada|" + mensaje);
					} else if (rs.getBoolean("sesion_finalizada")){
						throw new Exception("sesion_finalizada");
					}
					
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
				} else {
					throw new Exception("no_existe_sesion");
				}
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
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
	
	public Result getSesionActivaLogin(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		Boolean isTemporal = false;
		Boolean examenReiniciado = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
			Map<String,Object> row = new HashMap<String,Object>();
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_SESION_USUARIO);
			pstm.setString(1, object.username);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				isTemporal = rs.getBoolean("istemporal");
				examenReiniciado = rs.getBoolean("examenreiniciado");
				row = new HashMap<String,Object>();
				
				if(rs.getBoolean("examenreiniciado") == true && rs.getBoolean("sesion_iniciada_temp") == false) {
					String mensaje = rs.getString("fechainicio_temp");
					throw new Exception("sesion_no_iniciada|" + mensaje);
				}
				
				Result checkBloqueado = new UsuariosDAO().checkBloqueado(object.username);
				Boolean bloqueado = false;
				Boolean tieneTolerancia = false;
				
				if(checkBloqueado.isSuccess()) {
					bloqueado = (Boolean) checkBloqueado.getData().get(0);
					
					if(bloqueado) {
						throw new Exception("block");
					} else {
						Result checkSesionFinalizada = new Result();
						
						Result checkTolerancia = new Result();
						
						if(isTemporal == true) {
							checkTolerancia = new UsuariosDAO().checkToleranciaTemp(object.username);
						} else {
							checkTolerancia = new UsuariosDAO().checkTolerancia(object.username);
						}
						
						if(checkTolerancia.isSuccess()) {
							errorlog += " | " + checkTolerancia.getError_info();
							tieneTolerancia = (Boolean) checkTolerancia.getData().get(0);
						} else {
							throw new Exception("no_sesion_asignada");
						}
						
						if(!tieneTolerancia) {
							errorlog += " | " + checkTolerancia.getError_info();
							throw new Exception("toler");
						}
					}
				}
			} else {
				pstm = con.prepareStatement(Statements.GET_SESION_LOGIN);
				pstm.setString(1, object.username);
				rs = pstm.executeQuery();
				
				if(rs.next()) {
					if(!rs.getBoolean("sesion_iniciada") || rs.getBoolean("sesion_iniciada_temp") == false) {
						String mensaje = "";
						if(!rs.getBoolean("sesion_iniciada")) {
							mensaje = rs.getString("entradahora");
							throw new Exception("sesion_no_iniciada|" + mensaje);
						} else if(rs.getObject("sesion_iniciada_temp") != null) {
							if(!rs.wasNull()) {
								mensaje = rs.getString("entradahora");
								throw new Exception("sesion_no_iniciada|" + mensaje);
							} 
						}
						
//						throw new Exception("sesion_no_iniciada|" + mensaje);
					} 
					
//					if(!rs.getBoolean("sesion_iniciada")) {
//						String mensaje = rs.getString("entradahora");
//						throw new Exception("sesion_no_iniciada|" + mensaje);
//					}
					
					Result  checkToler = new UsuariosDAO().checkTolerancia(object.username);
					if(checkToler.isSuccess()) {
						if(!((boolean) checkToler.getData().get(0))) {
							throw new Exception("toler");
						}
					} else {
						throw new Exception("fallo_tolerancia");
					}
				} else {
					throw new Exception("no_existe_sesion");
				}
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
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
			List<?> additional_data = new ArrayList<?>();
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
				 
				rows.add(row);
			} else {
				pstm = con.prepareStatement(Statements.GET_SESION_LOGIN);
				pstm.setString(1, object.username);
				rs = pstm.executeQuery();
				
				if(rs.next()) {
					Result  checkToler = new UsuariosDAO().checkTolerancia(object.username);
					if(checkToler.isSuccess()) {
						if(!((boolean) checkToler.getData().get(0))) {
							additional_data.add("toler");
							row.put("havesesion", true);
							rows.add(row);
						} 
					} else {
						throw new Exception("fallo_tolerancia");
					}
				} else {
					throw new Exception("no_existe_sesion");
				}
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
			pstm.setLong(3, Long.valueOf(object.idsesion));
			
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
	
	public Result getTerminadoExamenLogin(String username) {
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
			if (rs.next()) {
				if(rs.getBoolean("terminado")) {
					throw new Exception("examen_terminado");
				}
			}
			resultado.setSuccess(true);
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
				errorlog += " dentro de la consulta " + " | ";
				row = new LoginSesion();
				row.setPersistenceId(rs.getLong("idsesion"));
				row.setNombre_sesion(rs.getString("nombresesion"));
				
				//tipoexamen
				row.setDescripcion(rs.getString("descripcion"))
				row.setNombre_prueba(rs.getString("nombre_prueba"));
				row.setId_prueba(rs.getLong("id_prueba"));
				try {
					row.setAplicacion(rs.getString("aplicacion"));
				} catch (Exception e) {
					LOGGER.error "[ERROR] " + e.getMessage();
					errorlog += e.getMessage();
				}
				row.setEntrada(rs.getString("entrada"));
				row.setSalida(rs.getString("salida"));
				row.setUsername(rs.getString("username"));
				row.setAplicacion_temp(rs.getString("aplicacion_temp"));
				row.setEntrada_temp(rs.getString("entrada_temp"));
				row.setSalida_temp(rs.getString("salida_temp"));
				row.setNombre_temp(rs.getString("nombre_temp"));
				row.setIdsesion_temp(rs.getLong("idsesion_temp"));

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
				row.put("idbanner", rs.getString("idbanner"));
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
			pstm.setString(2, username);
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
	
	public Result updateterminadoGet(String username, Boolean terminado, String idsesion) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		Boolean success = false;
		String error_log = "";
		String success_log = "";
		Long resultReq = 0;

		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.UPDATE_TERMINADO_EXAMEN_GET);
			pstm.setBoolean(1, terminado);
			pstm.setLong(2, Long.valueOf(idsesion));
			pstm.setString(3, username);	
			pstm.executeUpdate();
			con.commit();
			
			success = true;
			if(resultReq > 0) {
				error_log = resultReq + " Exito! query UPDATE_TERMINADO_EXAMEN_GET"
			} else {
				error_log = resultReq + " Error! query UPDATE_TERMINADO_EXAMEN_GET"
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
	
	public Result getSesionTerminada(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		
		try {
			Map<String,Boolean> row = new HashMap<String,Boolean>();
			List<Long> rows = new ArrayList<Long>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_SESION_FINALIZADA_BY_USERNAME);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				if(rs.getBoolean("finalizada") == true) {
					throw new Exception("prueba_finalizada");
				}
			}
			resultado.setSuccess(true);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
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
