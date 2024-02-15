package com.anahuac.rest.api.DAO

import static org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion.DEFAULT

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.sql.Time
import java.sql.Timestamp
import java.text.SimpleDateFormat

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.flownode.ActivityInstance
import org.bonitasoft.engine.bpm.flownode.TaskInstance
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo
import org.bonitasoft.engine.exception.SearchException
import org.bonitasoft.engine.identity.ContactDataCreator
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserCreator
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.identity.UserNotFoundException
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
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
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
			apiClient.login(username, password);
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
					try {
						con.rollback();
					} catch(Exception ex) {
						ex.printStackTrace();
					}
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
			} catch(Exception ex) {
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
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_IDIOMA_USUARIO)
			pstm.setString(1, username)
			
			rs = pstm.executeQuery()
			lstIdioma = new ArrayList<IdiomaExamen>();
			while(rs.next()) {
				idioma = new IdiomaExamen();
				idioma.setPersistenceId(rs.getLong("persistenceId"));
				idioma.setPersistenceVersion(rs.getString("persistenceVersion"));
				idioma.setIdioma(rs.getString("idioma"));
				idioma.setUsuario(rs.getString("username"));
				idioma.setNombresesion(rs.getString("nombresesion"));
				
				lstIdioma.add(idioma);
			}
			
			resultado.setSuccess(true);
			resultado.setData(lstIdioma);
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
			try {
				con.rollback();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		} finally {
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
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat formatterEntrada = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			Long userLogged = context.getApiSession().getUserId();
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
			
			errorLog += " | lstGrupo.size(): " + lstGrupo.size().toString() + " | object.campus: " + object.campus.toString();
			if (lstGrupo.size() > 0 && object.campus == null) {
				where += " AND (";
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += " ccam.descripcion = '" + campusMiembro + "'"
					if (i == (lstGrupo.size() - 1)) {
						where += ") "
					} else {
						where += " OR "
					}
				}
			} else if(object.campus != null) {
				where += " AND ccam.grupobonita = '" + object.campus + "'"
			}
			
			if(object.idbanner != null) {
				where += "  AND ( LOWER(dets.idbanner) like lower('%[valor]%') )"
				where = where.replace("[valor]", object.idbanner);
			}
			
			if(object.idBpm != null) {
				where += " AND ( LOWER(creg.caseid::VARCHAR) like lower('%[valor]%') )"
				where = where.replace("[valor]", object.idBpm);
			}
			
			if(object.correoelectronico != null) {
				where += " AND ( LOWER(creg.correoelectronico) like lower('%[valor]%') ) "
				where = where.replace("[valor]", object.correoelectronico);
			}	
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				switch (filtro.get("columna")) {
					case "id_sesion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( prue.sesion_pid = [valor] )";
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "idBpm,idbanner":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( ( LOWER(creg.caseid::VARCHAR) like lower('%[valor]%') )";
						where += " OR ( LOWER(dets.idbanner) like lower('%[valor]%') ) )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "nombre":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( ( LOWER(creg.primernombre) like lower('%[valor]%') ) ";
						where += " OR ( LOWER(creg.segundonombre) like lower('%[valor]%') ) ";
						where += " OR ( LOWER(creg.apellidopaterno) like lower('%[valor]%') )  ";
						where += " OR ( LOWER(creg.apellidomaterno) like lower('%[valor]%') ) )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Id Banner":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(dets.idbanner) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "uni":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(ccam.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "telefono,celular,correo":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( ( LOWER(sdad.telefono) like lower('%[valor]%') )";
						where += " OR ( LOWER(sdad.telefonocelular) like lower('%[valor]%') )";
						where += " OR ( LOWER(creg.correoelectronico) like lower('%[valor]%') ) )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Celular":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(sdad.telefonocelular) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Correo":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(creg.correoelectronico) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Preguntas":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(total_preguntas) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Contestadas":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(total_respuestas) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "inicio,termino,tiempo":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( ( LOWER(extr.fechainicio) like lower('%[valor]%') )";
						where += " OR ( LOWER(extr.fechafin) like lower('%[valor]%') ) )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Término":
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
				String nombre = rs.getString("apellidopaterno");
				if(!rs.getString("apellidomaterno").equals("") && rs.getString("apellidomaterno") != null) {
					nombre += " " + rs.getString("apellidomaterno")
				}
				nombre += " " + rs.getString("primernombre");
				if(!rs.getString("segundonombre").equals("") && rs.getString("segundonombre") != null) {
					nombre += " " + rs.getString("segundonombre");
				}
				row.setIdBpm(rs.getLong("idbpm"));
				row.setNombre(nombre);
				row.setUni(rs.getString("campus"));
				row.setCorreoElectronico(rs.getString("correoelectronico"));
				row.setTelefono(rs.getString("telefono"));
				row.setCelular(rs.getString("telefonocelular"));
				row.setPreguntas(rs.getInt("total_preguntas"));
				row.setContestadas(rs.getInt("total_respuestas"));
				
				if(rs.getTimestamp("fechainicio") != null) {
					Date date = new Date(rs.getTimestamp("fechainicio").getTime());
					row.setInicio(formatter.format(date));
				} else {
					row.setInicio("...");
				}
				
				if(rs.getTimestamp("fechafin") != null) {
					Date date = new Date(rs.getTimestamp("fechafin").getTime());
					row.setTermino(formatter.format(date));
				}
				
//				if(rs.getTimestamp("tiempo") != null) {
//					Date date = new Date(rs.getTimestamp("tiempo").getTime());
//					row.setTiempo(formatterTime.format(date));
//				}
				
				if(rs.getString("fechaprueba")) {
					Date now = new Date();
					Date fechaprueba = formatterEntrada.parse(rs.getString("fechaprueba"));
					long diferenciaMilisegundos = fechaprueba.getTime() - now.getTime();
					long diferenciaDias = diferenciaMilisegundos / (24 * 60 * 60 * 1000);
					
					if(diferenciaDias < 3) {
						row.setFechaValida(false);
					} else {
						row.setFechaValida(true);
					}
				} else {
					row.setFechaValida(true);
				}
				
//				Object fieldValue = rs.getObject("tiempo");
//				
//				if(fieldValue instanceof Timestamp) {
//					Date date = new Date(rs.getTimestamp("tiempo").getTime());
//					row.setTiempo(formatterTime.format(date));
//				}
				
				Object fieldValue = rs.getObject("tiempo");
				
				if(fieldValue instanceof Timestamp) {
					Date date = new Date(rs.getTimestamp("tiempo").getTime());
					row.setTiempo(formatterTime.format(date));
				} else {
					if(rs.getString("tiempo") != null) {
						row.setTiempo(rs.getString("tiempo").split("\\.")[0]);
					}
				}
				
				row.setIdBanner(rs.getString("idbanner"));
				row.setIdioma(rs.getString("idioma") != null ? rs.getString("idioma") : "ESP");
				row.setBloqueado(rs.getBoolean("usuariobloqueado"));
				row.setTerminado(rs.getBoolean("terminado"));
				row.setCaseidINVP(rs.getString("caseidinvp"))
				row.setEstatusINVP(rs.getString("estatusinvp") == null ? "Por iniciar" : rs.getString("estatusinvp"));
				row.setExamenReiniciado(rs.getBoolean("examenReiniciado"));
				row.setUsuarioBloqueado(rs.getBoolean("usuariobloqueadob"));
				row.setResultadoEnviado(rs.getBoolean("resultadoenviado"));
				row.setIdsesion(rs.getLong("idsesion"));	
							
				rows.add(row);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			
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
			try {
				con.rollback();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
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
	
	public Result insertUpdateIidiomaUsuario(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		Boolean success = false;
		String error_log = "";
		String success_log = "";
		Long resultReq = 0;
		Boolean existe = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			pstm = con.prepareStatement(Statements.GET_IDIOMA_EXISTE_USUARIO);
			pstm.setString(1, object.nombreusuario);
			
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				errorlog += " | existe :  GET_IDIOMA_EXISTE_USUARIO " 
				existe = rs.getBoolean("existe");
			}
			
			if(existe) {
				errorlog += " | UPDATE_IDIOMA_USUARIO "
				pstm = con.prepareStatement(Statements.UPDATE_IDIOMA_USUARIO);
				pstm.setString(1, object.idioma);
				pstm.setString(2, object.nombreusuario);
				pstm.executeUpdate();
				con.commit();
			} else {
				errorlog += " | UPDATE_IDIOMA_REGISTRO_BY_USERNAME "
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
				
				con.commit();
			}
			
			success = true;
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
	
	public Result getAspirantesTemporales(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = " WHERE  idio.isTemporal = true  ";
		String errorlog = "  ";
		String orderBy = " ORDER BY ";
		List < String > lstGrupo = new ArrayList < String > ();
		String errorLog = "";
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat formatterNoTime = new SimpleDateFormat("dd/MM/yyyy");
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			Long userLogged = context.getApiSession().getUserId();
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
			
			errorLog += " | lstGrupo.size(): " + lstGrupo.size().toString() + " | object.campus: " + object.campus.toString();
			if (lstGrupo.size() > 0 && object.campus == null) {
				where += " AND (";
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += " ccam.descripcion = '" + campusMiembro + "'"
					errorLog += " [| campusMiembro" + campusMiembro;
					if (i == (lstGrupo.size() - 1)) {
						where += ") "
					} else {
						where += " OR "
					}
				}
				errorLog += "] "
			} else if(object.campus != null) {
				where += " AND ccam.grupobonita = '" + object.campus + "'"
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
			
			String consultaCcount = Statements.GET_ASPIRANTES_SESIONES_TEMP_COUNT.replace("[WHERE]", where);
			pstm = con.prepareStatement(consultaCcount);
			rs = pstm.executeQuery();
			while (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("total_registros"));
			}
			
			String consulta = Statements.GET_ASPIRANTES_SESIONES_TEMP.replace("[WHERE]", where).replace("[ORDERBY]", orderBy)
			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new AspiranteSesionCustom();
				String nombre = rs.getString("apellidopaterno");
				if(!rs.getString("apellidomaterno").equals("") && rs.getString("apellidomaterno") != null) {
					nombre += " " + rs.getString("apellidomaterno")
				}
				nombre += " " + rs.getString("primernombre");
				if(!rs.getString("segundonombre").equals("") && rs.getString("segundonombre") != null) {
					nombre += " " + rs.getString("segundonombre");
				}
				row.setIdBpm(rs.getLong("idbpm"));
				row.setNombre(nombre);
				row.setUni(rs.getString("campus"));
				row.setCorreoElectronico(rs.getString("correoelectronico"));
				row.setTelefono(rs.getString("telefono"));
				row.setCelular(rs.getString("telefonocelular"));
				row.setPreguntas(rs.getInt("total_preguntas"));
				row.setContestadas(rs.getInt("total_respuestas"));
				
				if(rs.getTimestamp("fechainicio") != null) {
					Date date = new Date(rs.getTimestamp("fechainicio").getTime());
					row.setInicio(formatter.format(date));
				} else {
					row.setInicio("...");
				}
				if(rs.getTimestamp("fechafin") != null) {
					Date date = new Date(rs.getTimestamp("fechafin").getTime());
					row.setTermino(formatter.format(date));
				}
				
				row.setIdBanner(rs.getString("idbanner"));
				row.setIdioma(rs.getString("idioma") != null ? rs.getString("idioma") : "ESP");
				row.setBloqueado(rs.getBoolean("usuariobloqueado"));
				row.setTerminado(rs.getBoolean("terminado"));
				row.setCaseidINVP(rs.getString("caseidinvp"));
				row.setEstatusINVP(rs.getString("estatusinvp") == null ? "Por iniciar" : rs.getString("estatusinvp"));
				row.setExamenReiniciado(rs.getBoolean("examenReiniciado"));
				row.setUsuarioBloqueado(rs.getBoolean("usuariobloqueadob"));
				row.setTempentrada(rs.getString("temp_horainiciosesion"));
				row.setTempsalida(rs.getString("tes_horafinsesion"));
				if(rs.getTimestamp("temp_fechainiciosesion") != null) {
					Date date = new Date(rs.getTimestamp("temp_fechainiciosesion").getTime());
					row.setTempfecha(formatterNoTime.format(date));
				} 
//				row.setTempfecha(rs.getString("temp_fechainiciosesion"));
				row.setTempprueba(rs.getString("temp_sesion"));
				row.setTemptoleranciaentrada(rs.getString("temp_toleranciaentrada"));
				row.setTemptoleranciaSalida(rs.getString("temp_toleranciasalida"));
				
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
	
	public Result updateIdiomaTodos(String idioma, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
		Boolean closeCon = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			Result resultadoAspirantes = getAspirantesTodos(jsonData, false, context);
			
			List<AspiranteSesionCustom> aspirantes = (List<AspiranteSesionCustom>) resultadoAspirantes.getData();
			
			for(AspiranteSesionCustom aspirante : aspirantes) {
				insertUpdateIidiomaUsuarioTodos(idioma, aspirante.getCorreoElectronico());
			}
			
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		}
		
		return resultado;
	}
	
	public Result insertUpdateIidiomaUsuarioTodos(String idioma, String nombreusuario) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		Boolean success = false;
		String error_log = "";
		String success_log = "";
		Long resultReq = 0;
		Boolean existe = false;
		
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			pstm = con.prepareStatement(Statements.GET_IDIOMA_EXISTE_USUARIO);
			pstm.setString(1, nombreusuario);
			
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				errorlog += " | existe :  GET_IDIOMA_EXISTE_USUARIO "
				existe = rs.getBoolean("existe");
			}
			
			if(existe) {
				errorlog += " | UPDATE_IDIOMA_USUARIO "
				pstm = con.prepareStatement(Statements.UPDATE_IDIOMA_USUARIO);
				pstm.setString(1, idioma);
				pstm.setString(2, nombreusuario);
				pstm.executeUpdate();
				con.commit();
			} else {
				errorlog += " | UPDATE_IDIOMA_REGISTRO_BY_USERNAME "
				pstm = con.prepareStatement(Statements.UPDATE_IDIOMA_REGISTRO_BY_USERNAME);
				pstm.setString(1, idioma);
				pstm.setString(2, nombreusuario);
				pstm.setBoolean(3, false);
				pstm.setBoolean(4, false);
				pstm.setString(5, "");
				pstm.setBoolean(6, false);
				//resultReq = pstm.executeUpdate();
				rs = pstm.executeQuery();
				if(rs.next()) {
					resultReq = rs.getLong("persistenceid")
				}
				
				con.commit();
			}
			
			success = true;
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
	
	public Result bloquearAspiranteDef(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		List<Boolean> data = new ArrayList<Boolean>();
		
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement("INSERT INTO AspirantesBloqueados (persistenceId, persistenceVersion, username, fechaBloqueo) VALUES (case when (SELECT max(persistenceId) + 1 from AspirantesBloqueados ) is null then 1 else (SELECT max(persistenceId) + 1 from AspirantesBloqueados) end, 0, ?, now())");
			pstm.setString(1, username);
			pstm.executeUpdate();
			
			pstm = con.prepareStatement(Statements.UPDATE_SESION_USUARIO);
			pstm.setBoolean(1, true);
			pstm.setString(2, username);
			pstm.executeUpdate();
			
			con.commit();
			
			data.add(true);
			resultado.setData(data);
			resultado.setSuccess(true);
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			errorLog += " | Fallido | " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			try {
				con.rollback();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result desbloquearAspiranteDef(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		List<Boolean> data = new ArrayList<Boolean>();
		
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement("DELETE FROM AspirantesBloqueados WHERE username = ?");
			pstm.setString(1, username);
			pstm.executeUpdate();
			
			pstm = con.prepareStatement(Statements.UPDATE_SESION_USUARIO);
			pstm.setBoolean(1, false);
			pstm.setString(2, username);
			pstm.executeUpdate();
			
			con.commit();
			
			data.add(true);
			resultado.setData(data);
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			try {
				con.rollback();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result checkBloqueado(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Boolean usuariobloqueado = false;
		List<Boolean> data = new ArrayList<Boolean>();
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT * FROM  AspirantesBloqueados WHERE username = ?");
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				usuariobloqueado = true;
			} else {
				usuariobloqueado = false;
			}
			
			data.add(usuariobloqueado);
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			try {
				con.rollback();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result getUsuariosBloqueados(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			List<Map<String, Boolean>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexionBonita();
			
			if(object.username != null && !object.username.equals("")) {
				where = " WHERE username = '" + object.username + "' ";
			}
			
			pstm = con.prepareStatement(Statements.GET_USUARIOS_BLOQUEADOS.replace("[WHERE]", where));
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery();
			
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()) {
				rows.add(true);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado
	}
	
	public Result checkTolerancia(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Boolean hasTolerance = false;
		List<Boolean> data = new ArrayList<Boolean>();
		String errorInfo = "";
		Boolean examenReiniciado = false;
		Boolean puedeentrar = false;
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_INVP_INSTANCIA);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			errorLog += "| 1 ";
			if(rs.next()) {
				examenReiniciado = rs.getBoolean("examenReiniciado");
				errorLog += "| 2 " +  rs.getString("examenReiniciado");
			} else {
				errorLog += "| 3 ";
				examenReiniciado = false;
			}
			
			if(examenReiniciado != true) {
				errorLog += "| 4 ";
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_TOLERANCIA_BY_USERNAME);
				pstm.setString(1, username);
				rs = pstm.executeQuery();
				
				if(rs.next()) {
					errorLog += "| 5 ";
					hasTolerance = rs.getBoolean("tienetolerancia");
				} else {
					errorLog += "| 6 ";
					hasTolerance = false;
					puedeentrar = false;
				}
			} else {
				errorLog += "| 7 ";
			}
			
			if(hasTolerance != true || examenReiniciado == true) {
				errorLog += "| 8 ";
				pstm = con.prepareStatement(Statements.GET_TOLERANCIATEMP_BY_USERNAME);
				pstm.setString(1, username);
				rs = pstm.executeQuery();
				
				if(rs.next()) {
					errorLog += "| 9 ";
					hasTolerance = rs.getBoolean("tienetolerancia");
				} else {
					errorLog += "| 10 ";
					hasTolerance = false;
				}
			}
			
			errorLog += "| 11 ";
			
			data.add(hasTolerance);
			resultado.setData(data);
			resultado.setSuccess(true);
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	} 
	
	public Result checkToleranciaV2(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Boolean hasTolerance = false;
		List<Boolean> data = new ArrayList<Boolean>();
		String errorInfo = "";
		Boolean examenReiniciado = false;
		Boolean puedeentrar = false;
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_INVP_INSTANCIA);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			errorLog += "| 1 ";
			if(rs.next()) {
				examenReiniciado = rs.getBoolean("examenReiniciado");
				errorLog += "| 2 " +  rs.getString("examenReiniciado");
			} else {
				errorLog += "| 3 ";
				examenReiniciado = false;
			}
			
			if(examenReiniciado != true) {
				errorLog += "| 4 ";
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_TOLERANCIA_BY_USERNAME);
				pstm.setString(1, username);
				rs = pstm.executeQuery();
				
				if(rs.next()) {
					errorLog += "| 5 ";
					hasTolerance = rs.getBoolean("tienetolerancia");
				} else {
					errorLog += "| 6 ";
					hasTolerance = false;
				}
			}
			
			if(hasTolerance != true || examenReiniciado == true) {
				errorLog += "| 8 ";
				pstm = con.prepareStatement(Statements.GET_TOLERANCIATEMP_BY_USERNAME);
				pstm.setString(1, username);
				rs = pstm.executeQuery();
				
				if(rs.next()) {
					errorLog += "| 9 ";
					hasTolerance = rs.getBoolean("tienetolerancia");
					if(!hasTolerance) {
						errorLog += "| 11 ";
						throw new Exception("no_tolerancia");
					}
				} else {
					errorLog += "| 10 ";
					throw new Exception("no_tolerancia");
				}
			}
			
			errorLog += "| 12 ";
			
			data.add(hasTolerance);
			resultado.setData(data);
			resultado.setSuccess(true);
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		} finally {
//			if(closeCon) {
//				new DBConnect().closeObj(con, stm, rs, pstm)
//			}
			new DBConnect().closeObj(con, stm, rs, pstm);
		}
		
		return resultado;
	}
	
	public Result checkToleranciaTemp(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Boolean hasTolerance = false;
		List<Boolean> data = new ArrayList<Boolean>();
		String errorInfo = "";
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_TOLERANCIATEMP_BY_USERNAME);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				hasTolerance = rs.getBoolean("tienetolerancia");
			} else {
				throw new Exception("no_sesion_asignada");
			}
			
			data.add(hasTolerance);
//			data.add(hasTolerance);//Para pued eentrar 
			resultado.setData(data);
			resultado.setSuccess(true);
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result checkToleranciaFront(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Boolean hasTolerance = false;
		Boolean isTemporal = false;
		List<Boolean> data = new ArrayList<Boolean>();
		String errorInfo = "";
		
		try {
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_USUARIO_TEMPORAL);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				isTemporal = rs.getBoolean("istemporal");
			} 
			
			if(!isTemporal) {
				pstm = con.prepareStatement(Statements.GET_TOLERANCIA_BY_USERNAME);
			} else {
				pstm = con.prepareStatement(Statements.GET_TOLERANCIATEMP_BY_USERNAME);
//				hasTolerance = true;
			}
			
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				hasTolerance = rs.getBoolean("tienetolerancia");
			} else {
				hasTolerance = false;
				throw new Exception("no_sesion_asignada");
			}
			
			data.add(hasTolerance);
			resultado.setData(data);
			resultado.setSuccess(true);
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result getAspirantesTodos(String jsonData, Boolean preguntas, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = " WHERE ( ( ctpr.descripcion = 'Examen Psicométrico'  ";
		String errorlog = "  ";
		String orderBy = " ORDER BY ";
		List < String > lstGrupo = new ArrayList < String > ();
		String errorLog = "";
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
		String idprueba = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			Long userLogged = context.getApiSession().getUserId();
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
			
//			if (lstGrupo.size() > 0 && object.campus == null) {
//				where += " AND (";
//				for (Integer i = 0; i < lstGrupo.size(); i++) {
//					String campusMiembro = lstGrupo.get(i);
//					where += " ccam.descripcion = '" + campusMiembro + "'"
//					
//					if (i == (lstGrupo.size() - 1)) {
//						where += ") "
//					} else {
//						where += " OR "
//					}
//				}
//			} else if(object.campus != null) {
//				where += " AND ccam.grupobonita = '" + object.campus + "'"
//			}
			
			where += " ) OR (temp.username IS NOT NULL AND (CASE WHEN sesq.sesiones_pid IS NOT NULL THEN ctpr.descripcion = 'Examen Psicométrico'  ELSE true END ) ) )";
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				switch (filtro.get("columna")) {
					case "id_sesion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
//						where += " ( prue.sesion_pid = [valor] )";
						where += "( (( sesq.sesiones_pid = [valor] )";
						where += "OR ( temp.idprueba = [valor] ) )";
						where = where.replace("[valor]", filtro.get("valor"));
						idprueba = filtro.get("valor");
						
					break;
					case "idBpm,idbanner":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( ( creg.caseid::VARCHAR like '%[valor]%' )";
						where += " OR ( dets.idbanner like '%[valor]%' ) )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "nombre":
						errorlog += "creg.primernombre"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( ( LOWER(creg.primernombre) like lower('%[valor]%') ) ";
						where += " OR ( LOWER(creg.segundonombre) like lower('%[valor]%') ) ";
						where += " OR ( LOWER(creg.apellidopaterno) like lower('%[valor]%') )  ";
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
					case "uni":
						errorlog += "ccam.descripcion "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(ccam.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "telefono,celular,correo":
						errorlog += "sdad.telefono "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( ( LOWER(sdad.telefono) like lower('%[valor]%') )";
						where += " OR ( LOWER(sdad.telefonocelular) like lower('%[valor]%') )";
						where += " OR ( LOWER(creg.correoelectronico) like lower('%[valor]%') ) )";
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
					case "preguntas":
						errorlog += "total_preguntas "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(total_preguntas::VARCHAR) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "contestadas":
						errorlog += "total_respuestas "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(total_respuestas::VARCHAR) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "inicio,termino,tiempo":
						errorlog += "extr.fechainicio "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( ( LOWER(extr.fechainicio) like lower('%[valor]%') )";
						where += " OR ( LOWER(extr.fechafin) like lower('%[valor]%') ) )";
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
						errorlog += "Estatus"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(invp.estatus) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					default:
						break;
				}
			}
			
			where += " )";

			switch(object.orderby) {
				case "idBpm":
					orderBy = " ORDER BY creg.caseid " + object.orientation;
				break;
				case "idbanner":
					orderBy = " ORDER BY dets.idbanner " + object.orientation;
				break;
				case "nombre":
					orderBy = " ORDER BY creg.apellidopaterno " + object.orientation;
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
				case "inicio":
					orderBy = " ORDER BY extr.fechainicio " + object.orientation;
				break;
				case "termino":
					orderBy = " ORDER BY extr.fechafin " + object.orientation;
				break;
				default:
					orderBy = " ORDER BY creg.apellidopaterno " + object.orientation;
				break;
			}
			
			AspiranteSesionCustom row = new AspiranteSesionCustom();
			List <AspiranteSesionCustom> rows = new ArrayList <AspiranteSesionCustom>();
			closeCon = validarConexion();
			
//			where += " ) OR (temp.username IS NOT NULL  AND temp.idprueba = " + idprueba + ")";
//			where += " ) OR (temp.username IS NOT NULL AND (CASE WHEN sesq.sesiones_pid IS NOT NULL THEN ctpr.descripcion = 'Examen Psicométrico'  ELSE true END ) AND temp.idprueba = " + idprueba + ")";
//			) OR (temp.username IS NOT NULL AND (CASE WHEN seas.sesiones_pid IS NOT NULL THEN ctpr.descripcion = 'Examen Psicométrico' END ) AND temp.idprueba = 237)
			String consultaCcount = Statements.GET_ASPIRANTES_SESIONES_COUNT_TODOS.replace("[WHERE]", where);
			pstm = con.prepareStatement(consultaCcount);
			pstm.setLong(1, Long.valueOf(idprueba));
			pstm.setLong(2, Long.valueOf(idprueba));
			pstm.setLong(3, Long.valueOf(idprueba));
			pstm.setLong(4, Long.valueOf(idprueba));
			
			rs = pstm.executeQuery();
			while (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("total_registros"));
			}
			
//			errorLog += where + " " + orderBy;
			String consulta = "";
			errorLog += "preguntas:" + preguntas.toString();
			
			if(preguntas == true) {
				errorLog += "| 1 GET_ASPIRANTES_SESIONES_TODOS" 
				consulta = Statements.GET_ASPIRANTES_SESIONES_TODOS.replace("[WHERE]", where).replace("[ORDERBY]", orderBy);
			} else {
				errorLog += "| 1.1 GET_ASPIRANTES_SESIONES_TODOS_NO_PREGUNTAS"
				consulta = Statements.GET_ASPIRANTES_SESIONES_TODOS_NO_PREGUNTAS.replace("[WHERE]", where).replace("[ORDERBY]", orderBy);
			}
			errorLog += "| 2"
			
//			String consulta = Statements.GET_ASPIRANTES_SESIONES_TODOS.replace("[WHERE]", where).replace("[ORDERBY]", orderBy);
//			String consulta = Statements.GET_ASPIRANTES_SESIONES_TODOS_NO_PREGUNTAS.replace("[WHERE]", where).replace("[ORDERBY]", orderBy);
//			errorLog += consulta;
			
			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, Long.valueOf(idprueba));
			pstm.setLong(2, Long.valueOf(idprueba));
			pstm.setLong(3, Long.valueOf(idprueba));
			pstm.setLong(4, Long.valueOf(idprueba));
			pstm.setInt(5, object.limit);
			pstm.setInt(6, object.offset);
//			pstm.setInt(1, object.limit);
//			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new AspiranteSesionCustom();
				String nombre = rs.getString("apellidopaterno");
				if(!rs.getString("apellidomaterno").equals("") && rs.getString("apellidomaterno") != null) {
					nombre += " " + rs.getString("apellidomaterno")
				}
				nombre += " " + rs.getString("primernombre");
				if(!rs.getString("segundonombre").equals("") && rs.getString("segundonombre") != null) {
					nombre += " " + rs.getString("segundonombre");
				}
				row.setIdBpm(rs.getLong("idbpm"));
				row.setNombre(nombre);
				row.setUni(rs.getString("campus"));
				row.setCorreoElectronico(rs.getString("correoelectronico"));
				row.setTelefono(rs.getString("telefono"));
				row.setCelular(rs.getString("telefonocelular"));
				row.setPreguntas(rs.getInt("total_preguntas"));
				row.setContestadas(rs.getInt("total_respuestas"));
				
				if(rs.getTimestamp("fechainicio") != null) {
					Date date = new Date(rs.getTimestamp("fechainicio").getTime());
					row.setInicio(formatter.format(date));
				} else {
					row.setInicio("...");
				}
				
				if(rs.getTimestamp("fechafin") != null) {
					Date date = new Date(rs.getTimestamp("fechafin").getTime());
					row.setTermino(formatter.format(date));
				}
				
//				if(rs.getTimestamp("tiempo") != null) {
//					try {
//						Date date = new Date(rs.getTimestamp("tiempo").getTime());
//						row.setTiempo(formatterTime.format(date));
//					} catch(Exception e){
//						row.setTiempo("+ de 24 horas");
//					}
//				}
				
				Object fieldValue = rs.getObject("tiempo");
				
				if(fieldValue instanceof Timestamp) {
					Date date = new Date(rs.getTimestamp("tiempo").getTime());
					row.setTiempo(formatterTime.format(date));
				} else {
					if(rs.getString("tiempo") != null) {
						row.setTiempo(rs.getString("tiempo").split("\\.")[0]);
					}
				}
				
				row.setIdBanner(rs.getString("idbanner"));
				row.setIdioma(rs.getString("idioma") != null ? rs.getString("idioma") : "ESP");
				row.setBloqueado(rs.getBoolean("usuariobloqueado"));
				row.setTerminado(rs.getBoolean("terminado"));
				row.setCaseidINVP(rs.getString("caseidinvp"));
				row.setCaseidINVP_temp(rs.getString("caseidinvp_temp"));
				if(rs.getString("estatusinvp") == null) {
					if(rs.getBoolean("terminado") == true ) {
						row.setEstatusINVP("Prueba terminada");
					} else {
						row.setEstatusINVP(rs.getString("estatusinvp") == null ? "Por iniciar" : rs.getString("estatusinvp"));
					}
				} else {
					row.setEstatusINVP(rs.getString("estatusinvp"));
				}
//				row.setEstatusINVP(rs.getString("estatusinvp") == null ? "Por iniciar" : rs.getString("estatusinvp"));
				row.setExamenReiniciado(rs.getBoolean("examenReiniciado"));
				row.setUsuarioBloqueado(rs.getBoolean("usuariobloqueadob"));
				row.setIsTemporal(rs.getBoolean("istemporal"));
				row.setTempentrada(rs.getString("horainiciosesion_temp"));
				row.setTempsalida(rs.getString("horafinsesion_temp"));
				row.setTempfecha(rs.getString("fechainiciosesion_temp"));
				row.setTempprueba(rs.getString("nombre__temp"));
				row.setTemptoleranciaentrada(rs.getString("toleranciaentradasesion_temp"));
				row.setTemptoleranciaSalida(rs.getString("toleranciasalidasesion_temp"));
				
				if(rs.getBoolean("resultadoenviado_temp")) {
					row.setResultadoEnviado(rs.getBoolean("resultadoenviado_temp"));
				} else {
					row.setResultadoEnviado(rs.getBoolean("resultadoenviado"));
				}
				
//				row.setResultadoEnviado(rs.getBoolean("resultadoenviado"));
				row.setSesionAsignada(rs.getBoolean("sesionasignada"));
				row.setIdsesion(rs.getLong("sesiones_pid"));
				row.setContestadas_temp(rs.getInt("total_respuestas_temp"));
				
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
	
	public Result terminarTodos(Long idesion, RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
		Boolean closeCon = false, processId = false;
		
		try {
			AspiranteSesionCustom row = new AspiranteSesionCustom();
			List <?> rows = new ArrayList <?>();
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_USUARIOS_BY_IDSESION);
			pstm.setLong(1, idesion);
			pstm.setLong(2, idesion);
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new AspiranteSesionCustom();
				row.setCaseidINVP(rs.getString("caseidinvp"));
				row.setEstatusINVP(rs.getString("estatusinvp"));
				row.setCorreoElectronico(rs.getString("correoelectronico"));
				ProcessAPI processAPI = context.getApiClient().getProcessAPI();
				IdentityAPI identityAPI =  context.getApiClient().getIdentityAPI();
				User user = identityAPI.getUserByUserName(rs.getString("correoelectronico"));
				List<TaskInstance> tasks = processAPI.getAssignedHumanTaskInstances(user.getId(), 0, 100, DEFAULT);
				TaskInstance taskToExecute = null;
				
				for(TaskInstance task: tasks) {
					if(task.name.equals("Examen INVP")) {
						taskToExecute = task;
						Map<String, Object> contract = new LinkedHashMap<String, Object>();
						Map<String, Object> instanciaINVPInput = new LinkedHashMap<String, Object>();
						instanciaINVPInput.put("mensajeTermino", "");
						contract.put("instanciaINVPInput", instanciaINVPInput);
						contract.put("terminadoFInput", true);
						contract.put("isTerminado", true);
						processAPI.assignAndExecuteUserTask(user.getId(), taskToExecute.getId(), contract);
						
						con.setAutoCommit(false);
						pstm = con.prepareStatement("UPDATE IdiomaINVPUsuario SET usuariobloqueado = ? WHERE username = ?")
						pstm.setBoolean(1, true);
						pstm.setString(2, row.getCorreoElectronico());
						pstm.executeUpdate();
						
						pstm = con.prepareStatement("UPDATE INVPExamenTerminado SET terminado = ? WHERE username = ?");
						pstm.setBoolean(1, true);
						pstm.setString(2, row.getCorreoElectronico());
						
						
						if(pstm.executeUpdate() == 0) {
							pstm = con.prepareStatement(Statements.INSERT_TERMINADO_EXAMEN);
							pstm.setString(1, row.getCorreoElectronico());
							pstm.setBoolean(2, true);
						}
						
						break;
					}
				}
			}
			
			pstm = con.prepareStatement(Statements.INSERT_SESION_TERMINADA);
			pstm.setLong(1, idesion);
			pstm.executeUpdate();

			resultado.setData(rows);
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
			e.printStackTrace();
		}  finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}
	
	public Result terminarUsuario(String username, Long caseid, RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
		Boolean closeCon = false, processId = false;
		
		try {
			AspiranteSesionCustom row = new AspiranteSesionCustom();
			List <?> rows = new ArrayList <?>();
			
			ProcessAPI processAPI = context.getApiClient().getProcessAPI();
			IdentityAPI identityAPI =  context.getApiClient().getIdentityAPI();
			User user = identityAPI.getUserByUserName(username);
			List<TaskInstance> tasks = processAPI.getAssignedHumanTaskInstances(user.getId(), 0, 100, DEFAULT);
			TaskInstance taskToExecute = null;
			if(tasks.size() > 0) {
				Boolean terminar =  false, bloquear = false, encontrado = false;
				
				for(TaskInstance task: tasks) {
					if(task.name.equals("Examen INVP")) {
						encontrado = true;
						taskToExecute = task;
						Map<String, Object> contract = new LinkedHashMap<String, Object>();
						Map<String, Object> instanciaINVPInput = new LinkedHashMap<String, Object>();
						instanciaINVPInput.put("mensajeTermino", "");
						contract.put("instanciaINVPInput", instanciaINVPInput);
						contract.put("terminadoFInput", true);
						contract.put("isTerminado", true);
						processAPI.assignAndExecuteUserTask(user.getId(), taskToExecute.getId(), contract);
						break;
					} else if (task.name.equals("Finalizar examen")){
						encontrado = true;
						closeCon = validarConexion();
						pstm = con.prepareStatement(Statements.GET_FECHA_TERMINO_BY_USERNAME);
						pstm.setString(1, username);
						rs = pstm.executeQuery();
						Long timer = 0L;
						
						if(rs.next()) {
							if(rs.getString("horafin_temp") != null) {
								timer = rs.getTimestamp("horafin_temp").getTime();
							} else {
								timer = rs.getTimestamp("horafin").getTime();
							}
							
							Result resultTimer = updateTimer(Long.valueOf(taskToExecute.getParentContainerId()), timer);
						}
						
						Map<String, Object> contract = new LinkedHashMap<String, Object>();
						contract.put("repetirExamenInput", true);
						processAPI.assignAndExecuteUserTask(user.getId(), taskToExecute.getId(), contract);
						break;
					}
					
					if(encontrado) {
						con.setAutoCommit(false);
						pstm = con.prepareStatement("UPDATE IdiomaINVPUsuario SET usuariobloqueado = ? WHERE username = ?")
						pstm.setBoolean(1, bloquear);
						pstm.setString(2, row.getCorreoElectronico());
						pstm.executeUpdate();
						
						pstm = con.prepareStatement("UPDATE INVPExamenTerminado SET terminado = ? WHERE username = ?");
						pstm.setBoolean(1, terminar);
						pstm.setString(2, row.getCorreoElectronico());
						pstm.executeUpdate();
						con.commit();
					}
				}
			} else {
				throw new Exception("No se puede realizar esta acción para este usuario.");
			}
			
			resultado.setData(rows);
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
			e.printStackTrace();
		}  finally {
			new DBConnect().closeObj(con, stm, rs, pstm);
		}
		
		return resultado;
	}
	
	public Result updateTimer(Long caseid, Long timer) {
		Result resultado = new Result();
		String errorLog = "";
		Boolean conection = false;
		
		try {
			String username = "";
			String password = "";
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
			
			ProcessAPI processAPI = apiClient.getProcessAPI();
//			String variableName = "terminarExamen";
			String variableName = "fechaTermino";
			Date fechaTermino = new Date(timer);
//			processAPI.updateProcessDataInstance(variableName, caseid, timer);
			processAPI.updateProcessDataInstance(variableName, caseid, fechaTermino);
			
			resultado.setSuccess(true);
			resultado.setError(errorLog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
			resultado.setError(errorLog);
		}
		
		return resultado;
	}
	
	public Result insertUpdateUsuarioTolerancias(String jsonData) {
		Result resultado = new Result();
		String errorlog = "";
		Boolean closeCon = false;
		Boolean existe = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.UPDATE_TOLERANCIAS_USUARIO);
			pstm.setInt(1, object.toleranciaminutos);
			pstm.setInt(2, object.toleranciasalidaminutos);
			if(object.idprueba == null) {
				pstm.setNull(3, 0);
			} else {
				pstm.setLong(3, object.idprueba);
			}
			pstm.setString(4, object.username);
			pstm.executeUpdate();
			con.commit();
			pstm = con.prepareStatement(Statements.GET_FECHA_TERMINO_BY_USERNAME);
			pstm.setString(1, object.username);
			rs = pstm.executeQuery();
			Long timer = 0L;
			
			if(rs.next()) {
				errorlog += " |5 ";
				if(rs.getString("horafin_temp") != null) {
					timer = rs.getTimestamp("horafin_temp").getTime();
				} else {
					timer = rs.getTimestamp("horafin").getTime();
				}
				if(object.caseid != null) {
					Result resultTimer = updateTimer(Long.valueOf(object.caseid), timer);
				}
			}
			
			resultado.setSuccess(true);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog += " | [ERROR]: " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	
	public Result insertUpdateUsuarioToleranciasTemp(String jsonData) {
		Result resultado = new Result();
		String errorlog = "";
		Boolean closeCon = false;
		Boolean existe = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.UPDATE_NUEVA_CONFIG_USUARIO);
			pstm.setString(1, object.aplicacion);
			pstm.setString(2, object.entrada);
			pstm.setString(3, object.salida);
			pstm.setInt(4, object.toleranciaminutos);
			pstm.setInt(5, object.toleranciasalidaminutos);
			if(object.idprueba == null) {
				pstm.setNull(6, 0);
			} else {
				pstm.setLong(6, object.idprueba);
			}
			pstm.setString(7, object.username);
			pstm.executeUpdate();
			
			con.commit();
			
			pstm = con.prepareStatement(Statements.GET_FECHA_TERMINO_BY_USERNAME);
			pstm.setString(1, object.username);
			rs = pstm.executeQuery();
			Long timer = 0L;
			
			if(rs.next()) {
				errorlog += " |5 ";
				if(rs.getString("horafin_temp") != null) {
					timer = rs.getTimestamp("horafin_temp").getTime();
				} else {
					timer = rs.getTimestamp("horafin").getTime();
				}
				if(object.caseid != null) {
					Result resultTimer = updateTimer(Long.valueOf(object.caseid), timer);
				}
				
			}
			
			resultado.setSuccess(true);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog += " | [ERROR]: " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result updateTimerTodos(Long idsesion) {
		Result resultado = new Result();
		String errorLog = "";
		Boolean closeCon = false, processId = false;
		
		try {
			AspiranteSesionCustom row = new AspiranteSesionCustom();
			List <AspiranteSesionCustom> rows = new ArrayList <AspiranteSesionCustom>();
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_USUARIOS_BY_IDSESION);
			pstm.setLong(1, idsesion);
			pstm.setLong(2, idsesion);
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new AspiranteSesionCustom();
				row.setCaseidINVP(rs.getString("caseidinvp"));
				row.setEstatusINVP(rs.getString("estatusinvp"));
				row.setCorreoElectronico(rs.getString("correoelectronico"));
				
				rows.add(row);
			}
			
			for(AspiranteSesionCustom aspirante: rows) {
				pstm = con.prepareStatement(Statements.GET_FECHA_TERMINO_BY_USERNAME);
				pstm.setString(1, aspirante.getCorreoElectronico());
				rs = pstm.executeQuery();
				Long timer = 0L;
				
				if(rs.next()) {
					if(rs.getString("horafin_temp") != null) {
						timer = rs.getTimestamp("horafin_temp").getTime();
					} else {
						timer = rs.getTimestamp("horafin").getTime();
					}
					
					Result resultTimer = updateTimer(Long.valueOf(aspirante.getCaseidINVP()), timer);
				}
			}
			
			con.commit();
			resultado.setData(rows);
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
			e.printStackTrace();
		}  finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}
	
	public Result enviarRespuestas(String jsonData) {
		Result resultado = new Result();
		String errorLog = "";
		Boolean closeCon = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			
			
			
		}  catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
			e.printStackTrace();
		}  finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}
	
	public Result cerrarSesionUsuarioRemoto(String username, RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
		Boolean closeCon = false;
		
		try {
			Long user_id = context.apiClient.identityAPI.getUserByUserName(username).getId();
			
		}  catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
			e.printStackTrace();
		}  finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}
	
	public Result checkEstatusExamen(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		
		try {
			Map<String,Boolean> row = new HashMap<String,Boolean>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_SESION_TODAY);
			pstm.setString(1, username);
			
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				if(rs.getBoolean("correct_date") == false) {
					throw new Exception("fecha_incorrecta");
				} else if(rs.getBoolean("exameniniciado") == false) {
					throw new Exception("examen_no_iniciado");
				} else if(rs.getBoolean("finalizada") == true) {
					throw new Exception("examen_finalizado");
				}
			} else {
				throw new Exception("no_sesion_asignada");
			}
			
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
}