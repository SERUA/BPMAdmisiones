package com.anahuac.rest.api;

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatNotificacionesFirma
import com.anahuac.rest.api.DAO.CatalogoBachilleratoDAO
import com.anahuac.rest.api.DAO.ConektaDAO
import com.anahuac.rest.api.DAO.HubspotDAO
import com.anahuac.rest.api.DAO.ListadoDAO
import com.anahuac.rest.api.DAO.MailGunDAO
import com.anahuac.rest.api.DAO.NotificacionDAO
import com.anahuac.rest.api.DAO.SesionesDAO
import com.anahuac.rest.api.DAO.TestDAO
import com.anahuac.rest.api.DAO.TransferenciasDAO
import com.anahuac.rest.api.DAO.CatalogosDAO
import com.anahuac.rest.api.DAO.UsuariosDAO
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.PruebaCustom
import com.anahuac.rest.api.Entity.Custom.ResponsableCustom
import com.anahuac.rest.api.Entity.Custom.SesionCustom
import com.anahuac.rest.api.Entity.db.ResponsableDisponible
import com.anahuac.rest.api.Entity.db.Sesion_Aspirante
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
		HubspotDAO hDAO = new HubspotDAO();
		TransferenciasDAO tDAO = new TransferenciasDAO();
		//MAPEO DE SERVICIOS==================================================
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
				case "getNuevasSolicitudes":
					result = lDao.getNuevasSolicitudes(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getExcelFile":
					result = lDao.getExcelFile(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getPdfFile":
					result = lDao.getPdfFile(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getExcelFileCatalogo":
					result = lDao.getExcelFileCatalogo(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getPdfFileCatalogo":
					result = lDao.getPdfFileCatalogo(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					case "getExcelFileCatalogosAD":
					result = lDao.getExcelFileCatalogosAD(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					case "getPdfFileCatalogoAD":
					result = lDao.getPdfFileCatalogoAD(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getAspirantesProceso":
					result = lDao.getAspirantesProceso(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "selectAspirantesEnproceso":
					result = lDao.selectAspirantesEnproceso(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				/**************DANIEL CERVANTES********************/
					case "getCatCampus":
					result = new CatalogosDAO().getCatCampus(jsonData, context)
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
					case "getCatEstados":
					result = new CatalogosDAO().getCatEstados(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					case "getCatCiudad":
					result = new CatalogosDAO().getCatCiudad(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					case "getCatBachillerato":
					result = new CatalogosDAO().getCatBachillerato(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					case "getCatFiltradoCalalogosAdMisiones":
					result = new CatalogosDAO().getCatFiltradoCalalogosAdMisiones(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
				/**************DANIEL CERVANTES FIN****************/
				/**************JESUS OSUNA*************************/
					case "getCatEscolaridad":
					result = new CatalogosDAO().getCatEscolaridad(jsonData, context)
					responseBuilder.withMediaType("application/json")

					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;		
					case "getCatSexo":
					result = new CatalogosDAO().getCatSexo(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getCatParentesco":
					result = new CatalogosDAO().getCatParentesco(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getCatNacionalidad":
					result = new CatalogosDAO().getCatNacionalidad(jsonData)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getCatDescuentos":
					result = new CatalogosDAO().getCatDescuentos(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getCatGenerico":
					result = new CatalogosDAO().getCatGenerico(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					
					case "getSesionesCalendarizadas":
					result = new SesionesDAO().getSesionesCalendarizadas(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getSesionesAspirantes":
					result = new SesionesDAO().getSesionesAspirantes(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getSesionesCalendarizadasPasadas":
					result = new SesionesDAO().getSesionesCalendarizadasPasadas(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "getSesionesAspirantesPasados":
					result = new SesionesDAO().getSesionesAspirantesPasados(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "insertPaseLista":
					result = new SesionesDAO().insertPaseLista(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
					case "updatePaseLista":
					result = new SesionesDAO().updatePaseLista(jsonData, context)
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
				/**************JESUS OSUNA FIN*********************/
				/***********************ERIC ROSAS**********************/
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
				case "selectSolicitudesEnProceso":
					result = lDao.selectSolicitudesEnProceso(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getAspirantesByStatus":
					result = lDao.getAspirantesByStatus(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getDocumentoTest":
					result = lDao.getDocumentoTest(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "pagoOxxoCash":
					LOGGER.error "pago oxxo"
					result = cDao.pagoOxxoCash(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
		  		case "pagoTarjeta":
					LOGGER.error "pago tarjeta"
					result = cDao.pagoTarjeta(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
		  		case "pagoSPEI":
					LOGGER.error "pago spei"
					result = cDao.pagoSPEI(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "RegistrarUsuario":
					result =  uDAO.postRegistrarUsuario(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getOrderPaymentMethod":
					result = cDao.getOrderPaymentMethod(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getOrderDetails":
					result =  cDao.getOrderDetails(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getConektaPublicKey":
					result = cDao.getConektaPublicKey(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "ejecutarEsperarPago":
					result = cDao.ejecutarEsperarPago(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
					
				/*case "encode":
					result =  aDAO.base64Encode(parameterP, parameterC, jsonData, context);
					break;
				case "decode":
					result =  aDAO.base64Decode(parameterP, parameterC, jsonData, context);
					break;*/
				case "getAspirantesByStatusTemprano":
					result = lDao.getAspirantesByStatusTemprano(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "recuparaPassword":
					result =  uDAO.postRecuperarPassword(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "sendEmail":
					result = mgDAO.sendEmail(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "generateHtml":
					result = nDAO.generateHtml(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getTestUpdate":
					result = nDAO.getDocumentoTest(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertLicenciatura":
					result = nDAO.insertLicenciatura(parameterP, parameterC, jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertLicenciaturaBonita":
					result = nDAO.insertLicenciaturaBonita(parameterP, parameterC, jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "simpleSelect":
					result = nDAO.simpleSelect(parameterP, parameterC, jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "simpleSelectBonita":
					result = nDAO.simpleSelectBonita(parameterP, parameterC, jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertBachillerato":
					result = bDao.insert(parameterP, parameterC, jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertCatBitacoraComentario":
					result = bDao.insertCatBitacoraComentario(jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "updateBachillerato":
					result = bDao.update(parameterP, parameterC, jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getBachillerato":
					result = bDao.get(parameterP, parameterC, jsonData, context)
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
					result = new NotificacionDAO().insertFirma(firma)
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
					sesion.setBachillerato_pid((object.bachillerato_pid==null)?null:object.bachillerato_pid)
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
				result = new SesionesDAO().getSesionesCalendarioAspirante(fecha,jsonData)
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
					result = new SesionesDAO().insertSesionAspirante(sesionAspirante)
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
					result = new NotificacionDAO().updateFirma(firma)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "enviarTarea":
					result = uDAO.enviarTarea(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "createOrUpdateRegistro":
					result = hDAO.createOrUpdateRegistro(parameterP, parameterC, jsonData, context);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "createOrUpdateEnviada":
					result = hDAO.createOrUpdateEnviada(parameterP, parameterC, jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getUsuariosTransferencia":
					result = tDAO.getUsuariosTransferencia(parameterP, parameterC, jsonData, context)
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
