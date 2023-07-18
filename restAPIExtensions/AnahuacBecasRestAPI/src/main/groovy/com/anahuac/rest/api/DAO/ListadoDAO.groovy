package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.RowId
import java.sql.Statement
import java.text.DateFormat
import java.text.SimpleDateFormat
import com.anahuac.rest.api.DB.Statements

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.bonitasoft.engine.bpm.data.DataDefinition
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.search.Order
import org.bonitasoft.engine.search.impl.SearchFilter
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatNotificacionesDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.model.ProcesoCasoDAO
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.DB.BannerWSInfo
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result
import com.anahuac.catalogos.CatBachilleratosDAO
import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.catalogos.CatGestionEscolarDAO

import com.anahuac.catalogos.CatMateriasDAO
import com.anahuac.catalogos.CatTipoLecturaDAO
import groovy.json.JsonSlurper


class ListadoDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ListadoDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Result selectSolicitudesApoyo(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
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

			assert object instanceof Map;
			where += " WHERE SDAE.eliminado = false "
			
			if (object.estatusSolicitud != null) {
				where += " AND SDAE.estatusSolicitud IN ("+object.estatusSolicitud+") "
			}
			
			if (object.caseId != null) {
				where += " AND SDAE.caseId = "+object.caseId +" "
			}
			
			if (object.caseId == null) {
				if (lstGrupo.size() > 0) {
					where += " AND ("
				}
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += "campus.descripcion='" + campusMiembro + "'"
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

			String consulta = Statements.GET_SOLICITUDES_APOYO_BY_ESTATUS
			String consultaCount = Statements.GET_COUNT_SOLICITUDES_APOYO_BY_ESTATUS

			errorlog = consulta + " 2";
			
			if (object.caseId == null) {
				for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
					errorlog = consulta + " 1";
					switch (filtro.get("columna")) {
	
						case "NOMBRE,EMAIL,CURP":
							errorlog += "NOMBRE,EMAIL,CURP"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
							where = where.replace("[valor]", filtro.get("valor"))
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
	
							where += " OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
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
							
						case "CAMPUS":
							errorlog += "CAMPUS"
							where += " AND LOWER(campus.DESCRIPCION) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
							
						case "ULTIMA MODIFICACION":
							errorlog += "FECHAULTIMAMODIFICACION"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							
							where += " (LOWER(to_char(SDAE.fechaUltimaModificacion::timestamp, 'DD/MM/YYYY HH24:MI')) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%'))"
							}
	
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "FECHALIMITEPROPUESTA":
							errorlog += "FECHALIMITEPROPUESTA"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " (LOWER(AA.fechaLimitePropuesta) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%'))"
							}
	
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "FECHALIITEPROPUESTAFINA":
							errorlog += "FECHALIITEPROPUESTAFINA"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " (LOWER(SF.fechaLimiteAceptaPropuesta) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%'))"
							}
	
							where = where.replace("[valor]", filtro.get("valor"))
							break;
							
						case "# EXPEDIENTE":
							errorlog += "SOLICITUD"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							//where += " LOWER(CAST(SDAE.caseid AS varchar)) ";
							where += " LOWER(CAST(da.idBanner AS varchar)) ";
							
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
							
						case "ESTATUS":
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " (LOWER(SDAE.estatusSolicitud) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "= LOWER('[valor]') OR LOWER(SF.estatusSolicitud)=LOWER('[valor]') "
							} else {
								where += " LIKE LOWER('%[valor]%') OR LOWER(SF.estatusSolicitud) LIKE LOWER('%[valor]%') "
							}
							where = where.replace("[valor]", filtro.get("valor"))
							
							where += " OR LOWER(CASE SDA.aceptado WHEN 't' THEN 'aceptado' WHEN 'f' THEN 'rechazado' ELSE 'en proceso de admisión' END) LIKE LOWER('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "P-BECA":
							errorlog += "P-BECA"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " (LOWER(AA.porcentajeBecaAutorizacion::varchar(255)) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%'))"
							}
	
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "P-FINAN":
							errorlog += "P-FINAN"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " (LOWER(SF.porcComite::varchar(255)) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%'))"
							}
	
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "F-TRAMITE":
							errorlog += "F-TRAMITE"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " (LOWER( CASE SF.finalizada WHEN 't' THEN 'Concluido' WHEN 'f' THEN 'En proceso' ELSE 'N/A' END ) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%'))"
							}
	
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "LICENCIATURA":
							errorlog += "LICENCIATURA"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "PERIODO":
							errorlog += "PERIODO"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						default:
							break;
					}
				}
			}	
			errorlog = consulta + " 2";
			if (object.caseId != null) {
				orderby = "";
			}else {
				switch (object.orderby) {
					case "FECHAULTIMAMODIFICACION":
						orderby += "sda.fechaultimamodificacion";
						break;
					case "NOMBRE":
						orderby += "sda.apellidopaterno";
						break;
					case "EMAIL":
						orderby += "sda.correoelectronico";
						break;
					case "CURP":
						orderby += "sda.curp";
						break;
					case "CAMPUS":
						orderby += "campus.DESCRIPCION"
						break;
					case "PREPARATORIA":
						orderby += "prepa.DESCRIPCION"
						break;
					case "PROGRAMA":
						orderby += "gestionescolar.NOMBRE"
						break;
					case "INGRESO":
						orderby += "periodo.DESCRIPCION"
						break;
					case "PROMEDIO":
						orderby += "sda.PROMEDIOGENERAL";
						break;
					case "ESTATUS":
						orderby += "SDAE.estatusSolicitud";
						break;
					case "TIPOBECA":
						orderby += "tipoapoyo.descripcion";
						break;
					case "EXPEDIENTE":
						//orderby += "sda.caseid::INTEGER";
						orderby += "da.idBanner::INTEGER";
						break;
					case "FECHAULTIMAMODIFICACION":
						orderby += "SDAE.fechaultimamodificacion";
						break;
					case "FECHALIMITEPROPUESTA":
						orderby += "AA.fechaLimitePropuesta";
						break;
					default:
						orderby += "SDAE.caseid"
						break;
						
				}
			}
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);
			consultaCount = consultaCount.replace("[WHERE]", where);

			pstm = con.prepareStatement(consultaCount)
			//errorlog = consultaCount + " 6";
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorlog = consulta + " 7";

			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)
			
			rs = pstm.executeQuery()
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
			resultado.setSuccess(true)

			resultado.setError_info(errorlog);
			resultado.setData(rows)

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
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
	
	public Result selectSolicitudesApoyoCompletadas(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
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

			assert object instanceof Map;
			where += " WHERE SDAE.eliminado = false ";
			
			if(object.isCompletadas == true) {
				if (object.estatusSolicitud != null) {
					where += " AND (SDAE.estatusSolicitud IN ("+object.estatusSolicitud+") OR (SDAE.estatusSolicitud = 'Solicitud de Financiamiento en Proceso' AND SF.estatusSolicitud = 'Propuesta de financiamiento aceptada por aspirante') ) ";
				}
			} else {
				if (object.estatusSolicitud != null) {
					where += " AND (SDAE.estatusSolicitud IN ("+object.estatusSolicitud+") ) ";
				}
			}
			
			if (object.caseId != null) {
				where += " AND SDAE.caseId = " + object.caseId + " ";
			}
			
			if (object.caseId == null) {
				if (lstGrupo.size() > 0) {
					where += " AND (";
				}
				
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += "campus.descripcion='" + campusMiembro + "'";
					if (i == (lstGrupo.size() - 1)) {
						where += ") ";
					} else {
						where += " OR ";
					}
				}
			}

			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();

			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA);
			rs = pstm.executeQuery();
			if (rs.next()) {
				SSA = rs.getString("valor");
			}

			String consulta = Statements.GET_SOLICITUDES_APOYO_BY_ESTATUS;
			String consultaCount = Statements.GET_COUNT_SOLICITUDES_APOYO_BY_ESTATUS;

			errorlog = consulta + " 2";
			
			if (object.caseId == null) {
				for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
					errorlog = consulta + " 1";
					switch (filtro.get("columna")) {
	
						case "NOMBRE,EMAIL,CURP":
							errorlog += "NOMBRE,EMAIL,CURP"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
							where = where.replace("[valor]", filtro.get("valor"))
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
	
							where += " OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
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
						case "CAMPUS":
							errorlog += "CAMPUS"
							where += " AND LOWER(campus.DESCRIPCION) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "ULTIMA MODIFICACION":
							errorlog += "FECHAULTIMAMODIFICACION"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " (LOWER(SDAE.fechaultimamodificacion) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%'))"
							}
	
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "# EXPEDIENTE":
							errorlog += "SOLICITUD"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							//where += " LOWER(CAST(SDAE.caseid AS varchar)) ";
							where += " LOWER(CAST(da.idBanner AS varchar)) ";
							
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
							
						case "ESTATUS":
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " LOWER(SDAE.estatusSolicitud) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]') OR LOWER(SF.estatusSolicitud)=LOWER('[valor]') "
							} else {
								where += "LIKE LOWER('%[valor]%') OR LOWER(SF.estatusSolicitud) LIKE LOWER('%[valor]%') "
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "P-BECA":
							errorlog += "P-BECA"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " (LOWER(AA.porcentajeBecaAutorizacion::varchar(255)) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%'))"
							}
	
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "T-BECA":
							errorlog += "T-BECA"
							String addWhere = "";
							if (where.contains("WHERE")) {
								addWhere += " AND "
							} else {
								addWhere += " WHERE "
							}
							
							if((Boolean) filtro.get("valor")) {
								addWhere += " (AA.porcentajeBecaAutorizacion IS NOT NULL) ";
								where += addWhere;
							}
							
							break;
						case "P-FINAN":
							errorlog += "P-FINAN"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " (LOWER(SF.porcComite::varchar(255)) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%'))"
							}
	
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "T-FINAN":
							errorlog += "T-FINAN"
							String addWhere = "";
							
							if (where.contains("WHERE")) {
								addWhere += " AND "
							} else {
								addWhere += " WHERE "
							}
							
							if((Boolean) filtro.get("valor")) {
								addWhere += " (SF.porcComite IS NOT NULL) ";
								where += addWhere;
							}
							
							break;
						case "F-TRAMITE":
							errorlog += "F-TRAMITE"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " (LOWER( CASE SF.finalizada WHEN 't' THEN 'Concluido' WHEN 'f' THEN 'En proceso' ELSE 'N/A' END ) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%'))"
							}
	
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "LICENCIATURA":
							errorlog += "LICENCIATURA"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "PERIODO":
							errorlog += "PERIODO"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						default:
							break;
					}
	
				}
			}
			errorlog += " 2";
			if (object.caseId != null) {
				orderby = "";
			}else {
				switch (object.orderby) {
					case "FECHAULTIMAMODIFICACION":
						orderby += "sda.fechaultimamodificacion";
						break;
					case "NOMBRE":
						orderby += "sda.apellidopaterno";
						break;
					case "EMAIL":
						orderby += "sda.correoelectronico";
						break;
					case "CURP":
						orderby += "sda.curp";
						break;
					case "CAMPUS":
						orderby += "campus.DESCRIPCION"
						break;
					case "PREPARATORIA":
						orderby += "prepa.DESCRIPCION"
						break;
					case "PROGRAMA":
						orderby += "gestionescolar.NOMBRE"
						break;
					case "INGRESO":
						orderby += "periodo.DESCRIPCION"
						break;
					case "PROMEDIO":
						orderby += "sda.PROMEDIOGENERAL";
						break;
					case "ESTATUS":
						orderby += "SDAE.estatusSolicitud";
						break;
					case "TIPOBECA":
						orderby += "tipoapoyo.descripcion";
						break;
					case "EXPEDIENTE":
						//orderby += "sda.caseid::INTEGER";
						orderby += "da.idBanner::INTEGER";
						break;
					case "FECHAULTIMAMODIFICACION":
						orderby += "SDAE.fechaultimamodificacion";
						break;
					default:
						orderby += "SDAE.caseid"
						break;
				}
			}
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);
			consultaCount = consultaCount.replace("[WHERE]", where);
			errorlog += " 3" + where;

			pstm = con.prepareStatement(consultaCount)
			rs = pstm.executeQuery()
			if (rs.next()) {
				errorlog += " 4";
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			errorlog += " 5";
			
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorlog += " 6";

			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)
			
			rs = pstm.executeQuery()
			errorlog += " 7";
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			
				while (rs.next()) {
					BannerRequestDAO bannerRequestDAO = new BannerRequestDAO();
					Result getBannerObject = bannerRequestDAO.getBannerInfo(rs.getString("idcampus"), rs.getString("idBanner"));
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
					errorlog += " | Resultado de banner";
					if (getBannerObject.isSuccess()) {
						errorlog += " | Banner encontrado ";
						columns.put("bannerInfo", getBannerObject.getData().get(0));
						BannerWSInfo bannerInfo = (BannerWSInfo) getBannerObject.getData().get(0);
						errorlog += " | bannerInfo ";
						String infoEscolar = bannerInfo.getPrograma_periodo_campus();
						errorlog += " | infoEscolar ";
						String[] infoEscolarArray;
						if (infoEscolar != null && !infoEscolar.equals("") && !infoEscolar.equals("null")) {
							errorlog += " | infoEscolar.split ";
							infoEscolarArray = infoEscolar.split("/");
							if (infoEscolarArray.length > 0) {
								String programa = infoEscolarArray[0]; // Guardar el valor en una nueva variable llamada 'programa'
								columns.put("licenciatura", programa);
								errorlog += " | infoEscolar " + programa;
								columns.put("periodo", infoEscolarArray[1]);
								errorlog += " | infoEscolar " + infoEscolarArray[1];
								columns.put("campus", infoEscolarArray[2]);
								errorlog += " | infoEscolar " + infoEscolarArray[2];
							} else {
								columns.put("licenciatura", "");
								columns.put("periodo", "");
								columns.put("campus", "");
							}
						} else {
							columns.put("licenciatura", "");
							columns.put("periodo", "");
							columns.put("campus", "");
						}
					} else {
						errorlog += " | Banner no encontrado ";
						errorlog += " | " + getBannerObject.error;
						errorlog += " | " + getBannerObject.error_info;
					}

					errorlog += " | Columnas iteradas ";
					for (int i = 1; i <= columnCount; i++) {
						errorlog += " | Columnas index  " + String.valueOf(i);
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						if (metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
							String encoded = "";
							boolean noAzure = false;
							try {
								String urlFoto = rs.getString("urlfoto");
								if (urlFoto != null && !urlFoto.isEmpty()) {
									columns.put("fotografiab64", rs.getString("urlfoto") + SSA);
								} else {
									noAzure = true;
									List<Document> doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10);
									for (Document doc : doc1) {
										encoded = "../API/formsDocumentImage?document=" + doc.getId();
										columns.put("fotografiab64", encoded);
									}
								}

								for (Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
									encoded = "../API/formsDocumentImage?document=" + doc.getId();
									columns.put("fotografiabpm", encoded);
								}
							} catch (Exception e) {
								LOGGER.error("[ERROR] " + e.getMessage());
								columns.put("fotografiabpm", "");
								if (noAzure) {
									columns.put("fotografiab64", "");
								}
								errorlog += "" + e.getMessage();
							}
						}
					}
    
					rows.add(columns);
				}
				
				resultado.setSuccess(true);
				resultado.setError_info(errorlog);
				resultado.setError_info(where);
				resultado.setData(rows);
			} catch (Exception e) {
				errorlog += " 9" + e.getMessage();
				LOGGER.error "[ERROR] " + e.getMessage();
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
	
	public Result selectBandejaMaestra(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
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

			assert object instanceof Map;
			where += " WHERE SDAE.eliminado = false "
			
			//if (object.estatusSolicitud != null) {
				//where += " AND SDAE.estatusSolicitud IN ("+object.estatusSolicitud+") "
			//}
			
			if (object.caseId != null) {
				where += " AND SDAE.caseId = "+object.caseId +" "
			}
			
			if (object.caseId == null) {
				if (lstGrupo.size() > 0) {
					where += " AND ("
				}
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += "campus.descripcion='" + campusMiembro + "'"
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

			String consulta = Statements.GET_SOLICITUDES_APOYO_BY_ESTATUS;
			String consultaCount = Statements.GET_COUNT_SOLICITUDES_APOYO_BY_ESTATUS;

			errorlog = consulta + " 2";
			
			if (object.caseId == null) {
				for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
					errorlog = consulta + " 1";
					switch (filtro.get("columna")) {
	
						case "NOMBRE,EMAIL,CURP":
							errorlog += "NOMBRE,EMAIL,CURP"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
							where = where.replace("[valor]", filtro.get("valor"))
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
	
							where += " OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
	
							where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
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
							
						case "CAMPUS":
							errorlog += "CAMPUS"
							where += " AND LOWER(campus.DESCRIPCION) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
							
						case "ULTIMA MODIFICACION":
							errorlog += "FECHAULTIMAMODIFICACION"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							
							where += " (LOWER(to_char(SDAE.fechaUltimaModificacion::timestamp, 'DD/MM/YYYY HH24:MI')) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%'))"
							}
	
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "# EXPEDIENTE":
							errorlog += "SOLICITUD"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							//where += " LOWER(CAST(SDAE.caseid AS varchar)) ";
							where += " LOWER(CAST(da.idBanner AS varchar)) ";
							
							if (filtro.get("operador").equals("Igual a")) {
								where += "=LOWER('[valor]')"
							} else {
								where += "LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
							
						case "ESTATUS":
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " (LOWER(SDAE.estatusSolicitud) ";
							if (filtro.get("operador").equals("Igual a")) {
								where += "= LOWER('[valor]') OR LOWER(SF.estatusSolicitud)=LOWER('[valor]') "
							} else {
								where += " LIKE LOWER('%[valor]%') OR LOWER(SF.estatusSolicitud) LIKE LOWER('%[valor]%') "
							}
							where = where.replace("[valor]", filtro.get("valor"))
							
							where += " OR LOWER(CASE SDA.aceptado WHEN 't' THEN 'aceptado' WHEN 'f' THEN 'rechazado' ELSE 'en proceso de admisión' END) LIKE LOWER('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "LICENCIATURA":
							errorlog += "LICENCIATURA"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "PERIODO":
							errorlog += "PERIODO"
							if (where.contains("WHERE")) {
								where += " AND "
							} else {
								where += " WHERE "
							}
							where += " ( LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						default:
							break;
					}
	
				}
			}
			errorlog = consulta + " 2";
			if (object.caseId != null) {
				orderby = "";
			}else {
				switch (object.orderby) {
					case "FECHAULTIMAMODIFICACION":
						orderby += "sda.fechaultimamodificacion";
						break;
					case "NOMBRE":
						orderby += "sda.apellidopaterno";
						break;
					case "EMAIL":
						orderby += "sda.correoelectronico";
						break;
					case "CURP":
						orderby += "sda.curp";
						break;
					case "CAMPUS":
						orderby += "campus.DESCRIPCION"
						break;
					case "PREPARATORIA":
						orderby += "prepa.DESCRIPCION"
						break;
					case "PROGRAMA":
						orderby += "gestionescolar.NOMBRE"
						break;
					case "INGRESO":
						orderby += "periodo.DESCRIPCION"
						break;
					case "PROMEDIO":
						orderby += "sda.PROMEDIOGENERAL";
						break;
					case "ESTATUS":
						orderby += "SDAE.estatusSolicitud";
						break;
					case "TIPOBECA":
						orderby += "tipoapoyo.descripcion";
						break;
					case "EXPEDIENTE":
						//orderby += "sda.caseid::INTEGER";
						orderby += "da.idBanner::INTEGER";
						break;
					case "FECHAULTIMAMODIFICACION":
						orderby += "SDAE.fechaultimamodificacion";
						break;
					default:
						orderby += "SDAE.caseid"
						break;
				}
			}
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);
			consultaCount = consultaCount.replace("[WHERE]", where);

			pstm = con.prepareStatement(consultaCount)
			//errorlog = consultaCount + " 6";
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorlog = consulta + " 7";

			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)
			
			rs = pstm.executeQuery()
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
			resultado.setSuccess(true)

			resultado.setError_info(errorlog);
			resultado.setData(rows)

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
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
	
	
	public Result getSolicitudApoyoByCaseId(int caseid, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = ""
		
		try {
		
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
	
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs = pstm.executeQuery();
			if (rs.next()) {
				SSA = rs.getString("valor")
			}
	
			pstm = con.prepareStatement(Statements.GET_SOLICITUD_APOYO_BY_CASE_ID)
			pstm.setInt(1, caseid)
				
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					if (metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
						String encoded = "";
						boolean noAzure = false;
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
			resultado.setSuccess(true)

			resultado.setError_info(errorlog);
			resultado.setData(rows)

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
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
	
	
	public Result selectAspirantesEnproceso(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = ""
		List < String > lstGrupo = new ArrayList < String > ();
		List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
		List < DetalleSolicitud > lstDetalleSolicitud = new ArrayList < DetalleSolicitud > ();

		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map < String, String > objGrupoCampus = new HashMap < String, String > ();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
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

			assert object instanceof Map;
			where += " WHERE sda.iseliminado=false and (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado = false ) "
			if (object.campus != null) {
				where += " AND LOWER(campus.grupoBonita) = LOWER('" + object.campus + "') "
			}
			if (object.estatusSolicitud != null) {
				if (object.estatusSolicitud.equals("Solicitud lista roja")) {
					where += " AND sda.ESTATUSSOLICITUD='Solicitud lista roja'"
				} else if (object.estatusSolicitud.equals("Solicitud rechazada")) {
					where += " AND sda.ESTATUSSOLICITUD='Solicitud rechazada'"
				} else if (object.estatusSolicitud.equals("Nuevas solicitudes")) {
					where += " AND (sda.ESTATUSSOLICITUD='Solicitud modificada' OR sda.ESTATUSSOLICITUD='Solicitud recibida')"
				} else if (object.estatusSolicitud.equals("Solicitud en proceso")) {
					where += " AND (sda.ESTATUSSOLICITUD='Solicitud en proceso' OR sda.ESTATUSSOLICITUD='Solicitud a modificar' OR sda.ESTATUSSOLICITUD='Periodo vencido')"
				} else if (object.estatusSolicitud.equals("Aspirantes registrados sin validación de cuenta")) {
					where += " AND (sda.ESTATUSSOLICITUD='Aspirantes registrados sin validación de cuenta')"
				} else if (object.estatusSolicitud.equals("Aspirantes registrados con validación de cuenta")) {
					where += " AND (sda.ESTATUSSOLICITUD='Aspirantes registrados con validación de cuenta')"
				} else if (object.estatusSolicitud.equals("Solicitud en espera de pago")) {
					where += " AND (sda.ESTATUSSOLICITUD='Solicitud en espera de pago')"
				} else if (object.estatusSolicitud.equals("Solicitud con pago aceptado")) {
					where += " AND (sda.ESTATUSSOLICITUD='Solicitud con pago aceptado')"
				} else if (object.estatusSolicitud.equals("Autodescripción en proceso")) {
					where += " AND (sda.ESTATUSSOLICITUD='Autodescripción en proceso')"
				} else if (object.estatusSolicitud.equals("Elección de pruebas no calendarizado")) {
					where += " AND (sda.ESTATUSSOLICITUD='Elección de pruebas no calendarizado')"
				} else if (object.estatusSolicitud.equals("No se ha impreso credencial")) {
					where += " AND (sda.ESTATUSSOLICITUD='No se ha impreso credencial')"
				} else if (object.estatusSolicitud.equals("Ya se imprimió su credencial")) {
					where += " AND (sda.ESTATUSSOLICITUD='Ya se imprimió su credencial')"
				}
				//CODIGO PP
				else if (object.estatusSolicitud.equals("Aspirantes en proceso")) {
					//where+=" AND (sda.ESTATUSSOLICITUD != 'Solicitud rechazada') AND (sda.ESTATUSSOLICITUD != 'Solicitud lista roja') AND (sda.ESTATUSSOLICITUD != 'Aspirantes registrados sin validación de cuenta') AND (sda.ESTATUSSOLICITUD !='Aspirantes registrados con validación de cuenta') AND (sda.ESTATUSSOLICITUD != 'Solicitud en proceso') AND (sda.ESTATUSSOLICITUD != 'Solicitud recibida' )"
					where += " AND (sda.ESTATUSSOLICITUD != 'Solicitud rechazada') AND (sda.ESTATUSSOLICITUD != 'Solicitud lista roja') AND (sda.ESTATUSSOLICITUD != 'Aspirantes registrados sin validación de cuenta') AND (sda.ESTATUSSOLICITUD !='Aspirantes registrados con validación de cuenta') AND (sda.ESTATUSSOLICITUD != 'Solicitud en proceso') AND (sda.ESTATUSSOLICITUD != 'Solicitud recibida' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud a modificar' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud modificada' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud vencida') AND (sda.ESTATUSSOLICITUD != 'Periodo vencido') AND (sda.ESTATUSSOLICITUD != 'Solicitud caduca') AND (sda.ESTATUSSOLICITUD not like '%Solicitud vencida en:%') AND (sda.ESTATUSSOLICITUD not like '%Período vencido en:%')"
				} else if (object.estatusSolicitud.equals("Aspirantes en proceso aceptados")) {
					where += " AND (sda.ESTATUSSOLICITUD = 'Sin definir')"
				} else if (object.estatusSolicitud.equals("Aspirantes en proceso resultados")) {
					where += " AND (sda.ESTATUSSOLICITUD = 'Carga y consulta de resultados')"
				}else if(object.estatusSolicitud.equals("Avanzar Aspirante")){
					//where += " AND ( sda.ESTATUSSOLICITUD = 'Autodescripción concluida' OR sda.ESTATUSSOLICITUD = 'Elección de pruebas calendarizado' OR sda.ESTATUSSOLICITUD = 'Ya se imprimió su credencial' OR sda.ESTATUSSOLICITUD = 'Periodo vencido')";
				}

			}
			if (lstGrupo.size() > 0) {
				campus += " AND ("
			}
			for (Integer i = 0; i < lstGrupo.size(); i++) {
				String campusMiembro = lstGrupo.get(i);
				campus += "campus.descripcion='" + campusMiembro + "'"
				if (i == (lstGrupo.size() - 1)) {
					campus += ") "
				} else {
					campus += " OR "
				}
			}

			errorlog += "campus" + campus;
			errorlog += "object.lstFiltro" + object.lstFiltro
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();

			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs = pstm.executeQuery();
			if (rs.next()) {
				SSA = rs.getString("valor")
			}

			String consulta = Statements.GET_ASPIRANTES_EN_PROCESO

			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				errorlog = consulta + " 1";
				switch (filtro.get("columna")) {

					case "NOMBRE,EMAIL,CURP":
						errorlog += "NOMBRE,EMAIL,CURP"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					case "PROGRAMA,PERÍODO DE INGRESO,CAMPUS INGRESO":
						errorlog += "PROGRAMA, PERÍODO DE INGRESO, CAMPUS INGRESO"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))

						break;

					case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
						errorlog += "PREPARATORIA,ESTADO,PROMEDIO"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						/*where +=" ( LOWER(estado.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						*/
						where += "( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += "  OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ULTIMA MODIFICACION":
						errorlog += "FECHAULTIMAMODIFICACION"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " (LOWER(fechaultimamodificacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where += " OR to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') ";
						where += "LIKE LOWER('%[valor]%'))";

						where = where.replace("[valor]", filtro.get("valor"))
						break;

						//filtrado utilizado en lista roja y rechazado
					case "NOMBRE,EMAIL,CURP":
						errorlog += "NOMBRE,EMAIL,CURP"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					case "CAMPUS,PROGRAMA,INGRESO":
						errorlog += "PROGRAMA,INGRESO,CAMPUS"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))

						break;

					case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
						errorlog += "PREPARATORIA,ESTADO,PROMEDIO"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += "( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						/*
						where +=" OR LOWER(sda.estadoextranjero) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						*/
						where += " OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					case "ESTATUS,TIPO":
						errorlog += "PREPARATORIA,ESTADO,PROMEDIO"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(R.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					case "INDICADORES":
						errorlog += "INDICADORES"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}

						where += " ( LOWER(R.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(TA.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(TAL.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))

						break;

						// filtrados normales
					case "NÚMERO DE SOLICITUD":
						errorlog += "SOLICITUD"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(CAST(sda.caseid AS varchar)) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					case "NOMBRE":
						errorlog += "NOMBRE"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "EMAIL":
						errorlog += "EMAIL"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(sda.correoelectronico) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "CURP":
						errorlog += "CURP"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(sda.curp) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "CAMPUS":
						errorlog += "CAMPUS"
						campus += " AND LOWER(campus.DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							campus += "=LOWER('[valor]')"
						} else {
							campus += "LIKE LOWER('%[valor]%')"
						}
						campus = campus.replace("[valor]", filtro.get("valor"))
						break;
					case "PREPARATORIA":
						errorlog += "PREPARATORIA"
						bachillerato += " AND LOWER(prepa.DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							bachillerato += "=LOWER('[valor]')"
						} else {
							bachillerato += "LIKE LOWER('%[valor]%')"
						}
						bachillerato = bachillerato.replace("[valor]", filtro.get("valor"))
						break;
					case "PROGRAMA":
						errorlog += "PROGRAMA"
						programa += " AND LOWER(gestionescolar.Nombre) ";
						if (filtro.get("operador").equals("Igual a")) {
							programa += "=LOWER('[valor]')"
						} else {
							programa += "LIKE LOWER('%[valor]%')"
						}
						programa = programa.replace("[valor]", filtro.get("valor"))
						break;
					case "INGRESO":
						errorlog += "INGRESO"
						ingreso += " AND LOWER(periodo.DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							ingreso += "=LOWER('[valor]')"
						} else {
							ingreso += "LIKE LOWER('%[valor]%')"
						}
						ingreso = ingreso.replace("[valor]", filtro.get("valor"))
						break;
					case "PROCEDENCIA":
						errorlog += "PROCEDENCIA"
						estado += " AND LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) ";
						if (filtro.get("operador").equals("Igual a")) {
							estado += "=LOWER('[valor]')"
						} else {
							estado += "LIKE LOWER('%[valor]%')"
						}
						estado = estado.replace("[valor]", filtro.get("valor"))
						break;
					case "PROMEDIO":
						errorlog += "PROMEDIO"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(sda.PROMEDIOGENERAL) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ESTATUS":
						errorlog += "ESTATUS"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(sda.ESTATUSSOLICITUD) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "TELEFONO":
						errorlog += "TELEFONO"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(sda.telefonocelular) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "TIPO":
						errorlog += "TIPO"
						tipoalumno += " AND LOWER(R.descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
					case "IDBANNER":
						errorlog += "IDBANNER"
						tipoalumno += " AND LOWER(da.idbanner) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;

					case "ID BANNER":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(da.idbanner) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					case "LISTAROJA":
						errorlog += "LISTAROJA"
						tipoalumno += " AND LOWER(da.observacionesListaRoja) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
					case "MOTIVO DE LISTA ROJA":
						errorlog += "LISTAROJA"
						tipoalumno += " AND LOWER(da.observacionesListaRoja) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
					case "RECHAZO":
						errorlog += "RECHAZO"
						tipoalumno += " AND LOWER(da.observacionesRechazo) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
					case "MOTIVO DE LISTA RECHAZO":
						errorlog += "RECHAZO"
						tipoalumno += " AND LOWER(da.observacionesRechazo) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
					case "FECHA SOLICITUD":
						errorlog += "FECHA SOLICITUD"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(fechasolicitudenviada) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}


						where = where.replace("[valor]", filtro.get("valor"))
						break;
						/*============================================*/
					case "CIUDAD":
						errorlog += "CIUDAD";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(sda.ciudad) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "PROCEDENCIA":
						errorlog += "PROCEDENCIA";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "PAÍS":
						errorlog += "PAÍS";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(catPais.descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "CAMPUS INGRESO":
						errorlog += "CAMPUS INGRESO";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(campusEstudio.descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "FECHA DE ENVIO":
						errorlog += "FECHA DE ENVIO";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " to_char(sda.fechasolicitudenviada::timestamp, 'DD-MM-YYYY HH24:MI:SS') ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ADMISIÓN ANÁHUAC":
						errorlog += "ADMISIÓN ANÁHUAC";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " sda.admisionAnahuac ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=[valor]"
						} else {
							where += "=[valor]"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "PAA (COLLEGE BOARD)":
						errorlog += "PAA (COLLEGE BOARD)";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " sda.tienePAA ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=[valor]"
						} else {
							where += "=[valor]"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "DESCUENTO EN EXAMEN":
						errorlog += "DESCUENTO EN EXAMEN";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " sda.tieneDescuento ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=[valor]"
						} else {
							where += "=[valor]"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						/*====================================================================*/

					default:

						//consulta=consulta.replace("[BACHILLERATO]", bachillerato)
						//consulta=consulta.replace("[WHERE]", where);

						break;
				}

			}
			errorlog = consulta + " 2";
			switch (object.orderby) {
				case "RESIDEICA":
					orderby += "residensia";
					break;
				case "TIPODEADMISION":
					orderby += "tipoadmision";
					break;
				case "TIPODEALUMNO":
					orderby += "tipoDeAlumno";
					break;
				case "FECHAULTIMAMODIFICACION":
					orderby += "sda.fechaultimamodificacion";
					break;
				case "NOMBRE":
					orderby += "sda.apellidopaterno";
					break;
				case "EMAIL":
					orderby += "sda.correoelectronico";
					break;
				case "CURP":
					orderby += "sda.curp";
					break;
				case "CAMPUS":
					orderby += "campus.DESCRIPCION"
					break;
				case "PREPARATORIA":
					orderby += "prepa.DESCRIPCION"
					break;
				case "PROGRAMA":
					orderby += "gestionescolar.NOMBRE"
					break;
				case "INGRESO":
					orderby += "periodo.DESCRIPCION"
					break;
				case "PROCEDENCIA":
					orderby += "CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END";
					break;
				case "PROMEDIO":
					orderby += "sda.PROMEDIOGENERAL";
					break;
				case "ESTATUS":
					orderby += "sda.ESTATUSSOLICITUD";
					break;
				case "TIPO":
					orderby += "da.TIPOALUMNO";
					break;
				case "TELEFONO":
					orderby += "sda.telefonocelular";
					break;
				case "IDBANNER":
					orderby += "da.idbanner";
					break;
				case "SOLICITUD":
					orderby += "sda.caseid::INTEGER";
					break;
				case "LISTAROJA":
					orderby += "da.observacionesListaRoja";
					break;
				case "RECHAZO":
					orderby += "da.observacionesRechazo";
					break;
				case "FECHASOLICITUD":
					orderby += "sda.fechasolicitudenviada";
					break;
				default:
					orderby += "sda.persistenceid"
					break;
			}
			errorlog = consulta + " 3";
			orderby += " " + object.orientation;
			consulta = consulta.replace("[CAMPUS]", campus)
			consulta = consulta.replace("[PROGRAMA]", programa)
			consulta = consulta.replace("[INGRESO]", ingreso)
			consulta = consulta.replace("[ESTADO]", estado)
			consulta = consulta.replace("[BACHILLERATO]", bachillerato)
			consulta = consulta.replace("[TIPOALUMNO]", tipoalumno)
			//consulta=consulta.replace("[TIPORESIDENCIA]", tiporecidencia)
			//consulta=consulta.replace("[TIPODEADMISION]", tipodeadmision)
			//consulta=consulta.replace("[SEDEDELEXAMEN]", sededelexamen)
			where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno
			errorlog = consulta + " 4";

			//where+=" "+campus +" "+programa +" " + ingreso + " " + estado +" "+bachillerato +" "+tipoalumno+" "+tiporecidencia+" "+tipodeadmision+" "+sededelexamen
			consulta = consulta.replace("[WHERE]", where);
			errorlog = consulta + " 5";
			pstm = con.prepareStatement(consulta.replace("periodo.fechafin as periodofin,CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechasolicitudenviada, sda.fechaultimamodificacion, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, sda.telefono, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "").replace("GROUP BY periodo.fechafin,prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.fechaultimamodificacion, sda.fechasolicitudenviada, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,da.tipoalumno,sda.caseid,sda.telefonocelular,sda.telefono,da.observacioneslistaroja,da.observacionesrechazo,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid", ""))
			//pstm = con.prepareStatement(consulta.replace("CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechasolicitudenviada, sda.fechaultimamodificacion, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
			//pstm = con.prepareStatement(consulta.replace("to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechasolicitudenviada, sda.fechaultimamodificacion, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
			//pstm = con.prepareStatement(consulta.replace("sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, estado.DESCRIPCION AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))

			//pstm = con.prepareStatement(consulta.replace("sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.DESCRIPCION AS licenciatura, periodo.DESCRIPCION AS ingreso, estado.DESCRIPCION AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
			errorlog = consulta + " 6";
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorlog = consulta + " 7";

			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)
			rs = pstm.executeQuery()
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
	
	public Result selectSolicitudesApoyoFinanzas(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
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

			assert object instanceof Map;
			where += " WHERE SDAE.eliminado = false "
				
			if (object.estatusSolicitud != null) {
				where += " AND SF.estatusSolicitud IN ("+object.estatusSolicitud+") "
			}
			
			if (object.caseId != null) {
				where += " AND SDAE.caseId = "+object.caseId +" "
			}
			
			if (lstGrupo.size() > 0) {
				where += " AND ("
			}
			for (Integer i = 0; i < lstGrupo.size(); i++) {
				String campusMiembro = lstGrupo.get(i);
				where += "campus.descripcion='" + campusMiembro + "'"
				if (i == (lstGrupo.size() - 1)) {
					where += ") "
				} else {
					where += " OR "
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

			String consulta = Statements.GET_SOLICITUDES_APOYO_BY_ESTATUS;
			String consultaCount = Statements.GET_COUNT_SOLICITUDES_APOYO_BY_ESTATUS;

			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				errorlog = consulta + " 1";
				switch (filtro.get("columna")) {

					case "NOMBRE,EMAIL,CURP":
						errorlog += "NOMBRE,EMAIL,CURP"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
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

						where += " OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
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
						
					case "CAMPUS":
						errorlog += "CAMPUS"
						where += " AND LOWER(campus.DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ULTIMA MODIFICACION":
						errorlog += "FECHAULTIMAMODIFICACION"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " (LOWER(SDAE.fechaultimamodificacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%'))"
						}

						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "# EXPEDIENTE":
						errorlog += "SOLICITUD"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(CAST(SDAE.caseid AS varchar)) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ESTATUS":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(SF.estatusSolicitud) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "P-BECA":
						errorlog += "P-BECA"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " (LOWER(AA.porcentajeBecaAutorizacion::varchar(255)) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%'))"
						}

						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "T-BECA":
						errorlog += "T-BECA"
						String addWhere = "";
						if (where.contains("WHERE")) {
							addWhere += " AND "
						} else {
							addWhere += " WHERE "
						}
						
						if((Boolean) filtro.get("valor")) {
							addWhere += " (AA.porcentajeBecaAutorizacion IS NOT NULL) ";
							where += addWhere;
						}
						
						break;
					case "P-FINAN":
						errorlog += "P-FINAN"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " (LOWER(SF.porcComite::varchar(255)) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%'))"
						}

						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "T-FINAN":
						errorlog += "T-FINAN"
						String addWhere = "";
						
						if (where.contains("WHERE")) {
							addWhere += " AND "
						} else {
							addWhere += " WHERE "
						}
						
						if((Boolean) filtro.get("valor")) {
							addWhere += " (SF.porcComite IS NOT NULL) ";
							where += addWhere;
						}
						
						break;
					case "F-TRAMITE":
						errorlog += "F-TRAMITE"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " (LOWER( CASE SF.finalizada WHEN 't' THEN 'Concluido' WHEN 'f' THEN 'En proceso' ELSE 'N/A' END ) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%'))"
						}

						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "LICENCIATURA":
						errorlog += "LICENCIATURA"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "PERIODO":
						errorlog += "PERIODO"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					default:
						break;
				}

			}
			errorlog = consulta + " 2";
			switch (object.orderby) {
				case "FECHAULTIMAMODIFICACION":
					orderby += "sda.fechaultimamodificacion";
					break;
				case "NOMBRE":
					orderby += "sda.apellidopaterno";
					break;
				case "EMAIL":
					orderby += "sda.correoelectronico";
					break;
				case "CURP":
					orderby += "sda.curp";
					break;
				case "CAMPUS":
					orderby += "campus.DESCRIPCION"
					break;
				case "PREPARATORIA":
					orderby += "prepa.DESCRIPCION"
					break;
				case "PROGRAMA":
					orderby += "gestionescolar.NOMBRE"
					break;
				case "INGRESO":
					orderby += "periodo.DESCRIPCION"
					break;
				case "PROMEDIO":
					orderby += "sda.PROMEDIOGENERAL";
					break;
				case "ESTATUS":
					orderby += "SDAE.estatusSolicitud";
					break;
				case "TIPOBECA":
					orderby += "tipoapoyo.descripcion";
					break;
				case "EXPEDIENTE":
					orderby += "sda.caseid::INTEGER";
					break;
				case "FECHAULTIMAMODIFICACION":
					orderby += "SDAE.fechaultimamodificacion";
					break;
				default:
					orderby += "SDAE.caseid"
					break;
			}
			
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);
			consultaCount = consultaCount.replace("[WHERE]", where);

			pstm = con.prepareStatement(consultaCount)
			//errorlog = consultaCount + " 6";
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorlog = consulta + " 7";

			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)
			
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			errorlog = consulta + " 8";
			
			errorlog += where;
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					if (metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
						String encoded = "";
						boolean noAzure = false;
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
			resultado.setSuccess(true)

			resultado.setError_info(errorlog);
			resultado.setData(rows)

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
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
	
	public Result getExcelFile(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
    Result resultado = new Result();
    String errorLog = "";

    try {
        def jsonSlurper = new JsonSlurper();
        def object = jsonSlurper.parseText(jsonData);

        Result dataResult = selectSolicitudesApoyo(parameterP, parameterC, jsonData, context);
        resultado = dataResult;

        int rowCount = 0;
        List<Object> lstParams;
        String type = object.type;
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(type);
        CellStyle style = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        if (dataResult.success) {
            lstParams = dataResult.getData();
        } else {
            throw new Exception("No encontró datos");
        }

        String title = object.estatussolicitud;
        Row titleRow = sheet.createRow(++rowCount);
        Cell cellReporte = titleRow.createCell(1);
        cellReporte.setCellValue("Reporte:");
        cellReporte.setCellStyle(style);
        Cell cellTitle = titleRow.createCell(2);
        cellTitle.setCellValue(title);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -7);
        Date date = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
        String sDate = formatter.format(date);

        Row blank = sheet.createRow(++rowCount);
        Cell cellusuario = blank.createCell(4);
        cellusuario.setCellValue("Usuario:");
        cellusuario.setCellStyle(style);
        Cell cellusuarioData = blank.createCell(5);
        cellusuarioData.setCellValue(object.usuario);
        Row espacio = sheet.createRow(++rowCount);
        Row headersRow = sheet.createRow(++rowCount);
        Cell header1 = headersRow.createCell(0);
        header1.setCellValue("ID Banner");
        header1.setCellStyle(style);
		Cell header2 = headersRow.createCell(1);
		header2.setCellValue("ID BPM");
		header2.setCellStyle(style);
        Cell header3 = headersRow.createCell(2);
        header3.setCellValue("Nombre");
        header3.setCellStyle(style);
        Cell header4 = headersRow.createCell(3);
        header4.setCellValue("Email");
        header4.setCellStyle(style);
        Cell header5 = headersRow.createCell(4);
        header5.setCellValue("CURP");
        header5.setCellStyle(style);
        Cell header6 = headersRow.createCell(5);
        header6.setCellValue("Programa");
        header6.setCellStyle(style);
        Cell header7 = headersRow.createCell(6);
        header7.setCellValue("Período de ingreso");
        header7.setCellStyle(style);
        Cell header8 = headersRow.createCell(7);
        header8.setCellValue("Campus ingreso");
        header8.setCellStyle(style);
        Cell header9 = headersRow.createCell(8);
        header9.setCellValue("Tipo Beca");
        header9.setCellStyle(style);
        Cell header10 = headersRow.createCell(9);
        header10.setCellValue("Promedio Admisiones");
        header10.setCellStyle(style);
        Cell header11 = headersRow.createCell(10);
        header11.setCellValue("Promedio Actualizado");
        header11.setCellStyle(style);
        Cell header12 = headersRow.createCell(11);
        header12.setCellValue("Estatus SDAE");
        header12.setCellStyle(style);
        Cell header13 = headersRow.createCell(12);
        header13.setCellValue("Estatus Admisión");
        header13.setCellStyle(style);
		Cell header14 = headersRow.createCell(13);
		header14.setCellValue("Asignado a");
		header14.setCellStyle(style);
		Cell header15 = headersRow.createCell(14);
		header15.setCellValue("Fecha de creación de la solicitud");
		header15.setCellStyle(style);
		Cell header16 = headersRow.createCell(15);
		header16.setCellValue("Última modificación");
		header16.setCellStyle(style);

        DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date fechaCreacion = new Date();
        DateFormat dformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        for (int i = 0; i < lstParams.size(); ++i) {
            Row row = sheet.createRow(++rowCount);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue(lstParams.get(i).idbanner);
			Cell cell2 = row.createCell(1);
			cell2.setCellValue(lstParams.get(i).caseid);
            String nombre = lstParams.get(i).apellidopaterno + " " + lstParams.get(i).apellidomaterno + " " + lstParams.get(i).primernombre + " " + lstParams.get(i).segundonombre;
            Cell cell3 = row.createCell(2);
            cell3.setCellValue(nombre);
            Cell cell4 = row.createCell(3);
            cell4.setCellValue(lstParams.get(i).correoelectronico);
            Cell cell5 = row.createCell(4);
            cell5.setCellValue(lstParams.get(i).curp);
            Cell cell6 = row.createCell(5);
            cell6.setCellValue(lstParams.get(i).licenciatura);
            Cell cell7 = row.createCell(6);
            cell7.setCellValue(lstParams.get(i).ingreso);
            Cell cell8 = row.createCell(7);
            cell8.setCellValue(lstParams.get(i).campusestudio);
            Cell cell9 = row.createCell(8);
            cell9.setCellValue(lstParams.get(i).tipoapoyo);
            Cell cell10 = row.createCell(9);
            cell10.setCellValue(lstParams.get(i).promediogeneral);
            Cell cell11 = row.createCell(10);
            cell11.setCellValue(lstParams.get(i).nuevopromedioprepa == null ? "N/A" : lstParams.get(i).nuevopromedioprepa);
            Cell cell12 = row.createCell(11);
            cell12.setCellValue(lstParams.get(i).estatussolicitud);
            Cell cell13 = row.createCell(12);
            cell13.setCellValue(lstParams.get(i).aceptado);
			 Cell cell14 = row.createCell(13);
				String estatus = lstParams[i].estatussolicitud.trim();

				if (estatus.equalsIgnoreCase("Esperando Pre-Autorización") || estatus.equalsIgnoreCase("En espera de resultado") || estatus.equalsIgnoreCase("Correcciones realizadas")) {
					cell14.setCellValue("Pre-Autorización");
				} else if (estatus.equalsIgnoreCase("Esperando revisión área artistica")) {
					cell14.setCellValue("Pre-Autorización");
				} else if (estatus.equalsIgnoreCase("Esperando revisión área deportiva")) {
					cell14.setCellValue("Pre-Autorización");
				}else if (estatus.equalsIgnoreCase("En espera de autorización")) {
					cell14.setCellValue("Comité de becas");
				} else if (estatus.equalsIgnoreCase("Solicitud Rechazada")) {
					cell14.setCellValue("Archivo");
				} else if (estatus.equalsIgnoreCase("Solicitud de financiamiento autorizada")) {
					cell14.setCellValue(""); // Dejar en blanco
				} else {
					cell14.setCellValue("Aspirante");
				}

            String fechaRegistroString = lstParams.get(i).fecharegistro;

            if (fechaRegistroString != null) {
                String year = fechaRegistroString.substring(0, 4);
                String month = fechaRegistroString.substring(5, 7);
                String day = fechaRegistroString.substring(8, 10);
                String time = fechaRegistroString.substring(11, 16);
                String fechaFormateada = day + "-" + month + "-" + year + " " + time;

                Cell cell15 = row.createCell(14);
                cell15.setCellValue(fechaFormateada);
            } else {
                Cell cell15 = row.createCell(14);
                cell15.setCellValue("N/A");
            }

            String fechaUltimaModificacionString = lstParams.get(i).fechaultimamodificacion;

            if (fechaUltimaModificacionString != null) {
                String year = fechaUltimaModificacionString.substring(0, 4);
                String month = fechaUltimaModificacionString.substring(5, 7);
                String day = fechaUltimaModificacionString.substring(8, 10);
                String time = fechaUltimaModificacionString.substring(11, 16);
                String fechaFormateada = day + "-" + month + "-" + year + " " + time;

                Cell cell16 = row.createCell(15);
                cell16.setCellValue(fechaFormateada);
            } else {
                Cell cell16 = row.createCell(15);
                cell16.setCellValue("N/A");
            }
        }

        for (int i = 0; i <= rowCount + 3; ++i) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream outputStream = new FileOutputStream("ReporteBecasCompletadas.xls");
        workbook.write(outputStream);

        List<Object> lstResultado = new ArrayList<Object>();
        lstResultado.add(encodeFileToBase64Binary("ReporteBecasCompletadas.xls"));
        resultado.setError_info(errorLog);
        resultado.setSuccess(true);
        resultado.setData(lstResultado);
    } catch (Exception e) {
        LOGGER.error("[ERROR] " + e.getMessage());
        e.printStackTrace();
        resultado.setSuccess(false);
        resultado.setError(e.getMessage());
        resultado.setError_info(errorLog);
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
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		is.close();
		return bytes;
	}
	
	public Boolean validarConexion() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno = true
		}
		return retorno
	}
	
	public Boolean validarConexionBonita() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno = true
		}
		return retorno
	}
	
	public Result getExcelFileCierre(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
    Result resultado = new Result();
    String errorLog = "";
    
    try {
        def jsonSlurper = new JsonSlurper();
        def object = jsonSlurper.parseText(jsonData);
        
        String buttonSource = object.buttonSource;
        String sourceButton = buttonSource;

        Result dataResult = new Result();
        
        
        int rowCount = 0;
        List < Object > lstParams;
        String type = object.type;
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(type);
        CellStyle style = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
		
		Result dataResultCompletadas = selectSolicitudesApoyoCompletadas(parameterP, parameterC, jsonData, context)

		if (dataResultCompletadas.success) {
			lstParams = dataResultCompletadas.getData()
		} else {
			throw new Exception("No se encontraron datos en infoResult")
		}
	
        

        
        String title = object.estatussolicitud;
        Row titleRow = sheet.createRow(++rowCount);
        Cell cellReporte = titleRow.createCell(1);
        cellReporte.setCellValue("Reporte:");
        cellReporte.setCellStyle(style);
        Cell cellTitle = titleRow.createCell(2);
        cellTitle.setCellValue(title);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -7);
        Date date = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
        String sDate = formatter.format(date);

        Row blank = sheet.createRow(++rowCount);
        Cell cellusuario = blank.createCell(4);
        cellusuario.setCellValue("Usuario:");
        cellusuario.setCellStyle(style);
        Cell cellusuarioData = blank.createCell(5);
        cellusuarioData.setCellValue(object.usuario);
        Row espacio = sheet.createRow(++rowCount);
        Row headersRow = sheet.createRow(++rowCount);
        Cell header1 = headersRow.createCell(0);
        header1.setCellValue("ID Banner");
        header1.setCellStyle(style);
		Cell header2 = headersRow.createCell(1);
		header2.setCellValue("ID BPM");
		header2.setCellStyle(style);
        Cell header3 = headersRow.createCell(2);
        header3.setCellValue("Nombre");
        header3.setCellStyle(style);
        Cell header4 = headersRow.createCell(3);
        header4.setCellValue("Email");
        header4.setCellStyle(style);
        Cell header5 = headersRow.createCell(4);
        header5.setCellValue("CURP");
        header5.setCellStyle(style);
        Cell header6 = headersRow.createCell(5);
        header6.setCellValue("Programa");
        header6.setCellStyle(style);
        Cell header7 = headersRow.createCell(6);
        header7.setCellValue("Período de ingreso");
        header7.setCellStyle(style);
        Cell header8 = headersRow.createCell(7);
        header8.setCellValue("Campus ingreso");
        header8.setCellStyle(style);
        Cell header9 = headersRow.createCell(8);
        header9.setCellValue("Tipo Beca");
        header9.setCellStyle(style);
        Cell header10 = headersRow.createCell(9);
        header10.setCellValue("Promedio");
        header10.setCellStyle(style);
        Cell header11 = headersRow.createCell(10);
        header11.setCellValue("Promedio Actualizado");
        header11.setCellStyle(style);
        Cell header12 = headersRow.createCell(11);
        header12.setCellValue("% de beca autorizado");
        header12.setCellStyle(style);
        if ("1".equals(buttonSource)) {
            Cell header13 = headersRow.createCell(12);
            header13.setCellValue("% de financiamiento autorizado");
            header13.setCellStyle(style);
            Cell header14 = headersRow.createCell(13);
            header14.setCellValue("Pago de propedéutico");
            header14.setCellStyle(style);
            Cell header15 = headersRow.createCell(14);
            header15.setCellValue("Admitido a Medicina");
            header15.setCellStyle(style);
            Cell header16 = headersRow.createCell(15);
            header16.setCellValue("Pago de inscripción");
            header16.setCellStyle(style);
            Cell header17 = headersRow.createCell(16);
            header17.setCellValue("Carga de materias");
            header17.setCellStyle(style);
			Cell header18 = headersRow.createCell(17);
			header18.setCellValue("Programa");
			header18.setCellStyle(style);
			Cell header19 = headersRow.createCell(18);
			header19.setCellValue("Período inscrito");
			header19.setCellStyle(style);
			Cell header20 = headersRow.createCell(19);
			header20.setCellValue("Campus inscrito");
			header20.setCellStyle(style);
        } else {
			Cell header13 = headersRow.createCell(12);
			header13.setCellValue("% de financiamiento pre-autorizado");
			header13.setCellStyle(style);
			Cell header14 = headersRow.createCell(13);
			header14.setCellValue("Estatus financiamiento");
			header14.setCellStyle(style);
			Cell header15 = headersRow.createCell(14);
			header15.setCellValue("Estatus SDAE");
			header15.setCellStyle(style);
            Cell header17 = headersRow.createCell(16);
            header17.setCellValue("Pago de propedéutico");
            header17.setCellStyle(style);
            Cell header18 = headersRow.createCell(17);
            header18.setCellValue("Admitido a Medicina");
            header18.setCellStyle(style);
            Cell header19 = headersRow.createCell(18);
            header19.setCellValue("Pago de inscripción");
            header19.setCellStyle(style);
            Cell header20 = headersRow.createCell(19);
            header20.setCellValue("Carga de materias");
            header20.setCellStyle(style);
			Cell header21 = headersRow.createCell(20);
			header21.setCellValue("Programa");
			header21.setCellStyle(style);
			Cell header22 = headersRow.createCell(21);
			header22.setCellValue("Período inscrito");
			header22.setCellStyle(style);
			Cell header23 = headersRow.createCell(22);
			header23.setCellValue("Campus inscrito");
			header23.setCellStyle(style);
        }

        DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date fechaCreacion = new Date();
        DateFormat dformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        for (int i = 0; i < lstParams.size(); ++i) {
            Row row = sheet.createRow(++rowCount);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue(lstParams[i].idbanner);
			Cell cell2 = row.createCell(1);
			cell2.setCellValue(lstParams[i].caseid);
            String nombre = lstParams[i].apellidopaterno + " " + lstParams[i].apellidomaterno + " " + lstParams[i].primernombre + " " + lstParams[i].segundonombre;
            Cell cell3 = row.createCell(2);
            cell3.setCellValue(nombre);
            Cell cell4 = row.createCell(3);
            cell4.setCellValue(lstParams[i].correoelectronico);
            Cell cell5 = row.createCell(4);
            cell5.setCellValue(lstParams[i].curp);
            Cell cell6 = row.createCell(5);
            cell6.setCellValue(lstParams[i].licenciatura);
            Cell cell7 = row.createCell(6);
            cell7.setCellValue(lstParams[i].ingreso);
            Cell cell8 = row.createCell(7);
            cell8.setCellValue(lstParams[i].campusestudio);
            Cell cell9 = row.createCell(8);
            cell9.setCellValue(lstParams[i].tipoapoyo);
            Cell cell10 = row.createCell(9);
            cell10.setCellValue(lstParams[i].promediogeneral);
            Cell cell11 = row.createCell(10);
            cell11.setCellValue(lstParams[i].nuevopromedioprepa == null ? "N/A" : lstParams[i].nuevopromedioprepa);
            Cell cell12 = row.createCell(11);
            cell12.setCellValue(lstParams[i].porcentajebecaautorizacion == null ? "N/A" : lstParams[i].porcentajebecaautorizacion + (lstParams[i].porcentajebecaautorizacion.equals("N/A") ? "" : "%"));
            if ("1".equals(buttonSource)) {
                Cell cell13 = row.createCell(12);
                def porcentaje = lstParams[i].porcentajefinaautorizacion != null ? lstParams[i].porcentajefinaautorizacion.toDouble() : 0.0

				if (porcentaje && porcentaje > 0) {
					cell13.setCellValue(porcentaje)
				} else {
					cell13.setCellValue("N/A")
				}
                Cell cell14 = row.createCell(13);
                cell14.setCellValue(lstParams[i].inscripcionPropedeutico == null ? "N/A" : lstParams[i].inscripcionPropedeutico);
                Cell cell15 = row.createCell(14);
                cell15.setCellValue(lstParams[i].pasoPropedeutico == null ? "N/A" : lstParams[i].pasoPropedeutico);
                Cell cell16 = row.createCell(15);
                cell16.setCellValue(lstParams[i].inscrito == null ? "N/A" : lstParams[i].inscrito);
                Cell cell17 = row.createCell(16);
                cell17.setCellValue(lstParams[i].tomaMaterias == null ? "N/A" : lstParams[i].tomaMaterias);
				Cell cell18 = row.createCell(17);
				cell18.setCellValue(lstParams[i].programa == null ? "N/A" : lstParams[i].programa);
				Cell cell19 = row.createCell(18);
				cell19.setCellValue(lstParams[i].periodo == null ? "N/A" : lstParams[i].periodo);
				Cell cell20 = row.createCell(19);
				cell20.setCellValue(lstParams[i].campus == null ? "N/A" : lstParams[i].campus);
            } else {
				Cell cell13 = row.createCell(12);
				cell13.setCellValue(lstParams[i].porcentajefinaautorizacion == null || lstParams[i].porcentajefinaautorizacion == 0 ? "N/A" : lstParams[i].porcentajefinaautorizacion);
				Cell cell14 = row.createCell(13);
				cell14.setCellValue(lstParams[i].estatusfinanciamiento != null ? lstParams[i].estatusfinanciamiento : "N/A");
				Cell cell15 = row.createCell(14);
				cell15.setCellValue(lstParams[i].aceptado != null ? lstParams[i].aceptado : "N/A");
                Cell cell17 = row.createCell(16);
                cell17.setCellValue(lstParams[i].inscripcionPropedeutico == null ? "N/A" : lstParams[i].inscripcionPropedeutico);
                Cell cell18 = row.createCell(17);
                cell18.setCellValue(lstParams[i].pasoPropedeutico == null ? "N/A" : lstParams[i].pasoPropedeutico);
                Cell cell19 = row.createCell(18);
                cell19.setCellValue(lstParams[i].inscrito == null ? "N/A" : lstParams[i].inscrito);
                Cell cell20 = row.createCell(19);
                cell20.setCellValue(lstParams[i].tomaMaterias == null ? "N/A" : lstParams[i].tomaMaterias);
				Cell cell21 = row.createCell(20);
				cell21.setCellValue(lstParams[i].programa == null ? "N/A" : lstParams[i].programa);
				Cell cell22 = row.createCell(21);
				cell22.setCellValue(lstParams[i].periodo == null ? "N/A" : lstParams[i].periodo);
				Cell cell23 = row.createCell(22);
				cell23.setCellValue(lstParams[i].campus == null ? "N/A" : lstParams[i].campus);
            }
        }
        
        for (int i = 0; i <= rowCount + 13; ++i) {
            sheet.autoSizeColumn(i);
        }
        
        FileOutputStream outputStream = new FileOutputStream("ReporteBecasCompletadas.xls");
        workbook.write(outputStream);

        List<Object> lstResultado = new ArrayList<>();
        lstResultado.add(encodeFileToBase64Binary("ReporteBecasCompletadas.xls"));
        resultado.setError_info(errorLog);
        resultado.setSuccess(true);
        resultado.setData(lstResultado);
    } catch (Exception e) {
        LOGGER.error("[ERROR] " + e.getMessage());
        e.printStackTrace();
        resultado.setError_info(e.getMessage());
        resultado.setSuccess(false);
    }
    
    return resultado;
	}

	
	public Result countSolicitudesDeApoyoByEstatus(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "",campus = "", orderby = "ORDER BY ", errorlog = ""
		List<String> lstGrupo = new ArrayList<String>();
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map <String, String> objGrupoCampus = new HashMap <String, String> ();
		List<String> lstEstatus = new ArrayList<String>();

		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
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

			assert object instanceof Map;
			
			if(object.isCompletadas == true) {
				where += " WHERE SDAE.eliminado = false AND (SDAE.estatusSolicitud IN (" + object.estatusSolicitud + ") OR (SDAE.estatusSolicitud = 'Solicitud de Financiamiento en Proceso' AND SF.estatusSolicitud = 'Propuesta de financiamiento aceptada por aspirante'))";
			} else {
				where += " WHERE SDAE.eliminado = false AND SDAE.estatusSolicitud IN (" + object.estatusSolicitud + ") ";
			}
			
			
			if (object.caseId == null) {
				if (lstGrupo.size() > 0) {
					where += " AND ("
				}
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += "campus.descripcion='" + campusMiembro + "'"
					if (i == (lstGrupo.size() - 1)) {
						where += ") "
					} else {
						where += " OR "
					}
				}
			}

			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>> ();
			closeCon = validarConexion();
			
			Map <String, Object> row = new HashMap <String, Object>();
			pstm = con.prepareStatement(Statements.GET_COUNT_SOLICITUDES_APOYO_BY_ESTATUS.replace("[WHERE]", where));
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"));
			} else {
				resultado.setTotalRegistros(0);
			}
			
			resultado.setError_info(where);
			resultado.setSuccess(true);
			resultado.setData(new ArrayList<String>());
		} catch (Exception e) {
			resultado.setError_info(errorlog);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}

		return resultado;
	}
	
	
	public Result getPAAByIdBanner(String idbanner, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
		Map <String, Object> row = new HashMap <String, Object>();
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_PAA_BY_IDBANNER);
			pstm.setString(1, idbanner);
			
			rs = pstm.executeQuery();

			while(rs.next()) {
				row = new HashMap <String, Object>();
				
				row.put("paan", rs.getString("paan"));
				row.put("paav", rs.getString("paav"));
				row.put("para", rs.getString("para"));
				row.put("total", rs.getString("total"));
				
				rows.add(row);
			} 
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}

		return resultado;
	} 
	public Result getExcelFileBandejaMaestra(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
	
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			Result dataResult = selectBandejaMaestra(parameterP, parameterC, jsonData, context);
			resultado = dataResult;
	
			int rowCount = 0;
			List<Object> lstParams;
			String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(type);
			CellStyle style = workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
	
			if (dataResult.success) {
				lstParams = dataResult.getData();
			} else {
				throw new Exception("No encontró datos");
			}
	
			String title = object.estatussolicitud;
			Row titleRow = sheet.createRow(++rowCount);
			Cell cellReporte = titleRow.createCell(1);
			cellReporte.setCellValue("Reporte:");
			cellReporte.setCellStyle(style);
			Cell cellTitle = titleRow.createCell(2);
			cellTitle.setCellValue(title);
	
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -7);
			Date date = cal.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String sDate = formatter.format(date);
	
			Row blank = sheet.createRow(++rowCount);
			Cell cellusuario = blank.createCell(4);
			cellusuario.setCellValue("Usuario:");
			cellusuario.setCellStyle(style);
			Cell cellusuarioData = blank.createCell(5);
			cellusuarioData.setCellValue(object.usuario);
			Row espacio = sheet.createRow(++rowCount);
			Row headersRow = sheet.createRow(++rowCount);
			Cell header1 = headersRow.createCell(0);
			header1.setCellValue("ID Banner");
			header1.setCellStyle(style);
			Cell header2 = headersRow.createCell(1);
			header2.setCellValue("ID BPM");
			header2.setCellStyle(style);
			Cell header3 = headersRow.createCell(2);
			header3.setCellValue("Nombre");
			header3.setCellStyle(style);
			Cell header4 = headersRow.createCell(3);
			header4.setCellValue("Email");
			header4.setCellStyle(style);
			Cell header5 = headersRow.createCell(4);
			header5.setCellValue("CURP");
			header5.setCellStyle(style);
			Cell header6 = headersRow.createCell(5);
			header6.setCellValue("Programa");
			header6.setCellStyle(style);
			Cell header7 = headersRow.createCell(6);
			header7.setCellValue("Período de ingreso");
			header7.setCellStyle(style);
			Cell header8 = headersRow.createCell(7);
			header8.setCellValue("Campus ingreso");
			header8.setCellStyle(style);
			Cell header9 = headersRow.createCell(8);
			header9.setCellValue("Tipo Beca");
			header9.setCellStyle(style);
			Cell header10 = headersRow.createCell(9);
			header10.setCellValue("Promedio Admisiones");
			header10.setCellStyle(style);
			Cell header11 = headersRow.createCell(10);
			header11.setCellValue("Promedio Actualizado");
			header11.setCellStyle(style);
			Cell header12 = headersRow.createCell(11);
			header12.setCellValue("Estatus SDAE");
			header12.setCellStyle(style);
			Cell header13 = headersRow.createCell(12);
			header13.setCellValue("Estatus Admisión");
			header13.setCellStyle(style);
			Cell header14 = headersRow.createCell(13);
			header14.setCellValue("Asignado a");
			header14.setCellStyle(style);
			Cell header15 = headersRow.createCell(14);
			header15.setCellValue("Fecha de creación de la solicitud");
			header15.setCellStyle(style);
			Cell header16 = headersRow.createCell(15);
			header16.setCellValue("Última modificación");
			header16.setCellStyle(style);
	
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat dformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
			for (int i = 0; i < lstParams.size(); ++i) {
				Row row = sheet.createRow(++rowCount);
				Cell cell1 = row.createCell(0);
				cell1.setCellValue(lstParams.get(i).idbanner);
				Cell cell2 = row.createCell(1);
				cell2.setCellValue(lstParams.get(i).caseid);
				String nombre = lstParams.get(i).apellidopaterno + " " + lstParams.get(i).apellidomaterno + " " + lstParams.get(i).primernombre + " " + lstParams.get(i).segundonombre;
				Cell cell3 = row.createCell(2);
				cell3.setCellValue(nombre);
				Cell cell4 = row.createCell(3);
				cell4.setCellValue(lstParams.get(i).correoelectronico);
				Cell cell5 = row.createCell(4);
				cell5.setCellValue(lstParams.get(i).curp);
				Cell cell6 = row.createCell(5);
				cell6.setCellValue(lstParams.get(i).licenciatura);
				Cell cell7 = row.createCell(6);
				cell7.setCellValue(lstParams.get(i).ingreso);
				Cell cell8 = row.createCell(7);
				cell8.setCellValue(lstParams.get(i).campussede);
				Cell cell9 = row.createCell(8);
				cell9.setCellValue(lstParams.get(i).tipoapoyo);
				Cell cell10 = row.createCell(9);
				cell10.setCellValue(lstParams.get(i).promediogeneral);
				Cell cell11 = row.createCell(10);
				cell11.setCellValue(lstParams.get(i).nuevopromedioprepa != null ? lstParams.get(i).nuevopromedioprepa : "N/A");
				Cell cell12 = row.createCell(11);
				cell12.setCellValue(lstParams.get(i).estatussolicitud);
				Cell cell13 = row.createCell(12);
				cell13.setCellValue(lstParams.get(i).aceptado);
				Cell cell14 = row.createCell(13);
				String estatus = lstParams.get(i).estatussolicitud.trim();
	
				if (estatus.equalsIgnoreCase("Esperando Pre-Autorización") || estatus.equalsIgnoreCase("En espera de resultado") || estatus.equalsIgnoreCase("Correcciones realizadas")) {
					cell14.setCellValue("Pre-Autorización");
				} else if (estatus.equalsIgnoreCase("Esperando revisión área artistica")) {
					cell14.setCellValue("Pre-Autorización");
				} else if (estatus.equalsIgnoreCase("Esperando revisión área deportiva")) {
					cell14.setCellValue("Pre-Autorización");
				} else if (estatus.equalsIgnoreCase("En espera de autorización")) {
					cell14.setCellValue("Comité de becas");
				} else if (estatus.equalsIgnoreCase("Solicitud Rechazada")) {
					cell14.setCellValue("Archivo");
				} else if (estatus.equalsIgnoreCase("Solicitud de financiamiento autorizada")) {
					cell14.setCellValue(""); // Dejar en blanco
				} else {
					cell14.setCellValue("Aspirante");
				}
	
				String fechaRegistroString = lstParams.get(i).fecharegistro;
	
				if (fechaRegistroString != null) {
					Date fechaRegistro = dfSalida.parse(fechaRegistroString);
					String fechaFormateada = dformat.format(fechaRegistro);
					Cell cell15 = row.createCell(14);
					cell15.setCellValue(fechaFormateada);
				} else {
					Cell cell15 = row.createCell(14);
					cell15.setCellValue("N/A");
				}
	
				String fechaUltimaModificacionString = lstParams.get(i).fechaultimamodificacion;
	
				if (fechaUltimaModificacionString != null) {
					Date fechaUltimaModificacion = dfSalida.parse(fechaUltimaModificacionString);
					String fechaFormateada = dformat.format(fechaUltimaModificacion);
					Cell cell16 = row.createCell(15);
					cell16.setCellValue(fechaFormateada);
				} else {
					Cell cell16 = row.createCell(15);
					cell16.setCellValue("N/A");
				}
			}
	
			for (int i = 0; i <= rowCount + 3; ++i) {
				sheet.autoSizeColumn(i);
			}
	
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
	
			List<Object> lstResultado = new ArrayList<Object>();
			lstResultado.add(Base64.getEncoder().encodeToString(outputStream.toByteArray()));
			resultado.setError_info(errorLog);
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
		} catch (Exception e) {
			LOGGER.error("[ERROR] " + e.getMessage());
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		}
	
		return resultado;
	}
}
