package com.anahuac.rest.api.DAO

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper

class SubIndex {
	private static final Logger LOGGER = LoggerFactory.getLogger(SubIndex.class);

	def lista_ = [];
	public Result _urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		ConektaDAO cDao = new ConektaDAO();
		Result result = new Result();
		try{
			switch(url) {
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}

	def lista_NuevoListadoDAO = ["selectReporteOV","selectReporteOVbysesion","selectUsuariosSesion","selectAspirantesOVGeneral"];
	public Result NuevoListadoDAO_urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		NuevoListadoDAO nlDao = new NuevoListadoDAO();
		Result result = new Result();
		try{
			switch(url) {
				case "selectReporteOV":
					result = nlDao.selectReporteOV(jsonData, context);
					break;
				case "selectReporteOVbysesion" :
					result = nlDao.selectReporteOV_by_SESION(jsonData, context);
					break;
				case "selectUsuariosSesion" :
					result = nlDao.selectUsuariosSesion(jsonData, context);
					break;
				case "selectAspirantesOVGeneral" :
					result = nlDao.selectAspirantesOVGeneral(jsonData, context);
					break;
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}

	def lista_SesionesDAO__Media = ["getSesionesCalendarizadas","getLastFechaExamenByUsername","getSesionesCalendarizadasPasadas", "getSesionesAspirantes","getSesionesAspirantesPasados","insertPaseLista","updatePrepaSolicitudDeAdmision","updatePaseLista","getSesionesAspirantesReporte","getAspirantes3Asistencias","updateAceptado","getSesionesCalendarizadasPsicologoSupervisor","getSesionesPsicologoAdministradorAspirantes","updateAspirantesPruebas","insertAspirantesPruebas","postBitacoraSesiones","postAspiranteSesionesByUsername","getSesionesINVP","getSesionesINVPTabla","getSesionesINVPTablaProcesadas","getUsersByPrueba2","postResultadosINVPIndividuales","insertRespuesta","postSelectAspirantePrueba","postGetIdSesionByIdBanner","postGetIdSesionByCaseId","PostUpdateDeleteCatEscalaINVP","getSesiones"];
	public Result SesionesDAO_urls_Media(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		SesionesDAO sDAO = new SesionesDAO();
		Result result = new Result();
		try{
			switch(url) {
				case "getSesionesCalendarizadas":
					result = sDAO.getSesionesCalendarizadas(jsonData, context)
					break;
					case "getLastFechaExamenByUsername" :
					result = sDAO.getLastFechaExamenByUsername(jsonData)
					break;
					case "getSesionesAspirantes":
					result = sDAO.getSesionesAspirantes(jsonData, context)
					break;
					
					case "getSesionesCalendarizadasPasadas":
					result = sDAO.getSesionesCalendarizadasPasadas(jsonData, context)

					break;
					
					case "getSesionesAspirantesPasados":
					result = sDAO.getSesionesAspirantesPasados(jsonData, context)

					break;
					
					case "insertPaseLista":
					result = sDAO.insertPaseLista(jsonData, context)

					break;

					case "updatePrepaSolicitudDeAdmision":
					result = sDAO.updatePrepaSolicitudDeAdmision(jsonData, context)
					break;

					case "updatePaseLista":
					result = sDAO.updatePaseLista(jsonData, context)

					break;
					case "getSesionesAspirantesReporte":
					result = sDAO.getSesionesCalendarizadasReporte(jsonData, context)

					break;
					
					case "getAspirantes3Asistencias":
					result = sDAO.getAspirantes3Asistencias(jsonData, context)

					break;
					case "updateAceptado":
					result = sDAO.updateAceptado(jsonData, context)

					break;
					case "getSesionesCalendarizadasPsicologoSupervisor":
					result = sDAO.getSesionesCalendarizadasPsicologoSupervisor(jsonData, context)
					break;

					case "getSesionesPsicologoAdministradorAspirantes":
					result = sDAO.getSesionesPsicologoAdministradorAspirantes(jsonData, context)
					break;

					case "updateAspirantesPruebas":
					result = sDAO.updateAspirantesPruebas(jsonData, context)
					break;
					
					case "insertAspirantesPruebas":
					result = sDAO.insertAspirantesPruebas(jsonData, context)
					break;
					
					case "postBitacoraSesiones":
						result = sDAO.postBitacoraSesiones(jsonData,context);
					break;
					
					case "postAspiranteSesionesByUsername":
						result = sDAO.postAspiranteSesionesByUsername(jsonData,context);
					break;
					
					case "getSesionesINVP":
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				result = sDAO.getSesionesINVP(object.sesion, object.fecha, object.uni, object.id)
				
				
				break;
				case "getSesionesINVPTabla":
				result = sDAO.getSesionesINVPTabla(jsonData,context)
				
				
				break;
				case "getSesionesINVPTablaProcesadas":
				result = sDAO.getSesionesINVPTablaProcesadas(jsonData,context)
				
				
				break;
				case "getUsersByPrueba2":
				result = sDAO.getUsersByPrueba2(jsonData,context)
				
				
				break;
				
				case "postResultadosINVPIndividuales":
				result = sDAO.postResultadosINVPIndividuales(jsonData,context)
				
				
				break;
				
				case "insertRespuesta":
				result = sDAO.insertRespuesta(jsonData)
				break;
				
				case "postSelectAspirantePrueba":
				break;
				
				case "postGetIdSesionByIdBanner":
				result = sDAO.postGetIdSesionByIdBanner(jsonData)
				break;
				
				case "postGetIdSesionByCaseId":
				result = sDAO.postGetIdSesionByCaseId(jsonData)
				break;
				
				case "PostUpdateDeleteCatEscalaINVP":
				result = sDAO.PostUpdateDeleteCatEscalaINVP(jsonData)
				break;

				case "getSesiones":
				result = sDAO.getSesiones(jsonData)
				break;
				
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}

	def lista_CatalogosDAO_Media = ["getCatEscolaridad","getCatSexo","getCatParentesco","getCatNacionalidad","getCatDescuentos","getCatGenerico","getCatParentescoA","getCatEstadoG"];
	public Result CatalogosDAO_urls_Media(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		CatalogosDAO cDAO = new CatalogosDAO();
		Result result = new Result();
		try{
			switch(url) {
				
					case "getCatEscolaridad":
					result = cDAO.getCatEscolaridad(jsonData, context)
					
					break;

					case "getCatSexo":
					result = cDAO.getCatSexo(jsonData, context)
					
					break;
					
					case "getCatParentesco":
					result = cDAO.getCatParentesco(jsonData, context)
					
					break;
					
					case "getCatNacionalidad":
					result = cDAO.getCatNacionalidad(jsonData)
					
					break;
					
					case "getCatDescuentos":
					result = cDAO.getCatDescuentos(jsonData, context)
					
					break;
					
					case "getCatGenerico":
					result = cDAO.getCatGenerico(jsonData, context)
					
					break;
					
					case "getCatParentescoA":
					result = cDAO.getCatParentescoA(jsonData, context)
					
					break;
					
					case "getCatEstadoG":
					result = cDAO.getCatEstadoG(jsonData, context)
					
					break;

					

			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}


	def lista_CatalogosDAO = ["getCatCampus","getCatPais","getCatCiudad","getCodigoPostalRepetido","getCatPropedeuticoGral","getCatPropedeuticoRelacionTipo","getCatCiudadExcel","getCatCiudadPdf","getCatEstados","getCatCiudad","getCatBachillerato","getCatFiltradoCalalogosAdMisiones","getCatNacionalidadNew","getCatPaisExcel"];
	public Result CatalogosDAO_urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		CatalogosDAO cDAO = new CatalogosDAO();
		Result result = new Result();
		try{
			switch(url) {
				case "getCatCampus":
					result = cDAO.getCatCampus(jsonData, context)
					break;

					case "getCatPais":
					result = cDAO.getCatPais(jsonData, context)

					break;
					case "getCatCiudad":
					result = cDAO.getCatCiudad(jsonData, context)

					break;
					case "getCodigoPostalRepetido":
					result = cDAO.getCodigoPostalRepetido(jsonData, context)

					break;
					case "getCatPropedeuticoGral":
					result = cDAO.getCatPropedeuticoGral(jsonData, context)

					break;
					case "getCatPropedeuticoRelacionTipo":
					result = cDAO.getCatPropedeuticoRelacionTipo(jsonData, context)

					break;
					case "getCatCiudadExcel":
					result = cDAO.getCatCiudadExcel(jsonData, context)

					break;
					case "getCatCiudadPdf":
					result = cDAO.getCatCiudadPdf(jsonData, context)

					break;
					case "getCatEstados":
					result = cDAO.getCatEstados(jsonData, context)

					break;
					case "getCatCiudad":
					result = cDAO.getCatCiudad(jsonData, context)

					break;
					case "getCatBachillerato":
					result = cDAO.getCatBachillerato(jsonData, context)

					break;
					case "getCatFiltradoCalalogosAdMisiones":
					result = cDAO.getCatFiltradoCalalogosAdMisiones(jsonData, context)
					break;
					case "getCatNacionalidadNew":
					result = cDAO.getCatNacionalidadNew(jsonData);
					break;

					case "getCatPaisExcel":
					result = cDAO.getCatPaisExcel(jsonData, context)
					break;
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}


	def lista_PsicometricoDAO = ["insertUpdatePsicometrico","insertUpdatePsicometricoV2","selectAspirantesPsicometrico","selectAspirantesConPsicometrico","selectAspirantesConPsicometricoRechazados"];
	public Result PsicometricoDAO_urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		PsicometricoDAO psiDAO = new PsicometricoDAO();
		Result result = new Result();
		try{
			switch(url) {
				case "insertUpdatePsicometrico":
					result = psiDAO.insertUpdatePsicometrico(parameterP, parameterC, jsonData, context)
					break;
				case "insertUpdatePsicometricoV2":
					result = psiDAO.insertUpdatePsicometricoV2(parameterP, parameterC, jsonData, context)
					break;
				case "selectAspirantesPsicometrico":
					result = psiDAO.selectAspirantesPsicometrico(jsonData, context)
					break;
	
				case "selectAspirantesConPsicometrico":
					result = psiDAO.selectAspirantesConPsicometrico(parameterP, parameterC, jsonData, context)
					break;
				case "selectAspirantesConPsicometricoRechazados":
					result = psiDAO.selectAspirantesConPsicometricoRechazados(parameterP, parameterC, jsonData, context)
					break;
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}

	def lista_SolicitudUsuarioDAO = ["getPadresTutorVencido","getPadreVencido","getMadreVencido","getContactoEmergencia","getHermanosVencidos","getInformacionEscolarVencidos","getUniviersidadesVencidos","getIdiomaVencidos","getTerapiaVencidos","getGrupoSocialVencidos","getParienteEgresadoVencidos"];
	public Result SolicitudUsuarioDAO_urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		SolicitudUsuarioDAO suDAO = new SolicitudUsuarioDAO();
		Result result = new Result();
		try{
			switch(url) {
				case "getPadresTutorVencido":
					result = suDAO.getPadresTutorVencido(parameterP, parameterC, jsonData, context)

					break;
					case "getPadreVencido":
					result = suDAO.getPadreVencido(parameterP, parameterC, jsonData, context)

					break;
				case "getMadreVencido":
					result = suDAO.getMadreVencido(parameterP, parameterC, jsonData, context)

					break;
				case "getContactoEmergencia":
					result = suDAO.getContactoEmergencia(parameterP, parameterC, jsonData, context)

					break;
				case "getHermanosVencidos":
					result = suDAO.getHermanosVencidos(parameterP, parameterC, jsonData, context)

					break;
				case "getInformacionEscolarVencidos":
					result = suDAO.getInformacionEscolarVencidos(parameterP, parameterC, jsonData, context)

					break;
				case "getUniviersidadesVencidos":
					result = suDAO.getUniviersidadesVencidos(parameterP, parameterC, jsonData, context)

					break;
					case "getIdiomaVencidos":
					result = suDAO.getIdiomaVencidos(parameterP, parameterC, jsonData, context)

					break;
				case "getTerapiaVencidos":
					result = suDAO.getTerapiaVencidos(parameterP, parameterC, jsonData, context)

					break;
				case "getGrupoSocialVencidos":
					result = suDAO.getGrupoSocialVencidos(parameterP, parameterC, jsonData, context)

					break;
				case "getParienteEgresadoVencidos":
					result = suDAO.getParienteEgresadoVencidos(parameterP, parameterC, jsonData, context)

					break;
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}

	def lista_ResultadosAdmisionDAO = ["getInformacionResultado","clearInfoCartaTemporal","selectInfoCartaTemporal","selectInfoCartaTemporalNoResultados","enviarCartas","selectConsultaDeResultados","seleccionarCarta","selectConsultaDeResultadosManual"];
	public Result ResultadosAdmisionDAO_urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		ResultadosAdmisionDAO rDAO = new ResultadosAdmisionDAO();
		Result result = new Result();
		try{
			switch(url) {
				case "getInformacionResultado":
					result = rDAO.obtieneDatosDelB64(parameterP, parameterC, jsonData, context);

					break;
				case "clearInfoCartaTemporal":
					result = rDAO.clearInfoCartaTemporal();

					break;
				case "selectInfoCartaTemporal":
					result = rDAO.selectInfoCartaTemporal(parameterP, parameterC, jsonData, context);

					break;
				case "selectInfoCartaTemporalNoResultados":
					result = rDAO.selectInfoCartaTemporalNoResultados(parameterP, parameterC, jsonData, context);

					break;
				case "enviarCartas":
					result = rDAO.enviarCartas(parameterP, parameterC, jsonData, context);

					break;
				case "selectConsultaDeResultados":
					result = rDAO.selectConsultaDeResultados(parameterP, parameterC, jsonData, context);

					break;
				case "seleccionarCarta":
					result = rDAO.seleccionarCarta(parameterP, parameterC, jsonData);

					break;	
					case "selectConsultaDeResultadosManual":
					result = rDAO.selectConsultaDeResultadosManual(parameterP, parameterC, jsonData, context);

					break;
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}

	def lista_ReactivacionDAO = ["getUsuariosRechazadosComite","reactivarAspirante","nuevoCasoSolicitud","RealizarRespaldo","selectAspirantesRechazadosRespaldo","guardarTutorIntento","respaldoUsuario"];
	public Result ReactivacionDAO_urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		ReactivacionDAO reDAO = new ReactivacionDAO();
		Result result = new Result();
		try{
			switch(url) {
				case "getUsuariosRechazadosComite":
					result = reDAO.getUsuariosRechazadosComite(parameterP, parameterC, jsonData, context)
					break;
				case "reactivarAspirante":
					result = reDAO.reactivarAspiranteV2(parameterP, parameterC, jsonData, context)

					break;
					
				case "nuevoCasoSolicitud":
					result = reDAO.nuevoCasoSolicitud(jsonData, context)

					break;
					
				case "RealizarRespaldo":
					result = reDAO.RealizarRespaldo(jsonData, context);

					break;
					
				case "selectAspirantesRechazadosRespaldo":
					result = reDAO.selectAspirantesRechazadosRespaldo(jsonData, context)

					break;
					
				case "guardarTutorIntento":
					result = reDAO.guardarTutorIntento(jsonData, context)

					break;
					
				case "respaldoUsuario":
					result = reDAO.respaldoUsuario( jsonData, context)
					break;
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}

	def lista_TransferenciasDAO = ["reagendarExamen","getUsuariosTransferencia","transferirAspirante","selectBitacoraTransferencias"];
	public Result TransferenciasDAO_urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		TransferenciasDAO tDAO = new TransferenciasDAO();
		Result result = new Result();
		try{
			switch(url) {
				case "reagendarExamen":
					result = tDAO.reagendarExamen(parameterP, parameterC, jsonData, context)
					break;
				case "getUsuariosTransferencia":
					result = tDAO.getUsuariosTransferencia(parameterP, parameterC, jsonData, context)
					break;
				case "transferirAspirante":
					result = tDAO.transferirAspirante(parameterP, parameterC, jsonData, context)
					break;
				case "selectBitacoraTransferencias":
					result = tDAO.selectBitacoraTransferencias(parameterP, parameterC, jsonData, context)
					break;
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}

	def lista_HubspotDAO = ["getApiCrispChat","createOrUpdateRegistro","createOrUpdateEnviada","createOrUpdateModificar","createOrUpdateUsuariosRegistrados","createOrUpdateValidar","createOrUpdateRechazoLRoja","createOrUpdateRestaurarRechazoLRoja","createOrUpdatePago","createOrUpdateAutodescripcion","createOrUpdateSeleccionoFechaExamen","createOrUpdateGenerarCredencial","createOrUpdateEsperaResultado","createOrUpdateNoAsistioPruebas"];
	public Result HubspotDAO_urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		HubspotDAO hDAO = new HubspotDAO();
		Result result = new Result();
		try{
			switch(url) {
				case "getApiCrispChat":
					result = hDAO.getApiCrispChat();
					break;
				case "createOrUpdateRegistro":
					result = hDAO.createOrUpdateRegistro(parameterP, parameterC, jsonData, context);
					break;
				case "createOrUpdateEnviada":
					result = hDAO.createOrUpdateEnviada(parameterP, parameterC, jsonData, context)
					break;
				case "createOrUpdateModificar":
						result = hDAO.createOrUpdateModificar(parameterP, parameterC, jsonData, context)
						break;
				case "createOrUpdateUsuariosRegistrados":
						result = hDAO.createOrUpdateUsuariosRegistrados(parameterP, parameterC, jsonData, context)
						break;
				case "createOrUpdateValidar":
					result = hDAO.createOrUpdateValidar(parameterP, parameterC, jsonData, context);
					break;
				case "createOrUpdateRechazoLRoja":
					result = hDAO.createOrUpdateRechazoLRoja(parameterP, parameterC, jsonData, context)
					break;
					case "createOrUpdateRestaurarRechazoLRoja":
					result = hDAO.createOrUpdateRestaurarRechazoLRoja(parameterP, parameterC, jsonData, context);
					break;
				case "createOrUpdatePago":
					result = hDAO.createOrUpdatePago(parameterP, parameterC, jsonData, context);
					break;
				case "createOrUpdateAutodescripcion":
					result = hDAO.createOrUpdateAutodescripcion(parameterP, parameterC, jsonData, context);
					break;
				case "createOrUpdateSeleccionoFechaExamen":
					result = hDAO.createOrUpdateSeleccionoFechaExamen(parameterP, parameterC, jsonData, context);
				break;
				case "createOrUpdateGenerarCredencial":
					result = hDAO.createOrUpdateGenerarCredencial(parameterP, parameterC, jsonData, context);
				break
				case "createOrUpdateEsperaResultado":
					result = hDAO.createOrUpdateEsperaResultado(parameterP, parameterC, jsonData, context);
				break
				case "createOrUpdateNoAsistioPruebas":
					result = hDAO.createOrUpdateNoAsistioPruebas(parameterP, parameterC, jsonData, context);
				break;
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}

	def lista_ConektaDAO = ["selectBitacoraPago","pagoOxxoCash","pagoTarjeta","pagoSPEI","pagoOxxoCashBecas","pagoTarjetaBecas","pagoSPEIBecas","getOrderPaymentMethod","getOrderDetails","getConektaPublicKey","ejecutarEsperarPago","ejecutarEsperarPagoBecas"];
	
	public Result ConektaDAO_urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		ConektaDAO cDao = new ConektaDAO();
		Result result = new Result();
		try{
			switch(url) {
				case "selectBitacoraPago":
					result = cDao.selectBitacoraPago(parameterP, parameterC, jsonData, context);
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
				case "pagoOxxoCashBecas":
					LOGGER.error "pago oxxo"
					result = cDao.pagoOxxoCashBecas(parameterP, parameterC, jsonData, context);
					break;
				 case "pagoTarjetaBecas":
					LOGGER.error "pago tarjeta"
					result = cDao.pagoTarjetaBecas(parameterP, parameterC, jsonData, context);
					break;
				 case "pagoSPEIBecas":
					LOGGER.error "pago spei"
					result = cDao.pagoSPEIBecas(parameterP, parameterC, jsonData, context);
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
				case "ejecutarEsperarPagoBecas":
					result = cDao.ejecutarEsperarPagoBecas(parameterP, parameterC, jsonData, context);
					break;

			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}
	
	def lista_ListadoDAO = ["getNuevasSolicitudes","getExcelFile","getPdfFile","getExcelFileCatalogo","getExcelBachilleratos","getPdfFileCatalogo","getExcelFileCatalogosAD","getPdfFileCatalogoAD","getAspirantesProceso","selectAspirantesEnproceso","selectAspirantesEnRed","selectAspirantesMigrados","selectAspirantesEnprocesoFechas","selectSolicitudesEnProceso","getAspirantesByStatus","getDocumentoTest","getAspirantesByStatusTemprano"];
	public Result ListadoDAO_urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		ListadoDAO lDao = new ListadoDAO();
		Result result = new Result();
		try{
			switch(url) {
				case "getNuevasSolicitudes":
						result = lDao.getNuevasSolicitudes(parameterP, parameterC, jsonData, context);
						break;
				case "getExcelFile":
					result = lDao.getExcelFile(parameterP, parameterC, jsonData, context);
					break;
				case "getPdfFile":
					result = lDao.getPdfFile(parameterP, parameterC, jsonData, context);
					
					break;
				case "getExcelFileCatalogo":
					result = lDao.getExcelFileCatalogo(parameterP, parameterC, jsonData, context);
					
					break;
				case "getExcelBachilleratos":
					result = lDao.getExcelBachilleratos(jsonData, context);
					
					break;
				case "getPdfFileCatalogo":
					result = lDao.getPdfFileCatalogo(parameterP, parameterC, jsonData, context);
					
					break;
				case "getExcelFileCatalogosAD":
					result = lDao.getExcelFileCatalogosAD(parameterP, parameterC, jsonData, context);
					
					break;
				case "getPdfFileCatalogoAD":
					result = lDao.getPdfFileCatalogoAD(parameterP, parameterC, jsonData, context);
					
					break;
				case "getAspirantesProceso":
					result = lDao.getAspirantesProceso(parameterP, parameterC, jsonData, context);
					
					break;
				case "selectAspirantesEnproceso":
					result = lDao.selectAspirantesEnproceso(parameterP, parameterC, jsonData, context);
					
					break;
				case "selectAspirantesEnRed":
					result = lDao.selectAspirantesEnRed(parameterP, parameterC, jsonData, context);
					
					break;
				case "selectAspirantesMigrados":
					result = lDao.selectAspirantesMigrados(parameterP, parameterC, jsonData, context);
					
					break;
				case "selectAspirantesEnprocesoFechas":
					result = lDao.selectAspirantesEnprocesoFechas(parameterP, parameterC, jsonData, context);
					
					break;
				case "selectSolicitudesEnProceso":
					result = lDao.selectSolicitudesEnProceso(parameterP, parameterC, jsonData, context);
					
					break;
				case "getAspirantesByStatus":
					result = lDao.getAspirantesByStatus(parameterP, parameterC, jsonData, context);
					
					break;
				case "getDocumentoTest":
					result = lDao.getDocumentoTest(parameterP, parameterC, jsonData, context);
					
					break;
				case "getAspirantesByStatusTemprano":
					result = lDao.getAspirantesByStatusTemprano(parameterP, parameterC, jsonData, context);
					
					break;
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}

	def lista_UsuariosDAO = ["updateInformacionAspirante","recuparaPassword","recuperarPasswordAdministrativo","enviarTarea","recoveryData","getUsuariosRegistrados","updateUsuarioRegistrado","selectAspirantesEnLaRed","B64File","selectAspirantesSmartCampus","updateCorreoElectronico","getUsuariosConSolicitudVencida","getUsuariosConSolicitudAbandonada","getUserFoto"]
	public Result UsuariosDAO_urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		UsuariosDAO uDAO = new UsuariosDAO();
		Result result = new Result();
		try{
			switch(url) {
				case "updateInformacionAspirante":
					result = uDAO.updateInformacionAspirante(parameterP, parameterC, jsonData, context);
				break;
				case "recuparaPassword":
					result =  uDAO.postRecuperarPassword(parameterP, parameterC, jsonData, context);
					break;
				case "recuperarPasswordAdministrativo":
					result =  uDAO.postRecuperarPasswordAdministrativo(parameterP, parameterC, jsonData, context);
					break;
				case "enviarTarea":
					result = uDAO.enviarTarea(parameterP, parameterC, jsonData, context);
					break;
				case "recoveryData":
					result = uDAO.recoveryData(jsonData);
					break;
				case "getUsuariosRegistrados":
					result = uDAO.getUsuariosRegistrados(parameterP, parameterC, jsonData, context)
					break;
				case "updateUsuarioRegistrado":
					result = uDAO.updateUsuarioRegistrado(parameterP, parameterC, jsonData, context)
					break;
				case "selectAspirantesEnLaRed":
					result = uDAO.selectAspirantesEnLaRed(parameterP, parameterC, jsonData, context)
					break;
				case "B64File":
					result = uDAO.getB64File(jsonData);
					break;
				case "selectAspirantesSmartCampus":
					result = uDAO.selectAspirantesSmartCampus(jsonData, context)
					break;
				case "updateCorreoElectronico":
					result = uDAO.updateCorreoElectronico(parameterP, parameterC, jsonData, context)
					break;
				case "getUsuariosConSolicitudVencida":
					result = uDAO.getUsuariosConSolicitudVencida(parameterP, parameterC, jsonData, context)
					break;
				case "getUsuariosConSolicitudAbandonada":
					result = uDAO.getUsuariosConSolicitudAbandonada(parameterP, parameterC, jsonData, context)
					break;
				case "getUserFoto":
					result = uDAO.getUserFoto(context,jsonData)
					break;
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}

	def lista_NotificacionDAO = ["generateHtml","generateHtmlSDAE","generateHtmlINVP","getTestUpdate","insertLicenciatura","insertLicenciaturaBonita","simpleSelect","simpleSelectBonita"];
	public Result NotificacionDAO_urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		NotificacionDAO nDAO = new NotificacionDAO();
		Result result = new Result();
		try{
			switch(url) {
				case "generateHtml":
					result = nDAO.generateHtml(parameterP, parameterC, jsonData, context);
					break;
				case "generateHtmlSDAE":
					result = nDAO.generateHtmlSDAE(parameterP, parameterC, jsonData, context);
					break;
				case "generateHtmlINVP":
					result = nDAO.generateHtmlINVP(parameterP, parameterC, jsonData, context);
					break;
				case "getTestUpdate":
					result = nDAO.getDocumentoTest(parameterP, parameterC, jsonData, context);
					break;
				case "insertLicenciatura":
					result = nDAO.insertLicenciatura(parameterP, parameterC, jsonData, context)
					break;
				case "insertLicenciaturaBonita":
					result = nDAO.insertLicenciaturaBonita(parameterP, parameterC, jsonData, context)
					break;
				case "simpleSelect":
					result = nDAO.simpleSelect(parameterP, parameterC, jsonData, context)
					break;
				case "simpleSelectBonita":
					result = nDAO.simpleSelectBonita(parameterP, parameterC, jsonData, context)
					break;
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}


	def lista_MailGunDAO = ["sendEmail","sendEmailPlantilla"];
	public Result MailGunDAO_urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		MailGunDAO mgDAO = new MailGunDAO();
		Result result = new Result();
		try{
			switch(url) {
				case "sendEmail":
					result = mgDAO.sendEmail(parameterP, parameterC, jsonData, context);
					break;

				case "sendEmailPlantilla":
					def jsonSlurper = new JsonSlurper();
					def object = jsonSlurper.parseText(jsonData);
					result = mgDAO.sendEmailPlantilla(object.correo,object.asunto,object.body,"",object.campus,context)	
					break;
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}


	def lista_CatalogoBachilleratoDAO = ["insertBachillerato","insertCatBitacoraComentario","updateBachillerato","updatePerteneceRed","getBachillerato"];
	public Result CatalogoBachilleratoDAO_urls(String url,Integer parameterP,Integer parameterC,String jsonData,RestAPIContext context) {
		CatalogoBachilleratoDAO bDao = new CatalogoBachilleratoDAO()
		Result result = new Result();
		try{
			switch(url) {
				case "insertBachillerato":
					result = bDao.insert(parameterP, parameterC, jsonData, context)
					
					break;
				case "insertCatBitacoraComentario":
					result = bDao.insertCatBitacoraComentario(jsonData, context)
					
					break;
				case "updateBachillerato":
					result = bDao.update(parameterP, parameterC, jsonData, context)
					
					break;
					case "updatePerteneceRed":
					result = bDao.updatePerteneceRed(parameterP, parameterC, jsonData, context)
					
					break;
				case "getBachillerato":
					result = bDao.get(parameterP, parameterC, jsonData, context)
					
					break;
			}
		}catch (Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
		}
		return result;
	}

}
