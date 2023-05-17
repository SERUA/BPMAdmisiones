package com.anahuac.rest.api;

import com.anahuac.rest.api.DAO.DocumentDAO
import com.anahuac.rest.api.DAO.PDFDocumentDAO
import com.anahuac.rest.api.Entity.Result
import groovy.json.JsonBuilder

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.http.HttpHeaders
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.bonitasoft.web.extension.rest.RestAPIContext
import com.bonitasoft.web.extension.rest.RestApiController

import java.time.LocalDate

class Index implements RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Index.class)

    @Override
    RestApiResponse doHandle(HttpServletRequest request, RestApiResponseBuilder responseBuilder, RestAPIContext context) {
        // To retrieve query parameters use the request.getParameter(..) method.
        // Be careful, parameter values are always returned as String values

        // Here is an example of how you can retrieve configuration parameters from a properties file
        // It is safe to remove this if no configuration is required
        Properties props = loadProperties("configuration.properties", context.resourceProvider)
        String paramValue = props["myParameterKey"]

        /* 
         * Execute business logic here
         * 
         * 
         * Your code goes here
         * 
         * 
         */
        // Prepare the result
		Result result  = new Result();
		String jsonData = null 
		if(!bonitaRolFilter(context)) {
			return buildResponse(responseBuilder, HttpServletResponse.SC_FORBIDDEN,"""{"error" : "Permission denied"}""")
		}
		try {
			jsonData=request.reader.readLines().join("\n")
			def url = request.getParameter "pdf"
			if (url == null) {
				result = new DocumentDAO().getDocs(jsonData, context)
				if(result.success) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result?.getData()).toString())
				}else {
					return buildResponse(responseBuilder, (result.error.contains("400"))?HttpServletResponse.SC_BAD_REQUEST:(result.error.contains("404"))?HttpServletResponse.SC_NOT_FOUND:HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new JsonBuilder(result).toString())
				}
			}else {
				if(url.equals("pdfPsicometricov3") || url == "pdfPsicometricov3") {
					result = new PDFDocumentDAO().PdfFileCatalogo(jsonData,context);
					if(result.success) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, (result.error.contains("400"))?HttpServletResponse.SC_BAD_REQUEST:(result.error.contains("404"))?HttpServletResponse.SC_NOT_FOUND:HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new JsonBuilder(result).toString())
					}
				} else if(url.equals("pdfDatosAval") || url == "pdfDatosAval") {
					String email = request.getParameter "email"
					result = new PDFDocumentDAO().pdfDatosAval(email, context);
					
					if(result.success) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new JsonBuilder(result).toString())
					}
				}  else if(url.equals("getInfoAval") || url == "getInfoAval") {
					String email = request.getParameter "email"
					result = new PDFDocumentDAO().getInfoAval(email, context);
					
					if(result.success) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new JsonBuilder(result).toString())
					}
				} else if(url.equals("getSolicitudApoyo") || url == "getSolicitudApoyo") {
					String email = request.getParameter "email"
					String caseid = request.getParameter "caseid"
					result = new PDFDocumentDAO().getSolicitudApoyo(email, caseid, context);
					if(result.success) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new JsonBuilder(result).toString())
					}
				} else if(url.equals("pdfSolicitudApoyo") || url == "pdfSolicitudApoyo") {
					String email = request.getParameter "email"
					String caseid = request.getParameter "caseid"
					result = new PDFDocumentDAO().pdfSolicitudApoyo(email, caseid, context);
					if(result.success) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new JsonBuilder(result).toString())
					}
				}
			}
		}catch(Exception ex) {
			jsonData = null
			return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder("[]").toString())
		}
		
		
		
		
		
		
		
		
		/*def resultado = [  "myParameterKey" : paramValue, "currentDate" : LocalDate.now().toString() ]

        // Send the result as a JSON representation
        // You may use buildPagedResponse if your result is multiple
        return buildResponse(responseBuilder, HttpServletResponse.SC_OK, jsonData)*/
		
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
	
	private Boolean bonitaRolFilter(RestAPIContext context) {
		Boolean valid = false;
		List<UserMembership> uMemberships=context.apiClient.identityAPI.getUserMemberships(context.apiSession.userId, 0, 100, UserMembershipCriterion.ROLE_NAME_ASC);
		uMemberships.each{
			it ->
			if((it.roleName.equals("EXTERIOR") && it.groupName.equals("CAMPUS-PUEBLA")) || it.roleName.equals("ADMINISTRADOR") || it.roleName.equals("TI SERUA") || it.roleName.equals("Becas")) {
				valid=true
			}
		}
		return valid;
	}
}
