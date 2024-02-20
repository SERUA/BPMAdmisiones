package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.data.DataInstance
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.web.extension.rest.RestAPIContext
import com.anahuac.posgrados.catalog.PSGRCatCampus
import com.anahuac.posgrados.catalog.PSGRCatCampusDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.custom.Calendario
import com.anahuac.rest.api.Entity.custom.Horarios
import com.anahuac.rest.api.Entity.custom.PruebaCustom
import com.anahuac.rest.api.Entity.custom.PruebasCustom
import com.anahuac.rest.api.Entity.custom.ResponsableCustom
import com.anahuac.rest.api.Entity.custom.Responsables
import com.anahuac.rest.api.Entity.custom.SesionCustom
import com.anahuac.rest.api.Entity.custom.SesionesAspiranteCustom
import com.anahuac.rest.api.Entity.custom.SesionesBack
import com.anahuac.rest.api.Entity.custom.SesionesPosibles
import com.anahuac.rest.api.Entity.db.CatTipoPrueba
import com.anahuac.rest.api.Entity.db.Responsable
import com.anahuac.rest.api.Entity.db.ResponsableDisponible
import com.anahuac.rest.api.Entity.db.Sesion_Aspirante
import com.anahuac.rest.api.Utilities.FileDownload
import com.anahuac.rest.api.Utilities.LoadParametros
import groovy.json.JsonSlurper

class SesionesDAO {
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	ResultSet rs2;

	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		return retorno
	}
	
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true
		}
		return retorno
	}
	
	private static java.sql.Date convert(java.util.Date uDate) {
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}
	
	private Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;

	}
	
	public Result getSesionesV1(String idcampus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		SesionesPosibles row = new SesionesPosibles();
		List < SesionesPosibles > rows = new ArrayList < SesionesPosibles > ();
		
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.GET_SESIONES_POSIBLES);
			pstm.setLong(1, Long.valueOf(idcampus));
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new SesionesPosibles();
				
				row.setPersistenceId(rs.getLong("persistenceid"));
				row.setPersistenceId_string(rs.getString("persistenceId_string"));
				row.setFecha_entrevista(convertirFecha(rs.getString("fecha_entrevista")));
				row.setNombre(rs.getString("nombre"));
				row.setDescripcion_entrevista(rs.getString("descripcion_entrevista"));
				row.setResponsable_id(rs.getString("responsable_id"));
				rows.add(row);
			}
			
			resultado.setData(rows);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			if(con != null) {
				con.rollback();
			}
		} finally {
			resultado.setError_info(errorLog);
			if(con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public static String convertirFecha(String fechaString) {
		try {
			DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("MM/dd/yyyy");

			LocalDateTime fecha = LocalDateTime.parse(fechaString, formatoEntrada);
			String fechaFormateada = fecha.format(formatoSalida);

			return fechaFormateada;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Result getHorariosByIdSesion(Long idSesion) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Map<String, Object> row = new HashMap<String, Object>();
		List <Map<String, Object>> rows = new ArrayList <Map<String, Object>>();
		Responsables resp = new Responsables();
		List <Responsables> responsables = new ArrayList<Responsables>();
		List<Long> ids = new ArrayList<Long>();
		
		try {
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_HORARIOS_BY_SESION);
			pstm.setLong(1, idSesion);
			
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new HashMap<String, Object>();
				SesionesPosibles sesion = new SesionesPosibles();
				sesion.setNombre(rs.getString("nombre"));
				sesion.setDescripcion_entrevista(rs.getString("descripcion_entrevista"));
				row.put("cita_entrevista", sesion);
				row.put("persistenceId", rs.getLong("persistenceid"));
				row.put("persistenceId_string", rs.getString("persistenceid"));
				row.put("hora_fin", rs.getString("hora_fin"));
				row.put("hora_inicio", rs.getString("hora_inicio"));
				
				rows.add(row);
			}
			
			for(Map<String, Object> hor: rows) {
				responsables = new ArrayList<Responsables>()
				pstm = con.prepareStatement(Statements.GET_RESPONSABLES_BY_ID_HORARIO);
				pstm.setLong(1, hor.get("persistenceId"));
				
				rs = pstm.executeQuery();
				
				while (rs.next()) {
					resp = new Responsables();
					resp.setPersistenceId(rs.getLong("persistenceid"));
					resp.setResponsable_id(rs.getString("responsable_id"));
					resp.setDisponible_resp(rs.getBoolean("disponible_resp"));
					resp.setOcupado(rs.getBoolean("ocupado"));
					
					responsables.add(resp);
					if(!ids.contains(Long.valueOf(resp.getResponsable_id()))) {
						ids.add(Long.valueOf(resp.getResponsable_id()));
					}
				}
				
				hor.put("responsables", responsables);
			} 
			
			
			resultado.setData(rows);
			resultado.setSuccess(true);
			resultado.setAdditional_data(ids);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			resultado.setError_info(errorLog);
			if(con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getHorariosByIdSesionV2(Long idSesion) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Map<String, Object> row = new HashMap<String, Object>();
		List < Map<String, Object> > rows = new ArrayList < Map<String, Object> > ();
		
		try {
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_HORARIOS_BY_SESION_FRONT);
			pstm.setLong(1, idSesion);
			
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new HashMap<String, Object>();
				SesionesPosibles sesion = new SesionesPosibles();
				sesion.setNombre(rs.getString("nombre"));
				sesion.setDescripcion_entrevista(rs.getString("descripcion_entrevista"));
				sesion.setFecha_entrevista(rs.getString("fecha_entrevista"));
				sesion.setResponsable_id(rs.getString("persistenceId_string"));	
				row.put("cita_entrevista", sesion);
				row.put("persistenceId", rs.getLong("persistenceid"));
				row.put("persistenceId_string", rs.getString("persistenceid"));
				row.put("hora_fin", rs.getString("hora_fin"));
				row.put("hora_inicio", rs.getString("hora_inicio"));
				row.put("disponible", rs.getBoolean("responsables_disponibles"));
				
				rows.add(row);
			}
			
			resultado.setData(rows);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			resultado.setError_info(errorLog);
			if(con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public static List<Map<String, String>> generarHoras(Integer duracion) {
		List<Map<String, String>> horasList = new ArrayList<>();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

		while (calendar.get(Calendar.HOUR_OF_DAY) <= 19) {
			Map<String, String> horario = new HashMap<String, String>();
			
			horario.put("inicio", formatoHora.format(calendar.getTime()));
			calendar.add(Calendar.MINUTE, duracion);
			horario.put("fin", formatoHora.format(calendar.getTime()));
			horasList.add(horario);
		}

		return horasList;
	}
	
	public Result insertSesion(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Long idSesion = 0L;
		List<String> ids = new ArrayList<String>();
		
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			SimpleDateFormat sdfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = "";
			
			if(object.nombre.equals("") || object.nombre == null) {
				throw new Exception("El campo \"Nombre sesión\" no debe ir vacío");
			} else if(object.descripcion_entrevista.equals("") || object.descripcion_entrevista == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			} else if(object.fecha_entrevista.equals("") || object.fecha_entrevista == null) {
				throw new Exception("El campo \"Fecha de sesión\" no debe ir vacío");
			} else if(object.duracion_entrevista_minutos.equals("") || object.duracion_entrevista_minutos == null) {
				throw new Exception("El campo \"Duración de entrevista\" no debe ir vacío");
			}
			
			Long idCampus = 0L;
			if(object.campus == null || object.campus.equals("")) {
				idCampus = 0L;
			} else {
				idCampus = Long.valueOf(object.campus);
			}
			
			Date fecha = sdfEntrada.parse(object.fecha_entrevista);
			fechaHoraFormateada = formato.format(fecha);
			
			pstm = con.prepareStatement(Statements.INSERT_SESION);
			pstm.setInt(1, Integer.parseInt(object.duracion_entrevista_minutos));
			pstm.setString(2, object.nombre);
			pstm.setString(3, object.descripcion_entrevista);
			pstm.setString(4, fechaHoraFormateada);
			pstm.setLong(5, idCampus);
			pstm.setBoolean(6, object.is_presencial);
			pstm.setString(7, object.liga);
			pstm.setString(8, object.ubicacion);
			
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				idSesion = rs.getLong("persistenceid");
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
			
			List<Map<String, Object>> lstHorarios = (List<Map<String, Object>>) object.horarios;
			for(Map<String, Object> horario: lstHorarios) {
				pstm = con.prepareStatement(Statements.INSERT_HORARIOS);
				pstm.setString(1, horario.get("hora_inicio"));
				pstm.setString(2, horario.get("hora_fin"));
				pstm.setLong(3, idSesion);
				
				rs = pstm.executeQuery();
				
				if(rs.next()) {
					Long horario_pid = rs.getLong("persistenceid");
					List<Map<String, Object>> lstResponsables = (List<Map<String, Object>>) horario.get("responsables");
					for(Map<String, Object> responsable: lstResponsables) {
						if(!ids.contains(responsable.get("responsable_id"))) {
							ids.add(responsable.get("responsable_id"));
						}
						
						pstm = con.prepareStatement(Statements.INSERT_RESPONSABLE_CITA);
						pstm.setLong(1, horario_pid);
						pstm.setLong(2, Integer.valueOf(responsable.get("responsable_id")));
						pstm.setLong(3, idSesion);
						pstm.setBoolean(4, responsable.get("ocupado"));
						pstm.setBoolean(5, responsable.get("disponible_resp"));
						
						rs = pstm.executeQuery();
						
						if(!rs.next()) {
							throw new Exception("No se ha podido insertar el responsable");
						}
					}
				} else {
					throw new Exception("No se ha podido insertar el  horario");
				}
			}
			
			if(!ids.empty) {
				errorLog += "|1";
				for(String id: ids) {
					errorLog += "|2";
					pstm = con.prepareStatement(Statements.INSERT_RESPONSABLES_LISTA);
					pstm.setString(1, "");
					pstm.setLong(2, Long.valueOf(id));
					pstm.setLong(3, idSesion);
					errorLog += "|3";
					if(pstm.executeUpdate() > 0) {
						errorLog += "|4";
						Map<String, Object> carreras = (Map<String, Object>) object.carreras;
						errorLog += "|5";
						List<Map<String, Object>> lstCarreras = (List<Map<String, Object>>) carreras.get(id);
						errorLog += "|6";
						for(Map<String, Object> responsableCarrera: lstCarreras) {
							errorLog += "|7";
							pstm = con.prepareStatement(Statements.INSERT_ENTREVISTADOR_CARRERA);
							pstm.setLong(1, Long.valueOf(id));
							pstm.setLong(2, Long.valueOf(responsableCarrera.get("persistenceId").toString()));
							pstm.setLong(3, idCampus);
							pstm.setLong(4, idSesion);
							errorLog += "|8";
							
							if(pstm.executeUpdate() == 0){
								throw new Exception("No se ha podido insertar el responsable relación carrera");
							}
							errorLog += "|9";
						}
					} else {
						throw new Exception("No se ha podido insertar el responsable");
					}
				}
			}
			
			con.commit();
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertSesion] " + e.getMessage());
			if (con != null) {
				con.rollback();
			}
		} finally {
			resultado.setError_info(errorLog);
			if (con != null) {
				con.setAutoCommit(true);
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result updateSesion(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Long idSesion = 0L;
		Long idCampus = 0L;
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			con.setAutoCommit(false);
			
			SimpleDateFormat sdfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = "";
			
			if(!object.persistenceId) {
				throw new Exception("El campo \"Persistence ID\" no debe ir vacío");
			} else if(object.nombre.equals("") || object.nombre == null) {
				throw new Exception("El campo \"Nombre sesión\" no debe ir vacío");
			} else if(object.descripcion_entrevista.equals("") || object.descripcion_entrevista == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			} else if(object.fecha_entrevista.equals("") || object.fecha_entrevista == null) {
				throw new Exception("El campo \"Fecha de sesión\" no debe ir vacío");
			} else if(object.duracion_entrevista_minutos.equals("") || object.duracion_entrevista_minutos == null) {
				throw new Exception("El campo \"Duración de entrevista\" no debe ir vacío");
			}
			
			Date fecha = sdfEntrada.parse(object.fecha_entrevista);
			fechaHoraFormateada = formato.format(fecha);
			
			pstm = con.prepareStatement(Statements.UPDATE_SESION);
			
			pstm.setString(1, object.nombre);
			pstm.setString(2, object.descripcion_entrevista);
			pstm.setLong(3, Long.valueOf(object.persistenceId));
			
			pstm.executeUpdate();
			
			if(object.campus == null || object.campus.equals("")) {
				idCampus = 0L;
			} else {
				idCampus = Long.valueOf(object.campus);
			}
			
			List<Map<String, Object>> lstHorarios = (List<Map<String, Object>>) object.horarios;
			
			for(Map<String, String> horario: lstHorarios) {
				List<Responsables> lstResponsables = new ArrayList<Responsables>();
				lstResponsables = (List<Responsables>) horario.get("responsables");
				Integer rows  = 0;
				String idUsuarioResponsable = "";
				for(Responsables responsable: lstResponsables) {
					errorLog += "|1";
					if(responsable.getPersistenceId() != null) {
						errorLog += "|2";
						pstm = con.prepareStatement(Statements.UPDATE_RESPONSABLE_CITA);
						pstm.setBoolean(1, responsable.getOcupado());
						pstm.setBoolean(2, responsable.getDisponible_resp());
						pstm.setLong(3, responsable.getPersistenceId());
						rows = pstm.executeUpdate();
						idUsuarioResponsable = responsable.getResponsable_id();
					} else {
						errorLog += "|3";
						pstm = con.prepareStatement(Statements.INSERT_RESPONSABLE_CITA);
						pstm.setLong(1, Long.valueOf(responsable.getHorario()?.persistenceId)); // horario
						pstm.setLong(2, Long.valueOf(responsable.getResponsable_id())); // responsable
						pstm.setLong(3, Long.valueOf(object.persistenceId));
						pstm.setBoolean(4, responsable.getOcupado());
						pstm.setBoolean(5, responsable.getDisponible_resp());	
						
						rs = pstm.executeQuery();
						errorLog += "|3.1";
						if(rs.next()) {
							Long idUsuarioLong = rs.getLong("persistenceid");
							idUsuarioResponsable = idUsuarioLong.toString();
							errorLog += "|3.2 ::"+ idUsuarioResponsable;
						}
					}
					errorLog += "|4";
					pstm = con.prepareStatement(Statements.DELETE_ENTREVISTADOR_CARRERA_BY_CARRERA);
					pstm.setLong(1, Long.valueOf(object.persistenceId));
					pstm.executeUpdate();
					
					errorLog += "|5";
					Map<String, Object> carreras = (Map<String, Object>) object.carreras;
					errorLog += "|6";
					List<Map<String, Object>> lstCarreras = (List<Map<String, Object>>) carreras.get(responsable.getResponsable_id());
					errorLog += "|7";
					for(Map<String, Object> responsableCarrera: lstCarreras) {
						errorLog += "|8 ::" + idUsuarioResponsable + ":: ";
						pstm = con.prepareStatement(Statements.INSERT_ENTREVISTADOR_CARRERA);
						pstm.setLong(1, Long.valueOf(idUsuarioResponsable));
						pstm.setLong(2, Long.valueOf(responsableCarrera.get("persistenceId").toString()));
						pstm.setLong(3, idCampus);
						pstm.setLong(4, Long.valueOf(object.persistenceId));
						errorLog += "|9";
						
						if(pstm.executeUpdate() == 0){
							throw new Exception("No se ha podido insertar el responsable relación carrera");
						}
						errorLog += "|10";
					}
					
				}
			}
			
			con.commit();
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateSesion] " + e.getMessage());
			if (!con.autoCommit) con.rollback();
		} finally {
			resultado.setError_info(errorLog);
			if (con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	
	
	public Result getSesionesV2(String idcampus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		SesionesBack row = new SesionesBack();
		List < SesionesBack > rows = new ArrayList < SesionesBack > ();
		
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.GET_SESIONES_TODAS);
			pstm.setLong(1, Long.valueOf(idcampus));
			
			rs = pstm.executeQuery();
			SimpleDateFormat sdfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			while (rs.next()) {
				row = new SesionesBack();
				
				row.setPersistenceId(rs.getLong("persistenceid"));
				row.setPersistenceId_string(rs.getString("persistenceId_string"));
				row.setFecha_entrevista(rs.getString("fecha_entrevista"));
				row.setFecha_entrevista(sdfEntrada.format(formato.parse(rs.getString("fecha_entrevista"))));
				row.setFecha_entrevista_back(convertirFecha(rs.getString("fecha_entrevista")));
				row.setNombre(rs.getString("nombre"));
				row.setDescripcion_entrevista(rs.getString("descripcion_entrevista"));
				row.setDuracion_entrevista_minutos(rs.getInt("duracion_entrevista_minutos"));
				row.setCampus(rs.getLong("campus_pid"));
				row.setIs_presencial(rs.getBoolean("is_presencial"));
				row.setLiga(rs.getString("liga"))
				row.setUbicacion(rs.getString("ubicacion"));
				
				rows.add(row);
			}
			
			resultado.setData(rows);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			resultado.setError_info(errorLog);
			if(con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getInfoCarrerasResponsable(Long idsesion, Long identrevistador) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		String carrerasString = "";
		List<String> data = new ArrayList<String>();
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.GET_ENTREVISTADOR_CARRERA_INFO);
			pstm.setLong(1, idsesion);
			pstm.setLong(2, identrevistador);
			
			rs = pstm.executeQuery();
			SimpleDateFormat sdfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			
			while (rs.next()) {
				carrerasString +=  rs.getString("nombre");
				if(!rs.isLast()) {
					carrerasString += ","
				}
			}
			
			data.add(carrerasString);
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			resultado.setError_info(errorLog);
			if(con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getHorariosCita(Long idsesion, RestAPIContext context) {
		Result resultado = new Result();
		List<Horarios> rows = new ArrayList<Horarios>();
		Horarios horario = new Horarios();
		Responsables resp = new Responsables();
		String errorLog = "";
		Boolean closeCon = false;
		
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.GET_HORARIOS_BY_SESION);
			pstm.setLong(1, Long.valueOf(idsesion));
			
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				horario = new Horarios();
				horario.setHora_fin(rs.getString("hora_fin"));
				horario.setHora_inicio(rs.getString("hora_inicio"));
				horario.setPersistenceid(rs.getLong("persistenceid"));
				
				rows.add(horario);
			}
			
			for(Horarios hor: rows) {
				pstm = con.prepareStatement(Statements.GET_RESPONSABLES_BY_ID_HORARIO);
				pstm.setLong(1, hor.getPersistenceid());
				
				rs = pstm.executeQuery();
				hor.setResponsables(new ArrayList<Responsables>());
				while (rs.next()) {
					resp = new Responsables();
					resp.setResponsable_id(rs.getString("responsable_id"));
					resp.setDisponible_resp(rs.getBoolean("disponible_resp"));
					resp.setHorario_pid(rs.getLong("horario_pid"));
					resp.setOcupado(rs.getBoolean("ocupado"));
					
					hor.getResponsables().add(resp);
				}
			}
			
			resultado.setData(rows);
			resultado.setSuccess(true);
		} catch(Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			resultado.setError_info(errorLog);
			if(con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result getSesionesCalendarizadasPsicologoSupervisor(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		List<PruebasCustom> lstSesion = new ArrayList();
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="", residencia=" ",campus="";
		List<String> lstGrupo = new ArrayList<String>();
		//Map<String, String> objGrupoCampus = new HashMap<String, String>();
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				def objCatCampusDAO = context.apiClient.getDAO(PSGRCatCampusDAO.class);
				List<PSGRCatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
				
				userLogged = context.getApiSession().getUserId();
				
				List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
				for(UserMembership objUserMembership : lstUserMembership) {
					//errorlog+=" Role: "+objUserMembership.getRoleName()
					if(objUserMembership.getRoleName().trim().equals("PSICOLOGO SUPERVISOR") ) {
						for(PSGRCatCampus rowGrupo : lstCatCampus) {
							if(objUserMembership.getGroupName().equals(rowGrupo.getGrupo_bonita())) {
								lstGrupo.add(rowGrupo.getDescripcion());
								break;
							}
						}
					}
					
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
				
				
				//errorlog+="campus = "+campus
				String consulta = Statements.GET_SESIONESPSICOLOGO
				PruebaCustom row = new PruebaCustom();
				List<PruebasCustom> rows = new ArrayList<PruebasCustom>();
				closeCon = validarConexion();
				errorlog+="llego a filtro "+object.lstFiltro.toString()
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
						
					case "TIPO DE PRUEBA, RESIDENCIA":
						if (residencia.contains("WHERE")) {
							residencia += " AND "
						} else {
							residencia += " WHERE "
						}
						
						residencia +="  ( LOWER(residencia) LIKE LOWER('%[valor]%')";
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						
						residencia +=" OR LOWER(tipo_prueba) LIKE LOWER('%[valor]%') )";
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						
						break;
					case "ID":
						where +=" AND CAST(Pruebas.persistenceid as varchar)";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE DE LA PRUEBA":
						where +=" AND LOWER(Pruebas.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CUPO DE LA PRUEBA":
						where +=" AND CAST(Pruebas.cupo as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					/*case "ALUMNOS REGISTRADOS":
						if (residencia.contains("WHERE")) {
							residencia += " AND "
						} else {
							residencia += " WHERE "
						}
						
						residencia +=" CAST(registrados as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							residencia+="='[valor]'"
						}else {
							residencia+="LIKE '%[valor]%'"
						}
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						break;*/
					case "ALUMNOS REGISTRADOS":
						where +=" AND Pruebas.registrados ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="= [valor]"
						}else {
							where+="= [valor]"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					break;

					case "RESIDENCIA":
						if (residencia.contains("WHERE")) {
							residencia += " AND "
						} else {
							residencia += " WHERE "
						}
						residencia +=" LOWER(residencia) ";
						if(filtro.get("operador").equals("Igual a")) {
							residencia+="=LOWER('[valor]')"
						}else {
							residencia+="LIKE LOWER('%[valor]%')"
						}
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						break;
						
					case "FECHA":
						where +=" AND  ( LOWER( CAST(TO_CHAR(Pruebas.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') ";
						where += "OR LOWER(Pruebas.entrada) LIKE LOWER('%[valor]%') "
						where += "OR LOWER(Pruebas.salida) LIKE LOWER('%[valor]%') )"
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LUGAR":
						where +=" AND LOWER(Pruebas.lugar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
						
					case "TIPO DE PRUEBA":
						where +=" AND LOWER(ctipoprueba.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE DE LA SESION":
						where +=" AND LOWER(Sesion.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CAMPUS":
						where +=" AND LOWER(campus.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					}
					
					
				}
					errorlog+="llego al orderby "
					switch(object.orderby) {
						
						case "ID":
						orderby+="pruebas_id";
						break;
						case "NOMBRE":
						orderby+="Pruebas.nombre";
						break;
						case "ALUMNOS REGISTRADOS":
						orderby+="Pruebas.registrados";
						break;
						case "CUPO":
						orderby+="Pruebas.cupo";
						break;
						case "RESIDENCIA":
						orderby+="Sesion.tipo";
						break;
						case "FECHA":
						orderby+="Pruebas.aplicacion";
						break;
						case "LUGAR":
						orderby+="Pruebas.lugar";
						break;
						case "TIPO_PRUEBA":
						orderby+="ctipoprueba.descripcion";
						break;
						case "NOMBRE SESION":
						orderby+="Sesion.nombre";
						break;
						default:
						orderby+="Pruebas.aplicacion"
						break;
						
					}
					errorlog+="paso el order "
					orderby+=" "+object.orientation;
					consulta=consulta.replace("[WHERE]", where);
					consulta=consulta.replace("[CAMPUS]", campus);
					
					
					errorlog+="paso el where:"
					String consulta_aspirante =  Statements.EXT_SESIONESPSICOLOGO;
					
					String conteo = Statements.COUNT_SESIONESPSICOLOGO
					//conteo=conteo.replace("[COUNTASPIRANTES]", consulta_aspirante)
					conteo=conteo.replace("[COUNTASPIRANTES]", '')
					conteo=conteo.replace("[WHERE]", where);
					conteo=conteo.replace("[CAMPUS]", campus);
					conteo=conteo.replace("[RESIDENCIA]", residencia);
					pstm = con.prepareStatement(conteo)
					
					rs= pstm.executeQuery()
					if(rs.next()) {
						resultado.setTotalRegistros(rs.getInt("registros"))
					}
					errorlog+="paso el registro"
					
					
					//
					consulta=consulta.replace("[ORDERBY]", orderby)
					consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
					consulta=consulta.replace("[RESIDENCIA]", residencia)
					consulta=consulta.replace("[COUNTASPIRANTES]", "")
					errorlog+="conteo exitoso "
					
					pstm = con.prepareStatement(consulta)
					pstm.setInt(1, object.limit)
					pstm.setInt(2, object.offset)
					
					PruebasCustom Pruebas = new PruebasCustom();
					rs = pstm.executeQuery()
					errorlog+="Listado "
					while(rs.next()) {
						
						row = new PruebaCustom();
						row.setNombre(rs.getString("nombre"))
						row.setRegistrados(rs.getInt("registrados"));
						row.setLugar(rs.getString("lugar"));
						row.setPersistenceId(rs.getLong("pruebas_id"));
						row.setCupo(rs.getInt("cupo"));
						row.setTipo(new CatTipoPrueba())
						row.getTipo().setDescripcion(rs.getString("tipo_prueba"));
						row.setEntrada(rs.getString("entrada"));
						row.setSalida(rs.getString("salida"))
						
						SesionCustom row2 = new SesionCustom();
						row2.setFecha_inicio(rs.getString("aplicacion"));
						row2.setTipo(rs.getString("residencia"));
						row2.setPersistenceId(rs.getLong("sesiones_id"));
						row2.setNombre(rs.getString("nombre_sesion"));
						
						
						Pruebas = new PruebasCustom();
						Pruebas.setPrueba(row);
						Pruebas.setSesion(row2);
						
						rows.add(Pruebas)
					}
					
				resultado.setError_info(consulta +" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
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
	
	public Result pasarLista(Long caseid, Boolean asistio) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		List<String> data = new ArrayList<String>();
		
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			pstm = con.prepareStatement(Statements.UPDATE_PASE_LISTA_ASISTENCIA);
			pstm.setBoolean(1, asistio);
			pstm.setLong(2, caseid);
			
			if(pstm.executeUpdate() < 1) {
				throw new Exception("No se ha podido actualizar el pase de lista.");
			}
			
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			resultado.setError_info(errorLog);
			if(con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
}
