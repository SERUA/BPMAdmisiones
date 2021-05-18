package com.anahuac.rest.api

import groovy.json.JsonBuilder

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.http.HttpHeaders
import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatBachilleratos
import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatEstados
import com.anahuac.catalogos.CatPais
import com.anahuac.rest.api.DAO.BannerDAO
import com.anahuac.rest.api.DAO.BecasDAO
import com.anahuac.rest.api.DAO.CatalogoBachilleratoDAO
import com.anahuac.rest.api.DAO.CatalogosDAO
import com.anahuac.rest.api.DAO.CustomUserRequestDAO
import com.anahuac.rest.api.DAO.HubspotDAO
import com.anahuac.rest.api.DAO.NotificacionDAO
import com.anahuac.rest.api.DAO.SesionesDAO
import com.anahuac.rest.api.DAO.SolicitudUsuarioDAO
import com.anahuac.rest.api.DAO.UsuariosDAO
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.PruebaCustom
import com.anahuac.rest.api.Entity.Custom.SesionCustom
import com.anahuac.rest.api.Entity.db.Responsable
import com.anahuac.rest.api.Entity.db.CatTipoPrueba
import com.anahuac.rest.api.Entity.db.Sesion
import com.bonitasoft.web.extension.rest.RestAPIContext
import com.bonitasoft.web.extension.rest.RestApiController

class IndexGet implements RestApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexGet.class)

	@Override
	RestApiResponse doHandle(HttpServletRequest request, RestApiResponseBuilder responseBuilder, RestAPIContext context) {
		// To retrieve query parameters use the request.getParameter(..) method.
		// Be careful, parameter values are always returned as String values
		RestApiResponseBuilder rb;
		Result result = new Result();
		def url = request.getParameter "url";
		
		//MAPEO DE SERVICIOS==================================================
		try{
			switch(url) {
				case "habilitarUsuario":
				// Retrieve p parameter
				def correo = request.getParameter "correo"
				if (correo == null) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST, """{"error " : "the parameter correo is missing "}""")
				}
				def p = request.getParameter "p";
				if (p == null) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter p is missing"}""")
				}
				def c = request.getParameter "c";
				if (c == null) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter c is missing"}""")
				}
				
				if (url == null) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter url is missing"}""")
				}
						
				Integer parameterP = Integer.valueOf(p);
				Integer parameterC = Integer.valueOf(c);
				//String jsonData = request.reader.readLines().join("\n")
		
				//def result = ["correo": correo, "myParameterKey": ""]
				
				//VARIABLES DAO===============================
				UsuariosDAO uDAO = new UsuariosDAO();
				responseBuilder.withMediaType("text/html; charset=utf-8")
				result = uDAO.getHabilitarUsaurio(parameterP, parameterC, correo, context);
				if(result.isSuccess()){
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, (result.getData().size()>0)?((url.equals("habilitarUsuario"))?result.getData().get(0):result.getData()):new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				case "getDescuentosCiudadBachillerato":
				def p = request.getParameter "p";
				if (p == null) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter p is missing"}""")
				}
				def c = request.getParameter "c";
				if (c == null) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter c is missing"}""")
				}
				String ciudad=request.getParameter "ciudad"
				String campus=request.getParameter "campus"
				String bachillerato=request.getParameter "bachillerato"
				result = new CatalogoBachilleratoDAO().getDescuentosCiudadBachillerato(Integer.valueOf(p), Integer.valueOf(c), campus, bachillerato, ciudad, context)
				responseBuilder.withMediaType("application/json")
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				break;
				case "getDescuentosCampana":
				def p = request.getParameter "p";
				if (p == null) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter p is missing"}""")
				}
				def c = request.getParameter "c";
				if (c == null) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter c is missing"}""")
				}
				String campus=request.getParameter "campus"
				result = new CatalogoBachilleratoDAO().getDescuentosCampana(Integer.valueOf(p), Integer.valueOf(c), campus, context)
				responseBuilder.withMediaType("application/json")
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				break;
				
				case "getCatNotificacionesFirma":
				String jsonData =request.getParameter "jsonData"
				result = new NotificacionDAO().getFirma(jsonData)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				case "getCartasNotificaciones":
				String campus =request.getParameter "campus"
				result = new NotificacionDAO().getCartasNotificaciones(campus)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				case "getCatTitulo":
				String jsonData =request.getParameter "jsonData"
				result = new CatalogosDAO().getCatTitulo(jsonData)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				case "getDatosUsername":
					String username =request.getParameter "username"
					result = new UsuariosDAO().getDatosUsername(username)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				break;
				case "getBusinessAppMenu":
				result = new UsuariosDAO().getBusinessAppMenu()
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				case "getMenuAdministrativo":
				result = new UsuariosDAO().getMenuAdministrativo(context)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				case "getCatTipoPrueba":
				String jsonData =request.getParameter "jsonData"
				result = new SesionesDAO().getCatTipoPrueba(jsonData)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				
				case "getCatPsicologo":
				String jsonData =request.getParameter "jsonData"
				result = new SesionesDAO().getCatTipoPrueba(jsonData)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				
				case "generarFirma":
				String persistenceId =request.getParameter "persistenceId"
				String resultado = new NotificacionDAO().generarFirma(persistenceId)
				responseBuilder.withMediaType("text/html; charset=utf-8")
				
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, resultado)
				
				break;
				
				case "getCatBitacoraComentario":
				String jsonData =request.getParameter "jsonData"
				result = new CatalogoBachilleratoDAO().getCatBitacoraComentario(jsonData, context)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				
				case "getCatPropedeutico":
				String jsonData =request.getParameter "jsonData"
				result = new CatalogosDAO().getCatPropedeutico(jsonData)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				case "getCatPropedeuticoByPeriodo":
					String id = request.getParameter "id";
					String grupobonita = request.getParameter "grupobonita";
					String tipoperiodo = request.getParameter "tipoperiodo";
					String idioma = request.getParameter "idioma";
					
					result = new CatalogosDAO().getCatPropedeuticoByPeriodo(id, grupobonita, tipoperiodo,idioma, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				case "getCatPeriodoActivo":
					String tipo = request.getParameter "tipo";
										
					result = new CatalogosDAO().getCatPeriodoActivo(tipo, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				case "getEstadoCivil":
				String jsonData =request.getParameter "jsonData"
				result = new CatalogosDAO().getEstadoCivil(jsonData)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				
				case "getUserBonita":
				String jsonData =request.getParameter "jsonData"
				result = new SesionesDAO().getUserBonita(jsonData,context)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				
				break;
				
				case "getPsicologoSesiones":
				String jsonData =request.getParameter "jsonData"
				Long id = Long.parseLong(jsonData)
				result = new SesionesDAO().getPsicologoSesiones(id)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				
				break;
				
				case "getCatGestionEscolar":
				String jsonData =request.getParameter "jsonData"
				result = new CatalogosDAO().getCatGestionEscolar(jsonData, context)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				
				break;
				
				case "getSesion":
				String persistenceId =request.getParameter "sessionid"
				result = new SesionesDAO().getSesion(Long.parseLong(persistenceId),context)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				
				break;
				
				case "getPruebasFechas":
				String persistenceId =request.getParameter "sessionid"
				String aspirante =request.getParameter "aspirante"
				result = new SesionesDAO().getPruebasFechas(Long.parseLong(persistenceId),aspirante)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				
				break;
				
				case "getHorarios":
				String persistenceId =request.getParameter "sessionid"
				String prueba_pid =request.getParameter "prueba_pid"
				String correoAspirante =request.getParameter "correoAspirante"
				result = new SesionesDAO().getHorarios(Long.parseLong(persistenceId), Long.parseLong(prueba_pid), correoAspirante, context)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				
				break;
				
				case "getAzureConfig":
				result = new CatalogosDAO().getAzureConfig();
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData().get(0)).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				
				break;
				
				case "getSesionAspirante":
				String persistenceId =request.getParameter "sessionid"
				result = new SesionesDAO().getSesionAspirante(Long.parseLong(persistenceId),context)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				
				case "getCatNotificacionesCampus":
				String grupoBonita =request.getParameter "grupoBonita"
				result = new NotificacionDAO().getCatNotificacionesCampus(grupoBonita)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				
				break;
				
				case "getDatosSesionUsername":
				String username =request.getParameter "username"
				result = new SesionesDAO().getDatosSesionUsername(username)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				
				break;
				
				case "getPaletteColor":
				result = new SesionesDAO().getPaletteColor()
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				
				break;
				
				case "getIdbanner":
				String idbanner=request.getParameter "idbanner"
				result = new SolicitudUsuarioDAO().getIdbanner(0, 9999, idbanner, context)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				
				case "getUserIdBanner":
				String idbanner=request.getParameter "idbanner"
				result = new SolicitudUsuarioDAO().getUserIdBanner(idbanner)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				
				break;
				case "getCatBachilleratos":
				result = new CatalogoBachilleratoDAO().get(0, 9999, "", context)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				
				case "getCatBachillerato":
				String jsonData =request.getParameter "jsonData"
				result = new CatalogosDAO().getCatBachillerato(jsonData, context)
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				case "getCatBitacoraCorreo":
				String jsonData =request.getParameter "jsonData"
				result = new NotificacionDAO().getCatBitacoraCorreo(jsonData)
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				
				
				case "getUsuarios":
				String jsonData =request.getParameter "jsonData"
				result = new UsuariosDAO().getUsuarios(jsonData,context)
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				case "getValidarClave":
					String tabla = request.getParameter "tabla";
					String clave = request.getParameter "clave";
					String id = request.getParameter "id";
					result = new CatalogosDAO().getValidarClave(0, 9999, tabla, clave, id);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				case "getValidarOrden":
					String tabla = request.getParameter "tabla";
					String ordenString = request.getParameter "orden";
					LOGGER.error "ORDEN STRING : : " + ordenString ;
					Integer orden = Integer.parseInt(ordenString);
					LOGGER.error "ORDEN : : " + orden.toString();
					String id = request.getParameter "id";
					result = new CatalogosDAO().getValidarOrden(0,9999, tabla, orden, id);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				case "getValidarIdBanner":
					String tabla = request.getParameter "tabla";
					String idBanner = request.getParameter "idBanner";
					String id = request.getParameter "id";
					result = new CatalogosDAO().getValidarIdBanner(0,9999, tabla, idBanner, id);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				case "getPeriodosActivos":
				String tipo = request.getParameter "tipo";
				String id = request.getParameter "id";
				result = new CatalogosDAO().getPeriodosActivos(0,9999, tipo, id);
				responseBuilder.withMediaType("application/json");
				if (result.isSuccess()) {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
				}else {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
				}
				break;
				case "buscarCambiosBannerPreparatoria":
					String operacion = request.getParameter "operacion";
					result = new BannerDAO().buscarCambiosBannerPreparatoria(context, operacion);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				case "replicarProperties":
					String nombreUsuario = request.getParameter "nombreUsuario";
					String correoElectronico = request.getParameter "correoElectronico";
					String apikeyHubspot = request.getParameter "apikeyHubspot";
					
					result = new HubspotDAO().replicarProperties(nombreUsuario, correoElectronico, apikeyHubspot);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				case "getPeriodosSiguientes":
				String tipo = request.getParameter "tipo";
				String fecha = request.getParameter "fecha";
				String id = request.getParameter "id";
				result = new CatalogosDAO().getPeriodosSiguientes(0,9999, tipo, fecha, id);
				responseBuilder.withMediaType("application/json");
				if (result.isSuccess()) {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
				}else {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
				}
				break;
				case "getValidarClavePeriodo":
				String clave = request.getParameter "clave";
				String tipo = request.getParameter "tipo";
				String id = request.getParameter "id";
				result = new CatalogosDAO().getValidarClavePeriodo(0,9999, clave,tipo, id);
				responseBuilder.withMediaType("application/json");
				if (result.isSuccess()) {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
				}else {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
				}
				break;
				
				case "getInfoPrueba":
				String id = request.getParameter "id";
				result = new SesionesDAO().getInfoPrueba(0, 9999, id);
				responseBuilder.withMediaType("application/json");
				if (result.isSuccess()) {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
				}else {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
				}
				break;
				
				case "getFechaServidor":
				result = new SesionesDAO().getFechaServidor(0, 9999);
				responseBuilder.withMediaType("application/json");
				if (result.isSuccess()) {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
				}else {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
				}
				break;
				
				case "getExcelPlantillaHermanos":
				String fecha = null;
				result = new BecasDAO().excelPlantillaHermanos(fecha);
				responseBuilder.withMediaType("application/json");
				if (result.isSuccess()) {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
				}else {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
				}
				break;
				
				case "getExcelPlantillaRegistro":
				String fecha = null;
				result = new BecasDAO().excelPlantillaRegistro(fecha);
				responseBuilder.withMediaType("application/json");
				if (result.isSuccess()) {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
				}else {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
				}
				break;
				
				case "getPlantillaRegistro":
				result = new BecasDAO().getPlantillaRegistro();
				responseBuilder.withMediaType("application/json");
				if (result.isSuccess()) {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
				}else {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
				}
				break;
				
				case "getEstados":
				String pais = request.getParameter "pais";
				result = new CatalogosDAO().getCatEstados(pais)
				responseBuilder.withMediaType("application/json");
				if (result.isSuccess()) {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
				}else {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
				}
				break;
				case "getTempKeyAzure":
					String pais = request.getParameter "pais";
					result = new SolicitudUsuarioDAO().getTempKeyAzure();
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				
				case "getDuplicado":
					//String idbanner = request.getParameter "idbanner";
					String correo = request.getParameter "correoElectronico";
					String curp = request.getParameter "curp";
					String nombre = request.getParameter "nombre";
					String fecha = request.getParameter "fechaNacimiento";
					String caseid = request.getParameter "caseid";
					String primerNombre = request.getParameter "primerNombre";
					String segundoNombre = request.getParameter "segundoNombre";
					String apellidoPaterno = request.getParameter "apellidoPaterno";
					String apellidoMaterno = request.getParameter "apellidoMaterno";
					
					if(curp.equals(null)) {
						curp = "";
					}
					
					/*if(idbanner.equals(null)) {
						idbanner = "";
					}*/
					
					nombre = nombre.replace("%20", " ");
					
					result = new SolicitudUsuarioDAO().getDuplicados(curp, nombre, primerNombre, segundoNombre, apellidoPaterno, apellidoMaterno, correo, fecha, caseid);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				
				case "getConstanciasHistorico":
					String caseId = request.getParameter "caseId";
					
					result = new UsuariosDAO().getConstanciasHistorico(Long.valueOf(caseId));
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				case "getSesions":
				List<SesionCustom> sesions = new ArrayList()
				SesionCustom sesion = new SesionCustom();
				sesion.setPreparatoria(new CatBachilleratos())
				sesion.setEstado(new CatEstados())
				sesion.setPais(new CatPais())
				PruebaCustom prueba = new PruebaCustom()
				prueba.setEstado(new CatEstados())
				prueba.setCampus(new CatCampus())
				prueba.setPais(new CatPais())
				prueba.setTipo(new CatTipoPrueba())
				List<Responsable> psicologos = new ArrayList();
				psicologos.add(new Responsable());
				
				prueba.setPsicologos(psicologos)
				List<PruebaCustom> pruebas = new ArrayList();
				pruebas.add(prueba)
				sesion.setPruebas(pruebas)
				sesions.add(sesion)
				result.setData(sesions)
				result.setSuccess(true);
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				case "getPropedeuticosNoFecha":									
				result = new CatalogosDAO().getPropedeuticosNoFecha(context);
				responseBuilder.withMediaType("application/json");
				if (result.isSuccess()) {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
				}else {
					 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
				}
				break;
				case "getActiveProcess":
					result = new CustomUserRequestDAO().getActiveProcess(context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				case "getCurrentTaskId":
					String caseId = request.getParameter "caseId";
					
					result = new CustomUserRequestDAO().getCurrentTaskId(caseId, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				case "getCaseVariables":
					String caseId = request.getParameter "caseId";
					
					result = new CustomUserRequestDAO().getCaseVariables(caseId, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
			}
		}catch (Exception e) {
			e.printStackTrace()
			result.setSuccess(false)
			result.setError("fallo por "+e.getMessage())
		}
		// Send the result as a JSON representation
		// You may use buildPagedResponse if your result is multiple
		if(result.isSuccess()){
			return buildResponse(responseBuilder, HttpServletResponse.SC_OK, (result.getData().size()>0)?((url.equals("habilitarUsuario"))?result.getData().get(0):result.getData()):new JsonBuilder(result).toString())
		}else {
			return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
		}
	}

	/**
	 * Build an HTTP response.
	 *
	 * @param  responseBuilder the Rest API response builder
	 * @param  httpStatus the status of the response
	 * @param  body the response body
	 * @return a RestAPIResponse
	 */
	RestApiResponse buildResponse(RestApiResponseBuilder responseBuilder, int httpStatus, Serializable body) {
		return responseBuilder.with {
			withResponseStatus(httpStatus)
			withResponse(body)
			build()
		}
	}

	/**
	 * Returns a paged result like Bonita BPM REST APIs.
	 * Build a response with a content-range.
	 *
	 * @param  responseBuilder the Rest API response builder
	 * @param  body the response body
	 * @param  p the page index
	 * @param  c the number of result per page
	 * @param  total the total number of results
	 * @return a RestAPIResponse
	 */
	RestApiResponse buildPagedResponse(RestApiResponseBuilder responseBuilder, Serializable body, int p, int c, long total) {
		return responseBuilder.with {
			withContentRange(p, c, total)
			withResponse(body)
			build()
		}
	}

	/**
	 * Load a property file into a java.util.Properties
	 */
	Properties loadProperties(String fileName, ResourceProvider resourceProvider) {
		Properties props = new Properties()
		resourceProvider.getResourceAsStream(fileName).withStream {
			InputStream s->
				props.load s
		}
		props
	}

}