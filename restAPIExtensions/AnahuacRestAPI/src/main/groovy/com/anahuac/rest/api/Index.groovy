package com.anahuac.rest.api

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.anahuac.catalogos.CatDocumentosTextos
import com.anahuac.catalogos.CatNotificaciones
import com.anahuac.catalogos.CatNotificacionesFirma
import com.anahuac.rest.api.DAO.BecasDAO
import com.anahuac.rest.api.DAO.BitacorasDAO
import com.anahuac.rest.api.DAO.BannerDAO
import com.anahuac.rest.api.DAO.CatalogoBachilleratoDAO
import com.anahuac.rest.api.DAO.ConektaDAO
import com.anahuac.rest.api.DAO.CustomUserRequestDAO
import com.anahuac.rest.api.DAO.DocumentosTextosDAO
import com.anahuac.rest.api.DAO.HubspotDAO
import com.anahuac.rest.api.DAO.ImportacionPAADAO
import com.anahuac.rest.api.DAO.ListadoDAO
import com.anahuac.rest.api.DAO.MailGunDAO
import com.anahuac.rest.api.DAO.MigracionDAO
import com.anahuac.rest.api.DAO.NotificacionDAO
import com.anahuac.rest.api.DAO.NuevoListadoDAO
import com.anahuac.rest.api.DAO.PsicometricoDAO
import com.anahuac.rest.api.DAO.ResultadosAdmisionDAO
import com.anahuac.rest.api.DAO.SesionesDAO
import com.anahuac.rest.api.DAO.AvanzeProcesoDAO
import com.anahuac.rest.api.DAO.SolicitudUsuarioDAO
import com.anahuac.rest.api.DAO.SubIndex
import com.anahuac.rest.api.DAO.TestDAO
import com.anahuac.rest.api.DAO.TransferenciasDAO
import com.anahuac.rest.api.DAO.ReactivacionDAO
import com.anahuac.rest.api.DAO.ReportesDAO
import com.anahuac.rest.api.DAO.ResultadoComiteDAO
import com.anahuac.rest.api.DAO.CatalogosDAO
import com.anahuac.rest.api.DAO.UsuariosDAO
import com.anahuac.rest.api.DAO.LogDAO
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.AppMenuRole
import com.anahuac.rest.api.Entity.Custom.AzureConfig
import com.anahuac.rest.api.Entity.Custom.PruebaCustom
import com.anahuac.rest.api.Entity.Custom.ResponsableCustom
import com.anahuac.rest.api.Entity.Custom.SesionCustom
import com.anahuac.rest.api.Entity.db.CatNotificacionesCampus
import com.anahuac.rest.api.Entity.db.ResponsableDisponible
import com.anahuac.rest.api.Entity.db.Role
import com.anahuac.rest.api.Entity.db.Sesion_Aspirante
import com.anahuac.rest.api.Entity.HubspotConfig
import com.anahuac.rest.api.Security.SecurityFilter
import com.bonitasoft.web.extension.rest.RestAPIContext
import com.bonitasoft.web.extension.rest.RestApiController

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class Index implements RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Index.class)

    @Override
    RestApiResponse doHandle(HttpServletRequest request, RestApiResponseBuilder responseBuilder, RestAPIContext context) {
		Result result = new Result();
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
		
		SecurityFilter security = new SecurityFilter();
		if(!security.allowedUrlPost(context,url)){
			return buildResponse(responseBuilder, HttpServletResponse.SC_FORBIDDEN,"""{"error" : "No tienes permisos"}""")
		}
				
		//VARIABLES===========================================================
		Integer parameterP = Integer.valueOf(p);
		Integer parameterC = Integer.valueOf(c);
		String jsonData = request.reader.readLines().join("\n")

		//VARIABLES DAO=======================================================
		TestDAO dao =  new TestDAO();
		CustomUserRequestDAO cuDAO = new CustomUserRequestDAO();
		SubIndex subIndex = new SubIndex();
	
		//MAPEO DE SERVICIOS==================================================
		try {
			def found;
			found = subIndex.lista_ListadoDAO.find{ it == url};
			if(found){
				result = subIndex.ListadoDAO_urls(url,parameterP,parameterC,jsonData,context);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_CatalogoBachilleratoDAO.find{it == url};
			if(found){
				result = subIndex.CatalogoBachilleratoDAO_urls(url,parameterP,parameterC,jsonData,context);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_MailGunDAO.find{it == url};
			if(found){
				result = subIndex.MailGunDAO_urls(url,parameterP,parameterC,jsonData,context);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_NotificacionDAO.find{it == url};
			if(found){
				result = subIndex.NotificacionDAO_urls(url,parameterP,parameterC,jsonData,context);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_UsuariosDAO.find{it == url};
			if(found){
				result = subIndex.UsuariosDAO_urls(url,parameterP,parameterC,jsonData,context);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_ConektaDAO.find{it == url};
			if(found){
				result = subIndex.ConektaDAO_urls(url,parameterP,parameterC,jsonData,context);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_HubspotDAO.find{it == url};
			if(found){
				result = subIndex.HubspotDAO_urls(url,parameterP,parameterC,jsonData,context);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_TransferenciasDAO.find{it == url};
			if(found){
				result = subIndex.TransferenciasDAO_urls(url,parameterP,parameterC,jsonData,context);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_ReactivacionDAO.find{it == url};
			if(found){
				result = subIndex.ReactivacionDAO_urls(url,parameterP,parameterC,jsonData,context);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_ResultadosAdmisionDAO.find{it == url};
			if(found){
				result = subIndex.ResultadosAdmisionDAO_urls(url,parameterP,parameterC,jsonData,context);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_SolicitudUsuarioDAO.find{it == url};
			if(found){
				result = subIndex.SolicitudUsuarioDAO_urls(url,parameterP,parameterC,jsonData,context);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_PsicometricoDAO.find{it == url};
			if(found){
				result = subIndex.PsicometricoDAO_urls(url,parameterP,parameterC,jsonData,context);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_CatalogosDAO.find{it == url};
			if(found){
				result = subIndex.CatalogosDAO_urls(url,parameterP,parameterC,jsonData,context);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_CatalogosDAO_Media.find{it == url};
			if(found){
				result = subIndex.CatalogosDAO_urls_Media(url,parameterP,parameterC,jsonData,context);
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_SesionesDAO__Media.find{it == url};
			if(found){
				result = subIndex.SesionesDAO_urls_Media(url,parameterP,parameterC,jsonData,context);
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			found = subIndex.lista_NuevoListadoDAO.find{it == url};
			if(found){
				result = subIndex.NuevoListadoDAO_urls(url,parameterP,parameterC,jsonData,context);
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			}
			
			
			switch(url) {

				case "test":
					result = dao.testFuction(parameterP, parameterC, jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;

				/**************DANIEL CERVANTES********************/
					
					
					
					case "callSesiones":
						result = new MigracionDAO().callSesiones(jsonData, context)
						responseBuilder.withMediaType("application/json")
						if (result.isSuccess()) {
							return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
						}else {
							return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
						}
					break;
					
					
					
					case "getExcelPaseLista":
						result = new ListadoDAO().getExcelPaseLista(parameterP, parameterC, jsonData, context)
						responseBuilder.withMediaType("application/json")
						if (result.isSuccess()) {
							return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
						}else {
							return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
						}
						break;
						
					case "getExcelFileReporteOvBusqueda":
						result = new NuevoListadoDAO().getExcelFileReporteOvBusqueda(jsonData, context)
						responseBuilder.withMediaType("application/json")
						if (result.isSuccess()) {
							return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
						}else {
							return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
						}
						break;
					case "getExcelFileReporteOvBusquedaAvanzada":
						result = new NuevoListadoDAO().getExcelFileReporteOvBusquedaAvanzada(jsonData, context)
						responseBuilder.withMediaType("application/json")
						if (result.isSuccess()) {
							return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
						}else {
							return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
						}
						break;
					
					case "getPdfPaseLista":
					result = new ListadoDAO().getPdfPaseLista(parameterP, parameterC, jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getExcelSesionesCalendarizadas":
					result = new ListadoDAO().getExcelSesionesCalendarizadas(parameterP, parameterC, jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getPdfSesionesCalendarizadas":
					result = new ListadoDAO().getPdfSesionesCalendarizadas(parameterP, parameterC, jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					
					
					case "getCatGestionEscolar":
					result = new CatalogosDAO().getCatGestionEscolar(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					case "getExcelGenerico":
					result = new ListadoDAO().getExcelGenerico(parameterP, parameterC, jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getPdfGenerico":
					result = new ListadoDAO().getPdfGenerico(parameterP, parameterC, jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					
					
					case "activarDesactivarLugarExamen":
					result = new CatalogosDAO().activarDesactivarLugarExamen(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getBitacorasComentarios":
					result = new BitacorasDAO().getBitacorasComentarios(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getComentariosValidacion":
					result = new BitacorasDAO().getComentariosValidacion(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getComentariosValidacion":
					result = new BitacorasDAO().getComentariosValidacion(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					
					case "getRegistrosBecas":
					result = new BecasDAO().getListadoRegistroBecas(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "postExcelBecas":
					result = new BecasDAO().excelOnDeman(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					
					
					case "postGuardarUsuario":
					result = new ImportacionPAADAO().postGuardarUsuario(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "subirDatosBannerEthos":
					result = new ImportacionPAADAO().subirDatosBannerEthos(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "postValidarUsuarioImportacionPAA":
					result = new ImportacionPAADAO().postValidarUsuario(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					
					case "getAspirantesSinPAA":
					result = new ImportacionPAADAO().getAspirantesSinPAA(0,9999,jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "postListaAspirantePAA":
					result = new ImportacionPAADAO().postListaAspirantePAA(0,9999,jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "bitacoraIntegracionEAC":
					result = new ImportacionPAADAO().bitacoraIntegracionEAC(0,9999,jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getCatEscalaEAC":
					result = new ImportacionPAADAO().getCatEscalaEAC(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "postGuardarBitacoraErrores":
					result = new ResultadoComiteDAO().postGuardarBitacoraErroresRC(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "postValidarUsuarioImportacionRC":
					result = new ResultadoComiteDAO().postValidarUsuario(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "postValidarUsuarioCantidadIntento":
					result = new ResultadoComiteDAO().postValidarUsuarioCantidadIntento(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getAspirantesSinRC":
					result = new ResultadoComiteDAO().getAspirantesSinRC(0,9999,jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "postGuardarUsuarioRC":
					result = new ResultadoComiteDAO().postGuardarUsuario(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "postListaAspiranteRC":
					result = new ResultadoComiteDAO().postListaAspiranteRC(0,9999,jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "postExcelAspirantesPAA":
						result = new ImportacionPAADAO().postExcelAspirantesPAA(jsonData, context);
						responseBuilder.withMediaType("application/json")
						if (result.isSuccess()) {
							return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
						}else {
							return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
						}
					break;
					
					case "postEliminarResultado":
					result = new ResultadoComiteDAO().postEliminarResultado(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "postUpdateLicenciaturaPeriodo":
					result = new ResultadoComiteDAO().postUpdateLicenciaturaPeriodo(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					
					
					case "postAsistenciaProceso":
						result = new AvanzeProcesoDAO().postAsistenciaProceso(jsonData,context);
						responseBuilder.withMediaType("application/json")
						if (result.isSuccess()) {
							return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
						}else {
							return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
						}
					break;
					

					case "getCatPeriodo":
					result = new CatalogosDAO().getCatPeriodo(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				/***********************ERIC ROSAS FIN******************/						
				
				case "RegistrarUsuario":
				try{
					UsuariosDAO uDAO = new UsuariosDAO();
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

				
				case "deleteFirma":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					
					assert object instanceof Map;
					CatNotificacionesFirma firma = new CatNotificacionesFirma()
					firma.setPersistenceId(object.persistenceId)
					firma.setCargo(object.cargo)
					firma.setCorreo(object.correo)
					firma.setGrupo(object.grupo)
					firma.setNombreCompleto(object.nombreCompleto)
					firma.setPersistenceVersion(object.persistenceVersion)
					firma.setShowCargo(object.showCargo)
					firma.setShowCorreo(object.showCorreo)
					firma.setShowGrupo(object.showGrupo)
					firma.setShowTelefono(object.showTelefono)
					firma.setShowTitulo(object.showTitulo)
					firma.setTelefono(object.telefono)
					firma.setTitulo(object.titulo)
					firma.setCampus(object.campus)
					firma.setFacebook(object.facebook)
					firma.setTwitter(object.twitter)
					firma.setApellido(object.apellido)
					firma.setBanner(object.banner)
					result = new NotificacionDAO().deleteFirma(firma)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					case "insertFirma":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					
					assert object instanceof Map;
					CatNotificacionesFirma firma = new CatNotificacionesFirma()
					firma.setPersistenceId(object.persistenceId)
					firma.setCargo(object.cargo)
					firma.setCorreo(object.correo)
					firma.setGrupo(object.grupo)
					firma.setNombreCompleto(object.nombreCompleto)
					firma.setPersistenceVersion(object.persistenceVersion)
					firma.setShowCargo(object.showCargo)
					firma.setShowCorreo(object.showCorreo)
					firma.setShowGrupo(object.showGrupo)
					firma.setShowTelefono(object.showTelefono)
					firma.setShowTitulo(object.showTitulo)
					firma.setTelefono(object.telefono)
					firma.setTitulo(object.titulo)
					firma.setCampus(object.campus)
					firma.setFacebook(object.facebook)
					firma.setTwitter(object.twitter)
					firma.setApellido(object.apellido)
					firma.setBanner(object.banner)
					result = new NotificacionDAO().insertFirma(firma)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;

					case "insertBachilleratoLog":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					
					assert object instanceof Map;

					result = new LogDAO().insertBachilleratoLog(object.operation, object.usuarioBanner, object.idBachillerato, object.pais, object.estado, object.ciudad, object.descripcion, object.typeInd, object.postalCode, object.isEliminado)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "insertCatNotificacionesCampus":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					
					assert object instanceof Map;
					CatNotificacionesCampus row =new CatNotificacionesCampus()
					row.setCatcampus_pid(object.catcampus_pid)
					row.setCodigo(object.codigo)
					row.setFooter(object.footer)
					row.setHeader(object.header)
					row.setCopia(object.copia)
					row.setCatnotificacionesfirma_pid(object.catnotificacionesfirma_pid)
					
					result = new NotificacionDAO().insertCatNotificacionesCampus(row)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "insertAzureConfig":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					
					assert object instanceof Map;
					AzureConfig row =new AzureConfig()
					row.setAzureAccountName(object.azureAccountName)
					row.setAzureAccountKey(object.azureAccountKey)
					row.setAzureDefaultEndpointsProtocol(object.azureDefaultEndpointsProtocol)
					row.setBannerToken(object.bannerToken)
					row.setAdminPassword(object.adminPassword)
					row.setBannerMatchPerson(object.bannerMatchPerson)
					
					result = new CatalogosDAO().insertAzureConfig(row)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
				case "insertSesion":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					
					assert object instanceof Map;
					SesionCustom sesion = new SesionCustom()
					try {
						sesion.setBachillerato_pid((object.bachillerato_pid==null)?null:Long.parseLong(object.bachillerato_pid))
					} catch (Exception e) {
						sesion.setBachillerato_pid((object.bachillerato_pid==null)?null:new Long(object.bachillerato_pid))
					}
					sesion.setBorrador(object.borrador)
					sesion.setDescripcion(object.descripcion)
					sesion.setEstado_pid(object.estado_pid)
					sesion.setFecha_inicio(object.fecha_inicio)
					sesion.setIsmedicina(object.ismedicina)
					sesion.setNombre(object.nombre)
					sesion.setPais_pid(object.pais_pid)
					sesion.setPersistenceId(object.persistenceId)
					sesion.setPersistenceVersion(object.persistenceVersion)
					sesion.setPruebas(new ArrayList())
					sesion.setTipo(object.tipo)
					sesion.setCiudad_pid(object.ciudad_pid)
					sesion.setCampus_pid(object.campus_pid)
					sesion.setUltimo_dia_inscripcion(object.ultimo_dia_inscripcion)
					sesion.setIsEliminado(object.isEliminado)
					sesion.setUsuarios_lst_id((object.usuarios_lst_id==null)?"":object.usuarios_lst_id)
					sesion.setEstado_preparatoria(object.estado_preparatoria)
					try {
						sesion.setPeriodo_pid((object.periodo_pid==null)?null:Long.parseLong(object.periodo_pid))
						
					} catch (Exception e) {
						sesion.setPeriodo_pid((object.periodo_pid==null)?null:new Long(object.periodo_pid))
					}
					for (int i =0; i<object.pruebas.size(); i++) {
						def obj = object.pruebas[i]
						PruebaCustom prueba = new PruebaCustom()
						prueba.setAplicacion(obj.aplicacion)
						prueba.setCalle(obj.calle)
						prueba.setCampus_pid(obj.campus_pid)
						prueba.setCattipoprueba_pid(obj.cattipoprueba_pid)
						prueba.setCodigo_postal(obj.codigo_postal)
						prueba.setColonia(obj.colonia)
						prueba.setCupo(obj.cupo)
						prueba.setDuracion(obj.duracion)
						prueba.setEntrada(obj.entrada)
						prueba.setEstado_pid(obj.estado_pid)
						prueba.setIseliminado(obj.iseliminado)
						prueba.setLugar(obj.lugar)
						prueba.setMunicipio(obj.municipio)
						prueba.setNombre(obj.nombre)
						prueba.setNumero_ext(obj.numero_ext)
						prueba.setNumero_int(obj.numero_int)
						prueba.setPais_pid(obj.pais_pid)
						prueba.setPersistenceId(obj.persistenceId)
						prueba.setPersistenceVersion(obj.persistenceVersion)
						prueba.setPsicologos(new ArrayList())
						prueba.setRegistrados(obj.registrados)
						prueba.setSalida(obj.salida)
						prueba.setSesion_pid(obj.sesion_pid)
						prueba.setUltimo_dia_inscripcion(obj.ultimo_dia_inscripcion)
						prueba.setDescripcion(obj.descripcion)
						prueba.setOnline(obj.online==null?false:obj.online)
						for(int j =0; j<obj.psicologos.size(); j++) {
							def psi = obj.psicologos[j]
							ResponsableCustom rc = new ResponsableCustom()
							rc.setPersistenceId(psi.persistenceId)
							rc.setFirstname(psi.firstname)
							rc.setGrupo(psi.grupo)
							try {
								rc.setId(psi.id)
							}catch(Exception e) {
								rc.setId(Long.parseLong(psi.id))
							}
							rc.setLastname(psi.lastname)
							rc.setPersistenceId(psi.persistenceId)
							rc.setIseliminado(psi.iseliminado)
							rc.setLstFechasDisponibles(new ArrayList())
							rc.setLicenciaturas(psi.licenciaturas)
							for(int k=0; k<psi.lstFechasDisponibles.size();k++) {
								def disponible=psi.lstFechasDisponibles[k]
								ResponsableDisponible rd = new ResponsableDisponible()
								rd.setDisponible(disponible.disponible)
								rd.setHorario(disponible.horario)
								rd.setPersistenceVersion(disponible.persistenceVersion)
								rd.setPersistenceId(disponible.persistenceId)
								
								rc.getLstFechasDisponibles().add(rd)
							}
							prueba.getPsicologos().add(rc)
							
						}
						sesion.getPruebas().add(prueba)
					}
					sesion.setTipo(object.tipo)
					result = new SesionesDAO().insertSesion(sesion)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getSesionesCalendario":
				String fecha=request.getParameter "fecha"
				result = new SesionesDAO().getSesionesCalendario(fecha,jsonData)
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				
				case "getSesionesCalendarioAspirante":
				String fecha=request.getParameter "fecha"
				String isMedicina=request.getParameter "isMedicina"
				result = new SesionesDAO().getSesionesCalendarioAspirante(fecha,(isMedicina.equals("true")?true:false),jsonData)
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				case "insertSesionAspirante":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					
					assert object instanceof Map;
					Sesion_Aspirante sesionAspirante = new Sesion_Aspirante()
					sesionAspirante.setResponsabledisponible_pid(object.responsabledisponible_pid)
					sesionAspirante.setSesiones_pid(object.sesiones_pid)
					sesionAspirante.setUsername(object.username)
					result = new SesionesDAO().insertSesionAspirante(sesionAspirante,context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
				case "updateFirma":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					
					assert object instanceof Map;
					CatNotificacionesFirma firma = new CatNotificacionesFirma()
					firma.setPersistenceId(object.persistenceId)
					firma.setCargo(object.cargo)
					firma.setCorreo(object.correo)
					firma.setGrupo(object.grupo)
					firma.setNombreCompleto(object.nombreCompleto)
					firma.setPersistenceVersion(object.persistenceVersion)
					firma.setShowCargo(object.showCargo)
					firma.setShowCorreo(object.showCorreo)
					firma.setShowGrupo(object.showGrupo)
					firma.setShowTelefono(object.showTelefono)
					firma.setShowTitulo(object.showTitulo)
					firma.setTelefono(object.telefono)
					firma.setTitulo(object.titulo)
					firma.setCampus(object.campus)
					firma.setFacebook(object.facebook)
					firma.setTwitter(object.twitter)
					firma.setApellido(object.apellido)
					firma.setBanner(object.banner)
					result = new NotificacionDAO().updateFirma(firma)
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
					case "insertDocumentosTextos":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					
					assert object instanceof Map;
					CatDocumentosTextos row = new CatDocumentosTextos()
					row.setNoSabes(object.noSabes)
					row.setTipsCB(object.tipsCB)
					row.setUrlGuiaExamenCB(object.urlGuiaExamenCB)
					row.setUrlTestVocacional(object.urlTestVocacional)
					row.setCampus_pid(object.campus_pid)
					row.ciudadCarta = object.ciudadCarta
					row.estadoCarta = object.estadoCarta
					row.documentosEntregar = object.documentosEntregar
					row.documentosEntregarExtranjero = object.documentosEntregarExtranjero
					row.notasDocumentos = object.notasDocumentos
					row.parrafoMatematicas1 = object.parrafoMatematicas1
					row.parrafoMatematicas2 = object.parrafoMatematicas2
					row.parrafoMatematicas3 = object.parrafoMatematicas3
					row.parrafoEspanol1 = object.parrafoEspanol1
					row.parrafoEspanol2 = object.parrafoEspanol2
					row.parrafoEspanol3 = object.parrafoEspanol3
					row.directorAdmisiones = object.directorAdmisiones
					row.tituloDirectorAdmisiones =object.tituloDirectorAdmisiones
					row.correoDirectorAdmisiones = object.correoDirectorAdmisiones
					row.telefonoDirectorAdmisiones = object.telefonoDirectorAdmisiones
					row.actividadIngreso1 = object.actividadIngreso1 
					row.actividadIngreso2 = object.actividadIngreso2
					row.costoSGM = object.costoSGM
					row.educacionGarantizada = object.educacionGarantizada
					row.instruccionesPagoBanco = object.instruccionesPagoBanco
					row.cancelarSeguroGastosMedicos = object.cancelarSeguroGastosMedicos
					row.cursoMatematicas1 = object.cursoMatematicas1
					row.cursoMatematicas2 = object.cursoMatematicas2
					row.instruccionesPagoCaja = object.instruccionesPagoCaja
					result = new DocumentosTextosDAO().insertDocumentosTextos(row)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				
				
				
				case "cambiosBannerPreparatoria":
					result = new BannerDAO().cambiosBannerPreparatoria(context,"",jsonData)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;	

				case "updateCatNotificacionesSDAE":
					result = new NotificacionDAO().updateCatNotificacionesSDAE(jsonData)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				break;
				case "updateCatNotificacionesAlt":
					result = new NotificacionDAO().updateCatNotificacionesAlt(jsonData)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				break;
				
				case "updateCatNotificaciones":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					
					assert object instanceof Map;
					CatNotificaciones catNotificaciones = new CatNotificaciones()
					catNotificaciones.anguloImagenFooter = object.anguloImagenFooter
					catNotificaciones.anguloImagenHeader = object.anguloImagenHeader
					catNotificaciones.asunto = object.asunto
					catNotificaciones.caseId = object.caseId
					catNotificaciones.codigo = object.codigo
					catNotificaciones.comentarioLeon = object.comentarioLeon
					catNotificaciones.contenido  = object.contenido
					catNotificaciones.contenidoCorreo = object.contenidoCorreo
					catNotificaciones.contenidoLeonel = object.contenidoLeonel
					catNotificaciones.descripcion = object.descripcion
					catNotificaciones.docGuiaEstudio = object.docGuiaEstudio
					catNotificaciones.enlaceBanner = object.enlaceBanner
					catNotificaciones.enlaceContacto = object.enlaceContacto
					catNotificaciones.enlaceFacebook = object.enlaceFacebook
					catNotificaciones.enlaceFooter = object.enlaceFooter
					catNotificaciones.enlaceInstagram = object.enlaceInstagram
					catNotificaciones.enlaceTwitter = object.enlaceTwitter
					catNotificaciones.nombreImagenFooter = object.nombreImagenFooter
					catNotificaciones.textoFooter  = object.textoFooter
					catNotificaciones.tipoCorreo = object.tipoCorreo
					catNotificaciones.titulo = object.titulo
					catNotificaciones.urlImgFooter = object.urlImgFooter
					catNotificaciones.urlImgHeader = object.urlImgHeader
					result = new NotificacionDAO().updateCatNotificaciones(catNotificaciones)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				/***********************MARIO ICEDO**********************/
				
				case "createOrUpdateTransferirAspirante":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					assert object instanceof Map;
					result = new HubspotDAO().createOrUpdateTransferirAspirante(object.valorcambio, object.valororginal, object.correoaspirante, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				
				
				
					
				case "postExcelINVPIndividual":
					result = new ListadoDAO().postExcelINVPIndividual(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getExcelTransferencias":
					result = new ListadoDAO().getExcelTransferencias(parameterP, parameterC, jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getInformacionReporteSolicitudRespaldo":
					ReactivacionDAO reDAO = new ReactivacionDAO();
					String caseid = request.getParameter "caseid";
					result = reDAO.getInformacionReporteSolicitudRespaldo(caseid, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
								
				
					
				case "postUpdatePeriodoVencido":
					result = new SolicitudUsuarioDAO().postUpdatePeriodoVencido(jsonData)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
				
				
				case "generarReportePerfilAspirante":
					result = new ReportesDAO().generarReportePerfilAspirante(jsonData)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				break;
				
				
				case "updateViewDownloadSolicitud":
					SolicitudUsuarioDAO suDAO = new SolicitudUsuarioDAO();
					String key = request.getParameter "key";
					String intento = request.getParameter "intento";
					Boolean tipoTabla = request.getParameter "tipoTabla";
					result = suDAO.updateViewDownloadSolicitud(parameterP, parameterC, key, intento, tipoTabla, jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				/*******************MARIO ICEDO FIN**********************/
				case "getActiveProcess":
					result = cuDAO.getActiveProcess(context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				case "reAssignTask":
					String task_id = request.getParameter "task_id"
					String fecha = request.getParameter "user_id"
					result = cuDAO.reAssignTask(task_id, user_id, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						 return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
				break;
				case "generarReporte":
				result = new ReportesDAO().generarReporte(jsonData);
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break
				case "generarReporteMetaProfile":
				result = new ReportesDAO().generarReporteMetaProfile(jsonData);
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break
				case "generarReporteResultadosExamenes":
				result = new ReportesDAO().generarReporteResultadosExamenes(jsonData)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break
				case "generarReporteAdmitidosPropedeutico":
				result = new ReportesDAO().generarReporteAdmitidosPropedeutico(jsonData)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				case "generarReporteDatosFamiliares":
				result = new ReportesDAO().generarReporteDatosFamiliares(jsonData)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				case "generarReporteRelacionAspirantes":
				result = new ReportesDAO().generarReporteRelacionAspirantes(jsonData)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				
				
				case "getUsersByPrueba":
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				result = new SesionesDAO().getUsersByPrueba(object.prueba)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				break;
				
				
				
				case "PostUpdateDeleteCatEscalaEAC":
				result = new ImportacionPAADAO().PostUpdateDeleteCatEscalaEAC(jsonData)
				responseBuilder.withMediaType("application/json")
				if (result.isSuccess()) {
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
				}else {
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
				
				break;
				
				
				
				case "insertEmailHubspotConfig":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);

					HubspotConfig row =new HubspotConfig()
					row.setEmailHubspotAutodescripcion(object.emailHubspotAutodescripcion)
					row.setEmailHubspotEnviada(object.emailHubspotEnviada)
					row.setEmailHubspotEsperaResultado(object.emailHubspotEsperaResultado)
					row.setEmailHubspotGenerarCredencial(object.emailHubspotGenerarCredencial)
					row.setEmailHubspotModificar(object.emailHubspotModificar)
					row.setEmailHubspotNoAsistioPruebas(object.emailHubspotNoAsistioPruebas)
					row.setEmailHubspotModificar(object.emailHubspotModificar)
					row.setEmailHubspotPago(object.emailHubspotPago)
					row.setEmailHubspotRegistro(object.emailHubspotRegistro)
					row.setEmailHubspotRestaurarRechazoLRoja(object.emailHubspotRestaurarRechazoLRoja)
					row.setEmailHubspotSeleccionoFechaExamen(object.emailHubspotSeleccionoFechaExamen)
					row.setEmailHubspotUsuarioRegistrado(object.emailHubspotUsuarioRegistrado)
					row.setEmailHubspotValidar(object.emailHubspotValidar)
					row.setEmailHubspotTransferirAspirante(object.emailHubspotTransferirAspirante)
					row.setEmailHubspotRechazoLRoja(object.emailHubspotRechazoLRoja)

					result = new HubspotDAO().insertUpdateEmail(row)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
				break;

				case "createOrUpdateBeca":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					
					result = new HubspotDAO().createOrUpdateBeca(jsonData, context)
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
			result.setError(e.getMessage())
			//e.printStackTrace()
		}
		
		return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
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
