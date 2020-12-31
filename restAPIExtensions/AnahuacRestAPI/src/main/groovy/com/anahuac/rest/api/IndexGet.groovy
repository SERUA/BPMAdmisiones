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
import com.anahuac.rest.api.DAO.CatalogoBachilleratoDAO
import com.anahuac.rest.api.DAO.CatalogosDAO
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
				
				case "getIdbanner":
				String idbanner=request.getParameter "idbanner"
				result = new SolicitudUsuarioDAO().getIdbanner(0, 9999, idbanner, context)
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