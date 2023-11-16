package com.anahuac.rest.api.DAO

import com.anahuac.model.DetalleSolicitud
//import com.anahuac.model.ProcesoCaso
import com.anahuac.posgrados.bitacora.PSGRCatBitacoraCorreos
import com.anahuac.posgrados.catalog.PSGRCatDocumentosTextos
import com.anahuac.posgrados.catalog.PSGRCatGestionEscolarDAO
import com.anahuac.posgrados.catalog.PSGRCatImageNotificacion
import com.anahuac.posgrados.catalog.PSGRCatImageNotificacionDAO
import com.anahuac.posgrados.catalog.PSGRCatNotificaciones
import com.anahuac.posgrados.catalog.PSGRCatNotificacionesDAO
import com.anahuac.posgrados.catalog.PSGRCatRegistro
import com.anahuac.posgrados.catalog.PSGRCatRegistroDAO
import com.anahuac.posgrados.model.PSGRProcesoCaso
import com.anahuac.posgrados.model.PSGRProcesoCasoDAO
import com.anahuac.posgrados.model.PSGRRegistro
import com.anahuac.posgrados.model.PSGRRegistroDAO
import com.anahuac.posgrados.model.PSGRSolAdmiPrograma
import com.anahuac.posgrados.model.PSGRSolAdmiProgramaDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.db.CatBitacoraCorreo
import com.anahuac.rest.api.Entity.db.CatNotificacionesCampus
import com.anahuac.rest.api.Utilities.LoadParametros
import groovy.json.JsonSlurper


import com.anahuac.rest.api.DB.Statements
//import com.anahuac.model.ProcesoCasoDAO
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO

import java.awt.image.BufferedImage
import java.nio.charset.StandardCharsets
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

//import com.anahuac.catalogos.CatRegistroDAO
//import com.anahuac.catalogos.CatNotificacionesDAO
//import com.anahuac.catalogos.CatRegistro
import com.anahuac.catalogos.CatDocumentosTextos
import com.anahuac.catalogos.CatImageNotificacion
import com.anahuac.catalogos.CatImageNotificacionDAO
//import com.anahuac.catalogos.CatNotificaciones


class NotificacionDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificacionDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	float costo1=0,costo2=0,costo3=0,costo4=0
	String periodo =""
	
	public Result generateHtml(String jsonData, RestAPIContext context) {
		
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
			errorlog += "| host =     "+ objProperties.getUrlHost();
			/*-------------------------------------------------------------*/

			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			Boolean closeConPlantilla=false;
			
			userLogged = context.getApiSession().getUserId();
			errorlog += "| Se obtuvo el usuario " + userLogged;
			PSGRCatNotificaciones catNotificaciones= null;
			PSGRProcesoCaso procesoCaso = new PSGRProcesoCaso()
			PSGRCatNotificaciones cn = new PSGRCatNotificaciones()
			def catNotificacionesDAO
			try {
				def procesoCasoDAO = context.getApiClient().getDAO(PSGRProcesoCasoDAO.class);
				procesoCaso = procesoCasoDAO.getCase_id(object.campus, "CatNotificaciones");
				errorlog += "| Despues con el campus " + object.campus + " se obtuvo el caseid " + procesoCaso.getCase_id()
				catNotificacionesDAO = context.getApiClient().getDAO(PSGRCatNotificacionesDAO.class);
				catNotificaciones = catNotificacionesDAO.get_cat_notificaciones(object.codigo, procesoCaso.getCase_id())
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
						catNotificaciones.setTipo_correo(rs.getString("tipo-correo"))
						catNotificaciones.setTitulo(rs.getString("titulo"))
						catNotificaciones.setLst_correo_copia(new ArrayList<String>())
						catNotificaciones.setLst_variable_notificacion(new ArrayList<String>())
						procesoCaso.setCase_id(rs.getString("case_id"))
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
			errorlog += "| | procesoCaso.getCaseId() = "+procesoCaso.getCase_id()
			
			//cn = catNotificacionesDAO.getCatNotificaciones(procesoCaso.getCaseId(),object.codigo)
			errorlog += "|  lstDoc"
			errorlog+="| seteando mensaje"
			plantilla=plantilla.replace("[banner-href]", cn.getEnlace_banner())
			
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
				List<CatImageNotificacion> lci = catImageNotificacion.findByCaseId(Long.valueOf(procesoCaso.getCase_id()), 0, 999)
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
			if(!cn.getContenido_leonel().equals("") ) {
				plantilla=plantilla.replace("<!--Leonel-->", "<table width=\"80%\"> <thead></thead> <tbody> <tr> <td width=\"25%\" style=\"text-align: right;\"> <img style=\"width: 145px;\" src=\"https://bpmpreprod.blob.core.windows.net/publico/Leoneldmnisiones_Mesa%20de%20trabajo%201.png\"> </td> <td class=\"col-6\"> <div class=\"arrow_box\" style=\"position: relative; background: #ff5900; border: 4px solid #ff5900;border-radius: 50px;\"> <h6 class=\"logo\" style=\"font-size: 12px; padding: 10px; color: white; font-weight: 500;font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif;\"> [leonel]</h6> </div> </td> </tr> </tbody> </table>"+"<hr>")
				plantilla=plantilla.replace("[leonel]", cn.getContenido_leonel())
			}
			
			errorlog += "| Variable15 PLANTILLA: " + object.codigo + " | ";
			
			if(object.codigo.equals("psgr-validar-cuenta") && object.isEnviar) {
				plantilla = plantilla.replace("[href-confirmar]", objProperties.getUrlHost() + "/apps/login/pg_activar_usuario/?correo=" + object.correo + "");	
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
				plantilla=plantilla.replace("[HEADER-IMG]", cn.getAngulo_imagen_header())
				plantilla=plantilla.replace("[TEXTO-FOOTER]", cn.getTexto_footer())
				plantilla=plantilla.replace("[firma]", "")
			}
			   
			plantilla=plantilla.replace("[header-href]", cn.getEnlace_banner())
		    plantilla=plantilla.replace("[footer-href]", cn.getEnlace_footer())
			List<String> lstData = new ArrayList();
			List<String> lstAdditionalData = new ArrayList();
			lstData.add(plantilla);
			resultado.setData(lstData);
			
			MailGunDAO mgd = new MailGunDAO();
			lstAdditionalData.add("correo="+correo)
			lstAdditionalData.add("asunto="+asunto)
			lstAdditionalData.add("cc="+cc);
			if((object.isEnviar && object.codigo!="carta-informacion") ||(object.isEnviar && object.codigo=="carta-informacion" && cartaenviar) ) {
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
				
				/*insertCatBitacoraCorreos(catBitacoraCorreo)*/
			}
			
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage())
			
			e.printStackTrace()
		}
		resultado.setError_info(errorlog);
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
					row.setTipoCorreo(rs.getString("tipo_correo"))
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
		def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(PSGRRegistroDAO.class);
		def objPSGRSolAdmiProgramaDAO = context.apiClient.getDAO(PSGRSolAdmiProgramaDAO.class);
		List<PSGRRegistro> objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreo_electronico(correo, 0, 999)
		def caseid = objSolicitudDeAdmision.get(0).caseid;
		List<PSGRSolAdmiPrograma> objPSGRSolAdmiPrograma = objSolicitudDeAdmisionDAO.findByCaseid(caseid, 0, 999)
		if(objSolicitudDeAdmision.size()>0) {
			Result documentosTextos = new DocumentosTextosDAO().getDocumentosTextos(objSolicitudDeAdmision.get(0).campus.getPersistenceId());
			
			plantilla=plantilla.replace("[NOMBRE-COMPLETO]",objSolicitudDeAdmision.get(0).nombre+" "+objSolicitudDeAdmision.get(0).apellido_paterno+" "+objSolicitudDeAdmision.get(0).apellido_materno)
			plantilla=plantilla.replace("[NOMBRE]",objSolicitudDeAdmision.get(0).nombre);
			
			try {
				plantilla=plantilla.replace("[CAMPUS]", objSolicitudDeAdmision.get(0).campus.descripcion)
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
				} else {
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
	
	private String DataUsuarioRegistro(String plantilla, RestAPIContext context, String correo, PSGRCatNotificaciones cn, String errorlog) {
		//8 Seccion table atributos usuario
		errorlog += ", Variable8"
		String tablaUsuario= ""
		String plantillaTabla="<tr> <td align= \"left \" valign= \"top \" style= \"text-align: justify;vertical-align: bottom; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #585858; font-size: 17px; line-height: 25px; \"> [clave]: </span> </font> </td> <td align= \"left \" valign= \"top \" style= \"text-align: justify;vertical-align: bottom; \"> <font face= \"'Source Sans Pro', sans-serif \" color= \"#585858 \"style= \"font-size: 17px; line-height: 25px; \"> <span style= \"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #ff5a00; font-size: 17px; line-height: 25px; \"> [valor] </span> </font> </td> </tr>"
		try {
		def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(PSGRCatRegistroDAO.class);
		List<PSGRCatRegistro> objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreo_electronico(correo, 0, 99)
		if(objSolicitudDeAdmision.size()>0) {
			/*for(String variables:cn.getLstVariableNotificacion()) {
				if(variables.equals("Nombre")) {
					tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getPrimernombre()+" "+objSolicitudDeAdmision.get(0).getSegundonombre()+" "+objSolicitudDeAdmision.get(0).getApellidopaterno()+" "+objSolicitudDeAdmision.get(0).getApellidomaterno())
				}

				if(variables.equals("Correo")){
					tablaUsuario += plantillaTabla.replace("[clave]", variables).replace("[valor]", objSolicitudDeAdmision.get(0).getCorreoelectronico())
				}
				
				
			}*/
			plantilla=plantilla.replace("[NOMBRE-COMPLETO]",objSolicitudDeAdmision.get(0).getPrimer_nombre()+" "+objSolicitudDeAdmision.get(0).getSegundo_nombre()+" "+objSolicitudDeAdmision.get(0).getApellidopaterno()+" "+objSolicitudDeAdmision.get(0).getApellidomaterno())
			plantilla=plantilla.replace("[NOMBRE]",objSolicitudDeAdmision.get(0).getPrimer_nombre()+" "+objSolicitudDeAdmision.get(0).getSegundo_nombre())
//			plantilla=plantilla.replace("[CORREO]",objSolicitudDeAdmision.get(0).getCorreo_electronico())
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
	
	public Result getCartasNotificaciones(String campus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
			PSGRCatNotificaciones row = new PSGRCatNotificaciones()
			List<CatNotificacionesCampus> rows = new ArrayList<CatNotificacionesCampus>();
			closeCon = validarConexion();
			String consulta = Statements.GET_CARTAS_NOTIFICACIONES_ALT;
			pstm = con.prepareStatement(consulta)
			pstm.setString(1, campus)
			rs = pstm.executeQuery()
				while(rs.next()) {
					row = new PSGRCatNotificaciones()
					row.angulo_imagen_footer = rs.getString("angulo_imagen_footer")
					row.angulo_imagen_header = rs.getString("angulo_imagen_header")
					row.asunto = rs.getString("asunto")
					row.case_id = rs.getString("case_id")
					row.codigo = rs.getString("codigo")
					row.comentario_leon = rs.getString("comentario_leon")
					row.contenido  = rs.getString("contenido")
					row.contenido_correo = rs.getString("contenido_correo")
					row.contenido_leonel = rs.getString("contenido_leonel")
					row.descripcion = rs.getString("descripcion")
					row.doc_guia_estudio = rs.getString("doc_guia_estudio")
					row.enlace_banner = rs.getString("enlace_banner")
					row.enlace_contacto = rs.getString("enlace_contacto")
					row.enlace_facebook = rs.getString("enlace_facebook")
					row.enlace_footer = rs.getString("enlace_footer")
					row.enlace_instagram = rs.getString("enlace_instagram")
					row.enlace_twitter = rs.getString("enlace_twitter")
					row.nombre_imagen_footer = rs.getString("nombre_imagen_footer")
					row.texto_footer  = rs.getString("texto_footer")
					row.tipo_correo = rs.getString("tipo_correo")
					row.titulo = rs.getString("titulo")
					row.url_img_footer = rs.getString("url_img_footer")
					row.url_img_header = rs.getString("url_img_header")
					row.persistenceId = rs.getLong("persistenceid")
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
	
	public Result getCartasNotificacionesByEstatus(String campus, String filtroEstatus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			PSGRCatNotificaciones row = new PSGRCatNotificaciones();
			List<CatNotificacionesCampus> rows = new ArrayList<CatNotificacionesCampus>();
			String url = java.net.URLDecoder.decode(filtroEstatus, StandardCharsets.UTF_8.name());
			String consulta = Statements.GET_CARTAS_NOTIFICACIONES_ESTATUS.replace("[ESTATUS]", url);
			errorLog += " campus :: " + campus;
			errorLog += " consulta :: " + consulta;
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			pstm.setString(1, campus);
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				row = new PSGRCatNotificaciones();
				row.angulo_imagen_footer = rs.getString("angulo_imagen_footer");
				row.angulo_imagen_header = rs.getString("angulo_imagen_header");
				row.asunto = rs.getString("asunto");
				row.case_id = rs.getString("case_id");
				row.codigo = rs.getString("codigo");
				row.comentario_leon = rs.getString("comentario_leon");
				row.contenido  = rs.getString("contenido");
				row.contenido_correo = rs.getString("contenido_correo");
				row.contenido_leonel = rs.getString("contenido_leonel");
				row.descripcion = rs.getString("descripcion");
				row.doc_guia_estudio = rs.getString("doc_guia_estudio");
				row.enlace_banner = rs.getString("enlace_banner");
				row.enlace_contacto = rs.getString("enlace_contacto");
				row.enlace_facebook = rs.getString("enlace_facebook");
				row.enlace_footer = rs.getString("enlace-footer");
				row.enlace_instagram = rs.getString("enlace_instagram");
				row.enlace_twitter = rs.getString("enlace_twitter");
				row.nombre_imagen_footer = rs.getString("nombre_imagen_footer");
				row.texto_footer  = rs.getString("texto_footer");
				row.tipo_correo = rs.getString("tipo_correo");
				row.titulo = rs.getString("titulo");
				row.url_img_footer = rs.getString("url_img_footer");
				row.url_img_header = rs.getString("url_img_header");
				rows.add(row);
			}
			
			resultado.setError_info(errorLog);
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			resultado.setError_info(errorLog);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}
	
	public Result updateCatNotificacionesAlt(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			List<?> rows = new ArrayList<?>();
			closeCon = validarConexion();
			
			if(object.persistenceId != null) {
				pstm = con.prepareStatement(Statements.UPDATE_CAT_NOTIFICACIONES_SDAE);
				pstm.setString(1, object.angulo_imagen_header);
				pstm.setString(2, object.angulo_imagen_footer);
				pstm.setString(3, object.asunto);
				pstm.setString(4, object.comentario_leon);
				pstm.setString(5, object.contenido);
				pstm.setString(6, object.contenido_correo);
				pstm.setString(7, object.contenido_leonel);
				pstm.setString(8, object.descripcion);
				pstm.setString(9, object.doc_guia_estudio);
				pstm.setString(10, object.enlace_banner);
				pstm.setString(11, object.enlace_contacto);
				pstm.setString(12, object.enlace_facebook);
				pstm.setString(13, object.enlace_footer);
				pstm.setString(14, object.enlace_instagram);
				pstm.setString(15, object.enlace_twitter);
				pstm.setString(16, object.nombre_imagen_footer);
				pstm.setString(17, object.angulo_imagen_footer);
				pstm.setString(18, object.tipo_correo);
				pstm.setString(19, object.titulo);
				pstm.setString(20, object.urlImgFooter);
				pstm.setString(21, object.urlImgHeader);
				pstm.setLong(22, Long.valueOf(object.persistenceId));
				
			} else {
				pstm = con.prepareStatement(Statements.INSERT_CAT_NOTIFICACIONES_SDAE);
				pstm.setString(1, object.angulo_imagen_footer);
				pstm.setString(2, object.angulo_imagen_header);
				pstm.setString(3, object.asunto);
				pstm.setString(4, object.comentarioLeon);
				pstm.setString(5, object.contenido);
				pstm.setString(6, object.contenido_correo);
				pstm.setString(7, object.contenidoLeonel);
				pstm.setString(8, object.descripcion);
				pstm.setString(9, object.docGuiaEstudio);
				pstm.setString(10, object.enlace_banner);
				pstm.setString(11, object.enlaceContacto);
				pstm.setString(12, object.enlaceFacebook);
				pstm.setString(13, object.enlace_footer);
				pstm.setString(14, object.enlaceInstagram);
				pstm.setString(15, object.enlaceTwitter);
				pstm.setString(16, object.nombre_imagen_footer);
				pstm.setString(17, object.angulo_imagen_footer);
				pstm.setString(18, object.tipoCorreo);
				pstm.setString(19, object.titulo);
				pstm.setString(20, object.urlImgFooter);
				pstm.setString(21, object.urlImgHeader);
				pstm.setString(22, object.codigo);
				pstm.setString(23, object.caseId);
			}
			
			pstm.execute();
			
			resultado.setSuccess(true);
			rows.add(object);
			resultado.setError_info(errorLog);
			resultado.setData(rows);
		} catch (Exception e) {
			errorLog += " | " + e.getMessage();
			resultado.setError_info(errorLog);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
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
				
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
}
