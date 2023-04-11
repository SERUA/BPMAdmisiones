package com.anahuac.rest.api;

import groovy.json.JsonBuilder

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.http.HttpHeaders
import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DAO.BannerRequestDAO
import com.anahuac.rest.api.DAO.BitacoraDAO
import com.anahuac.rest.api.DAO.BitacoraSDAEDAO
import com.anahuac.rest.api.DAO.BonitaGetsDAO
import com.anahuac.rest.api.DAO.CatalogosDAO
import com.anahuac.rest.api.DAO.ListadoDAO
import com.anahuac.rest.api.DAO.NotificacionDAO
import com.anahuac.rest.api.DAO.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Security.SecurityFilter

import org.bonitasoft.web.extension.rest.RestAPIContext
import org.bonitasoft.web.extension.rest.RestApiController

import java.time.LocalDate

class IndexGET implements RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexGET.class)

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
		
//		SecurityFilter security = new SecurityFilter();
//		if(!security.allowedUrl(context,url)){
//			return buildResponse(responseBuilder, HttpServletResponse.SC_FORBIDDEN,"""{"error" : "No tienes permisos"}""")
//		}
		
		//VARIABLES===========================================================
		Integer parameterP = Integer.valueOf(p);
		Integer parameterC = Integer.valueOf(c);
		String jsonData = "{}"
		try {
			jsonData = request.reader.readLines().join("\n")
		}catch (Exception e) {
			jsonData = "{}" 
		}
		
		try {
			switch(url) {
				case "test":
					result = dao.testFuction(parameterP, parameterC, jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatalogosGenericos":
					String catalogo =request.getParameter "catalogo"
					result = new CatalogosDAO().getCatalogosGenericos(catalogo);
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getBitacoraSDAEByCaseId":
					String caseId = request.getParameter "caseId"
					result = new BitacoraSDAEDAO().getBitacoraSDAEByCaseId(Long.valueOf(caseId), context);
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getSDAEGestionEscolarByCarrera":
					String idCarrera =request.getParameter "idCarrera"
					result = new CatalogosDAO().getSDAEGestionEscolarByCarrera(Long.valueOf(idCarrera));
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatTipoAoyoByCampus":
					String campus = request.getParameter "campus"
					result = new CatalogosDAO().getCatTipoAoyoByCampus(campus, jsonData, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
				case "getPromedioMinimoApoyoByCampus":
					String idCampus = request.getParameter "idCampus"
					result = new CatalogosDAO().getPromedioMinimoApoyoByCampus(Long.valueOf(idCampus));
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
					
				case "getDocumentosByTipoApoyo":
					String campus = request.getParameter "campus"
					String idTipoApoyo = request.getParameter "idTipoApoyo"
					String isAvalString = request.getParameter "isAval"
					Boolean isAval = isAvalString.equals("true");
					result = new CatalogosDAO().getDocumentosByTipoApoyo(campus, idTipoApoyo, isAval, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
				case "getInfoBitacora":
					String caseId =request.getParameter "caseId"
					result = new CatalogosDAO().getInfoBitacora(Long.valueOf(caseId));
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
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
				case "getSolicitudDeAdmision":
					String email = request.getParameter "email"
					result = new SolicitudDeAdmisionDAO().getSolicitudDeAdmision(email);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getInfrmacionEscolar":
					String email = request.getParameter "email"
					result = new SolicitudDeAdmisionDAO().getInfrmacionEscolar(email);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getPadresTutorByCaseid":
					String caseid = request.getParameter "caseid"
					result = new SolicitudDeAdmisionDAO().getPadresTutorByCaseid(caseid);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatTienesHijos":
					result = new CatalogosDAO().getCatTienesHijos();
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
				case "getUserProcessApoyoEducativo":
					result = new BonitaGetsDAO().getUserProcessApoyoEducativo(context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
				case "getSolicitudByCaseid":
					String caseid = request.getParameter "caseid"
					result = new ListadoDAO().getSolicitudApoyoByCaseId(Integer.parseInt(caseid), context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
				case "getLinkDescarga":
					String linkAzure = request.getParameter "linkAzure"
					result = new SolicitudDeAdmisionDAO().getLinkAzureDescarga(linkAzure, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getBitacoraSDAEByCaseId":
					String caseId = request.getParameter "caseId"
					result = new BitacoraSDAEDAO().getBitacoraSDAEByCaseId(Long.valueOf(caseId), context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getB64FileByPersistenceId":
					String persistenceId = request.getParameter "persistenceId"
					result = new SolicitudDeAdmisionDAO().getB64FileByPersistenceId(Integer.parseInt(persistenceId));
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getComentariosByCaseid":
					String caseId = request.getParameter "caseId"
					result = new BitacoraDAO().getComentariosByCaseid(Long.valueOf(caseId));
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				break;
				case"getImagenesByCaseId":
					String caseId = request.getParameter "caseId"
					result = new CatalogosDAO().getImagenesByCaseId(Long.valueOf(caseId));
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				break;
				case"getDocumentosByCaseId":
					String caseId = request.getParameter "caseId"
					result = new CatalogosDAO().getDocumentosByCaseId(Long.valueOf(caseId));
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				break;
				case"getBannerInfo":
					String idbanner = request.getParameter "idbanner"
					String idcampus = request.getParameter "idcampus"
					result = new BannerRequestDAO().getBannerInfo(idcampus, idbanner);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				break;
				case "getB64FileByUrlAzure":
					String urlAzure = request.getParameter "urlAzure"
					result = new SolicitudDeAdmisionDAO().getB64FileByUrlAzure(urlAzure);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.getData()).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getExisteSDAEGestionEscolar":
					String pid = request.getParameter "id"
					Long id = 0L;
					if(!pid.equals(null) && !pid.equals("") && !pid.equals("null")) {
						id = Long.valueOf(pid)
					}
					result = new CatalogosDAO().getExisteSDAEGestionEscolar(id);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getExisteSDAECreditoGE":
					String pid = request.getParameter "sdaeGE"
					String fecha = request.getParameter "fecha"
					Long id = 0L;
					if(!pid.equals(null) && !pid.equals("") && !pid.equals("null")) {
						id = Long.valueOf(pid)
					}
					result = new CatalogosDAO().getExisteSDAECreditoGE(id,fecha);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getSDAEGestionEscolar":
					String pid = request.getParameter "id"
					Long id = 0L;
					if(!pid.equals(null) && !pid.equals("") && !pid.equals("null")) {
						id = Long.valueOf(pid)
					}
					result = new CatalogosDAO().getSDAEGestionEscolar(id);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getSDAEGestionEscolarByCarrera":
					String pid = request.getParameter "id"
					Long id = 0L;
					if(!pid.equals(null) && !pid.equals("") && !pid.equals("null")) {
						id = Long.valueOf(pid)
					}
					result = new CatalogosDAO().getSDAEGestionEscolarByCarrera(id);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCreditoGE":
					String pid = request.getParameter "sdaeGE"
					String fecha = request.getParameter "fecha"
					Long id = 0L;
					if(!pid.equals(null) && !pid.equals("") && !pid.equals("null")) {
						id = Long.valueOf(pid)
					}
					result = new CatalogosDAO().getCreditoGE(id,fecha);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getYear":
					result = new CatalogosDAO().getFechaServidor();
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getConfiguracionCampus":
					String idCampus = request.getParameter "idCampus"
					result = new CatalogosDAO().getConfiguracionCampus(Long.valueOf(idCampus));
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
				case "getConfiguracionPagoEstudioSocEco":
					String idCampus = request.getParameter "idCampus"
					result = new CatalogosDAO().getConfiguracionPagoEstudioSocEco(Long.valueOf(idCampus));
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
				case "getConfiguracionPagoEstudioSocEcoAspirante":
					String idCampus = request.getParameter "idCampus"
					result = new CatalogosDAO().getConfiguracionPagoEstudioSocEcoAspirante(Long.valueOf(idCampus));
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
				case "getPRomedioMinimoByCampus":
					String idCampus = request.getParameter "idCampus"
					result = new CatalogosDAO().getPRomedioMinimoByCampus(Long.valueOf(idCampus));
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
				case "updateEstatusSolicitud":
					String caseId = request.getParameter "caseId"
					String estatus = request.getParameter "estatus"
					result = new SolicitudDeAdmisionDAO().updateEstatusSolicitud(estatus, Long.valueOf(caseId));
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getB64CurriculumByCaseId":
					String caseId = request.getParameter "caseId"
					result = new SolicitudDeAdmisionDAO().getB64CurriculumByCaseId(Integer.parseInt(caseId));
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
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
		
		//return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
		
		/*
        Properties props = loadProperties("configuration.properties", context.resourceProvider)
        String paramValue = props["myParameterKey"] 
        def result = [ "p" : p ,"c" : c ,"url" : url , "myParameterKey" : paramValue, "currentDate" : LocalDate.now().toString() ] 
        // Send the result as a JSON representation
        // You may use buildPagedResponse if your result is multiple
        return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
		*/
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
