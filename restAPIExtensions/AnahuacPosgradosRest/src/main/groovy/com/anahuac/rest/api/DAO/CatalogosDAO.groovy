	package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.sql.Timestamp
import java.sql.Types
import java.text.SimpleDateFormat

import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatPeriodo
import com.anahuac.posgrados.catalog.PSGRCatCampus
import com.anahuac.posgrados.catalog.PSGRCatCampusDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.StatementsCatalogos
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.custom.CatDescuentosCustom
import com.anahuac.rest.api.Entity.custom.CatGestionEscolar
import com.anahuac.rest.api.Entity.custom.ConfiguracionesGenerales
import com.anahuac.rest.api.Entity.db.CatCampusCustomFiltro
import com.anahuac.rest.api.Entity.db.CatGenerico
import com.anahuac.rest.api.Entity.db.CatPaisCustomFiltro
import com.anahuac.rest.api.Entity.db.CatRequisitosAdicionales
import com.anahuac.rest.api.Entity.db.PSGRCatEstado
import com.anahuac.rest.api.Entity.db.PSGRCatEstadoCivil
import com.anahuac.rest.api.Entity.db.PSGRCatEstatusProceso
import com.anahuac.rest.api.Entity.db.PSGRCatNacionalidad
import com.anahuac.rest.api.Entity.db.PSGRCatSiNo
import com.anahuac.rest.api.Entity.db.PSGRCatTipoEmpresa
import com.anahuac.rest.api.Entity.db.PSGRFiltroSeguridad
import groovy.json.JsonSlurper

class CatalogosDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogosDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Boolean validarConexion() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno = true
		}
		return retorno
	}
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true
		}
		return retorno;
	}
	public Result simpleSelect(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof List;
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			for(def row: object) {
				pstm = con.prepareStatement(row);
				rs = pstm.executeQuery();
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
				
				resultado.setSuccess(true);
				resultado.setData(rows);
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result simpleSelectBonita(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof List;
			
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexionBonita();
			for(def row: object) {
				pstm = con.prepareStatement(row);
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
				
				resultado.setSuccess(true);
				resultado.setData(rows);
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getCatGenerico(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "", orderby = "ORDER BY ", errorLog="entro";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			String consulta = StatementsCatalogos.GET_CATGENERICO;
			CatGenerico row = new CatGenerico();
			List < CatGenerico > rows = new ArrayList < CatGenerico > ();
			closeCon = validarConexion();
			where += " WHERE isEliminado = false";
			errorLog +=" 1";
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {

				switch (filtro.get("columna")) {
					case "CLAVE":
						where += " AND LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "DESCRIPCION":
						where += " AND LOWER(DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "FECHACREACION":
						where += " AND LOWER(FECHACREACION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "PERSISTENCEID":
						where += " AND LOWER(PERSISTENCEID) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "PERSISTENCEVERSION":
						where += " AND LOWER(PERSISTENCEVERSION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "USUARIOCREACION":
						where += " AND LOWER(USUARIOCREACION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}
			errorLog +=" 2";
			switch (object.orderby) {
				case "CLAVE":
					orderby += "clave";
					break;
				case "DESCRIPCION":
					orderby += "descripcion";
					break;
				case "FECHACREACION":
					orderby += "fechaCreacion";
					break;
				case "ISELIMINADO":
					orderby += "isEliminado";
					break;
				case "PERSISTENCEID":
					orderby += "persistenceId";
					break;
				case "PERSISTENCEVERSION":
					orderby += "persistenceVersion";
					break;
				case "USUARIOCREACION":
					orderby += "usuarioCreacion";
					break;
				default:
					orderby += "persistenceid"
					break;
			}
			errorLog +=" 3";
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);
			consulta = consulta.replace("[CATALOGO]", object.catalogo)

			String consultaCount = StatementsCatalogos.GET_COUNT_CATGENERICO
			consultaCount = consultaCount.replace("[WHERE]", where);
			consultaCount = consultaCount.replace("[CATALOGO]", object.catalogo)
			errorLog +=" 4";
			pstm = con.prepareStatement(consultaCount);
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			errorLog +=" 4";
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)

			errorLog +=" 5";
			rs = pstm.executeQuery()
			while (rs.next()) {

				row = new CatGenerico();
				row.setClave(rs.getString("clave"))
				row.setDescripcion(rs.getString("descripcion"));
				row.setFechaCreacion(rs.getString("fechacreacion"));
				row.setIsEliminado(rs.getBoolean("isEliminado"));
				row.setPersistenceId(rs.getLong("PERSISTENCEID"));
				row.setPersistenceId_string(String.valueOf(row.getPersistenceId()));				
				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
				row.setUsuarioCreacion(rs.getString("usuariocreacion"));

				rows.add(row)
			}
			errorLog +=" 6";
			//
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
	
	public Result getCatalogosGenericos(String catalogo) {
		Result resultado = new Result();
		Boolean closeCon = false;
		CatGenerico objCatGenerico = new CatGenerico();
		List<CatGenerico> lstCatGenerico= new ArrayList<CatGenerico>();
		try {
				closeCon = validarConexion();
				String consulta = StatementsCatalogos.GET_CATGENERICO;
				consulta = consulta.replace("[WHERE]", " WHERE isEliminado = false");
				consulta = consulta.replace("[CATALOGO]", catalogo);
				consulta = consulta.replace("[ORDERBY]", "");
				consulta = consulta.replace("[LIMITOFFSET]", "");

				pstm = con.prepareStatement(consulta);
				rs = pstm.executeQuery();
				while(rs.next()) {
					objCatGenerico = new CatGenerico();
					objCatGenerico.setPersistenceId(rs.getLong("PERSISTENCEID"));
					objCatGenerico.setPersistenceId_string(String.valueOf(objCatGenerico.getPersistenceId()));
					objCatGenerico.setClave(rs.getString("CLAVE"));
					objCatGenerico.setDescripcion(rs.getString("DESCRIPCION"));
					objCatGenerico.setFechaCreacion(rs.getString("FECHACREACION"));
					objCatGenerico.setIsEliminado(rs.getBoolean("ISELIMINADO"));
					objCatGenerico.setPersistenceVersion(rs.getLong("PERSISTENCEVERSION"));
					objCatGenerico.setUsuarioCreacion(rs.getString("USUARIOCREACION"));
					lstCatGenerico.add(objCatGenerico)
				}
				resultado.setData(lstCatGenerico)
				resultado.setSuccess(true)
				
			} catch (Exception e) {
				LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError("[getCatTipoMoneda] " + e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result insertCatFiltroSeguridad(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
	    Boolean closeCon = false;
	
	    try {
	        closeCon = validarConexion();
	        def jsonSlurper = new JsonSlurper();
	        def object = jsonSlurper.parseText(jsonData);
			
			if(object.rol.equals("") || object.rol == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.servicio.equals("") || object.servicio == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
	        pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATFILTROSEGURIDAD);
	        pstm.setLong(1, 0);
	        pstm.setString(2, object.rol);
	        pstm.setString(3, object.servicio);
		
	        if (pstm.executeUpdate() > 0) {
	            resultado.setSuccess(true);
	        } else {
	            throw new Exception("No se pudo insertar el registro.");
	        }
	    } catch (Exception e) {
	        resultado.setSuccess(false);
	        resultado.setError("[insertCatFiltroSeguridad] " + e.getMessage());
	    } finally {
	        if (closeCon) {
	            new DBConnect().closeObj(con, stm, rs, pstm);
	        }
	    }
	
	    return resultado;
	}




    public Result deleteCatFiltroSeguridad(String jsonData, RestAPIContext context) {
	    Result resultado = new Result();
	    Boolean closeCon = false;
	
	    try {
	        closeCon = validarConexion();
	        def jsonSlurper = new JsonSlurper();
	        def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			} 
	
	        pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATFILTROSEGURIDAD);
	        pstm.setLong(1, object.persistenceid);
	
	        if (pstm.executeUpdate() > 0) {
	            resultado.setSuccess(true);
	        } else {
	            throw new Exception("No se pudo eliminar el registro.");
	        }
	    } catch (Exception e) {
	        resultado.setSuccess(false);
			resultado.setError("[deleteCatFiltroSeguridad] " + e.getMessage())
	    } finally {
	        if (closeCon) {
	            new DBConnect().closeObj(con, stm, rs, pstm);
	        }
	    }
	
	    return resultado;
	}

    public Result updateCatFiltroSeguridad(String jsonData, RestAPIContext context) {
	    Result resultado = new Result();
	    Boolean closeCon = false;
	
	    try {
	        closeCon = validarConexion();
	        def jsonSlurper = new JsonSlurper();
	        def object = jsonSlurper.parseText(jsonData)
			
			if(object.rol.equals("") || object.rol == null) {
				throw new Exception("El campo \"rol\" no debe ir vacío");
			} else if(object.servicio.equals("") || object.servicio == null) {
				throw new Exception("El campo \"servicio\" no debe ir vacío");
			} else if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceId\" no debe ir vacío");
			}
	
	        pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATFILTROSEGURIDAD);
	        pstm.setString(1, object.rol);
	        pstm.setString(2, object.servicio);
	        pstm.setLong(3, object.persistenceid);
	
	        if (pstm.executeUpdate() > 0) {
	            resultado.setSuccess(true);
	        } else {
				throw new Exception("No se pudo modificar el registro.")
	        }
	    } catch (Exception e) {
	        resultado.setSuccess(false);
			resultado.setError("[updateCatFiltroSeguridad] " + e.getMessage())
	    } finally {
	        if (closeCon) {
	            new DBConnect().closeObj(con, stm, rs, pstm);
	        }
	    }
	
	    return resultado;
	}
	
	public Result getCatFiltroSeguridad(String jsonData) {
	    Result resultado = new Result();
	    Boolean closeCon = false;
	    List<PSGRFiltroSeguridad> data = new ArrayList<>();
	    String where = ""; // Aplicar filtro por defecto para registros no eliminados
	    String orderby = ""; // Ordenamiento por defecto
	
	    try {
	        // Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
	        def jsonSlurper = new JsonSlurper();
	        def object = jsonSlurper.parseText(jsonData);
	
	        closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "rol":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(rol) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "servicio":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(servicio) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

	        String consulta = StatementsCatalogos.SELECT_CATFILTROSEGURIDAD.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
	        pstm = con.prepareStatement(consulta);
	        rs = pstm.executeQuery();
	
	        while (rs.next()) {
	            PSGRFiltroSeguridad row = new PSGRFiltroSeguridad();
	            row.setPersistenceid(rs.getLong("persistenceid"));
	            row.setRol(rs.getString("rol"));
	            row.setServicio(rs.getString("servicio"));
	
	            data.add(row);
	        }
	
	        resultado.setData(data);
	        resultado.setSuccess(true);
	    } catch (Exception e) {
	        resultado.setSuccess(false);
	        resultado.setError("[getCatFiltroSeguridad] " + e.getMessage());
	    } finally {
	        if (closeCon) {
	            new DBConnect().closeObj(con, stm, rs, pstm);
	        }
	    }
	
	    return resultado;
	}
	
	public Result insertCatPais(String jsonData, RestAPIContext context) {
	    Result resultado = new Result();
	    Boolean closeCon = false;
	
	    try {
	        closeCon = validarConexion();
	        def jsonSlurper = new JsonSlurper();
	        def object = jsonSlurper.parseText(jsonData);
	
	        if (object == null || object.isEmpty()) {
	            throw new Exception("La lista \"lstCatPaisInput\" no debe estar vacía.");
	        }
	
			if (object.orden == null || object.orden.isEmpty()) {
				throw new Exception("El campo \"Orden\" no debe ir vacío.");
			} else if (object.clave == null || object.clave.isEmpty()) {
				throw new Exception("El campo \"Clave\" no debe ir vacío.");
			} else if (object.descripcion == null || object.descripcion.isEmpty()) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío.");
			}

			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATPAIS);
			pstm.setInt(1, 0);
			pstm.setBoolean(2, false); // is_eliminado
			pstm.setString(3, object.clave);
			pstm.setString(4, object.descripcion);
			pstm.setString(5, object.usuario_creacion);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(6, fechaHoraFormateada);
			String ordenString = object.orden;
			try {
			    int ordenInt = Integer.parseInt(ordenString);
			    pstm.setInt(7, ordenInt);
			} catch (NumberFormatException e) {
			    e.printStackTrace();
			}
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
			
	    } catch (Exception e) {
	        resultado.setSuccess(false);
	        resultado.setError("[insertCatPais] " + e.getMessage());
	    } finally {
	        if (closeCon) {
	            new DBConnect().closeObj(con, stm, rs, pstm);
	        }
	    }
	
	    return resultado;
	}




	public Result deleteCatPais(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceids.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío" + object);
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATPAIS);
			pstm.setBoolean(1, true);
			pstm.setLong(2, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatPais] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatPais(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			} else if(object.orden.equals("") || object.usuario_creacion == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATPAIS);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setInt(3, object.orden);
			pstm.setString(4, object.usuario_creacion);
			pstm.setLong(5, object.persistenceId);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatPais] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatPais(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		String where = "WHERE IS_ELIMINADO=false", orderby = "ORDER BY "
		String consulta = StatementsCatalogos.SELECT_CATPAIS;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			CatPaisCustomFiltro row = new CatPaisCustomFiltro();
			List < CatPaisCustomFiltro > rows = new ArrayList < CatPaisCustomFiltro > ();
			errorlog += " Antes de validar conexion "
			closeCon = validarConexion();
			errorlog += " Despues de validar conexion "
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				errorlog += " Filtros "
				switch (filtro.get("columna")) {
					case "ORDEN":
				        if (where.contains("WHERE")) {
				            where += " AND ";
				        } else {
				            where += " WHERE ";
				        }
				        where += " CAST(orden AS TEXT) ";
				        if (filtro.get("operador").equals("Igual a")) {
				            where += "= '" + filtro.get("valor") + "'";
				        } else {
				            where += " LIKE '%" + filtro.get("valor") + "%'";
				        }
				        break;
					case "CLAVE":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "DESCRIPCIÓN":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "USUARIO CREACIÓN":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(USUARIO_CREACION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "FECHA CREACIÓN":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(FECHA_CREACION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}
			errorlog += " Order by "
			switch (object.orderby) {
				case "ORDEN":
					orderby += "orden";
					break;
				case "CLAVE":
					orderby += "clave";
					break;
				case "DESCRIPCIÓN":
					orderby += "descripcion";
					break;
				case "USUARIO CREACIÓN":
					orderby += "usuario_creacion";
					break;
				case "FECHA CREACIÓN":
					orderby += "fecha_creacion";
					break;
				default:
					orderby += "persistenceid"
					break;
			}
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);
			errorlog += " Consulta " + consulta
			pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")

			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)

			rs = pstm.executeQuery()

			while (rs.next()) {
				row = new CatPaisCustomFiltro();
				row.setClave(rs.getString("clave"))
				row.setDescripcion(rs.getString("descripcion"));
				//row.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechaCreacion")))
				row.setFechaCreacion(rs.getString("fecha_creacion"));
				row.setIsEliminado(rs.getBoolean("is_eliminado"));
				row.setOrden(rs.getLong("orden"));
				row.setPersistenceId(rs.getLong("persistenceId"));
				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
				row.setUsuarioCreacion(rs.getString("usuario_creacion"));
				rows.add(row)
			}
			resultado.setSuccess(true)

			resultado.setData(rows)

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog += " ERROR " + e.getMessage();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result insertCatEstado(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATESTADO);
			pstm.setBoolean(1, false); // is_eliminado
			pstm.setLong(2, 1);
			pstm.setString(3, object.clave);
			pstm.setString(4, object.descripcion);
			pstm.setString(5, object.usuario_creacion);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(6, fechaHoraFormateada);
			pstm.setInt(7, object.orden);
			pstm.setInt(8, 0);  // Establecer el valor para el parámetro 8
			
			if (pstm.executeUpdate() > 0) {
			    resultado.setSuccess(true);
			} else {
			    throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatEstado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}




	public Result deleteCatEstado(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATESTADO);
			pstm.setBoolean(1, true);
			pstm.setLong(2, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatEstado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatEstado(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATESTADO);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setString(3, object.usuario_creacion);
			pstm.setLong(4, object.persistenceId);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatEstado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatEstado(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRCatEstado> data = new ArrayList<>();
		String where = "WHERE IS_ELIMINADO=false"; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "orden":
				        if (where.contains("WHERE")) {
				            where += " AND ";
				        } else {
				            where += " WHERE ";
				        }
				        where += " CAST(orden AS TEXT) ";
				        if (filtro.get("operador").equals("Igual a")) {
				            where += "= '" + filtro.get("valor") + "'";
				        } else {
				            where += " LIKE '%" + filtro.get("valor") + "%'";
				        }
				        break;
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "usuario_creacion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(usuario_creacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "fecha_creacion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(fecha_creacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ISELIMINADO":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(ELIMINADO) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATESTADO.replace("[WHERE]", where + " AND is_eliminado = false").replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				PSGRCatEstado row = new PSGRCatEstado();
				row.setPersistenceId(rs.getLong("persistenceid"));
				row.setOrden(rs.getLong("orden"));
				row.setClave(rs.getString("clave"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setUsuarioCreacion(rs.getString("usuario_creacion"));
				row.setFechaCreacion(rs.getString("fecha_creacion"));

				row.setIsEliminado(rs.getBoolean("is_eliminado"));
//				row.setPersistenceId(rs.getLong("persistenceId"));
//				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
	
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatEstado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatSexo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATSEXO);
			pstm.setBoolean(1, false); // is_eliminado
			pstm.setInt(2, 0);
			pstm.setString(3, object.clave);
			pstm.setString(4, object.descripcion);
			pstm.setString(5, object.usuario_creacion);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(6, fechaHoraFormateada);

			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatSexo] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}




	public Result deleteCatSexo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATSEXO);
			pstm.setLong(1, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatSexo] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatSexo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			} else if(object.usuario_creacion.equals("") || object.usuario_creacion == null) {
				throw new Exception("El campo \"usuario_creacion\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATSEXO);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setString(3, object.usuario_creacion);
			pstm.setLong(4, object.persistenceId);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatSexo] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatSexo(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRCatEstado> data = new ArrayList<>();
		String where = ""; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "usuarioCreacion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(usuario_creacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "fecha_creacion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(fechaCreacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATSEXO.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				PSGRCatEstado row = new PSGRCatEstado();
				row.setPersistenceId(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setUsuarioCreacion(rs.getString("usuario_creacion"));
				row.setFechaCreacion(rs.getString("fecha_creacion"));
				row.setIsEliminado(rs.getBoolean("is_eliminado"));
//				row.setPersistenceId(rs.getLong("persistenceId"));
//				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
	
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatSexo] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatCampus(String jsonData, RestAPIContext context) {
	    Result resultado = new Result();
	    Boolean closeCon = false;
	
	    try {
	        closeCon = validarConexion();
	        def jsonSlurper = new JsonSlurper()
			def jsonObject = jsonSlurper.parseText(jsonData)
			
			// Accede al primer elemento del arreglo lstCatCampusInput (suponiendo que haya solo uno)
			def object = jsonObject.lstCatCampusInput[0]
	        if (object == null) {
			    throw new Exception("El objeto 'object' no debe ser nulo.");
			} else if (object.id == null || object.id == "") {
					throw new Exception("El campo \"id\" no debe ir vacío.");
			} else if (object.clave == null || object.clave == "") {
					throw new Exception("El campo \"Clave\" no debe ir vacío.");
			} else if (object.orden == null || object.orden == "") {
					throw new Exception("El campo \"Orden\" no debe ir vacío.");
			} else if (object.descripcion == null || object.descripcion == "") {
					throw new Exception("El campo \"descripcion\" no debe ir vacío.");
			} else if (object.urlAvisoPrivacidad == null || object.urlAvisoPrivacidad == "") {
					throw new Exception("El campo \"urlAvisoPrivacidad\" no debe ir vacío.");
			} else if (object.calle == null || object.calle == "") {
					throw new Exception("El campo \"Calle\" no debe ir vacío.");
			} else if (object.numeroExterior == null || object.numeroExterior == "") {
					throw new Exception("El campo \"numeroExterior\" no debe ir vacío.");
			} else if (object.numeroInterior == null || object.numeroInterior == "") {
					throw new Exception("El campo \"numeroInterior\" no debe ir vacío.");
			} else if (object.codigoPostal == null || object.codigoPostal == "") {
					throw new Exception("El campo \"codigoPostal\" no debe ir vacío.");
			} else if (object.municipio == null || object.municipio == "") {
					throw new Exception("El campo \"municipio\" no debe ir vacío.");
			} else if (object.estado_pid == null || object.estado_pid == "") {
					throw new Exception("El campo \"estado_pid\" no debe ir vacío.");
			} else if (object.urlImagen == null || object.urlImagen == "") {
					throw new Exception("El campo \"urlImagen\" no debe ir vacío.");
			} else if (object.email == null || object.email == "") {
					throw new Exception("El campo \"email\" no debe ir vacío.");
			} else if (object.grupoBonita == null || object.grupoBonita == "") {
					throw new Exception("El campo \"grupoBonita\" no debe ir vacío.");
			} else if (object.orden == null || object.orden == "") {
					throw new Exception("El campo \"orden\" no debe ir vacío.");
			} else if (object.usuarioBanner == null || object.usuarioBanner == "") {
					throw new Exception("El campo \"usuarioBanner\" no debe ir vacío.");
			} else if (object.tienePosgrado == null || object.tienePosgrado == "") {
					throw new Exception("El campo \"tienePosgrado\" no debe ir vacío.");
			}
			
	        pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATCAMPUS);
	        pstm.setString(1, object.calle);
			pstm.setString(2, object.clave);
			pstm.setString(3, object.codigoPostal);
			pstm.setString(4, object.colonia);
			pstm.setString(5, object.descripcion);
			pstm.setString(6, object.email);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(7, fechaHoraFormateada);
			pstm.setString(8, fechaHoraFormateada);
			pstm.setString(9, object.grupoBonita);
			pstm.setString(10, object.municipio);
			pstm.setString(11, object.numeroExterior);
			pstm.setString(12, object.numeroInterior);
			String ordenStr = object.orden; // Obtén el valor como cadena
			int orden = 0; // Valor predeterminado en caso de error
			
			try {
			    orden = Integer.parseInt(ordenStr); // Intenta convertir la cadena a un entero
			} catch (NumberFormatException e) {
			}

			pstm.setInt(13, orden);
			pstm.setString(14, object.urlAvisoPrivacidad);
			pstm.setString(15, object.urlImagen);
			pstm.setString(16, object.usuarioBanner);
			pstm.setLong(17, Long.valueOf(object.estado_pid));
			pstm.setLong(18, 1);
			pstm.setString(19, object.id);
			pstm.setBoolean(20, true);
			pstm.setBoolean(21, false);
			pstm.setBoolean(22, object.tienePosgrado);

	        if (pstm.executeUpdate() > 0) {
	            resultado.setSuccess(true);
	        } else {
	            throw new Exception("No se pudo insertar el registro.");
	        }
	    } catch (Exception e) {
	        resultado.setSuccess(false);
	        resultado.setError("[insertCatCampus] " + e.getMessage());
	    } finally {
	        if (closeCon) {
	            new DBConnect().closeObj(con, stm, rs, pstm);
	        }
	    }
	
	    return resultado;
	}
	
	
	
	
	public Result deleteCatCampus(String jsonData, RestAPIContext context) {
	    Result resultado = new Result();
	    Boolean closeCon = false;
	
	    try {
	        closeCon = validarConexion();
	        def jsonSlurper = new JsonSlurper();
	        def object = jsonSlurper.parseText(jsonData)
	        
	        if(object.persistenceid.equals("") || object.persistenceid == null) {
	            throw new Exception("El campo \"persistenceid\" no debe ir vacío" + object);
	        }
	
	        pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATCAMPUS);
			pstm.setBoolean(1, true);
	        pstm.setLong(2, object.persistenceid);
	
	        if (pstm.executeUpdate() > 0) {
	            resultado.setSuccess(true);
	        } else {
	            throw new Exception("No se pudo eliminar el registro.");
	        }
	    } catch (Exception e) {
	        resultado.setSuccess(false);
	        resultado.setError("[deleteCatCampus] " + e.getMessage())
	    } finally {
	        if (closeCon) {
	            new DBConnect().closeObj(con, stm, rs, pstm);
	        }
	    }
	
	    return resultado;
	}
	
	public Result updateCatCampus(String jsonData, RestAPIContext context) {
	    Result resultado = new Result();
	    Boolean closeCon = false;
	
	    try {
	        closeCon = validarConexion();
	        def jsonSlurper = new JsonSlurper()
			def jsonObject = jsonSlurper.parseText(jsonData)
			
			// Accede al primer elemento del arreglo lstCatCampusInput (suponiendo que haya solo uno)
			def object = jsonObject.lstCatCampusInput[0]

			if (object == null) {
			    throw new Exception("El objeto 'object' no debe ser nulo.");
			} else if (object.id == null || object.id == "") {
					throw new Exception("El campo \"id\" no debe ir vacío.");
			} else if (object.clave == null || object.clave == "") {
					throw new Exception("El campo \"Clave\" no debe ir vacío.");
			} else if (object.orden == null || object.orden == "") {
					throw new Exception("El campo \"Orden\" no debe ir vacío.");
			} else if (object.descripcion == null || object.descripcion == "") {
					throw new Exception("El campo \"descripcion\" no debe ir vacío.");
			} else if (object.urlAvisoPrivacidad == null || object.urlAvisoPrivacidad == "") {
					throw new Exception("El campo \"urlAvisoPrivacidad\" no debe ir vacío.");
			} else if (object.calle == null || object.calle == "") {
					throw new Exception("El campo \"Calle\" no debe ir vacío.");
			} else if (object.numeroExterior == null || object.numeroExterior == "") {
					throw new Exception("El campo \"numeroExterior\" no debe ir vacío.");
			} else if (object.numeroInterior == null || object.numeroInterior == "") {
					throw new Exception("El campo \"numeroInterior\" no debe ir vacío.");
			} else if (object.codigoPostal == null || object.codigoPostal == "") {
					throw new Exception("El campo \"codigoPostal\" no debe ir vacío.");
			} else if (object.municipio == null || object.municipio == "") {
					throw new Exception("El campo \"municipio\" no debe ir vacío.");
			} else if (object.estado_pid == null || object.estado_pid == "") {
					throw new Exception("El campo \"estado_pid\" no debe ir vacío.");
			} else if (object.urlImagen == null || object.urlImagen == "") {
					throw new Exception("El campo \"urlImagen\" no debe ir vacío.");
			} else if (object.email == null || object.email == "") {
					throw new Exception("El campo \"email\" no debe ir vacío.");
			} else if (object.grupoBonita == null || object.grupoBonita == "") {
					throw new Exception("El campo \"grupoBonita\" no debe ir vacío.");
			} else if (object.orden == null || object.orden == "") {
					throw new Exception("El campo \"orden\" no debe ir vacío.");
			} else if (object.usuarioBanner == null || object.usuarioBanner == "") {
					throw new Exception("El campo \"usuarioBanner\" no debe ir vacío.");
			} else if (object.tienePosgrado == null || object.tienePosgrado == "") {
					throw new Exception("El campo \"tienePosgrado\" no debe ir vacío.");
			}
	
	        pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATCAMPUS);
	        pstm.setString(1, object.calle.isEmpty() ? null : object.calle);
			pstm.setString(2, object.clave.isEmpty() ? null : object.clave);
			pstm.setString(3, object.codigoPostal);
			pstm.setString(4, object.colonia.isEmpty() ? null : object.colonia);
			pstm.setString(5, object.descripcion.isEmpty() ? null : object.descripcion);
			pstm.setString(6, object.email.isEmpty() ? null : object.email);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(7, fechaHoraFormateada);
			pstm.setString(8, fechaHoraFormateada);
			pstm.setString(9, object.grupoBonita.isEmpty() ? null : object.grupoBonita);
			pstm.setString(10, object.municipio.isEmpty() ? null : object.municipio);
			pstm.setString(11, object.numeroExterior.isEmpty() ? null : object.numeroExterior);
			pstm.setString(12, object.numeroInterior.isEmpty() ? null : object.numeroInterior);
			pstm.setLong(13, Long.valueOf(object.orden));
			pstm.setString(14, object.urlAvisoPrivacidad.isEmpty() ? null : object.urlAvisoPrivacidad);
			pstm.setString(15, object.urlImagen.isEmpty() ? null : object.urlImagen);
			pstm.setString(16, object.usuarioBanner);
			pstm.setLong(17, object.estado_pid);
			pstm.setLong(18, object.pais_pid);
			pstm.setString(19, object.id); // ID es de tipo VARCHAR_IGNORECASE, por lo que puedes usar setString
			pstm.setBoolean(20, object.tienePosgrado);
			pstm.setLong(21, object.persistenceId);
	
	        if (pstm.executeUpdate() > 0) {
	            resultado.setSuccess(true);
	        } else {
	            throw new Exception("No se pudo modificar el registro.")
	        }
	    } catch (Exception e) {
	        resultado.setSuccess(false);
	        resultado.setError("[updateCatCampus] " + e.getMessage())
	    } finally {
	        if (closeCon) {
	            new DBConnect().closeObj(con, stm, rs, pstm);
	        }
	    }
	
	    return resultado;
	}
	
	public Result getCatCampus(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "WHERE c.ELIMINADO=false", orderby = "ORDER BY ", errorLog = "";
        String consulta = ("SELECT  c.*, p.descripcion as pais, e.clave as cEstado, e.descripcion as dEstado FROM PSGRCATCAMPUS c left join PSGRCATPAIS p ON c.PAISES_RELACION_PID  = p.PERSISTENCEID  left join PSGRCATESTADOS e ON  e.PERSISTENCEID  = c.ESTADOS_RELACION_PID  [WHERE] [ORDERBY] [LIMITOFFSET]")
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);


            CatCampusCustomFiltro row = new CatCampusCustomFiltro();
            List < CatCampusCustomFiltro > rows = new ArrayList < CatCampusCustomFiltro > ();
            closeCon = validarConexion();
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                switch (filtro.get("columna")) {
                    case "CLAVE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(clave) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "DESCRIPCIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "URLIMAGEN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(URL_IMAGEN) ";
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
                        where += " LOWER(EMAIL) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA CREACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHA_CREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA IMPORTACIÓN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(FECHA_IMPLEMENTACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "GRUPOBONITA":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(GRUPO_BONITA) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ISELIMINADO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ELIMINADO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ISENABLED":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ACTIVADO) ";
                        if (filtro.get("ISENABLED").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ORDEN":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ORDEN) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEID":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PERSISTENCEVERSION":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PERSISTENCEVERSION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "URLAUTORDATOS":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(URL_AUTOR_DATOS) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "URLAVISOPRIVACIDAD":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(URL_AVISO_PRIVACIDAD) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "URLDATOSVERIDICOS":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(URL_DATOS_VERIDICOS) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIO BANNER":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(USUARIO_BANNER) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CALLE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CALLE) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "COLONIA":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(COLONIA) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "NÚMERO INTERIOR":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(NUMERO_INT) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "NÚMERO EXTERIOR":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(NUMERO_EXT) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CÓDIGO POSTAL":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(CODIGO_POSTAL) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "MUNICIPIO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(MUNICIPIO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PAÍS":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(PAISES_RELACION_PID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ESTADO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(ESTADOS_RELACION_PID) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                }
            }
            switch (object.orderby) {
                case "CLAVE":
                    orderby += "clave";
                    break;
                case "DESCRIPCION":
                    orderby += "descripcion";
                    break;
                case "FECHA CREACIÓN":
                    orderby += "fecha_creacion";
                    break;
                case "FECHA IMPORTACIÓN":
                    orderby += "fecha_implementacion";
                    break;
                case "GRUPOBONITA":
                    orderby += "grupo_bonita";
                    break;
                case "ID":
                    orderby += "id";
                    break;
                case "ISELIMINADO":
                    orderby += "eliminado";
                    break;
                case "ISENABLED":
                    orderby += "activado";
                    break;
                case "ORDEN":
                    orderby += "orden";
                    break;
                case "PERSISTENCEID":
                    orderby += "persistenceId";
                    break;
                case "PERSISTENCEVERSION":
                    orderby += "persistenceVersion";
                    break;
                case "URLAUTORDATOS":
                    orderby += "url_autor_datos";
                    break;
                case "URLAVISOPRIVACIDAD":
                    orderby += "url_aviso_privacidad";
                    break;
                case "URLDATOSVERIDICOS":
                    orderby += "url_datos_veridicos";
                    break;
                case "USUARIO BANNER":
                    orderby += "usuario_banner";
                    break;
                case "ORDEN":
                    orderby += "ORDEN";
                    break;
                case "CALLE":
                    orderby += "CALLE";
                    break;
                case "COLONIA":
                    orderby += "COLONIA";
                    break;
                case "NUMEROINTERIOR":
                    orderby += "NUMERO_INT";
                    break;
                case "NUMEROEXTERIOR":
                    orderby += "NUMERO_EXT";
                    break;
                case "CODIGOPOSTAL":
                    orderby += "CODIGO_POSTAL";
                    break;
                case "MUNICIPIO":
                    orderby += "MUNICIPIO";
                    break;
                case "URLIMAGEN":
                    orderby += "URL_IMAGEN";
                    break;
                case "EMAIL":
                    orderby += "EMAIL";
                    break;
                case "PAIS":
                    orderby += "PAISES_RELACION_PID";
                    break;
                case "ESTADO":
                    orderby += "ESTADOS_RELACION_PID";
                    break;
                default:
                    orderby += "ORDEN"
                    break;
            }
            errorLog += ""
            orderby += " " + object.orientation;
            consulta = consulta.replace("[WHERE]", where);
            pstm = con.prepareStatement(consulta.replace("c.*, p.descripcion as pais, e.clave as cEstado, e.descripcion as dEstado", "COUNT(c.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")

            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            rs = pstm.executeQuery()
            errorLog += " " + consulta
            while (rs.next()) {
                row = new CatCampusCustomFiltro();
                row.setClave(rs.getString("clave"))
                row.setDescripcion(rs.getString("descripcion"));
				row.setTienePosgrado(rs.getBoolean("tiene_posgrado"));
                //row.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechaCreacion")))
                row.setFechaCreacion(rs.getString("fecha_creacion"));
                //row.setFechaImplementacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechaImplementacion")))
                row.setFechaImplementacion(rs.getString("fecha_implementacion"));
                row.setGrupoBonita(rs.getString("grupo_bonita"));
                row.setId(rs.getString("id"));
                row.setIsEliminado(rs.getBoolean("eliminado"));
                row.setIsEnabled(rs.getBoolean("activado"));
                row.setOrden(rs.getLong("orden"));
                row.setPersistenceId(rs.getLong("persistenceId"));
                row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                row.setUrlAutorDatos(rs.getString("url_autor_datos"));
                row.setUrlAvisoPrivacidad(rs.getString("url_aviso_privacidad"));
                row.setUrlDatosVeridicos(rs.getString("url_datos_veridicos"));
                row.setUsuarioBanner(rs.getString("usuario_banner"));
                row.setCalle(rs.getString("calle"));
                row.setColonia(rs.getString("colonia"));
                row.setNumeroExterior(rs.getString("numero_ext"));
                row.setNumeroInterior(rs.getString("numero_int"));
                row.setCodigoPostal(rs.getString("codigo_postal"));
                row.setMunicipio(rs.getString("municipio"));
                row.setUrlImagen(rs.getString("url_imagen"));
                row.setEmail(rs.getString("email"))
                row.setPaisesRelacionPid(rs.getString("paises_relacion_pid"))
                row.setEstadosRelacionPid(rs.getString("estados_relacion_pid"))
                errorLog += "pais"
                try {
                    row.setPais(new CatPaisCustomFiltro())
                    row.getPais().setDescripcion(rs.getString("pais"))
                    row.getPais().setPersistenceId(rs.getLong("PAIS_PID"))
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorLog += e.getMessage()
                }
                try {
                    row.setEstados(new PSGRCatEstado())
                    row.getEstados().setDescripcion(rs.getString("destado"))
                    row.getEstados().setClave(rs.getString("cestado"))
                    row.getEstados().setPersistenceId(rs.getLong("estado_pid"))
                    //row.getEstado().setPais(rs.getString("pEstado"))
                    //row.getEstado().setCaseId(rs.getString("ciestado"))
                    //row.getEstado().setIsEliminado(rs.getBoolean("eestado"))
                    //row.getEstado().setOrden(rs.getInt("oestado"))
                    //row.getEstado().setPersistenceVersion(rs.getLong("pvestado"))
                    //row.getEstado().setPersistenceVersion_string(rs.getString("pvestado"))
                    //row.getEstado().setUsuarioCreacion(rs.getString("ucestado"))
                    //row.getEstado().setPersistenceId_string(rs.getString("piestado"))
                    //row.getEstado().setFechaCreacion(rs.getString("fcEstado"));
                } catch (Exception e) {
                	LOGGER.error "[ERROR] " + e.getMessage();
                    errorLog += e.getMessage()
                }
                rows.add(row)
            }
			errorLog += (" fecha==" + rows)
            resultado.setSuccess(true)

            resultado.setData(rows)
            
        } catch (Exception e) {
        	LOGGER.error "[ERROR] " + e.getMessage();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            resultado.setError_info(consulta)
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }
	
	/**
	 * @param jsonData
	 * @param context
	 * @return
	 */
	public Result insertCatGestionEscolar(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def dataResultAgregados = [];
			def jsonSlurper = new JsonSlurper()
			def jsonObject = jsonSlurper.parseText(jsonData)
			
			if (!jsonObject.lstCatGestionEscolarInput || !(jsonObject.lstCatGestionEscolarInput instanceof ArrayList)) {
				throw new Exception("El objeto 'lstCatGestionEscolarInput' no debe ser nulo y debe ser una lista.");
			}
			
			con.setAutoCommit(false);
			
			jsonObject.lstCatGestionEscolarInput.each { object ->
				if (object == null) {
					throw new Exception("El objeto 'object' no debe ser nulo.");
				} else if (object.CAMPUS == null || object.CAMPUS.isEmpty()) {
					throw new Exception("El campo \"CAMPUS\" no debe ir vacío.");
				} else if (object.grado == null) {
					throw new Exception("El campo \"grado\" no debe ir vacío.");
				} else if (object.orden == null) {
					throw new Exception("El campo \"orden\" no debe ir vacío.");
				} else if (object.clave == null || object.clave.isEmpty()) {
					throw new Exception("El campo \"Clave\" no debe ir vacío.");
				} else if (object.nombre == null || object.nombre.isEmpty()) {
					throw new Exception("El campo \"nombre\" no debe ir vacío.");
				}/* else if (object.descripcion == null || object.descripcion.isEmpty()) {
					throw new Exception("El campo \"descripcion\" no debe ir vacío.");
				}*/ else if (object.enlace == null || object.enlace.isEmpty()) {
					throw new Exception("El campo \"enlace\" no debe ir vacío.");
				} else if (object.tipoCentroEstudio == null || object.tipoCentroEstudio.isEmpty()) {
					throw new Exception("El campo \"tipoCentroEstudio\" no debe ir vacío.");
				} else if (object.tipoLicenciatura == null || object.tipoLicenciatura.isEmpty()) {
					throw new Exception("El campo \"tipoLicenciatura\" no debe ir vacío.");
				}
				
	//			else if (object.periodo == null) {
	//				throw new Exception("El campo \"periodo\" no debe ir vacío.");
	//			}
				
//				if (!object.periodoDisponible || !object.periodoDisponible instanceof ArrayList) {
//					throw new Exception("Debes seleccionar al menos un período.");
//				}
				
				else if (object.idioma == null || object.idioma.isEmpty()) {
					throw new Exception("El campo \"idioma\" no debe ir vacío.");
				} else if (object.usuarioCreacion == null || object.usuarioCreacion.isEmpty()) {
					throw new Exception("El campo \"usuarioCreacion\" no debe ir vacío.");
				}
				
				// Consultar no duplicados
				pstm = con.prepareStatement(StatementsCatalogos.GET_SIMPLE_CATGESTIONESCOLAR);
				pstm.setString(1, object.nombre);
				pstm.setLong(2, object.CAMPUS.persistenceId);
				pstm.setString(3, object.clave);
				pstm.setLong(4, object.CAMPUS.persistenceId);
				
				rs = pstm.executeQuery();
				
				if (rs.next()) {
					throw new Exception('El programa con nombre "' + object.nombre + '" y campus referencia "' + object.CAMPUS.persistenceId + '" ya existe, o la clave "' + object.clave + '" esta duplicada en el mismo campus.' );
				}
				
				// Obtener el posgrado id
				Long posgrado_pid = 0L;
				if (object.grado.persistenceId) {
					posgrado_pid = object.grado.persistenceId;
				}
				else if (object.grado.clave) {
					pstm = con.prepareStatement(StatementsCatalogos.GET_POSGRADO_BY_CLAVE);
					pstm.setLong(1, object.CAMPUS.persistenceId);
					pstm.setString(2, object.grado.clave);
					
					rs = pstm.executeQuery();
					
					if (rs.next()) {
						posgrado_pid = rs.getLong("persistenceid")
					}
					else {
						throw new Exception('No se encontró ningun posgrado con la clave: ' + object.grado.clave);
					}
				}
				else {
					throw new Exception('Se debe especificar el "persistenceId" o la "clave" en el objeto grado.');
				}
				
				// Obtener los periodos id
				if (object.periodoDisponible && object.periodoDisponible instanceof ArrayList) {
					object.periodoDisponible.each { item ->
						Long periodo_pid = 0L;
						// Si se mando en el request
						if (item.persistenceId) {
							
						}
						// Si se mando la clave
						else if (item.clave) {
							pstm = con.prepareStatement(StatementsCatalogos.GET_PERIODO_BY_CLAVE);
							pstm.setString(1, item.clave);
							
							rs = pstm.executeQuery();
							
							if (rs.next()) {
								periodo_pid = rs.getLong("persistenceid")
							}
							else {
								throw new Exception('No se encontró ningun periodo con la clave: ' + item.clave + ', en el programa ' + object.nombre);
							}
							item.persistenceId = periodo_pid
						}
						// Si no se mando nada
						else {
							throw new Exception('Se debe especificar el "persistenceId" o la "clave" para todos los elementos del objeto "periodoDisponible".');
						}
					}
				}
				
				// Insertar
				pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATGESTIONESCOLAR);
				Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
				String fechaHoraFormateada = formato.format(timestampActual);
				pstm.setString(1, fechaHoraFormateada);
				pstm.setBoolean(2, false); // IS_ELIMINADO
				pstm.setString(3, object.CAMPUS.descripcion); // CAMPUS
	//			pstm.setInt(4, 0); // PROPEDEUTICOS
				pstm.setString(4, object.clave); // Clave
				pstm.setString(5, object.nombre); // NOMBRE
				pstm.setString(6, object.descripcion); // DESCRIPCION
				pstm.setString(7, object.enlace); // ENLACE
				pstm.setString(8, object.tipoCentroEstudio); // TIPO_CENTRO_ESTUDIO
	//			Boolean propedeuticoValue = (object.propedeutico != null) ? object.propedeutico : false;
	//			pstm.setBoolean(9, propedeuticoValue); // PROPEDEUTICO
	//			Boolean programaParcialValue = (object.programaparcial != null) ? object.programaparcial : false;
	//			pstm.setBoolean(10, programaParcialValue); // PROGRAMA_PARCIAL
	//			Boolean isMedicina = object.isMedicina != null ? object.isMedicina : false;
	//			pstm.setBoolean(11, isMedicina); // IS_MEDICINA
				pstm.setBoolean(9, false); // PROPEDEUTICO siempre se guarda como false
				pstm.setBoolean(10, false); // PROGRAMA_PARCIAL siempre se guarda como false
				pstm.setBoolean(11, false); // IS_MEDICINA siempre se guarda como false
				pstm.setString(12, object.tipoLicenciatura); // TIPO_LICENCIATURA
	//			pstm.setString(13, object.inscripcionenero); // INSCRIPCION_ENERO
	//			pstm.setString(14, object.inscripcionMayo); // INSCRIPCION_MAYO
	//			pstm.setString(15, object.inscripcionagosto); // INSCRIPCION_AGOSTO
	//			pstm.setString(16, object.inscripcionSeptiembre); // INSCRIPCION_SEPTIEMBRE
	//			pstm.setString(17, object.urlImgLicenciatura); // URL_IMG_LICENCIATURA
				pstm.setString(13, ""); // INSCRIPCION_ENERO
				pstm.setString(14, ""); // INSCRIPCION_MAYO
				pstm.setString(15, ""); // INSCRIPCION_AGOSTO
				pstm.setString(16, ""); // INSCRIPCION_SEPTIEMBRE
				pstm.setString(17, ""); // URL_IMG_LICENCIATURA
				pstm.setString(18, object.idioma); // IDIOMA
				pstm.setString(19, object.usuarioCreacion); // UUSUARIOCREACION
				pstm.setLong(20, object.CAMPUS.persistenceId);
				pstm.setLong(21, object.orden);
				pstm.setNull(22, Types.BIGINT);
				pstm.setLong(23, posgrado_pid);

				Long idResult = 0L;
				rs = pstm.executeQuery();
				
				if (rs.next()) {
					idResult = rs.getLong("persistenceid");
					dataResultAgregados.add(idResult);
				} else {
					throw new Exception("No se pudo insertar el registro con nombre: " + object.nombre);
				}
				
				if (object.periodoDisponible && object.periodoDisponible instanceof ArrayList) {
					// Agregar las relaciones necesarias para actualiar el nuevo estado de la lista
					String valuesToInsert = ""
					object.periodoDisponible.eachWithIndex { periodo, index ->
						if (index != 0) {
							valuesToInsert += ", "
						}
						
						int orderValue = index + 1;
						valuesToInsert += "(" + idResult + ", " + periodo.persistenceId + ", " + orderValue + ")"
					}
					
					pstm = con.prepareStatement(StatementsCatalogos.INSERT_LST_PERIODOS_DISPONIBLES.replace("[VALUES]", valuesToInsert));
					pstm.execute();
				}
			}
			
			con.commit();
			resultado.setData(dataResultAgregados);
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatGestionEscolar] " + e.getMessage());
			if (!con.autoCommit) con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result deleteCatGestionEscolar(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío" + object);
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATGESTIONESCOLAR);
			pstm.setBoolean(1, true);
			pstm.setLong(2, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatGestionEscolar] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result updateCatGestionEscolar(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			def jsonSlurper = new JsonSlurper()
			def jsonObject = jsonSlurper.parseText(jsonData)
			
			// Accede al primer elemento del arreglo lstCatCampusInput (suponiendo que haya solo uno)
			def object = jsonObject.lstCatGestionEscolarInput[0]

			if(object.equals("") || object == null) {
				throw new Exception("El campo \"object\" no debe ir vacío" + object);
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"clave\" no debe ir vacío");
			} else if(object.nombre.equals("") || object.nombre == null) {
				throw new Exception("El campo \"nombre\" no debe ir vacío");
			} /*else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			}*/ else if(object.enlace.equals("") || object.enlace == null) {
				throw new Exception("El campo \"enlace\" no debe ir vacío");
			} else if(object.tipoCentroEstudio.equals("") || object.tipo_centro_estudio == null) {
				throw new Exception("El campo \"tipoCentroEstudio\" no debe ir vacío");
			} else if(object.tipoLicenciatura.equals("") || object.tipo_licenciatura == null) {
				throw new Exception("El campo \"tipoLicenciatura\" no debe ir vacío");
			} else if(object.inscripcionenero.equals("") || object.inscripcion_enero == null) {
				throw new Exception("El campo \"inscripcionenero\" no debe ir vacío");
			} else if(object.inscripcionMayo.equals("") || object.inscripcion_mayo == null) {
				throw new Exception("El campo \"inscripcionMayo\" no debe ir vacío");
			} else if(object.inscripcionagosto.equals("") || object.inscripcion_agosto == null) {
				throw new Exception("El campo \"inscripcionagosto\" no debe ir vacío");
			} else if(object.inscripcionSeptiembre.equals("") || object.inscripcion_septiembre == null) {
				throw new Exception("El campo \"inscripcionSeptiembre\" no debe ir vacío");
			} else if(object.urlImgLicenciatura.equals("") || object.url_img_licenciatura == null) {
				throw new Exception("El campo \"urlImgLicenciatura\" no debe ir vacío");
			} else if(object.idioma.equals("") || object.idioma == null) {
				throw new Exception("El campo \"idioma\" no debe ir vacío");
			} else if(object.usuarioCreacion.equals("") || object.usuario_creacion == null) {
				throw new Exception("El campo \"usuarioCreacion\" no debe ir vacío");
			} else if(object.persistenceId.equals("") || object.persistenceId == null) {
				throw new Exception("El campo \"persistenceId\" no debe ir vacío");
			}
	
			// Actualizar datos del registro Gestion escolar.
			
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATGESTIONESCOLAR);
//			pstm.setString(1, object.CAMPUS); // CAMPUS
//			pstm.setInt(4, 0); // PROPEDEUTICOS
			pstm.setString(1, object.clave); // Clave
			pstm.setString(2, object.nombre); // NOMBRE
			pstm.setString(3, object.descripcion); // DESCRIPCION
			pstm.setString(4, object.enlace); // ENLACE
			pstm.setString(5, object.tipo_centro_estudio); // TIPO_CENTRO_ESTUDIO
			Boolean propedeuticoValue = (object.propedeutico != null) ? object.propedeutico : false;
			pstm.setBoolean(6, propedeuticoValue); // PROPEDEUTICO
			Boolean programaParcialValue = (object.programa_parcial != null) ? object.programa_parcial : false;
			pstm.setBoolean(7, programaParcialValue); // PROGRAMA_PARCIAL
			Boolean isMedicina = object.is_medicina != null ? object.is_medicina : false;
			pstm.setBoolean(8, isMedicina); // IS_MEDICINA
			pstm.setString(9, object.tipo_licenciatura); // TIPO_LICENCIATURA
			pstm.setString(10, object.inscripcion_enero); // INSCRIPCION_ENERO
			pstm.setString(11, object.inscripcion_mayo); // INSCRIPCION_MAYO
			pstm.setString(12, object.inscripcion_agosto); // INSCRIPCION_AGOSTO
			pstm.setString(13, object.inscripcion_septiembre); // INSCRIPCION_SEPTIEMBRE
			pstm.setString(14, object.url_img_licenciatura); // URL_IMG_LICENCIATURA
			pstm.setString(15, object.idioma); // IDIOMA
			pstm.setString(16, object.usuario_creacion); // UUSUARIOCREACION
			pstm.setLong(17, object.persistenceId);
	
			if (pstm.executeUpdate() > 0) {
				
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
			
			// Actualizar la lista de periodos disponibles:
			
			if (object.periodo_disponible instanceof ArrayList) {
				
				// Conversión
				Long programa_pid = 0L;
				try {
					programa_pid = Long.valueOf(object.persistenceId);
				}
				catch (Exception e) {
					throw new Exception("Falló al tratar de convertir el persistenceId a Long. " + e.message);
				}
				
				if (programa_pid != 0) {
					// Eliminar las relaciones existentes para el programa especificado
					pstm = con.prepareStatement(StatementsCatalogos.DELETE_LST_PERIODOS_DISPONIBLES);
					pstm.setLong(1, programa_pid);
					
					pstm.executeUpdate();
					
					// Agregar las relaciones necesarias para actualiar el nuevo estado de la lista
					if (!object.periodo_disponible.isEmpty()) {
						String valuesToInsert = ""
						object.periodo_disponible.eachWithIndex { periodo, index ->
							if (index != 0) {
								valuesToInsert += ", "
							}
							
							int orderValue = index + 1;
							valuesToInsert += "(" + programa_pid + ", " + periodo.persistenceId + ", " + orderValue + ")"
						}
						
						pstm = con.prepareStatement(StatementsCatalogos.INSERT_LST_PERIODOS_DISPONIBLES.replace("[VALUES]", valuesToInsert));
						pstm.execute();
					}
				}
			}
			
			con.commit();
			
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatGestionEscolar] " + e.getMessage());
			if (!con.autoCommit) con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatGestionEscolar(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", orderby = "ORDER BY ", errorLog = "", bachillerato = "", campus = ""

		List < String > lstGrupo = new ArrayList < String > ();
		List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();

		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map < String, String > objGrupoCampus = new HashMap < String, String > ();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			def objCatCampusDAO = context.apiClient.getDAO(PSGRCatCampusDAO.class);
			List < PSGRCatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)

			userLogged = context.getApiSession().getUserId();

			List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for (UserMembership objUserMembership: lstUserMembership) {
				for (PSGRCatCampus rowGrupo: lstCatCampus) {
					if (objUserMembership.getGroupName().equals(rowGrupo.getGrupo_bonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}

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

			String consulta = StatementsCatalogos.GET_CATGESTIONESCOLAR
			CatGestionEscolar row = new CatGestionEscolar();
			List < CatGestionEscolar > rows = new ArrayList < CatGestionEscolar > ();
			closeCon = validarConexion();

			where = "WHERE GE.is_eliminado = false AND campus.eliminado = false AND GE.campus = '" + object.campus + "'" + " AND GE.posgrado_pid = " + object.posgrado_pid
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				def booleanos = filtro.get("valor");
				switch (filtro.get("columna")) {
					case "NOMBRE LICENCIATURA":
						where += " AND LOWER(GE.nombre) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "CLAVE":
						where += " AND LOWER(GE.CLAVE) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "LIGA":
						where += " AND LOWER(GE.enlace) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "DESCRIPCION DE CARRERA":
						where += " AND LOWER(GE.descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					case "INSCRIPCIÓN ENERO":
						where += " AND LOWER(GE.inscripcion_enero) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "INSCRIPCIÓN AGOSTO":
						where += " AND LOWER(GE.inscripcion_agosto) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "PERSISTENCEVERSION":
						where += " AND LOWER(GE.PERSISTENCEVERSION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "TIPO LICENCIATURA":
						where += " AND LOWER(GE.TIPO_LICENCIATURA) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					case "INSCRIPCIÓN MAYO":
						where += " AND LOWER(GE.inscripcion_mayo) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					case "INSCRIPCIÓN SEPTIEMBRE":
						where += " AND LOWER(GE.inscripcion_septiembre) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					case "PROPEDÉUTICO":
						where += " AND GE.propedeutico ";
						where += " = [valor]"
						errorLog += " Que valor tienen: " + booleanos.toString().equals("Si") + " "
						where = where.replace("[valor]", (booleanos.toString().equals("Si") ? "true" : booleanos.toString().equals("Sí") ? "true" : "false"))
						break;


					case "PROGRAMA PARCIAL":
						where += " AND GE.programa_parcial ";
						where += " = [valor]"
						errorLog += " Que valor tienen: " + booleanos.toString().equals("Si") + " "
						where = where.replace("[valor]", (booleanos.toString().equals("Si") ? "true" : booleanos.toString().equals("Sí") ? "true" : "false"))
						break;
						/* case "CAMPUS":
							 campus +=" AND LOWER(campus.DESCRIPCION) ";
							 if(filtro.get("operador").equals("Igual a")) {
								 campus+="=LOWER('[valor]')"
							 }else {
								 campus+="LIKE LOWER('%[valor]%')"
							 }
							 campus = campus.replace("[valor]", filtro.get("valor"))
						break;*/
				}
			}
			switch (object.orderby) {
				case "NOMBRE LICENCIATURA":
					orderby += "GE.nombre";
					break;
				case "LIGA":
					orderby += "GE.enlace";
					break;
				case "CLAVE":
					orderby += "GE.clave";
					break;
				case "DESCRIPCION DE CARRERA":
					orderby += "GE.descripcion";
					break;
				case "INSCRIPCIÓN ENERO":
					orderby += "GE.inscripcion_enero::Integer";
					break;
				case "INSCRIPCIÓN AGOSTO":
					orderby += "GE.inscripcion_agosto::Integer";
					break;
				case "INSCRIPCIÓN MAYO":
					orderby += "GE.inscripcion_mayo::Integer";
					break;
				case "INSCRIPCIÓN SEPTIEMBRE":
					orderby += "GE.inscripcion_septiembre::Integer";
					break;
				case "PERSISTENCEVERSION":
					orderby += "GE.PERSISTENCEVERSION";
					break;
				case "CAMPUS":
					orderby += "campus.descripcion";
					break;
				case "TIPO LICENCIATURA":
					orderby += "GE.tipo_licenciatura";
					break;
				case "PROPEDÉUTICO":
					orderby += "GE.propedeutico";
					break;
				case "PROGRAMA PARCIAL":
					orderby += "GE.programa_parcial";
					break;
				default:
					orderby += "GE.nombre"
					break;
			}

			orderby += " " + object.orientation;
			//where+=" "+campus
			consulta = consulta.replace("[CAMPUS]", campus)
			consulta = consulta.replace("[WHERE]", where);

//			pstm = con.prepareStatement(consulta.replace("GE.*, campus.descripcion as nombreCampus", "COUNT(GE.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
//			rs = pstm.executeQuery()
//			if (rs.next()) {
//				resultado.setTotalRegistros(rs.getInt("registros"))
//			}
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorLog += " " + consulta
//			errorLog += " consulta= "
//			errorLog += consulta
//			errorLog += " where = " + where
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)

//			errorLog += "fecha=="

			rs = pstm.executeQuery()
			while (rs.next()) {

				row = new CatGestionEscolar()
				row.setCampus(rs.getString("campus"))
				//row.setCaseId(rs.getString("caseId"))
				row.setDescripcion(rs.getString("descripcion"))
				row.setEnlace(rs.getString("enlace"))
//				try {
//					row.setFecha_creacion(new java.util.Date(rs.getDate("fechaCreacion").getTime()))
//				} catch (Exception e) {
//					LOGGER.error "[ERROR] " + e.getMessage();
//					errorLog += ", " + e.getMessage()
//				}
				row.setClave(rs.getString("clave"));
				row.setInscripcion_agosto(rs.getString("inscripcion_agosto"))
				row.setInscripcion_enero(rs.getString("inscripcion_enero"))
				row.setIs_eliminado(rs.getBoolean("is_eliminado"))
				//row.setMatematicas(rs.getBoolean("matematicas"))
				row.setNombre(rs.getString("nombre"))
				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setCampus_referencia_pid(rs.getLong("campus_referencia_pid"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				row.setPrograma_parcial(rs.getBoolean("programa_parcial"))
				row.setPropedeutico(rs.getBoolean("propedeutico"))
				row.setUsuario_creacion(rs.getString("usuario_creacion"))
				row.setTipo_licenciatura(rs.getString("tipo_licenciatura"))
				row.setTipo_centro_estudio(rs.getString("tipo_centro_estudio"))
				row.setInscripcion_mayo(rs.getString("inscripcion_mayo"))
				row.setInscripcion_septiembre(rs.getString("inscripcion_septiembre"))
				row.setUrl_img_licenciatura(rs.getString("url_img_licenciatura"))
				row.setFecha_creacion(rs.getString("fecha_creacion"))
				row.setUsuario_creacion(rs.getString("usuario_creacion"))
				row.setIs_medicina(rs.getBoolean("is_medicina"))
				row.setIdioma(rs.getString("idioma"))
				row.setOrden(rs.getLong("orden"))
				row.setPeriodo_pid(rs.getLong("periodo_pid"))
				row.setPosgrado_pid(rs.getLong("posgrado_pid"))
				row.setPeriodo(rs.getString("periodo"))
				row.setPosgrado(rs.getString("posgrado"))

				rows.add(row)
			}
			
			// Agregando los periodos disponibles a la data respuesta
			rows.each { programa ->
				pstm = con.prepareStatement(StatementsCatalogos.GET_LST_PERIODOS_DISPONIBLES);
				pstm.setLong(1, programa.getPersistenceId());
	
				rs = pstm.executeQuery();
				String periodos_disponible_value = ""
				boolean first = true;
				
				while (rs.next()) {
					if (first) {
						periodos_disponible_value += rs.getString("descripcion");
						first = false;
					}
					else {
						periodos_disponible_value += ", " + rs.getString("descripcion");
					}
				}
				
				programa.setPeriodos_disponible(periodos_disponible_value);
			}

			resultado.setSuccess(true)
			resultado.setError(errorLog)
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
	
	public Result getLstPeriodosDisponibles(String programa_pid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Map<String, Object> row = new HashMap<String, Object>();
		List < Map<String, Object> > rows = new ArrayList < Map<String, Object> > ();
		
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			// ---Validación---
			if (programa_pid == null) {
				throw new Exception('El parametro "programa_pid" es requerido');
			}
			
			// ---Conversión---
			Long programaId = -1L;
			
			try {
				programaId = Long.valueOf(programa_pid);
			}
			catch (Exception e) {
				throw new Exception('Falló al tratar de convertir el parametro "programa_pid" a Long. ' + e.message);
			}
			
			// ---Ejecución---
			pstm = con.prepareStatement(StatementsCatalogos.GET_LST_PERIODOS_DISPONIBLES);
			pstm.setLong(1, programaId);

			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new HashMap<String, Object>();
				row.put("persistenceId", rs.getLong("persistenceid"));
				row.put("persistenceId_string", rs.getString("persistenceid"));
				row.put("activo", rs.getBoolean("activo"));
				row.put("is_eliminado", rs.getBoolean("is_eliminado"));
				row.put("clave", rs.getString("clave"));
				row.put("descripcion", rs.getString("descripcion"));
				row.put("fecha_inicio", rs.getString("fecha_inicio"));
				row.put("fecha_fin", rs.getString("fecha_fin"));
				row.put("fecha_creacion", rs.getString("fecha_creacion"));
				row.put("fecha_importacion", rs.getString("fecha_importacion"));
				row.put("id", rs.getString("id"));
				row.put("is_anual", rs.getBoolean("is_anual"));
				row.put("is_cuatrimestral", rs.getBoolean("is_cuatrimestral"));
				row.put("is_semestral", rs.getBoolean("is_semestral"));
				row.put("is_propedeutico", rs.getBoolean("is_propedeutico"));
				row.put("is_enabled", rs.getBoolean("is_enabled"));
				row.put("ano", rs.getString("ano"));
				row.put("codigo", rs.getString("codigo"));
				
				rows.add(row);
			}
			
			resultado.setData(rows);
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getLstPeriodosDisponibles] " + e.getMessage());
			if (!con.autoCommit) con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getLstCampus(String jsonData) {
	    Result resultado = new Result()
	    boolean closeCon = false
	    List<Map<String, Object>> data = []
	
	    try {
	        // Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
	        def jsonSlurper = new JsonSlurper()
	        def object = jsonSlurper.parseText(jsonData)
	
	        closeCon = validarConexion()
	
	        // Ejecutar la consulta SQL
	        pstm = con.prepareStatement(StatementsCatalogos.GET_LSTCAMPUS)
	        rs = pstm.executeQuery()
	
	        // Obtener los metadatos de las columnas para obtener los nombres de los campos
	        def metaData = rs.getMetaData()
	        int columnCount = metaData.getColumnCount()
	
	        // Procesar los resultados y llenar la lista data
	        while (rs.next()) {
	            def row = [:]
	            for (int i = 1; i <= columnCount; i++) {
	                row[metaData.getColumnLabel(i)] = rs.getObject(i)
	            }
	            data.add(row)
	        }
	
	        resultado.setData(data)
	        resultado.setSuccess(true)
	    } catch (Exception e) {
	        resultado.setSuccess(false)
	        resultado.setError("[getLstCampus] ${e.message}")
	    } finally {
	        // Cerrar la conexión en caso de que esté abierta
	        if (closeCon) {
	            new DBConnect().closeObj(con, stm, rs, pstm)
	        }
	    }
	
	    return resultado
	}
	
	public Result getLstCampusByGurpoBonita(String jsonData) {
		Result resultado = new Result()
		boolean closeCon = false
		List<Map<String, Object>> data = []
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper()
			def jsonObject = jsonSlurper.parseText(jsonData)
			def lstCatCampus = jsonObject.lstCatCampus
			
			closeCon = validarConexion()
			
			// Iterar sobre cada campus
			lstCatCampus.each { campus ->
			    // Ejecutar la consulta SQL para cada campus
			    pstm = con.prepareStatement("SELECT * FROM PSGRCatCampus WHERE grupo_bonita = ? AND eliminado = false")
			    pstm.setString(1, campus)
			    rs = pstm.executeQuery()
			
			    // Obtener los metadatos de las columnas para obtener los nombres de los campos
			    def metaData = rs.getMetaData()
			    int columnCount = metaData.getColumnCount()
			
			    // Procesar los resultados y llenar la lista data
			    while (rs.next()) {
			        def row = [:]
			        for (int i = 1; i <= columnCount; i++) {
			            row[metaData.getColumnLabel(i)] = rs.getObject(i)
			        }
			        data.add(row)
			    }
			}
	
			resultado.setData(data)
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError("[getLstCampus] ${e.message}")
		} finally {
			// Cerrar la conexión en caso de que esté abierta
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado
	}
	
	public Result getLstPeriodoByIdCampus(String id_campus, RestAPIContext context) {
		Result resultado = new Result()
		boolean closeCon = false
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> row = new HashMap<String, Object>();
	
		try {
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(StatementsCatalogos.GET_PERIODOBYIDCAMPUS);
			pstm.setLong(1, Long.valueOf(id_campus));
			rs = pstm.executeQuery()
	
			while (rs.next()) {
				row = new HashMap<String, Object>();
				
				row.put("persistenceId", rs.getLong("persistenceid"));
				row.put("persistenceId_string", rs.getString("persistenceid"));
				row.put("descripcion", rs.getString("descripcion"));
				
				data.add(row);
			}
	
			resultado.setData(data)
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError("[getLstCampus] ${e.message}")
		} finally {
			// Cerrar la conexión en caso de que esté abierta
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado
	}
	
	public Result getLstPosgradoByIdCampus(String id_campus, RestAPIContext context) {
		Result resultado = new Result()
		boolean closeCon = false
		List<Map<String, Object>> data = []
	
		try {
			closeCon = validarConexion()
	
			pstm = con.prepareStatement(StatementsCatalogos.GET_POSGRADOBYIDCAMPUS);
			pstm.setLong(1, Long.valueOf(id_campus));
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new HashMap<String, Object>();
				
				row.put("persistenceId", rs.getLong("persistenceid"));
				row.put("persistenceId_string", rs.getString("persistenceid"));
				row.put("descripcion", rs.getString("descripcion"));
				
				data.add(row)
			}
	
			resultado.setData(data)
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError("[getLstCampus] ${e.message}")
		} finally {
			// Cerrar la conexión en caso de que esté abierta
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado
	}
	
	public Result getLstGestionEscolarByIdCampus(String id_campus, RestAPIContext context) {
		Result resultado = new Result()
		boolean closeCon = false
		List<CatGestionEscolar> data = new ArrayList<CatGestionEscolar>();
		CatGestionEscolar cgest = new CatGestionEscolar();
		
		String errorLog = "";
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(StatementsCatalogos.GET_GESTIONESCOLARBYIDCAMPUS);
			pstm.setLong(1, Long.valueOf(id_campus));
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				cgest = new CatGestionEscolar();
				
				cgest.setPersistenceId(rs.getLong("persistenceid"));
				cgest.setPersistenceId_string(rs.getString("persistenceid"));
				cgest.setDescripcion(rs.getString("descripcion"));
				cgest.setNombre(rs.getString("nombre"));
				cgest.setPropedeutico(rs.getBoolean("propedeutico"));
				cgest.setIs_medicina(rs.getBoolean("is_medicina"));
				
				data.add(cgest);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			errorLog += " [ERROR] | " e.getMessage();
			resultado.setSuccess(false);
			resultado.setError("[getLstCampus] ${e.message}");
		} finally {
			resultado.setError_info(errorLog);
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado
	}
	
	public Result getCatConfiguraciones(String campus) {
		LOGGER.error "[getCatConfiguraciones] 1 " ;
		Result resultado = new Result()
		boolean closeCon = false
		List<Map<String, Object>> data = new ArrayList<>();
		Map<String, Object> row = new HashMap<>();
		
		try {
			closeCon = validarConexion();
			LOGGER.error "[getCatConfiguraciones] 2 " ;
			// Ejecutar la consulta SQL
			pstm = con.prepareStatement(StatementsCatalogos.GET_APYKEYMAILGUN)
			pstm.setString(1, campus);
			LOGGER.error "[getCatConfiguraciones] " + campus;
			rs = pstm.executeQuery();
			LOGGER.error "[getCatConfiguraciones] 3 " ;
			while (rs.next()) {
				LOGGER.error "[getCatConfiguraciones] " +  rs.getString("clave") + " | | " +  rs.getString("valor");
				if(rs.getString("clave").equals("mailgun_dominio")) {
					row.put("mailgun_dominio", rs.getString("valor"));
				} else if (rs.getString("clave").equals("mailgun_apikey")) {
					row.put("mailgun_apikey", rs.getString("valor"));
				}  else if (rs.getString("clave").equals("mailgun_apikey")) {
					row.put("mailgun_correo", rs.getString("valor"));
				} 
				
				row.put("id_campus", rs.getLong("id_campus"));
			}
			data.add(row);
			
			resultado.setData(data);
			resultado.setSuccess(true)
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatConfiguraciones] ${e.message}")
		} finally {
			if(con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado
	}
	
	public Result insertCatPropedeutico(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			} else if(object.usuario_creacion.equals("") || object.usuario_creacion == null) {
				throw new Exception("El campo \"usuario_creacion\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATPROPEDEUTICO);
			pstm.setBoolean(1, false); // is_eliminado
			pstm.setInt(2, 0);
			pstm.setString(3, object.clave);
			pstm.setString(4, object.descripcion);
			pstm.setString(5, object.usuario_creacion);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(6, fechaHoraFormateada);

			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatPropedeutico] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}




	public Result deleteCatPropedeutico(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATPROPEDEUTICO);
			pstm.setBoolean(1, true);
			pstm.setLong(2, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatPropedeutico] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatPropedeutico(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			} else if(object.usuario_creacion.equals("") || object.usuario_creacion == null) {
				throw new Exception("El campo \"usuario_creacion\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATPROPEDEUTICO);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setLong(3, object.persistenceId);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatPropedeutico] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatPropedeutico(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRCatEstado> data = new ArrayList<>();
		String where = "WHERE IS_ELIMINADO=false"; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "usuarioCreacion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(usuario_creacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "fechaCreacion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(fecha_creacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ISELIMINADO":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(IS_ELIMINADO) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATPROPEDEUTICO.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				PSGRCatEstado row = new PSGRCatEstado();
				row.setPersistenceId(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setUsuarioCreacion(rs.getString("usuario_creacion"));
				row.setFechaCreacion(rs.getString("fecha_creacion"));
				row.setIsEliminado(rs.getBoolean("is_eliminado"));
//				row.setPersistenceId(rs.getLong("persistenceId"));
//				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
	
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatPropedeutico] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatPosgrado(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATPOSGRADO);
			pstm.setBoolean(1, false); // is_eliminado
			pstm.setInt(2, 0);
			pstm.setString(3, object.clave);
			pstm.setString(4, object.descripcion);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(5, fechaHoraFormateada);

			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatPropedeutico] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result deleteCatPosgrado(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATPOSGRADO);
			pstm.setBoolean(1, true);
			pstm.setLong(2, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatPosgrado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatPosgrado(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATPOSGRADO);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setLong(3, object.persistenceId);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatPosgrado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatPosgrado(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRCatEstado> data = new ArrayList<>();
		String where = "WHERE IS_ELIMINADO=false"; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "fecha_creacion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(fecha_registro) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ISELIMINADO":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(IS_ELIMINADO) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATPOSGRADO.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				PSGRCatEstado row = new PSGRCatEstado();
				row.setPersistenceId(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setFechaCreacion(rs.getString("fecha_registro"));
				row.setIsEliminado(rs.getBoolean("is_eliminado"));
//				row.setPersistenceId(rs.getLong("persistenceId"));
//				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
	
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatPosgrado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatEstadoCivil(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.id.equals("") || object.id == null) {
				throw new Exception("El campo \"ID banner\" no debe ir vacío");
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATESTADOCIVIL);
			pstm.setBoolean(1, false); // is_eliminado
			pstm.setInt(2, 0); //persistenceversion
			pstm.setString(3, object.id);
			pstm.setBoolean(4, true);//is_enabled
			pstm.setString(5, object.clave);
			pstm.setString(6, object.descripcion);
			pstm.setString(7, object.usuario_creacion);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(8, fechaHoraFormateada);//fecha_creacion
			pstm.setString(9, fechaHoraFormateada);//fecha_importacion

			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatEstadoCivil] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}




	public Result deleteCatEstadoCivil(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATESTADOCIVIL);
			pstm.setBoolean(1, true);
			pstm.setLong(2, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatEstadoCivil] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatEstadoCivil(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.id.equals("") || object.id == null) {
				throw new Exception("El campo \"ID banner\" no debe ir vacío");
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATESTADOCIVIL);
//			pstm.setInt(2, 0); //persistenceversion
			pstm.setString(1, object.id);
			pstm.setString(2, object.clave);
			pstm.setString(3, object.descripcion);
			pstm.setLong(4, object.persistenceId);
//			pstm.setString(7, object.usuario_creacion);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatEstadoCivil] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatEstadoCivil(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRCatEstado> data = new ArrayList<>();
		String where = "WHERE IS_ELIMINADO=false"; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "id":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(id) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "usuarioCreacion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(usuario_banner) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "fechaCreacion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(fecha_creacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ISELIMINADO":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(IS_ELIMINADO) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATESTADOCIVIL.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				PSGRCatEstadoCivil row = new PSGRCatEstadoCivil();
				row.setPersistenceId(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setId(rs.getString("id"));
				row.setUsuarioBanner(rs.getString("usuario_banner"));
				row.setFechaCreacion(rs.getString("fecha_creacion"));
				row.setFechaImportacion(rs.getString("fecha_importacion"));
				row.setIsEliminado(rs.getBoolean("is_eliminado"));
//				row.setPersistenceId(rs.getLong("persistenceId"));
//				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
	
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatEstadoCivil] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatReligion(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.id.equals("") || object.id == null) {
				throw new Exception("El campo \"ID banner\" no debe ir vacío");
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATRELIGION);
			pstm.setBoolean(1, false); // is_eliminado
			pstm.setInt(2, 0); //persistenceversion
			pstm.setString(3, object.id);
			pstm.setBoolean(4, true);//is_enabled
			pstm.setString(5, object.clave);
			pstm.setString(6, object.descripcion);
			pstm.setString(7, object.usuario_creacion);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(8, fechaHoraFormateada);//fecha_creacion
			pstm.setString(9, fechaHoraFormateada);//fecha_importacion

			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatReligion] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}




	public Result deleteCatReligion(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATRELIGION);
			pstm.setBoolean(1, true);
			pstm.setLong(2, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatReligion] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result deleteCatReligionFisico(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATRELIGIONFISICO);
			pstm.setLong(1, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatReligion] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatReligion(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.id.equals("") || object.id == null) {
				throw new Exception("El campo \"ID banner\" no debe ir vacío");
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATRELIGION);
//			pstm.setInt(2, 0); //persistenceversion
			pstm.setString(1, object.id);
			pstm.setString(2, object.clave);
			pstm.setString(3, object.descripcion);
			pstm.setLong(4, object.persistenceId);
//			pstm.setString(7, object.usuario_creacion);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatReligion] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatReligion(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRCatEstado> data = new ArrayList<>();
		String where = "WHERE IS_ELIMINADO_VALUE = false"; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "id":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(id) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "usuarioBanner":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(usuario_banner) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "fechaCreacion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(fecha_creacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "fechaImplementacion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(fecha_importacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ISELIMINADO":
					    if (where.contains("WHERE")) {
					        where.append(" AND ");
					    } else {
					        where.append(" WHERE ");
					    }
					    where.append(" IS_ELIMINADO_VALUE ");
					
					    // No es necesario LOWER para booleanos
					    if (filtro.get("operador").equals("Igual a")) {
					        where.append("= ?");
					    } else {
					        where.append("LIKE ?");
					        filtro.put("valor", "%" + filtro.get("valor") + "%");  // Ajustar el valor para el LIKE
					    }
					
					    parameterValues.add(filtro.get("valor"));
					    break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATRELIGION.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				PSGRCatEstadoCivil row = new PSGRCatEstadoCivil();
				row.setPersistenceId(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setId(rs.getString("id"));
				row.setUsuarioBanner(rs.getString("usuario_banner"));
				row.setFechaCreacion(rs.getString("fecha_creacion"));
				row.setFechaImportacion(rs.getString("fecha_importacion"));
				row.setIsEliminadoValue(rs.getBoolean("is_eliminado_value"));
//				row.setPersistenceId(rs.getLong("persistenceId"));
//				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
	
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatReligion] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatNacionalidad(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.id.equals("") || object.id == null) {
				throw new Exception("El campo \"ID banner\" no debe ir vacío");
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATNACIONALIDAD);
			pstm.setBoolean(1, false); // is_eliminado
			pstm.setInt(2, 0); //persistenceversion
			pstm.setString(3, object.id);
			pstm.setBoolean(4, object.is_enabled);
			pstm.setString(5, object.clave);
			pstm.setString(6, object.descripcion);
			pstm.setString(7, object.usuario_creacion);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(8, fechaHoraFormateada);//fecha_creacion
			try {
			    int ordenInt = Integer.valueOf(object.orden);
			    pstm.setInt(9, ordenInt);
			} catch (NumberFormatException e) {
			    e.printStackTrace();
			}

			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatNacionalidad] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}




	public Result deleteCatNacionalidad(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATNACIONALIDAD);
			pstm.setBoolean(1, true);
			pstm.setLong(2, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatNacionalidad] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatNacionalidad(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.id.equals("") || object.id == null) {
				throw new Exception("El campo \"ID banner\" no debe ir vacío");
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATNACIONALIDAD);
//			pstm.setInt(2, 0); //persistenceversion
			pstm.setString(1, object.id);
			pstm.setString(2, object.clave);
			pstm.setString(3, object.descripcion);
			pstm.setInt(4, object.orden);
			pstm.setBoolean(5, object.is_enabled);
			pstm.setLong(6, object.persistenceId);
//			pstm.setString(7, object.usuario_creacion);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatNacionalidad] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatNacionalidad(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRCatEstado> data = new ArrayList<>();
		String where = "WHERE IS_ELIMINADO=false"; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "orden":
				        if (where.contains("WHERE")) {
				            where += " AND ";
				        } else {
				            where += " WHERE ";
				        }
				        where += " CAST(orden AS TEXT) ";
				        if (filtro.get("operador").equals("Igual a")) {
				            where += "= '" + filtro.get("valor") + "'";
				        } else {
				            where += " LIKE '%" + filtro.get("valor") + "%'";
				        }
				        break;
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "idbanner":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(id) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "usuariobanner":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(usuario_banner) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "fechacreacion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(fecha_creacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ISELIMINADO":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(IS_ELIMINADO) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATNACIONALIDAD.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				PSGRCatNacionalidad row = new PSGRCatNacionalidad();
				row.setPersistenceId(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setId(rs.getString("id"));
				row.setUsuarioBanner(rs.getString("usuario_banner"));
				row.setFechaCreacion(rs.getString("fecha_creacion"));
				row.setIsEliminado(rs.getBoolean("is_eliminado"));
				row.setOrden(rs.getLong("orden"));
//				row.setPersistenceId(rs.getLong("persistenceId"));
//				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
	
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatNacionalidad] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatPosgrado2(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper()
			def jsonObject = jsonSlurper.parseText(jsonData)
			
			// Accede al primer elemento del arreglo lstCatCampusInput (suponiendo que haya solo uno)
			def object = jsonObject.lstCatGestionEscolarInput[0]
	
			if (object == null) {
				throw new Exception("El objeto 'object' no debe ser nulo.");
			} else if (object.clave == null || object.clave.isEmpty()) {
				throw new Exception("El campo \"Clave\" no debe ir vacío.");
			} else if (object.descripcion == null || object.descripcion.isEmpty()) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío.");
			} else if (object.usuarioCreacion == null || object.usuarioCreacion.isEmpty()) {
				throw new Exception("El campo \"usuarioCreacion\" no debe ir vacío.");
			} 
//			else if (object.CAMPUS.PERSISTENCEID == null || object.CAMPUS.PERSISTENCEID.isEmpty()) {
//				throw new Exception("El campo \"CAMPUS.PERSISTENCEID\" no debe ir vacío.");
//			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATPOSGRADO2);
			pstm.setLong(1, 0); // PERSISTENCEVERSION
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(2, fechaHoraFormateada);
			pstm.setBoolean(3, false); // IS_ELIMINADO
			pstm.setString(4, object.clave); // Clave
			pstm.setString(5, object.descripcion); // DESCRIPCION
			pstm.setString(6, object.usuarioCreacion); // UUSUARIOCREACION
			pstm.setLong(7, object.CAMPUS.persistenceId); // CAMPUS_pid

			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatPosgrado2] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	
	
	
	public Result deleteCatPosgrado2(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío" + object);
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATPOSGRADO2);
			pstm.setBoolean(1, true);
			pstm.setLong(2, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatPosgrado2] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result updateCatPosgrado2(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper()
			def jsonObject = jsonSlurper.parseText(jsonData)
			
			// Accede al primer elemento del arreglo lstCatCampusInput (suponiendo que haya solo uno)
			def object = jsonObject.lstCatGestionEscolarInput[0]

			if(object.equals("") || object == null) {
				throw new Exception("El campo \"object\" no debe ir vacío" + object);
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATPOSGRADO2);
			pstm.setString(1, object.clave);       // Clave
			pstm.setString(2, object.descripcion); // DESCRIPCION
			pstm.setLong(3, object.persistenceId); // persistenceid
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatPosgrado2] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatPosgrado2(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", orderby = "ORDER BY ", errorLog = "", bachillerato = "", campus = ""

		List < String > lstGrupo = new ArrayList < String > ();
		List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();

		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map < String, String > objGrupoCampus = new HashMap < String, String > ();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			def objCatCampusDAO = context.apiClient.getDAO(PSGRCatCampusDAO.class);
			List < PSGRCatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)

			userLogged = context.getApiSession().getUserId();

			List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for (UserMembership objUserMembership: lstUserMembership) {
				for (PSGRCatCampus rowGrupo: lstCatCampus) {
					if (objUserMembership.getGroupName().equals(rowGrupo.getGrupo_bonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}

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

			String consulta = StatementsCatalogos.GET_CATPOSGRADO2
			CatGestionEscolar row = new CatGestionEscolar();
			List < CatDescuentosCustom > rows = new ArrayList < CatDescuentosCustom > ();
			closeCon = validarConexion();
//			throw new Exception("El campo \"Descripción\" no debe ir vacío"+ object);
			where = "WHERE GE.is_eliminado = false and campus.eliminado = false and GE.campus_pid = '" + object.persistenceid + "'"
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				def booleanos = filtro.get("valor");
				switch (filtro.get("columna")) {
					case "CLAVE":
						where += " AND LOWER(GE.CLAVE) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "DESCRIPCIÓN":
						where += " AND LOWER(GE.descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
//					case "ID CAMPUS":
//						where += " AND LOWER(GE.descripcion) ";
//						if (filtro.get("operador").equals("Igual a")) {
//							where += "=LOWER('[valor]')"
//						} else {
//							where += "LIKE LOWER('%[valor]%')"
//						}
//						where = where.replace("[valor]", filtro.get("valor"))
//						break;
					case "USUARIO_CREACION":
						where += " AND LOWER(GE.usuario_creacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "FECHA_CREACIÓN":
						where += " AND LOWER(GE.fecha_registro) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "PERSISTENCEVERSION":
						where += " AND LOWER(GE.PERSISTENCEVERSION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}
			switch (object.orderby) {
				case "CLAVE":
					orderby += "GE.clave";
					break;
				case "DESCRIPCION DE CARRERA":
					orderby += "GE.descripcion";
					break;
				case "PERSISTENCEVERSION":
					orderby += "GE.PERSISTENCEVERSION";
					break;
				case "CAMPUS":
					orderby += "campus.descripcion";
					break;
				default:
					orderby += "GE.persistenceid"
					break;
			}

			orderby += " " + object.orientation;
			//where+=" "+campus
			consulta = consulta.replace("[CAMPUS]", campus)
			consulta = consulta.replace("[WHERE]", where);

			pstm = con.prepareStatement(consulta.replace("GE.*, campus.descripcion as nombreCampus", "COUNT(GE.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorLog += " " + consulta
			errorLog += " consulta= "
			errorLog += consulta
			errorLog += " where = " + where
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)

			errorLog += "fecha=="

			rs = pstm.executeQuery()
			while (rs.next()) {

				row = new CatGestionEscolar()
				row.setCampus_pid(rs.getLong("campus_pid"))
				//row.setCaseId(rs.getString("caseId"))
				row.setDescripcion(rs.getString("descripcion"))
				row.setFecha_registro(rs.getString("fecha_registro"));
				row.setClave(rs.getString("clave"));
				row.setIs_eliminado(rs.getBoolean("is_eliminado"))
				//row.setMatematicas(rs.getBoolean("matematicas"))
				row.setPersistenceId(rs.getLong("persistenceId"))
//				row.setCampusReferenciaPid(rs.getLong("campus_referencia_pid"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				row.setUsuario_creacion(rs.getString("usuario_creacion"))				

				rows.add(row)
			}

			resultado.setSuccess(true)
			resultado.setError(errorLog)
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
	
	public Result insertCatTipoEmleado(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATTIPOEMPLEADO);
			pstm.setString(1, object.clave);
			pstm.setLong(2, object.orden);
			pstm.setString(3, object.descripcion);
		
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatTipoEmpleado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}




	public Result deleteCatTipoEmpleado(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATTIPOEMPLEADO);
			pstm.setLong(1, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatTipoEmpleado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatTipoEmpleado(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATTIPOEMPLEADO);
			pstm.setString(1, object.clave);
			pstm.setLong(2, object.orden);
			pstm.setString(3, object.descripcion);
			pstm.setLong(4, object.persistenceid);
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatTipoEmpleado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatTipoEmpleado(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRFiltroSeguridad> data = new ArrayList<>();
		String where = "WHERE IS_ELIMINADO=false"; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "orden":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(orden) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATTIPOEMPLEADO.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				PSGRCatTipoEmpresa row = new PSGRCatTipoEmpresa();
				row.setPersistenceid(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setOrden(rs.getLong("orden"));
				row.setDescripcion(rs.getString("descripcion"));
	
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatTipoEmpleado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatTipoAlumno(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATTIPOALUMNO);
			pstm.setString(1, object.clave);
			pstm.setLong(2, object.orden);
			pstm.setString(3, object.descripcion);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(4, fechaHoraFormateada);//fecha_creacion
			pstm.setString(5, object.usuario_creacion);
		
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatTipoEmpleado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result deleteCatTipoAlumno(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATTIPOALUMNO);
			pstm.setLong(1, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatTipoEmpleado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatTipoAlumno(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATTIPOALUMNO);
			pstm.setString(1, object.clave);
			pstm.setLong(2, object.orden);
			pstm.setString(3, object.descripcion);
			pstm.setLong(4, object.persistenceid);
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatTipoEmpleado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatTipoAlumno(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRFiltroSeguridad> data = new ArrayList<>();
		String where = "WHERE IS_ELIMINADO=false"; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "orden":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(orden) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATTIPOALUMNO.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				PSGRCatTipoEmpresa row = new PSGRCatTipoEmpresa();
				row.setPersistenceid(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setOrden(rs.getLong("orden"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setUsuario_creacion(rs.getString("usuario_creacion"));
				row.setFecha_creacion(rs.getString("fecha_creacion"));
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatTipoEmpleado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatResidencia(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATRESIDENCIA);
			pstm.setString(1, object.clave);
			pstm.setLong(2, object.orden);
			pstm.setString(3, object.descripcion);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(4, fechaHoraFormateada);//fecha_creacion
			pstm.setString(5, object.usuario_creacion);
		
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatTipoEmpleado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result deleteCatResidencia(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATRESIDENCIA);
			pstm.setLong(1, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatTipoEmpleado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatResidencia(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATRESIDENCIA);
			pstm.setString(1, object.clave);
			pstm.setLong(2, object.orden);
			pstm.setString(3, object.descripcion);
			pstm.setLong(4, object.persistenceid);
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatTipoEmpleado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatResidencia(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRFiltroSeguridad> data = new ArrayList<>();
		String where = "WHERE IS_ELIMINADO=false"; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "orden":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(orden) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATRESIDENCIA.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				PSGRCatTipoEmpresa row = new PSGRCatTipoEmpresa();
				row.setPersistenceid(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setOrden(rs.getLong("orden"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setUsuario_creacion(rs.getString("usuario_creacion"));
				row.setFecha_creacion(rs.getString("fecha_creacion"));
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatTipoEmpleado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatTipoAdmision(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATTIPOADMISION);
			pstm.setString(1, object.clave);
			pstm.setLong(2, object.orden);
			pstm.setString(3, object.descripcion);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(4, fechaHoraFormateada);//fecha_creacion
			pstm.setString(5, object.usuario_creacion);
		
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatTipoEmpleado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result deleteCatTipoAdmision(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATTIPOADMISION);
			pstm.setLong(1, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatTipoEmpleado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatTipoAdmision(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATTIPOADMISION);
			pstm.setString(1, object.clave);
			pstm.setLong(2, object.orden);
			pstm.setString(3, object.descripcion);
			pstm.setLong(4, object.persistenceid);
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatTipoEmpleado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatTipoAdmision(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRFiltroSeguridad> data = new ArrayList<>();
		String where = "WHERE IS_ELIMINADO=false"; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "orden":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(orden) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATTIPOADMISION.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				PSGRCatTipoEmpresa row = new PSGRCatTipoEmpresa();
				row.setPersistenceid(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setOrden(rs.getLong("orden"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setUsuario_creacion(rs.getString("usuario_creacion"));
				row.setFecha_creacion(rs.getString("fecha_creacion"));
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatTipoEmpleado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatGiroEmpresa(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATGIROEMPRESA);
			pstm.setString(1, object.clave);
			pstm.setLong(2, object.orden);
			pstm.setString(3, object.descripcion);
		
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatFiltroSeguridad] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}




	public Result deleteCatGiroEmpresa(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATGIROEMPRESA);
			pstm.setLong(1, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatFiltroSeguridad] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatGiroEmpresa(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATGIROEMPRESA);
			pstm.setString(1, object.clave);
			pstm.setLong(2, object.orden);
			pstm.setString(3, object.descripcion);
			pstm.setLong(4, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatFiltroSeguridad] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatGiroEmpresa(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRFiltroSeguridad> data = new ArrayList<>();
		String where = "WHERE IS_ELIMINADO=false"; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "orden":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(orden) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATGIROEMPRESA.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				PSGRCatTipoEmpresa row = new PSGRCatTipoEmpresa();
				row.setPersistenceid(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setOrden(rs.getLong("orden"));
				row.setDescripcion(rs.getString("descripcion"));
	
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatFiltroSeguridad] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatGradoEstudio(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATGRADOESTUDIO);
			pstm.setString(1, object.clave);
			pstm.setLong(2, object.orden);
			pstm.setString(3, object.descripcion);
		
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatFiltroSeguridad] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}




	public Result deleteCatGradoEstudio(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATGRADOESTUDIO);
			pstm.setLong(1, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatFiltroSeguridad] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatGradoEstudio(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATGRADOESTUDIO);
			pstm.setString(1, object.clave);
			pstm.setLong(2, object.orden);
			pstm.setString(3, object.descripcion);
			pstm.setLong(4, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatFiltroSeguridad] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatGradoEstudio(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRFiltroSeguridad> data = new ArrayList<>();
		String where = "WHERE IS_ELIMINADO=false"; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "orden":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(orden) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATGRADOESTUDIO.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				PSGRCatTipoEmpresa row = new PSGRCatTipoEmpresa();
				row.setPersistenceid(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setOrden(rs.getLong("orden"));
				row.setDescripcion(rs.getString("descripcion"));
	
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatFiltroSeguridad] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatPuestoTrabajo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATPUESTOTRABAJO);
			pstm.setString(1, object.clave);
			pstm.setLong(2, object.orden);
			pstm.setString(3, object.descripcion);
		
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatFiltroSeguridad] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}




	public Result deleteCatPuestoTrabajo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATPUESTOTRABAJO);
			pstm.setLong(1, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatFiltroSeguridad] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatPuestoTrabajo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATPUESTOTRABAJO);
			pstm.setString(1, object.clave);
			pstm.setLong(2, object.orden);
			pstm.setString(3, object.descripcion);
			pstm.setLong(4, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatFiltroSeguridad] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatPuestoTrabajo(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRFiltroSeguridad> data = new ArrayList<>();
		String where = "WHERE IS_ELIMINADO=false"; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "orden":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(orden) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATPUESTOTRABAJO.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				PSGRCatTipoEmpresa row = new PSGRCatTipoEmpresa();
				row.setPersistenceid(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setOrden(rs.getLong("orden"));
				row.setDescripcion(rs.getString("descripcion"));
	
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatFiltroSeguridad] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatParentesco(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATPARENTESCO);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(3, fechaHoraFormateada);//fecha_creacion
			pstm.setString(4, object.usuario_banner);
		
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatTipoEmpleado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}




	public Result deleteCatParentesco(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATPARENTESCO);
			pstm.setLong(1, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatTipoEmpleado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatParentesco(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATPARENTESCO);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setLong(3, object.persistenceId);
			
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatTipoEmpleado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatParentesco(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRFiltroSeguridad> data = new ArrayList<>();
		String where = "WHERE IS_ELIMINADO=false"; // Aplicar filtro por defecto para registros no eliminados
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATPARENTESCO.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				CatPaisCustomFiltro row = new CatPaisCustomFiltro();
				row.setPersistenceId(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setUsuarioCreacion(rs.getString("usuario_creacion"));
				row.setFechaCreacion(rs.getString("fecha_creacion"));
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatTipoEmpleado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatNotificaciones(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
//			if(object.clave.equals("") || object.clave == null) {
//				throw new Exception("El campo \"Clave\" no debe ir vacío");
//			} else if(object.orden.equals("") || object.orden == null) {
//				throw new Exception("El campo \"orden\" no debe ir vacío");
//			} else if(object.descripcion.equals("") || object.descripcion == null) {
//				throw new Exception("El campo \"Descripción\" no debe ir vacío");
//			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATNOTIFICACIONES);
			pstm.setString(1, object.anguloimagenfooter);
			pstm.setString(2, object.anguloimagenheader);
			pstm.setString(3, object.asunto);
			pstm.setBoolean(4, object.bloqueaspirante);
			pstm.setString(5, object.caseid);
			pstm.setString(6, object.codigo);
			pstm.setString(7, object.comentarioleon);
			pstm.setString(8, object.contenido);
			pstm.setString(9, object.contenidocorreo);
			pstm.setString(10, object.contenidoleonel);
			pstm.setString(11, object.descripcion);
			pstm.setString(12, object.docguiaestudio);
			pstm.setString(13, object.enlacebanner);
			pstm.setString(14, object.enlacecontacto);
			pstm.setString(15, object.enlacefacebook);
			pstm.setString(16, object.enlacefooter);
			pstm.setString(17, object.enlaceinstagram);
			pstm.setString(18, object.enlacetwitter);
			pstm.setBoolean(19, object.informacionlic);
			pstm.setBoolean(20, object.iseliminado);
			pstm.setString(21, object.nombreimagenfooter);
			pstm.setString(22, object.nombreimagenheader);
			pstm.setLong(23, object.persistenceversion);
			pstm.setString(24, object.textofooter);
			pstm.setString(25, object.tipocorreo);
			pstm.setString(26, object.titulo);
			pstm.setString(27, object.urlimgfooter);
			pstm.setString(28, object.urlimgheader);
		
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatFiltroSeguridad] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatProcesoCaso(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
//			if(object.clave.equals("") || object.clave == null) {
//				throw new Exception("El campo \"Clave\" no debe ir vacío");
//			} else if(object.orden.equals("") || object.orden == null) {
//				throw new Exception("El campo \"orden\" no debe ir vacío");
//			} else if(object.descripcion.equals("") || object.descripcion == null) {
//				throw new Exception("El campo \"Descripción\" no debe ir vacío");
//			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATPROCESOCASO);
			pstm.setString(1, object.campus);
			pstm.setString(2, object.proceso);
		
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatFiltroSeguridad] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatNotificacionescampus(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
//			if(object.clave.equals("") || object.clave == null) {
//				throw new Exception("El campo \"Clave\" no debe ir vacío");
//			} else if(object.orden.equals("") || object.orden == null) {
//				throw new Exception("El campo \"orden\" no debe ir vacío");
//			} else if(object.descripcion.equals("") || object.descripcion == null) {
//				throw new Exception("El campo \"Descripción\" no debe ir vacío");
//			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATNOTIFICACIONESCAMPUS);
			pstm.setLong(1, object.catcampus_pid);
			pstm.setLong(2, object.catnotificacionesfirma_pid);
			pstm.setString(3, object.codigo);
			pstm.setString(4, object.copia);
			pstm.setString(5, object.footer);
			pstm.setString(6, object.header);
		
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatFiltroSeguridad] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result deleteCatNotificaciones(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATNOTIFICACIONES);
			pstm.setLong(1, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatTipoEmpleado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatRequisitosAdicionales(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATREQUISITOSADICIONALES);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(3, fechaHoraFormateada);//fecha_creacion
			pstm.setString(4, object.usuario_banner);
			pstm.setLong(5, object.campus_pid.persistenceId);
			pstm.setLong(6, object.posgrado_pid.persistenceId);
			pstm.setString(7, object.nombre);
			pstm.setString(8, object.tipo_de_archivo);
			pstm.setBoolean(9, object.requiere_documento);
			
		
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatTipoEmpleado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}




	public Result deleteCatRequisitosAdicionales(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATREQUISITOSADICIONALES);
			pstm.setLong(1, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatTipoEmpleado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}

	public Result updateCatRequisitosAdicionales(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATREQUISITOSADICIONALES);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setString(3, object.nombre);
			pstm.setString(4, object.tipo_de_archivo);
			pstm.setBoolean(5, object.requiere_documento);
			pstm.setLong(6, object.persistenceId);
			
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.")
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatTipoEmpleado] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatRequisitosAdicionales(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PSGRFiltroSeguridad> data = new ArrayList<>();
		
		String orderby = ""; // Ordenamiento por defecto
	
		try {
			// Parsear el objeto JSON para obtener los filtros y configuración de ordenamiento
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			String where = "WHERE IS_ELIMINADO=false AND campus_pid = '" + object.campus_pid.persistenceId +"'" + " AND posgrado_pid = '" + object.posgrado_pid.persistenceId + "'";
			closeCon = validarConexion();
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				
				switch (filtro.get("columna")) {
					case "clave":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "descripcion":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATREQUISITOSADICIONALES.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				CatRequisitosAdicionales row = new CatRequisitosAdicionales();
				row.setPersistenceId(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setUsuario_creacion(rs.getString("usuario_creacion"));
				row.setFecha_creacion(rs.getString("fecha_creacion"));
				row.setTipo_de_archivo(rs.getString("tipo_de_archivo"));
				row.setNombre(rs.getString("nombre"));
				row.setRequiere_documento(rs.getBoolean("requiere_documento"));
				data.add(row);
			}
	
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatTipoEmpleado] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	
	public Result getValidarOrden(Integer parameterP, Integer parameterC, String tabla, Integer orden, String id) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List < Boolean > lstResultado = new ArrayList < Boolean > ();

		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			String consulta;

			if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
				consulta = StatementsCatalogos.GET_VALIDACION_ORDEN_EDIT;
			} else {
				consulta = StatementsCatalogos.GET_VALIDACION_ORDEN;
			}

			consulta = consulta.replace("[TABLA]", tabla);
			String errorLog = consulta;
			
			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, orden);
			if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
				pstm.setLong(2, Long.valueOf(id));
			}

			rs = pstm.executeQuery();

			while (rs.next()) {
				lstResultado.add(rs.getInt("total") < 1);
			}

			resultado.setSuccess(true);
			resultado.setData(lstResultado);
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
	
	public Result getValidarClave(Integer parameterP, Integer parameterC, String tabla, String clave, String id) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List < Boolean > lstResultado = new ArrayList < Boolean > ();

		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			String consulta;
			String errorLog = consulta + ";";
			if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
				consulta = StatementsCatalogos.GET_VALIDACION_CLAVE_EDIT;
				errorLog += "GET_VALIDACION_CLAVE_EDIT; ";
			} else {
				consulta = StatementsCatalogos.GET_VALIDACION_CLAVE;
				errorLog += "GET_VALIDACION_CLAVE; ";
			}

			errorLog += consulta + ";";
			
			consulta = consulta.replace("[TABLA]", tabla);
			errorLog += consulta + ";";

			pstm = con.prepareStatement(consulta);
			pstm.setString(1, clave.toLowerCase());
			if (!id.equals(null) && !id.equals(" ") && !id.equals("")) {
				pstm.setLong(2, Long.valueOf(id));
			}

			rs = pstm.executeQuery();

			while (rs.next()) {
				lstResultado.add(rs.getInt("total") < 1);
			}

			resultado.setSuccess(true);
			resultado.setData(lstResultado);
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
	
	public Result insertCatEstatusProceso(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
			
			//Validaciones en el catálogo
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"Orden\" no debe ir vacío");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATESTATUSPROCESO);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setInt(3, object.orden);
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("Error al insertar el registro en la tabla.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatEstatusProceso] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result updateCatEstatusProceso(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//Validaciones en el catálogo
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El registro no existe.");
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"Orden\" no debe ir vacío");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATESTATUSPROCESO);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setInt(3, object.orden);
			pstm.setLong(4, Long.valueOf(object.persistenceid));
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("Error al actualizar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatEstatusProceso] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result deleteCatEstatusProceso(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//Validaciones en el catálogo
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El registro no existe.");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATESTATUSPROCESO);
			pstm.setLong(1, Long.valueOf(object.persistenceid));
			
			pstm.executeUpdate();
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatEstatusProceso] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result getCatEstatusProceso(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		PSGRCatEstatusProceso row = new PSGRCatEstatusProceso();
		List<PSGRCatEstatusProceso> data = new ArrayList<PSGRCatEstatusProceso>();
		String where = "", orderby = "";
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			orderby = " ORDER BY orden ASC";
			
			pstm = con.prepareStatement(StatementsCatalogos.SELECT_CATESTATUSPROCESO.replace("[WHERE]", where).replace("[ORDERBY]", orderby));
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				row = new PSGRCatEstatusProceso();
				row.setPersistenceid(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setOrden(rs.getInt("orden"));
				
				data.add(row);
			}
			
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatEstatusProceso] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result deleteCatEstatusProceso(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATESTATUSPROCESO);
			pstm.setLong(1, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatEstatusProceso] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result insertCatSiNo(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//Validaciones en el catálogo
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			} 
			
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATSINO);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setBoolean(3, Boolean.valueOf(object.esSiNo));
			pstm.setBoolean(4, Boolean.valueOf(object.esTalvez));
			pstm.setBoolean(5, Boolean.valueOf(object.esOtro));
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("Error al insertar el registro en la tabla.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatSiNo] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result deleteCatSiNo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío");
			}
	
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATSINO);
			pstm.setBoolean(1, true);
			pstm.setLong(2, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatSiNo] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result updateCatSiNo(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//Validaciones en el catálogo
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El registro no existe.");
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATSINO);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setBoolean(3, Boolean.valueOf(object.esSiNo));
			pstm.setBoolean(4, Boolean.valueOf(object.esTalvez));
			pstm.setBoolean(5, Boolean.valueOf(object.esOtro));
			pstm.setLong(6, Long.valueOf(object.persistenceid));
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("Error al actualizar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatSiNo] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
//	public Result deleteCatSiNo(String jsonData) {
//		Result resultado = new Result();
//		Boolean closeCon = false;
//	
//		try {
//			closeCon = validarConexion();
//			def jsonSlurper = new JsonSlurper();
//			def object = jsonSlurper.parseText(jsonData);
//			
//			//Validaciones en el catálogo
//			if(object.persistenceid.equals("") || object.persistenceid == null) {
//				throw new Exception("El registro no existe.");
//			}
//			
//			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATSINO);
//			pstm.setLong(1, Long.valueOf(object.persistenceid));
//			
//			pstm.executeUpdate();
//			resultado.setSuccess(true);
//		} catch (Exception e) {
//			resultado.setSuccess(false);
//			resultado.setError("[deleteCatSiNo] " + e.getMessage());
//		} finally {
//			if (closeCon) {
//				new DBConnect().closeObj(con, stm, rs, pstm)
//			}
//		}
//	
//		return resultado;
//	}
	
	public Result getCatSiNo(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		PSGRCatSiNo row = new PSGRCatSiNo();
		List<PSGRCatSiNo> data = new ArrayList<PSGRCatSiNo>();
		String where = "", orderby = "";
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			where = " WHERE eliminado <> true ";
			orderby = " ORDER BY clave ASC";
			
			pstm = con.prepareStatement(StatementsCatalogos.SELECT_CATSINO.replace("[WHERE]", where).replace("[ORDERBY]", orderby));
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				row = new PSGRCatSiNo();
				row.setPersistenceid(rs.getLong("persistenceid"));
				row.setClave(rs.getString("clave"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setEsSiNo(rs.getBoolean("es_si_o_no"));
				row.setEsTalvez(rs.getBoolean("es_talvez"));
				row.setEsOtro(rs.getBoolean("es_otro"));
				
				data.add(row);
			}
			
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatSiNo] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result insertConfiguraciones(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//Validaciones en el catálogo
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.valor.equals("") || object.valor == null) {
				throw new Exception("El campo \"Valor\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			} else if(object.id_campus.equals("") || object.id_campus == null) {
				throw new Exception("No campus seleccionado");
			} 
			
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CONFIGURACIONES);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.valor);
			pstm.setLong(3, Long.valueOf(object.id_campus));
			pstm.setString(4, object.descripcion);
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("Error al insertar el registro en la tabla.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertConfiguraciones] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result updateConfiguraciones(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//Validaciones en el catálogo
			if(object.persistenceId.equals("") || object.persistenceId == null) {
				throw new Exception("El registro no existe.");
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}else if(object.valor.equals("") || object.valor == null) {
				throw new Exception("El campo \"Valor\" no debe ir vacío");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CONFIGURACIONES);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.valor);
			pstm.setString(3, object.descripcion);
			pstm.setLong(4, Long.valueOf(object.persistenceId));
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("Error al actualizar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateConfiguraciones] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result deleteConfiguraciones(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//Validaciones en el catálogo
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El registro no existe.");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CONFIGURACIONES);
			pstm.setLong(1, Long.valueOf(object.persistenceid));
			
			pstm.executeUpdate();
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteConfiguraciones] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result getConfiguraciones(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", orderby = "ORDER BY ", errorLog = "", bachillerato = "", campus = ""

		List < String > lstGrupo = new ArrayList < String > ();
		List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();

		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map < String, String > objGrupoCampus = new HashMap < String, String > ();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def objCatCampusDAO = context.apiClient.getDAO(PSGRCatCampusDAO.class);
			List < PSGRCatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)
			userLogged = context.getApiSession().getUserId();
			List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for (UserMembership objUserMembership: lstUserMembership) {
				for (PSGRCatCampus rowGrupo: lstCatCampus) {
					if (objUserMembership.getGroupName().equals(rowGrupo.getGrupo_bonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}

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

			String consulta = StatementsCatalogos.GET_CONFIGURACIONES;
			ConfiguracionesGenerales row = new ConfiguracionesGenerales();
			List < ConfiguracionesGenerales > rows = new ArrayList < ConfiguracionesGenerales > ();
			
			closeCon = validarConexion();
			
			where = "WHERE campus.eliminado = false and GE.id_campus = '" + object.persistenceid + "'"
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				def booleanos = filtro.get("valor");
				switch (filtro.get("columna")) {
					case "CLAVE":
						where += " AND LOWER(GE.CLAVE) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "VALOR":
						where += " AND LOWER(GE.valor) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ID CAMPUS":
						where += " AND LOWER(GE.id_campus) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}
			switch (object.orderby) {
				case "CLAVE":
					orderby += "GE.clave";
					break;
				case "VALOR":
					orderby += "GE.valor";
					break;
				case "ID CAMPUS":
					orderby += "GE.id_campus";
					break;
				default:
					orderby += "GE.persistenceid"
					break;
			}

			orderby += " " + object.orientation;
			consulta = consulta.replace("[CAMPUS]", campus)
			consulta = consulta.replace("[WHERE]", where);
			
			pstm = con.prepareStatement(consulta.replace("GE.*, campus.descripcion as nombreCampus", "COUNT(GE.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
			
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"));
			}
			
			consulta = consulta.replace("[ORDERBY]", orderby);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");

			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new ConfiguracionesGenerales();
				row.setId_campus(rs.getLong("id_campus"));
				row.setValor(rs.getString("valor"));
				row.setClave(rs.getString("clave"));
				row.setPersistenceId(rs.getLong("persistenceId"));
				row.setDescripcion(rs.getString("descripcion"));
				
				rows.add(row);
			}
			
			resultado.setSuccess(true);
			resultado.setError(errorLog);
			resultado.setData(rows);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getConfiguraciones] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result insertCatDocumentos(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//Validaciones en el catálogo
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			}  else if(object.nombre_documento.equals("") || object.nombre_documento == null) {
				throw new Exception("El campo \"nombre_documento\" no debe ir vacío");
			}  else if(object.tipo_de_archivo.equals("") || object.tipo_de_archivo == null) {
				throw new Exception("El campo \"tipo_de_archivo\" no debe ir vacío");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATDOCUMENTOS);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setString(3, object.nombre_documento);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(4, fechaHoraFormateada);
			pstm.setBoolean(5, object.es_opcional);
			pstm.setString(6, object.tipo_de_archivo);
			pstm.setLong(7, object.campus_pid.persistenceId);
			pstm.setLong(8, object.posgrado_pid.persistenceId);
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("Error al insertar el registro en la tabla.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertConfiguraciones] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result updateCatDocumentos(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//Validaciones en el catálogo
			if(object.persistenceId.equals("") || object.persistenceId == null) {
				throw new Exception("El registro no existe.");
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			} else if(object.nombre_documento.equals("") || object.nombre_documento == null) {
				throw new Exception("El campo \"nombre_documento\" no debe ir vacío");
			} else if(object.tipo_de_archivo.equals("") || object.tipo_de_archivo == null) {
				throw new Exception("El campo \"tipo_de_archivo\" no debe ir vacío");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATDOCUMENTOS);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setString(3, object.nombre_documento);
			pstm.setBoolean(4, object.es_opcional);
			pstm.setString(5, object.tipo_de_archivo);
			pstm.setLong(6, object.persistenceId);
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("Error al actualizar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateConfiguraciones] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result deleteCatDocumentos(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//Validaciones en el catálogo
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El registro no existe.");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATDOCUMENTOS);
			pstm.setLong(1, Long.valueOf(object.persistenceid));
			
			pstm.executeUpdate();
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteConfiguraciones] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result getCatDocumentos(String jsonData, RestAPIContext context) {
		
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", orderby = "ORDER BY ", errorLog = "", bachillerato = "", campus = ""

		List < String > lstGrupo = new ArrayList < String > ();
		List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();

		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map < String, String > objGrupoCampus = new HashMap < String, String > ();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			def objCatCampusDAO = context.apiClient.getDAO(PSGRCatCampusDAO.class);
			List < PSGRCatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)

			userLogged = context.getApiSession().getUserId();

			List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for (UserMembership objUserMembership: lstUserMembership) {
				for (PSGRCatCampus rowGrupo: lstCatCampus) {
					if (objUserMembership.getGroupName().equals(rowGrupo.getGrupo_bonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}

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

			String consulta = StatementsCatalogos.GET_CATDOCUMENTOS
			CatGestionEscolar row = new CatGestionEscolar();
			List < CatDescuentosCustom > rows = new ArrayList < CatDescuentosCustom > ();
			closeCon = validarConexion();
//			throw new Exception("el valor de object es = "+ object);
			where = "WHERE campus.eliminado = false and GE.is_eliminado = false and GE.campus_pid = '" + object.campus_pid.persistenceId +"'" + " AND posgrado_pid = '" + object.posgrado_pid.persistenceId + "'"
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				def booleanos = filtro.get("valor");
				switch (filtro.get("columna")) {
					case "CLAVE":
						where += " AND LOWER(GE.CLAVE) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "VALOR":
						where += " AND LOWER(GE.valor) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ID CAMPUS":
						where += " AND LOWER(GE.id_campus) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}
			switch (object.orderby) {
				case "CLAVE":
					orderby += "GE.clave";
					break;
				case "VALOR":
					orderby += "GE.valor";
					break;
				case "ID CAMPUS":
					orderby += "GE.id_campus";
					break;
				default:
					orderby += "GE.persistenceid"
					break;
			}

//			orderby += " " + object.orientation;
			//where+=" "+campus
			consulta = consulta.replace("[CAMPUS]", campus)
			consulta = consulta.replace("[WHERE]", where);
			
			pstm = con.prepareStatement(consulta.replace("GE.*, campus.descripcion as nombreCampus", "COUNT(GE.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
			errorLog += consulta;
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			consulta = consulta.replace("[ORDERBY]", orderby)
//			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorLog += " " + consulta
			errorLog += " consulta= "
			errorLog += consulta
			errorLog += " where = " + where
			pstm = con.prepareStatement(consulta)
//			pstm.setInt(1, 9999)
//			pstm.setInt(2, 0)
			
			errorLog += "fecha=="
//			throw new Exception("el valor de consulta es = "+ consulta);
			rs = pstm.executeQuery()
			while (rs.next()) {
				row = new CatGestionEscolar()
				row.setClave(rs.getString("clave"));
				row.setDescripcion(rs.getString("descripcion"));
				row.setNombre_documento(rs.getString("nombre_documento"));
				row.setFecha_creacion_date(rs.getString("fecha_creacion_date"));
//				row.setFechaCreacion(rs.getString("fecha_creacion"));
				row.setEs_opcional(rs.getBoolean("es_opcional"));
				row.setTipo_de_archivo(rs.getString("tipo_de_archivo"))
				row.setCampus_pid(rs.getLong("campus_pid"))
				row.setPersistenceId(rs.getLong("persistenceId"))
				
				rows.add(row)
			}
			
			resultado.setSuccess(true)
			resultado.setError(errorLog)
			resultado.setData(rows)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getConfiguraciones] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result getProcessDef(String processName) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> row = new HashMap<String, Object>();
		String where = "", orderby = "", errorLog = "";
		
		try {
			closeCon = validarConexionBonita();
			
			pstm = con.prepareStatement(StatementsCatalogos.GET_PROCESS_DEFINITION);
			pstm.setString(1, processName.replace("%20", " "));
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				row = new HashMap<String, Object>();
				row.put("processid", rs.getString("processid"));
				row.put("name", rs.getString("name"));
				row.put("version", rs.getString("version"));
				
				data.add(row);
			}
			
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getProcessDef] " + e.getMessage());
		} finally {
			resultado.setError_info(errorLog);
			if (con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result insertCatMediosEnteraste(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//Validaciones en el catálogo
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"Orden\" no debe ir vacío");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATMEDIOSENTERASTE);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setInt(3, Integer.valueOf(object.orden));
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("Error al insertar el registro en la tabla.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatMediosEnteraste] " + e.getMessage());
		} finally {
			if (con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result updateCatMediosEnteraste(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//Validaciones en el catálogo
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El registro no existe.");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"Orden\" no debe ir vacío");
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATMEDIOSENTERASTE);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setInt(3, Integer.valueOf(object.orden));
			pstm.setLong(4, Long.valueOf(object.persistenceid));
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("Error al actualizar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatMediosEnteraste] " + e.getMessage());
		} finally {
			if (con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result deleteCatMediosEnteraste(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//Validaciones en el catálogo
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El registro no existe.");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATMEDIOSENTERASTE);
			pstm.setLong(1, Long.valueOf(object.persistenceid));
			
			pstm.executeUpdate();
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatMediosEnteraste] " + e.getMessage());
		} finally {
			if (con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result getCatMediosEnteraste(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> row = new HashMap<String, Object>();
		String where = "WHERE IS_ELIMINADO=false", orderby = "";
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			pstm = con.prepareStatement(StatementsCatalogos.SELECT_CATMEDIOSENTERASTE.replace("[WHERE]", where).replace("[ORDERBY]", orderby));
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				row = new HashMap<String, Object>();
				row.put("clave", rs.getString("clave"));
				row.put("descripcion", rs.getString("descripcion"));
				row.put("orden", rs.getInt("orden"));
				row.put("persistenceid", rs.getString("persistenceid"));
				
				data.add(row);
			}
			
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatMediosEnteraste] " + e.getMessage());
		} finally {
			if (con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result insertCatPeriodo(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			SimpleDateFormat sdfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(new Date());
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			} else if(object.fecha_inicio.equals("") || object.fecha_inicio == null) {
				throw new Exception("El campo \"Fecha de inicio\" no debe ir vacío");
			} else if(object.fecha_fin.equals("") || object.fecha_fin == null) {
				throw new Exception("El campo \"Fecha fin\" no debe ir vacío");
			} else if(object.id.equals("") || object.id == null) {
				throw new Exception("El campo \"Id\" no debe ir vacío");
			} else if(object.year.equals("") || object.year == null) {
				throw new Exception("El campo \"Year\" no debe ir vacío");
			} else if(object.codigo.equals("") || object.codigo == null) {
				throw new Exception("El campo \"Codigo\" no debe ir vacío");
			}
			
			Date fecha_inicio = sdfEntrada.parse(object.fecha_inicio);
			Date fecha_fin = sdfEntrada.parse(object.fecha_fin);
			
			Long idCampus = 0L;
			if(object.id_campus == null) {
				idCampus = 0L;
			} else {
				idCampus = Long.valueOf(object.id_campus);
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATPERIODO);
			pstm.setString(1,  object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setString(3, fechaHoraFormateada);
			pstm.setString(4, formato.format(fecha_inicio));
			pstm.setString(5, formato.format(fecha_fin));
			pstm.setString(6, fechaHoraFormateada);
			pstm.setString(7, object.id);
			pstm.setBoolean(8, false);
			pstm.setBoolean(9, false);
			pstm.setBoolean(10, object.is_semestral);
			pstm.setBoolean(11, object.is_trimestral);
			pstm.setLong(12, idCampus);
			pstm.setString(13, object.usuario_banner);
			pstm.setLong(14, object.year);
			pstm.setLong(15, Long.parseLong(object.codigo.toString()));
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatPeriodo] " + e.getMessage());
		} finally {
			resultado.setError_info(errorLog);
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result updateCatPeriodos(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			SimpleDateFormat sdfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(new Date());
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"Descripción\" no debe ir vacío");
			} else if(object.fecha_inicio.equals("") || object.fecha_inicio == null) {
				throw new Exception("El campo \"Fecha de inicio\" no debe ir vacío");
			} else if(object.fecha_fin.equals("") || object.fecha_fin == null) {
				throw new Exception("El campo \"Fecha fin\" no debe ir vacío");
			} else if(object.id.equals("") || object.id == null) {
				throw new Exception("El campo \"Id\" no debe ir vacío");
			} else if(object.year.equals("") || object.year == null) {
				throw new Exception("El campo \"Year\" no debe ir vacío");
			} else if(object.codigo.equals("") || object.codigo == null) {
				throw new Exception("El campo \"Codigo\" no debe ir vacío");
			} else if(object.activo.equals("") || object.activo == null) {
				throw new Exception("El campo \"Activo\" no debe ir vacío");
			}
			
			Date fecha_inicio = formato.parse(object.fecha_inicio);
			Date fecha_fin = formato.parse(object.fecha_fin);
			
			Long idCampus = 0L;
			if(object.id_campus == null) {
				idCampus = 0L;
			} else {
				idCampus = Long.valueOf(object.id_campus);
			}
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATPERIODO);
			pstm.setString(1,  object.clave);
			pstm.setString(2, object.descripcion);
			pstm.setString(3, formato.format(fecha_inicio));
			pstm.setString(4, formato.format(fecha_fin));
			pstm.setString(5, object.id);
			pstm.setBoolean(6, object.is_anual);
			pstm.setBoolean(7, false);
			pstm.setBoolean(8, object.is_semestral);
			pstm.setBoolean(9, object.is_trimestral);
			pstm.setInt(10, object.year);
			pstm.setInt(11, object.codigo);
			pstm.setBoolean(12, object.activo);
			pstm.setLong(13, object.persistenceid);
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo modificar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[updateCatPeriodo] " + e.getMessage());
		} finally {
			resultado.setError_info(errorLog);
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result deleteCatPeriodos(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
	
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData)
			
			if(object.persistenceids.equals("") || object.persistenceid == null) {
				throw new Exception("El campo \"persistenceid\" no debe ir vacío" + object);
			}
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATPERIODO);
			pstm.setBoolean(1, true);
			pstm.setLong(2, object.persistenceid);
	
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo eliminar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatPais] " + e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
	
	public Result getCatPeriodos(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> row = new HashMap<String, Object>();
		String where = " WHERE is_eliminado = false", orderby = "";
		Integer total_rows = 0;
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			pstm = con.prepareStatement(StatementsCatalogos.SELECT_COUNT_CATPERIODO.replace("[WHERE]", where).replace("[ORDERBY]", orderby));
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				total_rows = rs.getInt("total_rows");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.SELECT_CATPERIODO.replace("[WHERE]", where).replace("[ORDERBY]", orderby));
			rs = pstm.executeQuery();
			
//			SimpleDateFormat sdfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			
			while(rs.next()) {
				row = new HashMap<String, Object>();
				row.put("persistenceid", rs.getLong("persistenceid"));
				row.put("clave", rs.getString("clave"));
				row.put("descripcion", rs.getString("descripcion"));
				row.put("fecha_creacion", rs.getString("fecha_creacion"));
				row.put("fecha_inicio", rs.getString("fecha_inicio"));
				row.put("fecha_fin", rs.getString("fecha_fin"));
				row.put("fecha_importacion", rs.getString("fecha_importacion"));
				row.put("id", rs.getString("id"));
				row.put("is_anual", rs.getBoolean("is_anual"));
				row.put("is_propedeutico", rs.getBoolean("is_propedeutico"));
				row.put("is_semestral", rs.getBoolean("is_semestral"));
				row.put("is_trimestral", rs.getBoolean("is_trimestral"));
				row.put("id_campus", rs.getLong("id_campus"));
				row.put("usuario_banner", rs.getString("usuario_banner"));
				row.put("year", rs.getInt("ano"));
				row.put("codigo", rs.getInt("codigo"));
				row.put("activo", rs.getBoolean("activo"));
								
				data.add(row);
			}
			
			resultado.setTotalRegistros(total_rows);
			resultado.setData(data);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[getCatPeriodos] " + e.getMessage());
		} finally {
			if (con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
	public Result insertCatManejoDocumentos(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			SimpleDateFormat sdfEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(new Date());
			
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"CAMPO\" no debe ir vacío");
			}
			
			Date fecha_inicio = sdfEntrada.parse(object.fecha_inicio);
			Date fecha_fin = sdfEntrada.parse(object.fecha_fin);
			
			Long idCampus = 0L;
			if(object.id_campus == null) {
				idCampus = 0L;
			} else {
				idCampus = Long.valueOf(object.id_campus);
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATPERIODO);
			pstm.setString(1,  object.clave);
			
			if (pstm.executeUpdate() > 0) {
				resultado.setSuccess(true);
			} else {
				throw new Exception("No se pudo insertar el registro.");
			}
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertCatPeriodo] " + e.getMessage());
		} finally {
			resultado.setError_info(errorLog);
			if (con != null) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
	
		return resultado;
	}
}
