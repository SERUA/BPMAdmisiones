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
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.bonitasoft.engine.bpm.contract.FileInputValue
import org.apache.commons.codec.binary.Base64;

import com.anahuac.posgrados.bitacora.PSGRCatBitacoraCorreos
import com.anahuac.posgrados.catalog.PSGRCatCampus
import com.anahuac.posgrados.catalog.PSGRCatCampusDAO
import com.anahuac.posgrados.model.PSGRRegistro
import com.anahuac.posgrados.model.PSGRRegistroDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.DBConnectBonita
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Menu
import com.anahuac.rest.api.Entity.MenuParent
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Usuarios
import com.anahuac.rest.api.Entity.custom.AppMenuRole
import com.anahuac.rest.api.Entity.custom.ModuloUsuario
import com.anahuac.rest.api.Entity.db.BusinessAppMenu
import com.anahuac.rest.api.Entity.db.Role
import com.anahuac.rest.api.Utilities.FileDownload
import com.anahuac.rest.api.Utilities.LoadParametros
import groovy.json.JsonSlurper

class UsuariosDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(UsuariosDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;

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
	
	public Result getMenuAdministrativo(RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			MenuParent row = new MenuParent()
			List<MenuParent> rows = new ArrayList<MenuParent>();
			closeCon = validarConexionBonita();
			pstm = con.prepareStatement(MenuParent.GET);
			pstm.setLong(1, context.apiSession.userId);
			rs = pstm.executeQuery();
			rows = new ArrayList<MenuParent>();

			while(rs.next()) {
				row = new MenuParent();
				row.setId(rs.getLong("id"));
				row.setIsparent(rs.getBoolean("isparent"));
				row.setUrl(rs.getString("url"));
				row.setToken(rs.getString("token"));
				row.setMenu(rs.getString("menu"));
				row.setDisplayname(rs.getString("Displayname"));
				row.setParent(rs.getString("parent"));
				row.setParentid(rs.getLong("parentid"));
				row.setParenttoken(rs.getString("parenttoken"));
				
				if(rs.getBoolean("isparent")) {
					row = new MenuParent();
					row.setId(rs.getLong("id"));
					row.setIsparent(rs.getBoolean("isparent"));
					row.setUrl(rs.getString("url"));
					row.setToken(rs.getString("token"));
					row.setMenu(rs.getString("menu"));
					row.setDisplayname(rs.getString("Displayname"));
					row.setParent(rs.getString("parent"));
					row.setParentid(rs.getLong("parentid"));
					row.setParenttoken(rs.getString("parenttoken"));
					row.setChild(new ArrayList<Menu>());
					rows.add(row);
				} else {
					Menu menu = new Menu();
					menu.setId(rs.getLong("id"));
					menu.setIsparent(rs.getBoolean("isparent"));
					menu.setUrl(rs.getString("url"));
					menu.setToken(rs.getString("token"));
					menu.setMenu(rs.getString("menu"));
					menu.setDisplayname(rs.getString("Displayname"));
					menu.setParent(rs.getString("parent"));
					menu.setParentid(rs.getLong("parentid"));
					menu.setParenttoken(rs.getString("parenttoken"));
					if(rows.contains(row)) {
						rows.get(rows.indexOf(row)).getChild().add(menu);
					}
				}
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			if(e.getMessage().contains("\"app_menu_role\" does not exist") || e.getMessage().contains("Table \"APP_MENU_ROLE\" not found")) {
				try {
					pstm = con.prepareStatement(AppMenuRole.CREATE)
					pstm.execute();
					resultado.setError("La tabla app_menu_role no existía, y ya fue creada, favor de ejecutar la consulta de nuevo.")
				} catch (Exception e2) {
					LOGGER.error "[ERROR] " + e2.getMessage();
					resultado.setError("falló al crear tabla "+e2.getMessage());
				}
				
			} else {
				resultado.setError("No entró al crear tabla "+e.getMessage());
			}
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updateBusinessAppMenu(AppMenuRole row) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			errorLog += "1"
			closeCon = validarConexionBonita();
			errorLog += "|2"
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
			errorLog += "|3"
			
			resultado.setSuccess(true);
		} catch (Exception e) {
			errorLog += ("|" + e.getMessage());
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError_info(errorLog);
			resultado.setError(e.getMessage());
		} finally {
			resultado.setError_info(errorLog);
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
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(" "+e.getMessage());
		} finally {
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
	
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true;
		}
		return retorno;
	}
	
	public Result instanciarProceso(String jsonData, org.bonitasoft.engine.api.APIClient apiClient) {
		Result resultado = new Result();
		String errorLog = "";
		
		try {
			String username = "";
			String password = "";
			
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			apiClient.login(username, password);
			IdentityAPI identityAPI = apiClient.getIdentityAPI()
			final User user = identityAPI.createUser(creator);
			apiClient.login(user.getUserName(), object.password);
			final IdentityAPI identityAPI2 = apiClient.getIdentityAPI();
			UserMembership membership = identityAPI2.addUserMembership(user.getId(), identityAPI2.getGroupByPath("/ASPIRANTE").getId(), identityAPI2.getRoleByName("ASPIRANTE").getId())
			UserUpdater update_user = new UserUpdater();
			update_user.setEnabled(true);
			final User user_update = identityAPI.updateUser(user.getId(), update_user);
			Map<String, Serializable> contract = new HashMap<String, Serializable>();
			contract.put("registroInput", object.registroInput);
			apiClient.getProcessAPI().startProcess(Long.valueOf(object.processDefinitionId), contract);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setError_info(errorLog);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		} finally {
			if(con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result registro(String jsonData) {
		Result resultado = new Result();
		Result crearUsuario;
		Result instanciarCaso;
		Result desactivarUsuario;
		String errorLog = "";
		String username = "";
		String password = "";
		
		try {
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			LOGGER.error "[registro] 1: "
			apiClient.login(username, password);
			LOGGER.error "[registro] 2 username: " + username + "| password: " + password;
//			apiClient.login("Administrador", "bpm");
			crearUsuario = registrarUsuario(jsonData, apiClient, objProperties);
			
//			errorLog += "[registro] 3 | "  +  crearUsuario.getError_info();
			resultado.setError_info(errorLog);
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[registro|ERROR] " + e.getMessage();
			resultado.setError_info(errorLog);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		} finally {
			if(con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result iniciarCaso(String jsonData, org.bonitasoft.engine.api.APIClient apiClient) {
		Result resultado = new Result();
		String errorLog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			LOGGER.error "[iniciarCaso] 1 | ";
			Map<String, Serializable> contract = new HashMap<String, Serializable>();
			Map<String, Serializable> registroInput = new HashMap<String, Serializable>();
			Map<String, Serializable> campus = new HashMap<String, Serializable>();
			campus.put("persistenceId_string", object.registroInput.campus.persistenceId_string as Serializable);
			registroInput.put("nombre", object.registroInput.nombre as Serializable);
			registroInput.put("apellido_paterno", object.registroInput.apellido_paterno as Serializable);
			registroInput.put("apellido_materno", object.registroInput.apellido_materno as Serializable);
			registroInput.put("telefono_celular", object.registroInput.telefono_celular as Serializable);
			registroInput.put("correo_electronico", object.registroInput.correo_electronico as Serializable);
			registroInput.put("password", object.registroInput.password as Serializable);
			registroInput.put("acepto_avisoprivacidad", object.registroInput.acepto_avisoprivacidad as Serializable);
			registroInput.put("campus",  campus as Serializable);
			contract.put("registroInput", registroInput as Serializable);
			LOGGER.error "[iniciarCaso] 2 | " + contract.toString();
			apiClient.getProcessAPI().startProcessWithInputs(Long.valueOf(object.processDefinitionId), contract);
			LOGGER.error "[iniciarCaso] 3 | ";
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[iniciarCaso|ERROR] " + e.getMessage();
			resultado.setError_info(errorLog);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		} finally {
			if(con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result registrarUsuario(String jsonData, org.bonitasoft.engine.api.APIClient apiClient, PropertiesEntity objProperties){
		Result resultado = new Result();
		Result resultadoN = new Result();
		String errorLog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			LOGGER.error "[registrarUsuario] 1 | ";
			UserCreator creator = new UserCreator(object.registroInput.correo_electronico, object.registroInput.password);
			creator.setFirstName(object.registroInput.nombre).setLastName(object.registroInput.apellido_paterno);
			LOGGER.error "[registrarUsuario] 2 | ";
			ContactDataCreator proContactDataCreator = new ContactDataCreator().setEmail(object.registroInput.correo_electronico);
			creator.setProfessionalContactData(proContactDataCreator);
			LOGGER.error "[registrarUsuario] 3 | ";
			IdentityAPI identityAPI = apiClient.getIdentityAPI()
			final User user = identityAPI.createUser(creator);
			LOGGER.error "[registrarUsuario] 4 | " + user.firstName;
			apiClient.login(user.getUserName(), object.registroInput.password);
			LOGGER.error "[registrarUsuario] 5 | ";
			final IdentityAPI identityAPI2 = apiClient.getIdentityAPI();
			UserMembership membership = identityAPI2.addUserMembership(user.getId(), identityAPI2.getGroupByPath("/ASPIRANTE").getId(), identityAPI2.getRoleByName("ASPIRANTE").getId())
			UserUpdater update_user = new UserUpdater();
			update_user.setEnabled(true);
			LOGGER.error "[registrarUsuario] 6 | ";
			final User user_enabler = identityAPI.updateUser(user.getId(), update_user);
			LOGGER.error "[registrarUsuario] 7 | ";
			Result resultadoInstanciar = iniciarCaso(jsonData, apiClient);
			update_user.setEnabled(false);
			final User user_disabler = identityAPI.updateUser(user.getId(), update_user);
			
//			Result resultado2 = new Result();
//			resultado2 = updateNumeroContacto(object.registroInput.correo_electronico, object.registroInput.telefono_celular);
			
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[registrarUsuario|ERROR] " + e.getMessage();
			resultado.setError_info(errorLog);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		} finally {
			if(con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	}
	
	public Result habilitarUsuario(String usernameAspirante, RestAPIContext context) {
		Usuarios objUsuario= new Usuarios();
		Result resultado = new Result();
		List<String> lstResultado = new ArrayList<String>();
		Boolean closeCon = false;
		
		try {
			String username = "";
			String password = "";
			LOGGER.error "[habilitarUsuario] 1 | " + usernameAspirante;
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			LOGGER.error "[habilitarUsuario] 2 | ";
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			apiClient.login(username, password);
			LOGGER.error "[habilitarUsuario] 3 | ";
			IdentityAPI identityAPI = apiClient.getIdentityAPI();
			final User user = identityAPI.getUserByUserName(usernameAspirante);
			
			LOGGER.error "[habilitarUsuario] 4 | ";
			resultado = enviarTareaRest(usernameAspirante, context);
			
			if(!resultado.isSuccess()) {
				LOGGER.error "[habilitarUsuario] 5 | ";
				throw new Exception ("No se ha podido activar el usuario. Intente de nuevo mas tarde.");
			}
			
			UserUpdater update_user = new UserUpdater();
			LOGGER.error "[habilitarUsuario] 6 | ";
			update_user.setEnabled(true);
			LOGGER.error "[habilitarUsuario] 7 | ";
			final User user_update= identityAPI.updateUser(user.getId(), update_user);
			LOGGER.error "[habilitarUsuario] 8 | ";
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado;
	}
	
	public Result enviarTareaRest(String correo, RestAPIContext context) {
		Result resultado = new Result();
		List<PSGRRegistro> lstCatRegistro = new ArrayList<PSGRRegistro>();
		PSGRRegistro objCatRegistro = new PSGRRegistro();
		String errorLog = "";
		Boolean closeCon = false;
		
		try {
			String username = "";
			String password = "";
			LOGGER.error "[enviarTareaRest] 1 | ";
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			LOGGER.error "[enviarTareaRest] 2 | ";
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			apiClient.login(username, password);
			LOGGER.error "[enviarTareaRest] 3 | ";
			def catRegistroDAO = context.apiClient.getDAO(PSGRRegistroDAO.class);
			lstCatRegistro = catRegistroDAO.findByCorreo_electronico(correo, 0, 1);
			
			if(lstCatRegistro.size() == 0) {
				LOGGER.error "[enviarTareaRest] 4 | ";
				throw new Exception ("No registro encontrado");
			}
			
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 1);
			searchBuilder.filter(HumanTaskInstanceSearchDescriptor.NAME, "Validar cuenta y activar usuario");
			searchBuilder.filter(HumanTaskInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID, lstCatRegistro.get(0).getCaseid());
			final SearchOptions searchOptions = searchBuilder.done();
			LOGGER.error "[enviarTareaRest] 5 | ";
			SearchResult<HumanTaskInstance>  SearchHumanTaskInstanceSearch = apiClient.getProcessAPI().searchHumanTaskInstances(searchOptions);
			List<HumanTaskInstance> lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
			LOGGER.error "[enviarTareaRest] 6 | ";
			if(lstHumanTaskInstanceSearch.size() == 0) {
				LOGGER.error "[enviarTareaRest] 7 | ";
				throw new Exception ("No tarea  activa ");
			}
			
			LOGGER.error "[enviarTareaRest] 8 | ";
			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstanceSearch) {
				LOGGER.error "[enviarTareaRest] 9 | ";
				apiClient.getProcessAPI().assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
				apiClient.getProcessAPI().executeFlowNode(objHumanTaskInstance.getId());
				break;
			}
			
			LOGGER.error "[enviarTareaRest] 10 | ";
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[enviarTareaRest|ERROR]" + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado;
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
	
	public Result getUsuarios(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = " WHERE user_.userName!='Administrador' AND role.name IN ('Comite PSG', 'Admisiones PSG', 'Chat PSG', 'SERUA PSG', 'TI campus PSG') ", orderby="ORDER BY ", errorlog="",campus="";
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
			
			def objCatCampusDAO = context.apiClient.getDAO(PSGRCatCampusDAO.class);
			List<PSGRCatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				lstGrupo.add(objUserMembership.getGroupId());
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
			orderby += " " + object.orientation;
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
					row = new ModuloUsuario();
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
				
		} finally {
			resultado.setError_info(errorlog);
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
}