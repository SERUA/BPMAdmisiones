package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.sql.Timestamp
import java.text.SimpleDateFormat

import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.StatementsCatalogos
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.db.CatCampusCustomFiltro
import com.anahuac.rest.api.Entity.db.CatGenerico
import com.anahuac.rest.api.Entity.db.CatPaisCustomFiltro
import com.anahuac.rest.api.Entity.db.PSGRCatEstado
import com.anahuac.rest.api.Entity.db.PSGRCatEstatusProceso
import com.anahuac.rest.api.Entity.db.PSGRCatSiNo
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
				pstm = con.prepareStatement(row)
				
				
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
				resultado.setSuccess(true)
				
				resultado.setData(rows)
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
				pstm = con.prepareStatement(row)
				
				
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
				resultado.setSuccess(true)
				
				resultado.setData(rows)
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
			pstm.setString(7, object.orden);

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
			resultado.setError("[updateCatFiltroSeguridad] " + e.getMessage())
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
		String consulta = StatementsCatalogos.GET_CATPAIS;
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
				}
			}
			errorlog += " Order by "
			switch (object.orderby) {
				case "CLAVE":
					orderby += "clave";
					break;
				case "DESCRIPCIÓN":
					orderby += "descripcion";
					break;
				case "FECHA CREACIÓN":
					orderby += "fecha_creacion";
					break;
				case "ISELIMINADO":
					orderby += "is_eliminado";
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
				case "USUARIO CREACIÓN":
					orderby += "usuario_creacion";
					break;
				case "ORDEN":
					orderby += "ORDEN";
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
			} else if(object.usuario_creacion.equals("") || object.usuario_creacion == null) {
				throw new Exception("El campo \"usuario_creacion\" no debe ir vacío");
			} 
	
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATESTADO);
			pstm.setBoolean(1, false); // is_eliminado
			pstm.setString(2, "México");
			pstm.setString(3, object.clave);
			pstm.setString(4, object.descripcion);
			pstm.setString(5, object.usuario_creacion);
			Timestamp timestampActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			String fechaHoraFormateada = formato.format(timestampActual);
			pstm.setString(6, fechaHoraFormateada);
			pstm.setInt(7, object.orden);

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
			} else if(object.usuario_creacion.equals("") || object.usuario_creacion == null) {
				throw new Exception("El campo \"usuario_creacion\" no debe ir vacío");
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
			resultado.setError("[updateCatFiltroSeguridad] " + e.getMessage())
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
		String where = ""; // Aplicar filtro por defecto para registros no eliminados
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
				}
			}

			String consulta = StatementsCatalogos.SELECT_CATESTADO.replace("[WHERE]", where).replace("[ORDERBY]", orderby);
	
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
			resultado.setError("[getCatFiltroSeguridad] " + e.getMessage());
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
			} else if(object.usuario_creacion.equals("") || object.usuario_creacion == null) {
				throw new Exception("El campo \"usuario_creacion\" no debe ir vacío");
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
			resultado.setError("[insertCatFiltroSeguridad] " + e.getMessage());
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
			resultado.setError("[deleteCatFiltroSeguridad] " + e.getMessage())
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
			resultado.setError("[updateCatFiltroSeguridad] " + e.getMessage())
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
			resultado.setError("[getCatFiltroSeguridad] " + e.getMessage());
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
			}
			
			if (object.calle == null || object.calle.isEmpty()) {
			    throw new Exception("El campo 'calle' no debe estar vacío.");
			}
			
			if (object.clave == null || object.clave.isEmpty()) {
			    throw new Exception("El campo 'clave' no debe estar vacío.");
			}
			
			if (object.codigoPostal == null || object.codigoPostal.isEmpty()) {
			    throw new Exception("El campo 'codigoPostal' no debe estar vacío.");
			}
			
			if (object.colonia == null || object.colonia.isEmpty()) {
			    throw new Exception("El campo 'colonia' no debe estar vacío.");
			}
			
			if (object.descripcion == null || object.descripcion.isEmpty()) {
			    throw new Exception("El campo 'descripcion' no debe estar vacío.");
			}
			
			if (object.email == null || object.email.isEmpty()) {
			    throw new Exception("El campo 'email' no debe estar vacío.");
			}
			
			if (object.grupoBonita == null || object.grupoBonita.isEmpty()) {
			    throw new Exception("El campo 'grupoBonita' no debe estar vacío.");
			}
			
			if (object.municipio == null || object.municipio.isEmpty()) {
			    throw new Exception("El campo 'municipio' no debe estar vacío.");
			}
			
			if (object.numeroExterior == null || object.numeroExterior.isEmpty()) {
			    throw new Exception("El campo 'numeroExterior' no debe estar vacío.");
			}
			
			if (object.numeroInterior == null || object.numeroInterior.isEmpty()) {
			    throw new Exception("El campo 'numeroInterior' no debe estar vacío.");
			}
			
			if (object.urlAvisoPrivacidad == null || object.urlAvisoPrivacidad.isEmpty()) {
			    throw new Exception("El campo 'urlAvisoPrivacidad' no debe estar vacío.");
			}
			
			if (object.urlImagen == null || object.urlImagen.isEmpty()) {
			    throw new Exception("El campo 'urlImagen' no debe estar vacío.");
			}
			
			if (object.usuarioBanner == null || object.usuarioBanner.isEmpty()) {
			    throw new Exception("El campo 'usuarioBanner' no debe estar vacío.");
			}
			
			if (object.estado_pid == null || object.estado_pid.isEmpty()) {
				throw new Exception("El campo 'estado_pid' no debe estar vacío.");
			}
			
			
			if (object.id == null || object.id.isEmpty()) {
			    throw new Exception("El campo 'id' no debe estar vacío.");
			}
	
	//			if (object.orden == null || object.orden.isEmpty()) {
	//				throw new Exception("El campo \"Orden\" no debe ir vacío.");
	//			} else if (object.clave == null || object.clave.isEmpty()) {
	//				throw new Exception("El campo \"Clave\" no debe ir vacío.");
	//			} else if (object.descripcion == null || object.descripcion.isEmpty()) {
	//				throw new Exception("El campo \"Descripción\" no debe ir vacío.");
	//			}
	
	        pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATCAMPUS);
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
			String ordenStr = object.orden; // Obtén el valor como cadena
			int orden = 0; // Valor predeterminado en caso de error
			
			try {
			    orden = Integer.parseInt(ordenStr); // Intenta convertir la cadena a un entero
			} catch (NumberFormatException e) {
			    // Maneja el error si la conversión falla
			    // Puedes lanzar una excepción, asignar un valor predeterminado o tomar alguna otra acción apropiada.
			    // Por ejemplo, puedes asignar un valor predeterminado o lanzar una excepción si el valor no es válido.
			}
			
			// Luego, puedes usar "orden" en tu consulta SQL
			pstm.setInt(13, orden);
			pstm.setString(14, object.urlAvisoPrivacidad.isEmpty() ? null : object.urlAvisoPrivacidad);
			pstm.setString(15, object.urlImagen.isEmpty() ? null : object.urlImagen);
			pstm.setString(16, object.usuarioBanner);
			pstm.setLong(17, Long.parseLong(object.estado_pid));
			pstm.setLong(18, 1);
			pstm.setString(19, object.id);
			pstm.setBoolean(20, true);
			pstm.setBoolean(21, false);
			
	
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
	
	public Result updateCatCampus(String jsonData, RestAPIContext context) {
	    Result resultado = new Result();
	    Boolean closeCon = false;
	
	    try {
	        closeCon = validarConexion();
	        def jsonSlurper = new JsonSlurper()
			def jsonObject = jsonSlurper.parseText(jsonData)
			
			// Accede al primer elemento del arreglo lstCatCampusInput (suponiendo que haya solo uno)
			def object = jsonObject.lstCatCampusInput[0]

			if(object.equals("") || object == null) {
				throw new Exception("El campo \"object\" no debe ir vacío" + object);
			}
		
			if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"clave\" no debe ir vacío" + object);
			} else if(object.descripcion.equals("") || object.descripcion == null) {
				throw new Exception("El campo \"descripcion\" no debe ir vacío");
			} else if(object.orden.equals("") || object.orden == null) {
				throw new Exception("El campo \"orden\" no debe ir vacío");
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
			pstm.setInt(13, object.orden);
			pstm.setString(14, object.urlAvisoPrivacidad.isEmpty() ? null : object.urlAvisoPrivacidad);
			pstm.setString(15, object.urlImagen.isEmpty() ? null : object.urlImagen);
			pstm.setString(16, object.usuarioBanner);
			pstm.setLong(17, Long.parseLong(object.estado_pid));
			pstm.setLong(18, Long.parseLong(object.pais_pid));
			pstm.setString(19, object.id); // ID es de tipo VARCHAR_IGNORECASE, por lo que puedes usar setString
			pstm.setLong(20, object.persistenceId);
	
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
	
	public Result getCatCampus(String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "WHERE c.ELIMINADO=false", orderby = "ORDER BY ", errorLog = "";
        String consulta = ("SELECT  c.*, p.descripcion as pais, e.clave as cEstado, e.descripcion as dEstado FROM PSGRCATCAMPUS c left join PSGRCATPAIS p ON c.PAIS_PID  = p.PERSISTENCEID  left join PSGRCATESTADOS e ON  e.PERSISTENCEID  = c.ESTADO_PID  [WHERE] [ORDERBY] [LIMITOFFSET]")
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
                        where += " LOWER(PAIS) ";
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
                        where += " LOWER(ESTADO) ";
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
                case "DESCRIPCIÓN":
                    orderby += "descripción";
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
                case "NÚMERO INTERIOR":
                    orderby += "NUMERO_INT";
                    break;
                case "NÚMERO EXTERIOR":
                    orderby += "NUMERO_EXT";
                    break;
                case "CÓDIGO POSTAL":
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
                case "PAÍS":
                    orderby += "PAIS";
                    break;
                case "ESTADO":
                    orderby += "ESTADO";
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
                row.setPais_pid(rs.getString("pais_pid"))
                row.setEstado_pid(rs.getString("estado_pid"))
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
	
	public Result deleteCatSiNo(String jsonData) {
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
			
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CATSINO);
			pstm.setLong(1, Long.valueOf(object.persistenceid));
			
			pstm.executeUpdate();
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[deleteCatSiNo] " + e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado;
	}
	
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
			} else if(object.id_campus.equals("") || object.id_campus == null) {
				throw new Exception("No campus seleccionado");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.INSERT_CONFIGURACIONES);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.valor);
			pstm.setLong(3, Long.valueOf(object.id_campus));
			
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
			if(object.persistenceid.equals("") || object.persistenceid == null) {
				throw new Exception("El registro no existe.");
			} else if(object.clave.equals("") || object.clave == null) {
				throw new Exception("El campo \"Clave\" no debe ir vacío");
			} else if(object.valor.equals("") || object.valor == null) {
				throw new Exception("El campo \"Valor\" no debe ir vacío");
			}
			
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CONFIGURACIONES);
			pstm.setString(1, object.clave);
			pstm.setString(2, object.valor);
			pstm.setLong(3, Long.valueOf(object.persistenceid));
			
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
	
	public Result getConfiguraciones(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> row = new HashMap<String, Object>();
		String where = "", orderby = "";
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			pstm = con.prepareStatement(StatementsCatalogos.SELECT_CONFIGURACIONES.replace("[WHERE]", where).replace("[ORDERBY]", orderby));
			pstm.setLong(1, Long.valueOf(object.id_campus));
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				row = new HashMap<String, Object>();
				row.put("clave", rs.getString("clave"));
				row.put("valor", rs.getString("valor"));
				row.put("id_campus", rs.getString("id_campus"));
				row.put("persistenceid", rs.getString("persistenceid"));
				
				data.add(row);
			}
			
			resultado.setData(data);
			resultado.setSuccess(true);
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
}
