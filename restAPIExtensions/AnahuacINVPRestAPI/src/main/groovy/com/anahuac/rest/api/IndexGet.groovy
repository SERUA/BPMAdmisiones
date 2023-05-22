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

import com.anahuac.rest.api.DAO.CatalogosDAO
import com.anahuac.rest.api.DAO.LoginSesionesDAO
import com.anahuac.rest.api.DAO.UsuariosDAO
import com.anahuac.rest.api.Entity.Result

import org.bonitasoft.web.extension.rest.RestAPIContext
import org.bonitasoft.web.extension.rest.RestApiController

import java.time.LocalDate

class IndexGet implements RestApiController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Index.class)
	
		@Override
		RestApiResponse doHandle(HttpServletRequest request, RestApiResponseBuilder responseBuilder, RestAPIContext context) {
		// To retrieve query parameters use the request.getParameter(..) method.
		// Be careful, parameter values are always returned as String values
		RestApiResponseBuilder rb;
		Result result = new Result();
		def url = request.getParameter "url";
		
		/*if(!security.allowedUrl(context,url)){
			return buildResponse(responseBuilder, HttpServletResponse.SC_FORBIDDEN,"""{"error" : "No tienes permisos"}""")
		}*/
		
		
		//MAPEO DE SERVICIOS==================================================
		try{
			switch(url) {
				//VARIABLES DAO===============================
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
				case "getPreguntasExamen":
				result = new CatalogosDAO().getCatPreguntasExamen();
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "getIdiomaUsuario":
			String username =request.getParameter "username"
			result = new UsuariosDAO().getIdiomaUsuario(username)
			responseBuilder.withMediaType("application/json")
			if (result.isSuccess()) {
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
			}else {
				return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
			}
			break;
			case "getTotalPreguntasContestadas":
			String username =request.getParameter "username"
			result = new LoginSesionesDAO().getTotalPreguntasContestadas(username)
			responseBuilder.withMediaType("application/json")
			if (result.isSuccess()) {
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
			}else {
				return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
			}
			break;
			case "getTerminadoExamen":
			String username =request.getParameter "username"
			result = new LoginSesionesDAO().getTerminadoExamen(username)
			responseBuilder.withMediaType("application/json")
			if (result.isSuccess()) {
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
			}else {
				return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
			}
			break;
			case "getidbanner":
				String username =request.getParameter "username"
				result = new LoginSesionesDAO().getidbanner(username)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				
			case "getMenuAdministrativo":
				result = new UsuariosDAO().getMenuAdministrativo(context)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
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
			break;
			case "updateTimer":
				String timer = request.getParameter "timer"
				String caseid = request.getParameter "caseid"
				String varName = "";
				result = new UsuariosDAO().updateTimer(Long.valueOf(caseid), Long.valueOf(timer));
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "bloquearAspiranteDef":
				String username = request.getParameter "username"
				result = new UsuariosDAO().bloquearAspiranteDef(username)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "bloquearAspiranteDef":
				String username = request.getParameter "username"
				result = new UsuariosDAO().bloquearAspiranteDef(username)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "desbloquearAspiranteDef":
				String username = request.getParameter "username"
				result = new UsuariosDAO().desbloquearAspiranteDef(username)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "checkBloqueado":
				String username = request.getParameter "username"
				result = new UsuariosDAO().checkBloqueado(username)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "getConfiguracionSesion":
				String idprueba = request.getParameter "idprueba"
				result = new CatalogosDAO().getConfiguracionSesion(Long.valueOf(idprueba));
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "getExamenTerminado":
				String username = request.getParameter "username"
				result = new LoginSesionesDAO().getExamenTerminado(username);
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "checkToleranciaFront":
				String username = request.getParameter "username"
				result = new UsuariosDAO().checkToleranciaFront(username);
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "checkTolerancia":
				String username = request.getParameter "username"
				result = new UsuariosDAO().checkTolerancia(username);
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "getSesionesByCampus":
				String campus = request.getParameter "campus"
				result = new CatalogosDAO().getSesionesByCampus(campus);
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "getNuevaConfigUsuario":
				String username = request.getParameter "username"
				result = new CatalogosDAO().getNuevaConfigUsuario(username);
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "getTerminoPrueba":
				String username =request.getParameter "username"
				result = new LoginSesionesDAO().getTerminoPrueba(username)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
			case "updateterminado":
				try{
					String username = request.getParameter "username"
					String terminado = request.getParameter "terminado"
					String idsesion = request.getParameter "idsesion"
					result =  new LoginSesionesDAO().updateterminadoGet(username, Boolean.valueOf(terminado), idsesion);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					}catch(Exception ou){
					result.setSuccess(false)
					result.setError(ou.getMessage())
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			case "getcelularusuariotemporal":
				String username =request.getParameter "username"
				result = new LoginSesionesDAO().getcelularusuariotemporal(username)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
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