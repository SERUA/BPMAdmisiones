package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.sql.Types

import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger

import com.anahuac.posgrados.auxiliar.AUXISolAdmiRequisitoAdicionalDAO
import com.anahuac.posgrados.auxiliar.AUXISolAdmiRequisitoAdicional
import com.anahuac.posgrados.catalog.PSGRCatCampus
import com.anahuac.posgrados.catalog.PSGRCatCampusDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utilities.FileDownload
import org.slf4j.LoggerFactory

import org.bonitasoft.engine.bpm.document.Document

import groovy.json.JsonSlurper

class SolicitudDeAdmisionDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SolicitudDeAdmisionDAO.class);
	
	Connection con
	Statement stm
	ResultSet rs
	PreparedStatement pstm;
	
	public Boolean validarConexion() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection()
			retorno = true
		}
		return retorno
	}
	
	public Result getB64FileByUrlAzure(String urlAzure) {
		Boolean closeCon = false;
		String errorLog = "";
		Result resultado = new Result();
		String SSA = "";

		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			Map < String, Object > columns = new LinkedHashMap < String, Object > ();
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA);
			rs = pstm.executeQuery();
			def num = Math.random();
			
			if (rs.next()) {
				SSA = rs.getString("valor");
				String urlDecodificada = "";
				urlDecodificada = urlAzure.replace("%20", " ");
				String[] elements = urlDecodificada.split("/");
				String url = java.net.URLEncoder.encode(elements[elements.length-1], "UTF-8");
				
				String finalURL = "";
				elements.eachWithIndex{it,index ->
					finalURL += (finalURL.length() == 0?"":"/")+"${(index == elements.length-1 ? url : it)}";
				}
				
				finalURL = finalURL.replace("+", "%20");
				
				if(urlAzure.toLowerCase().contains(".jpeg")) {
					columns.put("extension", ".jpeg");
					columns.put("b64", "data:image/jpeg;base64, " + (new FileDownload().b64Url(finalURL, SSA + "&v=" + num)));
				}else if(urlAzure.toLowerCase().contains(".png")) {
					columns.put("extension", ".png");
					columns.put("b64", "data:image/png;base64, " + (new FileDownload().b64Url(finalURL, SSA + "&v=" + num)));
				}else if(urlAzure.toLowerCase().contains(".jpg")) {
					columns.put("extension", ".jpg");
					columns.put("b64", "data:image/jpg;base64, " + (new FileDownload().b64Url(finalURL, SSA + "&v=" + num)));
				}else if(urlAzure.toLowerCase().contains(".jfif")) {
					columns.put("extension", ".jfif");
					columns.put("b64", "data:image/jfif;base64, " + (new FileDownload().b64Url(finalURL, SSA + "&v=" + num)));
				}else if(urlAzure.toLowerCase().contains(".pdf")) {
					columns.put("extension", ".pdf");
					columns.put("b64", "data:application/pdf;base64, " + (new FileDownload().b64Url(finalURL, SSA + "&v=" + num)));
				}
				
				rows.add(columns);
			}

			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setError(errorLog);
		} catch (Exception e) {
			errorLog += e.toString();
			resultado.setSuccess(false)
			resultado.setError("500 Internal Server Error")
			resultado.setError_info(errorLog);
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result selectSolicitudesAdmision(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "",campus = "", orderby = "ORDER BY ", errorlog = ""
		List < String > lstGrupo = new ArrayList < String > ();

		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map < String, String > objGrupoCampus = new HashMap < String, String > ();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def objCatCampusDAO = context.apiClient.getDAO(PSGRCatCampusDAO.class);
			List < PSGRCatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999);
			userLogged = context.getApiSession().getUserId();
			List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for (UserMembership objUserMembership: lstUserMembership) {
				for (PSGRCatCampus rowGrupo: lstCatCampus) {
					if (objUserMembership.getGroupName().equals(rowGrupo.getGrupo_bonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}

			assert object instanceof Map;
			where += " WHERE (regi.is_eliminado = false OR regi.is_eliminado IS null) "
			
			if (object.estatusSolicitud != null && object.estatusSolicitud.toString().trim() != "") {
				where += " AND regi.estatus_solicitud IN ("+object.estatusSolicitud+") "
			}
			
			if (object.caseId != null) {
				where += " AND regi.caseId = " + object.caseId + " ";
			}
			
			Boolean filtroCampus = false;
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				switch (filtro.get("columna")) {
					case "CAMPUS": 
					filtroCampus = true
					break;
				}
			}
			
			if (object.caseId == null && filtroCampus != true) {
				errorlog += "filtroCampus = " + filtroCampus;
				if (lstGrupo.size() > 0) {
					where += " AND ("
				}
				
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += "camp.descripcion='" + campusMiembro + "'";
					if (i == (lstGrupo.size() - 1)) {
						where += ") "
					} else {
						where += " OR "
					}
				}
			} else {
				where += " AND LOWER(camp.DESCRIPCION) = LOWER('" + object.lstFiltro[0].valor + "') ";
			}
			if (object.tiene_requisitos_adicionales) {
			    where += " AND regi.tiene_requisitos_adicionales = 'true' ";
			} else {
				where += " AND regi.tiene_requisitos_adicionales IS NULL ";
			}

			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs = pstm.executeQuery();
			if (rs.next()) {
				SSA = rs.getString("valor")
			}
			String consulta = Statements.GET_LISTADO_SOLICITUDES;
			String consultaCount = Statements.GET_CONTEO_SOLICITUDES;
			if (object.caseId == null) {
				for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
					
					switch (filtro.get("columna")) {
	
						case "# EXPEDIENTE":
							errorlog += "# EXPEDIENTE | "
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " LOWER(regi.caseid::VARCHAR) like lower('%[valor]%')";
							where = where.replace("[valor]", filtro.get("valor"));
							break;
						case "NOMBRE,EMAIL,CURP":
							errorlog += "NOMBRE,EMAIL,CURP"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(concat(pers.apellido_paterno,' ',pers.apellido_materno,' ',pers.nombre)) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(regi.correo_electronico) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(pers.curp) like lower('%[valor]%') ) ";
							where = where.replace("[valor]", filtro.get("valor"));
	
							break;
						case "PROGRAMA,INGRESO,CAMPUS":
							errorlog += "PROGRAMA,INGRESO,CAMPUS"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(posg.descripcion) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(peri.descripcion) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(camp.descripcion) like lower('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
	
							break;
						case "PROCEDENCIA,PROMEDIO":
							errorlog += "PROCEDENCIA,PROMEDIO"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(posinfo.institucion) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(posinfo.promedio) like lower('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
							errorlog += "saliendo de case"
							break;
						case "ESTATUS":
							errorlog += "ESTATUS"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(estatus.descripcion) like lower('%[valor]%') ) ";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "FECHAREGISTRO":
							errorlog += "FECHAREGISTRO"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							
							where += " (LOWER(to_char(regi.fecha_registro::timestamp, 'DD/MM/YYYY HH24:MI')) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%'))"
							}
	
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "ULTIMA MODIFICACION":
							errorlog += "ULTIMA MODIFICACION"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							
							where += " (LOWER(to_char(regi.fecha_ultima_modificacion::timestamp, 'DD/MM/YYYY HH24:MI')) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%'))"
							}
	
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "MOTIVO":
							errorlog += "# EXPEDIENTE | "
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " LOWER(regi.mensaje_admin_escolar) like lower('%[valor]%')";
							where = where.replace("[valor]", filtro.get("valor"));
							break;
						case "CARRERA":
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " LOWER(gest.nombre) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"));
							break;
						case "PERIODO":
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " LOWER(peri.descripcion) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "ID BANNER":
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " LOWER(regi.caseid::VARCHAR) like lower('%[valor]%')";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "NOMBRE DEL ALUMNO":
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " LOWER(concat(pers.apellido_paterno,' ',pers.apellido_materno,' ',pers.nombre)) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "CORREO":
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " LOWER(regi.correo_electronico) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						default:
							break;
					}
				}
			}
//			where += " AND SF.persistenceversion >= 0 ";
			
			if (object.caseId != null) {
				orderby = "";
			} else {
				switch (object.orderby) {
					case "ULTIMA MODIFICACION":
						orderby += "sda.fechaultimamodificacion";
						break;
					case "NOMBRE":
						orderby += "pers.apellido_paterno";
						break;
					default:
						orderby += " regi.persistenceid "
						break;
				}
			}
			
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);
			consultaCount = consultaCount.replace("[WHERE]", where);
			
			pstm = con.prepareStatement(consultaCount);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("total_registros"));
			}
			
			consulta = consulta.replace("[ORDER_BY]", orderby);
			consulta = consulta.replace("[LIMIT_OFFSET]", " LIMIT ? OFFSET ?");
			errorlog += "CONSULTA = " + consulta;
			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			errorlog += "[consulta] " + consulta + " :: ";
			
			rs = pstm.executeQuery();
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			errorlog = consulta + " 8";
			
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					if (metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
						String encoded = "";
						boolean noAzure = false;
//						String urlFoto = rs.getString("urlfoto");
//						throw new Exception("El campo \"Clave\" no debe ir vacío" + urlFoto);
						try {
							String urlFoto = rs.getString("urlfoto");
							if (urlFoto != null && !urlFoto.isEmpty()) {
								columns.put("fotografiab64", rs.getString("urlfoto") + SSA);
							} else {
								noAzure = true;
								List < Document > doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
								for (Document doc: doc1) {
									encoded = "../API/formsDocumentImage?document=" + doc.getId();
									columns.put("fotografiab64", encoded);
								}
							}

							for (Document doc: context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
								encoded = "../API/formsDocumentImage?document=" + doc.getId();
								columns.put("fotografiabpm", encoded);
							}

							/*for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
								encoded = "../API/formsDocumentImage?document="+doc.getId();
								columns.put("fotografiab64", encoded);
							} */
						} catch (Exception e) {
							LOGGER.error "[ERROR] " + e.getMessage();
							columns.put("fotografiabpm", "");
							if(noAzure){
								columns.put("fotografiab64", "");
							}
							errorlog += "" + e.getMessage();
						}
					}
				}

				rows.add(columns);
			}
			
			errorlog = consulta + " 9";
			resultado.setSuccess(true);
			resultado.setError_info(errorlog);
			resultado.setData(rows);
		} catch (Exception e) {
			resultado.setError_info(errorlog)
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updateCorreccionesSolicitud(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			closeCon = validarConexion();
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			// ---Validación---
			
			if (object.datosPersonales.equals("") || object.datosPersonales == null) {
				throw new Exception('El campo "datosPersonales" no debe ir vacío');
			} else if(object.datosPersonales.persistenceId_string.equals("") || object.datosPersonales.persistenceId_string == null) {
				throw new Exception('El campo "Persistence Id", en datosPersonales, no debe ir vacío');
			} else if(object.datosPersonales.nombre.equals("") || object.datosPersonales.nombre == null) {
				throw new Exception('El campo "Nombre" no debe ir vacío');
			} else if(object.datosPersonales.apellido_paterno.equals("") || object.datosPersonales.apellido_paterno == null) {
				throw new Exception('El campo "Apellido paterno" no debe ir vacío');
			} else if(object.datosPersonales.apellido_materno.equals("") || object.datosPersonales.apellido_materno == null) {
				throw new Exception('El campo "Apellido materno" no debe ir vacío');
			} else if(object.datosPersonales.curp.equals("") || object.datosPersonales.curp == null) {
				throw new Exception('El campo "CURP" no debe ir vacío');
				
			} else if (object.datosContacto.equals("") || object.datosContacto == null) {
				throw new Exception('El campo "datosContacto" no debe ir vacío');
			} else if(object.datosContacto.persistenceId_string.equals("") || object.datosContacto.persistenceId_string == null) {
				throw new Exception('El campo "Persistence Id", en datosContacto, no debe ir vacío');
			} else if(object.datosContacto.correo_contacto.equals("") || object.datosContacto.correo_contacto == null) {
				throw new Exception('El campo "Correo de contacto" no debe ir vacío');
			
			} else if (object.datosPrograma.equals("") || object.datosPrograma == null) {
				throw new Exception('El campo "datosContacto" no debe ir vacío');
			} else if(object.datosPrograma.persistenceId_string.equals("") || object.datosPrograma.persistenceId_string == null) {
				throw new Exception('El campo "Persistence Id", en datosPrograma, no debe ir vacío');
			} else if(object.datosPrograma.campus == null || object.datosPrograma.campus.persistenceId_string.equals("") || object.datosPrograma.campus.persistenceId_string == null) {
				throw new Exception('El campo "Campus" no debe ir vacío');
			} else if(object.datosPrograma.posgrado == null || object.datosPrograma.posgrado.persistenceId_string.equals("") || object.datosPrograma.posgrado.persistenceId_string == null) {
				throw new Exception('El campo "Posgrado" no debe ir vacío');
			} else if(object.datosPrograma.programa_interes == null || object.datosPrograma.programa_interes.persistenceId_string.equals("") || object.datosPrograma.programa_interes.persistenceId_string == null) {
				throw new Exception('El campo "Programa de interes" no debe ir vacío');
			} else if(object.datosPrograma.periodo_ingreso == null || object.datosPrograma.periodo_ingreso.persistenceId_string.equals("") || object.datosPrograma.periodo_ingreso.persistenceId_string == null) {
				throw new Exception('El campo "Periodo de ingreso" no debe ir vacío');
			}	
			
			// ---Conversión---
			
			Long datosPersonales_id = -1L;
			Long datosContacto_id = -1L;
			Long datosPrograma_id = -1L;
			Long campus_id = -1L;
			Long posgrado_id = -1L;
			Long pograma_interes_id = -1L;
			Long periodo_ingreso_id = -1L;
			
			try {
				datosPersonales_id = Long.valueOf(object.datosPersonales.persistenceId_string);
				datosContacto_id = Long.valueOf(object.datosContacto.persistenceId_string);
				datosPrograma_id = Long.valueOf(object.datosPrograma.persistenceId_string);
				campus_id = Long.valueOf(object.datosPrograma.campus.persistenceId_string);
				posgrado_id = Long.valueOf(object.datosPrograma.posgrado.persistenceId_string);
				pograma_interes_id = Long.valueOf(object.datosPrograma.programa_interes.persistenceId_string);
				periodo_ingreso_id = Long.valueOf(object.datosPrograma.periodo_ingreso.persistenceId_string);
			}
			catch (Exception e) {
				throw new Exception("Falló en la conversión de tipo, en los atributos del objeto recibido. " + e.message);
			}
			
			// ---Ejecución--- 
			
			con.setAutoCommit(false);
			
			// UPDATE datosPersonales
			pstm = con.prepareStatement(Statements.UPDATE_SOL_ADMI_DATOS_PERSONALES);
			pstm.setString(1, object.datosPersonales.nombre);
			pstm.setString(2, object.datosPersonales.apellido_paterno);
			pstm.setString(3, object.datosPersonales.apellido_materno);
			pstm.setString(4, object.datosPersonales.curp);
			pstm.setLong(5, datosPersonales_id);
			if (pstm.executeUpdate() == 0) {
				throw new Exception("No se pudo modificar el registro de la tabla 'Datos personales'.");
			}
			
			// UPDATE datosContacto
			pstm = con.prepareStatement(Statements.UPDATE_SOL_ADMI_DATOS_CONTACTO);
			pstm.setString(1, object.datosContacto.correo_contacto);
			pstm.setLong(2, datosContacto_id);
			if (pstm.executeUpdate() == 0) {
				throw new Exception("No se pudo modificar el registro de la tabla 'Datos de contacto'.");
			}
			
			// UPDATE datosPrograma
			pstm = con.prepareStatement(Statements.UPDATE_SOL_ADMI_PROGRAMA);
			pstm.setLong(1, campus_id);
			pstm.setLong(2, posgrado_id);
			pstm.setLong(3, pograma_interes_id);
			pstm.setLong(4, periodo_ingreso_id);
			pstm.setLong(5, datosPrograma_id);
			if (pstm.executeUpdate() == 0) {
				throw new Exception("No se pudo modificar el registro de la tabla 'Datos de programa'.");
			}
			
			con.commit();
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCorreccionesSolicitud] " + e.getMessage());
			if (!con.autoCommit) con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}	
	
	public Result getRequisitosAdicionalesAuxiliar(String caseid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Map<String, Object> row = new HashMap<String, Object>();
		List < Map<String, Object> > rows = new ArrayList < Map<String, Object> > ();
		
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			// ---Validación---
			if (caseid == null) {
				throw new Exception('El parametro "caseid" es requerido');
			}
			
			// ---Conversión---
			Long caseidLong = -1L
			
			try {
				caseidLong = Long.valueOf(caseid);
			}
			catch (Exception e) {
				throw new Exception("Falló al tratar de convertir el caseid a Long. " + e.message);
			}
			
			// ---Ejecución---
			pstm = con.prepareStatement(Statements.GET_REQUISITOS_ADICIONALES_AUXILIAR);
			pstm.setLong(1, caseidLong);

			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new HashMap<String, Object>();
				row.put("persistenceid", rs.getLong("persistenceid"));
				row.put("persistenceid_string", rs.getString("persistenceid"));
				row.put("caseid", rs.getLong("caseid"));
				row.put("clave", rs.getString("clave"));
				row.put("nombre", rs.getString("nombre"));
				row.put("descripcion", rs.getString("descripcion"));
				row.put("requiere_documento", rs.getBoolean("requiere_documento"));
				row.put("tipo_de_archivo_aceptado", rs.getString("tipo_de_archivo_aceptado"));
				row.put("cumplido", rs.getBoolean("cumplido"));
				row.put("url_azure", rs.getString("url_azure"));
				
				rows.add(row);
			}
			
			resultado.setData(rows);
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getRequisitosAdicionalesAuxiliar] " + e.getMessage());
			if (!con.autoCommit) con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;	
	}
	
	public Result insertRequisitosAdicionalesAuxiliar(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			closeCon = validarConexion();
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def dataResult = [];
			
			// ---Validación---
			
			if (object.caseid == null) {
				throw new Exception('El campo "caseid" no debe ir vacío');
			} else if(object.requisitosAdicionales == null) {
				throw new Exception('El campo "requisitosAdicionales" no debe ir vacío');
			} else if(!(object.requisitosAdicionales instanceof ArrayList) || object.requisitosAdicionales.empty) {
				throw new Exception('El campo "requisitosAdicionales" debe ser una lista y no debe estar vacia');
			}
			
			con.setAutoCommit(false);
			
			// ---Conversión---
			
			Long caseid = -1L;
			Long catRequisitoId = -1L;
			
			try {
				caseid = Long.valueOf(object.caseid);
			}
			catch (Exception e) {
				throw new Exception("Falló al tratar de convertir el caseid a Long. " + e.message);
			}
			
			object.requisitosAdicionales.eachWithIndex { item, index ->
				
				try {
					catRequisitoId = Long.valueOf(item.persistenceId_string);
				}
				catch (Exception e) {
					throw new Exception("Falló al tratar de convertir el persistenceId_string a Long. " + e.message);
				}
				
				// ---Ejecución---
				
				pstm = con.prepareStatement(Statements.INSERT_REQUISITO_ADICIONAL_AUXILIAR);
				pstm.setLong(1, caseid);
				pstm.setBoolean(2, false);
				pstm.setLong(3, catRequisitoId);
				
				rs = pstm.executeQuery();
			
				if (rs.next()) {
					Long idResult = rs.getLong("persistenceid");
					dataResult.add(idResult);
				} else {
					throw new Exception("No se pudo insertar el registro.");
				}
			}

			con.commit();
			resultado.setData(dataResult);
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertRequisitoAdicionalAuxiliar] " + e.getMessage());
			if (!con.autoCommit) con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result updateRequisitosAdicionalesAuxiliar(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			throw new Exception("Sin implementar");
			
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCorreccionesSolicitud] " + e.getMessage());
			if (!con.autoCommit) con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	
	public Result getSolicitudesDuplicadas(String nombre, String apellido_m, String apellido_p, String curp, String pasaporte, String id_banner) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Map<String, Object> row = new HashMap<String, Object>();
		List < Map<String, Object> > rows = new ArrayList < Map<String, Object> > ();
		
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.GET_SOLICITUDES_DUPLICADOS.replace("[NOMBRE]", nombre).replace("[APELLIDO_M]", apellido_m).replace("[APELLIDO_P]", apellido_p).replace("[CURP]", curp).replace("[PASAPORTE]", pasaporte).replace("[IDBANNER]", id_banner));
			
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new HashMap<String, Object>();
				row.put("nombre", rs.getString("nombre"));
				row.put("apellido_paterno", rs.getString("apellido_paterno"));
				row.put("apellido_materno", rs.getString("apellido_materno"));
				row.put("curp", rs.getString("curp"));
				row.put("id_banner", rs.getString("id_banner"));
				row.put("fecha_registro", rs.getString("fecha_registro"));
				row.put("pasaporte", rs.getString("pasaporte"));
				
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
