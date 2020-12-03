package com.anahuac.rest.api.DAO

import java.sql.RowId
import java.text.DateFormat
import java.text.SimpleDateFormat

import org.bonitasoft.engine.bpm.data.DataDefinition
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessDefinition
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.bpm.process.ProcessInstanceCriterion
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.search.Order
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatNotificacionesDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.model.ProcesoCasoDAO
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.Entity.Result
import com.anahuac.catalogos.CatBachilleratosDAO
import com.anahuac.catalogos.CatGestionEscolarDAO
import com.anahuac.rest.api.Entity.Custom.CatBachilleratosCustom
import com.anahuac.rest.api.Entity.Custom.CatCampusCustom
import com.anahuac.rest.api.Entity.Custom.CatEstadosCustom
import com.anahuac.rest.api.Entity.Custom.CatGestionEscolar
import com.anahuac.rest.api.Entity.Custom.CatLicenciaturaCustom
import com.anahuac.rest.api.Entity.Custom.CatPeriodoCustom
import com.anahuac.rest.api.Entity.Custom.DetalleSolicitudCustom
import com.anahuac.rest.api.Entity.Custom.SolicitudAdmisionCustom
import com.bonitasoft.engine.bpm.parameter.ParameterCriterion
import com.bonitasoft.engine.bpm.process.impl.ProcessInstanceSearchDescriptor
import com.bonitasoft.web.extension.rest.RestAPIContext
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document as DocumentItext
import com.itextpdf.text.DocumentException
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import groovy.json.JsonSlurper

class ListadoDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ListadoDAO.class);
	
	public Result getNuevasSolicitudes(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<SolicitudAdmisionCustom> lstResultado = new ArrayList<SolicitudAdmisionCustom>();
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		Integer start = 0;
		Integer end = 99999;
		Integer inicioContador = 0;
		Integer finContador = 0;
		
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		SolicitudDeAdmision objSolicitudDeAdmision = null;
		SolicitudAdmisionCustom objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
		CatCampusCustom objCatCampusCustom = new CatCampusCustom();
		CatPeriodoCustom objCatPeriodoCustom = new CatPeriodoCustom();
		CatEstadosCustom objCatEstadosCustom = new CatEstadosCustom();
		CatLicenciaturaCustom objCatLicenciaturaCustom = new CatLicenciaturaCustom();
		CatBachilleratosCustom objCatBachilleratosCustom = new CatBachilleratosCustom();
		CatGestionEscolar objCatGestionEscolar = new CatGestionEscolar();

		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		
		DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		Boolean isFound = true;
		Boolean isFoundCampus = false;
		
		String nombreCandidato = "";
		String gestionEscolar = "";
		String catCampusStr = "";
		String catPeriodoStr = "";
		String catEstadoStr = "";
		String catBachilleratoStr = "";
		String strError = "";
		
		ProcessDefinition objProcessDefinition;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			if(object.lstFiltro != null) {
				assert object.lstFiltro instanceof List;
			}

			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Cancún");
			objGrupoCampus.put("valor","CAMPUS-CANCUN");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Mayab");
			objGrupoCampus.put("valor","CAMPUS-MAYAB");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac México Norte");
			objGrupoCampus.put("valor","CAMPUS-MNORTE");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac México Sur");
			objGrupoCampus.put("valor","CAMPUS-MSUR");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Oaxaca");
			objGrupoCampus.put("valor","CAMPUS-OAXACA");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Puebla");
			objGrupoCampus.put("valor","CAMPUS-PUEBLA");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Querétaro");
			objGrupoCampus.put("valor","CAMPUS-QUERETARO");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Xalapa");
			objGrupoCampus.put("valor","CAMPUS-XALAPA");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Querétaro");
			objGrupoCampus.put("valor","CAMPUS-QUERETARO");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Juan Pablo II");
			objGrupoCampus.put("valor","CAMPUS-JP2");
			lstGrupoCampus.add(objGrupoCampus);
			
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(Map<String, String> rowGrupo : lstGrupoCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.get("valor"))) {
						lstGrupo.add(rowGrupo.get("descripcion"));
						break;
					}
				}
			}
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def procesoCasoDAO = context.apiClient.getDAO(ProcesoCasoDAO.class);
			//procesoCasoDAO.getCaseId("", "");
			
			SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Proceso admisiones");
			final SearchOptions searchOptionsProccess = searchBuilderProccess.done();
			SearchResult<ProcessDeploymentInfo> SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			List<ProcessDeploymentInfo> lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			
			//Long processDefinitionId = context.getApiClient().getProcessAPI().getProcessDefinitionId("Proceso admisiones", "1.0");
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			
			//searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
			inicioContador = 0;
			finContador = 0;
			for(ProcessDeploymentInfo  objProcessDeploymentInfo : lstProcessDeploymentInfo) {
				LOGGER.error objProcessDeploymentInfo.getProcessId().toString()+" - "+objProcessDeploymentInfo.getName() + " - " + objProcessDeploymentInfo.getVersion();
				if(inicioContador == 0) {
					LOGGER.error "IF"
					searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
				}
				else {
					LOGGER.error "ELSE"
					searchBuilder.or();
					searchBuilder.differentFrom(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
				}
				inicioContador++;
			}
			
			searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);
			
			final SearchOptions searchOptions = searchBuilder.done();
			SearchResult<HumanTaskInstance>  SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
			List<HumanTaskInstance> lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
			
			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstanceSearch) {
				strError = strError+", objHumanTaskInstance.getName(): " + ""+objHumanTaskInstance.getName();
				objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
				objCatCampusCustom = new CatCampusCustom();
				
				
				if(objHumanTaskInstance.getName().equals(object.tarea)) {
					isFound = true;
					
					caseId = objHumanTaskInstance.getRootContainerId();
					lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end);
					if(!lstSolicitudDeAdmision.empty){
						//objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end).get(0);
						objSolicitudDeAdmision = lstSolicitudDeAdmision.get(0);
						catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
						isFoundCampus = false;
						strError = strError+", catCampusStr.toLowerCase(): " + ""+catCampusStr.toLowerCase();
						for(String objGrupo : lstGrupo) {
							if(catCampusStr.toLowerCase().equals(objGrupo.toLowerCase())) {
								isFoundCampus = true;
							}
						}
						strError = strError+", isFoundCampus: " + ""+isFoundCampus;
						if(isFoundCampus) {
							
							if(object.lstFiltro != null) {
								for(def row: object.lstFiltro) {
									
									
									isFound = false;
									assert row instanceof Map;
									strError = strError+", row.columna: " + ""+row.columna;
									
									nombreCandidato = objSolicitudDeAdmision.getPrimerNombre()+" "+objSolicitudDeAdmision.getSegundoNombre()+" "+objSolicitudDeAdmision.getApellidoPaterno()+" "+objSolicitudDeAdmision.getApellidoMaterno();
									strError = strError+", nombreCandidato: " + ""+nombreCandidato;
									strError = strError+", row.operador: " + ""+row.operador;
									if(row.operador == 'Que contenga') {
										if (row.columna == "NOMBRE") {
											if (nombreCandidato.toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "EMAIL") {
											if (objSolicitudDeAdmision.getCorreoElectronico().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "CURP") {
											if (objSolicitudDeAdmision.getCurp().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "CAMPUS") {
											catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
											if(catCampusStr.toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PROGRAMA") {
											gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null ? "" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
											if(gestionEscolar.contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "INGRESO") {
											catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null ? "" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
											if(catPeriodoStr.toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "ESTADO") {
											catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
											if(catEstadoStr.toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PREPARATORIA") {
											catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null ? "" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
											if(catBachilleratoStr.toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PROMEDIO") {
											if(objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "ESTATUS") {
											if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "SOLICITUD") {
											if(objSolicitudDeAdmision.getCaseId().toString().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "TELÉFONO") {
											if(objSolicitudDeAdmision.getTelefonoCelular().toString().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PROCEDENCIA") {
											catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
											if(catEstadoStr.toString().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "FECHA DE ENVIO") {
											if(dfSalida.format(objHumanTaskInstance.getLastUpdateDate()).toString().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										}
									}
									else {
										if (row.columna == "NOMBRE") {
											if (nombreCandidato.toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "EMAIL") {
											if (objSolicitudDeAdmision.getCorreoElectronico().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "CURP") {
											if (objSolicitudDeAdmision.getCurp().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "CAMPUS") {
											catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
											if(catCampusStr.toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PROGRAMA") {
											gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null ? "" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
											if(gestionEscolar.equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "INGRESO") {
											catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null ? "" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
											if(catPeriodoStr.toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "ESTADO") {
											catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
											if(catEstadoStr.toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PREPARATORIA") {
											catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null ? "" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
											if(catBachilleratoStr.toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PROMEDIO") {
											if(objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "ESTATUS") {
											if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "SOLICITUD") {
											if(objSolicitudDeAdmision.getCaseId().toString().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "TELÉFONO") {
											if(objSolicitudDeAdmision.getTelefonoCelular().toString().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PROCEDENCIA") {
											catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
											if(catEstadoStr.toString().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "FECHA DE ENVIO") {
											if(dfSalida.format(objHumanTaskInstance.getLastUpdateDate()).toString().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										}
										
									}
								}
							}
						}
						else {
							isFound = false;
						}
						strError = strError+", isFound: " + ""+isFound;
						if(isFound) {
							String encoded = "";
							for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(objSolicitudDeAdmision.getCaseId(), "fotoPasaporte", 0, 10)) {
								//encoded ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
								encoded = "../API/formsDocumentImage?document="+doc.getId();
								//LOGGER.error encoded;
							}
							
							//objSolicitudAdmisionCustom.setUpdateDate(updateDate);
							
							objProcessDefinition = context.getApiClient().getProcessAPI().getProcessDefinition(objHumanTaskInstance.getProcessDefinitionId());
							objSolicitudAdmisionCustom.setTaskName(objHumanTaskInstance.getName());
							objSolicitudAdmisionCustom.setUpdateDate(dfSalida.format(objHumanTaskInstance.getLastUpdateDate()));
							objSolicitudAdmisionCustom.setProcessName(objProcessDefinition.getName());
							objSolicitudAdmisionCustom.setProcessVersion(objProcessDefinition.getVersion());
							objSolicitudAdmisionCustom.setPersistenceId(objSolicitudDeAdmision.getPersistenceId());
							objSolicitudAdmisionCustom.setPersistenceVersion(objSolicitudDeAdmision.getPersistenceVersion());
							objSolicitudAdmisionCustom.setCaseId(objSolicitudDeAdmision.getCaseId());
							objSolicitudAdmisionCustom.setPrimerNombre(objSolicitudDeAdmision.getPrimerNombre());
							objSolicitudAdmisionCustom.setSegundoNombre(objSolicitudDeAdmision.getSegundoNombre());
							objSolicitudAdmisionCustom.setApellidoPaterno(objSolicitudDeAdmision.getApellidoPaterno());
							objSolicitudAdmisionCustom.setApellidoMaterno(objSolicitudDeAdmision.getApellidoMaterno());
							objSolicitudAdmisionCustom.setCorreoElectronico(objSolicitudDeAdmision.getCorreoElectronico());
							objSolicitudAdmisionCustom.setCurp(objSolicitudDeAdmision.getCurp());
							objSolicitudAdmisionCustom.setUsuarioFacebook(objSolicitudDeAdmision.getUsuarioFacebook());
							objSolicitudAdmisionCustom.setUsiarioTwitter(objSolicitudDeAdmision.getUsiarioTwitter());
							objSolicitudAdmisionCustom.setUsuarioInstagram(objSolicitudDeAdmision.getUsuarioInstagram());
							objSolicitudAdmisionCustom.setTelefonoCelular(objSolicitudDeAdmision.getTelefonoCelular());
							objSolicitudAdmisionCustom.setFoto(objSolicitudDeAdmision.getFoto());
							objSolicitudAdmisionCustom.setActaNacimiento(objSolicitudDeAdmision.getActaNacimiento());
							objSolicitudAdmisionCustom.setCalle(objSolicitudDeAdmision.getCalle());
							objSolicitudAdmisionCustom.setCodigoPostal(objSolicitudDeAdmision.getCodigoPostal());
							objSolicitudAdmisionCustom.setCiudad(objSolicitudDeAdmision.getCiudad());
							objSolicitudAdmisionCustom.setCalle2(objSolicitudDeAdmision.getCalle2());
							objSolicitudAdmisionCustom.setNumExterior(objSolicitudDeAdmision.getNumExterior());
							objSolicitudAdmisionCustom.setNumInterior(objSolicitudDeAdmision.getNumInterior());
							objSolicitudAdmisionCustom.setColonia(objSolicitudDeAdmision.getColonia());
							objSolicitudAdmisionCustom.setTelefono(objSolicitudDeAdmision.getTelefono());
							objSolicitudAdmisionCustom.setOtroTelefonoContacto(objSolicitudDeAdmision.getOtroTelefonoContacto());
							objSolicitudAdmisionCustom.setPromedioGeneral(objSolicitudDeAdmision.getPromedioGeneral());
							objSolicitudAdmisionCustom.setComprobanteCalificaciones(objSolicitudDeAdmision.getComprobanteCalificaciones());
							//objSolicitudAdmisionCustom.setCiudadExamen(objSolicitudDeAdmision.getCiudadExamen());
							objSolicitudAdmisionCustom.setTaskId(objHumanTaskInstance.getId());
							
														
							objCatGestionEscolar = new CatGestionEscolar();
							if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){
								
								objCatGestionEscolar.setPersistenceId(objSolicitudDeAdmision.getCatGestionEscolar().getPersistenceId());
								objCatGestionEscolar.setPersistenceVersion(objSolicitudDeAdmision.getCatGestionEscolar().getPersistenceVersion());
								objCatGestionEscolar.setNombre(objSolicitudDeAdmision.getCatGestionEscolar().getNombre());
								objCatGestionEscolar.setDescripcion(objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion());
								objCatGestionEscolar.setEnlace(objSolicitudDeAdmision.getCatGestionEscolar().getEnlace());
								objCatGestionEscolar.setPropedeutico(objSolicitudDeAdmision.getCatGestionEscolar().isPropedeutico());
								objCatGestionEscolar.setProgramaparcial(objSolicitudDeAdmision.getCatGestionEscolar().isProgramaparcial());
								objCatGestionEscolar.setMatematicas(objSolicitudDeAdmision.getCatGestionEscolar().isMatematicas());
								objCatGestionEscolar.setInscripcionenero(objSolicitudDeAdmision.getCatGestionEscolar().getInscripcionenero());
								objCatGestionEscolar.setInscripcionagosto(objSolicitudDeAdmision.getCatGestionEscolar().getInscripcionagosto());
								objCatGestionEscolar.setIsEliminado(objSolicitudDeAdmision.getCatGestionEscolar().isIsEliminado());
								objCatGestionEscolar.setUsuarioCreacion(objSolicitudDeAdmision.getCatGestionEscolar().getUsuarioCreacion());
								objCatGestionEscolar.setCampus(objSolicitudDeAdmision.getCatGestionEscolar().getCampus());
								objCatGestionEscolar.setCaseId(objSolicitudDeAdmision.getCatGestionEscolar().getCaseId());
								
							}
							objSolicitudAdmisionCustom.setObjCatGestionEscolar(objCatGestionEscolar);
							
							objCatCampusCustom = new CatCampusCustom();
							if(objSolicitudDeAdmision.getCatCampus() != null){
								
								objCatCampusCustom.setPersistenceId(objSolicitudDeAdmision.getCatCampus().getPersistenceId());
								objCatCampusCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatCampus().getPersistenceVersion());
								objCatCampusCustom.setDescripcion(objSolicitudDeAdmision.getCatCampus().getDescripcion());
								objCatCampusCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatCampus().getUsuarioBanner());
								objCatCampusCustom.setClave(objSolicitudDeAdmision.getCatCampus().getClave());
								
							}
							objSolicitudAdmisionCustom.setCatCampus(objCatCampusCustom);
							
							objCatPeriodoCustom = new CatPeriodoCustom();
							if(objSolicitudDeAdmision.getCatPeriodo() != null){
								
								objCatPeriodoCustom.setPersistenceId(objSolicitudDeAdmision.getCatPeriodo().getPersistenceId());
								objCatPeriodoCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatPeriodo().getPersistenceVersion());
								objCatPeriodoCustom.setDescripcion(objSolicitudDeAdmision.getCatPeriodo().getDescripcion());
								objCatPeriodoCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatPeriodo().getUsuarioBanner());
								objCatPeriodoCustom.setClave(objSolicitudDeAdmision.getCatPeriodo().getClave());
								
							}
							objSolicitudAdmisionCustom.setCatPeriodo(objCatPeriodoCustom);
							
							objCatEstadosCustom = new CatEstadosCustom();
							if(objSolicitudDeAdmision.getCatEstado() != null){
								
								objCatEstadosCustom.setPersistenceId(objSolicitudDeAdmision.getCatEstado().getPersistenceId());
								objCatEstadosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatEstado().getPersistenceVersion());
								objCatEstadosCustom.setClave(objSolicitudDeAdmision.getCatEstado().getClave());
								objCatEstadosCustom.setDescripcion(objSolicitudDeAdmision.getCatEstado().getDescripcion());
								objCatEstadosCustom.setUsuarioCreacion(objSolicitudDeAdmision.getCatEstado().getUsuarioCreacion());
								
							}
							objSolicitudAdmisionCustom.setCatEstado(objCatEstadosCustom);
							objSolicitudAdmisionCustom.setCatLicenciatura(objCatLicenciaturaCustom);
							
							objCatBachilleratosCustom = new CatBachilleratosCustom();
							if(objSolicitudDeAdmision.getCatBachilleratos() != null){
								
								objCatBachilleratosCustom.setPersistenceId(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceId());
								objCatBachilleratosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceVersion());
								objCatBachilleratosCustom.setClave(objSolicitudDeAdmision.getCatBachilleratos().getClave());
								objCatBachilleratosCustom.setDescripcion(objSolicitudDeAdmision.getCatBachilleratos().getDescripcion());
								objCatBachilleratosCustom.setPais(objSolicitudDeAdmision.getCatBachilleratos().getPais());
								objCatBachilleratosCustom.setEstado(objSolicitudDeAdmision.getCatBachilleratos().getEstado());
								objCatBachilleratosCustom.setCiudad(objSolicitudDeAdmision.getCatBachilleratos().getCiudad());
								
							}
							objSolicitudAdmisionCustom.setCatBachilleratos(objCatBachilleratosCustom);

							objSolicitudAdmisionCustom.setFotografiaB64(encoded);
							lstResultado.add(objSolicitudAdmisionCustom);
						}
						else {
							
						}
					}
				}
				else {
				}
			}
			resultado.setError_info(strError);
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setError_info(strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result getAspirantesProceso(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<SolicitudAdmisionCustom> lstResultado = new ArrayList<SolicitudAdmisionCustom>();
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		Integer start = 0;
		Integer end = 99999;
		Integer inicioContador = 0;
		Integer finContador = 0;
		
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		List<SolicitudDeAdmision> lstAllSolicitud = new ArrayList<SolicitudDeAdmision>();
		SolicitudDeAdmision objSolicitudDeAdmision = null;
		SolicitudAdmisionCustom objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
		CatCampusCustom objCatCampusCustom = new CatCampusCustom();
		CatPeriodoCustom objCatPeriodoCustom = new CatPeriodoCustom();
		CatEstadosCustom objCatEstadosCustom = new CatEstadosCustom();
		CatLicenciaturaCustom objCatLicenciaturaCustom = new CatLicenciaturaCustom();
		CatBachilleratosCustom objCatBachilleratosCustom = new CatBachilleratosCustom();
		CatGestionEscolar objCatGestionEscolar = new CatGestionEscolar();
		DetalleSolicitudCustom objDetalleSolicitud = new DetalleSolicitudCustom();
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		
		Boolean isFound = true;
		Boolean isFoundCampus = false;
		
		String nombreCandidato = "";
		String nombreProceso = "";
		
		String gestionEscolar = "";
		String catCampusStr = "";
		String catPeriodoStr = "";
		String catEstadoStr = "";
		String catBachilleratoStr = "";
		
		ProcessDefinition objProcessDefinition;
		
		DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			if(object.lstFiltro != null) {
				assert object.lstFiltro instanceof List;
			}

			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Cancún");
			objGrupoCampus.put("valor","CAMPUS-CANCUN");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Mayab");
			objGrupoCampus.put("valor","CAMPUS-MAYAB");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac México Norte");
			objGrupoCampus.put("valor","CAMPUS-MNORTE");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac México Sur");
			objGrupoCampus.put("valor","CAMPUS-MSUR");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Oaxaca");
			objGrupoCampus.put("valor","CAMPUS-OAXACA");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Puebla");
			objGrupoCampus.put("valor","CAMPUS-PUEBLA");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Querétaro");
			objGrupoCampus.put("valor","CAMPUS-QUERETARO");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Xalapa");
			objGrupoCampus.put("valor","CAMPUS-XALAPA");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Querétaro");
			objGrupoCampus.put("valor","CAMPUS-QUERETARO");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Juan Pablo II");
			objGrupoCampus.put("valor","CAMPUS-JP2");
			lstGrupoCampus.add(objGrupoCampus);
					
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(Map<String, String> rowGrupo : lstGrupoCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.get("valor"))) {
						lstGrupo.add(rowGrupo.get("descripcion"));
						break;
					}
				}
			}
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def procesoCasoDAO = context.apiClient.getDAO(ProcesoCasoDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			
			//context.getApiClient().getProcessAPI().getHumanTaskInstances(rootProcessInstanceId, taskName, startIndex, maxResults)
			LOGGER.error "--------------------------------------------------------------------------------------------------"
			/*for(ProcessInstance objProcessInstance : lstProcessInstance) {
				objProcessInstance = lstProcessInstance.get(0).getState();
				LOGGER.error "RootProcessInstance "+objProcessInstance.getRootProcessInstanceId();
			}*/
			LOGGER.error "--------------------------------------------------------------------------------------------------"			
			

			lstAllSolicitud = objSolicitudDeAdmisionDAO.find(0, 99999);
			
			for(SolicitudDeAdmision objAllSolicitud : lstAllSolicitud) {
				objAllSolicitud.getCaseId()
			}
			
			/*Long processDefinitionId = context.getApiClient().getProcessAPI().getProcessDefinitionId("Proceso admisiones", "1.0");
			
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, processDefinitionId.toString());
			searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);*/
			
			SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Proceso admisiones");
			final SearchOptions searchOptionsProccess = searchBuilderProccess.done();
			SearchResult<ProcessDeploymentInfo> SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			List<ProcessDeploymentInfo> lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			
			//Long processDefinitionId = context.getApiClient().getProcessAPI().getProcessDefinitionId("Proceso admisiones", "1.0");
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			
			//searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
			inicioContador = 0;
			finContador = 0;
			for(ProcessDeploymentInfo  objProcessDeploymentInfo : lstProcessDeploymentInfo) {
				LOGGER.error objProcessDeploymentInfo.getProcessId().toString()+" - "+objProcessDeploymentInfo.getName() + " - " + objProcessDeploymentInfo.getVersion();
				if(inicioContador == 0) {
					LOGGER.error "IF"
					searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
				}
				else {
					LOGGER.error "ELSE"
					searchBuilder.or();
					searchBuilder.differentFrom(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
				}
				inicioContador++;
			}
			
			searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);
			final SearchOptions searchOptions = searchBuilder.done();
			SearchResult<HumanTaskInstance>  SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
			List<HumanTaskInstance> lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
			
			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstanceSearch) {
				
				LOGGER.error "--------------------------------------------------------------------------------------------------"
				LOGGER.error objHumanTaskInstance.getName()
				LOGGER.error "--------------------------------------------------------------------------------------------------"
				
				objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
				objCatCampusCustom = new CatCampusCustom();
				isFound = true;
				caseId = objHumanTaskInstance.getRootContainerId();
				lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end);
				lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(caseId.toString(), start, end);
				if(!lstDetalleSolicitud.empty)
				{
					LOGGER.error "--------------------------------------------------------------------------------------------------"
					LOGGER.error "entra"
					LOGGER.error "--------------------------------------------------------------------------------------------------"
					
					objDetalleSolicitud = new DetalleSolicitudCustom();
					objDetalleSolicitud.setPersistenceId(lstDetalleSolicitud.get(0).getPersistenceId());
					objDetalleSolicitud.setPersistenceVersion(lstDetalleSolicitud.get(0).getPersistenceVersion());
					objDetalleSolicitud.setIdBanner(lstDetalleSolicitud.get(0).getIdBanner());
					objDetalleSolicitud.setIsCurpValidado(lstDetalleSolicitud.get(0).isIsCurpValidado());
					objDetalleSolicitud.setPromedioCoincide(lstDetalleSolicitud.get(0).isPromedioCoincide());
					objDetalleSolicitud.setRevisado(lstDetalleSolicitud.get(0).isRevisado());
					objDetalleSolicitud.setTipoAlumno(lstDetalleSolicitud.get(0).getTipoAlumno());
					objDetalleSolicitud.setDescuento(lstDetalleSolicitud.get(0).getDescuento());
					objDetalleSolicitud.setObservacionesDescuento(lstDetalleSolicitud.get(0).getObservacionesDescuento());
					objDetalleSolicitud.setObservacionesCambio(lstDetalleSolicitud.get(0).getObservacionesCambio());
					objDetalleSolicitud.setObservacionesRechazo(lstDetalleSolicitud.get(0).getObservacionesRechazo());
					objDetalleSolicitud.setObservacionesListaRoja(lstDetalleSolicitud.get(0).getObservacionesListaRoja());
					objDetalleSolicitud.setOrdenPago(lstDetalleSolicitud.get(0).getOrdenPago());
					objDetalleSolicitud.setCaseId(lstDetalleSolicitud.get(0).getCaseId());
					objSolicitudAdmisionCustom.setObjDetalleSolicitud(objDetalleSolicitud);
					
				}
				LOGGER.error "--------------------------------------------------------------------------------------------------"
				LOGGER.error "sale"
				LOGGER.error "--------------------------------------------------------------------------------------------------"
				
				
				if(!lstSolicitudDeAdmision.empty){
					objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end).get(0);
					
					catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
					isFoundCampus = false;
					for(String objGrupo : lstGrupo) {
						if(catCampusStr.toLowerCase().equals(objGrupo.toLowerCase())) {
							isFoundCampus = true;
						}
					}
					if(isFoundCampus) {
						if(object.lstFiltro != null) {
							for(def row: object.lstFiltro) {
								isFound = false;
								assert row instanceof Map;
							
								nombreCandidato = objSolicitudDeAdmision.getPrimerNombre()+" "+objSolicitudDeAdmision.getSegundoNombre()+" "+objSolicitudDeAdmision.getApellidoPaterno()+" "+objSolicitudDeAdmision.getApellidoMaterno();
																
								
								if(row.operador == 'Que contenga') {
									if (row.columna == "NOMBRE") {
										if (nombreCandidato.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "EMAIL") {
										if (objSolicitudDeAdmision.getCorreoElectronico().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CURP") {
										if (objSolicitudDeAdmision.getCurp().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CAMPUS") {
										catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
										if(catCampusStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROGRAMA") {
										gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null ? "" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
										if(gestionEscolar.contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "INGRESO") {
										catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null ? "" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
										if(catPeriodoStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTADO") {
										catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
										if(catEstadoStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PREPARATORIA") {
										catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null ? "" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
										if(catBachilleratoStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROMEDIO") {
										if(objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTATUS") {
										if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ÚLTIMA MODIFICACION") {
										if(dfSalida.format(objHumanTaskInstance.getReachedStateDate()).toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "TIPO") {
										if(objDetalleSolicitud.getTipoAlumno().toString().toLowerCase().contains(row.valor.toString().toLowerCase())) {
											isFound = true;
											break;
										}
									}
									
									//
									
								}
								else {
									if (row.columna == "NOMBRE") {
										if (nombreCandidato.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "EMAIL") {
										if (objSolicitudDeAdmision.getCorreoElectronico().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CURP") {
										if (objSolicitudDeAdmision.getCurp().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CAMPUS") {
										catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
										if(catCampusStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROGRAMA") {
										gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null ? "" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
										if(gestionEscolar.equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "INGRESO") {
										catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null ? "" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
										if(catPeriodoStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTADO") {
										catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
										if(catEstadoStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PREPARATORIA") {
										catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null ? "" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
										if(catBachilleratoStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROMEDIO") {
										if(objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTATUS") {
										if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ÚLTIMA MODIFICACION") {
										if(dfSalida.format(objHumanTaskInstance.getReachedStateDate()).toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "TIPO") {
										if(objDetalleSolicitud.getTipoAlumno().toString().toLowerCase().equals(row.valor.toString().toLowerCase())) {
											isFound = true;
											break;
										}
									}
									
								}
							}
						}
					}
					else {
						isFound = false;
					}
					
					if(isFound) {
						String encoded = "";
						for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(objSolicitudDeAdmision.getCaseId(), "fotoPasaporte", 0, 10)) {
							//encoded ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
							encoded = "../API/formsDocumentImage?document="+doc.getId();
						}
						objProcessDefinition = context.getApiClient().getProcessAPI().getProcessDefinition(objHumanTaskInstance.getProcessDefinitionId());
						objSolicitudAdmisionCustom.setTaskName(objHumanTaskInstance.getName());
						objSolicitudAdmisionCustom.setProcessName(objProcessDefinition.getName());
						objSolicitudAdmisionCustom.setProcessVersion(objProcessDefinition.getVersion());
						objSolicitudAdmisionCustom.setPersistenceId(objSolicitudDeAdmision.getPersistenceId());
						objSolicitudAdmisionCustom.setPersistenceVersion(objSolicitudDeAdmision.getPersistenceVersion());
						objSolicitudAdmisionCustom.setCaseId(objSolicitudDeAdmision.getCaseId());
						objSolicitudAdmisionCustom.setPrimerNombre(objSolicitudDeAdmision.getPrimerNombre());
						objSolicitudAdmisionCustom.setSegundoNombre(objSolicitudDeAdmision.getSegundoNombre());
						objSolicitudAdmisionCustom.setApellidoPaterno(objSolicitudDeAdmision.getApellidoPaterno());
						objSolicitudAdmisionCustom.setApellidoMaterno(objSolicitudDeAdmision.getApellidoMaterno());
						objSolicitudAdmisionCustom.setCorreoElectronico(objSolicitudDeAdmision.getCorreoElectronico());
						objSolicitudAdmisionCustom.setCurp(objSolicitudDeAdmision.getCurp());
						objSolicitudAdmisionCustom.setUsuarioFacebook(objSolicitudDeAdmision.getUsuarioFacebook());
						objSolicitudAdmisionCustom.setUsiarioTwitter(objSolicitudDeAdmision.getUsiarioTwitter());
						objSolicitudAdmisionCustom.setUsuarioInstagram(objSolicitudDeAdmision.getUsuarioInstagram());
						objSolicitudAdmisionCustom.setTelefonoCelular(objSolicitudDeAdmision.getTelefonoCelular());
						objSolicitudAdmisionCustom.setFoto(objSolicitudDeAdmision.getFoto());
						objSolicitudAdmisionCustom.setActaNacimiento(objSolicitudDeAdmision.getActaNacimiento());
						objSolicitudAdmisionCustom.setCalle(objSolicitudDeAdmision.getCalle());
						objSolicitudAdmisionCustom.setCodigoPostal(objSolicitudDeAdmision.getCodigoPostal());
						objSolicitudAdmisionCustom.setCiudad(objSolicitudDeAdmision.getCiudad());
						objSolicitudAdmisionCustom.setCalle2(objSolicitudDeAdmision.getCalle2());
						objSolicitudAdmisionCustom.setNumExterior(objSolicitudDeAdmision.getNumExterior());
						objSolicitudAdmisionCustom.setNumInterior(objSolicitudDeAdmision.getNumInterior());
						objSolicitudAdmisionCustom.setColonia(objSolicitudDeAdmision.getColonia());
						objSolicitudAdmisionCustom.setTelefono(objSolicitudDeAdmision.getTelefono());
						objSolicitudAdmisionCustom.setOtroTelefonoContacto(objSolicitudDeAdmision.getOtroTelefonoContacto());
						objSolicitudAdmisionCustom.setPromedioGeneral(objSolicitudDeAdmision.getPromedioGeneral());
						objSolicitudAdmisionCustom.setComprobanteCalificaciones(objSolicitudDeAdmision.getComprobanteCalificaciones());
						//objSolicitudAdmisionCustom.setCiudadExamen(objSolicitudDeAdmision.getCiudadExamen());
						objSolicitudAdmisionCustom.setTaskId(objHumanTaskInstance.getId());
						
													
						objCatGestionEscolar = new CatGestionEscolar();
						if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){
							objCatGestionEscolar.setPersistenceId(objSolicitudDeAdmision.getCatGestionEscolar().getPersistenceId());
							objCatGestionEscolar.setPersistenceVersion(objSolicitudDeAdmision.getCatGestionEscolar().getPersistenceVersion());
							objCatGestionEscolar.setNombre(objSolicitudDeAdmision.getCatGestionEscolar().getNombre());
							objCatGestionEscolar.setDescripcion(objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion());
							objCatGestionEscolar.setEnlace(objSolicitudDeAdmision.getCatGestionEscolar().getEnlace());
							objCatGestionEscolar.setPropedeutico(objSolicitudDeAdmision.getCatGestionEscolar().isPropedeutico());
							objCatGestionEscolar.setProgramaparcial(objSolicitudDeAdmision.getCatGestionEscolar().isProgramaparcial());
							objCatGestionEscolar.setMatematicas(objSolicitudDeAdmision.getCatGestionEscolar().isMatematicas());
							objCatGestionEscolar.setInscripcionenero(objSolicitudDeAdmision.getCatGestionEscolar().getInscripcionenero());
							objCatGestionEscolar.setInscripcionagosto(objSolicitudDeAdmision.getCatGestionEscolar().getInscripcionagosto());
							objCatGestionEscolar.setIsEliminado(objSolicitudDeAdmision.getCatGestionEscolar().isIsEliminado());
							objCatGestionEscolar.setUsuarioCreacion(objSolicitudDeAdmision.getCatGestionEscolar().getUsuarioCreacion());
							objCatGestionEscolar.setCampus(objSolicitudDeAdmision.getCatGestionEscolar().getCampus());
							objCatGestionEscolar.setCaseId(objSolicitudDeAdmision.getCatGestionEscolar().getCaseId());
						}
						objSolicitudAdmisionCustom.setObjCatGestionEscolar(objCatGestionEscolar);
						
						objCatCampusCustom = new CatCampusCustom();
						if(objSolicitudDeAdmision.getCatCampus() != null){
							objCatCampusCustom.setPersistenceId(objSolicitudDeAdmision.getCatCampus().getPersistenceId());
							objCatCampusCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatCampus().getPersistenceVersion());
							objCatCampusCustom.setDescripcion(objSolicitudDeAdmision.getCatCampus().getDescripcion());
							objCatCampusCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatCampus().getUsuarioBanner());
							objCatCampusCustom.setClave(objSolicitudDeAdmision.getCatCampus().getClave());
						}
						objSolicitudAdmisionCustom.setCatCampus(objCatCampusCustom);
						
						objCatPeriodoCustom = new CatPeriodoCustom();
						if(objSolicitudDeAdmision.getCatPeriodo() != null){
							objCatPeriodoCustom.setPersistenceId(objSolicitudDeAdmision.getCatPeriodo().getPersistenceId());
							objCatPeriodoCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatPeriodo().getPersistenceVersion());
							objCatPeriodoCustom.setDescripcion(objSolicitudDeAdmision.getCatPeriodo().getDescripcion());
							objCatPeriodoCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatPeriodo().getUsuarioBanner());
							objCatPeriodoCustom.setClave(objSolicitudDeAdmision.getCatPeriodo().getClave());
						}
						objSolicitudAdmisionCustom.setCatPeriodo(objCatPeriodoCustom);
						
						objCatEstadosCustom = new CatEstadosCustom();
						if(objSolicitudDeAdmision.getCatEstado() != null){
							objCatEstadosCustom.setPersistenceId(objSolicitudDeAdmision.getCatEstado().getPersistenceId());
							objCatEstadosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatEstado().getPersistenceVersion());
							objCatEstadosCustom.setClave(objSolicitudDeAdmision.getCatEstado().getClave());
							objCatEstadosCustom.setDescripcion(objSolicitudDeAdmision.getCatEstado().getDescripcion());
							objCatEstadosCustom.setUsuarioCreacion(objSolicitudDeAdmision.getCatEstado().getUsuarioCreacion());
						}
						objSolicitudAdmisionCustom.setCatEstado(objCatEstadosCustom);
						objSolicitudAdmisionCustom.setCatLicenciatura(objCatLicenciaturaCustom);
						
						objCatBachilleratosCustom = new CatBachilleratosCustom();
						if(objSolicitudDeAdmision.getCatBachilleratos() != null){
							objCatBachilleratosCustom.setPersistenceId(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceId());
							objCatBachilleratosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceVersion());
							objCatBachilleratosCustom.setClave(objSolicitudDeAdmision.getCatBachilleratos().getClave());
							objCatBachilleratosCustom.setDescripcion(objSolicitudDeAdmision.getCatBachilleratos().getDescripcion());
							objCatBachilleratosCustom.setPais(objSolicitudDeAdmision.getCatBachilleratos().getPais());
							objCatBachilleratosCustom.setEstado(objSolicitudDeAdmision.getCatBachilleratos().getEstado());
							objCatBachilleratosCustom.setCiudad(objSolicitudDeAdmision.getCatBachilleratos().getCiudad());
						}
						objSolicitudAdmisionCustom.setCatBachilleratos(objCatBachilleratosCustom);
						
						objSolicitudAdmisionCustom.setFotografiaB64(encoded);
						objSolicitudAdmisionCustom.setEstatusSolicitud(objSolicitudDeAdmision.getEstatusSolicitud());
						objSolicitudAdmisionCustom.setUpdateDate(dfSalida.format(objHumanTaskInstance.getReachedStateDate()));
						
						lstResultado.add(objSolicitudAdmisionCustom);
					}
				}
			}
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result getDocumentoTest(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Long caseId =11001L;
		try {
				
			for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(caseId, "DocInformacionDocumento", 0, 10)) {
				String encoded ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
			}
			
			
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result getAspirantesByStatus(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<SolicitudAdmisionCustom> lstResultado = new ArrayList<SolicitudAdmisionCustom>();
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstAllSolicitud = new ArrayList<SolicitudDeAdmision>();
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		Integer start = 0;
		Integer end = 99999;
		Integer inicioContador = 0;
		Integer finContador = 0;
		

		SolicitudDeAdmision objSolicitudDeAdmision = null;
		SolicitudAdmisionCustom objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
		CatCampusCustom objCatCampusCustom = new CatCampusCustom();
		CatPeriodoCustom objCatPeriodoCustom = new CatPeriodoCustom();
		CatEstadosCustom objCatEstadosCustom = new CatEstadosCustom();
		CatLicenciaturaCustom objCatLicenciaturaCustom = new CatLicenciaturaCustom();
		CatBachilleratosCustom objCatBachilleratosCustom = new CatBachilleratosCustom();
		CatGestionEscolar objCatGestionEscolar = new CatGestionEscolar();
		DetalleSolicitudCustom objDetalleSolicitud = new DetalleSolicitudCustom();
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		
		Boolean isFound = true;
		Boolean isFoundCampus = false;
		
		String nombreCandidato = "";
		String nombreProceso = "";
		String estatusSolicitud = "";
		
		String gestionEscolar = "";
		String catCampusStr = "";
		String catPeriodoStr = "";
		String catEstadoStr = "";
		String catBachilleratoStr = "";		
		String strError = "";
		ProcessDefinition objProcessDefinition;
		
		DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			estatusSolicitud = object.estatusSolicitud
			
			assert object instanceof Map;
			if(object.lstFiltro != null) {
				assert object.lstFiltro instanceof List;
			}

			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Cancún");
			objGrupoCampus.put("valor","CAMPUS-CANCUN");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Mayab");
			objGrupoCampus.put("valor","CAMPUS-MAYAB");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac México Norte");
			objGrupoCampus.put("valor","CAMPUS-MNORTE");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac México Sur");
			objGrupoCampus.put("valor","CAMPUS-MSUR");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Oaxaca");
			objGrupoCampus.put("valor","CAMPUS-OAXACA");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Puebla");
			objGrupoCampus.put("valor","CAMPUS-PUEBLA");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Querétaro");
			objGrupoCampus.put("valor","CAMPUS-QUERETARO");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Xalapa");
			objGrupoCampus.put("valor","CAMPUS-XALAPA");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Querétaro");
			objGrupoCampus.put("valor","CAMPUS-QUERETARO");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Juan Pablo II");
			objGrupoCampus.put("valor","CAMPUS-JP2");
			lstGrupoCampus.add(objGrupoCampus);
					
			userLogged = context.getApiSession().getUserId();
			LOGGER.error userLogged+""; 
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(Map<String, String> rowGrupo : lstGrupoCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.get("valor"))) {
						lstGrupo.add(rowGrupo.get("descripcion"));
						break;
					}
				}
			}
			LOGGER.error "for mebership";
			
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def procesoCasoDAO = context.apiClient.getDAO(ProcesoCasoDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			
			//context.getApiClient().getProcessAPI().getHumanTaskInstances(rootProcessInstanceId, taskName, startIndex, maxResults)
			LOGGER.error "--------------------------------------------------------------------------------------------------"
			/*for(ProcessInstance objProcessInstance : lstProcessInstance) {
				objProcessInstance = lstProcessInstance.get(0).getState();
				LOGGER.error "RootProcessInstance "+objProcessInstance.getRootProcessInstanceId();
			}*/
			LOGGER.error "--------------------------------------------------------------------------------------------------"
			

			lstAllSolicitud = objSolicitudDeAdmisionDAO.find(0, 99999);
			
			for(SolicitudDeAdmision objAllSolicitud : lstAllSolicitud) {
				objAllSolicitud.getCaseId()
			}
			/*
			Long processDefinitionId = context.getApiClient().getProcessAPI().getProcessDefinitionId("Proceso admisiones", "1.0");
			
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, processDefinitionId.toString());*/
			SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Proceso admisiones");
			final SearchOptions searchOptionsProccess = searchBuilderProccess.done();
			SearchResult<ProcessDeploymentInfo> SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			List<ProcessDeploymentInfo> lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			
			//Long processDefinitionId = context.getApiClient().getProcessAPI().getProcessDefinitionId("Proceso admisiones", "1.0");
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			
			//searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
			inicioContador = 0;
			finContador = 0;
			for(ProcessDeploymentInfo  objProcessDeploymentInfo : lstProcessDeploymentInfo) {
				LOGGER.error objProcessDeploymentInfo.getProcessId().toString()+" - "+objProcessDeploymentInfo.getName() + " - " + objProcessDeploymentInfo.getVersion();
				if(inicioContador == 0) {
					LOGGER.error "IF"
					searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
				}
				else {
					LOGGER.error "ELSE"
					searchBuilder.or();
					searchBuilder.differentFrom(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
				}
				inicioContador++;
			}
			
			searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);
			
			final SearchOptions searchOptions = searchBuilder.done();
			SearchResult<HumanTaskInstance>  SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
			List<HumanTaskInstance> lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();

			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstanceSearch) {
								
				objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
				objCatCampusCustom = new CatCampusCustom();
				isFound = true;
				
				caseId = objHumanTaskInstance.getRootContainerId();
				lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end);
				
				lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(caseId.toString(), start, end);
				if(!lstDetalleSolicitud.empty)
				{
					objDetalleSolicitud = new DetalleSolicitudCustom();
					objDetalleSolicitud.setPersistenceId(lstDetalleSolicitud.get(0).getPersistenceId());
					objDetalleSolicitud.setPersistenceVersion(lstDetalleSolicitud.get(0).getPersistenceVersion());
					objDetalleSolicitud.setIdBanner(lstDetalleSolicitud.get(0).getIdBanner());
					objDetalleSolicitud.setIsCurpValidado(lstDetalleSolicitud.get(0).isIsCurpValidado());
					objDetalleSolicitud.setPromedioCoincide(lstDetalleSolicitud.get(0).isPromedioCoincide());
					objDetalleSolicitud.setRevisado(lstDetalleSolicitud.get(0).isRevisado());
					objDetalleSolicitud.setTipoAlumno(lstDetalleSolicitud.get(0).getTipoAlumno());
					objDetalleSolicitud.setDescuento(lstDetalleSolicitud.get(0).getDescuento());
					objDetalleSolicitud.setObservacionesDescuento(lstDetalleSolicitud.get(0).getObservacionesDescuento());
					objDetalleSolicitud.setObservacionesCambio(lstDetalleSolicitud.get(0).getObservacionesCambio());
					objDetalleSolicitud.setObservacionesRechazo(lstDetalleSolicitud.get(0).getObservacionesRechazo());
					objDetalleSolicitud.setObservacionesListaRoja(lstDetalleSolicitud.get(0).getObservacionesListaRoja());
					objDetalleSolicitud.setOrdenPago(lstDetalleSolicitud.get(0).getOrdenPago());
					objDetalleSolicitud.setCaseId(lstDetalleSolicitud.get(0).getCaseId());
					objSolicitudAdmisionCustom.setObjDetalleSolicitud(objDetalleSolicitud);
					
				}
				if(!lstSolicitudDeAdmision.empty){
					objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end).get(0);
					
					catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
					isFoundCampus = false;
					for(String objGrupo : lstGrupo) {
						if(catCampusStr.toLowerCase().equals(objGrupo.toLowerCase())) {
							isFoundCampus = true;
						}
					}
					if(isFoundCampus) {
						if(object.lstFiltro != null) {
							for(def row: object.lstFiltro) {
								isFound = false;
								assert row instanceof Map;
							
								nombreCandidato = objSolicitudDeAdmision.getPrimerNombre()+" "+objSolicitudDeAdmision.getSegundoNombre()+" "+objSolicitudDeAdmision.getApellidoPaterno()+" "+objSolicitudDeAdmision.getApellidoMaterno();
								if(row.operador == 'Que contenga') {
									if (row.columna == "NOMBRE") {
										if (nombreCandidato.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "EMAIL") {
										if (objSolicitudDeAdmision.getCorreoElectronico().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CURP") {
										if (objSolicitudDeAdmision.getCurp().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CAMPUS") {
										catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
										if(catCampusStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROGRAMA") {
										gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null ? "" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
										if(gestionEscolar.contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "INGRESO") {
										catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null ? "" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
										if(catPeriodoStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTADO") {
										catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
										if(catEstadoStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PREPARATORIA") {
										catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null ? "" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
										if(catBachilleratoStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROMEDIO") {
										if(objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTATUS") {
										if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna.toString() == "ID BANNER") {
										if(objDetalleSolicitud.getIdBanner().toString().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}
									else
									if (row.columna == "TELÉFONO") {
										if(objSolicitudDeAdmision.getTelefonoCelular().toString().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}
									else
									if (row.columna == "MOTIVO RECHAZO") {
										if(objDetalleSolicitud.getObservacionesRechazo().toString().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "MOTIVO LISTA ROJA") {
										if(objDetalleSolicitud.getObservacionesListaRoja().toString().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}

									
								}
								else {
									if (row.columna == "NOMBRE") {
										if (nombreCandidato.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "EMAIL") {
										if (objSolicitudDeAdmision.getCorreoElectronico().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CURP") {
										if (objSolicitudDeAdmision.getCurp().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CAMPUS") {
										catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
										if(catCampusStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROGRAMA") {
										gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null ? "" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
										if(gestionEscolar.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "INGRESO") {
										catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null ? "" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
										if(catPeriodoStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTADO") {
										catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
										if(catEstadoStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PREPARATORIA") {
										catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null ? "" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
										if(catBachilleratoStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROMEDIO") {
										if(objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTATUS") {
										if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ID BANNER") {
										
										if(objDetalleSolicitud.getIdBanner().toString().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}
									else
									if (row.columna == "TELÉFONO") {
										if(objSolicitudDeAdmision.getTelefonoCelular().toString().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}
									else
									if (row.columna == "MOTIVO RECHAZO") {
										if(objDetalleSolicitud.getObservacionesRechazo().toString().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}
									else
									if (row.columna == "MOTIVO LISTA ROJA") {
										if(objDetalleSolicitud.getObservacionesListaRoja().toString().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}
								}
							}
						}
					}
					else {
						isFound = false;
					}
					LOGGER.error "isFound: "+isFound;
					if(isFound) {
						
						
						LOGGER.error "IF"
						LOGGER.error "isFound: "+objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase()
						if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(estatusSolicitud.toLowerCase())) {
							String encoded = "";
							for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(objSolicitudDeAdmision.getCaseId(), "fotoPasaporte", 0, 10)) {
								//encoded ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
								encoded = "../API/formsDocumentImage?document="+doc.getId();
							}
							objProcessDefinition = context.getApiClient().getProcessAPI().getProcessDefinition(objHumanTaskInstance.getProcessDefinitionId());
							objSolicitudAdmisionCustom.setTaskName(objHumanTaskInstance.getName());
							objSolicitudAdmisionCustom.setProcessName(objProcessDefinition.getName());
							objSolicitudAdmisionCustom.setProcessVersion(objProcessDefinition.getVersion());
							objSolicitudAdmisionCustom.setPersistenceId(objSolicitudDeAdmision.getPersistenceId());
							objSolicitudAdmisionCustom.setPersistenceVersion(objSolicitudDeAdmision.getPersistenceVersion());
							objSolicitudAdmisionCustom.setCaseId(objSolicitudDeAdmision.getCaseId());
							objSolicitudAdmisionCustom.setPrimerNombre(objSolicitudDeAdmision.getPrimerNombre());
							objSolicitudAdmisionCustom.setSegundoNombre(objSolicitudDeAdmision.getSegundoNombre());
							objSolicitudAdmisionCustom.setApellidoPaterno(objSolicitudDeAdmision.getApellidoPaterno());
							objSolicitudAdmisionCustom.setApellidoMaterno(objSolicitudDeAdmision.getApellidoMaterno());
							objSolicitudAdmisionCustom.setCorreoElectronico(objSolicitudDeAdmision.getCorreoElectronico());
							objSolicitudAdmisionCustom.setCurp(objSolicitudDeAdmision.getCurp());
							objSolicitudAdmisionCustom.setUsuarioFacebook(objSolicitudDeAdmision.getUsuarioFacebook());
							objSolicitudAdmisionCustom.setUsiarioTwitter(objSolicitudDeAdmision.getUsiarioTwitter());
							objSolicitudAdmisionCustom.setUsuarioInstagram(objSolicitudDeAdmision.getUsuarioInstagram());
							objSolicitudAdmisionCustom.setTelefonoCelular(objSolicitudDeAdmision.getTelefonoCelular());
							objSolicitudAdmisionCustom.setFoto(objSolicitudDeAdmision.getFoto());
							objSolicitudAdmisionCustom.setActaNacimiento(objSolicitudDeAdmision.getActaNacimiento());
							objSolicitudAdmisionCustom.setCalle(objSolicitudDeAdmision.getCalle());
							objSolicitudAdmisionCustom.setCodigoPostal(objSolicitudDeAdmision.getCodigoPostal());
							objSolicitudAdmisionCustom.setCiudad(objSolicitudDeAdmision.getCiudad());
							objSolicitudAdmisionCustom.setCalle2(objSolicitudDeAdmision.getCalle2());
							objSolicitudAdmisionCustom.setNumExterior(objSolicitudDeAdmision.getNumExterior());
							objSolicitudAdmisionCustom.setNumInterior(objSolicitudDeAdmision.getNumInterior());
							objSolicitudAdmisionCustom.setColonia(objSolicitudDeAdmision.getColonia());
							objSolicitudAdmisionCustom.setTelefono(objSolicitudDeAdmision.getTelefono());
							objSolicitudAdmisionCustom.setOtroTelefonoContacto(objSolicitudDeAdmision.getOtroTelefonoContacto());
							objSolicitudAdmisionCustom.setPromedioGeneral(objSolicitudDeAdmision.getPromedioGeneral());
							objSolicitudAdmisionCustom.setComprobanteCalificaciones(objSolicitudDeAdmision.getComprobanteCalificaciones());
							//objSolicitudAdmisionCustom.setCiudadExamen(objSolicitudDeAdmision.getCiudadExamen());
							objSolicitudAdmisionCustom.setTaskId(objHumanTaskInstance.getId());
							
														
							objCatGestionEscolar = new CatGestionEscolar();
							if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){";
								objCatGestionEscolar.setPersistenceId(objSolicitudDeAdmision.getCatGestionEscolar().getPersistenceId());
								objCatGestionEscolar.setPersistenceVersion(objSolicitudDeAdmision.getCatGestionEscolar().getPersistenceVersion());
								objCatGestionEscolar.setNombre(objSolicitudDeAdmision.getCatGestionEscolar().getNombre());
								objCatGestionEscolar.setDescripcion(objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion());
								objCatGestionEscolar.setEnlace(objSolicitudDeAdmision.getCatGestionEscolar().getEnlace());
								objCatGestionEscolar.setPropedeutico(objSolicitudDeAdmision.getCatGestionEscolar().isPropedeutico());
								objCatGestionEscolar.setProgramaparcial(objSolicitudDeAdmision.getCatGestionEscolar().isProgramaparcial());
								objCatGestionEscolar.setMatematicas(objSolicitudDeAdmision.getCatGestionEscolar().isMatematicas());
								objCatGestionEscolar.setInscripcionenero(objSolicitudDeAdmision.getCatGestionEscolar().getInscripcionenero());
								objCatGestionEscolar.setInscripcionagosto(objSolicitudDeAdmision.getCatGestionEscolar().getInscripcionagosto());
								objCatGestionEscolar.setIsEliminado(objSolicitudDeAdmision.getCatGestionEscolar().isIsEliminado());
								objCatGestionEscolar.setUsuarioCreacion(objSolicitudDeAdmision.getCatGestionEscolar().getUsuarioCreacion());
								objCatGestionEscolar.setCampus(objSolicitudDeAdmision.getCatGestionEscolar().getCampus());
								objCatGestionEscolar.setCaseId(objSolicitudDeAdmision.getCatGestionEscolar().getCaseId());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){";
							}
							objSolicitudAdmisionCustom.setObjCatGestionEscolar(objCatGestionEscolar);
							
							objCatCampusCustom = new CatCampusCustom();
							if(objSolicitudDeAdmision.getCatCampus() != null){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatCampus() != null){";
								objCatCampusCustom.setPersistenceId(objSolicitudDeAdmision.getCatCampus().getPersistenceId());
								objCatCampusCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatCampus().getPersistenceVersion());
								objCatCampusCustom.setDescripcion(objSolicitudDeAdmision.getCatCampus().getDescripcion());
								objCatCampusCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatCampus().getUsuarioBanner());
								objCatCampusCustom.setClave(objSolicitudDeAdmision.getCatCampus().getClave());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatCampus() != null){";
							}
							objSolicitudAdmisionCustom.setCatCampus(objCatCampusCustom);
							
							objCatPeriodoCustom = new CatPeriodoCustom();
							if(objSolicitudDeAdmision.getCatPeriodo() != null){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatPeriodo() != null){";
								objCatPeriodoCustom.setPersistenceId(objSolicitudDeAdmision.getCatPeriodo().getPersistenceId());
								objCatPeriodoCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatPeriodo().getPersistenceVersion());
								objCatPeriodoCustom.setDescripcion(objSolicitudDeAdmision.getCatPeriodo().getDescripcion());
								objCatPeriodoCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatPeriodo().getUsuarioBanner());
								objCatPeriodoCustom.setClave(objSolicitudDeAdmision.getCatPeriodo().getClave());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatPeriodo() != null){";
							}
							objSolicitudAdmisionCustom.setCatPeriodo(objCatPeriodoCustom);
							
							objCatEstadosCustom = new CatEstadosCustom();
							if(objSolicitudDeAdmision.getCatEstado() != null){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatEstado() != null){";
								objCatEstadosCustom.setPersistenceId(objSolicitudDeAdmision.getCatEstado().getPersistenceId());
								objCatEstadosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatEstado().getPersistenceVersion());
								objCatEstadosCustom.setClave(objSolicitudDeAdmision.getCatEstado().getClave());
								objCatEstadosCustom.setDescripcion(objSolicitudDeAdmision.getCatEstado().getDescripcion());
								objCatEstadosCustom.setUsuarioCreacion(objSolicitudDeAdmision.getCatEstado().getUsuarioCreacion());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatEstado() != null){";
							}
							objSolicitudAdmisionCustom.setCatEstado(objCatEstadosCustom);

							objSolicitudAdmisionCustom.setUpdateDate(dfSalida.format(objHumanTaskInstance.getReachedStateDate()));
							objSolicitudAdmisionCustom.setCatLicenciatura(objCatLicenciaturaCustom);

							objCatBachilleratosCustom = new CatBachilleratosCustom();
							if(objSolicitudDeAdmision.getCatBachilleratos() != null){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatBachilleratos() != null){";
								objCatBachilleratosCustom.setPersistenceId(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceId());
								objCatBachilleratosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceVersion());
								objCatBachilleratosCustom.setClave(objSolicitudDeAdmision.getCatBachilleratos().getClave());
								objCatBachilleratosCustom.setDescripcion(objSolicitudDeAdmision.getCatBachilleratos().getDescripcion());
								objCatBachilleratosCustom.setPais(objSolicitudDeAdmision.getCatBachilleratos().getPais());
								objCatBachilleratosCustom.setEstado(objSolicitudDeAdmision.getCatBachilleratos().getEstado());
								objCatBachilleratosCustom.setCiudad(objSolicitudDeAdmision.getCatBachilleratos().getCiudad());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatBachilleratos() != null){";
							}
							objSolicitudAdmisionCustom.setCatBachilleratos(objCatBachilleratosCustom);
							
							objSolicitudAdmisionCustom.setFotografiaB64(encoded);
							objSolicitudAdmisionCustom.setEstatusSolicitud(objSolicitudDeAdmision.getEstatusSolicitud())
							lstResultado.add(objSolicitudAdmisionCustom);
						}
					}
				}
			}
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result getExcelFile(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams; 
			String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(type);
			
			if(type.equals("nuevas_solicitudes")) {
				dataResult = getNuevasSolicitudes(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				
				Row titleRow = sheet.createRow(++rowCount);
				Cell cellTitle = titleRow.createCell(2);
				cellTitle.setCellValue("NUEVAS SOLICITUDES");
				
				Row headersRow = sheet.createRow(++rowCount);
				Cell header1 = headersRow.createCell(0);
				header1.setCellValue("SOLICITUD");
				Cell header2 = headersRow.createCell(1);
				header2.setCellValue("NOMBRE");
				Cell header3 = headersRow.createCell(2);
				header3.setCellValue("EMAIL");
				Cell header4 = headersRow.createCell(3);
				header4.setCellValue("TELÉFONO");
				Cell header5= headersRow.createCell(4);
				header5.setCellValue("PREPARATORIA");
				Cell header6 = headersRow.createCell(5);
				header6.setCellValue("PROCEDENCIA");
				Cell header7 = headersRow.createCell(6);
				header7.setCellValue("PROMEDIO");
				Cell header8 = headersRow.createCell(7);
				header8.setCellValue("FECHA DE ENVIO");
				SolicitudAdmisionCustom  solicitud = new SolicitudAdmisionCustom();
				
				for (int i = 0; i < lstParams.size(); ++i){
					solicitud = new SolicitudAdmisionCustom();
					solicitud = (SolicitudAdmisionCustom)lstParams.get(i);
					Row row = sheet.createRow(++rowCount);
					Cell cell1 = row.createCell(0);
					cell1.setCellValue(solicitud.getPersistenceId().toString());
					Cell cell2 = row.createCell(1);
					cell2.setCellValue(
						solicitud.getPrimerNombre() + " " + 
						solicitud.getSegundoNombre() + " " +
						solicitud.getApellidoPaterno() + " " +
						solicitud.getApellidoMaterno()
					);
					Cell cell3 = row.createCell(2);
					cell3.setCellValue(solicitud.getCorreoElectronico());
					Cell cell4 = row.createCell(3);
					cell4.setCellValue(solicitud.getTelefono());
					Cell cell5= row.createCell(4);
					cell5.setCellValue(solicitud.getCatBachilleratos().getDescripcion());
					Cell cell6 = row.createCell(5);
					cell6.setCellValue(solicitud.getCatEstado().getDescripcion());
					Cell cell7 = row.createCell(6);
					cell7.setCellValue(solicitud.getPromedioGeneral());
					Cell cell8 = row.createCell(7);
					cell8.setCellValue("");
				}
			} else if (type.equals("aspirantes_proceso")) {
				dataResult = getAspirantesProceso(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				
				Row titleRow = sheet.createRow(++rowCount);
				Cell cellTitle = titleRow.createCell(2);
				cellTitle.setCellValue("ASPITANTES EN PROCESO");
				
				Row headersRow = sheet.createRow(++rowCount);
				Cell header1 = headersRow.createCell(0);
				header1.setCellValue("NOMBRE");
				Cell header2 = headersRow.createCell(1);
				header2.setCellValue("EMAIL");
				Cell header3 = headersRow.createCell(2);
				header3.setCellValue("CURP");
				Cell header4 = headersRow.createCell(3);
				header4.setCellValue("CAMPUS");
				Cell header5= headersRow.createCell(4);
				header5.setCellValue("PROGRAMA");
				Cell header6 = headersRow.createCell(5);
				header6.setCellValue("INGRESO ESTADO");
				Cell header7 = headersRow.createCell(6);
				header7.setCellValue("PREPARATORIA");
				Cell header8 = headersRow.createCell(7);
				header8.setCellValue("PROMEDIO");
				Cell header9 = headersRow.createCell(8);
				header9.setCellValue("ESTATUS");
				Cell header10 = headersRow.createCell(9);
				header10.setCellValue("TIPO");
				Cell header11 = headersRow.createCell(7);
				header11.setCellValue("ÚLTIMA MODIFICACION");
				
				for (int i = 0; i < lstParams.size(); ++i){
					SolicitudAdmisionCustom  solicitud = (SolicitudAdmisionCustom) lstParams.get(i);
					Row row = sheet.createRow(++rowCount);
					Cell cell1 = row.createCell(0);
					cell1.setCellValue(
						solicitud.getPrimerNombre() + " " +
						solicitud.getSegundoNombre() + " " +
						solicitud.getApellidoPaterno() + " " +
						solicitud.getApellidoMaterno()
					);
					Cell cell2 = row.createCell(1);
					cell2.setCellValue(solicitud.getCorreoElectronico());
					Cell cell3 = row.createCell(2);
					cell3.setCellValue(solicitud.getCurp());
					Cell cell4 = row.createCell(3);
					cell4.setCellValue(solicitud.getCatCampus().getDescripcion());
					Cell cell5= row.createCell(4);
					cell5.setCellValue(solicitud.getCatLicenciatura().getDescripcion());
					Cell cell6 = row.createCell(5);
					cell6.setCellValue(solicitud.getCatEstado().getDescripcion());
					Cell cell7 = row.createCell(6);
					cell7.setCellValue(solicitud.getCatBachilleratos().getDescripcion());
					Cell cell8 = row.createCell(7);
					cell8.setCellValue(solicitud.getPromedioGeneral());
					Cell cell9 = row.createCell(8);
					cell9.setCellValue(solicitud.getEstatusSolicitud());
					Cell cell10 = row.createCell(9);
					cell10.setCellValue(solicitud.getObjDetalleSolicitud() == null ? "" : solicitud.getObjDetalleSolicitud().getTipoAlumno());
					Cell cell11 = row.createCell(10);
					cell11.setCellValue("");
				}
			} else if (type.equals("lista_roja") || type.equals("aspirantes_rechazados")) {
				dataResult = getAspirantesByStatus(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				
				String title = object.estatusSolicitud;
				Row titleRow = sheet.createRow(++rowCount);
				Cell cellTitle = titleRow.createCell(2);
				cellTitle.setCellValue(title);
				
				/*ID Banner	
				Nombre / Email / Teléfono	
				Campus / Programa / Ingreso	
				Promedio	
				Motivo de lista roja*/
				
				Row headersRow = sheet.createRow(++rowCount);
				Cell header1 = headersRow.createCell(0);
				header1.setCellValue("SOLICITUD");
				Cell header2 = headersRow.createCell(1);
				header2.setCellValue("NOMBRE");
				Cell header3 = headersRow.createCell(2);
				header3.setCellValue("EMAIL");
				Cell header4 = headersRow.createCell(3);
				header4.setCellValue("TELÉFONO");
				Cell header5= headersRow.createCell(4);
				header5.setCellValue("CAMPUS");
				Cell header6 = headersRow.createCell(5);
				header6.setCellValue("PROGRAMA");
				Cell header7 = headersRow.createCell(6);
				header7.setCellValue("INGRESO");
				Cell header8 = headersRow.createCell(7);
				header8.setCellValue("PROMEDIO");
				Cell header9 = headersRow.createCell(7);
				header9.setCellValue(type.equals("lista_roja") ? "MOTIVO DE LISTA ROJA" : "MOTIVO DE RECHAZO");
				SolicitudAdmisionCustom  solicitud = new SolicitudAdmisionCustom();
				
				for (int i = 0; i < lstParams.size(); ++i){
					solicitud = new SolicitudAdmisionCustom();
					solicitud = (SolicitudAdmisionCustom)lstParams.get(i);
					Row row = sheet.createRow(++rowCount);
					Cell cell1 = row.createCell(0);
					cell1.setCellValue(solicitud.getPersistenceId().toString());
					Cell cell2 = row.createCell(1);
					cell2.setCellValue(
						solicitud.getPrimerNombre() + " " +
						solicitud.getSegundoNombre() + " " +
						solicitud.getApellidoPaterno() + " " +
						solicitud.getApellidoMaterno()
					);
					Cell cell3 = row.createCell(2);
					cell3.setCellValue(solicitud.getCorreoElectronico());
					Cell cell4 = row.createCell(3);
					cell4.setCellValue(solicitud.getTelefono());
					Cell cell5= row.createCell(4);
					cell5.setCellValue(solicitud.getCatCampus().getDescripcion());
					Cell cell6 = row.createCell(5);
					cell6.setCellValue(solicitud.getCatLicenciatura().getDescripcion());
					Cell cell7 = row.createCell(6);
					cell7.setCellValue(solicitud.getPromedioGeneral());
					Cell cell8 = row.createCell(7);
					cell8.setCellValue(solicitud.getPromedioGeneral());
					Cell cell9 = row.createCell(8);
					String motivo = type.equals("lista_roja") ? solicitud.getObjDetalleSolicitud().getObservacionesListaRoja() : solicitud.getObjDetalleSolicitud().getObservacionesRechazo();
					cell9.setCellValue(motivo);
				}
			} 
			
			FileOutputStream outputStream = new FileOutputStream("Report.xls");
			workbook.write(outputStream);
			
			List<Object> lstResultado = new ArrayList<Object>();
			lstResultado.add(encodeFileToBase64Binary("Report.xls"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
					
		} catch (Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	public Result getPdfFile(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams;
			String type = object.type;
			
			def documento="Report.pdf"
			DocumentItext document = new DocumentItext();
			document.setPageSize(PageSize.A4.rotate());
			PdfWriter.getInstance(document, new FileOutputStream(documento));
			
			if(type.equals("nuevas_solicitudes")) {
				dataResult = getNuevasSolicitudes(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				
				document.open();
				document.add(new Paragraph("NUEVAS SOLICITUDES"));
				PdfPTable table = new PdfPTable(8);
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				
				header.setPhrase(new Phrase("SOLICITUD"));
				table.addCell(header);
				header.setPhrase(new Phrase("NOMBRE"));
				table.addCell(header);
				header.setPhrase(new Phrase("EMAIL"))
				table.addCell(header);
				header.setPhrase(new Phrase("TELÉFONO"))
				table.addCell(header);
				header.setPhrase(new Phrase("PREPARATORIA"))
				table.addCell(header);
				header.setPhrase(new Phrase("PROCEDENCIA"))
				table.addCell(header);
				header.setPhrase(new Phrase("PROMEDIO"))
				table.addCell(header);
				header.setPhrase(new Phrase("FECHA DE ENVIO"))
				table.addCell(header);
				
				SolicitudAdmisionCustom  solicitud = new SolicitudAdmisionCustom();
				
				for (int i = 0; i < lstParams.size(); ++i){
					solicitud = new SolicitudAdmisionCustom();
					solicitud = (SolicitudAdmisionCustom)lstParams.get(i);
					table.addCell(new Phrase(solicitud.getPersistenceId().toString()));
					table.addCell(new Phrase(
						solicitud.getPrimerNombre() + " " +
						solicitud.getSegundoNombre() + " " +
						solicitud.getApellidoPaterno() + " " +
						solicitud.getApellidoMaterno())
					);
					table.addCell(new Phrase(solicitud.getCorreoElectronico()));
					table.addCell(new Phrase(solicitud.getTelefono()));
					table.addCell(new Phrase(solicitud.getCatBachilleratos().getDescripcion()));
					table.addCell(new Phrase(solicitud.getCatEstado().getDescripcion()));
					table.addCell(new Phrase(solicitud.getPromedioGeneral()));
					table.addCell(new Phrase(""));
				}
				
				document.add(table);
				document.close();
			} else if (type.equals("aspirantes_proceso")) {
				dataResult = getAspirantesProceso(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				
				document.open();
				document.add(new Paragraph("ASPIRANTES EN PROCESO"));
				PdfPTable table = new PdfPTable(12);
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				
				header.setPhrase(new Phrase("NOMBRE"));
				table.addCell(header);
				header.setPhrase(new Phrase("EMAIL"));
				table.addCell(header);
				header.setPhrase(new Phrase("CURP"));
				table.addCell(header);
				header.setPhrase(new Phrase("CAMPUS"));
				table.addCell(header);
				header.setPhrase(new Phrase("PROGRAMA"));
				table.addCell(header);
				header.setPhrase(new Phrase("INGRESO"));
				table.addCell(header);
				header.setPhrase(new Phrase("ESTADO"));
				table.addCell(header);
				header.setPhrase(new Phrase("PREPARATORIA"));
				table.addCell(header);
				header.setPhrase(new Phrase("PROMEDIO"));
				table.addCell(header);
				header.setPhrase(new Phrase("ESTATUS"));
				table.addCell(header);
				header.setPhrase(new Phrase("TIPO"))
				table.addCell(header);
				header.setPhrase(new Phrase("ÚLTIMA MODIFICACION"))
				table.addCell(header);
				
				SolicitudAdmisionCustom  solicitud = new SolicitudAdmisionCustom();
				
				for (int i = 0; i < lstParams.size(); ++i){
					solicitud = new SolicitudAdmisionCustom();
					solicitud = (SolicitudAdmisionCustom)lstParams.get(i);
					String phraseToInput = new String();
					
					phraseToInput = solicitud.getPrimerNombre() + " " +
						solicitud.getSegundoNombre() + " " +
						solicitud.getApellidoPaterno() + " " +
						solicitud.getApellidoMaterno();
					table.addCell(new Phrase(phraseToInput = null ? "" : phraseToInput));
					phraseToInput = solicitud.getCorreoElectronico();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput));
					phraseToInput = solicitud.getCurp();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput));
					phraseToInput = solicitud.getCatCampus().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput));
					phraseToInput = solicitud.getCatLicenciatura().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput));
					phraseToInput = solicitud.getCatPeriodo().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput));
					phraseToInput = solicitud.getCatEstado().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput));
					phraseToInput = solicitud.getCatBachilleratos().getDescripcion()
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput));
					phraseToInput = solicitud.getPromedioGeneral();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput));
					phraseToInput = solicitud.getEstatusSolicitud();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput));
					phraseToInput = solicitud.getObjDetalleSolicitud() == null ? "" :solicitud.getObjDetalleSolicitud().getTipoAlumno();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput));
					table.addCell(new Phrase(""));
				}
				
				document.add(table);
				document.close();
			} else if (type.equals("lista_roja") || type.equals("aspirantes_rechazados")) {
				dataResult = getAspirantesByStatus(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				
				String title = object.estatusSolicitud;
				
				document.open();
				document.add(new Paragraph("ASPIRANTES EN PROCESO"));
				PdfPTable table = new PdfPTable(9);
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				
				header.setPhrase(new Phrase("SOLICITUD"));
				table.addCell(header);
				header.setPhrase(new Phrase("NOMBRE"));
				table.addCell(header);
				header.setPhrase(new Phrase("EMAIL"));
				table.addCell(header);
				header.setPhrase(new Phrase("TELÉFONO"));
				table.addCell(header);
				header.setPhrase(new Phrase("CAMPUS"));
				table.addCell(header);
				header.setPhrase(new Phrase("PROGRAMA"));
				table.addCell(header);
				header.setPhrase(new Phrase("INGRESO"));
				table.addCell(header);
				header.setPhrase(new Phrase("PROMEDIO"));
				table.addCell(header);
				header.setPhrase(new Phrase(type.equals("lista_roja") ? "MOTIVO DE LISTA ROJA" : "MOTIVO DE RECHAZO"));
				table.addCell(header);
				
				SolicitudAdmisionCustom  solicitud = new SolicitudAdmisionCustom();
				
				for (int i = 0; i < lstParams.size(); ++i){
					solicitud = new SolicitudAdmisionCustom();
					solicitud = (SolicitudAdmisionCustom)lstParams.get(i);
					String phraseToInput = new String();
					table.addCell(new Phrase(solicitud.getPersistenceId().toString()));
					table.addCell(new Phrase(
						solicitud.getPrimerNombre() + " " +
						solicitud.getSegundoNombre() + " " +
						solicitud.getApellidoPaterno() + " " +
						solicitud.getApellidoMaterno())
					);
					table.addCell(new Phrase(solicitud.getCorreoElectronico()));
					table.addCell(new Phrase(solicitud.getTelefono()));
					phraseToInput = solicitud.getCatCampus().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput));
					phraseToInput = solicitud.getCatLicenciatura().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput));
					phraseToInput = solicitud.getCatPeriodo().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput));
					table.addCell(new Phrase(solicitud.getPromedioGeneral()));
					phraseToInput = type.equals("lista_roja") ? solicitud.getObjDetalleSolicitud().getObservacionesListaRoja(): solicitud.getObjDetalleSolicitud().getObservacionesRechazo();
					table.addCell(new Phrase(phraseToInput));
				}
			}
			
			List<Object> lstResultado = new ArrayList<Object>();
			lstResultado.add(encodeFileToBase64Binary("Report.pdf"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
					
		} catch (Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	
	public Result getExcelFileCatalogo(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams;
			String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(type);
			
			if(type.equals("gestion_escolar")) {
				dataResult = getGestionEscolar(parameterP, parameterC, jsonData, context);

				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				def campus= (object.campus == "CAMPUS-MNORTE"?"Anáhuac México Norte":object.campus == "CAMPUS-MSUR"?"Anáhuac México Sur":object.campus == "CAMPUS-MAYAB"?"Anáhuac Merida":object.campus =="CAMPUS-XALAPA"?"Anáhuac Xalapa":object.campus =="CAMPUS-CORDOBA"?"Anáhuac Cordoba":object.campus =="CAMPUS-CANCUN"?"Anáhuac Cancún":object.campus =="CAMPUS-OAXACA"?"Anáhuac Oaxaca":object.campus =="CAMPUS-PUEBLA"?"Anáhuac Puebla":object.campus =="CAMPUS-QUERETARO"?"Anáhuac Querétaro":"Juan Pablo II");
				Row titleRow = sheet.createRow(++rowCount);
				Cell cellTitle = titleRow.createCell(2);
				cellTitle.setCellValue("LISTADO DE LICENCIATURAS DEL CAMPUS \""+ campus+"\"");

				Row headersRow = sheet.createRow(++rowCount);
				Cell header1 = headersRow.createCell(0);
				header1.setCellValue("NOMBRE LICENCIATURA");
				Cell header2 = headersRow.createCell(1);
				header2.setCellValue("LIGA");
				Cell header3 = headersRow.createCell(2);
				header3.setCellValue("DESCRIPCION DE LA CARRERA");
				Cell header4 = headersRow.createCell(3);
				header4.setCellValue("INSCRIPCION DE ENERO");
				Cell header5= headersRow.createCell(4);
				header5.setCellValue("INSCRIPCION DE AGOSTO");
				Cell header6= headersRow.createCell(5);
				header6.setCellValue("PERIODOS");
				com.anahuac.catalogos.CatGestionEscolar Escolar = new com.anahuac.catalogos.CatGestionEscolar();

				for (int i = 0; i < lstParams.size(); ++i){
					Escolar = new com.anahuac.catalogos.CatGestionEscolar();
					Escolar = (com.anahuac.catalogos.CatGestionEscolar)lstParams.get(i);
					Row row = sheet.createRow(++rowCount);
					Cell cell1 = row.createCell(0);
					cell1.setCellValue(Escolar.getNombre().toString());
					Cell cell2 = row.createCell(1);
					cell2.setCellValue(Escolar.getEnlace().toString());
					Cell cell3 = row.createCell(2);
					cell3.setCellValue(Escolar.getDescripcion());
					Cell cell4 = row.createCell(3);
					cell4.setCellValue("\$"+Escolar.getInscripcionenero());
					Cell cell5= row.createCell(4);
					cell5.setCellValue("\$"+Escolar.getInscripcionagosto());
					com.anahuac.catalogos.CatPeriodo lstP = new com.anahuac.catalogos.CatPeriodo();
					def periodos = "";
					for(int f=0; f<Escolar.getPeriodoDisponible().size(); f++) {
						lstP = new com.anahuac.catalogos.CatPeriodo();
						lstP = (com.anahuac.catalogos.CatPeriodo)Escolar.getPeriodoDisponible().get(f);
						periodos +=lstP.getDescripcion() + ",";
					}
					Cell cell6= row.createCell(5);
					cell6.setCellValue(periodos);
				}
			}else if(type.equals("bachilleratos")) {
				dataResult = getBachilleratos(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				Row titleRow = sheet.createRow(++rowCount);
				Cell cellTitle = titleRow.createCell(2);
				cellTitle.setCellValue("LISTADO DE BACHILLERATOS");
				
				Row headersRow = sheet.createRow(++rowCount);
				Cell header1 = headersRow.createCell(0);
				header1.setCellValue("DESCRIPCION");
				Cell header2 = headersRow.createCell(1);
				header2.setCellValue("PAIS");
				Cell header3 = headersRow.createCell(2);
				header3.setCellValue("ESTADO");
				Cell header4 = headersRow.createCell(3);
				header4.setCellValue("CIUDAD");
				Cell header5= headersRow.createCell(4);
				header5.setCellValue("PERTENECE A LA RED");
				com.anahuac.catalogos.CatBachilleratos  bachillerato = new com.anahuac.catalogos.CatBachilleratos();
			
				for (int i = 0; i < lstParams.size(); ++i){
					bachillerato = new com.anahuac.catalogos.CatBachilleratos();
					bachillerato = (com.anahuac.catalogos.CatBachilleratos)lstParams.get(i);
					Row row = sheet.createRow(++rowCount);
					Cell cell1 = row.createCell(0);
					cell1.setCellValue(bachillerato.getDescripcion().toString());
					Cell cell2 = row.createCell(1);
					cell2.setCellValue(bachillerato.getPais());
					Cell cell3 = row.createCell(2);
					cell3.setCellValue(bachillerato.getEstado());
					Cell cell4 = row.createCell(3);
					cell4.setCellValue(bachillerato.getCiudad());
					Cell cell5= row.createCell(4);
					def red = bachillerato.isPerteneceRed()?'Sí':"No";
					cell5.setCellValue(red);
				}
			}
			
			FileOutputStream outputStream = new FileOutputStream("ReportCatalogo.xls");
			workbook.write(outputStream);
			
			List<Object> lstResultado = new ArrayList<Object>();
			lstResultado.add(encodeFileToBase64Binary("ReportCatalogo.xls"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
					
		} catch (Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	public Result getPdfFileCatalogo(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams;
			String type = object.type;
			
			def documento="ReportCatalogos.pdf"
			DocumentItext document = new DocumentItext();
			document.setPageSize(PageSize.A4.rotate());
			PdfWriter.getInstance(document, new FileOutputStream(documento));
			
			if(type.equals("gestion_escolar")) {
				dataResult = getGestionEscolar(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				def campus= (object.campus == "CAMPUS-MNORTE"?"Anáhuac México Norte":object.campus == "CAMPUS-MSUR"?"Anáhuac México Sur":object.campus == "CAMPUS-MAYAB"?"Anáhuac Merida":object.campus =="CAMPUS-XALAPA"?"Anáhuac Xalapa":object.campus =="CAMPUS-CORDOBA"?"Anáhuac Cordoba":object.campus =="CAMPUS-CANCUN"?"Anáhuac Cancún":object.campus =="CAMPUS-OAXACA"?"Anáhuac Oaxaca":object.campus =="CAMPUS-PUEBLA"?"Anáhuac Puebla":object.campus =="CAMPUS-QUERETARO"?"Anáhuac Querétaro":"Juan Pablo II");
				document.open();
				Paragraph preface = new Paragraph("LISTADO DE LICENCIATURAS DEL CAMPUS \""+ campus+"\"");
				preface.setAlignment(Paragraph.ALIGN_CENTER);
				document.add(preface);
				document.add( new Paragraph(" "))
				PdfPTable table = new PdfPTable(6);
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				
				header.setPhrase(new Phrase("NOMBRE lICENCIATURA"));
				table.addCell(header);
				header.setPhrase(new Phrase("LIGA"));
				table.addCell(header);
				header.setPhrase(new Phrase("DESCRIPCION DE LA CARRERA"))
				table.addCell(header);
				header.setPhrase(new Phrase("INSCRIPCION DE ENERO"))
				table.addCell(header);
				header.setPhrase(new Phrase("INSCRIPCION DE AGOSTO"))
				table.addCell(header);
				header.setPhrase(new Phrase("PERIODOS"))
				table.addCell(header);
				
				com.anahuac.catalogos.CatGestionEscolar  G_Escolar = new com.anahuac.catalogos.CatGestionEscolar();
				
				for (int i = 0; i < lstParams.size(); ++i){
					G_Escolar = new com.anahuac.catalogos.CatGestionEscolar();
					G_Escolar = (com.anahuac.catalogos.CatGestionEscolar)lstParams.get(i);
					table.addCell(new Phrase(G_Escolar.getNombre()));
					table.addCell(new Phrase(G_Escolar.getEnlace()));
					table.addCell(new Phrase(G_Escolar.getDescripcion()));
					table.addCell(new Phrase("\$"+G_Escolar.getInscripcionenero()));
					table.addCell(new Phrase("\$"+G_Escolar.getInscripcionagosto()));
					com.anahuac.catalogos.CatPeriodo lstP = new com.anahuac.catalogos.CatPeriodo();
					def periodos = "";
					for(int f=0; f<G_Escolar.getPeriodoDisponible().size(); f++) {
						lstP = new com.anahuac.catalogos.CatPeriodo();
						lstP = (com.anahuac.catalogos.CatPeriodo)G_Escolar.getPeriodoDisponible().get(f);
						periodos +=lstP.getDescripcion() + ",";
					}
					table.addCell(new Phrase(periodos));
					
				}
				
				document.add(table);
				document.close();
			}else if(type.equals("bachilleratos")) {
				dataResult = getBachilleratos(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				document.open();
				Paragraph preface = new Paragraph("LISTADO DE BACHILLERATOS");
				preface.setAlignment(Paragraph.ALIGN_CENTER);
				document.add(preface);
				document.add( new Paragraph(" "))
				PdfPTable table = new PdfPTable(5);
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				
				header.setPhrase(new Phrase("DESCRIPCION"));
				table.addCell(header);
				header.setPhrase(new Phrase("PAIS"));
				table.addCell(header);
				header.setPhrase(new Phrase("ESTADO"))
				table.addCell(header);
				header.setPhrase(new Phrase("CIUDAD"))
				table.addCell(header);
				header.setPhrase(new Phrase("PERTENECE A LA RED"))
				table.addCell(header);
				
				com.anahuac.catalogos.CatBachilleratos  bachillerato = new com.anahuac.catalogos.CatBachilleratos();
				
				for (int i = 0; i < lstParams.size(); ++i){
					bachillerato = new com.anahuac.catalogos.CatBachilleratos();
					bachillerato = (com.anahuac.catalogos.CatBachilleratos)lstParams.get(i);
					table.addCell(new Phrase(bachillerato.getDescripcion()));
					table.addCell(new Phrase(bachillerato.getPais()));
					table.addCell(new Phrase(bachillerato.getEstado()));
					table.addCell(new Phrase(bachillerato.getCiudad()));	
					def red = bachillerato.isPerteneceRed()?'Sí':"No";
					table.addCell(new Phrase(red));
					
				}
				
				document.add(table);
				document.close();
			}
			
			List<Object> lstResultado = new ArrayList<Object>();
			lstResultado.add(encodeFileToBase64Binary("ReportCatalogos.pdf"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
					
		} catch (Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	public Result getGestionEscolar(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context)  {
		Result resultado = new Result();
		
		List<CatGestionEscolar> lstResultado = new ArrayList<CatGestionEscolar>();
		
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		Integer start = 0;
		Integer end = 99999;
		Integer inicioContador = 0;
		Integer finContador = 0;
		
		List<com.anahuac.catalogos.CatGestionEscolar> lstGestionEscolar = new ArrayList<com.anahuac.catalogos.CatGestionEscolar>();
		
		String strError = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			def objGestionEscolarDAO = context.apiClient.getDAO(CatGestionEscolarDAO.class);
			lstGestionEscolar = objGestionEscolarDAO.getCatGestionEscolarByCampus(object.campus.toString(), 0, 9999)
			resultado.setError_info(strError);
			resultado.setData(lstGestionEscolar);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setError_info(strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result getBachilleratos(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context)  {
		Result resultado = new Result();	
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		Integer start = 0;
		Integer end = 99999;
		Integer inicioContador = 0;
		Integer finContador = 0;
		
		List<com.anahuac.catalogos.CatBachilleratos> lstBachilleratos = new ArrayList<com.anahuac.catalogos.CatBachilleratos>();
		
		String strError = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			LOGGER.error "====object==="
			LOGGER.error jsonData
			def objBachilleratosDAO = context.apiClient.getDAO(CatBachilleratosDAO.class);
			/*if(object.opcion == 2) {
				lstBachilleratos = objBachilleratosDAO.getCatBachilleratosByEstado(object.estado)
			}else if(object.opcion == 3) {
				lstBachilleratos = objBachilleratosDAO.getCatBachilleratosByPais(object.pais)
			}else {
				
			}*/
			LOGGER.error "====LLEGO==="
			lstBachilleratos = objBachilleratosDAO.getCatBachilleratos(0, 9999);
			LOGGER.error "====PASO==="
			
			resultado.setError_info(strError);
			resultado.setData(lstBachilleratos);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setError_info(strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	private String encodeFileToBase64Binary(String fileName)
	throws IOException {

		File file = new File(fileName);
		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encoder.encode(bytes);
		String encodedString = new String(encoded);
		
		return encodedString;
	}
	
	private static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int)length];
		
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
			   && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}
		
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "+file.getName());
		}
		
		is.close();
		return bytes;
	}
}
