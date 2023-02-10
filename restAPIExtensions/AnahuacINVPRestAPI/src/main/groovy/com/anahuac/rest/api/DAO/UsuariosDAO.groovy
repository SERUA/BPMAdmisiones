package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.identity.ContactDataCreator
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserCreator
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.identity.UserUpdater
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.IdiomaExamen
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Usuarios
import com.anahuac.rest.api.Entity.custom.AppMenuRole
import com.anahuac.rest.api.Entity.custom.AspiranteSesionCustom
import com.anahuac.rest.api.Entity.custom.Menu
import com.anahuac.rest.api.Entity.custom.MenuParent
import com.anahuac.rest.api.Entity.custom.SesionesCustom
import com.anahuac.rest.api.Entity.db.BusinessAppMenu
import com.anahuac.rest.api.Entity.db.Role
import com.anahuac.rest.api.Utilities.LoadParametros
import groovy.json.JsonSlurper

class UsuariosDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(UsuariosDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true
		}
		return retorno;
	}

	public Boolean validarConexion() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno = true
		}
		return retorno
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
			error_log = error_log + " | "+username + password + " ";
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
					pstm.setBoolean(3, false);
					pstm.setBoolean(4, false);
					pstm.setString(5, "Sesion Temporal");
					pstm.setBoolean(6, true);
					//resultReq = pstm.executeUpdate();
					rs = pstm.executeQuery();
					if(rs.next()) {
						resultReq = rs.getLong("persistenceid")
					}
					
					
					success = true;
					if(resultReq > 0) {
						error_log = resultReq + " Exito! query update_idioma_registro_by_username_1 insertado " + resultReq + " | " + object.idioma + object.nombreusuario
						//error_log = resultReq + " Exito! query update_idioma_registro_by_username_1"
					} else {
						error_log = resultReq + " Error! query update_idioma_registro_by_username_1"
					}
					
				} catch (Exception e) {
					con.rollback();
					/*if (success == false) {
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
					}*/

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
			update_user.setEnabled(true);
			error_log = error_log + " | UserUpdater update_user = new UserUpdater();";
			final User user_update = identityAPI.updateUser(user.getId(), update_user);
			error_log = error_log + " | final User user_update= identityAPI.updateUser(user.getId(), update_user);";
	
			def str = jsonSlurper.parseText('{"campus": "' + object.campus + '","correo":"' + object.nombreusuario + '", "codigo": "registrar","isEnviar":false}');
			error_log = error_log + " | def str = jsonSlurper.parseText";
			error_log = error_log + " | " + str;
			con.commit();
			Result resultado2 = new Result();
			resultado2 = updateNumeroContacto(object.nombreusuario,object.numeroContacto);
			error_log = error_log + resultado2.getError();
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
			resultado.setError_info(error_log)
		} catch (Exception e) {
			try {
				con.rollback();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
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
	
	public Result getIdiomaUsuario(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String error_log = ""; 
		IdiomaExamen idioma = new IdiomaExamen();
		List<IdiomaExamen> lstIdioma = new ArrayList<IdiomaExamen>();
		
		try {
		
			error_log = " USUARIO | " + username
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_IDIOMA_USUARIO)
				pstm.setString(1, username)
				
				rs = pstm.executeQuery()
				lstIdioma = new ArrayList<IdiomaExamen>();
				while(rs.next()) {
					idioma = new IdiomaExamen();
					error_log = " | EN EL WHILE " + rs.getString("idioma") + " | "
					idioma.setPersistenceId(rs.getLong("persistenceId"));
					error_log = " | EN EL WHILE " + rs.getString("persistenceId") + " | "
					idioma.setPersistenceVersion(rs.getString("persistenceVersion"));
					error_log = " | EN EL WHILE " + rs.getString("persistenceVersion") + " | "
					idioma.setIdioma(rs.getString("idioma"));
					error_log = " | EN EL WHILE " + rs.getString("idioma") + " | "
					idioma.setUsuario(rs.getString("username"));
					error_log = " | EN EL WHILE " + rs.getString("username") + " | "
					idioma.setNombresesion(rs.getString("nombresesion"));
					
					lstIdioma.add(idioma);
				}
				resultado.setSuccess(true)
				
				resultado.setData(lstIdioma)
				resultado.setError_info(error_log);
				
			} catch (Exception e) {
				LOGGER.error "[ERROR] " + e.getMessage();
				resultado.setError_info(error_log)
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
	
	public Result getMenuAdministrativo(RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			MenuParent row = new MenuParent();
			List<MenuParent> rows = new ArrayList<MenuParent>();
			closeCon = validarConexionBonita();
			pstm = con.prepareStatement(MenuParent.GET)
			pstm.setLong(1, context.apiSession.userId)
			rs = pstm.executeQuery()
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
				} else {
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
	
	public Result getAspirantes(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = " WHERE ctpr.descripcion = 'Examen Psicométrico'  ";
		String errorlog = "  ";
		String orderBy = " ORDER BY ";
		List < String > lstGrupo = new ArrayList < String > ();
		String errorLog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)
//			Long userLogged = context.getApiSession().getUserId();
//			List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
//
//			for (UserMembership objUserMembership: lstUserMembership) {
//				for (CatCampus rowGrupo: lstCatCampus) {
//					if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
//						lstGrupo.add(rowGrupo.getDescripcion());
//						break;
//					}
//				}
//			}
			
			if (lstGrupo.size() > 0 && object.campus != null ) {
				where += " AND (";
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += " cca.descripcion = '" + campusMiembro + "'"
					if (i == (lstGrupo.size() - 1)) {
						where += ") "
					} else {
						where += " OR "
					}
				}
			}
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				switch (filtro.get("columna")) {
					case "id_sesion":
						errorlog += "prue.sesion_pid "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( prue.sesion_pid = [valor] )";
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "No.":
						errorlog += "creg.caseid "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(creg.caseid::VARCHAR) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Nombre":
						errorlog += "ses.nombre"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( ( LOWER(creg.primernombre) like lower('%[valor]%') ) ";
						where += " OR ( LOWER(creg.segundonombre) like lower('%[valor]%') ) ";
						where += " OR ( LOWER(creg.apellidopaterno) like lower('%[valor]%') ) ";
						where += " OR ( LOWER(creg.apellidomaterno) like lower('%[valor]%') ) )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Id Banner":
						errorlog += "dets.idbanner "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(dets.idbanner) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Uni.":
						errorlog += "ccam.descripcion "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(ccam.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Teléfono":
						errorlog += "sdad.telefono "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(sdad.telefono) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Celular":
						errorlog += "sdad.telefonocelular "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(sdad.telefonocelular) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Correo":
						errorlog += "creg.correoelectronico "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(creg.correoelectronico) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Preguntas":
						errorlog += "total_preguntas "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(total_preguntas) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Contestadas":
						errorlog += "total_respuestas "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(total_respuestas) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Inicio":
						errorlog += "extr.fechainicio "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(extr.fechainicio) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Término":
						errorlog += "extr.fechafin "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(extr.fechafin) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Tiempo":
//						errorlog += "fechafin "
//						if (where.contains("WHERE")) {
//							where += " AND "
//						} else {
//							where += " WHERE "
//						}
//						where += " ( LOWER(fechafin) like lower('%[valor]%') )";
//						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Estatus":
//						errorlog += "fechafin "
//						if (where.contains("WHERE")) {
//							where += " AND "
//						} else {
//							where += " WHERE "
//						}
//						where += " ( LOWER(fechafin) like lower('%[valor]%') )";
//						where = where.replace("[valor]", filtro.get("valor"))
						break;
					default:
						break;
				}
			}
			
			switch(object.orderby) {
				case "idBpm":
					orderBy = " ORDER BY creg.caseid " + object.orientation;
				break;
				case "idbanner":
					orderBy = " ORDER BY dets.idbanner " + object.orientation;
				break;
				case "nombre":
					orderBy = " ORDER BY creg.primernombre " + object.orientation;
				break;
				case "uni":
					orderBy = " ORDER BY ccam.descripcion " + object.orientation;
				break;
				case "telefono":
					orderBy = " ORDER BY sdad.telefono  " + object.orientation;
				break;
				case "celular":
					orderBy = " ORDER BY creg.caseid " + object.orientation;
				break;
				case "correo":
					orderBy = " ORDER BY creg.correoelectronico " + object.orientation;
				break;
				case "preguntas":
					orderBy = " ORDER BY total_preguntas " + object.orientation;
				break;
				case "contestadas":
					orderBy = " ORDER BY total_respuestas " + object.orientation;
				break;
				case "inicio":
					orderBy = " ORDER BY extr.fechainicio " + object.orientation;
				break;
				case "termino":
					orderBy = " ORDER BY extr.fechafin " + object.orientation;
				break;
				default:
					orderBy = " ORDER BY creg.caseid " + object.orientation;
				break;
			}
			
//			errorLog += where;
			
			AspiranteSesionCustom row = new AspiranteSesionCustom();
			List <AspiranteSesionCustom> rows = new ArrayList <AspiranteSesionCustom>();
			closeCon = validarConexion();
			
			String consultaCcount = Statements.GET_ASPIRANTES_SESIONES_COUNT.replace("[WHERE]", where);
			pstm = con.prepareStatement(consultaCcount);
			rs = pstm.executeQuery();
			while (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("total_registros"));
			}
			
			String consulta = Statements.GET_ASPIRANTES_SESIONES.replace("[WHERE]", where).replace("[ORDERBY]", orderBy)
			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new AspiranteSesionCustom();
				String nombre = rs.getString("primernombre");
				if(rs.getString("segundonombre").equals("") || rs.getString("segundonombre") == null) {
					nombre += " " + rs.getString("segundonombre")
				}
				nombre += " " + rs.getString("apellidopaterno");
				if(rs.getString("apellidomaterno").equals("") || rs.getString("apellidomaterno") == null) {
					nombre += " " + rs.getString("apellidomaterno")
				}
				row.setIdBpm(rs.getLong("idbpm"));
				row.setNombre(nombre);
				row.setUni(rs.getString("campus"));
				row.setCorreoElectronico(rs.getString("correoelectronico"));
				row.setTelefono(rs.getString("telefono"));
				row.setCelular(rs.getString("telefonocelular"));
				row.setPreguntas(rs.getInt("total_preguntas"));
				row.setContestadas(rs.getInt("total_respuestas"));
				row.setInicio(rs.getString("fechainicio"));
				row.setTermino(rs.getString("fechafin"));
				row.setIdBanner(rs.getString("idbanner"));
				row.setEstatus(rs.getString("estatus"))
				row.setIdioma(rs.getString("idioma"))
				row.setBloqueado(rs.getString("usuariobloqueado"));
				row.setTerminado(rs.getBoolean("terminado"));
//				row.setTiempo(rs.getString("correoelectronico"));
//				row.setEstatus(rs.getString("correoelectronico"));
				
				rows.add(row);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}
	
	public Result bloquearAspirante(String username, Boolean bloquear, Boolean terminar) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement("UPDATE IdiomaINVPUsuario SET usuariobloqueado = ? WHERE username = ?")
			pstm.setBoolean(1, bloquear);
			pstm.setString(2, username);
			pstm.executeUpdate();
			
			pstm = con.prepareStatement("UPDATE INVPExamenTerminado SET terminado = ? WHERE username = ?");
			pstm.setBoolean(1, terminar);
			pstm.setString(2, username);
			pstm.executeUpdate();
			
			con.commit();
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result insertIidiomaUsuario(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		Boolean success = false;
		String error_log = "";
		String success_log = "";
		Long resultReq = 0;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.UPDATE_IDIOMA_REGISTRO_BY_USERNAME);
					pstm.setString(1, object.idioma);
					pstm.setString(2, object.nombreusuario);
					pstm.setBoolean(3, false);
					pstm.setBoolean(4, false);
					pstm.setString(5, "");
					pstm.setBoolean(6, false);
					//resultReq = pstm.executeUpdate();
					rs = pstm.executeQuery();
					if(rs.next()) {
						resultReq = rs.getLong("persistenceid")
					}
					
					success = true;
					if(resultReq > 0) {
						error_log = resultReq + " Exito! query UPDATE_IDIOMA_REGISTRO_BY_USERNAME insertado " + resultReq + " | " + object.idioma + object.nombreusuario
						//error_log = resultReq + " Exito! query update_idioma_registro_by_username_1"
					} else {
						error_log = resultReq + " Error! query UPDATE_IDIOMA_REGISTRO_BY_USERNAME"
					}
			/*rs = pstm.executeQuery();
			if(rs.next()) {
				resultReq = rs.getLong("persistenceid")
			}
			
			success = true;
			if(resultReq > 0) {
				error_log = resultReq + " Exito! query INSERT_TERMINADO_EXAMEN"
			} else {
				error_log = resultReq + " Error! query INSERT_TERMINADO_EXAMEN"
			}*/
			con.commit();
			
			resultado.setSuccess(true)
			resultado.setError_info(errorlog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updateidiomausuario(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		Boolean success = false;
		String error_log = "";
		String success_log = "";
		Long resultReq = 0;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.UPDATE_IDIOMA_USUARIO);
			pstm.setString(1, object.idioma);
			pstm.setString(2, object.nombreusuario);
			pstm.executeUpdate();
			con.commit();
			
			success = true;
			if(resultReq > 0) {
				error_log = resultReq + " Exito! query UPDATE_TERMINADO_EXAMEN"
			} else {
				error_log = resultReq + " Error! query UPDATE_TERMINADO_EXAMEN"
			}
			
			resultado.setSuccess(true)
			resultado.setError_info(errorlog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
}
