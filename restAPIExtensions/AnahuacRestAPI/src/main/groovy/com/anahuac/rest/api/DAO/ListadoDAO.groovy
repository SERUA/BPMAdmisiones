package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.RowId
import java.sql.Statement
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
import org.bonitasoft.engine.search.impl.SearchFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatNotificacionesDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.model.ProcesoCasoDAO
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result
import com.anahuac.catalogos.CatBachilleratosDAO
import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.catalogos.CatGestionEscolarDAO
import com.anahuac.rest.api.Entity.Custom.CatBachilleratosCustom
import com.anahuac.rest.api.Entity.Custom.CatCampusCustom
import com.anahuac.rest.api.Entity.Custom.CatEstadosCustom
import com.anahuac.rest.api.Entity.Custom.CatGestionEscolar
import com.anahuac.rest.api.Entity.Custom.CatLicenciaturaCustom
import com.anahuac.rest.api.Entity.Custom.CatPeriodoCustom
import com.anahuac.rest.api.Entity.Custom.DetalleSolicitudCustom
import com.anahuac.rest.api.Entity.Custom.SolicitudAdmisionCustom
import com.anahuac.rest.api.DB.Statements;
import com.bonitasoft.engine.bpm.parameter.ParameterCriterion
import com.bonitasoft.engine.bpm.process.impl.ProcessInstanceSearchDescriptor
import com.bonitasoft.web.extension.rest.RestAPIContext
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document as DocumentItext
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import groovy.json.JsonSlurper

class ListadoDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ListadoDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Result getNuevasSolicitudes(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<SolicitudAdmisionCustom> lstResultado = new ArrayList<SolicitudAdmisionCustom>();
		List<String> lstGrupo = new ArrayList<String>();
		//List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		
		
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
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			
			assert object instanceof Map;
			if(object.lstFiltro != null) {
				assert object.lstFiltro instanceof List;
			}

			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			/*
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
			lstGrupoCampus.add(objGrupoCampus);*/
			
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(CatCampus rowGrupo : lstCatCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
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
	public Result selectAspirantesEnproceso(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", bachillerato="", campus="", programa="", ingreso="", estado ="", tipoalumno ="", orderby="ORDER BY ", errorlog=""
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			
			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			/*
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
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Cordoba");
			objGrupoCampus.put("valor","CAMPUS-CORDOBA");
			lstGrupoCampus.add(objGrupoCampus);
			*/
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(CatCampus rowGrupo : lstCatCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}
			
			assert object instanceof Map;
			where+=" WHERE sda.iseliminado=false"
			if(object.estatusSolicitud !=null) {
				if(object.estatusSolicitud.equals("Solicitud lista roja")) {
					where+=" AND sda.ESTATUSSOLICITUD='Solicitud lista roja'"
				}else if(object.estatusSolicitud.equals("Solicitud rechazada")) {
					where+=" AND sda.ESTATUSSOLICITUD='Solicitud rechazada'"
				}else if(object.estatusSolicitud.equals("Nuevas solicitudes")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Solicitud modificada' OR sda.ESTATUSSOLICITUD='Solicitud recibida')"
				}else if(object.estatusSolicitud.equals("Solicitud en progreso")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Solicitud en progreso')"
				} else if(object.estatusSolicitud.equals("Aspirantes registrados sin validación de cuenta")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Aspirantes registrados sin validación de cuenta')"
				} else if(object.estatusSolicitud.equals("Aspirantes registrados con validación de cuenta")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Aspirantes registrados con validación de cuenta')"
				}
				else if(object.estatusSolicitud.equals("Solicitud en espera de pago")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Solicitud en espera de pago')"
				}
				else if(object.estatusSolicitud.equals("Solicitud con pago aceptado")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Solicitud con pago aceptado')"
				}
				else if(object.estatusSolicitud.equals("Autodescripción en proceso")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Autodescripción en proceso')"
				}
				else if(object.estatusSolicitud.equals("Elección de pruebas no calendarizado")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Elección de pruebas no calendarizado')"
				}
				else if(object.estatusSolicitud.equals("No se ha impreso credencial")) {
					where+=" AND (sda.ESTATUSSOLICITUD='No se ha impreso credencial')"
				}
				else if(object.estatusSolicitud.equals("Ya se imprimió su credencial")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Ya se imprimió su credencial')"
				}

			}
			if(lstGrupo.size()>0) {
				campus+=" AND ("
			}
			for(Integer i=0; i<lstGrupo.size(); i++) {
				String campusMiembro=lstGrupo.get(i);
				campus+="campus.descripcion='"+campusMiembro+"'"
				if(i==(lstGrupo.size()-1)) {
					campus+=") "	
				}
				else {
					campus+=" OR "
				}
			}
			
			errorlog+="campus" + campus;
				errorlog+="object.lstFiltro" +object.lstFiltro
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				String consulta = Statements.GET_ASPIRANTES_EN_PROCESO
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
                    errorlog+=", columna "+ filtro.get("columna")
					switch(filtro.get("columna")) {
						
                        case "NOMBRE":
                            errorlog+="NOMBRE"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(concat(sda.primernombre,' ', sda.segundonombre,' ',sda.apellidopaterno,' ',sda.apellidomaterno)) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
                            where = where.replace("[valor]", filtro.get("valor"))
                            break;
                        case "EMAIL":
                            errorlog+="EMAIL"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.correoelectronico) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
                            where = where.replace("[valor]", filtro.get("valor"))
                            break;
                        case "CURP":
                            errorlog+="CURP"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.curp) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
                            where = where.replace("[valor]", filtro.get("valor"))
                            break;
                        case "CAMPUS":
                            errorlog+="CAMPUS"
							campus +=" AND LOWER(campus.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								campus+="=LOWER('[valor]')"
							}else {
								campus+="LIKE LOWER('%[valor]%')"
							}
                            campus = campus.replace("[valor]", filtro.get("valor"))
                            break;
                        case "PREPARATORIA":
                            errorlog+="PREPARATORIA"
							bachillerato +=" AND LOWER(prepa.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								bachillerato+="=LOWER('[valor]')"
							}else {
								bachillerato+="LIKE LOWER('%[valor]%')"
							}
                            bachillerato = bachillerato.replace("[valor]", filtro.get("valor"))
                            break;
                        case "PROGRAMA":
                            errorlog+="PROGRAMA"
							programa +=" AND LOWER(gestionescolar.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								programa+="=LOWER('[valor]')"
							}else {
								programa+="LIKE LOWER('%[valor]%')"
							}
                            programa = programa.replace("[valor]", filtro.get("valor"))
                            break;
                        case "INGRESO":
                            errorlog+="INGRESO"
							ingreso +=" AND LOWER(periodo.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								ingreso+="=LOWER('[valor]')"
							}else {
								ingreso+="LIKE LOWER('%[valor]%')"
							}
                            ingreso = ingreso.replace("[valor]", filtro.get("valor"))
                            break;
                        case "ESTADO":
                            errorlog+="ESTADO"
							estado +=" AND LOWER(estado.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								estado+="=LOWER('[valor]')"
							}else {
								estado+="LIKE LOWER('%[valor]%')"
							}
                            estado = estado.replace("[valor]", filtro.get("valor"))
                            break;
                        case "PROMEDIO":
                            errorlog+="PROMEDIO"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.PROMEDIOGENERAL) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
                            where = where.replace("[valor]", filtro.get("valor"))
                            break;
                        case "ESTATUS":
                            errorlog+="ESTATUS"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.ESTATUSSOLICITUD) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
                            where = where.replace("[valor]", filtro.get("valor"))
                            break;
						case "TELEFONO":
							errorlog+="TELEFONO"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.telefonocelular) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
                        case "TIPO":
                            errorlog+="TIPO"
							tipoalumno +=" AND LOWER(da.TIPOALUMNO) ";
							if(filtro.get("operador").equals("Igual a")) {
								tipoalumno+="=LOWER('[valor]')"
							}else {
								tipoalumno+="LIKE LOWER('%[valor]%')"
							}
                            tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                            break;
						case "IDBANNER":
							errorlog+="IDBANNER"
							tipoalumno +=" AND LOWER(da.idbanner) ";
							if(filtro.get("operador").equals("Igual a")) {
								tipoalumno+="=LOWER('[valor]')"
							}else {
								tipoalumno+="LIKE LOWER('%[valor]%')"
							}
							tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
							break;
						case "LISTAROJA":
							errorlog+="LISTAROJA"
							tipoalumno +=" AND LOWER(da.observacionesListaRoja) ";
							if(filtro.get("operador").equals("Igual a")) {
								tipoalumno+="=LOWER('[valor]')"
							}else {
								tipoalumno+="LIKE LOWER('%[valor]%')"
							}
							tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
							break;
						case "RECHAZO":
							errorlog+="RECHAZO"
							tipoalumno +=" AND LOWER(da.observacionesRechazo) ";
							if(filtro.get("operador").equals("Igual a")) {
								tipoalumno+="=LOWER('[valor]')"
							}else {
								tipoalumno+="LIKE LOWER('%[valor]%')"
							}
							tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
							break;
						default:
						//consulta=consulta.replace("[BACHILLERATO]", bachillerato)
						//consulta=consulta.replace("[WHERE]", where);
						
						break;
					} 
				}
				switch(object.orderby) {
					case "NOMBRE":
					orderby+="sda.primernombre";
					break;
					case "EMAIL":
					orderby+="sda.correoelectronico";
					break;
					case "CURP":
					orderby+="sda.curp";
					break;
					case "CAMPUS":
					orderby+="campus.DESCRIPCION"
					break;
					case "PREPARATORIA":
					orderby+="prepa.DESCRIPCION"
					break;
					case "PROGRAMA":
					orderby+="gestionescolar.DESCRIPCION"
					break;
					case "INGRESO":
					orderby+="periodo.DESCRIPCION"
					break;
					case "ESTADO":
					orderby +="estado.DESCRIPCION";
					break;
					case "PROMEDIO":
					orderby+="sda.PROMEDIOGENERAL";
					break;
					case "ESTATUS":
					orderby+="sda.ESTATUSSOLICITUD";
					break;
					case "TIPO":
					orderby+="da.TIPOALUMNO";
					break;
					case "TELEFONO":
					orderby+="sda.telefonocelular";
					break;
					case "IDBANNER":
					orderby+="da.idbanner";
					break;
					case "LISTAROJA":
					orderby+="da.observacionesListaRoja";
					break;
					case "RECHAZO":
					orderby+="da.observacionesRechazo";
					break;
					default:
					orderby+="sda.persistenceid"
					break;
				}
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[CAMPUS]", campus)
				consulta=consulta.replace("[PROGRAMA]", programa)
				consulta=consulta.replace("[INGRESO]", ingreso)
				consulta=consulta.replace("[ESTADO]", estado)
				consulta=consulta.replace("[BACHILLERATO]", bachillerato)
				consulta=consulta.replace("[TIPOALUMNO]", tipoalumno)
				where+=" "+campus +" "+programa +" " + ingreso + " " + estado +" "+bachillerato +" "+tipoalumno
				consulta=consulta.replace("[WHERE]", where);
				
				pstm = con.prepareStatement(consulta.replace("sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campus.descripcion         AS campus, gestionescolar.DESCRIPCION AS licenciatura, periodo.DESCRIPCION        AS ingreso, estado.DESCRIPCION         AS estado, prepa.DESCRIPCION          AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+=consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
							String encoded = "";
							try {
								for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
									encoded = "../API/formsDocumentImage?document="+doc.getId();
									columns.put("fotografiab64", encoded);
								}
							}catch(Exception e) {
								columns.put("fotografiab64", "");
								errorlog+= ""+e.getMessage();
							}
						}
					}
	
					rows.add(columns);
				}
				resultado.setSuccess(true)
				
				resultado.setError_info(errorlog);
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setError_info(errorlog)	
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result selectSolicitudesEnProceso(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", bachillerato="", campus="", programa="", ingreso="", estado ="", tipoalumno ="", orderby="ORDER BY ", errorlog=""
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			
			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			/*
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
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Cordoba");
			objGrupoCampus.put("valor","CAMPUS-CORDOBA");
			lstGrupoCampus.add(objGrupoCampus);*/
					
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(CatCampus rowGrupo : lstCatCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}
			
			assert object instanceof Map;
			where+=" WHERE sda.iseliminado=false"
			if(object.estatusSolicitud !=null) {
				if(object.estatusSolicitud.equals("Solicitud en progreso")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Solicitud en progreso')"
				} else if(object.estatusSolicitud.equals("Aspirantes registrados sin validación de cuenta")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Aspirantes registrados sin validación de cuenta')"
				} else if(object.estatusSolicitud.equals("Aspirantes registrados con validación de cuenta")) {
					where+=" AND (sda.ESTATUSSOLICITUD='Aspirantes registrados con validación de cuenta')"
				}
			}
			
			campus+=" AND ("
			
			for(Integer i=0; i<lstGrupo.size(); i++) {
				String campusMiembro=lstGrupo.get(i);
				campus+="campus.descripcion='"+campusMiembro+"'"
				if(i==(lstGrupo.size()-1)) {
					campus+=") "
				}
				else {
					campus+=" OR "
				}
			}
			
			errorlog+="campus" + campus;
				errorlog+="object.lstFiltro" +object.lstFiltro
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				String consulta = Statements.GET_SOLICITUDES_EN_PROCESO
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					errorlog+=", columna "+ filtro.get("columna")
					switch(filtro.get("columna")) {
						
						case "NOMBRE":
							errorlog+="NOMBRE"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(concat(sda.primernombre,' ', sda.segundonombre,' ',sda.apellidopaterno,' ',sda.apellidomaterno)) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "EMAIL":
							errorlog+="EMAIL"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.correoelectronico) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "CURP":
							errorlog+="CURP"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.curp) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "CAMPUS":
							errorlog+="CAMPUS"
							campus +=" AND LOWER(campus.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								campus+="=LOWER('[valor]')"
							}else {
								campus+="LIKE LOWER('%[valor]%')"
							}
							campus = campus.replace("[valor]", filtro.get("valor"))
							break;
						case "PREPARATORIA":
							errorlog+="PREPARATORIA"
							bachillerato +=" AND LOWER(prepa.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								bachillerato+="=LOWER('[valor]')"
							}else {
								bachillerato+="LIKE LOWER('%[valor]%')"
							}
							bachillerato = bachillerato.replace("[valor]", filtro.get("valor"))
							break;
						case "PROGRAMA":
							errorlog+="PROGRAMA"
							programa +=" AND LOWER(gestionescolar.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								programa+="=LOWER('[valor]')"
							}else {
								programa+="LIKE LOWER('%[valor]%')"
							}
							programa = programa.replace("[valor]", filtro.get("valor"))
							break;
						case "INGRESO":
							errorlog+="INGRESO"
							ingreso +=" AND LOWER(periodo.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								ingreso+="=LOWER('[valor]')"
							}else {
								ingreso+="LIKE LOWER('%[valor]%')"
							}
							ingreso = ingreso.replace("[valor]", filtro.get("valor"))
							break;
						case "ESTADO":
							errorlog+="ESTADO"
							estado +=" AND LOWER(estado.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								estado+="=LOWER('[valor]')"
							}else {
								estado+="LIKE LOWER('%[valor]%')"
							}
							estado = estado.replace("[valor]", filtro.get("valor"))
							break;
						case "PROMEDIO":
							errorlog+="PROMEDIO"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.PROMEDIOGENERAL) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "ESTATUS":
							errorlog+="ESTATUS"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.ESTATUSSOLICITUD) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "TELEFONO":
							errorlog+="TELEFONO"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(sda.telefonocelular) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "TIPO":
							errorlog+="TIPO"
							tipoalumno +=" AND LOWER(da.TIPOALUMNO) ";
							if(filtro.get("operador").equals("Igual a")) {
								tipoalumno+="=LOWER('[valor]')"
							}else {
								tipoalumno+="LIKE LOWER('%[valor]%')"
							}
							tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
							break;
						case "IDBANNER":
							errorlog+="IDBANNER"
							tipoalumno +=" AND LOWER(da.idbanner) ";
							if(filtro.get("operador").equals("Igual a")) {
								tipoalumno+="=LOWER('[valor]')"
							}else {
								tipoalumno+="LIKE LOWER('%[valor]%')"
							}
							tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
							break;
						case "LISTAROJA":
							errorlog+="LISTAROJA"
							tipoalumno +=" AND LOWER(da.observacionesListaRoja) ";
							if(filtro.get("operador").equals("Igual a")) {
								tipoalumno+="=LOWER('[valor]')"
							}else {
								tipoalumno+="LIKE LOWER('%[valor]%')"
							}
							tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
							break;
						case "RECHAZO":
							errorlog+="RECHAZO"
							tipoalumno +=" AND LOWER(da.observacionesRechazo) ";
							if(filtro.get("operador").equals("Igual a")) {
								tipoalumno+="=LOWER('[valor]')"
							}else {
								tipoalumno+="LIKE LOWER('%[valor]%')"
							}
							tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
							break;
						default:
						//consulta=consulta.replace("[BACHILLERATO]", bachillerato)
						//consulta=consulta.replace("[WHERE]", where);
						
						break;
					}
				}
				switch(object.orderby) {
					case "NOMBRE":
					orderby+="sda.primernombre";
					break;
					case "EMAIL":
					orderby+="sda.correoelectronico";
					break;
					case "CURP":
					orderby+="sda.curp";
					break;
					case "CAMPUS":
					orderby+="campus.DESCRIPCION"
					break;
					case "PREPARATORIA":
					orderby+="prepa.DESCRIPCION"
					break;
					case "PROGRAMA":
					orderby+="gestionescolar.DESCRIPCION"
					break;
					case "INGRESO":
					orderby+="periodo.DESCRIPCION"
					break;
					case "ESTADO":
					orderby +="estado.DESCRIPCION";
					break;
					case "PROMEDIO":
					orderby+="sda.PROMEDIOGENERAL";
					break;
					case "ESTATUS":
					orderby+="sda.ESTATUSSOLICITUD";
					break;
					case "TIPO":
					orderby+="da.TIPOALUMNO";
					break;
					case "TELEFONO":
					orderby+="sda.telefonocelular";
					break;
					case "IDBANNER":
					orderby+="da.idbanner";
					break;
					case "LISTAROJA":
					orderby+="da.observacionesListaRoja";
					break;
					case "RECHAZO":
					orderby+="da.observacionesRechazo";
					break;
					default:
					orderby+="sda.persistenceid"
					break;
				}
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[CAMPUS]", campus)
				consulta=consulta.replace("[PROGRAMA]", programa)
				consulta=consulta.replace("[INGRESO]", ingreso)
				consulta=consulta.replace("[ESTADO]", estado)
				consulta=consulta.replace("[BACHILLERATO]", bachillerato)
				consulta=consulta.replace("[TIPOALUMNO]", tipoalumno)
				consulta=consulta.replace("[WHERE]", where);
				
				pstm = con.prepareStatement(consulta.replace("sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campus.descripcion         AS campus, gestionescolar.DESCRIPCION AS licenciatura, periodo.DESCRIPCION        AS ingreso, estado.DESCRIPCION         AS estado, prepa.DESCRIPCION          AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+=consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
							String encoded = "";
							try {
								for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
									encoded = "../API/formsDocumentImage?document="+doc.getId();
									columns.put("fotografiab64", encoded);
								}
							}catch(Exception e) {
								columns.put("fotografiab64", "");
								errorlog+= ""+e.getMessage();
							}
						}
					}
	
					rows.add(columns);
				}
				resultado.setSuccess(true)
				
				resultado.setError_info(errorlog);
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setError_info(errorlog)
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
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
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			
			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			
			assert object instanceof Map;
			if(object.lstFiltro != null) {
				assert object.lstFiltro instanceof List;
			}

			/*
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
			lstGrupoCampus.add(objGrupoCampus);*/
					
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(CatCampus rowGrupo : lstCatCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
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
	
	public Result getAspirantesByStatusTemprano(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<SolicitudAdmisionCustom> lstResultado = new ArrayList<SolicitudAdmisionCustom>();
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
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
		String errorLog = "";
		
		DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			estatusSolicitud = object.estatusSolicitud
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			
			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			assert object instanceof Map;
			if(object.lstFiltro != null) {
				assert object.lstFiltro instanceof List;
			}
	
			/*
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
			*/
			userLogged = context.getApiSession().getUserId();
			LOGGER.error userLogged + "";
	
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(CatCampus rowGrupo : lstCatCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}
			
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def procesoCasoDAO = context.apiClient.getDAO(ProcesoCasoDAO.class);
			
			lstAllSolicitud = objSolicitudDeAdmisionDAO.find(0, 99999);
			
			for(SolicitudDeAdmision objAllSolicitud : lstAllSolicitud) {
				objAllSolicitud.getCaseId()
			}
			
			SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Proceso admisiones");
			
			final SearchOptions searchOptionsProccess = searchBuilderProccess.done();
			SearchResult<ProcessDeploymentInfo> SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			List<ProcessDeploymentInfo> lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);

			inicioContador = 0;
			finContador = 0;
			
//			for(ProcessDeploymentInfo  objProcessDeploymentInfo : lstProcessDeploymentInfo) {
//				errorLog += objProcessDeploymentInfo.getVersion() + " ESTE ES EL CASEID DEL PROCESO, ";
//				LOGGER.error objProcessDeploymentInfo.getProcessId().toString()+" - "+objProcessDeploymentInfo.getName() + " - " + objProcessDeploymentInfo.getVersion();
//				if(inicioContador == 0) {
//					LOGGER.error "IF";
//					searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
//				}
//				else {
//					LOGGER.error "ELSE"
//					searchBuilder.or();
//					searchBuilder.differentFrom(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
//				}
//				
//				inicioContador++;
//			}
//			
			searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, "9041478379247961769");
		
			errorLog += "searchBuilder :: " + searchBuilder.toString() + " ";
			searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);
			
			final SearchOptions searchOptions = searchBuilder.done();
			errorLog += "searchOptions " + searchOptions.toString() + " :: ";
			SearchResult<HumanTaskInstance>  SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
			List<HumanTaskInstance> lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstanceSearch) {
				
				objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
				objCatCampusCustom = new CatCampusCustom();
				isFound = true;
				caseId = objHumanTaskInstance.getRootContainerId();
				lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end);

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
						if(object.lstFiltro.size() > 0) {
							for(def row: object.lstFiltro) {
								isFound = false;
								assert row instanceof Map;
                                isFound = true;
								nombreCandidato = objSolicitudDeAdmision.getPrimerNombre()+" "+objSolicitudDeAdmision.getSegundoNombre()+" "+objSolicitudDeAdmision.getApellidoPaterno()+" "+objSolicitudDeAdmision.getApellidoMaterno();
								if(row.operador == 'Que contenga') {
									if (row.columna == "NOMBRE") {
										if (nombreCandidato.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else if (row.columna == "EMAIL") {
										if (objSolicitudDeAdmision.getCorreoElectronico().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else if (row.columna == "CAMPUS") {
										catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
										if(catCampusStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else if (row.columna == "PROGRAMA") {
										gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null ? "" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
										if(gestionEscolar.contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else if (row.columna == "INGRESO") {
										catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null ? "" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
										if(catPeriodoStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else if (row.columna == "ESTATUS") {
										if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}
								} else {
									if (row.columna == "NOMBRE") {
										if (nombreCandidato.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else if (row.columna == "EMAIL") {
										if (objSolicitudDeAdmision.getCorreoElectronico().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else if (row.columna == "CAMPUS") {
										catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
										if(catCampusStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else if (row.columna == "PROGRAMA") {
										gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null ? "" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
										if(gestionEscolar.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else if (row.columna == "INGRESO") {
										catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null ? "" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
										if(catPeriodoStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else if (row.columna == "ESTATUS") {
										if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}
								}
							}
						}
					} else {
						isFound = false;
					}

                    errorLog += objSolicitudDeAdmision.getEstatusSolicitud() + " : : " + estatusSolicitud + "; ";
					
					if(isFound) {
						if(estatusSolicitud != null && objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(estatusSolicitud.toLowerCase())) {
							String encoded = "";
							for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(objSolicitudDeAdmision.getCaseId(), "fotoPasaporte", 0, 10)) {
								encoded = "../API/formsDocumentImage?document="+doc.getId();
							}

                            errorLog += " context.getApiClient(); ";
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
							objSolicitudAdmisionCustom.setFoto(objSolicitudDeAdmision.getFoto());
							objSolicitudAdmisionCustom.setTaskId(objHumanTaskInstance.getId());
														
							objCatGestionEscolar = new CatGestionEscolar();
							if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){
								strError = strError + ", " + "if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){";
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
								strError = strError + ", " + "end if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){";
							}
							objSolicitudAdmisionCustom.setObjCatGestionEscolar(objCatGestionEscolar);
							
							objCatCampusCustom = new CatCampusCustom();
							if(objSolicitudDeAdmision.getCatCampus() != null){
								strError = strError + ", " + "if(objSolicitudDeAdmision.getCatCampus() != null){";
								objCatCampusCustom.setPersistenceId(objSolicitudDeAdmision.getCatCampus().getPersistenceId());
								objCatCampusCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatCampus().getPersistenceVersion());
								objCatCampusCustom.setDescripcion(objSolicitudDeAdmision.getCatCampus().getDescripcion());
								objCatCampusCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatCampus().getUsuarioBanner());
								objCatCampusCustom.setClave(objSolicitudDeAdmision.getCatCampus().getClave());
								strError = strError + ", " + "end if(objSolicitudDeAdmision.getCatCampus() != null){";
							}
							objSolicitudAdmisionCustom.setCatCampus(objCatCampusCustom);
							
							objCatPeriodoCustom = new CatPeriodoCustom();
							if(objSolicitudDeAdmision.getCatPeriodo() != null){
								strError = strError + ", " + "if(objSolicitudDeAdmision.getCatPeriodo() != null){";
								objCatPeriodoCustom.setPersistenceId(objSolicitudDeAdmision.getCatPeriodo().getPersistenceId());
								objCatPeriodoCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatPeriodo().getPersistenceVersion());
								objCatPeriodoCustom.setDescripcion(objSolicitudDeAdmision.getCatPeriodo().getDescripcion());
								objCatPeriodoCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatPeriodo().getUsuarioBanner());
								objCatPeriodoCustom.setClave(objSolicitudDeAdmision.getCatPeriodo().getClave());
								strError = strError + ", " + "end if(objSolicitudDeAdmision.getCatPeriodo() != null){";
							}
							objSolicitudAdmisionCustom.setCatPeriodo(objCatPeriodoCustom);
							
							objCatEstadosCustom = new CatEstadosCustom();
							if(objSolicitudDeAdmision.getCatEstado() != null){
								strError = strError + ", " + "if(objSolicitudDeAdmision.getCatEstado() != null){";
								objCatEstadosCustom.setPersistenceId(objSolicitudDeAdmision.getCatEstado().getPersistenceId());
								objCatEstadosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatEstado().getPersistenceVersion());
								objCatEstadosCustom.setClave(objSolicitudDeAdmision.getCatEstado().getClave());
								objCatEstadosCustom.setDescripcion(objSolicitudDeAdmision.getCatEstado().getDescripcion());
								objCatEstadosCustom.setUsuarioCreacion(objSolicitudDeAdmision.getCatEstado().getUsuarioCreacion());
								strError = strError + ", " + "end if(objSolicitudDeAdmision.getCatEstado() != null){";
							}
							objSolicitudAdmisionCustom.setCatEstado(objCatEstadosCustom);
	
							objSolicitudAdmisionCustom.setUpdateDate(dfSalida.format(objHumanTaskInstance.getReachedStateDate()));
							objSolicitudAdmisionCustom.setCatLicenciatura(objCatLicenciaturaCustom);
	
							objCatBachilleratosCustom = new CatBachilleratosCustom();
							if(objSolicitudDeAdmision.getCatBachilleratos() != null){
								strError = strError + ", " + "if(objSolicitudDeAdmision.getCatBachilleratos() != null){";
								objCatBachilleratosCustom.setPersistenceId(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceId());
								objCatBachilleratosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceVersion());
								objCatBachilleratosCustom.setClave(objSolicitudDeAdmision.getCatBachilleratos().getClave());
								objCatBachilleratosCustom.setDescripcion(objSolicitudDeAdmision.getCatBachilleratos().getDescripcion());
								objCatBachilleratosCustom.setPais(objSolicitudDeAdmision.getCatBachilleratos().getPais());
								objCatBachilleratosCustom.setEstado(objSolicitudDeAdmision.getCatBachilleratos().getEstado());
								objCatBachilleratosCustom.setCiudad(objSolicitudDeAdmision.getCatBachilleratos().getCiudad());
								strError = strError + ", " + "end if(objSolicitudDeAdmision.getCatBachilleratos() != null){";
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
			resultado.setError_info(errorLog);
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
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			
			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
		
			/*
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
				*/	
			userLogged = context.getApiSession().getUserId();
			LOGGER.error userLogged+""; 
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(CatCampus rowGrupo : lstCatCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
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
			CellStyle style = workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
			
			if(type.equals("nuevas_solicitudes")) {
				dataResult = getNuevasSolicitudes(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				
				Row titleRow = sheet.createRow(++rowCount);
				Cell cellReporte = titleRow.createCell(1);
				cellReporte.setCellValue("Reporte:");
				cellReporte.setCellStyle(style);
				Cell cellTitle = titleRow.createCell(2);
				cellTitle.setCellValue("NUEVAS SOLICITUDES");
				Cell cellFecha = titleRow.createCell(4);
				cellFecha.setCellValue("Fecha:");
				cellFecha.setCellStyle(style);
				
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR_OF_DAY, -7);
				Date date = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
								
				/*Date date = new Date();
				TimeZone timeZone = TimeZone.getTimeZone("UTC-6");
				formatter.setTimeZone(timeZone);
				String sDate = formatter.format(date);*/
				
				String sDate = formatter.format(date);
				Cell cellFechaData = titleRow.createCell(5);
				cellFechaData.setCellValue(sDate);
				
				Row blank = sheet.createRow(++rowCount);
				Cell cellusuario = blank.createCell(4);
				cellusuario.setCellValue("Usuario:");
				cellusuario.setCellStyle(style);
				Cell cellusuarioData = blank.createCell(5);
				cellusuarioData.setCellValue(object.usuario);
				
				Row espacio = sheet.createRow(++rowCount);
				
				Row headersRow = sheet.createRow(++rowCount);
				Cell header1 = headersRow.createCell(0);
				header1.setCellValue("SOLICITUD");
				header1.setCellStyle(style);
				Cell header2 = headersRow.createCell(1);
				header2.setCellValue("NOMBRE");
				header2.setCellStyle(style);
				Cell header3 = headersRow.createCell(2);
				header3.setCellValue("EMAIL");
				header3.setCellStyle(style);
				Cell header4 = headersRow.createCell(3);
				header4.setCellValue("TELÉFONO");
				header4.setCellStyle(style);
				Cell header5= headersRow.createCell(4);
				header5.setCellValue("PREPARATORIA");
				header5.setCellStyle(style);
				Cell header6 = headersRow.createCell(5);
				header6.setCellValue("PROCEDENCIA");
				header6.setCellStyle(style);
				Cell header7 = headersRow.createCell(6);
				header7.setCellValue("PROMEDIO");
				header7.setCellStyle(style);
				Cell header8 = headersRow.createCell(7);
				header8.setCellValue("FECHA DE ENVIO");
				header8.setCellStyle(style);
				
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
					cell8.setCellValue(solicitud.getUpdateDate());
				}
			} else if (type.equals("aspirantes_proceso")) {
				dataResult = getAspirantesProceso(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				
				Row titleRow = sheet.createRow(++rowCount);
				Cell cellReporte = titleRow.createCell(1);
				cellReporte.setCellValue("Reporte:");
				cellReporte.setCellStyle(style);
				Cell cellTitle = titleRow.createCell(2);
				cellTitle.setCellValue("ASPITANTES EN PROCESO");
				Cell cellFecha = titleRow.createCell(4);
				cellFecha.setCellValue("Fecha:");
				cellFecha.setCellStyle(style);
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR_OF_DAY, -7);
				Date date = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
								
				/*Date date = new Date();
				TimeZone timeZone = TimeZone.getTimeZone("UTC-6");
				formatter.setTimeZone(timeZone);
				String sDate = formatter.format(date);*/
				
				String sDate = formatter.format(date);
				Cell cellFechaData = titleRow.createCell(5);
				cellFechaData.setCellValue(sDate);
				
				Row blank = sheet.createRow(++rowCount);
				Cell cellusuario = blank.createCell(4);
				cellusuario.setCellValue("Usuario:");
				cellusuario.setCellStyle(style);
				Cell cellusuarioData = blank.createCell(5);
				cellusuarioData.setCellValue(object.usuario);
				
				Row espacio = sheet.createRow(++rowCount);
				
				Row headersRow = sheet.createRow(++rowCount);
				Cell header1 = headersRow.createCell(0);
				header1.setCellValue("NOMBRE");
				header1.setCellStyle(style);
				Cell header2 = headersRow.createCell(1);
				header2.setCellValue("EMAIL");
				header2.setCellStyle(style);
				Cell header3 = headersRow.createCell(2);
				header3.setCellValue("CURP");
				header3.setCellStyle(style);
				Cell header4 = headersRow.createCell(3);
				header4.setCellValue("CAMPUS");
				header4.setCellStyle(style);
				Cell header5= headersRow.createCell(4);
				header5.setCellValue("PROGRAMA");
				header5.setCellStyle(style);
				Cell header6 = headersRow.createCell(5);
				header6.setCellValue("INGRESO ESTADO");
				header6.setCellStyle(style);
				Cell header7 = headersRow.createCell(6);
				header7.setCellValue("PREPARATORIA");
				header7.setCellStyle(style);
				Cell header8 = headersRow.createCell(7);
				header8.setCellValue("PROMEDIO");
				header8.setCellStyle(style);
				Cell header9 = headersRow.createCell(8);
				header9.setCellValue("ESTATUS");
				header9.setCellStyle(style);
				Cell header10 = headersRow.createCell(9);
				header10.setCellValue("TIPO");
				header10.setCellStyle(style);
				Cell header11 = headersRow.createCell(7);
				header11.setCellValue("ÚLTIMA MODIFICACION");
				header11.setCellStyle(style);
				
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
					cell11.setCellValue(solicitud.getUpdateDate());
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
				Cell cellReporte = titleRow.createCell(1);
				cellReporte.setCellValue("Reporte:");
				cellReporte.setCellStyle(style);
				Cell cellTitle = titleRow.createCell(2);
				cellTitle.setCellValue(title);
				//Fecha y Nombre Usuario
				Cell cellFecha = titleRow.createCell(4);
				cellFecha.setCellValue("Fecha:");
				cellFecha.setCellStyle(style);
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR_OF_DAY, -7);
				Date date = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
								
				/*Date date = new Date();
				TimeZone timeZone = TimeZone.getTimeZone("UTC-6");
				formatter.setTimeZone(timeZone);
				String sDate = formatter.format(date);*/
				
				String sDate = formatter.format(date);
				Cell cellFechaData = titleRow.createCell(5);
				cellFechaData.setCellValue(sDate);
				
				Row blank = sheet.createRow(++rowCount);
				Cell cellusuario = blank.createCell(4);
				cellusuario.setCellValue("Usuario:");
				cellusuario.setCellStyle(style);
				Cell cellusuarioData = blank.createCell(5);
				cellusuarioData.setCellValue(object.usuario);
				
				Row espacio = sheet.createRow(++rowCount);
				
				Row headersRow = sheet.createRow(++rowCount);
				Cell header1 = headersRow.createCell(0);
				header1.setCellValue("SOLICITUD");
				header1.setCellStyle(style);
				Cell header2 = headersRow.createCell(1);
				header2.setCellValue("NOMBRE");
				header2.setCellStyle(style);
				Cell header3 = headersRow.createCell(2);
				header3.setCellValue("EMAIL");
				header3.setCellStyle(style);
				Cell header4 = headersRow.createCell(3);
				header4.setCellValue("TELÉFONO");
				header4.setCellStyle(style);
				Cell header5= headersRow.createCell(4);
				header5.setCellValue("CAMPUS");
				header5.setCellStyle(style);
				Cell header6 = headersRow.createCell(5);
				header6.setCellValue("PROGRAMA");
				header6.setCellStyle(style);
				Cell header7 = headersRow.createCell(6);
				header7.setCellValue("INGRESO");
				header7.setCellStyle(style);
				Cell header8 = headersRow.createCell(7);
				header8.setCellValue("PROMEDIO");
				header8.setCellStyle(style);
				Cell header9 = headersRow.createCell(8);
				header9.setCellValue(type.equals("lista_roja") ? "MOTIVO DE LISTA ROJA" : "MOTIVO DE RECHAZO");
				header9.setCellStyle(style);
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
			} else if(type.equals("solicitudes_en_progreso")) {
				dataResult = getNuevasSolicitudes(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				
				Row titleRow = sheet.createRow(++rowCount);
				Cell cellReporte = titleRow.createCell(1);
				cellReporte.setCellValue("Reporte:");
				cellReporte.setCellStyle(style);
				Cell cellTitle = titleRow.createCell(2);
				cellTitle.setCellValue("NUEVAS SOLICITUDES");
				
				Cell cellFecha = titleRow.createCell(4);
				cellFecha.setCellValue("Fecha:");
				cellFecha.setCellStyle(style);
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR_OF_DAY, -7);
				Date date = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
								
				/*Date date = new Date();
				TimeZone timeZone = TimeZone.getTimeZone("UTC-6");
				formatter.setTimeZone(timeZone);
				String sDate = formatter.format(date);*/
				
				String sDate = formatter.format(date);
				Cell cellFechaData = titleRow.createCell(5);
				cellFechaData.setCellValue(sDate);
				
				Row blank = sheet.createRow(++rowCount);
				Cell cellusuario = blank.createCell(4);
				cellusuario.setCellValue("Usuario:");
				cellusuario.setCellStyle(style);
				Cell cellusuarioData = blank.createCell(5);
				cellusuarioData.setCellValue(object.usuario);
				
				Row espacio = sheet.createRow(++rowCount);
				
				Row headersRow = sheet.createRow(++rowCount);
				Cell header1 = headersRow.createCell(0);
				header1.setCellValue("SOLICITUD");
				header1.setCellStyle(style);
				Cell header2 = headersRow.createCell(1);
				header2.setCellValue("NOMBRE");
				header2.setCellStyle(style);
				Cell header3 = headersRow.createCell(2);
				header3.setCellValue("EMAIL");
				header3.setCellStyle(style);
				Cell header4 = headersRow.createCell(3);
				header4.setCellValue("FECHA DE ENVIO");
				header4.setCellStyle(style);
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
					cell4.setCellValue("");
				}
			}
			
			
			for(int i=0; i<=rowCount+3; ++i) {
				sheet.autoSizeColumn(i);
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
			float fontSize = 8.5f;
			Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, fontSize, BaseColor.BLACK);
			String phraseToInput = "";
			if(type.equals("nuevas_solicitudes")) {
				dataResult = getNuevasSolicitudes(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				
				document.open();
				
				Paragraph par = new Paragraph("REPORTE DE NUEVAS SOLICITUDES");
				par.setAlignment(Paragraph.ALIGN_CENTER);
				document.add(par);
				document.add(new Paragraph(" "));
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR_OF_DAY, -7);
				Date date = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				String sDate = formatter.format(date);
				
				Chunk glue = new Chunk(new VerticalPositionMark());
				Paragraph p = new Paragraph("FECHA: "+sDate);
				p.add(new Chunk(glue));
				p.add("USUARIO: "+object.usuario);
				document.add(p);
				document.add( new Paragraph(" "));
				
				PdfPTable table = new PdfPTable(8);
				table.setWidthPercentage(100f);
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				Phrase ph = new Phrase();
				ph.setFont(normalFont);
				ph = new Phrase("SOLICITUD", normalFont);
				header.setPhrase(ph);
				table.addCell(header);
				ph = new Phrase("NOMBRE", normalFont);
				header.setPhrase(ph);
				table.addCell(header);
				ph = new Phrase("EMAIL", normalFont);
				header.setPhrase(ph);
				table.addCell(header);
				ph = new Phrase("TELÉFONO", normalFont);
				header.setPhrase(ph);
				table.addCell(header);
				ph = new Phrase("PREPARATORIA", normalFont);
				header.setPhrase(ph);
				table.addCell(header);
				ph = new Phrase("PROCEDENCIA", normalFont);
				header.setPhrase(ph);
				table.addCell(header);
				ph = new Phrase("PROMEDIO", normalFont);
				header.setPhrase(ph)
				table.addCell(header);
				ph = new Phrase("FECHA DE ENVIO", normalFont);
				header.setPhrase(ph);
				table.addCell(header);
				
				SolicitudAdmisionCustom  solicitud = new SolicitudAdmisionCustom();
				phraseToInput = new String();
				for (int i = 0; i < lstParams.size(); ++i){
					phraseToInput = new String();
					solicitud = new SolicitudAdmisionCustom();
					solicitud = (SolicitudAdmisionCustom)lstParams.get(i);
					table.addCell(new Phrase(solicitud.getPersistenceId().toString(), normalFont));
					table.addCell(new Phrase(
						solicitud.getPrimerNombre() + " " +
						solicitud.getSegundoNombre() + " " +
						solicitud.getApellidoPaterno() + " " +
						solicitud.getApellidoMaterno()), normalFont
					);
					phraseToInput = solicitud.getCorreoElectronico();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getTelefono();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getCatBachilleratos().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getCatEstado().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getPromedioGeneral();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getUpdateDate();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
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
				Paragraph par = new Paragraph("REPORTE DE ASPIRANTES EN PROCESO");
				par.setAlignment(Paragraph.ALIGN_CENTER);
				document.add(par);
				document.add(new Paragraph(" "));
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR_OF_DAY, -7);
				Date date = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				String sDate = formatter.format(date);
				
				Chunk glue = new Chunk(new VerticalPositionMark());
				Paragraph p = new Paragraph("FECHA: "+sDate);
				p.add(new Chunk(glue));
				p.add("USUARIO: "+object.usuario);
				document.add(p);
				document.add( new Paragraph(" "));
				
				
				PdfPTable table = new PdfPTable(12);
				table.setWidthPercentage(100f);
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				
				header.setPhrase(new Phrase("NOMBRE", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("EMAIL", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("CURP", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("CAMPUS", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("PROGRAMA", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("INGRESO", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("ESTADO", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("PREPARATORIA", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("PROMEDIO", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("ESTATUS", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("TIPO", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("ÚLTIMA MODIFICACION", normalFont));
				table.addCell(header);
				
				SolicitudAdmisionCustom  solicitud = new SolicitudAdmisionCustom();
				
				for (int i = 0; i < lstParams.size(); ++i){
					solicitud = new SolicitudAdmisionCustom();
					solicitud = (SolicitudAdmisionCustom)lstParams.get(i);
					phraseToInput = new String();
					
					phraseToInput = 
						solicitud.getPrimerNombre()  + " " +
						solicitud.getSegundoNombre() + " " +
						solicitud.getApellidoPaterno() + " " +
						solicitud.getApellidoMaterno();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getCorreoElectronico();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getCurp();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getCatCampus().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getObjCatGestionEscolar().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getCatPeriodo().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getCatEstado().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getCatBachilleratos().getDescripcion()
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getPromedioGeneral();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getEstatusSolicitud();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getObjDetalleSolicitud() == null ? "" :solicitud.getObjDetalleSolicitud().getTipoAlumno();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getUpdateDate();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
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
				String title1 = "REPORTE DE " + (type.equals("lista_roja") ? "LISTA ROJA" : "ASPIRANTES RECHAZADOS");
				Paragraph par = new Paragraph(title1);
				par.setAlignment(Paragraph.ALIGN_CENTER);
				document.add(par);
				document.add(new Paragraph(" "));
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR_OF_DAY, -7);
				Date date = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				String sDate = formatter.format(date);
				
				Chunk glue = new Chunk(new VerticalPositionMark());
				Paragraph p = new Paragraph("FECHA: "+sDate);
				p.add(new Chunk(glue));
				p.add("USUARIO: "+object.usuario);
				document.add(p);
				document.add( new Paragraph(" "));
				
				
				PdfPTable table = new PdfPTable(9);
				table.setWidthPercentage(100f);
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				
				header.setPhrase(new Phrase("SOLICITUD", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("NOMBRE", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("EMAIL", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("TELÉFONO", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("CAMPUS", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("PROGRAMA", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("INGRESO", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("PROMEDIO", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase(type.equals("lista_roja") ? "MOTIVO DE LISTA ROJA" : "MOTIVO DE RECHAZO", normalFont));
				table.addCell(header);
				
				SolicitudAdmisionCustom  solicitud = new SolicitudAdmisionCustom();
				
				for (int i = 0; i < lstParams.size(); ++i){
					solicitud = new SolicitudAdmisionCustom();
					solicitud = (SolicitudAdmisionCustom)lstParams.get(i);
					phraseToInput = new String();
					table.addCell(new Phrase(solicitud.getPersistenceId().toString(), normalFont));
					table.addCell(new Phrase(
						solicitud.getPrimerNombre() + " " +
						solicitud.getSegundoNombre() + " " +
						solicitud.getApellidoPaterno() + " " +
						solicitud.getApellidoMaterno(), normalFont)
					);
					phraseToInput = solicitud.getCorreoElectronico();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getTelefono();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getCatCampus().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getObjCatGestionEscolar().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getCatPeriodo().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getPromedioGeneral();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput));
					phraseToInput = type.equals("lista_roja") ? solicitud.getObjDetalleSolicitud().getObservacionesListaRoja() : solicitud.getObjDetalleSolicitud().getObservacionesRechazo();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
				}
				
				document.add(table);
				document.close();
			} else if(type.equals("aspirantes_en_progreso")) {
				dataResult = getAspirantesByStatusTemprano(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				
				String title = object.estatusSolicitud;
				
				document.open();
				String title1 = "REPORTE DE SOLICITUDES EN PROGRESO" ;
				Paragraph par = new Paragraph(title1);
				par.setAlignment(Paragraph.ALIGN_CENTER);
				document.add(par);
				document.add(new Paragraph(" "));
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR_OF_DAY, -7);
				Date date = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				String sDate = formatter.format(date);
				
				Chunk glue = new Chunk(new VerticalPositionMark());
				Paragraph p = new Paragraph("FECHA: "+sDate);
				p.add(new Chunk(glue));
				p.add("USUARIO: "+object.usuario);
				document.add(p);
				document.add( new Paragraph(" "));
				
				PdfPTable table = new PdfPTable(6);
				table.setWidthPercentage(100f);
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				
				header.setPhrase(new Phrase("SOLICITUD", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("NOMBRE", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("EMAIL", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("CAMPUS", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("PROGRAMA", normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("INGRESO", normalFont));
				table.addCell(header);
				
				SolicitudAdmisionCustom  solicitud = new SolicitudAdmisionCustom();
				
				for (int i = 0; i < lstParams.size(); ++i){
					solicitud = new SolicitudAdmisionCustom();
					solicitud = (SolicitudAdmisionCustom)lstParams.get(i);
					phraseToInput = new String();
					table.addCell(new Phrase(solicitud.getPersistenceId().toString(), normalFont));
					table.addCell(new Phrase(
						solicitud.getPrimerNombre() + " " +
						solicitud.getSegundoNombre() + " " +
						solicitud.getApellidoPaterno() + " " +
						solicitud.getApellidoMaterno(), normalFont)
					);
					phraseToInput = solicitud.getCorreoElectronico();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getCatCampus().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getObjCatGestionEscolar().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
					phraseToInput = solicitud.getCatPeriodo().getDescripcion();
					table.addCell(new Phrase(phraseToInput == null ? "" : phraseToInput, normalFont));
				}
				
				document.add(table);
				document.close();
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
			CellStyle style = workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
			
			if(type.equals("gestion_escolar")) {
				dataResult = getGestionEscolar(parameterP, parameterC, jsonData, context);

				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				def campus= (object.campus == "CAMPUS-MNORTE"?"Anáhuac México Norte":object.campus == "CAMPUS-MSUR"?"Anáhuac México Sur":object.campus == "CAMPUS-MAYAB"?"Anáhuac Merida":object.campus =="CAMPUS-XALAPA"?"Anáhuac Xalapa":object.campus =="CAMPUS-CORDOBA"?"Anáhuac Cordoba":object.campus =="CAMPUS-CANCUN"?"Anáhuac Cancún":object.campus =="CAMPUS-OAXACA"?"Anáhuac Oaxaca":object.campus =="CAMPUS-PUEBLA"?"Anáhuac Puebla":object.campus =="CAMPUS-QUERETARO"?"Anáhuac Querétaro":"Juan Pablo II");
				Row titleRow = sheet.createRow(++rowCount);
				Cell cellReporte = titleRow.createCell(1);
				cellReporte.setCellValue("Reporte:");
				cellReporte.setCellStyle(style);
				Cell cellTitle = titleRow.createCell(2);
				cellTitle.setCellValue("LISTADO DE LICENCIATURAS DEL CAMPUS \""+ campus+"\"");
		
				Cell cellFecha = titleRow.createCell(4);
				cellFecha.setCellValue("Fecha:");
				cellFecha.setCellStyle(style);
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR_OF_DAY, -7);
				Date date = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
								
				/*Date date = new Date();
				TimeZone timeZone = TimeZone.getTimeZone("UTC-6");
				formatter.setTimeZone(timeZone);
				String sDate = formatter.format(date);*/
				
				String sDate = formatter.format(date);
				Cell cellFechaData = titleRow.createCell(5);
				cellFechaData.setCellValue(sDate);
				
				Row blank = sheet.createRow(++rowCount);
				Cell cellusuario = blank.createCell(4);
				cellusuario.setCellValue("Usuario:");
				cellusuario.setCellStyle(style);
				Cell cellusuarioData = blank.createCell(5);
				cellusuarioData.setCellValue(object.usuario);
				
				Row espacio = sheet.createRow(++rowCount);
				
				Row headersRow = sheet.createRow(++rowCount);
				Cell header1 = headersRow.createCell(0);
				header1.setCellValue("NOMBRE LICENCIATURA");
				header1.setCellStyle(style);
				Cell header2 = headersRow.createCell(1);
				header2.setCellValue("LIGA");
				header2.setCellStyle(style);
				Cell header3 = headersRow.createCell(2);
				header3.setCellValue("DESCRIPCION DE LA CARRERA");
				header3.setCellStyle(style);
				Cell header4 = headersRow.createCell(3);
				header4.setCellValue("INSCRIPCION DE ENERO");
				header4.setCellStyle(style);
				Cell header5= headersRow.createCell(4);
				header5.setCellValue("INSCRIPCION DE AGOSTO");
				header5.setCellStyle(style);
				Cell header6= headersRow.createCell(5);
				header6.setCellValue("PERIODOS");
				header6.setCellStyle(style);
				
				Cell header7= headersRow.createCell(6);
				header7.setCellValue("PROPEDEUTICO");
				header7.setCellStyle(style);
				Cell header8= headersRow.createCell(7);
				header8.setCellValue("PROG PARCIAL");
				header8.setCellStyle(style);
				Cell header9= headersRow.createCell(8);
				header9.setCellValue("CUATRIMESTRAL");
				
				header9.setCellStyle(style);
				
				headersRow.setRowStyle(style);
				
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
					
					String isPropedeutico = Escolar.isPropedeutico() ? "Sí" : "No";
					Cell cell7= row.createCell(6);
					cell7.setCellValue(isPropedeutico);
					Cell cell8= row.createCell(7);
					String isProgramaParcial = Escolar.isProgramaparcial() ? "Sí" : "No";
					cell8.setCellValue(isProgramaParcial);
					String isCuatrimestral = Escolar.isMatematicas()  ? "Sí" : "No";
					Cell cell9= row.createCell(8);
					cell9.setCellValue(isCuatrimestral);
				}
			}else if(type.equals("bachilleratos")) {
				dataResult = getBachilleratos(parameterP, parameterC, jsonData, context);
				
				if (dataResult.success) {
					lstParams = dataResult.getData();
				} else {
					throw new Exception("No encontro datos");
				}
				Row titleRow = sheet.createRow(++rowCount);
				Cell cellReporte = titleRow.createCell(1);
				cellReporte.setCellValue("Reporte:");
				cellReporte.setCellStyle(style);
				Cell cellTitle = titleRow.createCell(2);
				cellTitle.setCellValue("LISTADO DE BACHILLERATOS");
				Cell cellFecha = titleRow.createCell(4);
				cellFecha.setCellValue("Fecha:");
				cellFecha.setCellStyle(style);
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR_OF_DAY, -7);
				Date date = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
								
				/*Date date = new Date();
				TimeZone timeZone = TimeZone.getTimeZone("UTC-6");
				formatter.setTimeZone(timeZone);
				String sDate = formatter.format(date);*/
				
				String sDate = formatter.format(date);
				Cell cellFechaData = titleRow.createCell(5);
				cellFechaData.setCellValue(sDate);
				
				Row blank = sheet.createRow(++rowCount);object
				Cell cellusuario = blank.createCell(4);
				cellusuario.setCellValue("Usuario:");
				cellusuario.setCellStyle(style);
				Cell cellusuarioData = blank.createCell(5);
				cellusuarioData.setCellValue(object.usuario);
				
				Row headersRow = sheet.createRow(++rowCount);
				
				Cell header1 = headersRow.createCell(0);
				header1.setCellValue("DESCRIPCION");
				header1.setCellStyle(style);
				Cell header2 = headersRow.createCell(1);
				header2.setCellValue("PAIS");
				header2.setCellStyle(style);
				Cell header3 = headersRow.createCell(2);
				header3.setCellValue("ESTADO");
				header3.setCellStyle(style);
				Cell header4 = headersRow.createCell(3);
				header4.setCellValue("CIUDAD");
				header4.setCellStyle(style);
				Cell header5= headersRow.createCell(4);
				header5.setCellValue("PERTENECE A LA RED");
				header5.setCellStyle(style);
				
				headersRow.setRowStyle(style);
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
			
			for(int i=0; i<=rowCount+3; ++i) {
				sheet.autoSizeColumn(i);
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
			float fontSize = 8.5f;
			Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, fontSize, BaseColor.BLACK);
			String phraseToInput = "";
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
				document.add( new Paragraph(" "));
				
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR_OF_DAY, -7);
				Date date = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				String sDate = formatter.format(date);
				
				Chunk glue = new Chunk(new VerticalPositionMark());
				Paragraph p = new Paragraph("FECHA: "+sDate);
				p.add(new Chunk(glue));
				p.add("USUARIO: "+object.usuario);
				document.add(p);
				document.add( new Paragraph(" "));
				
				PdfPTable table = new PdfPTable(6);
				table.setWidthPercentage(100f);
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				
				header.setPhrase(new Phrase("NOMBRE lICENCIATURA",normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("LIGA",normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("DESCRIPCION DE LA CARRERA",normalFont))
				table.addCell(header);
				header.setPhrase(new Phrase("INSCRIPCION DE ENERO",normalFont))
				table.addCell(header);
				header.setPhrase(new Phrase("INSCRIPCION DE AGOSTO",normalFont))
				table.addCell(header);
				header.setPhrase(new Phrase("PERIODOS",normalFont))
				table.addCell(header);
				
				com.anahuac.catalogos.CatGestionEscolar  G_Escolar = new com.anahuac.catalogos.CatGestionEscolar();
				
				for (int i = 0; i < lstParams.size(); ++i){
					G_Escolar = new com.anahuac.catalogos.CatGestionEscolar();
					G_Escolar = (com.anahuac.catalogos.CatGestionEscolar)lstParams.get(i);
					table.addCell(new Phrase(G_Escolar.getNombre(),normalFont));
					table.addCell(new Phrase(G_Escolar.getEnlace(),normalFont));
					table.addCell(new Phrase(G_Escolar.getDescripcion(),normalFont));
					table.addCell(new Phrase("\$"+G_Escolar.getInscripcionenero(),normalFont));
					table.addCell(new Phrase("\$"+G_Escolar.getInscripcionagosto(),normalFont));
					com.anahuac.catalogos.CatPeriodo lstP = new com.anahuac.catalogos.CatPeriodo();
					def periodos = "";
					for(int f=0; f<G_Escolar.getPeriodoDisponible().size(); f++) {
						lstP = new com.anahuac.catalogos.CatPeriodo();
						lstP = (com.anahuac.catalogos.CatPeriodo)G_Escolar.getPeriodoDisponible().get(f);
						periodos +=lstP.getDescripcion() + ",";
					}
					table.addCell(new Phrase(periodos,normalFont));
					
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
				document.add( new Paragraph(" "));
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR_OF_DAY, -7);
				Date date = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				String sDate = formatter.format(date);
				

				Chunk glue = new Chunk(new VerticalPositionMark());
				Paragraph p = new Paragraph("FECHA: "+sDate);
				p.add(new Chunk(glue));
				p.add("USUARIO: "+object.usuario);
				document.add(p);
				
				
				document.add( new Paragraph(" "))
				
				PdfPTable table = new PdfPTable(5);
				table.setWidthPercentage(100f);
				
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				
				header.setPhrase(new Phrase("DESCRIPCION",normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("PAIS",normalFont));
				table.addCell(header);
				header.setPhrase(new Phrase("ESTADO",normalFont))
				table.addCell(header);
				header.setPhrase(new Phrase("CIUDAD",normalFont))
				table.addCell(header);
				header.setPhrase(new Phrase("PERTENECE A LA RED",normalFont))
				table.addCell(header);
				
				com.anahuac.catalogos.CatBachilleratos  bachillerato = new com.anahuac.catalogos.CatBachilleratos();
				
				for (int i = 0; i < lstParams.size(); ++i){
					bachillerato = new com.anahuac.catalogos.CatBachilleratos();
					bachillerato = (com.anahuac.catalogos.CatBachilleratos)lstParams.get(i);
					table.addCell(new Phrase(bachillerato.getDescripcion(),normalFont));
					table.addCell(new Phrase(bachillerato.getPais(),normalFont));
					table.addCell(new Phrase(bachillerato.getEstado(),normalFont));
					table.addCell(new Phrase(bachillerato.getCiudad(),normalFont));	
					def red = bachillerato.isPerteneceRed()?'Sí':"No";
					table.addCell(new Phrase(red,normalFont));
					
				}
				
				document.add(table);
				document.close();
			}
			
			List<Object> lstResultado = new ArrayList<Object>();
			lstResultado.add(encodeFileToBase64Binary("ReportCatalogos.pdf"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
		   LOGGER.error "LLego al final"
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
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
	}
}
