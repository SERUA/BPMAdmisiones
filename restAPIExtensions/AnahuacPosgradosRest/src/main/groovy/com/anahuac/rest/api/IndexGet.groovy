package com.anahuac.rest.api

import groovy.json.JsonBuilder

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.http.HttpHeaders
import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.bonitasoft.web.extension.rest.RestApiController
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets

import com.anahuac.rest.api.DAO.BitacoraDAO
import com.anahuac.rest.api.DAO.CatalogosDAO
import com.anahuac.rest.api.DAO.NotificacionDAO
import com.anahuac.rest.api.DAO.SesionesDAO
import com.anahuac.rest.api.DAO.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.DAO.UsuariosDAO
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Security.SecurityFilter
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion

class IndexGet implements RestApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexGet.class)

	@Override
	RestApiResponse doHandle(HttpServletRequest request, RestApiResponseBuilder responseBuilder, RestAPIContext context) {
		RestApiResponseBuilder rb;
		Result result = new Result();
		def url = request.getParameter "url";
		if (url == null) {
			return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter url is missing"}""")
        }
		
		SecurityFilter security = new SecurityFilter();
		Result resultadoFiltro = security.allowedUrl(context, url);
		if(!resultadoFiltro.isSuccess()){
			return buildResponse(responseBuilder, HttpServletResponse.SC_FORBIDDEN,"""{"error" : "No tienes permisos"}""")
		}
		
		//MAPEO DE SERVICIOS==================================================
		try{
			switch(url) {
				case "getMenuAdministrativo":
					result = new UsuariosDAO().getMenuAdministrativo(context);
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				break;
				case "getBusinessAppMenu":
					result = new UsuariosDAO().getBusinessAppMenu();
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
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
				case "getProcessDef":
					String processName = request.getParameter "processName"
					result = new CatalogosDAO().getProcessDef(processName);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				case "habilitarUsuario":
					String usernameAspirante = request.getParameter "usernameAspirante"
					result = new UsuariosDAO().habilitarUsuario(usernameAspirante, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				case "getLstGestionEscolarByIdCampus":
					String id_campus = request.getParameter "id_campus"
					result = new CatalogosDAO().getLstGestionEscolarByIdCampus(id_campus, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getLstPeriodoByIdCampus":
					String id_campus = request.getParameter "id_campus"
					result = new CatalogosDAO().getLstPeriodoByIdCampus(id_campus, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getInfoCarrerasResponsable":
					String idsesion = request.getParameter "idsesion"
					String identrevistador = request.getParameter "identrevistador"
					result = new SesionesDAO().getInfoCarrerasResponsable(Long.valueOf(idsesion), Long.valueOf(identrevistador))
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
				case "getLstPosgradoByIdCampus":
					String id_campus = request.getParameter "id_campus"
					result = new CatalogosDAO().getLstPosgradoByIdCampus(id_campus, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getB64FileByUrlAzure":
					String urlAzure = request.getParameter "urlAzure"
					String fileType = request.getParameter "fileType"
					result = new SolicitudDeAdmisionDAO().getB64FileByUrlAzure(urlAzure, fileType);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
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
				case "getCartasNotificacionesByEstatus":
					String campus = request.getParameter "campus"
					String estatus = request.getParameter "estatus"
					result = new NotificacionDAO().getCartasNotificacionesByEstatus(campus, estatus);
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getSesionesV1":
					String idcampus = request.getParameter "idcampus"
					result = new SesionesDAO().getSesionesV1(idcampus);
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getSesionesV2":
					String idcampus = request.getParameter "idcampus"
					result = new SesionesDAO().getSesionesV2(idcampus);
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getHorariosCita":
					String idsesion = request.getParameter "idsesion"
					result = new SesionesDAO().getHorariosCita(idsesion, context);
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;	
				case "getHorariosByIdSesion":
					String idSesion = request.getParameter "idSesion"
					result = new SesionesDAO().getHorariosByIdSesion(Long.valueOf(idSesion));
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getHorariosByIdSesionV2":
					String idSesion = request.getParameter "idSesion"
					result = new SesionesDAO().getHorariosByIdSesionV2(Long.valueOf(idSesion));
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getSolicitudesDuplicadas":
					String nombre = request.getParameter "nombre"
					String apellido_paterno = request.getParameter "apellido_paterno"
					String apellido_materno = request.getParameter "apellido_materno"
					String curp = request.getParameter "curp"
					String id_banner = request.getParameter "id_banner"
					String pasaporte = request.getParameter "pasaporte"
					
					result = new SolicitudDeAdmisionDAO().getSolicitudesDuplicadas(nombre, apellido_materno, apellido_paterno, curp, pasaporte, id_banner);
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getRequisitosAdicionalesAuxiliar":
					String caseid = request.getParameter "caseid"
					
					result = new SolicitudDeAdmisionDAO().getRequisitosAdicionalesAuxiliar(caseid);
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getLstPeriodosDisponibles":
					String programa_pid = request.getParameter "programa_pid"
					
					result = new CatalogosDAO().getLstPeriodosDisponibles(programa_pid);
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getUserByCitaResponsable":
					String citaResponsable_pid = request.getParameter "citaResponsable_pid"
					
					result = new SolicitudDeAdmisionDAO().getUserByCitaResponsable(citaResponsable_pid, context);
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getBitacoraByCaseid":
					String caseid = request.getParameter "caseid"
					
					result = new BitacoraDAO().getBitacoraByCaseid(Long.valueOf(caseid));
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
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
					
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setError("fallo por "+e.getMessage());
		}
		// Send the result as a JSON representation
		// You may use buildPagedResponse if your result is multiple
		if(result.isSuccess()){
			return buildResponse(responseBuilder, HttpServletResponse.SC_OK, (result.getData().size()>0)?((url.equals("habilitarUsuario"))?result.getData().get(0):result.getData()):new JsonBuilder(result).toString())
		}else {
			return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
		}
	}

	public Result notFound(String url) {
		Result resultado = new Result();
		resultado.setSuccess(false);
		resultado.setError("No se reconoce el servicio: "+url);
		return resultado
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