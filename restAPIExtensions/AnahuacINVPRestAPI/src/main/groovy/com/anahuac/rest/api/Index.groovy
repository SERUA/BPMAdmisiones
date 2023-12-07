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
import com.anahuac.rest.api.DAO.EnvioRespuestasDAO
import com.anahuac.rest.api.DAO.LoginSesionesDAO
import com.anahuac.rest.api.DAO.RespuestasExamenDAO
import com.anahuac.rest.api.DAO.UsuariosDAO
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.custom.AppMenuRole
import com.anahuac.rest.api.Entity.db.Role

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
		CatalogosDAO cDAO = new CatalogosDAO();
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
				case "insertRespuestaFinal":
					result = new CatalogosDAO().insertRespuestaFinal(jsonData)
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
			case "generarRespuestasArchivo":
				try{
					result =  rexa.generarRespuestasArchivo(jsonData);
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
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					
					Map<String, Serializable> contract = new HashMap<String, Serializable>();
					Map<String, Serializable> instanciaINVPInput = new HashMap<String, Serializable>();
	
					contract.put("idUsuarioInput",Long.valueOf(object.idUsuarioInput))
					instanciaINVPInput.put("username", object.instanciaINVPInput.username);
					instanciaINVPInput.put("idsesion", Long.valueOf(object.instanciaINVPInput.idsesion));
					contract.put("terminarExamenInput", object.terminarExamenInput);
					contract.put("instanciaINVPInput", instanciaINVPInput);
					
					Long processId = context.getApiClient().getProcessAPI().getLatestProcessDefinitionId("Examen INVP");
					ProcessInstance processInstance = context.getApiClient().getProcessAPI().startProcessWithInputs(processId, contract);
					Long caseId = processInstance.getRootProcessInstanceId();
					Result resultBloquear = new UsuariosDAO().bloquearAspiranteDef(object.instanciaINVPInput.username);
					
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK,"{\"caseId\": "+caseId+"}")
				} catch(Exception ex) {
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
			case "checkInstance":
				try{
					result =  lses.checkInstance(jsonData);
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
			case "terminarTodos":
				String idsesion = request.getParameter "idsesion"
				result = new UsuariosDAO().terminarTodos(Long.valueOf(idsesion), context);
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
			case "terminarUsuario":
				String username = request.getParameter "username"
				result = new UsuariosDAO().terminarUsuario(username, context);
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				
			case "getUsuariosBloqueados":
				try{
					result =  new UsuariosDAO().getUsuariosBloqueados(jsonData);
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
			case "insertUpdateConfiguracionSesion":
				try{
					result =  new CatalogosDAO().insertUpdateConfiguracionSesion(jsonData);
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
			case "getDatosSesionLogin":
			try{
				result =  lses.getDatosSesionLogin(jsonData);
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
			case "updateBusinessAppMenu":
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				assert object instanceof Map;
				AppMenuRole row = new AppMenuRole()
				row.setDisplayname(object.displayname)
				row.setId(object.id)
				row.setRoles(new ArrayList<Role>())
				
				for(def i=0; i<object.roles.size(); i++) {
					Role rol = new Role();
					try {
						rol.setId(Long.parseLong(object.roles[i].id))
					} catch (Exception e) {
						rol.setId(object.roles[i].id)
					}
					
					rol.setEliminado(object.roles[i].eliminado)
					rol.setNuevo(object.roles[i].nuevo)
					row.getRoles().add(rol)
				}
				result = new UsuariosDAO().updateBusinessAppMenu(row)
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "insertRespuestaid":
			try{
				result =  rexa.insertRespuestaid(jsonData);
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
			case "getSesiones":
				try{
					result =  cDAO.getSesiones(jsonData, context);
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
			case "getExcelFileSesiones":
				try{
					result =  cDAO.getExcelFileSesiones(jsonData, context);
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
			case "getExcelFileSesionesToday":
				try{
					result =  cDAO.getExcelFileSesionesToday(jsonData, context);
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
			case "getExcelFileAspirantes":
				try{
					result =  cDAO.getExcelFileAspirantes(jsonData, context);
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
			case "getExcelFileAspirantesTodos":
				try{
					result =  cDAO.getExcelFileAspirantesTodos(jsonData, context);
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
			
			case "getExcelFileAspirantesTemporales":
				try{
					result =  cDAO.getExcelFileAspirantesTemporales(jsonData, context);
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
			case "insertUpdateUsuarioNuevaConfig":
				try{
					result =  cDAO.insertUpdateUsuarioNuevaConfig(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				} catch(Exception ou){
					result.setSuccess(false)
					result.setError(ou.getMessage())
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "insertUpdateUsuarioTolerancias":
				try{
					result =  uDAO.insertUpdateUsuarioTolerancias(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				} catch(Exception ou){
					result.setSuccess(false)
					result.setError(ou.getMessage())
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "insertUpdateUsuarioToleranciasTemp":
				try{
					result =  uDAO.insertUpdateUsuarioToleranciasTemp(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				} catch(Exception ou){
					result.setSuccess(false)
					result.setError(ou.getMessage())
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
			case "getSesionesToday":
				try{
					result =  cDAO.getSesionesToday(jsonData, context);
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
			
			case "getAspirantes":
				try{
					result =  new UsuariosDAO().getAspirantes(jsonData, context);
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
			case "getAspirantesTemporales":
				try{
					result =  new UsuariosDAO().getAspirantesTemporales(jsonData, context);
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
			case "getAspirantesTodos":
				try{
					result =  new UsuariosDAO().getAspirantesTodos(jsonData, context);
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
			case "updateIdiomaTodos":
				try{
					String idioma = request.getParameter "idioma"
					result =  new UsuariosDAO().updateIdiomaTodos(idioma, jsonData, context);
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
			case "bloquearAspirante":
				try{
					String username =request.getParameter "username"
					String bloquear =request.getParameter "bloquear"
					String terminar =request.getParameter "terminar"
					
					result =  new UsuariosDAO().bloquearAspirante(username, Boolean.valueOf(bloquear), Boolean.valueOf(terminar));
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
			case "insertidiomausuario":
				try{
					result =  uDAO.insertIidiomaUsuario(jsonData);
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
			case "updateidiomausuario":
				try{
					result =  uDAO.updateidiomausuario(jsonData);
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
			case "loginINVP":
				try{
					result =  lses.loginINVP(jsonData, context);
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
			case "loginV2":
				try{
					result =  lses.loginV2(jsonData, context);
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
			case "insertUpdateIidiomaUsuario":
				try{
					result =  uDAO.insertUpdateIidiomaUsuario(jsonData);
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
				
			case "PostRespuestaSesion":
				try{
					result =  new EnvioRespuestasDAO().PostRespuestaSesion(jsonData);
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
			case "PostMasivoRespuestaSesion":
				try{
					result =  new EnvioRespuestasDAO().PostMasivoRespuestaSesion(jsonData);
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
			case "insertCatFiltroSeguridad":
				result = new CatalogosDAO().insertCatFiltroSeguridad(jsonData, context)
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
			case "deleteCatFiltroSeguridad":
				result = new CatalogosDAO().deleteCatFiltroSeguridad(jsonData, context)
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
			case "updateCatFiltroSeguridad":
				result = new CatalogosDAO().updateCatFiltroSeguridad(jsonData, context)
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
			case "getCatFiltroSeguridad":
				result = new CatalogosDAO().getCatFiltroSeguridad(jsonData)
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
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
