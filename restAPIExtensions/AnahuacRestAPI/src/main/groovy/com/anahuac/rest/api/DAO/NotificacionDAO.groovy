package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatDocumentosTextos
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
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.db.CatBitacoraCorreo
import com.anahuac.rest.api.Entity.db.CatNotificacionesCampus
import com.anahuac.rest.api.Utilities.LoadParametros
import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper
import javax.imageio.ImageIO
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement

import org.apache.commons.codec.net.BCodec
import org.bonitasoft.engine.bpm.document.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NotificacionDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificacionDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	float costo1=0,costo2=0,costo3=0,costo4=0
	String periodo =""
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
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			/*-------------------------------------------------------------*/
			
			
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			
			userLogged = context.getApiSession().getUserId();
			errorlog += "| Se obtuvo el usuario " + userLogged;
			CatNotificaciones catNotificaciones= null;
			ProcesoCaso procesoCaso = new ProcesoCaso()
			CatNotificaciones cn = new CatNotificaciones()
			try {
				def procesoCasoDAO = context.getApiClient().getDAO(ProcesoCasoDAO.class);
				procesoCaso = procesoCasoDAO.getCaseId(object.campus, "CatNotificaciones");
				errorlog += "| Despues con el campus " + object.campus + " se obtuvo el caseid " + procesoCaso.getCaseId()
				def catNotificacionesDAO = context.getApiClient().getDAO(CatNotificacionesDAO.class);
				catNotificaciones = catNotificacionesDAO.getCatNotificaciones(procesoCaso.getCaseId(),object.codigo)
				cn=catNotificaciones
			} catch (Exception e) {
				Boolean closeCon2=false;
				try {
					closeCon2 = validarConexion();
					String ordenpago = ""
					String campus_id =""
					pstm = con.prepareStatement(Statements.GET_CAT_NOTIFICACIONES_CAMPUS_PROCESO_CODIGO)
					pstm.setString(1, object.campus)
					pstm.setString(2, object.codigo)
					rs = pstm.executeQuery()
					if (rs.next()) {
						catNotificaciones = new CatNotificaciones()
						catNotificaciones.setAnguloImagenFooter(rs.getString("anguloImagenFooter"))
						catNotificaciones.setAnguloImagenHeader(rs.getString("anguloImagenHeader"))
						catNotificaciones.setAsunto(rs.getString("asunto"))
						catNotificaciones.setBloqueAspirante(rs.getBoolean("bloqueAspirante"))
						catNotificaciones.setCaseId(rs.getString("caseId"))
						catNotificaciones.setCodigo(rs.getString("codigo"))
						catNotificaciones.setComentarioLeon(rs.getString("comentarioLeon"))
						catNotificaciones.setContenido(rs.getString("contenido"))
						catNotificaciones.setContenidoCorreo(rs.getString("contenidoCorreo"))
						catNotificaciones.setContenidoLeonel(rs.getString("contenidoLeonel"))
						catNotificaciones.setDescripcion(rs.getString("descripcion"))
						catNotificaciones.setDocGuiaEstudio(rs.getString("docGuiaEstudio"))
						catNotificaciones.setEnlaceBanner(rs.getString("enlaceBanner"))
						catNotificaciones.setEnlaceContacto(rs.getString("enlaceContacto"))
						catNotificaciones.setEnlaceFacebook(rs.getString("enlaceFacebook"))
						catNotificaciones.setEnlaceFooter(rs.getString("enlaceFooter"))
						catNotificaciones.setEnlaceInstagram(rs.getString("enlaceInstagram"))
						catNotificaciones.setEnlaceTwitter(rs.getString("enlaceTwitter"))
						catNotificaciones.setInformacionLic(rs.getBoolean("informacionLic"))
						catNotificaciones.setIsEliminado(rs.getBoolean("isEliminado"))
						catNotificaciones.setNombreImagenFooter(rs.getString("nombreImagenFooter"))
						catNotificaciones.setNombreImagenHeader(rs.getString("nombreImagenHeader"))
						catNotificaciones.setPersistenceId(rs.getLong("persistenceId"))
						catNotificaciones.setPersistenceVersion(rs.getLong("persistenceVersion"))
						catNotificaciones.setTextoFooter(rs.getString("textoFooter"))
						catNotificaciones.setTipoCorreo(rs.getString("tipoCorreo"))
						catNotificaciones.setTitulo(rs.getString("titulo"))
						catNotificaciones.setLstCorreoCopia(new ArrayList<String>())
						catNotificaciones.setLstVariableNotificacion(new ArrayList<String>())
						procesoCaso.setCaseId(rs.getString("caseId"))
						cn.setAnguloImagenFooter(rs.getString("anguloImagenFooter"))
						cn.setAnguloImagenHeader(rs.getString("anguloImagenHeader"))
						cn.setAsunto(rs.getString("asunto"))
						cn.setBloqueAspirante(rs.getBoolean("bloqueAspirante"))
						cn.setCaseId(rs.getString("caseId"))
						cn.setCodigo(rs.getString("codigo"))
						cn.setComentarioLeon(rs.getString("comentarioLeon"))
						cn.setContenido(rs.getString("contenido"))
						cn.setContenidoCorreo(rs.getString("contenidoCorreo"))
						cn.setContenidoLeonel(rs.getString("contenidoLeonel"))
						cn.setDescripcion(rs.getString("descripcion"))
						cn.setDocGuiaEstudio(rs.getString("docGuiaEstudio"))
						cn.setEnlaceBanner(rs.getString("enlaceBanner"))
						cn.setEnlaceContacto(rs.getString("enlaceContacto"))
						cn.setEnlaceFacebook(rs.getString("enlaceFacebook"))
						cn.setEnlaceFooter(rs.getString("enlaceFooter"))
						cn.setEnlaceInstagram(rs.getString("enlaceInstagram"))
						cn.setEnlaceTwitter(rs.getString("enlaceTwitter"))
						cn.setInformacionLic(rs.getBoolean("informacionLic"))
						cn.setIsEliminado(rs.getBoolean("isEliminado"))
						cn.setNombreImagenFooter(rs.getString("nombreImagenFooter"))
						cn.setNombreImagenHeader(rs.getString("nombreImagenHeader"))
						cn.setPersistenceId(rs.getLong("persistenceId"))
						cn.setPersistenceVersion(rs.getLong("persistenceVersion"))
						cn.setTextoFooter(rs.getString("textoFooter"))
						cn.setTipoCorreo(rs.getString("tipoCorreo"))
						cn.setTitulo(rs.getString("titulo"))
						
					}
				}catch(Exception ex) {
					errorlog +=", consulta custom " + ex.getMessage();
				}finally {
				if(closeCon2) {
					new DBConnect().closeObj(con, stm, rs, pstm);
				}
					
				}
			}
			
			
			//SELECT * from catnotificaciones where caseid=(SELECT caseid FROM procesocaso where campus = 'CAMPUS-MNORTE' and proceso='CatNotificaciones') and codigo='registrar'
			
			
			
			errorlog += "| se obtiene el catNotificaciones para generar el b64 del documento "

			errorlog += "|  catNotificacionDAO"

			errorlog += "|  lcn"
			// 1 variable plantilla [banner-href]
			errorlog += "| Variable1"
			errorlog += "| | procesoCaso.getCaseId() = "+procesoCaso.getCaseId()
			
			//cn = catNotificacionesDAO.getCatNotificaciones(procesoCaso.getCaseId(),object.codigo)
			errorlog += "|  lstDoc"
			errorlog+="| seteando mensaje"
			
			plantilla=plantilla.replace("[banner-href]", cn.getEnlaceBanner())
			
			//3 variable plantilla [contacto]
			errorlog += "| Variable3"
			//7 variable plantilla [titulo]
			errorlog += "| Variable7"
			plantilla=plantilla.replace("[titulo]",cn.getTitulo())
			
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.HOUR_OF_DAY, -6)
			int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			
			String dayOfMonthStr = String.valueOf(dayOfMonth);
			Integer mes = cal.get(Calendar.MONTH);
			String annio = Integer.toString(cal.get(Calendar.YEAR));
			String hora = (cal.get(Calendar.HOUR_OF_DAY)<10)?"0"+Integer.toString(cal.get(Calendar.HOUR_OF_DAY)):Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
			String minuto = (cal.get(Calendar.MINUTE)<10)?"0"+Integer.toString(cal.get(Calendar.MINUTE)):Integer.toString(cal.get(Calendar.MINUTE));
			
			String[] Month = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
			
			
			//9 variable plantilla [contenido]
			errorlog += "| Variable9"
			if(!cn.getContenidoCorreo().equals("")) {
				plantilla=plantilla.replace("<!--[CONTENIDO]-->", "<table width=\"80%\"> <thead></thead> <tbody> <tr> <td class=\"col-12\"style=\"font-size: initial; font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif;\"> [contenido]</td> </tr> </tbody> </table>")
				plantilla=plantilla.replace("[contenido]", cn.getContenidoCorreo())
				
				plantilla=plantilla.replace("[HOST]", objProperties.getUrlHost())
				if(object.mensaje != null) {
					errorlog += "| mensaje " + object.mensaje
					plantilla = plantilla.replace("[MENSAJE]", object.mensaje);
				}
			}
			plantilla=plantilla.replace("[DAY]",String.valueOf(dayOfMonth))
			plantilla=plantilla.replace("[MONTH]",Month[mes])
			plantilla=plantilla.replace("[YEAR]",annio)
			plantilla=plantilla.replace("[HOUR]",hora)
			plantilla=plantilla.replace("[MIN]",minuto)
			//8 Seccion table atributos usuario
			errorlog += "| Variable8.1 listado de correos copia"
			String tablaUsuario= ""
			String plantillaTabla="<tr> <td align= \"left \" valign= \"top \" style= \"text-align: justify; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #585858; font-size: 17px; line-height: 25px; \"> [clave] </span> </font> </td> <td align= \"left \" valign= \"top \" style= \"text-align: justify; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #ff5a00; font-size: 17px; line-height: 25px; \"> [valor] </span> </font> </td> </tr>"
			errorlog += "| Variable8.2 object.correo=" + object.correo
			correo=object.correo;
			errorlog += "| Variable8.3 cn.getAsunto()=" + cn.getAsunto()
			asunto=cn.getAsunto();
			errorlog += "| Variable8.4 cn.getLstCorreoCopia().size()=" + cn.getLstCorreoCopia().size()
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
					errorlog += "| Variable15.1"
					plantilla=plantilla.replace("[IDBANNER]",rs.getString("IdBanner")==null?"":rs.getString("IdBanner"))
					errorlog += "| Variable15.2"
					if(object.isEnviar) {
						plantilla=plantilla.replace("[RECHAZO-COMENTARIOS]",rs.getString("ObservacionesRechazo")==null?"[RECHAZO-COMENTARIOS]":(object.isEnviar)?rs.getString("ObservacionesRechazo"):"[RECHAZO-COMENTARIOS]")
						errorlog += "| Variable15.3"
						plantilla=plantilla.replace("[LISTAROJA-COMENTARIOS]",rs.getString("ObservacionesListaRoja")==null?"[LISTAROJA-COMENTARIOS]":(object.isEnviar)?rs.getString("ObservacionesListaRoja"):"[LISTAROJA-COMENTARIOS]")
						errorlog += "| Variable15.3"
						plantilla=plantilla.replace("[COMENTARIOS-CAMBIO]", rs.getString("ObservacionesCambio")==null?"[COMENTARIOS-CAMBIO]": (object.isEnviar)?rs.getString("ObservacionesCambio"):"[COMENTARIOS-CAMBIO]")
					}
					ordenpago = rs.getString("ordenpago")==null?"": rs.getString("ordenpago")
					
					if(!ordenpago.equals("")) {
						errorlog += "| campusid"
						pstm = con.prepareStatement(Statements.GET_CAMPUS_ID_FROM_CLAVE)
						pstm.setString(1, object.campus)
						rs = pstm.executeQuery()
						if(rs.next()) {
							
							campus_id = rs.getString("campus_id")==null?"": rs.getString("campus_id")
							errorlog += "| se obtuvo el campusid"+campus_id
							resultado = new ConektaDAO().getOrderDetails(0, 999, "{\"order_id\":\""+ordenpago+"\", \"campus_id\":\""+campus_id+"\"}", context)
							errorlog += "| se va castear map string string por data"
							Map<String, String> conektaData =(Map<String, String>) resultado.getData().get(0)
							errorlog += "| casteo exitoso"
							plantilla=plantilla.replace("[MONTO]", conektaData.get("amount")==null?"": conektaData.get("amount"))
							plantilla=plantilla.replace("[TRANSACCION]", conektaData.get("authorizationCode")==null?"": conektaData.get("authorizationCode"))
							plantilla=plantilla.replace("[METODO]", conektaData.get("type")==null?"": (conektaData.get("type").equals("credit"))?"Tarjeta":(conektaData.get("type").equals("oxxo"))?"OXXO Pay":"SPEI")
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
			errorlog += "| Variable8.5 DataUsuarioAdmision"
			plantilla = DataUsuarioAdmision(plantilla, context, correo, cn, errorlog,object.isEnviar);
			errorlog += "| Variable8.6 DataUsuarioRegistro"
			plantilla = DataUsuarioRegistro(plantilla, context, correo, cn, errorlog);

			String tablaPasos=""
			String plantillaPasos="<tr> <td class= \"col-xs-1 col-sm-1 col-md-1 col-lg-1 text-center aling-middle backgroundOrange color-index number-table \"> [numero]</td> <td class= \"col-xs-4 col-sm-4 col-md-4 col-lg-4 text-center aling-middle backgroundDGray \"> <div class= \"row \"> <div class= \"col-12 form-group color-titulo \"> <img src= \"[imagen] \"> </div> <div class= \"col-12 color-index sub-img \"style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; \"> [titulo] </div> </div> </td> <td class= \"col-xs-7 col-sm-7 col-md-7 col-lg-7 col-7 text-justify aling-middle backgroundLGray \"style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; \"> [descripcion] </td> </tr>"
			try {
				def catImageNotificacion = context.apiClient.getDAO(CatImageNotificacionDAO.class);
				errorlog += "| Variable9.1 catImageNotificacion.findByCaseId"
				List<CatImageNotificacion> lci = catImageNotificacion.findByCaseId(Long.valueOf(procesoCaso.getCaseId()), 0, 999)
				Integer numero= 0;
				errorlog += "| Variable9.2 lci.size()=" + lci.size()
				if(lci.size()>0) {
					plantilla= plantilla.replace("<!--[PASOS]-->", "<table class=\"table table-bordered\"> <tbody> [pasos] </tbody> </table>")
					for(CatImageNotificacion ci: lci) {
						if(ci.getCodigo().equals(cn.getCodigo())) {
						numero++
						errorlog += "| Variable10."+numero
						String imagen= "";
						//Descripcion es el nombre del documento
						errorlog += "| Variable10.1 doc=" + ci.getDescripcion()
						if(docEtapaProceso.size()>0) {
							for(Document doc:docEtapaProceso) {
									errorlog += "| Variable10.1 doc=" + ci.getDescripcion()+"= doc.getName()="+ doc.getContentFileName()
									if(doc.getContentFileName().equals(ci.getDescripcion())) {
										imagen ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
									}
								}
								tablaPasos += plantillaPasos.replace("[imagen]", imagen).replace("[numero]", numero+"").replace("[titulo]", ci.getTitulo()).replace("[descripcion]", ci.getTexto())
							}
						}
					}
				}
			} catch (Exception e) {
				errorlog += "| Fallo al momento de obtener los pasos"
			}
			
			errorlog += "| Variable11"
			plantilla=plantilla.replace("[pasos]", tablaPasos)
			
			
			
			errorlog += "| Variable13"
			if(!cn.getContenidoLeonel().equals("") ) {
				plantilla=plantilla.replace("<!--Leonel-->", "<table width=\"80%\"> <thead></thead> <tbody> <tr> <td width=\"25%\" style=\"text-align: right;\"> <img style=\"width: 145px;\" src=\"https://bpmpreprod.blob.core.windows.net/publico/Leoneldmnisiones_Mesa%20de%20trabajo%201.png\"> </td> <td class=\"col-6\"> <div class=\"arrow_box\" style=\"position: relative; background: #ff5900; border: 4px solid #ff5900;border-radius: 50px;\"> <h6 class=\"logo\" style=\"font-size: 12px; padding: 10px; color: white; font-weight: 500;font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif;\"> [leonel]</h6> </div> </td> </tr> </tbody> </table>"+"<hr>")
				plantilla=plantilla.replace("[leonel]", cn.getContenidoLeonel())
			}
			
			errorlog += "| Variable15"
			
			encoded=""
			if(object.codigo.equals("examenentrevista")) {
				SesionesDAO sd = new SesionesDAO()
				resultado = sd.getDatosSesionUsername(object.correo)
				object.data = resultado.getData();
				for (def index = 0; index < resultado.getData().size(); index++) {
					def element = resultado.getData().get(index);
					plantilla=plantilla.replace("[SNOMBRE]",  element.snombre)
					plantilla=plantilla.replace("[SDESCRIPCION]",  element.sdescripcion)
					if(element.descripcion == "Examen de aptitudes y conocimientos"){
						plantilla=plantilla.replace("[NOMBRE-COLLAGE]",  element.pnombre)
						plantilla=plantilla.replace("[DESCRIPCION-COLLAGE]",   element.pdescripcion==null?"":element.pdescripcion)
						plantilla=plantilla.replace("[FECHA-COLLAGE]",   element.aplicacion.split("-")[2]+"-"+element.aplicacion.split("-")[1]+"-"+element.aplicacion.split("-")[0])
						plantilla=plantilla.replace("[HORA-COLLAGE]",   element.horario)
						plantilla=plantilla.replace("[Lugar-EAC]",   element.online=='t'?"URL":"Lugar")
						plantilla=plantilla.replace("[LUGAR-COLLAGE]",   element.lugar)
						if(element.calle==null && element.numero_int==null && element.colonia==null) {
							plantilla=plantilla.replace("<tr> <td style=\"color: rgb(255, 89, 0);\">Dirección</td> <td>[DIRECCION-COLLAGE]</td> </tr>","")
						}
						plantilla=plantilla.replace("[DIRECCION-COLLAGE]",   (element.calle==null?"":element.calle+" - ")+((element.numero_int==null)?"":"#"+element.numero_int )+"  "+ (element.colonia==null?"":element.colonia))
					}
					if(element.descripcion == "Examen Psicométrico"){
						plantilla=plantilla.replace("[NOMBRE-PSICOMETRICO]",  element.pnombre)
						plantilla=plantilla.replace("[DESCRIPCION-PSICOMETRICO]",   element.pdescripcion==null?"":element.pdescripcion)
						plantilla=plantilla.replace("[FECHA-PSICOMETRICO]",   element.aplicacion.split("-")[2]+"-"+element.aplicacion.split("-")[1]+"-"+element.aplicacion.split("-")[0])
						plantilla=plantilla.replace("[HORA-PSICOMETRICO]",   element.horario)
						plantilla=plantilla.replace("[Lugar-EP]",   element.online=='t'?"URL":"Lugar")
						plantilla=plantilla.replace("[LUGAR-PSICOMETRICO]",   element.lugar)
						if(element.calle==null && element.numero_int==null && element.colonia==null) {
							plantilla=plantilla.replace("<tr> <td style=\"color: rgb(255, 89, 0);\">Dirección</td> <td>[DIRECCION-PSICOMETRICO]</td> </tr>","")
						}
						plantilla=plantilla.replace("[DIRECCION-PSICOMETRICO]",   (element.calle==null?"":element.calle+" - ")+((element.numero_int==null)?"":"#"+element.numero_int )+"  "+ (element.colonia==null?"":element.colonia))
					}
					if(element.descripcion == "Entrevista"){
						plantilla=plantilla.replace("[NOMBRE-ENTREVISTA]",  element.pnombre)
						plantilla=plantilla.replace("[DESCRIPCION-ENTREVISTA]",   element.pdescripcion==null?"":element.pdescripcion)
						plantilla=plantilla.replace("[FECHA-ENTREVISTA]",   element.aplicacion.split("-")[2]+"-"+element.aplicacion.split("-")[1]+"-"+element.aplicacion.split("-")[0])
						plantilla=plantilla.replace("[HORA-ENTREVISTA]",   element.horario)
						plantilla=plantilla.replace("[Lugar-E]",   element.online=='t'?"URL":"Lugar")
						plantilla=plantilla.replace("[LUGAR-ENTREVISTA]",   element.lugar)
						if(element.calle==null && element.numero_int==null && element.colonia==null) {
							plantilla=plantilla.replace("<tr> <td style=\"color: rgb(255, 89, 0);\">Dirección</td> <td>[DIRECCION-ENTREVISTA]</td> </tr>","")
						}
						plantilla=plantilla.replace("[DIRECCION-ENTREVISTA]",   (element.calle==null?"":element.calle+" - ")+((element.numero_int==null)?"":"#"+element.numero_int )+"  "+ (element.colonia==null?"":element.colonia))
	
					}
				}
			}else if(object.codigo.equals("registrar") && object.isEnviar) {
				plantilla = plantilla.replace("[href-confirmar]", objProperties.getUrlHost() + "/bonita/apps/login/activate/?correo=" + object.correo + "");	
			}else if (object.codigo.equals("transferencia")) {
				try {
					closeCon = validarConexion();
					pstm = con.prepareStatement(Statements.GET_ASPIRANTE_TRANSFERENCIA)
					pstm.setString(1, object.correo)
					rs = pstm.executeQuery()
					if(rs.next()) {
						plantilla=plantilla.replace("[UNIVERSIDAD-DESTINO]", rs.getString("campusnuevo"))
						plantilla=plantilla.replace("[UNIVERSIDAD-PROCEDENTE]", rs.getString("campusanterior"))
					}
				} catch (Exception e) {
					errorlog += "| TRANSFERENCIA " + e.getMessage()
				}finally {
				if(closeCon) {
					new DBConnect().closeObj(con, stm, rs, pstm);
				}
					
				}
			}else if (object.codigo.equals("reagendar")) {
				try {
					closeCon = validarConexion();
					pstm = con.prepareStatement(Statements.GET_ASPIRANTE_ASISTENCIA)
					pstm.setString(1, object.correo)
					rs = pstm.executeQuery()
					while(rs.next()) {
						if(rs.getString("descripcion").equals("Examen de aptitudes y conocimientos")) {
							plantilla=plantilla.replace("[FALTA-ASISTENCIA-PCA]", "asistencia")
						} else if(rs.getString("descripcion").equals("Examen Psicométrico")) {
							plantilla=plantilla.replace("[FALTA-ASISTENCIA-P]", "asistencia")
						} else {
							plantilla=plantilla.replace("[FALTA-ASISTENCIA-E", "asistencia")
						}
						
						
						
					}
					plantilla=plantilla.replace("[FALTA-ASISTENCIA-PCA]", "falta")
					plantilla=plantilla.replace("[FALTA-ASISTENCIA-P]", "falta")
					plantilla=plantilla.replace("[FALTA-ASISTENCIA-E", "falta")
				} catch (Exception e) {
					errorlog += "| TRANSFERENCIA " + e.getMessage()
				}finally {
				if(closeCon) {
					new DBConnect().closeObj(con, stm, rs, pstm);
				}
					
				}
			}else if(object.codigo.equals("carta-aceptar") || object.codigo.equals("carta-rechazo") || object.codigo.equals("carta-pdu")|| object.codigo.equals("carta-informacion") || object.codigo.equals("carta-propedeutico")) {
				try {
					CatDocumentosTextos dt = new CatDocumentosTextos();
					/*def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
					List<SolicitudDeAdmision> objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.correo, 0, 999)
					if(objSolicitudDeAdmision.size()>0) {
						Result documentosTextos = new DocumentosTextosDAO().getDocumentosTextos(objSolicitudDeAdmision.get(0).getCatCampus().getPersistenceId());
						if(documentosTextos.data.size()>0) {
							dt = documentosTextos.data.get(0);
						}
					}*/
					closeCon = validarConexion();
					
					pstm = con.prepareStatement(Statements.GET_DOCUMENTOSTEXTOS_BY_CAMPUSPID)
					pstm.setString(1, object.campus)
					rs = pstm.executeQuery()
					if(rs.next()) {
						dt.ciudadCarta=rs.getString("ciudadCarta");
						dt.estadoCarta=rs.getString("estadoCarta");
						dt.documentosEntregar=rs.getString("documentosEntregar");
						dt.documentosEntregarExtranjero=rs.getString("documentosEntregarExtranjero");
						dt.notasDocumentos=rs.getString("notasDocumentos");
						dt.parrafoMatematicas1=rs.getString("parrafoMatematicas1");
						dt.parrafoMatematicas2=rs.getString("parrafoMatematicas2");
						dt.parrafoMatematicas3=rs.getString("parrafoMatematicas3");
						dt.parrafoEspanol1=rs.getString("parrafoEspanol1");
						dt.parrafoEspanol2=rs.getString("parrafoEspanol2");
						dt.parrafoEspanol3=rs.getString("parrafoEspanol3");
						dt.directorAdmisiones=rs.getString("directorAdmisiones");
						dt.tituloDirectorAdmisiones=rs.getString("tituloDirectorAdmisiones");
						dt.correoDirectorAdmisiones=rs.getString("correoDirectorAdmisiones");
						dt.telefonoDirectorAdmisiones=rs.getString("telefonoDirectorAdmisiones");
						dt.actividadIngreso1=rs.getString("actividadIngreso1");
						dt.actividadIngreso2=rs.getString("actividadIngreso2");
						dt.costoSGM=rs.getString("costoSGM");
						dt.educacionGarantizada=rs.getString("educacionGarantizada");
						dt.instruccionesPagoBanco=rs.getString("instruccionesPagoBanco");
						dt.instruccionesPagoCaja=rs.getString("instruccionespagocaja");
						dt.cancelarSeguroGastosMedicos=rs.getString("cancelarSeguroGastosMedicos");
						dt.cursoMatematicas1=rs.getString("cursoMatematicas1");
						dt.cursoMatematicas2=rs.getString("cursoMatematicas2");
						
						try{
							plantilla=plantilla.replace("[CIUDAD-CARTA]",dt.ciudadCarta);
						}
						catch(Exception dex){
							plantilla=plantilla.replace("[CIUDAD-CARTA]","");
						}
						try{
							plantilla=plantilla.replace("[ESTADO-CARTA]",dt.estadoCarta);
						}
						catch(Exception dex){
							plantilla=plantilla.replace("[ESTADO-CARTA]","");
						}
						try{
							plantilla=plantilla.replace("[DOCUMENTOS-ENTREGAR]",dt.documentosEntregar);
						}
						catch(Exception dex){
							plantilla=plantilla.replace("[DOCUMENTOS-ENTREGAR]","");
						}
						try{
							plantilla=plantilla.replace("[DOCUMENTOS-EXTRANJEROS]",dt.documentosEntregarExtranjero);
						}
						catch(Exception dex){
							plantilla=plantilla.replace("[DOCUMENTOS-EXTRANJEROS]","");
						}
						try{
							plantilla=plantilla.replace("[NOTAS-DOCUMENTOS]",dt.notasDocumentos);
						}
						catch(Exception dex){
							plantilla=plantilla.replace("[NOTAS-DOCUMENTOS]","");
						}
						try{
							plantilla=plantilla.replace("[DIRECTOR-ADMISIONES]",dt.directorAdmisiones);
						}
						catch(Exception dex){
							plantilla=plantilla.replace("[DIRECTOR-ADMISIONES]","");
						}
						try{
							plantilla=plantilla.replace("[TITULO-DIRECTOR-ADMISIONES]",dt.tituloDirectorAdmisiones);
						}
						catch(Exception dex){
							plantilla=plantilla.replace("[TITULO-DIRECTOR-ADMISIONES]","");
						}
						try{
							plantilla=plantilla.replace("[CORREO-DIRECTOR-ADMISIONES]",dt.correoDirectorAdmisiones);
						}
						catch(Exception dex){
							plantilla=plantilla.replace("[CORREO-DIRECTOR-ADMISIONES]","");
						}
						try{
							plantilla=plantilla.replace("[TELEFONO-DIRECTOR-ADMISIONES]",dt.telefonoDirectorAdmisiones);
						}
						catch(Exception dex){
							plantilla=plantilla.replace("[TELEFONO-DIRECTOR-ADMISIONES]","");
						}
						try{
							plantilla=plantilla.replace("[COSTO-SGM]",dt.costoSGM);
						}catch(Exception dex){
							plantilla=plantilla.replace("[COSTO-SGM]","");
						}
						try{
							plantilla=plantilla.replace("[EDUCACION-GARANTIZADA]",dt.educacionGarantizada);
						}
						catch(Exception dex){
							plantilla=plantilla.replace("[EDUCACION-GARANTIZADA]","");
						}
						try{
							plantilla=plantilla.replace("[INSTRUCCIONES-PAGO-BANCO]",dt.instruccionesPagoBanco);
						}
						catch(Exception dex){
							plantilla=plantilla.replace("[INSTRUCCIONES-PAGO-BANCO]","");
						}
						try{
							plantilla=plantilla.replace("[CANCELAR-SEGURO-GASTOS]",dt.cancelarSeguroGastosMedicos);
						}
						catch(Exception dex){
							plantilla=plantilla.replace("[CANCELAR-SEGURO-GASTOS]","");
						}
						try{
							plantilla=plantilla.replace("[INSTRUCCIONES-PAGO-CAJA]",dt.instruccionesPagoCaja);
						}
						catch(Exception dex){
							plantilla=plantilla.replace("[INSTRUCCIONES-PAGO-CAJA]","");
						}
						
					}
					
					pstm = con.prepareStatement(Statements.GET_INFOCARTATEMPORAL_PLANTILLA)
					pstm.setString(1, object.correo)
					rs = pstm.executeQuery()
					if(rs.next()) {
						try {	
						plantilla=plantilla.replace("[|persistenceid|]",rs.getString("persistenceid"));
						plantilla=plantilla.replace("[|admitido|]",rs.getString("admitido"));
						plantilla=plantilla.replace("[|campusadmision|]",rs.getString("campusadmision"));
						plantilla=plantilla.replace("[|carrera|]",rs.getString("carrera"));
						plantilla=plantilla.replace("[|carta|]",rs.getString("carta"));
						plantilla=plantilla.replace("[|curp|]",rs.getString("curp"));
						plantilla=plantilla.replace("[|decisiondeadmision|]",rs.getString("decisiondeadmision"));
						plantilla=plantilla.replace("[|descuentoprontopagomes1|]",rs.getString("descuentoprontopagomes1"));
						plantilla=plantilla.replace("[|descuentoprontopagomes1fecha|]",rs.getString("descuentoprontopagomes1fecha"));
						plantilla=plantilla.replace("[|descuentoprontopagomes2|]",rs.getString("descuentoprontopagomes2"));
						plantilla=plantilla.replace("[|descuentoprontopagomes2fecha|]",rs.getString("descuentoprontopagomes2fecha"));
						plantilla=plantilla.replace("[|eema|]",rs.getString("eema"));
						plantilla=plantilla.replace("[|eemi|]",rs.getString("eemi"));
						plantilla=plantilla.replace("[|eemn|]",rs.getString("eemn"));
						plantilla=plantilla.replace("[|eemq|]",rs.getString("eemq"));
						plantilla=plantilla.replace("[|eems|]",rs.getString("eems"));
						plantilla=plantilla.replace("[|eemt|]",rs.getString("eemt"));
						plantilla=plantilla.replace("[|escm|]",rs.getString("escm"));
						plantilla=plantilla.replace("[|escp|]",rs.getString("escp"));
						plantilla=plantilla.replace("[|espanol|]",rs.getString("espanol"));
						plantilla=plantilla.replace("[|examinado|]",rs.getString("examinado"));
						plantilla=plantilla.replace("[|fechalimitedepago|]",rs.getString("fechalimitedepago"));
						plantilla=plantilla.replace("[|fechalimiteentregadocumentacion|]",rs.getString("fechalimiteentregadocumentacion"));
						plantilla=plantilla.replace("[|filaexcel|]",rs.getString("filaexcel"));
						plantilla=plantilla.replace("[|inscrito|]",rs.getString("inscrito"));
						plantilla=plantilla.replace("[|mmpi|]",rs.getString("mmpi"));
						plantilla=plantilla.replace("[|nivel|]",rs.getString("nivel"));
						plantilla=plantilla.replace("[|nombre|]",rs.getString("nombre"));
						plantilla=plantilla.replace("[|notastransferencia|]",rs.getString("notastransferencia"));
						plantilla=plantilla.replace("[|numerodematricula|]",rs.getString("numerodematricula"));
						plantilla=plantilla.replace("[|paa|]",rs.getString("paa"));
						plantilla=plantilla.replace("[|paanumerica|]",rs.getString("paanumerica"));
						plantilla=plantilla.replace("[|paaverbal|]",rs.getString("paaverbal"));
						plantilla=plantilla.replace("[|pagoinscripcion|]",rs.getString("pagoinscripcion"));
						plantilla=plantilla.replace("[|para|]",rs.getString("para"));
						plantilla=plantilla.replace("[|pca|]",rs.getString("pca"));
						plantilla=plantilla.replace("[|pcda|]",rs.getString("pcda"));
						plantilla=plantilla.replace("[|pdp|]",rs.getString("pdp"));
						plantilla=plantilla.replace("[|pdu|]",rs.getString("pdu"));
						plantilla=plantilla.replace("[|periodo|]",rs.getString("periodo"));
						plantilla=plantilla.replace("[|persistenceversion|]",rs.getString("persistenceversion"));
						plantilla=plantilla.replace("[|pia|]",rs.getString("pia"));
						plantilla=plantilla.replace("[|preparatoriade|]",rs.getString("preparatoriade"));
						plantilla=plantilla.replace("[|promedio|]",rs.getString("promedio"));
						plantilla=plantilla.replace("[|religion|]",rs.getString("religion"));
						plantilla=plantilla.replace("[|selecciondecursosprevia|]",rs.getString("selecciondecursosprevia"));
						plantilla=plantilla.replace("[|sesion|]",rs.getString("sesion"));
						plantilla=plantilla.replace("[|sexo|]",rs.getString("sexo"));
						plantilla=plantilla.replace("[|sihaceonomatematicas|]",rs.getString("sihaceonomatematicas"));
						plantilla=plantilla.replace("[|solicitante|]",rs.getString("solicitante"));
						plantilla=plantilla.replace("[|sse|]",rs.getString("sse"));
						plantilla=plantilla.replace("[|statuspdu|]",rs.getString("statuspdu"));
						plantilla=plantilla.replace("[|tipodeadmision|]",rs.getString("tipodeadmision"));
						plantilla=plantilla.replace("[|tipodeestudiante|]",rs.getString("tipodeestudiante"));
						plantilla=plantilla.replace("[|universidad|]",rs.getString("universidad"));
						plantilla=plantilla.replace("[|notaslistaroja|]",rs.getString("notaslistaroja"));
						plantilla=plantilla.replace("[|seleccionado|]",rs.getString("seleccionado"));
						}catch(Exception infex) {
							
						}
						/*plantilla=plantilla.replace("<!-- GUIA DE ESTUDIO-->", "<table width=\"80%\"> <thead></thead> <tbody> <tr style=\"text-align: center;\"> <td class=\"col-12\"> <div class=\"row\" > <div class=\"col-12 form-group color-titulo\"> <a href=\"[guia-src]\" target=\"_blank\" style=\"text-decoration: underline; cursor: pointer;\"><img style=\"\" src=\"https://bpmpreprod.blob.core.windows.net/publico/Gu%C3%ADa%20de%20estudios.png\" alt=\"no disponible\"></a> </div> </div> </td> </tr> </tbody> </table> <hr>")
						plantilla=plantilla.replace("[guia-src]", dt.urlGuiaExamenCB)
						plantilla=plantilla.replace("[TIPS]",dt.tipsCB);
						plantilla=plantilla.replace("<!--[PASOS]-->","<table style='width:80%; font-size: initial; font-family:  Arial;'><tr><td style='font-family:  Arial;'>"+dt.noSabes+"</td></tr></table>");
						plantilla=plantilla.replace("[LIGA-PARA-TEST-VOCACIONAL]",dt.urlTestVocacional);*/
						try {
							
							plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO1]",(costo1-(costo1*(Float.parseFloat(rs.getString("descuentoprontopagomes1"))/100)))+Float.parseFloat(dt.costoSGM)+"")
							plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO2]",(costo1-(costo1*(Float.parseFloat(rs.getString("descuentoprontopagomes2"))/100)))+Float.parseFloat(dt.costoSGM)+"")
						
						} catch (Exception dex) {
							plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO1]",dex.getMessage())
							plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO2]",dex.getMessage())
						}
						try {
							plantilla=plantilla.replace("[MATEMATICAS-1]",(rs.getString("sihaceonomatematicas").equals("1"))?dt.parrafoMatematicas1:"");
							plantilla=plantilla.replace("[MATEMATICAS-2]",(rs.getString("sihaceonomatematicas").equals("2"))?dt.parrafoMatematicas2:"");
							plantilla=plantilla.replace("[MATEMATICAS-3]",(rs.getString("sihaceonomatematicas").equals("3"))?dt.parrafoMatematicas3:"");
							plantilla=plantilla.replace("[ESPANOL-1]",(rs.getString("espanol").equals("1"))?dt.parrafoEspanol1:"");
							plantilla=plantilla.replace("[ESPANOL-2]",(rs.getString("espanol").equals("2"))?dt.parrafoEspanol2:"");
							plantilla=plantilla.replace("[ESPANOL-3]",(rs.getString("espanol").equals("3"))?dt.parrafoEspanol3:"");
							plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-ENERO]",(periodo.substring(4,6).equals("10")|| periodo.substring(4,6).equals("05"))?dt.actividadIngreso1:"");
							plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-AGOSTO]",(periodo.substring(4,6).equals("60")|| periodo.substring(4,6).equals("35")|| periodo.substring(4,6).equals("75"))?dt.actividadIngreso2:"");
							plantilla=plantilla.replace("[CURSO-MATEMATICAS-ENERO]",(periodo.substring(4,6).equals("10") || periodo.substring(4,6).equals("05"))?dt.cursoMatematicas1:"");
							plantilla=plantilla.replace("[CURSO-MATEMATICAS-AGOSTO]",(periodo.substring(4,6).equals("60")|| periodo.substring(4,6).equals("35")|| periodo.substring(4,6).equals("75"))?dt.cursoMatematicas2:"");
							try {
								Float descuento = (costo1-(costo1*(Float.parseFloat(rs.getString("descuentoprontopagomes1"))/100)))+Float.parseFloat(dt.costoSGM)
								plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO1]",descuento.toString());
							} catch (Exception e) {
								plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO1]",dt.costoSGM);
							}
							try {
								Float descuento = (costo1-(costo1*(Float.parseFloat(rs.getString("descuentoprontopagomes2"))/100)))+Float.parseFloat(dt.costoSGM)
								plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO2]",descuento.toString());
							} catch (Exception e) {
								plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO2]",dt.costoSGM);
							}
							
							
							
							
						} catch (Exception e) {
							plantilla=plantilla.replace("[MATEMATICAS-1]","");
							plantilla=plantilla.replace("[MATEMATICAS-2]","");
							plantilla=plantilla.replace("[MATEMATICAS-3]","");
							plantilla=plantilla.replace("[ESPANOL-1]","");
							plantilla=plantilla.replace("[ESPANOL-2]","");
							plantilla=plantilla.replace("[ESPANOL-3]","");
							plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-ENERO]","");
							plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-AGOSTO]","");
							plantilla=plantilla.replace("[CURSO-MATEMATICAS-ENERO]","");
							plantilla=plantilla.replace("[CURSO-MATEMATICAS-AGOSTO]","");
							
						}
						plantilla=plantilla.replace("style=\"background-color: inherit;\"", "style=\"background-color: transparent;\"")
												
					}else {
						pstm = con.prepareStatement(Statements.GET_INFOCARTA_PLANTILLA)
						pstm.setString(1, object.correo)
						rs = pstm.executeQuery()
						if(rs.next()) {
							try {
							plantilla=plantilla.replace("[|persistenceid|]",rs.getString("persistenceid"));
							plantilla=plantilla.replace("[|carta|]",rs.getString("carta"));
							plantilla=plantilla.replace("[|curp|]",rs.getString("curp"));
							plantilla=plantilla.replace("[|descuentoprontopagomes1|]",rs.getString("descuentoprontopagomes1"));
							plantilla=plantilla.replace("[|descuentoprontopagomes1fecha|]",rs.getString("descuentoprontopagomes1fecha"));
							plantilla=plantilla.replace("[|descuentoprontopagomes2|]",rs.getString("descuentoprontopagomes2"));
							plantilla=plantilla.replace("[|descuentoprontopagomes2fecha|]",rs.getString("descuentoprontopagomes2fecha"));
							plantilla=plantilla.replace("[|espanol|]",rs.getString("espanol"));
							plantilla=plantilla.replace("[|fechalimitedepago|]",rs.getString("fechalimitedepago"));
							plantilla=plantilla.replace("[|fechalimiteentregadocumentacion|]",rs.getString("fechalimiteentregadocumentacion"));
							plantilla=plantilla.replace("[|nombre|]",rs.getString("nombre"));
							plantilla=plantilla.replace("[|notastransferencia|]",rs.getString("notastransferencia"));
							plantilla=plantilla.replace("[|numerodematricula|]",rs.getString("numerodematricula"));
							plantilla=plantilla.replace("[|pca|]",rs.getString("pca"));
							plantilla=plantilla.replace("[|pcda|]",rs.getString("pcda"));
							plantilla=plantilla.replace("[|pdp|]",rs.getString("pdp"));
							plantilla=plantilla.replace("[|pdu|]",rs.getString("pdu"));
							plantilla=plantilla.replace("[|persistenceversion|]",rs.getString("persistenceversion"));
							plantilla=plantilla.replace("[|pia|]",rs.getString("pia"));
							plantilla=plantilla.replace("[|sihaceonomatematicas|]",rs.getString("sihaceonomatematicas"));
							plantilla=plantilla.replace("[|sse|]",rs.getString("sse"));
							plantilla=plantilla.replace("[|statuspdu|]",rs.getString("statuspdu"));
							plantilla=plantilla.replace("[|universidad|]",rs.getString("universidad"));
							plantilla=plantilla.replace("[|notaslistaroja|]",rs.getString("notaslistaroja"));
							}catch(Exception infex) {
								
							}
							/*plantilla=plantilla.replace("<!-- GUIA DE ESTUDIO-->", "<table width=\"80%\"> <thead></thead> <tbody> <tr style=\"text-align: center;\"> <td class=\"col-12\"> <div class=\"row\" > <div class=\"col-12 form-group color-titulo\"> <a href=\"[guia-src]\" target=\"_blank\" style=\"text-decoration: underline; cursor: pointer;\"><img style=\"\" src=\"https://bpmpreprod.blob.core.windows.net/publico/Gu%C3%ADa%20de%20estudios.png\" alt=\"no disponible\"></a> </div> </div> </td> </tr> </tbody> </table> <hr>")
							plantilla=plantilla.replace("[guia-src]", dt.urlGuiaExamenCB)
							plantilla=plantilla.replace("[TIPS]",dt.tipsCB);
							plantilla=plantilla.replace("<!--[PASOS]-->","<table style='width:80%; font-size: initial; font-family:  Arial;'><tr><td style='font-family:  Arial;'>"+dt.noSabes+"</td></tr></table>");
							plantilla=plantilla.replace("[LIGA-PARA-TEST-VOCACIONAL]",dt.urlTestVocacional);*/
							try {
								plantilla=plantilla.replace("[MATEMATICAS-1]",(rs.getString("sihaceonomatematicas").equals("1"))?dt.parrafoMatematicas1:"");
								plantilla=plantilla.replace("[MATEMATICAS-2]",(rs.getString("sihaceonomatematicas").equals("2"))?dt.parrafoMatematicas2:"");
								plantilla=plantilla.replace("[MATEMATICAS-3]",(rs.getString("sihaceonomatematicas").equals("3"))?dt.parrafoMatematicas3:"");
								plantilla=plantilla.replace("[ESPANOL-1]",(rs.getString("espanol").equals("1"))?dt.parrafoEspanol1:"");
								plantilla=plantilla.replace("[ESPANOL-2]",(rs.getString("espanol").equals("2"))?dt.parrafoEspanol2:"");
								plantilla=plantilla.replace("[ESPANOL-3]",(rs.getString("espanol").equals("3"))?dt.parrafoEspanol3:"");
								plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-ENERO]",(periodo.substring(4,6).equals("10")|| periodo.substring(4,6).equals("05"))?dt.actividadIngreso1:"");
								plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-AGOSTO]",(periodo.substring(4,6).equals("60")|| periodo.substring(4,6).equals("35")|| periodo.substring(4,6).equals("75"))?dt.actividadIngreso2:"");
								plantilla=plantilla.replace("[CURSO-MATEMATICAS-ENERO]",(periodo.substring(4,6).equals("10") || periodo.substring(4,6).equals("05"))?dt.cursoMatematicas1:"");
								plantilla=plantilla.replace("[CURSO-MATEMATICAS-AGOSTO]",(periodo.substring(4,6).equals("60")|| periodo.substring(4,6).equals("35")|| periodo.substring(4,6).equals("75"))?dt.cursoMatematicas2:"");
							try {
									Float descuento = (costo1-(costo1*(Float.parseFloat(rs.getString("descuentoprontopagomes1"))/100)))+Float.parseFloat(dt.costoSGM)
									plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO1]",descuento.toString());
								} catch (Exception e) {
									plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO1]",e.getMessage());
								}
								try {
									Float descuento = (costo1-(costo1*(Float.parseFloat(rs.getString("descuentoprontopagomes2"))/100)))+Float.parseFloat(dt.costoSGM)
									plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO2]",descuento.toString());
								} catch (Exception e) {
									plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO2]",e.getMessage());
								}
								
								
								
								
							} catch (Exception e) {
								plantilla=plantilla.replace("[MATEMATICAS-1]","");
								plantilla=plantilla.replace("[MATEMATICAS-2]","");
								plantilla=plantilla.replace("[MATEMATICAS-3]","");
								plantilla=plantilla.replace("[ESPANOL-1]","");
								plantilla=plantilla.replace("[ESPANOL-2]","");
								plantilla=plantilla.replace("[ESPANOL-3]","");
								plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-ENERO]","");
								plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-AGOSTO]","");
								plantilla=plantilla.replace("[CURSO-MATEMATICAS-ENERO]","");
								plantilla=plantilla.replace("[CURSO-MATEMATICAS-AGOSTO]","");
								
							}
							plantilla=plantilla.replace("style=\"background-color: inherit;\"", "style=\"background-color: transparent;\"")
													
						}
					}
				} catch (Exception e) {
					errorlog += "| TRANSFERENCIA " + e.getMessage()
				}finally {
				if(closeCon) {
					new DBConnect().closeObj(con, stm, rs, pstm);
				}
					
				}
			}
			
			try {
				Result hffc = new Result()
				hffc = getCatNotificacionesCampusCodigoCampus(object.codigo, object.campus)
				if(hffc.getData().size()>0) {
					CatNotificacionesCampus catHffc = (CatNotificacionesCampus) hffc.getData().get(0)
					plantilla=plantilla.replace("[HEADER-IMG]", catHffc.getHeader())
					plantilla=plantilla.replace("[TEXTO-FOOTER]", catHffc.getFooter())
					cc=catHffc.getCopia();
					try {
						plantilla=plantilla.replace("[firma]", generarFirma(catHffc.getCatnotificacionesfirma_pid().toString()))
						Result rfirma = getFirma("{\"estatusSolicitud\":\"Solicitud en progreso\",\"tarea\":\"Llenar solicitud\",\"lstFiltro\":[{\"columna\":\"PERSISTENCEID\",\"operador\":\"Igual a\",\"valor\":\""+catHffc.getCatnotificacionesfirma_pid().toString()+"\"}],\"type\":\"solicitudes_progreso\",\"usuario\":0,\"orderby\":\"NOMBRECOMPLETO\",\"orientation\":\"ASC\",\"limit\":20,\"offset\":0}")
						
						plantilla=plantilla.replace("(CONTACTO DE CAMPUS DESTINO)", rfirma.data.get(0).nombreCompleto + " " +rfirma.data.get(0).apellido)
					} catch (Exception e) {
						plantilla=plantilla.replace("[firma]", "")
					}
					
				}
			} catch (Exception e) {
				plantilla=plantilla.replace("[HEADER-IMG]", cn.getAnguloImagenHeader())
				plantilla=plantilla.replace("[TEXTO-FOOTER]", cn.getTextoFooter())
				plantilla=plantilla.replace("[firma]", "")
			}
			   
			plantilla=plantilla.replace("[header-href]", cn.getEnlaceBanner())
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
				CatBitacoraCorreo catBitacoraCorreo = new CatBitacoraCorreo();
				catBitacoraCorreo.setCodigo(object.codigo)
				catBitacoraCorreo.setDe(resultado.getAdditional_data().get(0))
				catBitacoraCorreo.setMensaje(object.mensaje)
				catBitacoraCorreo.setPara(object.correo)
				catBitacoraCorreo.setCampus(object.campus)
				
				if(resultado.success) {
					catBitacoraCorreo.setEstatus("Enviado a Mailgun")
					
				}else {
					catBitacoraCorreo.setEstatus("Fallido")
				}
				insertCatBitacoraCorreos(catBitacoraCorreo)
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

			private String DataUsuarioAdmision(String plantilla, RestAPIContext context, String correo, CatNotificaciones cn, String errorlog,Boolean isEnviar) {
			//8 Seccion table atributos usuario
		    errorlog += ", Variable8"
			String tablaUsuario= ""
			String plantillaTabla="<tr> <td align= \"left \" valign= \"top \" style= \"text-align: justify;vertical-align: bottom; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #585858; font-size: 17px; line-height: 25px; \"> [clave]: </span> </font> </td> <td align= \"left \" valign= \"top \" style= \"text-align: justify; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #ff5a00; font-size: 17px; line-height: 25px;vertical-align: bottom; \"> [valor] </span> </font> </td> </tr>"
			try {
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			List<SolicitudDeAdmision> objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(correo, 0, 999)
			if(objSolicitudDeAdmision.size()>0) {
				Result documentosTextos = new DocumentosTextosDAO().getDocumentosTextos(objSolicitudDeAdmision.get(0).getCatCampus().getPersistenceId());
				if(documentosTextos.data.size()>0) {
					def dt = documentosTextos.data.get(0);
					if(objSolicitudDeAdmision.get(0).necesitoAyuda && cn.getCodigo().equals("registrar")) {
						plantilla=plantilla.replace("<!--[PASOS]-->", "<table style='width:80%; font-size: initial; font-family:  Arial;'><tr><td style='font-family:  Arial;'>"+dt.noSabes+"</td></tr></table>")
						plantilla=plantilla.replace("[LIGA-PARA-TEST-VOCACIONAL]", dt.urlTestVocacional)
					}
					if(!cn.docGuiaEstudio.equals("")) {
						plantilla=plantilla.replace("<!-- GUIA DE ESTUDIO-->", "<table width=\"80%\"> <thead></thead> <tbody> <tr style=\"text-align: center;\"> <td class=\"col-12\"> <div class=\"row\" > <div class=\"col-12 form-group color-titulo\"> <a href=\"[guia-src]\" target=\"_blank\" style=\"text-decoration: underline; cursor: pointer;\"><img style=\"\" src=\"https://bpmpreprod.blob.core.windows.net/publico/Gu%C3%ADa%20de%20estudios.png\" alt=\"no disponible\"></a> </div> </div> </td> </tr> </tbody> </table> <hr>")
						plantilla=plantilla.replace("[guia-src]", dt.urlGuiaExamenCB)
					}
					if(!dt.tipsCB.equals("")) {
						plantilla=plantilla.replace("[TIPS]", dt.tipsCB)
					}else {
						plantilla=plantilla.replace("[TIPS]", "")
					}
				}
				if(objSolicitudDeAdmision.get(0).getCatSexo()!=null) {
					plantilla=plantilla.replace("Estimado(a)", objSolicitudDeAdmision.get(0).getCatSexo().getDescripcion().equals("Masculino")?"Estimado":"Estimada")
				}else {
					plantilla=plantilla.replace("Estimado(a)", "Estimado")
				}
				plantilla=plantilla.replace("[NOMBRE-COMPLETO]",objSolicitudDeAdmision.get(0).getPrimerNombre()+" "+objSolicitudDeAdmision.get(0).getSegundoNombre()+" "+objSolicitudDeAdmision.get(0).getApellidoPaterno()+" "+objSolicitudDeAdmision.get(0).getApellidoMaterno())
				plantilla=plantilla.replace("[NOMBRE]",objSolicitudDeAdmision.get(0).getPrimerNombre()+" "+objSolicitudDeAdmision.get(0).getSegundoNombre())
				plantilla=plantilla.replace("[UNIVERSIDAD]", objSolicitudDeAdmision.get(0).getCatCampusEstudio().getDescripcion())
				plantilla=plantilla.replace("[LICENCIATURA]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().getNombre())
				//plantilla=plantilla.replace("[LICENCIATURA-COSTO1]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionagosto==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionagosto)
				//plantilla=plantilla.replace("[LICENCIATURA-COSTO2]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero)
				//plantilla=plantilla.replace("[LICENCIATURA-COSTO3]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionMayo==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionMayo)
				//plantilla=plantilla.replace("[LICENCIATURA-COSTO4]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionSeptiembre==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionSeptiembre)
				
			    periodo = objSolicitudDeAdmision.get(0).getCatPeriodo().getClave()
				
				try {
					costo1=Float.parseFloat( periodo.substring(4,6).equals("10")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero:
					periodo.substring(4,6).equals("05")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero:
					periodo.substring(4,6).equals("60")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionagosto:
					periodo.substring(4,6).equals("35")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionMayo:
				    periodo.substring(4,6).equals("75")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionSeptiembre:"0")
					
					plantilla=plantilla.replace("[LICENCIATURA-COSTO1]", costo1.toString())
				} catch (Exception e) {
					e.printStackTrace()
				}
				if(objSolicitudDeAdmision.get(0).getCatGestionEscolar().isPropedeutico()) {
					plantilla=plantilla.replace("<!--PROPEDEUTICO-->", "<tr> <td valign=\"top\" style=\"text-align: justify;text-align: left;\"> <font face=\"'Source Sans Pro', sans-serif\" color=\"#4F4E4D\"> <span style=\"color: #4F4E4D;\"> Propedéutico: </span> </font> </td> <td valign=\"top\" style=\"text-align: justify;text-align: left;\"> <font face=\"'Source Sans Pro', sans-serif\" color=\"#4F4E4D\"> <span style=\"color: #FF5900;\"> [PROPEDEUTICO] </span> </font> </td> </tr>")
					plantilla=plantilla.replace("[PROPEDEUTICO]", objSolicitudDeAdmision.get(0).getCatPropedeutico().getDescripcion())
				}
				plantilla=plantilla.replace("[CAMPUSEXAMEN]",objSolicitudDeAdmision.get(0).getCatCampus().getDescripcion())
				try {
					plantilla=plantilla.replace("[CAMPUS]",objSolicitudDeAdmision.get(0).getCatCampusEstudio().getDescripcion())
				} catch (Exception e) {
					plantilla=plantilla.replace("[CAMPUS]","CAMPUS PREVIEW")
				}
				
				plantilla=plantilla.replace("[CORREO]",objSolicitudDeAdmision.get(0).getCorreoElectronico())
				plantilla=plantilla.replace("[PERIODO]",objSolicitudDeAdmision.get(0).getCatPeriodo().getDescripcion())
				plantilla=plantilla.replace("[PREPARATORIA]",(objSolicitudDeAdmision.get(0).getCatBachilleratos().getDescripcion().equals("Otro"))?objSolicitudDeAdmision.get(0).getBachillerato():objSolicitudDeAdmision.get(0).getCatBachilleratos().getDescripcion())
				plantilla=plantilla.replace("[PROMEDIO]",objSolicitudDeAdmision.get(0).getPromedioGeneral())
				plantilla=plantilla.replace("[ESTATUS]",objSolicitudDeAdmision.get(0).getEstatusSolicitud())
				
				errorlog += ", Variable14"
				
				if(cn.isInformacionLic()) {
					errorlog += ", Variable14.1"
					plantilla=plantilla.replace("<!--isInformacionLic-->", "<table width=\"80%\"> <tbody> <tr style=\"text-align: center;\"> <td class=\"col-4\" style=\"width:33.33%;margin: 0; padding:0; vertical-align: middle;\"> <img style=\"width:193px\" src=\"[URL-IMG-LICENCIATURA]\"> </td> <td class=\"col-4\" style=\"width:33.33%; background: #4F4E4D; vertical-align: middle; padding: 0; margin: 0;\"> <div class=\"row\"> <div class=\"col-12 form-group color-titulo\"> <img src=\"https://i.ibb.co/C8yv3pD/sello.png\"> </div> <div class=\"col-12 color-index sub-img\" style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif;\"> <a  style=\"font-size: 15px;color: white;\" href=\"[LICENCIATURA-URL]\"  target=\"_blank\">[LICENCIATURA]</a> </div> </div> </td> <td class=\"col-4\" style=\"width:33.33%; text-decoration: underline; font-size: 9px; background: #ff5900; color: white; font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; margin: 0; padding:0; vertical-align: middle;\"> <p>[descripcion-licenciatura] </p> </td> </tr> </tbody> </table>"+"<hr>")
					errorlog += ", Variable14.2"
					plantilla=plantilla.replace("[LICENCIATURA]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().getNombre())
					//plantilla=plantilla.replace("[LICENCIATURA-COSTO1]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionagosto==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionagosto)
					//plantilla=plantilla.replace("[LICENCIATURA-COSTO2]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero)
					//plantilla=plantilla.replace("[LICENCIATURA-COSTO3]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionMayo==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionMayo)
					//plantilla=plantilla.replace("[LICENCIATURA-COSTO4]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionSeptiembre==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionSeptiembre)
					periodo = objSolicitudDeAdmision.get(0).getCatPeriodo().getClave()
				
				try {
						costo1=Float.parseFloat( periodo.substring(4,6).equals("10")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero:
						periodo.substring(4,6).equals("05")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero:
						periodo.substring(4,6).equals("60")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionagosto:
						periodo.substring(4,6).equals("35")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionMayo:
					    periodo.substring(4,6).equals("75")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionSeptiembre:"0")
						plantilla=plantilla.replace("[LICENCIATURA-COSTO1]", costo1.toString())
					} catch (Exception e) {
						e.printStackTrace()
					}
					errorlog += ", Variable14.3"
					plantilla=plantilla.replace("[descripcion-licenciatura]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().getDescripcion())
					
					plantilla=plantilla.replace("[LICENCIATURA-URL]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().getEnlace())
					try {
						if(objSolicitudDeAdmision.get(0).getCatGestionEscolar().getUrlImgLicenciatura().equals("")) {
							plantilla=plantilla.replace("[URL-IMG-LICENCIATURA]", "https://bpmpreprod.blob.core.windows.net/publico/Afirma_Mesa%20de%20trabajo%201.png")
						}else {
							plantilla=plantilla.replace("[URL-IMG-LICENCIATURA]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().getUrlImgLicenciatura())
						}
					} catch (Exception e) {
						plantilla=plantilla.replace("[URL-IMG-LICENCIATURA]", "https://bpmpreprod.blob.core.windows.net/publico/Afirma_Mesa%20de%20trabajo%201.png")
					}
					
					
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
					if(isEnviar) {
						plantilla=plantilla.replace("[RECHAZO-COMENTARIOS]",detalleSolicitud.get(0).getObservacionesRechazo())
						errorlog += ", Variable15.3"
						plantilla=plantilla.replace("[LISTAROJA-COMENTARIOS]",detalleSolicitud.get(0).getObservacionesListaRoja())
						errorlog += ", Variable15.3"
						plantilla=plantilla.replace("[COMENTARIOS-CAMBIO]", detalleSolicitud.get(0).getObservacionesCambio())
					}
					if(detalleSolicitud.get(0).isCbCoincide()) {
						plantilla=plantilla.replace("<table class=\"cbCoincide\"", "<!--table class='cbCoincide'")
						plantilla=plantilla.replace("<br class=\"cbCoincide\">", "<br class='cbCoincide'-->")
						plantilla=plantilla.replace("<li>Entrevista <font color=\"#FF5900\">([FALTA-ASISTENCIA-E)</font> </li>", "<!--li>Entrevista <font color=\"#FF5900\">([FALTA-ASISTENCIA-E)</font> </li-->")
					}
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
						if(isEnviar) {
							plantilla=plantilla.replace("[RECHAZO-COMENTARIOS]",rs.getString("ObservacionesRechazo"))
							errorlog += ", Variable15.3"
							plantilla=plantilla.replace("[LISTAROJA-COMENTARIOS]",rs.getString("ObservacionesListaRoja"))
							errorlog += ", Variable15.3"
							plantilla=plantilla.replace("[COMENTARIOS-CAMBIO]", rs.getString("ObservacionesCambio"))
						}
					}
				}catch(Exception ex) {
					
				}finally {
					if(closeCon) {
						new DBConnect().closeObj(con, stm, rs, pstm);
						}
					
					}
				}
				String encoded = "";
				Boolean closeCon=false;
				try {
				closeCon = validarConexion();
					if (objSolicitudDeAdmision.get(0).urlFoto!= null ) {
						String SSA = "";
						pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
						rs= pstm.executeQuery();
						if(rs.next()) {
							SSA = rs.getString("valor")
						}
						plantilla=plantilla.replace("[USR-B64]", objSolicitudDeAdmision.get(0).urlFoto+SSA)
					}else {
						for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(objSolicitudDeAdmision.get(0).getCaseId(), "fotoPasaporte", 0, 10)) {
							// convert byte[] back to a BufferedImage
							InputStream is = new ByteArrayInputStream(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId));
							BufferedImage newBi = ImageIO.read(is);
							
							encoded = "data:image/png;base64, "+ Base64.getEncoder().encodeToString(toByteArray(resizeImage(newBi, 135, 180), "png"))
							plantilla=plantilla.replace("[USR-B64]", encoded)
						}
				}
				}catch(Exception e) {
					plantilla=plantilla.replace("[USR-B64]", "https://i.ibb.co/WyCsXQy/usuariofoto.jpg")
					errorlog+= ""+e.getMessage();
				}finally {
					if(closeCon) {
						new DBConnect().closeObj(con, stm, rs, pstm);
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
				/*for(String variables:cn.getLstVariableNotificacion()) {
					if(variables.equals("Nombre")) {
						tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getPrimernombre()+" "+objSolicitudDeAdmision.get(0).getSegundonombre()+" "+objSolicitudDeAdmision.get(0).getApellidopaterno()+" "+objSolicitudDeAdmision.get(0).getApellidomaterno())
					}

					if(variables.equals("Correo")){
						tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getCorreoelectronico())
					}
					
					
				}*/
				plantilla=plantilla.replace("[NOMBRE-COMPLETO]",objSolicitudDeAdmision.get(0).getPrimernombre()+" "+objSolicitudDeAdmision.get(0).getSegundonombre()+" "+objSolicitudDeAdmision.get(0).getApellidopaterno()+" "+objSolicitudDeAdmision.get(0).getApellidomaterno())
				plantilla=plantilla.replace("[NOMBRE]",objSolicitudDeAdmision.get(0).getPrimernombre()+" "+objSolicitudDeAdmision.get(0).getSegundonombre())
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
		public Result getCatBitacoraCorreo(String jsonData) {
			Result resultado = new Result();
			Boolean closeCon = false;
			String where ="", orderby="ORDER BY ", errorlog=""
			try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				CatBitacoraCorreo catBitacoraCorreo = new CatBitacoraCorreo()
				List<CatBitacoraCorreo> rows = new ArrayList<CatBitacoraCorreo>();
				closeCon = validarConexion();
				
				
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						case "PERSISTENCEID":
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" PERSISTENCEID ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=[valor]"
							}else {
								where+="=[valor]"
							}
							where = where.replace("[valor]", filtro.get("valor"))
						break;
						
						
						
						case "CODIGO":
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(CODIGO) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "DE":
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(DE) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "ESTATUS":
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(ESTATUS) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "FECHACREACION":
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(FECHACREACION) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "MENSAJE":
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(MENSAJE) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "PARA":
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(PARA) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "CAMPUS":
						
						if(filtro.get("valor")== null || filtro.get("valor")== "null") {
							
						}else {
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(CAMPUS) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
						}

						break;
					}
				}
				switch(object.orderby) {
					case "PERSISTENCEID":
					orderby+="PERSISTENCEID";
					break;
					case "CODIGO":
					orderby+="CODIGO";
					break;
					case "DE":
						orderby+="DE";
					break;
					case "ESTATUS":
						orderby+="ESTATUS";
					break;
					case "FECHACREACION":
						orderby+="FECHACREACION";
					break;
					case "MENSAJE":
						orderby+="MENSAJE";
					break;
					case "PARA":
						orderby+="PARA";
					break;
					case "CAMPUS":
					orderby+="CAMPUS";
					break;
					default:
					orderby+="PERSISTENCEID";
					break;
				}
				orderby+=" "+object.orientation;
				String consulta = catBitacoraCorreo.GET_CATBITACORACORREO
				consulta=consulta.replace("[WHERE]", where);
				errorlog+="consulta:"
					pstm = con.prepareStatement(consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
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
						catBitacoraCorreo = new CatBitacoraCorreo()
						catBitacoraCorreo.setPersistenceId(rs.getLong("persistenceId"))
						catBitacoraCorreo.setPersistenceVersion(rs.getLong("persistenceVersion"))
						catBitacoraCorreo.setCampus(rs.getString("campus"))
						catBitacoraCorreo.setCodigo(rs.getString("codigo"))
						catBitacoraCorreo.setDe(rs.getString("de"))
						catBitacoraCorreo.setEstatus(rs.getString("estatus"))
						catBitacoraCorreo.setFechacreacion(rs.getString("fechacreacion"))
						catBitacoraCorreo.setMensaje(rs.getString("mensaje"))
						catBitacoraCorreo.setPara(rs.getString("para"))
						catBitacoraCorreo.setCampus(rs.getString("campus"))
	
						rows.add(catBitacoraCorreo)
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
		public Result insertCatBitacoraCorreos(CatBitacoraCorreo bcorreo) {
			Result resultado = new Result();
			Boolean closeCon = false;
			List<CatBitacoraCorreo> data = new ArrayList<CatBitacoraCorreo>()
			try {
				
				closeCon = validarConexion();
			
				pstm = con.prepareStatement(Statements.INSERT_CATBITACORACORREOS, Statement.RETURN_GENERATED_KEYS)
				pstm.setString(1, bcorreo.getCodigo())
				pstm.setString(2, bcorreo.getDe())
				pstm.setString(3, bcorreo.getEstatus())
				pstm.setString(4, bcorreo.getMensaje())
				pstm.setString(5, bcorreo.getPara())
				pstm.setString(6, bcorreo.getCampus())
				pstm.executeUpdate();
				rs = pstm.getGeneratedKeys()
				if(rs.next()) {
					bcorreo.setPersistenceId(rs.getLong("persistenceId"))
				}
				data.add(bcorreo)
				resultado.setSuccess(true);
				resultado.setData(data)
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
		String where =" WHERE isEliminar=false ", orderby="ORDER BY ", errorlog=""
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
						where +=" PERSISTENCEID ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=[valor]"
						}else {
							where+="=[valor]"
						}
						where = where.replace("[valor]", filtro.get("valor").toString())
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
					case "FACEBOOK":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" LOWER(FACEBOOK) ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=LOWER('[valor]')"
					}else {
						where+="LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
				break;
							case "TWITTER":
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(TWITTER) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "BANNER":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(BANNER) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "APELLIDO":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(APELLIDO) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "CAMPUS":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(CAMPUS) ";
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
				case "CAMPUS":
				orderby+="CAMPUS";
									break;
				case "APELLIDO":
				orderby+="APELLIDO";
				break;
				case "FACEBOOK":
				orderby+="FACEBOOK";
				break;
				case "TWITTER":
				orderby+="TWITTER";
									break;
				case "BANNER":
				orderby+="BANNER";
							break;
				default:
				orderby+="PERSISTENCEID";
				break;
			}
			orderby+=" "+object.orientation;
			String consulta = Statements.GET_CAT_NOTIFICACION_FIRMA;
			consulta=consulta.replace("[WHERE]", where);
			errorlog+="consulta:"
			errorlog+=consulta.replace("PERSISTENCEID, CARGO, CORREO, GRUPO, NOMBRECOMPLETO, PERSISTENCEVERSION, SHOWCARGO, SHOWCORREO, SHOWGRUPO, SHOWTELEFONO, SHOWTITULO, TELEFONO, TITULO, CAMPUS, FACEBOOK, TWITTER, APELLIDO, BANNER", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "")
				pstm = con.prepareStatement(consulta.replace("PERSISTENCEID, CARGO, CORREO, GRUPO, NOMBRECOMPLETO, PERSISTENCEVERSION, SHOWCARGO, SHOWCORREO, SHOWGRUPO, SHOWTELEFONO, SHOWTITULO, TELEFONO, TITULO, CAMPUS, FACEBOOK, TWITTER, APELLIDO, BANNER", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
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
					firma.setCampus(rs.getString("campus"))
					firma.setFacebook(rs.getString("facebook"))
					firma.setTwitter(rs.getString("twitter"))
					firma.setApellido(rs.getString("apellido"))
					firma.setBanner(rs.getString("banner"))
					rows.add(firma)
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				resultado.setError_info(errorlog)
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
				pstm.setString(12,firma.getCampus())
				pstm.setString(13,firma.getFacebook())
				pstm.setString(14,firma.getTwitter())
				pstm.setString(15,firma.getApellido())
				pstm.setString(16,firma.getBanner())
				
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
				pstm.setString(12,firma.getCampus())
				pstm.setString(13,firma.getFacebook())
				pstm.setString(14,firma.getTwitter())
				pstm.setString(15,firma.getApellido())
				pstm.setString(16,firma.getBanner())
				pstm.setLong(17,firma.getPersistenceId())
				
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
	public Result deleteFirma(CatNotificacionesFirma firma) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
				List<CatNotificacionesFirma> rows = new ArrayList<CatNotificacionesFirma>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.DELETE_CAT_NOTIFICACIONES_FIRMA)
				
				pstm.setLong(1,firma.getPersistenceId())
				
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
	public Result updateCatNotificaciones(CatNotificaciones catNotificaciones) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
				List<CatNotificacionesFirma> rows = new ArrayList<CatNotificacionesFirma>();
				closeCon = validarConexion();
				if(catNotificaciones.persistenceId>0) {
					pstm = con.prepareStatement(Statements.UPDATE_CAT_NOTIFICACIONES)
				}else {
					pstm = con.prepareStatement(Statements.INSERT_CAT_NOTIFICACIONES)
				}
				
				pstm.setString(1, catNotificaciones.anguloImagenFooter);
				pstm.setString(2, catNotificaciones.anguloImagenHeader);
				pstm.setString(3, catNotificaciones.asunto);
				pstm.setString(4, catNotificaciones.comentarioLeon);
				pstm.setString(5, catNotificaciones.contenido);
				pstm.setString(6, catNotificaciones.contenidoCorreo);
				pstm.setString(7, catNotificaciones.contenidoLeonel);
				pstm.setString(8, catNotificaciones.descripcion);
				pstm.setString(9, catNotificaciones.docGuiaEstudio);
				pstm.setString(10, catNotificaciones.enlaceBanner);
				pstm.setString( 11,catNotificaciones.enlaceContacto);
				pstm.setString( 12,catNotificaciones.enlaceFacebook);
				pstm.setString( 13,catNotificaciones.enlaceFooter);
				pstm.setString( 14,catNotificaciones.enlaceInstagram);
				pstm.setString( 15,catNotificaciones.enlaceTwitter);
				pstm.setString( 16,catNotificaciones.nombreImagenFooter);
				pstm.setString( 17,catNotificaciones.textoFooter);
				pstm.setString( 18,catNotificaciones.tipoCorreo);
				pstm.setString( 19,catNotificaciones.titulo);
				pstm.setString( 20,catNotificaciones.urlImgFooter);
				pstm.setString( 21,catNotificaciones.urlImgHeader);
				pstm.setString( 22,catNotificaciones.codigo);
				pstm.setString( 23,catNotificaciones.caseId);
				
				
				pstm.execute()
				
				resultado.setSuccess(true)
				rows.add(catNotificaciones)
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
		return retorno;
	}
	
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true
		}
		return retorno;
	}
	BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		graphics2D.dispose();
		return resizedImage;
	}
	// convert BufferedImage to byte[]
	public static byte[] toByteArray(BufferedImage bi, String format)
		throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, format, baos);
		byte[] bytes = baos.toByteArray();
		return bytes;

	}
	public String generarFirma(String persistenceId){
		Boolean closeCon=false;
		CatNotificacionesFirma firma = new CatNotificacionesFirma()
		String nombretitulo = "";
		String cargo = "";
		String telefono = "";
		String pagina = "";
		String correo = "";
		String facebook ="";
		String twitter = "";
		String banner = "";
		if(!persistenceId.equals("")) {
		try {
		closeCon = validarConexion();
		
		pstm = con.prepareStatement(Statements.GET_FIRMA_PERSISTENCEID)
		pstm.setLong(1, Long.parseLong(persistenceId))
		rs = pstm.executeQuery()
			if (rs.next()) {
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
				firma.setCampus(rs.getString("campus"))
				firma.setFacebook(rs.getString("facebook"))
				firma.setTwitter(rs.getString("twitter"))
				firma.setApellido(rs.getString("apellido"))
				firma.setBanner(rs.getString("banner"))
				
				nombretitulo = "<p style='line-height: 15px; margin-left: 10px; margin-right: 10px;'><b>" + firma.titulo + " " + firma.nombreCompleto + " <span style='color: #FF5900;'>"+firma.apellido+" </span></b> <br>";
				cargo = (firma.cargo == null || firma.cargo.equals(""))?"":firma.cargo + "</p>";
				telefono = (firma.telefono == null || firma.telefono.equals(""))?"<p style='line-height: 15px;margin-left: 10px; margin-right: 10px;'>":"<p style='line-height: 15px;'><img style='width: 10px;'src='https://i.ibb.co/vsCB2MF/icono-UA-04.png'>&nbsp;" + firma.telefono + "<br> ";
				pagina = (firma.grupo == null || firma.grupo.equals(""))?"":"<img style='width: 10px;'src='https://i.ibb.co/172hPYQ/icono-UA-Mesa-de-trabajo-1.png'>&nbsp;" + firma.grupo + "<br>";
				facebook = (firma.facebook == null || firma.facebook.equals(""))?"":"<img style='width: 10px;'src='https://i.ibb.co/B49GzyB/icono-UA-03.png'>&nbsp;"+firma.facebook+"<br>";
				twitter = (firma.twitter == null || firma.twitter.equals(""))?"":"<img style='width: 10px;'src='https://i.ibb.co/7Wd817H/icono-UA-02.png'>&nbsp;"+firma.twitter+"<br>";
				correo = (firma.correo == null || firma.correo.equals(""))?"":"<img style='width: 10px;'src='https://i.ibb.co/Yc0TXX9/Iconos-UA-05.png'>&nbsp;" + firma.correo + "<br> "
				banner = (firma.banner == null || firma.banner.equals(""))?"</p>":"<img src='"+firma.banner+"'><br> </p>"
				

				
			}
		}catch(Exception ex) {
			
		}finally {
		if(closeCon) {
			new DBConnect().closeObj(con, stm, rs, pstm);
		}
			
		}
		
			return nombretitulo + cargo + telefono + pagina + facebook + twitter + correo + banner;
		}else {
			return ""
		}
	}
	public Result getCatNotificacionesCampus(String grupoBonita) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {

			CatNotificacionesCampus row = new CatNotificacionesCampus()
			List<CatNotificacionesCampus> rows = new ArrayList<CatNotificacionesCampus>();
			closeCon = validarConexion();
			String consulta = CatNotificacionesCampus.GET_CATNOTIFICACIONESCAMPUS
			pstm = con.prepareStatement(consulta)
			pstm.setString(1, grupoBonita)
			rs = pstm.executeQuery()
				while(rs.next()) {
					row = new CatNotificacionesCampus()
					row.setCatcampus_pid(rs.getLong("catcampus_pid"))
					row.setCatnotificacionesfirma_pid(rs.getLong("catnotificacionesfirma_pid"))
					row.setCodigo(rs.getString("codigo"))
					row.setDescripcion(rs.getString("descripcion"))
					row.setFooter(rs.getString("footer"))
					row.setHeader(rs.getString("header"))
					row.setCopia(rs.getString("copia"))
					row.setTipoCorreo(rs.getString("tipoCorreo"))
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
	
	public Result getCatNotificacionesCampusCodigoCampus(String codigo,String campus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {

			CatNotificacionesCampus row = new CatNotificacionesCampus()
			List<CatNotificacionesCampus> rows = new ArrayList<CatNotificacionesCampus>();
			closeCon = validarConexion();
			String consulta = CatNotificacionesCampus.GET_CATNOTIFICACIONESCAMPUSCC
			pstm = con.prepareStatement(consulta)
			pstm.setString(1, codigo)
			pstm.setString(2, campus)
			rs = pstm.executeQuery()
				while(rs.next()) {
					row = new CatNotificacionesCampus()
					row.setCatcampus_pid(rs.getLong("catcampus_pid"))
					row.setCatnotificacionesfirma_pid(rs.getLong("catnotificacionesfirma_pid"))
					row.setCodigo(rs.getString("codigo"))
					row.setDescripcion(rs.getString("descripcion"))
					row.setFooter(rs.getString("footer"))
					row.setHeader(rs.getString("header"))
					row.setCopia(rs.getString("copia"))
					row.setTipoCorreo(rs.getString("tipoCorreo"))
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
	
	public Result getCartasNotificaciones(String campus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {

			CatNotificaciones row = new CatNotificaciones()
			List<CatNotificacionesCampus> rows = new ArrayList<CatNotificacionesCampus>();
			closeCon = validarConexion();
			String consulta = Statements.GET_CARTAS_NOTIFICACIONES
			pstm = con.prepareStatement(consulta)
			pstm.setString(1, campus)
			rs = pstm.executeQuery()
				while(rs.next()) {
					row = new CatNotificaciones()
					row.anguloImagenFooter = rs.getString("anguloImagenFooter")
					row.anguloImagenHeader = rs.getString("anguloImagenHeader")
					row.asunto = rs.getString("asunto")
					row.caseId = rs.getString("caseId")
					row.codigo = rs.getString("codigo")
					row.comentarioLeon = rs.getString("comentarioLeon")
					row.contenido  = rs.getString("contenido")
					row.contenidoCorreo = rs.getString("contenidoCorreo")
					row.contenidoLeonel = rs.getString("contenidoLeonel")
					row.descripcion = rs.getString("descripcion")
					row.docGuiaEstudio = rs.getString("docGuiaEstudio")
					row.enlaceBanner = rs.getString("enlaceBanner")
					row.enlaceContacto = rs.getString("enlaceContacto")
					row.enlaceFacebook = rs.getString("enlaceFacebook")
					row.enlaceFooter = rs.getString("enlaceFooter")
					row.enlaceInstagram = rs.getString("enlaceInstagram")
					row.enlaceTwitter = rs.getString("enlaceTwitter")
					row.nombreImagenFooter = rs.getString("nombreImagenFooter")
					row.textoFooter  = rs.getString("textoFooter")
					row.tipoCorreo = rs.getString("tipoCorreo")
					row.titulo = rs.getString("titulo")
					row.urlImgFooter = rs.getString("urlImgFooter")
					row.urlImgHeader = rs.getString("urlImgHeader")
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
	
	public Result insertCatNotificacionesCampus(CatNotificacionesCampus row) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {

			
			List<CatNotificacionesCampus> rows = new ArrayList<CatNotificacionesCampus>();
			closeCon = validarConexion();
			String consulta = CatNotificacionesCampus.GET_CATNOTIFICACIONESCAMPUS_CODIGO_CAMPUS
			pstm = con.prepareStatement(consulta)
			pstm.setString(1, row.getCodigo())
			pstm.setLong(2, row.getCatcampus_pid())
			rs = pstm.executeQuery()
				if(rs.next()) {
					pstm = con.prepareStatement(CatNotificacionesCampus.UPDATE_CATNOTIFICACIONESCAMPUS)
					(row.getCatnotificacionesfirma_pid()==null)?pstm.setNull(1, java.sql.Types.NULL):pstm.setLong(1, row.getCatnotificacionesfirma_pid())
					pstm.setString(2, row.getCopia())
					pstm.setString(3, row.getFooter())
					pstm.setString(4, row.getHeader())
					pstm.setString(5, row.getCodigo())
					pstm.setLong(6, row.getCatcampus_pid())
					
				}else {
					pstm = con.prepareStatement(CatNotificacionesCampus.INSERT_CATNOTIFICACIONESCAMPUS)
					(row.getCatnotificacionesfirma_pid()==null)?pstm.setNull(1, java.sql.Types.NULL):pstm.setLong(1, row.getCatnotificacionesfirma_pid())
					pstm.setString(2, row.getCodigo())
					pstm.setString(3, row.getCopia())
					pstm.setString(4, row.getFooter())
					pstm.setString(5, row.getHeader())
					pstm.setLong(6, row.getCatcampus_pid())
				}
				pstm.executeUpdate();
				rows.add(row)
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
}