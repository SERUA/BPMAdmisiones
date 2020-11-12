package com.anahuac.rest.api;

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DAO.ConektaDAO
import com.anahuac.rest.api.DAO.ListadoDAO
import com.anahuac.rest.api.DAO.MailGunDAO
import com.anahuac.rest.api.DAO.TestDAO
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext
import com.bonitasoft.web.extension.rest.RestApiController

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class Index implements RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Index.class)

    @Override
    RestApiResponse doHandle(HttpServletRequest request, RestApiResponseBuilder responseBuilder, RestAPIContext context) {
		def result = null;
        def p = request.getParameter "p";
        if (p == null) {
            return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter p is missing"}""")
        }
        def c = request.getParameter "c";
        if (c == null) {
            return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter c is missing"}""")
        }
		def url = request.getParameter "url";
		if (url == null) {
			return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter url is missing"}""")
        }
		
		//VARIABLES===========================================================
		Integer parameterP = Integer.valueOf(p);
		Integer parameterC = Integer.valueOf(c);
		String jsonData = request.reader.readLines().join("\n")
		
		//VARIABLES DAO=======================================================
		TestDAO dao =  new TestDAO();
		ListadoDAO lDao = new ListadoDAO();
		MailGunDAO mgdao = new MailGunDAO();
		ConektaDAO cDao = new ConektaDAO();
		
		//MAPEO DE SERVICIOS==================================================
		try {
			switch(url) {
				case "test":
					result = dao.testFuction(parameterP, parameterC, jsonData);
					break;
				case "getSolicitudTramite":
					result = lDao.getSolicitudTramite(parameterP, parameterC, jsonData, context);
					break;
				case "sendEmail":
					result = mgdao.sendEmail(parameterP, parameterC, jsonData, context);
					break;
				case "pagoOxxoCash":
					LOGGER.error "pago oxxo"
					result = cDao.pagoOxxoCash(parameterP, parameterC, jsonData, context);
					break;
				case "pagoTarjeta":
					LOGGER.error "pago tarjeta"
					result = cDao.pagoTarjeta(parameterP, parameterC, jsonData, context);
					break;
				case "pagoSPEI":
					LOGGER.error "pago spei"
					result = cDao.pagoSPEI(parameterP, parameterC, jsonData, context);
					break;
				default:
					result = notFound(url);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace()
		}
		
		return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
    }
	
	public Result notFound(String url) {
		Result resultado = new Result();
		resultado.setSuccess(false);
		resultado.setError("No se reconose el servicio: "+url);
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
