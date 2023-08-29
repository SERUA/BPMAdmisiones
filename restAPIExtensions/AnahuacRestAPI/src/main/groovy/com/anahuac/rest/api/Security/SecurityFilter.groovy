package com.anahuac.rest.api.Security


import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion

import com.bonitasoft.web.extension.rest.RestAPIContext

class SecurityFilter {
	public Boolean bonitaRolFilter(RestAPIContext context,String roleName) {
		Boolean valid = false;
		List<UserMembership> uMemberships=context.apiClient.identityAPI.getUserMemberships(context.apiSession.userId, 0, 100, UserMembershipCriterion.ROLE_NAME_ASC);
		uMemberships.each{
			it ->
			if(it.roleName.toLowerCase().equals(roleName.toLowerCase()) || it.roleName.equals("ADMINISTRADOR") || it.roleName.equals("TI SERUA") || it.roleName.equals("SERUA") || it.roleName.equals("TI CAMPUS") || it.roleName.equals("PSICOLOGO") || it.roleName.equals("PSICOLOGO SUPERVISOR") || it.roleName.equals("PASE DE LISTA")) {
				valid=true
			}
		}
		
		return valid;
	} 
	
	public Boolean allowedUrl(RestAPIContext context, String url) {
		
			Boolean allow= false;
			switch(url){
				case "habilitarUsuario":
					allow=bonitaRolFilter(context,"ADMISIONES")
					if(allow){break;}
					allow=bonitaRolFilter(context,"EXTERIOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
				break;
				case "enviarTareaRest":
					allow=bonitaRolFilter(context,"ADMISIONES")
					if(allow){break;}
					allow=bonitaRolFilter(context,"EXTERIOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"GUEST")
					if(allow){break;}
				break; 
				case "getIdiomaByUsername":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getConektaPublicKeyV2":
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
				break;
				case "createToken":
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){
						break;
					}
				break;
				case "getDescuentosCiudadBachillerato":
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				break;
				case "getDescuentosCiudadBachilleratoById":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "correcionNuevoCampoSesion":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getDescuentosCampana":
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				break;
				case "getCatNotificacionesFirma":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getCartasNotificaciones":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getCartasNotificacionesByEstatus":
					allow = bonitaRolFilter(context,"ADMISIONES");
					if(allow){
						break;
					}
				break;
				case "getCartasNotificacionesSDAE":
					allow=bonitaRolFilter(context,"ADMISIONES")
					if(allow){
						break;
					}
				break;
				case "getCatTitulo":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getDatosUsername":allow=bonitaRolFilter(context,"EXTERIOR")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getBusinessAppMenu":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Becas")
				if(allow){break;}
				break;
				case "getMenuAdministrativo":
					allow=bonitaRolFilter(context,"ADMISIONES")
					if(allow){break;}
					allow=bonitaRolFilter(context,"SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Finanzas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Comite de Finanzas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Chat")
					if(allow){break;}
				break;
				case "getUniversidadSmartCampus":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getCatTipoPrueba":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getCatPsicologo":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "generarFirma":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getCatBitacoraComentario":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getCatPropedeutico":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getCatPropedeuticoByPeriodo":allow=bonitaRolFilter(context,"EXTERIOR")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				break;
				case "getCatPeriodoActivo":allow=bonitaRolFilter(context,"EXTERIOR")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				break;
				case "getPsicometricoCompleto":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getPsicometricoMotivo":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "postGetCatBitacoraComentariosPsicometrico":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getFechaINVP":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Usuario Auxiliar")
				if(allow){break;}
				break;
				case "getFechaSesion":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getFechaSesion":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getPsicometricoFinalizado":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getInfoReportes":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getInfoRelativos":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getInfoRelativosHermanos":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getInfoFuentesInfluyeron":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getInfoRasgos":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getInfoCapacidadAdaptacion":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getInfoSaludPSeccion":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getInfoSaludSSeccion":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getEstadosPreparatorias":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getCatPeriodoActivoFechaEspecifica":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getEstadoCivil":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "archivedCaseVariable":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getUserBonita":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getPsicologoSesiones":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getUserByCaseIdINVP":
				allow = bonitaRolFilter(context, "ADMISIONES");
				if (allow) {
				  break;
				}
				allow = bonitaRolFilter(context, "Usuario Auxiliar");
				if (allow) {
				  break;
				}
				break;
				case "getPsicologoSesiones":
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getCatGestionEscolar":
				allow = bonitaRolFilter(context, "ADMINISTRADOR"); 
				if (allow) {break;}
				allow = bonitaRolFilter(context, "TI SERUA"); 
				if (allow) {break;}
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Becas")
				if(allow){break;}
				allow = bonitaRolFilter(context, "Config Campus SDAE"); if (allow) {break;}
				break;
				case "getSesion":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getPruebasFechas":
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				break;
				case "getHorarios":
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				break;
				case "getAzureConfig":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getSesionAspirante":
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				break;
				case "getCatNotificacionesCampus":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getDatosSesionUsername":
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				break;
				case "getDatosSesionId":
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				break;
				case "getPaletteColor":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getFechaPruebasByUsername":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getEstatusDelAspirante":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getIsPeriodoActivo":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "updateTimmerPeriodoVencido":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getInfoByIdBanner":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getInfoByIdBanner2":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getIdbanner":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getUserIdBanner":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getUpdateFamiliaresIntento":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getCatBachilleratos":					
					allow=bonitaRolFilter(context,"ADMISIONES")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
					allow=bonitaRolFilter(context,"EXTERIOR")
					if(allow){break;}
				break;
				case "getCatBachillerato":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getCatBitacoraCorreo":
					allow=bonitaRolFilter(context,"ADMISIONES")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Finanzas")
					if(allow){break;}
				break;
				case "getUsuarios":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getValidarClave":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getValidarEscalaINVP":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Usuario Auxiliar")
				if(allow){break;}
				break;
				case "getValidarOrden":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getValidarIdBanner":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getPeriodosActivos":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "buscarCambiosBannerPreparatoria":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "testMultiThread":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "replicarProperties":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getPeriodosSiguientes":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getValidarClavePeriodo":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getValidarEscalaEAC":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getInfoPrueba":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getInfoSesion":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getFechaServidor":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getTransactionLog":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getHubspotLog":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getBachilleratoLog":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getExcelPlantillaHermanos":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getExcelPlantillaRegistro":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getPlantillaRegistro":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getEstados":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Becas")
				if(allow){break;}
				break;
				case "getTempKeyAzure":
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				break;
				case "getAspirantePAA":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "cargarEACBANNER":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "cargarEACBANNER_IDBANNER":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getLstAspirantes":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getListaBitacoraRC":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getLimpiarBitacoraErrores":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getAspiranteRC":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getAspiranteRC_Expecifico":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getResponsableEntrevista":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getResponsablesPrueba":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getDuplicado":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getIsPeriodoVencido":
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				break;
				case "getConstanciasHistorico":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getSesions":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getPropedeuticosNoFecha":
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"SERUA")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Becas")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Area Artistica")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Area Deportiva")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Finanzas")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Comite de Finanzas")
				if(allow){break;}
				allow=bonitaRolFilter(context,"PreAutorizacion")
				if(allow){break;}
				break;
				case "getActiveProcess":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getCurrentTaskId":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getCaseVariables":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getBitacoraPagosByEmail":
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				break;
				case "getSesionesReporte":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getPeriodosReporte":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getCatGestionEscolarMultiple":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "updatePerfil":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getUserByIdbanner":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getTipoEscala":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getResultadoINVP":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Usuario Auxiliar")
				if(allow){break;}
				break;
				case "getEscalaINVPSexo":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Usuario Auxiliar")
				if(allow){break;}
				break;
				case "getResultadosINVPIndividuales":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Usuario Auxiliar")
				if(allow){break;}
				break;
				case "formateoVariablesPaseListaProceso":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getUserContext":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "updateDatosSolicitud":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getExistsIdBannerINVP":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Usuario Auxiliar")
				if(allow){break;}
				break;
				case "getGetFechaPsicometrico":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getEmailHubspotConfig":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				break;
				case "getFileTest":allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
				case "getCatDescuentosByInfoAspirante":
					allow=bonitaRolFilter(context,"ADMISIONES");if(allow){break;}
					allow=bonitaRolFilter(context,"TI SERUA");if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR");if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE");if(allow){break;}
				break;
				
				case "updateSesionesCaseId":
				  	allow=bonitaRolFilter(context,"ADMISIONES");if(allow){break;}
				break;
			}
			return allow;
	}
	public Boolean allowedUrlPost(RestAPIContext context, String url) {
		Boolean allow = false;
	
		switch (url) {
			case "getExcelBachilleratos":
				allow=bonitaRolFilter(context,"TI SERUA")
				if(allow){break;}
			break;
			case "getUsuariosTransferencia":
				allow=bonitaRolFilter(context,"ADMISIONES")
				if(allow){break;}
			break;
		  case "test":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getNuevasSolicitudes":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "updateInformacionAspirante":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getExcelFile":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getPdfFile":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getExcelFileCatalogo":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getInformacionResultado":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "clearInfoCartaTemporal":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectInfoCartaTemporal":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectInfoCartaTemporalNoResultados":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "enviarCartas":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectConsultaDeResultados":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "seleccionarCarta":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getPdfFileCatalogo":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getExcelFileCatalogosAD":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getPdfFileCatalogoAD":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getAspirantesProceso":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectAspirantesEnproceso":
		  	allow=bonitaRolFilter(context,"PreAutorizacion")
			if(allow){break;}
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {break;}
		    allow = bonitaRolFilter(context, "ASPIRANTE");
		    if (allow) {break;}
			allow=bonitaRolFilter(context,"SERUA")
			if(allow){break;}
			allow=bonitaRolFilter(context,"Becas")
			if(allow){break;}
			allow=bonitaRolFilter(context,"Chat")
			if(allow){break;}
		    break;
		  case "selectAspirantesEnRed":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectAspirantesMigrados":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectAspirantesEnprocesoFechas":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectBitacoraPago":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatCampus":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow=bonitaRolFilter(context,"Becas")
			if(allow){break;}
		    break;
		  case "getCatPais":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatCiudad":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCodigoPostalRepetido":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatPropedeuticoGral":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatPropedeuticoRelacionTipo":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatCiudadExcel":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatCiudadPdf":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatEstados":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatCiudad":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatBachillerato":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatFiltradoCalalogosAdMisiones":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatEscolaridad":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatSexo":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatParentesco":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatNacionalidad":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatDescuentos":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatGenerico":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatParentescoA":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatEstadoG":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getSesionesCalendarizadas":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getLastFechaExamenByUsername":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "callSesiones":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getSesionesAspirantes":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getSesionesCalendarizadasPasadas":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getSesionesAspirantesPasados":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "insertPaseLista":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "updatePrepaSolicitudDeAdmision":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "updatePaseLista":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getExcelPaseLista":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getPdfPaseLista":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getExcelSesionesCalendarizadas":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getPdfSesionesCalendarizadas":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getSesionesAspirantesReporte":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getAspirantes3Asistencias":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatGestionEscolar":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {break;}
			allow=bonitaRolFilter(context,"Becas")
			if(allow){break;}
			allow = bonitaRolFilter(context, "ADMINISTRADOR");
			if (allow) {break;}
			allow = bonitaRolFilter(context, "TI SERUA");
			if (allow) {break;}
			allow=bonitaRolFilter(context,"ADMISIONES")
			if(allow){break;}
			allow=bonitaRolFilter(context,"Becas")
			if(allow){break;}
			allow = bonitaRolFilter(context, "Config Campus SDAE"); if (allow) {break;}
		    break;
		  case "getExcelGenerico":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getPdfGenerico":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "updateAceptado":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "activarDesactivarLugarExamen":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getBitacorasComentarios":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getComentariosValidacion":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getComentariosValidacion":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getRegistrosBecas":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postExcelBecas":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getSesionesCalendarizadasPsicologoSupervisor":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getSesionesPsicologoAdministradorAspirantes":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postGuardarUsuario":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "subirDatosBannerEthos":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postValidarUsuarioImportacionPAA":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getAspirantesSinPAA":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postListaAspirantePAA":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "bitacoraIntegracionEAC":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatEscalaEAC":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postGuardarBitacoraErrores":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postValidarUsuarioImportacionRC":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postValidarUsuarioCantidadIntento":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getAspirantesSinRC":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postGuardarUsuarioRC":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postListaAspiranteRC":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postExcelAspirantesPAA":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postEliminarResultado":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postUpdateLicenciaturaPeriodo":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "updateAspirantesPruebas":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "insertAspirantesPruebas":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postBitacoraSesiones":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postAspiranteSesionesByUsername":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postAsistenciaProceso":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatNacionalidadNew":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatPeriodo":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectSolicitudesEnProceso":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getAspirantesByStatus":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getDocumentoTest":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "pagoOxxoCash":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    allow = bonitaRolFilter(context, "ASPIRANTE");
		    if (allow) {
		      break;
		    }
		    break;
		case "pagoOxxoCashBecas":
			allow = bonitaRolFilter(context, "ASPIRANTE");
			if (allow) {
			  break;
			}
			break;
		  case "pagoTarjeta":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    allow = bonitaRolFilter(context, "ASPIRANTE");
		    if (allow) {
		      break;
		    }
		    break;
		  case "pagoTarjetaBecas":
			allow = bonitaRolFilter(context, "ADMISIONES");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "ASPIRANTE");
			if (allow) {
			  break;
			}
			break;
		  case "pagoSPEI":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    allow = bonitaRolFilter(context, "ASPIRANTE");
		    if (allow) {
		      break;
		    }
		    break;
		case "pagoSPEIBecas":
			allow = bonitaRolFilter(context, "ASPIRANTE");
			if (allow) {
			  break;
			}
			break;
		  case "RegistrarUsuario":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    allow = bonitaRolFilter(context, "EXTERIOR");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getOrderPaymentMethod":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    allow = bonitaRolFilter(context, "ASPIRANTE");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getOrderDetails":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    allow = bonitaRolFilter(context, "ASPIRANTE");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getConektaPublicKey":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow=bonitaRolFilter(context,"ASPIRANTE")
			if(allow){break;}
		    break;
		  case "ejecutarEsperarPago":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "encode":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "decode":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getAspirantesByStatusTemprano":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "recuparaPassword":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow = bonitaRolFilter(context, "EXTERIOR");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "ASPIRANTE");
			if (allow) {
			  break;
			}
		    break;
		  case "recuperarPasswordAdministrativo":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow = bonitaRolFilter(context, "EXTERIOR");
			if (allow) {
			  break;
			}
		    break;
		  case "sendEmail":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "generateHtml":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    allow = bonitaRolFilter(context, "ASPIRANTE");
		    if (allow) {
		      break;
		    }
		    break;
		  case "generateHtmlSDAE":
			allow = bonitaRolFilter(context, "ADMISIONES");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "ASPIRANTE");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "ADMINISTRADOR");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "BECAS");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "PreAutorizacion");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "Finanzas");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "Comite de Finanzas");
			if (allow) {
			  break;
			}
			break;
		  case "generateHtmlINVP":
			allow = bonitaRolFilter(context, "ADMISIONES");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "ASPIRANTE");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "ADMINISTRADOR");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "BECAS");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "PreAutorizacion");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "Finanzas");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "Comite de Finanzas");
			if (allow) {
			  break;
			}
			break;
		  case "getTestUpdate":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "insertLicenciatura":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "insertLicenciaturaBonita":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "simpleSelect":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    } 
			allow = bonitaRolFilter(context, "TI SERUA");
			if (allow) {
			  break;
			}
		    break;
		  case "simpleSelectBonita":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "insertBachillerato":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "insertCatBitacoraComentario":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "updateBachillerato":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "updatePerteneceRed":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getBachillerato":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "deleteFirma":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "insertFirma":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "insertBachilleratoLog":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "insertCatNotificacionesCampus":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "insertAzureConfig":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "insertSesion":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getSesionesCalendario":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getSesionesCalendarioAspirante":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow=bonitaRolFilter(context,"ASPIRANTE")
			if(allow){break;}
		    break;
		  case "insertSesionAspirante":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    allow = bonitaRolFilter(context, "ASPIRANTE");
		    if (allow) {
		      break;
		    }
		    break;
		  case "updateFirma":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "updateBusinessAppMenu":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "insertDocumentosTextos":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "enviarTarea":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "recoveryData":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getApiCrispChat":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    allow = bonitaRolFilter(context, "ASPIRANTE");
		    if (allow) {
		      break;
		    }
		    allow = bonitaRolFilter(context, "EXTERIOR");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdateRegistro":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdateEnviada":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdateModificar":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdateUsuariosRegistrados":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdateValidar":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdateRechazoLRoja":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "insertUpdatePsicometrico":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "insertUpdatePsicometricoV2":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "cambiosBannerPreparatoria":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectAspirantesPsicometrico":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectAspirantesConPsicometrico":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectAspirantesConPsicometricoRechazados":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdateRestaurarRechazoLRoja":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdatePago":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdateAutodescripcion":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdateSeleccionoFechaExamen":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdateGenerarCredencial":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdateEsperaResultado":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdateNoAsistioPruebas":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "reagendarExamen":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    allow = bonitaRolFilter(context, "ASPIRANTE");
		    if (allow) {
		      break;
		    }
		
		    break;
		  case "updateCatNotificaciones":
		    allow = bonitaRolFilter(context, "ADMINISTRADOR");
		    if (allow) {
			  break;
		    }
		    allow = bonitaRolFilter(context, "TI SERUA");
		    if (allow) {
			  break;
		    }
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "updateCatNotificacionesSDAE":
			allow = bonitaRolFilter(context, "ADMISIONES");
			if (allow) {
			  break;
			} 
			allow = bonitaRolFilter(context, "Config Campus SDAE");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "ADMINISTRADOR");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "TI SERUA");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "Config Campus SDAE"); if (allow) {break;}
			break;
		  case "updateCatNotificacionesAlt":
		    allow = bonitaRolFilter(context, "ADMINISTRADOR");
		    if (allow) {
			  break;
		    }
			allow = bonitaRolFilter(context, "TI SERUA");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "ADMISIONES");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "Config Campus SDAE"); 
			if (allow) {
			  break;
			}
			break;
		  case "transferirAspirante":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdateTransferirAspirante":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectBitacoraTransferencias":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getCatPaisExcel":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getUsuariosRegistrados":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "updateUsuarioRegistrado":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectAspirantesEnLaRed":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectAspirantesSmartCampus":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "B64File":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "updateCorreoElectronico":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postExcelINVPIndividual":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow = bonitaRolFilter(context, "Usuario Auxiliar");
			if (allow) {
			  break;
			}
		    break;
		  case "getExcelTransferencias":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getUsuariosRechazadosComite":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "reactivarAspirante":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "nuevoCasoSolicitud":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "RealizarRespaldo":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectAspirantesRechazadosRespaldo":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getInformacionReporteSolicitudRespaldo":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "guardarTutorIntento":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "respaldoUsuario":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getUsuariosConSolicitudVencida":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getUsuariosConSolicitudAbandonada":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getPadresTutorVencido":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postUpdatePeriodoVencido":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow=bonitaRolFilter(context,"ASPIRANTE")
			if(allow){break;}
		    break;
		  case "getPadreVencido":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getMadreVencido":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		case "getUserByCaseIdINVP":
			allow = bonitaRolFilter(context, "ADMISIONES");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "Usuario Auxiliar");
			if (allow) {
			  break;
			}
			break;
		  case "getContactoEmergencia":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getHermanosVencidos":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getInformacionEscolarVencidos":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getUniviersidadesVencidos":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getIdiomaVencidos":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getTerapiaVencidos":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getGrupoSocialVencidos":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getParienteEgresadoVencidos":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "selectConsultaDeResultadosManual":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "updateViewDownloadSolicitud":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getActiveProcess":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "reAssignTask":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "generarReporte":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "generarReporteResultadosExamenes":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "generarReporteAdmitidosPropedeutico":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "generarReporteDatosFamiliares":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "generarReporteRelacionAspirantes":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		case "generarReportePerfilAspirante":
			allow = bonitaRolFilter(context, "ADMISIONES");
			if (allow) {
			  break;
			}
			break;
		  case "getSesionesINVP":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow = bonitaRolFilter(context, "Usuario Auxiliar");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "Proveedor");
			if (allow) {
			  break;
			}
		    break;
		  case "getSesionesINVPTabla":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow = bonitaRolFilter(context, "Usuario Auxiliar");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "Proveedor");
			if (allow) {
			  break;
			}
		    break;
		  case "getSesionesINVPTablaProcesadas":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow = bonitaRolFilter(context, "Usuario Auxiliar");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "Proveedor");
			if (allow) {
			  break;
			}
		    break;
		  case "getUsersByPrueba":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getUsersByPrueba2":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postResultadosINVPIndividuales":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow = bonitaRolFilter(context, "Usuario Auxiliar");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "Proveedor");
			if (allow) {
			  break;
			}
		    break;
		  case "insertRespuesta":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow = bonitaRolFilter(context, "Proveedor");
			if (allow) {
			  break;
			}
		    break;
		  case "postSelectAspirantePrueba":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "postGetIdSesionByIdBanner":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow = bonitaRolFilter(context, "Proveedor");
			if (allow) {
			  break;
			}
		    break;
		  case "postGetIdSesionByCaseId":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "PostUpdateDeleteCatEscalaINVP":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow = bonitaRolFilter(context, "Usuario Auxiliar");
			if (allow) {
			  break;
			}
			allow = bonitaRolFilter(context, "Proveedor");
			if (allow) {
			  break;
			}
		    break;
		  case "PostUpdateDeleteCatEscalaEAC":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "getSesiones":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
			allow = bonitaRolFilter(context, "Proveedor");
			if (allow) {
			  break;
			}
		    break;
		  case "insertEmailHubspotConfig":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "sendEmailPlantilla":
		    allow = bonitaRolFilter(context, "ADMISIONES");
		    if (allow) {
		      break;
		    }
		    break;
		  case "createOrUpdateBeca":
			allow = bonitaRolFilter(context, "TI SERUA");
			if (allow) {
			  break;
			}
			break;
			
		  case "getUserFoto":
		  allow=bonitaRolFilter(context,"ADMISIONES")
			if(allow){break;}
			break;
		}
		
		return allow;
	}
}
