package com.anahuac.rest.api.DAO
import java.nio.charset.StandardCharsets
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.DateFormat
import java.time.LocalDate

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utilities.FileDownload
import com.anahuac.rest.api.Utilities.LoadParametros
import com.bonitasoft.engine.api.APIClient
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class ResultadosAdmisionDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogosDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		return retorno
	}
	
	/*
	 * Servicio apara subida del archivo Excel de resultados
	 * */
	public Result obtieneDatosDelB64(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			String excelB64 = object.fileBase64;
			byte[] decodedExcel = Base64.getDecoder().decode(excelB64.getBytes(StandardCharsets.UTF_8));
			InputStream fis = new ByteArrayInputStream(decodedExcel);//Para manejar el archivo
			List<LinkedHashMap<String, String>> valoresExcel = new ArrayList<>();
			XSSFWorkbook workbook = new XSSFWorkbook(fis);//Manejo del Excel
			XSSFSheet sheet = workbook.getSheetAt(0);//Hoja
			Iterator<Row> rowIterator = sheet.iterator();//Para iterar las filas
			Row row;
			List<String> titulos = new ArrayList<>();//Para el nombre de la columna en el objeto
			int rowCount = 0;
			int maxCellCount = 0;
			
			//Iterando las filas usando un iterator
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				LinkedHashMap<String, String> objetoRow = new LinkedHashMap<>();
				objetoRow.put("filaExcel", row.getRowNum());
//				Iterator<Cell> cellIterator = row.cellIterator();
				Cell celda;
				int cellIndex = 0;
				DataFormatter dataFormatter = new DataFormatter();
				if(dataFormatter.formatCellValue(row.getCell(0)).equals("") || dataFormatter.formatCellValue(row.getCell(0)) == null) {
					break;	
				}
				//Iterando las columnas con un for por que iterator presentaba datos incompletos
				for(int i = 0; i < 54; i++){
					//Se obtiene el valor de la celda en específico
					celda = row.getCell(i);
					if (row.getRowNum() == 0) {
						titulos.add(celda.getStringCellValue().toLowerCase().trim());
					}

					int numCelda = 0;
					numCelda = i;
					
					//Obtener el título
					String tituloActual = "";
					if (numCelda < titulos.size()) {
						tituloActual = titulos.get(numCelda).toString().toLowerCase();
						if (tituloActual == null) {
							tituloActual = "";
						}
					}
					
					tituloActual = tituloActual.trim();
					if(tituloActual.equals("iversidad")) {
						tituloActual = "universidad"
					}
					
					//Se quitan los acentos, espacios y ñ
					tituloActual = tituloActual.replace('á', 'a');
					tituloActual = tituloActual.replace('é', 'e');
					tituloActual = tituloActual.replace('í', 'i');
					tituloActual = tituloActual.replace('ó', 'o');
					tituloActual = tituloActual.replace('ú', 'u');
					tituloActual = tituloActual.replace('ñ', 'n');
					tituloActual = tituloActual.replace(' ', '');
					
					String value = dataFormatter.formatCellValue(celda);
					
					//Validar valor de campos específicos
					if(tituloActual.toLowerCase().trim().equals("numerodematricula")) {
						value = "00" + value;
					} else if (tituloActual.toLowerCase().trim().equals("carta")) {
						if(value.equals("A")) {
							value = "Aceptado";
						} else if(value.equals("N")) {
							value = "Rechazado";
						} else if(value.equals("R")) {
							value = "Enviado a revisión";
						}
					} else if (tituloActual.toLowerCase().trim().equals("statuspdu")) {
						if(value.equals("1")) {
							value = "Hace PDU";
						} else if(value.equals("0")) {
							value = "No hace PDU";
						}
					}
					
					//Poner el dato con su clave dentro del objeto
					objetoRow.put(tituloActual.toLowerCase().trim(), value);
				}
				
				if(rowCount  > 0) {
					valoresExcel.add(objetoRow);
				}
				
				rowCount ++;
			}
			
			workbook.close();
			fis.close();
			resultado.setData(valoresExcel);
			resultado.setSuccess(true);
		} catch (IOException ioEx) {
			  ioEx.printStackTrace();
			  resultado.setSuccess(false);
			  resultado.setError(ioEx.getMessage());
		} catch (IllegalArgumentException ilegalExc) {
			resultado.setSuccess(false);
			resultado.setError(ilegalExc.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(ex.getMessage());
		}
		
		return resultado;
	}
	
	/*
	 * Servicio para limpiar la tabla de cartas temporales
	 * */
	public Result clearInfoCartaTemporal() {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog="", campus = "";
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			String consulta = Statements.CLEAR_INFO_CARTA_TEMPORAL;
			pstm = con.prepareStatement(consulta);
			pstm.executeUpdate();
			
			resultado.setSuccess(true);
			
			
		} catch (Exception e) {
			
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
	
	public Result selectInfoCartaTemporal(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = "";
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
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
			where+=" WHERE INFTEMP.persistenceid IS NOT null AND SOLAD.estatussolicitud = 'Carga y consulta de resultados' ";
//			where+=" WHERE INFTEMP.persistenceid IS NOT null AND SOLAD.estatussolicitud = 'Carga y consulta de resultados' ";
//			if(object.campus != null){
//				if(!object.campus.equals("")) {
//					where+=" AND LOWER(universidad) = LOWER('" + object.campus + "') ";
//				}
//			}
//			
//			if(object.campus != null){
//				if(!object.campus.equals("Todos los campus")) {
//					where+=" AND LOWER(universidad) = LOWER('" + object.campus + "') ";
//				}
//			}
//			
//			if(lstGrupo.size() > 0) {
//				campus+=" AND ("
//			}
//			
//			for(Integer i=0; i<lstGrupo.size(); i++) {
//				String campusMiembro=lstGrupo.get(i);
//				campus += "universidad='" + campusMiembro + "'"
//				if(i == (lstGrupo.size() - 1)) {
//					campus += ") "
//				} else {
//					campus += " OR "
//				}
//			}
			
			if(object.campus != null){
				if(object.campus.equals("Todos los campus")) {
					if(lstGrupo.size() > 0) {
						campus+=" AND ("
					}
					
					for(Integer i=0; i < lstGrupo.size(); i++) {
						String campusMiembro = lstGrupo.get(i);
						campus += "universidad='" + campusMiembro + "'";
						if(i == (lstGrupo.size() - 1)) {
							campus += ") ";
						} else {
							campus += " OR ";
						}
					}
					
//					where+=" AND LOWER(universidad) = LOWER('" + object.campus + "') ";
				} else {
					campus = "";
				}
			}
			
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			
			String consulta = Statements.GET_INFO_CARTA_TEMPORAL;
			
			errorlog += " | | " + where;
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				errorlog+=", columna "+ filtro.get("columna")
				switch(filtro.get("columna")) {
					case "ID":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(ID) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "IDBANNER,NOMBRE,EMAIL":
						if(where.contains("WHERE")) {
							where += " AND ";
						}else {
							where += " WHERE ";
						}
						where +=" ( LOWER(DETSOL.IDBANNER) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(INFTEMP.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(SOLAD.CORREOELECTRONICO) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "CAMPUS,LICENCIATURA":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(INFTEMP.UNIVERSIDAD) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(INFTEMP.CARRERA) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "CAMPUS":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						
						where +=" ( LOWER(INFTEMP.UNIVERSIDAD) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "PDU":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(PDU) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					/*====================================================================*/
				}
			}
				
			switch(object.orderby) {
				case "ID":
					orderby += "SOLAD.PERSISTENCEID";
				break;
				case "IDBANNER":
					orderby += "DETSOL.IDBANNER";
				break;
				case "NOMBRE":
					orderby += "INFTEMP.NOMBRE";
				break;
				case "EMAIL":
					orderby += "SOLAD.CORREOELECTRONICO";
				break;
				case "CAMPUS":
					orderby += "INFTEMP.UNIVERSIDAD";
				break;
				case "LICENCIATURA":
					orderby += "INFTEMP.CARRERA";
				break;
				case "PDU":
					orderby += "INFTEMP.PDU";
				break;
				case "MATE":
					orderby += "INFTEMP.SIHACEONOMATEMATICAS";
				break;
				case "ESP":
					orderby += "INFTEMP.ESPANOL";
				break;
				case "RESPUESTA":
					orderby += "INFTEMP.DESICIONDEADMISION";
				break;
				default:
					orderby += "INFTEMP.NOMBRE";
				break;
			}
			
			orderby += " " + object.orientation;
			
//			consulta = consulta.replace("[CAMPUS]", campus);
			where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno;
			
			errorlog += " | | " + where;
			consulta = consulta.replace("[WHERE]", where);
			
			String countQuery = Statements.GET_INFO_CARTA_TEMPORAL_COUNT;
			countQuery = countQuery.replace("[WHERE]", where);
			pstm = con.prepareStatement(countQuery);

			rs= pstm.executeQuery()
			if(rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"));
			}
			consulta = consulta.replace("[ORDERBY]", orderby);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery()
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
//			errorlog = consulta;
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("persistenceid")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					} else if (metaData.getColumnLabel(i).toLowerCase().equals("seleccionado")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getBoolean(i));
					} else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			
			
			resultado.setData(rows);
		} catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error e.getMessage();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result selectInfoCartaTemporalNoResultados(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = "";
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
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
			where+=" WHERE DETSOL.IDBANNER IS NULL OR SOLAD.estatussolicitud <> 'Carga y consulta de resultados' ";
			if(object.campus != null){
				if(object.campus.equals("Todos los campus")) {
					if(lstGrupo.size() > 0) {
						campus+=" AND ("
					}
					
					for(Integer i=0; i < lstGrupo.size(); i++) {
						String campusMiembro = lstGrupo.get(i);
						campus += "universidad='" + campusMiembro + "'";
						if(i == (lstGrupo.size() - 1)) {
							campus += ") ";
						} else {
							campus += " OR ";
						}
					}
				} else {
					campus = "";
				}
			}
			
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			
			String consulta = Statements.GET_INFO_CARTA_TEMPORAL_NO_RESULTADOS;
			
			errorlog += " | | " + where;
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				errorlog+=", columna "+ filtro.get("columna")
				switch(filtro.get("columna")) {
					case "ID":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(ID) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "IDBANNER,NOMBRE,EMAIL":
						if(where.contains("WHERE")) {
							where += " AND ";
						}else {
							where += " WHERE ";
						}
						where +=" ( LOWER(DETSOL.IDBANNER) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(INFTEMP.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(SOLAD.CORREOELECTRONICO) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "CAMPUS,LICENCIATURA":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(INFTEMP.UNIVERSIDAD) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(INFTEMP.CARRERA) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "CAMPUS":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						
						where +=" ( LOWER(INFTEMP.UNIVERSIDAD) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "PDU":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(PDU) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					/*====================================================================*/
				}
			}
				
			switch(object.orderby) {
				case "ID":
					orderby += "SOLAD.PERSISTENCEID";
				break;
				case "IDBANNER":
					orderby += "DETSOL.IDBANNER";
				break;
				case "NOMBRE":
					orderby += "INFTEMP.NOMBRE";
				break;
				case "EMAIL":
					orderby += "SOLAD.CORREOELECTRONICO";
				break;
				case "CAMPUS":
					orderby += "INFTEMP.UNIVERSIDAD";
				break;
				case "LICENCIATURA":
					orderby += "INFTEMP.CARRERA";
				break;
				case "PDU":
					orderby += "INFTEMP.PDU";
				break;
				case "MATE":
					orderby += "INFTEMP.SIHACEONOMATEMATICAS";
				break;
				case "ESP":
					orderby += "INFTEMP.ESPANOL";
				break;
				case "RESPUESTA":
					orderby += "INFTEMP.DESICIONDEADMISION";
				break;
				default:
					orderby += "INFTEMP.NOMBRE";
				break;
			}
			
			orderby += " " + object.orientation;
			
//			consulta = consulta.replace("[CAMPUS]", campus);
			where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno;
			
			errorlog += " | | " + where;
			consulta = consulta.replace("[WHERE]", where);
			
			String countQuery = Statements.GET_INFO_CARTA_TEMPORAL_COUNT_NO_RESULTADOS;
			countQuery = countQuery.replace("[WHERE]", where);
			pstm = con.prepareStatement(countQuery);

			rs= pstm.executeQuery()
			if(rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"));
			}
			consulta = consulta.replace("[ORDERBY]", orderby);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery()
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
//			errorlog = consulta;
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("persistenceid")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					} else if (metaData.getColumnLabel(i).toLowerCase().equals("seleccionado")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getBoolean(i));
					} else if (metaData.getColumnLabel(i).toLowerCase().equals("idnocoincide")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getBoolean(i));
					} else if (metaData.getColumnLabel(i).toLowerCase().equals("estatusincorrecto")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getBoolean(i));
					} else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			
			
			resultado.setData(rows);
		} catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error e.getMessage();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result selectConsultaDeResultados(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = "";
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
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
			String consulta = "";
			 
			if(object.tipoResultado.equals("Sin resultado")){
				consulta =  Statements.GET_INFO_CONSULTA_SIN_RESULTADOS;
				where+="WHERE SOLAD.ESTATUSSOLICITUD='Carga y consulta de resultados'";
			}else{
				consulta = Statements.GET_INFO_CONSULTA_RESULTADOS;
				where+=" WHERE INFTEMP.persistenceid IS NOT null";
				if(object.tipoResultado.equals("Aceptado")) {
					where+="  AND (carta='Aceptado') ";
				}else if(object.tipoResultado.equals("Rechazada")){
					where+="  AND (carta='Rechazado') ";
				}
			}
			errorlog=errorlog+" | INICIO";
			//where+=" WHERE INFTEMP.persistenceid IS NOT null";
			if(object.campus != null){
				if(!object.campus.equals("Todos los campus")) {
					where+=" AND LOWER(campus.descripcion) = LOWER('" + object.campus + "') ";
				}

			}
			
			if(lstGrupo.size()>0) {
				campus+=" AND ("
			}
			
			for(Integer i=0; i<lstGrupo.size(); i++) {
				String campusMiembro=lstGrupo.get(i);
				//campus += "universidad='" + campusMiembro + "'"
				campus += "campus.descripcion='" + campusMiembro + "'"
				if(i == (lstGrupo.size() - 1)) {
					campus += ") "
				} else {
					campus += " OR "
				}
			}
			

			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs= pstm.executeQuery();
			if(rs.next()) {
				SSA = rs.getString("valor")
			}
			//String consulta = Statements.GET_INFO_CONSULTA_RESULTADOS;

			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				errorlog=errorlog+" | =======================================================================";
				errorlog=errorlog+" | COLUMNA FILTRO"+ filtro.get("columna");
				switch(filtro.get("columna")) {
					case "IDBANNER":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(DETSOL.IDBANNER) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
						errorlog=errorlog+" | WHERE: " + where;
					break;
					case "NOMBRE,EMAIL,CURP":
					
						if(where.contains("WHERE")) {
							where += " AND ";
						}else {
							where += " WHERE ";
						}
						where +=" ( LOWER(concat(SOLAD.apellidopaterno,' ',SOLAD.apellidomaterno,' ',SOLAD.primernombre,' ', SOLAD.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(SOLAD.CORREOELECTRONICO) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(SOLAD.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"));
						errorlog=errorlog+" | WHERE: " + where;
					break;
					case "CAMPUS,PROGRAMA,PERÍODO":
					errorlog="5";
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));

						where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
						errorlog=errorlog+" | WHERE: " + where;
					break;
					
					case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN SOLAD.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(CASE WHEN prepa.DESCRIPCION = 'Otro' THEN SOLAD.bachillerato ELSE prepa.DESCRIPCION END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));

						where +=" OR LOWER(SOLAD.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
						errorlog=errorlog+" | WHERE: " + where;
					break;

					case "RESIDENCIA,TIPO DE ADMISIÓN,TIPO DE ALUMNO":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(R.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(TA.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));

						where +=" OR LOWER(TAL.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
						errorlog=errorlog+" | WHERE: " + where;
					break;

					case "FECHA DE ENVÍO":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(to_char( TO_TIMESTAMP(SOLAD.fechasolicitudenviada, 'YYYY-MM-DDTHH:MI'), 'DD/MM/YYYY') ) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
						errorlog=errorlog+" | WHERE: " + where;
					break;
					case "CIUDAD":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(SOLAD.ciudad) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
						errorlog=errorlog+" | WHERE: " + where;
					break;
					case "PAÍS":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(paisvives.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
						errorlog=errorlog+" | WHERE: " + where;
					break;
					case "PROCEDENCIA":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN SOLAD.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
						errorlog=errorlog+" | WHERE: " + where;
					break;
					case "CAMPUS INGRESO":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
						errorlog=errorlog+" | WHERE: " + where;
					break;
					case "PERÍODO":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
						errorlog=errorlog+" | WHERE: " + where;
					break;
					/*====================================================================*/
				}
			}
			errorlog=errorlog+" | FIN FOR======================================";
			
			switch(object.orderby) {
				case "IDBANNER":
					orderby += "DETSOL.IDBANNER";
				break;
				case "NOMBRE":
				orderby+="SOLAD.apellidopaterno";
				//orderby += "INFTEMP.NOMBRE";
				break;
				case "EMAIL":
					orderby += "SOLAD.CORREOELECTRONICO";
				break;
				case "CURP":
				orderby += "SOLAD.curp";
				break;
				case "CAMPUS":
					orderby += "campusEstudio.descripcion";
				break;
				case "PROGRAMA":
					orderby += "gestionescolar.NOMBRE";
				break;
				case "INGRESO":
					orderby += "periodo.DESCRIPCION";
				break;
				case "PROCEDENCIA":
					orderby += "procedencia";
				break;
				case "PREPARATORIA":
					orderby += "preparatoria";
				break;
				case "PROMEDIO":
					orderby += "SOLAD.PROMEDIOGENERAL";
				break;
				case "RESIDEICA":
				orderby += "R.descripcion";
				break;
				case "TIPODEADMISION":
				orderby += "TA.descripcion";
				break;
				case "TIPODEALUMNO":
				orderby += "TAL.descripcion";
				break;
				case "RESULTADOADMISION":
				if(object.tipoResultado.equals("Sin resultado")) {
					orderby += "SOLAD.ESTATUSSOLICITUD";
				}else{
					orderby += "INFTEMP.carta";
				}
				//orderby += "INFTEMP.carta";
				break;
				case "FECHAENVIO":
				orderby += "fechaSolicitudEnviadaFormato";
				break;
				case "NOMBRESESION":
				orderby += "DETSOL.IDBANNER";
				break;
				case "FECHASESION":
				orderby += "DETSOL.IDBANNERE";
				break;
				default:
					orderby += "DETSOL.IDBANNER";
				break;
			}

			
			orderby += " " + object.orientation;
			
			LOGGER.error "orderby : : " + orderby;
			
			consulta = consulta.replace("[CAMPUS]", campus);
			where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno;
			consulta = consulta.replace("[WHERE]", where);
			//String countQuery = Statements.GET_INFO_CONSULTA_RESULTADOS_COUNT;
			String countQuery = "";
			
			if(object.tipoResultado.equals("Sin resultado")){
				countQuery = Statements.GET_INFO_CONSULTA_SIN_RESULTADOS_COUNT;
			}else{
				countQuery = Statements.GET_INFO_CONSULTA_RESULTADOS_COUNT; 
			}
			 
			countQuery = countQuery.replace("[WHERE]", where); 
			errorlog=errorlog+" | countQuery: " + countQuery;
			pstm = con.prepareStatement(countQuery); 
			rs= pstm.executeQuery();
			
			if(rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"));
			}
			
			consulta = consulta.replace("[ORDERBY]", orderby);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			errorlog=errorlog+" | consulta: " + consulta;
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery()
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			errorlog = consulta; 
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("persistenceid")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					} else if (metaData.getColumnLabel(i).toLowerCase().equals("seleccionado")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getBoolean(i));
					}else if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
							String encoded = "";
							try {
								String urlFoto = rs.getString("urlfoto");
								if(urlFoto != null && !urlFoto.isEmpty()) {
									columns.put("fotografiab64", rs.getString("urlfoto") + SSA);
									//columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
								}else {
									List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
									for(Document doc : doc1) {
										encoded = "../API/formsDocumentImage?document="+doc.getId();
										columns.put("fotografiab64", encoded);
									}
								}
							}catch(Exception e) {
								columns.put("fotografiab64", "");
								errorlog+= ""+e.getMessage();
							}
						} else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
				}
	
				rows.add(columns);
			}
			resultado.setSuccess(true);
			
			
			resultado.setData(rows);
			
		} catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error e.getMessage();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result seleccionarCarta(Integer parameterP, Integer parameterC, String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog="", campus = "";
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			Boolean todos = object.todos;
			Boolean seleccionado = object.seleccionado;
			String persistenceid = object.persistenceid;
			String consulta = "";
			
			if(todos) {
				consulta = Statements.SELECCIONAR_TODAS_CARTAS;
				pstm = con.prepareStatement(consulta);
				pstm.setBoolean(1, seleccionado);
			} else {
				consulta = Statements.SELECCIONAR_CARTA;
				pstm = con.prepareStatement(consulta);
				pstm.setBoolean(1, seleccionado);
				pstm.setLong(2, Long.valueOf(persistenceid));
			} 
			
			pstm.executeUpdate();
			
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			
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
	
	public Result enviarCartas(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<?> lstData = new ArrayList<?>();
		List<?> lstEnviados = new ArrayList<?>();
		List<?> lstNoEnviados = new ArrayList<?>();
		String errorlog = "";
		try {
			closeCon = validarConexion();
			NotificacionDAO nDAO = new NotificacionDAO();
			String consulta = "";
			consulta = Statements.GET_CARTAS_A_ENVIAR;
			String where = " WHERE INFTEMP.SELECCIONADO = true  AND SOLAD.estatussolicitud = 'Carga y consulta de resultados' ";
			consulta = consulta.replace("[WHERE]", where);
			pstm = con.prepareStatement(consulta);
			
			rs = pstm.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while(rs.next()) {
				String correo = rs.getString("email");
				String carta = rs.getString("carta");
				String campusCorreo = rs.getString("grupobonita");
				Boolean isMedicina = rs.getBoolean("ismedicina");
				String codigo = "";
				
				if(carta.equals("Aceptado") && !isMedicina) {
					codigo = "carta-aceptar";
				} else if(carta.equals("Aceptado") && isMedicina) {
					codigo = "carta-propedeutico";
				} else if (carta.equals("Rechazado")) {
					codigo = "carta-rechazo";
				} else {
					codigo = "carta-rechazo";
				}
				
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				
				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("persistenceid")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					} else if (metaData.getColumnLabel(i).toLowerCase().equals("seleccionado")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getBoolean(i));
					} else if (metaData.getColumnLabel(i).toLowerCase().equals("ismedicina")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getBoolean(i));
					} else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
				}
				
				LocalDate local = LocalDate.now(); 
				columns.put("fechaEnvio", local);
				String jsonCorreo = '{"campus":"' + campusCorreo + '","correo":"' + correo +  '","codigo":"' + codigo + '","isEnviar":true}"';
				//se envia al principio
				Result resultadoEnvioCarta = nDAO.generateHtml(parameterP, parameterC, jsonCorreo, context);
				Map<String, Object> contract = new LinkedHashMap<String, Object>();
				contract.put("isTransferencia", false);
				contract.put("infoCartaInput", columns);
				
				String correoLog = "";
				Boolean isError = false;
				String errorInfo = "";
				
				if(resultadoEnvioCarta.isSuccess()) {
					correoLog += "Se envió la carta.";
					if(!codigo.equals("carta-propedeutico") && !codigo.equals("carta-rechazo")) {
						if(!columns.get("pdu").equals("No") && carta.equals("Aceptado")) {
							String codigoPDU = "carta-pdu";
							String jsonCorreoPDU = '{"campus":"' + campusCorreo + '","correo":"' + correo +  '","codigo":"' + codigoPDU + '","isEnviar":true}"';
							Result resultadoEnvioPDU = nDAO.generateHtml(parameterP, parameterC, jsonCorreoPDU, context);
							
							if(resultadoEnvioPDU.isSuccess()) {
								correoLog += " Se envió el correo del PDU.";
							} else {
								isError = true;
								errorInfo = resultadoEnvioPDU.getError();
							}
							
						} else {
							correoLog += " No requiere PDU.";
						}
					}
				} else {
//					throw new Exception(resultadoEnvioCarta.getError());
					correoLog += "No se pudo enviar la carta";
					isError = true;
					errorInfo = resultadoEnvioCarta.getError();
				}
				
				if(!isError) {
					Result resultadoEjecucionTarea = ejecutarCargaResultado(columns.get("numerodematricula"), contract, context);
					
					if(resultadoEjecucionTarea.isSuccess()) {
						correoLog += " Tarea ejecutada."
					} else {
						isError = true;
						errorInfo = resultadoEjecucionTarea.getError();
					}
				}
				
				if(isError) {
					Map<String, Object> correoError = new LinkedHashMap<String, Object>();
					correoError.put("correo", correo);
					correoError.put("correoLog", errorInfo);
					correoError.put("error", true);
					lstNoEnviados.add(correoError);
				} else {
					Map<String, Object> correoEnviado = new LinkedHashMap<String, Object>();
					correoEnviado.put("correo", correo);
					correoEnviado.put("correoLog", correoLog);
					correoEnviado.put("error", false);
					lstEnviados.add(correoEnviado);
				}
			}
			
			lstData.add(lstEnviados);
			lstData.add(lstNoEnviados);
			
			resultado.setData(lstData);
			resultado.setSuccess(true);
			
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
	
	public Result ejecutarCargaResultado(String idBanner, Map<String, Serializable> contract, RestAPIContext context) {
		Result resultado = new Result();
		List<DetalleSolicitud> lstResultado = new ArrayList<DetalleSolicitud>();
		Boolean closeCon = false;
		try {
			String username = "";
			String password = "";
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			def objDetalleSolicitudDAO = context.getApiClient().getDAO(DetalleSolicitudDAO.class);
			List<DetalleSolicitud> detalleSolicitud = objDetalleSolicitudDAO.findByIdBanner(idBanner, 0, 1);
			apiClient.login(username, password);
			String caseId = detalleSolicitud.get(0).caseId;
			def startedBy = apiClient.getProcessAPI().getProcessInstance(Integer.parseInt(caseId)).startedBy;
			Long taskID = apiClient.processAPI.getHumanTaskInstances(Long.valueOf(caseId), "Carga y consulta de resultados", 0, 1).get(0).getId();
			apiClient.processAPI.assignAndExecuteUserTask(startedBy, taskID, contract);
			resultado.setSuccess(true);
		}catch(Exception ex) {
			LOGGER.error ex.getMessage()
			resultado.setSuccess(false)
			resultado.setError(ex.getMessage())
		}
		return resultado;
	}
	
	public Result selectConsultaDeResultadosManual(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = "";
        List<String> lstGrupo = new ArrayList<String>();
        List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
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
            String consulta = "";
             
                 if(object.tipoResultado.equals("Sin resultado")){
                     consulta =  Statements.GET_INFO_CONSULTA_SIN_RESULTADOS;
                     where+="WHERE SOLAD.ESTATUSSOLICITUD='Carga y consulta de resultados'";
                 }else{
                      consulta = Statements.GET_INFO_CONSULTA_RESULTADOS_MANUAL;
                     if(object.tipoResultado.equals("AceptadoManual")) {
                        where+=" WHERE SOLAD.aceptado = true ";
                     }else if(object.tipoResultado.equals("Rechazada")){
                        where+="  AND (carta='Rechazado') ";
                     }
                 }
             
            
            if(object.campus != null){
                if(!object.campus.equals("Todos los campus")) {
                    where+=" AND LOWER(campus.descripcion) = LOWER('" + object.campus + "') ";
                }

            }
            
            if(lstGrupo.size()>0) {
                campus+=" AND ("
            }
            
            for(Integer i=0; i<lstGrupo.size(); i++) {
                String campusMiembro=lstGrupo.get(i);
                //campus += "universidad='" + campusMiembro + "'"
                campus += "campus.descripcion='" + campusMiembro + "'"
                if(i == (lstGrupo.size() - 1)) {
                    campus += ") "
                } else {
                    campus += " OR "
                }
            }
            

            List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
            closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs= pstm.executeQuery();
			if(rs.next()) {
				SSA = rs.getString("valor")
			}
			
            //String consulta = Statements.GET_INFO_CONSULTA_RESULTADOS;

            
            for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
                switch(filtro.get("columna")) {
                    case "IDBANNER":
                        if(where.contains("WHERE")) {
                            where+= " AND ";
                        }else {
                            where+= " WHERE ";
                        }
                        where +=" ( LOWER(DETSOL.IDBANNER) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"));
                    break;
                    case "NOMBRE,EMAIL,CURP":
                    
                        if(where.contains("WHERE")) {
                            where += " AND ";
                        }else {
                            where += " WHERE ";
                        }
                        where +=" ( LOWER(concat(SOLAD.apellidopaterno,' ',SOLAD.apellidomaterno,' ',SOLAD.primernombre,' ', SOLAD.segundonombre)) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"));
                        
                        where +=" OR LOWER(SOLAD.CORREOELECTRONICO) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"));
                        
                        where +=" OR LOWER(SOLAD.curp) like lower('%[valor]%') ) ";
                        where = where.replace("[valor]", filtro.get("valor"));
                    break;
                    case "CAMPUS,PROGRAMA,PERÍODO":
                    errorlog="5";
                        if(where.contains("WHERE")) {
                            where+= " AND ";
                        }else {
                            where+= " WHERE ";
                        }
                        where +=" ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"));
                        
                        where +=" OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"));

                        where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"));
                    break;
                    
                    case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
                        if(where.contains("WHERE")) {
                            where+= " AND ";
                        }else {
                            where+= " WHERE ";
                        }
                        where +=" ( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN SOLAD.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"));
                        
                        where +=" OR LOWER(CASE WHEN prepa.DESCRIPCION = 'Otro' THEN SOLAD.bachillerato ELSE prepa.DESCRIPCION END) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"));

                        where +=" OR LOWER(SOLAD.PROMEDIOGENERAL) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"));
                    break;

                    case "RESIDENCIA,TIPO DE ADMISIÓN,TIPO DE ALUMNO":
                        if(where.contains("WHERE")) {
                            where+= " AND ";
                        }else {
                            where+= " WHERE ";
                        }
                        where +=" ( LOWER(R.descripcion) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"));
                        
                        where +=" OR LOWER(TA.descripcion) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"));

                        where +=" OR LOWER(TAL.descripcion) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"));
                    break;

                    case "FECHA DE ENVÍO":
                        if(where.contains("WHERE")) {
                            where+= " AND ";
                        }else {
                            where+= " WHERE ";
                        }
                        where +=" ( LOWER(to_char( TO_TIMESTAMP(SOLAD.fechasolicitudenviada, 'YYYY-MM-DDTHH:MI'), 'DD/MM/YYYY') ) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"));
                    break;
                    case "CIUDAD":
                    if(where.contains("WHERE")) {
                        where+= " AND ";
                    }else {
                        where+= " WHERE ";
                    }
                    where +=" ( LOWER(SOLAD.ciudad) like lower('%[valor]%') )";
                    where = where.replace("[valor]", filtro.get("valor"));
                    break;
                    case "PAÍS":
                    if(where.contains("WHERE")) {
                        where+= " AND ";
                    }else {
                        where+= " WHERE ";
                    }
                    where +=" ( LOWER(paisvives.descripcion) like lower('%[valor]%') )";
                    where = where.replace("[valor]", filtro.get("valor"));
                    break;
                    case "PROCEDENCIA":
                    if(where.contains("WHERE")) {
                        where+= " AND ";
                    }else {
                        where+= " WHERE ";
                    }
                    where +=" ( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN SOLAD.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') )";
                    where = where.replace("[valor]", filtro.get("valor"));
                    break;
                    case "CAMPUS INGRESO":
                    if(where.contains("WHERE")) {
                        where+= " AND ";
                    }else {
                        where+= " WHERE ";
                    }
                    where +=" ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
                    where = where.replace("[valor]", filtro.get("valor"));
                    break;
                    case "PERÍODO":
                    if(where.contains("WHERE")) {
                        where+= " AND ";
                    }else {
                        where+= " WHERE ";
                    }
                    where +=" ( LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
                    where = where.replace("[valor]", filtro.get("valor"));
                    break;
                    /*====================================================================*/
                }


            }
            
            switch(object.orderby) {
                case "IDBANNER":
                    orderby += "DETSOL.IDBANNER";
                break;
                case "NOMBRE":
                orderby+="SOLAD.apellidopaterno";
                break;
                case "EMAIL":
                    orderby += "SOLAD.CORREOELECTRONICO";
                break;
                case "CURP":
                orderby += "SOLAD.curp";
                break;
                case "CAMPUS":
                    orderby += "campusEstudio.descripcion";
                break;
                case "PROGRAMA":
                    orderby += "gestionescolar.NOMBRE";
                break;
                case "INGRESO":
                    orderby += "periodo.DESCRIPCION";
                break;
                case "PROCEDENCIA":
                    orderby += "procedencia";
                break;
                case "PREPARATORIA":
                    orderby += "preparatoria";
                break;
                case "PROMEDIO":
                    orderby += "SOLAD.PROMEDIOGENERAL";
                break;
                case "RESIDEICA":
                orderby += "R.descripcion";
                break;
                case "TIPODEADMISION":
                orderby += "TA.descripcion";
                break;
                case "TIPODEALUMNO":
                orderby += "TAL.descripcion";
                break;
                case "RESULTADOADMISION":
                orderby += "SOLAD.ESTATUSSOLICITUD";
                break;
                case "FECHAENVIO":
                orderby += "fechaSolicitudEnviadaFormato";
                break;
                case "NOMBRESESION":
                orderby += "DETSOL.IDBANNER";
                break;
                case "FECHASESION":
                orderby += "DETSOL.IDBANNERE";
                break;
                default:
                    orderby += "DETSOL.IDBANNER";
                break;
            }

            
            orderby += " " + object.orientation;
            
            LOGGER.error "orderby : : " + orderby;
            
            consulta = consulta.replace("[CAMPUS]", campus);
            where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno;

            consulta = consulta.replace("[WHERE]", where);
            //String countQuery = Statements.GET_INFO_CONSULTA_RESULTADOS_COUNT;
            String countQuery = "";
            
            if(object.tipoResultado.equals("Sin resultado")){
                countQuery = Statements.GET_INFO_CONSULTA_SIN_RESULTADOS_COUNT;
            }else{
                countQuery = Statements.GET_INFO_CONSULTA_RESULTADOS_COUNT_MANUAL;
            }
             
            countQuery = countQuery.replace("[WHERE]", where);
            pstm = con.prepareStatement(countQuery);
            rs= pstm.executeQuery();
            
            if(rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"));
            }
            
            consulta = consulta.replace("[ORDERBY]", orderby);
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");

            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit);
            pstm.setInt(2, object.offset);
            rs = pstm.executeQuery()
            rows = new ArrayList<Map<String, Object>>();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
			String urlFoto = "";
			
            while(rs.next()) {
                Map<String, Object> columns = new LinkedHashMap<String, Object>();
				
                for (int i = 1; i <= columnCount; i++) {
                    if(metaData.getColumnLabel(i).toLowerCase().equals("persistenceid")) {
                        columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
                    } else if (metaData.getColumnLabel(i).toLowerCase().equals("seleccionado")) {
                        columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getBoolean(i));
                    } else if(metaData.getColumnLabel(i).toLowerCase().equals("urlfoto")) {
						urlFoto = rs.getString(i);
						errorlog += " urlFoto " + urlFoto  
                	}else if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
                        columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
                            String encoded = "";
                            try {
								if(urlFoto != null && !urlFoto.isEmpty()) {
									columns.put("fotografiab64", rs.getString("urlfoto") + SSA );
									//columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
								}else {
	                                for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
	                                    encoded = "../API/formsDocumentImage?document="+doc.getId();
	                                    columns.put("fotografiab64", encoded);
	                                }
								}
                            }catch(Exception e) {
                                columns.put("fotografiab64", "");
                                errorlog+= ""+e.getMessage();
                            }
                        } else {
                        columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
                    }
                }
    
                rows.add(columns);
            }
            resultado.setSuccess(true);
            
            
            resultado.setData(rows);
            
        } catch (Exception e) {
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            LOGGER.error e.getMessage();
        }finally {
            if(closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }
	
	public String base64Imagen(String url)  throws Exception {
		String b64 = "";
		if(url.toLowerCase().contains(".jpeg")) {
				b64 = ( "data:image/jpeg;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".png")) {
				b64 = ( "data:image/png;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".jpg")) {
				b64 = ( "data:image/jpg;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".jfif")) {
				b64 = ( "data:image/jfif;base64, "+(new FileDownload().b64Url(url)));
			}
		return  b64
	}
}