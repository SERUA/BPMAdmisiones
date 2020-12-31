package com.anahuac.rest.api.DAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.CatBachilleratoCustomFiltro
import com.anahuac.rest.api.Entity.Custom.CatCampusCustomFiltro
import com.anahuac.rest.api.Entity.Custom.CatCiudadCustonFiltro
import com.anahuac.rest.api.Entity.Custom.CatPaisCustomFiltro
import com.anahuac.catalogos.CatBachilleratos
import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatEstados
import com.anahuac.catalogos.CatPais
import com.anahuac.catalogos.CatPropedeutico
import com.anahuac.catalogos.CatTitulo
import com.anahuac.rest.api.Entity.Custom.CatDescuentosCustom
import com.anahuac.rest.api.Entity.Custom.CatEscolaridadCustom
import com.anahuac.rest.api.Entity.Custom.CatEstadoCivilCustom
import com.anahuac.rest.api.Entity.Custom.CatEstadoCustomFiltro
import com.anahuac.rest.api.Entity.Custom.CatFiltradoCatalogosAutoDescripcion
import com.anahuac.rest.api.Entity.Custom.CatGenericoFiltro
import com.anahuac.rest.api.Entity.Custom.CatGestionEscolar
import com.anahuac.rest.api.Entity.Custom.CatNacionalidadCustomeFiltro
import com.anahuac.rest.api.Entity.Custom.CatParentescoCustom
import com.anahuac.rest.api.Entity.Custom.CatPropedeuticoCustom
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
import java.time.LocalTime

import org.bonitasoft.engine.identity.UserMembership  
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.slf4j.Logger
import org.slf4j.LoggerFactory
class CatalogosDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogosDAO.class);
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
		String where = "WHERE c.ISELIMINADO=false", orderby = "ORDER BY ", errorLog ="";
		String consulta = Statements.GET_CATCAMPUS
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			
			CatCampusCustomFiltro row = new CatCampusCustomFiltro();
			List < CatCampusCustomFiltro > rows = new ArrayList<CatCampusCustomFiltro>();
			closeCon = validarConexion();
			for (Map < String, Object > filtro: (List<Map<String, Object>>) object.lstFiltro) {
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
						where += " LOWER(FECHACREACION) ";
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
						where += " LOWER(FECHAIMPLEMENTACION) ";
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
						where += " LOWER(GRUPOBONITA) ";
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
						where += " LOWER(ISELIMINADO) ";
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
						where += " LOWER(clave) ";
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
						where += " LOWER(URLAUTORDATOS) ";
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
						where += " LOWER(URLAVISOPRIVACIDAD) ";
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
						where += " LOWER(URLDATOSVERIDICOS) ";
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
						where += " LOWER(USUARIOBANNER) ";
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
						where += " LOWER(NUMEROINTERIOR) ";
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
						where += " LOWER(NUMEROEXTERIOR) ";
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
						where += " LOWER(CODIGOPOSTAL) ";
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
					orderby += "fechaCreacion";
					break;
				case "FECHA IMPORTACIÓN":
					orderby += "fechaImplementacion";
					break;
				case "GRUPOBONITA":
					orderby += "grupoBonita";
					break;
				case "ID":
					orderby += "id";
					break;
				case "ISELIMINADO":
					orderby += "isEliminado";
					break;
				case "ISENABLED":
					orderby += "isEnabled";
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
					orderby += "urlAutorDatos";
					break;
				case "URLAVISOPRIVACIDAD":
					orderby += "urlAvisoPrivacidad";
					break;
				case "URLDATOSVERIDICOS":
					orderby += "urlDatosVeridicos";
					break;
				case "USUARIO BANNER":
					orderby += "usuarioBanner";
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
					orderby += "NUMEROINTERIOR";
					break;
				case "NÚMERO EXTERIOR":
					orderby += "NUMEROEXTERIOR";
					break;
				case "CÓDIGO POSTAL":
					orderby += "CODIGOPOSTAL";
					break;
				case "MUNICIPIO":
					orderby += "MUNICIPIO";
					break;
				case "PAÍS":
					orderby += "PAIS";
					break;
				case "ESTADO":
					orderby += "ESTADO";
					break;
				default:
					orderby += "persistenceid"
					break;
			}
			errorLog+= ""
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);
			pstm = con.prepareStatement(consulta.replace("c.*, p.descripcion as pais ,e.descripcion as estado", "COUNT(c.persistenceid) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
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
				row = new CatCampusCustomFiltro();
				row.setClave(rs.getString("clave"))
				row.setDescripcion(rs.getString("descripcion"));
				//row.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechaCreacion")))
				row.setFechaCreacion(rs.getString("fechaCreacion"));
				//row.setFechaImplementacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechaImplementacion")))
				row.setFechaImplementacion(rs.getString("fechaImplementacion"));
				row.setGrupoBonita(rs.getString("grupoBonita"));
				row.setId(rs.getString("id"));
				row.setIsEliminado(rs.getBoolean("isEliminado"));
				row.setIsEnabled(rs.getBoolean("isEnabled"));
				row.setOrden(rs.getLong("orden"));
				row.setPersistenceId(rs.getLong("persistenceId"));
				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
				row.setUrlAutorDatos(rs.getString("urlAutorDatos"));
				row.setUrlAvisoPrivacidad(rs.getString("urlAvisoPrivacidad"));
				row.setUrlDatosVeridicos(rs.getString("urlDatosVeridicos"));
				row.setUsuarioBanner(rs.getString("usuarioBanner"));
				row.setCalle(rs.getString("calle"));
				row.setColonia(rs.getString("colonia"));
				row.setNumeroExterior(rs.getString("numeroExterior"));
				row.setNumeroInterior(rs.getString("numeroInterior"));
				row.setCodigoPostal(rs.getString("codigoPostal"));
				row.setMunicipio(rs.getString("municipio"));
				row.setPais_pid(rs.getString("pais_pid"))
				row.setEstado_pid(rs.getString("pais_pid"))
				errorLog+= "pais"
				try {
					row.setPais(new CatPais())
					row.getPais().setDescripcion(rs.getString("pais"))
					row.getPais().setPersistenceId(rs.getLong("PAIS_PID"))
				} catch (Exception e) {
					errorLog += e.getMessage()
				}
				errorLog+= "paso pais"
				try {
					row.setEstado(new CatEstados())
					row.getEstado().setDescripcion(rs.getString("estado"))
					row.getEstado().setPersistenceId(rs.getLong("ESTADO_PID"))
				} catch (Exception e) {
					errorLog += e.getMessage()
				}
				rows.add(row)
			}
			resultado.setSuccess(true)
	
			resultado.setData(rows)
			resultado.setError_info(errorLog)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(consulta)
		}finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getCatPais(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
			String where ="WHERE ISELIMINADO=false", orderby="ORDER BY "
			String consulta = Statements.GET_CATPAIS;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
				
				CatPaisCustomFiltro row = new CatPaisCustomFiltro();
				List<CatPaisCustomFiltro> rows = new ArrayList<CatPaisCustomFiltro>();
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
							 case "USUARIO CREACIÓN":
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
					case "DESCRIPCIÓN":
					orderby+="descripcion";
					break;
					case "FECHA CREACIÓN":
					orderby+="fechaCreacion";
					break;
					case "ISELIMINADO":
					orderby+="isEliminado";
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
					case "USUARIO CREACIÓN":
					orderby+="usuarioCreacion";
					break;
					case "ORDEN":
					orderby+="ORDEN";
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
					row = new CatPaisCustomFiltro();
					row.setClave(rs.getString("clave"))
					row.setDescripcion(rs.getString("descripcion"));
					//row.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechaCreacion")))
					row.setFechaCreacion(rs.getString("fechaCreacion"));
					row.setIsEliminado(rs.getBoolean("isEliminado"));
					row.setOrden(rs.getLong("orden"));
					row.setPersistenceId(rs.getLong("persistenceId"));
					row.setPersistenceVersion(rs.getLong("persistenceVersion"));
					row.setUsuarioCreacion(rs.getString("usuarioCreacion"));
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
	public Result getCatEstados(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
			String where ="WHERE ISELIMINADO=false", orderby="ORDER BY "
			String consulta = Statements.GET_CATESTADOS;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
				
				CatEstadoCustomFiltro row = new CatEstadoCustomFiltro();
				List<CatEstadoCustomFiltro> rows = new ArrayList<CatEstadoCustomFiltro>();
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
							 case "PAÍS":
							 if(where.contains("WHERE")) {
									   where+= " AND "
								  }else {
									   where+= " WHERE "
								  }
								  where +=" LOWER(PAIS) ";
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
							 case "USUARIO CREACIÓN":
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
					case "DESCRIPCIÓN":
					orderby+="descripcion";
					break;
					case "FECHA CREACIÓN":
					orderby+="fechaCreacion";
					break;
					case "ISELIMINADO":
					orderby+="isEliminado";
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
					case "USUARIO CREACIÓN":
					orderby+="usuarioCreacion";
					break;
					case "PAÍS":
					orderby+="pais";
					break;
					case "ORDEN":
					orderby+="orden";
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
					row = new CatEstadoCustomFiltro();
					row.setClave(rs.getString("clave"));
					row.setOrden(rs.getLong("orden"));
					row.setPais(rs.getString("pais"));
					row.setDescripcion(rs.getString("descripcion"));
					row.setFechaCreacion(rs.getString("fechaCreacion"));
					row.setIsEliminado(rs.getBoolean("isEliminado"));
					row.setCaseId(rs.getString("caseId"));
					row.setPersistenceId(rs.getLong("persistenceId"));
					row.setPersistenceVersion(rs.getLong("persistenceVersion"));
					row.setUsuarioCreacion(rs.getString("usuarioCreacion"));
					rows.add(row)
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError_info(consulta)
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
public Result getCatBachillerato(String jsonData, RestAPIContext context) {
	Result resultado = new Result();
	Boolean closeCon = false;
	String where = "WHERE ISELIMINADO=false", orderby = "ORDER BY "
	try {
		def jsonSlurper = new JsonSlurper();
		def object = jsonSlurper.parseText(jsonData);

		String consulta = Statements.GET_CATBACHILLERATO;
		CatBachilleratoCustomFiltro row = new CatBachilleratoCustomFiltro();
		List < CatBachilleratoCustomFiltro > rows = new ArrayList<CatBachilleratoCustomFiltro>();
		closeCon = validarConexion();
		for (Map < String, Object > filtro: (List<Map<String, Object>>) object.lstFiltro) {
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
					where += " LOWER(FECHACREACION) ";
					if (filtro.get("operador").equals("Igual a")) {
						where += "=LOWER('[valor]')"
					} else {
						where += "LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "FECHA IMPLEMENTACIÓN":
					if (where.contains("WHERE")) {
						where += " AND "
					} else {
						where += " WHERE "
					}
					where += " LOWER(FECHAIMPLEMENTACION) ";
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
					where += " LOWER(ISELIMINADO) ";
					if (filtro.get("operador").equals("Igual a")) {
						where += "=LOWER('[valor]')"
					} else {
						where += "LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				case "CIUDAD":
					if (where.contains("WHERE")) {
						where += " AND "
					} else {
						where += " WHERE "
					}
					where += " LOWER(CIUDAD) ";
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
					where += " LOWER(USUARIOBANNER) ";
					if (filtro.get("operador").equals("Igual a")) {
						where += "=LOWER('[valor]')"
					} else {
						where += "LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "PERTENECE A LA RED":
					if (where.contains("WHERE")) {
						where += " AND "
					} else {
						where += " WHERE "
					}
					where += " LOWER(PERTENECERED) ";
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
				case "PERTENECE A LA RED":
				orderby += "PERTENECERED";
				break;
			case "DESCRIPCIÓN":
				orderby += "descripcion";
				break;
			case "FECHA CREACIÓN":
				orderby += "fechaCreacion";
				break;
				case "FECHA IMPLEMENTACIÓN":
				orderby += "fechaImplementacion";
				break;
			case "ISELIMINADO":
				orderby += "isEliminado";
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
				orderby += "usuarioBanner";
				break;
			case "PAÍS":
				orderby += "pais";
				break;
			case "ESTADO":
				orderby += "estado";
				break;
				case "CIUDAD":
				orderby += "ciudad";
				break;
			default:
				orderby += "persistenceid"
				break;
		}
		orderby += " " + object.orientation;
		consulta = consulta.replace("[WHERE]", where);
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
			row = new CatBachilleratoCustomFiltro();
			row.setCiudad(rs.getString("ciudad"));
			row.setClave(rs.getString("clave"));
			row.setDescripcion(rs.getString("descripcion"));
			row.setEstado(rs.getString("estado"));
			row.setFechaCreacion(rs.getString("fechaCreacion"));
			row.setFechaImportacion(rs.getString("fechaImportacion"));
			row.setPais(rs.getString("pais"));
			row.setIsEliminado(rs.getBoolean("isEliminado"));
			row.setIsEnabled(rs.getBoolean("isEnabled"));
			row.setUsuarioBanner(rs.getString("usuarioBanner"));
			row.setPersistenceId(rs.getLong("persistenceId"));
			row.setPersistenceVersion(rs.getLong("persistenceVersion"));
			row.setPerteneceRed(rs.getBoolean("perteneceRed"));
			rows.add(row)
		}
		resultado.setSuccess(true)

		resultado.setData(rows)

	} catch (Exception e) {
		resultado.setSuccess(false);
		resultado.setError(e.getMessage());
	}finally {
		if (closeCon) {
			new DBConnect().closeObj(con, stm, rs, pstm)
		}
	}
	return resultado
}
public Result getCatFiltradoCalalogosAdMisiones(String jsonData, RestAPIContext context) {
	Result resultado = new Result();
	Boolean closeCon = false;
	String where ="", orderby="ORDER BY ", errorLog = ""
	try {
		def jsonSlurper = new JsonSlurper();
		def object = jsonSlurper.parseText(jsonData);

			String consulta = Statements.GET_CATFILTRADOCATALOGOSAUTODESCRIPCION
			CatFiltradoCatalogosAutoDescripcion row = new CatFiltradoCatalogosAutoDescripcion();
			List<CatFiltradoCatalogosAutoDescripcion> rows = new ArrayList<CatFiltradoCatalogosAutoDescripcion>();
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
					case "ORDEN":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" LOWER(orden) ";
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
				case "ORDEN":
				orderby+="orden";
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
			orderby+=" "+object.orientation;
			consulta=consulta.replace("[WHERE]", where);
			consulta=consulta.replace("[CATALOGOAD]",object.catalogo)
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
			rs = pstm.executeQuery()
			errorLog+=" entro set";
			while(rs.next()) {	
				row = new CatFiltradoCatalogosAutoDescripcion();
				row.setClave(rs.getString("clave"))
				
				row.setDescripcion(rs.getString("descripcion"));
				//row.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechaCreacion")));
				row.setIsEliminado(rs.getBoolean("isEliminado"));
				row.setPersistenceId(rs.getLong("PERSISTENCEID"));
				row.setPersistenceVersion(rs.getLong("persistenceVersion"));
				try {
					row.setUsuarioCreacion(rs.getString("usuariocreacion"));
				} catch (Exception e) {
					errorLog += e.getMessage()
				}
				try {
					row.setOrden(rs.getLong("orden"))
				} catch (Exception e) {
					errorLog += e.getMessage()
				}
				try {
					row.setUsuarioBanner(rs.getString("usuarioBanner"));
				} catch (Exception e) {
					errorLog += e.getMessage()
				}
				try {
					row.setFechaCreacion(rs.getString("fechaCreacion"));
				} catch (Exception e) {
				errorLog += e.getMessage()
				}
				try {
					row.setFechaImplementacion(rs.getString("fechaImplementacion"));
				} catch (Exception e) {
					errorLog += e.getMessage()
				}
				try {
					row.setFechaImplementacion(rs.getString("fechaImportacion"));
				} catch (Exception e) {
					errorLog += e.getMessage()
				}
				try {
					row.setIsEnabled(rs.getString("isEnabled"));
				} catch (Exception e) {
					errorLog += e.getMessage()
				}
				rows.add(row)
			}
			errorLog+=" salio set";
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
				orderby+="fechaCreacion";
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
					row = new CatEstadoCivilCustom()
					row.setClave(rs.getString("clave"))
					row.setDescripcion(rs.getString("descripcion"))
					row.setIsEliminado(rs.getBoolean("isEliminado"))
					try {
						row.setFechaCreacion(rs.getString("fechaCreacion"))
						row.setFechaImportacion(rs.getString("fechaImportacion"))
						//row.fechaCreacion(new java.util.Date(rs.getDate("fechacreacion")))
						//row.fechaImportacion(new java.util.Date(rs.getDate("fechaimportacion")))
						}catch(Exception e) {
						errorlog+=e.getMessage()
					}
					
					row.setPersistenceId(rs.getLong("persistenceId"))
					row.setPersistenceVersion(rs.getLong("persistenceVersion"))
					row.setusuarioBanner(rs.getString("usuariobanner"))
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
				String consulta = Statements.GET_CATGENERICO
				CatGenericoFiltro row = new CatGenericoFiltro();
				List<CatGenericoFiltro> rows = new ArrayList<CatGenericoFiltro>();
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
				consulta=consulta.replace("[CATALOGO]",object.catalogo)
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
				
				errorLog+= " fecha=="

				rs = pstm.executeQuery()
				while(rs.next()) {
					
					row = new CatGenericoFiltro();
					row.setClave(rs.getString("clave"))
					row.setDescripcion(rs.getString("descripcion"));
					row.setFechaCreacion(rs.getString("fechacreacion"));
					row.setIsEliminado(rs.getBoolean("isEliminado"));
					row.setPersistenceId(rs.getLong("PERSISTENCEID"));
					row.setPersistenceVersion(rs.getLong("persistenceVersion"));
					row.setUsuarioCreacion(rs.getString("usuariocreacion"));
					
					rows.add(row)
				}
				errorLog+=" paso el listado";
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
					 
					 case "CAMPUS":
					 if(where.contains("WHERE")) {
						 where+= " AND "
					 }else {
						 where+= " WHERE "
					 }
						 where +=" LOWER(a.descripcion) ";
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
						row.getCatBachilleratos().setPersistenceId(rs.getLong("CATBACHILLERATOS_PID"))
						}catch(Exception e) {
						errorLog+=e.getMessage()
					}
					
					row.setIsEliminado(rs.getBoolean("isEliminado"));
					row.setPersistenceId(rs.getLong("PERSISTENCEID"));
					row.setPersistenceVersion(rs.getLong("persistenceVersion"));
					row.setUsuarioCreacion(rs.getString("usuariocreacion"))
					row.setCampus(rs.getString("campus"))
					row.setConvenioDescuento(rs.getString("convenioDescuento"))
					
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
	public Result getCatGestionEscolar(String jsonData, RestAPIContext context) {
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
			
			where+=" WHERE isEliminado = false";
			campus+=" AND ("
			for(Integer i=0; i<lstGrupo.size(); i++) {
				String campusMiembro=lstGrupo.get(i);
				campus+="campus='"+campusMiembro+"'"
				if(i==(lstGrupo.size()-1)) {
					campus+=") "
				}
				else {
					campus+=" OR "
				}
			}
			
				String consulta = Statements.GET_CATGESTIONESCOLAR
				CatGestionEscolar row = new CatGestionEscolar();
				List<CatDescuentosCustom> rows = new ArrayList<CatDescuentosCustom>();
				closeCon = validarConexion();
				
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					
					switch(filtro.get("columna")) {
						case "NOMBRE LICENCIATURA":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "LIGA":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(enlace) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "DESCRIPCION DE CARRERA":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(descripcion) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
							 
						case "INSCRIPCIÓN ENERO":
						if(where.contains("WHERE")) {
									   where+= " AND "
								  }else {
									   where+= " WHERE "
								  }
								  where +=" LOWER(inscripcionenero) ";
								  if(filtro.get("operador").equals("Igual a")) {
									   where+="=LOWER('[valor]')"
								  }else {
									   where+="LIKE LOWER('%[valor]%')"
								  }
								  where = where.replace("[valor]", filtro.get("valor"))
								  break;
						case "INSCRIPCIÓN AGOSTO":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(inscripcionagosto) ";
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
						 case "CAMPUS":
							 if(where.contains("WHERE")) {
									   where+= " AND "
								  }else {
									   where+= " WHERE "
								  }
								  where +=" LOWER(campus) ";
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
					case "NOMBRE LICENCIATURA":
					orderby+="nombre";
					break;
					case "LIGA":
					orderby+="enlace";
					break;
					case "DESCRIPCION DE CARRERA":
					orderby+="descripcion";
					break;
					case "INSCRIPCIÓN ENERO":
					orderby+="inscripcionenero";
					break;
					case "INSCRIPCIÓN AGOSTO":
					orderby+="inscripcionagosto";
					break;
					case "PERSISTENCEVERSION":
					orderby+="PERSISTENCEVERSION";
					break;
					case "CAMPUS":
					orderby+="campus";
					break;
					default:
					orderby+="orden"
					break;
				}
				
				orderby+=" "+object.orientation;
				where+=" "+campus 
				consulta=consulta.replace("[WHERE]", where);
				
				pstm = con.prepareStatement(consulta.replace("*", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorLog += " "+consulta
				errorLog+= "consulta"
				errorLog+= consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				
				errorLog+= "fecha=="

				rs = pstm.executeQuery()
				while(rs.next()) {
					
					row = new CatGestionEscolar()
					row.setCampus(rs.getString("campus"))
					row.setCaseId(rs.getString("caseId"))
					row.setDescripcion(rs.getString("descripcion"))
					row.setEnlace(rs.getString("enlace"))
					try {
					row.setFechaCreacion(new java.util.Date(rs.getDate("fechaCreacion").getTime()))
					}catch(Exception e) {
						errorLog+=", "+e.getMessage()
					}
					row.setInscripcionagosto(rs.getString("inscripcionagosto"))
					row.setInscripcionenero(rs.getString("inscripcionenero"))
					row.setIsEliminado(rs.getBoolean("isEliminado"))
					row.setMatematicas(rs.getBoolean("matematicas"))
					row.setNombre(rs.getString("nombre"))
					row.setPersistenceId(rs.getLong("persistenceId"))
					row.setPersistenceVersion(rs.getLong("persistenceVersion"))
					row.setProgramaparcial(rs.getBoolean("programaparcial"))
					row.setPropedeutico(rs.getBoolean("propedeutico"))
					row.setUsuarioCreacion(rs.getString("usuarioCreacion"))
					
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
	
	public Result getCatGenerico(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby="ORDER BY ", errorLog = ""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			//assert object instanceof List;
				String consulta = Statements.GET_CATGENERICO
				CatGenericoFiltro row = new CatGenericoFiltro();
				List<CatGenericoFiltro> rows = new ArrayList<CatGenericoFiltro>();
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
				consulta=consulta.replace("[CATALOGO]",object.catalogo)
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
				
				errorLog+= " fecha=="

				rs = pstm.executeQuery()
				while(rs.next()) {
					
					row = new CatGenericoFiltro();
					row.setClave(rs.getString("clave"))
					row.setDescripcion(rs.getString("descripcion"));
					row.setFechaCreacion(rs.getString("fechacreacion"));
					row.setIsEliminado(rs.getBoolean("isEliminado"));
					row.setPersistenceId(rs.getLong("PERSISTENCEID"));
					row.setPersistenceVersion(rs.getLong("persistenceVersion"));
					row.setUsuarioCreacion(rs.getString("usuariocreacion"));
					
					rows.add(row)
				}
				errorLog+=" paso el listado";
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
	
	public Result getCatNacionalidad(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="WHERE ISELIMINADO=false", orderby="ORDER BY ", errorlog=""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatNacionalidadCustomeFiltro row = new CatNacionalidadCustomeFiltro()
			List<CatNacionalidadCustomeFiltro> rows = new ArrayList<CatNacionalidadCustomeFiltro>();
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
					case "FECHA IMPLEMENTACIÓN":
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
				case "FECHACREACION":
				orderby+="fechacreacion";
				break
				case "FECHAIMPORTACION":
				orderby+="fechaImplementacion";
				break
				default:
				orderby+="ORDEN";
				break;
			}
			orderby+=" "+object.orientation;
			String consulta = Statements.GET_CATNACIONALIDAD;
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
					row = new CatNacionalidadCustomeFiltro()
					row.setClave(rs.getString("clave"))
					row.setCaseId(rs.getString("CASEID"))
					row.setDescripcion(rs.getString("descripcion"))
					row.setIsEliminado(rs.getBoolean("isEliminado"))
					row.setIsEnabled(rs.getBoolean("isEnabled"))
					try {
						row.setFechaCreacion(rs.getString("fechacreacion"))
						row.setFechaImplementacion(rs.getString("fechaimplementacion"))
						}catch(Exception e) {
						errorlog+=e.getMessage()
					}
					
					row.setPersistenceId(rs.getLong("persistenceId"))
					row.setPersistenceVersion(rs.getLong("persistenceVersion"))
					row.setUsuarioBanner(rs.getString("usuariobanner"))
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
	
	
	
	/***********************JESUS OSUNA FIN********************************/
	
	/***********************JOSÉ GARCÍA ******************************/
	public Result getCatPropedeutico(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="WHERE p.ISELIMINADO=false", orderby="ORDER BY ", errorlog=""
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatPropedeuticoCustom row = new CatPropedeuticoCustom();
			List<CatTitulo> rows = new ArrayList<CatTitulo>();
			closeCon = validarConexion();
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
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
					case "CAMPUS":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(c.descripcion) ";
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
					case "FECHAINICIO":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(FECHAINICIO) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "FECHAFINAL":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(FECHAFINAL) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "NOMBRECAMPUS":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(NOMBRECAMPUS) ";
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
						where +=" LOWER(campus) ";
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
				case "Clave":
					orderby+="clave";
				break;
				case "Descripción":
					orderby+="DESCRIPCION";
				break
				case "Usuario creación":
					orderby+="USUARIOCREACION";
				break
				case "Fecha creación":
					orderby+="FECHACREACION";
				break 
				case "Fecha inicio":
					orderby+="FECHAINICIO";
				break
				case "Fecha final":
					orderby+="FECHAFINAL";
				break
				case "Nombre campus":
					orderby+="NOMBRECAMPUS";
				break
			}
			
			if(orderby.equals("ORDER BY ")) {
				orderby+="p.PERSISTENCEID";
			}
			
			orderby+=" "+object.orientation;
			
			String consulta = Statements.GET_CATPROPEDEUTICO;
			consulta=consulta.replace("[WHERE]", where);
			errorlog+="consulta:"
			errorlog+=consulta.replace("*", "COUNT(p.PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "")
				pstm = con.prepareStatement(consulta.replace("*", "COUNT(p.PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
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
				rs = pstm.executeQuery();
				while(rs.next()) {
					Date creacion = sdf.parse(rs.getString("fechacreacion"));
					Date inicio = sdf.parse(rs.getString("fechaInicio"));
					Date fin = sdf.parse(rs.getString("fechaFinal"));
					
					row = new CatPropedeuticoCustom()
					row.setClave(rs.getString("clave"));
					row.setDescripcion(rs.getString("descripcion"));
					row.setIsEliminado(rs.getBoolean("isEliminado"));
					row.setPersistenceId(rs.getLong("persistenceId"));
					row.setPersistenceVersion(rs.getLong("persistenceVersion"));
					row.setUsuarioCreacion(rs.getString("usuarioCreacion"));
					row.setFechaCreacion(sdf.format(creacion));
					row.setFechaInicio(sdf.format(inicio));
					row.setFechaFinal(sdf.format(fin));
					row.setNombreCampus(rs.getString("nombreCampus"));
					CatCampus campus = new CatCampus();
					campus.setDescripcion(rs.getString("campus"))
					row.setCampus(campus);
					
					rows.add(row);
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
	/***********************JOSÉ GARCÍA FIN ******************************/

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
								 case "CAMPUS":
								 if(where.contains("WHERE")) {
										   where+= " AND "
									  }else {
										   where+= " WHERE "
									  }
									  where +=" LOWER(nombrecampus) ";
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
						
						row.setIsEliminado(rs.getBoolean("isEliminado"));
						row.setPersistenceId(rs.getLong("PERSISTENCEID"));
						row.setPersistenceVersion(rs.getLong("persistenceVersion"));
						row.setUsuarioBanner(rs.getString("usuariobanner"));
						
						row.setIsEnabled(rs.getBoolean("isEnabled"))
						row.setPersistenceId_string(rs.getString("PERSISTENCEID"))
						row.setNombreCampus(rs.getString("NombreCampus"))
						try {
							row.setFechaImportacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("FECHAIMPORTACION")));
							row.setIsCuatrimestral(rs.getBoolean("isCuatrimestral"))
							row.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rs.getString("fechacreacion")))
							} catch (Exception e) {
							errorLog+=" cuatrimestral " +e.getMessage()
						}
						
						
						rows.add(row)
					}
					
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
	
	/***********************ERIC ROSAS FIN********************************/
}
