package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement



import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class TransferenciasDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferenciasDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Result getUsuariosTransferencia(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
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
			where+=" WHERE sda.iseliminado=false "
			where+=" AND (sda.ESTATUSSOLICITUD <> 'Solicitud lista roja' AND sda.ESTATUSSOLICITUD <> 'Solicitud rechazada' AND sda.ESTATUSSOLICITUD <> 'Aspirantes registrados sin validación de cuenta' AND sda.ESTATUSSOLICITUD <> 'Aspirantes registrados con validación de cuenta')"
//				if(object.estatusSolicitud !=null) {
				
//					where+="AND (sda.ESTATUSSOLICITUD <> 'Solicitud lista roja' AND sda.ESTATUSSOLICITUD <> 'Solicitud rechazada' AND sda.ESTATUSSOLICITUD <> 'Aspirantes registrados sin validación de cuenta' AND sda.ESTATUSSOLICITUD <> 'Aspirantes registrados con validación de cuenta')"
				
				/*if(object.estatusSolicitud.equals("Solicitud lista roja")) {
					where+=" AND sda.ESTATUSSOLICITUD='Solicitud lista roja'"
				}
				else if(object.estatusSolicitud.equals("Solicitud rechazada")) {
					where+=" AND sda.ESTATUSSOLICITUD='Solicitud rechazada'"
				} else if(object.estatusSolicitud.equals("Aspirantes registrados sin validación de cuenta")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Aspirantes registrados sin validación de cuenta')"
				} else if(object.estatusSolicitud.equals("Aspirantes registrados con validación de cuenta")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Aspirantes registrados con validación de cuenta')"
				}else if(object.estatusSolicitud.equals("Solicitud en espera de pago")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Solicitud en espera de pago')"
				}
				else if(object.estatusSolicitud.equals("Solicitud con pago aceptado")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Solicitud con pago aceptado')"
				}
				else if(object.estatusSolicitud.equals("Autodescripción en proceso")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Autodescripción en proceso')"
				}
				else if(object.estatusSolicitud.equals("Elección de pruebas no calendarizado")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Elección de pruebas no calendarizado')"
				}
				else if(object.estatusSolicitud.equals("No se ha impreso credencial")) {
					where+=" AND (sda.ESTATUSSOLICITUD='No se ha impreso credencial')"
				}
				else if(object.estatusSolicitud.equals("Ya se imprimió su credencial")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Ya se imprimió su credencial')"
				}*/
//			}
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
			
			//errorlog+="campus" + campus;
				errorlog+="object.lstFiltro" +object.lstFiltro
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				String consulta = Statements.GET_SOLICITUDES_TRANSFERENCIA
				errorlog+="Aqui tomo la primer consulta: "+consulta
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					errorlog+=", columna "+ filtro.get("columna")
					switch(filtro.get("columna")) {
						
						case "NOMBRE":
							errorlog+="NOMBRE"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(concat(sda.primernombre,' ', sda.segundonombre,' ',sda.apellidopaterno,' ',sda.apellidomaterno)) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "EMAIL":
							errorlog+="EMAIL"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.correoelectronico) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "CURP":
							errorlog+="CURP"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.curp) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "CAMPUS":
							errorlog+="CAMPUS"
							campus +=" AND LOWER(campus.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								campus+="=LOWER('[valor]')"
							}else {
								campus+="LIKE LOWER('%[valor]%')"
							}
							campus = campus.replace("[valor]", filtro.get("valor"))
							break;
						case "PREPARATORIA":
							errorlog+="PREPARATORIA"
							bachillerato +=" AND LOWER(prepa.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								bachillerato+="=LOWER('[valor]')"
							}else {
								bachillerato+="LIKE LOWER('%[valor]%')"
							}
							bachillerato = bachillerato.replace("[valor]", filtro.get("valor"))
							break;
						case "PROGRAMA":
							errorlog+="PROGRAMA"
							programa +=" AND LOWER(gestionescolar.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								programa+="=LOWER('[valor]')"
							}else {
								programa+="LIKE LOWER('%[valor]%')"
							}
							programa = programa.replace("[valor]", filtro.get("valor"))
							break;
						case "INGRESO":
							errorlog+="INGRESO"
							ingreso +=" AND LOWER(periodo.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								ingreso+="=LOWER('[valor]')"
							}else {
								ingreso+="LIKE LOWER('%[valor]%')"
							}
							ingreso = ingreso.replace("[valor]", filtro.get("valor"))
							break;
						case "ESTADO":
							errorlog+="ESTADO"
							estado +=" AND LOWER(estado.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								estado+="=LOWER('[valor]')"
							}else {
								estado+="LIKE LOWER('%[valor]%')"
							}
							estado = estado.replace("[valor]", filtro.get("valor"))
							break;
						case "PROMEDIO":
							errorlog+="PROMEDIO"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.PROMEDIOGENERAL) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "ESTATUS":
							errorlog+="ESTATUS"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.ESTATUSSOLICITUD) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "TELEFONO":
							errorlog+="TELEFONO"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.telefonocelular) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "TIPO":
							errorlog+="TIPO"
							tipoalumno +=" AND LOWER(da.TIPOALUMNO) ";
							if(filtro.get("operador").equals("Igual a")) {
								tipoalumno+="=LOWER('[valor]')"
							}else {
								tipoalumno+="LIKE LOWER('%[valor]%')"
							}
							tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
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
						case "LISTAROJA":
							errorlog+="LISTAROJA"
							tipoalumno +=" AND LOWER(da.observacionesListaRoja) ";
							if(filtro.get("operador").equals("Igual a")) {
								tipoalumno+="=LOWER('[valor]')"
							}else {
								tipoalumno+="LIKE LOWER('%[valor]%')"
							}
							tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
							break;
						case "RECHAZO":
							errorlog+="RECHAZO"
							tipoalumno +=" AND LOWER(da.observacionesRechazo) ";
							if(filtro.get("operador").equals("Igual a")) {
								tipoalumno+="=LOWER('[valor]')"
							}else {
								tipoalumno+="LIKE LOWER('%[valor]%')"
							}
							tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
							break;
						default:
						//consulta=consulta.replace("[BACHILLERATO]", bachillerato)
						//consulta=consulta.replace("[WHERE]", where);
						
						break;
					}
				}
				switch(object.orderby) {
					case "NOMBRE":
					orderby+="sda.primernombre";
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
					orderby+="gestionescolar.DESCRIPCION"
					break;
					case "INGRESO":
					orderby+="periodo.DESCRIPCION"
					break;
					case "ESTADO":
					orderby +="estado.DESCRIPCION";
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
					case "LISTAROJA":
					orderby+="da.observacionesListaRoja";
					break;
					case "RECHAZO":
					orderby+="da.observacionesRechazo";
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
				
				//pstm = con.prepareStatement(consulta.replace("SELECT sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.DESCRIPCION AS licenciatura, periodo.DESCRIPCION AS ingreso, estado.DESCRIPCION AS estado, prepa.DESCRIPCION AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, le.descripcion, ciudadestado.descripcion, ciudadpais.descripcion, estadoexamen.descripcion, pais.descripcion FROM SOLICITUDDEADMISION sda ", "SELECT COUNT(sda.persistenceid) as registros FROM SOLICITUDDEADMISION sda ").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("GROUP BY sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion, campus.descripcion, gestionescolar.DESCRIPCION, periodo.DESCRIPCION, estado.DESCRIPCION, prepa.DESCRIPCION, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, le.descripcion, ciudadestado.descripcion, ciudadpais.descripcion, estadoexamen.descripcion, pais.descripcion", "GROUP BY sda.persistenceid"))
				//errorlog+=consulta
				String consultaCount = Statements.GET_COUNT_SOLICITUDES_TRASNFERENCIA;
				consultaCount=consultaCount.replace("[CAMPUS]", campus)
				consultaCount=consultaCount.replace("[PROGRAMA]", programa)
				consultaCount=consultaCount.replace("[INGRESO]", ingreso)
				consultaCount=consultaCount.replace("[ESTADO]", estado)
				consultaCount=consultaCount.replace("[BACHILLERATO]", bachillerato)
				consultaCount=consultaCount.replace("[TIPOALUMNO]", tipoalumno)
				consultaCount=consultaCount.replace("[WHERE]", where)
				consultaCount=consultaCount.replace("[LIMITOFFSET]","");
				consultaCount=consultaCount.replace("[ORDERBY]", "");
//				errorlog+=" AQUI ES LA CONSULTA PARA CONTAR "+consultaCount
				pstm = con.prepareStatement(consultaCount);
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+=" AQUI VOY A REALIZAR LA CONSULTA "+consulta
				errorlog+=" LIMIT "+object.limit + " OFFSET " +object.offset
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
							String encoded = "";
							try {
								for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
									encoded = "../API/formsDocumentImage?document="+doc.getId();
									columns.put("fotografiab64", encoded);
								}
							}catch(Exception e) {
								columns.put("fotografiab64", "");
								errorlog+= ""+e.getMessage();
							}
						}
					}
	
					rows.add(columns);
				}
				resultado.setSuccess(true)
				
				resultado.setError_info(errorlog);
				//resultado.setError(consulta);
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setError_info(errorlog)
			//resultado.setError_info(consulta)
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result transferirAspirante(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		try {
			String username = "";
			String password = "";
			Properties prop = new Properties();
			String propFileName = "configuration.properties";
			InputStream inputStream;
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			username = prop.getProperty("USERNAME");
			password = prop.getProperty("PASSWORD");
			/*def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);*/
			LOGGER.error "def object = jsonSlurper.parseText(jsonData);";
			errorLog = errorLog + "";
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
			LOGGER.error "apiClient.login";
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID, "caseid");
			LOGGER.error "searchBuilder.filter(HumanTaskInstanceSearchDescriptor.NAME";
		}catch(Exception ex){
			
		}
		
		return resultado;
	}
	
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
	}
}
