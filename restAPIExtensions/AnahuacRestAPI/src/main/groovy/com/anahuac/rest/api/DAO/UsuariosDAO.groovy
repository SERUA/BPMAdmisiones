package com.anahuac.rest.api.DAO

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.process.ProcessDefinition
import org.bonitasoft.engine.identity.ContactDataCreator
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserCreator
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserUpdater
import org.bonitasoft.engine.profile.Profile
import org.bonitasoft.engine.profile.ProfileMemberCreator
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Usuarios
import com.bonitasoft.web.extension.rest.RestAPIContext

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
	
	public Result postRegistrarUsuario(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoN = new Result();
		//List<Usuarios> lstResultado = new ArrayList<Usuarios>();
		List<String> lstResultado = new ArrayList<String>();
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		Integer start = 0;
		Integer end = 99999;
		
		Usuarios objUsuario= new Usuarios();
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			// Datos de la cuenta del Usuario
			UserCreator creator = new UserCreator(object.nombreusuario,object.password);
			creator.setFirstName(object.nombre).setLastName(object.apellido);
			ContactDataCreator proContactDataCreator = new ContactDataCreator().setEmail(object.nombreusuario);
			creator.setProfessionalContactData(proContactDataCreator);
			//inicializa la cuenta con la cual tendras permisos para registrar el usuario
			apiClient.login("Administrador", "bpm")
			//Registro del usuario
			IdentityAPI identityAPI = apiClient.getIdentityAPI()
			final User user = identityAPI.createUser(creator);
			
			
			apiClient.login(user.getUserName(), object.password)
			final IdentityAPI identityAPI2 = apiClient.getIdentityAPI()
			
			UserMembership membership = identityAPI2.addUserMembership(user.getId(), identityAPI2.getGroupByPath("/ASPIRANTE").getId(), identityAPI2.getRoleByName("ASPIRANTE").getId())
			
			UserUpdater update_user = new UserUpdater();
			update_user.setEnabled(false);
			final User user_update= identityAPI.updateUser(user.getId(), update_user);
			
			
			def str = jsonSlurper.parseText('{"campus": "CAMPUS-PUEBLA","correo":"'+object.nombreusuario+'", "codigo": "registrar","isEnviar":false}');
			
			NotificacionDAO nDAO = new NotificacionDAO();
			resultadoN = nDAO.generateHtml(parameterP, parameterC, "{\"campus\": \"CAMPUS-PUEBLA\", \"correo\":\""+object.nombreusuario+"\", \"codigo\": \"registrar\", \"isEnviar\":false }", context);
			String plantilla = resultadoN.getData().get(0);
			
			
			Properties prop = new Properties();
			String propFileName = "configuration.properties";
			InputStream inputStream;
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			plantilla = plantilla.replace("[href-confirmar]", prop.getProperty("HOST")+ "/bonita/API/extension/AnahuacRestGet?url=habilitarUsuario&correo="+str.correo+"&p=0&c=10" );
			
			MailGunDAO dao = new MailGunDAO();
			dao.sendEmailPlantilla(str.correo,"Completar Registro",plantilla.replace("\\", ""),"","CAMPUS-PUEBLA", context);
			
			lstResultado.add(plantilla.replace("\\", ""))
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (Exception e) {
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
			dao.sendEmailPlantilla(object.nombreusuario,"Nueva contraseña",plantilla.replace("\\", ""),"","", context);
			
			
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
			
			
			UserUpdater update_user = new UserUpdater();
			update_user.setEnabled(true);
			final User user_update= identityAPI.updateUser(user.getId(), update_user);
		
			lstResultado.add(user_update);
					
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
	
	public Result getHabilitarUsaurio(Integer parameterP,Integer parameterC, String correo, RestAPIContext context) {
		Usuarios objUsuario= new Usuarios();
		Result resultado = new Result();
		//List<Usuarios> lstResultado = new ArrayList<Usuarios>();
		List<String> lstResultado = new ArrayList<String>();
		try {
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
<<<<<<< HEAD
			apiClient.login("Administrador", "bpm")
			IdentityAPI identityAPI = apiClient.getIdentityAPI()
			final User user = identityAPI.getUserByUserName(correo);
=======
			apiClient.login("alvarado.eduardo@correo.com", "bpm")
			LOGGER.error "================Paso 1=================";
			IdentityAPI identityAPI = apiClient.getIdentityAPI()
			final User user = identityAPI.getUserByUserName(correo);
			LOGGER.error "=================Paso 2================";
>>>>>>> dac4a5a09a64de6f42d6151f0753b574ef1cb11a
			
			UserUpdater update_user = new UserUpdater();
			update_user.setEnabled(true);
			final User user_update= identityAPI.updateUser(user.getId(), update_user);
			
			NotificacionDAO nDAO = new NotificacionDAO();
			resultado = nDAO.generateHtml(parameterP, parameterC, "{\"campus\": \"CAMPUS-PUEBLA\", \"correo\":\""+correo+"\", \"codigo\": \"activado\", \"isEnviar\":false }", context);
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
