package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.document.DocumentValue
import org.bonitasoft.engine.bpm.document.impl.DocumentImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utilities.LoadParametros
import com.bonitasoft.engine.api.APIClient
import com.bonitasoft.web.extension.rest.RestAPIContext

class ProcessFileDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessFileDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Boolean validarConexion() {
		Boolean retorno = false;
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno = true;
		}
		return retorno
	}
	
	public Result obtenerDocumentos(Long caseId, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoDeleteDocument = new Result();
		
		List<Object> lstResultado = new ArrayList<Object>();
		Map<String, String> mapDocumentos = new LinkedHashMap<String, String>();
		Boolean closeCon = false;
		String fotoPasaporteURL = "";
		String actaNacimientoURL = "";
		String constanciaURL = "";
		String descuentoURL = "";
		String resultadoCBURL = "";
		Integer countConstancias = 0;
		
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
			
			closeCon = validarConexion();
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			apiClient.login(username, password);
			
			Map<String, String> fotoPasaporte = getFileBase64(apiClient, caseId, "fotoPasaporte");
			if(fotoPasaporte.get("b64").equals("") || fotoPasaporte.get("b64") == null) {
				mapDocumentos.put("fotoPasaporte","");
			} else {
				Result fotoPasaporteResult = uploadAzure(fotoPasaporte);
				mapDocumentos.put("fotoPasaporte", fotoPasaporteResult.getData().get(0));
			}
			
			Map<String, String> actaNacimiento = getFileBase64(apiClient, caseId, "actaNacimiento");
			if(actaNacimiento.get("b64").equals("") || actaNacimiento.get("b64") == null) {
				mapDocumentos.put("actaNacimiento","");
			} else {
				Result actaNacimientoResult = uploadAzure(actaNacimiento);
				mapDocumentos.put("actaNacimiento", actaNacimientoResult.getData().get(0));
			}
			
			Map<String, String> constancia = getFileBase64(apiClient, caseId, "constancia");
			if(constancia.get("b64").equals("") || constancia.get("b64") == null) {
				mapDocumentos.put("constancia","");
			} else {
				Result constanciaResult = uploadAzure(constancia);
				mapDocumentos.put("constancia", constanciaResult.getData().get(0));
				
				String insertConstancia = Statements.INSERT_CONSTANCIA;
				pstm = con.prepareStatement(insertConstancia);
				pstm.setLong(1, caseId);
				pstm.setString(2, constanciaResult.getData().get(0).toString());
				
				pstm.executeUpdate();
			}
			
			Map<String, String> descuento = getFileBase64(apiClient, caseId, "descuento");
			if(descuento.get("b64").equals("") || descuento.get("b64") == null) {
				mapDocumentos.put("descuento","");
			} else {
				Result descuentoResult = uploadAzure(descuento);
				mapDocumentos.put("descuento", descuentoResult.getData().get(0));
			}
			
			Map<String, String> resultadoCB = getFileBase64(apiClient, caseId, "resultadoCB");
			if(resultadoCB.get("b64").equals("") || resultadoCB.get("b64") == null) {
				mapDocumentos.put("resultadoCB","");
			} else {
				Result resultadoCBResult = uploadAzure(resultadoCB);
				mapDocumentos.put("resultadoCB", resultadoCBResult.getData().get(0));
			}
			
			Map<String, String> cartaAA = getFileBase64(apiClient, caseId, "cartaAA");
			if(cartaAA.get("b64").equals("") || cartaAA.get("b64") == null) {
				mapDocumentos.put("cartaAA","");
			} else {
				Result cartaAAResult = uploadAzure(cartaAA);
				mapDocumentos.put("cartaAA", cartaAAResult.getData().get(0));
			}
			
			closeCon = validarConexion();
			
			if(!mapDocumentos.get("fotoPasaporte").equals("")) {
				pstm = con.prepareStatement(Statements.UPDATE_FILE_URLFOTO);
				pstm.setString(1, mapDocumentos.get("fotoPasaporte"));
				pstm.setLong(2, caseId);
				pstm.executeUpdate();
			}
			
			if(!mapDocumentos.get("actaNacimiento").equals("")) {
				pstm = con.prepareStatement(Statements.UPDATE_FILE_URLACTANACIMIENTO);
				pstm.setString(1, mapDocumentos.get("actaNacimiento"));
				pstm.setLong(2, caseId);
				pstm.executeUpdate();
			}
			
			if(!mapDocumentos.get("constancia").equals("")) {
				pstm = con.prepareStatement(Statements.UPDATE_FILE_URLCONSTANCIA);
				pstm.setString(1, mapDocumentos.get("constancia"));
				pstm.setLong(2, caseId);
				pstm.executeUpdate();
			}
			
			if(!mapDocumentos.get("descuento").equals("")) {
				pstm = con.prepareStatement(Statements.UPDATE_FILE_URLDESCUENTOS);
				pstm.setString(1, mapDocumentos.get("descuento"));
				pstm.setLong(2, caseId);
				pstm.executeUpdate();
			}
			
			if(!mapDocumentos.get("resultadoCB").equals("")) {
				pstm = con.prepareStatement(Statements.UPDATE_FILE_URLRESULTADOPAA);
				pstm.setString(1, mapDocumentos.get("resultadoCB"));
				pstm.setLong(2, caseId);
				pstm.executeUpdate();
			}
			
			if(!mapDocumentos.get("cartaAA").equals("")) {
				pstm = con.prepareStatement(Statements.UPDATE_FILE_URLURLCARTAAA);
				pstm.setString(1, mapDocumentos.get("cartaAA"));
				pstm.setLong(2, caseId);
				pstm.executeUpdate();
			}
			
			new DBConnect().closeObj(con, stm, rs, pstm);
			
			resultadoDeleteDocument = deleteDocumentBonita(caseId);
			if(resultadoDeleteDocument.isSuccess()) {
				lstResultado.add(mapDocumentos);
				resultado.setData(lstResultado);
				resultado.setSuccess(true);
			}
			else {
				LOGGER.error resultadoDeleteDocument.getError();
				resultado.setSuccess(false)
				resultado.setError(resultadoDeleteDocument.getError());
			}			
			
		} catch(Exception ex) {
			LOGGER.error ex.getMessage()
			resultado.setSuccess(false)
			resultado.setError(ex.getMessage())
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		  
		return resultado;
	}
	
	public Result deleteDocumentBonita(Long caseId) {
		Result resultado = new Result();
		
		Boolean closeCon = false;
		
		List<Integer> lstDocumentId = new ArrayList<Integer>();
		
		try {
			closeCon = validarConexionBonita();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.GET_DOCUMENT_TO_DELETE);
			pstm.setLong(1, caseId);
			rs = pstm.executeQuery();
			while(rs.next()) {
				lstDocumentId.add(rs.getInt("documentid"));
			}
			
			if(lstDocumentId.size() > 0) {
				pstm = con.prepareStatement(Statements.DELETE_DOCUMENT.replace("[LSTDOCUMENTOID]", lstDocumentId.join(",")));
				pstm.executeUpdate();
				
				pstm = con.prepareStatement(Statements.DELETE_DOCUMENT_MAPPING);
				pstm.setLong(1, caseId);
				pstm.executeUpdate();
				
			}
			con.commit();
			con.setAutoCommit(true);
			resultado.setSuccess(true);
		} catch (Exception e) {
			con.rollback();
			con.setAutoCommit(true);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true
		}
		return retorno;
	}
	
	public Map<String, String> getFileBase64(org.bonitasoft.engine.api.APIClient apiClient, Long caseId, String fileName) {
		String fileBase64 = "";
		Boolean closeCon=false;
		String idbanner=caseId;
		Map<String, String> mapEnviarAzure = new LinkedHashMap<String, String>();
		try {
			List<DocumentImpl> lstDocument = apiClient.getProcessAPI().getDocumentList(caseId, fileName, 0, 1);
			if(lstDocument.size() > 0) {
				DocumentImpl archivo = lstDocument.get(0);
				Document archivoDoc = apiClient.getProcessAPI().getDocument(archivo.id);
				DocumentValue archivoDocValue = new DocumentValue(apiClient.getProcessAPI().getDocumentContent(archivoDoc.getContentStorageId()), archivoDoc.getContentMimeType(), archivoDoc.getContentFileName());
				fileBase64 = Base64.getEncoder().encodeToString(archivoDocValue.content);
				closeCon = validarConexion();
				/*pstm = con.prepareStatement(Statements.GET_IDBANNER);
				pstm.setString(1, caseId.toString());
				rs =pstm.executeQuery()
				if(rs.next()) {
					idbanner=rs.getString("idbanner")
				}*/
				
				mapEnviarAzure.put("b64", archivoDoc.getContentMimeType() + "," + fileBase64);
				mapEnviarAzure.put("filename", caseId + "/" + archivoDoc.getContentFileName());
				mapEnviarAzure.put("filetype", archivoDoc.getContentMimeType());
				mapEnviarAzure.put("contenedor", "privado");
				//esto estaba comentado y las otras dos funciones descomentadas
				/*if(!fileName.equals("fotoPasaporte")) {
					Document deletedDoc = apiClient.getProcessAPI().removeDocument(archivo.id);
					apiClient.getProcessAPI().deleteContentOfArchivedDocument(archivo.id);
				}*/
				
				if(fileName.equals("constancia")) {
					Integer countConstancias = 0;
					
					String consultaConstancia = Statements.GET_COUNT_CONSTANCIAS;
					pstm = con.prepareStatement(consultaConstancia);
					pstm.setLong(1, caseId);
	
					rs = pstm.executeQuery();
	
					if(rs.next()) {
						countConstancias = rs.getInt("countConstancias");
					}
					
					mapEnviarAzure.put("filename", caseId + "/v-" + (countConstancias + 1) + " " + archivoDoc.getContentFileName());
				}
			} else {
				throw new Exception("Documento no encontrado");
			}
		} catch(Exception e) {
			fileBase64 = e.getMessage();
			
		}finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return mapEnviarAzure;
	}
	
	public Result uploadAzure(Map<String, String> mapEnviarAzure) {
		Result result = new Result();
		String imageDataBytes = mapEnviarAzure.get("b64").substring(mapEnviarAzure.get("b64").indexOf(",") + 1);
		byte[] decodedBytes = Base64.getDecoder().decode(imageDataBytes);
		InputStream is = new ByteArrayInputStream(decodedBytes);
		result = new AzureBlobFileUpload().uploadFile(is, mapEnviarAzure.get("filename"), mapEnviarAzure.get("filetype"), mapEnviarAzure.get("contenedor"), decodedBytes.length);
		return result;
	}
	
	public Result obtenerDocumentosNoDelete(Long caseId, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoDeleteDocument = new Result();
		
		List<Object> lstResultado = new ArrayList<Object>();
		Map<String, String> mapDocumentos = new LinkedHashMap<String, String>();
		Boolean closeCon = false;
		String fotoPasaporteURL = "";
		String actaNacimientoURL = "";
		String constanciaURL = "";
		String descuentoURL = "";
		String resultadoCBURL = "";
		Integer countConstancias = 0;
		
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
			
			closeCon = validarConexion();
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			apiClient.login(username, password);
			
			Map<String, String> fotoPasaporte = getFileBase64(apiClient, caseId, "fotoPasaporte");
			if(fotoPasaporte.get("b64").equals("") || fotoPasaporte.get("b64") == null) {
				mapDocumentos.put("fotoPasaporte","");
			} else {
				Result fotoPasaporteResult = uploadAzure(fotoPasaporte);
				mapDocumentos.put("fotoPasaporte", fotoPasaporteResult.getData().get(0));
			}
			
			Map<String, String> actaNacimiento = getFileBase64(apiClient, caseId, "actaNacimiento");
			if(actaNacimiento.get("b64").equals("") || actaNacimiento.get("b64") == null) {
				mapDocumentos.put("actaNacimiento","");
			} else {
				Result actaNacimientoResult = uploadAzure(actaNacimiento);
				mapDocumentos.put("actaNacimiento", actaNacimientoResult.getData().get(0));
			}
			
			Map<String, String> constancia = getFileBase64(apiClient, caseId, "constancia");
			if(constancia.get("b64").equals("") || constancia.get("b64") == null) {
				mapDocumentos.put("constancia","");
			} else {
				Result constanciaResult = uploadAzure(constancia);
				mapDocumentos.put("constancia", constanciaResult.getData().get(0));
				
				String insertConstancia = Statements.INSERT_CONSTANCIA;
				pstm = con.prepareStatement(insertConstancia);
				pstm.setLong(1, caseId);
				pstm.setString(2, constanciaResult.getData().get(0).toString());
				
				pstm.executeUpdate();
			}
			
			Map<String, String> descuento = getFileBase64(apiClient, caseId, "descuento");
			if(descuento.get("b64").equals("") || descuento.get("b64") == null) {
				mapDocumentos.put("descuento","");
			} else {
				Result descuentoResult = uploadAzure(descuento);
				mapDocumentos.put("descuento", descuentoResult.getData().get(0));
			}
			
			Map<String, String> resultadoCB = getFileBase64(apiClient, caseId, "resultadoCB");
			if(resultadoCB.get("b64").equals("") || resultadoCB.get("b64") == null) {
				mapDocumentos.put("resultadoCB","");
			} else {
				Result resultadoCBResult = uploadAzure(resultadoCB);
				mapDocumentos.put("resultadoCB", resultadoCBResult.getData().get(0));
			}
			
			Map<String, String> cartaAA = getFileBase64(apiClient, caseId, "cartaAA");
			if(cartaAA.get("b64").equals("") || cartaAA.get("b64") == null) {
				mapDocumentos.put("cartaAA","");
			} else {
				Result cartaAAResult = uploadAzure(cartaAA);
				mapDocumentos.put("cartaAA", cartaAAResult.getData().get(0));
			}
			
			closeCon = validarConexion();
			
			if(!mapDocumentos.get("fotoPasaporte").equals("")) {
				pstm = con.prepareStatement(Statements.UPDATE_FILE_URLFOTO);
				pstm.setString(1, mapDocumentos.get("fotoPasaporte"));
				pstm.setLong(2, caseId);
				pstm.executeUpdate();
			}
			
			if(!mapDocumentos.get("actaNacimiento").equals("")) {
				pstm = con.prepareStatement(Statements.UPDATE_FILE_URLACTANACIMIENTO);
				pstm.setString(1, mapDocumentos.get("actaNacimiento"));
				pstm.setLong(2, caseId);
				pstm.executeUpdate();
			}
			
			if(!mapDocumentos.get("constancia").equals("")) {
				pstm = con.prepareStatement(Statements.UPDATE_FILE_URLCONSTANCIA);
				pstm.setString(1, mapDocumentos.get("constancia"));
				pstm.setLong(2, caseId);
				pstm.executeUpdate();
			}
			
			if(!mapDocumentos.get("descuento").equals("")) {
				pstm = con.prepareStatement(Statements.UPDATE_FILE_URLDESCUENTOS);
				pstm.setString(1, mapDocumentos.get("descuento"));
				pstm.setLong(2, caseId);
				pstm.executeUpdate();
			}
			
			if(!mapDocumentos.get("resultadoCB").equals("")) {
				pstm = con.prepareStatement(Statements.UPDATE_FILE_URLRESULTADOPAA);
				pstm.setString(1, mapDocumentos.get("resultadoCB"));
				pstm.setLong(2, caseId);
				pstm.executeUpdate();
			}
			
			if(!mapDocumentos.get("cartaAA").equals("")) {
				pstm = con.prepareStatement(Statements.UPDATE_FILE_URLURLCARTAAA);
				pstm.setString(1, mapDocumentos.get("cartaAA"));
				pstm.setLong(2, caseId);
				pstm.executeUpdate();
			}
			
			new DBConnect().closeObj(con, stm, rs, pstm);
			
		} catch(Exception ex) {
			LOGGER.error ex.getMessage()
			resultado.setSuccess(false)
			resultado.setError(ex.getMessage())
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		  
		return resultado;
	}
}
