package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.SimpleDateFormat
import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.actor.ActorUpdater
import org.bonitasoft.engine.bpm.data.DataDefinition
import org.bonitasoft.engine.bpm.flownode.ActivityInstance
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion
import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstance
import org.bonitasoft.engine.bpm.process.ActivationState
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance
import org.bonitasoft.engine.bpm.process.DesignProcessDefinition
import org.bonitasoft.engine.bpm.process.ProcessDefinition
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException
import org.bonitasoft.engine.exception.BonitaRuntimeException
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.identity.UserWithContactData
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

import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class BonitaGetsDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(BonitaGetsDAO.class)
	Connection con
	Statement stm
	ResultSet rs
	ResultSet rs2
	PreparedStatement pstm
	
	
	public Result getUserHumanTask(Long caseid,String value,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false, processId = false;
		try {
			String username = "";
			String password = "";
			
			List<ActivityInstance> activityInstances = [];
			ProcessDeploymentInfo info;
			Map<String, Serializable> datos = new HashMap<String, Serializable>();
			List < Map < String, Serializable >> rows = new ArrayList < Map < String, Serializable >> ();
			
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
				activityInstances = apiClient.getProcessAPI().getActivities(caseid, 0, 10);
				
				for(int i = 0; i< activityInstances?.size(); i++) {
					datos = new HashMap<String, Serializable>();
					datos.put("displayDescription", activityInstances[i]['displayDescription'] );
					datos.put("executedBy", activityInstances[i]['executedBy'] );
					datos.put("rootContainerId", activityInstances[i]['rootContainerId'] );
					datos.put("displayName", activityInstances[i]['displayName'] );
					datos.put("executedBySubstitute", activityInstances[i]['executedBySubstitute'] );
					datos.put("description", activityInstances[i]['description'] );
					datos.put("type", activityInstances[i]['type'] );
					datos.put("priority", activityInstances[i]['priority'] );
					datos.put("actorId", activityInstances[i]['actorId'] );
					datos.put("caseId", activityInstances[i]['parentContainerId'] );
					datos.put("name", activityInstances[i]['name'] );
					datos.put("reached_state_date", activityInstances[i]['reachedStateDate'] );
					datos.put("rootCaseId", activityInstances[i]['rootContainerId'] );
					datos.put("id", activityInstances[i]['id'] );
					datos.put("state", activityInstances[i]['state'] );
					datos.put("parentCaseId", activityInstances[i]['parentProcessInstanceId'] );
					datos.put("last_update_date", activityInstances[i]['lastUpdateDate'] );
					datos.put("assigned_id", activityInstances[i]['assigneeId'] );
					
					if( value.length() > 0 && value == "processId") {
						info = apiClient.getProcessAPI().getProcessDeploymentInfo( activityInstances[i]['processDefinitionId']);
						datos.put("processId", info );
					}else {
						datos.put("processId", activityInstances[i]['processDefinitionId'] );
					}
					
					rows.add(datos)
						
				}
				
			}catch(Exception ex) {
				errorLog += ex;
			}
			
			resultado.setData(rows)
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
			e.printStackTrace();
		}
		return resultado;
	}
	
	public Result getCaseInstance(Long caseid,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false, processId = false;
		try {
			String username = "";
			String password = "";
			
			ProcessDefinition info;
			List < ProcessDefinition> rows = new ArrayList < ProcessDefinition> ();
			
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
				
				info = apiClient.getProcessAPI()?.getProcessDefinition(apiClient.getProcessAPI().getProcessInstance(caseid)?.processDefinitionId)
				rows.add(info);
				
			}catch(Exception ex) {
				info = apiClient.getProcessAPI()?.getProcessDefinition(apiClient.getProcessAPI().getArchivedProcessInstance(caseid)?.processDefinitionId)
				rows.add(info);
			}
			
			resultado.setData(rows)
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
			e.printStackTrace();
		}
		return resultado;
	}
	
	public Result getUserTask(Long taskId,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false, processId = false;
		try {
			String username = "";
			String password = "";
			
			ActivityInstance activityInstances;
			ProcessDeploymentInfo info;
			Map<String, Serializable> datos = new HashMap<String, Serializable>();
			List < Map < String, Serializable >> rows = new ArrayList < Map < String, Serializable >> ();
			
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
				
				activityInstances = apiClient.getProcessAPI().getActivityInstance(taskId)
				
				datos = new HashMap<String, Serializable>();
				datos.put("displayDescription", activityInstances['displayDescription'] );
				datos.put("executedBy", activityInstances['executedBy'] );
				datos.put("rootContainerId", activityInstances['rootContainerId'] );
				datos.put("displayName", activityInstances['displayName'] );
				datos.put("executedBySubstitute", activityInstances['executedBySubstitute'] );
				datos.put("description", activityInstances['description'] );
				datos.put("type", activityInstances['type'] );
				datos.put("priority", activityInstances['priority'] );
				datos.put("actorId", activityInstances['actorId'] );
				datos.put("caseId", activityInstances['parentContainerId'] );
				datos.put("name", activityInstances['name'] );
				datos.put("reached_state_date", activityInstances['reachedStateDate'] );
				datos.put("rootCaseId", activityInstances['rootContainerId'] );
				datos.put("id", activityInstances['id'] );
				datos.put("state", activityInstances['state'] );
				datos.put("parentCaseId", activityInstances['parentProcessInstanceId'] );
				datos.put("last_update_date", activityInstances['lastUpdateDate'] );
				datos.put("assigned_id", activityInstances['assigneeId'] );
				datos.put("processId", activityInstances['processDefinitionId'] );
				
				
				rows.add(datos)
				
			}catch(Exception ex) {
				errorLog += ex;
			}
			
			resultado.setData(rows)
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
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
			Map<String, Serializable> datos = new HashMap<String, Serializable>();
			List < Map < String, Serializable >> rows = new ArrayList < Map < String, Serializable >> ();
			
			String username = "";
			String password = "";
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
			
			//org.bonitasoft.engine.api.APIClient apiClient = context.getApiClient();
			errorLog+= "1"
			activityInstances = apiClient.getProcessAPI().getArchivedActivityInstances(caseid, 0, 100, ActivityInstanceCriterion.LAST_UPDATE_DESC)
			
			errorLog+= "2"
			errorLog+= "|"+activityInstances?.size()
			for(int i = 0; i< activityInstances?.size(); i++) {
				
				datos = new HashMap<String, Serializable>();
				datos.put("displayDescription", activityInstances[i]['displayDescription'] );
				datos.put("executedBy", activityInstances[i]['executedBy'] );
				datos.put("archivedDate", activityInstances[i]['archiveDate'] );
				datos.put("rootContainerId", activityInstances[i]['rootContainerId'] );
				datos.put("displayName", activityInstances[i]['displayName'] );
				datos.put("executedBySubstitute", activityInstances[i]['executedBySubstitute'] );
				datos.put("description", activityInstances[i]['description'] );
				datos.put("type", activityInstances[i]['type'] );
				
				datos.put("processId", activityInstances[i]['processDefinitionId'] );
				datos.put("caseId", activityInstances[i]['processInstanceId'] );
				datos.put("name", activityInstances[i]['name'] );
				datos.put("reached_state_date", activityInstances[i]['reachedStateDate'] );
				datos.put("rootCaseId", activityInstances[i]['rootContainerId'] );
				datos.put("id", activityInstances[i]['id'] );
				datos.put("state", activityInstances[i]['state'] );
				datos.put("parentCaseId", activityInstances[i]['parentContainerId'] );
				datos.put("last_update_date", activityInstances[i]['lastUpdateDate'] );
				
				try {
					datos.put("assigned_id", activityInstances[i]['assigneeId'] );
					datos.put("priority", activityInstances[i]["priority"] );
					datos.put("actorId", activityInstances[i]["actorId"] );
				}catch(Exception ex) {
					errorLog += ex;
				}
				
				rows.add(datos)
			}
			
			
			resultado.setSuccess(true);
			resultado.setData(rows)
			resultado.setError(errorLog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
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
			
			e.printStackTrace();
		} 
		finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result getCase() {
		Result resultado = new Result();
		
		resultado.setSuccess(true)
		resultado.setData([]);
		return resultado;
	}
	
	
	
	public Result getCaseVariable(Long caseid,String name,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="", where = "";
		Boolean closeCon = false;
		try {
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			if(name.length() > 0) {
				where = "AND name =  '${name}'"
			}
			
			closeCon = validarConexionBonita()
			pstm = con.prepareStatement(" SELECT containerid AS case_id,name,description,classname AS type,booleanvalue,shorttextvalue,clobvalue,longvalue FROM data_instance WHERE containerid = ${caseid} ${where}");
			rs = pstm.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("type") || metaData.getColumnLabel(i).toLowerCase().equals("case_id") || metaData.getColumnLabel(i).toLowerCase().equals("name") || metaData.getColumnLabel(i).toLowerCase().equals("description")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
					if(metaData.getColumnLabel(i).toLowerCase().equals("type")) {
						if(rs.getString(i).equals("java.lang.Boolean")) {
							columns.put("value", rs.getString("booleanvalue")?.toLowerCase())
						} else 
							if(rs.getString(i).equals("java.lang.String")) {
							columns.put("value", rs.getString("shorttextvalue"))
						} else 
							if(rs.getString(i).equals("java.lang.Long")) {
							columns.put("value", rs.getString("longvalue"))
						} else
							 if(rs.getString(i).equals("java.util.Map")) {
							columns.put("value", rs.getString("clobvalue"))
						} else 
							if(rs.getString(i).equals("java.util.Date")) {
								if(rs.getString("longvalue") != null && rs.getString("longvalue") != 'null') {
									Date currentDate = new Date(rs.getLong("longvalue"));
									columns.put("value", currentDate.toString() )
								} else {
									columns.put("value", null )
								}
								
						}
						
					}
				}
	
					rows.add(columns);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows)
			resultado.setError(errorLog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
			e.printStackTrace();
		}
		finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	
	public Result getUserContext(Long caseid,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false;
		try {
			String username = "";
			String password = "";
			def ref;
			List < Map < String, Serializable >> rows = new ArrayList < Map < String, Serializable >> ();
			Map<String, Serializable> contexto;
			Map<String, Serializable> contexto2 = new HashMap<String, Serializable>();
			Map<String, Serializable> contextoDetalle = new HashMap<String, Serializable>();
			List < Map < String, Serializable >> foto = new ArrayList < Map < String, Serializable >> ();
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
			
			
			try {
				contexto = apiClient.getProcessAPI().getProcessInstanceExecutionContext(caseid);
				boolean link = false
				for ( prop in contexto ) {
					
					contextoDetalle = new HashMap<String, Serializable>();
					
					if(prop.key == "fotoPasaporte_ref" || prop.key == "actaNacimiento_ref" || prop.key == "constancia_ref" || prop.key == "descuento_ref" || prop.key == "resultadoCB_ref" || prop.key == "cartaAA_ref" ) {
						foto = new ArrayList < Map < String, Serializable >> ();
						if(contexto[prop.key][0]?.processInstanceId != null) {
							contextoDetalle.put("id", contexto[prop.key][0]?.id);
							contextoDetalle.put("processInstanceId", contexto[prop.key][0]?.processInstanceId);
							contextoDetalle.put("author", contexto[prop.key][0]?.author);
							contextoDetalle.put("creationDate", contexto[prop.key][0]?.creationDate);
							contextoDetalle.put("fileName", contexto[prop.key][0]?.fileName);
							contextoDetalle.put("contentMimeType", contexto[prop.key][0]?.contentMimeType);
							contextoDetalle.put("contentStorageId", contexto[prop.key][0]?.contentStorageId);
							contextoDetalle.put("url", contexto[prop.key][0]?.url);
							contextoDetalle.put("description", contexto[prop.key][0]?.description);
							contextoDetalle.put("version", contexto[prop.key][0]?.version);
							contextoDetalle.put("index", contexto[prop.key][0]?.index);
							contextoDetalle.put("contentFileName", contexto[prop.key][0]?.contentFileName);
							foto.add(contextoDetalle);
						}			
						contexto2.put(prop.key, foto)
					}else {
						
						contextoDetalle.put("name", contexto[prop.key]?.name);
						contextoDetalle.put("type", contexto[prop.key]?.type);
						try {
							contextoDetalle.put("storageId", contexto[prop.key]?.storageId);
							contextoDetalle.put("storageId_string", contexto[prop.key]?.storageIdAsString);
							contextoDetalle.put("link", "API/bdm/businessData/"+contexto[prop.key]?.type+"/"+ (contexto[prop.key]?.storageId != null ?contexto[prop.key]?.storageId: "" ));
						}catch(Exception ex) {
							contextoDetalle.put("storageIds", contexto[prop.key]?.storageIds);
							contextoDetalle.put("storageIds_string", contexto[prop.key]?.storageIdsAsString);
							contextoDetalle.put("link", "API/bdm/businessData/"+contexto[prop.key]?.type+"/findByIds?ids="+ (contexto[prop.key]?.storageIds?.join(",")));
							
						}
						
						contexto2.put(prop.key, contextoDetalle)
					
					}
					 
					 
				}
				
			}catch(ProcessInstanceNotFoundException ex) {
				
			}
			
			/**/
			rows.add(contexto2);	
			resultado.setSuccess(true);
			resultado.setData(rows)
			resultado.setError_info( errorLog)
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info( errorLog)
			e.printStackTrace();
		}
		finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result getUserArchivedContext(Long caseid,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false;
		try {
			String username = "";
			String password = "";
			def ref;
			List < Map < String, Serializable >> rows = new ArrayList < Map < String, Serializable >> ();
			Map<String, Serializable> contexto;
			Map<String, Serializable> contexto2 = new HashMap<String, Serializable>();
			Map<String, Serializable> contextoDetalle = new HashMap<String, Serializable>();
			List < Map < String, Serializable >> foto = new ArrayList < Map < String, Serializable >> ();
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
			
			try {
				closeCon = validarConexionBonita();
				pstm = con.prepareStatement(" SELECT id FROM ARCH_PROCESS_INSTANCE WHERE sourceobjectid = ${caseid} ");
				rs = pstm.executeQuery();
				if(rs.next()) {
					contexto = apiClient.getProcessAPI().getArchivedProcessInstanceExecutionContext( Long.parseLong(rs.getString("id")));
					boolean link = false
					for ( prop in contexto ) {
						
						contextoDetalle = new HashMap<String, Serializable>();
						
						if(prop.key == "fotoPasaporte_ref") {
							contextoDetalle.put("id", contexto[prop.key][0]?.id);
							contextoDetalle.put("processInstanceId", contexto[prop.key][0]?.processInstanceId);
							contextoDetalle.put("author", contexto[prop.key][0]?.author);
							contextoDetalle.put("creationDate", contexto[prop.key][0]?.creationDate);
							contextoDetalle.put("fileName", contexto[prop.key][0]?.fileName);
							contextoDetalle.put("contentMimeType", contexto[prop.key][0]?.contentMimeType);
							contextoDetalle.put("contentStorageId", contexto[prop.key][0]?.contentStorageId);
							contextoDetalle.put("url", contexto[prop.key][0]?.url);
							contextoDetalle.put("description", contexto[prop.key][0]?.description);
							contextoDetalle.put("version", contexto[prop.key][0]?.version);
							contextoDetalle.put("index", contexto[prop.key][0]?.index);
							contextoDetalle.put("contentFileName", contexto[prop.key][0]?.contentFileName);
							
							foto.add(contextoDetalle);
							contexto2.put(prop.key, foto)
						}else {
							
							contextoDetalle.put("name", contexto[prop.key]?.name);
							contextoDetalle.put("type", contexto[prop.key]?.type);
							try {
								contextoDetalle.put("storageId", contexto[prop.key]?.storageId);
								contextoDetalle.put("storageId_string", contexto[prop.key]?.storageIdAsString);
								contextoDetalle.put("link", "API/bdm/businessData/"+contexto[prop.key]?.type+"/"+ (contexto[prop.key]?.storageId != null ?contexto[prop.key]?.storageId: "" ));
							}catch(Exception ex) {
								contextoDetalle.put("storageIds", contexto[prop.key]?.storageIds);
								contextoDetalle.put("storageIds_string", contexto[prop.key]?.storageIdsAsString);
								contextoDetalle.put("link", "API/bdm/businessData/"+contexto[prop.key]?.type+"/findByIds?ids="+ (contexto[prop.key]?.storageIds?.join(",")));
								
							}
							
							contexto2.put(prop.key, contextoDetalle)
						
						}
						 
						 
					}
				}
					
				}catch(ProcessInstanceNotFoundException ex) {
					
				}
			
			/**/
			rows.add(contexto2);
			resultado.setSuccess(true);
			resultado.setData(rows)
			resultado.setError_info( errorLog)
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info( errorLog)
			e.printStackTrace();
		}
		finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result caseDocument(Long caseid,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean conection = false;
		try {
			String username = "";
			String password = "";
			
			List<Document> documents = [];
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()
			apiClient.login(username, password)
			
			try {
				documents = apiClient.getProcessAPI().getDocumentList(caseid, "fotoPasaporte", 0, 9999)
			}catch(Exception ex) {
				errorLog += ex;
			}
			
			resultado.setSuccess(true);
			resultado.setData(documents)
			resultado.setError(errorLog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
			e.printStackTrace();
		}
		return resultado;
	}
	
	public Result archivedCase(Long caseid,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean conection = false;
		try {
			String username = "";
			String password = "";
			
			List<ArchivedProcessInstance>  archivedProcessInstances = [];
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()
			apiClient.login(username, password)
			
			try {
				archivedProcessInstances = apiClient.processAPI.getArchivedProcessInstances(caseid, 0, 999)
			}catch(Exception ex) {
				errorLog += ex;
			}
			
			resultado.setSuccess(true);
			resultado.setData(archivedProcessInstances)
			resultado.setError(errorLog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
			e.printStackTrace();
		}
		return resultado;
	}
	
	
	public Result getUserIdentity(Long user,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false, processId = false;
		try {
			String username = "";
			String password = "";
			
			User usuario;
			UserWithContactData uwcd;
			Map<String, Serializable> datos = new HashMap<String, Serializable>();
			List < Map < String, Serializable >> rows = new ArrayList < Map < String, Serializable >> ();
			
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
				
				usuario = apiClient.getIdentityAPI().getUser(context.apiSession.getUserId())
				uwcd = apiClient.getIdentityAPI().getUserWithProfessionalDetails(context.apiSession.getUserId())
				datos = new HashMap<String, Serializable>();
				datos.put("firstname", usuario['firstName'] );
				datos.put("icon", usuario['iconPath'] );
				datos.put("creation_date", usuario['creationDate'] );
				datos.put("userName", usuario['userName'] );
				datos.put("title", usuario['title'] );
				datos.put("created_by_user_id", usuario['createdBy'] );
				datos.put("enabled", usuario['enabled'] );
				datos.put("lastname", usuario['lastName'] );
				datos.put("last_connection", usuario['lastConnection'] );
				datos.put("password", "");
				datos.put("manager_id", usuario['managerUserId'] );
				datos.put("id", usuario['id'] );
				datos.put("job_title", usuario['jobTitle'] );
				datos.put("last_update_date", usuario['lastUpdate'] );
				datos.put("professional_data", uwcd.contactData);
				
				rows.add(datos)
				
			}catch(Exception ex) {
				errorLog += ex;
			}
			
			resultado.setData(rows)
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
			e.printStackTrace();
		}
		return resultado;
	}
	
	public Result getUserIdentityMembership(Long user,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false, processId = false;
		try {
			String username = "";
			String password = "";
			
			List<UserMembership> userMemberships;
			UserWithContactData uwcd;
			Map<String, Serializable> datos = new HashMap<String, Serializable>();
			List < Map < String, Serializable >> rows = new ArrayList < Map < String, Serializable >> ();
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()
			apiClient.login(username, password)
			
			userMemberships = apiClient.getIdentityAPI().getUserMemberships(context.apiSession.getUserId(), 0, 999, UserMembershipCriterion.ROLE_NAME_ASC)
			for(UserMembership um: userMemberships) {
				datos = new HashMap<String, Serializable>();
				datos.put("membership", apiClient.getIdentityAPI().getGroup(um.groupId))
				datos.put("group_id", apiClient.getIdentityAPI().getGroup(um.groupId))
				datos.put("role_id", apiClient.getIdentityAPI().getRole(um.roleId))
				rows.add(datos)
			}
			
			resultado.setData(rows)
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
			e.printStackTrace();
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
	
	public Result getUserProcessApoyoEducativo(RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false;
		try {
			String username = "";
			String password = "";
			
			List<Long> lstId = new ArrayList<Long>();
			
			List<ProcessDeploymentInfo> map = [];
			ProcessDeploymentInfo info;
			Map<String, Object> mapResult = new HashMap<String, Object>();
			mapResult = getProcessId(context);
			List<String> data2 = new ArrayList<String>();
			data2.add(mapResult.get("processIdString").toString());
			resultado.setData(data2);
			resultado.setSuccess(true);
			resultado.setError(errorLog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
			e.printStackTrace();
		}
		finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	private Map<String, Object> getProcessId(RestAPIContext context) {
		Map<String,Object> result = new HashMap<>();
		
		try {
		
		SearchOptionsBuilder searchProcessBuilder = new SearchOptionsBuilder(0,100);
		searchProcessBuilder.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Solicitud de apoyo educativo");
		searchProcessBuilder.filter(ProcessDeploymentInfoSearchDescriptor.ACTIVATION_STATE, ActivationState.ENABLED.name());
		searchProcessBuilder.sort(ProcessDeploymentInfoSearchDescriptor.VERSION, Order.DESC);
		
		ProcessDeploymentInfo procDepInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchProcessBuilder.done()).getResult().get(0);
			result.put("success",true);
			result.put("error",null);
			result.put("errorInfo", null);
			result.put("processIdString", procDepInfo.getProcessId().toString());
			result.put("processIdLong", procDepInfo.getProcessId());
		} catch (Exception e) {
			result.put("success",false);
			result.put("error", e.getMessage());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			result.put("errorInfo", sw.toString());
			sw.close();
			pw.close();
			result.put("processIdString", null);
			result.put("processIdLong", -1L);
		}
		
		return result;
	}
}