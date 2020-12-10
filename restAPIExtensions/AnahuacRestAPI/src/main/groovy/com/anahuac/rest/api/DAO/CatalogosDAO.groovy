package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatBachilleratos
import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatTitulo
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.CatDescuentosCustom
import com.anahuac.rest.api.Entity.Custom.CatEscolaridadCustom
import com.anahuac.rest.api.Entity.Custom.CatEstadoCivilCustom
import com.anahuac.rest.api.Entity.Custom.CatParentescoCustom
import com.anahuac.rest.api.Entity.Custom.CatPeriodoCustom
import com.anahuac.rest.api.Entity.Custom.CatSexoCustom
import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import org.bonitasoft.engine.identity.UserMembership  
import org.bonitasoft.engine.identity.UserMembershipCriterion

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
	/***********************JUAN ESQUER******************************/
	public Result getCatTitulo(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="WHERE ISELIMINADO=false", orderby="ORDER BY ", errorlog=""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatTitulo row = new CatTitulo()
			List<CatTitulo> rows = new ArrayList<CatTitulo>();
			closeCon = validarConexion();
			
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
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
					case "CLAVE":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(CLAVE) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;	
				}
			}
			orderby+=object.orderby
			if(orderby.equals("ORDER BY ")) {
				orderby+="PERSISTENCEID";
			}
			orderby+=" "+object.orientation;
			String consulta = Statements.GET_CATTITULO;
			consulta=consulta.replace("[WHERE]", where);
			errorlog+="consulta:"
			errorlog+=consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "")
				pstm = con.prepareStatement(consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
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
					row = new CatTitulo()
					row.setClave(rs.getString("clave"))
					row.setDescripcion(rs.getString("descripcion"))
					row.setIsEliminado(rs.getBoolean("isEliminado"))
					row.setOrden(rs.getInt("orden"))
					row.setPersistenceId(rs.getLong("persistenceId"))
					row.setPersistenceVersion(rs.getLong("persistenceVersion"))
					row.setUsuarioCreacion(rs.getString("usuarioCreacion"))
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
	public Result getEstadoCivil(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="WHERE ISELIMINADO=false", orderby="ORDER BY ", errorlog=""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatEstadoCivilCustom row = new CatEstadoCivilCustom()
			List<CatEstadoCivilCustom> rows = new ArrayList<CatEstadoCivilCustom>();
			closeCon = validarConexion();
			
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					case "CLAVE":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(CLAVE) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "DESCRIPCIÓN":
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
					case "USUARIO BANNER":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(usuariobanner) ";
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
					where +=" LOWER(FECHACREACION) ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=LOWER('[valor]')"
					}else {
						where+="LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "FECHA IMPORTACIÓN":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" LOWER(FECHAIMPORTACION) ";
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
				break
				case "DESCRIPCIÓN":
				orderby+="descripcion";
				break
				case "USUARIO BANNER":
				orderby+="usuariobanner";
				break
				case "FECHA CREACIÓN":
				orderby+="fechacreacion";
				break
				case "FECHA IMPORTACIÓN":
				orderby+="fechaImportacion";
				break
				default:
				orderby+="PERSISTENCEID";
				break;
			}
			orderby+=" "+object.orientation;
			String consulta = Statements.GET_CATESTADOCIVIL;
			consulta=consulta.replace("[WHERE]", where);
			errorlog+="consulta:"
			errorlog+=consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "")
				pstm = con.prepareStatement(consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
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
					row = new CatTitulo()
					row.setClave(rs.getString("clave"))
					row.setDescripcion(rs.getString("descripcion"))
					row.setIsEliminado(rs.getBoolean("isEliminado"))
					try {
						row.fechaCreacion(new java.util.Date(rs.getDate("fechacreacion")))
						row.fechaImportacion(new java.util.Date(rs.getDate("fechaImportacion")))
						}catch(Exception e) {
						errorlog+=e.getMessage()
					}
					
					row.setPersistenceId(rs.getLong("persistenceId"))
					row.setPersistenceVersion(rs.getLong("persistenceVersion"))
					row.setUsuarioCreacion(rs.getString("usuariobanner"))
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
	/**********************JUAN ESQUER FIN******************************/
	/***********************JESUS OSUNA********************************/
	public Result getCatEscolaridad(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby="ORDER BY ", errorLog = ""
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
	
	public Result getCatParentesco(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby="ORDER BY ", errorLog = ""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//assert object instanceof List;
			
				String consulta = Statements.GET_CATPARENTESCO
				CatParentescoCustom row = new CatParentescoCustom();
				List<CatParentescoCustom> rows = new ArrayList<CatParentescoCustom>();
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
					
					row = new CatParentescoCustom();
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
	
	
	public Result getCatDescuentos(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby="ORDER BY ", errorLog = "",bachillerato="", campus=""
		
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//assert object instanceof List;
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Cancún");
			objGrupoCampus.put("valor","CAMPUS-CANCUN");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Mayab");
			objGrupoCampus.put("valor","CAMPUS-MAYAB");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac México Norte");
			objGrupoCampus.put("valor","CAMPUS-MNORTE");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac México Sur");
			objGrupoCampus.put("valor","CAMPUS-MSUR");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Oaxaca");
			objGrupoCampus.put("valor","CAMPUS-OAXACA");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Puebla");
			objGrupoCampus.put("valor","CAMPUS-PUEBLA");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Querétaro");
			objGrupoCampus.put("valor","CAMPUS-QUERETARO");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Xalapa");
			objGrupoCampus.put("valor","CAMPUS-XALAPA");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Querétaro");
			objGrupoCampus.put("valor","CAMPUS-QUERETARO");
			lstGrupoCampus.add(objGrupoCampus);
			
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Juan Pablo II");
			objGrupoCampus.put("valor","CAMPUS-JP2");
			lstGrupoCampus.add(objGrupoCampus);
					
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(Map<String, String> rowGrupo : lstGrupoCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.get("valor"))) {
						lstGrupo.add(rowGrupo.get("valor"));
						break;
					}
				}
			}
			
			where+=" WHERE c.isEliminado = false";
			campus+=" AND ("
			for(Integer i=0; i<lstGrupo.size(); i++) {
				String campusMiembro=lstGrupo.get(i);
				campus+="c.campus='"+campusMiembro+"'"
				if(i==(lstGrupo.size()-1)) {
					campus+=") "
				}
				else {
					campus+=" OR "
				}
			}
			
				String consulta = Statements.GET_CATDESCUENTOS
				CatDescuentosCustom row = new CatDescuentosCustom();
				List<CatDescuentosCustom> rows = new ArrayList<CatDescuentosCustom>();
				closeCon = validarConexion();
				
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					
					switch(filtro.get("columna")) {
						case "TIPO":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(TIPO) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "CONVENI O DESCUENTO":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(CONVENIODESCUENTO) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "INICIO VIGENCIA":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(INICIOVIGENCIA) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
							 
						case "FIN VIGENCIA":
					    if(where.contains("WHERE")) {
									   where+= " AND "
								  }else {
									   where+= " WHERE "
								  }
								  where +=" LOWER(FINVIGENCIA) ";
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
							 
						case "BACHILLERATO":
						if(where.contains("WHERE")) {
									   where+= " AND "
								  }else {
									   where+= " WHERE "
								  }
								  where +=" LOWER(BACHILLERATO) ";
								  if(filtro.get("operador").equals("Igual a")) {
									   where+="=LOWER('[valor]')"
								  }else {
									   where+="LIKE LOWER('%[valor]%')"
								  }
								  where = where.replace("[valor]", filtro.get("valor"))
								  break;
								  
						case "CAMPANA":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
				    	}
						    where +=" LOWER(CAMPANA) ";
					   if(filtro.get("operador").equals("Igual a")) {
						   where+="=LOWER('[valor]')"
					   }else {
							where+="LIKE LOWER('%[valor]%')"
					   }
					   where = where.replace("[valor]", filtro.get("valor"))
					   break;
					   
					   
					   case "CIUDAD":
					   if(where.contains("WHERE")) {
						   where+= " AND "
					   }else {
						   where+= " WHERE "
					   }
						   where +=" LOWER(CIUDAD) ";
					  if(filtro.get("operador").equals("Igual a")) {
						  where+="=LOWER('[valor]')"
					  }else {
						   where+="LIKE LOWER('%[valor]%')"
					  }
					  where = where.replace("[valor]", filtro.get("valor"))
					  break;
					  
					  
					  case "DESCUENTO":
					  if(where.contains("WHERE")) {
						  where+= " AND "
					  }else {
						  where+= " WHERE "
					  }
						  where +=" LOWER(DESCUENTO) ";
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
					case "TIPO":
					orderby+="TIPO";
					break;
					case "DESCUENTO":
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
					case "INICIOVIGENCIA":
					orderby+="INICIOVIGENCIA";
					break;
					case "FINVIGENCIA":
					orderby+="FINVIGENCIA";
					break;
					default:
					orderby+="c.persistenceid"
					break;
				}
				errorLog+= "orderby"
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[BACHILLERATO]", bachillerato)
				where+=" "+campus +" "+bachillerato 
				consulta=consulta.replace("[WHERE]", where);
				errorLog += " "+consulta
				pstm = con.prepareStatement(consulta.replace("c.*, b.descripcion as bachilleratos", "COUNT(c.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorLog+= "consulta"
				errorLog+= consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				
				errorLog+= "fecha=="

				rs = pstm.executeQuery()
				while(rs.next()) {
					
					row = new CatDescuentosCustom();
					row.setTipo(rs.getString("TIPO"))
					row.setDescuento(rs.getInt("descuento"));
					row.setInicioVigencia(rs.getString("iniciovigencia"));
					row.setFinVigencia(rs.getString("finvigencia"));
					row.setCampana(rs.getString("campana"));
					row.setCiudad(rs.getString("ciudad"));
					try {
						row.setCatBachilleratos(new CatBachilleratos())
						row.getCatBachilleratos().setDescripcion(rs.getString("bachilleratos"))
						}catch(Exception e) {
						errorLog+=e.getMessage()
					}
					
					row.setIsEliminado(rs.getBoolean("isEliminado"));
					row.setPersistenceId(rs.getLong("PERSISTENCEID"));
					row.setPersistenceVersion(rs.getLong("persistenceVersion"));
					row.setUsuarioCreacion(rs.getString("usuariocreacion"))
					
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
	
	/***********************ERIC ROSAS ********************************/
	public Result getCatPeriodo(String jsonData, RestAPIContext context) {
			Result resultado = new Result();
			Boolean closeCon = false;
			String where ="", orderby="ORDER BY ", errorLog = ""
			try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				//assert object instanceof List;
				
					String consulta = Statements.GET_CATPERIODO
					CatPeriodoCustom row = new CatPeriodoCustom();
					List<CatPeriodoCustom> rows = new ArrayList<CatPeriodoCustom>();
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
							case "CUATRIMESTRE":
							if(where.contains("WHERE")) {
									  where+= " AND "
								 }else {
									  where+= " WHERE "
								 }
								 where +=" LOWER(CUATRIMESTRE) ";
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
						case "CUATRIMESTRE":
						orderby+="isCuatrimestral";
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
						case "USUARIOBANNER":
						orderby+="usuarioBanner";
						break;
						case "FECHAIMPORTACION":
						orderby+="fechaImplementacion";
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
						
						row = new CatPeriodoCustom();
						row.setClave(rs.getString("clave"))
						row.setDescripcion(rs.getString("descripcion"));
						row.setFechaCreacion(rs.getString("fechacreacion"));
						row.setIsEliminado(rs.getBoolean("isEliminado"));
						row.setPersistenceId(rs.getLong("PERSISTENCEID"));
						row.setPersistenceVersion(rs.getLong("persistenceVersion"));
						row.setUsuarioBanner(rs.getString("usuariobanner"));
						row.setIsCuatrimestral(rs.getBoolean("isCuatrimestral"));
						row.setFechaImplementacion(rs.getString("fechaImplementacion"));
						
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
	
	
	/***********************ERIC ROSAS FIN********************************/

}
