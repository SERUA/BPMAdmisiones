package com.anahuac.rest.api;

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.http.HttpHeaders
import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DAO.CatalogosDAO
import com.anahuac.rest.api.DAO.NotificacionDAO
import com.anahuac.rest.api.DAO.UsuariosDAO
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.custom.AppMenuRole
import com.anahuac.rest.api.Entity.db.Role
import com.anahuac.rest.api.Security.SecurityFilter
import com.anahuac.rest.api.DB.DBConnect

import org.bonitasoft.web.extension.rest.RestAPIContext
import org.bonitasoft.web.extension.rest.RestApiController

import java.time.LocalDate

class Index implements RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Index.class)

    @Override
    RestApiResponse doHandle(HttpServletRequest request, RestApiResponseBuilder responseBuilder, RestAPIContext context) {
		Result result = new Result();
        Properties props = loadProperties("configuration.properties", context.resourceProvider);
		def url = request.getParameter "url"
		if (url == null) {
			return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter url is missing"}""")
        }
		
		SecurityFilter security = new SecurityFilter();
		Result resultadoFiltro = security.allowedUrl(context, url); 
		if(!resultadoFiltro.isSuccess()){
			return buildResponse(responseBuilder, HttpServletResponse.SC_FORBIDDEN,"""{"error" : "No tienes permisos"}""")
		}
		
		String jsonData = "{}"
		try {
			jsonData = request.reader.readLines().join("\n");
		} catch (Exception e) {
			jsonData = "{}"
		}
		
		String errorLog = resultadoFiltro.getError_info();
		
		try {
			switch(url) {
				case "simpleSelect":
					result = new CatalogosDAO().simpleSelect(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "simpleSelectBonita":
					result = new CatalogosDAO().simpleSelectBonita(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
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
				case "insertCatPais":
					result = new CatalogosDAO().insertCatPais(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatPais":
					result = new CatalogosDAO().deleteCatPais(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateCatPais":
					result = new CatalogosDAO().updateCatPais(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatPais":
					result = new CatalogosDAO().getCatPais(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertCatEstado":
					result = new CatalogosDAO().insertCatEstado(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatEstado":
					result = new CatalogosDAO().deleteCatEstado(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateCatEstado":
					result = new CatalogosDAO().updateCatEstado(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatEstado":
					result = new CatalogosDAO().getCatEstado(jsonData)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertCatSexo":
					result = new CatalogosDAO().insertCatSexo(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatSexo":
					result = new CatalogosDAO().deleteCatSexo(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateCatSexo":
					result = new CatalogosDAO().updateCatSexo(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatSexo":
					result = new CatalogosDAO().getCatSexo(jsonData)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertCatCampus":
					result = new CatalogosDAO().insertCatCampus(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatCampus":
					result = new CatalogosDAO().deleteCatCampus(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateCatCampus":
					result = new CatalogosDAO().updateCatCampus(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatCampus":
					result = new CatalogosDAO().getCatCampus(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertCatGestionEscolar":
					result = new CatalogosDAO().insertCatGestionEscolar(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatGestionEscolar":
					result = new CatalogosDAO().deleteCatGestionEscolar(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateCatGestionEscolar":
					result = new CatalogosDAO().updateCatGestionEscolar(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatGestionEscolar":
					result = new CatalogosDAO().getCatGestionEscolar(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getLstCampus":
					result = new CatalogosDAO().getLstCampus(jsonData)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertCatPropedeutico":
					result = new CatalogosDAO().insertCatPropedeutico(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatPropedeutico":
					result = new CatalogosDAO().deleteCatPropedeutico(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateCatPropedeutico":
					result = new CatalogosDAO().updateCatPropedeutico(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatPropedeutico":
					result = new CatalogosDAO().getCatPropedeutico(jsonData)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertCatPosgrado":
					result = new CatalogosDAO().insertCatPosgrado(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatPosgrado":
					result = new CatalogosDAO().deleteCatPosgrado(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateCatPosgradoo":
					result = new CatalogosDAO().updateCatPosgrado(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatPosgrado":
					result = new CatalogosDAO().getCatPosgrado(jsonData)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertCatEstadoCivil":
					result = new CatalogosDAO().insertCatEstadoCivil(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatEstadoCivil":
					result = new CatalogosDAO().deleteCatEstadoCivil(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateCatEstadoCivil":
					result = new CatalogosDAO().updateCatEstadoCivil(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatEstadoCivil":
					result = new CatalogosDAO().getCatEstadoCivil(jsonData)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertCatReligion":
					result = new CatalogosDAO().insertCatReligion(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatReligion":
					result = new CatalogosDAO().deleteCatReligion(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatReligionFisico":
					result = new CatalogosDAO().deleteCatReligionFisico(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateCatReligion":
					result = new CatalogosDAO().updateCatReligion(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatReligion":
					result = new CatalogosDAO().getCatReligion(jsonData)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertCatNacionalidad":
					result = new CatalogosDAO().insertCatNacionalidad(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatNacionalidad":
					result = new CatalogosDAO().deleteCatNacionalidad(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateCatNacionalidad":
					result = new CatalogosDAO().updateCatNacionalidad(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatNacionalidad":
					result = new CatalogosDAO().getCatNacionalidad(jsonData)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertCatPosgrado2":
					result = new CatalogosDAO().insertCatPosgrado2(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatPosgrado2":
					result = new CatalogosDAO().deleteCatPosgrado2(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateCatPosgrado2":
					result = new CatalogosDAO().updateCatPosgrado2(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatPosgrado2":
					result = new CatalogosDAO().getCatPosgrado2(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "generateHtml":
					result = new NotificacionDAO().generateHtml(jsonData, context);
					/*result = new Result();
					result.setSuccess(true);*/

					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateBusinessAppMenu":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					assert object instanceof Map;
					AppMenuRole row = new AppMenuRole();
					row.setDisplayname(object.displayname);
					row.setId(object.id);
					row.setRoles(new ArrayList<Role>());
					
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
					
					result = new UsuariosDAO().updateBusinessAppMenu(row);
					
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertCatEstatusProceso":
					result = new CatalogosDAO().insertCatEstatusProceso(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateCatEstatusProceso":
					result = new CatalogosDAO().updateCatEstatusProceso(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatEstatusProceso":
					result = new CatalogosDAO().deleteCatEstatusProceso(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatEstatusProceso":
					result = new CatalogosDAO().getCatEstatusProceso(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertCatSiNo":
					result = new CatalogosDAO().insertCatSiNo(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateCatSiNo":
					result = new CatalogosDAO().updateCatSiNo(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatSiNo":
					result = new CatalogosDAO().deleteCatSiNo(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatSiNo":
					result = new CatalogosDAO().getCatSiNo(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getConfiguraciones":
					result = new CatalogosDAO().getConfiguraciones(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertConfiguraciones":
					result = new CatalogosDAO().insertConfiguraciones(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateConfiguraciones":
					result = new CatalogosDAO().updateConfiguraciones(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteConfiguraciones":
					result = new CatalogosDAO().deleteConfiguraciones(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertCatMediosEnteraste":
					result = new CatalogosDAO().insertCatMediosEnteraste(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatFiltroSeguridad":
					result = new CatalogosDAO().deleteCatFiltroSeguridad(jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateCatMediosEnteraste":
					result = new CatalogosDAO().updateCatMediosEnteraste(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatMediosEnteraste":
					result = new CatalogosDAO().getCatMediosEnteraste(jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result.data).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "RegistrarUsuario":
					try{
						result =  UsuariosDAO.postRegistrarUsuario(parameterP, parameterC, jsonData, context);
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
				case "registro":
					result = new UsuariosDAO().registro(jsonData)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
			}
		} catch (Exception e) {
			result.setSuccess(false)
			result.setError("500 INTERNAL SERVER ERROR: " + e.getMessage());
			result.setError_info(errorLog)
			return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,new JsonBuilder(result).toString())
		}
		
        return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
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
