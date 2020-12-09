package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatEscolaridad
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.CatEscolaridadCustom
import com.anahuac.rest.api.Entity.Custom.CatSexoCustom
import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CatalogosDAO {
	
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
	}
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogosDAO.class);
	/************************DANIEL CERVANTES****************************/
	public Result getCatCampus(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby=""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof List;
			
				String consulta = Statements.GET_CATCAMPUS
				CatCampus row = new CatCampus();
				List<CatCampus> rows = new ArrayList<CatCampus>();
				closeCon = validarConexion();
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						case "CLAVE":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(clave) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "DESCRIPCION":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(DESCRIPCION) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "FECHACREACION":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(FECHACREACION) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "FECHAIMPLEMENTACION":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(FECHAIMPLEMENTACION) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "GRUPOBONITA":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(GRUPOBONITA) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "ID":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(ID) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "ISELIMINADO":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(ISELIMINADO) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "ISENABLED":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(clave) ";
							 if(filtro.get("ISENABLED").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "ORDEN":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(ORDEN) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "PERSISTENCEID":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(PERSISTENCEID) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "PERSISTENCEVERSION":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(PERSISTENCEVERSION) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "URLAUTORDATOS":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(URLAUTORDATOS) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "URLAVISOPRIVACIDAD":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(URLAVISOPRIVACIDAD) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "URLDATOSVERIDICOS":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(URLDATOSVERIDICOS) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "USUARIOBANNER":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(USUARIOBANNER) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
					}
				}
				switch(object.orderby) {
					case "CLAVE":
					orderby+="clave";
					break;
					case "DESCRIPCION":
					orderby+="descripcion";
					break;
					case "FECHACREACION":
					orderby+="fechaCreacion";
					break;
					case "FECHAIMPLEMENTACION":
					orderby+="fechaImplementacion";
					break;
					case "GRUPOBONITA":
					orderby+="grupoBonita";
					break;
					case "ID":
					orderby+="id";
					break;
					case "ISELIMINADO":
					orderby+="isEliminado";
					break;
					case "ISENABLED":
					orderby+="isEnabled";
					break;
					case "ORDEN":
					orderby+="orden";
					break;
					case "PERSISTENCEID":
					orderby+="persistenceId";
					break;
					case "PERSISTENCEVERSION":
					orderby+="persistenceVersion";
					break;
					case "URLAUTORDATOS":
					orderby+="urlAutorDatos";
					break;
					case "URLAVISOPRIVACIDAD":
					orderby+="urlAvisoPrivacidad";
					break;
					case "URLDATOSVERIDICOS":
					orderby+="urlDatosVeridicos";
					break;
					case "USUARIOBANNER":
					orderby+="usuarioBanner";
					break;
					default:
					orderby+="persistenceid"
					break;
				}
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[WHERE]", where);
				pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				
				rs = pstm.executeQuery()
				
				while(rs.next()) {
					row = new CatCampus();
					row.setClave(rs.getString("clave"))
					row.setDescripcion(rs.getString("descripcion"));
                    row.setFechaCreacion(rs.getString("fechaCreacion"));
                    row.setFechaImplementacion(rs.getString("fechaImplementacion"));
                    row.setGrupoBonita(rs.getString("grupoBonita"));
                    row.setId(rs.getString("id"));
                    row.setIsEliminado(rs.getString("isEliminado"));
                    row.setIsEnabled(rs.getString("isEnabled"));
                    row.setOrden(rs.getString("orden"));
                    row.setPersistenceId(rs.getLong("persistenceId"));
                    row.setPersistenceVersion(rs.getLong("persistenceVersion"));
                    row.setUrlAutorDatos(rs.getString("urlAutorDatos"));
                    row.setUrlAvisoPrivacidad(rs.getString("urlAvisoPrivacidad"));
                    row.setUrlDatosVeridicos(rs.getString("urlDatosVeridicos"));
                    row.setUsuarioBanner(rs.getString("usuarioBanner"));
					
					rows.add(row)
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
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
	/***********************DANIEL CERVANTES FIN************************/
	
	/***********************JESUS OSUNA********************************/
	public Result getCatEscolaridad(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby="ORDER BY ", errorLog = ""
		errorLog+= "informacion"
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//assert object instanceof List;
			
				String consulta = Statements.GET_CATESCOLARIDAD
				CatEscolaridadCustom row = new CatEscolaridadCustom();
				List<CatEscolaridadCustom> rows = new ArrayList<CatEscolaridadCustom>();
				closeCon = validarConexion();
				errorLog+= "informacion 1"
				where+=" WHERE isEliminado = false";
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					
					switch(filtro.get("columna")) {
						case "CLAVE":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(clave) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "DESCRIPCION":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(DESCRIPCION) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "FECHACREACION":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(FECHACREACION) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "PERSISTENCEID":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(PERSISTENCEID) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "PERSISTENCEVERSION":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(PERSISTENCEVERSION) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "USUARIOCREACION":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(USUARIOCREACION) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
					}
				}
				switch(object.orderby) {
					case "CLAVE":
					orderby+="clave";
					break;
					case "DESCRIPCION":
					orderby+="descripcion";
					break;
					case "FECHACREACION":
					orderby+="fechaCreacion";
					break;
					case "ISELIMINADO":
					orderby+="isEliminado";
					break;
					case "PERSISTENCEID":
					orderby+="persistenceId";
					break;
					case "PERSISTENCEVERSION":
					orderby+="persistenceVersion";
					break;
					case "USUARIOCREACION":
					orderby+="usuarioCreacion";
					break;
					default:
					orderby+="persistenceid"
					break;
				}
				errorLog+= "orderby"
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[WHERE]", where);
				pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorLog+= "consulta"
				errorLog+= consulta
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				
				errorLog+= "fecha=="

				rs = pstm.executeQuery()
				while(rs.next()) {
					errorLog+="entro"
					Date creacion = sdf.parse(rs.getString("fechacreacion"))
					LocalDateTime localDateTime = creacion.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
					row = new CatEscolaridadCustom();
					row.setClave(rs.getString("clave"))
					row.setDescripcion(rs.getString("descripcion"));
					row.setFechaCreacion(rs.getString("fechacreacion"));
					row.setIsEliminado(rs.getBoolean("isEliminado"));
					row.setPersistenceId(rs.getLong("PERSISTENCEID"));
					row.setPersistenceVersion(rs.getLong("persistenceVersion"));
					row.setUsuarioCreacion(rs.getString("usuariocreacion"));
					
					rows.add(row)
				}
				errorLog+= "llenado"
				
				resultado.setSuccess(true)
				resultado.setError(errorLog)
				resultado.setData(rows)
				
			} catch (Exception e) {
				resultado.setError_info(errorLog)
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	
	public Result getCatSexo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby="ORDER BY ", errorLog = ""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//assert object instanceof List;
			
				String consulta = Statements.GET_CATSEXO
				CatSexoCustom row = new CatSexoCustom();
				List<CatSexoCustom> rows = new ArrayList<CatSexoCustom>();
				closeCon = validarConexion();
				where+=" WHERE isEliminado = false";
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					
					switch(filtro.get("columna")) {
						case "CLAVE":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(clave) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "DESCRIPCION":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(DESCRIPCION) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "FECHACREACION":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(FECHACREACION) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "PERSISTENCEID":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(PERSISTENCEID) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "PERSISTENCEVERSION":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(PERSISTENCEVERSION) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "USUARIOCREACION":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(USUARIOCREACION) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
					}
				}
				switch(object.orderby) {
					case "CLAVE":
					orderby+="clave";
					break;
					case "DESCRIPCION":
					orderby+="descripcion";
					break;
					case "FECHACREACION":
					orderby+="fechaCreacion";
					break;
					case "ISELIMINADO":
					orderby+="isEliminado";
					break;
					case "PERSISTENCEID":
					orderby+="persistenceId";
					break;
					case "PERSISTENCEVERSION":
					orderby+="persistenceVersion";
					break;
					case "USUARIOCREACION":
					orderby+="usuarioCreacion";
					break;
					default:
					orderby+="persistenceid"
					break;
				}
				errorLog+= "orderby"
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[WHERE]", where);
				pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorLog+= "consulta"
				errorLog+= consulta
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				
				errorLog+= "fecha=="

				rs = pstm.executeQuery()
				while(rs.next()) {
					
					row = new CatSexoCustom();
					row.setClave(rs.getString("clave"))
					row.setDescripcion(rs.getString("descripcion"));
					row.setFechaCreacion(rs.getString("fechacreacion"));
					row.setIsEliminado(rs.getBoolean("isEliminado"));
					row.setPersistenceId(rs.getLong("PERSISTENCEID"));
					row.setPersistenceVersion(rs.getLong("persistenceVersion"));
					row.setUsuarioCreacion(rs.getString("usuariocreacion"));
					
					rows.add(row)
				}
				
				resultado.setSuccess(true)
				resultado.setError(errorLog)
				resultado.setData(rows)
				
			} catch (Exception e) {
				resultado.setError_info(errorLog)
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public LocalDateTime stringParse(String fecha) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
		Date creacion = sdf.parse(fecha)
		LocalDateTime localDateTime = creacion.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
		return localDateTime;
	}
	/***********************JESUS OSUNA FIN********************************/
	
	
}
