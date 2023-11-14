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
//				where += " AND SDAE.caseId = "+object.caseId +" "
			}
			
			if (object.caseId == null) {
				if (lstGrupo.size() > 0) {
					where += " AND ("
				}
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += "camp.descripcion='" + campusMiembro + "'"
					if (i == (lstGrupo.size() - 1)) {
						where += ") "
					} else {
						where += " OR "
					}
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
			errorlog += "5|";
			if (object.caseId == null) {
				for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
					errorlog = consulta + " 1";
					switch (filtro.get("columna")) {
	
						case "EXPEDIENTE":
							errorlog += "FILTRO EXPEDIENTE | "
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " LOWER(regi.caseid::VARCHAR) like lower('%[valor]%')";
							where = where.replace("[valor]", filtro.get("valor"));
							break;
						case "PROGRAMA,INGRESO,CAMPUS":
							errorlog += "PROGRAMA,INGRESO,CAMPUS"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							break;
						case "TIPO APOYO,PROMEDIO":
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(tipoapoyo.descripcion) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
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
					case "FECHAULTIMAMODIFICACION":
						orderby += "sda.fechaultimamodificacion";
						break;
					case "NOMBRE":
						orderby += "sda.apellidopaterno";
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
}
