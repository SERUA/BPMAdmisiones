package com.anahuac.rest.api.DAO

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessDefinition
import org.bonitasoft.engine.identity.ContactDataCreator
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserCreator
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserUpdater
import org.bonitasoft.engine.profile.Profile
import org.bonitasoft.engine.profile.ProfileMemberCreator
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatRegistro
import com.anahuac.catalogos.CatRegistroDAO
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Usuarios
import com.bonitasoft.web.extension.rest.RestAPIContext
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import groovy.json.JsonSlurper

class UsuariosDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ListadoDAO.class);
	
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
	
		Integer start = 0;
		Integer end = 99999;
	
		Usuarios objUsuario = new Usuarios();
	
		String error_log = "";
		
		try {
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
			apiClient.login("Administrador", "bpm")
			error_log = error_log + " | apiClient.login(Administrador, bpm)";
	
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
	
	
			Properties prop = new Properties();
			String propFileName = "configuration.properties";
			InputStream inputStream;
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			error_log = error_log + " | inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);";
	
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			error_log = error_log + " | if (inputStream != null) {";
			plantilla = plantilla.replace("[href-confirmar]", prop.getProperty("HOST") + "/bonita/apps/login/activate/?correo=" + str.correo + "");
			error_log = error_log + " | plantilla = plantilla.replace([href-confirmar], prop.getProperty";
			MailGunDAO dao = new MailGunDAO();
			resultado = dao.sendEmailPlantilla(str.correo, "Completar Registro", plantilla.replace("\\", ""), "", object.campus, context);
			error_log = error_log + " | resultado = dao.sendEmailPlantilla(str.correo,";
			lstResultado.add(plantilla.replace("\\", ""))
			error_log = error_log + " | lstResultado.add(plantilla.replace(";
			resultado.setData(lstResultado);
			error_log = error_log + " | resultado.setData(lstResultado);";
			resultado.setSuccess(true);
			resultado.setError_info(error_log)
		} catch (Exception e) {
			resultado.setError_info(error_log)
			resultado.setData(lstResultado);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
		}
		return resultado;
	}
	
	
	public Result postRecuperarPassword(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		
		Result resultadoN = new Result();
		Usuarios objUsuario= new Usuarios();
		Result resultado = new Result();
		//List<Usuarios> lstResultado = new ArrayList<Usuarios>();
		List<String> lstResultado = new ArrayList<String>();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			apiClient.login("Administrador", "bpm");
			
			IdentityAPI identityAPI = apiClient.getIdentityAPI()
			final User user = identityAPI.getUserByUserName(object.nombreusuario);
			
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
			def str = jsonSlurper.parseText('{"campus": "CAMPUS-PUEBLA","correo":"'+object.nombreusuario+'", "codigo": "recuperar","isEnviar":false}');
			
			
			NotificacionDAO nDAO = new NotificacionDAO();
			 
			resultadoN = nDAO.generateHtml(parameterP, parameterC, "{\"campus\": \"CAMPUS-PUEBLA\", \"correo\":\""+object.nombreusuario+"\", \"codigo\": \"recuperar\", \"isEnviar\":false }", context);
			String plantilla = resultadoN.getData().get(0);
			plantilla = plantilla.replace("[password]", object.password );
			MailGunDAO dao = new MailGunDAO();
			resultado = dao.sendEmailPlantilla(object.nombreusuario,"Nueva contraseña",plantilla.replace("\\", ""),"","CAMPUS-PUEBLA", context);
			
			
			lstResultado.add(plantilla);
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
		}
		return resultado;
	}
	
	public Result postHabilitarUsaurio(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		
		
		Usuarios objUsuario= new Usuarios();
		Result resultado = new Result();
		//List<Usuarios> lstResultado = new ArrayList<Usuarios>();
		List<String> lstResultado = new ArrayList<String>();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login("Administrador", "bpm")
			
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
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
		}
		return resultado;
	}
	
	public Result enviarTarea(String correo,RestAPIContext context) {
		Result resultado = new Result();
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		CatRegistro objCatRegistro = new CatRegistro();
		String errorLog ="";
		try {
			
			/*def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);*/
			LOGGER.error "def object = jsonSlurper.parseText(jsonData);";
			errorLog = errorLog + "";
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login("Administrador", "bpm")
			LOGGER.error "apiClient.login";
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			searchBuilder.filter(HumanTaskInstanceSearchDescriptor.NAME, "Validar Cuenta");
			LOGGER.error "searchBuilder.filter(HumanTaskInstanceSearchDescriptor.NAME";
			final SearchOptions searchOptions = searchBuilder.done();
			SearchResult<HumanTaskInstance>  SearchHumanTaskInstanceSearch = apiClient.getProcessAPI().searchHumanTaskInstances(searchOptions);
			List<HumanTaskInstance> lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
			def catRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstanceSearch) {
				LOGGER.error "=================================";
				LOGGER.error objHumanTaskInstance.getName();
				LOGGER.error "=================================";
				LOGGER.error "RootContainer" + objHumanTaskInstance.getRootContainerId()
				lstCatRegistro = catRegistroDAO.findByCaseId(objHumanTaskInstance.getRootContainerId(), 0, 1)
				if(lstCatRegistro != null) {
					LOGGER.error "objCatRegistro=================================";
					LOGGER.error "lstCatRegistro.size "+ lstCatRegistro.size()
					if(lstCatRegistro.size() > 0) {
						objCatRegistro = new CatRegistro();
						objCatRegistro = lstCatRegistro.get(0);
						
						LOGGER.error "objCatRegistro=================================";
						LOGGER.error objCatRegistro.getCorreoelectronico();
						LOGGER.error "objCatRegistro=================================";
						LOGGER.error correo
						
						if(objCatRegistro.getCorreoelectronico().equals(correo)) {
							apiClient.getProcessAPI().assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
							apiClient.getProcessAPI().executeFlowNode(objHumanTaskInstance.getId());
						}
					}
				}
			}
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
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
		try {
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			apiClient.login("Administrador", "bpm")
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
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
		}
		return resultado;
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
	
}
