package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatImageNotificacion
import com.anahuac.catalogos.CatImageNotificacionDAO
import com.anahuac.catalogos.CatNotificaciones
import com.anahuac.catalogos.CatNotificacionesDAO
import com.anahuac.catalogos.CatNotificacionesFirma
import com.anahuac.catalogos.CatRegistro
import com.anahuac.catalogos.CatRegistroDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.model.ProcesoCaso
import com.anahuac.model.ProcesoCasoDAO
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement

import org.bonitasoft.engine.bpm.document.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NotificacionDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificacionDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Result generateHtml(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		Long userLogged = 0L;
		Long caseId=0L;
		String encoded = "";
		String errorlog = "";
		
		String plantilla ="";
		String correo="",  asunto="",  body="",  cc="";
		try {
			Properties prop = new Properties();
			String propFileName = "configuration.properties";
			InputStream inputStream;
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			plantilla = prop.getProperty("plantilla")
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			
			userLogged = context.getApiSession().getUserId();
			errorlog += "Se obtuvo el usuario " + userLogged;
			def procesoCasoDAO = context.getApiClient().getDAO(ProcesoCasoDAO.class);
			ProcesoCaso procesoCaso = procesoCasoDAO.getCaseId(object.campus, "CatNotificaciones");
			errorlog += ", Despues con el campus " + object.campus + " se obtuvo el caseid " + procesoCaso.getCaseId()
			def catNotificacionesDAO = context.getApiClient().getDAO(CatNotificacionesDAO.class);
			CatNotificaciones catNotificaciones = catNotificacionesDAO.getCatNotificaciones(procesoCaso.getCaseId(),object.codigo)
			
			errorlog += ", se obtiene el catNotificaciones para generar el b64 del documento "

			errorlog += ",  catNotificacionDAO"

			errorlog += ",  lcn"
			List<Document> lstDoc = new ArrayList()
			try {
				lstDoc =context.getApiClient().getProcessAPI().getDocumentList(Long.valueOf(procesoCaso.getCaseId()), "imageHeader", 0, 10)
			} catch (Exception e) {
				errorlog += ", No hay imageHeader en " + object.campus +" con codigo " + object.codigo
			}
			errorlog += ", se genera el b64 banner"
			List<Document> docEtapaProceso = new ArrayList();
			try {
				docEtapaProceso = context.getApiClient().getProcessAPI().getDocumentList(Long.valueOf(procesoCaso.getCaseId()), "docEtapaProceso", 0, 100)
			} catch (Exception e) {
				errorlog += ", No hay docEtapaProceso en " + object.campus +" con codigo " + object.codigo
			}
			List<Document> docGuiaEstudio = new ArrayList();
			try {
				docGuiaEstudio = context.getApiClient().getProcessAPI().getDocumentList(Long.valueOf(procesoCaso.getCaseId()), "docGuiaEstudio", 0, 100)
			} catch (Exception e) {
				e.printStackTrace()
			}
			// 1 variable plantilla [banner-href]
			errorlog += ", Variable1"
			CatNotificaciones cn = catNotificacionesDAO.getCatNotificaciones(procesoCaso.getCaseId(),object.codigo)
			errorlog += ",  lstDoc"
			if(lstDoc.size()>0) {
				errorlog += ",  lstDoc.size="+lstDoc.size()
				for(Document doc : lstDoc) {
					if(doc.getContentFileName().equals(cn.getNombreImagenHeader())) {
						encoded ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
						// 2 variable plantilla [banner]
						errorlog += ", Variable2"
						plantilla=plantilla.replace("[banner]", encoded)
					}
				 }
			}
			errorlog+="seteando mensaje"
			
			plantilla=plantilla.replace("[banner-href]", cn.getEnlaceBanner())
			
			//3 variable plantilla [contacto]
			errorlog += ", Variable3"
			/*
			if(!cn.getEnlaceContacto().equals("")) {
				plantilla = plantilla.replace("<!--CONTACTO-->", prop.getProperty("plantilla_contacto"))
				plantilla=plantilla.replace("[contacto]", cn.getEnlaceContacto())
			}
			
			//4 variable plantilla [facebook-href]
			errorlog += ", Variable4"
			if(!cn.getEnlaceFacebook().equals("")) {
				plantilla = plantilla.replace("<!--FACEBOOK-->", "<td> <a href=\"[facebook-href]\"> <img style=\"width: 20px;\"src=\"https://i.ibb.co/vJmN84t/facebook.png\"> </a> </td>")
				plantilla=plantilla.replace("[facebook-href]",cn.getEnlaceFacebook())
			}
			
			//5 variable plantilla [instagram-href]
			errorlog += ", Variable5"
			if(!cn.getEnlaceInstagram().equals("")) {
				plantilla = plantilla.replace("<!--INSTAGRAM-->", "<td><a href=\"[instagram-href]\"> <img style=\"width: 25px;\"src=\"https://i.ibb.co/BHhs2y9/instagram.png\"> </a> </td>")
				plantilla=plantilla.replace("[instagram-href]", cn.getEnlaceInstagram())
			}
			//6 variable plantilla [twitter-href]
			errorlog += ", Variable6"
			if(!cn.getEnlaceTwitter().equals("")) {
				plantilla = plantilla.replace("<!--twitter-->", "<td><a href=\"[twitter-href]\"> <img style=\"width: 20px;\"src=\"https://i.ibb.co/X2hsqx4/twitter.png\"> </a> </td>")
				plantilla=plantilla.replace("[twitter-href]",cn.getEnlaceTwitter())
			}
			*/
			//7 variable plantilla [titulo]
			errorlog += ", Variable7"
			plantilla=plantilla.replace("[titulo]",cn.getTitulo())
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -7)
			int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			
			String dayOfMonthStr = String.valueOf(dayOfMonth);
			Integer mes = cal.get(Calendar.MONTH);
			String annio = Integer.toString(cal.get(Calendar.YEAR));
			String hora = (cal.get(Calendar.HOUR_OF_DAY)<10)?"0"+Integer.toString(cal.get(Calendar.HOUR_OF_DAY)):Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
			String minuto = (cal.get(Calendar.MINUTE)<10)?"0"+Integer.toString(cal.get(Calendar.MINUTE)):Integer.toString(cal.get(Calendar.MINUTE));
			plantilla=plantilla.replace("[DAY]",String.valueOf(dayOfMonth))
			String[] Month = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
			plantilla=plantilla.replace("[MONTH]",Month[mes])
			plantilla=plantilla.replace("[YEAR]",annio)
			plantilla=plantilla.replace("[HOUR]",hora)
			plantilla=plantilla.replace("[MIN]",minuto)
			
			//9 variable plantilla [contenido]
			errorlog += ", Variable9"
			if(!cn.getContenidoCorreo().equals("")) {
				plantilla=plantilla.replace("<!--[CONTENIDO]-->", "<table width=\"80%\"> <thead></thead> <tbody> <tr> <td class=\"col-12\"style=\"font-size: initial; font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif;\"> [contenido]</td> </tr> </tbody> </table>")
				plantilla=plantilla.replace("[contenido]", cn.getContenidoCorreo())
				plantilla=plantilla.replace("[firma]", cn.getContenido())
				plantilla=plantilla.replace("[HOST]", prop.getProperty("HOST"))
				if(object.mensaje != null) {
					errorlog += "mensaje " + object.mensaje
					plantilla = plantilla.replace("[MENSAJE]", object.mensaje);
				}
			}
			
			
			//8 Seccion table atributos usuario
			errorlog += ", Variable8.1 listado de correos copia"
			String tablaUsuario= ""
			String plantillaTabla="<tr> <td align= \"left \" valign= \"top \" style= \"text-align: justify; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #585858; font-size: 17px; line-height: 25px; \"> [clave] </span> </font> </td> <td align= \"left \" valign= \"top \" style= \"text-align: justify; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #ff5a00; font-size: 17px; line-height: 25px; \"> [valor] </span> </font> </td> </tr>"
			errorlog += ", Variable8.2 object.correo=" + object.correo
			correo=object.correo;
			errorlog += ", Variable8.3 cn.getAsunto()=" + cn.getAsunto()
			asunto=cn.getAsunto();
			errorlog += ", Variable8.4 cn.getLstCorreoCopia().size()=" + cn.getLstCorreoCopia().size()
			if(cn.getLstCorreoCopia().size()>0) {
				for(String row: cn.getLstCorreoCopia()) {
					if(cc == "") {
						cc = row
					}else {
						cc = cc + ";" + row
					}
				}
			}
			Boolean closeCon=false;
			try {
			closeCon = validarConexion();
			String ordenpago = ""
			String campus_id =""
			pstm = con.prepareStatement(Statements.GET_DETALLESOLICITUD)
			pstm.setString(1, object.correo)
			rs = pstm.executeQuery()
				if (rs.next()) {
					errorlog += ", Variable15.1"
					plantilla=plantilla.replace("[IDBANNER]",rs.getString("IdBanner")==null?"":rs.getString("IdBanner"))
					errorlog += ", Variable15.2"
					plantilla=plantilla.replace("[RECHAZO-COMENTARIOS]",rs.getString("ObservacionesRechazo")==null?"[RECHAZO-COMENTARIOS]":(object.isEnviar)?rs.getString("ObservacionesRechazo"):"[RECHAZO-COMENTARIOS]")
					errorlog += ", Variable15.3"
					plantilla=plantilla.replace("[LISTAROJA-COMENTARIOS]",rs.getString("ObservacionesListaRoja")==null?"[LISTAROJA-COMENTARIOS]":(object.isEnviar)?rs.getString("ObservacionesListaRoja"):"[LISTAROJA-COMENTARIOS]")
					errorlog += ", Variable15.3"
					plantilla=plantilla.replace("[COMENTARIOS-CAMBIO]", rs.getString("ObservacionesCambio")==null?"[COMENTARIOS-CAMBIO]": (object.isEnviar)?rs.getString("ObservacionesCambio"):"[COMENTARIOS-CAMBIO]")
					ordenpago = rs.getString("ordenpago")==null?"": rs.getString("ordenpago")
					
					if(!ordenpago.equals("")) {
						errorlog += ", campusid"
						pstm = con.prepareStatement(Statements.GET_CAMPUS_ID_FROM_CLAVE)
						pstm.setString(1, object.campus)
						rs = pstm.executeQuery()
						if(rs.next()) {
							
							campus_id = rs.getString("campus_id")==null?"": rs.getString("campus_id")
							errorlog += ", se obtuvo el campusid"+campus_id
							resultado = new ConektaDAO().getOrderDetails(0, 999, "{\"order_id\":\""+ordenpago+"\", \"campus_id\":\""+campus_id+"\"}", context)
							errorlog += ", se va castear map string string por data"
							Map<String, String> conektaData =(Map<String, String>) resultado.getData().get(0)
							errorlog += ", casteo exitoso"
							plantilla=plantilla.replace("[MONTO]", conektaData.get("amount")==null?"": conektaData.get("amount"))
							plantilla=plantilla.replace("[TRANSACCION]", conektaData.get("authorizationCode")==null?"": conektaData.get("authorizationCode"))
							plantilla=plantilla.replace("[METODO]", conektaData.get("type")==null?"": conektaData.get("type"))
						}
						
					}
					
				}
			}catch(Exception ex) {
				errorlog +=", consulta custom " + ex.getMessage();
			}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
				
			}
			errorlog += ", Variable8.5 DataUsuarioAdmision"
			plantilla = DataUsuarioAdmision(plantilla, context, correo, cn, errorlog);
			errorlog += ", Variable8.6 DataUsuarioRegistro"
			plantilla = DataUsuarioRegistro(plantilla, context, correo, cn, errorlog);

			String tablaPasos=""
			String plantillaPasos="<tr> <td class= \"col-xs-1 col-sm-1 col-md-1 col-lg-1 text-center aling-middle backgroundOrange color-index number-table \"> [numero]</td> <td class= \"col-xs-4 col-sm-4 col-md-4 col-lg-4 text-center aling-middle backgroundDGray \"> <div class= \"row \"> <div class= \"col-12 form-group color-titulo \"> <img src= \"[imagen] \"> </div> <div class= \"col-12 color-index sub-img \"style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; \"> [titulo] </div> </div> </td> <td class= \"col-xs-7 col-sm-7 col-md-7 col-lg-7 col-7 text-justify aling-middle backgroundLGray \"style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; \"> [descripcion] </td> </tr>"
			
			def catImageNotificacion = context.apiClient.getDAO(CatImageNotificacionDAO.class);
			errorlog += ", Variable9.1 catImageNotificacion.findByCaseId"
			List<CatImageNotificacion> lci = catImageNotificacion.findByCaseId(Long.valueOf(procesoCaso.getCaseId()), 0, 999)
			Integer numero= 0;
			errorlog += ", Variable9.2 lci.size()=" + lci.size()
			if(lci.size()>0) {
				plantilla= plantilla.replace("<!--[PASOS]-->", "<table class=\"table table-bordered\"> <tbody> [pasos] </tbody> </table>")
				for(CatImageNotificacion ci: lci) {
					if(ci.getCodigo().equals(cn.getCodigo())) {
					numero++
					errorlog += ", Variable10."+numero
					String imagen= "";
					//Descripcion es el nombre del documento
					errorlog += ", Variable10.1 doc=" + ci.getDescripcion()
					if(docEtapaProceso.size()>0) {
						for(Document doc:docEtapaProceso) {
								errorlog += ", Variable10.1 doc=" + ci.getDescripcion()+"= doc.getName()="+ doc.getContentFileName()
								if(doc.getContentFileName().equals(ci.getDescripcion())) {
									imagen ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
								}
							}
							tablaPasos += plantillaPasos.replace("[imagen]", imagen).replace("[numero]", numero+"").replace("[titulo]", ci.getTitulo()).replace("[descripcion]", ci.getTexto())
						}
					}
				}
			}
			errorlog += ", Variable11"
			plantilla=plantilla.replace("[pasos]", tablaPasos)
			errorlog += ", Variable12"
			String guia=cn.getDocGuiaEstudio()==null?"":cn.getDocGuiaEstudio();
			/*for(Document doc:docGuiaEstudio) {
				errorlog += ", Variable10.1 doc.getName()="+ doc.getContentFileName()
				if(cn.getDocGuiaEstudio().equals(doc.getContentFileName())) {
					guia ="src=\"data:application/octet-stream;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId)) +"\" download=\""+doc.getContentFileName() +"\""
				}
				
				
			}*/
			if(!guia.equals("")) {
				plantilla=plantilla.replace("<!-- GUIA DE ESTUDIO-->", "<table width=\"80%\" style=\"padding-bottom: 0; margin-bottom: 0; \"> <tbody> <tr style=\"text-align: center; font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif;\"> <td> <div class=\"row\"> <div class=\"col-12\"> <label style=\"color:orange; font-size:23em;\"> Guía de estudio </label> <hr style=\"border-top: 1px solid orange; width: 40%;\"> </div> <div class=\"col-12\"> <p style=\"font-size:15em; color:black;\"> Hemos preparado para ti esta guía y algunos tips que te ayudarán a prepararte para tu examen de admisión. </p> </div> </div> </td> </tr> </tbody> </table> <table width=\"80%\"> <thead></thead> <tbody> <tr style=\"text-align: center;\"> <td class=\"col-12\"style=\"background: #fbcf80; font-size: initial; font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; padding-left: 43%; padding-bottom: 0;\"> <div class=\"row\" style=\"background-color: orange; width: 40%;\"> <div class=\"col-12 form-group color-titulo\"> <img src=\"https://i.ibb.co/JHDk1zt/guia.png\"> </div> <div class=\"col-12 color-index sub-img\"style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif;\"> <a href=\"[guia-src]\" target=\"_blank\"  style=\"text-decoration: underline; cursor: pointer;\">Guia para el examén</a> </div> </div> </td> </tr> </tbody> </table><hr>")
				plantilla=plantilla.replace("[guia-src]", guia)
			}
			
			errorlog += ", Variable13"
			if(!cn.getContenidoLeonel().equals("") ) {
				plantilla=plantilla.replace("<!--Leonel-->", "<table width=\"80%\"> <thead></thead> <tbody> <tr> <td width=\"25%\" style=\"text-align: right;\"> <img style=\"width: 57%;\"src=\"https://i.ibb.co/g4NJKJz/leonel-1.png\"> </td> <td class=\"col-6\"> <div class=\"arrow_box\" style=\"position: relative; background: rgb(255, 131, 0); border: 4px solid rgb(255, 131, 0);\"> <h6 class=\"logo\"style=\"font-size: 12px; padding: 10px; color: white; font-weight: 500;font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif;\"> [leonel]</h6> </div> </td> </tr> </tbody> </table>")
				plantilla=plantilla.replace("[leonel]", cn.getContenidoLeonel())
			}
			
			errorlog += ", Variable15"
			
			encoded=""
			try {
				for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.valueOf(procesoCaso.getCaseId()), "imageFooter", 0, 10)) {
					if(doc.getContentFileName().equals(cn.getNombreImagenFooter())) {
						encoded ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
						plantilla=plantilla.replace("[imgFooter]", encoded)
					}
				}
			} catch (Exception e) {
				errorlog += ", No se encuentra la imageFooter de campus " + object.campus + " con codigo " + object.codigo 
			}
			
			   
			 
			plantilla=plantilla.replace("[TEXTO-FOOTER]", cn.getTextoFooter())
			plantilla=plantilla.replace("[footer-href]", cn.getEnlaceFooter())
			List<String> lstData = new ArrayList();
			List<String> lstAdditionalData = new ArrayList();
			lstData.add(plantilla);
			resultado.setData(lstData);
			
			MailGunDAO mgd = new MailGunDAO();
			lstAdditionalData.add("correo="+correo)
			lstAdditionalData.add("asunto="+asunto)
			lstAdditionalData.add("cc="+cc)
			if(object.isEnviar) {
				resultado = mgd.sendEmailPlantilla(correo, asunto, plantilla.replace("\\", ""), cc, object.campus, context)
			}
			resultado.setError_info(errorlog)
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage())
			resultado.setError_info(errorlog)
			e.printStackTrace()
		}
		
		return resultado;
	}

			private String DataUsuarioAdmision(String plantilla, RestAPIContext context, String correo, CatNotificaciones cn, String errorlog) {
			//8 Seccion table atributos usuario
		    errorlog += ", Variable8"
			String tablaUsuario= ""
			String plantillaTabla="<tr> <td align= \"left \" valign= \"top \" style= \"text-align: justify;vertical-align: bottom; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #585858; font-size: 17px; line-height: 25px; \"> [clave]: </span> </font> </td> <td align= \"left \" valign= \"top \" style= \"text-align: justify; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #ff5a00; font-size: 17px; line-height: 25px;vertical-align: bottom; \"> [valor] </span> </font> </td> </tr>"
			try {
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			List<SolicitudDeAdmision> objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(correo, 0, 999)
			if(objSolicitudDeAdmision.size()>0) {
				for(String variables:cn.getLstVariableNotificacion()) {
					if(variables.equals("Nombre")) {
						tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getPrimerNombre()+" "+objSolicitudDeAdmision.get(0).getSegundoNombre()+" "+objSolicitudDeAdmision.get(0).getApellidoPaterno()+" "+objSolicitudDeAdmision.get(0).getApellidoMaterno())
					}
					if(variables.equals("Universidad")){
						tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getCatCampus().getDescripcion())
					}
					if(variables.equals("Licenciatura")){
						tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().getDescripcion())
					}
					if(variables.equals("Campus de examen")){
						tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getCatCampus().getDescripcion())
					}
					if(variables.equals("Correo")){
						tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getCorreoElectronico())
					}
					if(variables.equals("Periodo")){
						tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getCatPeriodo().getDescripcion())
					}
					if(variables.equals("Preparatoria")){
						tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", (objSolicitudDeAdmision.get(0).getCatBachilleratos().getDescripcion().equals("Otro"))?objSolicitudDeAdmision.get(0).getBachillerato():objSolicitudDeAdmision.get(0).getCatBachilleratos().getDescripcion())
					}
					if(variables.equals("Promedio")){
						tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getPromedioGeneral())
					}
					if(variables.equals("Estatus de tu solicitud")){
						tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getEstatusSolicitud())
					}
					if(variables.equals("ID Banner")){
						try {
						def objDetalleSolicitudDAO = context.getApiClient().getDAO(DetalleSolicitudDAO.class)
						List<DetalleSolicitud> detalleSolicitud = objDetalleSolicitudDAO.findByCaseId(objSolicitudDeAdmision.get(0).getCaseId()+"", 0, 999)
						if(detalleSolicitud.size()>0) {
							tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", detalleSolicitud.get(0).getIdBanner())
						}
						}catch(Exception e) {
							errorlog+= " e" + e.getMessage()
						}
					}
					
				}
				plantilla=plantilla.replace("Estimado(a)", objSolicitudDeAdmision.get(0).getCatSexo().getDescripcion().equals("Masculino")?"Estimado":"Estimada")
				plantilla=plantilla.replace("[NOMBRE]",objSolicitudDeAdmision.get(0).getPrimerNombre()+" "+objSolicitudDeAdmision.get(0).getSegundoNombre()+" "+objSolicitudDeAdmision.get(0).getApellidoPaterno()+" "+objSolicitudDeAdmision.get(0).getApellidoMaterno())
				plantilla=plantilla.replace("[UNIVERSIDAD]", objSolicitudDeAdmision.get(0).getCatCampus().getDescripcion())
				plantilla=plantilla.replace("[LICENCIATURA]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().getDescripcion())
				plantilla=plantilla.replace("[CAMPUSEXAMEN]",objSolicitudDeAdmision.get(0).getCatCampus().getDescripcion())
				plantilla=plantilla.replace("[CAMPUS]",objSolicitudDeAdmision.get(0).getCatCampus().getDescripcion())
				plantilla=plantilla.replace("[CORREO]",objSolicitudDeAdmision.get(0).getCorreoElectronico())
				plantilla=plantilla.replace("[PERIODO]",objSolicitudDeAdmision.get(0).getCatPeriodo().getDescripcion())
				plantilla=plantilla.replace("[PREPARATORIA]",(objSolicitudDeAdmision.get(0).getCatBachilleratos().getDescripcion().equals("Otro"))?objSolicitudDeAdmision.get(0).getBachillerato():objSolicitudDeAdmision.get(0).getCatBachilleratos().getDescripcion())
				plantilla=plantilla.replace("[PROMEDIO]",objSolicitudDeAdmision.get(0).getPromedioGeneral())
				plantilla=plantilla.replace("[ESTATUS]",objSolicitudDeAdmision.get(0).getEstatusSolicitud())
				errorlog += ", Variable14"
				
				if(cn.isInformacionLic()) {
					errorlog += ", Variable14.1"
					plantilla=plantilla.replace("<!--isInformacionLic-->", "<table width=\"80%\"> <tbody> <tr style=\"text-align: center;\"> <td class=\"col-4\" style=\"width:33.33%;margin: 0; padding:0; vertical-align: middle;\"> <img src=\"https://i.ibb.co/RbptTkY/licenciatura.jpg\"> </td> <td class=\"col-4\"style=\"width:33.33%; background: #bf6d27; vertical-align: middle; padding: 0; margin: 0;\"> <div class=\"row\"> <div class=\"col-12 form-group color-titulo\"> <img src=\"https://i.ibb.co/C8yv3pD/sello.png\"> </div> <div class=\"col-12 color-index sub-img\"style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif;\"> <a style=\"text-decoration: underline; cursor: pointer;\">[LICENCIATURA]</a> </div> </div> </td> <td class=\"col-4\"style=\"width:33.33%; text-decoration: underline; font-size: 9px; background: orange; color: white; font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; margin: 0; padding:0; vertical-align: middle;\"> <p>[descripcion-licenciatura] </p> </td> </tr> </tbody> </table>")
					errorlog += ", Variable14.2"
					plantilla=plantilla.replace("[LICENCIATURA]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().getNombre())
					errorlog += ", Variable14.3"
					plantilla=plantilla.replace("[descripcion-licenciatura]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().getDescripcion())
				}
				errorlog += ", Variable15"
				try {
				def objDetalleSolicitudDAO = context.getApiClient().getDAO(DetalleSolicitudDAO.class)
				errorlog += ", Variable15.0"
				List<DetalleSolicitud> detalleSolicitud = objDetalleSolicitudDAO.findByCaseId(objSolicitudDeAdmision.get(0).getCaseId()+"", 0, 999)
				if(detalleSolicitud.size()>0) {
					errorlog += ", Variable15.1"
					plantilla=plantilla.replace("[IDBANNER]",detalleSolicitud.get(0).getIdBanner())
					errorlog += ", Variable15.2"
					plantilla=plantilla.replace("[RECHAZO-COMENTARIOS]",detalleSolicitud.get(0).getObservacionesRechazo())
					errorlog += ", Variable15.3"
					plantilla=plantilla.replace("[LISTAROJA-COMENTARIOS]",detalleSolicitud.get(0).getObservacionesListaRoja())
					errorlog += ", Variable15.3"
					plantilla=plantilla.replace("[COMENTARIOS-CAMBIO]", detalleSolicitud.get(0).getObservacionesCambio())
				}}catch(Exception e) {
				errorlog+=" e2" + e.getMessage()
				Boolean closeCon=false;
				try {
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_DETALLESOLICITUD)
				pstm.setString(1, objSolicitudDeAdmision.get(0).getCaseId()+"")
				rs = pstm.executeQuery()
					if (rs.next()) {
						errorlog += ", Variable15.1"
						plantilla=plantilla.replace("[IDBANNER]",rs.getString("IdBanner"))
						errorlog += ", Variable15.2"
						plantilla=plantilla.replace("[RECHAZO-COMENTARIOS]",rs.getString("ObservacionesRechazo"))
						errorlog += ", Variable15.3"
						plantilla=plantilla.replace("[LISTAROJA-COMENTARIOS]",rs.getString("ObservacionesListaRoja"))
						errorlog += ", Variable15.3"
						plantilla=plantilla.replace("[COMENTARIOS-CAMBIO]", rs.getString("ObservacionesCambio"))
					}
				}catch(Exception ex) {
					
				}finally {
					if(closeCon) {
						new DBConnect().closeObj(con, stm, rs, pstm);
						}
					
					}
				}
			}
			errorlog += ", Variable10 tablaUsuario"
			if(!tablaUsuario.equals("")) {
				plantilla = plantilla.replace("<!--[Variables de usuario]-->", "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"88%\"style=\"width: 88% !important; min-width: 88%; max-width: 88%; padding-left: 50px;padding-right: 50px;\"> [getLstVariableNotificacion] </table>")
				plantilla=plantilla.replace("[getLstVariableNotificacion]", tablaUsuario)
			}
			} catch (Exception e) {
				e.printStackTrace()
			}
			plantilla=plantilla.replace("[getLstVariableNotificacion]", tablaUsuario)
			return plantilla
		}
		
		private String DataUsuarioRegistro(String plantilla, RestAPIContext context, String correo, CatNotificaciones cn, String errorlog) {
			//8 Seccion table atributos usuario
			errorlog += ", Variable8"
			String tablaUsuario= ""
			String plantillaTabla="<tr> <td align= \"left \" valign= \"top \" style= \"text-align: justify;vertical-align: bottom; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #585858; font-size: 17px; line-height: 25px; \"> [clave]: </span> </font> </td> <td align= \"left \" valign= \"top \" style= \"text-align: justify;vertical-align: bottom; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #ff5a00; font-size: 17px; line-height: 25px; \"> [valor] </span> </font> </td> </tr>"
			try {
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			List<CatRegistro> objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoelectronico(correo, 0, 99)
			if(objSolicitudDeAdmision.size()>0) {
				for(String variables:cn.getLstVariableNotificacion()) {
					if(variables.equals("Nombre")) {
						tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getPrimernombre()+" "+objSolicitudDeAdmision.get(0).getSegundonombre()+" "+objSolicitudDeAdmision.get(0).getApellidopaterno()+" "+objSolicitudDeAdmision.get(0).getApellidomaterno())
					}

					if(variables.equals("Correo")){
						tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getCorreoelectronico())
					}
					
					
				}
				plantilla=plantilla.replace("[NOMBRE]",objSolicitudDeAdmision.get(0).getPrimernombre()+" "+objSolicitudDeAdmision.get(0).getSegundonombre()+" "+objSolicitudDeAdmision.get(0).getApellidopaterno()+" "+objSolicitudDeAdmision.get(0).getApellidomaterno())
				plantilla=plantilla.replace("[CORREO]",objSolicitudDeAdmision.get(0).getCorreoelectronico())
				errorlog += ", Variable14"
			}
			errorlog += ", Variable10 tablaUsuario"
			if(!tablaUsuario.equals("")) {
				plantilla = plantilla.replace("<!--[Variables de usuario]-->", "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"88%\"style=\"width: 88% !important; min-width: 88%; max-width: 88%; padding-left: 50px;padding-right: 50px;\"> [getLstVariableNotificacion] </table>")
				plantilla=plantilla.replace("[getLstVariableNotificacion]", tablaUsuario)
			}}catch(Exception e) {
			errorlog+="Fallo en datausuario" + e.getMessage()
			}
			return plantilla
		}
		
		public Result getDocumentoTest(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
			Result resultado = new Result();
			Long caseId =11001L;
			Connection con;
			Statement stm;
			ResultSet rs;
			PreparedStatement pstm;
			try {
				
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				assert object instanceof Map;
				
				con = new DBConnect().getConnection();
				pstm = con.prepareStatement(Statements.DELETE_CATGESTIONESCOLAR)
				pstm.setLong(1, Long.valueOf(object.persistenceid.toString()))
				pstm.execute()
	
				} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
			}finally {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
			return resultado
		}
		public Result insertLicenciatura(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
			Result resultado = new Result();
			try {
				
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				assert object instanceof List;
				
				con = new DBConnect().getConnection();
				for(def row: object) {
					pstm = con.prepareStatement(row)
					//pstm.setString(1, "165")
					//pstm.setString(2, "Administración Pública y Gobierno")
					//pstm.setString(3, "2020-11-16")
					//pstm.setBoolean(4, false)
					//pstm.setString(5, "1")
					//pstm.setLong(6, 1L)
					//pstm.setLong(7, 1L)
					pstm.execute()
				}
				resultado.setSuccess(true);
				resultado.setError_info("Statement ejecutado correctamente ")
				} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
			}finally {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
			return resultado
		}

		public Result insertLicenciaturaBonita(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
			Result resultado = new Result();
			try {
				
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				assert object instanceof List;
				
				con = new DBConnect().getConnectionBonita();
				for(def row: object) {
					pstm = con.prepareStatement(row)
					//pstm.setString(1, "165")
					//pstm.setString(2, "Administración Pública y Gobierno")
					//pstm.setString(3, "2020-11-16")
					//pstm.setBoolean(4, false)
					//pstm.setString(5, "1")
					//pstm.setLong(6, 1L)
					//pstm.setLong(7, 1L)
					pstm.execute()
				}
				resultado.setSuccess(true);
				resultado.setError_info("Statement ejecutado correctamente ")
				} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
			}finally {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
			return resultado
		}
		
	public Result simpleSelect(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof List;
			
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				for(def row: object) {
				pstm = con.prepareStatement(row)
				
				
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
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				}
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
	
	public Result simpleSelectBonita(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof List;
			
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexionBonita();
				for(def row: object) {
				pstm = con.prepareStatement(row)
				
				
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
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				}
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
	public Result getFirma(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby="ORDER BY ", errorlog=""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatNotificacionesFirma firma = new CatNotificacionesFirma()
			List<CatNotificacionesFirma> rows = new ArrayList<CatNotificacionesFirma>();
			closeCon = validarConexion();
			
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					case "PERSISTENCEID":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(PERSISTENCEID) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "CARGO":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(CARGO) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "CORREO":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(CORREO) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "GRUPO":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(GRUPO) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "NOMBRECOMPLETO":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(NOMBRECOMPLETO) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "TELEFONO":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(TELEFONO) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "TITULO":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(TITULO) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "PREVIEW":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(PREVIEW) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "EDITAR":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(EDITAR) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
				}
			}
			switch(object.orderby) {
				case "PERSISTENCEID":
				orderby+="PERSISTENCEID";
									break;
				case "CARGO":
				orderby+="CARGO";
									break;
				case "CORREO":
				orderby+="CORREO";
									break;
				case "GRUPO":
				orderby+="GRUPO";
									break;
				case "NOMBRECOMPLETO":
				orderby+="NOMBRECOMPLETO";
									break;
				case "TELEFONO":
				orderby+="TELEFONO";
									break;
				case "TITULO":
				orderby+="TITULO";
									break;
				case "PREVIEW":
				orderby+="PREVIEW";
									break;
				case "EDITAR":
				orderby+="EDITAR";
									break;	
				default:
				orderby+="PERSISTENCEID";
				break;
			}
			orderby+=" "+object.orientation;
			String consulta = Statements.GET_CAT_NOTIFICACION_FIRMA;
			consulta=consulta.replace("[WHERE]", where);
			errorlog+="consulta:"
			errorlog+=consulta.replace("PERSISTENCEID, CARGO, CORREO, GRUPO, NOMBRECOMPLETO, PERSISTENCEVERSION, SHOWCARGO, SHOWCORREO, SHOWGRUPO, SHOWTELEFONO, SHOWTITULO, TELEFONO, TITULO", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "")
				pstm = con.prepareStatement(consulta.replace("PERSISTENCEID, CARGO, CORREO, GRUPO, NOMBRECOMPLETO, PERSISTENCEVERSION, SHOWCARGO, SHOWCORREO, SHOWGRUPO, SHOWTELEFONO, SHOWTITULO, TELEFONO, TITULO", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs = pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+="consulta:"
				errorlog+=consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()
				while(rs.next()) {
					firma = new CatNotificacionesFirma()
					firma.setCargo(rs.getString("cargo"))
					firma.setCorreo(rs.getString("correo"))
					firma.setGrupo(rs.getString("grupo"))
					firma.setNombreCompleto(rs.getString("nombreCompleto"))
					firma.setPersistenceId(rs.getLong("persistenceId"))
					firma.setPersistenceVersion(rs.getLong("persistenceVersion"))
					firma.setShowCargo(rs.getBoolean("showCargo"))
					firma.setShowCorreo(rs.getBoolean("showCorreo"))
					firma.setShowGrupo(rs.getBoolean("showGrupo"))
					firma.setShowTelefono(rs.getBoolean("showTelefono"))
					firma.setShowTitulo(rs.getBoolean("showTitulo"))
					firma.setTelefono(rs.getString("telefono"))
					firma.setTitulo(rs.getString("titulo"))
					rows.add(firma)
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Result insertFirma(CatNotificacionesFirma firma) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
				List<CatNotificacionesFirma> rows = new ArrayList<CatNotificacionesFirma>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.INSERT_CAT_NOTIFICACIONES_FIRMA)
				pstm.setString(1,firma.getCargo())
				pstm.setString(2,firma.getCorreo())
				pstm.setString(3,firma.getGrupo())
				pstm.setString(4,firma.getNombreCompleto())
				/*pstm.setLong(5,firma.getPersistenceId())
				pstm.setString(6,firma.getPersistenceVersion())*/
				pstm.setBoolean(5,firma.isShowCargo())
				pstm.setBoolean(6,firma.isShowCorreo())
				pstm.setBoolean(7,firma.isShowGrupo())
				pstm.setBoolean(8,firma.isShowTelefono())
				pstm.setBoolean(9,firma.isShowTitulo())
				pstm.setString(10,firma.getTelefono())
				pstm.setString(11,firma.getTitulo())
				//pstm.setLong(12,firma.getPersistenceId())
				
				rs = pstm.executeQuery()
				if(rs.next()) {
					firma.setPersistenceId(rs.getLong("persistenceId"))
					rows.add(firma)
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
	public Result updateFirma(CatNotificacionesFirma firma) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
				List<CatNotificacionesFirma> rows = new ArrayList<CatNotificacionesFirma>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.UPDATE_CAT_NOTIFICACIONES_FIRMA)
				pstm.setString(1,firma.getCargo())
				pstm.setString(2,firma.getCorreo())
				pstm.setString(3,firma.getGrupo())
				pstm.setString(4,firma.getNombreCompleto())
				/*pstm.setLong(5,firma.getPersistenceId())
				pstm.setString(6,firma.getPersistenceVersion())*/
				pstm.setBoolean(5,firma.isShowCargo())
				pstm.setBoolean(6,firma.isShowCorreo())
				pstm.setBoolean(7,firma.isShowGrupo())
				pstm.setBoolean(8,firma.isShowTelefono())
				pstm.setBoolean(9,firma.isShowTitulo())
				pstm.setString(10,firma.getTelefono())
				pstm.setString(11,firma.getTitulo())
				pstm.setLong(12,firma.getPersistenceId())
				
				pstm.execute()
				
				resultado.setSuccess(true)
				rows.add(firma)
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
	
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
	}
	
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true
		}
	}
}