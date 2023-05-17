package com.anahuac.rest.api.DAO

import java.nio.file.Path
import java.nio.file.Paths
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.RowId
import java.sql.Statement
import java.text.DateFormat
import java.text.SimpleDateFormat
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.IndexedColorMap
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bonitasoft.engine.identity.UserMembership
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.codec.binary.Hex;

import com.anahuac.rest.api.Entity.Result
import com.anahuac.catalogos.CatCampus
import com.anahuac.model.DetalleSolicitud
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements

import com.anahuac.catalogos.CatCampusDAO
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.bpm.document.Document

import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.StorageException
import com.microsoft.azure.storage.blob.BlobProperties
import com.microsoft.azure.storage.blob.BlobType
import com.microsoft.azure.storage.blob.CloudBlobClient
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob


import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper

class BecasDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(BecasDAO.class);
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
	
	
	public Result getPlantillaHermanos(String fecha) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			//String consulta = "select * from plantillaHermanos where CAST(fechaRegistro as DATE) = (CAST(TO_CHAR(NOW(),'YYYY-MM-DD') as DATE) - integer '1')"
			//String consulta = "select * from plantillaHermanos where CAST(fechaRegistro as DATE) = (CAST(TO_CHAR(NOW(),'YYYY-MM-DD') as DATE) - integer '1')"
			String consulta = "select * from plantillaHermanos where CAST(fechaRegistro as DATE) = (CAST([FECHA] as DATE) [MENOS] )"	
			if(!fecha.equals(null) && !fecha.equals(" ") && !fecha.equals("")) {
				consulta = consulta.replace("[FECHA]", "'"+fecha+"'")
				consulta = consulta.replace("[MENOS]", " ")
			}else {
				consulta = consulta.replace("[FECHA]", "TO_CHAR(NOW(),'YYYY-MM-DD')")
				consulta = consulta.replace("[MENOS]", "- integer '1'")
			}
			List<String> rows = new ArrayList<String>();
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs= pstm.executeQuery();
			if(rs.next()) {
				SSA = rs.getString("valor")
			}
			
			pstm = con.prepareStatement(consulta);
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
			resultado.setError_info(" errorLog = "+errorLog)
			resultado.setData(rows)
			resultado.setSuccess(true)
			resultado.setTotalRegistros(columnCount)
		}catch(Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog+" "+e.getMessage())
		}
		finally{
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getPlantillaRegistro(String fechaCarga) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			//String consulta = "select *,TO_CHAR(CAST(fechanacimiento as DATE),'YYYY-MM-DD') as fechanacimiento from plantillaRegistro where CAST(fecharegistro as DATE) = (CAST(TO_CHAR(NOW(),'YYYY-MM-DD') as DATE) - integer '1')"
			//String consulta = "select * from plantillaRegistro where CAST(fecharegistro as DATE) = (CAST(TO_CHAR(NOW(),'YYYY-MM-DD') as DATE) - integer '1')"
			String consulta = "select * from plantillaRegistro where CAST(fecharegistro as DATE) = (CAST([FECHA] as DATE) [MENOS] )"
			if(!fechaCarga.equals(null) && !fechaCarga.equals(" ") && !fechaCarga.equals("")) {
				consulta = consulta.replace("[FECHA]", "'"+fechaCarga+"'")
				consulta = consulta.replace("[MENOS]", " ")
			}else {
				consulta = consulta.replace("[FECHA]", "TO_CHAR(NOW(),'YYYY-MM-DD')")
				consulta = consulta.replace("[MENOS]", "- integer '1'")
			}
			List<String> rows = new ArrayList<String>();
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs= pstm.executeQuery();
			if(rs.next()) {
				SSA = rs.getString("valor")
			}
			
			
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery()
			
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();

					for (int i = 1; i <= columnCount; i++) {
						if(metaData.getColumnLabel(i).toLowerCase().equals("fechanacimiento")) {
							String fecha = rs.getString(i);
							if(fecha != null && !fecha.equals("null") ) {
								columns.put(metaData.getColumnLabel(i).toLowerCase(), fecha[0..9]);
							}else {
								columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
							}
						}else 
							if(metaData.getColumnLabel(i).toLowerCase().equals("foto") || metaData.getColumnLabel(i).toLowerCase().equals("kardex")) {
							columns.put(metaData.getColumnLabel(i).toLowerCase(), (metaData.getColumnLabel(i).toLowerCase().equals("foto")?"regAvatarName":"kardexPr")+rs.getString("nregistro"));
							columns.put("url"+metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						}else {
							columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						}
					}
					rows.add(columns);
			}
			resultado.setError_info(" errorLog = "+errorLog)
			resultado.setData(rows)
			resultado.setSuccess(true)
		}catch(Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog+" "+e.getMessage())
		}
		finally{
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result excelPlantillaRegistro(String fechaCarga) {
		Result resultado = new Result();
		String errorLog = "";
		
		try {
			Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams;
			//String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Registros");
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER)
			//color
			IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
			XSSFColor  color = new XSSFColor(new java.awt.Color(191,220,249),colorMap);
			
			style.setFillForegroundColor(color)
			
			dataResult = getPlantillaRegistro(fechaCarga);
			if (dataResult.success) {
				lstParams = dataResult.getData();
				
			} else {
				throw new Exception("No encontro datos");
			}
			
			def titulos = ["#","EXPEDIENTE","NOMBRE","SEGUNDO NOMBRE","APELL PATERNO","APELL MATERNO","CAMPUS","ESTADO","MUNICIPIO","PAIS","CORREO","CONTRASEÑA","SEXO","FECHA NACI","NACIONALIDAD","RELIGION","CURP/PASAPORTE","CELULAR","AVATAR","KARDEX","ESTCICIL","CCALLE","NUMEXT","NUMINT","COLONIA","CP","TEL-OTRO","PAA","UNIVERSIDAD","LICENCIATURA","PERIODO INGRESO","OTRA UNIVERSIDAD","PREPA","PREPA PAIS","PREPA ESTADO","PREPA CIUDAD","PREPA PROMEDIO","TUTOR TITULO","TUTOR NOMBRE","TUTOR APELLIDO","TUTOR EGRESADO","TUTOR UNI EGRESADO","TUTOR ESCOLARIDAD","TUTOR PARENTESCO","TRABAJO TUTOR","TUTOR EMPRESA","TUTOR GIRO EMPRESA","TUTOR PUESTO","TUTOR CALLE","TUTOR NUM EXT","TUTOR NUM INT","TUTOR COLONIA","TUTOR CP","TUTOR MUNICIPIO","TUTOR PAIS","TUTOR ESTADO","TUTOR CELULAR","PADRE TITULO","PADRE NOMBRE","PADRE APELLIDOS","PADRE VIVO","PADRE EGRESADO","PADRE UNI EGRESO","PADRE ESCOLARIDAD","PADRE TUTOR","PADRE CORREO","PADRE TRABAJA","PADRE EMPRESA","PADRE GIRO EMPRESA","PADRE","PADRE CALLE","PADRE NUM EXT","PADRE NUM INT","PADRE COLONIA","PADRE CP","PADRE MUNICIPIO","PADRE PAIS","PADRE ESTADO","PADRE CEL","MADRE TITULO","MADRE NOMBRE","MADRE APELLIDO","MADRE VIVE","MADRE EGRESADA","MADRE UNI EGRESADA","MADRE CORREO","MADRE ESCOL","MADRE CALLE","MADRE NUM EXT","MADRE NUM INT","MADRE COLONIA","MADRE CP","MADRE MUNICIPIO","MADRE PAIS","MADRE ESTADO","MADRE CEL","EMERGENCIA","EMERGENCIA NOMBRE","EMERGENCIA TELEFONO","EMERGENCIA CELULAR","CON QUIEN VIVES","ESTADO PADRES","DISCAPACIDAD","ATENCION MEDICA","PERSONA SALUDABLE","ALGUN DEPORTE","AYUDA SOCIAL","TIEMPO LIBRE","GUSTA LEER","TIPO LECTURA","JEFE ASOCIACION","CUAL ASOCIACION","ASOCIACION DEP","ASOCIACION SOC","ASOCIACION REL","ASOCIACION POL","ASOCIACION EST","FAMILIAR ANAHUAC","ATENCION ESPIRITUAL","CONOCE REGNUM","TIENE RELIGION","VALOR RELIGION","PRACTICAS RELIGION","NO GUSTAR RELIGION"]
			Row headersRow = sheet.createRow(rowCount);
			++rowCount;
			List<Cell> header = new ArrayList<Cell>();
			for(int i = 0; i < titulos.size(); ++i) {
				header.add(headersRow.createCell(i))
				header[i].setCellValue(titulos.get(i))
				header[i].setCellStyle(style)
			}
			
			CellStyle bodyStyle = workbook.createCellStyle();
			bodyStyle.setWrapText(true);
			bodyStyle.setAlignment(HorizontalAlignment.CENTER);
			
			
			def info = ["nregistro","expediente","primernombre","segundonombre","apellidopaterno","apellidomaterno","campus","estado","municipio","pais","correo","contrasena","sexo","fechanacimiento","nacionalidad","religion","curp","celular","foto","kardex","estadocivil","casacalle","numext","numint","colonia","cp","otrotelefono","puntajepaa","universidad","licenciatura","periodoingreso","otrauniversidad","preparatoria","prepapais","prepaestado","prepaciudad","prepapromedio","tutortitulo","tutornombre","tutorapellido","tutoregresado","tutoruniegresado","tutorescolaridad","tutorparentesco","tutortrabaja","tutorempresa","tutorgiroempresa","tutorpuesto","tutorcalle","tutornumext","tutornumint","tutorcolonia","tutorcp","tutormunicipio","tutorpais","tutorestado","tutorcelular","padretitulo","padrenombre","padreapellido","padrevivo","padreegresado","padreuniegresado","padreescolaridad","padretutor","padrecorreo","padretrabaja","padreempresa","padregiroempresa","padrepuesto","padrecalle","padrenumext","padrenumint","padrecolonia","padrecp","padremunicipio","padrepais","padreestado","padrecel","madretitulo","madrenombre","madreapellido","madrevive","madreegresada","madreuniegresada","madrecorreo","madreescolaridad","madrecalle","madrenumext","madrenumint","madrecolonia","madrecp","madremunicipio","madrepais","madreestado","madrecel","emergencia","emergencianombre","emergenciatelefono","emergenciacelular","conquienvives","estadopadres","discapacidad","atencionmedica","personasaludable","algundeporte","ayudasocial","tiempolibre","tegustaleer","tipolectura","jefeasociacion","cualasociacion","asociaciondeportiva","asociacionsocial","asociacionreligiosa","asociacionpolitica","asociacionescolar","familiarenanahuac","atencionespiritual","conoceregnum","tienereligion","valorreligion","practicasreligion","aspectoreligionnogustar"]
			List<Cell> body;
			for (int i = 0; i < lstParams.size(); ++i){
				Row row = sheet.createRow(rowCount);
				++rowCount;
				body = new ArrayList<Cell>()
				for(int j=0;  j < info.size(); ++j) {
					body.add(row.createCell(j))
					body[j].setCellValue(lstParams[i][info.get(j)])
					body[j].setCellStyle(bodyStyle);
					
				}
				
			}
			if(lstParams.size()>0) {
				for(int i=0; i<=138; ++i) {
					sheet.autoSizeColumn(i);
				}
				String fecha = lstParams[0]["fecharegistro"].toString()+"";
				String nameFile = "Registros-"+fecha+".xls";
				FileOutputStream outputStream = new FileOutputStream(nameFile);
				workbook.write(outputStream);
				outputStream.close();
				
				dataResult = ftpUpload(nameFile,fecha);
				errorLog+="ftpUpload:"+dataResult
				if (dataResult.success) {					
					File file = new File(nameFile);
					if (file.exists()){
						if(file.delete()) {
							errorLog+="Sea eliminado Registros"
						}
					 }
					//file.delete();
					String url = "";
					Result dataResult2 = new Result();
					for (int i = 0; i < lstParams.size(); ++i){
						url = lstParams[i]["urlfoto"].toString()+"";
						dataResult2 = DownloadAzure(url,1,fecha+"/documentos")
						errorLog+=" foto Info:"+dataResult2.getError_info()+" error:"+dataResult2.getError()
						
						url = lstParams[i]["urlkardex"].toString()+"";
						dataResult2 = DownloadAzure(url,2,fecha+"/documentos")
						errorLog+=" kardex: Info:"+dataResult2.getError_info()+" error:"+dataResult2.getError()
					}
					
					
				} else {
					throw new Exception("No encontro datos:"+errorLog+dataResult.getError());
				}
				
			}
			
			
			//List<Object> lstResultado = new ArrayList<Object>();
			//lstResultado.add(encodeFileToBase64Binary(nameFile));
			resultado.setSuccess(true);
			//resultado.setData(lstResultado);
			
			
		}catch(Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	public Result excelPlantillaHermanos(String fechaCarga) {
		Result resultado = new Result();
		String errorLog = "";
		
		try {
			Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams;
			//String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Hermanos");
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER)
			//color
			IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
			XSSFColor  color = new XSSFColor(new java.awt.Color(191,220,249),colorMap);
			
			style.setFillForegroundColor(color)
			
			dataResult = getPlantillaHermanos(fechaCarga);
			int countColumns = 0;
			if (dataResult.success) {
				lstParams = dataResult.getData();
			} else {
				throw new Exception("No encontro datos");
			}
			
			def titulos = ["#","NUMEROHERMANO","NOMBRE","SEGUNDO NOMBRE","APELL PATERNO","APELL MATERNO","CAMPUS","EXPEDIENTE","ID REG","NOMBRE HERMANO","APELLIDO HERMANO","FECHA NAC HERMANO","ESTUDIO HERMANO","INSTITUCIÓN HERMANO","TRABAJO HERMANO","EMPRESA HERMANO"]
			Row headersRow = sheet.createRow(rowCount);
			++rowCount;
			List<Cell> header = new ArrayList<Cell>();
			for(int i = 0; i < titulos.size(); ++i) {
				header.add(headersRow.createCell(i))
				header[i].setCellValue(titulos.get(i))
				header[i].setCellStyle(style)
			}
			
			CellStyle bodyStyle = workbook.createCellStyle();
			bodyStyle.setWrapText(true);
			bodyStyle.setAlignment(HorizontalAlignment.CENTER);
			
			def info = ["idreg","persistenceid","primernombre","segundonombre","apellidopaterno","apellidomaterno","campus","expediente","idreg","nombrehermano","apellidohermano","fechanacimientohermano","estudiohermano","institucionhermano","trabajohermano","empresahermano"]
			List<Cell> body;
			for (int i = 0; i < lstParams.size(); ++i){
				Row row = sheet.createRow(rowCount);
				++rowCount;
				body = new ArrayList<Cell>()
				for(int j=0;  j < info.size(); ++j) {
					body.add(row.createCell(j))
					body[j].setCellValue(lstParams[i][info.get(j)])
					body[j].setCellStyle(bodyStyle);
					
				}
			}
			
			for(int i=0; i<=15; ++i) {
				sheet.autoSizeColumn(i);
			}
			
			if(lstParams.size()>0) {
				String fecha = lstParams[0]["fecharegistro"].toString()+"";
				String nameFile = "Hermanos-"+fecha+".xls";
				FileOutputStream outputStream = new FileOutputStream(nameFile);
				workbook.write(outputStream);
				outputStream.close();
				
				
				dataResult = ftpUpload(nameFile,fecha);
				errorLog+=" Hermanos :"+dataResult
				if (dataResult.success) {
					File file = new File(nameFile);
					if (file.exists()){
						if(file.delete()) {
							errorLog+="Sea eliminado Hermanos"
						}
					 }
					//file.delete();
				} else {
					throw new Exception("No encontro datos:"+errorLog+dataResult.getError());
				}
			}
			
			
			resultado.setSuccess(true);
			
			
		}catch(Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	
	
	
	public Result ftpUpload(String nameFile,String carpeta) {
		Result resultado = new Result();
		String errorLog = "";
		
		String server = "";
		int port = 21;
		String user = "";
		String pass = "";
		
		FTPClient ftpClient = new FTPClient();
		try {
			Result dataResult = new Result();
			List<Object> lstParams;
			
			dataResult = infoFTP();
			if (dataResult.success) {
				lstParams = dataResult.getData();
			} else {
				throw new Exception("Fallo al obtener la configuracion del ftp");
			}
			
			server = lstParams[0].dominio;
			port = Integer.parseInt(lstParams[0].puerto);
			user = lstParams[0].usuario;
			pass = lstParams[0].password;
			
			ftpClient.connect(server, port);
			ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();
			
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			
			String dirPath = "/exportacion excel/"+carpeta;
			boolean creado = makeDirectories(ftpClient, dirPath);
			
			if(creado) {
				InputStream inputStream = new FileInputStream(nameFile);
				Path path = Paths.get(nameFile);
				errorLog+=("Start uploading first file "+nameFile+" path: "+path);
				boolean done = ftpClient.storeFile(dirPath+"/"+nameFile, inputStream);
				inputStream.close();
				if (done) {
					errorLog+=("The file is uploaded successfully.");
				}
			}else {
				throw new Exception("Fallo al crear la carpeta");
			}
			
			
			resultado.setSuccess(true);
			
			
		} catch (IOException ex) {
			ex.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(ex.getMessage());
			
			ex.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return resultado;
	}
	
	
	
	public static boolean makeDirectories(FTPClient ftpClient, String dirPath)
	throws IOException {
		String[] pathElements = dirPath.split("/");
		if (pathElements != null && pathElements.length > 0) {
			for (String singleDir : pathElements) {
				boolean existed = ftpClient.changeWorkingDirectory(singleDir);
				if (!existed) {
					boolean created = ftpClient.makeDirectory(singleDir);
					if (created) {
						System.out.println("CREATED directory: " + singleDir);
						ftpClient.changeWorkingDirectory(singleDir);
					} else {
						System.out.println("COULD NOT create directory: " + singleDir);
						return false;
					}
				}	
			}
		}
		return true;
	}
	
	/*private String remoteHost = "";
	private String username = "";
	private String password = ""
	
	private SSHClient setupSshj() throws IOException {
		SSHClient client = new SSHClient();
		client.addHostKeyVerifier(new PromiscuousVerifier());
		client.connect(remoteHost,21);
		client.authPassword(username, password);
		return client;
	}
	
	private String remoteDir = "/";
	
	public void whenUploadFileUsingSshj_thenSuccess(String nameFile) throws IOException {
		SSHClient sshClient = setupSshj();
		SFTPClient sftpClient = sshClient.newSFTPClient();
	 
		sftpClient.put(nameFile, remoteDir);
	 
		sftpClient.close();
		sshClient.disconnect();
	}
	*/
	
	
	
	public Result infoFTP() {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			String consulta = "select * from configuracionftp"
			List<String> rows = new ArrayList<String>();
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
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
			//resultado.setError_info(" errorLog = "+errorLog)
			resultado.setData(rows)
			resultado.setSuccess(true)
			//resultado.setTotalRegistros(columnCount)
		}catch(Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			//resultado.setError_info(errorLog+" "+e.getMessage())
		}
		finally{
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result DownloadAzure(String URL,int tipo,String carpeta) {
		File downloadedFile = null;

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		
		Result result = new Result();
		Result dataResult = new Result();
		String defaultEndpointsProtocol=""
		String accountName=""
		String accountKey=""
		Boolean closeCon=false;
		String errorLog = "";
		try {
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.CONFIGURACIONES)
			rs = pstm.executeQuery()
			while (rs.next()) {
				switch(rs.getString("clave")) {
					case "AzureAccountName":
						accountName=rs.getString("valor")
					break;
					case "AzureAccountKey":
						accountKey=rs.getString("valor")
					break;
					case "AzureDefaultEndpointsProtocol":
						defaultEndpointsProtocol=rs.getString("valor")
					break;
				}
			}
			
			String STORAGE_CONNECTION_STRING ="DefaultEndpointsProtocol="+defaultEndpointsProtocol+";" + "AccountName="+accountName+";" + "AccountKey="+accountKey;
			CloudStorageAccount account = CloudStorageAccount.parse(STORAGE_CONNECTION_STRING);
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(STORAGE_CONNECTION_STRING);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference("privado");


			String urlBlob = URL;
			
			String [] info = urlBlob.split("/");
			String blobUrlFinal = info[4]+"/"+info[5];
			
			blobUrlFinal = blobUrlFinal.replace("%20", " ");
			//Getting a blob reference
			CloudBlockBlob blob = container.getBlockBlobReference(blobUrlFinal);

			String[] tipoDato = info[5].split("\\.");
			String extencion = tipoDato[1]?.toString();
			if(tipoDato.length > 2) {
				for(int i = 0; i < tipoDato; i++) {
					if(tipoDato[i]?.toLowerCase() == "png" || tipoDato[i]?.toLowerCase() == "jpeg" || tipoDato[i]?.toLowerCase() == "jpg" || tipoDato[i]?.toLowerCase() == "jfif" || tipoDato[i]?.toLowerCase() == "pdf"  ) {

						extencion = tipoDato[i];
					}
				}
			}
			
			String nombreFile= (tipo == 1?"regAvatarName":"kardexPr")+info[4]+"."+extencion;
			
			//downloadedFile = new File(nombreFile);
			errorLog+="se creo el archivo"
			blob.download( new FileOutputStream(nombreFile))
	
	
			dataResult = ftpUpload2(nombreFile,carpeta);
			errorLog+=""+dataResult
			if (dataResult.success) {
				File file = new File(nombreFile);
				if (file.exists()){
					if(file.delete()) {
						errorLog+="Sea eliminado "+nombreFile;
					}
				 }
				
			} else {
				throw new Exception("No encontro datos:"+dataResult.getError());
			}
			result.setSuccess(true)
			result.setError_info(errorLog)
		}
		catch (StorageException ex)
		{
			errorLog += (String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
			result.setSuccess(false);
			//resultado.setError(e.getMessage());
			result.setError_info(errorLog)
		}
		catch (Exception ex)
		{
			errorLog += (ex.getMessage());
			result.setSuccess(false);
			//resultado.setError(e.getMessage());
			result.setError_info(errorLog)
		}
		finally
		{
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
			
			/*if (downloadedFile.exists()){
				downloadedFile.delete();
			 }*/
		}
		
		return result;
	}
	
	
	public Result ftpUpload2(String nameFile,String carpeta) {
		Result resultado = new Result();
		String errorLog = "";
		
		String server = "";
		int port = 21;
		String user = "";
		String pass = "";
		
		FTPClient ftpClient = new FTPClient();
		try {
			Result dataResult = new Result();
			List<Object> lstParams;
			
			dataResult = infoFTP();
			if (dataResult.success) {
				lstParams = dataResult.getData();
			} else {
				throw new Exception("Fallo al obtener la configuracion del ftp");
			}
			
			server = lstParams[0].dominio;
			port = Integer.parseInt(lstParams[0].puerto);
			user = lstParams[0].usuario;
			pass = lstParams[0].password;
			
			ftpClient.connect(server, port);
			ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();
			
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			
			String dirPath = "/exportacion excel/"+carpeta;
			boolean creado = makeDirectories(ftpClient, dirPath);
			
			if(creado) {
				InputStream inputStream = new FileInputStream(nameFile);
				
				errorLog+=("Start uploading first file "+nameFile);
				boolean done = ftpClient.storeFile(dirPath+"/"+nameFile, inputStream);
				inputStream.close();
				if (done) {
					
					errorLog+=("The file is uploaded successfully.");
				}
			}else {
				throw new Exception("Fallo al crear la carpeta");
			}
			
			
			resultado.setSuccess(true);
			
			
		} catch (IOException ex) {
			ex.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(ex.getMessage());
			
			ex.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return resultado;
	}
	
	
	public Result getListadoRegistroBecas( String jsonData, RestAPIContext context) {
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
			where+=" WHERE sda.iseliminado=false and (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado = false ) "
			if(object.campus != null){
				where+=" AND LOWER(campus.grupoBonita) = LOWER('"+object.campus+"') "
			}
			
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
			
			errorlog+="campus" + campus;
				errorlog+="object.lstFiltro" +object.lstFiltro
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				
				String SSA = "";
				pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
				rs= pstm.executeQuery();
				if(rs.next()) {
					SSA = rs.getString("valor")
				}
				
				String consulta = Statements.GET_REGISTRADOSBECAS
				
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					errorlog=consulta+" 1";
					switch(filtro.get("columna")) {
					
					case "NOMBRE,EMAIL,CURP":
						errorlog+="NOMBRE,EMAIL,CURP"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
						
					case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
						errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						/*where +=" ( LOWER(estado.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						*/
						where +="( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +="  OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
						
					case "CAMPUS,PROGRAMA,INGRESO":
						errorlog+="PROGRAMA,INGRESO,CAMPUS"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;
						
					case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
						errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +="( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						/*
						where +=" OR LOWER(sda.estadoextranjero) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						*/
						where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "INDICADORES":
						errorlog+="INDICADORES"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						
						where +=" ( LOWER(R.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(TA.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(TAL.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;
						
					// filtrados normales
					case "NÚMERO DE SOLICITUD":
						errorlog+="SOLICITUD"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(CAST(sda.caseid AS varchar)) ";
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
						
					case "ID BANNER":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(da.idbanner) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "FECHA REGISTRO":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" to_char(pr.fecharegistro::date, 'DD-MM-YYYY') ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					default:
					
					//consulta=consulta.replace("[BACHILLERATO]", bachillerato)
					//consulta=consulta.replace("[WHERE]", where);
					
					break;
					}
					
				}
				errorlog=consulta+" 2";
				switch(object.orderby) {
					case "RESIDEICA":
					orderby+="residensia";
					break;
					case "TIPODEADMISION":
					orderby+="tipoadmision";
					break;
					case "TIPODEALUMNO":
					orderby+="tipoDeAlumno";
					break;
					case "FECHAULTIMAMODIFICACION":
					orderby+="sda.fechaultimamodificacion";
					break;
					case "NOMBRE":
					orderby+="sda.apellidopaterno";
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
					orderby+="gestionescolar.NOMBRE"
					break;
					case "INGRESO":
					orderby+="periodo.DESCRIPCION"
					break;
					case "PROCEDENCIA":
					orderby +="CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END";
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
					case "IDBANNER":
					orderby+="da.idbanner";
					break;
					case "SOLICITUD":
					orderby+="sda.caseid::INTEGER";
					break;
					case "FECHASOLICITUD":
					orderby+="sda.fechasolicitudenviada";
					break;
					case "FECHAREGISTRO":
					orderby+="CAST(pr.fecharegistro as DATE)";
					break;
					default:
					orderby+="CAST(pr.fecharegistro as DATE)"
					break;
				}
				errorlog=consulta+" 3";
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[CAMPUS]", campus)
				consulta=consulta.replace("[PROGRAMA]", programa)
				consulta=consulta.replace("[INGRESO]", ingreso)
				consulta=consulta.replace("[ESTADO]", estado)
				consulta=consulta.replace("[BACHILLERATO]", bachillerato)
				consulta=consulta.replace("[TIPOALUMNO]", tipoalumno)
				
				where+=" "+campus +" "+programa +" " + ingreso + " " + estado +" "+bachillerato +" "+tipoalumno
				errorlog=consulta+" 4";
				
				
				consulta=consulta.replace("[WHERE]", where);
				errorlog=consulta+" 5";
				pstm = con.prepareStatement(consulta.replace(" CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, sda.fechaultimamodificacion, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura, PR.fecharegistro", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("GROUP BY pr.fecharegistro,prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.fechaultimamodificacion, sda.fechasolicitudenviada, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,da.tipoalumno,sda.caseid,sda.telefonocelular,da.observacioneslistaroja,da.observacionesrechazo,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid",""))
				
				errorlog=consulta+" 6";
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog=consulta+" 7";
				
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				errorlog=consulta+" 8";
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
							String encoded = "";
							try {
								String urlFoto = rs.getString("urlfoto");
								if(urlFoto != null && !urlFoto.isEmpty()) {
									columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
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
						}
					}
	
					rows.add(columns);
				}
				errorlog=consulta+" 9";
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
	
	
	
	
	public Result excelOnDeman(String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				Result dataResult = new Result();
				dataResult = excelPlantillaRegistro(object.fecha)
				errorLog +=" Registro:"+dataResult.getError_info();
				if(!dataResult.isSuccess()) {
					throw new Exception("Fallo en Registros:"+dataResult.getError());
				}
				
				dataResult = excelPlantillaHermanos(object.fecha);
				errorLog +=" Hermanos:"+dataResult.getError_info();
				if(!dataResult.isSuccess()) {
					throw new Exception("Fallo en Hermanos:"+dataResult.getError());
				}
				resultado.setSuccess(true);
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
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
		byte[] bytes = new byte[(int)length];
		
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
			   && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}
		
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "+file.getName());
		}
		
		is.close();
		return bytes;
	}
	
}
