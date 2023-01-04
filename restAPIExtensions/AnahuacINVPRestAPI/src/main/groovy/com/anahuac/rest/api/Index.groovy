package com.anahuac.rest.api;

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.http.HttpHeaders
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DAO.CatalogosDAO
import com.anahuac.rest.api.DAO.LoginSesionesDAO
import com.anahuac.rest.api.DAO.RespuestasExamenDAO
import com.anahuac.rest.api.DAO.UsuariosDAO
import com.anahuac.rest.api.Entity.Result

import org.bonitasoft.web.extension.rest.RestAPIContext
import org.bonitasoft.web.extension.rest.RestApiController

import java.time.LocalDate

class Index implements RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Index.class)

    @Override
    RestApiResponse doHandle(HttpServletRequest request, RestApiResponseBuilder responseBuilder, RestAPIContext context) {
        Result result = new Result();
		def p = request.getParameter "p"
        if (p == null) {
            return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter p is missing"}""")
        }
        def c = request.getParameter "c"
        if (c == null) {
            return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter c is missing"}""")
        }
        def url = request.getParameter "url"
        if (url == null) {
            return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter url is missing"}""")
        }
		
		//VARIABLES===========================================================
		Integer parameterP = Integer.valueOf(p);
		Integer parameterC = Integer.valueOf(c);
		//VARIABLES DAO=======================================================
		UsuariosDAO uDAO = new UsuariosDAO();
		RespuestasExamenDAO rexa = new RespuestasExamenDAO();
		LoginSesionesDAO lses = new LoginSesionesDAO();
		//MAPEO DE SERVICIOS==================================================
		String jsonData = "{}"
		try {
			jsonData = request.reader.readLines().join("\n")
		}catch (Exception e) {
			jsonData = "{}" 
		}
		
		try {
			switch(url) {
				case "test":
				break;
				case "getCatPreguntas":
					result = new CatalogosDAO().getCatPreguntas(jsonData)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				break;
				case "RegistrarUsuario":
				try{
					result =  uDAO.postRegistrarUsuario(parameterP, parameterC, jsonData, context);
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
				break;
				case "getSesionLogin":
					result = new LoginSesionesDAO().getSesionLogin(jsonData)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				break;
				case "getRespuestasExamen":
				result = new RespuestasExamenDAO().getCatRespuestasExamen(jsonData)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "insertRespuesta":
			try{
				result =  rexa.insertRespuesta(jsonData, context);
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
			break;
			case "getCatRespuestasExamenbyUsuariocaso":
			try{
				result =  rexa.getCatRespuestasExamenbyUsuariocaso(jsonData);
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
			break;
			case "getCaseIdbyuserid":
			try{
				result =  rexa.getCaseIdbyuserid(jsonData);
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
			break;
			case "updateRespuesta":
			try{
				result =  rexa.updateRespuesta(jsonData, context);
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
			break;
			case "instantiation":
			String errorlog = " "
			try {
				errorlog = "[1] "
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				//assert object instanceof Map
				Map<String, Serializable> contract = new HashMap<String, Serializable>();

				contract.put("idUsuarioInput",Long.valueOf(object.idUsuarioInput))
				errorlog+="[5] "
				Long processId = context.getApiClient().getProcessAPI().getLatestProcessDefinitionId("Examen INVP");
				errorlog+="[6] " + contract.toMapString()
				ProcessInstance processInstance = context.getApiClient().getProcessAPI().startProcessWithInputs(processId, contract);
				errorlog+="[7] "
				Long caseId = processInstance.getRootProcessInstanceId();
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK,"{\"caseId\": "+caseId+"}")
				}catch(Exception ex) {
					result.setSuccess(false)
					result.setError(ex.getMessage())
					result.setError_info(errorlog)
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "updateSesion":
			try{
				result =  lses.updateUsuarioSesion(jsonData);
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
			case "getSesionActiva":
			try{
				result =  lses.getSesionActiva(jsonData);
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
			break;
			case "insertterminado":
			try{
				result =  lses.insertterminado(jsonData);
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
			break;
			case "updateterminado":
			try{
				result =  lses.updateterminado(jsonData);
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
			break;
				default:
				result = notFound(url);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
			}
			
		
	} catch (Exception e) {
		result.setSuccess(false)
		result.setError("500 INTERNAL SERVER ERROR")
		result.setError_info(e.getMessage())
		return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,new JsonBuilder(result).toString())
	}
	// Send the result as a JSON representation
	
	// You may use buildPagedResponse if your result is multiple
	return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
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
            withContentRange(p,c,total)
            withResponse(body)
            build()
        }
    }

    /**
     * Load a property file into a java.util.Properties
     */
    Properties loadProperties(String fileName, ResourceProvider resourceProvider) {
        Properties props = new Properties()
        resourceProvider.getResourceAsStream(fileName).withStream { InputStream s ->
            props.load s
        }
        props
    }

}
