package com.anahuac.rest.api.DAO

import static org.junit.Assert.format

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.document.DocumentValue
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessDefinition
import org.bonitasoft.engine.identity.ContactDataCreator
import org.bonitasoft.engine.identity.ContactDataUpdater
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserCreator
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.identity.UserUpdater
import org.bonitasoft.engine.profile.Profile
import org.bonitasoft.engine.profile.ProfileMemberCreator
import org.bonitasoft.engine.search.Order
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.bouncycastle.asn1.x509.sigi.PersonalData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.bonitasoft.engine.bpm.contract.FileInputValue
import org.apache.commons.codec.binary.Base64;

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.catalogos.CatRegistro
import com.anahuac.catalogos.CatRegistroDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Usuarios
import com.anahuac.rest.api.Entity.Custom.AppMenuRole
import com.anahuac.rest.api.Entity.Custom.Menu
import com.anahuac.rest.api.Entity.Custom.MenuParent
import com.anahuac.rest.api.Entity.Custom.ModuloUsuario
import com.anahuac.rest.api.Entity.db.BusinessAppMenu
import com.anahuac.rest.api.Entity.db.CatBitacoraCorreo
import com.anahuac.rest.api.Entity.db.Role
import com.anahuac.rest.api.Utilities.FileDownload
import com.anahuac.rest.api.Utilities.LoadParametros
import com.bonitasoft.web.extension.rest.RestAPIContext
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import groovy.json.JsonSlurper

class UsuariosDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(UsuariosDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;

	public Result getTest(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		List<String> lstResultado = new ArrayList<String>();
		
		lstResultado.add("Informacion de prueba");
		resultado.setData(lstResultado);
		resultado.setSuccess(true);
		
		return resultado;
	}
	
	public Result postRegistrarUsuario(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoN = new Result();
		//List<Usuarios> lstResultado = new ArrayList<Usuarios>();
		List < String > lstResultado = new ArrayList < String > ();
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Long resultReq = 0;
		Long resultReqA = 0;
		Integer start = 0;
		Integer end = 99999;
		Long step = 0;
		Usuarios objUsuario = new Usuarios();
	
		Boolean success = false;
		String error_log = "";
		String success_log = "";
		Boolean closeCon = false;
		
		try {
			String username = "";
			String password = "";
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/

			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			error_log = error_log + " | ";
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			// Datos de la cuenta del Usuario
			UserCreator creator = new UserCreator(object.nombreusuario, object.password);
			creator.setFirstName(object.nombre).setLastName(object.apellido);
			ContactDataCreator proContactDataCreator = new ContactDataCreator().setEmail(object.nombreusuario);
			creator.setProfessionalContactData(proContactDataCreator);
			//inicializa la cuenta con la cual tendras permisos para registrar el usuario
			apiClient.login(username, password)
			error_log = error_log + " | "+apiClient.login(username, password);
			error_log = error_log + " | apiClient.login(username, password)";
			
			closeCon = validarConexion();
			
			    try {
			        con.setAutoCommit(false);
			        pstm = con.prepareStatement(Statements.UPDATE_IDIOMA_REGISTRO_BY_USERNAME);
			        pstm.setString(1, object.idioma);
			        pstm.setString(2, object.nombreusuario);
			
			        resultReq = pstm.executeUpdate();
			        con.commit();
					
			        success = true;
					if(resultReq > 0) {
						error_log = resultReq + " Exito! query update_idioma_registro_by_username_1"
						//error_log = resultReq + " Exito! query update_idioma_registro_by_username_1"
					} else {
						error_log = resultReq + " Error! query update_idioma_registro_by_username_1"
					}
			        
			    } catch (Exception e) {
					con.rollback();
			        if (success == false) {
			            try {
			                con.setAutoCommit(false);
			                pstm = con.prepareStatement(Statements.UPDATE_TABLE_CATREGISTRO);
			                resultReqA = pstm.executeUpdate();
			                con.commit();

			                if (resultReqA > 0) {
			                    con.setAutoCommit(false);
			                    pstm = con.prepareStatement(Statements.UPDATE_IDIOMA_REGISTRO_BY_USERNAME);
			                    pstm.setString(1, parameterIdioma);
			                    pstm.setString(2, object.nombreusuario);
			
			                    resultReq = pstm.executeUpdate();
			                    con.commit();
								if(resultReq > 0) {
									error_log = resultReq + " Exito! query update_idioma_registro_by_username_2"
								} else {
									error_log = resultReq + " Error! query update_idioma_registro_by_username_2"
								}
			                    
			                } else {
								error_log = resultReqA + " Error! query update_table_registro"
							}
			            } catch (Exception e1) {
			                con.rollback();
			                error_log = "Error catch_1: " + e + " error query1: " + resultReq + " error query2: " + resultReqA;
			            }
			        }

			    }
				
			//Registro del usuario
			IdentityAPI identityAPI = apiClient.getIdentityAPI()
			final User user = identityAPI.createUser(creator);
			error_log = error_log + " | final User user = identityAPI.createUser(creator);";
	
			apiClient.login(user.getUserName(), object.password)
			final IdentityAPI identityAPI2 = apiClient.getIdentityAPI()
			error_log = error_log + " | final IdentityAPI identityAPI2 = apiClient.getIdentityAPI()";
			UserMembership membership = identityAPI2.addUserMembership(user.getId(), identityAPI2.getGroupByPath("/ASPIRANTE").getId(), identityAPI2.getRoleByName("ASPIRANTE").getId())
			error_log = error_log + " | UserMembership membership = identityAPI2.addUserMembership(user.getId(), identityAPI2.getGroupByPath(/ASPIRANTE).getId(), identityAPI2.getRoleByName(ASPIRANTE).getId())";
			UserUpdater update_user = new UserUpdater();
			update_user.setEnabled(false);
			error_log = error_log + " | UserUpdater update_user = new UserUpdater();";
			final User user_update = identityAPI.updateUser(user.getId(), update_user);
			error_log = error_log + " | final User user_update= identityAPI.updateUser(user.getId(), update_user);";
	
			def str = jsonSlurper.parseText('{"campus": "' + object.campus + '","correo":"' + object.nombreusuario + '", "codigo": "registrar","isEnviar":false}');
			error_log = error_log + " | def str = jsonSlurper.parseText";
			error_log = error_log + " | " + str;
	
			NotificacionDAO nDAO = new NotificacionDAO();
			resultadoN = nDAO.generateHtml(parameterP, parameterC, "{\"campus\": \""+object.campus+"\", \"correo\":\"" + object.nombreusuario + "\", \"codigo\": \"registrar\", \"isEnviar\":false }", context);
			error_log = error_log + " | " + resultadoN.getError_info();
			error_log = error_log + " | " + resultadoN.getError();
	
			String plantilla = resultadoN.getData().get(0);
			error_log = error_log + " | String plantilla = resultadoN.getData().get(0);";
			
			error_log = error_log + " | if (inputStream != null) {";
			plantilla = plantilla.replace("[href-confirmar]", objProperties.getUrlHost() + "/bonita/apps/login/activate/?correo=" + str.correo + "");
			error_log = error_log + " | plantilla = plantilla.replace([href-confirmar], prop.getProperty";
			MailGunDAO dao = new MailGunDAO();
			resultado = dao.sendEmailPlantilla(str.correo, "Completar Registro", plantilla.replace("\\", ""), "", object.campus, context);
			CatBitacoraCorreo catBitacoraCorreo = new CatBitacoraCorreo();
			catBitacoraCorreo.setCodigo("registrar")
			catBitacoraCorreo.setDe(resultado.getAdditional_data().get(0))
			catBitacoraCorreo.setMensaje("")
			catBitacoraCorreo.setPara(str.correo)
			catBitacoraCorreo.setCampus(object.campus)
			
			if(resultado.success) {
				catBitacoraCorreo.setEstatus("Enviado a Mailgun")
			}else {
				catBitacoraCorreo.setEstatus("Fallido")
			}
			new NotificacionDAO().insertCatBitacoraCorreos(catBitacoraCorreo)
			error_log = error_log + " | resultado = dao.sendEmailPlantilla(str.correo,";
			lstResultado.add(plantilla.replace("\\", ""))
			error_log = error_log + " | lstResultado.add(plantilla.replace(";
			Result resultado2 = new Result();
			resultado2 = updateNumeroContacto(object.nombreusuario,object.numeroContacto);
			error_log = error_log + resultado2.getError();
			resultado.setData(lstResultado);
			error_log = error_log + " | resultado.setData(lstResultado);";
			resultado.setSuccess(true);
			resultado.setError_info(error_log)
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setError_info(error_log)
			resultado.setData(lstResultado);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}finally{
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
		
	public Result postRecuperarPassword(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		
		Result resultadoN = new Result();
		Usuarios objUsuario= new Usuarios();
		Result resultado = new Result();
		
		//List<Usuarios> lstResultado = new ArrayList<Usuarios>();
		List<String> lstResultado = new ArrayList<String>();
		Boolean closeCon = false;
		
		try {
			Result dataResult = new Result();
			List<Object> lstParams;
			String username = "";
			String password = "";
						
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			apiClient.login(username, password);
			
			IdentityAPI identityAPI = apiClient.getIdentityAPI()
			final User user = identityAPI.getUserByUserName(object.nombreusuario);
			dataResult = getUsuarioRegistrado(object.nombreusuario);
			
			if (dataResult.success) {
				lstParams = dataResult.getData();
			} else {
				throw new Exception("No encontro campus");
			}
			//generacion del ramdon
			String asciiUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			String asciiLowerCase = asciiUpperCase.toLowerCase();
			String digits = "1234567890";
			String asciiChars = asciiUpperCase + asciiLowerCase + digits ;
			int length = 8;
			String randomString = generateRandomString(length, asciiChars);
			
			UserUpdater update_user = new UserUpdater();
			update_user.setPassword(randomString);
			final User user_update= identityAPI.updateUser(user.getId(), update_user);
			object.password = randomString;
			def str = jsonSlurper.parseText('{"campus": "'+lstParams[0].grupobonita+'","correo":"'+object.nombreusuario+'", "codigo": "recuperar","isEnviar":false}');

			NotificacionDAO nDAO = new NotificacionDAO();
			 
			resultadoN = nDAO.generateHtml(parameterP, parameterC, "{\"campus\": \""+lstParams[0].grupobonita+"\", \"correo\":\""+object.nombreusuario+"\", \"codigo\": \"recuperar\", \"isEnviar\":false }", context);
			String plantilla = resultadoN.getData().get(0);
			plantilla = plantilla.replace("[password]", object.password );
			MailGunDAO dao = new MailGunDAO();
			resultado = dao.sendEmailPlantilla(object.nombreusuario,"Nueva contraseña",plantilla.replace("\\", ""),"",lstParams[0].grupobonita+"", context);
			
			dataResult = updatePassword(object.password,object.nombreusuario);
			/*if (dataResult.success) {
				
			} else {
				throw new Exception("no pudo guardar la contraseña, se cambio ");
			}*/
			lstResultado.add(plantilla);
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public Result postRecuperarPasswordAdministrativo(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		
		Result resultadoN = new Result();
		Usuarios objUsuario= new Usuarios();
		Result resultado = new Result();
		Long userLogged = 0L;
		String campus = "";
		
		//List<Usuarios> lstResultado = new ArrayList<Usuarios>();
		List<String> lstResultado = new ArrayList<String>();
		try {
			Result dataResult = new Result();
			List<Object> lstParams;
			String username = "";
			String password = "";
			Properties prop = new Properties();
			String propFileName = "configuration.properties";
			InputStream inputStream;
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			apiClient.login(username, password);
			
			IdentityAPI identityAPI = apiClient.getIdentityAPI()
			final User user = identityAPI.getUserByUserName(object.nombreusuario);
			
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			
			//userLogged = context.getApiSession().getUserId();
			userLogged = user.getId();
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				if(campus.length() < 1 ) {
					for(CatCampus rowGrupo : lstCatCampus) {
						if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
							campus = objUserMembership.getGroupName();
							break;
						}
					}
				}
			}
			//generacion del ramdon
			String asciiUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			String asciiLowerCase = asciiUpperCase.toLowerCase();
			String digits = "1234567890";
			String asciiChars = asciiUpperCase + asciiLowerCase + digits;
			int length = 8;
			String randomString = generateRandomString(length, asciiChars);
			
			UserUpdater update_user = new UserUpdater();
			update_user.setPassword(randomString);
			final User user_update= identityAPI.updateUser(user.getId(), update_user);
			object.password = randomString;
			def str = jsonSlurper.parseText('{"campus": "'+campus+'","correo":"'+object.nombreusuario+'", "codigo": "recuperar","isEnviar":false}');
			NotificacionDAO nDAO = new NotificacionDAO();
			 
			resultadoN = nDAO.generateHtml(parameterP, parameterC, "{\"campus\": \""+campus+"\", \"correo\":\""+object.nombreusuario+"\", \"codigo\": \"recuperar\", \"isEnviar\":false }", context);
			String plantilla = resultadoN.getData().get(0);
			plantilla = plantilla.replace("[password]", object.password );
			plantilla = plantilla.replace("[NOMBRE]", (user.getFirstName()+" "+user.getLastName()) );
			MailGunDAO dao = new MailGunDAO();
			resultado = dao.sendEmailPlantilla(object.nombreusuario,"Nueva contraseña",plantilla.replace("\\", ""),"",campus+"", context);
			lstResultado.add(plantilla);
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
			e.printStackTrace();
		}
		return resultado;
	}
	
	
	public Result postHabilitarUsaurio(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		Usuarios objUsuario= new Usuarios();
		Result resultado = new Result();
		//List<Usuarios> lstResultado = new ArrayList<Usuarios>();
		List<String> lstResultado = new ArrayList<String>();
		Boolean closeCon = false;
		try {
			String username = "";
			String password = "";
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/

			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
			
			IdentityAPI identityAPI = apiClient.getIdentityAPI()
			final User user = identityAPI.getUserByUserName(object.nombreusuario);
			
			resultado = enviarTarea(object.nombreusuario, context);
			
			UserUpdater update_user = new UserUpdater();
			update_user.setEnabled(true);
			final User user_update= identityAPI.updateUser(user.getId(), update_user);
		
			lstResultado.add(user_update);
			
			//enviarTarea(parameterP, parameterC, jsonData, context);
			
			lstResultado.add(object.nombreusuario);
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public Result enviarTarea(String correo,RestAPIContext context) {
		Result resultado = new Result();
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		CatRegistro objCatRegistro = new CatRegistro();
		String errorLog ="";
		Boolean closeCon = false;
		try {
			String username = "";
			String password = "";
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			/*def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);*/

			errorLog = errorLog + "";
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
			
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			searchBuilder.filter(HumanTaskInstanceSearchDescriptor.NAME, "Validar Cuenta");
			
			final SearchOptions searchOptions = searchBuilder.done();
			SearchResult<HumanTaskInstance>  SearchHumanTaskInstanceSearch = apiClient.getProcessAPI().searchHumanTaskInstances(searchOptions);
			List<HumanTaskInstance> lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
			def catRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstanceSearch) {
				lstCatRegistro = catRegistroDAO.findByCaseId(objHumanTaskInstance.getRootContainerId(), 0, 1)
				if(lstCatRegistro != null) {
					if(lstCatRegistro.size() > 0) {
						objCatRegistro = new CatRegistro();
						objCatRegistro = lstCatRegistro.get(0);
						if(objCatRegistro.getCorreoelectronico().equals(correo)) {
							apiClient.getProcessAPI().assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
							apiClient.getProcessAPI().executeFlowNode(objHumanTaskInstance.getId());
						}
					}
				}
			}
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public Result getHabilitarUsaurio(Integer parameterP,Integer parameterC, String correo, RestAPIContext context) {
		Usuarios objUsuario= new Usuarios();
		Result resultado = new Result();
		//List<Usuarios> lstResultado = new ArrayList<Usuarios>();
		List<String> lstResultado = new ArrayList<String>();
		NotificacionDAO nDAO = new NotificacionDAO();
		
		Boolean closeCon = false;
		
		try {
			String username = "";
			String password = "";
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			apiClient.login(username, password)
			IdentityAPI identityAPI = apiClient.getIdentityAPI()
			final User user = identityAPI.getUserByUserName(correo);
			if(!user.isEnabled()) {
				UserUpdater update_user = new UserUpdater();
				update_user.setEnabled(true);
				final User user_update= identityAPI.updateUser(user.getId(), update_user);
				
				resultado = enviarTarea(correo, context);
				resultado = nDAO.generateHtml(parameterP, parameterC, "{\"campus\": \"CAMPUS-PUEBLA\", \"correo\":\""+correo+"\", \"codigo\": \"activado\", \"isEnviar\":false }", context);
			}else {
				resultado = nDAO.generateHtml(parameterP, parameterC, "{\"campus\": \"CAMPUS-PUEBLA\", \"correo\":\""+correo+"\", \"codigo\": \"usado\", \"isEnviar\":false }", context);
			}
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado;
	}

	public Result getUsuarios(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="WHERE role.name not in ('ADMINISTRADOR','TI SERUA') AND user_.userName!='Administrador'", orderby="ORDER BY ", errorlog="",campus="";
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		List<String> lstGrupo = new ArrayList<String>();
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			ModuloUsuario row = new ModuloUsuario()
			List<ModuloUsuario> rows = new ArrayList<ModuloUsuario>();
			closeCon = validarConexionBonita();
			
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				lstGrupo.add(objUserMembership.getGroupId());
				//break;
				/*for(CatCampus rowGrupo : lstCatCampus) {
					if(objUserMembership.getGroupId().equals(rowGrupo.getPersistenceId())) {
						
					}
				}*/
			}
			
			if(lstGrupo.size()>0) {
				campus+=" ("
			}
			for(Integer i=0; i<lstGrupo.size(); i++) {
				String campusMiembro=lstGrupo.get(i);
				campus+="group_.id="+campusMiembro
				if(i==(lstGrupo.size()-1)) {
					campus+=") "
				}
				else {
					campus+=" OR "
				}
			}
			
			Boolean hayCampus=false;
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					case "ID":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" user_.id ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=[valor]"
						}else {
							where+="=[valor]"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					
					case "CAMPUS":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" group_.id ";
					if(filtro.get("operador").equals("Igual a")) {
						if(filtro.get("valor").toString().contains("(")) {
							where+="IN [valor]"
						}else {
							where+="=[valor]"
						}
						
					}else {
						where+="=[valor]"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					hayCampus= true;
					break;
					
					case "NOMBRE(S)":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(user_.firstname) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					
					case "APELLIDOS":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(user_.lastname) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					
					case "FECHA CREACIÓN":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" TO_CHAR((TIMESTAMP 'epoch' + user_.creationdate  * interval '1 ms'),'YYYY-MM-DD') ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					
					case "USUARIO":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" LOWER(user_.username) ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=LOWER('[valor]')"
					}else {
						where+="LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
				break;
				
				case "ÚLTIMA CONEXIÓN":
				if(where.contains("WHERE")) {
					where+= " AND "
				}else {
					where+= " WHERE "
				}
				where +=" TO_CHAR((TIMESTAMP 'epoch' + ul.lastconnection  * interval '1 ms'),'YYYY-MM-DD') ";
				if(filtro.get("operador").equals("Igual a")) {
					where+="='[valor]'"
				}else {
					where+="LIKE '%[valor]%'"
				}
				where = where.replace("[valor]", filtro.get("valor"))
			break;
				
				}
			}
			switch(object.orderby) {
				case "ID":
				orderby+="user_.id";
				break;
				case"NOMBRE(S)":
				orderby+="user_.firstname"
				break;
				 case"APELLIDOS":
				 orderby+="user_.lastname"
				 break;
				 case"FECHA CREACIÓN":
				 orderby+="user_.creationdate"
				 break;
				 case"USUARIO":
				 orderby+="user_.userName"
				 break;
				 case"ÚLTIMA CONEXIÓN":
				 orderby+="ul.lastconnection"
				 break;
				default:
				orderby+="user_.firstname";
				break;
			}
			orderby+=" "+object.orientation;
			String consulta = ModuloUsuario.GET_USUARIOS_CUSTOM
			/*if(where.length()>0) {
				if(!hayCampus) {
					where+=" AND "+campus;
				}
			}else if(!hayCampus) {
				where+=" WHERE "+campus;
			}*/
			consulta=consulta.replace("[WHERE]", where);
			//consulta=consulta.replace("[CAMPUS]", campus);
			errorlog+="consulta:"
			errorlog+=consulta
			resultado.setError_info(consulta.replace("user_.enabled, user_.firstname, user_.lastname, user_.creationdate, user_.userName, ul.lastconnection last_connection,user_.id, STRING_AGG(role.name || ' en ' || group_.name, ',' order by role.name ) membresia", "COUNT(user_.id) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("GROUP BY user_.enabled, user_.firstname, user_.lastname, user_.creationdate, user_.userName, ul.lastconnection,user_.id", ""))
				pstm = con.prepareStatement(consulta.replace("user_.enabled, user_.firstname, user_.lastname, user_.creationdate, user_.userName, ul.lastconnection last_connection,user_.id, STRING_AGG(role.name || ' en ' || group_.name, ',' order by role.name ) membresia", "COUNT(user_.id) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("GROUP BY user_.enabled, user_.firstname, user_.lastname, user_.creationdate, user_.userName, ul.lastconnection,user_.id", ""))
				rs = pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+="consulta:"
				errorlog+=consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()
				while(rs.next()) {
					row = new ModuloUsuario()
					row.setFirstname(rs.getString("firstname"))
					row.setId(rs.getLong("id"))
					row.setLast_connection(rs.getLong("last_connection"))
					row.setLastname(rs.getString("lastname"))
					row.setMembresia(rs.getString("membresia"))
					row.setUserName(rs.getString("username"))
					row.setCreation_date(rs.getLong("creationdate"))
					row.setEnabled(rs.getBoolean("enabled"))
					rows.add(row)
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
				LOGGER.error "[ERROR] " + e.getMessage();
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Result recoveryData(String jsonData) {
		String error_log=""
		Result resultado = new Result();
		Boolean closeCon = false;
		Long id =1L;
		Long flownodedefinitionid=0L;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			closeCon = validarConexionBonita();
			error_log+="[1] Buscando si hay tareas activas "
			pstm = con.prepareStatement("SELECT displayName FROM flownode_instance where kind='user' and rootcontainerid=?")
			pstm.setLong(1, new Long(object.caseId))
			rs = pstm.executeQuery()
			if(!rs.next()) {
				error_log+="[2] Buscando si existe la tabla temporal_id "
				pstm = con.prepareStatement("SELECT table_name FROM information_schema.tables   WHERE  table_name = 'temporal_id'")
				rs = pstm.executeQuery()
				if(rs.next()) {
					error_log+="[3] Actualizando tabla temporal_id "
					pstm = con.prepareStatement("update temporal_id set id=(SELECT id+1 FROM temporal_id limit 1)")
					pstm.execute()
					
					pstm = con.prepareStatement("SELECT id FROM temporal_id limit 1")
					rs = pstm.executeQuery()
					if(rs.next()) {
						id=rs.getLong("id")
					}
				}else {
					error_log+="[3] Creando tabla temporal_id "
					pstm = con.prepareStatement("CREATE TABLE temporal_id (id bigint)")
					pstm.execute()
					pstm = con.prepareStatement("INSERT INTO temporal_id (id) values (1)")
					pstm.execute()
				}
				
				error_log+="[4] Buscando última tarea abortada "
				pstm = con.prepareStatement("SELECT flownodedefinitionid, name, displayname FROM arch_flownode_instance afi where afi.id=(SELECT max(id) from arch_flownode_instance where rootcontainerid=? and kind='user' and statename='aborted' limit 1)")
				pstm.setLong(1, new Long(object.caseId))
				rs = pstm.executeQuery()
				if(rs.next()) {
					error_log+="[5] Última tarea " + rs.getString("name") + " "
					flownodedefinitionid=rs.getLong("flownodedefinitionid")
					
					error_log+="[6] Actualizando timer fechavencimientoperiodo "
					pstm = con.prepareStatement("update data_instance set longvalue=7942405330000 where containerid=? and name='fechavencimientoperiodo';")
					pstm.setLong(1, new Long(object.caseId))
					pstm.execute()
					
					
					error_log+="[7] Recuperando tarea "
					pstm = con.prepareStatement("INSERT INTO flownode_instance (tenantid, id, flownodedefinitionid, kind, rootcontainerid, parentcontainerid, name, displayname, displaydescription, stateid, statename, prev_state_id, terminal, stable, actorid, assigneeid, reachedstatedate, lastupdatedate, expectedenddate, claimeddate, priority, gatewaytype, hitbys, statecategory, logicalgroup1, logicalgroup2, logicalgroup3, logicalgroup4, loop_counter, loop_max, description, sequential, loopdatainputref, loopdataoutputref, datainputitemref, dataoutputitemref, loopcardinality, nbactiveinst, nbcompletedinst, nbterminatedinst, executedby, executedbysubstitute, activityinstanceid, state_executing, abortedbyboundary, triggeredbyevent, interrupting, tokencount) VALUES ( '1', ?, ?, 'user', ?, ?, '"+rs.getString("name")+"', '"+rs.getString("displayname")+"', null, '4', 'ready', '32', false, true, '911', '0', '1630625311223', '1630625311223', null, '0', '2', null, null, 'NORMAL', ?, ?, '0', ?, '-1', null, null, null, null, null, null, null, null, null, null, null, '0', '0', null, false, '0', null, null, '0')")
					pstm.setLong(1, id)
					pstm.setLong(2, flownodedefinitionid)
					pstm.setLong(3, new Long(object.caseId))
					pstm.setLong(4, new Long(object.caseId))
					pstm.setLong(5, Long.valueOf(object.processDefinitionId))
					pstm.setLong(6, new Long(object.caseId))
					pstm.setLong(7, new Long(object.caseId))
					pstm.execute()
					
					new DBConnect().closeObj(con, stm, rs, pstm)
					
					closeCon = validarConexion();
					
					pstm = con.prepareStatement("SELECT  estatussolicitud FROM solicituddeadmision where caseid=?")
					pstm.setString(1, object.caseId.toString())
					rs= pstm.executeQuery()
					if(rs.next()) {
						
						pstm = con.prepareStatement("UPDATE solicituddeadmision set estatussolicitud=? where caseid=?;")
						pstm.setString(1, rs.getString("estatussolicitud").split(":")[1].trim())
						pstm.setString(2, object.caseId.toString())
						pstm.execute()
					}
					resultado.setSuccess(true)
					
				}else {
					resultado.setError(" No tiene tareas abortadas")
					resultado.setSuccess(true)
					resultado.setError_info("No es posible recuperar datos de un proceso sin tareas abortadas")
				}
				
				
				
				
			}else {
				resultado.setError(" Tiene tareas activas")
				resultado.setSuccess(true)
				resultado.setError_info(" Revisar caso para tareas activas ")
			}
				
			} catch (Exception e) {
				LOGGER.error "[ERROR] " + e.getMessage();
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				resultado.setError_info(error_log)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true
		}
		return retorno;
	}

	private static String generateRandomString(int length, String seedChars) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		Random rand = new Random();
		while (i < length) {
			sb.append(seedChars.charAt(rand.nextInt(seedChars.length())));
			i++;
		}
		return sb.toString();
	}
	
	public Result getUsuariosRegistrados(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
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
			
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs= pstm.executeQuery();
			if(rs.next()) {
				SSA = rs.getString("valor")
			}
			
			assert object instanceof Map;
			where+=" WHERE sda.iseliminado=false and (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado = false ) "
			where+=" AND (sda.ESTATUSSOLICITUD <> 'Solicitud rechazada' AND sda.ESTATUSSOLICITUD <> 'Aspirantes registrados sin validación de cuenta' AND sda.ESTATUSSOLICITUD <> 'Aspirantes registrados con validación de cuenta' AND sda.ESTATUSSOLICITUD <> 'Solicitud en progreso' AND sda.ESTATUSSOLICITUD <> 'Aspirante migrado' AND sda.ESTATUSSOLICITUD <> 'estatus1' AND sda.ESTATUSSOLICITUD <> 'estatus2' AND sda.ESTATUSSOLICITUD <> 'estatus3' AND sda.ESTATUSSOLICITUD <> 'Solicitud vencida') AND (sda.ESTATUSSOLICITUD != 'Solicitud caduca') AND (sda.ESTATUSSOLICITUD not like '%Solicitud vencida en:%') AND (sda.ESTATUSSOLICITUD not like '%Período vencido en:%')"
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
			
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				String consulta = Statements.GET_USUARIOS_REGISTRADOS
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
						case "NOMBRE,EMAIL,CURP":
						errorlog+="NOMBRE,EMAIL,CURP"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(concat(sda.apellidopaterno,' ', sda.apellidomaterno,' ', sda.primernombre,' ', sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;


						case "CAMPUS,PROGRAMA,INGRESO":
							errorlog+="CAMPUS,PROGRAMA,INGRESO"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" ( LOWER(gestionescolar.DESCRIPCION) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
							
							where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%')  ";
							where = where.replace("[valor]", filtro.get("valor"))
							
							where +=" OR LOWER(campus.DESCRIPCION) like lower('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
							break;


						case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
							errorlog+="ESTADO,PREPARATORIA,PROMEDIO"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +="( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.bachillerato ELSE prepa.descripcion END) like lower('%[valor]%')  ";
							where = where.replace("[valor]", filtro.get("valor"))
							
							where +=" OR LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
							
							where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
							break;

						case "ESTATUS,TIPO":
							errorlog+="ESTADO,PREPARATORIA,PROMEDIO"
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +="( LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') ";
							where = where.replace("[valor]", filtro.get("valor"))
							
							where +=" OR LOWER(tipoalumno.descripcion) like lower('%[valor]%') )";
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "NOMBRE":
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
						case "EMAIL":
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
							campus +=" AND LOWER(campus.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								campus+="=LOWER('[valor]')"
							}else {
								campus+="LIKE LOWER('%[valor]%')"
							}
							campus = campus.replace("[valor]", filtro.get("valor"))
							break;
						case "PREPARATORIA":
							bachillerato +=" AND LOWER(prepa.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								bachillerato+="=LOWER('[valor]')"
							}else {
								bachillerato+="LIKE LOWER('%[valor]%')"
							}
							bachillerato = bachillerato.replace("[valor]", filtro.get("valor"))
							break;
						case "PROGRAMA":
							programa +=" AND LOWER(gestionescolar.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								programa+="=LOWER('[valor]')"
							}else {
								programa+="LIKE LOWER('%[valor]%')"
							}
							programa = programa.replace("[valor]", filtro.get("valor"))
							break;
						case "INGRESO":
							ingreso +=" AND LOWER(periodo.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								ingreso+="=LOWER('[valor]')"
							}else {
								ingreso+="LIKE LOWER('%[valor]%')"
							}
							ingreso = ingreso.replace("[valor]", filtro.get("valor"))
							break;
						case "PROCEDENCIA":
							estado +=" AND LOWER( CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) ";
							if(filtro.get("operador").equals("Igual a")) {
								estado+="=LOWER('[valor]')"
							}else {
								estado+="LIKE LOWER('%[valor]%')"
							}
							estado = estado.replace("[valor]", filtro.get("valor"))
							break;
						case "PROMEDIO":
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
							tipoalumno +=" AND LOWER(da.TIPOALUMNO) ";
							if(filtro.get("operador").equals("Igual a")) {
								tipoalumno+="=LOWER('[valor]')"
							}else {
								tipoalumno+="LIKE LOWER('%[valor]%')"
							}
							tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
							break;
						case "ID BANNER":
							tipoalumno +=" AND LOWER(da.idbanner) ";
							if(filtro.get("operador").equals("Igual a")) {
								tipoalumno+="=LOWER('[valor]')"
							}else {
								tipoalumno+="LIKE LOWER('%[valor]%')"
							}
							tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
							break;
						case "LISTAROJA":
							tipoalumno +=" AND LOWER(da.observacionesListaRoja) ";
							if(filtro.get("operador").equals("Igual a")) {
								tipoalumno+="=LOWER('[valor]')"
							}else {
								tipoalumno+="LIKE LOWER('%[valor]%')"
							}
							tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
							break;
						case "RECHAZO":
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
					case "PROCEDENCIA":
					orderby +="procedencia";
					break;
					case "PROMEDIO":
					orderby+="sda.PROMEDIOGENERAL";
					break;
					case "ESTATUS":
					orderby+="sda.ESTATUSSOLICITUD";
					break;
					case "TIPO":
					orderby+="tipoalumno.descripcion";
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
				
				String consultaCount = Statements.GET_COUNT_USUARIOS_REGISTRADOS;
				consultaCount=consultaCount.replace("[CAMPUS]", campus)
				consultaCount=consultaCount.replace("[PROGRAMA]", programa)
				consultaCount=consultaCount.replace("[INGRESO]", ingreso)
				consultaCount=consultaCount.replace("[ESTADO]", estado)
				consultaCount=consultaCount.replace("[BACHILLERATO]", bachillerato)
				consultaCount=consultaCount.replace("[TIPOALUMNO]", tipoalumno)
				consultaCount=consultaCount.replace("[WHERE]", where)
				consultaCount=consultaCount.replace("[LIMITOFFSET]","");
				consultaCount=consultaCount.replace("[ORDERBY]", "");
				pstm = con.prepareStatement(consultaCount);
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog += " ///////*/*/*/*/*/ la consulta es: " + consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				def num = Math.random();
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						
						String label = metaData.getColumnLabel(i).toLowerCase();
						if(label.equals("urlconstancia") || label.equals("urlcartaaa") || label.equals("urlresultadopaa") || label.equals("urlactanacimiento") || label.equals("urldescuentos")) {
							if(rs.getString(i).equals("null") || rs.getString(i) == null) {
								columns.put(metaData.getColumnLabel(i).toLowerCase(), "");
							} else {
								columns.put(metaData.getColumnLabel(i).toLowerCase(),  rs.getString(label) + SSA+"&v="+num );
							}
							
						} else {
							columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
							if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
								String encoded = "";
								try {
									String urlFoto = rs.getString("urlfoto");
									if(urlFoto != null && !urlFoto.isEmpty()) {
										columns.put("fotografiab64",  base64Imagen((rs.getString("urlfoto") + SSA+"&v="+num)) );
									}else {
										List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
										for(Document doc : doc1) {
											encoded = "../API/formsDocumentImage?document="+doc.getId();
											columns.put("fotografiab64", encoded);
										}
									}
								}catch(Exception e) {
									LOGGER.error "[ERROR] " + e.getMessage();
									columns.put("fotografiab64", "");
									errorlog+= ""+e.getMessage();
								}
							}
						}
					}
					
					rows.add(columns);
				}
				resultado.setSuccess(true)
				
				
				//resultado.setError(consulta);
				resultado.setData(rows)
				
			} catch (Exception e) {
				LOGGER.error "[ERROR] " + e.getMessage();
			
			//resultado.setError_info(consulta)
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}

	public Result updateUsuarioRegistrado(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false;
		try {
			ProcessAPI processAPI = context.getApiClient().getProcessAPI()
			String username = "";
			String password = "";
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			assert object instanceof Map;

			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)

			errorLog += " Antes del update "
			closeCon = validarConexion();
			errorLog += " closeCon " + closeCon
			
			String consulta = "";
			/*if(object.catResidencia != null) {
				consulta += " ,catResidencia_pid = "+object.catGestionEscolar.persistenceId;
			}
					
			if(object.catTipoAlumno != null) {
				consulta += " ,catTipoAlumno_pid = "+object.catGestionEscolar.persistenceId;
			}
				
			if(object.catTipoAdmision != null) {
				consulta += " ,catTipoAdmision_pid = "+object.catGestionEscolar.persistenceId;
			}*/
			
			if(object.catLugarExamen != null) {
				consulta += " ,catlugarexamen_pid = "+object.catLugarExamen.persistenceId;
			}
			
			if(object.tienePAA != null && object.tienePAA) {
				consulta += " , resultadoPAA  = "+object.resultadoPAA +(object.resultadoPAA ==0?", tienePAA = 'false'":", tienePAA = 'true'");
			} else if(!object.tienePAA && object.resultadoPAA > 0){
				consulta += " , resultadoPAA  = "+object.resultadoPAA +", tienePAA = 'true'";
			}else if(!object.tienePAA && object.resultadoPAA == 0) {
				consulta += " , resultadoPAA  = 0 , tienePAA = 'false'";
			}
			
			consulta+=" WHERE ";
			con.setAutoCommit(false)
			
			if(object.telefonoCelular != null ) {
				errorLog +="actualizar contacto";
				pstm = con.prepareStatement(Statements.UPDATE_REGISTRO_NUMEROCONTACTO);
				pstm.setString(1,object.telefonoCelular);
				pstm.setString(2,object.correoelectronico);
				pstm.executeUpdate();
			}		
			
			errorLog += " consulta = "+Statements.UPDATE_USUARIOS_REGISTRADOS.replace("WHERE", consulta)
			pstm = con.prepareStatement(Statements.UPDATE_USUARIOS_REGISTRADOS.replace("WHERE", consulta))
			pstm.setString(1,object.primernombre);
			pstm.setString(2,object.segundonombre);
			pstm.setString(3,object.apellidopaterno);
			pstm.setString(4,object.apellidomaterno);
			pstm.setString(5,object.correoelectronico);
			pstm.setLong(6, object.campusestudio);
			pstm.setLong(7, object.licenciatura);
			if(object.periodo == null){
				pstm.setNull(8, java.sql.Types.BIGINT);
			}else{
				pstm.setLong(8, object.periodo);
			}
			if(object.propedeutico == null) {
				pstm.setNull(9, java.sql.Types.BIGINT);
			}else {
				pstm.setLong(9, object.propedeutico);
			}
			if(object.campus == null){
				pstm.setNull(10, java.sql.Types.BIGINT);
			}else{
				pstm.setLong(10, object.campus);
			}
			if(object.sexo == null){
				pstm.setNull(11, java.sql.Types.BIGINT);
			}else{
				pstm.setLong(11, object.sexo);
			}
			pstm.setString(12, object.fechanacimiento?.toString().replace("t","T"));
			/*if(object.estado == null){
				pstm.setNull(13, java.sql.Types.BIGINT);
			}else{
				pstm.setLong(13, object.estado);
			}
			pstm.setString(14, object.estadoextranjero);*/
			pstm.setLong(13, object.bachillerato);
			if(object.nombrebachillerato == null){
				pstm.setNull(14, java.sql.Types.VARCHAR);
			}else{
				pstm.setString(14, object.nombrebachillerato);
			}
			pstm.setString(15, object.paisbachillerato);
			pstm.setString(16, object.estadobachillerato);
			pstm.setString(17, object.ciudadbachillerato);
			pstm.setString(18, object.promedio);
			if(object.catEstadoExamen == null) {
				pstm.setNull(19, java.sql.Types.BIGINT);
			}else {
				pstm.setLong(19, object.catEstadoExamen.persistenceId)
			}
			
			if(object.catCiudadExamen == null) {
				pstm.setNull(20, java.sql.Types.BIGINT);
			}else {
				pstm.setLong(20, object.catCiudadExamen.persistenceId)
			}
			
			if(object.catPaisExamen == null) {
				pstm.setNull(21, java.sql.Types.BIGINT);
			}else {
				pstm.setLong(21, object.catPaisExamen.persistenceId)
			}
			pstm.setString(22, object.telefonoCelular);
			
			// el where final
			pstm.setLong(23, Long.valueOf(object.caseid));
			pstm.executeUpdate();
			String detalleSolicitud ="";
			
			if(object.catResidencia != null) {
				detalleSolicitud +=(detalleSolicitud.length() >=1?",":"" )+" catResidencia_pid = "+object.catResidencia.persistenceId;
			}
				 
			if(object.catTipoAlumno != null) {
				 detalleSolicitud +=(detalleSolicitud.length() >=1?",":"" ) + " catTipoAlumno_pid = "+object.catTipoAlumno.persistenceId;
			}
			 
			if(object.catTipoAdmision != null) {
				 detalleSolicitud +=(detalleSolicitud.length() >=1?",":"" ) +" catTipoAdmision_pid = "+object.catTipoAdmision.persistenceId;
			}
			if(object.cbCoincide != null) {
				detalleSolicitud +=(detalleSolicitud.length() >=1?",":"" ) +" cbCoincide = '"+object.cbCoincide+"'";
			}
			if(object.observacionListaRoja != null) {
				detalleSolicitud +=(detalleSolicitud.length() >=1?",":"" ) +" observacionesListaRoja = '"+object.observacionListaRoja+"'";
			}
			if(object.descuento != null) {
				detalleSolicitud +=(detalleSolicitud.length() >=1?",":"" ) +" descuento = "+object.descuento;
			}
			
			if(detalleSolicitud.length() >=1) {
				detalleSolicitud+= " WHERE  caseid = '"+Long.valueOf(object.caseid)+"'"
				errorLog+=" segunda consulta : "+Statements.UPDATE_DETALLE_SOLICITUD.replace("[SETS]",detalleSolicitud)
				pstm = con.prepareStatement(Statements.UPDATE_DETALLE_SOLICITUD.replace("[SETS]",detalleSolicitud))
				pstm.executeUpdate();
			}
			
			//Document doc;
			DocumentValue dv;
			errorLog+=" documentos :"
			byte[] b;
			if(object.Documentos[0].url.length() > 1) {
				errorLog+=" Imagen"
				b = Base64.decodeBase64(object.Documentos[0].b64);
				
//				if(object.Documentos[0].documentId != null) {
//					dv = new DocumentValue(object.Documentos[0].documentId, b, object.Documentos[0].valor.contentType, object.Documentos[0].valor.filename);
//					dv.setIndex(0);
//					processAPI.updateDocument(object.Documentos[0].documentId,dv)
//				}else {
//					dv = new DocumentValue( b, object.Documentos[0].valor.contentType, object.Documentos[0].valor.filename);
//					processAPI.addDocument(Long.valueOf(object.caseid),"fotoPasaporte","",dv)
//				}
				dv = new DocumentValue( b, object.Documentos[0].valor.contentType, object.Documentos[0].valor.filename);
//				processAPI.addDocument(Long.valueOf(object.caseid),"fotoPasaporte","",dv)
				List<DocumentValue> lstDV = new ArrayList<DocumentValue>();
				lstDV.add(dv);
				processAPI.setDocumentList(Long.valueOf(object.caseid), "fotoPasaporte", lstDV);
			}
			if(object.Documentos[1].url.length() > 1) {
				errorLog+=" acta"
				b = Base64.decodeBase64(object.Documentos[1].b64);
//				if(object.Documentos[1].documentId != null) {
//					dv = new DocumentValue(object.Documentos[1].documentId, b, object.Documentos[1].valor.contentType, object.Documentos[1].valor.filename);
//					//dv.setIndex(0);
//					processAPI.updateDocument(object.Documentos[1].documentId,dv)
//				}else {
//					dv = new DocumentValue(b, object.Documentos[1].valor.contentType, object.Documentos[1].valor.filename);
//					processAPI.addDocument(Long.valueOf(object.caseid), "actaNacimiento", "", dv)
//				}
				
				dv = new DocumentValue(b, object.Documentos[1].valor.contentType, object.Documentos[1].valor.filename);
				List<DocumentValue> lstDV = new ArrayList<DocumentValue>();
				lstDV.add(dv);
				processAPI.setDocumentList(Long.valueOf(object.caseid), "actaNacimiento", lstDV);
			}
			if(object.Documentos[2].url.length() > 1) {
				errorLog+=" constancia"
				b = Base64.decodeBase64(object.Documentos[2].b64);
				
//				if(object.Documentos[3].documentId != null) {
//					dv =new DocumentValue(object.Documentos[2].documentId, b, object.Documentos[2].valor.contentType, object.Documentos[2].valor.filename);
//				}else {
//					dv =new DocumentValue(b, object.Documentos[2].valor.contentType, object.Documentos[2].valor.filename);
//				}
//				dv.setIndex(0);
				dv = new DocumentValue( b, object.Documentos[2].valor.contentType, object.Documentos[2].valor.filename);
				processAPI.addDocument(Long.valueOf(object.caseid),"constancia","",dv)

//				dv = new DocumentValue(b, object.Documentos[2].valor.contentType, object.Documentos[2].valor.filename);
				
//				dv = new DocumentValue( b, object.Documentos[2].valor.contentType, object.Documentos[2].valor.filename);
//				List<DocumentValue> lstDV = new ArrayList<DocumentValue>();
//				lstDV.add(dv);
//				processAPI.setDocumentList(Long.valueOf(object.caseid), "constancia", lstDV);
			}
			if(object.Documentos[3].url.length() > 1) {
				
				errorLog+=" cartaAA"
				b = Base64.decodeBase64(object.Documentos[3].b64);
				
//				if(object.Documentos[3].documentId != null) {
//					dv = new DocumentValue(object.Documentos[3].documentId, b, object.Documentos[3].valor.contentType, object.Documentos[3].valor.filename);
//					//dv.setIndex(0);
//					processAPI.updateDocument(object.Documentos[3].documentId,dv)
//				}else {
//					dv = new DocumentValue(b, object.Documentos[3].valor.contentType, object.Documentos[3].valor.filename);
//					processAPI.addDocument(Long.valueOf(object.caseid),"cartaAA","",dv)
//				}
				dv = new DocumentValue(b, object.Documentos[3].valor.contentType, object.Documentos[3].valor.filename);
//				processAPI.addDocument(Long.valueOf(object.caseid),"cartaAA","",dv)
				List<DocumentValue> lstDV = new ArrayList<DocumentValue>();
				lstDV.add(dv);
				processAPI.setDocumentList(Long.valueOf(object.caseid), "cartaAA", lstDV);
			}
			
			if(object.Documentos[4].url.length() > 1) {
				
				errorLog+=" resultadoCB"
				b = Base64.decodeBase64(object.Documentos[4].b64);
				
//				if(object.Documentos[4].documentId != null) {
//					dv = new DocumentValue(object.Documentos[4].documentId, b, object.Documentos[4].valor.contentType, object.Documentos[4].valor.filename);
//					//dv.setIndex(0);
//					processAPI.updateDocument(object.Documentos[4].documentId,dv)
//				}else {
//					dv = new DocumentValue(b, object.Documentos[4].valor.contentType, object.Documentos[4].valor.filename);
//					processAPI.addDocument(Long.valueOf(object.caseid),"resultadoCB","",dv)
//				}
				dv = new DocumentValue(b, object.Documentos[4].valor.contentType, object.Documentos[4].valor.filename);
//				processAPI.addDocument(Long.valueOf(object.caseid),"resultadoCB","",dv)
				List<DocumentValue> lstDV = new ArrayList<DocumentValue>();
				lstDV.add(dv);
				processAPI.setDocumentList(Long.valueOf(object.caseid), "resultadoCB", lstDV);
				
			}
			errorLog += " descuento = "+object.descuento
			if( object.descuento.toString().equals("100")) {
				errorLog += " en el if con el caseid " + object.caseid
				SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
				searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID, object.caseid);
				searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);
				final SearchOptions searchOptions = searchBuilder.done();
				SearchResult < HumanTaskInstance > SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
				List < HumanTaskInstance > lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
				errorLog += " en el if "
				for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {
					if (objHumanTaskInstance.getName().equals("Pago de examen")) {
						Map < String, Serializable > inputs = new HashMap < String, Serializable > ();
						inputs.put("conIsPagoValidado", false);
						inputs.put("isPagoTarjeta", false);
						inputs.put("detalleSolicitudInput", null);
						inputs.put("isPagoCondonado", false);
						inputs.put("isDescuento100", true);
	
						processAPI.assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
						processAPI.executeUserTask(objHumanTaskInstance.getId(), inputs);
					}
				}
			}
			HubspotDAO hDAO = new HubspotDAO()
			Result hResultado = hDAO.createOrUpdateUsuarioRegistrado(jsonData)
			
			if(!hResultado.success) {
				resultado.setError("hubspot: "+hResultado.error + " | " + hResultado.error_info)
			}
			
			con.commit();
			resultado.setSuccess(true)
			
		}catch(Exception ex){
			LOGGER.error "[ERROR] " + ex.getMessage();
			
			resultado.setSuccess(false);
			resultado.setError(ex.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result updateCorreoElectronico(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false;
		try {
			errorLog += " Preparando contexto "

			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			assert object instanceof Map;
			errorLog += " Antes del update "
			closeCon = validarConexion();
			con.setAutoCommit(false)
			errorLog += " closeCon " + closeCon
			UserUpdater updater  = new UserUpdater();
			ContactDataUpdater persoContactUpdater = new ContactDataUpdater();
			persoContactUpdater.setEmail(object.correoNuevo)
			ContactDataUpdater proContactUpdater = new ContactDataUpdater();
			proContactUpdater.setEmail(object.correoNuevo)
			updater.setUserName(object.correoNuevo)
			updater.setPersonalContactData(persoContactUpdater)
			updater.setProfessionalContactData(proContactUpdater)
			errorLog += "  " + object.correoAnterior
			Long userId= context.getApiClient().identityAPI.getUserByUserName(object.correoAnterior).id
			context.getApiClient().identityAPI.updateUser(userId, updater)
			String consulta = "";
			
			pstm = con.prepareStatement(Statements.UPDATE_CORREO_ELECTRONICO_CAT_REGISTRO)
			pstm.setString(1,object.correoNuevo);
			pstm.setString(2,object.correoAnterior);
			pstm.execute()
			pstm = con.prepareStatement(Statements.UPDATE_CORREO_ELECTRONICO_SOLICITUDDEADMISION)
			pstm.setString(1,object.correoNuevo);
			pstm.setString(2,object.correoAnterior);
			pstm.execute()
			pstm = con.prepareStatement(Statements.UPDATE_CORREO_ELECTRONICO_SESIONASPIRANTE)
			pstm.setString(1,object.correoNuevo);
			pstm.setString(2,object.correoAnterior);
			pstm.execute()
			pstm = con.prepareStatement(Statements.UPDATE_CORREO_ELECTRONICO_PASELISTA)
			pstm.setString(1,object.correoNuevo);
			pstm.setString(2,object.correoAnterior);
			pstm.execute()
			pstm = con.prepareStatement(Statements.UPDATE_CORREO_ELECTRONICO_ASPIRANTESPRUEBAS)
			pstm.setString(1,object.correoNuevo);
			pstm.setString(2,object.correoAnterior);
			pstm.execute()
			pstm = con.prepareStatement(Statements.UPDATE_CORREO_ELECTRONICO_PLANTILLAREGISTRO)
			pstm.setString(1,object.correoNuevo);
			pstm.setString(2,object.correoAnterior);
			pstm.execute()
			pstm = con.prepareStatement(Statements.UPDATE_CORREO_ELECTRONICO_BITACORACORREO)
			pstm.setString(1,object.correoNuevo);
			pstm.setString(2,object.correoAnterior);
			pstm.execute();
			con.commit();
			resultado.setSuccess(true)
			
		}catch(Exception ex){
			LOGGER.error "[ERROR] " + ex.getMessage();
			resultado.setError_info(errorLog);
			resultado.setSuccess(false);
			resultado.setError(ex.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result getConstanciasHistorico(Long caseId) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexionBonita();
			
			String SSA = "";
			
//			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
//			rs= pstm.executeQuery();
//			if(rs.next()) {
//				SSA = rs.getString("valor")
//			}
			
			pstm = con.prepareStatement(Statements.GET_CONSTANCIAS_HISTORICO);
			pstm.setLong(1, caseId);
			
			rs = pstm.executeQuery()
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					if(metaData.getColumnLabel(i).toLowerCase().equals("urlconstancia")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i) + SSA);
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
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	def DocumentValue optionalUpdateDocument(long documentId, FileInputValue fileFromContract) {
		return fileFromContract
			? new DocumentValue(documentId, fileFromContract.content, fileFromContract.contentType, fileFromContract.fileName)
			: new DocumentValue(documentId)
	}
	
	public Result getDatosUsername(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
		
				List<Map<String, Boolean>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexionBonita();
				pstm = con.prepareStatement(Statements.GET_USERS_BY_USERNAME)
				pstm.setString(1, username)
				
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				while(rs.next()) {	
					rows.add(true);
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
				LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getBusinessAppMenu() {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
				AppMenuRole row = new AppMenuRole()
				Role role = new Role()
				List<AppMenuRole> rows = new ArrayList<AppMenuRole>();
				closeCon = validarConexionBonita();
				pstm = con.prepareStatement(AppMenuRole.GET)
				rs = pstm.executeQuery()
				rows = new ArrayList<BusinessAppMenu>();
				while(rs.next()) {
					row = new AppMenuRole()
					role = new Role()
					row.setApplicationid(rs.getLong("applicationid"))
					row.setApplicationpageid(rs.getLong("applicationpageid"))
					row.setDisplayname(rs.getString("displayname"))
					row.setId(rs.getLong("id"))
					row.setIndex_(rs.getInt("index_"))
					row.setTenantid(rs.getLong("tenantid"))
					role.setId(rs.getLong("roleid"))
					role.setName(rs.getString("rolename"))
					role.setEliminado(false)
					role.setNuevo(false)
					row.setRoles(new ArrayList<Role>())
					if(role.id>0) {
						row.getRoles().add(role)
					}
					if(rows.contains(row)) {
						if(!rows.get(rows.indexOf(row)).roles.contains(role)) {
							
							if(role.id>0) {
								rows.get(rows.indexOf(row)).roles.add(role)
							}
						}
					}else {
						rows.add(row);
					}
					
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
				LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(" "+e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}

	public Result getMenuAdministrativo(RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			
				MenuParent row = new MenuParent()
				List<MenuParent> rows = new ArrayList<MenuParent>();
				closeCon = validarConexionBonita();
				pstm = con.prepareStatement(MenuParent.GET)
				pstm.setLong(1,context.apiSession.userId)
				rs = pstm.executeQuery()
				rows = new ArrayList<MenuParent>();
				while(rs.next()) {
					row = new MenuParent()
					row.setId(rs.getLong("id"))
					row.setIsparent(rs.getBoolean("isparent"))
					row.setUrl(rs.getString("url"))
					row.setToken(rs.getString("token"))
					row.setMenu(rs.getString("menu"))
					row.setDisplayname(rs.getString("Displayname"))
					row.setParent(rs.getString("parent"))
					row.setParentid(rs.getLong("parentid"))
					row.setParenttoken(rs.getString("parenttoken"))
					if(rs.getBoolean("isparent")) {
						row = new MenuParent()
						row.setId(rs.getLong("id"))
						row.setIsparent(rs.getBoolean("isparent"))
						row.setUrl(rs.getString("url"))
						row.setToken(rs.getString("token"))
						row.setMenu(rs.getString("menu"))
						row.setDisplayname(rs.getString("Displayname"))
						row.setParent(rs.getString("parent"))
						row.setParentid(rs.getLong("parentid"))
						row.setParenttoken(rs.getString("parenttoken"))
						row.setChild(new ArrayList<Menu>())
						rows.add(row)
					}else {
						Menu menu = new Menu()
						menu.setId(rs.getLong("id"))
						menu.setIsparent(rs.getBoolean("isparent"))
						menu.setUrl(rs.getString("url"))
						menu.setToken(rs.getString("token"))
						menu.setMenu(rs.getString("menu"))
						menu.setDisplayname(rs.getString("Displayname"))
						menu.setParent(rs.getString("parent"))
						menu.setParentid(rs.getLong("parentid"))
						menu.setParenttoken(rs.getString("parenttoken"))
						if(rows.contains(row)) {
							rows.get(rows.indexOf(row)).getChild().add(menu)
						}
					}
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
				LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			if(e.getMessage().contains("\"app_menu_role\" does not exist")) {
				try {
					pstm = con.prepareStatement(AppMenuRole.CREATE)
					pstm.execute()
					resultado.setError("La tabla app_menu_role no existía, y ya fue creada, favor de ejecutar la consulta de nuevo.")
				} catch (Exception e2) {
					LOGGER.error "[ERROR] " + e2.getMessage();
					resultado.setError("falló al crear tabla "+e2.getMessage());
				}
				
			}else {
				resultado.setError("No entró al crear tabla "+e.getMessage());
			}
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}

	public Result updateBusinessAppMenu(AppMenuRole row) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
				
				closeCon = validarConexionBonita();
				for(Role rol : row.roles) {
					if(rol.nuevo && !rol.eliminado) {
						pstm = con.prepareStatement(AppMenuRole.INSERT)
						pstm.setString(1, row.getDisplayname())
						pstm.setLong(2, rol.getId())
						pstm.execute();
					}else if(!rol.nuevo && rol.eliminado) {
						pstm = con.prepareStatement(AppMenuRole.DELETE)
						pstm.setString(1, row.getDisplayname())
						pstm.setLong(2, rol.getId())
						pstm.execute();
					}
				}
				
				resultado.setSuccess(true)
			} catch (Exception e) {
				LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		return retorno;
	}
	
	public Result getUsuariosConSolicitudAbandonada(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
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
			where += " WHERE sda.iseliminado=false "
			where += " AND (sda.ESTATUSSOLICITUD = 'Solicitud caduca')"

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
			
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs= pstm.executeQuery();
			if(rs.next()) {
				SSA = rs.getString("valor")
			}

			errorlog += "object.lstFiltro" + object.lstFiltro
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			
			String consulta = Statements.GET_USUARIOS_SOLICITUD_ABANDONADA
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				errorlog += ", columna " + filtro.get("columna")
				switch (filtro.get("columna")) {

					case "IDBANNER,NOMBRE,EMAIL":
					errorlog+="IDBANNER,NOMBRE,EMAIL"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(da.idbanner) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				break;
				case "CAMPUS,PROGRAMA,PERÍODO":
					errorlog+="CAMPUS,PROGRAMA,PERÍODO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				
				break;
				case "PREPARATORIA,PROCEDENCIA,PROMEDIO":
					errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					/*
					where +="  OR LOWER(sda.estadoextranjero) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					*/
					where +="  OR LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				break;
				case "ESTATUS":
					errorlog+="ESTATUS,TIPO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				break;
				case "RESIDENCIA,TIPO ALUMNO,TIPO ADMISION":
					errorlog+="ESTATUS,TIPO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(tipoalumno.descripcion) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +="  OR LOWER(tipoadmision.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(residencia.descripcion) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				break;
					case "NOMBRE":
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
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(campus.descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "PREPARATORIA":
						bachillerato += " AND LOWER(prepa.DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							bachillerato += "=LOWER('[valor]')"
						} else {
							bachillerato += "LIKE LOWER('%[valor]%')"
						}
						bachillerato = bachillerato.replace("[valor]", filtro.get("valor"))
						break;
					case "PROGRAMA":
						programa += " AND LOWER(gestionescolar.DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							programa += "=LOWER('[valor]')"
						} else {
							programa += "LIKE LOWER('%[valor]%')"
						}
						programa = programa.replace("[valor]", filtro.get("valor"))
						break;
					case "INGRESO":
						ingreso += " AND LOWER(periodo.DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							ingreso += "=LOWER('[valor]')"
						} else {
							ingreso += "LIKE LOWER('%[valor]%')"
						}
						ingreso = ingreso.replace("[valor]", filtro.get("valor"))
						break;
					case "PROCEDENCIA":
						estado += " AND LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) ";
						if (filtro.get("operador").equals("Igual a")) {
							estado += "=LOWER('[valor]')"
						} else {
							estado += "LIKE LOWER('%[valor]%')"
						}
						estado = estado.replace("[valor]", filtro.get("valor"))
						break;
					case "PROMEDIO":
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
						tipoalumno += " AND LOWER(residencia.descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
					case "IDBANNER":
						tipoalumno += " AND LOWER(da.idbanner) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
					case "LISTAROJA":
						tipoalumno += " AND LOWER(da.observacionesListaRoja) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
					case "RECHAZO":
						tipoalumno += " AND LOWER(da.observacionesRechazo) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
					default:
						break;
				}
			}
			switch (object.orderby) {
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
					orderby += "campusEstudio.descripcion"
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
			
			if (where.contains("WHERE")) {
				where += " AND "
			} else {
				where += " WHERE "
			}
			where += " sda.correoelectronico like '%(ABANDONADO)%' ";
			
			orderby += " " + object.orientation;
			consulta = consulta.replace("[CAMPUS]", campus)
			consulta = consulta.replace("[PROGRAMA]", programa)
			consulta = consulta.replace("[INGRESO]", ingreso)
			consulta = consulta.replace("[ESTADO]", estado)
			consulta = consulta.replace("[BACHILLERATO]", bachillerato)
			consulta = consulta.replace("[TIPOALUMNO]", tipoalumno)
			where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno
			consulta = consulta.replace("[WHERE]", where);
			
			String consultaCount = Statements.GET_COUNT_SOLICITUDES_ABANDONADA;
			consultaCount = consultaCount.replace("[CAMPUS]", campus)
			consultaCount = consultaCount.replace("[PROGRAMA]", programa)
			consultaCount = consultaCount.replace("[INGRESO]", ingreso)
			consultaCount = consultaCount.replace("[ESTADO]", estado)
			consultaCount = consultaCount.replace("[BACHILLERATO]", bachillerato)
			consultaCount = consultaCount.replace("[TIPOALUMNO]", tipoalumno)
			consultaCount = consultaCount.replace("[WHERE]", where)
			consultaCount = consultaCount.replace("[LIMITOFFSET]", "");
			consultaCount = consultaCount.replace("[ORDERBY]", "");
			pstm = con.prepareStatement(consultaCount);
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorlog += " ///////*/*/*/*/*/ la consultaCount es: " + consultaCount
			errorlog += " ///////*/*/*/*/*/ la consulta es: " + consulta
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
								if(urlFoto != null && !urlFoto.isEmpty()) {
									columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
								}else {
									List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
										for(Document doc : doc1) {
											encoded = "../API/formsDocumentImage?document="+doc.getId();
											columns.put("fotografiab64", encoded);
										}
								}
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
	
	public Result getUsuariosConSolicitudVencida(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
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
			where += " WHERE sda.iseliminado=false "
			where += " AND (sda.ESTATUSSOLICITUD = 'Solicitud vencida')"

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
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs= pstm.executeQuery();
			if(rs.next()) {
				SSA = rs.getString("valor")
			}
			errorlog += "object.lstFiltro" + object.lstFiltro
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			//closeCon = validarConexion();
			String consulta = Statements.GET_USUARIOS_SOLICITUD_VENCIDA
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				errorlog += ", columna " + filtro.get("columna")
				switch (filtro.get("columna")) {

					case "IDBANNER,NOMBRE,EMAIL":
					errorlog+="IDBANNER,NOMBRE,EMAIL"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(da.idbanner) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				break;
				case "CAMPUS,PROGRAMA,PERÍODO":
					errorlog+="CAMPUS,PROGRAMA,PERÍODO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				
				break;
				case "PREPARATORIA,PROCEDENCIA,PROMEDIO":
					errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					/*
					where +="  OR LOWER(sda.estadoextranjero) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					*/
					where +="  OR LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				break;
				case "ESTATUS":
					errorlog+="ESTATUS,TIPO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				break;
				case "RESIDENCIA,TIPO ALUMNO,TIPO ADMISION":
					errorlog+="ESTATUS,TIPO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(tipoalumno.descripcion) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +="  OR LOWER(tipoadmision.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(residencia.descripcion) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				break;
					case "NOMBRE":
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
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(campus.descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "PREPARATORIA":
						bachillerato += " AND LOWER(prepa.DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							bachillerato += "=LOWER('[valor]')"
						} else {
							bachillerato += "LIKE LOWER('%[valor]%')"
						}
						bachillerato = bachillerato.replace("[valor]", filtro.get("valor"))
						break;
					case "PROGRAMA":
						programa += " AND LOWER(gestionescolar.DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							programa += "=LOWER('[valor]')"
						} else {
							programa += "LIKE LOWER('%[valor]%')"
						}
						programa = programa.replace("[valor]", filtro.get("valor"))
						break;
					case "INGRESO":
						ingreso += " AND LOWER(periodo.DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							ingreso += "=LOWER('[valor]')"
						} else {
							ingreso += "LIKE LOWER('%[valor]%')"
						}
						ingreso = ingreso.replace("[valor]", filtro.get("valor"))
						break;
					case "PROCEDENCIA":
						estado += " AND LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) ";
						if (filtro.get("operador").equals("Igual a")) {
							estado += "=LOWER('[valor]')"
						} else {
							estado += "LIKE LOWER('%[valor]%')"
						}
						estado = estado.replace("[valor]", filtro.get("valor"))
						break;
					case "PROMEDIO":
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
						tipoalumno += " AND LOWER(residencia.descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
					case "IDBANNER":
						tipoalumno += " AND LOWER(da.idbanner) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
					case "LISTAROJA":
						tipoalumno += " AND LOWER(da.observacionesListaRoja) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
					case "RECHAZO":
						tipoalumno += " AND LOWER(da.observacionesRechazo) ";
						if (filtro.get("operador").equals("Igual a")) {
							tipoalumno += "=LOWER('[valor]')"
						} else {
							tipoalumno += "LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
					default:
						break;
				}
			}
			switch (object.orderby) {
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
					orderby += "campusEstudio.descripcion"
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
			
			if (where.contains("WHERE")) {
				where += " AND "
			} else {
				where += " WHERE "
			}
			where += " sda.correoelectronico like '%(VENCIDO)%' ";
			
			orderby += " " + object.orientation;
			consulta = consulta.replace("[CAMPUS]", campus)
			consulta = consulta.replace("[PROGRAMA]", programa)
			consulta = consulta.replace("[INGRESO]", ingreso)
			consulta = consulta.replace("[ESTADO]", estado)
			consulta = consulta.replace("[BACHILLERATO]", bachillerato)
			consulta = consulta.replace("[TIPOALUMNO]", tipoalumno)
			where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno
			consulta = consulta.replace("[WHERE]", where);
			
			String consultaCount = Statements.GET_COUNT_SOLICITUDES_VENCIDA;
			consultaCount = consultaCount.replace("[CAMPUS]", campus)
			consultaCount = consultaCount.replace("[PROGRAMA]", programa)
			consultaCount = consultaCount.replace("[INGRESO]", ingreso)
			consultaCount = consultaCount.replace("[ESTADO]", estado)
			consultaCount = consultaCount.replace("[BACHILLERATO]", bachillerato)
			consultaCount = consultaCount.replace("[TIPOALUMNO]", tipoalumno)
			consultaCount = consultaCount.replace("[WHERE]", where)
			consultaCount = consultaCount.replace("[LIMITOFFSET]", "");
			consultaCount = consultaCount.replace("[ORDERBY]", "");
			pstm = con.prepareStatement(consultaCount);
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorlog += " ///////*/*/*/*/*/ la consultaCount es: " + consultaCount
			errorlog += " ///////*/*/*/*/*/ la consulta es: " + consulta
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
							if(urlFoto != null && !urlFoto.isEmpty()) {
								columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
							}else {
								List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
									for(Document doc : doc1) {
										encoded = "../API/formsDocumentImage?document="+doc.getId();
										columns.put("fotografiab64", encoded);
									}
							}
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
	
	public Result getUsuarioRegistrado(String Correo) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			String consulta = "select campus.grupoBonita from solicituddeadmision sda left join catcampus as campus on campus.persistenceid = sda.catcampusestudio_pid where sda.correoelectronico ='"+Correo+"'";
			List<String> rows = new ArrayList<String>();
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery()
			
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();

					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
					rows.add(columns);
			}
			resultado.setError_info(" errorLog = "+errorLog)
			resultado.setData(rows)
			resultado.setSuccess(true)
		}catch(Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog+" "+e.getMessage())
		}
		finally{
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updatePassword(String password,String correo) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			String consulta = "Update catregistro set password = '"+ password +"' where correoelectronico = '"+correo+"'";
			List<String> rows = new ArrayList<String>();
			closeCon = validarConexion();
			con.setAutoCommit(false)
			pstm = con.prepareStatement(consulta);
			pstm.executeUpdate();
				
			con.commit();
			//resultado.setError_info(" errorLog = "+errorLog)
			resultado.setData(rows)
			resultado.setSuccess(true)
		}catch(Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog+" "+e.getMessage())
			con.rollback();
		}
		finally{
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Result updatePerfil() {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			String consulta = "update profileentry set profileid=202 where profileid in (1,2,3)";
			List<String> rows = new ArrayList<String>();
			closeCon = validarConexionBonita();
			con.setAutoCommit(false)
			pstm = con.prepareStatement(consulta);
			pstm.executeUpdate();
				
			con.commit();
			//resultado.setError_info(" errorLog = "+errorLog)
			resultado.setData(rows)
			resultado.setSuccess(true)
		}catch(Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog+" "+e.getMessage())
			con.rollback();
		}
		finally{
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updateInformacionAspirante(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		Boolean closeCon = false;
		
		String errorLog = "";
		
		JsonSlurper jsonSlurper = new JsonSlurper();
		
		try {
			def object = jsonSlurper.parseText(jsonData)
			assert object instanceof Map;
			
			closeCon = validarConexion();
			con.setAutoCommit(false)
			pstm = con.prepareStatement(Statements.UPDATE_INFORMACION_ASPIRANTE);
			pstm.setString(1, object.objSolicitudDeAdmisionPrimerNombre);
			pstm.setString(2, object.objSolicitudDeAdmisionSegundoNombre);
			pstm.setString(3, object.objSolicitudDeAdmisionApellidoPaterno);
			pstm.setString(4, object.objSolicitudDeAdmisionApellidoMaterno);
			pstm.setString(5, object.objSolicitudDeAdmisionCurp);
			pstm.setString(6, object.objSolicitudDeAdmisionPromedioGeneral==null ? "0" :object.objSolicitudDeAdmisionPromedioGeneral.toString());
			pstm.setString(7, object.objSolicitudDeAdmisionFechaNacimiento);
			pstm.setInt(8, Integer.valueOf(object.objSolicitudDeAdmisionCatSexoPersistenceId));
			pstm.setInt(9, Integer.valueOf(object.objSolicitudDeAdmisionCatNacionalidadPersistenceId));
			pstm.setInt(10, Integer.valueOf(object.objSolicitudDeAdmisionPersistenceId));
			pstm.executeUpdate();
			
			pstm = con.prepareStatement(Statements.UPDATE_INFORMACION_ASPIRANTE_PADRESTUTOR);
			pstm.setString(1, object.objTutorNombre);
			pstm.setString(2, object.objTutorApellidos);
			pstm.setInt(3, Integer.valueOf(object.objTutorPersistenceId));
			pstm.executeUpdate();
			
			con.commit();
			resultado.setSuccess(true)
		}catch(Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog+" "+e.getMessage())
			con.rollback();
		}
		finally{
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
		public Result selectAspirantesEnLaRed(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", idsesionalumno = "", orderby = "ORDER BY ", errorlog = "";
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
			where += " WHERE sda.iseliminado=false and (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado = false ) AND (sda.ESTATUSSOLICITUD != 'Solicitud vencida') AND (sda.ESTATUSSOLICITUD != 'Periodo vencido') AND (sda.ESTATUSSOLICITUD != 'Solicitud caduca') AND (sda.ESTATUSSOLICITUD not like '%Solicitud vencida en:%') AND (sda.ESTATUSSOLICITUD not like '%Período vencido en:%')"
			if (object.campus != null) {
				where += " AND LOWER(campus.grupoBonita) = LOWER('" + object.campus + "') "
			}
			
			/*if (lstGrupo.size() > 0) {
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
			}*/

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

			String consulta = Statements.GET_ASPIRANTES_EN_PROCESO_RED

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
						
					case "NOMBRE,CURP":
						errorlog += "NOMBRE,CURP"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;
						
					case "NOMBRE,CURP,VPD":
						errorlog += "NOMBRE,CURP"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(sda.curp) like lower('%[valor]%')  ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where += " OR LOWER(campus.descripcion) like lower('%[valor]%') )";
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
					case "ID SESION":
						errorlog += "ID SESION";
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " sea.sesiones_pid ";
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
					orderby += "campusEstudio.DESCRIPCION"
					break;
				case "VPD":
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
				case "IDSESION":
					orderby += "sea.sesiones_pid";
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
			consulta = consulta.replace("[IDSESIONALUMNO]", idsesionalumno)
			where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno + " " + idsesionalumno
			errorlog = consulta + " 4";

			consulta = consulta.replace("[WHERE]", where);
			errorlog = consulta + " 5";
			
			String consultaCount = Statements.GET_ASPIRANTES_EN_PROCESO_COUNT
			pstm = con.prepareStatement(consultaCount)
            //pstm = con.prepareStatement(consulta.replaceAll("CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado end AS procedencia, to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechasolicitudenviada, sda.fechaultimamodificacion, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION isnull THEN sda.estadoextranjero ELSE estado.descripcion end AS estado, CASE WHEN prepa.descripcion = 'Otro' THEN sda.bachillerato ELSE prepa.descripcion end AS preparatoria, sda.promediogeneral, sda.estatussolicitud, da.tipoalumno, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion AS tipoadmision , R.descripcion AS residensia, TAL.descripcion AS tipodealumno, catcampus.descripcion AS transferencia, campusEstudio.clave AS claveCampus, gestionescolar.clave AS claveLicenciatura, sea.sesiones_pid", "COUNT(sda.persistenceid) as registros").replaceAll("[LIMITOFFSET]", "").replaceAll("[ORDERBY]", "").replaceAll("GROUP BY prepa.descripcion, sda.estadobachillerato, prepa.estado, sda.fechaultimamodificacion, sda.fechasolicitudenviada, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion, campus.descripcion, gestionescolar.nombre, periodo.descripcion, estado.descripcion, sda.estadoextranjero, sda.bachillerato, sda.promediogeneral, sda.estatussolicitud, da.tipoalumno, sda.caseid, sda.telefonocelular, da.observacioneslistaroja, da.observacionesrechazo, da.idbanner, campus.grupobonita, ta.descripcion, r.descripcion, tal.descripcion, catcampus.descripcion, campusestudio.clave, gestionescolar.clave, sea.sesiones_pid, sda.persistenceid", ""))
			
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

	
	public Result getIdiomaByUsername(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String idioma = "";
		String errorlog = "";
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		
		try {
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.SELECT_IDIOMA_BY_USERNAME);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			
			Map<String, Object> columns = new LinkedHashMap<String, Object>();
			errorlog = "Se ejecuto la consulta";
				while(rs.next()) {
					idioma = rs.getString("idioma").toString();
					columns.put("idioma", idioma);
				}
				
				errorlog = "Se completo el llenado del mapa "+ columns;
				
				rows.add(columns);
				resultado.setData(rows);
				resultado.setSuccess(true);
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updateNumeroContacto(String nombreUsuario, String numeroContacto) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
				closeCon = validarConexion();
				con.setAutoCommit(false)
				pstm = con.prepareStatement("UPDATE CATREGISTRO SET numeroContacto = ? WHERE nombreusuario = ?")
				pstm.setString(1, numeroContacto)
				pstm.setString(2, nombreUsuario)
				pstm.executeUpdate();
				
				pstm = con.prepareStatement("UPDATE SolicitudDeAdmision SET telefonocelular = ? WHERE correoelectronico = ?")
				pstm.setString(1, numeroContacto)
				pstm.setString(2, nombreUsuario)
				pstm.executeUpdate();
				
				con.commit();
				resultado.setSuccess(true)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	
	public Result selectAspirantesSmartCampus( String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", orderby = "ORDER BY ", errorlog = "";

		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
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
			
			if(object.campus != null) {
				where += " WHERE LOWER(campusDestino) = LOWER('" + object.campus + "') " ;
			}
			
			String consulta = Statements.GET_ASPIRANTES_SMART_CAMPUS;

			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				errorlog = consulta + " 1";
				switch (filtro.get("columna")) {
					case "NOMBRE,CURP,CORREO":
						errorlog += "NOMBRE,CURP"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(concat(apellidopaterno,' ',apellidomaterno,' ',nombre,' ',segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(curp) like lower('%[valor]%')  ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where += " OR LOWER(correo) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;

				case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
					errorlog += "PREPARATORIA,ESTADO,PROMEDIO"
					if (where.contains("WHERE")) {
						where += " AND "
					} else {
						where += " WHERE "
					}
					where += "( LOWER(estadoPreparatoria) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))

					where += "  OR LOWER(concat(clavePreparatoria,' - ',preparatoria)) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))

					/*where += " OR LOWER(clavePreparatoria) like lower('%[valor]%')";
					where = where.replace("[valor]", filtro.get("valor"))*/

					where += " OR LOWER(promedio) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				case "ULTIMA MODIFICACION":
					errorlog += "FECHAULTIMAMODIFICACION"
					if (where.contains("WHERE")) {
						where += " AND "
					} else {
						where += " WHERE "
					}
					where += " LOWER(fechaUltimaModificacion) ";
					if (filtro.get("operador").equals("Igual a")) {
						where += "=LOWER('[valor]')"
					} else {
						where += "LIKE LOWER('%[valor]%')"
					}

					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "FECHA SOLICITUD, FECHA PAGO":
					errorlog += "fechaEnvioSolicitud"
					if (where.contains("WHERE")) {
						where += " AND "
					} else {
						where += " WHERE "
					}
					where += " ( LOWER(fechaEnvioSolicitud) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))

					where += " OR LOWER(fechaPago) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				case "CAMPUS,PROGRAMA,INGRESO":
					errorlog += "PROGRAMA,INGRESO,CAMPUS"
					if (where.contains("WHERE")) {
						where += " AND "
					} else {
						where += " WHERE "
					}
					where += " ( LOWER(campusDestino) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))

					where += " OR LOWER(licenciatura) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))

					where += " OR LOWER(periodo) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))

					break;
					
				case "PAIS PREPARATORIA,RESIDENCIA,ESTATUS":
					errorlog += "ESTATUS"
					if (where.contains("WHERE")) {
						where += " AND "
					} else {
						where += " WHERE "
					}
					where += " ( LOWER(residencia) like lower('%[valor]%') ";
					where += " OR LOWER(estatus) like lower('%[valor]%') ";	
					where += " OR LOWER(paispreparatoria) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				
				case "ID BANNER, VPD":
					if (where.contains("WHERE")) {
						where += " AND "
					} else {
						where += " WHERE "
					}
					where += " ( LOWER(idbanner) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))

					where += " OR LOWER(vpd) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
					
				}

			}
			
			switch (object.orderby) {
				case "IDBANNER":
					orderby += "idbanner";
				break;
				case "NOMBRE":
					orderby += "nombre";
				break;
				case "CURP":
					orderby += "CURP";
				break;
				case "VPD":
					orderby += "VPD";
				break;
				
				case "CAMPUS":
					orderby += "CAMPUSDESTIONE";
				break;
				case "PROGRAMA":
					orderby += "LICENCIATURA";
				break;
				case "PERIODO":
					orderby += "PERIODO";
				break;
				
				case "PROCEDENCIA":
					orderby += "ESTADOPREPARATORIA";
				break;
				case "PREPARATORIA":
					orderby += "PREPARATORIA";
				break;
				case "PROMEDIO":
					orderby += "PROMEDIO";
				break;
				
				
				case "RESIDENCIA":
					orderby += "RESIDENCIA";
				break;
				case "ESTATUS":
					orderby += "ESTATUS";
				break;
				case "FECHA SOLICITUD":
					orderby += "fechaEnvioSolicitud";
				break;
				case "ULTIMA MODIFICACION":
					orderby += "fechaUltimaModificacion";
				break;
				case "FECHA PAGO":
					orderby += "fechaPago";
				break;
				case "CORREO":
					orderby += "correo";
				break;
				case "PAISPREPARATORIA":
					orderby += "paisPreparatoria";
				break;
				default:					
					orderby += "NOMBRE"
				break;
			}
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);
			
			pstm = con.prepareStatement(consulta.replace("idbanner,concat(apellidopaterno,' ',apellidomaterno,' ',nombre,' ',segundonombre) as nombre, curp, vpd, campusDestino as campus, licenciatura as programa, periodo, estadoPreparatoria as procedencia, concat(clavePreparatoria,' - ',preparatoria) as preparatoria, promedio, residencia, estatus, fechaEnvioSolicitud, fechaUltimaModificacion, correo, fechaPago, rutaPago, rutaSolicitud, foto, paisPreparatoria, rutaActaNacimiento, rutaKardex", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""));
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")

			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)
			rs = pstm.executeQuery();
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			def num = Math.random();
			
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					String encoded = "";
					boolean noAzure = false;
					try {
						String urlfoto = rs.getString("foto");
						if (urlfoto != null && !urlfoto.isEmpty()) {
							
							
							if(rs.getString("foto").toLowerCase().contains(".jpeg")) {
								columns.put("fotografiab64", "data:image/jpeg;base64, "+(new FileDownload().b64Url(rs.getString("foto") + SSA+"&v="+num)));
							}else if(rs.getString("foto").toLowerCase().contains(".png")) {
								columns.put("fotografiab64", "data:image/png;base64, "+(new FileDownload().b64Url(rs.getString("foto") + SSA+"&v="+num)));
							}else if(rs.getString("foto").toLowerCase().contains(".jpg")) {
								columns.put("fotografiab64", "data:image/jpg;base64, "+(new FileDownload().b64Url(rs.getString("foto") + SSA+"&v="+num)));
							}else if(rs.getString("foto").toLowerCase().contains(".jfif")) {
								columns.put("fotografiab64", "data:image/jfif;base64, "+(new FileDownload().b64Url(rs.getString("foto") + SSA+"&v="+num)));
							}
							
							//columns.put("fotografiab64", rs.getString("foto") + SSA);
							//columns.put("rutaPagob64", rs.getString("rutaPago") + SSA);
							//columns.put("rutaSolicitudb64", rs.getString("rutaSolicitud") + SSA);
						}
						 
					}
					catch(Exception e) {
						LOGGER.error "[ERROR] " + e.getMessage();
						columns.put("fotografiabpm", "");
						if(noAzure){
							columns.put("fotografiab64", "");
						}
						errorlog += "" + e.getMessage();
					}
				}

				rows.add(columns);
			}
			//errorlog = consulta + " 9";
			errorlog = " 9";
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
	
	public Result getUniversidadSmartCampus() {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement("SELECT DISTINCT campusDestino FROM AspirantesSmartCampus");
			rs = pstm.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();
				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				rows.add(columns);
			}
			
			resultado.setSuccess(true)
			resultado.setData(rows)
			
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError("500 Internal Server Error")
			resultado.setError_info(e.getMessage())
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getUserFoto(RestAPIContext context,String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs= pstm.executeQuery();
			if(rs.next()) {
				SSA = rs.getString("valor")
			}
			
			pstm = con.prepareStatement(Statements.GET_USER_FOTO);
			pstm.setString(1, object.idbanner);
			rs = pstm.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();
				
				if(!bonitaRolFilter(context,rs.getString("grupobonita"))) {
					columns.put("Error", "el aspirante no se encuentra");
				}else if(rs.getString("aceptado") =="t" || rs.getString("estatussolicitud") == "Resultado final del comité"){
					for (int i = 1; i <= columnCount; i++) {
						
						if(metaData.getColumnLabel(i).toLowerCase().equals("urlfoto")) {
							columns.put("fotografiab64",  base64Imagen((rs.getString("urlfoto") + SSA)) );
						}else if(metaData.getColumnLabel(i).toLowerCase().equals("primernombre")) {
							String nombre = rs.getString(i)+" "+(rs.getString("segundonombre")=="null"?"":rs.getString("segundonombre")+" ") +rs.getString("apellidopaterno")+""+rs.getString("apellidomaterno");
							
						}else if(!metaData.getColumnLabel(i).toLowerCase().equals("aceptado") && !metaData.getColumnLabel(i).toLowerCase().equals("apellidopaterno") && !metaData.getColumnLabel(i).toLowerCase().equals("apellidomaterno") && !metaData.getColumnLabel(i).toLowerCase().equals("segundonombre") ){
							columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						}
	
					}
				}else {
					columns.put("Error", "El aspirante se encuentra en otra etapa del proceso");
				}			
				rows.add(columns);
			}
		
			
			//String log = bonitaRolFilter(context);
			
			resultado.setSuccess(true)
			resultado.setData(rows)
			
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError("500 Internal Server Error")
			resultado.setError_info(e.getMessage())
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getB64File(String jsonData) {
		Boolean closeCon = false;
		String errorLog = "";
		Result resultado = new Result();
		try {
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			List <String> rows = new ArrayList <String> ();
			List <String> tipo = new ArrayList <String> ();
			closeCon = validarConexion();
			
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs = pstm.executeQuery();
			if (rs.next()) {
				SSA = rs.getString("valor")
			}
			
			
			String consulta = Statements.GET_RUTA_FILE_SMART_CAMPUS.replace("[RUTA]",object.ruta)
			pstm = con.prepareStatement(consulta);
			pstm.setString(1, object.idbanner)
			rs = pstm.executeQuery();
			//rutaPago, rutaSolicitud, rutaActaNacimiento, rutaKardex
			def num = Math.random();
			if (rs.next()) {
				String[] elements = rs.getString("RUTA").split("/")
				
				def ruta = java.net.URLEncoder.encode(elements[elements.length-1], "UTF-8");
				
				String url = "";
				elements.eachWithIndex{it,index ->
					url += (url.length() == 0?"":"/")+"${(index == elements.length-1 ? ruta : it)}";
				}
				
				if(rs.getString("RUTA").toLowerCase().contains(".jpeg")) {
						rows.add( "data:image/jpeg;base64, "+(new FileDownload().b64Url(url + SSA+"&v="+num)));
						tipo.add("imagen");
						tipo.add("jpeg");
					}else if(rs.getString("RUTA").toLowerCase().contains(".png")) {
						rows.add( "data:image/png;base64, "+(new FileDownload().b64Url(url + SSA+"&v="+num)));
						tipo.add("imagen");
						tipo.add("png");
					}else if(rs.getString("RUTA").toLowerCase().contains(".jpg")) {
						rows.add( "data:image/jpg;base64, "+(new FileDownload().b64Url(url + SSA+"&v="+num)));
						tipo.add("imagen");
						tipo.add("jpg");
					}else if(rs.getString("RUTA").toLowerCase().contains(".jfif")) {
						rows.add( "data:image/jfif;base64, "+(new FileDownload().b64Url(url + SSA+"&v="+num)));
						tipo.add("imagen");
						tipo.add("jfif");
					}else if(rs.getString("RUTA").toLowerCase().contains(".pdf")) {
						rows.add( "data:application/pdf;base64, "+(new FileDownload().b64Url(url+ SSA+"&v="+num)));
						tipo.add("archivo");
					}
				
			}

			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setAdditional_data(tipo);
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError("500 Internal Server Error")
			resultado.setError_info(e.getMessage())
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
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
	
	public Boolean bonitaRolFilter(RestAPIContext context,String groupName) {
		Boolean valid = false; 
		List<UserMembership> uMemberships=context.apiClient.identityAPI.getUserMemberships(context.apiSession.userId, 0, 100, UserMembershipCriterion.ROLE_NAME_ASC);
		uMemberships.each{
			it ->
			if(it.groupName.toLowerCase().equals(groupName.toLowerCase()) ) {
				valid=true
			}
		}
		
		return valid;
	}

}