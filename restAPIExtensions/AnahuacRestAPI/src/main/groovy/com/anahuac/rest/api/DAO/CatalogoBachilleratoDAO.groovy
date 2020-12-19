package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion

import com.anahuac.bitacora.CatBitacoraComentarios
import com.anahuac.catalogos.CatBachilleratos
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.StatementsBachillerato
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class CatalogoBachilleratoDAO {
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	public Result insert(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			CatBachilleratos catBachillerado = new CatBachilleratos();
			List<CatBachilleratos> data =  new ArrayList<CatBachilleratos>();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
				closeCon = validarConexion();
				pstm = con.prepareStatement(StatementsBachillerato.INSERT)
                pstm.setString(1, object.ciudad)
                pstm.setString(2, object.clave)
                pstm.setString(3, object.descripcion)
                pstm.setString(4, object.estado)
                pstm.setString(5, object.pais)
                pstm.setString(6, "")
                pstm.setString(7, object.usuarioBanner)
				
				pstm.execute()
				resultado.setSuccess(true)
				data.add(catBachillerado)
				resultado.setData(data)
			
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
	public Result insertCatBitacoraComentario(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
				closeCon = validarConexion();
				pstm = con.prepareStatement(StatementsBachillerato.INSERT_CATBITACORACOMENTARIOS)
				pstm.setString(1,object.comentario);
				pstm.setString(2, object.fechaCreacion)
				pstm.setBoolean(3, false)
				pstm.setString(4,object.modulo)
				pstm.setString(5, object.usuario)
				pstm.setString(6, object.usuarioComentario)
				
				pstm.execute()
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
	public Result get(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(StatementsBachillerato.GET)
				
				
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
	public Result getCatBitacoraComentario(String jsonData, RestAPIContext context) {
			Result resultado = new Result();
			Boolean closeCon = false;
			String where ="", orderby="ORDER BY ", errorLog = ""
			try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				//assert object instanceof List;
				
					String consulta = StatementsBachillerato.GET_CATBITACORACOMENTARIOS;
					List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
					List<String> lstGrupo = new ArrayList<String>();
					List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
					
					Long userLogged = 0L;
					Long caseId = 0L;
					Long total = 0L;
					Map<String, String> objGrupoCampus = new HashMap<String, String>();
					
					closeCon = validarConexion();
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
					
					objGrupoCampus = new HashMap<String, String>();
					objGrupoCampus.put("descripcion","Anáhuac Cordoba");
					objGrupoCampus.put("valor","CAMPUS-CORDOBA");
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
					where+=" WHERE bitacora.ISELIMINADO = false";
					where+=" AND ("
					for(Integer i=0; i<lstGrupo.size(); i++) {
						String campusMiembro=lstGrupo.get(i);
						where+="campus.grupobonita='"+campusMiembro+"'"
						if(i==(lstGrupo.size()-1)) {
							where+=") "
						}
						else {
							where+=" OR "
						}
					}
					for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
						
						switch(filtro.get("columna")) {
							case "PERSISTENCEID":
							if(where.contains("WHERE")) {
								where+= " AND "
							}else {
								where+= " WHERE "
							}
							where +=" LOWER(bitacora.PERSISTENCEID) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
							case "COMENTARIO":
							if(where.contains("WHERE")) {
									  where+= " AND "
								 }else {
									  where+= " WHERE "
								 }
								 where +=" LOWER(bitacora.COMENTARIO) ";
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
								 where +=" LOWER(bitacora.FECHACREACION) ";
								 if(filtro.get("operador").equals("Igual a")) {
									  where+="=LOWER('[valor]')"
								 }else {
									  where+="LIKE LOWER('%[valor]%')"
								 }
								 where = where.replace("[valor]", filtro.get("valor"))
								 break;
							case "MODULO":
							if(where.contains("WHERE")) {
									  where+= " AND "
								 }else {
									  where+= " WHERE "
								 }
								 where +=" LOWER(bitacora.MODULO) ";
								 if(filtro.get("operador").equals("Igual a")) {
									  where+="=LOWER('[valor]')"
								 }else {
									  where+="LIKE LOWER('%[valor]%')"
								 }
								 where = where.replace("[valor]", filtro.get("valor"))
								 break;
							case "USUARIO":
							if(where.contains("WHERE")) {
									  where+= " AND "
								 }else {
									  where+= " WHERE "
								 }
								 where +=" LOWER(bitacora.USUARIO) ";
								 if(filtro.get("operador").equals("Igual a")) {
									  where+="=LOWER('[valor]')"
								 }else {
									  where+="LIKE LOWER('%[valor]%')"
								 }
								 where = where.replace("[valor]", filtro.get("valor"))
								 break;
							case "USUARIOCOMENTARIO":
							if(where.contains("WHERE")) {
									  where+= " AND "
								 }else {
									  where+= " WHERE "
								 }
								 where +=" LOWER(bitacora.USUARIOCOMENTARIO) ";
								 if(filtro.get("operador").equals("Igual a")) {
									  where+="=LOWER('[valor]')"
								 }else {
									  where+="LIKE LOWER('%[valor]%')"
								 }
								 where = where.replace("[valor]", filtro.get("valor"))
								 break;
							 case "CAMPUS":
								 if(where.contains("WHERE")) {
										   where+= " AND "
									  }else {
										   where+= " WHERE "
									  }
									  where +=" LOWER(campus.grupobonita) ";
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
						case "COMENTARIO":
						orderby+="bitacora.COMENTARIO";
						break;
						case "FECHACREACION":
						orderby+="bitacora.FECHACREACION";
						break;
						case "CUATRIMESTRE":
						orderby+="bitacora.isCuatrimestral";
						break;
						case "ISELIMINADO":
						orderby+="bitacora.ISELIMINADO";
						break;
						case "MODULO":
						orderby+="bitacora.MODULO";
						break;
						case "USUARIO":
						orderby+="bitacora.USUARIO";
						break;
						case "USUARIOCOMENTARIO":
						orderby+="bitacora.USUARIOCOMENTARIO";
						break;
						default:
						orderby+="bitacora.PERSISTENCEID"
						break;
					}
					orderby+=" "+object.orientation;
					consulta=consulta.replace("[WHERE]", where);
					//errorLog+=consulta
					pstm = con.prepareStatement(consulta.replace("bitacora.*", "COUNT(bitacora.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
					rs= pstm.executeQuery()
					if(rs.next()) {
						resultado.setTotalRegistros(rs.getInt("registros"))
					}
					consulta=consulta.replace("[ORDERBY]", orderby)
					consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
					errorLog+= "consulta"
					//errorLog+= consulta
					pstm = con.prepareStatement(consulta)
					pstm.setInt(1, object.limit)
					pstm.setInt(2, object.offset)
					
					errorLog+= "fecha=="
	
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
					
					resultado.setSuccess(true)
					resultado.setError_info(errorLog)
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
	public Result getDescuentosCiudadBachillerato(Integer parameterP, Integer parameterC, String campus, String bachillerato, String ciudad, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(StatementsBachillerato.GET_DESCUENTOS_CIUDAD_BACHILLERATO)
				pstm.setString(1, campus)
				pstm.setString(2, bachillerato)
				pstm.setString(3, ciudad)
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
	public Result getDescuentosCampana(Integer parameterP, Integer parameterC, String campus, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(StatementsBachillerato.GET_DESCUENTOS_CAMPANA)
				pstm.setString(1, campus)
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
	public Result update(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			CatBachilleratos catBachillerado = new CatBachilleratos();
			List<CatBachilleratos> data =  new ArrayList<CatBachilleratos>();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
				closeCon = validarConexion();
				pstm = con.prepareStatement(StatementsBachillerato.UPDATE)
				pstm.setString(1, object.ciudad)
                pstm.setString(2, object.clave)
                pstm.setString(3, object.descripcion)
                pstm.setString(4, object.estado)
                pstm.setString(5, object.pais)
                pstm.setString(6, object.usuariobanner)
                pstm.setBoolean(7, object.iseliminado)
                pstm.setBoolean(8, object.isenabled)
                pstm.setLong(9, object.persistenceId)
				
				pstm.execute()
				resultado.setSuccess(true)
				data.add(catBachillerado)
				resultado.setData(data)
			
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
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
	}
}
