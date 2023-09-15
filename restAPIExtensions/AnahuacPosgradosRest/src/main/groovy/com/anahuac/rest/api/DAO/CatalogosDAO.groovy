package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.SimpleDateFormat

import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.StatementsCatalogos
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.db.CatGenerico
import com.anahuac.rest.api.Entity.db.PSGRCatEstatusProceso
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
		PSGRFiltroSeguridad row = new PSGRFiltroSeguridad();
		List<PSGRFiltroSeguridad> data = new ArrayList<PSGRFiltroSeguridad>();
		String where = "", orderby = "";
	
		try {
			closeCon = validarConexion();
	
			// Hardcodea la orden de la consulta
			orderby = "";
	
			// Supongamos que StatementsCatalogos.SELECT_CATFILTROSEGURIDAD es la consulta que deseas ejecutar
			pstm = con.prepareStatement(StatementsCatalogos.SELECT_CATFILTROSEGURIDAD.replace("[WHERE]", where).replace("[ORDERBY]", orderby));
			rs = pstm.executeQuery();
	
			while (rs.next()) {
				row = new PSGRFiltroSeguridad();
				row.setPersistenceid(rs.getLong("persistenceid"));
				row.setRol(rs.getString("rol"));
				row.setServicio(rs.getString("servicio"));
				// Agrega más setters para otros campos si es necesario
	
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
	
	//	GETCATFILTROSEGURIDAD PRIMERO
	//	public Result getCatFiltroSeguridad(String jsonData) {
	//	    Result resultado = new Result();
	//	    Boolean closeCon = false;
	//	    PSGRFiltroSeguridad row = new PSGRFiltroSeguridad();
	//	    List<PSGRFiltroSeguridad> data = new ArrayList<PSGRFiltroSeguridad>();
	//	    String where = "", orderby = "";
	//
	//	    try {
	//	        closeCon = validarConexion();
	//	        def jsonSlurper = new JsonSlurper();
	//	        def object = jsonSlurper.parseText(jsonData);
	//
	//	        orderby = " ORDER BY orden ASC";
	//
	//	        pstm = con.prepareStatement(StatementsCatalogos.SELECT_CATFILTROSEGURIDAD.replace("[WHERE]", where).replace("[ORDERBY]", orderby));
	//	        rs = pstm.executeQuery();
	//
	//	        while (rs.next()) {
	//	            row = new PSGRFiltroSeguridad();
	//	            row.setPersistenceid(rs.getLong("persistenceid"));
	//	            row.setRol(rs.getString("rol"));
	//	            row.setServicio(rs.getString("servicio"));
	//	            // Agrega más setters para otros campos si es necesario
	//
	//	            data.add(row);
	//	        }
	//
	//	        resultado.setData(data);
	//	        resultado.setSuccess(true);
	//	    } catch (Exception e) {
	//	        resultado.setSuccess(false);
	//	        resultado.setError("[getCatFiltroSeguridad] " + e.getMessage());
	//	    } finally {
	//	        if (closeCon) {
	//	            new DBConnect().closeObj(con, stm, rs, pstm);
	//	        }
	//	    }
	//
	//	    return resultado;
	//	}
	
	
}
