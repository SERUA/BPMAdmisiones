package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.text.DateFormat
import java.text.SimpleDateFormat

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.bonitasoft.web.extension.rest.RestAPIContext

import com.anahuac.catalogos.CatDocumentosTextos
import com.anahuac.posgrados.catalog.PSGRCatDocumentosTextos
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result

import groovy.json.JsonSlurper

class DocumentosTextosDAO {
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	public static final String GET="SELECT * FROM CATDOCUMENTOSTEXTOS WHERE campus_pid=?"
	public static final String INSERT="INSERT INTO CATDOCUMENTOSTEXTOS  (NOSABES ,TIPSCB ,URLGUIAEXAMENCB , URLTESTVOCACIONAL ,persistenceid,persistenceversion, actividadingreso1, actividadingreso2, cancelarsegurogastosmedicos, ciudadcarta, correodirectoradmisiones, costosgm, cursomatematicas1, cursomatematicas2, directoradmisiones, documentosentregar, documentosentregarextranjero, educaciongarantizada, estadocarta, instruccionespagobanco, notasdocumentos, parrafoespanol1, parrafoespanol2, parrafoespanol3, parrafomatematicas1, parrafomatematicas2, parrafomatematicas3, telefonodirectoradmisiones, titulodirectoradmisiones,instruccionespagocaja, campus_pid) values (?,?,?,?,case when (SELECT max(persistenceId)+1 from CATDOCUMENTOSTEXTOS  ) is null then 1 else (SELECT max(persistenceId)+1 from CATDOCUMENTOSTEXTOS) end,0,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?);"
	public static final String UPDATE="UPDATE CATDOCUMENTOSTEXTOS SET NOSABES=?, TIPSCB=?, URLGUIAEXAMENCB=?, URLTESTVOCACIONAL=?,actividadingreso1=?, actividadingreso2=?, cancelarsegurogastosmedicos=?, ciudadcarta=?, correodirectoradmisiones=?, costosgm=?, cursomatematicas1=?, cursomatematicas2=?, directoradmisiones=?, documentosentregar=?, documentosentregarextranjero=?, educaciongarantizada=?, estadocarta=?, instruccionespagobanco=?, notasdocumentos=?, parrafoespanol1=?, parrafoespanol2=?, parrafoespanol3=?, parrafomatematicas1=?, parrafomatematicas2=?, parrafomatematicas3=?, telefonodirectoradmisiones=?, titulodirectoradmisiones=?, instruccionespagocaja=? where campus_pid=?;"
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		return retorno
	}
	public Result getDocumentosTextos(Long campus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {

			CatDocumentosTextos row = new CatDocumentosTextos()
			List<CatDocumentosTextos> rows = new ArrayList<CatDocumentosTextos>();
			closeCon = validarConexion();
			String consulta = GET
			pstm = con.prepareStatement(consulta)
			pstm.setLong(1, campus_pid)
			rs = pstm.executeQuery()
				while(rs.next()) {
					row = new CatDocumentosTextos()
					row.setNoSabes(rs.getString("noSabes"))
					row.setTipsCB(rs.getString("tipsCB"))
					row.setUrlGuiaExamenCB(rs.getString("urlGuiaExamenCB"))
					row.setUrlTestVocacional(rs.getString("urlTestVocacional"))
					row.setCiudadCarta(rs.getString("ciudadCarta"))
					row.setEstadoCarta(rs.getString("estadoCarta"))
					row.setDocumentosEntregar(rs.getString("documentosEntregar"))
					row.setDocumentosEntregarExtranjero(rs.getString("documentosEntregarExtranjero"))
					row.setNotasDocumentos(rs.getString("notasDocumentos"))
					row.setParrafoMatematicas1(rs.getString("parrafoMatematicas1"))
					row.setParrafoMatematicas2(rs.getString("parrafoMatematicas2"))
					row.setParrafoMatematicas3(rs.getString("parrafoMatematicas3"))
					row.setParrafoEspanol1(rs.getString("parrafoEspanol1"))
					row.setParrafoEspanol2(rs.getString("parrafoEspanol2"))
					row.setParrafoEspanol3(rs.getString("parrafoEspanol3"))
					row.setDirectorAdmisiones(rs.getString("directorAdmisiones"))
					row.setTituloDirectorAdmisiones(rs.getString("tituloDirectorAdmisiones"))
					row.setCorreoDirectorAdmisiones(rs.getString("correoDirectorAdmisiones"))
					row.setTelefonoDirectorAdmisiones(rs.getString("telefonoDirectorAdmisiones"))
					row.setActividadIngreso1(rs.getString("actividadIngreso1"))
					row.setActividadIngreso2(rs.getString("actividadIngreso2"))
					row.setCostoSGM(rs.getString("costoSGM"))
					row.setEducacionGarantizada(rs.getString("educacionGarantizada"))
					row.setInstruccionesPagoBanco(rs.getString("instruccionesPagoBanco"))
					row.setCancelarSeguroGastosMedicos(rs.getString("cancelarSeguroGastosMedicos"))
					row.setCursoMatematicas1(rs.getString("cursoMatematicas1"))
					row.setCursoMatematicas2(rs.getString("cursoMatematicas2"))
					row.setInstruccionesPagoCaja(rs.getString("instruccionesPagoCaja"))
					row.setCampus_pid(rs.getLong("campus_pid"))
					rows.add(row)
				}
				resultado.setSuccess(true)
				resultado.setData(rows)
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
	public Result insertDocumentosTextos(CatDocumentosTextos row) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			List<CatDocumentosTextos> rows = new ArrayList<CatDocumentosTextos>();
			closeCon = validarConexion();
			String consulta = GET
			pstm = con.prepareStatement(consulta)
			pstm.setLong(1, row.getCampus_pid())
			rs = pstm.executeQuery()
					pstm = con.prepareStatement(rs.next()?UPDATE:INSERT)
					pstm.setString(1, row.getNoSabes())
					pstm.setString(2, row.getTipsCB())
					pstm.setString(3, row.getUrlGuiaExamenCB())
					pstm.setString(4, row.getUrlTestVocacional())
					pstm.setString(5,row.actividadIngreso1);//1
					pstm.setString(6,row.actividadIngreso2);//2
					pstm.setString(7,row.cancelarSeguroGastosMedicos);//3
					pstm.setString(8,row.ciudadCarta);//4
					pstm.setString(9,row.correoDirectorAdmisiones);//5
					pstm.setString(10,row.costoSGM);//6
					pstm.setString(11,row.cursoMatematicas1);//7
					pstm.setString(12,row.cursoMatematicas2);//8
					pstm.setString(13,row.directorAdmisiones);//9
					pstm.setString(14,row.documentosEntregar);//10
					pstm.setString(15,row.documentosEntregarExtranjero);//11
					pstm.setString(16,row.educacionGarantizada);//12
					pstm.setString(17,row.estadoCarta);//13
					pstm.setString(18,row.instruccionesPagoBanco);//14
					pstm.setString(19,row.notasDocumentos);//15
					pstm.setString(20,row.parrafoEspanol1);//16
					pstm.setString(21,row.parrafoEspanol2);//17
					pstm.setString(22,row.parrafoEspanol3);//18
					pstm.setString(23,row.parrafoMatematicas1);//19
					pstm.setString(24,row.parrafoMatematicas2);//20
					pstm.setString(25,row.parrafoMatematicas3);//21
					pstm.setString(26,row.telefonoDirectorAdmisiones);//22
					pstm.setString(27,row.tituloDirectorAdmisiones);//23
					pstm.setString(28,row.instruccionesPagoCaja);//23
					pstm.setLong(29, row.getCampus_pid())
					pstm.execute()
				resultado.setSuccess(true)
				resultado.setData(rows)
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
	
	public Result getExcelFileBandejaMaestra(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
	
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			Result dataResult = new SolicitudDeAdmisionDAO().selectSolicitudesAdmision(jsonData, context);
	
			int rowCount = 0;
			List<Object> lstParams;
			String type = "ReporteSesiones";
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
	
			Row titleRow = sheet.createRow(++rowCount);
			Cell cellReporteLabel = titleRow.createCell(0);
			cellReporteLabel.setCellValue("Reporte:");
			cellReporteLabel.setCellStyle(style);
			
			Cell cellReporteValor = titleRow.createCell(1);
			cellReporteValor.setCellValue("Bandeja Maestra Posgrados"); // Reemplaza esto con el nombre de tu reporte
			
			Cell cellUsuarioLabel = titleRow.createCell(2);
			cellUsuarioLabel.setCellValue("Usuario:");
			cellUsuarioLabel.setCellStyle(style);
			
			Cell cellUsuarioValor = titleRow.createCell(3);
			cellUsuarioValor.setCellValue(object.usuario);
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -7);
			Date date = cal.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String fechaActual = formatter.format(date);
			
			Cell cellFechaLabel = titleRow.createCell(4);
			cellFechaLabel.setCellValue("Fecha:");
			cellFechaLabel.setCellStyle(style);
			
			Cell cellFechaValor = titleRow.createCell(5);
			cellFechaValor.setCellValue(fechaActual);
			Row espacio = sheet.createRow(++rowCount);
			Row headersRow = sheet.createRow(++rowCount);
			Cell header1 = headersRow.createCell(0);
			header1.setCellValue("Foto");
			header1.setCellStyle(style);
			Cell header2 = headersRow.createCell(1);
			header2.setCellValue("Expediente");
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
			header9.setCellValue("Estatus");
			header9.setCellStyle(style);
			Cell header10 = headersRow.createCell(9);
			header10.setCellValue("Fecha de registro");
			header10.setCellStyle(style);
			Cell header11 = headersRow.createCell(10);
			header11.setCellValue("Última modificación");
			header11.setCellStyle(style);
	
			DateFormat dfSalida = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat dformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
			for (int i = 0; i < lstParams.size(); ++i) {
				Row row = sheet.createRow(++rowCount);
				Cell cell1 = row.createCell(0);
				cell1.setCellValue(lstParams.get(i).idbanner);
				Cell cell2 = row.createCell(1);
				cell2.setCellValue(lstParams.get(i).caseid);
				String nombre = lstParams.get(i).apellido_paterno + " " + lstParams.get(i).apellido_materno + " " + lstParams.get(i).nombre ;
				Cell cell3 = row.createCell(2);
				cell3.setCellValue(nombre);
				Cell cell4 = row.createCell(3);
				cell4.setCellValue(lstParams.get(i).correo_electronico);
				Cell cell5 = row.createCell(4);
				cell5.setCellValue(lstParams.get(i).curp);
				Cell cell6 = row.createCell(5);
				cell6.setCellValue(lstParams.get(i).posgrado);
				Cell cell7 = row.createCell(6);
				cell7.setCellValue(lstParams.get(i).periodo);
				Cell cell8 = row.createCell(7);
				cell8.setCellValue(lstParams.get(i).campus);
				Cell cell9 = row.createCell(8);
				cell9.setCellValue(lstParams.get(i).estatus_solicitud);
				Cell cell10 = row.createCell(9);
				cell10.setCellValue(lstParams.get(i).fecha_registro);
				Cell cell11 = row.createCell(10);
				cell11.setCellValue(lstParams.get(i).fecha_ultima_modificacion);
				Cell cell12 = row.createCell(11);
//				cell12.setCellValue(lstParams.get(i).estatussolicitud);
//				Cell cell13 = row.createCell(12);
//				cell13.setCellValue(lstParams.get(i).aceptado);
//				Cell cell14 = row.createCell(13);
//				cell14.setCellValue("Aspirante");
////				String estatus = lstParams.get(i).estatussolicitud.trim();
////	
////				if (estatus.equalsIgnoreCase("Esperando Pre-Autorización") || estatus.equalsIgnoreCase("En espera de resultado") || estatus.equalsIgnoreCase("Correcciones realizadas")) {
////					cell14.setCellValue("Pre-Autorización");
////				} else if (estatus.equalsIgnoreCase("Esperando revisión área artistica")) {
////					cell14.setCellValue("Pre-Autorización");
////				} else if (estatus.equalsIgnoreCase("Esperando revisión área deportiva")) {
////					cell14.setCellValue("Pre-Autorización");
////				} else if (estatus.equalsIgnoreCase("En espera de autorización")) {
////					cell14.setCellValue("Comité de becas");
////				} else if (estatus.equalsIgnoreCase("Solicitud Rechazada")) {
////					cell14.setCellValue("Archivo");
////				} else if (estatus.equalsIgnoreCase("Solicitud de financiamiento autorizada")) {
////					cell14.setCellValue(""); // Dejar en blanco
////				} else {
////					cell14.setCellValue("Aspirante");
////				}
//				
//				Cell cell15 = row.createCell(14);
//				cell15.setCellValue(lstParams.get(i).descripcion_bachillerato);
//				Cell cell16 = row.createCell(15);
//				cell16.setCellValue(lstParams.get(i).ciudad_bachillerato);
//				Cell cell17 = row.createCell(16);
//				cell17.setCellValue(lstParams.get(i).estado_bachillerato);
//				Cell cell18 = row.createCell(17);
//				cell18.setCellValue(lstParams.get(i).pais_bachillerato);
//	
//				String fechaRegistroString = lstParams.get(i).fecharegistro;
//	
//				if (fechaRegistroString != null) {
//					Date fechaRegistro = dfSalida.parse(fechaRegistroString);
//					String fechaFormateada = dformat.format(fechaRegistro);
//					Cell cell19 = row.createCell(18);
////					cell15.setCellValue(fechaFormateada);
//					cell19.setCellValue(fechaRegistroString);
//				} else {
//					Cell cell19 = row.createCell(18);
//					cell19.setCellValue("N/A");
//				}
//	
//				String fechaUltimaModificacionString = lstParams.get(i).fechaultimamodificacion;
//	
//				if (fechaUltimaModificacionString != null) {
//					Date fechaUltimaModificacion = dfSalida.parse(fechaUltimaModificacionString);
//					String fechaFormateada = dformat.format(fechaUltimaModificacion);
//					Cell cell20 = row.createCell(19);
////					cell16.setCellValue(fechaFormateada);
//					cell20.setCellValue(fechaUltimaModificacionString);
//				} else {
//					Cell cell20 = row.createCell(19);
//					cell20.setCellValue("N/A");
//				}
				
			}
	
			for (int i = 0; i <= rowCount + 11; ++i) {
				sheet.autoSizeColumn(i);
			}
	
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
//			errorLog +- "valor"+outputStream;
			List<Object> lstResultado = new ArrayList<Object>();
			lstResultado.add(Base64.getEncoder().encodeToString(outputStream.toByteArray()));
			resultado.setError_info(errorLog);
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
		} catch (Exception e) {
//			LOGGER.error("[ERROR] " + e.getMessage());
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		}
	
		return resultado;
	}
	
}
