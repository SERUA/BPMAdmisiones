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
	public Result simpleSelect(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = validarConexion();
		
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
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result simpleSelectBonita(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = validarConexion();
		
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
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Result getCatGenerico(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = validarConexion();
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
				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
				row.setUsuarioCreacion(rs.getString("usuariocreacion"));

				rows.add(row)
			}
			errorLog +=" 6";
			//resultado.setError_info(errorLog);
			resultado.setSuccess(true)
			resultado.setData(rows)

		} catch (Exception e) {
			resultado.setError_info(errorLog);
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
		Boolean closeCon = validarConexion();
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
	
	public Result insertUpdateCatTipoMoneda(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = validarConexion();
		
		def jsonSlurper = new JsonSlurper();
		def objCatGenerico = jsonSlurper.parseText(jsonData);

		String errorLog = "Entro";
		try {
			errorLog+= " 1";
			 closeCon = validarConexion();
					 if(objCatGenerico.persistenceId != 0) {
						 errorLog+= " update";
						pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATTIPOMONEDA);
						pstm.setString(1, objCatGenerico.clave);
						pstm.setString(2, objCatGenerico.descripcion);
						pstm.setBoolean(3, objCatGenerico.isEliminado);
						pstm.setString(4, objCatGenerico.usuarioCreacion); 
						pstm.setLong(5, objCatGenerico.persistenceId);
						pstm.execute();
					}else {
						errorLog+= " insert";
						pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATTIPOMONEDA);
						pstm.setString(1, objCatGenerico.clave);
						pstm.setString(2, objCatGenerico.descripcion);
						pstm.setString(3, objCatGenerico.usuarioCreacion);
						pstm.execute();
					}
					errorLog+= " salio";
					con.commit();
				resultado.setSuccess(true)
				resultado.setError_info(errorLog);
			} catch (Exception e) {
				LOGGER.error "[ERROR] " + e.getMessage();
				resultado.setSuccess(false);
				resultado.setError("[insertarCatTipoMoneda] " + e.getMessage());
				resultado.setError_info(errorLog);
			} finally {
				if(closeCon) {
					new DBConnect().closeObj(con, stm, rs, pstm)
				}
			}
			return resultado;
		}
		
		public Result insertUpdateCatProvienenIngresos(String jsonData, RestAPIContext context) {
			Result resultado = new Result();
			Boolean closeCon = validarConexion();
			
			def jsonSlurper = new JsonSlurper();
			def objCatGenerico = jsonSlurper.parseText(jsonData);
	
			String errorLog = "Entro";
			try {
				errorLog+= " 1";
				 closeCon = validarConexion();
						 if(objCatGenerico.persistenceId != 0) {
							 errorLog+= " update";
							pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CATPROVIENENINGRESOS);
							pstm.setString(1, objCatGenerico.clave);
							pstm.setString(2, objCatGenerico.descripcion);
							pstm.setBoolean(3, objCatGenerico.isEliminado);
							pstm.setString(4, objCatGenerico.usuarioCreacion);
							pstm.setLong(5, objCatGenerico.persistenceId);
							pstm.execute();
						}else {
							errorLog+= " insert";
							pstm = con.prepareStatement(StatementsCatalogos.INSERT_CATPROVIENENINGRESOS);
							pstm.setString(1, objCatGenerico.clave);
							pstm.setString(2, objCatGenerico.descripcion);
							pstm.setString(3, objCatGenerico.usuarioCreacion);
							pstm.execute();
						}
						errorLog+= " salio";
						con.commit();
					resultado.setSuccess(true)
					resultado.setError_info(errorLog);
				} catch (Exception e) {
					LOGGER.error "[ERROR] " + e.getMessage();
					resultado.setSuccess(false);
					resultado.setError("[insertarCatTipoMoneda] " + e.getMessage());
					resultado.setError_info(errorLog);
				} finally {
					if(closeCon) {
						new DBConnect().closeObj(con, stm, rs, pstm)
					}
				}
				return resultado;
			}
			
			public Result insertUpdateCatGenerico(String jsonData, RestAPIContext context) {
				Result resultado = new Result();
				Boolean closeCon = validarConexion();
				
				def jsonSlurper = new JsonSlurper();
				def objeto = jsonSlurper.parseText(jsonData);
		
				//assert objCatGenerico instanceof CatGenerico;
				String errorLog = "Entro", consulta = "";
				//CatGenerico objCatGenerico;
				try {
					errorLog+= " 1";
					 closeCon = validarConexion();
							 if(objeto.objCatGenerico.persistenceId != 0) {
								 consulta= StatementsCatalogos.UPDATE_CAT_GENERICO
								 errorLog = consulta;
								 consulta=consulta.replaceAll("CATGEN", objeto.catalogo)
								 errorLog += "|| "+consulta;
								pstm = con.prepareStatement(consulta);
								pstm.setString(1, objeto.objCatGenerico.clave);
								pstm.setString(2, objeto.objCatGenerico.descripcion);
								pstm.setBoolean(3, objeto.objCatGenerico.isEliminado);
								pstm.setString(4, objeto.objCatGenerico.usuarioCreacion);
								pstm.setLong(5, objeto.objCatGenerico.persistenceId);
								pstm.execute();
							}else {
								consulta= StatementsCatalogos.INSERT_CAT_GENERICO
								errorLog = consulta;
								consulta = consulta.replaceAll("CATGEN", objeto.catalogo)
								errorLog += "|| "+consulta;
								pstm = con.prepareStatement(consulta);
								pstm.setString(1, objeto.objCatGenerico.clave);
								pstm.setString(2, objeto.objCatGenerico.descripcion);
								pstm.setString(3, objeto.objCatGenerico.usuarioCreacion);
								pstm.execute();
							}
							con.commit();
						resultado.setSuccess(true)
						//resultado.setError_info(errorLog);
					} catch (Exception e) {
						LOGGER.error "[ERROR] " + e.getMessage();
						resultado.setSuccess(false);
						resultado.setError("[insertUpdateCatGenerico] " + e.getMessage());
						resultado.setError_info(errorLog);
					} finally {
						if(closeCon) {
							new DBConnect().closeObj(con, stm, rs, pstm)
						}
					}
					return resultado;
				}
}
