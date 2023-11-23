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
import org.bonitasoft.web.extension.rest.RestAPIContext
import com.anahuac.posgrados.catalog.PSGRCatCampus
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.custom.Calendario
import com.anahuac.rest.api.Entity.custom.PruebaCustom
import com.anahuac.rest.api.Entity.custom.PruebasCustom
import com.anahuac.rest.api.Entity.custom.ResponsableCustom
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
	
	public Result getSesionesV1() {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		SesionesPosibles row = new SesionesPosibles();
		List < SesionesPosibles > rows = new ArrayList < SesionesPosibles > ();
		
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.GET_SESIONES_POSIBLES);
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new SesionesPosibles();
				
				row.setPersistenceId(rs.getLong("persistenceid"));
				row.setPersistenceId_string(rs.getString("persistenceId_string"));
				row.setFecha_entrevista(convertirFecha(rs.getString("fecha_entrevista")));
				row.setNombre(rs.getString("nombre"));
				row.setDescripcion_entrevista(rs.getString("descripcion_entrevista"));
				
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
		List < Map<String, Object> > rows = new ArrayList < Map<String, Object> > ();
		
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
			
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				idSesion = rs.getLong("persistenceid");
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
			
			List<Map<String, Object>> lstHorarios = (List<Map<String, Object>>) object.horarios;
			errorLog += "1|";
			for(Map<String, Object> horario: lstHorarios) {
				errorLog += "1|";
				pstm = con.prepareStatement(Statements.INSERT_HORARIOS);
				pstm.setString(1, horario.get("hora_inicio"));
				pstm.setString(2, horario.get("hora_fin"));
				pstm.setLong(3, idSesion);
				
				rs = pstm.executeQuery();
				
				if(rs.next()) {
					Long horario_pid = rs.getLong("persistenceid");
					List<Map<String, Object>> lstResponsables = (List<Map<String, Object>>) horario.get("responsables");
					errorLog += "1|";
					for(Map<String, Object> responsable: lstResponsables) {
						pstm = con.prepareStatement(Statements.INSERT_RESPONSABLE_CITA);
						pstm.setLong(1, horario_pid);
						pstm.setLong(2, Integer.valueOf(responsable.get("responsable_id")));
						pstm.setLong(3, idSesion);
						pstm.setBoolean(4, responsable.get("ocupado"));
						pstm.setBoolean(5, responsable.get("disponible_resp"));
						
						if(pstm.executeUpdate() == 0) {
							throw new Exception("No se ha podido insertar el responsable");
						}
					}
				} else {
					throw new Exception("No se ha podido insertar el  horario");
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
		
		try {
			closeCon = validarConexion();
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
			
			Date fecha = sdfEntrada.parse(object.fecha_entrevista);
			fechaHoraFormateada = formato.format(fecha);
			
			pstm = con.prepareStatement(Statements.UPDATE_SESION);
			
			pstm.setString(1, object.nombre);
			pstm.setString(2, object.nombre);
			pstm.setLong(3, Long.valueOf(object.persistenceid));
			
			rs = pstm.executeQuery();
			
			List<Map<String, String>> lstHorarios = generarHoras(Integer.parseInt(object.duracion_entrevista_minutos));
			for(Map<String, String> horario: lstHorarios) {
				pstm = con.prepareStatement(Statements.INSERT_HORARIOS);
				pstm.setString(1, horario.get("inicio"));
				pstm.setString(2, horario.get("fin"));
				pstm.setLong(3, idSesion);
				
				pstm.executeUpdate();
			}
			
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertSesion] " + e.getMessage());
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
}
