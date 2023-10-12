package com.anahuac.rest.api.DAO

import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.ProcesoCaso
import com.anahuac.posgrados.bitacora.PSGRCatBitacoraCorreos
import com.anahuac.posgrados.catalog.PSGRCatDocumentosTextos
import com.anahuac.posgrados.catalog.PSGRCatImageNotificacion
import com.anahuac.posgrados.catalog.PSGRCatNotificaciones
import com.anahuac.posgrados.catalog.PSGRCatNotificacionesDAO
import com.anahuac.posgrados.catalog.PSGRCatRegistro
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.db.CatBitacoraCorreo
import com.anahuac.rest.api.Entity.db.CatNotificacionesCampus
import com.anahuac.rest.api.Utilities.LoadParametros
import groovy.json.JsonSlurper


import com.anahuac.rest.api.DB.Statements
import com.anahuac.model.ProcesoCasoDAO
import com.anahuac.model.SolicitudDeAdmision
import java.awt.image.BufferedImage
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatRegistroDAO
import com.anahuac.catalogos.CatNotificacionesDAO
import com.anahuac.catalogos.CatImageNotificacionDAO


class NotificacionDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificacionDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	float costo1=0,costo2=0,costo3=0,costo4=0
	String periodo =""
	
	public Result generateHtml(String jsonData, RestAPIContext context) {
		Connection con;
		Statement stm;
		ResultSet rs;
		PreparedStatement pstm;
		Result resultado = new Result();
		
		Long userLogged = 0L;
		Long caseId=0L;
		String encoded = "";
		String errorlog = "";
		
		String idioma = "";
		String plantilla ="";
		String correo="",  asunto="",  body="",  cc="";
		Boolean cartaenviar=false;
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
			
			errorlog += "| username = "+ objProperties.getUsuario();
			errorlog += "| password = "+ objProperties.getPassword();
			errorlog += "| host =     "+objProperties.getUrlHost();
			/*-------------------------------------------------------------*/

			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			Boolean closeConPlantilla=false;
			/*-------------------ENG/ESP-----------------------------------*/
			/*try {
				closeConPlantilla = validarConexion();
				pstm = con.prepareStatement(Statements.SELECT_IDIOMA_BY_USERNAME);
				pstm.setString(1, object.correo);
					
				rs = pstm.executeQuery();
					
				if(rs.next()) {
					idioma = rs.getString("idioma");
				}
					
			} catch (Exception e) {
				errorlog = "Error generateHtml - select_idioma_by_username: "+rs+" object: "+object+" idioma: "+idioma+" exception: "+e;
			} finally {
				if(closeConPlantilla) {
					new DBConnect().closeObj(con, stm, rs, pstm);
				}
			}
			
			/*-------------------CARTAS, REGISTRO, REESTABLECER, RECUPERAR, ACTIVADO, ETC-----------------------------------*/
			/*if(idioma == "ENG") {
				if(object.codigo.equals("registrar") || object.codigo.equals("reestablecer")) {
					object.codigo+="-eng";
					errorlog = "Registrar // Reestablecer";
					
				} else if(object.codigo.equals("recuperar")) {
					object.codigo="reestablecer-eng";
					errorlog = "Recuperar";
					
				} else if(object.codigo.equals("activado")) {
					object.codigo+="-eng";
					errorlog = "Activado";
					
				} else if(object.codigo.equals("carta-aceptar")) {
					object.codigo+="-eng";
					errorlog = "Carta-aceptar";
					
				} else if(object.codigo.equals("carta-rechazo")) {
					object.codigo+="-eng";
					errorlog = "Carta-rechazo";
					
				} else if(object.codigo.equals("carta-informacion")) {
					object.codigo+="-eng";
					errorlog = "Carta-informacion";
					
				} else if(object.codigo.equals("examenentrevista")) {
					object.codigo+="-eng";
					errorlog = "Examen entrevista";
					
				} else if(object.codigo.equals("transferencia")) {
					object.codigo+="-eng";
					errorlog = "Transferencia";
					
				} else if(object.codigo.equals("carta-propedeutico")) {
					object.codigo+="-eng";
					errorlog = "Carta-propedeutico";
					
				} else if(object.codigo.equals("carta-pdu")) {
					object.codigo+="-eng";
					errorlog = "Carta-pdu";
					
				} else if(object.codigo.equals("enviada")) {
					object.codigo+="-eng";
					errorlog = "Enviada";
					
				} else if(object.codigo.equals("cambios")) {
					object.codigo+="-eng";
					errorlog = "Cambios";
					
				} else if(object.codigo.equals("rechazada")) {
					object.codigo+="-eng";
					errorlog = "Rechazada";
					
				} else if(object.codigo.equals("listaroja")) {
					object.codigo+="-eng";
					errorlog = "Listaroja";
					
				} else if(object.codigo.equals("validada")) {
					object.codigo+="-eng";
					errorlog = "Validada";
					
				} else if(object.codigo.equals("pago")) {
					object.codigo+="-eng";
					errorlog = "Pago";
					
				} else if(object.codigo.equals("validada-aa")) {
					object.codigo+="-eng";
					errorlog = "Validada-aa";
					
				} else if(object.codigo.equals("recordatorio")) {
					object.codigo+="-eng";
					errorlog = "recordatorio";
					
				} else if(object.codigo.equals("autodescripcion")) {
					object.codigo+="-eng";
					errorlog = "Autodescripcion";
					
				} else if(object.codigo.equals("credencial")) {
					object.codigo+="-eng";
					errorlog = "Credencial";
					
				} else if(object.codigo.equals("validada-100")) {
					object.codigo+="-eng";
					errorlog = "Validada-100";
					
				}
			}*/
			/*--------------------FIN-------------------------------------*/
			userLogged = context.getApiSession().getUserId();
			errorlog += "| Se obtuvo el usuario " + userLogged;
			PSGRCatNotificaciones catNotificaciones= null;
			ProcesoCaso procesoCaso = new ProcesoCaso()
			PSGRCatNotificaciones cn = new PSGRCatNotificaciones()
			try {
				def procesoCasoDAO = context.getApiClient().getDAO(ProcesoCasoDAO.class);
				procesoCaso = procesoCasoDAO.getCaseId(object.campus, "CatNotificaciones");
				errorlog += "| Despues con el campus " + object.campus + " se obtuvo el caseid " + procesoCaso.getCaseId()
				def catNotificacionesDAO = context.getApiClient().getDAO(PSGRCatNotificacionesDAO.class);
				catNotificaciones = catNotificacionesDAO.getCat_notificaciones(procesoCaso.getCaseId(),object.codigo)
				cn=catNotificaciones
				throw new Exception("catNotificaciones" + catNotificaciones);
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
						catNotificaciones = new PSGRCatNotificaciones()
						catNotificaciones.setAngulo_imagen_footer(rs.getString("angulo_imagen_footer"))
						catNotificaciones.setAngulo_imagen_header(rs.getString("angulo_imagen_header"))
						catNotificaciones.setAsunto(rs.getString("asunto"))
						catNotificaciones.setBloque_aspirante(rs.getBoolean("bloque_aspirante"))
						catNotificaciones.setCase_id(rs.getString("case_id"))
						catNotificaciones.setCodigo(rs.getString("codigo"))
						catNotificaciones.setComentario_leon(rs.getString("comentario_leon"))
						catNotificaciones.setContenido(rs.getString("contenido"))
						catNotificaciones.setContenido_correo(rs.getString("contenido_correo"))
						catNotificaciones.setContenido_leonel(rs.getString("contenido_leonel"))
						catNotificaciones.setDescripcion(rs.getString("descripcion"))
						catNotificaciones.setDoc_guia_estudio(rs.getString("doc_guia_estudio"))
						catNotificaciones.setEnlace_banner(rs.getString("enlace_banner"))
						catNotificaciones.setEnlace_contacto(rs.getString("enlace_contacto"))
						catNotificaciones.setEnlace_facebook(rs.getString("enlace_facebook"))
						catNotificaciones.setEnlace_footer(rs.getString("enlace_footer"))
						catNotificaciones.setEnlace_instagram(rs.getString("enlace_instagram"))
						catNotificaciones.setEnlace_twitter(rs.getString("enlace_twitter"))
						catNotificaciones.setInformacion_lic(rs.getBoolean("informacion_lic"))
						catNotificaciones.setIs_eliminado(rs.getBoolean("is_eliminado"))
						catNotificaciones.setNombre_imagen_footer(rs.getString("nombre_imagen_footer"))
						catNotificaciones.setNombre_imagen_header(rs.getString("nombre_imagen_header"))
						catNotificaciones.setPersistenceId(rs.getLong("persistenceId"))
						catNotificaciones.setPersistenceVersion(rs.getLong("persistenceVersion"))
						catNotificaciones.setTexto_footer(rs.getString("texto_footer"))
						catNotificaciones.setTipo_correo(rs.getString("tipo_correo"))
						catNotificaciones.setTitulo(rs.getString("titulo"))
						catNotificaciones.setLst_correo_copia(new ArrayList<String>())
						catNotificaciones.setLst_variable_notificacion(new ArrayList<String>())
						procesoCaso.setCaseId(rs.getString("case_id"))
						cn.setAngulo_imagen_footer(rs.getString("angulo_imagen_footer"))
						cn.setAngulo_imagen_header(rs.getString("angulo_imagen_header"))
						cn.setAsunto(rs.getString("asunto"))
						cn.setBloque_aspirante(rs.getBoolean("bloque_aspirante"))
						cn.setCase_id(rs.getString("case_id"))
						cn.setCodigo(rs.getString("codigo"))
						cn.setComentario_leon(rs.getString("comentario_leon"))
						cn.setContenido(rs.getString("contenido"))
						cn.setContenido_correo(rs.getString("contenido_correo"))
						cn.setContenido_leonel(rs.getString("contenido_leonel"))
						cn.setDescripcion(rs.getString("descripcion"))
						cn.setDoc_guia_estudio(rs.getString("doc_guia_estudio"))
						cn.setEnlace_banner(rs.getString("enlace_banner"))
						cn.setEnlace_contacto(rs.getString("enlace_contacto"))
						cn.setEnlace_facebook(rs.getString("enlace_facebook"))
						cn.setEnlace_footer(rs.getString("enlace_footer"))
						cn.setEnlace_instagram(rs.getString("enlace_instagram"))
						cn.setEnlace_twitter(rs.getString("enlace_twitter"))
						cn.setInformacion_lic(rs.getBoolean("informacion_lic"))
						cn.setIs_eliminado(rs.getBoolean("is_eliminado"))
						cn.setNombre_imagen_footer(rs.getString("nombre_imagen_footer"))
						cn.setNombre_imagen_header(rs.getString("nombre_imagen_header"))
						cn.setPersistenceId(rs.getLong("persistenceId"))
						cn.setPersistenceVersion(rs.getLong("persistenceVersion"))
						cn.setTexto_footer(rs.getString("texto_footer"))
						cn.setTipo_correo(rs.getString("tipo_correo"))
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
			
			plantilla=plantilla.replace("[banner-href]", cn.getEnlace_banner())
			throw new Exception("DESPUES DEL PRIMER REMPLACE");
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
			if(!cn.getContenido_correo().equals("")) {
				plantilla=plantilla.replace("<!--[CONTENIDO]-->", "<table width=\"80%\"> <thead></thead> <tbody> <tr> <td class=\"col-12\"style=\"font-size: initial; font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif;\"> [contenido]</td> </tr> </tbody> </table>")
				plantilla=plantilla.replace("[contenido]", cn.getContenido_correo())
				
				plantilla=plantilla.replace("[HOST]", objProperties.getUrlHost())
				if(object.mensaje != null) {
					errorlog += "| mensaje " + object.mensaje
					plantilla = plantilla.replace("[MENSAJE]", object.mensaje);
				}
			}
			
			if(idioma == "ENG") {
				String[] MonthEng = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
				plantilla=plantilla.replace("[DAY] / [MONTH] / [YEAR] | [HOUR]:[MIN]","[MONTH] / [DAY] / [YEAR] | [HOUR]:[MIN]");
				
				plantilla=plantilla.replace("[MONTH]",MonthEng[mes])
				plantilla=plantilla.replace("[DAY]",String.valueOf(dayOfMonth))
				plantilla=plantilla.replace("[YEAR]",annio)
				plantilla=plantilla.replace("[HOUR]",hora)
				plantilla=plantilla.replace("[MIN]",minuto)
			} else {
				plantilla=plantilla.replace("[DAY]",String.valueOf(dayOfMonth))
				plantilla=plantilla.replace("[MONTH]",Month[mes])
				plantilla=plantilla.replace("[YEAR]",annio)
				plantilla=plantilla.replace("[HOUR]",hora)
				plantilla=plantilla.replace("[MIN]",minuto)
			}
			//8 Seccion table atributos usuario
			errorlog += "| Variable8.1 listado de correos copia"
			String tablaUsuario= ""
			String plantillaTabla="<tr> <td align= \"left \" valign= \"top \" style= \"text-align: justify; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #585858; font-size: 17px; line-height: 25px; \"> [clave] </span> </font> </td> <td align= \"left \" valign= \"top \" style= \"text-align: justify; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #ff5a00; font-size: 17px; line-height: 25px; \"> [valor] </span> </font> </td> </tr>"
			errorlog += "| Variable8.2 object.correo=" + object.correo
			correo=object.correo;
			errorlog += "| Variable8.3 cn.getAsunto()=" + cn.getAsunto()
			asunto=cn.getAsunto();
			errorlog += "| Variable8.4 cn.getLstCorreoCopia().size()=" + cn.getLst_correo_copia().size()
			if(cn.getLst_correo_copia().size()>0) {
				for(String row: cn.getLst_correo_copia()) {
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
				List<PSGRCatImageNotificacion> lci = catImageNotificacion.findByCaseId(Long.valueOf(procesoCaso.getCaseId()), 0, 999)
				Integer numero= 0;
				errorlog += "| Variable9.2 lci.size()=" + lci.size()
				if(lci.size()>0) {
//					plantilla= plantilla.replace("<!--[PASOS]-->", "<table class=\"table table-bordered\"> <tbody> [pasos] </tbody> </table>")
					for(PSGRCatImageNotificacion ci: lci) {
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
//								tablaPasos += plantillaPasos.replace("[imagen]", imagen).replace("[numero]", numero+"").replace("[titulo]", ci.getTitulo()).replace("[descripcion]", ci.getTexto())
							}
						}
					}
				}
			} catch (Exception e) {
				errorlog += "| Fallo al momento de obtener los pasos"
			}
			
			errorlog += "| Variable11"
//			plantilla=plantilla.replace("[pasos]", tablaPasos)
			
			
			
			errorlog += "| Variable13"
			if(!cn.getContenido_leonel().equals("") ) {
//				plantilla=plantilla.replace("<!--Leonel-->", "<table width=\"80%\"> <thead></thead> <tbody> <tr> <td width=\"25%\" style=\"text-align: right;\"> <img style=\"width: 145px;\" src=\"https://bpmpreprod.blob.core.windows.net/publico/Leoneldmnisiones_Mesa%20de%20trabajo%201.png\"> </td> <td class=\"col-6\"> <div class=\"arrow_box\" style=\"position: relative; background: #ff5900; border: 4px solid #ff5900;border-radius: 50px;\"> <h6 class=\"logo\" style=\"font-size: 12px; padding: 10px; color: white; font-weight: 500;font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif;\"> [leonel]</h6> </div> </td> </tr> </tbody> </table>"+"<hr>")
//				plantilla=plantilla.replace("[leonel]", cn.getContenido_leonel())
			}
			
			errorlog += "| Variable15"
			
			encoded=""
			if(object.codigo.equals("examenentrevista")) {
				SesionesDAO sd = new SesionesDAO()
				resultado = sd.getDatosSesionUsername(object.correo)
				object.data = resultado.getData();
				for (def index = 0; index < resultado.getData().size(); index++) {
//					def element = resultado.getData().get(index);
//					plantilla=plantilla.replace("[SNOMBRE]",  element.snombre)
//					plantilla=plantilla.replace("[SDESCRIPCION]",  element.sdescripcion)
					if(element.descripcion == "Examen de aptitudes y conocimientos"){
//						plantilla=plantilla.replace("[NOMBRE-COLLAGE]",  element.pnombre)
//						plantilla=plantilla.replace("[DESCRIPCION-COLLAGE]",   element.pdescripcion==null?"":element.pdescripcion)
//						plantilla=plantilla.replace("[FECHA-COLLAGE]",   element.aplicacion.split("-")[2]+"-"+element.aplicacion.split("-")[1]+"-"+element.aplicacion.split("-")[0])
//						plantilla=plantilla.replace("[HORA-COLLAGE]",   element.horario)
//						plantilla=plantilla.replace("[Lugar-EAC]",   element.online=='t'?"URL":"Lugar")
//						plantilla=plantilla.replace("[LUGAR-COLLAGE]",   element.lugar)
						if(element.calle==null && element.numero_int==null && element.colonia==null) {
//							plantilla=plantilla.replace("<tr> <td style=\"color: rgb(255, 89, 0);\">Dirección</td> <td>[DIRECCION-COLLAGE]</td> </tr>","")
						}
//						plantilla=plantilla.replace("[DIRECCION-COLLAGE]",   (element.calle==null?"":element.calle+" - ")+((element.numero_int==null)?"":"#"+element.numero_int )+"  "+ (element.colonia==null?"":element.colonia))
					}
					if(element.descripcion == "Examen Psicométrico"){
//						plantilla=plantilla.replace("[NOMBRE-PSICOMETRICO]",  element.pnombre)
//						plantilla=plantilla.replace("[DESCRIPCION-PSICOMETRICO]",   element.pdescripcion==null?"":element.pdescripcion)
//						plantilla=plantilla.replace("[FECHA-PSICOMETRICO]",   element.aplicacion.split("-")[2]+"-"+element.aplicacion.split("-")[1]+"-"+element.aplicacion.split("-")[0])
//						plantilla=plantilla.replace("[HORA-PSICOMETRICO]",   element.horario)
//						plantilla=plantilla.replace("[Lugar-EP]",   element.online=='t'?"URL":"Lugar")
//						plantilla=plantilla.replace("[LUGAR-PSICOMETRICO]",   element.lugar)
						if(element.calle==null && element.numero_int==null && element.colonia==null) {
//							plantilla=plantilla.replace("<tr> <td style=\"color: rgb(255, 89, 0);\">Dirección</td> <td>[DIRECCION-PSICOMETRICO]</td> </tr>","")
						}
//						plantilla=plantilla.replace("[DIRECCION-PSICOMETRICO]",   (element.calle==null?"":element.calle+" - ")+((element.numero_int==null)?"":"#"+element.numero_int )+"  "+ (element.colonia==null?"":element.colonia))
					}
					if(element.descripcion == "Entrevista"){
//						plantilla=plantilla.replace("[NOMBRE-ENTREVISTA]",  element.pnombre)
//						plantilla=plantilla.replace("[DESCRIPCION-ENTREVISTA]",   element.pdescripcion==null?"":element.pdescripcion)
//						plantilla=plantilla.replace("[FECHA-ENTREVISTA]",   element.aplicacion.split("-")[2]+"-"+element.aplicacion.split("-")[1]+"-"+element.aplicacion.split("-")[0])
//						plantilla=plantilla.replace("[HORA-ENTREVISTA]",   element.horario)
//						plantilla=plantilla.replace("[Lugar-E]",   element.online=='t'?"URL":"Lugar")
//						plantilla=plantilla.replace("[LUGAR-ENTREVISTA]",   element.lugar)
						if(element.calle==null && element.numero_int==null && element.colonia==null) {
//							plantilla=plantilla.replace("<tr> <td style=\"color: rgb(255, 89, 0);\">Dirección</td> <td>[DIRECCION-ENTREVISTA]</td> </tr>","")
						}
//						plantilla=plantilla.replace("[DIRECCION-ENTREVISTA]",   (element.calle==null?"":element.calle+" - ")+((element.numero_int==null)?"":"#"+element.numero_int )+"  "+ (element.colonia==null?"":element.colonia))
	
					}
				}
			} else if(object.codigo.equals("registrar") && object.isEnviar) {
//				plantilla = plantilla.replace("[href-confirmar]", objProperties.getUrlHost() + "/bonita/apps/login/activate/?correo=" + object.correo + "");
			} else if (object.codigo.equals("transferencia")) {
				try {
					closeCon = validarConexion();
					pstm = con.prepareStatement(Statements.GET_ASPIRANTE_TRANSFERENCIA)
					pstm.setString(1, object.correo)
					rs = pstm.executeQuery()
					if(rs.next()) {
//						plantilla=plantilla.replace("[UNIVERSIDAD-DESTINO]", rs.getString("campusnuevo"))
//						plantilla=plantilla.replace("[UNIVERSIDAD-PROCEDENTE]", rs.getString("campusanterior"))
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
//							plantilla=plantilla.replace("[FALTA-ASISTENCIA-PCA]", "asistencia")
						} else if(rs.getString("descripcion").equals("Examen Psicométrico")) {
//							plantilla=plantilla.replace("[FALTA-ASISTENCIA-P]", "asistencia")
						} else {
//							plantilla=plantilla.replace("[FALTA-ASISTENCIA-E", "asistencia")
						}
						
						
						
					}
//					plantilla=plantilla.replace("[FALTA-ASISTENCIA-PCA]", "falta")
//					plantilla=plantilla.replace("[FALTA-ASISTENCIA-P]", "falta")
//					plantilla=plantilla.replace("[FALTA-ASISTENCIA-E", "falta")
				} catch (Exception e) {
					errorlog += "| TRANSFERENCIA " + e.getMessage()
				}finally {
				if(closeCon) {
					new DBConnect().closeObj(con, stm, rs, pstm);
				}
					
				}
			} else if(object.codigo.equals("carta-aceptar") || object.codigo.equals("carta-rechazo") || object.codigo.equals("carta-pdu")|| object.codigo.equals("carta-informacion") || object.codigo.equals("carta-propedeutico")) {
				try {
					PSGRCatDocumentosTextos dt = new PSGRCatDocumentosTextos();
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
						dt.ciudad_carta=rs.getString("ciudad_carta");
						dt.estado_carta=rs.getString("estado_carta");
						dt.documentos_entregar=rs.getString("documentos_entregar");
						dt.documentos_entregar_extranjero=rs.getString("documentos_entregar_extranjero");
						dt.notas_documentos=rs.getString("notas_documentos");
						dt.parrafo_matematicas_1=rs.getString("parrafo_matematicas_1");
						dt.parrafo_matematicas_2=rs.getString("parrafo_matematicas_2");
						dt.parrafo_matematicas_3=rs.getString("parrafo_matematicas_3");
						dt.parrafo_espanol_1=rs.getString("parrafo_espanol_1");
						dt.parrafo_espanol_2=rs.getString("parrafo_espanol_2");
						dt.parrafo_espanol_3=rs.getString("parrafo_espanol_3");
						dt.director_admisiones=rs.getString("director_admisiones");
						dt.titulo_director_admisiones=rs.getString("titulo_director_admisiones");
						dt.correo_director_admisiones=rs.getString("correo_director_admisiones");
						dt.telefono_director_admisiones=rs.getString("telefono_director_admisiones");
						dt.actividad_ingreso_1=rs.getString("actividad_ingreso_1");
						dt.actividad_ingreso_2=rs.getString("actividad_ingreso_2");
						dt.costo_sgm=rs.getString("costo_sgm");
						dt.educacion_garantizada=rs.getString("educacion_garantizada");
						dt.instrucciones_pago_banco=rs.getString("instrucciones_pago_banco");
						dt.instrucciones_pago_caja=rs.getString("instrucciones_pagoc_aja");
						dt.cancelar_seguro_gastos_medicos=rs.getString("cancelar_seguro_gastos_medicos");
						dt.curso_matematicas_1=rs.getString("curso_matematicas_1");
						dt.curso_matematicas_2=rs.getString("curso_matematicas_2");
						
						try{
//							plantilla=plantilla.replace("[CIUDAD-CARTA]",dt.ciudad_carta);
						}
						catch(Exception dex){
//							plantilla=plantilla.replace("[CIUDAD-CARTA]","");
						}
						try{
//							plantilla=plantilla.replace("[ESTADO-CARTA]",dt.estado_carta);
						}
						catch(Exception dex){
//							plantilla=plantilla.replace("[ESTADO-CARTA]","");
						}
						try{
//							plantilla=plantilla.replace("[DOCUMENTOS-ENTREGAR]",dt.documentos_entregar);
						}
						catch(Exception dex){
//							plantilla=plantilla.replace("[DOCUMENTOS-ENTREGAR]","");
						}
						try{
//							plantilla=plantilla.replace("[DOCUMENTOS-EXTRANJEROS]",dt.documentos_entregar_extranjero);
						}
						catch(Exception dex){
//							plantilla=plantilla.replace("[DOCUMENTOS-EXTRANJEROS]","");
						}
						try{
//							plantilla=plantilla.replace("[NOTAS-DOCUMENTOS]",dt.notas_documentos);
						}
						catch(Exception dex){
//							plantilla=plantilla.replace("[NOTAS-DOCUMENTOS]","");
						}
						try{
//							plantilla=plantilla.replace("[DIRECTOR-ADMISIONES]",dt.director_admisiones);
						}
						catch(Exception dex){
//							plantilla=plantilla.replace("[DIRECTOR-ADMISIONES]","");
						}
						try{
//							plantilla=plantilla.replace("[TITULO-DIRECTOR-ADMISIONES]",dt.titulo_director_admisiones);
						}
						catch(Exception dex){
//							plantilla=plantilla.replace("[TITULO-DIRECTOR-ADMISIONES]","");
						}
						try{
//							plantilla=plantilla.replace("[CORREO-DIRECTOR-ADMISIONES]",dt.correo_director_admisiones);
						}
						catch(Exception dex){
//							plantilla=plantilla.replace("[CORREO-DIRECTOR-ADMISIONES]","");
						}
						try{
//							plantilla=plantilla.replace("[TELEFONO-DIRECTOR-ADMISIONES]",dt.telefono_director_admisiones);
						}
						catch(Exception dex){
//							plantilla=plantilla.replace("[TELEFONO-DIRECTOR-ADMISIONES]","");
						}
						try{
//							plantilla=plantilla.replace("[COSTO-SGM]",dt.costo_sgm);
						}catch(Exception dex){
//							plantilla=plantilla.replace("[COSTO-SGM]","");
						}
						try{
//							plantilla=plantilla.replace("[EDUCACION-GARANTIZADA]",dt.educacion_garantizada);
						}
						catch(Exception dex){
//							plantilla=plantilla.replace("[EDUCACION-GARANTIZADA]","");
						}
						try{
//							plantilla=plantilla.replace("[INSTRUCCIONES-PAGO-BANCO]",dt.instrucciones_pago_banco);
						}
						catch(Exception dex){
//							plantilla=plantilla.replace("[INSTRUCCIONES-PAGO-BANCO]","");
						}
						try{
//							plantilla=plantilla.replace("[CANCELAR-SEGURO-GASTOS]",dt.cancelar_seguro_gastos_medicos);
						}
						catch(Exception dex){
//							plantilla=plantilla.replace("[CANCELAR-SEGURO-GASTOS]","");
						}
						try{
//							plantilla=plantilla.replace("[INSTRUCCIONES-PAGO-CAJA]",dt.instrucciones_pago_caja);
						}
						catch(Exception dex){
//							plantilla=plantilla.replace("[INSTRUCCIONES-PAGO-CAJA]","");
						}
						
					}
					
					pstm = con.prepareStatement(Statements.GET_INFOCARTATEMPORAL_PLANTILLA)
					pstm.setString(1, object.correo)
					rs = pstm.executeQuery()
					if(rs.next()) {
						try {
//						plantilla=plantilla.replace("[|persistenceid|]",rs.getString("persistenceid"));
//						plantilla=plantilla.replace("[|admitido|]",rs.getString("admitido"));
//						plantilla=plantilla.replace("[|campusadmision|]",rs.getString("campusadmision"));
//						plantilla=plantilla.replace("[|carrera|]",rs.getString("carrera"));
//						plantilla=plantilla.replace("[|carta|]",rs.getString("carta"));
//						plantilla=plantilla.replace("[|curp|]",rs.getString("curp"));
//						plantilla=plantilla.replace("[|decisiondeadmision|]",rs.getString("decisiondeadmision"));
//						plantilla=plantilla.replace("[|descuentoprontopagomes1|]",rs.getString("descuentoprontopagomes1"));
//						plantilla=plantilla.replace("[|descuentoprontopagomes1fecha|]",rs.getString("descuentoprontopagomes1fecha"));
//						plantilla=plantilla.replace("[|descuentoprontopagomes2|]",rs.getString("descuentoprontopagomes2"));
//						plantilla=plantilla.replace("[|descuentoprontopagomes2fecha|]",rs.getString("descuentoprontopagomes2fecha"));
//						plantilla=plantilla.replace("[|eema|]",rs.getString("eema"));
//						plantilla=plantilla.replace("[|eemi|]",rs.getString("eemi"));
//						plantilla=plantilla.replace("[|eemn|]",rs.getString("eemn"));
//						plantilla=plantilla.replace("[|eemq|]",rs.getString("eemq"));
//						plantilla=plantilla.replace("[|eems|]",rs.getString("eems"));
//						plantilla=plantilla.replace("[|eemt|]",rs.getString("eemt"));
//						plantilla=plantilla.replace("[|escm|]",rs.getString("escm"));
//						plantilla=plantilla.replace("[|escp|]",rs.getString("escp"));
//						plantilla=plantilla.replace("[|espanol|]",rs.getString("espanol"));
//						plantilla=plantilla.replace("[|examinado|]",rs.getString("examinado"));
//						plantilla=plantilla.replace("[|fechalimitedepago|]",rs.getString("fechalimitedepago"));
//						plantilla=plantilla.replace("[|fechalimiteentregadocumentacion|]",rs.getString("fechalimiteentregadocumentacion"));
//						plantilla=plantilla.replace("[|filaexcel|]",rs.getString("filaexcel"));
//						plantilla=plantilla.replace("[|inscrito|]",rs.getString("inscrito"));
//						plantilla=plantilla.replace("[|mmpi|]",rs.getString("mmpi"));
//						plantilla=plantilla.replace("[|nivel|]",rs.getString("nivel"));
//						plantilla=plantilla.replace("[|nombre|]",rs.getString("nombre"));
//						plantilla=plantilla.replace("[|notastransferencia|]",rs.getString("notastransferencia"));
//						plantilla=plantilla.replace("[|numerodematricula|]",rs.getString("numerodematricula"));
//						plantilla=plantilla.replace("[|paa|]",rs.getString("paa"));
//						plantilla=plantilla.replace("[|paanumerica|]",rs.getString("paanumerica"));
//						plantilla=plantilla.replace("[|paaverbal|]",rs.getString("paaverbal"));
//						plantilla=plantilla.replace("[|pagoinscripcion|]",rs.getString("pagoinscripcion"));
//						plantilla=plantilla.replace("[|para|]",rs.getString("para"));
//						plantilla=plantilla.replace("[|pca|]",rs.getString("pca"));
//						plantilla=plantilla.replace("[|pcda|]",rs.getString("pcda"));
//						plantilla=plantilla.replace("[|pdp|]",rs.getString("pdp"));
//						plantilla=plantilla.replace("[|pdu|]",rs.getString("pdu"));
//						plantilla=plantilla.replace("[|periodo|]",rs.getString("periodo"));
//						plantilla=plantilla.replace("[|persistenceversion|]",rs.getString("persistenceversion"));
//						plantilla=plantilla.replace("[|pia|]",rs.getString("pia"));
//						plantilla=plantilla.replace("[|preparatoriade|]",rs.getString("preparatoriade"));
//						plantilla=plantilla.replace("[|promedio|]",rs.getString("promedio"));
//						plantilla=plantilla.replace("[|religion|]",rs.getString("religion"));
//						plantilla=plantilla.replace("[|selecciondecursosprevia|]",rs.getString("selecciondecursosprevia"));
//						plantilla=plantilla.replace("[|sesion|]",rs.getString("sesion"));
//						plantilla=plantilla.replace("[|sexo|]",rs.getString("sexo"));
//						plantilla=plantilla.replace("[|sihaceonomatematicas|]",rs.getString("sihaceonomatematicas"));
//						plantilla=plantilla.replace("[|solicitante|]",rs.getString("solicitante"));
//						plantilla=plantilla.replace("[|sse|]",rs.getString("sse"));
//						plantilla=plantilla.replace("[|statuspdu|]",rs.getString("statuspdu"));
//						plantilla=plantilla.replace("[|tipodeadmision|]",rs.getString("tipodeadmision"));
//						plantilla=plantilla.replace("[|tipodeestudiante|]",rs.getString("tipodeestudiante"));
//						plantilla=plantilla.replace("[|universidad|]",rs.getString("universidad"));
//						plantilla=plantilla.replace("[|notaslistaroja|]",rs.getString("notaslistaroja"));
//						plantilla=plantilla.replace("[|seleccionado|]",rs.getString("seleccionado"));
						cartaenviar=true;
						}catch(Exception infex) {
							
						}
						/*plantilla=plantilla.replace("<!-- GUIA DE ESTUDIO-->", "<table width=\"80%\"> <thead></thead> <tbody> <tr style=\"text-align: center;\"> <td class=\"col-12\"> <div class=\"row\" > <div class=\"col-12 form-group color-titulo\"> <a href=\"[guia-src]\" target=\"_blank\" style=\"text-decoration: underline; cursor: pointer;\"><img style=\"\" src=\"https://bpmpreprod.blob.core.windows.net/publico/Gu%C3%ADa%20de%20estudios.png\" alt=\"no disponible\"></a> </div> </div> </td> </tr> </tbody> </table> <hr>")
						plantilla=plantilla.replace("[guia-src]", dt.urlGuiaExamenCB)
						plantilla=plantilla.replace("[TIPS]",dt.tipsCB);
						plantilla=plantilla.replace("<!--[PASOS]-->","<table style='width:80%; font-size: initial; font-family:  Arial;'><tr><td style='font-family:  Arial;'>"+dt.noSabes+"</td></tr></table>");
						plantilla=plantilla.replace("[LIGA-PARA-TEST-VOCACIONAL]",dt.urlTestVocacional);*/
						try {
							
//							plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO1]",(costo1-(costo1*(Float.parseFloat(rs.getString("descuentoprontopagomes1"))/100)))+Float.parseFloat(dt.costoSGM)+"")
//							plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO2]",(costo1-(costo1*(Float.parseFloat(rs.getString("descuentoprontopagomes2"))/100)))+Float.parseFloat(dt.costoSGM)+"")
						
						} catch (Exception dex) {
//							plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO1]",dex.getMessage())
//							plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO2]",dex.getMessage())
						}
						try {
//							plantilla=plantilla.replace("[MATEMATICAS-1]",(rs.getString("sihaceonomatematicas").equals("1"))?dt.parrafo_matematicas_1:"");
//							plantilla=plantilla.replace("[MATEMATICAS-2]",(rs.getString("sihaceonomatematicas").equals("2"))?dt.parrafo_matematicas_2:"");
//							plantilla=plantilla.replace("[MATEMATICAS-3]",(rs.getString("sihaceonomatematicas").equals("3"))?dt.parrafo_matematicas_3:"");
//							plantilla=plantilla.replace("[ESPANOL-1]",(rs.getString("espanol").equals("1"))?dt.parrafo_espanol_1:"");
//							plantilla=plantilla.replace("[ESPANOL-2]",(rs.getString("espanol").equals("2"))?dt.parrafo_espanol_2:"");
//							plantilla=plantilla.replace("[ESPANOL-3]",(rs.getString("espanol").equals("3"))?dt.parrafo_espanol_3:"");
//							plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-ENERO]",(periodo.substring(4,6).equals("10")|| periodo.substring(4,6).equals("05"))?dt.actividad_ingreso_1:"");
//							plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-AGOSTO]",(periodo.substring(4,6).equals("60")|| periodo.substring(4,6).equals("35")|| periodo.substring(4,6).equals("75"))?dt.actividad_ingreso_2:"");
//							plantilla=plantilla.replace("[CURSO-MATEMATICAS-ENERO]",(periodo.substring(4,6).equals("10") || periodo.substring(4,6).equals("05"))?dt.curso_matematicas_1:"");
//							plantilla=plantilla.replace("[CURSO-MATEMATICAS-AGOSTO]",(periodo.substring(4,6).equals("60")|| periodo.substring(4,6).equals("35")|| periodo.substring(4,6).equals("75"))?dt.curso_matematicas_2:"");
							try {
								Float descuento = (costo1-(costo1*(Float.parseFloat(rs.getString("descuentoprontopagomes1"))/100)))+Float.parseFloat(dt.costo_sgm)
//								plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO1]",descuento.toString());
							} catch (Exception e) {
//								plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO1]",dt.costo_sgm);
							}
							try {
								Float descuento = (costo1-(costo1*(Float.parseFloat(rs.getString("descuentoprontopagomes2"))/100)))+Float.parseFloat(dt.costo_sgm)
//								plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO2]",descuento.toString());
							} catch (Exception e) {
//								plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO2]",dt.costo_sgm);
							}
							
							
							
							
						} catch (Exception e) {
//							plantilla=plantilla.replace("[MATEMATICAS-1]","");
//							plantilla=plantilla.replace("[MATEMATICAS-2]","");
//							plantilla=plantilla.replace("[MATEMATICAS-3]","");
//							plantilla=plantilla.replace("[ESPANOL-1]","");
//							plantilla=plantilla.replace("[ESPANOL-2]","");
//							plantilla=plantilla.replace("[ESPANOL-3]","");
//							plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-ENERO]","");
//							plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-AGOSTO]","");
//							plantilla=plantilla.replace("[CURSO-MATEMATICAS-ENERO]","");
//							plantilla=plantilla.replace("[CURSO-MATEMATICAS-AGOSTO]","");
							
						}
//						plantilla=plantilla.replace("style=\"background-color: inherit;\"", "style=\"background-color: transparent;\"")
												
					}else {
						pstm = con.prepareStatement(Statements.GET_INFOCARTA_PLANTILLA)
						pstm.setString(1, object.correo)
						rs = pstm.executeQuery()
						if(rs.next()) {
							try {
//							plantilla=plantilla.replace("[|persistenceid|]",rs.getString("persistenceid"));
//							plantilla=plantilla.replace("[|carta|]",rs.getString("carta"));
//							plantilla=plantilla.replace("[|curp|]",rs.getString("curp"));
//							plantilla=plantilla.replace("[|descuentoprontopagomes1|]",rs.getString("descuentoprontopagomes1"));
//							plantilla=plantilla.replace("[|descuentoprontopagomes1fecha|]",rs.getString("descuentoprontopagomes1fecha"));
//							plantilla=plantilla.replace("[|descuentoprontopagomes2|]",rs.getString("descuentoprontopagomes2"));
//							plantilla=plantilla.replace("[|descuentoprontopagomes2fecha|]",rs.getString("descuentoprontopagomes2fecha"));
//							plantilla=plantilla.replace("[|espanol|]",rs.getString("espanol"));
//							plantilla=plantilla.replace("[|fechalimitedepago|]",rs.getString("fechalimitedepago"));
//							plantilla=plantilla.replace("[|fechalimiteentregadocumentacion|]",rs.getString("fechalimiteentregadocumentacion"));
//							plantilla=plantilla.replace("[|nombre|]",rs.getString("nombre"));
//							plantilla=plantilla.replace("[|notastransferencia|]",rs.getString("notastransferencia"));
//							plantilla=plantilla.replace("[|numerodematricula|]",rs.getString("numerodematricula"));
//							plantilla=plantilla.replace("[|pca|]",rs.getString("pca"));
//							plantilla=plantilla.replace("[|pcda|]",rs.getString("pcda"));
//							plantilla=plantilla.replace("[|pdp|]",rs.getString("pdp"));
//							plantilla=plantilla.replace("[|pdu|]",rs.getString("pdu"));
//							plantilla=plantilla.replace("[|persistenceversion|]",rs.getString("persistenceversion"));
//							plantilla=plantilla.replace("[|pia|]",rs.getString("pia"));
//							plantilla=plantilla.replace("[|sihaceonomatematicas|]",rs.getString("sihaceonomatematicas"));
//							plantilla=plantilla.replace("[|sse|]",rs.getString("sse"));
//							plantilla=plantilla.replace("[|statuspdu|]",rs.getString("statuspdu"));
//							plantilla=plantilla.replace("[|universidad|]",rs.getString("universidad"));
//							plantilla=plantilla.replace("[|notaslistaroja|]",rs.getString("notaslistaroja"));
							cartaenviar=true;
							}catch(Exception infex) {
								
							}
							/*plantilla=plantilla.replace("<!-- GUIA DE ESTUDIO-->", "<table width=\"80%\"> <thead></thead> <tbody> <tr style=\"text-align: center;\"> <td class=\"col-12\"> <div class=\"row\" > <div class=\"col-12 form-group color-titulo\"> <a href=\"[guia-src]\" target=\"_blank\" style=\"text-decoration: underline; cursor: pointer;\"><img style=\"\" src=\"https://bpmpreprod.blob.core.windows.net/publico/Gu%C3%ADa%20de%20estudios.png\" alt=\"no disponible\"></a> </div> </div> </td> </tr> </tbody> </table> <hr>")
							plantilla=plantilla.replace("[guia-src]", dt.urlGuiaExamenCB)
							plantilla=plantilla.replace("[TIPS]",dt.tipsCB);
							plantilla=plantilla.replace("<!--[PASOS]-->","<table style='width:80%; font-size: initial; font-family:  Arial;'><tr><td style='font-family:  Arial;'>"+dt.noSabes+"</td></tr></table>");
							plantilla=plantilla.replace("[LIGA-PARA-TEST-VOCACIONAL]",dt.urlTestVocacional);*/
							try {
//								plantilla=plantilla.replace("[MATEMATICAS-1]",(rs.getString("sihaceonomatematicas").equals("1"))?dt.parrafo_matematicas_1:"");
//								plantilla=plantilla.replace("[MATEMATICAS-2]",(rs.getString("sihaceonomatematicas").equals("2"))?dt.parrafo_matematicas_2:"");
//								plantilla=plantilla.replace("[MATEMATICAS-3]",(rs.getString("sihaceonomatematicas").equals("3"))?dt.parrafo_matematicas_3:"");
//								plantilla=plantilla.replace("[ESPANOL-1]",(rs.getString("espanol").equals("1"))?dt.parrafo_espanol_1:"");
//								plantilla=plantilla.replace("[ESPANOL-2]",(rs.getString("espanol").equals("2"))?dt.parrafo_espanol_2:"");
//								plantilla=plantilla.replace("[ESPANOL-3]",(rs.getString("espanol").equals("3"))?dt.parrafo_espanol_3:"");
//								plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-ENERO]",(periodo.substring(4,6).equals("10")|| periodo.substring(4,6).equals("05"))?dt.actividad_ingreso_1:"");
//								plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-AGOSTO]",(periodo.substring(4,6).equals("60")|| periodo.substring(4,6).equals("35")|| periodo.substring(4,6).equals("75"))?dt.actividad_ingreso_2:"");
//								plantilla=plantilla.replace("[CURSO-MATEMATICAS-ENERO]",(periodo.substring(4,6).equals("10") || periodo.substring(4,6).equals("05"))?dt.curso_matematicas_1:"");
//								plantilla=plantilla.replace("[CURSO-MATEMATICAS-AGOSTO]",(periodo.substring(4,6).equals("60")|| periodo.substring(4,6).equals("35")|| periodo.substring(4,6).equals("75"))?dt.curso_matematicas_2:"");
							try {
									Float descuento = (costo1-(costo1*(Float.parseFloat(rs.getString("descuentoprontopagomes1"))/100)))+Float.parseFloat(dt.costo_sgm)
//									plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO1]",descuento.toString());
								} catch (Exception e) {
									plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO1]",e.getMessage());
								}
								try {
									Float descuento = (costo1-(costo1*(Float.parseFloat(rs.getString("descuentoprontopagomes2"))/100)))+Float.parseFloat(dt.costo_sgm)
//									plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO2]",descuento.toString());
								} catch (Exception e) {
//									plantilla=plantilla.replace("[COSTO-SGM-DESCUENTO2]",e.getMessage());
								}
								
								
								
								
							} catch (Exception e) {
//								plantilla=plantilla.replace("[MATEMATICAS-1]","");
//								plantilla=plantilla.replace("[MATEMATICAS-2]","");
//								plantilla=plantilla.replace("[MATEMATICAS-3]","");
//								plantilla=plantilla.replace("[ESPANOL-1]","");
//								plantilla=plantilla.replace("[ESPANOL-2]","");
//								plantilla=plantilla.replace("[ESPANOL-3]","");
//								plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-ENERO]","");
//								plantilla=plantilla.replace("[ACTIVIDADES-DE-INGRESO-AGOSTO]","");
//								plantilla=plantilla.replace("[CURSO-MATEMATICAS-ENERO]","");
//								plantilla=plantilla.replace("[CURSO-MATEMATICAS-AGOSTO]","");
								
							}
//							plantilla=plantilla.replace("style=\"background-color: inherit;\"", "style=\"background-color: transparent;\"")
													
						}
					}
				} catch (Exception e) {
					errorlog += "| TRANSFERENCIA " + e.getMessage()
				}finally {
				if(closeCon) {
					new DBConnect().closeObj(con, stm, rs, pstm);
				}
					
				}
			} else if(object.codigo.equals("BC_SOLICITUD_BECASYFINAN_AUTORIZADA")) {
				errorlog += "| PLANTILLA :: BC_SOLICITUD_BECASYFINAN_AUTORIZADA  ";
				Integer costoCredito = 0;
				Integer creditosemestre = 0;
				Integer parcialidad = 0;
				Integer porcentajebecaautorizacion = 0;
				Integer porcentajecreditoautorizacion = 0;
				Integer descuentoanticipadoautorizacion = 0;
				Integer inscripcionmayo = 0;
				Integer inscripcionseptiembre = 0;
				Integer inscripcionagosto = 0;
				Integer inscripcionenero = 0;
				String descripcionPeriodo = "";

				//OBTENIENDO
				try {
					closeCon = validarConexion();
					pstm = con.prepareStatement(Statements.GET_SOLICITUD_APOYO_BY_CORREOELECTRONICO);
					pstm.setString(1, object.correo);
					rs = pstm.executeQuery();
					if(rs.next()) {
						errorlog += "| BC_SOLICITUD_BECASYFINAN_AUTORIZADA SOLICITUD ENCONTRADA ";
						creditosemestre = rs.getInt("creditosemestre");
						parcialidad = rs.getInt("parcialidad");
						porcentajebecaautorizacion = rs.getInt("porcentajebecaautorizacion");
						porcentajecreditoautorizacion = rs.getInt("porcentajecreditoautorizacion");
						descuentoanticipadoautorizacion = rs.getInt("descuentoanticipadoautorizacion");
						descripcionPeriodo = rs.getString("descripcion");
						inscripcionmayo = rs.getInt("inscripcionmayo");
						inscripcionseptiembre = rs.getInt("inscripcionseptiembre");
						inscripcionagosto = rs.getInt("inscripcionagosto");
						inscripcionenero = rs.getInt("inscripcionenero");
						Long sdaecatgestionescolar_pid = rs.getLong("sdaecatgestionescolar_pid");
						//OBTENCION De LOS CREDITOS
						closeCon = validarConexion();
						pstm = con.prepareStatement(Statements.GET_SDAECAT_CREDITO_GE);
						pstm.setLong(1, sdaecatgestionescolar_pid);
						pstm.setString(2, descripcionPeriodo.split(" ")[1]);
						rs = pstm.executeQuery();
						errorlog +=
							" | " + Statements.GET_SDAECAT_CREDITO_GE +
							" | " + sdaecatgestionescolar_pid +
							(" | " + descripcionPeriodo.split(" ")[1]
						);
						if(rs.next()) {
							costoCredito = rs.getInt("CREDITOAGOSTO");
							errorlog += "| BC_SOLICITUD_BECASYFINAN_AUTORIZADA  DATOS DEL PERIODO ENCONTRADOS  ";
							Integer montoInscripcion = inscripcionagosto;
							Integer montoBeca = (inscripcionagosto - (inscripcionagosto * (porcentajebecaautorizacion * 0.01)));
							Integer montoBecaFinanciamiento = (inscripcionagosto - (inscripcionagosto * ((porcentajebecaautorizacion * 0.01) + (porcentajecreditoautorizacion * 0.01))));
							Integer montoFinanciamiento = (inscripcionagosto - (inscripcionagosto * (porcentajecreditoautorizacion * 0.01)));
							Integer montoCreditos = costoCredito;
							Integer montoColegiaturaNormal = montoCreditos * 48; //48 para el ejemplo en pantalla
							Integer montoColegiaturaBeca = montoColegiaturaNormal - (montoCreditos * ((porcentajebecaautorizacion * 0.01)));
							Integer montoColegiaturaBecaFinanciamiento = (montoColegiaturaNormal - (montoColegiaturaNormal * ((porcentajebecaautorizacion * 0.01) + (porcentajecreditoautorizacion * 0.01))));
							Integer montoColegiaturaFinanciamiento = montoColegiaturaNormal - (montoCreditos * ((porcentajebecaautorizacion * 0.01)));
							Integer montoPagototalNormal =  montoInscripcion + montoColegiaturaNormal;
							Integer mongoPagoTotalBeca = montoBeca + montoColegiaturaBeca;
							Integer mongoPagoTotalBecaFinanciamiento = montoBeca + montoColegiaturaBeca;
							Integer totalFinanciado = montoFinanciamiento + montoColegiaturaFinanciamiento;
							Integer interesSemestre = totalFinanciado * 0.035;
							
//							plantilla = plantilla.replace("[PERIODO]", descripcionPeriodo);
//							plantilla = plantilla.replace("[MONTO-INSCRIPCION]", formatCurrency(montoInscripcion.toString()));
//							plantilla = plantilla.replace("[MONTO-BECA]", formatCurrency(montoBeca.toString()));
//							plantilla = plantilla.replace("[MONTO-BECA-FINANCIAMIENTO]", formatCurrency(montoBecaFinanciamiento.toString()));
//							plantilla = plantilla.replace("[MONTO-FINANCIAMIENTO]", formatCurrency(montoFinanciamiento.toString()));
//							plantilla = plantilla.replace("[MONTO-CREDITOS]", formatCurrency(montoCreditos.toString()));
//							plantilla = plantilla.replace("[MONTO-COLEGIATURA-NORMAL]", formatCurrency(montoColegiaturaNormal.toString()));
//							plantilla = plantilla.replace("[MONTO-COLEGIATURA-BECA]", formatCurrency(montoColegiaturaBeca.toString()));
//							plantilla = plantilla.replace("[MONTO-COLEGIATURA-BECA-FINANCIAMIENTO]", formatCurrency(montoColegiaturaBecaFinanciamiento.toString()));
//							plantilla = plantilla.replace("[MONTO-COLEGIATURA-FINANCIAMIENTO]", formatCurrency(montoColegiaturaFinanciamiento.toString()));
//							plantilla = plantilla.replace("[MONTO-PAGOS-NORMAL]", formatCurrency((montoPagototalNormal / 4).toString()));
//							plantilla = plantilla.replace("[MONTO-PAGOS-BECA]", formatCurrency((montoPagototalNormal / 4).toString()));
//							plantilla = plantilla.replace("[MONTO-PAGOS-BECA-FINANCIAMIENTO]", formatCurrency((mongoPagoTotalBeca / 4).toString()));
//							plantilla = plantilla.replace("[MONTO-PAGOTOTAL-NORMAL]", formatCurrency(montoPagototalNormal.toString()));
//							plantilla = plantilla.replace("[MONTO-PAGOTOTAL-BECA]", formatCurrency(mongoPagoTotalBeca.toString()));
//							plantilla = plantilla.replace("[MONTO-PAGOTOTAL-BECA-FINANCIAMIENTO]", formatCurrency(mongoPagoTotalBecaFinanciamiento.toString()));
//							plantilla = plantilla.replace("[TOTAL-FINANCIADO]", formatCurrency(totalFinanciado.toString()));
//							plantilla = plantilla.replace("[INTERES-SEMESTRE]", formatCurrency(interesSemestre.toString()));
						}
					}
				} catch (Exception e) {
					errorlog += "| TRANSFERENCIA " + e.getMessage()
				} finally {
					if(closeCon) {
						new DBConnect().closeObj(con, stm, rs, pstm);
					}
				}
			} else if (object.codigo.equals("BC_MODIFICACION_AUTORIZACION")){
				//OBTENIENDO
				try {
					closeCon = validarConexion();
					pstm = con.prepareStatement(Statements.GET_SOLICITUD_APOYO_BY_CORREOELECTRONICO);
					pstm.setString(1, object.correoAspirante);
					rs = pstm.executeQuery();
					if(rs.next()) {
//						plantilla = plantilla.replace("[COMENTARIOS-CAMBIO]", rs.getString("cambiosSolicitudAutorizacionText"));
					}
				} catch (Exception e) {
					errorlog += "| TRANSFERENCIA " + e.getMessage()
				} finally {
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
//					plantilla=plantilla.replace("[HEADER-IMG]", catHffc.getHeader())
//					plantilla=plantilla.replace("[TEXTO-FOOTER]", catHffc.getFooter())
					cc=catHffc.getCopia();
					try {
//						plantilla=plantilla.replace("[firma]", generarFirma(catHffc.getCatnotificacionesfirma_pid().toString()))
						Result rfirma = getFirma("{\"estatusSolicitud\":\"Solicitud en progreso\",\"tarea\":\"Llenar solicitud\",\"lstFiltro\":[{\"columna\":\"PERSISTENCEID\",\"operador\":\"Igual a\",\"valor\":\""+catHffc.getCatnotificacionesfirma_pid().toString()+"\"}],\"type\":\"solicitudes_progreso\",\"usuario\":0,\"orderby\":\"NOMBRECOMPLETO\",\"orientation\":\"ASC\",\"limit\":20,\"offset\":0}")
						
//						plantilla=plantilla.replace("(CONTACTO DE CAMPUS DESTINO)", rfirma.data.get(0).nombreCompleto + " " +rfirma.data.get(0).apellido)
					} catch (Exception e) {
						plantilla=plantilla.replace("[firma]", "")
					}
					
				}
			} catch (Exception e) {
//				plantilla=plantilla.replace("[HEADER-IMG]", cn.getAnguloImagenHeader())
//				plantilla=plantilla.replace("[TEXTO-FOOTER]", cn.getTextoFooter())
//				plantilla=plantilla.replace("[firma]", "")
			}
			   
//			plantilla=plantilla.replace("[header-href]", cn.getEnlaceBanner())
//			plantilla=plantilla.replace("[footer-href]", cn.getEnlaceFooter())
			List<String> lstData = new ArrayList<>();
	        List<String> lstAdditionalData = new ArrayList<>();
	        lstData.add(plantilla);
	        resultado.setData(lstData);
	        
	        MailGunDAO mgd = new MailGunDAO(); // Asegúrate de instanciar la clase MailGunDAO adecuadamente
	        
	        lstAdditionalData.add("correo=" + correo);
	        lstAdditionalData.add("asunto=" + asunto);
	        lstAdditionalData.add("cc=" + cc);
			if((object.isEnviar && object.codigo!="carta-informacion") ||(object.isEnviar && object.codigo=="carta-informacion" && cartaenviar) ) {
//				resultado = mgd.sendEmailPlantilla(correo, asunto, plantilla.replace("\\", ""), cc, object.campus, context)
				if (plantilla != null) {
				    System.out.println("Contenido de la plantilla: " + plantilla);
					throw new Exception("primer if." + object);
				    resultado = mgd.sendEmailPlantilla(correo, asunto, plantilla.replace("\\", ""), cc, object.campus, context);
				} else {
					String campusAsString = object.campus.toString()
					
					asunto = "Asunto del correo";
					body = "Cuerpo del correo";
					cc = "correo_cc@example.com";
				    // Manejo de caso en que plantilla es nula, por ejemplo, asignar una cadena vacía
				    resultado = mgd.sendEmailPlantilla(correo, asunto, body, cc, campusAsString, context);
				}
				CatBitacoraCorreo catBitacoraCorreo = new CatBitacoraCorreo();
				catBitacoraCorreo.setCodigo(object.codigo)
//				catBitacoraCorreo.setDe(resultado.getAdditional_data().get(0))
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
			
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage())
			
			e.printStackTrace()
		}
		resultado.setError_info("");
		return resultado;
	}
	
	public Result insertCatBitacoraCorreos(PSGRCatBitacoraCorreos bcorreo) {
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
	
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		return retorno;
	}
	
	private String DataUsuarioAdmision(String plantilla, RestAPIContext context, String correo, PSGRCatNotificaciones cn, String errorlog,Boolean isEnviar) {
		//8 Seccion table atributos usuario
		errorlog += ", Variable8"
		String tablaUsuario= ""
		String plantillaTabla="<tr> <td align= \"left \" valign= \"top \" style= \"text-align: justify;vertical-align: bottom; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #585858; font-size: 17px; line-height: 25px; \"> [clave]: </span> </font> </td> <td align= \"left \" valign= \"top \" style= \"text-align: justify; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #ff5a00; font-size: 17px; line-height: 25px;vertical-align: bottom; \"> [valor] </span> </font> </td> </tr>"
		try {
		def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
		List<SolicitudDeAdmision> objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(correo, 0, 999)
		if(objSolicitudDeAdmision.size()>0) {
//			Result documentosTextos = new DocumentosTextosDAO().getDocumentosTextos(objSolicitudDeAdmision.get(0).getCatCampus().getPersistenceId());
			if(documentosTextos.data.size()>0) {
//				def dt = documentosTextos.data.get(0);
//				if(objSolicitudDeAdmision.get(0).necesitoAyuda && cn.getCodigo().equals("registrar")) {
//					plantilla=plantilla.replace("<!--[PASOS]-->", "<table style='width:80%; font-size: initial; font-family:  Arial;'><tr><td style='font-family:  Arial;'>"+dt.noSabes+"</td></tr></table>")
//					plantilla=plantilla.replace("[LIGA-PARA-TEST-VOCACIONAL]", dt.urlTestVocacional)
//				}
				if(!cn.docGuiaEstudio.equals("")) {
//					plantilla=plantilla.replace("<!-- GUIA DE ESTUDIO-->", "<table width=\"80%\"> <thead></thead> <tbody> <tr style=\"text-align: center;\"> <td class=\"col-12\"> <div class=\"row\" > <div class=\"col-12 form-group color-titulo\"> <a href=\"[guia-src]\" target=\"_blank\" style=\"text-decoration: underline; cursor: pointer;\"><img style=\"\" src=\"https://bpmpreprod.blob.core.windows.net/publico/Gu%C3%ADa%20de%20estudios.png\" alt=\"no disponible\"></a> </div> </div> </td> </tr> </tbody> </table> <hr>")
//					plantilla=plantilla.replace("[guia-src]", dt.urlGuiaExamenCB)
				}
				if(!dt.tipsCB.equals("")) {
//					plantilla=plantilla.replace("[TIPS]", dt.tipsCB)
				}else {
//					plantilla=plantilla.replace("[TIPS]", "")
				}
			}
//			if(objSolicitudDeAdmision.get(0).getCatSexo()!=null) {
//				plantilla=plantilla.replace("o(a)", objSolicitudDeAdmision.get(0).getCatSexo().getDescripcion().equals("Masculino")?"o":"a")
//			}else {
//				plantilla=plantilla.replace("o(a)", "o")
//			}
//			plantilla=plantilla.replace("[NOMBRE-COMPLETO]",objSolicitudDeAdmision.get(0).getPrimerNombre()+" "+objSolicitudDeAdmision.get(0).getSegundoNombre()+" "+objSolicitudDeAdmision.get(0).getApellidoPaterno()+" "+objSolicitudDeAdmision.get(0).getApellidoMaterno())
//			plantilla=plantilla.replace("[NOMBRE]",objSolicitudDeAdmision.get(0).getPrimerNombre()+" "+objSolicitudDeAdmision.get(0).getSegundoNombre())
//			plantilla=plantilla.replace("[UNIVERSIDAD]", objSolicitudDeAdmision.get(0).getCatCampusEstudio().getDescripcion())
//			plantilla=plantilla.replace("[LICENCIATURA]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().getNombre())
			//plantilla=plantilla.replace("[LICENCIATURA-COSTO1]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionagosto==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionagosto)
			//plantilla=plantilla.replace("[LICENCIATURA-COSTO2]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero)
			//plantilla=plantilla.replace("[LICENCIATURA-COSTO3]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionMayo==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionMayo)
			//plantilla=plantilla.replace("[LICENCIATURA-COSTO4]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionSeptiembre==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionSeptiembre)
			
//			periodo = objSolicitudDeAdmision.get(0).getCatPeriodo().getClave()
			
			try {
//				costo1=Float.parseFloat( periodo.substring(4,6).equals("10")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero:
//				periodo.substring(4,6).equals("05")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero:
//				periodo.substring(4,6).equals("60")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionagosto:
//				periodo.substring(4,6).equals("35")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionMayo:
//				periodo.substring(4,6).equals("75")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionSeptiembre:"0")
				
//				plantilla=plantilla.replace("[LICENCIATURA-COSTO1]", costo1.toString())
				
			} catch (Exception e) {
				e.printStackTrace()
			}
//			if(objSolicitudDeAdmision.get(0).getCatGestionEscolar().isPropedeutico()) {
//				plantilla=plantilla.replace("<!--PROPEDEUTICO-->", "<tr> <td valign=\"top\" style=\"text-align: justify;text-align: left;\"> <font face=\"'Source Sans Pro', sans-serif\" color=\"#4F4E4D\"> <span style=\"color: #4F4E4D;\"> Propedéutico: </span> </font> </td> <td valign=\"top\" style=\"text-align: justify;text-align: left;\"> <font face=\"'Source Sans Pro', sans-serif\" color=\"#4F4E4D\"> <span style=\"color: #FF5900;\"> [PROPEDEUTICO] </span> </font> </td> </tr>")
//				plantilla=plantilla.replace("[PROPEDEUTICO]", objSolicitudDeAdmision.get(0).getCatPropedeutico().getDescripcion())
//			}
//			plantilla=plantilla.replace("[CAMPUSEXAMEN]",objSolicitudDeAdmision.get(0).getCatCampus().getDescripcion())
			try {
//				plantilla=plantilla.replace("[CAMPUS]",objSolicitudDeAdmision.get(0).getCatCampusEstudio().getDescripcion())
			} catch (Exception e) {
				plantilla=plantilla.replace("[CAMPUS]","CAMPUS PREVIEW")
			}
			
//			plantilla=plantilla.replace("[CORREO]",objSolicitudDeAdmision.get(0).getCorreoElectronico())
//			plantilla=plantilla.replace("[PERIODO]",objSolicitudDeAdmision.get(0).getCatPeriodo().getDescripcion())
//			plantilla=plantilla.replace("[PREPARATORIA]",(objSolicitudDeAdmision.get(0).getCatBachilleratos().getDescripcion().equals("Otro"))?objSolicitudDeAdmision.get(0).getBachillerato():objSolicitudDeAdmision.get(0).getCatBachilleratos().getDescripcion())
//			plantilla=plantilla.replace("[PROMEDIO]",objSolicitudDeAdmision.get(0).getPromedioGeneral())
//			plantilla=plantilla.replace("[ESTATUS]",objSolicitudDeAdmision.get(0).getEstatusSolicitud())
			
			errorlog += ", Variable14"
			
			if(cn.isInformacionLic()) {
				errorlog += ", Variable14.1"
				plantilla=plantilla.replace("<!--isInformacionLic-->", "<table width=\"80%\"> <tbody> <tr style=\"text-align: center;\"> <td class=\"col-4\" style=\"width:33.33%;margin: 0; padding:0; vertical-align: middle;\"> <img style=\"width:193px\" src=\"[URL-IMG-LICENCIATURA]\"> </td> <td class=\"col-4\" style=\"width:33.33%; background: #4F4E4D; vertical-align: middle; padding: 0; margin: 0;\"> <div class=\"row\"> <div class=\"col-12 form-group color-titulo\"> <img src=\"https://i.ibb.co/C8yv3pD/sello.png\"> </div> <div class=\"col-12 color-index sub-img\" style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif;\"> <a  style=\"font-size: 15px;color: white;\" href=\"[LICENCIATURA-URL]\"  target=\"_blank\">[LICENCIATURA]</a> </div> </div> </td> <td class=\"col-4\" style=\"width:33.33%; text-decoration: underline; font-size: 9px; background: #ff5900; color: white; font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; margin: 0; padding:0; vertical-align: middle;\"> <p>[descripcion-licenciatura] </p> </td> </tr> </tbody> </table>"+"<hr>")
				errorlog += ", Variable14.2"
//				plantilla=plantilla.replace("[LICENCIATURA]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().getNombre())
				//plantilla=plantilla.replace("[LICENCIATURA-COSTO1]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionagosto==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionagosto)
				//plantilla=plantilla.replace("[LICENCIATURA-COSTO2]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero)
				//plantilla=plantilla.replace("[LICENCIATURA-COSTO3]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionMayo==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionMayo)
				//plantilla=plantilla.replace("[LICENCIATURA-COSTO4]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionSeptiembre==0?"":objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionSeptiembre)
//				periodo = objSolicitudDeAdmision.get(0).getCatPeriodo().getClave()
			
			try {
//					costo1=Float.parseFloat( periodo.substring(4,6).equals("10")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero:
//					periodo.substring(4,6).equals("05")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionenero:
//					periodo.substring(4,6).equals("60")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionagosto:
//					periodo.substring(4,6).equals("35")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionMayo:
//					periodo.substring(4,6).equals("75")?objSolicitudDeAdmision.get(0).getCatGestionEscolar().inscripcionSeptiembre:"0")
					
					plantilla=plantilla.replace("[LICENCIATURA-COSTO1]", costo1.toString())
				} catch (Exception e) {
					e.printStackTrace()
				}
				errorlog += ", Variable14.3"
//				plantilla=plantilla.replace("[descripcion-licenciatura]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().getDescripcion())
				
//				plantilla=plantilla.replace("[LICENCIATURA-URL]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().getEnlace())
				try {
//					if(objSolicitudDeAdmision.get(0).getCatGestionEscolar().getUrlImgLicenciatura().equals("")) {
//						plantilla=plantilla.replace("[URL-IMG-LICENCIATURA]", "https://bpmpreprod.blob.core.windows.net/publico/Afirma_Mesa%20de%20trabajo%201.png")
//					}else {
//						plantilla=plantilla.replace("[URL-IMG-LICENCIATURA]", objSolicitudDeAdmision.get(0).getCatGestionEscolar().getUrlImgLicenciatura())
//					}
				} catch (Exception e) {
//					plantilla=plantilla.replace("[URL-IMG-LICENCIATURA]", "https://bpmpreprod.blob.core.windows.net/publico/Afirma_Mesa%20de%20trabajo%201.png")
				}
				
				
			}
			errorlog += ", Variable15"
			try {
			def objDetalleSolicitudDAO = context.getApiClient().getDAO(DetalleSolicitudDAO.class)
			errorlog += ", Variable15.0"
//			List<DetalleSolicitud> detalleSolicitud = objDetalleSolicitudDAO.findByCaseId(objSolicitudDeAdmision.get(0).getCaseId()+"", 0, 999)
			if(detalleSolicitud.size()>0) {
				errorlog += ", Variable15.1"
//				plantilla=plantilla.replace("[IDBANNER]",detalleSolicitud.get(0).getIdBanner())
				errorlog += ", Variable15.2"
				if(isEnviar) {
//					plantilla=plantilla.replace("[RECHAZO-COMENTARIOS]",detalleSolicitud.get(0).getObservacionesRechazo())
					errorlog += ", Variable15.3"
//					plantilla=plantilla.replace("[LISTAROJA-COMENTARIOS]",detalleSolicitud.get(0).getObservacionesListaRoja())
					errorlog += ", Variable15.3"
//					plantilla=plantilla.replace("[COMENTARIOS-CAMBIO]", detalleSolicitud.get(0).getObservacionesCambio())
				}
//				if(detalleSolicitud.get(0).isCbCoincide()) {
//					plantilla=plantilla.replace("<table class=\"cbCoincide\"", "<!--table class='cbCoincide'")
//					plantilla=plantilla.replace("<br class=\"cbCoincide\">", "<br class='cbCoincide'-->")
//					plantilla=plantilla.replace("<li>Entrevista <font color=\"#FF5900\">([FALTA-ASISTENCIA-E)</font> </li>", "<!--li>Entrevista <font color=\"#FF5900\">([FALTA-ASISTENCIA-E)</font> </li-->")
//				}
			}}catch(Exception e) {
			errorlog+=" e2" + e.getMessage()
			Boolean closeCon=false;
			try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_DETALLESOLICITUD)
//			pstm.setString(1, objSolicitudDeAdmision.get(0).getCaseId()+"")
			rs = pstm.executeQuery()
				if (rs.next()) {
					errorlog += ", Variable15.1"
//					plantilla=plantilla.replace("[IDBANNER]",rs.getString("IdBanner"))
					errorlog += ", Variable15.2"
					if(isEnviar) {
//						plantilla=plantilla.replace("[RECHAZO-COMENTARIOS]",rs.getString("ObservacionesRechazo"))
						errorlog += ", Variable15.3"
//						plantilla=plantilla.replace("[LISTAROJA-COMENTARIOS]",rs.getString("ObservacionesListaRoja"))
						errorlog += ", Variable15.3"
//						plantilla=plantilla.replace("[COMENTARIOS-CAMBIO]", rs.getString("ObservacionesCambio"))
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
//				if (objSolicitudDeAdmision.get(0).urlFoto!= null ) {
//					String SSA = "";
//					pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
//					rs= pstm.executeQuery();
//					if(rs.next()) {
//						SSA = rs.getString("valor")
//					}
//					plantilla=plantilla.replace("[USR-B64]", objSolicitudDeAdmision.get(0).urlFoto+SSA)
//				}else {
//					for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(objSolicitudDeAdmision.get(0).getCaseId(), "fotoPasaporte", 0, 10)) {
//						// convert byte[] back to a BufferedImage
//						InputStream is = new ByteArrayInputStream(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId));
//						BufferedImage newBi = ImageIO.read(is);
//						
//						encoded = "data:image/png;base64, "+ Base64.getEncoder().encodeToString(toByteArray(resizeImage(newBi, 135, 180), "png"))
////						plantilla=plantilla.replace("[USR-B64]", encoded)
//					}
//			}
			}catch(Exception e) {
//				plantilla=plantilla.replace("[USR-B64]", "https://i.ibb.co/WyCsXQy/usuariofoto.jpg")
				errorlog+= ""+e.getMessage();
			}finally {
				if(closeCon) {
					new DBConnect().closeObj(con, stm, rs, pstm);
				}
			}
		}
		errorlog += ", Variable10 tablaUsuario"
		if(!tablaUsuario.equals("")) {
//			plantilla = plantilla.replace("<!--[Variables de usuario]-->", "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"88%\"style=\"width: 88% !important; min-width: 88%; max-width: 88%; padding-left: 50px;padding-right: 50px;\"> [getLstVariableNotificacion] </table>")
//			plantilla=plantilla.replace("[getLstVariableNotificacion]", tablaUsuario)
		}
		} catch (Exception e) {
			e.printStackTrace()
		}
//		plantilla=plantilla.replace("[getLstVariableNotificacion]", tablaUsuario)
		return plantilla
	}
	
	private String DataUsuarioRegistro(String plantilla, RestAPIContext context, String correo, PSGRCatNotificaciones cn, String errorlog) {
		//8 Seccion table atributos usuario
		errorlog += ", Variable8"
		String tablaUsuario= ""
		String plantillaTabla="<tr> <td align= \"left \" valign= \"top \" style= \"text-align: justify;vertical-align: bottom; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #585858; font-size: 17px; line-height: 25px; \"> [clave]: </span> </font> </td> <td align= \"left \" valign= \"top \" style= \"text-align: justify;vertical-align: bottom; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #ff5a00; font-size: 17px; line-height: 25px; \"> [valor] </span> </font> </td> </tr>"
		try {
		def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(CatRegistroDAO.class);
		List<PSGRCatRegistro> objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoelectronico(correo, 0, 99)
		if(objSolicitudDeAdmision.size()>0) {
			/*for(String variables:cn.getLstVariableNotificacion()) {
				if(variables.equals("Nombre")) {
					tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getPrimernombre()+" "+objSolicitudDeAdmision.get(0).getSegundonombre()+" "+objSolicitudDeAdmision.get(0).getApellidopaterno()+" "+objSolicitudDeAdmision.get(0).getApellidomaterno())
				}

				if(variables.equals("Correo")){
					tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getCorreoelectronico())
				}
				
				
			}*/
//			plantilla=plantilla.replace("[NOMBRE-COMPLETO]",objSolicitudDeAdmision.get(0).getPrimer_nombre()+" "+objSolicitudDeAdmision.get(0).getSegundonombre()+" "+objSolicitudDeAdmision.get(0).getApellido_paterno()+" "+objSolicitudDeAdmision.get(0).getApellido_materno())
//			plantilla=plantilla.replace("[NOMBRE]",objSolicitudDeAdmision.get(0).getPrimer_nombre()+" "+objSolicitudDeAdmision.get(0).getSegundo_nombre())
//			plantilla=plantilla.replace("[CORREO]",objSolicitudDeAdmision.get(0).getCorreo_electronico())
			errorlog += ", Variable14"
		}
		errorlog += ", Variable10 tablaUsuario"
		if(!tablaUsuario.equals("")) {
//			plantilla = plantilla.replace("<!--[Variables de usuario]-->", "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"88%\"style=\"width: 88% !important; min-width: 88%; max-width: 88%; padding-left: 50px;padding-right: 50px;\"> [getLstVariableNotificacion] </table>")
//			plantilla=plantilla.replace("[getLstVariableNotificacion]", tablaUsuario)
		}}catch(Exception e) {
		errorlog+="Fallo en datausuario" + e.getMessage()
		}
		return plantilla
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
}
