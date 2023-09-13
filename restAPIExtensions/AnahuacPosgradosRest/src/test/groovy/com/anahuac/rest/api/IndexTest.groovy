package com.anahuac.rest.api;

import groovy.json.JsonSlurper

import javax.servlet.http.HttpServletRequest

import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder

import spock.lang.Specification
import org.bonitasoft.web.extension.rest.RestAPIContext

import java.time.LocalDate;

/**
 * @see http://spockframework.github.io/spock/docs/
 */
class IndexTest extends Specification {

    // Declare mocks here
    // Mocks are used to simulate external dependencies behavior
    def httpRequest = Mock(HttpServletRequest)
    def resourceProvider = Mock(ResourceProvider)
    def context = Mock(RestAPIContext)

    /**
     * You can configure mocks before each tests in the setup method
     */
    def setup(){
        // Simulate access to configuration.properties resource
        context.resourceProvider >> resourceProvider
        resourceProvider.getResourceAsStream("configuration.properties") >> IndexTest.class.classLoader.getResourceAsStream("testConfiguration.properties")
    }

    def should_return_a_json_representation_as_result() {
    given: "a RestAPIController"
    def index = new Index()
    
    // Simular una solicitud con el parámetro "url" nulo
    httpRequest.getParameter("url") >> null
    
    when: "Invocar la API REST"
    def apiResponse = index.doHandle(httpRequest, new RestApiResponseBuilder(), context)

    then: "Una representación JSON es devuelta en el cuerpo de la respuesta"
    def jsonResponse = new JsonSlurper().parseText(apiResponse.response)

    // Verificar que la respuesta tenga un estado HTTP 400 cuando "url" es nulo
    apiResponse.httpStatus == 400
    jsonResponse.error == "the parameter url is missing"
	}

}