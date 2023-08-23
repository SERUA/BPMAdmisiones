package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.custom.CatPreguntasCustomFiltro
import com.anahuac.rest.api.Entity.custom.ConfiguracionesINVP
import com.anahuac.rest.api.Entity.custom.SesionCampusCustom
import com.anahuac.rest.api.Entity.custom.SesionesCustom

import groovy.json.JsonSlurper

class CatalogosDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogosDAO.class);
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
	
	public Result simpleSelect(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof List;
			
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				for(def row: object) {
				pstm = con.prepareStatement(row)
				
				
				rs = pstm.executeQuery()
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
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				}
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
	
	public Result simpleSelectBonita(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof List;
			
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexionBonita();
				for(def row: object) {
				pstm = con.prepareStatement(row)
				
				
				rs = pstm.executeQuery()
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
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				}
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
	
public Result getCatPreguntas(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "WHERE ISELIMINADO=false", orderby = "ORDER BY ", errorlog = ""

		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatPreguntasCustomFiltro row = new CatPreguntasCustomFiltro()
			List < CatPreguntasCustomFiltro > rows = new ArrayList < CatPreguntasCustomFiltro > ();
			closeCon = validarConexion();

			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				switch (filtro.get("columna")) {
					case "ORDEN":
						if (where.contains("WHERE")) {
							where += " AND ";
						} else {
							where += " WHERE ";
						}
						where += " LOWER(COALESCE(orden, 0)::VARCHAR) ";
						if (filtro.operador.equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.valor);
						break;
					case "PREGUNTA":
						if (where.contains("WHERE")) {
							where += " AND ";
						} else {
							where += " WHERE ";
						}
						where += " LOWER(PREGUNTA) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "QUESTION":
						if (where.contains("WHERE")) {
							where += " AND ";
						} else {
							where += " WHERE ";
						}
						where += " LOWER(QUESTION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "USUARIO CREACIÓN":
						if (where.contains("WHERE")) {
							where += " AND ";
						} else {
							where += " WHERE ";
						}
						where += " LOWER(usuariocreacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "FECHA CREACIÓN":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " TO_CHAR(TO_TIMESTAMP(FECHACREACION, 'YYYY-MM-DD HH24:MI:SS'), 'DD-MM-YYYY HH24:MI:SS') ";
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
				case "PREGUNTA":
					orderby += "clave";
					break;
				case "QUESTION":
					orderby += "descripcion";
					break;
				case "USUARIO CREACIÓN":
					orderby += "usuariocreacion";
					break;
				case "FECHA CREACIÓN":
					orderby += "fechacreacion";
					break;
				default:
					orderby += "ORDEN";
					break;
			}

			orderby += " " + object.orientation;
			String consulta = Statements.GET_CAT_PREGUNTAS;
			consulta = consulta.replace("[WHERE]", where);
			errorlog += "consulta:";
			errorlog += consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "");
			pstm = con.prepareStatement(consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""));
			rs = pstm.executeQuery();
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"));
			}
			consulta = consulta.replace("[ORDERBY]", orderby);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			errorlog += "consulta:";
			errorlog += consulta;
			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new CatPreguntasCustomFiltro()
				row.setPregunta(rs.getString("pregunta"));
				row.setCaseId(rs.getString("CASEID"));
				row.setOrden(rs.getLong("orden"));
				row.setQuestion(rs.getString("question"));
				row.setIsEliminado(rs.getBoolean("isEliminado"));
				row.setUsuarioCreacion(rs.getString("usuarioCreacion"));
				try {
					row.setFechaCreacion(rs.getString("fechacreacion"))
				} catch (Exception e) {
					LOGGER.error "[ERROR] " + e.getMessage();
					errorlog += e.getMessage()
				}
				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				rows.add(row)
			}
			resultado.setSuccess(true)

			resultado.setData(rows)
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getCatPreguntasExamen() {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "WHERE ISELIMINADO=false"
		String errorlog = "";
		try {
			CatPreguntasCustomFiltro row = new CatPreguntasCustomFiltro()
			List < CatPreguntasCustomFiltro > rows = new ArrayList < CatPreguntasCustomFiltro > ();
			closeCon = validarConexion();

			errorlog += "consulta:";
			pstm = con.prepareStatement(Statements.GET_CAT_PREGUNTAS_EXAMEN.replace("[WHERE]", where));
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new CatPreguntasCustomFiltro()
				row.setPregunta(rs.getString("pregunta"));
				row.setCaseId(rs.getString("CASEID"));
				row.setOrden(rs.getLong("orden"));
				row.setQuestion(rs.getString("question"));
				row.setIsEliminado(rs.getBoolean("isEliminado"));
				row.setUsuarioCreacion(rs.getString("usuarioCreacion"));
				try {
					row.setFechaCreacion(rs.getString("fechacreacion"))
				} catch (Exception e) {
					LOGGER.error "[ERROR] " + e.getMessage();
					errorlog += e.getMessage()
				}
				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				rows.add(row)
			}
			resultado.setSuccess(true)

			resultado.setData(rows)
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getSesiones(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
//		String where = "WHERE ctp.iseliminado <> true AND res.responsableid = [USUARIO] ";
		String where = "WHERE ctp.iseliminado <> true AND p.iseliminado <> true ";
		String errorlog = "";
		String orderBy = "";
		List < String > lstGrupo = new ArrayList < String > ();
		String errorLog = "";
		Boolean esPsicologo = false;
		Boolean esTi = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			assert object instanceof Map;
			
			Long userLogged = context.getApiSession().getUserId();
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)
            userLogged = context.getApiSession().getUserId();
            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (CatCampus rowGrupo: lstCatCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
                        lstGrupo.add(rowGrupo.getDescripcion());
                        break;
                    }
                }
            }
			
			if (lstGrupo.size() > 0 && object.campus == null) {
				where += " AND (";
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += " cca.descripcion = '" + campusMiembro + "'"
					if (i == (lstGrupo.size() - 1)) {
						where += ") "
					} else {
						where += " OR "
					}
				}
				errorLog += "] "
			} else if(object.campus != null) {
				where += " AND cca.grupobonita = '" + object.campus + "'"
			}
			
			for (UserMembership objUserMembership: lstUserMembership) {
				if(objUserMembership.getRoleName().equals("PSICOLOGO") ){
					esPsicologo = true;
				} else if (objUserMembership.getRoleName().equals("PSICOLOGO SUPERVISOR") ||
					objUserMembership.getRoleName().equals("TI CAMPUS") ||
					objUserMembership.getRoleName().equals("TI SERUA") ||
					objUserMembership.getRoleName().equals("ADMINISTRADOR")) {
					esTi = true;
				}
				for (CatCampus rowGrupo: lstCatCampus) {
					if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}
			
			if(esPsicologo && !esTi) {
				errorLog += " | Filtro aplicado"
				where += " AND res.responsableid = [USUARIO] AND res.iseliminado <> true ";
				where = where.replace("[USUARIO]", object.user_id);
				errorLog += " | WHERE: " + where;
			}
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				switch (filtro.get("columna")) {

					case "No. ":
						errorlog += "id_sesion "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(id_sesion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Nombre":
						errorlog += "ses.nombre"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(ses.nombre) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Uni.":
						errorlog += "cca.descripcion"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(cca.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Sesión":
						errorlog += " ses.nombre"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(ses.nombre) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Fecha":
						errorlog += "ses.fecha_inicio"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( ses.fecha_inicio::VARCHAR like '%[valor]%' )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Hr. Inicio":
						errorlog += "p.entrada"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(p.entrada) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Hr. Término":
						errorlog += " p.salida"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(p.salida) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Estatus":
//						errorlog += "cca.descripcion"
//						if (where.contains("WHERE")) {
//							where += " AND "
//						} else {
//							where += " WHERE "
//						}
//						where += " ( LOWER(cca.descripcion) like lower('%[valor]%') )";
//						where = where.replace("[valor]", filtro.get("valor"))
//						break;
					case "Aspirantes":
						errorlog += " p.registrados"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( p.registrados::VARCHAR like ('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Idioma":
						errorlog += "cca.descripcion"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(cca.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					default:
						break;
				}
			}
			
			switch(object.orderby) {
				case "id_sesion":
					orderBy = " ORDER BY id_sesion " + object.orientation;
				break;
				case "nombre":
					orderBy = " ORDER BY ses.nombre " + object.orientation;
				break;
				case "uni":
					orderBy = " ORDER BY cca.descripcion " + object.orientation;
				break;
				case "sesion":
					orderBy = " ORDER BY ses.nombre " + object.orientation;
				break;
				case "fecha_prueba":
					orderBy = " ORDER BY ses.fecha_inicio " + object.orientation;
				break;
				case "entrada_prueba":
					orderBy = " ORDER BY p.entrada " + object.orientation;
				break;
				case "salida_prueba":
					orderBy = " ORDER BY p.salida " + object.orientation;
				break;
				case "estatus":
//					orderBy = " ORDER BY id_sesion " + object.orientation;
				break;
				case "registrados_prueba":
					orderBy = " ORDER BY p.registrados " + object.orientation;
				break;
				case "idioma":
//					orderBy = " ORDER BY id_sesion " + object.orientation;
				break;
				default:
					orderBy = " ORDER BY id_sesion " + object.orientation;
				break;
			}
			
//			errorLog += where;
			
			SesionesCustom row = new SesionesCustom();
			List <SesionesCustom> rows = new ArrayList <SesionesCustom>();
			closeCon = validarConexion();
			
			String consultaCcount = Statements.GET_COUNT_SESONES_TODAS.replace("[WHERE]", where);
			pstm = con.prepareStatement(consultaCcount);
			rs = pstm.executeQuery();
			while (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("total_registros"));
			}
			
			String consulta = Statements.GET_SESONES_TODAS.replace("[WHERE]", where).replace("[ORDERBY]", orderBy)
			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new SesionesCustom();
				row.setIdSesion(rs.getLong("id_sesion"));
				row.setNombreSesion(rs.getString("nombre_sesion"));
				row.setUni(rs.getString("campus"));
				row.setSesion(rs.getString("descripcion_sesion"));
				row.setFecha(rs.getString("fecha_prueba"));
				row.setHoraInicio(rs.getString("entrada_prueba"));
				row.setHoraTermino(rs.getString("salida_prueba"));
				row.setEstatus(rs.getString("estatus"));
				row.setAspirantes(rs.getString("registrados_prueba"));
				row.setAspirantesNoRegistrados(rs.getString("no_registrados_prueba"));
				row.setFechaValida(fechaValida(rs.getString("fecha_prueba") + " " + rs.getString("entrada_prueba")));
				row.setFinalizada(rs.getBoolean("finalizada"));
				row.setToleranciaMinutos(rs.getInt("toleranciaminutos"));
				row.setToleranciaSalidaMinutos(rs.getInt("toleranciasalidaminutos"));
				
				rows.add(row);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}
	
	public Result getSesionesToday(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
//		String where = "WHERE ctp.iseliminado <> true AND p.aplicacion = CURRENT_DATE AND res.responsableid = [USUARIO] ";
		String where = "WHERE ctp.iseliminado <> true AND p.iseliminado <> true AND p.aplicacion = CURRENT_DATE  ";
		String errorlog = "";
		String orderBy = "";
		List < String > lstGrupo = new ArrayList < String > ();
		String errorLog = "";
		Boolean esPsicologo = false;
		Boolean esTi = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			assert object instanceof Map;
			
			Long userLogged = context.getApiSession().getUserId();
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)
            userLogged = context.getApiSession().getUserId();
            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (CatCampus rowGrupo: lstCatCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
                        lstGrupo.add(rowGrupo.getDescripcion());
                        break;
                    }
                }
            }
			
			if (lstGrupo.size() > 0 && object.campus == null) {
				where += " AND (";
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += " cca.descripcion = '" + campusMiembro + "'"
					if (i == (lstGrupo.size() - 1)) {
						where += ") "
					} else {
						where += " OR "
					}
				}
				errorLog += "] "
			} else if(object.campus != null) {
				where += " AND cca.grupobonita = '" + object.campus + "'"
			}
			
			for (UserMembership objUserMembership: lstUserMembership) {
				if(objUserMembership.getRoleName().equals("PSICOLOGO") ){
					esPsicologo = true;
				} else if (objUserMembership.getRoleName().equals("PSICOLOGO SUPERVISOR") || 
					objUserMembership.getRoleName().equals("TI CAMPUS") || 
					objUserMembership.getRoleName().equals("TI SERUA") || 
					objUserMembership.getRoleName().equals("ADMINISTRADOR")) {
					esTi = true;
				}
				for (CatCampus rowGrupo: lstCatCampus) {
					if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}
			 
			if(esPsicologo && !esTi) {
				errorLog += " | Filtro aplicado"
				where += " AND res.responsableid = [USUARIO] AND res.iseliminado <> true";
				where = where.replace("[USUARIO]", object.user_id);
			}
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				switch (filtro.get("columna")) {

					case "No. ":
						errorlog += "id_sesion "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(id_sesion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Nombre":
						errorlog += "ses.nombre"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(ses.nombre) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Uni.":
						errorlog += "cca.descripcion"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(cca.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Sesión":
						errorlog += " ses.nombre"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(ses.nombre) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Fecha":
						errorlog += "ses.fecha_inicio"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( ses.fecha_inicio::VARCHAR like '%[valor]%' )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Hr. Inicio":
						errorlog += "p.entrada"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(p.entrada) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Hr. Término":
						errorlog += " p.salida"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(p.salida) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Estatus":
//						errorlog += "cca.descripcion"
//						if (where.contains("WHERE")) {
//							where += " AND "
//						} else {
//							where += " WHERE "
//						}
//						where += " ( LOWER(cca.descripcion) like lower('%[valor]%') )";
//						where = where.replace("[valor]", filtro.get("valor"))
//						break;
					case "Aspirantes":
						errorlog += " p.registrados"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( p.registrados::VARCHAR like ('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					default:
						break;
				}
			}
			
			switch(object.orderby) {
				case "id_sesion":
					orderBy = " ORDER BY id_sesion " + object.orientation;
				break;
				case "nombre":
					orderBy = " ORDER BY ses.nombre " + object.orientation;
				break;
				case "uni":
					orderBy = " ORDER BY cca.descripcion " + object.orientation;
				break;
				case "sesion":
					orderBy = " ORDER BY ses.nombre " + object.orientation;
				break;
				case "fecha_prueba":
					orderBy = " ORDER BY ses.fecha_inicio " + object.orientation;
				break;
				case "entrada_prueba":
					orderBy = " ORDER BY p.entrada " + object.orientation;
				break;
				case "salida_prueba":
					orderBy = " ORDER BY p.salida " + object.orientation;
				break;
				case "estatus":
//					orderBy = " ORDER BY id_sesion " + object.orientation;
				break;
				case "registrados_prueba":
					orderBy = " ORDER BY p.registrados " + object.orientation;
				break;
				default:
					orderBy = " ORDER BY id_sesion " + object.orientation;
				break;
			}
			
			SesionesCustom row = new SesionesCustom();
			List <SesionesCustom> rows = new ArrayList <SesionesCustom>();
			closeCon = validarConexion();
			
			String consultaCcount = Statements.GET_COUNT_SESONES_TODAS.replace("[WHERE]", where);
			pstm = con.prepareStatement(consultaCcount);
			rs = pstm.executeQuery();
			while (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("total_registros"));
			}
			
			String consulta = Statements.GET_SESONES_TODAS.replace("[WHERE]", where).replace("[ORDERBY]", orderBy)
			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new SesionesCustom();
				row.setIdSesion(rs.getLong("id_sesion"));
				row.setNombreSesion(rs.getString("nombre_sesion"));
				row.setUni(rs.getString("campus"));
				row.setSesion(rs.getString("descripcion_sesion"));
				row.setFecha(rs.getString("fecha_prueba"));
				row.setHoraInicio(rs.getString("entrada_prueba"));
				row.setHoraTermino(rs.getString("salida_prueba"));
				row.setEstatus(rs.getString("estatus"));
				row.setAspirantes(rs.getString("registrados_prueba"));
				row.setAspirantesNoRegistrados(rs.getString("no_registrados_prueba"));
				row.setFechaValida(fechaValida(rs.getString("fecha_prueba") + " " + rs.getString("entrada_prueba")));
				row.setFinalizada(rs.getBoolean("finalizada"));
				row.setToleranciaMinutos(rs.getInt("toleranciaminutos"));
				row.setToleranciaSalidaMinutos(rs.getInt("toleranciasalidaminutos"));
				
				rows.add(row);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}
	
	private boolean fechaValida(String fechaInput) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date fecha = formatter.parse(fechaInput);
		Date hoy = new Date();
		long diferenciaEnMs = hoy.getTime() - fecha.getTime();
		long diasTranscurridos = diferenciaEnMs / (1000 * 60 * 60 * 24);
		return diasTranscurridos < 3;
	}
	
	public Result getExcelFileSesiones(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		List < Object > lstParams;
		String errorlog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			Result dataResult = new Result();
			int rowCount = 0;

			String type = "Sesiones";
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(type);
			CellStyle style = workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);

			dataResult = getSesiones(jsonData, context);

			if (dataResult.success) {
				lstParams = dataResult.getData();
			} else {
				throw new Exception("No encontro datos");
			}
			
			Row titleRow = sheet.createRow(++rowCount);
			Cell cellReporte = titleRow.createCell(1);
			cellReporte.setCellValue("Reporte:");
			cellReporte.setCellStyle(style);
			Cell cellTitle = titleRow.createCell(2);
			cellTitle.setCellValue("SESIONES DE EXAMEN PSICOMÉTRICO");
			Cell cellFecha = titleRow.createCell(4);
			cellFecha.setCellValue("Fecha:");
			cellFecha.setCellStyle(style);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -7);
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
			cellusuarioData.setCellValue(object.usuario);
			Row espacio = sheet.createRow(++rowCount);
			Row headersRow = sheet.createRow(++rowCount);
			Cell header0 = headersRow.createCell(0);
			header0.setCellValue("ID sesión");
			header0.setCellStyle(style);
			Cell header1 = headersRow.createCell(1);
			header1.setCellValue("Nombre");
			header1.setCellStyle(style);
			Cell header2 = headersRow.createCell(2);
			header2.setCellValue("Uni.");
			header2.setCellStyle(style);
			Cell header3 = headersRow.createCell(3);
			header3.setCellValue("Sesión");
			header3.setCellStyle(style);
			Cell header4 = headersRow.createCell(4);
			header4.setCellValue("Fecha");
			header4.setCellStyle(style);
			Cell header5 = headersRow.createCell(5);
			header5.setCellValue("Hr. Inicio");
			header5.setCellStyle(style);
			Cell header6 = headersRow.createCell(6);
			header6.setCellValue("Hr. Término");
			header6.setCellStyle(style);
			Cell header7 = headersRow.createCell(7);
			header7.setCellValue("Estatus");
			header7.setCellStyle(style);
			Cell header8 = headersRow.createCell(8);
			header8.setCellValue("Aspirantes");
			header8.setCellStyle(style);
			Cell header9 = headersRow.createCell(9);
			header9.setCellValue("Aspirantes no registrados");
			header9.setCellStyle(style);
			
			for (int i = 0; i < lstParams.size(); ++i) {
				Row row = sheet.createRow(++rowCount);
				Cell cell0 = row.createCell(0);
				cell0.setCellValue(lstParams[i].idSesion);
				Cell cell1 = row.createCell(1);
				cell1.setCellValue(lstParams[i].nombreSesion);
				Cell cell2 = row.createCell(2);
				cell2.setCellValue(lstParams[i].uni);
				Cell cell3 = row.createCell(3);
				cell3.setCellValue(lstParams[i].sesion);
				Cell cell4 = row.createCell(4);
				cell4.setCellValue(lstParams[i].fecha);
				Cell cell5 = row.createCell(5);
				cell5.setCellValue(lstParams[i].horaInicio);
				Cell cell6 = row.createCell(6);
				cell6.setCellValue(lstParams[i].horaTermino);
				Cell cell7 = row.createCell(7);
				cell7.setCellValue(lstParams[i].estatus);
				Cell cell8 = row.createCell(8);
				cell8.setCellValue(lstParams[i].aspirantes);
				Cell cell9 = row.createCell(9);
				cell9.setCellValue(lstParams[i].aspirantesNoRegistrados);
			}


			for (int i = 0; i <= (20); ++i) {
				sheet.autoSizeColumn(i);
			}
			FileOutputStream outputStream = new FileOutputStream("Report.xls");
			workbook.write(outputStream);

			List < Object > lstResultado = new ArrayList < Object > ();
			lstResultado.add(encodeFileToBase64Binary("Report.xls"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			e.printStackTrace();
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}

		return resultado;
	}
	
	public Result getExcelFileSesionesToday(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		List < Object > lstParams;
		String errorlog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			Result dataResult = new Result();
			int rowCount = 0;

			String type = "Sesiones";
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(type);
			CellStyle style = workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);

			dataResult = getSesionesToday(jsonData, context);

			if (dataResult.success) {
				lstParams = dataResult.getData();
			} else {
				throw new Exception("No encontro datos");
			}
			
			Row titleRow = sheet.createRow(++rowCount);
			Cell cellReporte = titleRow.createCell(1);
			cellReporte.setCellValue("Reporte:");
			cellReporte.setCellStyle(style);
			Cell cellTitle = titleRow.createCell(2);
			cellTitle.setCellValue("SESIONES DE EXAMEN PSICOMÉTRICO");
			Cell cellFecha = titleRow.createCell(4);
			cellFecha.setCellValue("Fecha:");
			cellFecha.setCellStyle(style);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -7);
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
			cellusuarioData.setCellValue(object.usuario);
			Row espacio = sheet.createRow(++rowCount);
			Row headersRow = sheet.createRow(++rowCount);
			Cell header0 = headersRow.createCell(0);
			header0.setCellValue("ID sesión");
			header0.setCellStyle(style);
			Cell header1 = headersRow.createCell(1);
			header1.setCellValue("Nombre");
			header1.setCellStyle(style);
			Cell header2 = headersRow.createCell(2);
			header2.setCellValue("Uni.");
			header2.setCellStyle(style);
			Cell header3 = headersRow.createCell(3);
			header3.setCellValue("Sesión");
			header3.setCellStyle(style);
			Cell header4 = headersRow.createCell(4);
			header4.setCellValue("Fecha");
			header4.setCellStyle(style);
			Cell header5 = headersRow.createCell(5);
			header5.setCellValue("Hr. Inicio");
			header5.setCellStyle(style);
			Cell header6 = headersRow.createCell(6);
			header6.setCellValue("Hr. Término");
			header6.setCellStyle(style);
			Cell header7 = headersRow.createCell(7);
			header7.setCellValue("Estatus");
			header7.setCellStyle(style);
			Cell header8 = headersRow.createCell(8);
			header8.setCellValue("Aspirantes");
			header8.setCellStyle(style);
			Cell header9 = headersRow.createCell(9);
			header9.setCellValue("Aspirantes no registrados");
			header9.setCellStyle(style);
			
			for (int i = 0; i < lstParams.size(); ++i) {
				Row row = sheet.createRow(++rowCount);
				Cell cell0 = row.createCell(0);
				cell0.setCellValue(lstParams[i].idSesion);
				Cell cell1 = row.createCell(1);
				cell1.setCellValue(lstParams[i].nombreSesion);
				Cell cell2 = row.createCell(2);
				cell2.setCellValue(lstParams[i].uni);
				Cell cell3 = row.createCell(3);
				cell3.setCellValue(lstParams[i].sesion);
				Cell cell4 = row.createCell(4);
				cell4.setCellValue(lstParams[i].fecha);
				Cell cell5 = row.createCell(5);
				cell5.setCellValue(lstParams[i].horaInicio);
				Cell cell6 = row.createCell(6);
				cell6.setCellValue(lstParams[i].horaTermino);
				Cell cell7 = row.createCell(7);
				cell7.setCellValue(lstParams[i].estatus);
				Cell cell8 = row.createCell(8);
				cell8.setCellValue(lstParams[i].aspirantes);
				Cell cell9 = row.createCell(9);
				cell9.setCellValue(lstParams[i].aspirantesNoRegistrados);
			}


			for (int i = 0; i <= (20); ++i) {
				sheet.autoSizeColumn(i);
			}
			FileOutputStream outputStream = new FileOutputStream("Report.xls");
			workbook.write(outputStream);

			List < Object > lstResultado = new ArrayList < Object > ();
			lstResultado.add(encodeFileToBase64Binary("Report.xls"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			e.printStackTrace();
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}

		return resultado;
	}
	
	public Result getExcelFileAspirantes(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		List < Object > lstParams;
		String errorlog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			Result dataResult = new Result();
			int rowCount = 0;

			String type = "Aspirantes";
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(type);
			CellStyle style = workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);

			dataResult = new UsuariosDAO().getAspirantes(jsonData, context);

			if (dataResult.success) {
				lstParams = dataResult.getData();
			} else {
				throw new Exception("No encontro datos");
			}
			
			Row titleRow = sheet.createRow(++rowCount);
			Cell cellReporte = titleRow.createCell(1);
			cellReporte.setCellValue("Reporte:");
			cellReporte.setCellStyle(style);
			Cell cellTitle = titleRow.createCell(2);
			cellTitle.setCellValue("ASPIRANTES CON SESIÓN");
			Cell cellFecha = titleRow.createCell(4);
			cellFecha.setCellValue("Fecha:");
			cellFecha.setCellStyle(style);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -7);
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
			cellusuarioData.setCellValue(object.usuario);
			Row espacio = sheet.createRow(++rowCount);
			Row headersRow = sheet.createRow(++rowCount);
			
			Cell header0 = headersRow.createCell(0);
			header0.setCellValue("No. Caso");
			header0.setCellStyle(style);
			Cell header1 = headersRow.createCell(1);
			header1.setCellValue("Id banner");
			header1.setCellStyle(style);
			Cell header2 = headersRow.createCell(2);
			header2.setCellValue("Nombre");
			header2.setCellStyle(style);
			Cell header3 = headersRow.createCell(3);
			header3.setCellValue("Uni.");
			header3.setCellStyle(style);
			Cell header4 = headersRow.createCell(4);
			header4.setCellValue("Teléfono");
			header4.setCellStyle(style);
			Cell header5 = headersRow.createCell(5);
			header5.setCellValue("Celular");
			header5.setCellStyle(style);
			Cell header6 = headersRow.createCell(6);
			header6.setCellValue("Correo");
			header6.setCellStyle(style);
			Cell header7 = headersRow.createCell(7);
			header7.setCellValue("Preguntas");
			header7.setCellStyle(style);
			Cell header8 = headersRow.createCell(8);
			header8.setCellValue("Contestadas");
			header8.setCellStyle(style);
			Cell header9 = headersRow.createCell(9);
			header9.setCellValue("Inicio");
			header9.setCellStyle(style);
			Cell header10 = headersRow.createCell(10);
			header10.setCellValue("Término");
			header10.setCellStyle(style);
			Cell header11 = headersRow.createCell(11);
			header11.setCellValue("Tiempo");
			header11.setCellStyle(style);
			Cell header12 = headersRow.createCell(12);
			header12.setCellValue("Estatus");
			header12.setCellStyle(style);
			Cell header13 = headersRow.createCell(13);
			header13.setCellValue("Idioma");
			header13.setCellStyle(style);
			
			for (int i = 0; i < lstParams.size(); ++i) {
				Row row = sheet.createRow(++rowCount);
				Cell cell0 = row.createCell(0);
				cell0.setCellValue(lstParams[i].idBpm);
				Cell cell1 = row.createCell(1);
				cell1.setCellValue(!lstParams[i].idBanner.equals("") ? lstParams[i].idBanner : "N/A");
				Cell cell2 = row.createCell(2);
				cell2.setCellValue(lstParams[i].nombre);
				Cell cell3 = row.createCell(3);
				cell3.setCellValue(lstParams[i].uni);
				Cell cell4 = row.createCell(4);
				cell4.setCellValue(!lstParams[i].telefono.equals("") ? lstParams[i].telefono : "N/A");
				Cell cell5 = row.createCell(5);
				cell5.setCellValue(lstParams[i].celular);
				Cell cell6 = row.createCell(6);
				cell6.setCellValue(lstParams[i].correoElectronico);
				Cell cell7 = row.createCell(7);
				cell7.setCellValue(lstParams[i].preguntas);
				Cell cell8 = row.createCell(8);
				cell8.setCellValue(lstParams[i].contestadas);
				Cell cell9 = row.createCell(9);
				cell9.setCellValue(lstParams[i].inicio);
				Cell cell10 = row.createCell(10);
				cell10.setCellValue(lstParams[i].termino);
				Cell cell11 = row.createCell(11);
				cell11.setCellValue(lstParams[i].tiempo);
				Cell cell12 = row.createCell(12);
				cell12.setCellValue(lstParams[i].estatusINVP);
				Cell cell13 = row.createCell(13);
				cell13.setCellValue(lstParams[i].idioma);
			}

			for (int i = 0; i <= (20); ++i) {
				sheet.autoSizeColumn(i);
			}
			FileOutputStream outputStream = new FileOutputStream("Report.xls");
			workbook.write(outputStream);

			List < Object > lstResultado = new ArrayList < Object > ();
			lstResultado.add(encodeFileToBase64Binary("Report.xls"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			e.printStackTrace();
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}

		return resultado;
	}
	
	public Result getExcelFileAspirantesTodos(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		List < Object > lstParams;
		String errorlog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			Result dataResult = new Result();
			int rowCount = 0;

			String type = "Aspirantes";
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(type);
			CellStyle style = workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);

			dataResult = new UsuariosDAO().getAspirantesTodos(jsonData, context);

			if (dataResult.success) {
				lstParams = dataResult.getData();
			} else {
				throw new Exception("No encontro datos");
			}
			
			Row titleRow = sheet.createRow(++rowCount);
			Cell cellReporte = titleRow.createCell(1);
			cellReporte.setCellValue("Reporte:");
			cellReporte.setCellStyle(style);
			Cell cellTitle = titleRow.createCell(2);
			cellTitle.setCellValue("ASPIRANTES CON SESIÓN");
			Cell cellFecha = titleRow.createCell(4);
			cellFecha.setCellValue("Fecha:");
			cellFecha.setCellStyle(style);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -7);
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
			cellusuarioData.setCellValue(object.usuario);
			Row espacio = sheet.createRow(++rowCount);
			Row headersRow = sheet.createRow(++rowCount);
			
			Cell header0 = headersRow.createCell(0);
			header0.setCellValue("No. Caso");
			header0.setCellStyle(style);
			Cell header1 = headersRow.createCell(1);
			header1.setCellValue("Id banner");
			header1.setCellStyle(style);
			Cell header2 = headersRow.createCell(2);
			header2.setCellValue("Nombre");
			header2.setCellStyle(style);
			Cell header3 = headersRow.createCell(3);
			header3.setCellValue("Uni.");
			header3.setCellStyle(style);
			Cell header4 = headersRow.createCell(4);
			header4.setCellValue("Teléfono");
			header4.setCellStyle(style);
			Cell header5 = headersRow.createCell(5);
			header5.setCellValue("Celular");
			header5.setCellStyle(style);
			Cell header6 = headersRow.createCell(6);
			header6.setCellValue("Correo");
			header6.setCellStyle(style);
			Cell header7 = headersRow.createCell(7);
			header7.setCellValue("Preguntas");
			header7.setCellStyle(style);
			Cell header8 = headersRow.createCell(8);
			header8.setCellValue("Contestadas");
			header8.setCellStyle(style);
			Cell header9 = headersRow.createCell(9);
			header9.setCellValue("Inicio");
			header9.setCellStyle(style);
			Cell header10 = headersRow.createCell(10);
			header10.setCellValue("Término");
			header10.setCellStyle(style);
			Cell header11 = headersRow.createCell(11);
			header11.setCellValue("Tiempo");
			header11.setCellStyle(style);
			Cell header12 = headersRow.createCell(12);
			header12.setCellValue("Estatus");
			header12.setCellStyle(style);
			Cell header13 = headersRow.createCell(13);
			header13.setCellValue("Idioma");
			header13.setCellStyle(style);
			
			for (int i = 0; i < lstParams.size(); ++i) {
				Row row = sheet.createRow(++rowCount);
				Cell cell0 = row.createCell(0);
				cell0.setCellValue(lstParams[i].idBpm);
				Cell cell1 = row.createCell(1);
				cell1.setCellValue((!lstParams[i].idBanner.equals("") && lstParams[i].idBanner != null )? lstParams[i].idBanner : "N/A");
				Cell cell2 = row.createCell(2);
				cell2.setCellValue(lstParams[i].nombre);
				Cell cell3 = row.createCell(3);
				cell3.setCellValue(lstParams[i].uni);
				Cell cell4 = row.createCell(4);
				cell4.setCellValue((!lstParams[i].telefono.equals("") && lstParams[i].telefono != null)? lstParams[i].telefono : "N/A");
				Cell cell5 = row.createCell(5);
				cell5.setCellValue(lstParams[i].celular);
				Cell cell6 = row.createCell(6);
				cell6.setCellValue(lstParams[i].correoElectronico);
				Cell cell7 = row.createCell(7);
				cell7.setCellValue(lstParams[i].preguntas);
				Cell cell8 = row.createCell(8);
				cell8.setCellValue(lstParams[i].contestadas);
				Cell cell9 = row.createCell(9);
				cell9.setCellValue(lstParams[i].inicio);
				Cell cell10 = row.createCell(10);
				cell10.setCellValue(lstParams[i].termino);
				Cell cell11 = row.createCell(11);
				cell11.setCellValue(lstParams[i].tiempo);
				Cell cell12 = row.createCell(12);
				cell12.setCellValue(lstParams[i].estatusINVP);
				Cell cell13 = row.createCell(13);
				cell13.setCellValue(lstParams[i].idioma);
			}

			for (int i = 0; i <= (20); ++i) {
				sheet.autoSizeColumn(i);
			}
			FileOutputStream outputStream = new FileOutputStream("Report.xls");
			workbook.write(outputStream);

			List < Object > lstResultado = new ArrayList < Object > ();
			lstResultado.add(encodeFileToBase64Binary("Report.xls"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			e.printStackTrace();
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}

		return resultado;
	}
	
	public Result getExcelFileAspirantesTemporales(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		List < Object > lstParams;
		String errorlog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			Result dataResult = new Result();
			int rowCount = 0;

			String type = "Aspirantes temporales";
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(type);
			CellStyle style = workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);

			dataResult = new UsuariosDAO().getAspirantesTemporales(jsonData, context);

			if (dataResult.success) {
				lstParams = dataResult.getData();
			} else {
				throw new Exception("No encontro datos");
			}
			
			Row titleRow = sheet.createRow(++rowCount);
			Cell cellReporte = titleRow.createCell(1);
			cellReporte.setCellValue("Reporte:");
			cellReporte.setCellStyle(style);
			Cell cellTitle = titleRow.createCell(2);
			cellTitle.setCellValue("ASPIRANTES  CON SESIÓN TEMPORAL");
			Cell cellFecha = titleRow.createCell(4);
			cellFecha.setCellValue("Fecha:");
			cellFecha.setCellStyle(style);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -7);
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
			cellusuarioData.setCellValue(object.usuario);
			Row espacio = sheet.createRow(++rowCount);
			Row headersRow = sheet.createRow(++rowCount);
			
			Cell header0 = headersRow.createCell(0);
			header0.setCellValue("No. Caso");
			header0.setCellStyle(style);
			Cell header1 = headersRow.createCell(1);
			header1.setCellValue("Id banner");
			header1.setCellStyle(style);
			Cell header2 = headersRow.createCell(2);
			header2.setCellValue("Nombre");
			header2.setCellStyle(style);
			Cell header3 = headersRow.createCell(3);
			header3.setCellValue("Uni.");
			header3.setCellStyle(style);
			Cell header4 = headersRow.createCell(4);
			header4.setCellValue("Teléfono");
			header4.setCellStyle(style);
			Cell header5 = headersRow.createCell(5);
			header5.setCellValue("Celular");
			header5.setCellStyle(style);
			Cell header6 = headersRow.createCell(6);
			header6.setCellValue("Correo");
			header6.setCellStyle(style);
			Cell header7 = headersRow.createCell(7);
			header7.setCellValue("Preguntas");
			header7.setCellStyle(style);
			Cell header8 = headersRow.createCell(8);
			header8.setCellValue("Contestadas");
			header8.setCellStyle(style);
			Cell header9 = headersRow.createCell(9);
			header9.setCellValue("Inicio");
			header9.setCellStyle(style);
			Cell header10 = headersRow.createCell(10);
			header10.setCellValue("Término");
			header10.setCellStyle(style);
			Cell header11 = headersRow.createCell(11);
			header11.setCellValue("Tiempo");
			header11.setCellStyle(style);
			Cell header12 = headersRow.createCell(12);
			header12.setCellValue("Estatus");
			header12.setCellStyle(style);
			Cell header13 = headersRow.createCell(13);
			header13.setCellValue("Idioma");
			header13.setCellStyle(style);
			
			for (int i = 0; i < lstParams.size(); ++i) {
				Row row = sheet.createRow(++rowCount);
				Cell cell0 = row.createCell(0);
				cell0.setCellValue(lstParams[i].idBpm);
				Cell cell1 = row.createCell(1);
				cell1.setCellValue(lstParams[i].idBanner);
				Cell cell2 = row.createCell(2);
				cell2.setCellValue(lstParams[i].nombre);
				Cell cell3 = row.createCell(3);
				cell3.setCellValue(lstParams[i].uni);
				Cell cell4 = row.createCell(4);
				cell4.setCellValue(lstParams[i].telefono);
				Cell cell5 = row.createCell(5);
				cell5.setCellValue(lstParams[i].celular);
				Cell cell6 = row.createCell(6);
				cell6.setCellValue(lstParams[i].correoElectronico);
				Cell cell7 = row.createCell(7);
				cell7.setCellValue(lstParams[i].preguntas);
				Cell cell8 = row.createCell(8);
				cell8.setCellValue(lstParams[i].contestadas);
				Cell cell9 = row.createCell(9);
				cell9.setCellValue(lstParams[i].inicio);
				Cell cell10 = row.createCell(10);
				cell10.setCellValue(lstParams[i].termino);
				Cell cell11 = row.createCell(11);
				cell11.setCellValue(lstParams[i].tiempo);
				Cell cell12 = row.createCell(12);
				cell12.setCellValue(lstParams[i].estatusINVP);
				Cell cell13 = row.createCell(13);
				cell13.setCellValue(lstParams[i].idioma);
			}

			for (int i = 0; i <= (20); ++i) {
				sheet.autoSizeColumn(i);
			}
			FileOutputStream outputStream = new FileOutputStream("Report.xls");
			workbook.write(outputStream);

			List < Object > lstResultado = new ArrayList < Object > ();
			lstResultado.add(encodeFileToBase64Binary("Report.xls"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			e.printStackTrace();
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}

		return resultado;
	}
	
	private String encodeFileToBase64Binary(String fileName)
	throws IOException {

		File file = new File(fileName);
		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encoder.encode(bytes);
		String encodedString = new String(encoded);

		return encodedString;
	}

	private static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length &&
			(numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		is.close();
		return bytes;
	}
	
	public Result getExcelFileAspirantes(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		List < Object > lstParams;
		String errorlog = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			Result dataResult = new Result();
			int rowCount = 0;

			String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(type);
			CellStyle style = workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);

			dataResult = new UsuariosDAO().getUsuariosRegistrados(parameterP, parameterC, jsonData, context);

			if (dataResult.success) {
				lstParams = dataResult.getData();
			} else {
				throw new Exception("No encontro datos");
			}
			Row titleRow = sheet.createRow(++rowCount);
			Cell cellReporte = titleRow.createCell(1);
			cellReporte.setCellValue("Reporte:");
			cellReporte.setCellStyle(style);
			Cell cellTitle = titleRow.createCell(2);
			cellTitle.setCellValue("SESIONES DE EXAMEN PSICOMÉTRICO");
			Cell cellFecha = titleRow.createCell(4);
			cellFecha.setCellValue("Fecha:");
			cellFecha.setCellStyle(style);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -7);
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
			cellusuarioData.setCellValue(object.usuario);
			Row espacio = sheet.createRow(++rowCount);
			Row headersRow = sheet.createRow(++rowCount);
			Cell header0 = headersRow.createCell(0);
			header0.setCellValue("No.");
			header0.setCellStyle(style);
			Cell header1 = headersRow.createCell(1);
			header1.setCellValue("Nombre");
			header1.setCellStyle(style);
			Cell header2 = headersRow.createCell(2);
			header2.setCellValue("Uni.");
			header2.setCellStyle(style);
			Cell header3 = headersRow.createCell(3);
			header3.setCellValue("Sesión");
			header3.setCellStyle(style);
			Cell header4 = headersRow.createCell(4);
			header4.setCellValue("Fecha");
			header4.setCellStyle(style);
			Cell header5 = headersRow.createCell(5);
			header5.setCellValue("Hr. Inicio");
			header5.setCellStyle(style);
			Cell header6 = headersRow.createCell(6);
			header6.setCellValue("Hr. Término");
			header6.setCellStyle(style);
			Cell header7 = headersRow.createCell(7);
			header7.setCellValue("Estatus");
			header7.setCellStyle(style);
			Cell header8 = headersRow.createCell(8);
			header8.setCellValue("Aspirantes");
			header8.setCellStyle(style);
			Cell header9 = headersRow.createCell(9);
			header9.setCellValue("Idioma");
			header9.setCellStyle(style);
			for (int i = 0; i < lstParams.size(); ++i) {
				//SolicitudAdmisionCustom  solicitud = (SolicitudAdmisionCustom) lstParams.get(i);
				Row row = sheet.createRow(++rowCount);
				Cell cell0 = row.createCell(0);
				cell0.setCellValue(lstParams[i].idbanner);

				Cell cell1 = row.createCell(1);
				cell1.setCellValue(
					lstParams[i].apellidopaterno + " " +
					lstParams[i].apellidomaterno + " " +
					lstParams[i].primernombre + " " +
					lstParams[i].segundonombre
				);

				Cell cell2 = row.createCell(2);
				cell2.setCellValue(lstParams[i].correoelectronico);
				Cell cell3 = row.createCell(3);
				cell3.setCellValue(lstParams[i].curp);
				Cell cell4 = row.createCell(4);
				cell4.setCellValue(lstParams[i].licenciatura);
				Cell cell5 = row.createCell(5);
				cell5.setCellValue(lstParams[i].ingreso);
				Cell cell6 = row.createCell(6);
				cell6.setCellValue(lstParams[i].campus);
				Cell cell7 = row.createCell(7);
				cell7.setCellValue(lstParams[i].procedencia);
				Cell cell8 = row.createCell(8);
				cell8.setCellValue(lstParams[i].preparatoria);
				Cell cell9 = row.createCell(9);
				cell9.setCellValue(lstParams[i].promediogeneral);
				Cell cell10 = row.createCell(10);
				cell10.setCellValue(lstParams[i].estatussolicitud);
				Cell cell11 = row.createCell(11);
				cell11.setCellValue(lstParams[i].tipoalumno);
				Cell cell12 = row.createCell(12);
				cell12.setCellValue(lstParams[i].telefonocelular);
				Cell cell13 = row.createCell(13);
				cell13.setCellValue(lstParams[i].telefono);

			}


			for (int i = 0; i <= (20); ++i) {
				sheet.autoSizeColumn(i);
			}
			FileOutputStream outputStream = new FileOutputStream("Report.xls");
			workbook.write(outputStream);

			List < Object > lstResultado = new ArrayList < Object > ();
			lstResultado.add(encodeFileToBase64Binary("Report.xls"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			e.printStackTrace();
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}

		return resultado;
	}
	
	
	public Result insertUpdateConfiguracionSesion(String jsonData) {
		Result resultado = new Result();
		String errorlog = "";
		Boolean closeCon = false;
		Boolean existe = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			pstm = con.prepareStatement(Statements.GET_EXISTE_CONFIGURACION_INVP);
			pstm.setLong(1, object.idprueba);
			
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				existe = rs.getBoolean("existe");
			}
			
			if(existe) {
				pstm = con.prepareStatement(Statements.UPDATE_CONFIGURACION_INVP);
			} else {
				pstm = con.prepareStatement(Statements.INSERT_CONFIGURACION_INVP);
			}
			
			pstm.setInt(1, Integer.valueOf(object.toleranciaminutos));
			pstm.setInt(2, Integer.valueOf(object.toleranciasalidaminutos));
			pstm.setLong(3, object.idprueba);
			pstm.executeUpdate();
			
			//Actualizar a todos en la sesión
			pstm = con.prepareStatement(Statements.UPDATE_INVP_TOLERANCIA_TEMPORALES_SESION);
			pstm.setInt(1, Integer.valueOf(object.toleranciaminutos));
			pstm.setInt(2, Integer.valueOf(object.toleranciasalidaminutos));
			pstm.setLong(3, object.idprueba);
			pstm.executeUpdate();
			
			con.commit();
			Result resultadoTimers = new UsuariosDAO().updateTimerTodos(object.idprueba);
			
			resultado.setSuccess(true);
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
	
	
	
	public Result getConfiguracionSesion(Long idprueba) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			List<ConfiguracionesINVP> rows = new ArrayList<ConfiguracionesINVP>();
			ConfiguracionesINVP config = new ConfiguracionesINVP();
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_CONFIGURACION_INVP);
			pstm.setLong(1, idprueba);
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				config = new ConfiguracionesINVP();
				config.setIdPrueba(rs.getInt("idprueba"));
				config.setToleranciaMinutos(rs.getInt("toleranciaminutos"));
				config.setToleranciaSalidaMinutos(rs.getInt("toleranciasalidaminutos"));
				
				rows.add(config);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}
	
	public Result getSesionesByCampus(String campus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			List<SesionCampusCustom> rows = new ArrayList<SesionCampusCustom>();
			SesionCampusCustom sesion = new SesionCampusCustom();
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_SESIONES_BY_CAMPUS);
			pstm.setString(1, campus);
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				sesion = new SesionCampusCustom();
				sesion.setIdprueba(rs.getLong("idprueba"));
				sesion.setEntrada(rs.getString("entrada"));
				sesion.setSalida(rs.getString("salida"));
				sesion.setAplicacion(rs.getString("aplicacion"));
				sesion.setNombresesion(rs.getString("nombresesion"));
				sesion.setToleranciaminutos(rs.getInt("toleranciaminutos"));
				sesion.setToleranciasalidaminutos(rs.getInt("toleranciasalidaminutos"));
				sesion.setEstatus(rs.getString("estatus"));
				sesion.setCampus(rs.getString("campus"));
				sesion.setGrupobonita(rs.getString("grupobonita"));
				
				rows.add(sesion);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}
	
	public Result insertUpdateUsuarioNuevaConfig(String jsonData) {
		Result resultado = new Result();
		String errorlog = "";
		Boolean closeCon = false;
		Boolean existe = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			pstm = con.prepareStatement(Statements.GET_EXISTE_NUEVA_CONF_USUARIO);
			pstm.setString(1, object.username);
			
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				existe = rs.getBoolean("existe");
			}
			
			if(existe) {
				pstm = con.prepareStatement(Statements.UPDATE_NUEVA_CONFIG_USUARIO);
				pstm.setString(1, object.aplicacion);
				pstm.setString(2, object.entrada);
				pstm.setString(3, object.salida);
				pstm.setInt(4, object.toleranciaminutos);
				pstm.setInt(5, object.toleranciasalidaminutos);
//				pstm.setLong(6, object.idprueba);
				if(object.idprueba == null) {
					pstm.setNull(6, 0);
				} else {
					pstm.setLong(6, object.idprueba);
				}
				pstm.setString(7, object.username);
			} else {
				pstm = con.prepareStatement(Statements.INSERT_NUEVA_CONFIG_USUARIO);
				pstm.setString(1, object.username);
				pstm.setString(2, object.aplicacion);
				pstm.setString(3, object.entrada);
				pstm.setString(4, object.salida);
				pstm.setInt(5, object.toleranciaminutos);
				pstm.setInt(6, object.toleranciasalidaminutos);
				if(object.idprueba == null) {
					pstm.setNull(7, 0);
				} else {
					pstm.setLong(7, object.idprueba);
				}
				
			}
			
			pstm.executeUpdate();
			
			con.commit();
			
			resultado.setSuccess(true);
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
	
	public Result getNuevaConfigUsuario(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			List<SesionCampusCustom> rows = new ArrayList<SesionCampusCustom>();
			SesionCampusCustom sesion = new SesionCampusCustom();
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_NUEVA_CONF_USUARIO);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				sesion = new SesionCampusCustom();
				sesion.setIdprueba(rs.getLong("idprueba"));
				sesion.setEntrada(rs.getString("horainiciosesion"));
				sesion.setSalida(rs.getString("horafinsesion"));
				sesion.setAplicacion(rs.getString("fechainiciosesion"));
				sesion.setToleranciaminutos(rs.getInt("toleranciaentradasesion"));
				sesion.setToleranciasalidaminutos(rs.getInt("toleranciasalidasesion"));
				sesion.setIdprueba(rs.getLong("idprueba"));
				
				rows.add(sesion);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}
	
	public Result insertRespuestaFinal(String jsonData) {
		def jsonSlurper = new JsonSlurper();
		def object = jsonSlurper.parseText(jsonData);
		Result resultado = new Result()
		Boolean closeCon = false
		String where ="",errorLog = "";
		String idbanner = object.idbanner//"00845125"
		String respuesta = object.respuestas//"FCCCFCFCCCF*CCFFFFCCFFFFFFFFCFFFF*FFFFFFFFCFCCCFCFCFFFCFCCFFCCCCFFCFFFFFFFCCFFCFFFCFFCFCFCCFCFCFFCFFFCFFFCFCCFFCCFCFCFFC*CFCCCFCFFCCFFFFFFCCCCFFFFFCFFCCFFCFCFFCCFCCCFFFFCFFFCFCCFCFCFFFFCCCCFFCFCFFFFCFFFCCFCCCFCCCFFFFFFFFFCCCFCFFCFCFFFFFCFCFFFCCFFFFCFFFFFCFFFFCCCCFFCCFCFCCFFFCFCFCFFFFCFFFFCCFCFCFCFFFFFFFFFFFCFFFCCFFFCFFCFFFFFFFFCCFFFCFFFFCFFCFCCFFFCCCFCFFFFCCFFCFCFFFFCFCFFFFFFFFFFCFCFFCFFCFFFFFFCFFCFCCCCFFFCFFFCFCFFFFFCFFFCCFCFFFFFFFFCCCFCCFFFFFFFFCCFCFFCCCCFFFFFCFFFFCCCFFCFCFFFFFFFFFFCFCCCFCFFFFCFFCFFFFCFFFFFFFFFCFCFCFFFFFFFCCCCFFCFFFCFFFFFCFFFCFFFFFFCCCCCFCFFF"
		String sesiones_pid = object.id_sesion//"1"
		List < Map < String, Object >> respuestas = new ArrayList < Map < String, Object >> ();
		List < Map < String, String >> additionalData = new ArrayList < Map < String, String >> ();
		List < Map < String, Integer >> rows = new ArrayList < Map < String, Integer >> ();
		Map<String,Integer> respuestainvp= new HashMap  < String, Integer >()
		Map<String,String> aData= new HashMap  < String, String >();
		String caseId = "";
		aData.put("idbanner", idbanner);
		aData.put("respuestas",respuesta);
		aData.put("id_sesion",sesiones_pid);
		try {
//			Result resultado2 = new Result();
//			resultado2 = insertBitacoraComentarios(jsonData);
//			errorLog = resultado2.getError();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement("SELECT persistenceid,  escala,  genero,  persistenceversion, pregunta, puntuacion, respuesta  FROM catrespuestasinvp where genero='ambos' OR genero=lower((SELECT cs.descripcion from solicituddeadmision sda inner join catsexo cs on cs.persistenceid=sda.catsexo_pid inner join detallesolicitud ds on ds.caseid::bigint=sda.caseid and sda.correoelectronico NOT LIKE '%(rechazado)%' where ds.idbanner=? )) order by pregunta asc");
			pstm.setString(1, idbanner);
			rs = pstm.executeQuery();
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
			
			respuestainvp.put("?", count);
			
			for(int i=0; i<respuestas.size(); i++) {
				if (respuesta.charAt((Integer.parseInt(respuestas.get(i).get("pregunta"))-1)) != "*") {
					if((respuesta.charAt(Integer.parseInt(respuestas.get(i).get("pregunta"))-1) == 'C') == (respuestas.get(i).get("respuesta") == 't')) {
						if(respuestainvp.get(respuestas.get(i).get("escala")) == null) {
							respuestainvp.put(respuestas.get(i).get("escala"), Integer.parseInt(respuestas.get(i).get("puntuacion")))
						}else {
							respuestainvp.put(respuestas.get(i).get("escala"), respuestainvp.get(respuestas.get(i).get("escala"))+Integer.parseInt(respuestas.get(0).get("puntuacion")))
						}
						
					} else {
						if(respuestainvp.get(respuestas.get(i).get("escala")) == null) {
							respuestainvp.put(respuestas.get(i).get("escala"), 0)
						}
					}
				} else {
					if(respuestainvp.get(respuestas.get(i).get("escala")) == null) {
						respuestainvp.put(respuestas.get(i).get("escala"), 0)
					}
				}
			}
			
			try {
				pstm = con.prepareStatement("SELECT ds.caseid FROM detallesolicitud as ds INNER JOIN solicitudDeAdmision as sda ON sda.caseid = ds.caseid::integer WHERE sda.correoelectronico NOT LIKE '%(rechazado)%' and  ds.idbanner = '${idbanner}' limit 1");
				rs= pstm.executeQuery();
				if(rs.next()) {
					caseId = rs.getString("caseid");
				}
				 
			} catch(Exception cd) {
				errorLog+=", fallo en traer el caseid";
				caseId = "0";
			}
			
			try {
				TimeZone tz = TimeZone.getTimeZone("UTC")
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"); // Quoted "Z" to indicate UTC, no timezone offset
				df.setTimeZone(tz)
				String nowAsISO = df.format(new Date());
				pstm = con.prepareStatement("SELECT idbanner, escala, puntuacion FROM resultadoinvp where idbanner = ? and sesiones_pid = ?");
				pstm.setString(1, idbanner);
				pstm.setLong(2, Long.parseLong(sesiones_pid));
				rs = pstm.executeQuery();
				
				if(!rs.next()) {
					for (Map.Entry<String,Integer> entry: respuestainvp) {
						pstm = con.prepareStatement("INSERT INTO resultadoinvp (idbanner,escala,puntuacion,sesiones_pid,persistenceid,persistenceversion,fecha_registro,caseId) values (?,?,?,?,case when (SELECT max(persistenceId)+1 from resultadoinvp ) is null then 1 else (SELECT max(persistenceId)+1 from resultadoinvp) end,0,?,?)")
						pstm.setString(1, idbanner)
						pstm.setString(2, entry.getKey())
						pstm.setInt(3, entry.getValue())
						pstm.setLong(4, Long.parseLong(sesiones_pid))
						pstm.setString(5, nowAsISO)
						pstm.setLong(6, Long.parseLong(caseId))
						pstm.execute()
					}
					
				} else {
					resultado.setError("Error");
					resultado.setError_info("Ya hay un aspirante con ese id_sesion")
				}
			} catch(Exception test) {
				resultado.setError("Error")
				resultado.setError_info(test.getMessage())
			}
			
			/*
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
			*/
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError(e.getMessage())
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	private Result insertRespuestasPeticion() {
		
		
		return new Result();
	}
}
