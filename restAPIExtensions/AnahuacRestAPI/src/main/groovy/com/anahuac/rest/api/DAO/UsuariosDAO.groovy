package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

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
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Usuarios
import com.anahuac.rest.api.Entity.Custom.ModuloUsuario
import com.anahuac.rest.api.Entity.db.CatBitacoraCorreo
import com.bonitasoft.web.extension.rest.RestAPIContext
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import groovy.json.JsonSlurper

class UsuariosDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ListadoDAO.class);
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
	
		Integer start = 0;
		Integer end = 99999;
	
		Usuarios objUsuario = new Usuarios();
	
		String error_log = "";
		
		try {
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

			username = prop.getProperty("USERNAME");
			password = prop.getProperty("PASSWORD");

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
			error_log = error_log + " | apiClient.login(username, password)";
	
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
			plantilla = plantilla.replace("[href-confirmar]", prop.getProperty("HOST") + "/bonita/apps/login/activate/?correo=" + str.correo + "");
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

			username = prop.getProperty("USERNAME");
			password = prop.getProperty("PASSWORD");
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			apiClient.login(username, password);
			
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

			username = prop.getProperty("USERNAME");
			password = prop.getProperty("PASSWORD");

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

			username = prop.getProperty("USERNAME");
			password = prop.getProperty("PASSWORD");
			/*def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);*/
			LOGGER.error "def object = jsonSlurper.parseText(jsonData);";
			errorLog = errorLog + "";
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
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

			username = prop.getProperty("USERNAME");
			password = prop.getProperty("PASSWORD");
			
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
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
		}
		return resultado;
	}
	public Result getUsuarios(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby="ORDER BY ", errorlog=""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			ModuloUsuario row = new ModuloUsuario()
			List<ModuloUsuario> rows = new ArrayList<ModuloUsuario>();
			closeCon = validarConexionBonita();
			
			
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
						where+="=[valor]"
					}else {
						where+="=[valor]"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
					case "NOMBRE":
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
					
					case "APELLIDO":
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
						where +=" user_.creationdate ";
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
				where +=" ul.lastconnection ";
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
				case"NOMBRE":
				orderby+="user_.firstname"
				break;
				 case"APELLIDO":
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
				orderby+="user_.id";
				break;
			}
			orderby+=" "+object.orientation;
			String consulta = ModuloUsuario.GET_USUARIOS_CUSTOM
			consulta=consulta.replace("[WHERE]", where);
			errorlog+="consulta:"
			resultado.setError_info(consulta.replace("user_.firstname, user_.lastname, user_.creationdate, user_.userName, ul.lastconnection last_connection,user_.id, STRING_AGG(role.name || ' en ' || group_.name, ',' order by role.name ) membresia", "COUNT(user_.id) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("GROUP BY user_.firstname, user_.lastname, user_.creationdate, user_.userName, ul.lastconnection,user_.id", ""))
				pstm = con.prepareStatement(consulta.replace("user_.firstname, user_.lastname, user_.creationdate, user_.userName, ul.lastconnection last_connection,user_.id, STRING_AGG(role.name || ' en ' || group_.name, ',' order by role.name ) membresia", "COUNT(user_.id) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("GROUP BY user_.firstname, user_.lastname, user_.creationdate, user_.userName, ul.lastconnection,user_.id", ""))
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
					rows.add(row)
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				resultado.setError_info(errorlog)
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
