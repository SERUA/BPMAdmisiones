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

import com.anahuac.catalogos.CatCampus
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.StatementsCatalogos
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.db.CatConfiguracionPagoEstudioSocEco
import com.anahuac.rest.api.Entity.db.CatGenerico
import com.anahuac.rest.api.Entity.db.CatImagenesSocioEconomico
import com.anahuac.rest.api.Entity.db.CatManejoDocumentos
import com.anahuac.rest.api.Entity.db.CatTypoApoyo
import com.anahuac.rest.api.Entity.db.ConfiguracionesCampus
import com.anahuac.rest.api.Entity.db.DocumentosSolicitante
import com.anahuac.rest.api.Entity.db.ImagesSocEcoSolicitante

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
	
	public Result simpleSelectBonita(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
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
	
	public Result getCatTienesHijos(String catalogo) {
		Result resultado = new Result();
		Boolean closeCon = false;
		CatGenerico objCatGenerico = new CatGenerico();
		List<CatGenerico> lstCatGenerico= new ArrayList<CatGenerico>();
		
		try {
			closeCon = validarConexion();
			String consulta = StatementsCatalogos.GET_CAT_TIENES_HIJOS;
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
				objCatGenerico.setIsEliminado(rs.getBoolean("ISELIMINADOBOOL"));
//				objCatGenerico.setPersistenceVersion(rs.getLong("PERSISTENCEVERSION"));
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
		Boolean closeCon = false;
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
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError("[insertarCatTipoMoneda] " + e.getMessage());
			
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}

	public Result insertManejoDocumento(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		def jsonSlurper = new JsonSlurper();
		def objCatGenerico = jsonSlurper.parseText(jsonData);

		String errorLog = "Entro";
		try {
			errorLog+= " 1";
			closeCon = validarConexion();
			
			errorLog += objCatGenerico.toString();
			if(objCatGenerico.persistenceId != 0) {
				errorLog+= " update";
				pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CAT_MANEJO_DOCUMENTOS);
				pstm.setLong(1, objCatGenerico.idCampus);
				pstm.setLong(2, objCatGenerico.idTipoApoyo);
				pstm.setBoolean(3, objCatGenerico.isObligatorio);
				pstm.setString(4, objCatGenerico.nombreDocumento);
				pstm.setString(5, objCatGenerico.urlDocumentoAzure);
				pstm.setString(6, objCatGenerico.descripcionDocumento);
				pstm.setBoolean(7, objCatGenerico.requiereEjemplo);
				pstm.setBoolean(8, Boolean.valueOf(objCatGenerico.isAval) ? true : false);
				pstm.setLong(9, objCatGenerico.persistenceId);
				pstm.execute();
			}else {
				errorLog+= " insert";
				pstm = con.prepareStatement(StatementsCatalogos.INSERT_CAT_MANEJO_DOCUMENTOS);
				pstm.setLong(1, objCatGenerico.idCampus);
				pstm.setLong(2, objCatGenerico.idTipoApoyo);
				pstm.setBoolean(3, objCatGenerico.isObligatorio);
				pstm.setString(4, objCatGenerico.nombreDocumento);
				pstm.setString(5, objCatGenerico.descripcionDocumento);
				pstm.setString(6, objCatGenerico.urlDocumentoAzure);
				pstm.setString(7, objCatGenerico.usuarioCreacion);
				pstm.setBoolean(8, Boolean.valueOf(objCatGenerico.requiereEjemplo) ? true : false);
				pstm.setBoolean(9, Boolean.valueOf(objCatGenerico.isAval) ? true : false);
				pstm.execute();
			}
			errorLog+= " salio";
//			con.commit();
			resultado.setSuccess(true)
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError("[insertManejoDocumento] " + e.getMessage());
			
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result insertUpdateCatProvienenIngresos(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
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
				
			} catch (Exception e) {
				LOGGER.error "[ERROR] " + e.getMessage();
				resultado.setSuccess(false);
				resultado.setError("[insertarCatTipoMoneda] " + e.getMessage());
				
			} finally {
				if(closeCon) {
					new DBConnect().closeObj(con, stm, rs, pstm)
				}
			}
			return resultado;
	}
			
	public Result insertUpdateCatGenerico(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
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
//			con.commit();
			resultado.setSuccess(true)
			//
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError("[insertUpdateCatGenerico] " + e.getMessage());
			
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	/**
	 * Obtiene la lista de registros del catálogo de manejo de documentos para cada tipo de apoyo educativo y campus 
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result getCatManejoDocumento(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", orderby = "ORDER BY ", errorLog="entro";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			String consulta = StatementsCatalogos.GET_CAT_MANEJO_DOCUMENTOS;
			CatManejoDocumentos row = new CatManejoDocumentos();
			List < CatManejoDocumentos > rows = new ArrayList < CatManejoDocumentos > ();
			closeCon = validarConexion();
			where += " WHERE isEliminado = false  AND IDCAMPUS = " + object.idCampus + " AND IDTIPOAPOYO = " + object.idTipoApoyo + " ";
			errorLog +=" 1";
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {

				switch (filtro.get("columna")) {
					case "CLAVE":
						where += " AND LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}

						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "DESCRIPCION":
						where += " AND LOWER(DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "FECHACREACION":
						where += " AND LOWER(FECHACREACION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "PERSISTENCEID":
						where += " AND LOWER(PERSISTENCEID) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "PERSISTENCEVERSION":
						where += " AND LOWER(PERSISTENCEVERSION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "USUARIOCREACION":
						where += " AND LOWER(USUARIOCREACION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += " =LOWER('[valor]')";
						} else {
							where += " LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"));
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
					orderby += "persistenceid";
					break;
			}
			
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);
			consulta = consulta.replace("[CATALOGO]", object.catalogo);

			String consultaCount = StatementsCatalogos.GET_COUNT_CAT_MANEJO_DOCUMENTOS;
			consultaCount = consultaCount.replace("[WHERE]", where);
			consultaCount = consultaCount.replace("[CATALOGO]", object.catalogo);
			
			pstm = con.prepareStatement(consultaCount);
			rs = pstm.executeQuery();
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"));
			}
			
			consulta = consulta.replace("[ORDERBY]", orderby);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			
			errorLog = consulta;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);

			rs = pstm.executeQuery()
			while (rs.next()) {
				row = new CatManejoDocumentos();
				row.setPersistenceId(rs.getLong("PERSISTENCEID"));
				row.setPersistenceId_string(String.valueOf(row.getPersistenceId()));
				row.setIdCampus(rs.getLong("IDCAMPUS"));
				row.setIdTipoApoyo(rs.getLong("IDTIPOAPOYO"));
				row.setNombreDocumento(rs.getString("NOMBREDOCUMENTO"));
				row.setUrlDocumentoAzure(rs.getString("URLDOCUMENTOAZURE"));
				row.setFechaCreacion(rs.getString("fechaCreacion"));
				row.setIsEliminado(rs.getBoolean("isEliminado"));
				row.setUsuarioCreacion(rs.getString("usuariocreacion"));
				row.setIsObligatorio(rs.getBoolean("ISOBLIGATORIO"));
				row.setRequiereEjemplo(rs.getBoolean("requiereEjemplo"));

				rows.add(row);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);

		} catch (Exception e) {
			
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	/**
	 * Obtiene la lista de registros del catálogo detipo de apoyo 
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result getCatTipoAoyoByCampus(String campus, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", orderby = "ORDER BY ", errorLog="entro";
		
		try {
			String consulta = StatementsCatalogos.GET_TIPO_APOYO_BY_CAMPUS;
			CatTypoApoyo row = new CatTypoApoyo();
			List < CatTypoApoyo > rows = new ArrayList < CatTypoApoyo > ();
			closeCon = validarConexion();
			where += " WHERE ta.ISELIMINADO  = false AND cc.GRUPOBONITA = '" + campus + "' ";

			consulta = consulta.replace("[WHERE]", where);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			pstm = con.prepareStatement(consulta);
			
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new CatTypoApoyo();
				row.setPersistenceId(rs.getLong("PERSISTENCEID"));
				row.setPersistenceId_string(String.valueOf(row.getPersistenceId()));
				row.setDescripcion(rs.getString("DESCRIPCION"));
				row.setRequiereVideo(rs.getBoolean("REQUIEREVIDEO"));
				row.setCondicionesVideo(rs.getString("CONICIONESVIDEO"));
				row.setEsSocioEconomico(rs.getBoolean("ESSOCIOECONOMICO"));
				row.setIsArtistica(rs.getBoolean("ISARTISTICA"));
				row.setIsDeportiva(rs.getBoolean("ISDEPORTIVA"));
				row.setIsFinanciamiento(rs.getBoolean("ISFINANCIAMIENTO"));
				row.setIsAcademica(rs.getBoolean("ISACADEMICA"));
				row.setUrlEjemploCurriculum(rs.getString("URLEJEMPLOCURRICULUM"));
				row.setRequiereCurriculum(rs.getBoolean("REQUIERECURRICULUM"));
				row.setPromedioMinimo(rs.getDouble("PROMEDIOMINIMO"));
				row.setRequierePromedioMinimo(rs.getBoolean("REQUIEREPROMEDIOMINIMO"));
				
				rows.add(row);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	
	/**
	 * Obtiene el primedio nimimo mas pequeño de los tipos de apoyo configurados en los campus
	 * @author José Carlos García Romero
	 * @param idCampus (Long)
	 * @return resultado (Result)
	 */
	public Result getPromedioMinimoApoyoByCampus(Long idCampus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			String consulta = StatementsCatalogos.GET_PROMEDIO_MINIMO_BY_CAMPUS;
			List < Double > rows = new ArrayList < Double > ();
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, idCampus);
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				rows.add(rs.getDouble("promediominimo"));
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	/**
	 * Obtiene la lista de registros del catálogo detipo de apoyo
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result getCatTipoAoyo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "", orderby = "ORDER BY ", errorLog="entro";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			String consulta = StatementsCatalogos.GET_CAT_TIPO_APOYO;
			CatTypoApoyo row = new CatTypoApoyo();
			List < CatTypoApoyo > rows = new ArrayList < CatTypoApoyo > ();
			closeCon = validarConexion();
			where += " WHERE isEliminado = false";
			errorLog +=" 1";
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {

				switch (filtro.get("columna")) {
					case "CLAVE":
						where += " AND LOWER(clave) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "DESCRIPCION":
						where += " AND LOWER(DESCRIPCION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "FECHACREACION":
						where += " AND LOWER(FECHACREACION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "PERSISTENCEID":
						where += " AND LOWER(PERSISTENCEID) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "PERSISTENCEVERSION":
						where += " AND LOWER(PERSISTENCEVERSION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"));
						break;
					case "USUARIOCREACION":
						where += " AND LOWER(USUARIOCREACION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"));
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
					orderby += "persistenceid";
					break;
			}
			errorLog +=" 3";
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);
			consulta = consulta.replace("[CATALOGO]", object.catalogo);

			String consultaCount = StatementsCatalogos.GET_COUNT_CATGENERICO;
			consultaCount = consultaCount.replace("[WHERE]", where);
			consultaCount = consultaCount.replace("[CATALOGO]", object.catalogo);
			errorLog +=" 4";
			pstm = con.prepareStatement(consultaCount);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"));
			}
			
			errorLog +=" 4";
			consulta = consulta.replace("[ORDERBY]", orderby);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);

			errorLog +=" 5";
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new CatTypoApoyo();
				row.setClave(rs.getString("clave"))
				row.setDescripcion(rs.getString("descripcion"));
				row.setFechaCreacion(rs.getString("fechacreacion"));
				row.setIsEliminado(rs.getBoolean("isEliminado"));
				row.setPersistenceId(rs.getLong("PERSISTENCEID"));
				row.setPersistenceId_string(String.valueOf(row.getPersistenceId()));
				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
				row.setUsuarioCreacion(rs.getString("usuariocreacion"));
				row.setIsArtistica(rs.getBoolean("ISARTISTICA"));
				row.setIsDeportiva(rs.getBoolean("ISDEPORTIVA"));
				row.setIsFinanciamiento(rs.getBoolean("ISFINANCIAMIENTO"));
				row.setIsAcademica(rs.getBoolean("ISACADEMICA"));

				rows.add(row);
			}
			errorLog +=" 6";
			//
			resultado.setSuccess(true);
			resultado.setData(rows);

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
	
	/**
	 * Obtiene la lista de registros del catálogo detipo de apoyo
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result getCatTienesHijos() {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "", orderby = "ORDER BY ", errorLog="entro";
		try {
			def jsonSlurper = new JsonSlurper();

			String consulta = StatementsCatalogos.GET_CAT_TIENES_HIJOS;
			CatGenerico row = new CatGenerico();
			List < CatGenerico > rows = new ArrayList < CatGenerico > ();
			closeCon = validarConexion();

			pstm = con.prepareStatement(consulta);

			errorLog +=" 5";
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new CatGenerico();
				row.setClave(rs.getString("clave"))
				row.setDescripcion(rs.getString("descripcion"));
				row.setFechaCreacion(rs.getString("fechacreacion"));
				row.setIsEliminado(rs.getBoolean("isEliminadoBool"));
				row.setPersistenceId(rs.getLong("PERSISTENCEID"));
				row.setPersistenceId_string(String.valueOf(row.getPersistenceId()));
				row.setUsuarioCreacion(rs.getString("usuariocreacion"));

				rows.add(row);
			}
			
			errorLog +=" 6";
			//
			resultado.setSuccess(true);
			resultado.setData(rows);

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
	
	/**
	 * Obtiene la lista de campus relacionados a cierto tipo de apoyo
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result getCampusByTipoApoyo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", orderby = "ORDER BY ", errorLog="entro";
		
		try {
			
			String consulta = StatementsCatalogos.GET_CAMPUS_BY_TIPO_APOYO;
			CatCampus row = new CatCampus();
			List < CatCampus > rows = new ArrayList < CatCampus > ();
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			
			pstm.setLong(1, object.persistenceId);
			
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new CatCampus();
				row.setDescripcion(rs.getString("descripcion"));
				row.setPersistenceId(rs.getLong("persistenceid"));

				rows.add(row);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
			

		} catch (Exception e) {
			
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	/**
	 * Inserta o elimina la relacion de campus con tipo apoyo
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result switchCampusTipoApoyo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		
		try {
			String consulta = StatementsCatalogos.GET_CAMPUS_TIPO_APOYO_EXIST;
			String mensajeError = "";
			Boolean isError = false;
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			
			pstm.setLong(1, object.idCampus);
			pstm.setLong(2, object.idTipoApoyo);
			
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				if(object.isDelete) {
					pstm = con.prepareStatement(StatementsCatalogos.DELETE_CAMPUS_TIPO_APOYO_EXIST);
				} else {
					throw new Exception("El registro ya existe");
				}
				
			} else {
				if(!object.isDelete) {
					pstm = con.prepareStatement(StatementsCatalogos.INSERT_CAMPUS_TIPO_APOYO_EXIST);
				} else {
					throw new Exception("No se puede eliminar un registro que no existe");
				}
			}
			
			pstm.setLong(1, object.idCampus);
			pstm.setLong(2, object.idTipoApoyo);
			
			pstm.executeUpdate();
			
			
			resultado.setSuccess(true);
			

		} catch (Exception e) {
			
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	/**
	 * Obtiene la lista de registros de documentos rrelacionados a un tipo de apoyo
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result getDocumentosByTipoApoyo(String campus, String idTipoApoyo, Boolean isAval, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", orderby = "ORDER BY ", errorLog="entro";
		
		try {
			errorLog += "ENTRO ";
			String consulta = StatementsCatalogos.GET_COCUMENTOS_BY_APOYO_AND_CAMPUS;
			CatManejoDocumentos row = new CatManejoDocumentos();
			List < CatManejoDocumentos > rows = new ArrayList < CatManejoDocumentos > ();
			closeCon = validarConexion();
			where += " WHERE doc.ISELIMINADO <> TRUE AND  doc.idtipoapoyo = ? AND cc.GRUPOBONITA = ? ";
			isAval == true ? (where += " AND doc.isaval = true" ) : (where += "AND (doc.isaval is null OR doc.isaval = false)");
			consulta = consulta.replace("[WHERE]", where);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, Long.valueOf(idTipoApoyo));
			pstm.setString(2, campus);
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new CatManejoDocumentos();
				row.setPersistenceId(rs.getLong("PERSISTENCEID"));
				row.setPersistenceId_string(String.valueOf(row.getPersistenceId()));
				row.setNombreDocumento(rs.getString("NOMBREDOCUMENTO"));
				row.setDescripcionDocumento(rs.getString("DESCRIPCIONDOCUMENTO"));
				row.setUrlDocumentoAzure(rs.getString("URLDOCUMENTOAZURE"));
				row.setIsObligatorio(rs.getBoolean("ISOBLIGATORIODOC").toString());
				row.setIdCampus(rs.getLong("IDCAMPUS"));
				row.setIdTipoApoyo(rs.getLong("idtipoapoyo"));
				row.setIsObligatorioDoc(rs.getBoolean("ISOBLIGATORIODOC"));
				row.setRequiereEjemplo(rs.getBoolean("requiereEjemplo"));		
				row.setIsAval(rs.getBoolean("isAval"));
				
				rows.add(row);
			}
			
//			resultado.setError_info(errorLog);
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			errorLog += e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	/**
	 * Actualiza las variables condicionesVideo, requiereVideo y isSocioEconomico de un tipo de video
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result updateTipoApoyoVideo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		def jsonSlurper = new JsonSlurper();
		def objCatGenerico = jsonSlurper.parseText(jsonData);

		String errorLog = "";
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CAT_TIPO_APOYO_VIDEO);
//			REQUIEREVIDEO = ?, CONICIONESVIDEO = ?, ESSOCIOECONOMICO = ?, requiereCurriculum = ?, 
//			urlEjemploCurriculum = ?, promedioMinimo = ?, requierePromedioMinimo = ?
			pstm.setBoolean(1, objCatGenerico.requiereVideo);
			pstm.setString(2, objCatGenerico.condicionesVideo);
			pstm.setBoolean(3, objCatGenerico.esSocioEconomico);
			pstm.setBoolean(4, objCatGenerico.requiereCurriculum);
			pstm.setString(5, objCatGenerico.urlEjemploCurriculum == null ? "" : objCatGenerico.urlEjemploCurriculum);
//			pstm.setLong(6, objCatGenerico.idCampus);
			pstm.setDouble(6, objCatGenerico.promedioMinimo  == null ? 0.0 : objCatGenerico.promedioMinimo);
			pstm.setBoolean(7, objCatGenerico.requierePromedioMinimo  == null ? false : objCatGenerico.requierePromedioMinimo);
			pstm.setLong(8, objCatGenerico.idCampus);
			pstm.setLong(9, objCatGenerico.persistenceId);
			pstm.execute();
			resultado.setSuccess(true)
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError("[insertManejoDocumento] " + e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result deleteCatManejoDocumentos(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		def jsonSlurper = new JsonSlurper();
		def objCatGenerico = jsonSlurper.parseText(jsonData);

		String errorLog = "";
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CAT_MANEJO_DOCUMENTOS);
			pstm.setLong(1, objCatGenerico.persistenceId);
			pstm.execute();
			errorLog += (" :: " + StatementsCatalogos.DELETE_CAT_MANEJO_DOCUMENTOS + "id::" + objCatGenerico.persistenceId + " :: ");
			resultado.setError_info(errorLog);
			resultado.setSuccess(true);
		} catch (Exception e) {
			errorLog += e.getMessage();
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setError_info(errorLog);
			resultado.setSuccess(false);
			resultado.setError("[insertManejoDocumento] " + e.getMessage());
			
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result insertUpdateCatTipoApoyo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		def jsonSlurper = new JsonSlurper();
		def objCatGenerico = jsonSlurper.parseText(jsonData);

		String errorLog = "Entro";
		try {
			errorLog+= " 1";
			closeCon = validarConexion();
			
			errorLog += objCatGenerico.toString();
			closeCon = validarConexion();
			if(objCatGenerico.persistenceId != 0) {
				errorLog+= " update";
				pstm = con.prepareStatement(StatementsCatalogos.UPDATE_CAT_TIPO_APOYO);
				pstm.setString(1, objCatGenerico.clave.toString());
				pstm.setString(2, objCatGenerico.descripcion);
				pstm.setBoolean(3, objCatGenerico.isEliminado);
				pstm.setBoolean(4, objCatGenerico.isArtistica);
				pstm.setBoolean(5, objCatGenerico.isDeportiva);
				pstm.setBoolean(6, objCatGenerico.isAcademica);
				pstm.setBoolean(7, objCatGenerico.isFinanciamiento);
				pstm.setLong(8, objCatGenerico.persistenceId);
				pstm.execute();
			}else {
				errorLog+= " insert";
				pstm = con.prepareStatement(StatementsCatalogos.INSERT_CAT_TIPO_APOYO);
				pstm.setString(1, objCatGenerico.clave.toString());
				pstm.setString(2, objCatGenerico.descripcion);
				pstm.setString(3, objCatGenerico.usuarioCreacion);
				pstm.setBoolean(4, objCatGenerico.isArtistica);
				pstm.setBoolean(5, objCatGenerico.isDeportiva);
				pstm.setBoolean(6, objCatGenerico.isAcademica);
				pstm.setBoolean(7, objCatGenerico.isFinanciamiento);
				pstm.execute();
			}
			errorLog+= " salio";
//			con.commit();
			resultado.setSuccess(true)
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError("[insertManejoDocumento] " + e.getMessage());
			
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result deleteCatTipoApoyo(String persistenceid, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "Entro";
		
		try {
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_CAT_TIPO_APOYO);

			pstm.setLong(1, Long.valueOf(persistenceid));
			pstm.execute();
			resultado.setSuccess(true);
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError("[insertManejoDocumento] " + e.getMessage());
			
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	/**
	 * Obtiene la lista de campus relacionados a cierto tipo de apoyo
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result getImagenesByTipoApoyo(String campus, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", orderby = "ORDER BY ", errorLog="entro";
		
		try {
			String consulta = StatementsCatalogos.GET_IMAGENES_BY_TIPO_APOYO;
			CatImagenesSocioEconomico row = new CatImagenesSocioEconomico();
			List < CatImagenesSocioEconomico > rows = new ArrayList < CatImagenesSocioEconomico > ();
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			
			pstm.setString(1, campus);
			pstm.setLong(2, object.idTipoApoyo);
			
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new CatImagenesSocioEconomico();
				row.setDescripcion(rs.getString("descripcion"));
				row.setPersistenceId(rs.getLong("persistenceid"));

				rows.add(row);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
			

		} catch (Exception e) {
			
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	public Result insertImagenSocioEconomico(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		def jsonSlurper = new JsonSlurper();
		def objCatGenerico = jsonSlurper.parseText(jsonData);

		String errorLog = "Entro";
		try {
			errorLog+= " 1";
			closeCon = validarConexion();
			
			errorLog += objCatGenerico.toString();
			closeCon = validarConexion();
			if(objCatGenerico.persistenceId != 0) {
				errorLog+= " update";
				pstm = con.prepareStatement(StatementsCatalogos.UPDATE_IMAGEN_SOCIO_ECONOMICO);

				pstm.setString(1, objCatGenerico.descripcion);
				pstm.setLong(2, objCatGenerico.persistenceId);
				
				pstm.execute();
			}else {
				errorLog+= " insert";
				
				pstm = con.prepareStatement(StatementsCatalogos.INSERT_IMAGEN_SOCIO_ECONOMICO);
				pstm.setString(1, objCatGenerico.descripcion);
				pstm.setString(2, objCatGenerico.usuarioCreacion);
				pstm.setLong(3, objCatGenerico.idCampus);
				pstm.setLong(4, objCatGenerico.idTipoApoyo);
				
				pstm.execute();
			}
			errorLog+= " salio";
//			con.commit();
			resultado.setSuccess(true)
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError("[insertManejoDocumento] " + e.getMessage());
			
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result deleteImagenSocioEconomico(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		def jsonSlurper = new JsonSlurper();
		def objCatGenerico = jsonSlurper.parseText(jsonData);
		String errorLog = "Entro";
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(StatementsCatalogos.DELETE_IMAGEN_SOCIO_ECONOMICO);
			pstm.setBoolean(1, true);
			pstm.setLong(2, objCatGenerico.persistenceId);	
			pstm.execute();
			
			resultado.setSuccess(true)
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError("[insertManejoDocumento] " + e.getMessage());
			
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	/**
	 * Inserta o elimina la relacion de campus con tipo apoyo
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result switchTipoApoyoImagen(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		
		try {
			String consulta = StatementsCatalogos.GET_CAMPUS_TIPO_APOYO_EXIST;
			String mensajeError = "";
			Boolean isError = false;
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			
			pstm.setLong(1, object.idCampus);
			pstm.setLong(2, object.idTipoApoyo);
			
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				if(object.isDelete) {
					pstm = con.prepareStatement(StatementsCatalogos.DELETE_CAMPUS_TIPO_APOYO_EXIST);
				} else {
					throw new Exception("El registro ya existe");
				}
				
			} else {
				if(!object.isDelete) {
					pstm = con.prepareStatement(StatementsCatalogos.INSERT_CAMPUS_TIPO_APOYO_EXIST);
				} else {
					throw new Exception("No se puede eliminar un registro que no existe");
				}
			}
			
			pstm.setLong(1, object.idCampus);
			pstm.setLong(2, object.idTipoApoyo);
			
			pstm.executeUpdate();
			
			
			resultado.setSuccess(true);
			

		} catch (Exception e) {
			
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	/**
	 * Obtiene la lista de registros del catálogo detipo de apoyo
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result getCatTipoAoyoByCampus(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "", orderby = "ORDER BY ", errorLog="entro";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			String consulta = StatementsCatalogos.GET_CAT_TIPO_APOYO;
			CatTypoApoyo row = new CatTypoApoyo();
			List < CatTypoApoyo > rows = new ArrayList < CatTypoApoyo > ();
			closeCon = validarConexion();
			where += " WHERE isEliminado = false";
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);
			consulta = consulta.replace("[CATALOGO]", object.catalogo);

			String consultaCount = StatementsCatalogos.GET_COUNT_CATGENERICO;
			consultaCount = consultaCount.replace("[WHERE]", where);
			consultaCount = consultaCount.replace("[CATALOGO]", object.catalogo);
			errorLog +=" 4";
			pstm = con.prepareStatement(consultaCount);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"));
			}
			
			errorLog +=" 4";
			consulta = consulta.replace("[ORDERBY]", orderby);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);

			errorLog +=" 5";
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new CatTypoApoyo();
				row.setClave(rs.getString("clave"))
				row.setDescripcion(rs.getString("descripcion"));
				row.setFechaCreacion(rs.getString("fechacreacion"));
				row.setIsEliminado(rs.getBoolean("isEliminado"));
				row.setPersistenceId(rs.getLong("PERSISTENCEID"));
				row.setPersistenceId_string(String.valueOf(row.getPersistenceId()));
				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
				row.setUsuarioCreacion(rs.getString("usuariocreacion"));
//				row.setRequiereVideo(rs.getBoolean("requierevideo"));
//				row.setCondicionesVideo(rs.getString("condicionesvideo"));
//				row.setEsSocioEconomico(rs.getBoolean("esSocioEconomico"));

				rows.add(row);
			}
			errorLog +=" 6";
			//resultado.setError_info(errorLog);
			resultado.setSuccess(true);
			resultado.setData(rows);

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
	
	/**
	 * Obtiene un registro por Id del catálogo de tipo de apoyo 
	 * @author José Angel González Zazueta 
	 * @param persistenceId (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result getCatTipoApoyoByPersistenceId(String persistenceId, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {

			CatTypoApoyo row = new CatTypoApoyo();
			List < CatTypoApoyo > rows = new ArrayList < CatTypoApoyo > ();
			closeCon = validarConexion();

			pstm = con.prepareStatement(StatementsCatalogos.GET_CAT_TIPO_APOYO_BY_ID);
			pstm.setInt(1, Integer.parseInt(persistenceId));
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new CatTypoApoyo();
				row.setClave(rs.getString("clave"))
				row.setDescripcion(rs.getString("descripcion"));
				row.setFechaCreacion(rs.getString("fechacreacion"));
				row.setIsEliminado(rs.getBoolean("isEliminado"));
				row.setPersistenceId(rs.getLong("PERSISTENCEID"));
				row.setPersistenceId_string(String.valueOf(row.getPersistenceId()));
				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
				row.setUsuarioCreacion(rs.getString("usuariocreacion"));
				row.setIsAcademica(rs.getBoolean("ISACADEMICA"));
				row.setIsArtistica(rs.getBoolean("ISARTISTICA"));
				row.setIsDeportiva(rs.getBoolean("ISDEPORTIVA"));
				row.setIsFinanciamiento(rs.getBoolean("ISFINANCIAMIENTO"));
				rows.add(row);
			}
			
			//resultado.setError_info(errorLog);
			resultado.setSuccess(true);
			resultado.setData(rows);

		} catch (Exception e) {
			resultado.setError_info(e.getMessage());
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
	
	/**
	 * Actualia el estatus de una solicitud
	 * @author José Carlos García Romero
	 * @param caseid (Long), nuevoEstatus(String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result updateEstatusSolicitudApoyo(Long caseid, String nuevoEstatus) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "", orderby = "ORDER BY ", errorLog="entro";
		try {
			def jsonSlurper = new JsonSlurper();

			String consulta = StatementsCatalogos.UPDATE_ESTATUS_SOLICITUD;
			CatGenerico row = new CatGenerico();
			List < CatGenerico > rows = new ArrayList < CatGenerico > ();
			closeCon = validarConexion();

			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, caseid);
			pstm.setString(2, nuevoEstatus);
			rs = pstm.execute();
			
			resultado.setSuccess(true);
			resultado.setData(rows);

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
	
	/**
	 * Creacion de los nuevos datos de GestionEscolar
	 * @author Jesus Angel Osuna Padilla
	 * @param jsonData(String)
	 * @return resultado (Result)
	 */
	public Result insertSDAEGestionEscolar(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "";
		String errorLog = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			errorLog += "Entro al metodo || ";
			String consulta = StatementsCatalogos.INSERT_SDAECAT_GESTION_ESCOLAR;
			closeCon = validarConexion();
			con.setAutoCommit(false)
			errorLog += "Ejecuto la consulta paso 1 || ";
			pstm = con.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
			pstm.setString(1, object.parcialidad);
			pstm.setInt(2, Integer.parseInt(object.creditosemestre.toString()));
			pstm.setBoolean(3, Boolean.parseBoolean(object.manejaapoyo.toString()));
			pstm.setLong(4, Long.parseLong(object.catgestionescolar_pid.toString()));
			pstm.setBoolean(5, Boolean.parseBoolean(object.manejaprontopago.toString()));
			pstm.executeUpdate();
			errorLog += "Ejecuto el update || ";
			con.commit();
			rs = pstm.getGeneratedKeys();
			errorLog += "Llaves generadas || ";
			List<String> info = new ArrayList<String>();
			if(rs.next()) {
				errorLog += " || ";
				info.add( rs.getString("persistenceid"))
			}
			errorLog += "Termino ejecución || ";
			resultado.setSuccess(true);
			resultado.setData(info);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			errorLog += " error || " + e.getMessage() + " || ";
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updateSDAEGestionEscolar(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			String consulta = StatementsCatalogos.UPDATE_SDAECAT_GESTION_ESCOLAR;
			closeCon = validarConexion();
			con.setAutoCommit(false)
			
			pstm = con.prepareStatement(consulta);
			pstm.setString(1, object.parcialidad);
			pstm.setInt(2, Integer.parseInt(object.creditosemestre.toString()));
			pstm.setBoolean(3, Boolean.parseBoolean(object.manejaapoyo.toString()));
			pstm.setBoolean(4, Boolean.parseBoolean(object.manejaprontopago.toString()));
			pstm.setLong(5, Long.parseLong(object.persistenceid.toString()));
			pstm.executeUpdate();
			
			con.commit();
			resultado.setSuccess(true);
		} catch (Exception e) {
			
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result insertSDAECreditoGE(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			String consulta = StatementsCatalogos.INSERT_SDAECAT_CREDITO_GE;
			closeCon = validarConexion();
			con.setAutoCommit(false)
			
			object.each{
				pstm = con.prepareStatement(consulta);
				pstm.setString(1, it.creditoenero);
				pstm.setString(2, it.creditomayo);
				pstm.setString(3, it.creditoagosto);
				pstm.setString(4, it.creditoseptiembre);
				pstm.setString(5, it.fecha);
				pstm.setLong(6, Long.parseLong(it.sdaecatgestionescolar_pid.toString()));
				pstm.executeUpdate();
			}
			
			
			con.commit();
			resultado.setSuccess(true);
		} catch (Exception e) {
			
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updateSDAECreditoGE(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			String consulta = StatementsCatalogos.UPDATE_SDAECAT_CREDITO_GE;
			String consultaInsert = StatementsCatalogos.INSERT_SDAECAT_CREDITO_GE;
			closeCon = validarConexion();
			con.setAutoCommit(false)
			object.each{
				if(it.persistenceid) {
					pstm = con.prepareStatement(consulta);
					pstm.setString(1, it.creditoenero);
					pstm.setString(2, it.creditomayo);
					pstm.setString(3, it.creditoagosto);
					pstm.setString(4, it.creditoseptiembre);
					pstm.setLong(5, Long.parseLong(it.persistenceid.toString()));
					pstm.executeUpdate();
				} else {
					pstm = con.prepareStatement(consultaInsert);
					pstm.setString(1, it.creditoenero);
					pstm.setString(2, it.creditomayo);
					pstm.setString(3, it.creditoagosto);
					pstm.setString(4, it.creditoseptiembre);
					pstm.setString(5, it.fecha);
					pstm.setLong(6, Long.parseLong(it.sdaecatgestionescolar_pid.toString()));
					pstm.executeUpdate();
				}
			}
			
			
			con.commit();
			resultado.setSuccess(true);
		} catch (Exception e) {
			
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getExisteSDAEGestionEscolar(Long GestionEscolar_pid) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "";
		try {
			String consulta = StatementsCatalogos.GET_EXISTE_SDAECAT_GESTION_ESCOLAR;
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, GestionEscolar_pid);
			rs= pstm.executeQuery();
			List<Boolean> info = new ArrayList<Boolean>();
			if(rs.next()) {
				info.add(true);
			}else {
				info.add(false);
			}
			
			resultado.setSuccess(true);
			resultado.setData(info);
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
	
	public Result getExisteSDAECreditoGE(Long SDAEGestionEscolar_pid,String fecha) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "";
		try {
			String consulta = StatementsCatalogos.GET_EXISTE_SDAECAT_CREDITO_GE;
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, SDAEGestionEscolar_pid);
			pstm.setString(2, fecha);
			rs= pstm.executeQuery();
			List<Boolean> info = new ArrayList<Boolean>();
			if(rs.next()) {
				info.add(true);
			}else {
				info.add(false);
			}
			
			resultado.setSuccess(true);
			resultado.setData(info);
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
	
	public Result getSDAEGestionEscolar(Long SDAEGestionEscolar_pid) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "";
		try {
			String consulta = StatementsCatalogos.GET_SDAECAT_GESTION_ESCOLAR;
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, SDAEGestionEscolar_pid);
			rs = pstm.executeQuery()
			
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
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
			resultado.setError_info(SDAEGestionEscolar_pid.toString())
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
	
	public Result getSDAEGestionEscolarByCarrera(Long catgestionescolar_pid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "";
		
		try {
			String consulta = StatementsCatalogos.GET_SDAECAT_GESTION_ESCOLAR_BY_CARRERA;
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, catgestionescolar_pid);
			rs = pstm.executeQuery();
			
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
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
			resultado.setError_info(catgestionescolar_pid.toString());
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}
	
	public Result getCreditoGE(Long SDAEGestionEscolar_pid,String fecha) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "";
		try {
			String consulta = StatementsCatalogos.GET_SDAECAT_CREDITO_GE;
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, SDAEGestionEscolar_pid);
			pstm.setString(2, fecha);
			rs = pstm.executeQuery();
			
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
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
	
	
	public Result getConfiguracionCampus(Long idCampus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		ConfiguracionesCampus configCampus = new ConfiguracionesCampus();
		List<ConfiguracionesCampus> lstData = new ArrayList<ConfiguracionesCampus>();
		String errorLog = "";
		
		try {
			errorLog += "Preparando conexión :: ";
			closeCon = validarConexion();
			pstm = con.prepareStatement(StatementsCatalogos.GET_CAMPUS_CONFIG);
			pstm.setLong(1, idCampus);
			rs = pstm.executeQuery();
			errorLog += "Query lista :: ";
			
			while(rs.next()) {
				configCampus = new ConfiguracionesCampus();
				configCampus.setDescuentoProntoPago(rs.getBoolean("DESCUENTOPRONTOPAGO"));
				configCampus.setInteresColegiatura(rs.getBoolean("INTERESCOLEGIATURA"));
				configCampus.setInteresInscripcion(rs.getBoolean("INTERESINSCRIPCION"));
				configCampus.setPersistenceId(rs.getLong("PERSISTENCEID"));
				configCampus.setPorcentajeInteresFinanciamiento(rs.getDouble("PORCENTAJEINTERESFINANCIAMIENTO"));
				configCampus.setTieneFinanciamiento(rs.getBoolean("TIENEFINANCIAMIENTO"));
				configCampus.setUrlReglamento(rs.getString("URLREGLAMENTO"));
				configCampus.setIdCampus(rs.getLong("IDCAMPUS"));
				configCampus.setPromedioMinimo(rs.getDouble("PROMEDIOMINIMO"));
				configCampus.setUrlDocumentoCondicionesFinanciamiento(rs.getString("URLDOCUMENTOCONDICIONESFINANCIAMIENTO"));
				
				lstData.add(configCampus);		
			}
			
			errorLog += "Query ejecutada :: ";
			
			resultado.setData(lstData);
			resultado.setSuccess(true);
		} catch (Exception e) {
			errorLog += e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		resultado.setError_info(errorLog);
		return resultado;
	}
	
	public Result getPRomedioMinimoByCampus(Long idCampus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		ConfiguracionesCampus configCampus = new ConfiguracionesCampus();
		List<ConfiguracionesCampus> lstData = new ArrayList<ConfiguracionesCampus>();
		String errorLog = "";
		
		try {
			errorLog += "Preparando conexión :: ";
			closeCon = validarConexion();
			pstm = con.prepareStatement(StatementsCatalogos.GET_CAMPUS_CONFIG);
			pstm.setLong(1, idCampus);
			rs = pstm.executeQuery();
			errorLog += "Query lista :: ";
			
			while(rs.next()) {
				configCampus = new ConfiguracionesCampus();
				configCampus.setPromedioMinimo(rs.getDouble("PROMEDIOMINIMO"));
				
				lstData.add(configCampus);
			}
			
			errorLog += "Query ejecutada :: ";
			
			resultado.setData(lstData);
			resultado.setSuccess(true);
		} catch (Exception e) {
			errorLog += e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		resultado.setError_info(errorLog);
		return resultado;
	}
	
	public Result getInfoBitacora(Long caseId) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "";
		
		try {
			String consulta = StatementsCatalogos.GET_DATOS_BITACORA;
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, caseId);
			rs = pstm.executeQuery();
			
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
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
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result insertUpdateConfigCampus(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "";
		String errorLog = "";
		
		try {
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			if(object.persistenceId == null) {
				errorLog = "INSERT";
				String consulta = StatementsCatalogos.INSERT_CAMPUS_CONFIG;
				closeCon = validarConexion();
				con.setAutoCommit(false);
				pstm = con.prepareStatement(consulta);
				pstm.setBoolean(1, object.descuentoProntoPago);
				pstm.setLong(2, Long.valueOf(object.idCampus.toString()));
				pstm.setBoolean(3, object.interesColegiatura);
				pstm.setBoolean(4, object.interesInscripcion);
				pstm.setDouble(5, Double.parseDouble(object.porcentajeInteresFinanciamiento.toString()));
				pstm.setBoolean(6, object.tieneFinanciamiento);
				pstm.setString(7, object.urlReglamento);
				pstm.setDouble(8, object.promedioMinimo);
				pstm.setString(9, object.urlDocumentoCondicionesFinanciamiento);
				
				pstm.executeUpdate();
				
				con.commit();
			} else {
				errorLog = "UPDATE";
				String consulta = StatementsCatalogos.UPDATE_CAMPUS_CONFIG;
				closeCon = validarConexion();
				con.setAutoCommit(false);
				pstm = con.prepareStatement(consulta);
				pstm.setBoolean(1, object.descuentoProntoPago);
				pstm.setBoolean(2, object.interesColegiatura);
				pstm.setBoolean(3, object.interesInscripcion);
				pstm.setDouble(4, Double.parseDouble(object.porcentajeInteresFinanciamiento.toString()));
				pstm.setBoolean(5, object.tieneFinanciamiento);
				pstm.setString(6, object.urlReglamento);
				pstm.setDouble(7, object.promedioMinimo);
				pstm.setString(8, object.urlDocumentoCondicionesFinanciamiento);
				pstm.setLong(9, Long.valueOf(object.idCampus.toString()));
				
				pstm.executeUpdate();
				
				con.commit();
			}
			resultado.setError_info(errorLog);
			resultado.setSuccess(true);
		} catch (Exception e) {
			
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setError_info(e.getMessage());
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getFechaServidor() {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
				closeCon = validarConexion();
				pstm = con.prepareStatement(StatementsCatalogos.GET_YEAR)
				rs = pstm.executeQuery()
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
				
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();

					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
					info.add(columns)
				}
				resultado.setData(info)
				resultado.setSuccess(true)
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
	
	/**
	 * Obtiene la lista de registros de documentos rrelacionados a un tipo de apoyo
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result getImagenesByCaseId(Long caseId) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", orderby = "ORDER BY ", errorLog="entro";
		CatImagenesSocioEconomico catimagenesSocEco = new CatImagenesSocioEconomico();
		ImagesSocEcoSolicitante row = new ImagesSocEcoSolicitante();
		
		try {
			errorLog += "ENTRO ";
			String consulta = StatementsCatalogos.GET_IMAGENES_SOLICITANTE_BY_CASEID;
			List < ImagesSocEcoSolicitante > rows = new ArrayList < ImagesSocEcoSolicitante > ();
			closeCon = validarConexion();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, Long.valueOf(caseId));
				
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new ImagesSocEcoSolicitante();
				row.setImagenSocioEconomico_id(rs.getLong("img_imagenSocioEconomico_id"));
				row.setCaseId(rs.getLong("img_caseid"));
				row.setUrlImagen(rs.getString("img_urlimagen"));
				row.setPersistenceId(rs.getLong("img_persistenceid"));
				catimagenesSocEco = new CatImagenesSocioEconomico();
				catimagenesSocEco.setPersistenceId(rs.getLong("cis_persistenceid"));
				catimagenesSocEco.setDescripcion(rs.getString("cis_descripcion"));
				row.setImagenSocioEconomico(catimagenesSocEco);
				
				rows.add(row);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			errorLog += e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	/**
	 * Obtiene la lista de registros de documentos rrelacionados a un tipo de apoyo
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result getDocumentosByCaseId(Long caseId) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", orderby = "ORDER BY ", errorLog="entro";
		CatManejoDocumentos catManejoDocumentos = new CatManejoDocumentos();
		DocumentosSolicitante row = new DocumentosSolicitante();
		
		try {
			errorLog += "ENTRO ";
			String consulta = StatementsCatalogos.GET_DOCUMENTOS_SOLICITANTE_BY_CASEID;
			List < CatManejoDocumentos > rows = new ArrayList < CatManejoDocumentos > ();
			closeCon = validarConexion();
			where += " WHERE doc.ISELIMINADO <> TRUE AND rel.IDTYPOAPOYO = ? AND cc.GRUPOBONITA = ? ";
			consulta = consulta.replace("[WHERE]", where);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, Long.valueOf(caseId));
				
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new DocumentosSolicitante();
				
				row.setCatManejoDocumentos_id(rs.getLong("cma_persistenceid"));
				row.setCaseId(rs.getLong("doc_caseid"));
				row.setUrlDocumento(rs.getString("doc_urlDocumento"));
				catManejoDocumentos = new CatManejoDocumentos();
				catManejoDocumentos.setPersistenceId(rs.getLong("cma_persistenceid"));
				catManejoDocumentos.setDescripcionDocumento(rs.getString("cma_descripciondocumento"));
				catManejoDocumentos.setIsObligatorioDoc(rs.getBoolean("cma_isobligatoriodoc"));
				catManejoDocumentos.setNombreDocumento(rs.getString("cma_nombredocumento"));
				catManejoDocumentos.setUrlDocumentoAzure(rs.getString("cma_urldocumentoazure"));
				catManejoDocumentos.setRequiereEjemplo(rs.getBoolean("cma_requiereejemplo"));
				row.setCatManejoDocumentos(catManejoDocumentos);
				
				rows.add(row);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			errorLog += e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	public Result getConfiguracionPagoEstudioSocEco(Long idCampus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		CatConfiguracionPagoEstudioSocEco configCampus = new CatConfiguracionPagoEstudioSocEco();
		List<CatConfiguracionPagoEstudioSocEco> lstData = new ArrayList<CatConfiguracionPagoEstudioSocEco>();
		String errorLog = "";
		
		try {
			errorLog += "Preparando conexión :: ";
			closeCon = validarConexion();
			pstm = con.prepareStatement(StatementsCatalogos.GET_CAMPUS_CONFIG_PAGO_ESTUDIO);
			pstm.setLong(1, idCampus);
			rs = pstm.executeQuery();
			errorLog += "Query lista :: ";
			
			if(rs.next()) {
				configCampus = new CatConfiguracionPagoEstudioSocEco();
				configCampus.setPersistenceId(rs.getLong("PERSISTENCEID"));
				configCampus.setIdCampus(idCampus);
				configCampus.setClave(rs.getString("CLAVE"));
				configCampus.setDescripcion(rs.getString("DESCRIPCION"));
				configCampus.setDeshabilitarPagoEstudioSocioEconomico(rs.getBoolean("DESHABILITARPAGOESTUDIOSOCIOECONOMICO"));
				configCampus.setFechaCreacion(rs.getString("FECHACREACION"));
				configCampus.setInstruccionesPago(rs.getString("INSTRUCCIONESPAGO"));
				configCampus.setMonto(rs.getDouble("MONTO"));
			} else {
				configCampus = new CatConfiguracionPagoEstudioSocEco();
			}
			
			lstData.add(configCampus);
			resultado.setData(lstData);
			resultado.setSuccess(true);
		} catch (Exception e) {
			errorLog += e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		resultado.setError_info(errorLog);
		return resultado;
	}
	
	public Result getConfiguracionPagoEstudioSocEcoAspirante(Long idCampus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		CatConfiguracionPagoEstudioSocEco configCampus = new CatConfiguracionPagoEstudioSocEco();
		List<CatConfiguracionPagoEstudioSocEco> lstData = new ArrayList<CatConfiguracionPagoEstudioSocEco>();
		String errorLog = "";
		
		try {
			errorLog += "Preparando conexión :: ";
			closeCon = validarConexion();
			pstm = con.prepareStatement(StatementsCatalogos.GET_CAMPUS_CONFIG_PAGO_ESTUDIO);
			pstm.setLong(1, idCampus);
			rs = pstm.executeQuery();
			errorLog += "Query lista :: ";
			
			if(rs.next()) {
				configCampus = new CatConfiguracionPagoEstudioSocEco();
				configCampus.setDescripcion(rs.getString("DESCRIPCION"));
				configCampus.setDeshabilitarPagoEstudioSocioEconomico(rs.getBoolean("DESHABILITARPAGOESTUDIOSOCIOECONOMICO"));
				configCampus.setInstruccionesPago(rs.getString("INSTRUCCIONESPAGO"));
				configCampus.setMonto(rs.getDouble("MONTO"));
			} else {
				configCampus = new CatConfiguracionPagoEstudioSocEco();
			}
			
			lstData.add(configCampus);
			resultado.setData(lstData);
			resultado.setSuccess(true);
		} catch (Exception e) {
			errorLog += e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		resultado.setError_info(errorLog);
		return resultado;
	}

	public Result insertUpdateConfigPagoEstudioSocEco(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;;
		String where = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			if(object.persistenceId == null) {
				String consulta = StatementsCatalogos.INSERT_CAMPUS_CONFIG_PAGO_ESTUDIO;
				closeCon = validarConexion();
				con.setAutoCommit(false);
				pstm = con.prepareStatement(consulta);
				pstm.setString(1, object.clave);
				pstm.setString(2, object.descripcion);
				pstm.setBoolean(3, object.deshabilitarPagoEstudioSocioEconomico);
				pstm.setLong(4, Long.valueOf(object.idCampus.toString()));
				pstm.setString(5, object.instruccionesPago);
				pstm.setDouble(6, Double.valueOf(object.monto.toString()));

				pstm.executeUpdate();
				con.commit();
			} else {
				String consulta = StatementsCatalogos.UPDATE_CAMPUS_CONFIG_PAGO_ESTUDIO;
				closeCon = validarConexion();
				con.setAutoCommit(false);
				pstm = con.prepareStatement(consulta);
//				CLAVE = ?, DESCRIPCION = ?,  DESHABILITARPAGOESTUDIOSOCIOECONOMICO = ?,  INSTRUCCIONESPAGO = ?,  MONTO = ?  WHERE IDCAMPUS = ?;
				pstm.setString(1, object.clave);
				pstm.setString(2, object.descripcion);
				pstm.setBoolean(3, object.deshabilitarPagoEstudioSocioEconomico);
				pstm.setString(4, object.instruccionesPago);
				pstm.setDouble(5, Double.valueOf(object.monto.toString()));
				pstm.setLong(6, Long.valueOf(object.idCampus.toString()));

				pstm.executeUpdate();
				con.commit();
			}
			resultado.setSuccess(true);
		} catch (Exception e) {
			
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
}
