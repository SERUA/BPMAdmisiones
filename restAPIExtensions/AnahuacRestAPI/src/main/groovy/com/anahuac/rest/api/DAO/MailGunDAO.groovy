package com.anahuac.rest.api.DAO

import java.io.File;

import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstanceSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessDefinition
import org.bonitasoft.engine.search.Order
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.api.APIAccessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.EstructuraMailGun
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import groovy.json.JsonSlurper

import com.bonitasoft.web.extension.rest.RestAPIContext

class MailGunDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MailGunDAO.class);
	
	public Result sendEmail(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		ProcessDefinition objProcessDefinition;
		Long procesoid = 0L;
		//LOGGER.error context.getApiClient().getProcessAPI()
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def correocopia = "";
			assert object instanceof Map;
			if(object.lstCopia != null) {
				assert object.lstCopia instanceof List;
			}
			
			String casoString = String.valueOf(object.idcaso);
			procesoid = Long.valueOf(casoString);
		
			EstructuraMailGun estructura = new EstructuraMailGun();
			estructura.setTo(object.to);
			for(def row: object.lstCopia) {
				if(correocopia == "") {
					correocopia = row;
				}else {
					correocopia = correocopia + ";" + row;
				}
			}
			estructura.setCc(correocopia);
			estructura.setSubject(object.subject);
			estructura.setBody(object.body);
			
			LOGGER.error "estructura para correo "+ estructura
			
			
			JsonNode jsonNode = sendSimpleMessage(estructura);
			LOGGER.error "=================================";
			LOGGER.error jsonNode.toString();
			resultado.setSuccess(true);
		}catch(Exception ex) {
			LOGGER.error ex.getMessage()
			resultado.setSuccess(false);
			resultado.setError(ex.getMessage());
		}
		
		return resultado;
	}
	
	public static JsonNode sendSimpleMessage(EstructuraMailGun estructura) throws UnirestException {
		
				HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + "sandbox639652c5285041f19607ec21c77aaa31.mailgun.org" + "/messages")
					.basicAuth("api", "5f81973d3331694641c504eeace14f71-ea44b6dc-f792acc6")
					.field("from", "Correo de pruebas <ejemplo@mailgun.com>")
					.field("to", estructura.to)
					.field("cc", estructura.cc)
					.field("subject", estructura.subject)
					.field("html", estructura.body)
					.asJson();
		
				return request.getBody();
			}
	
}
