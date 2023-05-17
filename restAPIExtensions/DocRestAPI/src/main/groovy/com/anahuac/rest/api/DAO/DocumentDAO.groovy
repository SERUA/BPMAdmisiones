package com.anahuac.rest.api.DAO

import com.bonitasoft.web.extension.rest.RestAPIContext

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import com.anahuac.catalogos.CatConfiguracion
import com.anahuac.catalogos.CatConfiguracionDAO
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utils.FileManagement

import groovy.json.JsonSlurper

class DocumentDAO {
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
	public Result getDocs(String jsonData = '{"type": "array", "items": {"type": "object", "properties": {"Id_banner": {"type": "string"} }, "required": ["00123456"] } }',RestAPIContext context) {
		Map<String, Serializable> datos = new HashMap<String, Serializable>();
		List < Map < String, Serializable >> rows = new ArrayList < Map < String, Serializable >> ();
		Result result = new Result();
		String errorLog="";
		Boolean closeCon = false;
		try {
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs = pstm.executeQuery();
			if (rs.next()) {
				SSA = rs.getString("valor")
			}
			errorLog+="[1] "
			def fm = new FileManagement()
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def num = Math.random();
			if(object?.type!="array") {
				throw new Exception("400 Bad Request type must be array");
			}
			if(object?.items.type!="object") {
				throw new Exception("400 Bad Request items type must be object");
			}
			if(object?.items.properties.Id_banner.type!="string") {
				throw new Exception("400 Bad Request items properties type must be string");
			}
			
			def records = object?.items.required.findAll { it instanceof  String }
			if(records.size!=object.items.required.size) {
				throw new Exception("400 Bad Request items required values must be string");
			}
			
			for(def i =0; i<object.items.required.size; i++) {
				datos = new HashMap<String, Serializable>();
				errorLog+=" | [2] "+ object.items.required[i]
				String [] info = new String[6];
				pstm = con.prepareStatement(Statements.DATOS_BY_IDBANNER)
				pstm.setString(1, object?.items?.required[i])
				rs = pstm.executeQuery();
				if (rs.next()) {
					info[0] = rs.getString("urlFoto")
					info[1] = rs.getString("urlConstancia")
					info[2] = rs.getString("urlActaNacimiento")
					info[3] = rs.getString("idbanner")
					info[4] = rs.getString("correoelectronico")
					info[5] = rs.getString("intentos")
				}else {
					throw new Exception("404 Record not found "+ object.items.required[i])
				}
				
				
				def foto = new HashMap<String, Serializable>();
				def constancia = new HashMap<String, Serializable>();
				def acta = new HashMap<String, Serializable>();
				def psicometrico = new HashMap<String, Serializable>();
				
				String[] extension = info[0].replace(".", "-").split('-')
				//foto.put("url", sda[0].urlFoto)
				foto.put("extension",extension[extension.length-1] )
				foto.put("content", fm.b64Url(info[0] + SSA + "&v="+num))
				
				String [] constanciaExt = info[1].replace('.', '-').split('-');
				//constancia.put("url", sda[0].urlConstancia);
				constancia.put("extension",constanciaExt[constanciaExt.length-1])
				constancia.put("content",fm.b64Url(info[1] + SSA + "&v="+num))
				
				String [] actaExt = info[2].replace('.', '-').split('-');
				//acta.put("url", sda[0].urlActaNacimiento);
				acta.put("extension", actaExt[actaExt.length-1])
				acta.put("content",fm.b64Url(info[2] + SSA + "&v="+num))
				
				String jsonSend = "{\"email\":\"${info[4]}\",\"intento\":${info[5]}}"
				String PDFpsico = new PDFDocumentDAO().PdfFileCatalogo(jsonSend, context)?.getData()?.get(0);
				psicometrico.put("extension", "pdf");
				psicometrico.put("content", PDFpsico);
				
				datos.put("idBanner", info[3]);
				datos.put("foto", foto);
				datos.put("constancia", constancia);
				datos.put("actaNacimiento", acta);
				datos.put("psicometrico", psicometrico);
				
				rows.add(datos)
			}
			result.setSuccess(true);
			result.setData(rows)
		}catch(Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
			result.setError_info(errorLog)
		}finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		
		
		return result;
	}
	
	public Result getDocsAval(String jsonData = '{"type": "array", "items": {"type": "object", "properties": {"Id_banner": {"type": "string"} }, "required": ["00123456"] } }',RestAPIContext context) {
		Map<String, Serializable> datos = new HashMap<String, Serializable>();
		List < Map < String, Serializable >> rows = new ArrayList < Map < String, Serializable >> ();
		Result result = new Result();
		String errorLog="";
		Boolean closeCon = false;
		try {
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs = pstm.executeQuery();
			if (rs.next()) {
				SSA = rs.getString("valor")
			}
			errorLog+="[1] "
			def fm = new FileManagement()
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def num = Math.random();
			if(object?.type!="array") {
				throw new Exception("400 Bad Request type must be array");
			}
			if(object?.items.type!="object") {
				throw new Exception("400 Bad Request items type must be object");
			}
			if(object?.items.properties.Id_banner.type!="string") {
				throw new Exception("400 Bad Request items properties type must be string");
			}
			
			def records = object?.items.required.findAll { it instanceof  String }
			if(records.size!=object.items.required.size) {
				throw new Exception("400 Bad Request items required values must be string");
			}
			
			for(def i =0; i<object.items.required.size; i++) {
				datos = new HashMap<String, Serializable>();
				errorLog+=" | [2] "+ object.items.required[i]
				String [] info = new String[6];
				pstm = con.prepareStatement(Statements.DATOS_BY_IDBANNER)
				pstm.setString(1, object?.items?.required[i])
				rs = pstm.executeQuery();
				if (rs.next()) {
					info[0] = rs.getString("urlFoto")
					info[1] = rs.getString("urlConstancia")
					info[2] = rs.getString("urlActaNacimiento")
					info[3] = rs.getString("idbanner")
					info[4] = rs.getString("correoelectronico")
					info[5] = rs.getString("intentos")
				}else {
					throw new Exception("404 Record not found "+ object.items.required[i])
				}
				
				
				def foto = new HashMap<String, Serializable>();
				def constancia = new HashMap<String, Serializable>();
				def acta = new HashMap<String, Serializable>();
				def psicometrico = new HashMap<String, Serializable>();
				
				String[] extension = info[0].replace(".", "-").split('-')
				//foto.put("url", sda[0].urlFoto)
				foto.put("extension",extension[extension.length-1] )
				foto.put("content", fm.b64Url(info[0] + SSA + "&v="+num))
				
				String [] constanciaExt = info[1].replace('.', '-').split('-');
				//constancia.put("url", sda[0].urlConstancia);
				constancia.put("extension",constanciaExt[constanciaExt.length-1])
				constancia.put("content",fm.b64Url(info[1] + SSA + "&v="+num))
				
				String [] actaExt = info[2].replace('.', '-').split('-');
				//acta.put("url", sda[0].urlActaNacimiento);
				acta.put("extension", actaExt[actaExt.length-1])
				acta.put("content",fm.b64Url(info[2] + SSA + "&v="+num))
				
				String jsonSend = "{\"email\":\"${info[4]}\",\"intento\":${info[5]}}"
				String PDFpsico = new PDFDocumentDAO().pdfDatosAval(jsonSend, context)?.getData()?.get(0);
				psicometrico.put("extension", "pdf");
				psicometrico.put("content", PDFpsico);
				
				datos.put("idBanner", info[3]);
				datos.put("foto", foto);
				datos.put("constancia", constancia);
				datos.put("actaNacimiento", acta);
				datos.put("psicometrico", psicometrico);
				
				rows.add(datos)
			}
			result.setSuccess(true);
			result.setData(rows)
		}catch(Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
			result.setError_info(errorLog)
		}finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		
		
		return result;
	}
}
