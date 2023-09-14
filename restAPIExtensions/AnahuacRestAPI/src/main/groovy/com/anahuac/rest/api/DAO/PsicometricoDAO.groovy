package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.catalogos.CatTerapiaDAO
import com.anahuac.rest.api.DAO.BannerDAO
import com.anahuac.model.Autodescripcion
import com.anahuac.model.AutodescripcionDAO
import com.anahuac.model.TestPsicometrico
import com.anahuac.model.TestPsicometricoDAO
import com.anahuac.model.TestPsicometricoRasgos
import com.anahuac.model.TestPsicometricoRasgosDAO
import com.anahuac.model.TestPsicometricoRelativosDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utilities.FileDownload
import com.bonitasoft.web.extension.rest.RestAPIContext
import com.anahuac.rest.api.DAO.SesionesDAO;

import groovy.json.JsonSlurper
import groovy.sql.Sql

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLType
import java.sql.Statement
import java.sql.Types
import java.text.SimpleDateFormat

import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.api.ProcessAPI
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PsicometricoDAO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PsicometricoDAO.class);
    Connection con;
    Statement stm;
    ResultSet rs;
    PreparedStatement pstm;
    public Boolean validarConexion() {
          Boolean retorno=false
          if (con == null || con.isClosed()) {
                con = new DBConnect().getConnection();
                retorno=true
          }
          return retorno
    }
    
    private String generateUpdate(def testPsicomInput) {
        def catPersonaSaludable = null;
        def catVivesEstadoDiscapacidad = null;
        def catProblemaSaludAtencionContinua = null;
        def catRequieresAsistencia = null;
        
        String columnaUpdate = "";
        
        if(testPsicomInput.ajusteEfectivo != null && testPsicomInput.ajusteEfectivo != ""){
            columnaUpdate = columnaUpdate + "ajusteEfectivo = '"+testPsicomInput.ajusteEfectivo+"', ";
        }
        if(testPsicomInput.ajusteEscolarPrevio != null && testPsicomInput.ajusteEscolarPrevio != ""){
            columnaUpdate = columnaUpdate + "ajusteEscolarPrevio = '"+testPsicomInput.ajusteEscolarPrevio+"', ";
        }
        if(testPsicomInput.ajusteExistencial != null && testPsicomInput.ajusteExistencial != ""){
            columnaUpdate = columnaUpdate + "ajusteExistencial = '"+testPsicomInput.ajusteExistencial+"', ";
        }
        if(testPsicomInput.ajusteMedioFamiliar != null && testPsicomInput.ajusteMedioFamiliar != ""){
            columnaUpdate = columnaUpdate + "ajusteMedioFamiliar = '"+testPsicomInput.ajusteMedioFamiliar+"', ";
        }
        if(testPsicomInput.ajusteMedioSocial != null && testPsicomInput.ajusteMedioSocial != ""){
            columnaUpdate = columnaUpdate + "ajusteMedioSocial = '"+testPsicomInput.ajusteMedioSocial+"', ";
        }
        if(testPsicomInput.ajusteReligioso != null && testPsicomInput.ajusteReligioso != ""){
            columnaUpdate = columnaUpdate + "ajusteReligioso = '"+testPsicomInput.ajusteReligioso+"', ";
        }
        if(testPsicomInput.califAjusteAfectivo != null && testPsicomInput.califAjusteAfectivo != ""){
            columnaUpdate = columnaUpdate + "califAjusteAfectivo = '"+testPsicomInput.califAjusteAfectivo+"', ";
        }
        if(testPsicomInput.califAjusteEscolarPrevio != null && testPsicomInput.califAjusteEscolarPrevio != ""){
            columnaUpdate = columnaUpdate + "califAjusteEscolarPrevio = '"+testPsicomInput.califAjusteEscolarPrevio+"', ";
        }
        if(testPsicomInput.califAjusteExistencial != null && testPsicomInput.califAjusteExistencial != ""){
            columnaUpdate = columnaUpdate + "califAjusteExistencial = '"+testPsicomInput.califAjusteExistencial+"', ";
        }
        if(testPsicomInput.califAjusteMedioFamiliar != null && testPsicomInput.califAjusteMedioFamiliar != ""){
            columnaUpdate = columnaUpdate + "califAjusteMedioFamiliar = '"+testPsicomInput.califAjusteMedioFamiliar+"', ";
        }
        if(testPsicomInput.califAjusteMedioSocial != null && testPsicomInput.califAjusteMedioSocial != ""){
            columnaUpdate = columnaUpdate + "califAjusteMedioSocial = '"+testPsicomInput.califAjusteMedioSocial+"', ";
        }
        if(testPsicomInput.califAjusteReligioso != null && testPsicomInput.califAjusteReligioso != ""){
            columnaUpdate = columnaUpdate + "califAjusteReligioso = '"+testPsicomInput.califAjusteReligioso+"', ";
        }
        if(testPsicomInput.conclusioneINVP != null && testPsicomInput.conclusioneINVP != ""){
            columnaUpdate = columnaUpdate + "conclusioneINVP = '"+testPsicomInput.conclusioneINVP+"', ";
        }
        if(testPsicomInput.fechaEntrevista != null && testPsicomInput.fechaEntrevista != ""){
            columnaUpdate = columnaUpdate + "fechaEntrevista = '"+testPsicomInput.fechaEntrevista+"', ";
        }
        if(testPsicomInput.finalizado != null && testPsicomInput.finalizado != ""){
            columnaUpdate = columnaUpdate + "finalizado = '"+testPsicomInput.finalizado+"', ";
        }
        if(testPsicomInput.hasParticipadoActividadesAyuda != null && testPsicomInput.hasParticipadoActividadesAyuda != ""){
            columnaUpdate = columnaUpdate + "hasParticipadoActividadesAyuda = '"+testPsicomInput.hasParticipadoActividadesAyuda+"', ";
        }
        if(testPsicomInput.interpretacionINVP != null && testPsicomInput.interpretacionINVP != ""){
            columnaUpdate = columnaUpdate + "interpretacionINVP = '"+testPsicomInput.interpretacionINVP+"', ";
        }
        if(testPsicomInput.otroTipoAsistencia != null && testPsicomInput.otroTipoAsistencia != ""){
            columnaUpdate = columnaUpdate + "otroTipoAsistencia = '"+testPsicomInput.otroTipoAsistencia+"', ";
        }
        if(testPsicomInput.participacionActividadesVoluntaria != null && testPsicomInput.participacionActividadesVoluntaria != ""){
            columnaUpdate = columnaUpdate + "participacionActividadesVoluntaria = '"+testPsicomInput.participacionActividadesVoluntaria+"', ";
        }
        if(testPsicomInput.puntuacionINVP != null && testPsicomInput.puntuacionINVP != ""){
            columnaUpdate = columnaUpdate + "puntuacionINVP = '"+testPsicomInput.puntuacionINVP+"', ";
        }
        if(testPsicomInput.quienIntegro != null && testPsicomInput.quienIntegro != ""){
            columnaUpdate = columnaUpdate + "quienIntegro = '"+testPsicomInput.quienIntegro+"', ";
        }
        if(testPsicomInput.quienRealizoEntrevista != null && testPsicomInput.quienRealizoEntrevista != ""){
            columnaUpdate = columnaUpdate + "quienRealizoEntrevista = '"+testPsicomInput.quienRealizoEntrevista+"', ";
        }
        if(testPsicomInput.resumenSalud != null && testPsicomInput.resumenSalud != ""){
            columnaUpdate = columnaUpdate + "resumenSalud = '"+testPsicomInput.resumenSalud+"', ";
        }
                
        //OBJ
        if (testPsicomInput.catPersonaSaludable != null && testPsicomInput.catPersonaSaludable != "") {
            catPersonaSaludable = testPsicomInput.catPersonaSaludable
            assert catPersonaSaludable instanceof Map;
            if (catPersonaSaludable.persistenceId != null && catPersonaSaludable.persistenceId != "") {
                columnaUpdate = columnaUpdate + "catpersonasaludable_pid = " + catPersonaSaludable.persistenceId + ", ";
            }
        }
        if (testPsicomInput.catVivesEstadoDiscapacidad != null && testPsicomInput.catVivesEstadoDiscapacidad != "") {
            catVivesEstadoDiscapacidad = testPsicomInput.catVivesEstadoDiscapacidad
            assert catVivesEstadoDiscapacidad instanceof Map;
            if (catVivesEstadoDiscapacidad.persistenceId != null && catVivesEstadoDiscapacidad.persistenceId != "") {
                columnaUpdate = columnaUpdate + "catvivesestadodiscapacidad_pid = " + catVivesEstadoDiscapacidad.persistenceId + ", ";
            }
        }
        if (testPsicomInput.catProblemaSaludAtencionContinua != null && testPsicomInput.catProblemaSaludAtencionContinua != "") {
            catProblemaSaludAtencionContinua = testPsicomInput.catProblemaSaludAtencionContinua
            assert catProblemaSaludAtencionContinua instanceof Map;
            if (catProblemaSaludAtencionContinua.persistenceId != null && catProblemaSaludAtencionContinua.persistenceId != "") {
                columnaUpdate = columnaUpdate + "catproblemasaludatencionco_pid = " + catProblemaSaludAtencionContinua.persistenceId + ", ";
            }
        }
        if (testPsicomInput.catRequieresAsistencia != null && testPsicomInput.catRequieresAsistencia != "") {
            catRequieresAsistencia = testPsicomInput.catRequieresAsistencia
            assert catRequieresAsistencia instanceof Map;
            if (catRequieresAsistencia.persistenceId != null && catRequieresAsistencia.persistenceId != "") {
                columnaUpdate = columnaUpdate + "catrequieresasistencia_pid = " + catRequieresAsistencia.persistenceId + ", ";
            }
        }
        columnaUpdate = columnaUpdate.substring(0, columnaUpdate.length()-2);
        return Statements.UPDATE_TESTPSICOMETRICO.replace("[COLUMNA]", columnaUpdate);
    }
    
    public Result insertUpdatePsicometrico(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
                
        String strError = "";
        String columnaUpdate = "";
        String columnaInsert = "";
        String valorInsert = "";
        String consultaInsert = "";
        String consultaUpdate = "";
        String caseId = "";
            
        def catPersonaSaludable = null;
        def catVivesEstadoDiscapacidad = null;
        def catProblemaSaludAtencionContinua = null;
        def catRequieresAsistencia = null;

		def catParentezco = null;
		def vive = null;
		def carrera = null;
		
		def rasgo = null;
		def calificacion = null;
		
		def catTipoTerapia = null;
		def catRecibidoTerapia = null;
		
        Boolean closeCon = false;
        
        Integer testPsicomInput_persistenceId = 0;
        Integer contador = 0;
        
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            assert object instanceof Map;
            
            def testPsicomInput = object.testPsicomInput;
            assert testPsicomInput instanceof Map;
            
            if(object.caseId != null && object.caseId != ""){
                caseId=object.caseId;
            }
                        
            consultaInsert = Statements.INSERT_TESTPSICOMETRICO;
            consultaUpdate = generateUpdate(testPsicomInput);
            
            strError = strError + " | " + consultaInsert;
            strError = strError + " | " + consultaUpdate;
            strError = strError + " | caseId: " + caseId
            
            closeCon = validarConexion();
            
            
            /*========================================================TEST PSICOMETRICO ACCIONES========================================================*/
            pstm = con.prepareStatement(Statements.SELECT_TESTPSICOMETRICO_BY_CASEID);
            pstm.setString(1, caseId);
            rs = pstm.executeQuery();
            if(rs.next()) {
                pstm = con.prepareStatement(consultaUpdate);
                pstm.setString(1, caseId);
                pstm.executeUpdate();
            }
            else {
                
                if (testPsicomInput.catPersonaSaludable != null && testPsicomInput.catPersonaSaludable != "") {
                    catPersonaSaludable = testPsicomInput.catPersonaSaludable
                    assert catPersonaSaludable instanceof Map;
            
                }
                if (testPsicomInput.catVivesEstadoDiscapacidad != null && testPsicomInput.catVivesEstadoDiscapacidad != "") {
                    catVivesEstadoDiscapacidad = testPsicomInput.catVivesEstadoDiscapacidad
                    assert catVivesEstadoDiscapacidad instanceof Map;
                    
                }
                if (testPsicomInput.catProblemaSaludAtencionContinua != null && testPsicomInput.catProblemaSaludAtencionContinua != "") {
                    catProblemaSaludAtencionContinua = testPsicomInput.catProblemaSaludAtencionContinua
                    assert catProblemaSaludAtencionContinua instanceof Map;
                    
                }
                if (testPsicomInput.catRequieresAsistencia != null && testPsicomInput.catRequieresAsistencia != "") {
                    catRequieresAsistencia = testPsicomInput.catRequieresAsistencia
                    assert catRequieresAsistencia instanceof Map;
                    
                }
            
                pstm = con.prepareStatement(consultaInsert);
                pstm.setString(1, (testPsicomInput.ajusteEfectivo != null && testPsicomInput.ajusteEfectivo != "") ? testPsicomInput.ajusteEfectivo : "");
                pstm.setString(2, (testPsicomInput.ajusteEscolarPrevio != null && testPsicomInput.ajusteEscolarPrevio != "") ? testPsicomInput.ajusteEscolarPrevio : "");
                pstm.setString(3, (testPsicomInput.ajusteExistencial != null && testPsicomInput.ajusteExistencial != "") ? testPsicomInput.ajusteExistencial : "");
                pstm.setString(4, (testPsicomInput.ajusteMedioFamiliar != null && testPsicomInput.ajusteMedioFamiliar != "") ? testPsicomInput.ajusteMedioFamiliar : "");
                pstm.setString(5, (testPsicomInput.ajusteMedioSocial != null && testPsicomInput.ajusteMedioSocial != "") ? testPsicomInput.ajusteMedioSocial : "");
                pstm.setString(6, (testPsicomInput.ajusteReligioso != null && testPsicomInput.ajusteReligioso != "") ? testPsicomInput.ajusteReligioso : "");
                pstm.setString(7, (testPsicomInput.califAjusteAfectivo != null && testPsicomInput.califAjusteAfectivo != "") ? testPsicomInput.califAjusteAfectivo : "");
                pstm.setString(8, (testPsicomInput.califAjusteEscolarPrevio != null && testPsicomInput.califAjusteEscolarPrevio != "") ? testPsicomInput.califAjusteEscolarPrevio : "");
                pstm.setString(9, (testPsicomInput.califAjusteExistencial != null && testPsicomInput.califAjusteExistencial != "") ? testPsicomInput.califAjusteExistencial : "");
                pstm.setString(10, (testPsicomInput.califAjusteMedioFamiliar != null && testPsicomInput.califAjusteMedioFamiliar != "") ? testPsicomInput.califAjusteMedioFamiliar : "");
                pstm.setString(11, (testPsicomInput.califAjusteMedioSocial != null && testPsicomInput.califAjusteMedioSocial != "") ? testPsicomInput.califAjusteMedioSocial : "");
                pstm.setString(12, (testPsicomInput.califAjusteReligioso != null && testPsicomInput.califAjusteReligioso != "") ? testPsicomInput.califAjusteReligioso : "");
                pstm.setString(13, caseId);
                pstm.setString(14, (testPsicomInput.conclusioneINVP != null && testPsicomInput.conclusioneINVP != "") ? testPsicomInput.conclusioneINVP : "");
                if(testPsicomInput.fechaEntrevista != null && testPsicomInput.fechaEntrevista != "") {
                    pstm.setString(15, testPsicomInput.fechaEntrevista);
                }
                else {
                    pstm.setNull(15, Types.TIMESTAMP);
                }
                pstm.setBoolean(16, (testPsicomInput.finalizado != null && testPsicomInput.finalizado != "") ? testPsicomInput.finalizado : false);
                pstm.setString(17, (testPsicomInput.hasParticipadoActividadesAyuda != null && testPsicomInput.hasParticipadoActividadesAyuda != "") ? testPsicomInput.hasParticipadoActividadesAyuda : "");
                pstm.setString(18, (testPsicomInput.interpretacionINVP != null && testPsicomInput.interpretacionINVP != "") ? testPsicomInput.interpretacionINVP : "");
                pstm.setString(19, (testPsicomInput.otroTipoAsistencia != null && testPsicomInput.otroTipoAsistencia != "") ? testPsicomInput.otroTipoAsistencia : "");
                pstm.setString(20, (testPsicomInput.participacionActividadesVoluntaria != null && testPsicomInput.participacionActividadesVoluntaria != "") ? testPsicomInput.participacionActividadesVoluntaria : "");
                pstm.setInt(21, 0);
                pstm.setInt(22, (testPsicomInput.puntuacionINVP != null && testPsicomInput.puntuacionINVP != "") ? testPsicomInput.puntuacionINVP : 0);
                pstm.setString(23, (testPsicomInput.quienIntegro != null && testPsicomInput.quienIntegro != "") ? testPsicomInput.quienIntegro : "");
                pstm.setString(24, (testPsicomInput.quienRealizoEntrevista != null && testPsicomInput.quienRealizoEntrevista != "") ? testPsicomInput.quienRealizoEntrevista : "");
                pstm.setString(25, (testPsicomInput.resumenSalud != null && testPsicomInput.resumenSalud != "") ? testPsicomInput.resumenSalud : "");
                
                if(catPersonaSaludable.persistenceId != null && catPersonaSaludable.persistenceId != ""){
                    pstm.setInt(26, catPersonaSaludable.persistenceId); 
                }
                else {
                    pstm.setNull(26, Types.INTEGER);
                }
                if(catProblemaSaludAtencionContinua.persistenceId != null && catProblemaSaludAtencionContinua.persistenceId != ""){
                    pstm.setInt(27, catProblemaSaludAtencionContinua.persistenceId);    
                }
                else {
                    pstm.setNull(27, Types.INTEGER);    
                }
                if(catRequieresAsistencia.persistenceId != null && catRequieresAsistencia.persistenceId != ""){
                    pstm.setInt(28, catRequieresAsistencia.persistenceId);  
                }
                else {
                    pstm.setNull(28, Types.INTEGER);    
                }
                if(catVivesEstadoDiscapacidad.persistenceId != null && catVivesEstadoDiscapacidad.persistenceId != ""){
                    pstm.setInt(29, catVivesEstadoDiscapacidad.persistenceId);  
                }
                else {
                    pstm.setNull(29, Types.INTEGER);    
                }
                pstm.executeUpdate();
            }
            
            //caseId;
            pstm = con.prepareStatement(Statements.SELECT_TESTPSICOMETRICO_BY_CASEID);
            pstm.setString(1, caseId);
            rs = pstm.executeQuery();
            if(rs.next()) {
                testPsicomInput_persistenceId = rs.getInt("persistenceid");
            }
            strError = strError + " | testPsicomInput_persistenceId: " + testPsicomInput_persistenceId;
            strError = strError + " | " + "-------------------------------------------";
            
            strError = strError + " | " + Statements.DELETE_TESTPSICOMETRICO_FIENTESINFLUYE;
            
            /*==============================================TESTPSICOMETRICO_FIENTESINFLUYE INICIO==============================================*/
            pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_FIENTESINFLUYE);
            pstm.setInt(1, testPsicomInput_persistenceId);
            pstm.executeUpdate();
            strError = strError + " | " + "-------------------------------------------";
            if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
                strError = strError + " | " + "-------------------------------------------";
                if (testPsicomInput.fientesInfluyeronDesicion != null && testPsicomInput.fientesInfluyeronDesicion != "") {
                    assert testPsicomInput.fientesInfluyeronDesicion instanceof List;
                    for (def row: testPsicomInput.fientesInfluyeronDesicion) {
                        if(row.persistenceId != null && row.persistenceId != ""){
                            pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_FIENTESINFLUYE);
                            pstm.setInt(1, testPsicomInput_persistenceId);
                            pstm.setInt(2, row.persistenceId);
                            pstm.setInt(3, contador);
                            pstm.executeUpdate();
                            contador++;
                            strError = strError + " | contador: " + contador;
                        }
                    }
                }
            }
            /*==============================================TESTPSICOMETRICO_FIENTESINFLUYE FIN==============================================*/
            /*==============================================TESTPSICOMETRICO_RAZONESINGRESO INICIO==============================================*/
            contador = 0;
            pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_RAZONESINGRESO);
            pstm.setInt(1, testPsicomInput_persistenceId);
            pstm.executeUpdate();
            strError = strError + " | " + "-------------------------------------------";
            if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
                strError = strError + " | " + "-------------------------------------------";
                if (testPsicomInput.razonesIngreso != null && testPsicomInput.razonesIngreso != "") {
                    assert testPsicomInput.razonesIngreso instanceof List;
                    for (def row: testPsicomInput.razonesIngreso) {
                        if(row.persistenceId != null && row.persistenceId != ""){
                            pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_RAZONESINGRESO);
                            pstm.setInt(1, testPsicomInput_persistenceId);
                            pstm.setInt(2, row.persistenceId);
                            pstm.setInt(3, contador);
                            pstm.executeUpdate();
                            contador++;
                            strError = strError + " | contador: " + contador;
                        }
                    }
                }
            }
            /*==============================================TESTPSICOMETRICO_RAZONESINGRESO FIN==============================================*/
            /*==============================================TESTPSICOMETRICO_DISCAPACIDADES INICIO==============================================*/
            contador = 0;
            pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_DISCAPACIDADES);
            pstm.setInt(1, testPsicomInput_persistenceId);
            pstm.executeUpdate();
            strError = strError + " | " + "-------------------------------------------";
            if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
                strError = strError + " | " + "-------------------------------------------";
                if (testPsicomInput.discapacidades != null && testPsicomInput.discapacidades != "") {
                    assert testPsicomInput.discapacidades instanceof List;
                    for (def row: testPsicomInput.discapacidades) {
                        if(row.persistenceId != null && row.persistenceId != ""){
                            pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_DISCAPACIDADES);
                            pstm.setInt(1, testPsicomInput_persistenceId);
                            pstm.setInt(2, row.persistenceId);
                            pstm.setInt(3, contador);
                            pstm.executeUpdate();
                            contador++;
                            strError = strError + " | contador: " + contador;
                        }
                    }
                }
            }
            /*==============================================TESTPSICOMETRICO_DISCAPACIDADES FIN==============================================*/
            /*==============================================TESTPSICOMETRICO_CUSTOSRECOMEND INICIO==============================================*/
            contador = 0;
            pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_CUSTOSRECOMEND);
            pstm.setInt(1, testPsicomInput_persistenceId);
            pstm.executeUpdate();
            strError = strError + " | " + "-------------------------------------------";
            if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
                strError = strError + " | " + "-------------------------------------------";
                if (testPsicomInput.custosRecomendados != null && testPsicomInput.custosRecomendados != "") {
                    assert testPsicomInput.custosRecomendados instanceof List;
                    for (def row: testPsicomInput.custosRecomendados) {
                        if(row.persistenceId != null && row.persistenceId != ""){
                            pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_CUSTOSRECOMEND);
                            pstm.setInt(1, testPsicomInput_persistenceId);
                            pstm.setInt(2, row.persistenceId);
                            pstm.setInt(3, contador);
                            pstm.executeUpdate();
                            contador++;
                            strError = strError + " | contador: " + contador;
                        }
                    }
                }
            }
            /*==============================================TESTPSICOMETRICO_CUSTOSRECOMEND FIN==============================================*/
            /*==============================================TESTPSICOMETRICO_PROBLEMASSALUD INICIO==============================================*/
            contador = 0;
            pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_PROBLEMASSALUD);
            pstm.setInt(1, testPsicomInput_persistenceId);
            pstm.executeUpdate();
            strError = strError + " | " + "-------------------------------------------";
			
//            if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
//                strError = strError + " | " + "-------------------------------------------";
//                if (testPsicomInput.problemasSalud != null && testPsicomInput.problemasSalud != "") {
//                    assert testPsicomInput.problemasSalud instanceof List;
//                    for (def row: testPsicomInput.problemasSalud) {
//                        if(row.persistenceId != null && row.persistenceId != ""){
//                            pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_PROBLEMASSALUD);
//                            pstm.setInt(1, testPsicomInput_persistenceId);
//                            pstm.setInt(2, row.persistenceId);
//                            pstm.setInt(3, contador);
//                            pstm.executeUpdate();
//                            contador++;
//                            strError = strError + " | contador: " + contador;
//                        }
//                    }
//                }
//            }
			
            /*==============================================TESTPSICOMETRICO_PROBLEMASSALUD FIN==============================================*/
            /*==============================================TESTPSICOMETRICO_TIPOASISTENCIA INICIO==============================================*/
            contador = 0;
            pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_TIPOASISTENCIA);
            pstm.setInt(1, testPsicomInput_persistenceId);
            pstm.executeUpdate();
            strError = strError + " | " + "-------------------------------------------";
            if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
                strError = strError + " | " + "-------------------------------------------";
                if (testPsicomInput.tipoAsistencia != null && testPsicomInput.tipoAsistencia != "") {
                    assert testPsicomInput.tipoAsistencia instanceof List;
                    for (def row: testPsicomInput.tipoAsistencia) {
                        if(row.persistenceId != null && row.persistenceId != ""){
                            pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_TIPOASISTENCIA);                                                                         
                            pstm.setInt(1, testPsicomInput_persistenceId);
                            pstm.setInt(2, row.persistenceId);
                            pstm.setInt(3, contador);
                            pstm.executeUpdate();
                            contador++;
                            strError = strError + " | contador: " + contador;
                        }
                    }
                }
            }
            /*==============================================TESTPSICOMETRICO_TIPOASISTENCIA FIN==============================================*/
            /*========================================================TEST PSICOMETRICO ACCIONES========================================================*/
			/*========================================================TEST PSICOMETRICO OBSERVACIONES ACCIONES========================================================*/
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_OBSERVACIONES);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomObservacionesInput != null && object.testPsicomObservacionesInput != "") {
					assert object.testPsicomObservacionesInput instanceof List;
					for (def row: object.testPsicomObservacionesInput) {
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_OBSERVACIONES);
						pstm.setString(1, (row.orden !=null && row.orden !="") ? row.orden : "");
						pstm.setString(2, (row.universidad !=null && row.universidad !="") ? row.universidad : "");
						pstm.setBoolean(3, (row.examen !=null && row.examen !="") ? row.examen : false);
						pstm.setBoolean(4, (row.admitido !=null && row.admitido !="") ? row.admitido : false);
						pstm.setBoolean(5, (row.vencido !=null && row.vencido !="") ? row.vencido : false);
						pstm.setInt(6, (row.porcentajeBeca !=null && row.porcentajeBeca !="") ? row.porcentajeBeca : 0);
						pstm.setInt(7, (row.porcentajeCredito !=null && row.porcentajeCredito !="") ? row.porcentajeCredito : 0);
						pstm.setString(8, caseId);
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			
			
			/*========================================================TEST PSICOMETRICO OBSERVACIONES ACCIONES========================================================*/
			/*========================================================TEST PSICOMETRICO RELATIVOS ACCIONES========================================================*/
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_RELATIVOS);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomRelativosInput != null && object.testPsicomRelativosInput != "") {
					assert object.testPsicomRelativosInput instanceof List;
					for (def row: object.testPsicomRelativosInput) {
						catParentezco = row.catParentezco;
						vive = row.vive;
						
						assert catParentezco instanceof Map;
						assert vive instanceof Map;
						
						strError = strError + " | contador: " + (1 + " - " + (row.nombre != null && row.nombre != "") ? row.nombre : "");
						strError = strError + " | contador: " + (2 + " - " + (row.apellidos != null && row.apellidos != "") ? row.apellidos : "");
						strError = strError + " | contador: " + (3 + " - " + (row.empresaTrabaja != null && row.empresaTrabaja != "") ? row.empresaTrabaja : "");
						strError = strError + " | contador: " + (4 + " - " + (row.otroParentesco != null && row.otroParentesco != "") ? row.otroParentesco : "");
						strError = strError + " | contador: " + (5 + " - " + caseId);
						strError = strError + " | contador: " + (6 + " - " + (row.jubilado != null && row.jubilado != "") ? row.jubilado : false);
						strError = strError + " | contador: " + (7 + " - " + (row.vencido != null && row.vencido != "") ? row.vencido : false);
						
						if (catParentezco.persistenceId != null && catParentezco.persistenceId != "") {
							strError = strError + " | contador: " + (8 + " - " + catParentezco.persistenceId);
						} else {
							strError = strError + " | contador: " + (8 + " - Types.INTEGER");
						}
						if (vive.persistenceId != null && vive.persistenceId != "") {
							strError = strError + " | contador: " + (9 + " - " + catParentezco.persistenceId);
						} else {
							strError = strError + " | contador: " + (9 + " - Types.INTEGER");
						}
						
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_RELATIVOS);
						pstm.setString(1, (row.nombre !=null && row.nombre !="") ? row.nombre : "");
						pstm.setString(2, (row.apellidos !=null && row.apellidos !="") ? row.apellidos : "");
						pstm.setString(3, (row.empresaTrabaja !=null && row.empresaTrabaja !="") ? row.empresaTrabaja : "");
						pstm.setString(4, (row.otroParentesco !=null && row.otroParentesco !="") ? row.otroParentesco : "");
						pstm.setString(5, caseId);
						pstm.setBoolean(6, (row.jubilado !=null && row.jubilado !="") ? row.jubilado : false);
						pstm.setBoolean(7, (row.vencido !=null && row.vencido !="") ? row.vencido : false);
						
						if(catParentezco.persistenceId != null && catParentezco.persistenceId != ""){
							pstm.setInt(8, catParentezco.persistenceId);
						}
						else {
							pstm.setNull(8, Types.INTEGER);
						}
						if(vive.persistenceId != null && vive.persistenceId != ""){
							pstm.setInt(9, vive.persistenceId);
						}
						else {
							pstm.setNull(9, Types.INTEGER);
						}
						
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			
			
			/*========================================================TEST PSICOMETRICO RELATIVOS ACCIONES========================================================*/
			/*========================================================TEST PSICOMETRICO CARRERAS REC ACCIONES========================================================*/
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_CARRERASREC);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomCarrerasRecInput != null && object.testPsicomCarrerasRecInput != "") {
					assert object.testPsicomCarrerasRecInput instanceof List;
					for (def row: object.testPsicomCarrerasRecInput) {
						carrera = row.carrera;
						
						assert carrera instanceof Map;
												
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_CARRERASREC);
						pstm.setInt(1, (row.orden !=null && row.orden !="") ? row.orden : "");
						pstm.setString(2, (row.comentarios !=null && row.comentarios !="") ? row.comentarios : "");
						pstm.setString(3, caseId);
						pstm.setBoolean(4, (row.vencido !=null && row.vencido !="") ? row.vencido : "");
						
						if(carrera.persistenceId != null && carrera.persistenceId != ""){
							pstm.setInt(5, carrera.persistenceId);
						}
						else {
							pstm.setNull(5, Types.INTEGER);
						}
						
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			/*========================================================TEST PSICOMETRICO CARRERAS REC ACCIONES========================================================*/
			/*========================================================TEST PSICOMETRICO RASGOS ACCIONES========================================================*/
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_RASGOS);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomRasgosInput != null && object.testPsicomRasgosInput != "") {
					assert object.testPsicomRasgosInput instanceof List;
					for (def row: object.testPsicomRasgosInput) {
						rasgo = row.rasgo;
						calificacion = row.calificacion;
						
						assert rasgo instanceof Map;
						assert calificacion instanceof Map;
												
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_RASGOS);
						if(rasgo.persistenceId != null && rasgo.persistenceId != ""){
							pstm.setInt(1, rasgo.persistenceId);
						}
						else {
							pstm.setNull(1, Types.INTEGER);
						}
						if(calificacion.persistenceId != null && calificacion.persistenceId != ""){
							pstm.setInt(2, calificacion.persistenceId);
						}
						else {
							pstm.setNull(2, Types.INTEGER);
						}
						pstm.setString(3, caseId);
						pstm.setBoolean(4, (row.vencido !=null && row.vencido !="") ? row.vencido : "");
						
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			/*========================================================TEST PSICOMETRICO RASGOS ACCIONES========================================================*/
			/*========================================================TEST PSICOMETRICO TERAPIAS ACCIONES========================================================*/
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_TERAPIAS);
			pstm.setLong(1, Long.valueOf(caseId));
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomTerapiasInput != null && object.testPsicomTerapiasInput != "") {
					assert object.testPsicomTerapiasInput instanceof List;
					for (def row: object.testPsicomTerapiasInput) {
						catTipoTerapia = row.catTipoTerapia;
						catRecibidoTerapia = row.catRecibidoTerapia;
						
						assert catTipoTerapia instanceof Map;
						assert catRecibidoTerapia instanceof Map;
												
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_TERAPIAS);
						pstm.setString(1, (row.tipoTerapia != null && row.tipoTerapia != "") ? row.tipoTerapia : "");
						pstm.setString(2, (row.cuantoTiempo != null && row.cuantoTiempo != "") ? row.cuantoTiempo : "");
						pstm.setString(3, (row.recibidoTerapiaString != null && row.recibidoTerapiaString != "") ? row.recibidoTerapiaString : "");
						pstm.setString(4, (row.otraTerapia != null && row.otraTerapia != "") ? row.otraTerapia : "");
						pstm.setLong(5, Long.valueOf(caseId));
						pstm.setBoolean(6, (row.vencido !=null && row.vencido !="") ? row.vencido : "");
						if(catTipoTerapia.persistenceId != null && catTipoTerapia.persistenceId != ""){
							pstm.setInt(7, catTipoTerapia.persistenceId);
						}
						else {
							pstm.setNull(7, Types.INTEGER);
						}
						if(catRecibidoTerapia.persistenceId != null && catRecibidoTerapia.persistenceId != ""){
							pstm.setInt(8, catRecibidoTerapia.persistenceId);
						}
						else {
							pstm.setNull(8, Types.INTEGER);
						}
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			/*========================================================TEST PSICOMETRICO TERAPIAS ACCIONES========================================================*/
			
			/*========================================================TEST PSICOMETRICO RELATIVOS ACCIONES========================================================*/
			//
            

            resultado.setError_info(strError);
            resultado.setSuccess(true);
        } catch (Exception e) {
            LOGGER.error "e: "+e.getMessage();
            resultado.setError_info(strError);
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            e.printStackTrace();
        } finally {
            if(closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }
    
	private Result generateUpdateResult(def testPsicomInput) {
		Result result = new Result();
		def catPersonaSaludable = null;
		def catVivesEstadoDiscapacidad = null;
		def catProblemaSaludAtencionContinua = null;
		def catRequieresAsistencia = null;
		def catRecibidoTerapia = null;
		
		String columnaUpdate = "";
		String strError = "error log :: ";
		try {
			if(testPsicomInput.ajusteEfectivo != null){
				columnaUpdate = columnaUpdate + "ajusteEfectivo = '"+testPsicomInput.ajusteEfectivo + "', ";
			}
			if(testPsicomInput.ajusteEscolarPrevio != null){
				columnaUpdate = columnaUpdate + "ajusteEscolarPrevio = '"+testPsicomInput.ajusteEscolarPrevio+"', ";
			}
			if(testPsicomInput.ajusteExistencial != null){
				columnaUpdate = columnaUpdate + "ajusteExistencial = '"+testPsicomInput.ajusteExistencial+"', ";
			}
			if(testPsicomInput.ajusteMedioFamiliar != null){
				columnaUpdate = columnaUpdate + "ajusteMedioFamiliar = '"+testPsicomInput.ajusteMedioFamiliar+"', ";
			}
			if(testPsicomInput.ajusteMedioSocial != null){
				columnaUpdate = columnaUpdate + "ajusteMedioSocial = '"+testPsicomInput.ajusteMedioSocial+"', ";
			}
			if(testPsicomInput.ajusteReligioso != null){
				columnaUpdate = columnaUpdate + "ajusteReligioso = '"+testPsicomInput.ajusteReligioso+"', ";
			}
			if(testPsicomInput.califAjusteAfectivo != null){
				columnaUpdate = columnaUpdate + "califAjusteAfectivo = '"+testPsicomInput.califAjusteAfectivo+"', ";
			}
			if(testPsicomInput.califAjusteEscolarPrevio != null){
				columnaUpdate = columnaUpdate + "califAjusteEscolarPrevio = '"+testPsicomInput.califAjusteEscolarPrevio+"', ";
			}
			if(testPsicomInput.califAjusteExistencial != null){
				columnaUpdate = columnaUpdate + "califAjusteExistencial = '"+testPsicomInput.califAjusteExistencial+"', ";
			}
			if(testPsicomInput.califAjusteMedioFamiliar != null){
				columnaUpdate = columnaUpdate + "califAjusteMedioFamiliar = '"+testPsicomInput.califAjusteMedioFamiliar+"', ";
			}
			if(testPsicomInput.califAjusteMedioSocial != null){
				columnaUpdate = columnaUpdate + "califAjusteMedioSocial = '"+testPsicomInput.califAjusteMedioSocial+"', ";
			}
			if(testPsicomInput.califAjusteReligioso != null){
				columnaUpdate = columnaUpdate + "califAjusteReligioso = '"+testPsicomInput.califAjusteReligioso+"', ";
			}
			if(testPsicomInput.conclusioneINVP != null ){
				columnaUpdate = columnaUpdate + "conclusioneINVP = '"+testPsicomInput.conclusioneINVP+"', ";
			}
			if(testPsicomInput.fechaEntrevista != null){
				columnaUpdate = columnaUpdate + "fechaEntrevista = '"+testPsicomInput.fechaEntrevista+"', ";
			}
			if(testPsicomInput.sesion_pid != null && testPsicomInput.sesion_pid == 0){
				columnaUpdate = columnaUpdate + "sesion_pid = "+testPsicomInput.sesion_pid+" , ";
			}
			if(testPsicomInput.finalizado != null && testPsicomInput.finalizado != ""){
				columnaUpdate = columnaUpdate + "finalizado = "+testPsicomInput.finalizado+" , ";
			}
			if(testPsicomInput.finalizado != null && testPsicomInput.finalizado != "" && testPsicomInput.finalizado == true){
				columnaUpdate = columnaUpdate + "fechaFinalizacion = NOW() , ";
			}
			if(testPsicomInput.hasParticipadoActividadesAyuda != null ){
				columnaUpdate = columnaUpdate + "hasParticipadoActividadesAyuda = '"+testPsicomInput.hasParticipadoActividadesAyuda+"', ";
			}
			if(testPsicomInput.interpretacionINVP != null){
				columnaUpdate = columnaUpdate + "interpretacionINVP = '"+testPsicomInput.interpretacionINVP+"', ";
			}
			if(testPsicomInput.otroTipoAsistencia != null){
				columnaUpdate = columnaUpdate + "otroTipoAsistencia = '"+testPsicomInput.otroTipoAsistencia+"', ";
			}
			if(testPsicomInput.participacionActividadesVoluntaria != null){
				columnaUpdate = columnaUpdate + "participacionActividadesVoluntaria = '"+testPsicomInput.participacionActividadesVoluntaria+"', ";
			}
			if(testPsicomInput.puntuacionINVP != null){
				columnaUpdate = columnaUpdate + "puntuacionINVP = '"+testPsicomInput.puntuacionINVP+"', ";
			}
			if(testPsicomInput.quienIntegro != null){
				columnaUpdate = columnaUpdate + "quienIntegro = '"+testPsicomInput.quienIntegro+"', ";
			}
			if(testPsicomInput.quienRealizoEntrevista != null){
				columnaUpdate = columnaUpdate + "quienRealizoEntrevista = '"+testPsicomInput.quienRealizoEntrevista+"', ";
			}
			if(testPsicomInput.resumenSalud != null){
				columnaUpdate = columnaUpdate + "resumenSalud = '"+testPsicomInput.resumenSalud+"', ";
			}
			if(testPsicomInput.fuentesInfluyeronDesicion != null){
				columnaUpdate = columnaUpdate + "fuentesInfluyeronDesicion = '"+testPsicomInput.fuentesInfluyeronDesicion+"', ";
			}
			if(testPsicomInput.personasInfluyeronDesicion != null){
				columnaUpdate = columnaUpdate + "personasInfluyeronDesicion = '"+testPsicomInput.personasInfluyeronDesicion+"', ";
			}
			if(testPsicomInput.problemasSaludAtencionContinua != null){
				columnaUpdate = columnaUpdate + "problemasSaludAtencionContinua = '"+testPsicomInput.problemasSaludAtencionContinua+"', ";
			}
			if(testPsicomInput.tipoDiscapacidad != null){
				columnaUpdate = columnaUpdate + "tipoDiscapacidad = '"+testPsicomInput.tipoDiscapacidad+"', ";
			}
			
			/*if(testPsicomInput.fuentesInfluyeronDesicion != null && testPsicomInput.fuentesInfluyeronDesicion != ""){
				columnaUpdate = columnaUpdate + "fuentesInfluyeronDesicion = '"+testPsicomInput.fuentesInfluyeronDesicion+"', ";
			}*/
			/*if(testPsicomInput.personasInfluyeronDesicion != null && testPsicomInput.personasInfluyeronDesicion != ""){
				columnaUpdate = columnaUpdate + "personasInfluyeronDesicion = '"+testPsicomInput.personasInfluyeronDesicion+"', ";
			}*/
			if(testPsicomInput.problemasSaludAtencionContinua != null && testPsicomInput.problemasSaludAtencionContinua != ""){
				columnaUpdate = columnaUpdate + "problemasSaludAtencionContinua = '"+testPsicomInput.problemasSaludAtencionContinua+"', ";
			}
			if(testPsicomInput.tipoDiscapacidad != null && testPsicomInput.tipoDiscapacidad != ""){
				columnaUpdate = columnaUpdate + "tipoDiscapacidad = '"+testPsicomInput.tipoDiscapacidad+"', ";
			}
			if(testPsicomInput.hasRecibidoAlgunaTerapia != null && testPsicomInput.hasRecibidoAlgunaTerapia != ""){
				columnaUpdate = columnaUpdate + "hasRecibidoAlgunaTerapia = '"+testPsicomInput.hasRecibidoAlgunaTerapia+"', ";
			}
			
					
			//OBJ
			if (testPsicomInput.catPersonaSaludable != null && testPsicomInput.catPersonaSaludable != "") {
				catPersonaSaludable = testPsicomInput.catPersonaSaludable
				assert catPersonaSaludable instanceof Map;
				if (catPersonaSaludable.persistenceId != null && catPersonaSaludable.persistenceId != "") {
					columnaUpdate = columnaUpdate + "catpersonasaludable_pid = " + catPersonaSaludable.persistenceId + ", ";
				}
			}
			if (testPsicomInput.catVivesEstadoDiscapacidad != null && testPsicomInput.catVivesEstadoDiscapacidad != "") {
				catVivesEstadoDiscapacidad = testPsicomInput.catVivesEstadoDiscapacidad
				assert catVivesEstadoDiscapacidad instanceof Map;
				if (catVivesEstadoDiscapacidad.persistenceId != null && catVivesEstadoDiscapacidad.persistenceId != "") {
					columnaUpdate = columnaUpdate + "catvivesestadodiscapacidad_pid = " + catVivesEstadoDiscapacidad.persistenceId + ", ";
				}
			}
			if (testPsicomInput.catProblemaSaludAtencionContinua != null && testPsicomInput.catProblemaSaludAtencionContinua != "") {
				catProblemaSaludAtencionContinua = testPsicomInput.catProblemaSaludAtencionContinua
				assert catProblemaSaludAtencionContinua instanceof Map;
				if (catProblemaSaludAtencionContinua.persistenceId != null && catProblemaSaludAtencionContinua.persistenceId != "") {
					columnaUpdate = columnaUpdate + "catproblemasaludatencionco_pid = " + catProblemaSaludAtencionContinua.persistenceId + ", ";
				}
			}
			if (testPsicomInput.catRequieresAsistencia != null && testPsicomInput.catRequieresAsistencia != "") {
				catRequieresAsistencia = testPsicomInput.catRequieresAsistencia
				assert catRequieresAsistencia instanceof Map;
				if (catRequieresAsistencia.persistenceId != null && catRequieresAsistencia.persistenceId != "") {
					columnaUpdate = columnaUpdate + "catrequieresasistencia_pid = " + catRequieresAsistencia.persistenceId + ", ";
				}
			}
			
			if (testPsicomInput.catRecibidoTerapia != null && testPsicomInput.catRecibidoTerapia != "") {
				catRecibidoTerapia = testPsicomInput.catRecibidoTerapia;
				assert catRecibidoTerapia instanceof Map;
				if (catRecibidoTerapia.persistenceId != null && catRecibidoTerapia.persistenceId != "") {
					columnaUpdate = columnaUpdate + "catRecibidoTerapia_pid = " + catRecibidoTerapia.persistenceId + ", ";
				}
			}
			
			strError += " |columnaUpdate :: " + columnaUpdate + " ||| ";
			if(columnaUpdate.length() > 0) {
				columnaUpdate = columnaUpdate.substring(0, columnaUpdate.length() - 2);
			}
			
			List<String> data = new ArrayList<String>();
			data.add(Statements.UPDATE_TESTPSICOMETRICO.replace("[COLUMNA]", columnaUpdate));
			
			result.setData(data);
			result.setError_info(strError);
			result.setSuccess(true);
		} catch(Exception e) {
			LOGGER.error "e: "+e.getMessage();
			result.setError_info(strError);
			result.setSuccess(false);
			result.setError(e.getMessage());
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e2) {
			LOGGER.error "e2: "+e2.getMessage();
			result.setError_info(strError);
			result.setSuccess(false);
			result.setError(e2.getMessage());
			e2.printStackTrace();
		}
		
		return result
	}
		
	public Result insertUpdatePsicometricoV2(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
				
		String strError = "";
		String columnaUpdate = "";
		String columnaInsert = "";
		String valorInsert = "";
		String consultaInsert = "";
		String consultaUpdate = "";
		String caseId = "";
			
		def catPersonaSaludable = null;
		def catVivesEstadoDiscapacidad = null;
		def catProblemaSaludAtencionContinua = null;
		def catRequieresAsistencia = null;

		def catParentezco = null;
		def vive = null;
		def carrera = null;
		
		def rasgo = null;
		def calificacion = null;
		
		def catTipoTerapia = null;
		def catRecibidoTerapia = null;
		
		Boolean closeCon = false;
		
		Integer testPsicomInput_persistenceId = 0;
		Integer contador = 0;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			assert object instanceof Map;
			
			def testPsicomInput = object.testPsicomInput;
			assert testPsicomInput instanceof Map;
			
			if(object.caseId != null && object.caseId != ""){
				caseId=object.caseId;
			}
						
			consultaInsert = Statements.INSERT_TESTPSICOMETRICO_V2;
			
			Result resultConsultaUpdate = generateUpdateResult(testPsicomInput);
			if(!resultConsultaUpdate.isSuccess()) {
				throw new Exception("Error en el update :: " + resultConsultaUpdate.getError() + " || " + resultConsultaUpdate.getError_info());
			} else {
				consultaUpdate = resultConsultaUpdate.getData().get(0);
				
			}
			
			closeCon = validarConexion();
			
			/*========================================================TEST PSICOMETRICO ACCIONES========================================================*/
			pstm = con.prepareStatement(Statements.SELECT_TESTPSICOMETRICO_BY_CASEID_V2+" AND countRechazo = "+ testPsicomInput.countRechazo);
			pstm.setString(1, caseId);
			rs = pstm.executeQuery();
			if(rs.next()) {
				strError+=consultaUpdate;
				pstm = con.prepareStatement(consultaUpdate);
				pstm.setString(1, caseId);
				if (testPsicomInput.countRechazo != null) {
			    try {
			        pstm.setLong(2, Long.parseLong(testPsicomInput.countRechazo.toString()));
			    } catch (NumberFormatException e) {
			        pstm.setNull(2, Types.INTEGER);
			    }
			} else {
			    pstm.setNull(2, Types.INTEGER);
			}
				pstm.executeUpdate();
			}
			else {
				if (testPsicomInput.catPersonaSaludable != null && testPsicomInput.catPersonaSaludable != "") {
					catPersonaSaludable = testPsicomInput.catPersonaSaludable
					assert catPersonaSaludable instanceof Map;
				}
				if (testPsicomInput.catVivesEstadoDiscapacidad != null && testPsicomInput.catVivesEstadoDiscapacidad != "") {
					catVivesEstadoDiscapacidad = testPsicomInput.catVivesEstadoDiscapacidad
					assert catVivesEstadoDiscapacidad instanceof Map;
				}
				if (testPsicomInput.catProblemaSaludAtencionContinua != null && testPsicomInput.catProblemaSaludAtencionContinua != "") {
					catProblemaSaludAtencionContinua = testPsicomInput.catProblemaSaludAtencionContinua
					assert catProblemaSaludAtencionContinua instanceof Map;
				}
				if (testPsicomInput.catRequieresAsistencia != null && testPsicomInput.catRequieresAsistencia != "") {
					catRequieresAsistencia = testPsicomInput.catRequieresAsistencia
					assert catRequieresAsistencia instanceof Map;
				}
				
				strError += " comienza el insert | "
			
				pstm = con.prepareStatement(consultaInsert);
				pstm.setString(1, (testPsicomInput.ajusteEfectivo != null && testPsicomInput.ajusteEfectivo != "") ? testPsicomInput.ajusteEfectivo : "");
				pstm.setString(2, (testPsicomInput.ajusteEscolarPrevio != null && testPsicomInput.ajusteEscolarPrevio != "") ? testPsicomInput.ajusteEscolarPrevio : "");
				pstm.setString(3, (testPsicomInput.ajusteExistencial != null && testPsicomInput.ajusteExistencial != "") ? testPsicomInput.ajusteExistencial : "");
				pstm.setString(4, (testPsicomInput.ajusteMedioFamiliar != null && testPsicomInput.ajusteMedioFamiliar != "") ? testPsicomInput.ajusteMedioFamiliar : "");
				pstm.setString(5, (testPsicomInput.ajusteMedioSocial != null && testPsicomInput.ajusteMedioSocial != "") ? testPsicomInput.ajusteMedioSocial : "");
				pstm.setString(6, (testPsicomInput.ajusteReligioso != null && testPsicomInput.ajusteReligioso != "") ? testPsicomInput.ajusteReligioso : "");
				pstm.setString(7, (testPsicomInput.califAjusteAfectivo != null && testPsicomInput.califAjusteAfectivo != "") ? testPsicomInput.califAjusteAfectivo : "");
				pstm.setString(8, (testPsicomInput.califAjusteEscolarPrevio != null && testPsicomInput.califAjusteEscolarPrevio != "") ? testPsicomInput.califAjusteEscolarPrevio : "");
				pstm.setString(9, (testPsicomInput.califAjusteExistencial != null && testPsicomInput.califAjusteExistencial != "") ? testPsicomInput.califAjusteExistencial : "");
				pstm.setString(10, (testPsicomInput.califAjusteMedioFamiliar != null && testPsicomInput.califAjusteMedioFamiliar != "") ? testPsicomInput.califAjusteMedioFamiliar : "");
				pstm.setString(11, (testPsicomInput.califAjusteMedioSocial != null && testPsicomInput.califAjusteMedioSocial != "") ? testPsicomInput.califAjusteMedioSocial : "");
				pstm.setString(12, (testPsicomInput.califAjusteReligioso != null && testPsicomInput.califAjusteReligioso != "") ? testPsicomInput.califAjusteReligioso : "");
				pstm.setString(13, caseId);
				pstm.setString(14, (testPsicomInput.conclusioneINVP != null && testPsicomInput.conclusioneINVP != "") ? testPsicomInput.conclusioneINVP : "");
				if (testPsicomInput.fechaEntrevista != null && !testPsicomInput.fechaEntrevista.isEmpty()) {
				    pstm.setString(15, testPsicomInput.fechaEntrevista);
				} else {
				    pstm.setNull(15, Types.TIMESTAMP);
				}
				if (testPsicomInput.finalizado != null) {
				    // Si no es nulo, verifica su valor
				    if (testPsicomInput.finalizado == 1) {
				        // Si es igual a 1, establece true en la sentencia SQL
				        pstm.setBoolean(16, true);
				    } else {
				        // De lo contrario, establece false en la sentencia SQL
				        pstm.setBoolean(16, false);
				    }
				} else {
				    // Si es nulo, establece el parámetro como nulo
				    pstm.setNull(16, Types.BOOLEAN);
				}
				pstm.setString(17, (testPsicomInput.hasParticipadoActividadesAyuda != null && testPsicomInput.hasParticipadoActividadesAyuda != "") ? testPsicomInput.hasParticipadoActividadesAyuda : "");
				pstm.setString(18, (testPsicomInput.interpretacionINVP != null && testPsicomInput.interpretacionINVP != "") ? testPsicomInput.interpretacionINVP : "");
				pstm.setString(19, (testPsicomInput.otroTipoAsistencia != null && testPsicomInput.otroTipoAsistencia != "") ? testPsicomInput.otroTipoAsistencia : "");
				pstm.setString(20, (testPsicomInput.participacionActividadesVoluntaria != null && testPsicomInput.participacionActividadesVoluntaria != "") ? testPsicomInput.participacionActividadesVoluntaria : "");
				pstm.setInt(21, 0);
				if (testPsicomInput.puntuacionINVP != null && !testPsicomInput.puntuacionINVP.isEmpty()) {
				    try {
				        pstm.setInt(22, Integer.parseInt(testPsicomInput.puntuacionINVP));
				    } catch (NumberFormatException e) {
				        pstm.setNull(22, Types.INTEGER);
				    }
				} else {
				    pstm.setNull(22, Types.INTEGER);
				}
				pstm.setString(23, (testPsicomInput.quienIntegro != null && testPsicomInput.quienIntegro != "") ? testPsicomInput.quienIntegro : "");
				pstm.setString(24, (testPsicomInput.quienRealizoEntrevista != null && testPsicomInput.quienRealizoEntrevista != "") ? testPsicomInput.quienRealizoEntrevista : "");
				pstm.setString(25, (testPsicomInput.resumenSalud != null && testPsicomInput.resumenSalud != "") ? testPsicomInput.resumenSalud : "");
				
				if (testPsicomInput.catPersonaSaludable != null) {
				    if (testPsicomInput.catPersonaSaludable.isEmpty()) {
				        pstm.setNull(26, Types.INTEGER);
				    } else {
				        pstm.setInt(26, catPersonaSaludable.persistenceId);
				    }
				} else {
				    pstm.setNull(26, Types.INTEGER);
				}
				
				if (testPsicomInput.catProblemaSaludAtencionContinua != null) {
				    if (testPsicomInput.catProblemaSaludAtencionContinua.isEmpty()) {
				        pstm.setNull(27, Types.INTEGER);
				    } else {
				        pstm.setInt(27, catProblemaSaludAtencionContinua.persistenceId);
				    }
				} else {
				    pstm.setNull(27, Types.INTEGER);
				}
				
				if (testPsicomInput.catRequieresAsistencia != null) {
				    if (testPsicomInput.catRequieresAsistencia.isEmpty()) {
				        pstm.setNull(28, Types.INTEGER);
				    } else {
				        pstm.setInt(28, catRequieresAsistencia.persistenceId);
				    }
				} else {
				    pstm.setNull(28, Types.INTEGER);
				}
				
				if (testPsicomInput.catVivesEstadoDiscapacidad != null) {
				    if (testPsicomInput.catVivesEstadoDiscapacidad.isEmpty()) {
				        pstm.setNull(29, Types.INTEGER);
				    } else {
				        pstm.setInt(29, catVivesEstadoDiscapacidad.persistenceId);
				    }
				} else {
				    pstm.setNull(29, Types.INTEGER);
				}
				
				pstm.setString(30, (testPsicomInput.fuentesInfluyeronDesicion != null && testPsicomInput.fuentesInfluyeronDesicion != "") ? testPsicomInput.fuentesInfluyeronDesicion : "");
				pstm.setString(31, (testPsicomInput.personasInfluyeronDesicion != null && testPsicomInput.personasInfluyeronDesicion != "") ? testPsicomInput.personasInfluyeronDesicion : "");
				pstm.setString(32, (testPsicomInput.problemasSaludAtencionContinua != null && testPsicomInput.problemasSaludAtencionContinua != "") ? testPsicomInput.problemasSaludAtencionContinua : "");
				pstm.setString(33, (testPsicomInput.tipoDiscapacidad != null && testPsicomInput.tipoDiscapacidad != "") ? testPsicomInput.tipoDiscapacidad : "");
				pstm.setString(34, (testPsicomInput.hasRecibidoAlgunaTerapia != null && testPsicomInput.hasRecibidoAlgunaTerapia != "") ? testPsicomInput.hasRecibidoAlgunaTerapia : "");
				if (testPsicomInput.countRechazo != null) {
				    try {
				        pstm.setLong(35, Long.parseLong(testPsicomInput.countRechazo.toString()));
				    } catch (NumberFormatException e) {
				        pstm.setNull(35, Types.INTEGER);
				    }
				} else {
				    pstm.setNull(35, Types.INTEGER);
				}
				//pstm.setLong(36, (testPsicomInput.sesion_pid != null) ? testPsicomInput.sesion_pid : 0 );
				if (testPsicomInput.sesion_pid == null || Long.parseLong(testPsicomInput.sesion_pid.toString()) == 0) {
				    pstm.setNull(36, Types.INTEGER);
				} else {
				    try {
				        pstm.setLong(36, Long.parseLong(testPsicomInput.sesion_pid.toString()));
				    } catch (NumberFormatException e) {
				        pstm.setNull(36, Types.INTEGER);
				    }
				}
				pstm.executeUpdate();
			}
			
			pstm = con.prepareStatement(Statements.SELECT_TESTPSICOMETRICO_BY_CASEID+" AND countRechazo = "+ testPsicomInput.countRechazo);
			pstm.setString(1, caseId);
			rs = pstm.executeQuery();
			if(rs.next()) {
				testPsicomInput_persistenceId = rs.getInt("persistenceid");
			}
			
			/*==============================================TESTPSICOMETRICO_RELATIVOS INICIO==============================================*/
			strError += "Relativos | "
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_RELATIVOS+" AND countRechazo = "+ testPsicomInput.countRechazo);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomRelativosInput != null && object.testPsicomRelativosInput != "") {
					assert object.testPsicomRelativosInput instanceof List;
					for (def row: object.testPsicomRelativosInput) {
						catParentezco = row.catParentezco;
						vive = row.vive;
						
						assert catParentezco instanceof Map;
						if(vive != null) {
							assert vive instanceof Map;
						}
						
						if (catParentezco.persistenceId != null && catParentezco.persistenceId != "") {
							strError = strError + " | contador: " + (8 + " - " + catParentezco.persistenceId);
						} else {
							strError = strError + " | contador: " + (8 + " - Types.INTEGER");
						}
						if(vive != null ) {
							if (vive.persistenceId != null && vive.persistenceId != "") {
								strError = strError + " | contador: " + (9 + " - " + catParentezco.persistenceId);
							} else {
								strError = strError + " | contador: " + (9 + " - Types.INTEGER");
							}
						} else {
							strError = strError + " | contador: " + (9 + " - Types.INTEGER");
						}
						
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_RELATIVOS);
						pstm.setString(1, (row.nombre !=null && row.nombre !="") ? row.nombre : "");
						pstm.setString(2, (row.apellidos !=null && row.apellidos !="") ? row.apellidos : "");
						pstm.setString(3, (row.empresaTrabaja !=null && row.empresaTrabaja !="") ? row.empresaTrabaja : "");
						pstm.setString(4, (row.otroParentesco !=null && row.otroParentesco !="") ? row.otroParentesco : "");
						pstm.setString(5, caseId);
						pstm.setBoolean(6, (row.jubilado !=null && row.jubilado !="") ? row.jubilado : false);
						pstm.setBoolean(7, (row.vencido !=null && row.vencido !="") ? row.vencido : false);
						
						if(catParentezco.persistenceId != null && catParentezco.persistenceId != ""){
							pstm.setInt(8, catParentezco.persistenceId);
						}
						else {
							pstm.setNull(8, Types.INTEGER);
						}
						
						if(vive != null ) {
							if(vive.persistenceId != null && vive.persistenceId != ""  && vive.persistenceId != "0" && vive.persistenceId != 0){
								pstm.setInt(9, vive.persistenceId);
							}
							else {
								pstm.setNull(9, Types.INTEGER);
							}
						} else {
							pstm.setNull(9, Types.INTEGER);
						}
						
						pstm.setLong(10, testPsicomInput.countRechazo)
						
						
						
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			if(testPsicomInput.puntuacionINVP != null) {
				pstm = con.prepareStatement(Statements.UPDATE_PUNTUACION_INVP);
				pstm.setString(1,testPsicomInput.puntuacionINVP+"")
				pstm.setString(2, caseId);
				pstm.setString(3, testPsicomInput.sesion_pid+"");
				pstm.executeUpdate();
			}
			/*==============================================TESTPSICOMETRICO_RELATIVOS FIN==============================================*/
			
			/*==============================================TESTPSICOMETRICO_CUSTOSRECOMEND INICIO==============================================*/
			strError += "Cursos reocmendados | "
			contador = 0;
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_CUSTOSRECOMEND);
			pstm.setInt(1, testPsicomInput_persistenceId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (testPsicomInput.custosRecomendados != null && testPsicomInput.custosRecomendados != "") {
					assert testPsicomInput.custosRecomendados instanceof List;
					for (def row: testPsicomInput.custosRecomendados) {
						if(row.persistenceId != null && row.persistenceId != ""){
							pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_CUSTOSRECOMEND);
							pstm.setInt(1, testPsicomInput_persistenceId);
							pstm.setInt(2, row.persistenceId);
							pstm.setInt(3, contador);
							pstm.executeUpdate();
							contador++;
							strError = strError + " | contador: " + contador;
						}
					}
				}
			}
			/*==============================================TESTPSICOMETRICO_CUSTOSRECOMEND FIN==============================================*/
			
			/*========================================================TEST PSICOMETRICO RASGOS ACCIONES========================================================*/
			strError += "Rasgos | "
			if(object.testPsicomRasgosInput != null && object.testPsicomRasgosInput != "" && object.testPsicomRasgosInput?.size() > 0) {
				contador = 0;
				pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_RASGOS+" AND countRechazo = "+ testPsicomInput.countRechazo);
				pstm.setString(1, caseId);
				pstm.executeUpdate();
				strError = strError + " | " + "-------------------------------------------";
				if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
					strError = strError + " | " + "-------------------------------------------";
					if (object.testPsicomRasgosInput != null && object.testPsicomRasgosInput != "") {
						assert object.testPsicomRasgosInput instanceof List;
						for (def row: object.testPsicomRasgosInput) {
							rasgo = row.rasgo;
							calificacion = row.calificacion;
							
							assert rasgo instanceof Map;
							if(calificacion != null ) {
								assert calificacion instanceof Map;
							}
													
							pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_RASGOS);
							if(rasgo.persistenceId != null && rasgo.persistenceId != ""){
								pstm.setInt(1, rasgo.persistenceId);
							}
							else {
								pstm.setNull(1, Types.INTEGER);
							}
							if(calificacion != null) {
								if(calificacion.persistenceId != null && calificacion.persistenceId != ""){
									pstm.setInt(2, calificacion.persistenceId);
								}
								else {
									pstm.setNull(2, Types.INTEGER);
								}
							} else {
								pstm.setNull(2, Types.INTEGER);
							}
							pstm.setString(3, caseId);
							pstm.setBoolean(4, false);
							pstm.setLong(5, testPsicomInput.countRechazo);
							
							pstm.executeUpdate();
							contador++;
						}
					}
				}
			} else {
				strError += " | Rasgos vacios "
			}
			
			/*========================================================TEST PSICOMETRICO RASGOS FIN========================================================*/
			
			/*========================================================TEST PSICOMETRICO CARRERAS REC ACCIONES========================================================*/
			strError += "Carreras recomendadas | "
			contador = 0;
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_CARRERASREC+" AND countRechazo = "+ testPsicomInput.countRechazo);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomCarrerasRecInput != null && object.testPsicomCarrerasRecInput != "") {
					assert object.testPsicomCarrerasRecInput instanceof List;
					for (def row: object.testPsicomCarrerasRecInput) {
						carrera = row.carrera;
						
						assert carrera instanceof Map;
												
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_CARRERASREC);
						pstm.setInt(1, (row.orden !=null && row.orden !="") ? row.orden : "");
						pstm.setString(2, (row.comentarios !=null && row.comentarios !="") ? row.comentarios : "");
						pstm.setString(3, caseId);
						pstm.setBoolean(4, (row.vencido !=null && row.vencido !="") ? row.vencido : "");
						
						if(carrera.persistenceId != null && carrera.persistenceId != ""){
							pstm.setInt(5, carrera.persistenceId);
						}
						else {
							pstm.setNull(5, Types.INTEGER);
						}
						pstm.setBoolean(6, testPsicomInput.countRechazo);
						
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			/*========================================================TEST PSICOMETRICO CARRERAS REC FIN========================================================*/
			
			/*========================================================TEST PSICOMETRICO OBSERVACIONES ACCIONES========================================================*/
			strError += "Observaciones | "
			contador = 0;
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_OBSERVACIONES+" AND countRechazo = "+ testPsicomInput.countRechazo);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomObservacionesInput != null && object.testPsicomObservacionesInput != "") {
					assert object.testPsicomObservacionesInput instanceof List;
					for (def row: object.testPsicomObservacionesInput) {
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_OBSERVACIONES);
						pstm.setString(1, (row.orden !=null && row.orden !="") ? row.orden.toString() : "");
						pstm.setString(2, (row.universidad !=null && row.universidad !="") ? row.universidad : "");
						pstm.setBoolean(3, (row.examen !=null && row.examen !="") ? row.examen : false);
						pstm.setBoolean(4, (row.admitido !=null && row.admitido !="") ? row.admitido : false);
						pstm.setBoolean(5, (row.vencido !=null && row.vencido !="") ? row.vencido : false);
						pstm.setInt(6, (row.porcentajeBeca !=null && row.porcentajeBeca !="") ? Integer.parseInt(row.porcentajeBeca) : 0);
						pstm.setInt(7, (row.porcentajeCredito !=null && row.porcentajeCredito !="") ? Integer.parseInt(row.porcentajeCredito) : 0);
						pstm.setString(8, caseId);
						pstm.setLong(9, testPsicomInput.countRechazo);
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			String idBanner=""
			pstm = con.prepareStatement("SELECT idbanner from detallesolicitud where caseid=?")
			pstm.setString(1, caseId)
			rs = pstm.executeQuery()
			if(rs.next()) {
				idBanner = rs.getString("idbanner")
			}
			
			/*if(closeCon && testPsicomInput.puntuacionINVP != null && testPsicomInput.puntuacionINVP != "" &&  testPsicomInput.fechaEntrevista !="") {
				String fecha = testPsicomInput.fechaEntrevista.substring(0,9)
				new DBConnect().closeObj(con, stm, rs, pstm);
				if(testPsicomInput.fechaEntrevista.substring(0,9).contains("/")) {
					SimpleDateFormat sdfd = new SimpleDateFormat("dd/MM/yyyy")
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
					fecha = sdf.format(sdfd.parse(testPsicomInput.fechaEntrevista.substring(0,9)))
				}
				resultado2=new BannerDAO().integracionBannerEthos(context, idBanner, "MMPI", testPsicomInput.puntuacionINVP+"",fecha)
				if(!resultado2.success) {
					
					strError = "fallo Ethos:" resultado2.error_info + strError;
				}
			}*/
			
			if (testPsicomInput.puntuacionINVP != null && !testPsicomInput.puntuacionINVP.isEmpty() && testPsicomInput.fechaEntrevista != null && !testPsicomInput.fechaEntrevista.isEmpty()) {
			    String fecha = testPsicomInput.fechaEntrevista.substring(0, 10);
			    
			    if (testPsicomInput.fechaEntrevista.substring(0, 10).contains("/")) {
			        fecha = testPsicomInput.fechaEntrevista.substring(6, 10) + "-" + testPsicomInput.fechaEntrevista.substring(3, 5) + "-" + testPsicomInput.fechaEntrevista.substring(0, 2);
			    }
			    
			    strError += "fecha:" + fecha;
			    
			    Result resultado2 = new Result();
			    resultado2 = integracionEthos(fecha, idBanner, "MMPI", testPsicomInput.puntuacionINVP + "", context);
				
				pstm = con.prepareStatement(Statements.GET_PRUEBAS_IDBANNER_IDSESION);
				pstm.setString(1, idBanner);
				pstm.setLong(2, Long.parseLong(testPsicomInput.sesion_pid));
				rs= pstm.executeQuery();
				String prueba = "", username2="";
				if(rs.next()) {
					prueba = rs.getString("prueba_pid");
					username2 = rs.getString("username");
				}
				
				pstm = con.prepareStatement("SELECT persistenceid from paseLista where username = ? and prueba_pid = ?");
				pstm.setString(1, username2);
				pstm.setLong(2, Long.parseLong(prueba));
				rs= pstm.executeQuery();
				String pl = '';
				if(rs.next()) {
					pl = rs.getString("persistenceid");
				}
				
				Result resultPaseLista = new Result();
				String jsdonPaseLista = "{\"prueba\":${prueba},\"username\":\"${username2}\",\"asistencia\":true,\"usuarioPaseLista\":\"Reporte OV\"}";
				if(pl.equals('')) {
					resultPaseLista = new SesionesDAO().insertPaseLista(jsdonPaseLista);					
				}else {
					resultPaseLista = new SesionesDAO().updatePaseLista(jsdonPaseLista);
				}
				strError += "INTEGRACION:"+resultado2.isSuccess()+"ERROR:"+resultado2.getError()+"ERROR_INFO:"+resultado2.getError_info();
				strError += " ||||JSON PASE LISTA ${jsdonPaseLista} |||| PASELISTA:"+resultPaseLista.isSuccess()+"ERROR:"+resultPaseLista.getError()+"ERROR_INFO:"+resultPaseLista.getError_info();
				
				if(resultPaseLista.isSuccess()){
					try {
						ProcessAPI processAPI = context.getApiClient().getProcessAPI();
						Map<String, Serializable> rows = new HashMap<String, Serializable>();
						rows.put("asistenciaEntrevista", true);
						processAPI.updateProcessDataInstances(caseId, rows);
					}catch (Exception e) {
						strError += '||| ERROR PROCESO '+e;
					}
					

				}
				
				
			}
			
			/*========================================================TEST PSICOMETRICO OBSERVACIONES ACCIONES========================================================*/
			resultado.setError_info(strError);
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result integracionEthos(String fecha,String idbanner,String nombreDato,String dato,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
		try {			
			resultado = new BannerDAO().integracionBannerEthos(context, idbanner, nombreDato,dato,fecha)
			errorLog += "INTEGRACION SUBIDA "+nombreDato +": "+resultado.isSuccess()+",ERROR:"+resultado.getError()+",ERROR_INFO:"+resultado.getError_info();
			
			resultado.setSuccess(true);
			
			
		}catch(Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}
		
		return resultado;
	}
	
public Result getPsicometricoCompleto(String caseId, Long intentos,RestAPIContext context) {
    String errorinfo="";
    try {
    Result resultado = new Result();
    Boolean closeCon = false;
    List<?> rows = new ArrayList<?>();
    String strError = "";
    Map<String, Object> objetoCompleto = new LinkedHashMap<String, Object>();
    Map<String, Object> testPsicomInput = new LinkedHashMap<String, Object>();
    List<Map<String, Object>> testPsicomObservacionesInput = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> testPsicomRelativosInput = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> testPsicomRasgosInput = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> custosRecomendados = new ArrayList<Map<String, Object>>();
    errorinfo+="[1] Preparacion de variables "
    try {
        errorinfo+="[2] AutodescripcionDAO.findByCaseId "
        closeCon = validarConexion();
        errorinfo+="[3] Conexion "
        pstm = con.prepareStatement(Statements.SELECT_TESTPSICOMETRICO_BY_CASEID_V2+" AND countRechazo Is Not Distinct From "+intentos);
        pstm.setString(1, caseId);
        rs = pstm.executeQuery();
        
        while(rs.next()) {
            testPsicomInput = new LinkedHashMap<String, Object>();
            testPsicomInput.put("fechaEntrevista", rs.getString("fechaEntrevista"));
            testPsicomInput.put("ajusteMedioFamiliar", rs.getString("ajusteMedioFamiliar"));
            testPsicomInput.put("califAjusteMedioFamiliar", rs.getString("califAjusteMedioFamiliar"));
            testPsicomInput.put("ajusteEscolarPrevio", rs.getString("ajusteEscolarPrevio"));
            testPsicomInput.put("califAjusteEscolarPrevio", rs.getString("califAjusteEscolarPrevio"));
            testPsicomInput.put("ajusteMedioSocial", rs.getString("ajusteMedioSocial"));
            testPsicomInput.put("califAjusteMedioSocial", rs.getString("califAjusteMedioSocial"));
            testPsicomInput.put("ajusteEfectivo", rs.getString("ajusteEfectivo"));
            testPsicomInput.put("califAjusteAfectivo", rs.getString("califAjusteAfectivo"));
            testPsicomInput.put("ajusteReligioso", rs.getString("ajusteReligioso"));
            testPsicomInput.put("califAjusteReligioso", rs.getString("califAjusteReligioso"));
            testPsicomInput.put("ajusteExistencial", rs.getString("ajusteExistencial"));
            testPsicomInput.put("califAjusteExistencial", rs.getString("califAjusteExistencial"));
            testPsicomInput.put("interpretacionINVP", rs.getString("interpretacionINVP"));
            testPsicomInput.put("puntuacionINVP", rs.getString("puntuacionINVP"));
            testPsicomInput.put("resumenSalud", rs.getString("resumenSalud"));
            testPsicomInput.put("finalizado", rs.getBoolean("finalizado"));
            testPsicomInput.put("otroTipoAsistencia", rs.getString("otroTipoAsistencia"));
            testPsicomInput.put("quienRealizoEntrevista", rs.getString("quienRealizoEntrevista"));
            testPsicomInput.put("quienIntegro", rs.getString("quienIntegro"));
            testPsicomInput.put("hasParticipadoActividadesAyuda", rs.getString("hasParticipadoActividadesAyuda"));
            testPsicomInput.put("participacionActividadesVoluntaria", rs.getString("participacionActividadesVoluntaria"));
            testPsicomInput.put("fuentesInfluyeronDesicion", rs.getString("fuentesInfluyeronDesicion"));
            testPsicomInput.put("personasInfluyeronDesicion", rs.getString("personasInfluyeronDesicion"));
            testPsicomInput.put("problemasSaludAtencionContinua", rs.getString("problemasSaludAtencionContinua"));
            testPsicomInput.put("tipoDiscapacidad", rs.getString("tipoDiscapacidad"));
            testPsicomInput.put("catrecibidoterapia_pid", rs.getLong("catrecibidoterapia_pid"));
            testPsicomInput.put("catproblemasaludatencionco_pid", rs.getLong("catproblemasaludatencionco_pid"));
            testPsicomInput.put("catrequieresasistencia_pid", rs.getLong("catrequieresasistencia_pid"));
            testPsicomInput.put("catvivesestadodiscapacidad_pid", rs.getLong("catvivesestadodiscapacidad_pid"));
            testPsicomInput.put("catpersonasaludable_pid", rs.getLong("catrecibidoterapia_pid"));
			testPsicomInput.put("conclusioneINVP", rs.getString("conclusioneINVP"));
			
            
        }
        errorinfo+="[4] testPsicomInput lleno "
        
        objetoCompleto.put("caseId", caseId);
		objetoCompleto.put("quienRealizoEntrevista", testPsicomInput.get("quienRealizoEntrevista"));
		objetoCompleto.put("quienIntegro", testPsicomInput.get("quienIntegro"));
		objetoCompleto.put("puntuacionINVP", testPsicomInput.get("puntuacionINVP"));
		objetoCompleto.put("fechaEntrevista", testPsicomInput.get("fechaEntrevista"));
		objetoCompleto.put("hasParticipadoActividadesAyuda", testPsicomInput.get("hasParticipadoActividadesAyuda"));
		objetoCompleto.put("participacionActividadesVoluntaria", testPsicomInput.get("participacionActividadesVoluntaria"));
		objetoCompleto.put("ajusteMedioFamiliar", testPsicomInput.get("ajusteMedioFamiliar"));
		objetoCompleto.put("ajusteEscolarPrevio", testPsicomInput.get("ajusteEscolarPrevio"));
		objetoCompleto.put("califAjusteMedioFamiliar", testPsicomInput.get("califAjusteMedioFamiliar"));
		objetoCompleto.put("ajusteMedioSocial", testPsicomInput.get("ajusteMedioSocial"));
		objetoCompleto.put("califAjusteEscolarPrevio", testPsicomInput.get("califAjusteEscolarPrevio"));
		objetoCompleto.put("califAjusteMedioSocial", testPsicomInput.get("califAjusteMedioSocial"));
		objetoCompleto.put("ajusteEfectivo", testPsicomInput.get("ajusteEfectivo"));
		objetoCompleto.put("califAjusteAfectivo", testPsicomInput.get("califAjusteAfectivo"));
		objetoCompleto.put("ajusteReligioso", testPsicomInput.get("ajusteReligioso"));
		objetoCompleto.put("califAjusteReligioso", testPsicomInput.get("califAjusteReligioso"));
		objetoCompleto.put("ajusteExistencial", testPsicomInput.get("ajusteExistencial"));
		objetoCompleto.put("califAjusteExistencial", testPsicomInput.get("califAjusteExistencial"));
		objetoCompleto.put("resumenSalud", testPsicomInput.get("resumenSalud"));
		objetoCompleto.put("interpretacionINVP", testPsicomInput.get("interpretacionINVP"));
		objetoCompleto.put("conclusioneINVP", testPsicomInput.get("conclusioneINVP"));
		
        pstm = con.prepareStatement("SELECT distinct ras.*, paren.descripcion as parentezco, vive.descripcion as vive, vive.persistenceid as vivepersistenceid, paren.persistenceid as parenpersistenceid FROM TestPsicometricoRelativos ras inner join catparentesco paren on paren.persistenceid=ras.catparentezco_pid inner join padrestutor pt on pt.catparentezco_pid=paren.persistenceid inner join catvive vive on vive.persistenceid=ras.vive_pid where ras.caseid=? AND countRechazo Is Not Distinct From "+intentos);
			pstm.setString(1, caseId)
			rs= pstm.executeQuery();
			
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				Map<String, Object> catParentezco = new LinkedHashMap<String, Object>();
				Map<String, Object> vive = new LinkedHashMap<String, Object>();
				catParentezco.put("descripcion", rs.getString("parentezco"))
				catParentezco.put("persistenceId", rs.getLong("parenpersistenceid"))
				vive.put("persistenceId", rs.getLong("vivepersistenceid"))
				vive.put("descripcion", rs.getString("vive")==null?"No":rs.getString("vive"))
				columns.put("catParentezco",catParentezco)
				columns.put("vive",vive)
				columns.put("nombre", rs.getString("nombre"));
				columns.put("apellidos", rs.getString("apellidos"));
				columns.put("empresaTrabaja", rs.getString("empresaTrabaja"));
				columns.put("jubilado", rs.getBoolean("jubilado"));
				columns.put("persistenceId", rs.getLong("persistenceId"));
				columns.put("persistenceVersion", rs.getLong("persistenceversion"));
				columns.put("vencido", rs.getBoolean("vencido"));
				columns.put("caseId", rs.getLong("caseId"));
				testPsicomRelativosInput.add(columns)
			}
			
        errorinfo+="[5] TestPsicometricoRelativosDAO.findByCaseId "
			objetoCompleto.put("testPsicomRelativosInput", testPsicomRelativosInput);
		
        

        pstm = con.prepareStatement("SELECT ras.*, rasgo.descripcion rasgodescripcion, calif.descripcion califdescripcion FROM TestPsicometricoRasgos ras inner join catrasgoscalif calif on calif.persistenceid=ras.calificacion_pid inner join catrasgosobservados rasgo on rasgo.persistenceid=ras.rasgo_pid where ras.caseid=? AND countRechazo Is Not Distinct From "+intentos);
			pstm.setString(1, caseId)
			rs= pstm.executeQuery();
			
			metaData = rs.getMetaData();
			columnCount = metaData.getColumnCount();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				Map<String, Object> rasgo = new LinkedHashMap<String, Object>();
				Map<String, Object> calificacion = new LinkedHashMap<String, Object>();
				rasgo.put("persistenceId",rs.getLong("rasgo_pid"))
				rasgo.put("persistenceId_string",rs.getString("rasgo_pid"))
				rasgo.put("descripcion",rs.getString("rasgodescripcion"))
				
				calificacion.put("persistenceId",rs.getLong("calificacion_pid"))
				calificacion.put("persistenceId_string",rs.getString("calificacion_pid"))
				calificacion.put("descripcion",rs.getString("califdescripcion"))
				columns.put("rasgo", rasgo);
				columns.put("calificacion", calificacion);
				columns.put("persistenceId", rs.getLong("persistenceId"));
				columns.put("persistenceVersion", rs.getLong("persistenceversion"));
				columns.put("vencido", rs.getBoolean("vencido"));
				columns.put("caseId", rs.getLong("caseId"));
				testPsicomRasgosInput.add(columns)
			}
			pstm = con.prepareStatement("SELECT * FROM TestPsicometricoObservaciones where caseid=? AND countRechazo Is Not Distinct From "+intentos);
			pstm.setString(1, caseId)
			rs= pstm.executeQuery();
			metaData = rs.getMetaData();
			columnCount = metaData.getColumnCount();
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).equals("admitido") || metaData.getColumnLabel(i).equals("examen")|| metaData.getColumnLabel(i).equals("vencido")) {
						columns.put(metaData.getColumnLabel(i), rs.getBoolean(i));
					}else if(metaData.getColumnLabel(i).equals("porcentajebeca")) {
						columns.put("porcentajeBeca", rs.getString(i));
					}else if(metaData.getColumnLabel(i).equals("porcentajecredito")) {
						columns.put("porcentajeCredito", rs.getString(i));
					} else {
						columns.put(metaData.getColumnLabel(i), rs.getString(i));
					}
				}
				testPsicomObservacionesInput.add(columns)
			}
			Integer testPsicomInput_persistenceId=0
			pstm = con.prepareStatement(Statements.SELECT_TESTPSICOMETRICO_BY_CASEID+"AND countRechazo Is Not Distinct From "+intentos);
			pstm.setString(1, caseId);
			rs = pstm.executeQuery();
			if(rs.next()) {
				testPsicomInput_persistenceId = rs.getInt("persistenceid");
			}
			pstm = con.prepareStatement("SELECT cc.* FROM testpsicometri_custosrecomend curso inner join catcursos cc on curso.catcursos_pid=cc.persistenceid where curso.testpsicometrico_pid=?");
			pstm.setInt(1, testPsicomInput_persistenceId)
			rs= pstm.executeQuery();
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				columns.put("persistenceId", rs.getLong("persistenceId"));
				columns.put("persistenceId_string",rs.getString("persistenceId"))
				columns.put("persistenceVersion",rs.getLong("persistenceVersion"))
				columns.put("persistenceVersion_string",rs.getString("persistenceVersion"))
				columns.put("clave",rs.getString("clave"))
				columns.put("descripcion",rs.getString("descripcion"))
				columns.put("fechaCreacion",rs.getString("fechaCreacion"))
				columns.put("usuarioCreacion",rs.getString("usuarioCreacion"))
				columns.put("isEliminado",rs.getBoolean("isEliminado"))
				
				custosRecomendados.add(columns)
			}
		testPsicomInput.put("custosRecomendados", custosRecomendados)
		objetoCompleto.put("testPsicomInput", testPsicomInput);
        errorinfo+="[6] TestPsicometricoRasgosDAO.findByCaseId "
        if(testPsicomRasgosInput.size()>0) {
			objetoCompleto.put("testPsicomRasgosInput", testPsicomRasgosInput);
		}
		
		if (testPsicomObservacionesInput.size()>0) {
			objetoCompleto.put("testPsicomObservacionesInput", testPsicomObservacionesInput);
		}
		
        rows.add(objetoCompleto);
        
        errorinfo+="[7] rows.add "
        resultado.setError_info(strError);
        resultado.setData(rows);
        resultado.setSuccess(true);
    }catch(Exception e) {
        resultado.setError_info(strError);
        resultado.setSuccess(false);
        resultado.setError(e.getMessage());
    }finally {
        if(closeCon) {
            new DBConnect().closeObj(con, stm, rs, pstm)
        }
    }
    
    return resultado;
    }catch(Exception fullEx) {
        Result result= new Result()
        result.setError(fullEx.getMessage())
        result.setSuccess(false)
        result.setError_info(errorinfo)
        return result
    }
}

	public Result getPsicometricoCursos(Long persistenceid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<?> rows = new ArrayList<?>();
		String strError = "";
		Map<String, Object> curso = new LinkedHashMap<String, Object>();
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.SELECT_TESTPSICOMETRICO_BY_CASEID_V2);
			pstm.setString(1, persistenceid);
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				curso = new LinkedHashMap<String, Object>();
				curso.put("fechaEntrevista", rs.getString("fechaEntrevista"));
				curso.put("ajusteMedioFamiliar", rs.getString("ajusteMedioFamiliar"));
				curso.put("califAjusteMedioFamiliar", rs.getString("califAjusteMedioFamiliar"));
				
				rows.add(curso);
			}
			
			
			strError += "Se obtuvieron los resultados | ";
			resultado.setError_info(strError);
			resultado.setData(rows);
			resultado.setSuccess(true);
		}catch(Exception e) {
			resultado.setError_info(strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	
	public Result selectAspirantesPsicometrico(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = ""
		List < String > lstGrupo = new ArrayList < String > ();
		List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();

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
			//AND ((SELECT COUNT(persistenceid) FROM TestPsicometrico as TP2 WHERE TP2.countRechazo = TP.countRechazo) = 0)
			
			//Se agrego el filtro con la fecha para l excluir los casos del INVP //////sda.fecharegistro > '2023-07-27T00:00:00.340974' AND
			where += " WHERE tp.finalizado IS NULL AND sda.iseliminado=false and (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado = false )"
			if (object.campus != null) {
				where += " AND LOWER(campus.grupoBonita) = LOWER('" + object.campus + "') "
			}
			
//			where += " AND (sda.ESTATUSSOLICITUD != 'Solicitud rechazada') AND (sda.ESTATUSSOLICITUD != 'Solicitud lista roja') AND (sda.ESTATUSSOLICITUD != 'Aspirantes registrados sin validación de cuenta') AND (sda.ESTATUSSOLICITUD !='Aspirantes registrados con validación de cuenta') AND (sda.ESTATUSSOLICITUD != 'Solicitud en proceso') AND (sda.ESTATUSSOLICITUD != 'Solicitud recibida' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud a modificar' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud modificada' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud vencida')"
			where += " AND (sda.ESTATUSSOLICITUD != 'Solicitud rechazada') AND (sda.ESTATUSSOLICITUD != 'Solicitud lista roja') AND (sda.ESTATUSSOLICITUD != 'Aspirantes registrados sin validación de cuenta') AND (sda.ESTATUSSOLICITUD !='Aspirantes registrados con validación de cuenta') AND (sda.ESTATUSSOLICITUD != 'Solicitud en proceso') AND (sda.ESTATUSSOLICITUD != 'Solicitud recibida' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud en espera de pago' ) AND (sda.ESTATUSSOLICITUD != 'Validación pago condonado' ) AND (sda.ESTATUSSOLICITUD != 'Autodescripción en proceso' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud a modificar' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud modificada' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud vencida')  AND (sda.ESTATUSSOLICITUD != 'Validación descuento 100%') AND (sda.ESTATUSSOLICITUD != 'Solicitud con pago aceptado') "
			
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

			String consulta = Statements.GET_ASPIRANTES_EN_PROCESO_PSICOMETRICO;

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
					case "SESIÓN,ID SESIÓN,FECHA ENTREVISTA":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						
						where += " ( LOWER(S.nombre) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where += " OR LOWER(CAST(S.persistenceid AS varchar)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER( CAST(TO_CHAR(P.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') )";
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
				case "SESIÓN":
					orderby += "S.nombre";
					break;
				case "FECHA ENTREVISTA":
					orderby += "P.APLICACION";
					break;
				case "ID SESIÓN":
					orderby += "s.persistenceid";
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
			where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno
			errorlog = consulta + " 4";

			consulta = consulta.replace("[WHERE]", where);
			errorlog = consulta + " 5";
			pstm = con.prepareStatement(consulta.replace("CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechasolicitudenviada, sda.fechaultimamodificacion, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso,periodo.fechafin AS periodofin, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura, s.nombre as sesion,s.persistenceid as idsesion,RD.responsableid,CAST(TO_CHAR(P.aplicacion, 'DD-MM-YYYY') as varchar) as fechaentrevista, tp.finalizado, sda.countrechazos", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "").replace("GROUP BY prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.fechaultimamodificacion, sda.fechasolicitudenviada, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion,periodo.fechafin, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,da.tipoalumno,sda.caseid,sda.telefonocelular,da.observacioneslistaroja,da.observacionesrechazo,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid, s.nombre, s.persistenceid,RD.responsableid, P.aplicacion,tp.finalizado,sda.countrechazos", ""))
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
					if(metaData.getColumnLabel(i).toLowerCase().equals("responsableid")) {
						User usr;
						String responsables = rs.getString(i);
						String nombres= "";
						if(!responsables.equals("null") && responsables != null) {
							String[] arrOfStr = responsables.split(",");
							for (String a: arrOfStr) {
								if(Long.parseLong(a)>0) {
									usr = context.getApiClient().getIdentityAPI().getUser(Long.parseLong(a))
									nombres+=(nombres.length()>1?", ":"")+usr.getFirstName()+" "+usr.getLastName()
								}
							}
						}
						columns.put(metaData.getColumnLabel(i).toLowerCase(), nombres);
					}else {						
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
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
	
	public Result selectAspirantesConPsicometrico(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = ""
		List < String > lstGrupo = new ArrayList < String > ();
		List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();

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
			where += " WHERE sda2.persistenceid IS NULL AND sda.iseliminado=false and (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado = false ) AND tp.persistenceid IS NOT NULL"
			if (object.campus != null) {
				where += " AND LOWER(campus.grupoBonita) = LOWER('" + object.campus + "') "
			}
			
			//where += " AND (sda.ESTATUSSOLICITUD != 'Solicitud rechazada') AND (sda.ESTATUSSOLICITUD != 'Solicitud lista roja') AND (sda.ESTATUSSOLICITUD != 'Aspirantes registrados sin validación de cuenta') AND (sda.ESTATUSSOLICITUD !='Aspirantes registrados con validación de cuenta') AND (sda.ESTATUSSOLICITUD != 'Solicitud en proceso') AND (sda.ESTATUSSOLICITUD != 'Solicitud recibida' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud a modificar' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud modificada' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud vencida')"
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

			String consulta = Statements.GET_ASPIRANTES_CON_PSICOMETRICO;

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
						where += " (LOWER(TO_CHAR(sda.fechaultimamodificacion::TIMESTAMP, 'dd-MM-yyyy hh:mm:ss')) ";
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
					case "ESTATUS":
						errorlog += "ESTATUS"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						//where += " LOWER(sda.ESTATUSSOLICITUD) ";
						//where +="(CASE WHEN sda2.ESTATUSSOLICITUD IS NOT NULL THEN LOWER(sda2.ESTATUSSOLICITUD) like lower('%[valor]%') ELSE LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]') END )"
						where += " LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;
					case "ESTATUS,ESTATUS DEl REPORTE":
						errorlog += "ESTATUS,ESTATUS DEl REPORTE"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}//TO-DO
						where += " (LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where += " OR tp.finalizado IS [valor] )";
						where = where.replace("[valor]", (filtro.get("valor") =="Finalizado" ? "true":(filtro.get("valor") == "En proceso"?"false":"NOT NULL") ))
						
//						if( filtro.get("valor").equals("Finalizado") || filtro.get("valor").equals("En proceso")) {
//							where += " OR tp.finalizado IS [valor] )";
//							where = where.replace("[valor]", (filtro.get("valor") =="Finalizado" ? "true":(filtro.get("valor") == "En proceso"?"false":"NOT NULL") ))
//						} else {
//							where += " )";
//						}
						
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
					case "IDBANNER":
						errorlog += "IDBANNER"
						tipoalumno += " AND LOWER(da.idbanner) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"));
						
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
						where = where.replace("[valor]", filtro.get("valor"));
						
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

						where = where.replace("[valor]", filtro.get("valor"));
						
						break;
					case "SESIÓN,ID SESIÓN,FECHA ENTREVISTA":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						
						where += " ( LOWER(S.nombre) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where += " OR LOWER(CAST(S.persistenceid AS varchar)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER( CAST(TO_CHAR(P.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;
					default:
					
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
				case "SESIÓN":
					orderby += "S.nombre";
					break;
				case "FECHA ENTREVISTA":
					orderby += "P.APLICACION";
					break;
				case "EstatusReporte":
					orderby += "tp.finalizado";
					break;
				case "ID SESIÓN":
					orderby += "s.persistenceid";
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
			where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno
			errorlog = consulta + " 4";

			consulta = consulta.replace("[WHERE]", where);
			errorlog = consulta + " 5";
			pstm = con.prepareStatement(consulta.replace("CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechasolicitudenviada, sda.fechaultimamodificacion, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso,periodo.fechafin AS periodofin, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura, s.nombre as sesion,s.persistenceid as idsesion,RD.responsableid,CAST(TO_CHAR(P.aplicacion, 'DD-MM-YYYY') as varchar) as fechaentrevista, tp.finalizado,tp.countrechazo", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "").replace("GROUP BY prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.fechaultimamodificacion, sda.fechasolicitudenviada, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion,periodo.fechafin, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,da.tipoalumno,sda.caseid,sda.telefonocelular,da.observacioneslistaroja,da.observacionesrechazo,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid, s.nombre, s.persistenceid, RD.responsableid, P.aplicacion,tp.finalizado,tp.countrechazo", ""))
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
					if(metaData.getColumnLabel(i).toLowerCase().equals("responsableid")) {
						User usr;
						String responsables = rs.getString(i);
						String nombres= "";
						if(!responsables.equals("null") && responsables != null) {
							String[] arrOfStr = responsables.split(",");
							for (String a: arrOfStr) {
								if(Long.parseLong(a)>0) {
									usr = context.getApiClient().getIdentityAPI().getUser(Long.parseLong(a))
									nombres+=(nombres.length()>1?", ":"")+usr.getFirstName()+" "+usr.getLastName()
								}
							}
						}
						columns.put(metaData.getColumnLabel(i).toLowerCase(), nombres);
					}else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
					if (metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
						String encoded = "";
						boolean noAzure = false;
						try {
							String urlFoto = rs.getString("urlfoto");
							if (urlFoto != null && !urlFoto.isEmpty()) {
								//columns.put("fotografiab64", rs.getString("urlfoto") + SSA);
								columns.put("fotografiab64", base64Imagen((rs.getString("urlfoto") + SSA)) );
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
	
	
	public Result selectAspirantesConPsicometricoRechazados(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = ""
		List < String > lstGrupo = new ArrayList < String > ();
		List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();

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
			where += " WHERE sda.persistenceid IS NOT NULL AND (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado::boolean IS false ) AND tp.persistenceid IS NOT NULL"
			if (object.campus != null) {
				where += " AND LOWER(campus.grupoBonita) = LOWER('" + object.campus + "') "
			}
			
			//where += " AND (sda.ESTATUSSOLICITUD != 'Solicitud rechazada') AND (sda.ESTATUSSOLICITUD != 'Solicitud lista roja') AND (sda.ESTATUSSOLICITUD != 'Aspirantes registrados sin validación de cuenta') AND (sda.ESTATUSSOLICITUD !='Aspirantes registrados con validación de cuenta') AND (sda.ESTATUSSOLICITUD != 'Solicitud en proceso') AND (sda.ESTATUSSOLICITUD != 'Solicitud recibida' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud a modificar' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud modificada' ) AND (sda.ESTATUSSOLICITUD != 'Solicitud vencida')"
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

			String consulta = Statements.GET_ASPIRANTES_CON_PSICOMETRICO_RECHAZADOS;

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
						//where += " LOWER(sda.ESTATUSSOLICITUD) ";
						//where +="(CASE WHEN sda2.ESTATUSSOLICITUD IS NOT NULL THEN LOWER(sda2.ESTATUSSOLICITUD) like lower('%[valor]%') ELSE LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]') END )"
						where += " LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') ";
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
					case "SESIÓN,FECHA ENTREVISTA":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						
						where += " ( LOWER(S.nombre) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER( CAST(TO_CHAR(P.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') )";
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
				case "SESIÓN":
					orderby += "S.nombre";
					break;
				case "FECHA ENTREVISTA":
					orderby += "P.APLICACION";
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
			where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno
			errorlog = consulta + " 4";

			consulta = consulta.replace("[WHERE]", where);
			errorlog = consulta + " 5";
			pstm = con.prepareStatement(consulta.replace("CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechasolicitudenviada, sda.fechaultimamodificacion, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura, s.nombre as sesion,RD.responsableid,CAST(TO_CHAR(P.aplicacion, 'DD-MM-YYYY') as varchar) as fechaentrevista, tp.finalizado,tp.countrechazo", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "").replace("GROUP BY prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.fechaultimamodificacion, sda.fechasolicitudenviada, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,da.tipoalumno,sda.caseid,sda.telefonocelular,da.observacioneslistaroja,da.observacionesrechazo,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid, s.nombre, RD.responsableid, P.aplicacion,tp.finalizado,tp.countrechazo", ""))
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
					if(metaData.getColumnLabel(i).toLowerCase().equals("responsableid")) {
						User usr;
						String responsables = rs.getString(i);
						String nombres= "";
						if(!responsables.equals("null") && responsables != null) {
							String[] arrOfStr = responsables.split(",");
							for (String a: arrOfStr) {
								if(Long.parseLong(a)>0) {
									usr = context.getApiClient().getIdentityAPI().getUser(Long.parseLong(a))
									nombres+=(nombres.length()>1?", ":"")+usr.getFirstName()+" "+usr.getLastName()
								}
							}
						}
						columns.put(metaData.getColumnLabel(i).toLowerCase(), nombres);
					}else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
					if (metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
						String encoded = "";
						boolean noAzure = false;
						try {
							String urlFoto = rs.getString("urlfoto");
							if (urlFoto != null && !urlFoto.isEmpty()) {
								//columns.put("fotografiab64", rs.getString("urlfoto") + SSA);
								columns.put("fotografiab64", base64Imagen((rs.getString("urlfoto") + SSA)) );
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
	
	
	public Result getPsicometricoMotivo(String caseId, int intentos) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion()
			//SELECT s.persistenceid, s.nombre sesion, prueba.nombre prueba, prueba.cupo, prueba.aplicacion fecha, prueba.lugar from paselista pl inner join pruebas prueba on prueba.persistenceid=pl.prueba_pid and prueba.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=prueba.sesion_pid where pl.asistencia=true
			pstm = con.prepareStatement("SELECT fuentesinfluyerondesicion, personasinfluyerondesicion FROM TestPsicometrico where caseid = ? and countRechazo = "+intentos)
			pstm.setString(1, caseId)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getPsicometricoRasgos(String caseId, int intentos) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion()
			//SELECT s.persistenceid, s.nombre sesion, prueba.nombre prueba, prueba.cupo, prueba.aplicacion fecha, prueba.lugar from paselista pl inner join pruebas prueba on prueba.persistenceid=pl.prueba_pid and prueba.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=prueba.sesion_pid where pl.asistencia=true
			pstm = con.prepareStatement("SELECT * FROM testPsicometricorasgos WHERE caseId = ? AND countRechazo = "+intentos+" ORDER BY persistenceid")
			pstm.setString(1, caseId)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase()) {
						
					}
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result postGetCatBitacoraComentariosPsicometrico(String usuarioComentario, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion()
			//SELECT s.persistenceid, s.nombre sesion, prueba.nombre prueba, prueba.cupo, prueba.aplicacion fecha, prueba.lugar from paselista pl inner join pruebas prueba on prueba.persistenceid=pl.prueba_pid and prueba.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=prueba.sesion_pid where pl.asistencia=true
			pstm = con.prepareStatement("SELECT comentario,fechaCreacion,isEliminado,modulo,persistenceId,persistenceVersion,usuario,usuarioComentario FROM CatBitacoraComentarios where usuarioComentario = ? ORDER BY persistenceId DESC")
			pstm.setString(1, usuarioComentario)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("usuario")) {
						User usr;
						String responsables = rs.getString(i);
						String nombres= "";
						if(!responsables.equals("null") && responsables != null) {
							usr = context.getApiClient().getIdentityAPI().getUserByUserName(responsables);
							nombres=usr.getFirstName()+" "+usr.getLastName();
						}
						columns.put(metaData.getColumnLabel(i), nombres);
					}else {
						columns.put(metaData.getColumnLabel(i), rs.getString(i));
					}
					
					
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getFechaINVP(String usuario) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT  p.aplicacion fecha_prueba, ri.fecha_registro FROM resultadoinvp ri INNER JOIN sesiones s on s.persistenceid=ri.sesiones_pid INNER JOIN pruebas p on p.sesion_pid=s.persistenceid and p.cattipoprueba_pid=2 INNER JOIN aspirantespruebas AS ap ON ap.prueba_pid = p.persistenceid where ap.username = ? order by ap.persistenceid DESC limit 1 " )
			pstm.setString(1, usuario)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	/*public Result getFechaSesion(String usuario,String intento) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT  COUNT(persistenceid) as cantidad FROM aspirantespruebas  where username = ? and asistencia is true and catTipoPrueba_pid = 1")
			pstm.setString(1, usuario)
			rs = pstm.executeQuery();
			int cantidad = 0;
			while(rs.next()) {
				cantidad = rs.getInt("cantidad")
			}
			
			int intentos = ((intento == null || intento.equals("null"))?1:Integer.parseInt(intento)+1)
			errorLog+="Intento: "+intentos
			
			String condicion = "";
			int offset = 0;
			if(cantidad == 0) {
				condicion = "";
			}else if(cantidad > 0 && intentos == cantidad) {
				condicion = "AND ap.asistencia is true"
			}else if(cantidad > 0 && intentos > cantidad) {
				condicion = "AND ap.asistencia is false and ap.acreditado is false AND pl.asistencia is null";
			}else if(cantidad > 0 && intentos < cantidad) {
				condicion = "AND ap.asistencia is true"
				offset = (cantidad - intentos)
			}
			
			errorLog+= "| consulta: SELECT  p.aplicacion fecha_prueba, ap.asistencia, paa.persistenceid, ap.sesiones_pid FROM aspirantespruebas AS ap INNER JOIN sesiones s on s.persistenceid=ap.sesiones_pid INNER JOIN pruebas p on p.sesion_pid=s.persistenceid and p.cattipoprueba_pid=2 LEFT JOIN solicitudDeAdmision AS sda ON sda.correoelectronico = ap.username LEFT JOIN detalleSolicitud AS ds ON ds.caseid = sda.caseid::varchar LEFT JOIN importacionPAA AS paa ON paa.idbanner = ds.idbanner and paa.sesion_pid = s.persistenceid::varchar LEFT JOIN paseLista AS pl ON pl.prueba_pid = ap.prueba_pid where ap.username = ? "+condicion+" order by ap.persistenceid DESC limit 1 OFFSET "+offset
			
			pstm = con.prepareStatement("SELECT  p.aplicacion fecha_prueba, ap.asistencia, paa.persistenceid, ap.sesiones_pid FROM aspirantespruebas AS ap INNER JOIN sesiones s on s.persistenceid=ap.sesiones_pid INNER JOIN pruebas p on p.sesion_pid=s.persistenceid and p.cattipoprueba_pid=2 LEFT JOIN solicitudDeAdmision AS sda ON sda.correoelectronico = ap.username LEFT JOIN detalleSolicitud AS ds ON ds.caseid = sda.caseid::varchar LEFT JOIN importacionPAA AS paa ON paa.idbanner = ds.idbanner and paa.sesion_pid = s.persistenceid::varchar LEFT JOIN paseLista AS pl ON pl.prueba_pid = ap.prueba_pid where ap.username = ? "+condicion+"  AND ap.catTipoPrueba_pid = 1 order by ap.persistenceid DESC limit 1 OFFSET "+offset )
			pstm.setString(1, usuario)
			rs = pstm.executeQuery();
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				
				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
			
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}*/
	
	public Result getFechaSesion(String usuario,String caseid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement("SELECT p.aplicacion fecha_prueba, ap.asistencia, paa.persistenceid, ap.sesiones_pid FROM aspirantespruebas AS ap INNER JOIN sesiones s on s.persistenceid=ap.sesiones_pid INNER JOIN pruebas p on p.sesion_pid=s.persistenceid and p.cattipoprueba_pid=2 LEFT JOIN solicitudDeAdmision AS sda ON sda.correoelectronico = ap.username LEFT JOIN detalleSolicitud AS ds ON ds.caseid = sda.caseid::varchar LEFT JOIN importacionPAA AS paa ON paa.idbanner = ds.idbanner and paa.sesion_pid = s.persistenceid::varchar LEFT JOIN paseLista AS pl ON pl.prueba_pid = ap.prueba_pid where ap.caseid = ${caseid} AND ap.asistencia IS TRUE AND ap.catTipoPrueba_pid = 1 order by ap.persistenceid DESC limit 1" )
			rs = pstm.executeQuery();
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				
				rows.add(columns);
			}
			if(rows.size() == 0 )  {
				pstm = con.prepareStatement("SELECT p.aplicacion fecha_prueba, ap.asistencia, paa.persistenceid, ap.sesiones_pid FROM aspirantespruebas AS ap INNER JOIN sesiones s on s.persistenceid=ap.sesiones_pid INNER JOIN pruebas p on p.sesion_pid=s.persistenceid and p.cattipoprueba_pid=2 LEFT JOIN solicitudDeAdmision AS sda ON sda.correoelectronico = ap.username LEFT JOIN detalleSolicitud AS ds ON ds.caseid = sda.caseid::varchar LEFT JOIN importacionPAA AS paa ON paa.idbanner = ds.idbanner and paa.sesion_pid = s.persistenceid::varchar LEFT JOIN paseLista AS pl ON pl.prueba_pid = ap.prueba_pid where ap.caseid = ${caseid} AND ap.asistencia is false and ap.acreditado IS FALSE AND ap.catTipoPrueba_pid = 1 order by ap.persistenceid DESC limit 1" )
				rs = pstm.executeQuery();
				metaData = rs.getMetaData();
				columnCount = metaData.getColumnCount();
				while (rs.next()) {
					Map < String, Object > columns = new LinkedHashMap < String, Object > ();
					
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
						
					rows.add(columns);
				}
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
			
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getPsicometricoFinalizado(String usuario,String intento) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			int intentos = ((intento == null || intento.toString().toLowerCase().equals("null"))?0:Integer.parseInt(intento))
			
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT finalizado FROM testPsicometrico AS tp INNER JOIN SolicitudDeAdmision AS sda ON tp.caseid::INTEGER = sda.caseid WHERE sda.correoelectronico = ? AND countRechazo = "+intentos)
			pstm.setString(1, usuario)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				//columns.put("intetos",intentos);

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setError(intentos+"");
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getInfoReportes(String usuario,Long intento,RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = ""; 
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs = pstm.executeQuery();
			if (rs.next()) {
				SSA = rs.getString("valor")
			}
			
			pstm = con.prepareStatement("SELECT  sda.resultadoPAA,sda.caseid,ds.idbanner,tp.fechaFinalizacion,sda.urlfoto,CONCAT(sda.apellidopaterno,' ',sda.apellidomaterno,CASE WHEN (sda.apellidomaterno != '' ) THEN ' ' END,sda.segundonombre,CASE WHEN ( sda.segundonombre != '' ) THEN ' ' END,sda.primernombre) AS nombre,  TO_CHAR(sda.fechanacimiento::DATE, 'dd-Mon-yyyy') AS fechanacimiento ,(CASE WHEN cb.descripcion = 'Otro' THEN sda.bachillerato ELSE cb.descripcion END) AS preparatoria, (CASE WHEN cb.descripcion = 'Otro' THEN sda.ciudadBachillerato ELSE cb.ciudad END) AS ciudad, cp.descripcion as pais, cge.nombre as carrera, IPAA.INVP,IPAA.PARA,IPAA.PAAN,IPAA.PAAV, sda.promediogeneral as promedio, cta.descripcion AS tipoAdmision, catP.clave as periodo,tp.quienIntegro, tp.quienRealizoEntrevista, date_part('year', age( sda.fechanacimiento::DATE)) as edad FROM SolicitudDeAdmision AS sda INNER JOIN DetalleSolicitud AS ds ON sda.caseid = ds.caseid::INTEGER INNER JOIN catbachilleratos AS cb ON cb.persistenceid = sda.catbachilleratos_pid INNER JOIN catpais AS cp ON cp.persistenceid = sda.catpais_pid INNER JOIN catGestionEscolar as CGE ON CGE.persistenceid = sda.catGestionEscolar_pid INNER JOIN importacionPAA AS IPAA ON IPAA.idbanner = DS.idbanner INNER JOIN catTipoAdmision AS cta ON cta.persistenceid = ds.cattipoadmision_pid INNER JOIN catPeriodo AS catP ON catP.persistenceid = sda.catperiodo_pid INNER JOIN testPsicometrico AS tp ON tp.caseid::INTEGER = sda.caseid WHERE sda.correoelectronico = ? AND countrechazo = ?")
			pstm.setString(1, usuario)
			pstm.setLong(2, intento)
			rs = pstm.executeQuery();
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
						String encoded = "";
						try {
							String urlFoto = rs.getString("urlfoto");
							/*if(urlFoto != null && !urlFoto.isEmpty()) {
								columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
							}else {*/
								List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
								for(Document doc : doc1) {
									encoded = "../API/formsDocumentImage?document="+doc.getId();
									columns.put("fotografiab64", encoded);
								}
							//}
						}catch(Exception e) {
							columns.put("fotografiab64", "");
						}
					}else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
					
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getInfoRelativos(String caseid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = ""; 
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT distinct on (pt.catparentezco_pid) pt.catparentezco_pid,cp.descripcion as parentesco,empresatrabaja,puesto,pt.vive_pid, cv.descripcion AS vive, pt.desconozcoDatosPadres ,cc.descripcion as campusAnahuac, CONCAT(pt.nombre,' ',pt.apellidos) AS nombre, pt.isTutor FROM PadresTutor AS pt LEFT JOIN catParentesco as cp ON cp.persistenceid = pt.catparentezco_pid LEFT JOIN catCampus AS CC ON cc.persistenceid = catcampusegreso_pid LEFT JOIN catVive AS cv ON cv.persistenceid = pt.vive_pid where  pt.caseid =  "+caseid)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getInfoRelativosHermanos(String caseid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT CONCAT(nombres,' ',apellidos) AS nombre, isestudia,istrabaja,escuelaestudia,empresatrabaja from hermano where caseid = "+caseid)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getInfoFuentesInfluyeron(String caseid, Long intentos) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Boolean autov1 = false;
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement("SELECT idlc.descripcion as fuentes  FROM CatinformacionDeLaCarrera AS idlc LEFT JOIN AUTODESCRIPCIO_INFORMACIONCAR AS ai ON idlc.persistenceid = ai.catinformaciondelacarrera_pid LEFT JOIN Autodescripcion AS auto ON auto.persistenceid = ai.autodescripcion_pid  where caseid = "+caseid)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					columns.put("autodescripcion", true);
					autov1 = true;
				}

				rows.add(columns);
			}
			
			if(!autov1) {
				//pstm = con.prepareStatement("SELECT fuentesInfluyeronDesicion as fuentes FROM autodescripcionv2 where caseid = "+caseid)
				pstm = con.prepareStatement("SELECT fuentesInfluyeronDesicion as fuentes FROM TestPsicometrico where caseid = '"+caseid+"' AND countRechazo = "+intentos)
				rs = pstm.executeQuery()
				rows = new ArrayList < Map < String, Object >> ();
				metaData = rs.getMetaData();
				columnCount = metaData.getColumnCount();
				while (rs.next()) {
					Map < String, Object > columns = new LinkedHashMap < String, Object > ();
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						columns.put("autodescripcionv2", true);
					}
	
					rows.add(columns);
				}
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getInfoRasgos(String caseid, Long intentos) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT rc.descripcion AS calificacion, cro.descripcion AS rasgo FROM TestPsicometricoRasgos AS tpr LEFT JOIN CatRasgosCalif AS rc ON rc.persistenceid = tpr.calificacion_pid LEFT JOIN CatRasgosObservados AS cro ON cro.persistenceid = tpr.rasgo_pid where tpr.caseid = ?  AND tpr.countRechazo = ?")
			pstm.setString(1, caseid);
			pstm.setLong(2, intentos);
			rs = pstm.executeQuery();
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getInfoCapacidadAdaptacion(String caseid, Long intentos) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT ajusteMedioFamiliar,califAjusteMedioFamiliar,ajusteEscolarPrevio,califAjusteEscolarPrevio,ajusteMedioSocial,califAjusteMedioSocial,ajusteEfectivo,califAjusteAfectivo,ajusteReligioso,califAjusteReligioso,ajusteExistencial,califAjusteExistencial FROM TestPsicometrico where caseid = ? AND countRechazo = ?")
			pstm.setString(1, caseid);
			pstm.setLong(2, intentos);
			rs = pstm.executeQuery();
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					columns.put("autodescripcionv2", true);
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getInfoSaludPSeccion(String caseid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Boolean querySuccess = false;
		String strError = "";
		
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			Map < String, Object > columns = new LinkedHashMap < String, Object > ();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.SELECT_SITUACION_SALUD);
			pstm.setLong(1, Long.parseLong(caseid));
			rs = pstm.executeQuery();

			strError += "SELECT_SITUACION_SALUD: Success | ";
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (rs.next()) {
				columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				
				rows.add(columns);
			}
			
			
			strError += "Se obtuvieron los resultados | ";
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			resultado.setError_info(errorLog+" - Error: "+strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	public Result getInfoSaludSSeccion(String caseid, Long intentos) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Boolean querySuccess = false;
		String strError = "";
		
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			Map < String, Object > columns = new LinkedHashMap < String, Object > ();
			List<Map<String, Object>> lstCursos = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.SELECT_RECOMENDACIONES_CONCLUSIONES);
			pstm.setString(1, caseid);
			pstm.setLong(2, intentos);
			rs = pstm.executeQuery();
				
			strError += "SELECT_RECOMENDACIONES_CONCLUSIONES: Success | ";
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while (rs.next()) {				
				columns = new LinkedHashMap < String, Object > ();
				
				for (int i = 1; i <= columnCount; i++) {
						columns.put("salud", rs.getString("salud"));
						columns.put("conclusiones_recomendaciones", rs.getString("conclusiones_recomendaciones"));
						columns.put("interpretacion", rs.getString("interpretacion"));
						columns.put("cursos_recomendados", rs.getString("cursos_recomendados"));
				}
				rows.add(columns);
			}


			
			strError += "Se obtuvieron los resultados | ";
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			resultado.setError_info(errorLog+" - Error: "+strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
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
	
}
