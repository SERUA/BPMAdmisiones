package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.sql.Timestamp
import java.text.SimpleDateFormat

import javax.naming.directory.SearchResult

import java.sql.Types

import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger

import com.anahuac.posgrados.auxiliar.AUXISolAdmiRequisitoAdicionalDAO
import com.anahuac.posgrados.auxiliar.AUXISolAdmiRequisitoAdicional
import com.anahuac.posgrados.catalog.PSGRCatCampus
import com.anahuac.posgrados.catalog.PSGRCatCampusDAO
import com.anahuac.posgrados.model.PSGRCitaResponsableDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.LogsTransferencias
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utilities.FileDownload
import org.slf4j.LoggerFactory
import org.bonitasoft.engine.bpm.contract.ContractDefinition
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor

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
	
	public Result getB64FileByUrlAzure(String urlAzure, String fileType) {
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
				}else if(urlAzure.toLowerCase().contains(".xml")) {
					columns.put("extension", ".xml");
					
					if (fileType == "text/xml") {
						// Cuando es text/xml
						columns.put("b64", "data:text/xml;base64, " + (new FileDownload().b64Url(finalURL, SSA + "&v=" + num)));
						columns.put("fileType", "text/xml");
					}
					else if (fileType == "data:application/xml") {
						// Cuando es application/xml
						columns.put("b64", "data:application/xml;base64, " + (new FileDownload().b64Url(finalURL, SSA + "&v=" + num)));
						columns.put("fileType", "application/xml");
					} 
					else {
						// Por defecto
						columns.put("b64", "data:text/xml;base64, " + (new FileDownload().b64Url(finalURL, SSA + "&v=" + num)));
						columns.put("fileType", "text/xml");
					}
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
			
			if (object.en_requisitos_adicionales_auxiliar != null) {
				
				// Buscar las solicitudes en requisitos adicionales usando la tabla auxiliar.
				if (object.en_requisitos_adicionales_auxiliar === true) {
					where += " AND regi.caseid IN (" + Statements.SELECT_DISTINCT_CASEID_REQUISITO_ADICIONAL_AUXILIAR + ") ";
				}
				else if (object.en_requisitos_adicionales_auxiliar === false) {
					where += " AND regi.caseid NOT IN (" + Statements.SELECT_DISTINCT_CASEID_REQUISITO_ADICIONAL_AUXILIAR + ") ";
				}
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
							// Se compara con el texto mostrado en el front el cual tiene el siguiente formato -> POSGRADO (PROGRAMA)
							where += " ( LOWER(CONCAT(posg.descripcion, ' (' , gest.nombre, ')')) like lower('%[valor]%') ";
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
						/*case "PERIODO":
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " LOWER(peri.descripcion) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
							break;*/
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
						//
						case "IDBANNER/IDBPM":
							errorlog += "IDBANNER/IDBPM | "
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(regi.id_banner_validacion) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(regi.caseid::VARCHAR) like lower('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
							
							break;
						// 
						case "POSGRADO":
							errorlog += "POSGRADO"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(posg.descripcion) = LOWER('[valor]')) ";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "PROGRAMA":
							errorlog += "PROGRAMA"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(gest.nombre) = LOWER('[valor]')) ";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "PERIODO":
							errorlog += "PERIODO"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(peri.descripcion) = LOWER('[valor]')) ";
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
			}/* else if(object.datosPersonales.apellido_materno.equals("") || object.datosPersonales.apellido_materno == null) {
				throw new Exception('El campo "Apellido materno" no debe ir vacío');
			}*/ else if(object.datosPersonales.curp.equals("") || object.datosPersonales.curp == null) {
				throw new Exception('El campo "CURP" no debe ir vacío');
				
			} else if (object.datosContacto.equals("") || object.datosContacto == null) {
				throw new Exception('El campo "datosContacto" no debe ir vacío');
			} else if(object.datosContacto.persistenceId_string.equals("") || object.datosContacto.persistenceId_string == null) {
				throw new Exception('El campo "Persistence Id", en datosContacto, no debe ir vacío');
			} else if(object.datosContacto.correo_contacto.equals("") || object.datosContacto.correo_contacto == null) {
				throw new Exception('El campo "Correo de contacto" no debe ir vacío');
			
			} else if (object.datosPrograma.equals("") || object.datosPrograma == null) {
				throw new Exception('El campo "datosPrograma" no debe ir vacío');
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
				row.put("persistenceId", rs.getLong("persistenceid"));
				row.put("persistenceId_string", rs.getString("persistenceid"));
				row.put("caseid", rs.getLong("caseid"));
				row.put("clave", rs.getString("clave"));
				row.put("nombre", rs.getString("nombre"));
				row.put("descripcion", rs.getString("descripcion"));
				row.put("requiere_documento", rs.getBoolean("requiere_documento"));
				row.put("tipo_de_archivo_aceptado", rs.getString("tipo_de_archivo_aceptado"));
				row.put("cumplido", rs.getBoolean("cumplido"));
				row.put("url_azure", rs.getString("url_azure"));
				row.put("requisito_adicional_pid", rs.getString("requisito_adicional_pid"));
				
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
	
	// Actualiza la lista de requisitos adicionales en base a un caseid y
	// una lista de requisitos adicionales (catalogos) en la tabla auxiliar.
	// Agrega y elimina los elementos necesarios.
	public Result updateListaRequisitosAdicionalesAuxiliar(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			closeCon = validarConexion();
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def dataResultAgregados = [];
			Map<String, Object> row = new HashMap<String, Object>();
			
			// ---Validación---
			
			if (object.caseid == null) {
				throw new Exception('El campo "caseid" no debe ir vacío');
			} else if(object.requisitosAdicionalesCatalogos == null) {
				throw new Exception('El campo "requisitosAdicionalesCatalogos" no debe ir vacío');
			} else if(!(object.requisitosAdicionalesCatalogos instanceof ArrayList)) {
				throw new Exception('El campo "requisitosAdicionalesCatalogos" debe ser una lista');
			} else if (object.requisitosAdicionalesCatalogos.empty && !object.confirmarLimpiarLista) {
				throw new Exception('Es necesario confirmar mediante el campo "confirmarLimpiarLista" para limpiar la lista por completo');
			}
			
			con.setAutoCommit(false);
			
			// ---Conversión---
			
			Long caseid = -1L;
			
			try {
				caseid = Long.valueOf(object.caseid);
			}
			catch (Exception e) {
				throw new Exception("Falló al tratar de convertir el caseid a Long. " + e.message);
			}
			
			// get estado actual
			pstm = con.prepareStatement(Statements.GET_REQUISITOS_ADICIONALES_AUXILIAR);
			pstm.setLong(1, caseid);

			rs = pstm.executeQuery();
			
			List < Map<String, Object> > currentList = new ArrayList < Map<String, Object> > ();
			List < Map<String, Object> > newList = object.requisitosAdicionalesCatalogos;

			while (rs.next()) {
				row = new HashMap<String, Object>();
				row.put("persistenceId", rs.getLong("persistenceid"));
				row.put("requisito_adicional_pid", rs.getString("requisito_adicional_pid"));
				row.put("caseid", rs.getString("caseid"));
				
				currentList.add(row);
			}
			
			// eliminar 
			currentList.each { currentItem ->
				Long currentCatRequisitoId = Long.valueOf(currentItem.get("requisito_adicional_pid"));
				Long currentPersistenceId = Long.valueOf(currentItem.get("persistenceId"));
				Long currentCaseId = Long.valueOf(currentItem.get("caseid"));
				boolean existe = false;
				
				newList.each { newItem ->
					Long newCatRequisitoId = Long.valueOf(newItem.persistenceId_string);
					if (currentCatRequisitoId == newCatRequisitoId) {
						existe = true;
					}
				}
				
				if (!existe && currentCaseId == caseid) {
					// Actualmente no elimina, solo pone en null el caseid.
					pstm = con.prepareStatement(Statements.DELETE_REQUISITO_ADICIONAL_AUXILIAR);
					pstm.setLong(1, currentPersistenceId);
					
					if (pstm.executeUpdate() == 0) {
						throw new Exception("No se pudo modificar el registro de la tabla 'Requisitos adicionales auxiliar'.");
					}					
				}
			}
			
			// agregar
			newList.each { newItem ->
				Long newCatRequisitoId = Long.valueOf(newItem.persistenceId_string);
				boolean existe = false;
				
				currentList.each { currentItem ->
					Long currentCatRequisitoId = Long.valueOf(currentItem.get("requisito_adicional_pid"));
					if (newCatRequisitoId == currentCatRequisitoId) {
						existe = true;
					}
				}
				
				if (!existe) {
					pstm = con.prepareStatement(Statements.INSERT_REQUISITO_ADICIONAL_AUXILIAR);
					pstm.setLong(1, caseid);
					pstm.setBoolean(2, false);
					pstm.setLong(3, newCatRequisitoId);
					
					rs = pstm.executeQuery();
				
					if (rs.next()) {
						Long idResult = rs.getLong("persistenceid");
						dataResultAgregados.add(idResult);
					} else {
						throw new Exception("No se pudo insertar el registro.");
					}
				}
				
			}

			con.commit();
			resultado.setData(dataResultAgregados);
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateRequisitosAdicionalesAuxiliar] " + e.getMessage());
			if (!con.autoCommit) con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	// Actualiza el campo cumplido y url_azure de los requisitos especificados con su persistenceId.
	public Result updateRequisitosAdicionalesAuxiliar(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			closeCon = validarConexion();
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			int modificacionesResult = 0;
			
			// ---Validación---
			
			if (object.requisitosAdicionales == null) {
				throw new Exception('El campo "requisitosAdicionales" no debe ir vacío');
			} else if(!(object.requisitosAdicionales instanceof ArrayList)) {
				throw new Exception('El campo "requisitosAdicionales" debe ser una lista');
			}
			
			con.setAutoCommit(false);
			
			object.requisitosAdicionales.each { item ->
				
				if (item.persistenceId_string == null) {
					throw new Exception('El campo "persistenceId_string", de los elementos del "requisitosAdicionales", no debe ir vacío');
				} else if(item.cumplido == null) {
					throw new Exception('El campo "cumplido", de los elementos del "requisitosAdicionales", no debe ir vacío');
				}
				
				// ---Conversión---
				
				Long persistenceId = -1L;
				Boolean cumplido = false;
				
				try {
					persistenceId = Long.valueOf(item.persistenceId_string);
					cumplido = Boolean.valueOf(item.cumplido);
				}
				catch (Exception e) {
					throw new Exception("Falló en la conversión de tipo. " + e.message);
				}
				
				// ---Ejecución---
				
				pstm = con.prepareStatement(Statements.UPDATE_REQUISITO_ADICIONAL_AUXILIAR);
				pstm.setBoolean(1, cumplido);
				pstm.setString(2, item.url_azure);
				pstm.setLong(3, persistenceId);
				
				int result = pstm.executeUpdate();
				if (result == 0) {
					throw new Exception("No se pudo modificar el registro de la tabla 'Requisitos adicionales auxiliar'. Falló el elemento con id: " + item.persistenceId_string);
				}
				modificacionesResult += result;
			}
			
			con.commit();
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateRequisitosAdicionalesAuxiliar] " + e.getMessage());
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
				row.put("caseid", rs.getString("caseid"));
				row.put("campus_clave", rs.getString("campus_clave"));
				row.put("posgrado_clave", rs.getString("posgrado_clave"));
				row.put("programa_interes_clave", rs.getString("programa_interes_clave"));
				row.put("periodo_ingreso", rs.getString("periodo_ingreso"));
				row.put("nombre", rs.getString("nombre"));
				row.put("apellido_paterno", rs.getString("apellido_paterno"));
				row.put("apellido_materno", rs.getString("apellido_materno"));
				row.put("curp", rs.getString("curp"));
				row.put("id_banner_validacion", rs.getString("id_banner_validacion"));
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
	
	public Result selectBandejaPaseDeLista(String jsonData, RestAPIContext context) {
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
			
			if (object.en_requisitos_adicionales_auxiliar != null) {
				
				// Buscar las solicitudes en requisitos adicionales usando la tabla auxiliar.
				if (object.en_requisitos_adicionales_auxiliar === true) {
					where += " AND regi.caseid IN (" + Statements.SELECT_DISTINCT_CASEID_REQUISITO_ADICIONAL_AUXILIAR + ") ";
				}
				else if (object.en_requisitos_adicionales_auxiliar === false) {
					where += " AND regi.caseid NOT IN (" + Statements.SELECT_DISTINCT_CASEID_REQUISITO_ADICIONAL_AUXILIAR + ") ";
				}
			}

			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs = pstm.executeQuery();
			if (rs.next()) {
				SSA = rs.getString("valor")
			}
			// GET LISTADO DE PASE LISTA
			String consulta = Statements.GET_LISTADO_SOLICITUDES_PASE_LISTA;
			String consultaCount = Statements.GET_CONTEO_SOLICITUDES_PASE_LISTA;
			
			def filtroResponsable = null;
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
							// Se compara con el texto mostrado en el front el cual tiene el siguiente formato -> POSGRADO (PROGRAMA)
							where += " ( LOWER(CONCAT(posg.descripcion, ' (' , gest.nombre, ')')) like lower('%[valor]%') ";
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
						/*case "PERIODO":
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " LOWER(peri.descripcion) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
							break;*/
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
						//
						case "IDBANNER/IDBPM":
							errorlog += "IDBANNER/IDBPM | "
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(regi.id_banner_validacion) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(regi.caseid::VARCHAR) like lower('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
							
							break;
						//
						case "POSGRADO":
							errorlog += "POSGRADO"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(posg.descripcion) = LOWER('[valor]')) ";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "PROGRAMA":
							errorlog += "PROGRAMA"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(gest.nombre) = LOWER('[valor]')) ";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "PERIODO":
							errorlog += "PERIODO"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(peri.descripcion) = LOWER('[valor]')) ";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "RESPONSABLE":
							// El filtro de responsable no se hace a nivel SQL
							errorlog += "RESPONSABLE"
							filtroResponsable = filtro.get("valor");
							break;
						case "FECHAENTREVISTA/HORARIO":
							errorlog += "FECHAENTREVISTA/HORARIO"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							
							where += "( LOWER(to_char(entr.fecha_entrevista::timestamp, 'DD/MM/YYYY')) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%')"
							}

							where += " OR LOWER(concat(hora.hora_inicio, ' - ', hora.hora_fin)) like lower('%[valor]%') )";
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
			
			// 
			if (where.contains("WHERE")) {
				where += " AND "
			} else {
				where += " WHERE "
			}
			where += " ( cita.eliminado_proceso IS NOT TRUE ) "
			
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
					// Trayendo el nombre del usuarios responsable de entrevista
					if (metaData.getColumnLabel(i).toLowerCase().equals("responsable_id")) {
						def nombre = null;
						if (rs.getString(i) != null) {
							try {
								def id = Long.valueOf(rs.getString(i));
								def responsableUser = context.apiClient.identityAPI.getUser(id);
								nombre = responsableUser.firstName + " " + responsableUser.lastName;
							}
							catch(e) {
								errorlog += "" + e.getMessage();
							}
						}
						columns.put("responsable_nombre", nombre);
					}
				}
				
				// Filtro RESPONSABLE
				
				if (filtroResponsable) {
					if (columns.responsable_nombre.toString().contains(filtroResponsable)) {
						rows.add(columns);
					}
				} 
				else {
					rows.add(columns);
				}	
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
	
	public Result transferirAspirante(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Map<String, Object> row = new HashMap<String, Object>();
		List <?> rows = new ArrayList <?> ();
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			closeCon = validarConexion();
			con.setAutoCommit(false);
			Long campus = 0L, carrera = 0L, posgrado = 0L, periodo = 0L;
			
			pstm = con.prepareStatement(Statements.GET_DATOS_REGISTRO);
			pstm.setLong(1, Long.parseLong(object.caseid));
			
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				campus = rs.getLong("campus_pid");
				carrera = rs.getLong("programa_interes_pid");
				posgrado = rs.getLong("posgrado_pid");
				periodo = rs.getLong("periodo_ingreso_pid");
			} else {
				throw new Exception("No se ha encontrado el registro.");
			}
			
			pstm = con.prepareStatement(Statements.UPDATE_TRANSFERENCIA_REGISTRO);
			pstm.setLong(1, Long.parseLong(object.campus_transferencia.persistenceId_string));
			pstm.setLong(2, Long.parseLong(object.caseid));
			
			if(pstm.executeUpdate() < 1) {
				throw new Exception("No se ha podido actualizar el registro, intente de nuevo mas tarde");
			}
			
			pstm = con.prepareStatement(Statements.UPDATE_TRANSFERENCIA_SOLICITUD);
			pstm.setLong(1, Long.parseLong(object.campus_transferencia.persistenceId_string));
			pstm.setLong(2, Long.parseLong(object.posgrado_transferencia.persistenceId_string));
			pstm.setLong(3, Long.parseLong(object.carrera_transferencia.persistenceId_string));
			pstm.setLong(4, Long.parseLong(object.periodo_transferencia.persistenceId_string));
			pstm.setLong(5, Long.parseLong(object.caseid));

			if(pstm.executeUpdate() < 1) {
				throw new Exception("No se ha podido actualizar el registro, intente de nuevo mas tarde");
			}
			
			SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 1);
			searchOptionsBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID, Long.parseLong(object.caseid));
			
			List<HumanTaskInstance> tareas = context.apiClient.processAPI.searchHumanTaskInstances(searchOptionsBuilder.done()).getResult();
			resultado.setAdditional_data(tareas);
			HumanTaskInstance tareaEjecutar;
			Map<String, Serializable> contrato = new HashMap<String, Serializable>();
			
			if(tareas.size() > 0) {
				tareaEjecutar = tareas.get(0);
				rows.add(tareaEjecutar);
				if(tareaEjecutar.name.equals("Revisar solicitud")) {
					Map<String, Serializable> registroInput = new HashMap<String, Serializable>();
					registroInput.put("mensaje_admin_escolar", "Tu solicitud fué transferida, es necesario  que vuelvas a seleccionar la cita para la entrevista");
					registroInput.put("aprobado_admin_escolar", false);
					registroInput.put("is_transferido", false);
					registroInput.put("tipo_alumno", null);
					registroInput.put("residencia", null);
					registroInput.put("tipo_admision", null);
					registroInput.put("curp_validada", false);
					registroInput.put("documentos_validados", false);
					registroInput.put("no_duplicado", false);
					registroInput.put("tiene_requisitos_adicionales", false);
					registroInput.put("id_banner_validacion", null);
					registroInput.put("isSolicitudRechazadaAdminEscolarInput", false);
					registroInput.put("is_transferido", true);
					contrato.put("registroInput", registroInput);
					contrato.put("isSolicitudRechazadaAdminEscolarInput", false);
					contrato.put("isModificarSolicitudInput", false);
					contrato.put("datosRequisitosAdicionalesInput", new ArrayList<?>());
					contrato.put("isReagendarInput", false);
				} else if(tareaEjecutar.name.equals("Pase de lista de entrevista")) {
					Map<String, Serializable> registroInput = new HashMap<String, Serializable>();
					registroInput.put("is_transferido", true);
					registroInput.put("mensaje_area_academic", "");
					contrato.put("registroInput", registroInput);
					contrato.put("isReagendarInput", false);
					contrato.put("asistenciaInput", false);
					contrato.put("isArchivarEnAreaAcademicaInput", false);
				} else if(tareaEjecutar.name.equals("Dictamen de solicitud")) {
					Map<String, Serializable> registroInput = new HashMap<String, Serializable>();
					registroInput.put("is_transferido", true);
					registroInput.put("mensaje_comite_admision", "");
					contrato.put("registroInput", registroInput);
					contrato.put("isSolicitudNoAdmitidaDictamenInput", false);
					contrato.put("isSolicitudAdmitidaDictamenInput", false);
					contrato.put("isSolicitudArchivadaDictamenInput", false);
					contrato.put("isReagendarInput", false);
				} else if(tareaEjecutar.name.equals("Reagendar cita")) {
					Map<String, Serializable> registroInput = new HashMap<String, Serializable>();
					registroInput.put("is_transferido", true);
					contrato.put("registroInput", registroInput);
					Map<String, Serializable> citaAspiranteInput = new HashMap<String, Serializable>();
					citaAspiranteInput.put("cita_horario", null);
					citaAspiranteInput.put("responsable", null);
					contrato.put("citaAspiranteInput", citaAspiranteInput);
					contrato.put("isSolicitudNoAdmitidaDictamenInput", false);
					contrato.put("isSolicitudAdmitidaDictamenInput", false);
					contrato.put("isSolicitudArchivadaDictamenInput", false);
					contrato.put("isArchivarEnReagendarInput", false);
					contrato.put("isReagendarInput", false);
				}
				
				rows.add(contrato);
				resultado.setData(rows);
				context.apiClient.processAPI.assignAndExecuteUserTask(context.apiClient.session.userId, tareaEjecutar.id,  contrato);
				
				//Bitácora de transferencias
				Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
				String fechaHoraFormateada = formato.format(timestampActual);
				
				pstm = con.prepareStatement(Statements.INSERT_BITACORA_TRANSFERENCIA);
				pstm.setLong(1, Long.parseLong(object.caseid));
				pstm.setLong(2, campus);
				pstm.setLong(3, Long.parseLong(object.campus_transferencia.persistenceId_string));
				pstm.setLong(4, posgrado);
				pstm.setLong(5, Long.parseLong(object.posgrado_transferencia.persistenceId_string));
				pstm.setLong(6, carrera);
				pstm.setLong(7, Long.parseLong(object.carrera_transferencia.persistenceId_string));
				pstm.setLong(8, periodo);
				pstm.setLong(9, Long.parseLong(object.periodo_transferencia.persistenceId_string));
				pstm.setString(10, fechaHoraFormateada);
				pstm.setString(11, context.apiSession.userName);
				
				if(pstm.executeUpdate() < 1) {
					throw new Exception("No se ha podido actualizar el registro, intente de nuevo mas tarde");
				}
				
				//Fin bitácora transferencias
			}
			
			con.commit();
			
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
		
		return resultado;
	}
	
	public Result selectBitacoraTransferencias(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", campus = "", orderby = "ORDER BY ", errorlog = "";
		List < String > lstGrupo = new ArrayList < String > ();
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			List<LogsTransferencias> rows = new ArrayList<LogsTransferencias>();
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				SSA = rs.getString("valor");
			}
			
			String consulta = Statements.GET_BITACORA_TRANSFERENCIAS;
			String consultaCount = Statements.GET_COUNT_BITACORA_TRANSFERENCIAS;
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				switch (filtro.get("columna")) {
					case "idbanner, nombre, correo":
						errorlog += "idbanner, nombre, correo";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(pers.id_banner::VARCHAR) like lower('%[valor]%') OR";
						where += " LOWER(pers.apellido_paterno::VARCHAR) like lower('%[valor]%') OR";
						where += " LOWER(pers.apellido_materno::VARCHAR) like lower('%[valor]%') OR"
						where += " LOWER(pers.nombre::VARCHAR) like lower('%[valor]%') ";
						
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "estatus":
						errorlog += "estatus";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(esta.descripcion) like lower('%[valor]%')";
						
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "programa, periodo, campus ingreso":
						errorlog += "programa, periodo, campus ingreso";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						
						where += " LOWER(gdes.descripcion) like lower('%[valor]%') OR";
						where += " LOWER(edes.descripcion) like lower('%[valor]%') OR";
						where += " LOWER(cdes.descripcion) like lower('%[valor]%') ";
						
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "vpd origen":
						errorlog += "vpd origen";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(cori.descripcion) like lower('%[valor]%')";
						
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "vpd destino":
						errorlog += "vpd destino";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(cdes.descripcion) like lower('%[valor]%')";
						
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "autor transferencia, fecha":
						errorlog += "vpd destino";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(logs.usuario) like lower('%[valor]%') OR";
						where += " LOWER(TO_CHAR(logs.fecha_transferencia::TIMESTAMP, 'DD/MM/YYYY dd:mm')) like lower('%[valor]%')";
						
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "CAMPUS":
						errorlog += "CAMPUS";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						
						if(object.esDestino == true) {
							where += " LOWER(cdes.descripcion) like lower('%[valor]%')";
						} else {
							where += " LOWER(cori.descripcion) like lower('%[valor]%')";
						}
						
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					default:
						break;
				} 
			}
			
			switch (object.orderby) {
				case "idbanner":
					orderby += " pers.id_banner ";
					break;
				case "nombre":
					orderby += " pers.nombre ";
					break;
				case "email":
					orderby += " regi.correo_electronico ";
					break;
				case "carrera":
					orderby += " gdes.descripcion ";
					break;
				case "periodo":
					orderby += " edes.descripcion ";
					break;
				case "campus_ingreso":
					orderby += " cdes.descripcion ";
					break;
				case "estatus":
					orderby += " regi.estatus_solicitud ";
					break;
				case "vpd_origen":
					orderby += " cori.descripcion ";
					break;
				case "vpd_destino":
					orderby += " cdes.descripcion ";
					break;
				case "usuario":
					orderby += " logs.usuario ";
					break;
				case "fecha":
					orderby += " logs.fecha_transferencia ";
					break;
				default:
					orderby += " logs.fecha_transferencia "
					break;
			}
			
			orderby += object.orientation;
			
			consulta = consulta.replace("[WHERE]", where);
			consultaCount = consultaCount.replace("[WHERE]", where);
			pstm = con.prepareStatement(consultaCount);
			errorlog += consultaCount;
			errorlog += " | ";
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("total_registros"));
			}
			
			consulta = consulta.replace("[ORDER_BY]", orderby);
			consulta = consulta.replace("[LIMIT_OFFSET]", " LIMIT ? OFFSET ?");
			errorlog += consulta;
			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			
			rs = pstm.executeQuery();
			
			rows = new ArrayList < LogsTransferencias > ();
			
			while (rs.next()) {
				LogsTransferencias logTransferencia = new LogsTransferencias();
				logTransferencia.setPersistenceid(rs.getLong("persistenceid"));
				logTransferencia.setCampus_origen(rs.getString("campus_origen"));
				logTransferencia.setCampus_destino(rs.getString("campus_detino"));
				logTransferencia.setPosgrado_origen(rs.getString("posgrado_origen"));
				logTransferencia.setPosgrado_destino(rs.getString("posgrado_destino"));
				logTransferencia.setCarrera_origen(rs.getString("carrera_origen"));
				logTransferencia.setCarrera_destino(rs.getString("carrera_destino"));
				logTransferencia.setPeriodo_origen(rs.getString("periodo_origen"));
				logTransferencia.setPeriodo_destino(rs.getString("periodo_destino"));
				logTransferencia.setUsuario(rs.getString("usuario"));
				logTransferencia.setFecha_transferencia(rs.getString("fecha_transferencia"))
				logTransferencia.setFoto(rs.getString("urlfoto") + SSA);
				logTransferencia.setIdbanner(rs.getString("id_banner"));
				logTransferencia.setNombre(rs.getString("nombre") + " " + rs.getString("apellido_paterno") + " " + rs.getString("apellido_materno"));
				logTransferencia.setCorreo_electronico(rs.getString("correo_electronico"));
				logTransferencia.setEstatus_solicitud(rs.getString("estatus_solicitud"));
				
				rows.add(logTransferencia);
			}
			
			resultado.setSuccess(true);
			resultado.setError_info(errorlog);
			resultado.setData(rows);
		} catch (Exception e) {
			resultado.setError_info(errorlog)
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			resultado.setError_info(errorlog);
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result updateEstatusSolicitudByCaseid(Long caseid, String estatus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			//Para no actualizar el estatus con el pase de lista cuando está en validación 
			pstm = con.prepareStatement(Statements.GET_ESTATUS_BY_CASEID);
			pstm.setLong(1, caseid);
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				String estatus_actual = rs.getString("estatus_solicitud");
				if(estatus_actual.equals("solicitud_completada") && estatus.equals("solicitud_lista_para_dictamen")) {
					estatus = "solicitud_pase_lista_esperando_validacion";
				}
			} else {
				throw new Exception("No se ha encontrado la solicitud. Intente d enuevo mas tarde.");
			}
			
			pstm = con.prepareStatement(Statements.UPDATE_ESTATUS_BY_CASEID);
			pstm.setString(1, estatus);
			pstm.setLong(2, caseid);
			
			pstm.executeUpdate();
			
			con.commit();
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateEstatusSolicitudByCaseid] " + e.getMessage());
			if (!con.autoCommit) con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getUserByCitaResponsable(String citaResponsable_pid, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Map<String, Object> row = new HashMap<String, Object>();
		List < Map<String, Object> > rows = new ArrayList < Map<String, Object> > ();
		
		try {
			def identityAPI = context.getApiClient().getIdentityAPI();
			def pSGRCitaResponsableDAO = context.getApiClient().getDAO(PSGRCitaResponsableDAO.class);
			
			// validación
			if (!citaResponsable_pid) {
				throw new Exception("El parametro 'citaResponsable_pid' no debe ir vacío")
			}
			
			Long persistenceId = 0L;
			try {
				persistenceId = Long.valueOf(citaResponsable_pid);	
			}
			catch (exeption) {
				throw new Exception("El parametro 'citaResponsable_pid' debe ser un número de tipo Long")
			}
			
			// Consulta
			def citaResponsable = pSGRCitaResponsableDAO.findByPersistenceId(persistenceId);
			if (!citaResponsable) {
				throw new Exception("No se encontró ningun registro de PSGRCitaResponsable con id " + persistenceId);	
			}
			
			def user = identityAPI.getUser(citaResponsable.responsable_id)
			if (!user) {
				throw new Exception("No se encontró ningun usuario con id " + citaResponsable.responsable_id);
			}
			
			row = new HashMap<String, Object>();
			row.put("nombre", user.firstName + " " + user.lastName);
			row.put("id", user.id);
			rows.add(row);
			
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
	
	public Result reagendarAspirante(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Map<String, Object> row = new HashMap<String, Object>();
		List <?> rows = new ArrayList <?> ();
		
		try {
			errorLog += "|1";
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 1);
			searchOptionsBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID, Long.parseLong(object.caseid));
			errorLog += "|2";
			
			List<HumanTaskInstance> tareas = context.apiClient.processAPI.searchHumanTaskInstances(searchOptionsBuilder.done()).getResult();
			resultado.setAdditional_data(tareas);
			
			errorLog += "|3";
			HumanTaskInstance tareaEjecutar;
			Map<String, Serializable> contrato = new HashMap<String, Serializable>();
			
			errorLog += "|4";
			if(tareas.size() > 0) {
				errorLog += "|4.1";
				tareaEjecutar = tareas.get(0);
				rows.add(tareaEjecutar);
				errorLog += "|4.2";
				if(tareaEjecutar.name.equals("Revisar solicitud")) {
					errorLog += "|4.3";
					Map<String, Serializable> registroInput = new HashMap<String, Serializable>();
					registroInput.put("mensaje_admin_escolar", "Solicitud enviada a reagendar");
					registroInput.put("aprobado_admin_escolar", false);
					registroInput.put("is_transferido", false);
					registroInput.put("tipo_alumno", null);
					registroInput.put("residencia", null);
					registroInput.put("tipo_admision", null);
					registroInput.put("curp_validada", false);
					registroInput.put("documentos_validados", false);
					registroInput.put("no_duplicado", false);
					registroInput.put("tiene_requisitos_adicionales", false);
					registroInput.put("id_banner_validacion", null);
					registroInput.put("isSolicitudRechazadaAdminEscolarInput", false);
					contrato.put("registroInput", registroInput);
					contrato.put("isSolicitudRechazadaAdminEscolarInput", false);
					contrato.put("isModificarSolicitudInput", false);
					contrato.put("isReagendarInput", true);
					contrato.put("datosRequisitosAdicionalesInput", new ArrayList<?>());
				} else if(tareaEjecutar.name.equals("Dictamen de solicitud")) {
					errorLog += "|4.4";
					Map<String, Serializable> registroInput = new HashMap<String, Serializable>();
					registroInput.put("is_transferido", false);
					registroInput.put("mensaje_comite_admision", "");
					contrato.put("registroInput", registroInput);
					contrato.put("isSolicitudNoAdmitidaDictamenInput", false);
					contrato.put("isSolicitudAdmitidaDictamenInput", false);
					contrato.put("isSolicitudArchivadaDictamenInput", false);
					contrato.put("isReagendarInput", true);
				}
				errorLog += "|5";
				rows.add(contrato);
				resultado.setData(rows);
				errorLog += "|6";
				context.apiClient.processAPI.assignAndExecuteUserTask(context.apiClient.session.userId, tareaEjecutar.id,  contrato);
				errorLog += "|7";
			}
			
			resultado.setData(rows);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			resultado.setError_info(errorLog);
		}
		
		return resultado;
	}
	
}
