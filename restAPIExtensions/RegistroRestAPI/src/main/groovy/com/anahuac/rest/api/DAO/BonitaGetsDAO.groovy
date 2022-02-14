package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.flownode.ActivityInstance
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion
import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstance
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException
import org.bonitasoft.engine.exception.BonitaRuntimeException
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.bonitasoft.engine.search.Order
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utilities.LoadParametros
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.bonitasoft.web.extension.rest.RestApiController

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class BonitaGetsDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(BonitaGetsDAO.class)
	Connection con
	Statement stm
	ResultSet rs
	ResultSet rs2
	PreparedStatement pstm
	
	
	public Result getUserHumanTask(Long caseid,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean conection = false;
		try {
			
			List<ActivityInstance> activityInstances = [];
			
			org.bonitasoft.engine.api.APIClient apiClient = context.getApiClient();
			try {
				activityInstances = context.getApiClient().getProcessAPI().getActivities(caseid, 0, 1);
			}catch(Exception ex) {
				errorLog += ex;
			}
			
			resultado.setSuccess(true);
			resultado.setData(activityInstances)
			resultado.setError(errorLog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog)
			e.printStackTrace();
		}
		return resultado;
	}
	
	public Result getUserArchivedHumanTask(Long caseid,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false;
		try {
			
			List<ArchivedActivityInstance> activityInstances = [];
			
			org.bonitasoft.engine.api.APIClient apiClient = context.getApiClient();
			try {
				
				closeCon = validarConexionBonita()
				
				pstm = con.prepareStatement("SELECT * FROM arch_flownode_instance WHERE sourceobjectid = ${caseid}");
				rs = pstm.executeQuery();
				if(rs.next()) {
					try {
						errorLog+=rs.getLong("id")
						activityInstances = context.getApiClient().getProcessAPI().getArchivedActivityInstances(rs.getLong("id"), 0, 10, ActivityInstanceCriterion.DEFAULT)
					}catch(BonitaRuntimeException a) {
						errorLog+="error"+a
					}
					
				}
				
			}catch(Exception ex) {
				errorLog += ex;
			}
			
			resultado.setSuccess(true);
			resultado.setData(activityInstances)
			resultado.setError(errorLog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog)
			e.printStackTrace();
		}
		finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result getUserProcess(RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false;
		try {
			String username = "";
			String password = "";
			
			List<ProcessDeploymentInfo> map = [];
			ProcessDeploymentInfo info;
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
			
			//org.bonitasoft.engine.api.APIClient apiClient = context.getApiClient();
			try {
				closeCon = validarConexionBonita()
				pstm = con.prepareStatement(" SELECT processid FROM process_definition WHERE NAME = 'Proceso admisiones' and activationstate = 'ENABLED' ");
				rs = pstm.executeQuery();
				if(rs.next()) {
					info = apiClient.getProcessAPI().getProcessDeploymentInfo(rs.getLong("processid"));
				}
			}catch(Exception ex) {
				errorLog += ex;
			}
			map.add(info)
			resultado.setSuccess(true);
			resultado.setData(map)
			resultado.setError(errorLog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog)
			e.printStackTrace();
		}
		if (closeCon) {
			new DBConnect().closeObj(con, stm, rs, pstm)
		}
		return resultado;
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
