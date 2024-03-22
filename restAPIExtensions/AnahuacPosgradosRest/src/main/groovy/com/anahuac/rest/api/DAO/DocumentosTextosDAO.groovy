package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.text.DateFormat
import java.text.ParseException
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
	        cellReporteValor.setCellValue("Bandeja Maestra Posgrados");
	
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
	
	        Cell header2 = headersRow.createCell(0);
	        header2.setCellValue("Expediente");
	        header2.setCellStyle(style);
	        Cell header3 = headersRow.createCell(1);
	        header3.setCellValue("Nombre");
	        header3.setCellStyle(style);
	        Cell header4 = headersRow.createCell(2);
	        header4.setCellValue("Email");
	        header4.setCellStyle(style);
	        Cell header5 = headersRow.createCell(3);
	        header5.setCellValue("CURP");
	        header5.setCellStyle(style);
	        Cell header6 = headersRow.createCell(4);
	        header6.setCellValue("Programa");
	        header6.setCellStyle(style);
	        Cell header7 = headersRow.createCell(5);
	        header7.setCellValue("Período de ingreso");
	        header7.setCellStyle(style);
	        Cell header8 = headersRow.createCell(6);
	        header8.setCellValue("Campus ingreso");
	        header8.setCellStyle(style);
			Cell header9 = headersRow.createCell(7);
			header9.setCellValue("Procedencia");
			header9.setCellStyle(style);
			Cell header10 = headersRow.createCell(8);
			header10.setCellValue("Promedio");
			header10.setCellStyle(style);
	        Cell header11 = headersRow.createCell(9);
	        header11.setCellValue("Estatus");
	        header11.setCellStyle(style);
	        Cell header12 = headersRow.createCell(10);
	        header12.setCellValue("Fecha de registro");
	        header12.setCellStyle(style);
	        Cell header13 = headersRow.createCell(11);
	        header13.setCellValue("Última modificación");
	        header13.setCellStyle(style);
	
	        DateFormat dfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			DateFormat dformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	        for (int i = 0; i < lstParams.size(); ++i) {
	            Row row = sheet.createRow(++rowCount);
	
	            Cell cell2 = row.createCell(0);
	            cell2.setCellValue(lstParams.get(i).caseid);
	            String nombre = lstParams.get(i).apellido_paterno + " " + lstParams.get(i).apellido_materno + " " + lstParams.get(i).nombre;
	            Cell cell3 = row.createCell(1);
	            cell3.setCellValue(nombre);
	            Cell cell4 = row.createCell(2);
	            cell4.setCellValue(lstParams.get(i).correo_electronico);
	            Cell cell5 = row.createCell(3);
	            cell5.setCellValue(lstParams.get(i).curp);
	            Cell cell6 = row.createCell(4);
	            cell6.setCellValue(lstParams.get(i).posgrado);
	            Cell cell7 = row.createCell(5);
	            cell7.setCellValue(lstParams.get(i).periodo);
	            Cell cell8 = row.createCell(6);
	            cell8.setCellValue(lstParams.get(i).campus);
	            Cell cell9 = row.createCell(7);
				cell9.setCellValue(lstParams.get(i).institucion);
				Cell cell10 = row.createCell(8);
				cell10.setCellValue(lstParams.get(i).promedio);
				Cell cell11 = row.createCell(9);
	            cell11.setCellValue(lstParams.get(i).estatus_solicitud);
				
				String fechaRegistroString = lstParams.get(i).fecha_registro;

				if (fechaRegistroString != null) {
				    try {
				        Date fechaRegistro = dfEntrada.parse(fechaRegistroString);
				        String fechaFormateada = dformat.format(fechaRegistro);
				        Cell cell12 = row.createCell(10);
				        cell12.setCellValue(fechaFormateada);
				    } catch (ParseException e) {
				        Cell cell12 = row.createCell(10);
				        cell12.setCellValue("Error en formato de fecha");
				    }
				} else {
				    Cell cell12 = row.createCell(10);
				    cell12.setCellValue("N/A");
				}
				
				String fechaUltimaModificacionString = lstParams.get(i).fecha_ultima_modificacion;
				
				if (fechaUltimaModificacionString != null) {
				    try {
				        Date fechaUltimaModificacion = dfEntrada.parse(fechaUltimaModificacionString);
				        String fechaFormateadaModificacion = dformat.format(fechaUltimaModificacion);
				        Cell cell13 = row.createCell(11);
				        cell13.setCellValue(fechaFormateadaModificacion);
				    } catch (ParseException e) {
				        Cell cell13 = row.createCell(11);
				        cell13.setCellValue("Error en formato de fecha");
				    }
				} else {
				    Cell cell13 = row.createCell(11);
				    cell13.setCellValue("N/A");
				}
	        }
	
	        for (int i = 0; i <= rowCount + 11; ++i) {
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
	        e.printStackTrace();
	        resultado.setSuccess(false);
	        resultado.setError(e.getMessage());
	        resultado.setError_info(errorLog);
	    }
	
	    return resultado;
	}
	
	public Result getExcelFileSolicitudesNuevas(String jsonData, RestAPIContext context) {
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
			cellReporteValor.setCellValue("Bandeja Solicitudes Nuevas Posgrados");
	
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
	
			Cell header2 = headersRow.createCell(0);
			header2.setCellValue("Expediente");
			header2.setCellStyle(style);
			Cell header3 = headersRow.createCell(1);
			header3.setCellValue("Nombre");
			header3.setCellStyle(style);
			Cell header4 = headersRow.createCell(2);
			header4.setCellValue("Email");
			header4.setCellStyle(style);
			Cell header5 = headersRow.createCell(3);
			header5.setCellValue("CURP");
			header5.setCellStyle(style);
			Cell header6 = headersRow.createCell(4);
			header6.setCellValue("Programa");
			header6.setCellStyle(style);
			Cell header7 = headersRow.createCell(5);
			header7.setCellValue("Período de ingreso");
			header7.setCellStyle(style);
			Cell header8 = headersRow.createCell(6);
			header8.setCellValue("Campus ingreso");
			header8.setCellStyle(style);
			Cell header9 = headersRow.createCell(7);
			header9.setCellValue("Procedencia");
			header9.setCellStyle(style);
			Cell header10 = headersRow.createCell(8);
			header10.setCellValue("Promedio");
			header10.setCellStyle(style);
			Cell header11 = headersRow.createCell(9);
			header11.setCellValue("Fecha de registro");
			header11.setCellStyle(style);
			Cell header12 = headersRow.createCell(10);
			header12.setCellValue("Última modificación");
			header12.setCellStyle(style);
	
			DateFormat dfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			DateFormat dformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
			for (int i = 0; i < lstParams.size(); ++i) {
				Row row = sheet.createRow(++rowCount);
	
				Cell cell2 = row.createCell(0);
				cell2.setCellValue(lstParams.get(i).caseid);
				String nombre = lstParams.get(i).apellido_paterno + " " + lstParams.get(i).apellido_materno + " " + lstParams.get(i).nombre;
				Cell cell3 = row.createCell(1);
				cell3.setCellValue(nombre);
				Cell cell4 = row.createCell(2);
				cell4.setCellValue(lstParams.get(i).correo_electronico);
				Cell cell5 = row.createCell(3);
				cell5.setCellValue(lstParams.get(i).curp);
				Cell cell6 = row.createCell(4);
				cell6.setCellValue(lstParams.get(i).posgrado);
				Cell cell7 = row.createCell(5);
				cell7.setCellValue(lstParams.get(i).periodo);
				Cell cell8 = row.createCell(6);
				cell8.setCellValue(lstParams.get(i).campus);
				Cell cell9 = row.createCell(7);
				cell9.setCellValue(lstParams.get(i).institucion);
				Cell cell10 = row.createCell(8);
				cell10.setCellValue(lstParams.get(i).promedio);
				
				String fechaRegistroString = lstParams.get(i).fecha_registro;
				
				if (fechaRegistroString != null) {
					try {
						Date fechaRegistro = dfEntrada.parse(fechaRegistroString);
						String fechaFormateada = dformat.format(fechaRegistro);
						Cell cell11 = row.createCell(9);
						cell11.setCellValue(fechaFormateada);
					} catch (ParseException e) {
						Cell cell11 = row.createCell(9);
						cell11.setCellValue("Error en formato de fecha");
					}
				} else {
					Cell cell11 = row.createCell(10);
					cell11.setCellValue("N/A");
				}
				
				String fechaUltimaModificacionString = lstParams.get(i).fecha_ultima_modificacion;
				
				if (fechaUltimaModificacionString != null) {
					try {
						Date fechaUltimaModificacion = dfEntrada.parse(fechaUltimaModificacionString);
						String fechaFormateadaModificacion = dformat.format(fechaUltimaModificacion);
						Cell cell12 = row.createCell(10);
						cell12.setCellValue(fechaFormateadaModificacion);
					} catch (ParseException e) {
						Cell cell12 = row.createCell(10);
						cell12.setCellValue("Error en formato de fecha");
					}
				} else {
					Cell cell13 = row.createCell(11);
					cell13.setCellValue("N/A");
				}
			}
	
			for (int i = 0; i <= rowCount + 11; ++i) {
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
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		}
	
		return resultado;
	}
	
	public Result getExcelFileSolicitudesRechazadas(String jsonData, RestAPIContext context) {
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
			cellReporteValor.setCellValue("Bandeja Solicitudes Rechazadas Posgrados");
	
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
	
			Cell header2 = headersRow.createCell(0);
			header2.setCellValue("Expediente");
			header2.setCellStyle(style);
			Cell header3 = headersRow.createCell(1);
			header3.setCellValue("Nombre");
			header3.setCellStyle(style);
			Cell header4 = headersRow.createCell(2);
			header4.setCellValue("Email");
			header4.setCellStyle(style);
			Cell header5 = headersRow.createCell(3);
			header5.setCellValue("CURP");
			header5.setCellStyle(style);
			Cell header6 = headersRow.createCell(4);
			header6.setCellValue("Programa");
			header6.setCellStyle(style);
			Cell header7 = headersRow.createCell(5);
			header7.setCellValue("Período de ingreso");
			header7.setCellStyle(style);
			Cell header8 = headersRow.createCell(6);
			header8.setCellValue("Campus ingreso");
			header8.setCellStyle(style);
			Cell header9 = headersRow.createCell(7);
			header9.setCellValue("Procedencia");
			header9.setCellStyle(style);
			Cell header10 = headersRow.createCell(8);
			header10.setCellValue("Promedio");
			header10.setCellStyle(style);
			Cell header11 = headersRow.createCell(9);
			header11.setCellValue("Motivo");
			header11.setCellStyle(style);
			Cell header12 = headersRow.createCell(10);
			header12.setCellValue("Última modificación");
			header12.setCellStyle(style);
	
			DateFormat dfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			DateFormat dformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
			for (int i = 0; i < lstParams.size(); ++i) {
				Row row = sheet.createRow(++rowCount);
	
				Cell cell2 = row.createCell(0);
				cell2.setCellValue(lstParams.get(i).caseid);
				String nombre = lstParams.get(i).apellido_paterno + " " + lstParams.get(i).apellido_materno + " " + lstParams.get(i).nombre;
				Cell cell3 = row.createCell(1);
				cell3.setCellValue(nombre);
				Cell cell4 = row.createCell(2);
				cell4.setCellValue(lstParams.get(i).correo_electronico);
				Cell cell5 = row.createCell(3);
				cell5.setCellValue(lstParams.get(i).curp);
				Cell cell6 = row.createCell(4);
				cell6.setCellValue(lstParams.get(i).posgrado);
				Cell cell7 = row.createCell(5);
				cell7.setCellValue(lstParams.get(i).periodo);
				Cell cell8 = row.createCell(6);
				cell8.setCellValue(lstParams.get(i).campus);
				Cell cell9 = row.createCell(7);
				cell9.setCellValue(lstParams.get(i).institucion);
				Cell cell10 = row.createCell(8);
				cell10.setCellValue(lstParams.get(i).promedio);
				Cell cell11 = row.createCell(9);
				cell11.setCellValue(lstParams.get(i).mensaje_admin_escolar);
				
				String fechaUltimaModificacionString = lstParams.get(i).fecha_ultima_modificacion;
				
				if (fechaUltimaModificacionString != null) {
					try {
						Date fechaUltimaModificacion = dfEntrada.parse(fechaUltimaModificacionString);
						String fechaFormateadaModificacion = dformat.format(fechaUltimaModificacion);
						Cell cell12 = row.createCell(10);
						cell12.setCellValue(fechaFormateadaModificacion);
					} catch (ParseException e) {
						Cell cell12 = row.createCell(10);
						cell12.setCellValue("Error en formato de fecha");
					}
				} else {
					Cell cell13 = row.createCell(11);
					cell13.setCellValue("N/A");
				}
			}
	
			for (int i = 0; i <= rowCount + 10; ++i) {
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
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		}
	
		return resultado;
	}
	
	public Result getExcelFileSolicitudesAdmitidas(String jsonData, RestAPIContext context) {
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
			cellReporteValor.setCellValue("Bandeja Solicitudes Rechazadas Posgrados");
	
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
	
			Cell header2 = headersRow.createCell(0);
			header2.setCellValue("Expediente");
			header2.setCellStyle(style);
			Cell header3 = headersRow.createCell(1);
			header3.setCellValue("Nombre");
			header3.setCellStyle(style);
			Cell header4 = headersRow.createCell(2);
			header4.setCellValue("Email");
			header4.setCellStyle(style);
			Cell header5 = headersRow.createCell(3);
			header5.setCellValue("CURP");
			header5.setCellStyle(style);
			Cell header6 = headersRow.createCell(4);
			header6.setCellValue("Programa");
			header6.setCellStyle(style);
			Cell header7 = headersRow.createCell(5);
			header7.setCellValue("Período de ingreso");
			header7.setCellStyle(style);
			Cell header8 = headersRow.createCell(6);
			header8.setCellValue("Campus ingreso");
			header8.setCellStyle(style);
			Cell header9 = headersRow.createCell(7);
			header9.setCellValue("Procedencia");
			header9.setCellStyle(style);
			Cell header10 = headersRow.createCell(8);
			header10.setCellValue("Promedio");
			header10.setCellStyle(style);
			Cell header11 = headersRow.createCell(9);
			header11.setCellValue("Resultado");
			header11.setCellStyle(style);
			Cell header12 = headersRow.createCell(10);
			header12.setCellValue("Última modificación");
			header12.setCellStyle(style);
	
			DateFormat dfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			DateFormat dformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
			for (int i = 0; i < lstParams.size(); ++i) {
				Row row = sheet.createRow(++rowCount);
	
				Cell cell2 = row.createCell(0);
				cell2.setCellValue(lstParams.get(i).caseid);
				String nombre = lstParams.get(i).apellido_paterno + " " + lstParams.get(i).apellido_materno + " " + lstParams.get(i).nombre;
				Cell cell3 = row.createCell(1);
				cell3.setCellValue(nombre);
				Cell cell4 = row.createCell(2);
				cell4.setCellValue(lstParams.get(i).correo_electronico);
				Cell cell5 = row.createCell(3);
				cell5.setCellValue(lstParams.get(i).curp);
				Cell cell6 = row.createCell(4);
				cell6.setCellValue(lstParams.get(i).posgrado);
				Cell cell7 = row.createCell(5);
				cell7.setCellValue(lstParams.get(i).periodo);
				Cell cell8 = row.createCell(6);
				cell8.setCellValue(lstParams.get(i).campus);
				Cell cell9 = row.createCell(7);
				cell9.setCellValue(lstParams.get(i).institucion);
				Cell cell10 = row.createCell(8);
				cell10.setCellValue(lstParams.get(i).promedio);
				Cell cell11 = row.createCell(9);
				cell11.setCellValue(lstParams.get(i).mensaje_admin_escolar);
				
				String fechaUltimaModificacionString = lstParams.get(i).fecha_ultima_modificacion;
				
				if (fechaUltimaModificacionString != null) {
					try {
						Date fechaUltimaModificacion = dfEntrada.parse(fechaUltimaModificacionString);
						String fechaFormateadaModificacion = dformat.format(fechaUltimaModificacion);
						Cell cell12 = row.createCell(10);
						cell12.setCellValue(fechaFormateadaModificacion);
					} catch (ParseException e) {
						Cell cell12 = row.createCell(10);
						cell12.setCellValue("Error en formato de fecha");
					}
				} else {
					Cell cell13 = row.createCell(11);
					cell13.setCellValue("N/A");
				}
			}
	
			for (int i = 0; i <= rowCount + 10; ++i) {
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
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		}
	
		return resultado;
	}
	
	public Result getExcelFileGeneralProceso(String jsonData, RestAPIContext context) {
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
	        cellReporteValor.setCellValue("General");
	
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
	
	        Cell header2 = headersRow.createCell(0);
	        header2.setCellValue("ID BPM");
	        header2.setCellStyle(style);
	        Cell header3 = headersRow.createCell(1);
	        header3.setCellValue("Campus de interés");
	        header3.setCellStyle(style);
	        Cell header4 = headersRow.createCell(2);
	        header4.setCellValue("Grado");
	        header4.setCellStyle(style);
	        Cell header5 = headersRow.createCell(3);
	        header5.setCellValue("Programa");
	        header5.setCellStyle(style);
	        Cell header6 = headersRow.createCell(4);
	        header6.setCellValue("Período");
	        header6.setCellStyle(style);
	        Cell header7 = headersRow.createCell(5);
	        header7.setCellValue("Estatus");
	        header7.setCellStyle(style);
	        Cell header8 = headersRow.createCell(6);
	        header8.setCellValue("Fecha de registro");
	        header8.setCellStyle(style);
			Cell header9 = headersRow.createCell(7);
			header9.setCellValue("Última modificación");
			header9.setCellStyle(style);
			Cell header10 = headersRow.createCell(8);
			header10.setCellValue("Fecha de dictamen");
			header10.setCellStyle(style);
	        Cell header11 = headersRow.createCell(9);
	        header11.setCellValue("Nombre(s)");
	        header11.setCellStyle(style);
	        Cell header12 = headersRow.createCell(10);
	        header12.setCellValue("Apellido paterno");
	        header12.setCellStyle(style);
	        Cell header13 = headersRow.createCell(11);
	        header13.setCellValue("Apellido materno");
	        header13.setCellStyle(style);
			Cell header14 = headersRow.createCell(12);
			header14.setCellValue("Sexo");
			header14.setCellStyle(style);
			Cell header15 = headersRow.createCell(13);
			header15.setCellValue("Nacionalidad");
			header15.setCellStyle(style);
			Cell header16 = headersRow.createCell(14);
			header16.setCellValue("Estado civil");
			header16.setCellStyle(style);
			Cell header17 = headersRow.createCell(15);
			header17.setCellValue("Fecha de nacimiento");
			header17.setCellStyle(style);
			Cell header18 = headersRow.createCell(16);
			header18.setCellValue("País de nacimiento");
			header18.setCellStyle(style);
			Cell header19 = headersRow.createCell(17);
			header19.setCellValue("Estado de nacimiento");
			header19.setCellStyle(style);
			Cell header20 = headersRow.createCell(18);
			header20.setCellValue("Ciudad de nacimiento");
			header20.setCellStyle(style);
			Cell header21 = headersRow.createCell(19);
			header21.setCellValue("Egresado Anáhuac");
			header21.setCellStyle(style);
			Cell header22 = headersRow.createCell(20);
			header22.setCellValue("Campus de esgresado");
			header22.setCellStyle(style);
			Cell header23 = headersRow.createCell(21);
			header23.setCellValue("Otros idiomas");
			header23.setCellStyle(style);
			Cell header24 = headersRow.createCell(22);
			header24.setCellValue("Trabaja actualmente");
			header24.setCellStyle(style);
			Cell header25 = headersRow.createCell(23);
			header25.setCellValue("Trabajo previo");
			header25.setCellStyle(style);
			
	
	        DateFormat dfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			DateFormat dformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	        for (int i = 0; i < lstParams.size(); ++i) {
	            Row row = sheet.createRow(++rowCount);
				
				
				
				/*
				 * lstParams.get(i).correo_electronico
				 * 
				 * 
				 * */
				
	            Cell cell2 = row.createCell(0);
	            cell2.setCellValue(lstParams.get(i).caseid);
	            
	            Cell cell3 = row.createCell(1);
	            cell3.setCellValue(lstParams.get(i).campus);
	            Cell cell4 = row.createCell(2);
	            cell4.setCellValue(lstParams.get(i).posgrado);
	            Cell cell5 = row.createCell(3);
	            cell5.setCellValue(lstParams.get(i).programa);
	            Cell cell6 = row.createCell(4);
	            cell6.setCellValue(lstParams.get(i).periodo);
	            Cell cell7 = row.createCell(5);
	            cell7.setCellValue(lstParams.get(i).estatus_solicitud);
				
				String fechaRegistroString = lstParams.get(i).fecha_registro;
				if (fechaRegistroString != null) {
					try {
						Date fechaRegistro = dfEntrada.parse(fechaRegistroString);
						String fechaFormateada = dformat.format(fechaRegistro);
						Cell cell8 = row.createCell(6);
						cell8.setCellValue(fechaFormateada);
					} catch (ParseException e) {
						Cell cell8 = row.createCell(6);
						cell8.setCellValue("Error en formato de fecha");
					}
				} else {
					Cell cell8 = row.createCell(6);
					cell8.setCellValue("N/A");
				}
				
				String fechaUltimaModificacionString = lstParams.get(i).fecha_ultima_modificacion;
				if (fechaUltimaModificacionString != null) {
					try {
						Date fechaUltimaModificacion = dfEntrada.parse(fechaUltimaModificacionString);
						String fechaFormateadaModificacion = dformat.format(fechaUltimaModificacion);
						Cell cell9 = row.createCell(7);
						cell9.setCellValue(fechaFormateadaModificacion);
					} catch (ParseException e) {
						Cell cell9 = row.createCell(7);
						cell9.setCellValue("Error en formato de fecha");
					}
				} else {
					Cell cell9 = row.createCell(7);
					cell9.setCellValue("N/A");
				}
				
				String fechaDictamenString = ""//lstParams.get(i).fecha_dictamen;
				if (fechaUltimaModificacionString != null) {
					try {
						Date fechaDictamen = dfEntrada.parse(fechaDictamenString);
						String fechaDictamenModificacion = dformat.format(fechaDictamen);
						Cell cell10 = row.createCell(8);
						cell10.setCellValue(fechaDictamenModificacion);
					} catch (ParseException e) {
						Cell cell10 = row.createCell(8);
						cell10.setCellValue("N/A");//"Error en formato de fecha");
					}
				} else {
					Cell cell10 = row.createCell(8);
					cell10.setCellValue("N/A");
				}

				Cell cell11 = row.createCell(9);
	            cell11.setCellValue(lstParams.get(i).nombre);
				
				Cell cell12 = row.createCell(10);
				cell12.setCellValue(lstParams.get(i).apellido_paterno);

				Cell cell13 = row.createCell(11);
				cell13.setCellValue(lstParams.get(i).apellido_materno);
				
				Cell cell14 = row.createCell(12);
				cell14.setCellValue(lstParams.get(i).sexo);
				
				Cell cell15 = row.createCell(13);
				cell15.setCellValue("Falta"); // Nacionalidad
				
				Cell cell16 = row.createCell(14);
				cell16.setCellValue("Falta"); // Estado civil
				
				String fechaNacimientoString = lstParams.get(i).fecha_nacimiento;
				if (fechaNacimientoString != null) {
					try {
						Date fechaNacimiento = dfEntrada.parse(fechaNacimientoString);
						String fechaNacimientoModificacion = dformat.format(fechaNacimiento);
						Cell cell17 = row.createCell(15);
						cell17.setCellValue(fechaNacimientoModificacion);
					} catch (ParseException e) {
						Cell cell17 = row.createCell(15);
						cell17.setCellValue("N/A");//"Error en formato de fecha");
					}
				} else {
					Cell cell17 = row.createCell(15);
					cell17.setCellValue("N/A");
				}
				
				Cell cell18 = row.createCell(16);
				cell18.setCellValue(lstParams.get(i).lugar_nacimiento_pais);
				
				Cell cell19 = row.createCell(17);
				cell19.setCellValue(lstParams.get(i).lugar_nacimiento_estado);
				
				Cell cell20 = row.createCell(18);
				cell20.setCellValue(lstParams.get(i).lugar_nacimiento_ciudad);
				
				Cell cell21 = row.createCell(19);
				cell21.setCellValue(lstParams.get(i).alumno_anahuac && lstParams.get(i).alumno_anahuac == "t" ? "Sí" : "No");
				
				Cell cell22 = row.createCell(20);
				cell22.setCellValue("Falta"); // Campus de egresado
				
				Cell cell23 = row.createCell(21);
				cell23.setCellValue("Falta"); // Otros idiomas
				
				Cell cell24 = row.createCell(22);
				cell24.setCellValue("Falta"); // Trabaja actualmente
				
				Cell cell25 = row.createCell(23);
				cell25.setCellValue("Falta"); // Trabajo previo

	        }
	
	        for (int i = 0; i <= rowCount + 11; ++i) {
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
	        e.printStackTrace();
	        resultado.setSuccess(false);
	        resultado.setError(e.getMessage());
	        resultado.setError_info(errorLog);
	    }
	
	    return resultado;
	}
	
}
