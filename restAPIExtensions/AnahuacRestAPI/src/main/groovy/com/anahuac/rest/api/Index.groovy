package com.anahuac.rest.api;

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DAO.CatalogoBachilleratoDAO
import com.anahuac.rest.api.DAO.ConektaDAO
import com.anahuac.rest.api.DAO.ListadoDAO
import com.anahuac.rest.api.DAO.MailGunDAO
import com.anahuac.rest.api.DAO.NotificacionDAO
import com.anahuac.rest.api.DAO.TestDAO
import com.anahuac.rest.api.DAO.UsuariosDAO
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
		ConektaDAO cDao = new ConektaDAO();
		UsuariosDAO uDAO = new UsuariosDAO();
		NotificacionDAO nDAO = new NotificacionDAO();
		MailGunDAO mgDAO = new MailGunDAO();
		CatalogoBachilleratoDAO bDao = new CatalogoBachilleratoDAO()
		//MAPEO DE SERVICIOS==================================================
		try {
			switch(url) {
				case "test":
					result = dao.testFuction(parameterP, parameterC, jsonData);
					break;
				case "getNuevasSolicitudes":
					result = lDao.getNuevasSolicitudes(parameterP, parameterC, jsonData, context);
					break;
				case "getAspirantesProceso":
					result = lDao.getAspirantesProceso(parameterP, parameterC, jsonData, context);
					break;
				case "getDocumentoTest":
					result = lDao.getDocumentoTest(parameterP, parameterC, jsonData, context);
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
				case "RegistrarUsuario":
					result =  uDAO.postRegistrarUsuario(parameterP, parameterC, jsonData, context);
					break;
				case "getOrderPaymentMethod":
					result = cDao.getOrderPaymentMethod(parameterP, parameterC, jsonData, context);
					break;
				case "getOrderDetails":
					result =  cDao.getOrderDetails(parameterP, parameterC, jsonData, context);
					break;
				case "getConektaPublicKey":
					result = cDao.getConektaPublicKey(parameterP, parameterC, jsonData, context);
					break;
				case "ejecutarEsperarPago":
					result = cDao.ejecutarEsperarPago(parameterP, parameterC, jsonData, context);
					break;
				/*case "encode":
					result =  aDAO.base64Encode(parameterP, parameterC, jsonData, context);
					break;
				case "decode":
					result =  aDAO.base64Decode(parameterP, parameterC, jsonData, context);
					break;*/
				case "recuparaPassword":
					result =  uDAO.postRecuperarPassword(parameterP, parameterC, jsonData, context);
					break;
				case "sendEmail":
					result = mgDAO.sendEmail(parameterP, parameterC, jsonData, context);
					break;
				case "generateHtml":
					result = nDAO.generateHtml(parameterP, parameterC, jsonData, context);
					break;
				case "getTestUpdate":
					result = nDAO.getDocumentoTest(parameterP, parameterC, jsonData, context);
					break;
					case "insertLicenciatura":
					result = nDAO.insertLicenciatura(parameterP, parameterC, jsonData, context)
					break;
					case "simpleSelect":
					result = nDAO.simpleSelect(parameterP, parameterC, jsonData, context)
					break;
					case "insertBachillerato":
					result = bDao.insert(parameterP, parameterC, jsonData, context)
					break;
					case "updateBachillerato":
					result = bDao.update(parameterP, parameterC, jsonData, context)
					break;
					case "getBachillerato":
					result = bDao.get(parameterP, parameterC, jsonData, context)
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
