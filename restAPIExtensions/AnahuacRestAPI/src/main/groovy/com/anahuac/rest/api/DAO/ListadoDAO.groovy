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
import org.bonitasoft.engine.identity.User
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
import com.anahuac.rest.api.Entity.CaseVariable
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Transferencias
import com.anahuac.catalogos.CatBachilleratosDAO
import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.catalogos.CatGestionEscolarDAO
import com.anahuac.rest.api.Entity.Custom.CatBachilleratosCustom
import com.anahuac.rest.api.Entity.Custom.CatCampusCustom
import com.anahuac.rest.api.Entity.Custom.CatCampusCustomFiltro
import com.anahuac.rest.api.Entity.Custom.CatEstadosCustom
import com.anahuac.rest.api.Entity.Custom.CatGenericoFiltro
import com.anahuac.rest.api.Entity.Custom.CatGestionEscolar
import com.anahuac.rest.api.Entity.Custom.CatLicenciaturaCustom
import com.anahuac.rest.api.Entity.Custom.CatPeriodoCustom
import com.anahuac.rest.api.Entity.Custom.DetalleSolicitudCustom
import com.anahuac.rest.api.Entity.Custom.PruebasCustom
import com.anahuac.rest.api.Entity.Custom.SolicitudAdmisionCustom
import com.anahuac.rest.api.Entity.Custom.SesionesAspiranteCustom
import com.anahuac.rest.api.DB.Statements;
import com.anahuac.rest.api.Utilities.FileDownload
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
import org.apache.poi.xssf.usermodel.IndexedColorMap
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.anahuac.catalogos.CatMateriasDAO
import com.anahuac.catalogos.CatTipoLecturaDAO
import groovy.json.JsonSlurper

class ListadoDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListadoDAO.class);
    Connection con;
    Statement stm;
    ResultSet rs;
    PreparedStatement pstm;

    public Result getNuevasSolicitudes(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();

        List < SolicitudAdmisionCustom > lstResultado = new ArrayList < SolicitudAdmisionCustom > ();
        List < String > lstGrupo = new ArrayList < String > ();
        //List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();

        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;

        Integer start = 0;
        Integer end = 99999;
        Integer inicioContador = 0;
        Integer finContador = 0;

        List < SolicitudDeAdmision > lstSolicitudDeAdmision = new ArrayList < SolicitudDeAdmision > ();
        SolicitudDeAdmision objSolicitudDeAdmision = null;
        SolicitudAdmisionCustom objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
        CatCampusCustom objCatCampusCustom = new CatCampusCustom();
        CatPeriodoCustom objCatPeriodoCustom = new CatPeriodoCustom();
        CatEstadosCustom objCatEstadosCustom = new CatEstadosCustom();
        CatLicenciaturaCustom objCatLicenciaturaCustom = new CatLicenciaturaCustom();
        CatBachilleratosCustom objCatBachilleratosCustom = new CatBachilleratosCustom();
        CatGestionEscolar objCatGestionEscolar = new CatGestionEscolar();

        Map < String, String > objGrupoCampus = new HashMap < String, String > ();

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
            if (object.lstFiltro != null) {
                assert object.lstFiltro instanceof List;
            }

            List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)
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

            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (CatCampus rowGrupo: lstCatCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
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
            SearchResult < ProcessDeploymentInfo > SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
            List < ProcessDeploymentInfo > lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();

            //Long processDefinitionId = context.getApiClient().getProcessAPI().getProcessDefinitionId("Proceso admisiones", "1.0");
            SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);

            //searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
            inicioContador = 0;
            finContador = 0;
            for (ProcessDeploymentInfo objProcessDeploymentInfo: lstProcessDeploymentInfo) {
                if (inicioContador == 0) {
                    searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
                } else {
                    searchBuilder.or();
                    searchBuilder.differentFrom(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
                }
                inicioContador++;
            }

            searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);

            final SearchOptions searchOptions = searchBuilder.done();
            SearchResult < HumanTaskInstance > SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
            List < HumanTaskInstance > lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();

            for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {
                strError = strError + ", objHumanTaskInstance.getName(): " + "" + objHumanTaskInstance.getName();
                objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
                objCatCampusCustom = new CatCampusCustom();


                if (objHumanTaskInstance.getName().equals(object.tarea)) {
                    isFound = true;

                    caseId = objHumanTaskInstance.getRootContainerId();
                    lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end);
                    if (!lstSolicitudDeAdmision.empty) {
                        //objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end).get(0);
                        objSolicitudDeAdmision = lstSolicitudDeAdmision.get(0);
                        catCampusStr = objSolicitudDeAdmision.getCatCampus() == null?"" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
                        isFoundCampus = false;
                        strError = strError + ", catCampusStr.toLowerCase(): " + "" + catCampusStr.toLowerCase();
                        for (String objGrupo: lstGrupo) {
                            if (catCampusStr.toLowerCase().equals(objGrupo.toLowerCase())) {
                                isFoundCampus = true;
                            }
                        }
                        strError = strError + ", isFoundCampus: " + "" + isFoundCampus;
                        if (isFoundCampus) {

                            if (object.lstFiltro != null) {
                                for (def row: object.lstFiltro) {


                                    isFound = false;
                                    assert row instanceof Map;
                                    strError = strError + ", row.columna: " + "" + row.columna;

                                    nombreCandidato = objSolicitudDeAdmision.getPrimerNombre() + " " + objSolicitudDeAdmision.getSegundoNombre() + " " + objSolicitudDeAdmision.getApellidoPaterno() + " " + objSolicitudDeAdmision.getApellidoMaterno();
                                    strError = strError + ", nombreCandidato: " + "" + nombreCandidato;
                                    strError = strError + ", row.operador: " + "" + row.operador;
                                    if (row.operador == 'Que contenga') {
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
                                            catCampusStr = objSolicitudDeAdmision.getCatCampus() == null?"" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
                                            if (catCampusStr.toLowerCase().contains(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "PROGRAMA") {
                                            gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null?"" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
                                            if (gestionEscolar.contains(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "INGRESO") {
                                            catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null?"" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
                                            if (catPeriodoStr.toLowerCase().contains(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "ESTADO") {
                                            catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null?"" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
                                            if (catEstadoStr.toLowerCase().contains(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "PREPARATORIA") {
                                            catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null?"" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
                                            if (catBachilleratoStr.toLowerCase().contains(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "PROMEDIO") {
                                            if (objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().contains(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "ESTATUS") {
                                            if (objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().contains(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "SOLICITUD") {
                                            if (objSolicitudDeAdmision.getCaseId().toString().toLowerCase().contains(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "TELÉFONO") {
                                            if (objSolicitudDeAdmision.getTelefonoCelular().toString().toLowerCase().contains(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "PROCEDENCIA") {
                                            catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null?"" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
                                            if (catEstadoStr.toString().toLowerCase().contains(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "FECHA DE ENVIO") {
                                            if (dfSalida.format(objHumanTaskInstance.getLastUpdateDate()).toString().toLowerCase().contains(row.valor.toLowerCase())) {
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
                                            catCampusStr = objSolicitudDeAdmision.getCatCampus() == null?"" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
                                            if (catCampusStr.toLowerCase().equals(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "PROGRAMA") {
                                            gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null?"" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
                                            if (gestionEscolar.equals(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "INGRESO") {
                                            catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null?"" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
                                            if (catPeriodoStr.toLowerCase().equals(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "ESTADO") {
                                            catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null?"" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
                                            if (catEstadoStr.toLowerCase().equals(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "PREPARATORIA") {
                                            catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null?"" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
                                            if (catBachilleratoStr.toLowerCase().equals(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "PROMEDIO") {
                                            if (objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().equals(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "ESTATUS") {
                                            if (objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "SOLICITUD") {
                                            if (objSolicitudDeAdmision.getCaseId().toString().toLowerCase().equals(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "TELÉFONO") {
                                            if (objSolicitudDeAdmision.getTelefonoCelular().toString().toLowerCase().equals(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "PROCEDENCIA") {
                                            catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null?"" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
                                            if (catEstadoStr.toString().toLowerCase().equals(row.valor.toLowerCase())) {
                                                isFound = true;
                                                break;
                                            }
                                        } else
                                        if (row.columna == "FECHA DE ENVIO") {
                                            if (dfSalida.format(objHumanTaskInstance.getLastUpdateDate()).toString().toLowerCase().equals(row.valor.toLowerCase())) {
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
                        strError = strError + ", isFound: " + "" + isFound;
                        if (isFound) {
                            String encoded = "";
                            for (Document doc: context.getApiClient().getProcessAPI().getDocumentList(objSolicitudDeAdmision.getCaseId(), "fotoPasaporte", 0, 10)) {
                                //encoded ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
                                encoded = "../API/formsDocumentImage?document=" + doc.getId();
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
                            if (objSolicitudDeAdmision.getCatGestionEscolar() != null) {

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
                            if (objSolicitudDeAdmision.getCatCampus() != null) {

                                objCatCampusCustom.setPersistenceId(objSolicitudDeAdmision.getCatCampus().getPersistenceId());
                                objCatCampusCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatCampus().getPersistenceVersion());
                                objCatCampusCustom.setDescripcion(objSolicitudDeAdmision.getCatCampus().getDescripcion());
                                objCatCampusCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatCampus().getUsuarioBanner());
                                objCatCampusCustom.setClave(objSolicitudDeAdmision.getCatCampus().getClave());

                            }
                            objSolicitudAdmisionCustom.setCatCampus(objCatCampusCustom);

                            objCatPeriodoCustom = new CatPeriodoCustom();
                            if (objSolicitudDeAdmision.getCatPeriodo() != null) {

                                objCatPeriodoCustom.setPersistenceId(objSolicitudDeAdmision.getCatPeriodo().getPersistenceId());
                                objCatPeriodoCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatPeriodo().getPersistenceVersion());
                                objCatPeriodoCustom.setDescripcion(objSolicitudDeAdmision.getCatPeriodo().getDescripcion());
                                objCatPeriodoCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatPeriodo().getUsuarioBanner());
                                objCatPeriodoCustom.setClave(objSolicitudDeAdmision.getCatPeriodo().getClave());

                            }
                            objSolicitudAdmisionCustom.setCatPeriodo(objCatPeriodoCustom);

                            objCatEstadosCustom = new CatEstadosCustom();
                            if (objSolicitudDeAdmision.getCatEstado() != null) {

                                objCatEstadosCustom.setPersistenceId(objSolicitudDeAdmision.getCatEstado().getPersistenceId());
                                objCatEstadosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatEstado().getPersistenceVersion());
                                objCatEstadosCustom.setClave(objSolicitudDeAdmision.getCatEstado().getClave());
                                objCatEstadosCustom.setDescripcion(objSolicitudDeAdmision.getCatEstado().getDescripcion());
                                objCatEstadosCustom.setUsuarioCreacion(objSolicitudDeAdmision.getCatEstado().getUsuarioCreacion());

                            }
                            objSolicitudAdmisionCustom.setCatEstado(objCatEstadosCustom);
                            objSolicitudAdmisionCustom.setCatLicenciatura(objCatLicenciaturaCustom);

                            objCatBachilleratosCustom = new CatBachilleratosCustom();
                            if (objSolicitudDeAdmision.getCatBachilleratos() != null) {

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
                        } else {

                        }
                    }
                } else {}
            }
            resultado.setError_info(strError);
            resultado.setData(lstResultado);
            resultado.setSuccess(true);
        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
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
        String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = ""
        List < String > lstGrupo = new ArrayList < String > ();
        List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
        List < DetalleSolicitud > lstDetalleSolicitud = new ArrayList < DetalleSolicitud > ();

        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;
        Map < String, String > objGrupoCampus = new HashMap < String, String > ();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);

            List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)

            userLogged = context.getApiSession().getUserId();

            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (CatCampus rowGrupo: lstCatCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
                        lstGrupo.add(rowGrupo.getDescripcion());
                        break;
                    }
                }
            }

            assert object instanceof Map;
            where += " WHERE sda.iseliminado=false and (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado = false ) "
            if (object.campus != null) {
                where += " AND LOWER(campus.grupoBonita) = LOWER('" + object.campus + "') "
            }
            if (object.estatusSolicitud != null) {
                if (object.estatusSolicitud.equals("Solicitud lista roja")) {
                    where += " AND sda.ESTATUSSOLICITUD='Solicitud lista roja'"
                } else if (object.estatusSolicitud.equals("Solicitud rechazada")) {
                    where += " AND sda.ESTATUSSOLICITUD='Solicitud rechazada'"
                } else if (object.estatusSolicitud.equals("Nuevas solicitudes")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Solicitud modificada' OR sda.ESTATUSSOLICITUD='Solicitud recibida')"
                } else if (object.estatusSolicitud.equals("Solicitud en proceso")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Solicitud en proceso' OR sda.ESTATUSSOLICITUD='Solicitud a modificar' OR sda.ESTATUSSOLICITUD='Periodo vencido')"
                } else if (object.estatusSolicitud.equals("Aspirantes registrados sin validación de cuenta")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Aspirantes registrados sin validación de cuenta')"
                } else if (object.estatusSolicitud.equals("Aspirantes registrados con validación de cuenta")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Aspirantes registrados con validación de cuenta')"
                } else if (object.estatusSolicitud.equals("Solicitud en espera de pago")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Solicitud en espera de pago')"
                } else if (object.estatusSolicitud.equals("Solicitud con pago aceptado")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Solicitud con pago aceptado')"
                } else if (object.estatusSolicitud.equals("Autodescripción en proceso")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Autodescripción en proceso')"
                } else if (object.estatusSolicitud.equals("Elección de pruebas no calendarizado")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Elección de pruebas no calendarizado')"
                } else if (object.estatusSolicitud.equals("No se ha impreso credencial")) {
                    where += " AND (sda.ESTATUSSOLICITUD='No se ha impreso credencial')"
                } else if (object.estatusSolicitud.equals("Ya se imprimió su credencial")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Ya se imprimió su credencial')"
                }
                //CODIGO PP
                else if (object.estatusSolicitud.equals("Aspirantes en proceso")) {
                    //where+=" AND (sda.ESTATUSSOLICITUD != 'Solicitud rechazada') AND (sda.ESTATUSSOLICITUD != 'Solicitud lista roja') AND (sda.ESTATUSSOLICITUD != 'Aspirantes registrados sin validación de cuenta') AND (sda.ESTATUSSOLICITUD !='Aspirantes registrados con validación de cuenta') AND (sda.ESTATUSSOLICITUD != 'Solicitud en proceso') AND (sda.ESTATUSSOLICITUD != 'Solicitud recibida' )"
                    where += " AND (sda.ESTATUSSOLICITUD != 'Solicitud rechazada') AND (sda.ESTATUSSOLICITUD != 'Solicitud lista roja') AND (sda.ESTATUSSOLICITUD != 'Aspirantes registrados sin validación de cuenta') AND (sda.ESTATUSSOLICITUD !='Aspirantes registrados con validación de cuenta') AND (sda.ESTATUSSOLICITUD != 'Solicitud en proceso') AND (sda.ESTATUSSOLICITUD != 'Solicitud recibida' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud a modificar' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud modificada' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud vencida') AND (sda.ESTATUSSOLICITUD != 'Periodo vencido') AND (sda.ESTATUSSOLICITUD != 'Solicitud caduca') AND (sda.ESTATUSSOLICITUD not like '%Solicitud vencida en:%') AND (sda.ESTATUSSOLICITUD not like '%Período vencido en:%')"
                } else if (object.estatusSolicitud.equals("Aspirantes en proceso aceptados")) {
                    where += " AND (sda.ESTATUSSOLICITUD = 'Sin definir')"
                } else if (object.estatusSolicitud.equals("Aspirantes en proceso resultados")) {
                    where += " AND (sda.ESTATUSSOLICITUD = 'Carga y consulta de resultados')"
                }else if(object.estatusSolicitud.equals("Avanzar Aspirante")){
                    //where += " AND ( sda.ESTATUSSOLICITUD = 'Autodescripción concluida' OR sda.ESTATUSSOLICITUD = 'Elección de pruebas calendarizado' OR sda.ESTATUSSOLICITUD = 'Ya se imprimió su credencial' OR sda.ESTATUSSOLICITUD = 'Periodo vencido')";
                }

            }
            if (lstGrupo.size() > 0) {
                campus += " AND ("
            }
            for (Integer i = 0; i < lstGrupo.size(); i++) {
                String campusMiembro = lstGrupo.get(i);
                campus += "campus.descripcion='" + campusMiembro + "'"
                if (i == (lstGrupo.size() - 1)) {
                    campus += ") "
                } else {
                    campus += " OR "
                }
            }

            errorlog += "campus" + campus;
            errorlog += "object.lstFiltro" + object.lstFiltro
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            closeCon = validarConexion();

            String SSA = "";
            pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
            rs = pstm.executeQuery();
            if (rs.next()) {
                SSA = rs.getString("valor")
            }

            String consulta = Statements.GET_ASPIRANTES_EN_PROCESO

            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                errorlog = consulta + " 1";
                switch (filtro.get("columna")) {

                    case "NOMBRE,EMAIL,CURP":
                        errorlog += "NOMBRE,EMAIL,CURP"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "PROGRAMA,PERÍODO DE INGRESO,CAMPUS INGRESO":
                        errorlog += "PROGRAMA, PERÍODO DE INGRESO, CAMPUS INGRESO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))

                        break;

                    case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
                        errorlog += "PREPARATORIA,ESTADO,PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        /*where +=" ( LOWER(estado.DESCRIPCION) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        */
                        where += "( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += "  OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ULTIMA MODIFICACION":
                        errorlog += "FECHAULTIMAMODIFICACION"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " (LOWER(fechaultimamodificacion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where += " OR to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') ";
                        where += "LIKE LOWER('%[valor]%'))";

                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                        //filtrado utilizado en lista roja y rechazado
                    case "NOMBRE,EMAIL,CURP":
                        errorlog += "NOMBRE,EMAIL,CURP"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "CAMPUS,PROGRAMA,INGRESO":
                        errorlog += "PROGRAMA,INGRESO,CAMPUS"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))

                        break;

                    case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
                        errorlog += "PREPARATORIA,ESTADO,PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += "( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        /*
                        where +=" OR LOWER(sda.estadoextranjero) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        */
                        where += " OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "ESTATUS,TIPO":
                        errorlog += "PREPARATORIA,ESTADO,PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(R.descripcion) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "INDICADORES":
                        errorlog += "INDICADORES"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }

                        where += " ( LOWER(R.descripcion) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(TA.descripcion) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(TAL.descripcion) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))

                        break;

                        // filtrados normales
                    case "NÚMERO DE SOLICITUD":
                        errorlog += "SOLICITUD"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CAST(sda.caseid AS varchar)) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "NOMBRE":
                        errorlog += "NOMBRE"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "EMAIL":
                        errorlog += "EMAIL"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.correoelectronico) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CURP":
                        errorlog += "CURP"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.curp) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS":
                        errorlog += "CAMPUS"
                        campus += " AND LOWER(campus.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            campus += "=LOWER('[valor]')"
                        } else {
                            campus += "LIKE LOWER('%[valor]%')"
                        }
                        campus = campus.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PREPARATORIA":
                        errorlog += "PREPARATORIA"
                        bachillerato += " AND LOWER(prepa.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            bachillerato += "=LOWER('[valor]')"
                        } else {
                            bachillerato += "LIKE LOWER('%[valor]%')"
                        }
                        bachillerato = bachillerato.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROGRAMA":
                        errorlog += "PROGRAMA"
                        programa += " AND LOWER(gestionescolar.Nombre) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            programa += "=LOWER('[valor]')"
                        } else {
                            programa += "LIKE LOWER('%[valor]%')"
                        }
                        programa = programa.replace("[valor]", filtro.get("valor"))
                        break;
                    case "INGRESO":
                        errorlog += "INGRESO"
                        ingreso += " AND LOWER(periodo.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            ingreso += "=LOWER('[valor]')"
                        } else {
                            ingreso += "LIKE LOWER('%[valor]%')"
                        }
                        ingreso = ingreso.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROCEDENCIA":
                        errorlog += "PROCEDENCIA"
                        estado += " AND LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            estado += "=LOWER('[valor]')"
                        } else {
                            estado += "LIKE LOWER('%[valor]%')"
                        }
                        estado = estado.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROMEDIO":
                        errorlog += "PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.PROMEDIOGENERAL) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ESTATUS":
                        errorlog += "ESTATUS"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.ESTATUSSOLICITUD) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "TELEFONO":
                        errorlog += "TELEFONO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.telefonocelular) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "TIPO":
                        errorlog += "TIPO"
                        tipoalumno += " AND LOWER(R.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "IDBANNER":
                        errorlog += "IDBANNER"
                        tipoalumno += " AND LOWER(da.idbanner) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;

                    case "ID BANNER":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(da.idbanner) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "LISTAROJA":
                        errorlog += "LISTAROJA"
                        tipoalumno += " AND LOWER(da.observacionesListaRoja) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "MOTIVO DE LISTA ROJA":
                        errorlog += "LISTAROJA"
                        tipoalumno += " AND LOWER(da.observacionesListaRoja) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "RECHAZO":
                        errorlog += "RECHAZO"
                        tipoalumno += " AND LOWER(da.observacionesRechazo) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "MOTIVO DE LISTA RECHAZO":
                        errorlog += "RECHAZO"
                        tipoalumno += " AND LOWER(da.observacionesRechazo) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA SOLICITUD":
                        errorlog += "FECHA SOLICITUD"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(fechasolicitudenviada) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }


                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                        /*============================================*/
                    case "CIUDAD":
                        errorlog += "CIUDAD";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.ciudad) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROCEDENCIA":
                        errorlog += "PROCEDENCIA";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PAÍS":
                        errorlog += "PAÍS";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(catPais.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS INGRESO":
                        errorlog += "CAMPUS INGRESO";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(campusEstudio.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA DE ENVIO":
                        errorlog += "FECHA DE ENVIO";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " to_char(sda.fechasolicitudenviada::timestamp, 'DD-MM-YYYY HH24:MI:SS') ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ADMISIÓN ANÁHUAC":
                        errorlog += "ADMISIÓN ANÁHUAC";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " sda.admisionAnahuac ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=[valor]"
                        } else {
                            where += "=[valor]"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PAA (COLLEGE BOARD)":
                        errorlog += "PAA (COLLEGE BOARD)";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " sda.tienePAA ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=[valor]"
                        } else {
                            where += "=[valor]"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCUENTO EN EXAMEN":
                        errorlog += "DESCUENTO EN EXAMEN";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " sda.tieneDescuento ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=[valor]"
                        } else {
                            where += "=[valor]"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                        /*====================================================================*/

                    default:

                        //consulta=consulta.replace("[BACHILLERATO]", bachillerato)
                        //consulta=consulta.replace("[WHERE]", where);

                        break;
                }

            }
            errorlog = consulta + " 2";
            switch (object.orderby) {
                case "RESIDEICA":
                    orderby += "residensia";
                    break;
                case "TIPODEADMISION":
                    orderby += "tipoadmision";
                    break;
                case "TIPODEALUMNO":
                    orderby += "tipoDeAlumno";
                    break;
                case "FECHAULTIMAMODIFICACION":
                    orderby += "sda.fechaultimamodificacion";
                    break;
                case "NOMBRE":
                    orderby += "sda.apellidopaterno";
                    break;
                case "EMAIL":
                    orderby += "sda.correoelectronico";
                    break;
                case "CURP":
                    orderby += "sda.curp";
                    break;
                case "CAMPUS":
                    orderby += "campus.DESCRIPCION"
                    break;
                case "PREPARATORIA":
                    orderby += "prepa.DESCRIPCION"
                    break;
                case "PROGRAMA":
                    orderby += "gestionescolar.NOMBRE"
                    break;
                case "INGRESO":
                    orderby += "periodo.DESCRIPCION"
                    break;
                case "PROCEDENCIA":
                    orderby += "CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END";
                    break;
                case "PROMEDIO":
                    orderby += "sda.PROMEDIOGENERAL";
                    break;
                case "ESTATUS":
                    orderby += "sda.ESTATUSSOLICITUD";
                    break;
                case "TIPO":
                    orderby += "da.TIPOALUMNO";
                    break;
                case "TELEFONO":
                    orderby += "sda.telefonocelular";
                    break;
                case "IDBANNER":
                    orderby += "da.idbanner";
                    break;
                case "SOLICITUD":
                    orderby += "sda.caseid::INTEGER";
                    break;
                case "LISTAROJA":
                    orderby += "da.observacionesListaRoja";
                    break;
                case "RECHAZO":
                    orderby += "da.observacionesRechazo";
                    break;
                case "FECHASOLICITUD":
                    orderby += "sda.fechasolicitudenviada";
                    break;
                default:
                    orderby += "sda.persistenceid"
                    break;
            }
            errorlog = consulta + " 3";
            orderby += " " + object.orientation;
            consulta = consulta.replace("[CAMPUS]", campus)
            consulta = consulta.replace("[PROGRAMA]", programa)
            consulta = consulta.replace("[INGRESO]", ingreso)
            consulta = consulta.replace("[ESTADO]", estado)
            consulta = consulta.replace("[BACHILLERATO]", bachillerato)
            consulta = consulta.replace("[TIPOALUMNO]", tipoalumno)
            //consulta=consulta.replace("[TIPORESIDENCIA]", tiporecidencia)
            //consulta=consulta.replace("[TIPODEADMISION]", tipodeadmision)
            //consulta=consulta.replace("[SEDEDELEXAMEN]", sededelexamen)
            where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno
            errorlog = consulta + " 4";

            //where+=" "+campus +" "+programa +" " + ingreso + " " + estado +" "+bachillerato +" "+tipoalumno+" "+tiporecidencia+" "+tipodeadmision+" "+sededelexamen
            consulta = consulta.replace("[WHERE]", where);
            errorlog = consulta + " 5";
            pstm = con.prepareStatement(consulta.replace("periodo.fechafin as periodofin,CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechasolicitudenviada, sda.fechaultimamodificacion, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, sda.telefono, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "").replace("GROUP BY periodo.fechafin,prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.fechaultimamodificacion, sda.fechasolicitudenviada, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,da.tipoalumno,sda.caseid,sda.telefonocelular,sda.telefono,da.observacioneslistaroja,da.observacionesrechazo,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid", ""))
            //pstm = con.prepareStatement(consulta.replace("CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechasolicitudenviada, sda.fechaultimamodificacion, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
            //pstm = con.prepareStatement(consulta.replace("to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechasolicitudenviada, sda.fechaultimamodificacion, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
            //pstm = con.prepareStatement(consulta.replace("sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, estado.DESCRIPCION AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))

            //pstm = con.prepareStatement(consulta.replace("sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.DESCRIPCION AS licenciatura, periodo.DESCRIPCION AS ingreso, estado.DESCRIPCION AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
            errorlog = consulta + " 6";
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorlog = consulta + " 7";

            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)
            rs = pstm.executeQuery()
            rows = new ArrayList < Map < String, Object >> ();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            errorlog = consulta + " 8";
            while (rs.next()) {
                Map < String, Object > columns = new LinkedHashMap < String, Object > ();

                for (int i = 1; i <= columnCount; i++) {
                    columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
                    if (metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
                        String encoded = "";
                        boolean noAzure = false;
                        try {
                            String urlFoto = rs.getString("urlfoto");
                            if (urlFoto != null && !urlFoto.isEmpty()) {
								columns.put("fotografiab64", base64Imagen((rs.getString("urlfoto") + SSA)) );
                                //columns.put("fotografiab64", rs.getString("urlfoto") + SSA);
                            } else {
                                noAzure = true;
                                List < Document > doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
                                for (Document doc: doc1) {
                                    encoded = "../API/formsDocumentImage?document=" + doc.getId();
                                    columns.put("fotografiab64", encoded);
                                }
                            }

                            for (Document doc: context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
                                encoded = "../API/formsDocumentImage?document=" + doc.getId();
                                columns.put("fotografiabpm", encoded);
                            }

                            /*for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
                                encoded = "../API/formsDocumentImage?document="+doc.getId();
                                columns.put("fotografiab64", encoded);
                            } */
                        } catch (Exception e) {
                            LOGGER.error "[ERROR] " + e.getMessage();
                            columns.put("fotografiabpm", "");
                            if(noAzure){
                                columns.put("fotografiab64", "");
                            }
                            errorlog += "" + e.getMessage();
                        }
                    }
                }

                rows.add(columns);
            }
            errorlog = consulta + " 9";
            resultado.setSuccess(true)

            
            resultado.setData(rows)

        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result selectAspirantesEnRed(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = ""
        List < DetalleSolicitud > lstDetalleSolicitud = new ArrayList < DetalleSolicitud > ();

        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;
        Map < String, String > objGrupoCampus = new HashMap < String, String > ();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            closeCon = validarConexion();

            String SSA = "";
            pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
            rs = pstm.executeQuery();
            if (rs.next()) {
                SSA = rs.getString("valor")
            }

            String consulta = Statements.GET_ASPIRANTES_EN_RED

            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                errorlog = consulta + " 1";
                switch (filtro.get("columna")) {

                    case "NOMBRE,EMAIL,CURP":
                        errorlog += "NOMBRE,EMAIL,CURP"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "PROGRAMA,PERÍODO DE INGRESO,CAMPUS INGRESO":
                        errorlog += "PROGRAMA, PERÍODO DE INGRESO, CAMPUS INGRESO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))

                        break;

                    case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
                        errorlog += "PREPARATORIA,ESTADO,PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        /*where +=" ( LOWER(estado.DESCRIPCION) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        */
                        where += "( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += "  OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ULTIMA MODIFICACION":
                        errorlog += "FECHAULTIMAMODIFICACION"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " (LOWER(fechaultimamodificacion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where += " OR to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') ";
                        where += "LIKE LOWER('%[valor]%'))";

                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS,PROGRAMA,INGRESO":
                        errorlog += "PROGRAMA,INGRESO,CAMPUS"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))

                        break;


                    case "ESTATUS,TIPO":
                        errorlog += "PREPARATORIA,ESTADO,PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(R.descripcion) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "VPD":
                        errorlog += "VPD"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }

                        where += " ( LOWER(catcampus.descripcion) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))

                        // where +=" OR LOWER(TA.descripcion) like lower('%[valor]%') ";
                        // where = where.replace("[valor]", filtro.get("valor"))

                        // where +=" OR LOWER(TAL.descripcion) like lower('%[valor]%') )";
                        // where = where.replace("[valor]", filtro.get("valor"))

                        break;
                    case "NOMBRE":
                        errorlog += "NOMBRE"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "EMAIL":
                        errorlog += "EMAIL"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.correoelectronico) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CURP":
                        errorlog += "CURP"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.curp) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS":
                        errorlog += "CAMPUS"
                        campus += " AND LOWER(campus.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            campus += "=LOWER('[valor]')"
                        } else {
                            campus += "LIKE LOWER('%[valor]%')"
                        }
                        campus = campus.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PREPARATORIA":
                        errorlog += "PREPARATORIA"
                        bachillerato += " AND LOWER(prepa.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            bachillerato += "=LOWER('[valor]')"
                        } else {
                            bachillerato += "LIKE LOWER('%[valor]%')"
                        }
                        bachillerato = bachillerato.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROGRAMA":
                        errorlog += "PROGRAMA"
                        programa += " AND LOWER(gestionescolar.Nombre) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            programa += "=LOWER('[valor]')"
                        } else {
                            programa += "LIKE LOWER('%[valor]%')"
                        }
                        programa = programa.replace("[valor]", filtro.get("valor"))
                        break;
                    case "INGRESO":
                        errorlog += "INGRESO"
                        ingreso += " AND LOWER(periodo.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            ingreso += "=LOWER('[valor]')"
                        } else {
                            ingreso += "LIKE LOWER('%[valor]%')"
                        }
                        ingreso = ingreso.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROCEDENCIA":
                        errorlog += "PROCEDENCIA"
                        estado += " AND LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            estado += "=LOWER('[valor]')"
                        } else {
                            estado += "LIKE LOWER('%[valor]%')"
                        }
                        estado = estado.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROMEDIO":
                        errorlog += "PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.PROMEDIOGENERAL) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ESTATUS":
                        errorlog += "ESTATUS"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.ESTATUSSOLICITUD) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "IDBANNER":
                        errorlog += "IDBANNER"
                        tipoalumno += " AND LOWER(da.idbanner) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;

                    case "ID BANNER":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(da.idbanner) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA SOLICITUD":
                        errorlog += "FECHA SOLICITUD"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(fechasolicitudenviada) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }


                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                        /*============================================*/
                    case "CIUDAD":
                        errorlog += "CIUDAD";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.ciudad) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PAÍS":
                        errorlog += "PAÍS";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(catPais.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS INGRESO":
                        errorlog += "CAMPUS INGRESO";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(campusEstudio.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA DE ENVIO":
                        errorlog += "FECHA DE ENVIO";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " to_char(sda.fechasolicitudenviada::timestamp, 'DD-MM-YYYY HH24:MI:SS') ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ADMISIÓN ANÁHUAC":
                        errorlog += "ADMISIÓN ANÁHUAC";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " sda.admisionAnahuac ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=[valor]"
                        } else {
                            where += "=[valor]"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PAA (COLLEGE BOARD)":
                        errorlog += "PAA (COLLEGE BOARD)";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " sda.tienePAA ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=[valor]"
                        } else {
                            where += "=[valor]"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCUENTO EN EXAMEN":
                        errorlog += "DESCUENTO EN EXAMEN";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " sda.tieneDescuento ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=[valor]"
                        } else {
                            where += "=[valor]"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                        /*====================================================================*/

                    default:

                        //consulta=consulta.replace("[BACHILLERATO]", bachillerato)
                        //consulta=consulta.replace("[WHERE]", where);

                        break;
                }

            }
            errorlog = consulta + " 2";
            switch (object.orderby) {
                case "FECHAULTIMAMODIFICACION":
                    orderby += "sda.fechaultimamodificacion";
                    break;
                case "NOMBRE":
                    orderby += "sda.apellidopaterno";
                    break;
                case "EMAIL":
                    orderby += "sda.correoelectronico";
                    break;
                case "CURP":
                    orderby += "sda.curp";
                    break;
                case "CAMPUS":
                    orderby += "campus.DESCRIPCION"
                    break;
                case "PREPARATORIA":
                    orderby += "prepa.DESCRIPCION"
                    break;
                case "PROGRAMA":
                    orderby += "gestionescolar.NOMBRE"
                    break;
                case "INGRESO":
                    orderby += "periodo.DESCRIPCION"
                    break;
                case "PROCEDENCIA":
                    orderby += "CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END";
                    break;
                case "PROMEDIO":
                    orderby += "sda.PROMEDIOGENERAL";
                    break;
                case "ESTATUS":
                    orderby += "sda.ESTATUSSOLICITUD";
                    break;
                case "TIPO":
                    orderby += "da.TIPOALUMNO";
                    break;
                case "VPD":
                    orderby += "NombreVPD";
                    break;
                case "IDBANNER":
                    orderby += "da.idbanner";
                    break;
                case "FECHASOLICITUD":
                    orderby += "sda.fechasolicitudenviada";
                    break;
                default:
                    orderby += "sda.persistenceid"
                    break;
            }
            errorlog = consulta + " 3";
            orderby += " " + object.orientation;
            consulta = consulta.replace("[PROGRAMA]", programa)
            consulta = consulta.replace("[INGRESO]", ingreso)
            consulta = consulta.replace("[ESTADO]", estado)
            consulta = consulta.replace("[BACHILLERATO]", bachillerato)
            consulta = consulta.replace("[TIPOALUMNO]", tipoalumno)
            where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno
            errorlog = consulta + " 4";
            where = where.replace("WHERE", "AND")
            consulta = consulta.replace("[WHERE]", where);
            errorlog = consulta + " 5";
            pstm = con.prepareStatement(consulta.replace("CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechasolicitudenviada, sda.fechaultimamodificacion,  sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as NombreVPD, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "").replace("GROUP BY prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.fechaultimamodificacion, sda.fechasolicitudenviada, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,da.tipoalumno,sda.caseid,sda.telefonocelular,da.observacioneslistaroja,da.observacionesrechazo,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid", ""))
            //pstm = con.prepareStatement(consulta.replace("CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechasolicitudenviada, sda.fechaultimamodificacion, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as NombreVPD, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))

            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorlog = consulta + " 7";

            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)
            rs = pstm.executeQuery()
            rows = new ArrayList < Map < String, Object >> ();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            errorlog = consulta + " 8";
            while (rs.next()) {
                Map < String, Object > columns = new LinkedHashMap < String, Object > ();

                for (int i = 1; i <= columnCount; i++) {
                    columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
                    if (metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
                        String encoded = "";
                        try {
                            String urlFoto = rs.getString("urlfoto");
                            if (urlFoto != null && !urlFoto.isEmpty()) {
                                columns.put("fotografiab64", rs.getString("urlfoto") + SSA);
                            } else {
                                List < Document > doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
                                for (Document doc: doc1) {
                                    encoded = "../API/formsDocumentImage?document=" + doc.getId();
                                    columns.put("fotografiab64", encoded);
                                }
                            }
                            /*
                                for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
                                    encoded = "../API/formsDocumentImage?document="+doc.getId();
                                    columns.put("fotografiab64", encoded);
                                }*/
                        } catch (Exception e) {
                            LOGGER.error "[ERROR] " + e.getMessage();
                            columns.put("fotografiab64", "");
                            errorlog += "" + e.getMessage();
                        }
                    }
                }

                rows.add(columns);
            }
            errorlog = consulta + " 9";
            resultado.setSuccess(true)

            
            resultado.setData(rows)

        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result selectAspirantesMigrados(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = ""
        List < String > lstGrupo = new ArrayList < String > ();
        List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
        List < DetalleSolicitud > lstDetalleSolicitud = new ArrayList < DetalleSolicitud > ();

        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;
        Map < String, String > objGrupoCampus = new HashMap < String, String > ();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);

            List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)

            userLogged = context.getApiSession().getUserId();

            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (CatCampus rowGrupo: lstCatCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
                        lstGrupo.add(rowGrupo.getDescripcion());
                        break;
                    }
                }
            }

            assert object instanceof Map;
            where += " WHERE sda.iseliminado=false AND sda.isAspiranteMigrado=true"
            if (lstGrupo.size() > 0) {
                campus += " AND ("
            }
            for (Integer i = 0; i < lstGrupo.size(); i++) {
                String campusMiembro = lstGrupo.get(i);
                campus += "campus.descripcion='" + campusMiembro + "'"
                if (i == (lstGrupo.size() - 1)) {
                    campus += ") "
                } else {
                    campus += " OR "
                }
            }

            errorlog += "campus" + campus;
            errorlog += "object.lstFiltro" + object.lstFiltro
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            closeCon = validarConexion();

            String SSA = "";
            pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
            rs = pstm.executeQuery();
            if (rs.next()) {
                SSA = rs.getString("valor")
            }

            String consulta = Statements.GET_ASPIRANTES_MIGRADOS

            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                errorlog += ", columna " + filtro.get("columna")
                switch (filtro.get("columna")) {

                    case "NOMBRE":
                        errorlog += "NOMBRE"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(concat(sda.primernombre,' ', sda.segundonombre,' ',sda.apellidopaterno,' ',sda.apellidomaterno)) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "EMAIL":
                        errorlog += "EMAIL"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.correoelectronico) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CURP":
                        errorlog += "CURP"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.curp) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS":
                        errorlog += "CAMPUS"
                        campus += " AND LOWER(campus.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            campus += "=LOWER('[valor]')"
                        } else {
                            campus += "LIKE LOWER('%[valor]%')"
                        }
                        campus = campus.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PREPARATORIA":
                        errorlog += "PREPARATORIA"
                        bachillerato += " AND LOWER(prepa.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            bachillerato += "=LOWER('[valor]')"
                        } else {
                            bachillerato += "LIKE LOWER('%[valor]%')"
                        }
                        bachillerato = bachillerato.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROGRAMA":
                        errorlog += "PROGRAMA"
                        programa += " AND LOWER(gestionescolar.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            programa += "=LOWER('[valor]')"
                        } else {
                            programa += "LIKE LOWER('%[valor]%')"
                        }
                        programa = programa.replace("[valor]", filtro.get("valor"))
                        break;
                    case "INGRESO":
                        errorlog += "INGRESO"
                        ingreso += " AND LOWER(periodo.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            ingreso += "=LOWER('[valor]')"
                        } else {
                            ingreso += "LIKE LOWER('%[valor]%')"
                        }
                        ingreso = ingreso.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ESTADO":
                        errorlog += "ESTADO"
                        estado += " AND LOWER(estado.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            estado += "=LOWER('[valor]')"
                        } else {
                            estado += "LIKE LOWER('%[valor]%')"
                        }
                        estado = estado.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROMEDIO":
                        errorlog += "PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.PROMEDIOGENERAL) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ESTATUS":
                        errorlog += "ESTATUS"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.ESTATUSSOLICITUD) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "TELEFONO":
                        errorlog += "TELEFONO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.telefonocelular) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "TIPO":
                        errorlog += "TIPO"
                        tipoalumno += " AND LOWER(da.TIPOALUMNO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "IDBANNER":
                        errorlog += "IDBANNER"
                        tipoalumno += " AND LOWER(da.idbanner) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "LISTAROJA":
                        errorlog += "LISTAROJA"
                        tipoalumno += " AND LOWER(da.observacionesListaRoja) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "RECHAZO":
                        errorlog += "RECHAZO"
                        tipoalumno += " AND LOWER(da.observacionesRechazo) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "NOMBRE,EMAIL,CURP":
                        errorlog += "NOMBRE,EMAIL,CURP"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS,PROGRAMA,PERIODO":
                        errorlog += "PROGRAMA, PERÍODO DE INGRESO, CAMPUS INGRESO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))

                        break;
                    case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
                        errorlog += "PREPARATORIA,ESTADO,PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        /*
                        where +="  OR LOWER(sda.estadoextranjero) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        */
                        where += "  OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "RESIDENCIA,TIPOADMISION,TIPOALUMNO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(R.descripcion) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(TA.descripcion) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(TAL.descripcion) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    default:
                        //consulta=consulta.replace("[BACHILLERATO]", bachillerato)
                        //consulta=consulta.replace("[WHERE]", where);

                        break;
                }
            }
            switch (object.orderby) {
                case "RESIDENCIA":
                    orderby += "R.descripcion";
                    break;
                case "NOMBRE":
                    orderby += "sda.primernombre";
                    break;
                case "EMAIL":
                    orderby += "sda.correoelectronico";
                    break;
                case "CURP":
                    orderby += "sda.curp";
                    break;
                case "CAMPUS":
                    orderby += "campus.DESCRIPCION"
                    break;
                case "PREPARATORIA":
                    orderby += "prepa.DESCRIPCION"
                    break;
                case "PROGRAMA":
                    orderby += "gestionescolar.DESCRIPCION"
                    break;
                case "INGRESO":
                    orderby += "periodo.DESCRIPCION"
                    break;
                case "ESTADO":
                    orderby += "CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END";
                    break;
                case "PROMEDIO":
                    orderby += "sda.PROMEDIOGENERAL";
                    break;
                case "ESTATUS":
                    orderby += "sda.ESTATUSSOLICITUD";
                    break;
                case "TIPO":
                    orderby += "da.TIPOALUMNO";
                    break;
                case "TELEFONO":
                    orderby += "sda.telefonocelular";
                    break;
                case "IDBANNER":
                    orderby += "da.idbanner";
                    break;
                case "LISTAROJA":
                    orderby += "da.observacionesListaRoja";
                    break;
                case "RECHAZO":
                    orderby += "da.observacionesRechazo";
                    break;
                default:
                    orderby += "sda.persistenceid"
                    break;
            }
            orderby += " " + object.orientation;
            consulta = consulta.replace("[CAMPUS]", campus)
            consulta = consulta.replace("[PROGRAMA]", programa)
            consulta = consulta.replace("[INGRESO]", ingreso)
            consulta = consulta.replace("[ESTADO]", estado)
            consulta = consulta.replace("[BACHILLERATO]", bachillerato)
            consulta = consulta.replace("[TIPOALUMNO]", tipoalumno)
            //consulta=consulta.replace("[TIPORESIDENCIA]", tiporecidencia)
            //consulta=consulta.replace("[TIPODEADMISION]", tipodeadmision)
            //consulta=consulta.replace("[SEDEDELEXAMEN]", sededelexamen)
            where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno
            //where+=" "+campus +" "+programa +" " + ingreso + " " + estado +" "+bachillerato +" "+tipoalumno+" "+tiporecidencia+" "+tipodeadmision+" "+sededelexamen
            consulta = consulta.replace("[WHERE]", where);

            pstm = con.prepareStatement(consulta.replace("sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE as licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, sda.urlFoto, sda.urlConstancia,sda.urlActaNacimiento,sda.urlCartaAA, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia", "COUNT(sda.persistenceid) as registros").replace("LEFT JOIN catRegistro Registro on Registro.caseId= sda.caseId LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=Registro.catgestionescolar_pid", " LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.catgestionescolar_pid ").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            //pstm = con.prepareStatement(consulta.replace("sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE as licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, sda.urlFoto, sda.urlConstancia,sda.urlActaNacimiento,sda.urlCartaAA, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia", "COUNT(sda.persistenceid) as registros").replace("LEFT JOIN catRegistro Registro on Registro.caseId= sda.caseId LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=Registro.catgestionescolar_pid"," LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.catgestionescolar_pid ").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
            //Aqui pegas abajo pegas
            //pstm = con.prepareStatement(consulta.replace("sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE as licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, sda.urlFoto, sda.urlConstancia,sda.urlActaNacimiento, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia", "COUNT(sda.persistenceid) as registros").replace("LEFT JOIN catRegistro Registro on Registro.caseId= sda.caseId LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=Registro.catgestionescolar_pid"," LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.catgestionescolar_pid ").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
            //Esta de abajo comentas
            //pstm = con.prepareStatement(consulta.replace("sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE as licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia", "COUNT(sda.persistenceid) as registros").replace("LEFT JOIN catRegistro Registro on Registro.caseId= sda.caseId LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=Registro.catgestionescolar_pid"," LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.catgestionescolar_pid ").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))

            //pstm = con.prepareStatement(consulta.replace("sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE as licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
            //pstm = con.prepareStatement(consulta.replace("sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
            //pstm = con.prepareStatement(consulta.replace("sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.DESCRIPCION AS licenciatura, periodo.DESCRIPCION AS ingreso, estado.DESCRIPCION AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))

            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorlog += consulta
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)
            rs = pstm.executeQuery()
            rows = new ArrayList < Map < String, Object >> ();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                Map < String, Object > columns = new LinkedHashMap < String, Object > ();

                for (int i = 1; i <= columnCount; i++) {
                    columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
                    if (metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
                        String encoded = "";
                        try {
                            String urlFoto = rs.getString("urlfoto");
                            if (urlFoto != null && !urlFoto.isEmpty()) {
                                columns.put("fotografiab64", rs.getString("urlfoto") + SSA);
                            } else {
                                List < Document > doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
                                for (Document doc: doc1) {
                                    encoded = "../API/formsDocumentImage?document=" + doc.getId();
                                    columns.put("fotografiab64", encoded);
                                }
                            }
                            /*
                                for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
                                    encoded = "../API/formsDocumentImage?document="+doc.getId();
                                    columns.put("fotografiab64", encoded);
                                }*/
                        } catch (Exception e) {
                            LOGGER.error "[ERROR] " + e.getMessage();
                            columns.put("fotografiab64", "");
                            errorlog += "" + e.getMessage();
                        }
                    }
                }

                rows.add(columns);
            }
            resultado.setSuccess(true)

            
            resultado.setData(rows)

        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result selectAspirantesEnprocesoFechas(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = "", tipo_sesion = "";
        List < String > lstGrupo = new ArrayList < String > ();
        List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
        List < DetalleSolicitud > lstDetalleSolicitud = new ArrayList < DetalleSolicitud > ();

        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;
        Map < String, String > objGrupoCampus = new HashMap < String, String > ();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);

            List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)

            userLogged = context.getApiSession().getUserId();

            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (CatCampus rowGrupo: lstCatCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
                        lstGrupo.add(rowGrupo.getDescripcion());
                        break;
                    }
                }
            }

            assert object instanceof Map;
            where += " WHERE sda.iseliminado=false"
            if (object.campus != null) {
                where += " AND LOWER(campus.grupoBonita) = LOWER('" + object.campus + "') "
            }
            if (object.estatusSolicitud != null) {
                if (object.estatusSolicitud.equals("Solicitud lista roja")) {
                    where += " AND sda.ESTATUSSOLICITUD='Solicitud lista roja'"
                } else if (object.estatusSolicitud.equals("Solicitud rechazada")) {
                    where += " AND sda.ESTATUSSOLICITUD='Solicitud rechazada' "
                } else if (object.estatusSolicitud.equals("Nuevas solicitudes")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Solicitud modificada' OR sda.ESTATUSSOLICITUD='Solicitud recibida')"
                } else if (object.estatusSolicitud.equals("Solicitud en proceso")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Solicitud en proceso' OR sda.ESTATUSSOLICITUD='Solicitud a modificar')"
                } else if (object.estatusSolicitud.equals("Aspirantes registrados sin validación de cuenta")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Aspirantes registrados sin validación de cuenta')"
                } else if (object.estatusSolicitud.equals("Aspirantes registrados con validación de cuenta")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Aspirantes registrados con validación de cuenta')"
                } else if (object.estatusSolicitud.equals("Solicitud en espera de pago")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Solicitud en espera de pago')"
                } else if (object.estatusSolicitud.equals("Solicitud con pago aceptado")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Solicitud con pago aceptado')"
                } else if (object.estatusSolicitud.equals("Autodescripción en proceso")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Autodescripción en proceso')"
                } else if (object.estatusSolicitud.equals("Elección de pruebas no calendarizado")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Elección de pruebas no calendarizado')"
                } else if (object.estatusSolicitud.equals("No se ha impreso credencial")) {
                    where += " AND (sda.ESTATUSSOLICITUD='No se ha impreso credencial')"
                } else if (object.estatusSolicitud.equals("Ya se imprimió su credencial")) {
                    where += " AND (sda.ESTATUSSOLICITUD='Ya se imprimió su credencial')"
                }
                //CODIGO PP
                else if (object.estatusSolicitud.equals("Aspirantes en proceso")) {
                    //where+=" AND (sda.ESTATUSSOLICITUD != 'Solicitud rechazada') AND (sda.ESTATUSSOLICITUD != 'Solicitud lista roja') AND (sda.ESTATUSSOLICITUD != 'Aspirantes registrados sin validación de cuenta') AND (sda.ESTATUSSOLICITUD !='Aspirantes registrados con validación de cuenta') AND (sda.ESTATUSSOLICITUD != 'Solicitud en proceso') AND (sda.ESTATUSSOLICITUD != 'Solicitud recibida' )"
                    //where+=" AND (sda.ESTATUSSOLICITUD != 'Solicitud rechazada') AND (sda.ESTATUSSOLICITUD != 'Solicitud lista roja') AND (sda.ESTATUSSOLICITUD != 'Aspirantes registrados sin validación de cuenta') AND (sda.ESTATUSSOLICITUD !='Aspirantes registrados con validación de cuenta') AND (sda.ESTATUSSOLICITUD != 'Solicitud en proceso') AND (sda.ESTATUSSOLICITUD != 'Solicitud recibida' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud a modificar' )"
                    /*if(object.sesiones) {
                        where+=" AND (sda.ESTATUSSOLICITUD = 'Ya se imprimió su credencial' OR sda.ESTATUSSOLICITUD = 'Elección de pruebas calendarizado' OR sda.ESTATUSSOLICITUD = 'Autodescripción en proceso' OR sda.ESTATUSSOLICITUD = 'Autodescripción concluida' )"
                     }else{
                        
                     }*/
                    if (object.sesiones) {
                        where += " AND (sda.ESTATUSSOLICITUD = 'Autodescripción concluida' OR sda.ESTATUSSOLICITUD = 'Ya se imprimió su credencial' OR sda.ESTATUSSOLICITUD = 'Elección de pruebas calendarizado' OR sda.ESTATUSSOLICITUD = 'Carga y consulta de resultados' OR sda.ESTATUSSOLICITUD = 'Resultado final del comité' OR sda.ESTATUSSOLICITUD = 'Rechazado por comité'  )"
                    }else {
                        where += " AND (sda.ESTATUSSOLICITUD = 'Ya se imprimió su credencial' OR sda.ESTATUSSOLICITUD = 'Elección de pruebas calendarizado'  )"
                    }
                }
            }
            if (lstGrupo.size() > 0) {
                campus += " AND ("
            }
            for (Integer i = 0; i < lstGrupo.size(); i++) {
                String campusMiembro = lstGrupo.get(i);
                campus += "campus.descripcion='" + campusMiembro + "'"
                if (i == (lstGrupo.size() - 1)) {
                    campus += ") "
                } else {
                    campus += " OR "
                }
            }

            errorlog += "campus" + campus;

            closeCon = validarConexion();
            String SSA = "";
            pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
            rs = pstm.executeQuery();
            if (rs.next()) {
                SSA = rs.getString("valor")
            }

            errorlog += "object.lstFiltro" + object.lstFiltro
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            //closeCon = validarConexion();

            String consulta = Statements.GET_ASPIRANTES_EN_PROCESO_FECHAS
            if (object.sesiones) {
                consulta = Statements.GET_ASPIRANTES_EN_PROCESO_FECHAS_SESIONES;
            }

            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                errorlog += ", columna " + filtro.get("columna")
                switch (filtro.get("columna")) {

                    case "NOMBRE,EMAIL,CURP":
                        errorlog += "NOMBRE,EMAIL,CURP"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "PROGRAMA,PERÍODO DE INGRESO,CAMPUS INGRESO":
                        errorlog += "PROGRAMA, PERÍODO DE INGRESO, CAMPUS INGRESO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))

                        break;

                    case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
                        errorlog += "PREPARATORIA,ESTADO,PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        /*
                        where +="  OR LOWER(sda.estadoextranjero) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        */
                        where += "  OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ULTIMA MODIFICACION":
                        errorlog += "FECHAULTIMAMODIFICACION"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " (LOWER(sda.fechaultimamodificacion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where += " OR to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') ";
                        where += "LIKE LOWER('%[valor]%'))";

                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                        //filtrado utilizado en lista roja y rechazado
                    case "NOMBRE,EMAIL,CURP":
                        errorlog += "NOMBRE,EMAIL,CURP"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "CAMPUS,PROGRAMA,INGRESO":
                        errorlog += "PROGRAMA,INGRESO,CAMPUS"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))

                        break;

                    case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
                        errorlog += "PREPARATORIA,ESTADO,PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += "( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "NOMBRE SESION,TIPO DE SESION,FECHA DE LA SESION":
                        errorlog += "NOMBRE SESION,TIPO DE SESION,FECHA DE LA SESION"

                        tipo_sesion += " WHERE( LOWER(nombre_sesion) like lower('%[valor]%') ";
                        tipo_sesion = tipo_sesion.replace("[valor]", filtro.get("valor"))

                        tipo_sesion += " OR LOWER(tipo_sesion) like lower('%[valor]%') ";
                        tipo_sesion = tipo_sesion.replace("[valor]", filtro.get("valor"))

                        tipo_sesion += " OR LOWER(CAST(fecha_sesion as varchar)) like lower('%[valor]%') ) ";
                        tipo_sesion = tipo_sesion.replace("[valor]", filtro.get("valor"))
                        break;

                    case "ESTATUS,TIPO":
                        errorlog += "PREPARATORIA,ESTADO,PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(R.descripcion) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "INDICADORES":
                        errorlog += "INDICADORES"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }

                        where += " ( LOWER(R.descripcion) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(TA.descripcion) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))


                        break;

                        // filtrados normales
                    case "NÚMERO DE SOLICITUD":
                        errorlog += "SOLICITUD"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CAST(sda.caseid AS varchar)) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "NOMBRE":
                        errorlog += "NOMBRE"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "EMAIL":
                        errorlog += "EMAIL"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.correoelectronico) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CURP":
                        errorlog += "CURP"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.curp) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS":
                        errorlog += "CAMPUS"
                        campus += " AND LOWER(campus.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            campus += "=LOWER('[valor]')"
                        } else {
                            campus += "LIKE LOWER('%[valor]%')"
                        }
                        campus = campus.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PREPARATORIA":
                        errorlog += "PREPARATORIA"
                        bachillerato += " AND LOWER(prepa.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            bachillerato += "=LOWER('[valor]')"
                        } else {
                            bachillerato += "LIKE LOWER('%[valor]%')"
                        }
                        bachillerato = bachillerato.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROGRAMA":
                        errorlog += "PROGRAMA"
                        programa += " AND LOWER(gestionescolar.Nombre) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            programa += "=LOWER('[valor]')"
                        } else {
                            programa += "LIKE LOWER('%[valor]%')"
                        }
                        programa = programa.replace("[valor]", filtro.get("valor"))
                        break;
                    case "INGRESO":
                        errorlog += "INGRESO"
                        ingreso += " AND LOWER(periodo.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            ingreso += "=LOWER('[valor]')"
                        } else {
                            ingreso += "LIKE LOWER('%[valor]%')"
                        }
                        ingreso = ingreso.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ESTADO":
                        errorlog += "ESTADO"
                        estado += " AND LOWER(estado.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            estado += "=LOWER('[valor]')"
                        } else {
                            estado += "LIKE LOWER('%[valor]%')"
                        }
                        estado = estado.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROMEDIO":
                        errorlog += "PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.PROMEDIOGENERAL) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ESTATUS":
                        errorlog += "ESTATUS"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.ESTATUSSOLICITUD) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "TELEFONO":
                        errorlog += "TELEFONO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.telefonocelular) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "TIPO":
                        errorlog += "TIPO"
                        tipoalumno += " AND LOWER(R.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "IDBANNER":
                        errorlog += "IDBANNER"
                        tipoalumno += " AND LOWER(da.idbanner) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;

                    case "ID BANNER":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(da.idbanner) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "LISTAROJA":
                        errorlog += "LISTAROJA"
                        tipoalumno += " AND LOWER(da.observacionesListaRoja) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "MOTIVO DE LISTA ROJA":
                        errorlog += "LISTAROJA"
                        tipoalumno += " AND LOWER(da.observacionesListaRoja) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "RECHAZO":
                        errorlog += "RECHAZO"
                        tipoalumno += " AND LOWER(da.observacionesRechazo) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "MOTIVO DE LISTA RECHAZO":
                        errorlog += "RECHAZO"
                        tipoalumno += " AND LOWER(da.observacionesRechazo) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA SOLICITUD":
                        errorlog += "FECHA SOLICITUD"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(to_char(sda.fechasolicitudenviada::timestamp, 'DD-MM-YYYY HH24:MI:SS') ) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }


                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "CIUDAD":
                        errorlog += "CIUDAD";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.ciudad) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROCEDENCIA":
                        errorlog += "PROCEDENCIA";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PAÍS":
                        errorlog += "PAÍS";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(catPais.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS INGRESO":
                        errorlog += "CAMPUS INGRESO";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(campusEstudio.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA DE ENVIO":
                        errorlog += "FECHA DE ENVIO";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " to_char(sda.fechasolicitudenviada::timestamp, 'DD-MM-YYYY HH24:MI:SS') ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ADMISIÓN ANÁHUAC":
                        errorlog += "ADMISIÓN ANÁHUAC";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " sda.admisionAnahuac ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=[valor]"
                        } else {
                            where += "=[valor]"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PAA (COLLEGE BOARD)":
                        errorlog += "PAA (COLLEGE BOARD)";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " sda.tienePAA ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=[valor]"
                        } else {
                            where += "=[valor]"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCUENTO EN EXAMEN":
                        errorlog += "DESCUENTO EN EXAMEN";
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " sda.tieneDescuento ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=[valor]"
                        } else {
                            where += "=[valor]"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    default:
                        break;
                }

            }
            switch (object.orderby) {
                case "FECHAULTIMAMODIFICACION":
                    orderby += "sda.fechaultimamodificacion";
                    break;
                case "NOMBRE":
                    orderby += "sda.apellidopaterno";
                    break;
                case "EMAIL":
                    orderby += "sda.correoelectronico";
                    break;
                case "CURP":
                    orderby += "sda.curp";
                    break;
                case "CAMPUS":
                    orderby += "campus.DESCRIPCION"
                    break;
                case "PREPARATORIA":
                    orderby += "prepa.DESCRIPCION"
                    break;
                case "PROGRAMA":
                    orderby += "gestionescolar.NOMBRE"
                    break;
                case "INGRESO":
                    orderby += "periodo.DESCRIPCION"
                    break;
                case "PROCEDENCIA":
                    orderby += "procedencia";
                    break;
                case "PROMEDIO":
                    orderby += "sda.PROMEDIOGENERAL";
                    break;
                case "ESTATUS":
                    orderby += "sda.ESTATUSSOLICITUD";
                    break;
                case "TIPO":
                    orderby += "da.TIPOALUMNO";
                    break;
                case "TELEFONO":
                    orderby += "sda.telefonocelular";
                    break;
                case "IDBANNER":
                    orderby += "da.idbanner";
                    break;
                case "SOLICITUD":
                    orderby += "sda.caseid::INTEGER";
                    break;
                case "LISTAROJA":
                    orderby += "da.observacionesListaRoja";
                    break;
                case "RECHAZO":
                    orderby += "da.observacionesRechazo";
                    break;
                case "FECHASOLICITUD":
                    orderby += "sda.fechasolicitudenviada";
                    break;

                case "NOMBRESESION":
                    orderby += "S.nombre";
                    break;

                case "TIPOSESION":
                    orderby += "s.tipo";
                    break;
                case "FECHASESION":
                    orderby += "s.fecha_inicio";
                    break;
                default:
                    orderby += "sda.persistenceid"
                    break;
            }
            orderby += " " + object.orientation;
            consulta = consulta.replace("[CAMPUS]", campus)
            consulta = consulta.replace("[PROGRAMA]", programa)
            consulta = consulta.replace("[INGRESO]", ingreso)
            consulta = consulta.replace("[ESTADO]", estado)
            consulta = consulta.replace("[BACHILLERATO]", bachillerato)
            consulta = consulta.replace("[TIPOALUMNO]", tipoalumno)
            consulta = consulta.replace("[RESIDENCIA]", tipo_sesion)
            where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno
            consulta = consulta.replace("[WHERE]", where);
            //errorlog += consulta;

            //errorlog= consulta;
            String consultaR = Statements.GET_ASPIRANTES_EN_PROCESO_FECHAS2;

            consultaR = consultaR.replace("[CAMPUS]", campus)
            consultaR = consultaR.replace("[PROGRAMA]", programa)
            consultaR = consultaR.replace("[INGRESO]", ingreso)
            consultaR = consultaR.replace("[ESTADO]", estado)
            consultaR = consultaR.replace("[BACHILLERATO]", bachillerato)
            consultaR = consultaR.replace("[TIPOALUMNO]", tipoalumno)
            consultaR = consultaR.replace("[WHERE]", where);

            //errorlog += " || "+consultaR;
            pstm = con.prepareStatement(consultaR);

            //pstm = con.prepareStatement(consulta.replace("sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.DESCRIPCION AS licenciatura, periodo.DESCRIPCION AS ingreso, estado.DESCRIPCION AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, catcampus.descripcion as transferencia, STRING_AGG( tipo.descripcion || '  ' || p.aplicacion || '   ' || case when tipo.persistenceid=1 then rd.horario  else p.entrada ||' - '||p.salida end, ',' ORDER BY tipo.descripcion , p.aplicacion, horario ) fechasExamenes ", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
            //errorlog = pstm

            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            //consultaR=consultaR.replace("[ORDERBY]", orderby)
            //consultaR=consultaR.replace("[LIMITOFFSET]", " LIMIT?OFFSET ?")

            //consulta=consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")


            errorlog += consulta;
            

            pstm = con.prepareStatement(consulta)
            errorlog = object.limit;
            pstm.setInt(1, object.limit)
            errorlog = "2";
            pstm.setInt(2, object.offset)
            errorlog = consulta;
            rs = pstm.executeQuery()
            errorlog = "4";
            rows = new ArrayList < Map < String, Object >> ();
            errorlog = "5";
            ResultSetMetaData metaData = rs.getMetaData();
            errorlog = "6";
            int columnCount = metaData.getColumnCount();
            errorlog = "7";
            while (rs.next()) {
                Map < String, Object > columns = new LinkedHashMap < String, Object > ();

                for (int i = 1; i <= columnCount; i++) {
                    if (metaData.getColumnLabel(i).toLowerCase().equals("responsables")) {
                        User usr;
                        UserMembership membership;

                        String responsables = rs.getString("responsables");
                        String nombres = "";
                        if (!responsables.equals("null") && responsables != null) {
                            errorlog += " " + responsables;
                            String[] arrOfStr = responsables.split(",");
                            for (String a: arrOfStr) {
                                if (Long.parseLong(a) > 0) {
                                    usr = context.getApiClient().getIdentityAPI().getUser(Long.parseLong(a))
                                    nombres += (nombres.length() > 1?", " : "") + usr.getFirstName() + " " + usr.getLastName()
                                }
                            }
                            columns.put(metaData.getColumnLabel(i).toLowerCase(), nombres);
                        }
                    } else {
                        columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
                        if (metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
                            String encoded = "";
                            try {
                                String urlFoto = rs.getString("urlfoto");
                                if (urlFoto != null && !urlFoto.isEmpty()) {
									columns.put("fotografiab64", base64Imagen((rs.getString("urlfoto") + SSA)) );
                                    //columns.put("fotografiab64", rs.getString("urlfoto") + SSA);
                                } else {
                                    List < Document > doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
                                    for (Document doc: doc1) {
                                        encoded = "../API/formsDocumentImage?document=" + doc.getId();
                                        columns.put("fotografiab64", encoded);
                                    }
                                }


                                /*
                                    for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
                                        encoded = "../API/formsDocumentImage?document="+doc.getId();
                                        columns.put("fotografiab64", encoded);
                                    }
                                */
                            } catch (Exception e) {
                                LOGGER.error "[ERROR] " + e.getMessage();
                                columns.put("fotografiab64", "");
                                errorlog += "" + e.getMessage();
                            }
                        }
                    }


                }

                rows.add(columns);
            }
            resultado.setSuccess(true)


            resultado.setData(rows)

        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getAspirantesProceso(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();

        List < SolicitudAdmisionCustom > lstResultado = new ArrayList < SolicitudAdmisionCustom > ();
        List < String > lstGrupo = new ArrayList < String > ();
        List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
        List < DetalleSolicitud > lstDetalleSolicitud = new ArrayList < DetalleSolicitud > ();

        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;

        Integer start = 0;
        Integer end = 99999;
        Integer inicioContador = 0;
        Integer finContador = 0;

        List < SolicitudDeAdmision > lstSolicitudDeAdmision = new ArrayList < SolicitudDeAdmision > ();
        List < SolicitudDeAdmision > lstAllSolicitud = new ArrayList < SolicitudDeAdmision > ();
        SolicitudDeAdmision objSolicitudDeAdmision = null;
        SolicitudAdmisionCustom objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
        CatCampusCustom objCatCampusCustom = new CatCampusCustom();
        CatPeriodoCustom objCatPeriodoCustom = new CatPeriodoCustom();
        CatEstadosCustom objCatEstadosCustom = new CatEstadosCustom();
        CatLicenciaturaCustom objCatLicenciaturaCustom = new CatLicenciaturaCustom();
        CatBachilleratosCustom objCatBachilleratosCustom = new CatBachilleratosCustom();
        CatGestionEscolar objCatGestionEscolar = new CatGestionEscolar();
        DetalleSolicitudCustom objDetalleSolicitud = new DetalleSolicitudCustom();
        Map < String, String > objGrupoCampus = new HashMap < String, String > ();

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

            List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)

            assert object instanceof Map;
            if (object.lstFiltro != null) {
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

            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (CatCampus rowGrupo: lstCatCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
                        lstGrupo.add(rowGrupo.getDescripcion());
                        break;
                    }
                }
            }
            def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
            def procesoCasoDAO = context.apiClient.getDAO(ProcesoCasoDAO.class);
            def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);

            //context.getApiClient().getProcessAPI().getHumanTaskInstances(rootProcessInstanceId, taskName, startIndex, maxResults)
            /*for(ProcessInstance objProcessInstance : lstProcessInstance) {
                objProcessInstance = lstProcessInstance.get(0).getState();
            }*/


            lstAllSolicitud = objSolicitudDeAdmisionDAO.find(0, 99999);

            for (SolicitudDeAdmision objAllSolicitud: lstAllSolicitud) {
                objAllSolicitud.getCaseId()
            }

            /*Long processDefinitionId = context.getApiClient().getProcessAPI().getProcessDefinitionId("Proceso admisiones", "1.0");
            
            SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
            searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, processDefinitionId.toString());
            searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);*/

            SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
            searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Proceso admisiones");
            final SearchOptions searchOptionsProccess = searchBuilderProccess.done();
            SearchResult < ProcessDeploymentInfo > SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
            List < ProcessDeploymentInfo > lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();

            //Long processDefinitionId = context.getApiClient().getProcessAPI().getProcessDefinitionId("Proceso admisiones", "1.0");
            SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);

            //searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
            inicioContador = 0;
            finContador = 0;
            for (ProcessDeploymentInfo objProcessDeploymentInfo: lstProcessDeploymentInfo) {
                if (inicioContador == 0) {
                    searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
                } else {
                    searchBuilder.or();
                    searchBuilder.differentFrom(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
                }
                inicioContador++;
            }

            searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);
            final SearchOptions searchOptions = searchBuilder.done();
            SearchResult < HumanTaskInstance > SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
            List < HumanTaskInstance > lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();

            for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {


                objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
                objCatCampusCustom = new CatCampusCustom();
                isFound = true;
                caseId = objHumanTaskInstance.getRootContainerId();
                lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end);
                lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(caseId.toString(), start, end);
                if (!lstDetalleSolicitud.empty) {

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


                if (!lstSolicitudDeAdmision.empty) {
                    objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end).get(0);

                    catCampusStr = objSolicitudDeAdmision.getCatCampus() == null?"" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
                    isFoundCampus = false;
                    for (String objGrupo: lstGrupo) {
                        if (catCampusStr.toLowerCase().equals(objGrupo.toLowerCase())) {
                            isFoundCampus = true;
                        }
                    }
                    if (isFoundCampus) {
                        if (object.lstFiltro != null) {
                            for (def row: object.lstFiltro) {
                                isFound = false;
                                assert row instanceof Map;

                                nombreCandidato = objSolicitudDeAdmision.getPrimerNombre() + " " + objSolicitudDeAdmision.getSegundoNombre() + " " + objSolicitudDeAdmision.getApellidoPaterno() + " " + objSolicitudDeAdmision.getApellidoMaterno();


                                if (row.operador == 'Que contenga') {
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
                                        catCampusStr = objSolicitudDeAdmision.getCatCampus() == null?"" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
                                        if (catCampusStr.toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "PROGRAMA") {
                                        gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null?"" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
                                        if (gestionEscolar.contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "INGRESO") {
                                        catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null?"" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
                                        if (catPeriodoStr.toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "ESTADO") {
                                        catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null?"" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
                                        if (catEstadoStr.toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "PREPARATORIA") {
                                        catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null?"" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
                                        if (catBachilleratoStr.toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "PROMEDIO") {
                                        if (objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "ESTATUS") {
                                        if (objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "ÚLTIMA MODIFICACION") {
                                        if (dfSalida.format(objHumanTaskInstance.getReachedStateDate()).toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "TIPO") {
                                        if (objDetalleSolicitud.getTipoAlumno().toString().toLowerCase().contains(row.valor.toString().toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    }

                                    //

                                } else {
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
                                        catCampusStr = objSolicitudDeAdmision.getCatCampus() == null?"" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
                                        if (catCampusStr.toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "PROGRAMA") {
                                        gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null?"" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
                                        if (gestionEscolar.equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "INGRESO") {
                                        catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null?"" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
                                        if (catPeriodoStr.toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "ESTADO") {
                                        catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null?"" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
                                        if (catEstadoStr.toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "PREPARATORIA") {
                                        catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null?"" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
                                        if (catBachilleratoStr.toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "PROMEDIO") {
                                        if (objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "ESTATUS") {
                                        if (objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "ÚLTIMA MODIFICACION") {
                                        if (dfSalida.format(objHumanTaskInstance.getReachedStateDate()).toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "TIPO") {
                                        if (objDetalleSolicitud.getTipoAlumno().toString().toLowerCase().equals(row.valor.toString().toLowerCase())) {
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

                    if (isFound) {
                        String encoded = "";
                        for (Document doc: context.getApiClient().getProcessAPI().getDocumentList(objSolicitudDeAdmision.getCaseId(), "fotoPasaporte", 0, 10)) {
                            //encoded ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
                            encoded = "../API/formsDocumentImage?document=" + doc.getId();
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
                        if (objSolicitudDeAdmision.getCatGestionEscolar() != null) {
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
                        if (objSolicitudDeAdmision.getCatCampus() != null) {
                            objCatCampusCustom.setPersistenceId(objSolicitudDeAdmision.getCatCampus().getPersistenceId());
                            objCatCampusCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatCampus().getPersistenceVersion());
                            objCatCampusCustom.setDescripcion(objSolicitudDeAdmision.getCatCampus().getDescripcion());
                            objCatCampusCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatCampus().getUsuarioBanner());
                            objCatCampusCustom.setClave(objSolicitudDeAdmision.getCatCampus().getClave());
                        }
                        objSolicitudAdmisionCustom.setCatCampus(objCatCampusCustom);

                        objCatPeriodoCustom = new CatPeriodoCustom();
                        if (objSolicitudDeAdmision.getCatPeriodo() != null) {
                            objCatPeriodoCustom.setPersistenceId(objSolicitudDeAdmision.getCatPeriodo().getPersistenceId());
                            objCatPeriodoCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatPeriodo().getPersistenceVersion());
                            objCatPeriodoCustom.setDescripcion(objSolicitudDeAdmision.getCatPeriodo().getDescripcion());
                            objCatPeriodoCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatPeriodo().getUsuarioBanner());
                            objCatPeriodoCustom.setClave(objSolicitudDeAdmision.getCatPeriodo().getClave());
                        }
                        objSolicitudAdmisionCustom.setCatPeriodo(objCatPeriodoCustom);

                        objCatEstadosCustom = new CatEstadosCustom();
                        if (objSolicitudDeAdmision.getCatEstado() != null) {
                            objCatEstadosCustom.setPersistenceId(objSolicitudDeAdmision.getCatEstado().getPersistenceId());
                            objCatEstadosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatEstado().getPersistenceVersion());
                            objCatEstadosCustom.setClave(objSolicitudDeAdmision.getCatEstado().getClave());
                            objCatEstadosCustom.setDescripcion(objSolicitudDeAdmision.getCatEstado().getDescripcion());
                            objCatEstadosCustom.setUsuarioCreacion(objSolicitudDeAdmision.getCatEstado().getUsuarioCreacion());
                        }
                        objSolicitudAdmisionCustom.setCatEstado(objCatEstadosCustom);
                        objSolicitudAdmisionCustom.setCatLicenciatura(objCatLicenciaturaCustom);

                        objCatBachilleratosCustom = new CatBachilleratosCustom();
                        if (objSolicitudDeAdmision.getCatBachilleratos() != null) {
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
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }
        return resultado
    }
	
	public String base64Imagen(String url)  throws Exception {
		String b64 = "";
		if(url.toLowerCase().contains(".jpeg")) {
				b64 = ( "data:image/jpeg;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".png")) {
				b64 = ( "data:image/png;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".jpg")) {
				b64 = ( "data:image/jpg;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".jfif")) {
				b64 = ( "data:image/jfif;base64, "+(new FileDownload().b64Url(url)));
			}
		return  b64
	}

    public Result selectSolicitudesEnProceso(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = ""
        List < String > lstGrupo = new ArrayList < String > ();
        List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
        List < DetalleSolicitud > lstDetalleSolicitud = new ArrayList < DetalleSolicitud > ();

        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;
        Map < String, String > objGrupoCampus = new HashMap < String, String > ();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);

            List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)
            

            userLogged = context.getApiSession().getUserId();

            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (CatCampus rowGrupo: lstCatCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
                        lstGrupo.add(rowGrupo.getDescripcion());
                        break;
                    }
                }
            }

            assert object instanceof Map;
            where += " WHERE sda.iseliminado=false AND campus.descripcion is not null AND (sda.ESTATUSSOLICITUD != 'Solicitud vencida') AND (sda.ESTATUSSOLICITUD != 'Solicitud caduca') AND (sda.ESTATUSSOLICITUD not like '%Solicitud vencida en:%') AND (sda.ESTATUSSOLICITUD not like '%Período vencido en:%') "
            if (object.campus != null) {
                where += " AND LOWER(campus.grupoBonita) = LOWER('" + object.campus + "') "
            }
			
            //Cambio aplicado para que fusione los tres estatus en un solo reporte
            if (object.estatusSolicitud != null) {
                where += " AND (sda.ESTATUSSOLICITUD='Solicitud en proceso' OR sda.ESTATUSSOLICITUD='Solicitud a modificar'  OR sda.ESTATUSSOLICITUD='Aspirantes registrados sin validación de cuenta' OR sda.ESTATUSSOLICITUD='Aspirantes registrados con validación de cuenta' OR sda.ESTATUSSOLICITUD='Periodo vencido' OR (sda.ESTATUSSOLICITUD = 'Solicitud vencida') OR (sda.ESTATUSSOLICITUD = 'Periodo vencido') OR (sda.ESTATUSSOLICITUD = 'Solicitud caduca') OR (sda.ESTATUSSOLICITUD  like '%Solicitud vencida en:%')) ";
            }

            campus += " AND ("

            for (Integer i = 0; i < lstGrupo.size(); i++) {
                String campusMiembro = lstGrupo.get(i);
                campus += "campus.descripcion='" + campusMiembro + "'"
                if (i == (lstGrupo.size() - 1)) {
                    campus += ") "
                } else {
                    campus += " OR "
                }
            }

            errorlog += "campus" + campus;


            closeCon = validarConexion();
            String SSA = "";
            pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
            rs = pstm.executeQuery();
            if (rs.next()) {
                SSA = rs.getString("valor")
            }

            errorlog += "object.lstFiltro" + object.lstFiltro
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			
            String consulta = Statements.GET_SOLICITUDES_EN_PROCESO
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                errorlog += ", columna " + filtro.get("columna")
                switch (filtro.get("columna")) {

                    case "NOMBRE,EMAIL,CURP":
                        errorlog += "NOMBRE,EMAIL,CURP"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "PROGRAMA,INGRESO,CAMPUS":
                        errorlog += "PROGRAMA,INGRESO,CAMPUS"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))

                        where += " OR LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))

                        break;

                    case "PREPARATORIA,PROCEDENCIA,PROMEDIO":
                        errorlog += "PREPARATORIA,ESTADO,PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += "( LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))
						
                        where += " OR LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
                        where = where.replace("[valor]", filtro.get("valor"))
						
                        where += " OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "FECHAULTIMAMODIFICACION":
                        errorlog += "FECHAULTIMAMODIFICACION"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(fechaultimamodificacion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "ULTIMA MODIFICACION":
                        errorlog += "FECHAULTIMAMODIFICACION"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(fechaultimamodificacion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where += " OR to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') ";
                        where += "LIKE LOWER('%[valor]%') )";

                        where = where.replace("[valor]", filtro.get("valor"))
                        break;

                    case "NÚMERO DE SOLICITUD":
                        errorlog += "SOLICITUD"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CAST(sda.caseid AS varchar)) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "NOMBRE":
                        errorlog += "NOMBRE"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "EMAIL":
                        errorlog += "EMAIL"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.correoelectronico) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CURP":
                        errorlog += "CURP"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.curp) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS":
                        errorlog += "CAMPUS"
                        campus += " AND LOWER(campus.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            campus += "=LOWER('[valor]')"
                        } else {
                            campus += "LIKE LOWER('%[valor]%')"
                        }
                        campus = campus.replace("[valor]", filtro.get("valor"))
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(campus.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PREPARATORIA":
                        errorlog += "PREPARATORIA"
                        bachillerato += " AND LOWER(prepa.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            bachillerato += "=LOWER('[valor]')"
                        } else {
                            bachillerato += "LIKE LOWER('%[valor]%')"
                        }
                        bachillerato = bachillerato.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROGRAMA":
                        errorlog += "PROGRAMA"
                        programa += " AND LOWER(gestionescolar.NOMBRE) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            programa += "=LOWER('[valor]')"
                        } else {
                            programa += "LIKE LOWER('%[valor]%')"
                        }
                        programa = programa.replace("[valor]", filtro.get("valor"))
                        break;
                    case "INGRESO":
                        errorlog += "INGRESO"
                        ingreso += " AND LOWER(periodo.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            ingreso += "=LOWER('[valor]')"
                        } else {
                            ingreso += "LIKE LOWER('%[valor]%')"
                        }
                        ingreso = ingreso.replace("[valor]", filtro.get("valor"))

                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(periodo.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ESTADO":
                        errorlog += "ESTADO"
                        estado += " AND LOWER(estado.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            estado += "=LOWER('[valor]')"
                        } else {
                            estado += "LIKE LOWER('%[valor]%')"
                        }
                        estado = estado.replace("[valor]", filtro.get("valor"))

                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(estado.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROMEDIO":
                        errorlog += "PROMEDIO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.PROMEDIOGENERAL) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ESTATUS":
                        errorlog += "ESTATUS"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.ESTATUSSOLICITUD) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
						
					case "ESTATUS,TELEFONO":
                        errorlog += "ESTATUS"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " ( LOWER(sda.ESTATUSSOLICITUD) LIKE LOWER('%[valor]%') ";
						where += " OR LOWER(sda.telefonocelular) LIKE LOWER('%[valor]%') )";
						
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "TELEFONO":
                        errorlog += "TELEFONO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.telefonocelular) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "TIPO":
                        errorlog += "TIPO"
                        tipoalumno += " AND LOWER(da.TIPOALUMNO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "IDBANNER":
                        errorlog += "IDBANNER"
                        tipoalumno += " AND LOWER(da.idbanner) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "LISTAROJA":
                        errorlog += "LISTAROJA"
                        tipoalumno += " AND LOWER(da.observacionesListaRoja) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "RECHAZO":
                        errorlog += "RECHAZO"
                        tipoalumno += " AND LOWER(da.observacionesRechazo) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                        /*ARTURO INICIO=====================================================*/
                    case "CIUDAD":
                        errorlog += "CIUDAD"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.ciudad) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PAÍS":
                        errorlog += "PAÍS"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(catPais.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS INGRESO":
                        errorlog += "CAMPUS INGRESO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(campusEstudio.descripcion) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA DE ULTIMO MOVIMIENTO":
                        errorlog += "FECHA DE ULTIMO MOVIMIENTO"
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " to_char(sda.fechaultimamodificacion::timestamp, 'DD-MM-YYYY HH24:MI:SS') ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                        /*ARTURO FIN========================================================*/
                    default:
                        //consulta=consulta.replace("[BACHILLERATO]", bachillerato)
                        //consulta=consulta.replace("[WHERE]", where);

                        break;
                }
            }
            switch (object.orderby) {
                case "FECHAULTIMAMODIFICACION":
                    orderby += "sda.fechaultimamodificacion";
                    break;
                case "NOMBRE":
                    orderby += "sda.apellidopaterno";
                    break;
                case "EMAIL":
                    orderby += "sda.correoelectronico";
                    break;
                case "CURP":
                    orderby += "sda.curp";
                    break;
                case "CAMPUS":
                    orderby += "campus.DESCRIPCION"
                    break;
                case "PREPARATORIA":
                    orderby += "prepa.DESCRIPCION"
                    break;
                case "PROGRAMA":
                    orderby += "gestionescolar.DESCRIPCION"
                    break;
                case "INGRESO":
                    orderby += "periodo.DESCRIPCION"
                    break;
                case "PROCEDENCIA":
                    orderby += "procedencia";
                    break;
                case "PROMEDIO":
                    orderby += "sda.PROMEDIOGENERAL";
                    break;
                case "ESTATUS":
                    orderby += "sda.ESTATUSSOLICITUD";
                    break;
                case "TIPO":
                    orderby += "da.TIPOALUMNO";
                    break;
                case "TELEFONO":
                    orderby += "sda.telefonocelular";
                    break;
                case "IDBANNER":
                    orderby += "da.idbanner";
                    break;
                case "SOLICITUD":
                    orderby += "sda.caseid::INTEGER";
                    break;
                case "LISTAROJA":
                    orderby += "da.observacionesListaRoja";
                    break;
                case "RECHAZO":
                    orderby += "da.observacionesRechazo";
                    break;
                case "CIUDAD":
                    orderby += "sda.ciudad";
                    break;
                case "PAÍS":
                    orderby += "catPais.descripcion";
                    break;
                case "CAMPUS INGRESO":
                    orderby += "campusEstudio.descripcion";
                    break;
                case "FECHA DE ULTIMO MOVIMIENTO":
                    orderby += "sda.fechaultimamodificacion";
                    break;
                default:
                    orderby += "sda.persistenceid"
                    break;
            }
            orderby += " " + object.orientation;
            consulta = consulta.replace("[CAMPUS]", campus)
            consulta = consulta.replace("[PROGRAMA]", programa)
            consulta = consulta.replace("[INGRESO]", ingreso)
            consulta = consulta.replace("[ESTADO]", estado)
            consulta = consulta.replace("[BACHILLERATO]", bachillerato)
            consulta = consulta.replace("[TIPOALUMNO]", tipoalumno)
            consulta = consulta.replace("[WHERE]", where);
            //|123|
            //                pstm = con.prepareStatement(consulta.replace("sda.urlfoto,sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, estado.DESCRIPCION AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
            pstm = con.prepareStatement(consulta.replace("to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechaultimamodificacion, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, periodo.fechafin AS periodofin,CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, REGISTRO.numeroContacto", "COUNT(DISTINCT sda.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""));
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
			
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", "")
			consulta= "SELECT DISTINCT * FROM (" + consulta + ")datos  LIMIT ? OFFSET ? "
            errorlog += consulta
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)
            rs = pstm.executeQuery()
            rows = new ArrayList < Map < String, Object >> ();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                Map < String, Object > columns = new LinkedHashMap < String, Object > ();

                for (int i = 1; i <= columnCount; i++) {
                    columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
                    if (metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
                        String encoded = "";
                        try {

                            String urlFoto = rs.getString("urlfoto");
                            if (urlFoto != null && !urlFoto.isEmpty()) {
							columns.put("fotografiab64", base64Imagen((rs.getString("urlfoto") + SSA)) );
                                //columns.put("fotografiab64", rs.getString("urlfoto") + SSA);
                            } else {
                                List < Document > doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
                                for (Document doc: doc1) {
                                    encoded = "../API/formsDocumentImage?document=" + doc.getId();
                                    columns.put("fotografiab64", encoded);
                                }
                            }

                            /*for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
                                encoded = "../API/formsDocumentImage?document="+doc.getId();
                                columns.put("fotografiab64", encoded);
                            }*/
                        } catch (Exception e) {
                            LOGGER.error "[ERROR] " + e.getMessage();
                            columns.put("fotografiab64", "");
                            errorlog += "" + e.getMessage();
                        }
                    }
                }

                rows.add(columns);
            }
            resultado.setSuccess(true)

            
            resultado.setData(rows)

        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result getAspirantesByStatusTemprano(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();

        List < SolicitudAdmisionCustom > lstResultado = new ArrayList < SolicitudAdmisionCustom > ();
        List < String > lstGrupo = new ArrayList < String > ();
        List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
        List < SolicitudDeAdmision > lstSolicitudDeAdmision = new ArrayList < SolicitudDeAdmision > ();
        List < SolicitudDeAdmision > lstAllSolicitud = new ArrayList < SolicitudDeAdmision > ();
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
        Map < String, String > objGrupoCampus = new HashMap < String, String > ();
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

            List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)
            assert object instanceof Map;
            if (object.lstFiltro != null) {
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

            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (CatCampus rowGrupo: lstCatCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
                        lstGrupo.add(rowGrupo.getDescripcion());
                        break;
                    }
                }
            }

            def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
            def procesoCasoDAO = context.apiClient.getDAO(ProcesoCasoDAO.class);

            lstAllSolicitud = objSolicitudDeAdmisionDAO.find(0, 99999);

            for (SolicitudDeAdmision objAllSolicitud: lstAllSolicitud) {
                objAllSolicitud.getCaseId()
            }

            SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
            searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Proceso admisiones");

            final SearchOptions searchOptionsProccess = searchBuilderProccess.done();
            SearchResult < ProcessDeploymentInfo > SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
            List < ProcessDeploymentInfo > lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
            SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);

            inicioContador = 0;
            finContador = 0;

            //            for(ProcessDeploymentInfo  objProcessDeploymentInfo : lstProcessDeploymentInfo) {
            //                errorLog += objProcessDeploymentInfo.getVersion() + " ESTE ES EL CASEID DEL PROCESO, ";
            //                if(inicioContador == 0) {
            //                    searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
            //                }
            //                else {
            //                    searchBuilder.or();
            //                    searchBuilder.differentFrom(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
            //                }
            //
            //                inicioContador++;
            //            }
            //
            searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, "9041478379247961769");

            errorLog += "searchBuilder :: " + searchBuilder.toString() + " ";
            searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);

            final SearchOptions searchOptions = searchBuilder.done();
            errorLog += "searchOptions " + searchOptions.toString() + " :: ";
            SearchResult < HumanTaskInstance > SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
            List < HumanTaskInstance > lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
            for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {

                objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
                objCatCampusCustom = new CatCampusCustom();
                isFound = true;
                caseId = objHumanTaskInstance.getRootContainerId();
                lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end);

                if (!lstSolicitudDeAdmision.empty) {
                    objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end).get(0);
                    catCampusStr = objSolicitudDeAdmision.getCatCampus() == null?"" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
                    isFoundCampus = false;
                    for (String objGrupo: lstGrupo) {
                        if (catCampusStr.toLowerCase().equals(objGrupo.toLowerCase())) {
                            isFoundCampus = true;
                        }
                    }
                    if (isFoundCampus) {
                        if (object.lstFiltro.size() > 0) {
                            for (def row: object.lstFiltro) {
                                isFound = false;
                                assert row instanceof Map;
                                isFound = true;
                                nombreCandidato = objSolicitudDeAdmision.getPrimerNombre() + " " + objSolicitudDeAdmision.getSegundoNombre() + " " + objSolicitudDeAdmision.getApellidoPaterno() + " " + objSolicitudDeAdmision.getApellidoMaterno();
                                if (row.operador == 'Que contenga') {
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
                                        catCampusStr = objSolicitudDeAdmision.getCatCampus() == null?"" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
                                        if (catCampusStr.toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else if (row.columna == "PROGRAMA") {
                                        gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null?"" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
                                        if (gestionEscolar.contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else if (row.columna == "INGRESO") {
                                        catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null?"" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
                                        if (catPeriodoStr.toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else if (row.columna == "ESTATUS") {
                                        if (objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().contains(row.valor.toLowerCase())) {
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
                                        catCampusStr = objSolicitudDeAdmision.getCatCampus() == null?"" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
                                        if (catCampusStr.toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else if (row.columna == "PROGRAMA") {
                                        gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null?"" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
                                        if (gestionEscolar.toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else if (row.columna == "INGRESO") {
                                        catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null?"" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
                                        if (catPeriodoStr.toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else if (row.columna == "ESTATUS") {
                                        if (objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(row.valor.toLowerCase())) {
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

                    if (isFound) {
                        if (estatusSolicitud != null && objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(estatusSolicitud.toLowerCase())) {
                            String encoded = "";
                            for (Document doc: context.getApiClient().getProcessAPI().getDocumentList(objSolicitudDeAdmision.getCaseId(), "fotoPasaporte", 0, 10)) {
                                encoded = "../API/formsDocumentImage?document=" + doc.getId();
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
                            if (objSolicitudDeAdmision.getCatGestionEscolar() != null) {
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
                            if (objSolicitudDeAdmision.getCatCampus() != null) {
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
                            if (objSolicitudDeAdmision.getCatPeriodo() != null) {
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
                            if (objSolicitudDeAdmision.getCatEstado() != null) {
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
                            if (objSolicitudDeAdmision.getCatBachilleratos() != null) {
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
            
        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }
        return resultado
    }

    public Result getDocumentoTest(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Long caseId = 11001L;
        try {

            for (Document doc: context.getApiClient().getProcessAPI().getDocumentList(caseId, "DocInformacionDocumento", 0, 10)) {
                String encoded = "data:image/png;base64, " + Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
            }



        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }
        return resultado
    }

    public Result getAspirantesByStatus(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();

        List < SolicitudAdmisionCustom > lstResultado = new ArrayList < SolicitudAdmisionCustom > ();
        List < String > lstGrupo = new ArrayList < String > ();
        List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
        List < SolicitudDeAdmision > lstSolicitudDeAdmision = new ArrayList < SolicitudDeAdmision > ();
        List < DetalleSolicitud > lstDetalleSolicitud = new ArrayList < DetalleSolicitud > ();
        List < SolicitudDeAdmision > lstAllSolicitud = new ArrayList < SolicitudDeAdmision > ();

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
        Map < String, String > objGrupoCampus = new HashMap < String, String > ();

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
            if (object.lstFiltro != null) {
                assert object.lstFiltro instanceof List;
            }
            def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);

            List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)

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
            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (CatCampus rowGrupo: lstCatCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
                        lstGrupo.add(rowGrupo.getDescripcion());
                        break;
                    }
                }
            }

            def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
            def procesoCasoDAO = context.apiClient.getDAO(ProcesoCasoDAO.class);
            def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);

            //context.getApiClient().getProcessAPI().getHumanTaskInstances(rootProcessInstanceId, taskName, startIndex, maxResults)
            /*for(ProcessInstance objProcessInstance : lstProcessInstance) {
                objProcessInstance = lstProcessInstance.get(0).getState();
            }*/


            lstAllSolicitud = objSolicitudDeAdmisionDAO.find(0, 99999);

            for (SolicitudDeAdmision objAllSolicitud: lstAllSolicitud) {
                objAllSolicitud.getCaseId()
            }
            /*
            Long processDefinitionId = context.getApiClient().getProcessAPI().getProcessDefinitionId("Proceso admisiones", "1.0");
            
            SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
            searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, processDefinitionId.toString());*/
            SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
            searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Proceso admisiones");
            final SearchOptions searchOptionsProccess = searchBuilderProccess.done();
            SearchResult < ProcessDeploymentInfo > SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
            List < ProcessDeploymentInfo > lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();

            //Long processDefinitionId = context.getApiClient().getProcessAPI().getProcessDefinitionId("Proceso admisiones", "1.0");
            SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);

            //searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
            inicioContador = 0;
            finContador = 0;
            for (ProcessDeploymentInfo objProcessDeploymentInfo: lstProcessDeploymentInfo) {
                if (inicioContador == 0) {
                    searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
                } else {
                    searchBuilder.or();
                    searchBuilder.differentFrom(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, objProcessDeploymentInfo.getProcessId().toString());
                }
                inicioContador++;
            }

            searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);

            final SearchOptions searchOptions = searchBuilder.done();
            SearchResult < HumanTaskInstance > SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
            List < HumanTaskInstance > lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();

            for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {

                objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
                objCatCampusCustom = new CatCampusCustom();
                isFound = true;

                caseId = objHumanTaskInstance.getRootContainerId();
                lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end);

                lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(caseId.toString(), start, end);
                if (!lstDetalleSolicitud.empty) {
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
                if (!lstSolicitudDeAdmision.empty) {
                    objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end).get(0);

                    catCampusStr = objSolicitudDeAdmision.getCatCampus() == null?"" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
                    isFoundCampus = false;
                    for (String objGrupo: lstGrupo) {
                        if (catCampusStr.toLowerCase().equals(objGrupo.toLowerCase())) {
                            isFoundCampus = true;
                        }
                    }
                    if (isFoundCampus) {
                        if (object.lstFiltro != null) {
                            for (def row: object.lstFiltro) {
                                isFound = false;
                                assert row instanceof Map;

                                nombreCandidato = objSolicitudDeAdmision.getPrimerNombre() + " " + objSolicitudDeAdmision.getSegundoNombre() + " " + objSolicitudDeAdmision.getApellidoPaterno() + " " + objSolicitudDeAdmision.getApellidoMaterno();
                                if (row.operador == 'Que contenga') {
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
                                        catCampusStr = objSolicitudDeAdmision.getCatCampus() == null?"" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
                                        if (catCampusStr.toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "PROGRAMA") {
                                        gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null?"" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
                                        if (gestionEscolar.contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "INGRESO") {
                                        catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null?"" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
                                        if (catPeriodoStr.toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "ESTADO") {
                                        catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null?"" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
                                        if (catEstadoStr.toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "PREPARATORIA") {
                                        catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null?"" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
                                        if (catBachilleratoStr.toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "PROMEDIO") {
                                        if (objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "ESTATUS") {
                                        if (objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna.toString() == "ID BANNER") {
                                        if (objDetalleSolicitud.getIdBanner().toString().toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "TELÉFONO") {
                                        if (objSolicitudDeAdmision.getTelefonoCelular().toString().toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "MOTIVO RECHAZO") {
                                        if (objDetalleSolicitud.getObservacionesRechazo().toString().toLowerCase().contains(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "MOTIVO LISTA ROJA") {
                                        if (objDetalleSolicitud.getObservacionesListaRoja().toString().toLowerCase().contains(row.valor.toLowerCase())) {
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
                                        catCampusStr = objSolicitudDeAdmision.getCatCampus() == null?"" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
                                        if (catCampusStr.toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "PROGRAMA") {
                                        gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null?"" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
                                        if (gestionEscolar.toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "INGRESO") {
                                        catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null?"" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
                                        if (catPeriodoStr.toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "ESTADO") {
                                        catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null?"" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
                                        if (catEstadoStr.toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "PREPARATORIA") {
                                        catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null?"" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
                                        if (catBachilleratoStr.toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "PROMEDIO") {
                                        if (objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "ESTATUS") {
                                        if (objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "ID BANNER") {

                                        if (objDetalleSolicitud.getIdBanner().toString().toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "TELÉFONO") {
                                        if (objSolicitudDeAdmision.getTelefonoCelular().toString().toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "MOTIVO RECHAZO") {
                                        if (objDetalleSolicitud.getObservacionesRechazo().toString().toLowerCase().equals(row.valor.toLowerCase())) {
                                            isFound = true;
                                            break;
                                        }
                                    } else
                                    if (row.columna == "MOTIVO LISTA ROJA") {
                                        if (objDetalleSolicitud.getObservacionesListaRoja().toString().toLowerCase().equals(row.valor.toLowerCase())) {
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
                    if (isFound) {


                        if (objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(estatusSolicitud.toLowerCase())) {
                            String encoded = "";
                            for (Document doc: context.getApiClient().getProcessAPI().getDocumentList(objSolicitudDeAdmision.getCaseId(), "fotoPasaporte", 0, 10)) {
                                //encoded ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
                                encoded = "../API/formsDocumentImage?document=" + doc.getId();
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
                            if (objSolicitudDeAdmision.getCatGestionEscolar() != null) {
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
                            if (objSolicitudDeAdmision.getCatCampus() != null) {
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
                            if (objSolicitudDeAdmision.getCatPeriodo() != null) {
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
                            if (objSolicitudDeAdmision.getCatEstado() != null) {
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
                            if (objSolicitudDeAdmision.getCatBachilleratos() != null) {
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
        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }
        return resultado
    }

    public Result getExcelFile(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        List < Object > lstParams;
        String errorlog = "";
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            Result dataResult = new Result();
            int rowCount = 0;

            String type = object.type;
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(type);
            CellStyle style = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);

            if (type.equals("nuevas_solicitudes")) {
                dataResult = selectAspirantesEnproceso(parameterP, parameterC, jsonData, context);

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
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

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
                header1.setCellValue("NÚMERO DE SOLICITUD");
                header1.setCellStyle(style);
                Cell header2 = headersRow.createCell(1);
                header2.setCellValue("NOMBRE");
                header2.setCellStyle(style);
                Cell header3 = headersRow.createCell(2);
                header3.setCellValue("EMAIL");
                header3.setCellStyle(style);
                Cell header4 = headersRow.createCell(3);
                header4.setCellValue("CURP");
                header4.setCellStyle(style);

                Cell header10 = headersRow.createCell(4);
                header10.setCellValue("PROGRAMA");
                header10.setCellStyle(style);
                Cell header11 = headersRow.createCell(5);
                header11.setCellValue("PERÍODO DE INGRESO");
                header11.setCellStyle(style);
                Cell header12 = headersRow.createCell(6);
                header12.setCellValue("CAMPUS INGRESO");
                header12.setCellStyle(style);


                Cell header6 = headersRow.createCell(7);
                header6.setCellValue("PREPARATORIA");
                header6.setCellStyle(style);
                Cell header5 = headersRow.createCell(8);
                header5.setCellValue("PROCEDENCIA");
                header5.setCellStyle(style);
                Cell header7 = headersRow.createCell(9);
                header7.setCellValue("PROMEDIO");
                header7.setCellStyle(style);

                Cell header8 = headersRow.createCell(10);
                header8.setCellValue("FECHA DE ENVIO");
                header8.setCellStyle(style);
                Cell header9 = headersRow.createCell(11);
                header9.setCellValue("FECHA ULTIMA MODIFICACIÓN");
                header9.setCellStyle(style);

                //SolicitudAdmisionCustom  solicitud = new SolicitudAdmisionCustom();
                DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date fechaCreacion = new Date();
                DateFormat dformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                for (int i = 0; i < lstParams.size(); ++i) {
                    //solicitud = new SolicitudAdmisionCustom();
                    //solicitud = (SolicitudAdmisionCustom)lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(lstParams[i].caseid);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(
                        lstParams[i].apellidopaterno + " " +
                        lstParams[i].apellidomaterno + " " +
                        lstParams[i].primernombre + " " +
                        lstParams[i].segundonombre
                    );
                    Cell cell3 = row.createCell(2);
                    cell3.setCellValue(lstParams[i].correoelectronico);
                    Cell cell4 = row.createCell(3);
                    cell4.setCellValue(lstParams[i].curp);

                    Cell cell10 = row.createCell(4);
                    cell10.setCellValue(lstParams[i].licenciatura);
                    Cell cell11 = row.createCell(5);
                    cell11.setCellValue(lstParams[i].ingreso);
                    Cell cell12 = row.createCell(6);
                    cell12.setCellValue(lstParams[i].campus);


                    Cell cell5 = row.createCell(7);
                    cell5.setCellValue(lstParams[i].preparatoria);
                    Cell cell6 = row.createCell(8);
                    cell6.setCellValue(lstParams[i].procedencia);
                    Cell cell7 = row.createCell(9);
                    cell7.setCellValue(lstParams[i].promediogeneral);

                    Cell cell8 = row.createCell(10);
                    if (lstParams[i].fechasolicitudenviada != null) {
                        fechaCreacion = dfSalida.parse(lstParams[i].fechasolicitudenviada.toString())
                        cell8.setCellValue(dformat.format(fechaCreacion));
                    } else {
                        cell8.setCellValue("");
                    }

                    Cell cell9 = row.createCell(11);
                    if (lstParams[i].fechaultimamodificacion != null) {
                        fechaCreacion = dfSalida.parse(lstParams[i].fechaultimamodificacion.toString())
                        cell9.setCellValue(dformat.format(fechaCreacion));
                    } else {
                        cell9.setCellValue("");
                    }

                }
            } else if (type.equals("solicitudes_progreso")) {
                dataResult = selectSolicitudesEnProceso(parameterP, parameterC, jsonData, context);

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
                cellTitle.setCellValue("SOLICITUDES EN PROCESO");
                Cell cellFecha = titleRow.createCell(4);
                cellFecha.setCellValue("Fecha:");
                cellFecha.setCellStyle(style);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
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
                //                Cell headerprbs = espacio.createCell(10);
                //                headerprbs.setCellValue("SOLICITUDES EN PROGRESO");
                //                headerprbs.setCellStyle(style);
                Row headersRow = sheet.createRow(++rowCount);
                Cell header0 = headersRow.createCell(0);
                header0.setCellValue("NÚMERO DE SOLICITUD");
                header0.setCellStyle(style);
                Cell header1 = headersRow.createCell(1);
                header1.setCellValue("NOMBRE");
                header1.setCellStyle(style);
                Cell header2 = headersRow.createCell(2);
                header2.setCellValue("EMAIL");
                header2.setCellStyle(style);
                Cell header3 = headersRow.createCell(3);
                header3.setCellValue("CURP");
                header3.setCellStyle(style);
                Cell header4 = headersRow.createCell(4);
                header4.setCellValue("PROGRAMA");
                header4.setCellStyle(style);
                Cell header5 = headersRow.createCell(5);
                header5.setCellValue("PERÍODO DE INGRESO");
                header5.setCellStyle(style);
                Cell header6 = headersRow.createCell(6);
                header6.setCellValue("CAMPUS INGRESO");
                header6.setCellStyle(style);
                Cell header7 = headersRow.createCell(7);
                header7.setCellValue("PROCEDENCIA");
                header7.setCellStyle(style);
                Cell header8 = headersRow.createCell(8);
                header8.setCellValue("PREPARATORIA");
                header8.setCellStyle(style);
                Cell header9 = headersRow.createCell(9);
                header9.setCellValue("PROMEDIO");
                header9.setCellStyle(style);
                Cell header10 = headersRow.createCell(10);
                header10.setCellValue("ESTATUS");
                header10.setCellStyle(style);
                Cell header11 = headersRow.createCell(11);
                header11.setCellValue("FECHA ULTIMA MODIFICACIÓN");
                header11.setCellStyle(style);
                //FORMATO DE LAS FECHAS
                DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date fechaCreacion = new Date();
                DateFormat dformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                for (int i = 0; i < lstParams.size(); ++i) {
                    //SolicitudAdmisionCustom  solicitud = (SolicitudAdmisionCustom) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    Cell cell0 = row.createCell(0);
                    cell0.setCellValue(lstParams[i].caseid);

                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(
                        lstParams[i].apellidopaterno + " " +
                        lstParams[i].apellidomaterno + " " +
                        lstParams[i].primernombre + " " +
                        lstParams[i].segundonombre
                    );

                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue(lstParams[i].correoelectronico);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(lstParams[i].curp);
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue(lstParams[i].licenciatura);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(lstParams[i].ingreso);
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(lstParams[i].campus);
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(lstParams[i].procedencia);
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue(lstParams[i].preparatoria);
                    Cell cell9 = row.createCell(9);
                    cell9.setCellValue(lstParams[i].promediogeneral);
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue(lstParams[i].estatussolicitud);
                    Cell cell11 = row.createCell(11);

                    if (lstParams[i].fechaultimamodificacion != null) {
                        fechaCreacion = dfSalida.parse(lstParams[i].fechaultimamodificacion.toString())
                        cell11.setCellValue(dformat.format(fechaCreacion));
                    } else {
                        cell11.setCellValue("");
                    }

                    //Cell cell11 = row.createCell(10);
                    //cell11.setCellValue( lstParams[i].fechaUltimaModificacion);
                }
            } else if (type.equals("aspirantes_proceso")) {
                //dataResult = getAspirantesProceso(parameterP, parameterC, jsonData, context);
                dataResult = selectAspirantesEnproceso(parameterP, parameterC, jsonData, context);
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
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

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

                Cell header0 = headersRow.createCell(0);
                header0.setCellValue("ID BANNER");
                header0.setCellStyle(style);

                Cell header1 = headersRow.createCell(1);
                header1.setCellValue("NOMBRE");
                header1.setCellStyle(style);
                Cell header2 = headersRow.createCell(2);
                header2.setCellValue("EMAIL");
                header2.setCellStyle(style);
                Cell header3 = headersRow.createCell(3);
                header3.setCellValue("CURP");
                header3.setCellStyle(style);
                Cell header4 = headersRow.createCell(4);
                header4.setCellValue("CAMPUS");
                header4.setCellStyle(style);
                Cell header5 = headersRow.createCell(5);
                header5.setCellValue("PROGRAMA");
                header5.setCellStyle(style);
                Cell header6 = headersRow.createCell(6);
                header6.setCellValue("INGRESO");
                header6.setCellStyle(style);
                Cell header7 = headersRow.createCell(7);
                header7.setCellValue("PROCEDENCIA");
                header7.setCellStyle(style);
                Cell header8 = headersRow.createCell(8);
                header8.setCellValue("PREPARATORIA");
                header8.setCellStyle(style);
                Cell header9 = headersRow.createCell(9);
                header9.setCellValue("PROMEDIO");
                header9.setCellStyle(style);

                Cell header10 = headersRow.createCell(10);
                header10.setCellValue("ESTATUS");
                header10.setCellStyle(style);

                Cell header13 = headersRow.createCell(11);
                header13.setCellValue("RESIDENCIA");
                header13.setCellStyle(style);

                Cell header14 = headersRow.createCell(12);
                header14.setCellValue("TIPO ADMISIÓN");
                header14.setCellStyle(style);


                Cell header15 = headersRow.createCell(13);
                header15.setCellValue("MISMO CAMPUS O TRANSFERENCIA");
                header15.setCellStyle(style);

                Cell header11 = headersRow.createCell(14);
                header11.setCellValue("FECHA SOLICITUD ENVIADA");
                header11.setCellStyle(style);

                Cell header12 = headersRow.createCell(15);
                header12.setCellValue("FECHA ULTIMA MODIFICACIÓN");
                header12.setCellStyle(style);
				
				Cell header16 = headersRow.createCell(16);
                header16.setCellValue("TELÉFONO CELULAR");
                header16.setCellStyle(style);
				
				Cell header17 = headersRow.createCell(17);
                header17.setCellValue("TELÉFONO DE CASA");
                header17.setCellStyle(style);

                //FORMATO DE LAS FECHAS
                DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date fechaCreacion = new Date();
                DateFormat dformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                for (int i = 0; i < lstParams.size(); ++i) {
                    //SolicitudAdmisionCustom  solicitud = (SolicitudAdmisionCustom) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);

                    Cell cell0 = row.createCell(0);
                    cell0.setCellValue(lstParams[i].idbanner);
                    //Nombre
                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(
                        lstParams[i].apellidopaterno + " " +
                        lstParams[i].apellidomaterno + " " +
                        lstParams[i].primernombre + " " +
                        lstParams[i].segundonombre
                    );
                    //Correo Electronico
                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue(lstParams[i].correoelectronico);
                    //CURP
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(lstParams[i].curp);
                    //Campus
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue(lstParams[i].campus);
                    //PROGRAMA
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(lstParams[i].licenciatura);
                    //Ingreso
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(lstParams[i].ingreso);
                    //Estado
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(lstParams[i].procedencia);
                    //PREPARATORIA
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue(lstParams[i].preparatoria);
                    //PROMEDIO
                    Cell cell9 = row.createCell(9);
                    cell9.setCellValue(lstParams[i].promediogeneral);
                    //ESTATUS
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue(lstParams[i].estatussolicitud);
                    //TIPO
                    //Cell cell11 = row.createCell(11);
                    //cell11.setCellValue(lstParams[i].getObjDetalleSolicitud() == null?"" : lstParams[i].getObjDetalleSolicitud().getTipoAlumno());

                    Cell cell13 = row.createCell(11);
                    cell13.setCellValue(lstParams[i].residensia);
                    Cell cell14 = row.createCell(12);
                    cell14.setCellValue(lstParams[i].tipoadmision);
                    Cell cell15 = row.createCell(13);
                    String estatus = (lstParams[i].campus.equals(lstParams[i].campussede))?"MISMO CAMPUS" : "TRANSFERENCIA";
                    cell15.setCellValue(estatus);

                    Cell cell11 = row.createCell(14);
                    if (lstParams[i].fechasolicitudenviada != null) {
                        fechaCreacion = dfSalida.parse(lstParams[i].fechasolicitudenviada.toString())
                        cell11.setCellValue(dformat.format(fechaCreacion));
                    } else {
                        cell11.setCellValue("");
                    }

                    //ULTIMA MODIFICACION
                    Cell cell12 = row.createCell(15);
                    if (lstParams[i].fechaultimamodificacion != null) {
                        fechaCreacion = dfSalida.parse(lstParams[i].fechaultimamodificacion.toString())
                        cell12.setCellValue(dformat.format(fechaCreacion));
                    } else {
                        cell12.setCellValue("");
                    }
					
					Cell cell16 = row.createCell(16);
                    cell16.setCellValue(lstParams[i].telefonocelular);
					Cell cell17 = row.createCell(17);
                    cell17.setCellValue(lstParams[i].telefono);
                    //cell12.setCellValue(solicitud.getUpdateDate());
                }
            } else if (type.equals("aspirantes_proceso_fechas")) {
                dataResult = selectAspirantesEnprocesoFechas(parameterP, parameterC, jsonData, context);

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
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
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
                Cell headerprbs = espacio.createCell(10);
                headerprbs.setCellValue("PRUEBAS Y HORAS DE EXAMEN");
                headerprbs.setCellStyle(style);
                Row headersRow = sheet.createRow(++rowCount);
                Cell header20 = headersRow.createCell(0);
                header20.setCellValue("ID BANNER");
                header20.setCellStyle(style);
                Cell header1 = headersRow.createCell(1);
                header1.setCellValue("NOMBRE");
                header1.setCellStyle(style);
                Cell header2 = headersRow.createCell(2);
                header2.setCellValue("EMAIL");
                header2.setCellStyle(style);
                Cell header3 = headersRow.createCell(3);
                header3.setCellValue("CURP");
                header3.setCellStyle(style);
                Cell header4 = headersRow.createCell(4);
                header4.setCellValue("CAMPUS");
                header4.setCellStyle(style);
                Cell header5 = headersRow.createCell(5);
                header5.setCellValue("PROGRAMA");
                header5.setCellStyle(style);
                Cell header6 = headersRow.createCell(6);
                header6.setCellValue("INGRESO");
                header6.setCellStyle(style);
                Cell header7 = headersRow.createCell(7);
                header7.setCellValue("PROCEDENCIA");
                header7.setCellStyle(style);
                Cell header8 = headersRow.createCell(8);
                header8.setCellValue("PREPARATORIA");
                header8.setCellStyle(style);
                Cell header9 = headersRow.createCell(9);
                header9.setCellValue("PROMEDIO");
                header9.setCellStyle(style);
                Cell header10 = headersRow.createCell(10);
                header10.setCellValue("EXAMEN DE APTITUDES Y CONOCIMIENTOS");
                header10.setCellStyle(style);
                Cell header17 = headersRow.createCell(11);
                header17.setCellValue("ASISTENCIA");
                header17.setCellStyle(style);
                Cell header11 = headersRow.createCell(12);
                header11.setCellValue("ENTREVISTA");
                header11.setCellStyle(style);
                Cell header18 = headersRow.createCell(13);
                header18.setCellValue("ASISTENCIA");
                header18.setCellStyle(style);
                Cell header12 = headersRow.createCell(14);
                header12.setCellValue("EXAMEN PSICOMÉTRICO");
                header12.setCellStyle(style);
                Cell header19 = headersRow.createCell(15);
                header19.setCellValue("ASISTENCIA");
                header19.setCellStyle(style);
                Cell header13 = headersRow.createCell(16);
                header13.setCellValue("ESTATUS");
                header13.setCellStyle(style);
                Cell header14 = headersRow.createCell(17);
                header14.setCellValue("FECHA SOLICITUD");
                header14.setCellStyle(style);
                Cell header15 = headersRow.createCell(18);
                header15.setCellValue("ÚLTIMA MODIFICACIÓN");
                header15.setCellStyle(style);
                Cell header16 = headersRow.createCell(19);
                header16.setCellValue("TIEMPO ÚLTIMA MODIFICACIÓN");
                header16.setCellStyle(style);
                for (int i = 0; i < lstParams.size(); ++i) {

                    //SolicitudAdmisionCustom  solicitud = (SolicitudAdmisionCustom) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    // ID BANNER
                    Cell cell20 = row.createCell(0);
                    cell20.setCellValue(lstParams[i].idbanner);
                    //Nombre
                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(
                        lstParams[i].apellidopaterno + " " +
                        lstParams[i].apellidomaterno + " " +
                        lstParams[i].primernombre + " " +
                        lstParams[i].segundonombre
                        // solicitud.getPrimerNombre() + " " +
                        // solicitud.getSegundoNombre() + " " +
                        // solicitud.getApellidoPaterno() + " " +
                        // solicitud.getApellidoMaterno()
                    );
                    //Correo Electronico
                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue(lstParams[i].correoelectronico);
                    //cell2.setCellValue(solicitud.getCorreoElectronico());
                    //CURP
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(lstParams[i].curp);
                    //cell3.setCellValue(solicitud.getCurp());
                    //Campus
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue(lstParams[i].campus);
                    //cell4.setCellValue(solicitud.getCatCampus().getDescripcion());
                    //PROGRAMA
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(lstParams[i].licenciatura);
                    //cell5.setCellValue(solicitud.getObjCatGestionEscolar().getNombre());
                    //Ingreso
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(lstParams[i].ingreso);
                    //cell6.setCellValue(solicitud.getCatPeriodo().getDescripcion());
                    //Estado
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(lstParams[i].procedencia);
                    //cell7.setCellValue(solicitud.getCatEstado().getDescripcion());
                    //PREPARATORIA
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue(lstParams[i].preparatoria);
                    //cell8.setCellValue(solicitud.getCatBachilleratos().getDescripcion());
                    //PROMEDIO
                    Cell cell9 = row.createCell(9);
                    cell9.setCellValue(lstParams[i].promediogeneral);
                    //cell9.setCellValue(solicitud.getPromedioGeneral());
                    //FECHAS
                    if (lstParams[i].fechasexamenes == null || lstParams[i].fechasexamenes == "" || lstParams[i].fechasexamenes == " ") {
                        Cell cell10 = row.createCell(10);
                        cell10.setCellValue("SIN PROGRAMAR");
                        Cell cell17 = row.createCell(11);
                        cell17.setCellValue((lstParams[i].cbcoincide == "t"?"Exento" : "No"));
                        Cell cell11 = row.createCell(12);
                        cell11.setCellValue("SIN PROGRAMAR");
                        Cell cell18 = row.createCell(13);
                        cell18.setCellValue("No");
                        Cell cell12 = row.createCell(14);
                        cell12.setCellValue("SIN PROGRAMAR");
                        Cell cell19 = row.createCell(15);
                        cell19.setCellValue("No");
                    } else {
                        String[] fechas = lstParams[i].fechasexamenes.split(",");

                        String fechas1 = "";
                        String fechas2 = "Entrevita";
                        String fechas3 = "";

                        String[] asistencias = lstParams[i].asistencia.split(",");

                        /*if(fechas.size() == 3) {
                             fechas1 = fechas[0];
                             fechas2 = fechas[1];
                             fechas3 = fechas[2];
                             
                        }else {*/

                        for (int j = 0; j < fechas.size(); ++j) {
                            if (fechas[j].contains("Examen de aptitudes y conocimientos")) {
                                fechas1 = fechas[j];
                            } else if (fechas[j].contains("Examen Psicométrico")) {
                                fechas3 = fechas[j];
                            } else {
                                fechas2 = fechas[j];
                            }
                        }
                        //}

                        String asistencia1 = "";
                        String asistencia2 = "";
                        String asistencia3 = "";
                        for (int j = 0; j < asistencias.size(); ++j) {

                            String[] asistencia = asistencias[j].split("-")
                            if (asistencia?.size() >= 2) {
                                if (asistencia[1].contains("Examen de aptitudes y conocimientos")) {
                                    asistencia1 = asistencia[0];
                                } else if (asistencia[1].contains("Examen Psicométrico")) {
                                    asistencia3 = asistencia[0];
                                } else {
                                    asistencia2 = asistencia[0];
                                }
                            }


                        }

                        Cell cell10 = row.createCell(10);
                        cell10.setCellValue(fechas1);
                        Cell cell17 = row.createCell(11);
                        cell17.setCellValue(lstParams[i].cbcoincide == "t"?"Exento" : (asistencia1.equals("0")?"No" : "Sí"));
                        Cell cell11 = row.createCell(12);
                        cell11.setCellValue(fechas2);
                        Cell cell18 = row.createCell(13);
                        cell18.setCellValue((asistencia2.equals("0")?"No" : "Sí"));
                        Cell cell12 = row.createCell(14);
                        cell12.setCellValue(fechas3);
                        Cell cell19 = row.createCell(15);
                        cell19.setCellValue((asistencia3.equals("0")?"No" : "Sí"));
                    }
                    errorlog += " | " + i + " estatussolicitud: " + lstParams[i].estatussolicitud + " | ";
                    Cell cell13 = row.createCell(16);
                    cell13.setCellValue(lstParams[i].estatussolicitud);

                    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    errorlog += " | " + i + " fechasolicitudenviada: " + lstParams[i].fechasolicitudenviada + " | ";
                    if (lstParams[i].fechasolicitudenviada != "null" && lstParams[i].fechasolicitudenviada != null && lstParams[i].fechasolicitudenviada != "" && lstParams[i].fechasolicitudenviada != " ") {

                        String sDate1 = lstParams[i].fechasolicitudenviada;
                        Date date1 = formatter1.parse(sDate1);
                        Cell cell14 = row.createCell(17);
                        cell14.setCellValue(f.format(date1));
                    }
                    errorlog += " | " + i + " fechaultimamodificacion: " + lstParams[i].fechaultimamodificacion + " | ";
                    if (lstParams[i].fechaultimamodificacion != "null" && lstParams[i].fechaultimamodificacion != null && lstParams[i].fechaultimamodificacion != "" && lstParams[i].fechaultimamodificacion != " ") {
                        String sDate2 = lstParams[i].fechaultimamodificacion;
                        Date date2 = formatter1.parse(sDate2);
                        Cell cell15 = row.createCell(18);
                        cell15.setCellValue(f.format(date2));
                    }
                    errorlog += " | " + i + " tiempoultimamodificacion: " + lstParams[i].tiempoultimamodificacion + " | ";
                    if (lstParams[i].tiempoultimamodificacion != "null" && lstParams[i].tiempoultimamodificacion != null && lstParams[i].tiempoultimamodificacion != "" && lstParams[i].tiempoultimamodificacion != " ") {
                        Cell cell16 = row.createCell(19);
                        cell16.setCellValue(lstParams[i].tiempoultimamodificacion);
                    }



                }
            }

            //-----------------------------------------------------------------------
            else if (type.equals("consulta_resultados")) {
                dataResult = new ResultadosAdmisionDAO().selectConsultaDeResultados(parameterP, parameterC, jsonData, context);

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
                cellTitle.setCellValue("CONSULTA DE RESULTADOS");
                Cell cellFecha = titleRow.createCell(4);
                cellFecha.setCellValue("Fecha:");
                cellFecha.setCellStyle(style);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String sDate = formatter.format(date);
                Cell cellFechaData = titleRow.createCell(5);
                cellFechaData.setCellValue(sDate);
                Row blank = sheet.createRow(++rowCount);
                Cell cellusuario = blank.createCell(4);
                cellusuario.setCellValue("Usuario:");
                cellusuario.setCellStyle(style);
                Cell cellusuarioData = blank.createCell(5);
                cellusuarioData.setCellValue(object.usuario);

                Row headersRow = sheet.createRow(++rowCount);
                Cell header1 = headersRow.createCell(0);
                header1.setCellValue("ID BANNER");
                header1.setCellStyle(style);
                Cell header2 = headersRow.createCell(1);
                header2.setCellValue("NOMBRE");
                header2.setCellStyle(style);
                Cell header3 = headersRow.createCell(2);
                header3.setCellValue("EMAIL");
                header3.setCellStyle(style);
                Cell header4 = headersRow.createCell(3);
                header4.setCellValue("CURP");
                header4.setCellStyle(style);
                Cell header5 = headersRow.createCell(4);
                header5.setCellValue("CAMPUS");
                header5.setCellStyle(style);
                Cell header6 = headersRow.createCell(5);
                header6.setCellValue("PROGRAMA");
                header6.setCellStyle(style);
                Cell header7 = headersRow.createCell(6);
                header7.setCellValue("PERÍODO");
                header7.setCellStyle(style);
                Cell header8 = headersRow.createCell(7);
                header8.setCellValue("PROCEDENCIA");
                header8.setCellStyle(style);
                Cell header9 = headersRow.createCell(8);
                header9.setCellValue("PREPARATORIA");
                header9.setCellStyle(style);
                Cell header10 = headersRow.createCell(9);
                header10.setCellValue("PROMEDIO");
                header10.setCellStyle(style);
                Cell header11 = headersRow.createCell(10);
                header11.setCellValue("RESIDENCIA");
                header11.setCellStyle(style);
                Cell header12 = headersRow.createCell(11);
                header12.setCellValue("TIPO DE ADMISIÓN");
                header12.setCellStyle(style);
                Cell header13 = headersRow.createCell(12);
                header13.setCellValue("TIPO DE ALUMNO");
                header13.setCellStyle(style);
                Cell header14 = headersRow.createCell(13);
                header14.setCellValue("RESULTADO DE ADMISIÓN");
                header14.setCellStyle(style);
                Cell header15 = headersRow.createCell(14);
                header15.setCellValue("FECHA DE ENVÍO");
                header15.setCellStyle(style);
                Cell header16 = headersRow.createCell(15);
                header16.setCellValue("EXAMEN DE APTITUDES Y CONOCIMIENTOS");
                header16.setCellStyle(style);
                Cell header17 = headersRow.createCell(16);
                header17.setCellValue("ENTREVISTA");
                header17.setCellStyle(style);
                Cell header18 = headersRow.createCell(17);
                header18.setCellValue("EXAMEN PSICOMÉTRICO");
                header18.setCellStyle(style);


                for (int i = 0; i < lstParams.size(); ++i) {

                    //SolicitudAdmisionCustom  solicitud = (SolicitudAdmisionCustom) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    //Nombre
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(lstParams[i].idbanner);
                    //Correo Electronico
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(
                        lstParams[i].apellidopaterno + " " +
                        lstParams[i].apellidomaterno + " " +
                        lstParams[i].primernombre + " " +
                        lstParams[i].segundonombre
                        //lstParams[i].primernombre+" "+lstParams[i].segundonombre+" "+lstParams[i].apellidopaterno+" "+lstParams[i].apellidomaterno
                    );
                    //cell2.setCellValue(solicitud.getCorreoElectronico());
                    //CURP
                    Cell cell3 = row.createCell(2);
                    cell3.setCellValue(lstParams[i].email);
                    //cell3.setCellValue(solicitud.getCurp());
                    //Campus
                    Cell cell4 = row.createCell(3);
                    cell4.setCellValue(lstParams[i].curp);
                    //cell4.setCellValue(solicitud.getCatCampus().getDescripcion());
                    //PROGRAMA
                    Cell cell5 = row.createCell(4);
                    cell5.setCellValue(lstParams[i].campus);
                    //cell5.setCellValue(solicitud.getObjCatGestionEscolar().getNombre());
                    //Ingreso
                    Cell cell6 = row.createCell(5);
                    cell6.setCellValue(lstParams[i].licenciatura);
                    //cell6.setCellValue(solicitud.getCatPeriodo().getDescripcion());
                    //Estado
                    Cell cell7 = row.createCell(6);
                    cell7.setCellValue(lstParams[i].ingreso);
                    //cell7.setCellValue(solicitud.getCatEstado().getDescripcion());
                    //PREPARATORIA
                    Cell cell8 = row.createCell(7);
                    cell8.setCellValue(lstParams[i].procedencia);
                    //cell8.setCellValue(solicitud.getCatBachilleratos().getDescripcion());
                    //PROMEDIO
                    Cell cell9 = row.createCell(8);
                    cell9.setCellValue(lstParams[i].preparatoria);
                    //cell9.setCellValue(solicitud.getPromedioGeneral());
                    //FECHAS
                    Cell cell10 = row.createCell(9);
                    cell10.setCellValue(lstParams[i].promediogeneral);

                    Cell cell11 = row.createCell(10);
                    cell11.setCellValue(lstParams[i].residensia);

                    Cell cell12 = row.createCell(11);
                    cell12.setCellValue(lstParams[i].tipoadmision);

                    Cell cell13 = row.createCell(12);
                    cell13.setCellValue(lstParams[i].tipodealumno);

                    Cell cell14 = row.createCell(13);
                    if (lstParams[i].carta == null || lstParams[i].carta == "null") {
                        cell14.setCellValue(lstParams[i].estatussolicitud);
                    } else {
                        cell14.setCellValue(lstParams[i].carta);
                    }


                    Cell cell15 = row.createCell(14);
                    cell15.setCellValue(lstParams[i].fechasolicitudenviadaformato);

                    if (lstParams[i].fechasexamenes == null || lstParams[i].fechasexamenes == "" || lstParams[i].fechasexamenes == " ") {
                        Cell cell16 = row.createCell(15);
                        cell16.setCellValue("SIN PROGRAMAR");
                        Cell cell17 = row.createCell(16);
                        cell17.setCellValue("SIN PROGRAMAR");
                        Cell cell18 = row.createCell(17);
                        cell18.setCellValue("SIN PROGRAMAR");
                    } else {
                        String[] fechas = lstParams[i].fechasexamenes.split(",");

                        String fechas1 = "";
                        String fechas2 = "";
                        String fechas3 = "";

                        if (fechas.size() == 3) {
                            fechas1 = fechas[0];
                            fechas2 = fechas[1];
                            fechas3 = fechas[2];

                        } else {

                            for (int j = 0; j < fechas.size(); ++j) {
                                if (fechas[j].contains("Examen de aptitudes y conocimientos")) {
                                    fechas2 = fechas[j];
                                } else if (fechas[j].contains("Entrevista")) {
                                    fechas1 = fechas[j];
                                } else {
                                    fechas3 = fechas[j];
                                }
                            }
                        }
                        Cell cell16 = row.createCell(15);
                        cell16.setCellValue(fechas2);
                        Cell cell17 = row.createCell(16);
                        cell17.setCellValue(fechas1);
                        Cell cell18 = row.createCell(17);
                        cell18.setCellValue(fechas3);
                    }



                }
            }
            //-----------------------------------------------------------------------
            else if (type.equals("usuarios_registrado")) {
                dataResult = new UsuariosDAO().getUsuariosRegistrados(parameterP, parameterC, jsonData, context);

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
                cellTitle.setCellValue("USUARIOS REGISTRADOS");
                Cell cellFecha = titleRow.createCell(4);
                cellFecha.setCellValue("Fecha:");
                cellFecha.setCellStyle(style);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
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
                Cell header0 = headersRow.createCell(0);
                header0.setCellValue("ID BANNER");
                header0.setCellStyle(style);
                Cell header1 = headersRow.createCell(1);
                header1.setCellValue("NOMBRE");
                header1.setCellStyle(style);
                Cell header2 = headersRow.createCell(2);
                header2.setCellValue("EMAIL");
                header2.setCellStyle(style);
                Cell header3 = headersRow.createCell(3);
                header3.setCellValue("CURP");
                header3.setCellStyle(style);
                Cell header4 = headersRow.createCell(4);
                header4.setCellValue("PROGRAMA");
                header4.setCellStyle(style);
                Cell header5 = headersRow.createCell(5);
                header5.setCellValue("PERÍODO");
                header5.setCellStyle(style);
                Cell header6 = headersRow.createCell(6);
                header6.setCellValue("CAMPUS");
                header6.setCellStyle(style);
                Cell header7 = headersRow.createCell(7);
                header7.setCellValue("PROCEDENCIA");
                header7.setCellStyle(style);
                Cell header8 = headersRow.createCell(8);
                header8.setCellValue("PREPARATORIA");
                header8.setCellStyle(style);
                Cell header9 = headersRow.createCell(9);
                header9.setCellValue("PROMEDIO");
                header9.setCellStyle(style);
                Cell header10 = headersRow.createCell(10);
                header10.setCellValue("ESTATUS");
                header10.setCellStyle(style);
                Cell header11 = headersRow.createCell(11);
                header11.setCellValue("TIPO");
                header11.setCellStyle(style);
				Cell header12 = headersRow.createCell(12);
                header12.setCellValue("TELÉFONO CELULAR");
                header12.setCellStyle(style);
				Cell header13 = headersRow.createCell(13);
                header13.setCellValue("TELÉFONO DE CASA");
                header13.setCellStyle(style);
                for (int i = 0; i < lstParams.size(); ++i) {
                    //SolicitudAdmisionCustom  solicitud = (SolicitudAdmisionCustom) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    Cell cell0 = row.createCell(0);
                    cell0.setCellValue(lstParams[i].idbanner);

                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(
                        lstParams[i].apellidopaterno + " " +
                        lstParams[i].apellidomaterno + " " +
                        lstParams[i].primernombre + " " +
                        lstParams[i].segundonombre
                    );

                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue(lstParams[i].correoelectronico);
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(lstParams[i].curp);
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue(lstParams[i].licenciatura);
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(lstParams[i].ingreso);
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(lstParams[i].campus);
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(lstParams[i].procedencia);
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue(lstParams[i].preparatoria);
                    Cell cell9 = row.createCell(9);
                    cell9.setCellValue(lstParams[i].promediogeneral);
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue(lstParams[i].estatussolicitud);
                    Cell cell11 = row.createCell(11);
                    cell11.setCellValue(lstParams[i].tipoalumno);
					Cell cell12 = row.createCell(12);
                    cell12.setCellValue(lstParams[i].telefonocelular);
					Cell cell13 = row.createCell(13);
                    cell13.setCellValue(lstParams[i].telefono);

                }
            }
            //-----------------------------------------------------------------------
            else if (type.equals("lista_roja") || type.equals("aspirantes_rechazados")) {
                //dataResult = getAspirantesByStatus(parameterP, parameterC, jsonData, context);
                dataResult = selectAspirantesEnproceso(parameterP, parameterC, jsonData, context);

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
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");

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
                header1.setCellValue("NÚMERO DE SOLICITUD");
                header1.setCellStyle(style);
                Cell header2 = headersRow.createCell(1);
                header2.setCellValue("NOMBRE");
                header2.setCellStyle(style);
                Cell header3 = headersRow.createCell(2);
                header3.setCellValue("EMAIL");
                header3.setCellStyle(style);
                Cell header4 = headersRow.createCell(3);
                header4.setCellValue("CURP");
                header4.setCellStyle(style);
                Cell header5 = headersRow.createCell(4);
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
                header9.setCellValue(type.equals("lista_roja")?"MOTIVO DE LISTA ROJA" : "MOTIVO DE RECHAZO");
                header9.setCellStyle(style);
                Cell header10 = headersRow.createCell(9);
                header10.setCellValue("FECHA ULTIMA MODIFICACIÓN");
                header10.setCellStyle(style);

                //FORMATO DE LAS FECHAS
                DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date fechaCreacion = new Date();
                DateFormat dformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                //SolicitudAdmisionCustom  solicitud = new SolicitudAdmisionCustom();

                for (int i = 0; i < lstParams.size(); ++i) {
                    //solicitud = new SolicitudAdmisionCustom();
                    //solicitud = (SolicitudAdmisionCustom)lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(lstParams[i].caseid);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(
                        lstParams[i].apellidopaterno + " " +
                        lstParams[i].apellidomaterno + " " +
                        lstParams[i].primernombre + " " +
                        lstParams[i].segundonombre
                    );
                    Cell cell3 = row.createCell(2);
                    cell3.setCellValue(lstParams[i].correoelectronico);
                    Cell cell4 = row.createCell(3);
                    cell4.setCellValue(lstParams[i].curp);
                    Cell cell5 = row.createCell(4);
                    cell5.setCellValue(lstParams[i].campus);
                    Cell cell6 = row.createCell(5);
                    cell6.setCellValue(lstParams[i].licenciatura);
                    Cell cell7 = row.createCell(6);
                    cell7.setCellValue(lstParams[i].ingreso);
                    Cell cell8 = row.createCell(7);
                    cell8.setCellValue(lstParams[i].promediogeneral);
                    Cell cell9 = row.createCell(8);
                    String motivo = type.equals("lista_roja")?lstParams[i].observacioneslistaroja : lstParams[i].observacionesrechazo;
                    cell9.setCellValue(motivo);
                    Cell cell10 = row.createCell(9);
                    if (lstParams[i].fechaultimamodificacion != null) {
                        fechaCreacion = dfSalida.parse(lstParams[i].fechaultimamodificacion.toString())
                        cell10.setCellValue(dformat.format(fechaCreacion));
                    } else {
                        cell10.setCellValue("");
                    }
                    //cell10.setCellValue(motivo);
                }
            } else if (type.equals("solicitudes_en_progreso")) {
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
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

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
                SolicitudAdmisionCustom solicitud = new SolicitudAdmisionCustom();

                for (int i = 0; i < lstParams.size(); ++i) {
                    solicitud = new SolicitudAdmisionCustom();
                    solicitud = (SolicitudAdmisionCustom) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(solicitud.getPersistenceId().toString());
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(

                        solicitud.getApellidoPaterno() + " " +
                        solicitud.getApellidoMaterno() + " " +
                        solicitud.getPrimerNombre() + " " +
                        solicitud.getSegundoNombre()
                    );
                    Cell cell3 = row.createCell(2);
                    cell3.setCellValue(solicitud.getCorreoElectronico());
                    Cell cell4 = row.createCell(3);
                    cell4.setCellValue("");
                }
            }


            for (int i = 0; i <= (20); ++i) {
                sheet.autoSizeColumn(i);
            }
            FileOutputStream outputStream = new FileOutputStream("Report.xls");
            workbook.write(outputStream);

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("Report.xls"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);
            

        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
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
            List < Object > lstParams;
            String type = object.type;

            def documento = "Report.pdf"
            DocumentItext document = new DocumentItext();
            document.setPageSize(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(documento));
            float fontSize = 8.5 f;
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, fontSize, BaseColor.BLACK);
            String phraseToInput = "";
            if (type.equals("nuevas_solicitudes")) {
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
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String sDate = formatter.format(date);

                Chunk glue = new Chunk(new VerticalPositionMark());
                Paragraph p = new Paragraph("FECHA: " + sDate);
                p.add(new Chunk(glue));
                p.add("USUARIO: " + object.usuario);
                document.add(p);
                document.add(new Paragraph(" "));

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

                SolicitudAdmisionCustom solicitud = new SolicitudAdmisionCustom();
                phraseToInput = new String();
                for (int i = 0; i < lstParams.size(); ++i) {
                    phraseToInput = new String();
                    solicitud = new SolicitudAdmisionCustom();
                    solicitud = (SolicitudAdmisionCustom) lstParams.get(i);
                    table.addCell(new Phrase(solicitud.getPersistenceId().toString(), normalFont));
                    table.addCell(new Phrase(
                        solicitud.getPrimerNombre() + " " +
                        solicitud.getSegundoNombre() + " " +
                        solicitud.getApellidoPaterno() + " " +
                        solicitud.getApellidoMaterno()), normalFont);
                    phraseToInput = solicitud.getCorreoElectronico();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getTelefono();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getCatBachilleratos().getDescripcion();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getCatEstado().getDescripcion();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getPromedioGeneral();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getUpdateDate();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
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
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String sDate = formatter.format(date);

                Chunk glue = new Chunk(new VerticalPositionMark());
                Paragraph p = new Paragraph("FECHA: " + sDate);
                p.add(new Chunk(glue));
                p.add("USUARIO: " + object.usuario);
                document.add(p);
                document.add(new Paragraph(" "));


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

                SolicitudAdmisionCustom solicitud = new SolicitudAdmisionCustom();

                for (int i = 0; i < lstParams.size(); ++i) {
                    solicitud = new SolicitudAdmisionCustom();
                    solicitud = (SolicitudAdmisionCustom) lstParams.get(i);
                    phraseToInput = new String();

                    phraseToInput =
                        solicitud.getPrimerNombre() + " " +
                        solicitud.getSegundoNombre() + " " +
                        solicitud.getApellidoPaterno() + " " +
                        solicitud.getApellidoMaterno();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getCorreoElectronico();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getCurp();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getCatCampus().getDescripcion();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getObjCatGestionEscolar().getDescripcion();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getCatPeriodo().getDescripcion();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getCatEstado().getDescripcion();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getCatBachilleratos().getDescripcion()
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getPromedioGeneral();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getEstatusSolicitud();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getObjDetalleSolicitud() == null?"" : solicitud.getObjDetalleSolicitud().getTipoAlumno();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getUpdateDate();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
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
                String title1 = "REPORTE DE " + (type.equals("lista_roja")?"LISTA ROJA" : "ASPIRANTES RECHAZADOS");
                Paragraph par = new Paragraph(title1);
                par.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(par);
                document.add(new Paragraph(" "));

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String sDate = formatter.format(date);

                Chunk glue = new Chunk(new VerticalPositionMark());
                Paragraph p = new Paragraph("FECHA: " + sDate);
                p.add(new Chunk(glue));
                p.add("USUARIO: " + object.usuario);
                document.add(p);
                document.add(new Paragraph(" "));


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
                header.setPhrase(new Phrase(type.equals("lista_roja")?"MOTIVO DE LISTA ROJA" : "MOTIVO DE RECHAZO", normalFont));
                table.addCell(header);

                SolicitudAdmisionCustom solicitud = new SolicitudAdmisionCustom();

                for (int i = 0; i < lstParams.size(); ++i) {
                    solicitud = new SolicitudAdmisionCustom();
                    solicitud = (SolicitudAdmisionCustom) lstParams.get(i);
                    phraseToInput = new String();
                    table.addCell(new Phrase(solicitud.getPersistenceId().toString(), normalFont));
                    table.addCell(new Phrase(
                        solicitud.getPrimerNombre() + " " +
                        solicitud.getSegundoNombre() + " " +
                        solicitud.getApellidoPaterno() + " " +
                        solicitud.getApellidoMaterno(), normalFont));
                    phraseToInput = solicitud.getCorreoElectronico();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getTelefono();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getCatCampus().getDescripcion();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getObjCatGestionEscolar().getDescripcion();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getCatPeriodo().getDescripcion();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getPromedioGeneral();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput));
                    phraseToInput = type.equals("lista_roja")?solicitud.getObjDetalleSolicitud().getObservacionesListaRoja() : solicitud.getObjDetalleSolicitud().getObservacionesRechazo();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                }

                document.add(table);
                document.close();
            } else if (type.equals("aspirantes_en_progreso")) {
                dataResult = getAspirantesByStatusTemprano(parameterP, parameterC, jsonData, context);

                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos");
                }

                String title = object.estatusSolicitud;

                document.open();
                String title1 = "REPORTE DE SOLICITUDES EN PROGRESO";
                Paragraph par = new Paragraph(title1);
                par.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(par);
                document.add(new Paragraph(" "));

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String sDate = formatter.format(date);

                Chunk glue = new Chunk(new VerticalPositionMark());
                Paragraph p = new Paragraph("FECHA: " + sDate);
                p.add(new Chunk(glue));
                p.add("USUARIO: " + object.usuario);
                document.add(p);
                document.add(new Paragraph(" "));

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

                SolicitudAdmisionCustom solicitud = new SolicitudAdmisionCustom();

                for (int i = 0; i < lstParams.size(); ++i) {
                    solicitud = new SolicitudAdmisionCustom();
                    solicitud = (SolicitudAdmisionCustom) lstParams.get(i);
                    phraseToInput = new String();
                    table.addCell(new Phrase(solicitud.getPersistenceId().toString(), normalFont));
                    table.addCell(new Phrase(
                        solicitud.getPrimerNombre() + " " +
                        solicitud.getSegundoNombre() + " " +
                        solicitud.getApellidoPaterno() + " " +
                        solicitud.getApellidoMaterno(), normalFont));
                    phraseToInput = solicitud.getCorreoElectronico();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getCatCampus().getDescripcion();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getObjCatGestionEscolar().getDescripcion();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                    phraseToInput = solicitud.getCatPeriodo().getDescripcion();
                    table.addCell(new Phrase(phraseToInput == null?"" : phraseToInput, normalFont));
                }

                document.add(table);
                document.close();
            }

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("Report.pdf"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);

        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error "[ERROR] " + e.getMessage();
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
            List < Object > lstParams;
            String type = object.type;
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(type);
            CellStyle style = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);

            if (type.equals("gestion_escolar")) {
                dataResult = getGestionEscolar(parameterP, parameterC, jsonData, context);

                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos");
                }
                def campus = (object.campus == "CAMPUS-MNORTE"?"Anáhuac México Norte" : object.campus == "CAMPUS-MSUR"?"Anáhuac México Sur" : object.campus == "CAMPUS-MAYAB"?"Anáhuac Merida" : object.campus == "CAMPUS-XALAPA"?"Anáhuac Xalapa" : object.campus == "CAMPUS-CORDOBA"?"Anáhuac Cordoba" : object.campus == "CAMPUS-CANCUN"?"Anáhuac Cancún" : object.campus == "CAMPUS-OAXACA"?"Anáhuac Oaxaca" : object.campus == "CAMPUS-PUEBLA"?"Anáhuac Puebla" : object.campus == "CAMPUS-QUERETARO"?"Anáhuac Querétaro" : "Juan Pablo II");
                Row titleRow = sheet.createRow(++rowCount);
                Cell cellReporte = titleRow.createCell(1);
                cellReporte.setCellValue("Reporte:");
                cellReporte.setCellStyle(style);
                Cell cellTitle = titleRow.createCell(2);
                cellTitle.setCellValue("LISTADO DE LICENCIATURAS DEL CAMPUS \"" + campus + "\"");

                Cell cellFecha = titleRow.createCell(4);
                cellFecha.setCellValue("Fecha:");
                cellFecha.setCellStyle(style);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				
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
                Cell header0 = headersRow.createCell(0);
                header0.setCellValue("CLAVE");
                header0.setCellStyle(style);

                Cell header1 = headersRow.createCell(1);
                header1.setCellValue("NOMBRE LICENCIATURA");
                header1.setCellStyle(style);
                Cell header2 = headersRow.createCell(2);
                header2.setCellValue("LIGA");
                header2.setCellStyle(style);
                Cell header3 = headersRow.createCell(3);
                header3.setCellValue("DESCRIPCION DE LA CARRERA");
                header3.setCellStyle(style);
                Cell header4 = headersRow.createCell(4);
                header4.setCellValue("INSCRIPCIÓN DE ENERO");
                header4.setCellStyle(style);
                Cell header5 = headersRow.createCell(5);
                header5.setCellValue("INSCRIPCIÓN DE MAYO");
                header5.setCellStyle(style);
                Cell header6 = headersRow.createCell(6);
                header6.setCellValue("INSCRIPCIÓN DE AGOSTO");
                header6.setCellStyle(style);

                Cell header7 = headersRow.createCell(7);
                header7.setCellValue("INSCRIPCIÓN DE SEPTIEMBRE");
                header7.setCellStyle(style);
                Cell header8 = headersRow.createCell(8);
                header8.setCellValue("PROG PARCIAL");
                header8.setCellStyle(style);
                Cell header9 = headersRow.createCell(9);
                header9.setCellValue("TIPO LICENCIATURA");
                header9.setCellStyle(style);

                Cell header10 = headersRow.createCell(10);
                header10.setCellValue("TIPO CENTRO ESTUDIO");
                header10.setCellStyle(style);


                headersRow.setRowStyle(style);

                com.anahuac.catalogos.CatGestionEscolar Escolar = new com.anahuac.catalogos.CatGestionEscolar();

                for (int i = 0; i < lstParams.size(); ++i) {
                    Escolar = new com.anahuac.catalogos.CatGestionEscolar();
                    Escolar = (com.anahuac.catalogos.CatGestionEscolar) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    Cell cell0 = row.createCell(0);
                    cell0.setCellValue(Escolar.getClave().toString());
                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(Escolar.getNombre().toString());
                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue(Escolar.getEnlace().toString());
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(Escolar.getDescripcion());
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue("\$" + Escolar.getInscripcionenero());
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue("\$" + Escolar.getInscripcionMayo());

                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue("\$" + Escolar.getInscripcionagosto());

                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue("\$" + Escolar.getInscripcionSeptiembre());
                    Cell cell8 = row.createCell(8);
                    String isProgramaParcial = Escolar.isProgramaparcial()?"Sí" : "No";
                    cell8.setCellValue(isProgramaParcial);
                    Cell cell9 = row.createCell(9);
                    cell9.setCellValue(Escolar.getTipoLicenciatura().toString());
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue(Escolar.getTipoCentroEstudio().toString());
                }
            } else if (type.equals("bachilleratos")) {
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
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                /*Date date = new Date();
                TimeZone timeZone = TimeZone.getTimeZone("UTC-6");
                formatter.setTimeZone(timeZone);
                String sDate = formatter.format(date);*/

                String sDate = formatter.format(date);
                Cell cellFechaData = titleRow.createCell(5);
                cellFechaData.setCellValue(sDate);

                Row blank = sheet.createRow(++rowCount);
                object
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
                Cell header5 = headersRow.createCell(4);
                header5.setCellValue("PERTENECE A LA RED");
                header5.setCellStyle(style);

                headersRow.setRowStyle(style);
                com.anahuac.catalogos.CatBachilleratos bachillerato = new com.anahuac.catalogos.CatBachilleratos();

                for (int i = 0; i < lstParams.size(); ++i) {
                    bachillerato = new com.anahuac.catalogos.CatBachilleratos();
                    bachillerato = (com.anahuac.catalogos.CatBachilleratos) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(bachillerato.getDescripcion().toString());
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(bachillerato.getPais());
                    Cell cell3 = row.createCell(2);
                    cell3.setCellValue(bachillerato.getEstado());
                    Cell cell4 = row.createCell(3);
                    cell4.setCellValue(bachillerato.getCiudad());
                    Cell cell5 = row.createCell(4);
                    def red = bachillerato.isPerteneceRed()?'Sí' : "No";
                    cell5.setCellValue(red);
                }
            } else if (type.equals("campus")) {
                dataResult = new CatalogosDAO().getCatCampus(jsonData, context);

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
                cellTitle.setCellValue("LISTADO DE CAMPUS");
                Cell cellFecha = titleRow.createCell(4);
                cellFecha.setCellValue("Fecha:");
                cellFecha.setCellStyle(style);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                /*Date date = new Date();
                TimeZone timeZone = TimeZone.getTimeZone("UTC-6");
                formatter.setTimeZone(timeZone);
                String sDate = formatter.format(date);*/

                String sDate = formatter.format(date);
                Cell cellFechaData = titleRow.createCell(5);
                cellFechaData.setCellValue(sDate);

                Row blank = sheet.createRow(++rowCount);
                object
                Cell cellusuario = blank.createCell(4);
                cellusuario.setCellValue("Usuario:");
                cellusuario.setCellStyle(style);
                Cell cellusuarioData = blank.createCell(5);
                cellusuarioData.setCellValue(object.usuario);

                Row headersRow = sheet.createRow(++rowCount);

                Cell header1 = headersRow.createCell(0);
                header1.setCellValue("CLAVE");
                header1.setCellStyle(style);
                Cell header2 = headersRow.createCell(1);
                header2.setCellValue("DESCRIPCION");
                header2.setCellStyle(style);
                Cell header3 = headersRow.createCell(2);
                header3.setCellValue("PAIS");
                header3.setCellStyle(style);
                Cell header4 = headersRow.createCell(3);
                header4.setCellValue("URL DE LA IMAGEN");
                header4.setCellStyle(style);
                Cell header5 = headersRow.createCell(4);
                header5.setCellValue("CORREO ELECTRONICO");
                header5.setCellStyle(style);


                headersRow.setRowStyle(style);
                CatCampusCustomFiltro campus = new CatCampusCustomFiltro();

                for (int i = 0; i < lstParams.size(); ++i) {
                    campus = new CatCampusCustomFiltro();
                    campus = (CatCampusCustomFiltro) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(campus.getClave().toString());
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(campus.getDescripcion().toString());
                    Cell cell3 = row.createCell(2);
                    cell3.setCellValue(campus.getPais().getDescripcion().toString());
                    Cell cell4 = row.createCell(3);
                    cell4.setCellValue(campus.getUrlImagen().toString());
                    Cell cell5 = row.createCell(4);
                    cell5.setCellValue(campus.getEmail().toString());
                }
            } else if (type.equals("periodo")) {
                dataResult = new CatalogosDAO().getCatPeriodo(jsonData, context);

                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos");
                }
                //def campus= (object.campus == "CAMPUS-MNORTE"?"Anáhuac México Norte":object.campus == "CAMPUS-MSUR"?"Anáhuac México Sur":object.campus == "CAMPUS-MAYAB"?"Anáhuac Merida":object.campus =="CAMPUS-XALAPA"?"Anáhuac Xalapa":object.campus =="CAMPUS-CORDOBA"?"Anáhuac Cordoba":object.campus =="CAMPUS-CANCUN"?"Anáhuac Cancún":object.campus =="CAMPUS-OAXACA"?"Anáhuac Oaxaca":object.campus =="CAMPUS-PUEBLA"?"Anáhuac Puebla":object.campus =="CAMPUS-QUERETARO"?"Anáhuac Querétaro":"Juan Pablo II");
                Row titleRow = sheet.createRow(++rowCount);
                Cell cellReporte = titleRow.createCell(1);
                cellReporte.setCellValue("Reporte:");
                cellReporte.setCellStyle(style);
                Cell cellTitle = titleRow.createCell(2);
                cellTitle.setCellValue("LISTADO DE PERIODOS");

                Cell cellFecha = titleRow.createCell(4);
                cellFecha.setCellValue("Fecha:");
                cellFecha.setCellStyle(style);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

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
                cellusuarioData.setCellValue(object.usuarioNombre);

                Row espacio = sheet.createRow(++rowCount);

                Row headersRow = sheet.createRow(++rowCount);
                Cell header0 = headersRow.createCell(0);
                header0.setCellValue("ID");
                header0.setCellStyle(style);
                Cell header1 = headersRow.createCell(1);
                header1.setCellValue("CLAVE");
                header1.setCellStyle(style);
                Cell header2 = headersRow.createCell(2);
                header2.setCellValue("DESCRIPCIÓN");
                header2.setCellStyle(style);
                Cell header3 = headersRow.createCell(3);
                header3.setCellValue("CUATRIMESTRAL");
                header3.setCellStyle(style);
                Cell header4 = headersRow.createCell(4);
                header4.setCellValue("SEMESTRAL");
                header4.setCellStyle(style);
                Cell header5 = headersRow.createCell(5);
                header5.setCellValue("ANUAL");
                header5.setCellStyle(style);
                Cell header6 = headersRow.createCell(6);
                header6.setCellValue("FECHA INICIO");
                header6.setCellStyle(style);

                Cell header7 = headersRow.createCell(7);
                header7.setCellValue("FECHA FIN");
                header7.setCellStyle(style);
                Cell header8 = headersRow.createCell(8);
                header8.setCellValue("ACTIVO");
                header8.setCellStyle(style);
                Cell header9 = headersRow.createCell(9);
                header9.setCellValue("USUARIO BANNER");
                header9.setCellStyle(style);
                Cell header10 = headersRow.createCell(10);
                header10.setCellValue("FECHA CREACIÓN");

                header10.setCellStyle(style);

                headersRow.setRowStyle(style);

                CatPeriodoCustom Periodo = new CatPeriodoCustom();
                DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date fecha = new Date();
                DateFormat dformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                for (int i = 0; i < lstParams.size(); ++i) {
                    Periodo = new CatPeriodoCustom();
                    Periodo = (CatPeriodoCustom) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    Cell cell0 = row.createCell(0);
                    cell0.setCellValue(Periodo.getId().toString());
                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(Periodo.getClave().toString());
                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue(Periodo.getDescripcion().toString());
                    Cell cell3 = row.createCell(3);
                    String isCautrimestral = Periodo.getIsCuatrimestral()?"Sí" : "No";
                    cell3.setCellValue(isCautrimestral);
                    Cell cell4 = row.createCell(4);
                    String isSemestral = Periodo.getIsSemestral()?"Sí" : "No";
                    cell4.setCellValue(isSemestral);
                    Cell cell5 = row.createCell(5);
                    String isAnual = Periodo.getIsAnual()?"Sí" : "No";
                    cell5.setCellValue(isAnual);


                    Cell cell6 = row.createCell(6);
                    //String[] fecha = Periodo.getFechaInicio().toString().split("T");
                    fecha = dfSalida.parse(Periodo.getFechaInicio().toString())
                    cell6.setCellValue(dformat.format(fecha));

                    Cell cell7 = row.createCell(7);
                    //fecha = Periodo.getFechaFin().toString().split("T");
                    fecha = dfSalida.parse(Periodo.getFechaFin().toString())
                    cell7.setCellValue(dformat.format(fecha));

                    Cell cell8 = row.createCell(8);
                    String Activo = Periodo.getActivo()?"Sí" : "No";
                    cell8.setCellValue(Activo);

                    Cell cell9 = row.createCell(9);
                    cell9.setCellValue(Periodo.getUsuarioBanner().toString());

                    Cell cell10 = row.createCell(10);
                    //fecha = Periodo.getFechaCreacion().toString().split("T");
                    fecha = dfSalida.parse(Periodo.getFechaCreacion().toString())
                    cell10.setCellValue(dformat.format(fecha));
                }
            }

            for (int i = 0; i <= rowCount + 3; ++i) {
                sheet.autoSizeColumn(i);
            }
            FileOutputStream outputStream = new FileOutputStream("ReportCatalogo.xls");
            workbook.write(outputStream);

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("ReportCatalogo.xls"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);

        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
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
            List < Object > lstParams;
            String type = object.type;

            def documento = "ReportCatalogos.pdf"
            DocumentItext document = new DocumentItext();
            document.setPageSize(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(documento));
            float fontSize = 8.5 f;
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, fontSize, BaseColor.BLACK);
            String phraseToInput = "";
            if (type.equals("gestion_escolar")) {
                dataResult = getGestionEscolar(parameterP, parameterC, jsonData, context);

                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos");
                }
                def campus = (object.campus == "CAMPUS-MNORTE"?"Anáhuac México Norte" : object.campus == "CAMPUS-MSUR"?"Anáhuac México Sur" : object.campus == "CAMPUS-MAYAB"?"Anáhuac Merida" : object.campus == "CAMPUS-XALAPA"?"Anáhuac Xalapa" : object.campus == "CAMPUS-CORDOBA"?"Anáhuac Cordoba" : object.campus == "CAMPUS-CANCUN"?"Anáhuac Cancún" : object.campus == "CAMPUS-OAXACA"?"Anáhuac Oaxaca" : object.campus == "CAMPUS-PUEBLA"?"Anáhuac Puebla" : object.campus == "CAMPUS-QUERETARO"?"Anáhuac Querétaro" : "Juan Pablo II");
                document.open();
                Paragraph preface = new Paragraph("LISTADO DE LICENCIATURAS DEL CAMPUS \"" + campus + "\"");
                preface.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(preface);
                document.add(new Paragraph(" "));


                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String sDate = formatter.format(date);

                Chunk glue = new Chunk(new VerticalPositionMark());
                Paragraph p = new Paragraph("FECHA: " + sDate);
                p.add(new Chunk(glue));
                p.add("USUARIO: " + object.usuario);
                document.add(p);
                document.add(new Paragraph(" "));

                PdfPTable table = new PdfPTable(7);
                table.setWidthPercentage(100f);
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);


                header.setPhrase(new Phrase("CLAVE", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("NOMBRE lICENCIATURA", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("LIGA", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("DESCRIPCION DE LA CARRERA", normalFont))
                table.addCell(header);
                header.setPhrase(new Phrase("INSCRIPCION DE ENERO", normalFont))
                table.addCell(header);
                header.setPhrase(new Phrase("INSCRIPCION DE AGOSTO", normalFont))
                table.addCell(header);
                header.setPhrase(new Phrase("PERIODOS", normalFont))
                table.addCell(header);

                com.anahuac.catalogos.CatGestionEscolar G_Escolar = new com.anahuac.catalogos.CatGestionEscolar();

                for (int i = 0; i < lstParams.size(); ++i) {
                    G_Escolar = new com.anahuac.catalogos.CatGestionEscolar();
                    G_Escolar = (com.anahuac.catalogos.CatGestionEscolar) lstParams.get(i);
                    table.addCell(new Phrase(G_Escolar.getClave(), normalFont));
                    table.addCell(new Phrase(G_Escolar.getNombre(), normalFont));
                    table.addCell(new Phrase(G_Escolar.getEnlace(), normalFont));
                    table.addCell(new Phrase(G_Escolar.getDescripcion(), normalFont));
                    table.addCell(new Phrase("\$" + G_Escolar.getInscripcionenero(), normalFont));
                    table.addCell(new Phrase("\$" + G_Escolar.getInscripcionagosto(), normalFont));
                    com.anahuac.catalogos.CatPeriodo lstP = new com.anahuac.catalogos.CatPeriodo();
                    def periodos = "";
                    for (int f = 0; f < G_Escolar.getPeriodoDisponible().size(); f++) {
                        lstP = new com.anahuac.catalogos.CatPeriodo();
                        lstP = (com.anahuac.catalogos.CatPeriodo) G_Escolar.getPeriodoDisponible().get(f);
                        periodos += lstP.getDescripcion() + ",";
                    }
                    table.addCell(new Phrase(periodos, normalFont));

                }

                document.add(table);
                document.close();
            } else if (type.equals("bachilleratos")) {
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
                document.add(new Paragraph(" "));

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String sDate = formatter.format(date);


                Chunk glue = new Chunk(new VerticalPositionMark());
                Paragraph p = new Paragraph("FECHA: " + sDate);
                p.add(new Chunk(glue));
                p.add("USUARIO: " + object.usuario);
                document.add(p);


                document.add(new Paragraph(" "))

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100f);

                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);

                header.setPhrase(new Phrase("DESCRIPCION", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("PAIS", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("ESTADO", normalFont))
                table.addCell(header);
                header.setPhrase(new Phrase("CIUDAD", normalFont))
                table.addCell(header);
                header.setPhrase(new Phrase("PERTENECE A LA RED", normalFont))
                table.addCell(header);

                com.anahuac.catalogos.CatBachilleratos bachillerato = new com.anahuac.catalogos.CatBachilleratos();

                for (int i = 0; i < lstParams.size(); ++i) {
                    bachillerato = new com.anahuac.catalogos.CatBachilleratos();
                    bachillerato = (com.anahuac.catalogos.CatBachilleratos) lstParams.get(i);
                    table.addCell(new Phrase(bachillerato.getDescripcion(), normalFont));
                    table.addCell(new Phrase(bachillerato.getPais(), normalFont));
                    table.addCell(new Phrase(bachillerato.getEstado(), normalFont));
                    table.addCell(new Phrase(bachillerato.getCiudad(), normalFont));
                    def red = bachillerato.isPerteneceRed()?'Sí' : "No";
                    table.addCell(new Phrase(red, normalFont));

                }

                document.add(table);
                document.close();
            } else if (type.equals("campus")) {
                dataResult = new CatalogosDAO().getCatCampus(jsonData, context);

                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos");
                }
                document.open();
                Paragraph preface = new Paragraph("LISTADO DE CAMPUS");
                preface.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(preface);
                document.add(new Paragraph(" "));

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String sDate = formatter.format(date);


                Chunk glue = new Chunk(new VerticalPositionMark());
                Paragraph p = new Paragraph("FECHA: " + sDate);
                p.add(new Chunk(glue));
                p.add("USUARIO: " + object.usuario);
                document.add(p);


                document.add(new Paragraph(" "))

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100f);

                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);

                header.setPhrase(new Phrase("CLAVE", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("DESCRIPCION", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("PAIS", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("URL DE LA IMAGEN", normalFont))
                table.addCell(header);
                header.setPhrase(new Phrase("CORREO ELECTRONICO", normalFont))
                table.addCell(header);

                CatCampusCustomFiltro CAMPUS = new CatCampusCustomFiltro();

                for (int i = 0; i < lstParams.size(); ++i) {
                    CAMPUS = new CatCampusCustomFiltro();
                    CAMPUS = (CatCampusCustomFiltro) lstParams.get(i);
                    table.addCell(new Phrase(CAMPUS.getClave(), normalFont));
                    table.addCell(new Phrase(CAMPUS.getDescripcion(), normalFont));
                    table.addCell(new Phrase(CAMPUS.getPais().getDescripcion(), normalFont));
                    table.addCell(new Phrase(CAMPUS.getUrlImagen(), normalFont));
                    table.addCell(new Phrase(CAMPUS.getEmail(), normalFont));

                }

                document.add(table);
                document.close();

            }

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("ReportCatalogos.pdf"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);
        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }

    public Result getGestionEscolar(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();

        List < CatGestionEscolar > lstResultado = new ArrayList < CatGestionEscolar > ();


        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;

        Integer start = 0;
        Integer end = 99999;
        Integer inicioContador = 0;
        Integer finContador = 0;

        List < com.anahuac.catalogos.CatGestionEscolar > lstGestionEscolar = new ArrayList < com.anahuac.catalogos.CatGestionEscolar > ();

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
            LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setError_info(strError);
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }
        return resultado
    }

    public Result getBachilleratos(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;

        Integer start = 0;
        Integer end = 99999;
        Integer inicioContador = 0;
        Integer finContador = 0;

        List < com.anahuac.catalogos.CatBachilleratos > lstBachilleratos = new ArrayList < com.anahuac.catalogos.CatBachilleratos > ();

        String strError = "";

        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            def objBachilleratosDAO = context.apiClient.getDAO(CatBachilleratosDAO.class);
            /*if(object.opcion == 2) {
                lstBachilleratos = objBachilleratosDAO.getCatBachilleratosByEstado(object.estado)
            }else if(object.opcion == 3) {
                lstBachilleratos = objBachilleratosDAO.getCatBachilleratosByPais(object.pais)
            }else {
                
            }*/
            lstBachilleratos = objBachilleratosDAO.getCatBachilleratos(0, 9999);

            resultado.setError_info(strError);
            resultado.setData(lstBachilleratos);
            resultado.setSuccess(true);
        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setError_info(strError);
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }
        return resultado
    }

    public Result getExcelFileCatalogosAD(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            Result dataResult = new Result();
            int rowCount = 0;
            List < Object > lstParams;
            String type = object.type;
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(type);
            CellStyle style = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);

            if (type.equals("materias")) {
                dataResult = getMaterias(parameterP, parameterC, jsonData, context);

                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos de materias");
                }
                Row titleRow = sheet.createRow(++rowCount);
                Cell cellReporte = titleRow.createCell(1);
                cellReporte.setCellValue("Reporte:");
                cellReporte.setCellStyle(style);
                Cell cellTitle = titleRow.createCell(2);
                cellTitle.setCellValue("LISTADO DE MATERIAS");
                Cell cellFecha = titleRow.createCell(4);
                cellFecha.setCellValue("Fecha:");
                cellFecha.setCellStyle(style);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                /*Date date = new Date();
                TimeZone timeZone = TimeZone.getTimeZone("UTC-6");
                formatter.setTimeZone(timeZone);
                String sDate = formatter.format(date);*/

                String sDate = formatter.format(date);
                Cell cellFechaData = titleRow.createCell(5);
                cellFechaData.setCellValue(sDate);

                Row blank = sheet.createRow(++rowCount);
                object
                Cell cellusuario = blank.createCell(4);
                cellusuario.setCellValue("Usuario:");
                cellusuario.setCellStyle(style);
                Cell cellusuarioData = blank.createCell(5);
                cellusuarioData.setCellValue(object.usuario);

                Row headersRow = sheet.createRow(++rowCount);

                Cell header1 = headersRow.createCell(0);
                header1.setCellValue("CLAVE");
                header1.setCellStyle(style);
                Cell header2 = headersRow.createCell(1);
                header2.setCellValue("DESCIPCION");
                header2.setCellStyle(style);
                headersRow.setRowStyle(style);
                com.anahuac.catalogos.CatMaterias materias = new com.anahuac.catalogos.CatMaterias();

                for (int i = 0; i < lstParams.size(); ++i) {
                    materias = new com.anahuac.catalogos.CatMaterias();
                    materias = (com.anahuac.catalogos.CatMaterias) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(materias.getClave().toString());
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(materias.getDescripcion().toString());
                }
            }
            if (type.equals("tipoLectura")) {
                dataResult = getMaterias(parameterP, parameterC, jsonData, context);

                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos tipo lectura");
                }
                Row titleRow = sheet.createRow(++rowCount);
                Cell cellReporte = titleRow.createCell(1);
                cellReporte.setCellValue("Reporte:");
                cellReporte.setCellStyle(style);
                Cell cellTitle = titleRow.createCell(2);
                cellTitle.setCellValue("LISTADO DE TIPO LECTURA");
                Cell cellFecha = titleRow.createCell(4);
                cellFecha.setCellValue("Fecha:");
                cellFecha.setCellStyle(style);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                /*Date date = new Date();
                TimeZone timeZone = TimeZone.getTimeZone("UTC-6");
                formatter.setTimeZone(timeZone);
                String sDate = formatter.format(date);*/

                String sDate = formatter.format(date);
                Cell cellFechaData = titleRow.createCell(5);
                cellFechaData.setCellValue(sDate);
                Row blank = sheet.createRow(++rowCount);
                object
                Cell cellusuario = blank.createCell(4);
                cellusuario.setCellValue("Usuario:");
                cellusuario.setCellStyle(style);
                Cell cellusuarioData = blank.createCell(5);
                cellusuarioData.setCellValue(object.usuario);

                Row headersRow = sheet.createRow(++rowCount);

                Cell header1 = headersRow.createCell(0);
                header1.setCellValue("CLAVE");
                header1.setCellStyle(style);
                Cell header2 = headersRow.createCell(1);
                header2.setCellValue("DESCRIPCION");
                header2.setCellStyle(style);
                headersRow.setRowStyle(style);
                com.anahuac.catalogos.CatTipoLectura tipoLectura = new com.anahuac.catalogos.CatTipoLectura();
                for (int i = 0; i < lstParams.size(); ++i) {
                    tipoLectura = new com.anahuac.catalogos.CatTipoLectura();
                    tipoLectura = (com.anahuac.catalogos.CatTipoLectura) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(tipoLectura.getClave().toString());
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue(tipoLectura.getDescripcion());
                }
            }

            for (int i = 0; i <= rowCount + 3; ++i) {
                sheet.autoSizeColumn(i);
            }
            FileOutputStream outputStream = new FileOutputStream("ReportCatalogo.xls");
            workbook.write(outputStream);

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("ReportCatalogo.xls"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);

        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }
	
	public Result getExcelBachilleratos(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("ReporteBachillerato");
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER)
			//color
			IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
			XSSFColor  color = new XSSFColor(new java.awt.Color(191,220,249),colorMap);
			errorLog = "0";
			
			dataResult = getBachilleratos(0, 20, jsonData, context);
			if (dataResult.success) {
				lstParams = dataResult.getData();

			} else {
				errorLog += dataResult.getError_info();
				throw new Exception("No encontro datos ");
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
			//cal.add(Calendar.HOUR_OF_DAY, -7);
			Date date = cal.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

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
			def titulos = ["ID BANNER","CLAVE","DESCRIPCION","CLAVE DEL PAIS","PAIS","CLAVE DEL ESTADO","ESTADO","CIUDAD","MUNICIPIO","PERTENECE A LA RED","ACTIVO","CODIGO POSTAL","CALLE 1","CALLE 2","CALLE 3","TIPO","FECHA DE CREACION","FECHA DE IMPORTACIÓN"];			
			
			errorLog += "1";
			Row headersRow = sheet.createRow(++rowCount);
			++rowCount;
			List<Cell> header = new ArrayList<Cell>();
			for(int i = 0; i < titulos.size(); ++i) {
				header.add(headersRow.createCell(i))
				header[i].setCellValue(titulos.get(i))
				header[i].setCellStyle(style)
			}
			errorLog += "2";
			
			CellStyle bodyStyle = workbook.createCellStyle();
			bodyStyle.setWrapText(true);
			bodyStyle.setAlignment(HorizontalAlignment.CENTER);
			errorLog += "3";
			def info = ["id","clave","descripcion","nationCode","pais","stateCode","estado","ciudad","municipio","perteneceRed","isEnabled","postalCode","streetLine1","streetLine2","streetLine3","typeInd","fechaCreacion","fechaImportacion"];
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date fechaCreacion = new Date();
			DateFormat dformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			
			List<Cell> body;
			for (int i = 0; i < lstParams.size(); ++i){
				Row row = sheet.createRow(rowCount);
				++rowCount;
				body = new ArrayList<Cell>()
				for(int j=0;  j < info.size(); ++j) {
					
					body.add(row.createCell(j))
					if(lstParams[i][info?.get(j)].toString() == "true" || lstParams[i][info?.get(j)].toString() == "false") {
						body[j].setCellValue( isNullOrBlanck(lstParams[i][info?.get(j)].toString() == "true"?"Si":"No") )
					} else if(info?.get(j) == "fechaCreacion" || info?.get(j) == "fechaImportacion" ) {
						fechaCreacion = dfSalida.parse(isNullOrBlanck(lstParams[i][info?.get(j)].toString()))
						body[j].setCellValue( dformat.format(fechaCreacion) )
					} else {
						body[j].setCellValue( isNullOrBlanck(lstParams[i][info?.get(j)].toString()) )
					}
					body[j].setCellStyle(bodyStyle);
					
				}
			}
			
			errorLog += "8";
			for (int i = 0; i <= 25; ++i) {
				sheet.autoSizeColumn(i);
			}
			
			FileOutputStream outputStream = new FileOutputStream("ReporteBachillerato.xls");
			workbook.write(outputStream);
			outputStream.close();
			
			List < Object > lstResultado = new ArrayList < Object > ();
			lstResultado.add(encodeFileToBase64Binary("ReporteBachillerato.xls"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			
			File file = new File("ReporteBachillerato.xls");
			if (file.exists()){
				if(file.delete()) {
					errorLog+="Sea eliminado Registros"
				}
			 }
			 resultado.setError_info(errorLog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
			e.printStackTrace();
		}

		return resultado;
	}

    public Result getMaterias(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;

        Integer start = 0;
        Integer end = 99999;
        Integer inicioContador = 0;
        Integer finContador = 0;

        List < com.anahuac.catalogos.CatMaterias > lstMaterias = new ArrayList < com.anahuac.catalogos.CatMaterias > ();

        String strError = "";

        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            def objMateriasDAO = context.apiClient.getDAO(CatMateriasDAO.class);
            /*if(object.opcion == 2) {
                lstBachilleratos = objBachilleratosDAO.getCatBachilleratosByEstado(object.estado)
            }else if(object.opcion == 3) {
                lstBachilleratos = objBachilleratosDAO.getCatBachilleratosByPais(object.pais)
            }else {
                
            }*/
            lstMaterias = objMateriasDAO.getCatMateriasList(0, 9999);

            resultado.setError_info(strError);
            resultado.setData(lstMaterias);
            resultado.setSuccess(true);
        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setError_info(strError);
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }
        return resultado
    }

    public Result getTipoLectura(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;

        Integer start = 0;
        Integer end = 99999;
        Integer inicioContador = 0;
        Integer finContador = 0;

        List < com.anahuac.catalogos.CatTipoLectura > lstTipoLectura = new ArrayList < com.anahuac.catalogos.CatTipoLectura > ();

        String strError = "";

        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            def objTipoLecturaDAO = context.apiClient.getDAO(CatTipoLecturaDAO.class);
            /*if(object.opcion == 2) {
                lstBachilleratos = objBachilleratosDAO.getCatBachilleratosByEstado(object.estado)
            }else if(object.opcion == 3) {
                lstBachilleratos = objBachilleratosDAO.getCatBachilleratosByPais(object.pais)
            }else {
                
            }*/
            lstTipoLectura = objTipoLecturaDAO.getCatTipoLecturaList(0, 9999);

            resultado.setError_info(strError);
            resultado.setData(lstTipoLectura);
            resultado.setSuccess(true);
        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setError_info(strError);
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }
        return resultado
    }

    public Result getPdfFileCatalogoAD(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            Result dataResult = new Result();
            int rowCount = 0;
            List < Object > lstParams;
            String type = object.type;

            def documento = "ReportCatalogos.pdf"
            DocumentItext document = new DocumentItext();
            document.setPageSize(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(documento));
            float fontSize = 8.5 f;
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, fontSize, BaseColor.BLACK);
            String phraseToInput = "";
            if (type.equals("materias")) {
                dataResult = getMaterias(parameterP, parameterC, jsonData, context);

                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos de materias");
                }
                document.open();
                Paragraph preface = new Paragraph("LISTADO DE MATERIAS");
                preface.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(preface);
                document.add(new Paragraph(" "));

                Calendar cal = Calendar.getInstance();
                //cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String sDate = formatter.format(date);


                Chunk glue = new Chunk(new VerticalPositionMark());
                Paragraph p = new Paragraph("FECHA: " + sDate);
                p.add(new Chunk(glue));
                p.add("USUARIO: " + object.usuario);
                document.add(p);


                document.add(new Paragraph(" "))

                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100f);

                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);

                header.setPhrase(new Phrase("CLAVE", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("DESCRIPCION", normalFont));
                table.addCell(header);

                com.anahuac.catalogos.CatMaterias materias = new com.anahuac.catalogos.CatMaterias();

                for (int i = 0; i < lstParams.size(); ++i) {
                    materias = new com.anahuac.catalogos.CatMaterias();
                    materias = (com.anahuac.catalogos.CatMaterias) lstParams.get(i);
                    table.addCell(new Phrase(materias.getClave(), normalFont));
                    table.addCell(new Phrase(materias.getDescripcion(), normalFont));
                }

                document.add(table);
                document.close();
            }

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("ReportCatalogos.pdf"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);
        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }

	public Result getExcelPaseLista(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        String errorLog = "";
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams;
			String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("ReportPaseLista");
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER)
			//color
			IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
			XSSFColor  color = new XSSFColor(new java.awt.Color(191,220,249),colorMap);
			errorLog = "0";
            if (type.equals("paselista") || type.equals("paselistareporte") || type.equals("paselistareportelistado") || type.equals("paselistapsicologoadministrador")) {

                if (type.equals("paselista")) {
                    dataResult = new SesionesDAO().getSesionesAspirantes(jsonData, context)
                } else if (type.equals("paselistareportelistado")) {
                    dataResult = new SesionesDAO().getAspirantesPasadosExcel(jsonData, context)
                } else if (type.equals("paselistapsicologoadministrador")) {
                    dataResult = new SesionesDAO().getSesionesPsicologoAdministradorAspirantes(jsonData, context)
                } else {
                    dataResult = new SesionesDAO().getSesionesAspirantesPasados(jsonData, context)
                }
				
                if (dataResult.success) {
                    lstParams = dataResult.getData();
                    errorLog += dataResult.getError_info();
                } else {
                    errorLog += dataResult.getError_info();
                    throw new Exception("No encontro datos de pase de lista");
                }
				

                Row titleRow = sheet.createRow(++rowCount);
                Cell cellReporte = titleRow.createCell(1);
                cellReporte.setCellValue("Reporte:");
                cellReporte.setCellStyle(style);
                Cell cellTitle = titleRow.createCell(2);
                cellTitle.setCellValue("LISTADO DE ASISTENCIA DE LA PRUEBA:\"" + object.nombrePrueba + "\"   ");
                Cell cellFecha = titleRow.createCell(4);
                cellFecha.setCellValue("Fecha:");
                cellFecha.setCellStyle(style);

                Calendar cal = Calendar.getInstance();
                //cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                String sDate = formatter.format(date);
                Cell cellFechaData = titleRow.createCell(5);
                cellFechaData.setCellValue(sDate);

                Row blank = sheet.createRow(++rowCount);

                Cell cellusuario = blank.createCell(4);
                cellusuario.setCellValue("Usuario:");
                cellusuario.setCellStyle(style);
                Cell cellusuarioData = blank.createCell(5);
                cellusuarioData.setCellValue(object.usuarioNombre);

                Row espacio = sheet.createRow(++rowCount);
                def titulos = ["ID BANNER","NOMBRE","EMAIL","CURP","SEXO","CAMPUS","PROGRAMA","INGRESO","PROCEDENCIA","PREPARATORIA","PROMEDIO","RESIDENCIA","TELEFONO","ASISTENCIA"]
                if ( lstParams[0].tipoprueba_pid == "1") {
                    titulos = ["ID BANNER","NOMBRE","EMAIL","CURP","SEXO","CAMPUS","PROGRAMA","INGRESO","PROCEDENCIA","PREPARATORIA","PROMEDIO","RESIDENCIA","TELEFONO","ASISTENCIA","PSICÓLOGO","HORA DE LA ENTREVISTA"];
                }
                
				errorLog += "1";
			    Row headersRow = sheet.createRow(++rowCount);
			    ++rowCount;
			    List<Cell> header = new ArrayList<Cell>();
			    for(int i = 0; i < titulos.size(); ++i) {
				    header.add(headersRow.createCell(i))
				    header[i].setCellValue(titulos.get(i))
				    header[i].setCellStyle(style)
			    }
				errorLog += "2";
				
			    CellStyle bodyStyle = workbook.createCellStyle();
			    bodyStyle.setWrapText(true);
			    bodyStyle.setAlignment(HorizontalAlignment.CENTER);
				errorLog += "3";
				def info = ["idbanner","nombre","correoelectronico","curp","sexo","campus","licenciatura","periodo","procedencia","preparatoria","promediogeneral","residencia","telefonocelular","asistencia"];
				if ( lstParams[0].tipoprueba_pid == "1") {
					info = ["idbanner","nombre","correoelectronico","curp","sexo","campus","licenciatura","periodo","procedencia","preparatoria","promediogeneral","residencia","telefonocelular","asistencia","responsable","horario"];
				}
				
			    List<Cell> body;
			    for (int i = 0; i < lstParams.size(); ++i){
				    Row row = sheet.createRow(rowCount);
				    ++rowCount;
				    body = new ArrayList<Cell>()
				    for(int j=0;  j < info.size(); ++j) {
                        if(j==1){
                            body.add(row.createCell(j))
					        body[j].setCellValue(lstParams[i].apellidopaterno + " " + lstParams[i].apellidomaterno+ " " + lstParams[i].primernombre + " " + lstParams[i].segundonombre)
					        body[j].setCellStyle(bodyStyle);
							errorLog += "5";
                        }else if(j==13){
							errorLog += "4";
                            body.add(row.createCell(j))
					        body[j].setCellValue((lstParams[i].asistencia != null? (lstParams[i].asistencia == "t"?"Sí" : ((lstParams[i].cbcoincide == "t" && lstParams[i].tipoprueba_pid == "4") ?"Aspirante exento" : (lstParams[i].acreditado == "t"?"Acreditado" : "No"))) : ((lstParams[i].cbcoincide == "t" && lstParams[i].tipoprueba_pid == "4")?"Aspirante exento" : (lstParams[i].acreditado == "t"?"Acreditado" : "No"))))
					        body[j].setCellStyle(bodyStyle);
                        }else{
							errorLog += "7"+j+info?.get(j);
                            body.add(row.createCell(j))
					        body[j].setCellValue( isNullOrBlanck(lstParams[i][info?.get(j)].toString()) )
					        body[j].setCellStyle(bodyStyle);
                        }
					    
				    }
			    }
				errorLog += "8";
                for (int i = 0; i <= 15; ++i) {
                    sheet.autoSizeColumn(i);
                }
                
            }
			
            FileOutputStream outputStream = new FileOutputStream("ReportPaseLista.xls");
            workbook.write(outputStream);
			outputStream.close();
			
            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("ReportPaseLista.xls"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);
			
			File file = new File("ReportPaseLista.xls");
			if (file.exists()){
				if(file.delete()) {
					errorLog+="Sea eliminado Registros"
				}
			 }
			 resultado.setError_info(errorLog);
        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
            e.printStackTrace();
        }

        return resultado;
    }
	
	private String isNullOrBlanck(String text) {
		if(text == null || text.equals(null) || text.equals("null") || text.equals("") || text.length() == 0) {
			return "N/A"
		}
		
		return text;
	}

    public Result getPdfPaseLista(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            Result dataResult = new Result();
            int rowCount = 0;
            List < Object > lstParams;
            String type = object.type;

            def documento = "ReportPaseLista.pdf"
            DocumentItext document = new DocumentItext();
            document.setPageSize(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(documento));
            float fontSize = 8.5 f;
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, fontSize, BaseColor.BLACK);
            String phraseToInput = "";
            if (type.equals("paselista") || type.equals("paselistareporte")) {

                if (type.equals("paselista")) {
                    dataResult = new SesionesDAO().getSesionesAspirantes(jsonData, context)
                } else {
                    dataResult = new SesionesDAO().getSesionesAspirantesPasados(jsonData, context)
                }
                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos de pase de lista");
                }
                document.open();
                Paragraph preface = new Paragraph("LISTADO DE ASISTENCIA DE LA PRUEBA:\"" + object.nombrePrueba + "\" ");
                preface.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(preface);
                document.add(new Paragraph(" "));

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String sDate = formatter.format(date);


                Chunk glue = new Chunk(new VerticalPositionMark());
                Paragraph p = new Paragraph("FECHA: " + sDate);
                p.add(new Chunk(glue));
                p.add("USUARIO: " + object.usuarioNombre);
                document.add(p);


                document.add(new Paragraph(" "))

                PdfPTable table = new PdfPTable(10);
                table.setWidthPercentage(100f);

                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);

                header.setPhrase(new Phrase("ID BANNER", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("NOMBRE", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("EMAIL", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("UNIVERSIDAD", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("LICENCIATURA", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("RESIDENCIA", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("SEXO", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("PROMEDIO", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("PREPARATORIA", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("ASISTENCIA", normalFont));
                table.addCell(header);

                SesionesAspiranteCustom Aspirantes = new SesionesAspiranteCustom();

                for (int i = 0; i < lstParams.size(); ++i) {
                    Aspirantes = new SesionesAspiranteCustom();
                    Aspirantes = (SesionesAspiranteCustom) lstParams.get(i);
                    table.addCell(new Phrase(Aspirantes.getAspirantes().get(0).get("idbanner").toString(), normalFont));
                    table.addCell(new Phrase(Aspirantes.getAspirantes().get(0).get("primernombre").toString() + " " + Aspirantes.getAspirantes().get(0).get("segundonombre").toString() + " " + Aspirantes.getAspirantes().get(0).get("apellidopaterno").toString() + " " + Aspirantes.getAspirantes().get(0).get("apellidomaterno").toString(), normalFont));
                    table.addCell(new Phrase(Aspirantes.getAspirantes().get(0).get("correoelectronico").toString(), normalFont));
                    table.addCell(new Phrase(Aspirantes.getAspirantes().get(0).get("campus").toString(), normalFont));
                    table.addCell(new Phrase(Aspirantes.getAspirantes().get(0).get("licenciatura").toString(), normalFont));
                    table.addCell(new Phrase(Aspirantes.getAspirantes().get(0).get("residencia").toString(), normalFont));
                    table.addCell(new Phrase(Aspirantes.getAspirantes().get(0).get("sexo").toString(), normalFont));
                    table.addCell(new Phrase(Aspirantes.getAspirantes().get(0).get("promediogeneral").toString(), normalFont));
                    table.addCell(new Phrase(Aspirantes.getAspirantes().get(0).get("preparatoria").toString(), normalFont));
                    table.addCell(new Phrase((Aspirantes.getAsistencia() != null?(Aspirantes.getAsistencia() == true?"Sí" : "No") : "No").toString(), normalFont));
                }

                document.add(table);
                document.close();
            }

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("ReportPaseLista.pdf"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);
        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }

    public Result getExcelSesionesCalendarizadas(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            Result dataResult = new Result();
            int rowCount = 0;
            List < Object > lstParams;
            String type = object.type;
            String orden = ""
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(type);
            CellStyle style = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);

            if (type.equals("sesioncalendarizadas") || type.equals("sesioncalendarizadasreporte") || type.equals("listasesioncalendarizadas") || type.equals("listasesioncalendarizadaspsicologo") ) {

                if (type.equals("sesioncalendarizadas")) {
                    dataResult = new SesionesDAO().getSesionesCalendarizadas(jsonData, context)
                } else if (type.equals("listasesioncalendarizadas")) {
                    orden = object.orden;
                    dataResult = new SesionesDAO().getSesionesCalendarizadasPasadas(jsonData, context)
                } else if (type.equals("listasesioncalendarizadaspsicologo")) {
                    dataResult = new SesionesDAO().getSesionesCalendarizadasPsicologoSupervisor(jsonData, context)
                } else {
                    dataResult = new SesionesDAO().getSesionesCalendarizadasReporte(jsonData, context)
                }

                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos de sesiones calendarizadas");
                }
                Row titleRow = sheet.createRow(++rowCount);
                Cell cellReporte = titleRow.createCell(1);
                cellReporte.setCellValue("Reporte:");
                cellReporte.setCellStyle(style);
                Cell cellTitle = titleRow.createCell(2);
                cellTitle.setCellValue((!type.equals("sesioncalendarizadas")?"REPORTE DE " : "") + "LISTADO DE SESIONES CALENDARIZADAS " + (orden.equals(">=")?"ACTIVAS" : orden.equals("<")?"PASADAS" : ""));
                Cell cellFecha = titleRow.createCell(4);
                cellFecha.setCellValue("Fecha:");
                cellFecha.setCellStyle(style);

                Calendar cal = Calendar.getInstance();
                //cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                String sDate = formatter.format(date);
                Cell cellFechaData = titleRow.createCell(5);
                cellFechaData.setCellValue(sDate);

                Row blank = sheet.createRow(++rowCount);
                Cell cellusuario = blank.createCell(4);
                cellusuario.setCellValue("Usuario:");
                cellusuario.setCellStyle(style);
                Cell cellusuarioData = blank.createCell(5);
                cellusuarioData.setCellValue(object.usuarioNombre);
                Row espacio = sheet.createRow(++rowCount);

                Row headersRow = sheet.createRow(++rowCount);

                Cell header1 = headersRow.createCell(0);
                header1.setCellValue("ID");
                header1.setCellStyle(style);

                Cell header0 = headersRow.createCell(1);
                header0.setCellValue("NOMBRE DE LA SESION");
                header0.setCellStyle(style);

                Cell header5 = headersRow.createCell(2);
                header5.setCellValue("TIPO PRUEBA");
                header5.setCellStyle(style);

                Cell header2 = headersRow.createCell(3);
                header2.setCellValue("NOMBRE DE LA PRUEBA");
                header2.setCellStyle(style);

                Cell header3 = headersRow.createCell(4);
                header3.setCellValue("CUPO DE LA PRUEBA");
                header3.setCellStyle(style);
                Cell header4 = headersRow.createCell(5);
                header4.setCellValue("ALUMNOS REGISTRADOS");
                header4.setCellStyle(style);

                Cell header6 = headersRow.createCell(6);
                header6.setCellValue("RESIDENCIA");
                header6.setCellStyle(style);

                Cell header7 = headersRow.createCell(7);
                header7.setCellValue("FECHA");
                header7.setCellStyle(style);
                Cell header8 = headersRow.createCell(8);
                header8.setCellValue("LUGAR");
                header8.setCellStyle(style);
                if (type.equals("sesioncalendarizadasreporte") || type.equals("listasesioncalendarizadas")) {
                    Cell header9 = headersRow.createCell(9);
                    header9.setCellValue("ALUMNOS ASISTIERON");
                    header9.setCellStyle(style);
                }
                if (type.equals("listasesioncalendarizadas")) {
                    Cell header10 = headersRow.createCell(10);
                    header10.setCellValue("RESPONSABLE");
                    header10.setCellStyle(style);
                }

                headersRow.setRowStyle(style);

                PruebasCustom SESION = new PruebasCustom();

                for (int i = 0; i < lstParams.size(); ++i) {
                    SESION = new PruebasCustom();
                    SESION = (PruebasCustom) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(SESION.getPrueba().getPersistenceId());

                    Cell cell0 = row.createCell(1);
                    cell0.setCellValue(SESION.getSesion().getNombre());


                    Cell cell5 = row.createCell(2);
                    cell5.setCellValue(SESION.getPrueba().getTipo().getDescripcion());


                    Cell cell2 = row.createCell(3);
                    cell2.setCellValue(SESION.getPrueba().getNombre());

                    Cell cell3 = row.createCell(4);
                    cell3.setCellValue(SESION.getPrueba().getCupo());
                    Cell cell4 = row.createCell(5);
                    cell4.setCellValue(SESION.getPrueba().getRegistrados());

                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(SESION.getSesion().getTipo());
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(SESION.getSesion().getFecha_inicio());
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue(SESION.getPrueba().getLugar());
                    if (type.equals("sesioncalendarizadasreporte") || type.equals("listasesioncalendarizadas")) {
                        Cell cell9 = row.createCell(9);
                        cell9.setCellValue(SESION.getPrueba().getAsistencia());
                    }
                    if (type.equals("listasesioncalendarizadas")) {
                        Cell cell10 = row.createCell(10);
                        cell10.setCellValue(SESION.getPrueba().getResponsables());
                    }


                }
            }

            for (int i = 0; i <= rowCount + 3; ++i) {
                sheet.autoSizeColumn(i);
            }
            FileOutputStream outputStream = new FileOutputStream("ReportSesionesCalendarizadas.xls");
            workbook.write(outputStream);

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("ReportSesionesCalendarizadas.xls"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);

        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }

    public Result getPdfSesionesCalendarizadas(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            Result dataResult = new Result();
            int rowCount = 0;
            List < Object > lstParams;
            String type = object.type;
            String orden = "";
            def documento = "ReportSesionesCalendarizadas.pdf"
            DocumentItext document = new DocumentItext();
            document.setPageSize(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(documento));
            float fontSize = 8.5 f;
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, fontSize, BaseColor.BLACK);
            String phraseToInput = "";
            if (type.equals("sesioncalendarizadas") || type.equals("sesioncalendarizadasreporte") || type.equals("listasesioncalendarizadas")) {

                if (type.equals("sesioncalendarizadas")) {
                    dataResult = new SesionesDAO().getSesionesCalendarizadas(jsonData, context)
                } else if (type.equals("listasesioncalendarizadas")) {
                    orden = object.orden;
                    dataResult = new SesionesDAO().getSesionesCalendarizadasPasadas(jsonData, context)
                } else {
                    dataResult = new SesionesDAO().getSesionesCalendarizadasReporte(jsonData, context)
                }
                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos de pase de lista");
                }
                document.open();
                Paragraph preface = new Paragraph((!type.equals("sesioncalendarizadas")?"REPORTE DE " : "") + "LISTADO DE SESIONES CALENDARIZADAS " + (orden.equals(">=")?"ACTIVAS" : orden.equals("<")?"PASADAS" : ""));
                preface.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(preface);
                document.add(new Paragraph(" "));

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");
                String sDate = formatter.format(date);


                Chunk glue = new Chunk(new VerticalPositionMark());
                Paragraph p = new Paragraph("FECHA: " + sDate);
                p.add(new Chunk(glue));
                p.add("USUARIO: " + object.usuarioNombre);
                document.add(p);


                document.add(new Paragraph(" "))

                PdfPTable table = new PdfPTable(8);
                table.setWidthPercentage(100f);

                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);

                header.setPhrase(new Phrase("ID", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("NOMBRE", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("ALUMNOS GENERALES", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("ALUMNOS ASIGNADOS", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("TIPO PRUEBA", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("RESIDENCIA", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("FECHA", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("LUGAR", normalFont));
                table.addCell(header);

                PruebasCustom SESION = new PruebasCustom();

                for (int i = 0; i < lstParams.size(); ++i) {
                    SESION = new PruebasCustom();
                    SESION = (PruebasCustom) lstParams.get(i);
                    table.addCell(new Phrase(SESION.getPrueba().getPersistenceId().toString(), normalFont));
                    table.addCell(new Phrase(SESION.getPrueba().getNombre().toString(), normalFont));
                    table.addCell(new Phrase(SESION.getPrueba().getCupo().toString(), normalFont));
                    table.addCell(new Phrase(SESION.getPrueba().getRegistrados().toString(), normalFont));
                    table.addCell(new Phrase(SESION.getPrueba().getTipo().getDescripcion().toString(), normalFont));
                    table.addCell(new Phrase(SESION.getSesion().getTipo().toString(), normalFont));
                    table.addCell(new Phrase(SESION.getSesion().getFecha_inicio().toString(), normalFont));
                    table.addCell(new Phrase(SESION.getPrueba().getLugar().toString(), normalFont));
                }

                document.add(table);
                document.close();
            }

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("ReportSesionesCalendarizadas.pdf"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);
        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }

    public Result getExcelGenerico(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            Result dataResult = new Result();
            int rowCount = 0;
            List < Object > lstParams;
            String type = object.type;
            String orden = ""
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(type);
            CellStyle style = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);

            if (!type.equals("")) {

                dataResult = new CatalogosDAO().getCatGenerico(jsonData, context)

                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos en " + type);
                }
                Row titleRow = sheet.createRow(++rowCount);
                Cell cellReporte = titleRow.createCell(1);
                cellReporte.setCellValue("Reporte:");
                cellReporte.setCellStyle(style);
                Cell cellTitle = titleRow.createCell(2);
                cellTitle.setCellValue("LISTADO DE " + type);
                Cell cellFecha = titleRow.createCell(4);
                cellFecha.setCellValue("Fecha:");
                cellFecha.setCellStyle(style);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");

                String sDate = formatter.format(date);
                Cell cellFechaData = titleRow.createCell(5);
                cellFechaData.setCellValue(sDate);

                Row blank = sheet.createRow(++rowCount);
                Cell cellusuario = blank.createCell(4);
                cellusuario.setCellValue("Usuario:");
                cellusuario.setCellStyle(style);
                Cell cellusuarioData = blank.createCell(5);
                cellusuarioData.setCellValue(object.usuarioNombre);

                Row headersRow = sheet.createRow(++rowCount);

                int count = 0;
                if (object.isOrden) {
                    Cell header0 = headersRow.createCell(count);
                    header0.setCellValue("ORDEN");
                    header0.setCellStyle(style);
                    count++;
                }
                Cell header1 = headersRow.createCell(count);
                header1.setCellValue(object.isOrdenAD?"ORDEN" : "CLAVE");
                header1.setCellStyle(style);
                count++;

                Cell header2 = headersRow.createCell(count);
                header2.setCellValue("DESCRIPCION");
                header2.setCellStyle(style);
                count++;

                Cell header3 = headersRow.createCell(count);
                header3.setCellValue("USUARIO CREACIÓN");
                header3.setCellStyle(style);
                count++;

                Cell header4 = headersRow.createCell(count);
                header4.setCellValue("FECHA CREACIÓN");
                header4.setCellStyle(style);


                headersRow.setRowStyle(style);
                DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date fechaCreacion = new Date();
                DateFormat dformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                CatGenericoFiltro GENERICO = new CatGenericoFiltro();
                for (int i = 0; i < lstParams.size(); ++i) {
                    GENERICO = new CatGenericoFiltro();
                    GENERICO = (CatGenericoFiltro) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    int count2 = 0;
                    if (object.isOrden) {
                        Cell cell1 = row.createCell(count2);
                        cell1.setCellValue(GENERICO.getOrden());
                        count2++;
                    }
                    Cell cell2 = row.createCell(count2);
                    cell2.setCellValue(GENERICO.getClave());
                    count2++;
                    Cell cell3 = row.createCell(count2);
                    cell3.setCellValue(GENERICO.getDescripcion());
                    count2++;
                    Cell cell5 = row.createCell(count2);
                    cell5.setCellValue(GENERICO.getUsuarioCreacion());
                    count2++;
                    Cell cell4 = row.createCell(count2);
                    fechaCreacion = dfSalida.parse(GENERICO.getFechaCreacion().toString())
                    cell4.setCellValue(dformat.format(fechaCreacion));
                }
            }

            for (int i = 0; i <= rowCount + 3; ++i) {
                sheet.autoSizeColumn(i);
            }
            FileOutputStream outputStream = new FileOutputStream("ReportGenericos.xls");
            workbook.write(outputStream);

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("ReportGenericos.xls"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);

        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }

    public Result getPdfGenerico(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            Result dataResult = new Result();
            int rowCount = 0;
            List < Object > lstParams;
            String type = object.type;
            String orden = "";
            def documento = "ReportGenericos.pdf"
            DocumentItext document = new DocumentItext();
            document.setPageSize(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(documento));
            float fontSize = 8.5 f;
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, fontSize, BaseColor.BLACK);
            String phraseToInput = "";
            if (!type.equals("")) {
                dataResult = new CatalogosDAO().getCatGenerico(jsonData, context)
                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos de " + type);
                }
                document.open();
                Paragraph preface = new Paragraph("LISTADO DE " + type);
                preface.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(preface);
                document.add(new Paragraph(" "));

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String sDate = formatter.format(date);


                Chunk glue = new Chunk(new VerticalPositionMark());
                Paragraph p = new Paragraph("FECHA: " + sDate);
                p.add(new Chunk(glue));
                p.add("USUARIO: " + object.usuarioNombre);
                document.add(p);


                document.add(new Paragraph(" "))

                PdfPTable table = new PdfPTable((object.isOrden?5 : 4));
                table.setWidthPercentage(100f);

                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);


                if (object.isOrden) {
                    header.setPhrase(new Phrase("ORDEN", normalFont));
                    table.addCell(header);
                }
                header.setPhrase(new Phrase("CLAVE", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("DESCRIPCIÓN", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("USUARIO CREACIÓN", normalFont));
                table.addCell(header);
                header.setPhrase(new Phrase("FECHA CREACIÓN", normalFont));
                table.addCell(header);

                CatGenericoFiltro GENERICO = new CatGenericoFiltro();
                DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date fechaCreacion = new Date();
                DateFormat dformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                for (int i = 0; i < lstParams.size(); ++i) {
                    GENERICO = new CatGenericoFiltro();
                    GENERICO = (CatGenericoFiltro) lstParams.get(i);
                    if (object.isOrden) {
                        table.addCell(new Phrase(GENERICO.getOrden().toString(), normalFont));
                    }
                    table.addCell(new Phrase(GENERICO.getClave().toString(), normalFont));
                    table.addCell(new Phrase(GENERICO.getDescripcion().toString(), normalFont));
                    table.addCell(new Phrase(GENERICO.getUsuarioCreacion().toString(), normalFont));
                    fechaCreacion = dfSalida.parse(GENERICO.getFechaCreacion().toString())
                    table.addCell(new Phrase(dformat.format(fechaCreacion), normalFont));
                }

                document.add(table);
                document.close();
            }

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("ReportGenericos.pdf"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);
        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }
    
    public Result postExcelINVPIndividual(String jsonData,RestAPIContext context) {
        Result resultado = new Result();
        String errorLog = "", where = "";
        Boolean closeCon = false;
        try {
            Result dataResult = new Result();
            int rowCount = 0;
            List < Object > lstParams;
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Reporte INVP individual");
            XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER)
                //color
            IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
            XSSFColor color = new XSSFColor(new java.awt.Color(191, 220, 249), colorMap);

            style.setFillForegroundColor(color)
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            
            dataResult = new SesionesDAO().postResultadosINVPIndividuales(jsonData, context)
            
            if (dataResult.success) {
                lstParams = dataResult.getData();

            } else {
                throw new Exception("No encontro datos");
            }

            def titulos = ["ID Banner", "Nombre aspirante", "Sesión id", "Sesión", "Fecha prueba", "Fecha registro"]
                Row headersRow = sheet.createRow(rowCount);
                ++rowCount;
                List < Cell > header = new ArrayList < Cell > ();
                for (int i = 0; i < titulos.size(); ++i) {
                    header.add(headersRow.createCell(i))
                    header[i].setCellValue(titulos.get(i))
                    header[i].setCellStyle(style)
                }
            CellStyle bodyStyle = workbook.createCellStyle();
            bodyStyle.setWrapText(true);
            bodyStyle.setAlignment(HorizontalAlignment.LEFT);
            DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date fechaCreacion = new Date();
            DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            def info = ["idbanner", "nombre", "sesion_id", "sesion", "fecha_prueba", "fecha_registro"]
            List<Cell> body;
            for (int i = 0; i < lstParams.size(); ++i){
                Row row = sheet.createRow(rowCount);
                ++rowCount;
                body = new ArrayList<Cell>()
                for(int j=0;  j < info.size(); ++j) {
                    body.add(row.createCell(j))
                    if(info.get(j).toString().equals("fecha_registro")) {
                        fechaCreacion = dfSalida.parse(lstParams[i][info.get(j)].toString())
                        body[j].setCellValue(dformat.format(fechaCreacion))
                    }else {
                        body[j].setCellValue(lstParams[i][info.get(j)]);
                    }
                    
                    body[j].setCellStyle(bodyStyle);
                    
                }
                
            }

            if (lstParams.size() > 0) {
                for (int i = 0; i <= 20; ++i) {
                    sheet.autoSizeColumn(i);
                }
                String nameFile = "Reporte INVP Individual.xls";
                FileOutputStream outputStream = new FileOutputStream(nameFile);
                workbook.write(outputStream);
                List < Object > lstResultado = new ArrayList < Object > ();
                lstResultado.add(encodeFileToBase64Binary(nameFile));
                resultado.setData(lstResultado)
                outputStream.close();

            } else {
                throw new Exception("No encontro datos:" + errorLog + dataResult.getError());
            }

            resultado.setSuccess(true);
            

        } catch (Exception e) {
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            
            e.printStackTrace();
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }

        return resultado;
    }
    
    public Result getExcelTransferencias(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            Result dataResult = new Result();
            int rowCount = 0;
            List < Object > lstParams;
            String type = object.type;
            String orden = object.orden;
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(type);
            CellStyle style = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);

            if (type.equals("bitacoratransferencias")) {

                if (type.equals("bitacoratransferencias")) {
                    dataResult = new TransferenciasDAO().selectBitacoraTransferencias(parameterP, parameterC, jsonData, context)
                }

                if (dataResult.success) {
                    lstParams = dataResult.getData();
                } else {
                    throw new Exception("No encontro datos de transferencias");
                }
                Row titleRow = sheet.createRow(++rowCount);
                Cell cellReporte = titleRow.createCell(1);
                cellReporte.setCellValue("Reporte:");
                cellReporte.setCellStyle(style);
                Cell cellTitle = titleRow.createCell(2);
                cellTitle.setCellValue((!type.equals("bitacoratransferencias")?"REPORTE DE " : "") + "BITACORA DE TRANSFERENCIAS " + (orden.equals(">=")?"ENVIADAS" : orden.equals("<")?"RECIBIDAS" : ""));
                Cell cellFecha = titleRow.createCell(4);
                cellFecha.setCellValue("Fecha:");
                cellFecha.setCellStyle(style);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, -7);
                Date date = cal.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                String sDate = formatter.format(date);
                Cell cellFechaData = titleRow.createCell(5);
                cellFechaData.setCellValue(sDate);

                Row blank = sheet.createRow(++rowCount);
                Cell cellusuario = blank.createCell(4);
                cellusuario.setCellValue("Usuario:");
                cellusuario.setCellStyle(style);
                Cell cellusuarioData = blank.createCell(5);
                cellusuarioData.setCellValue(object.usuarioNombre);

                Row headersRow = sheet.createRow(++rowCount);

                Cell header1 = headersRow.createCell(0);
                header1.setCellValue("ID ASPIRANTE");
                header1.setCellStyle(style);

                Cell header0 = headersRow.createCell(1);
                header0.setCellValue("NOMBRE");
                header0.setCellStyle(style);

                Cell header5 = headersRow.createCell(2);
                header5.setCellValue("CARRERA");
                header5.setCellStyle(style);

                Cell header2 = headersRow.createCell(3);
                header2.setCellValue("PERÍODO");
                header2.setCellStyle(style);

                Cell header3 = headersRow.createCell(4);
                header3.setCellValue("ESTATUS");
                header3.setCellStyle(style);
                Cell header4 = headersRow.createCell(5);
                header4.setCellValue("VPD ORIGEN");
                header4.setCellStyle(style);

                Cell header6 = headersRow.createCell(6);
                header6.setCellValue("VPD DESTINO");
                header6.setCellStyle(style);

                Cell header7 = headersRow.createCell(7);
                header7.setCellValue("AUTOR TRANSFERENCIA");
                header7.setCellStyle(style);
                Cell header8 = headersRow.createCell(8);
                header8.setCellValue("FECHA MOVIMIENTO");
                header8.setCellStyle(style);
                /*if(type.equals("sesioncalendarizadasreporte") || type.equals("listasesioncalendarizadas")){
                     Cell header9 = headersRow.createCell(9);
                     header9.setCellValue("ALUMNOS ASISTIERON");
                     header9.setCellStyle(style);
                }
                if(type.equals("listasesioncalendarizadas")){
                     Cell header10 = headersRow.createCell(10);
                     header10.setCellValue("RESPONSABLE");
                     header10.setCellStyle(style);
                }*/

                headersRow.setRowStyle(style);

                PruebasCustom SESION = new PruebasCustom();
                Transferencias BT = new Transferencias();

                for (int i = 0; i < lstParams.size(); ++i) {
                    BT = new Transferencias();
                    BT = (Transferencias) lstParams.get(i);
                    Row row = sheet.createRow(++rowCount);
                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(BT.getIdbanner());

                    Cell cell0 = row.createCell(1);
                    cell0.setCellValue(BT.getAspirante());


                    Cell cell5 = row.createCell(2);
                    cell5.setCellValue(BT.getLicenciatura());


                    Cell cell2 = row.createCell(3);
                    cell2.setCellValue(BT.getPeriodo());

                    Cell cell3 = row.createCell(4);
                    cell3.setCellValue(BT.getEstatus());
                    Cell cell4 = row.createCell(5);
                    cell4.setCellValue(BT.getCampusAnterior());

                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(BT.getCampusNuevo());
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(BT.getUsuarioCreacion());
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue(BT.getFechaCreacion());
                    /*if(type.equals("sesioncalendarizadasreporte") || type.equals("listasesioncalendarizadas")){
                              Cell cell9 = row.createCell(9);
                              cell9.setCellValue(SESION.getPrueba().getAsistencia());
                         }
                         if(type.equals("listasesioncalendarizadas")){
                              Cell cell10 = row.createCell(10);
                              cell10.setCellValue(SESION.getPrueba().getResponsables());
                         }*/


                }
            }

            for (int i = 0; i <= rowCount + 3; ++i) {
                sheet.autoSizeColumn(i);
            }
            FileOutputStream outputStream = new FileOutputStream("ReportBitacoraTransferencias.xls");
            workbook.write(outputStream);

            List < Object > lstResultado = new ArrayList < Object > ();
            lstResultado.add(encodeFileToBase64Binary("ReportBitacoraTransferencias.xls"));
            resultado.setSuccess(true);
            resultado.setData(lstResultado);

        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }
    
    public Result archivedCaseVariable(String caseId, RestAPIContext context) {
        Result resultado = new Result();
        
        CaseVariable objCaseVariable = new CaseVariable();
        
        List<CaseVariable> lstCaseVariable = new ArrayList<CaseVariable>();
        List<String> lstIdVariable = new ArrayList<String>();
        
        Boolean closeCon = false;
        
        String strListVariable = "";
        
        try {
            closeCon = validarConexionBonita();
            pstm = con.prepareStatement(Statements.GET_ID_CASEVARIABLE)
            pstm.setLong(1, Long.valueOf(caseId));
            rs = pstm.executeQuery()
            while (rs.next()) {
                lstIdVariable.add(rs.getString("idVariable"));
            }
            
            if(lstIdVariable.size()>0) {
                strListVariable = lstIdVariable.join(",");
                pstm = con.prepareStatement(Statements.GET_CASEVARIABLE.replace("[LISTIDVARIABLE]", strListVariable));
                rs = pstm.executeQuery()
                while (rs.next()) {
                    objCaseVariable = new CaseVariable();
                    objCaseVariable.setCase_id(rs.getString("containerid"));
                    objCaseVariable.setName(rs.getString("name"));
                    objCaseVariable.setDescription("");
                    objCaseVariable.setType(rs.getString("classname"));
                    
                    if(rs.getString("classname").equals("java.lang.Long")){
                        objCaseVariable.setValue(rs.getString("longvalue") == null ? null : rs.getString("longvalue"));
                    } else if(rs.getString("classname").equals("java.lang.Boolean")){
                        objCaseVariable.setValue(rs.getBoolean("booleanvalue") == null ? null : rs.getBoolean("booleanvalue").toString());
                    } else if(rs.getString("classname").equals("java.lang.String")){
                        objCaseVariable.setValue(rs.getString("clobvalue") == null ? null : rs.getString("clobvalue"));
                    } else if(rs.getString("classname").equals("java.util.Date")){
                        objCaseVariable.setValue(rs.getString("longvalue") == null ? null : rs.getString("longvalue"));
                    } else if(rs.getString("classname").equals("java.util.Map")){
                        objCaseVariable.setValue(rs.getString("clobvalue") == null ? null : rs.getString("clobvalue"));
                    }
                    
                    lstCaseVariable.add(objCaseVariable);
                }
            }
            resultado.setData(lstCaseVariable);
            resultado.setSuccess(true);
        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado;
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
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    public Boolean validarConexion() {
        Boolean retorno = false
        if (con == null || con.isClosed()) {
            con = new DBConnect().getConnection();
            retorno = true
        }
        return retorno
    }
    
    public Boolean validarConexionBonita() {
        Boolean retorno = false
        if (con == null || con.isClosed()) {
            con = new DBConnect().getConnectionBonita();
            retorno = true
        }
        return retorno
    }

}