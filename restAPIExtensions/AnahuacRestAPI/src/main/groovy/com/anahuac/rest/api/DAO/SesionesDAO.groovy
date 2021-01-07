package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatNotificacionesFirma
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.Calendario
import com.anahuac.rest.api.Entity.Custom.PruebaCustom
import com.anahuac.rest.api.Entity.Custom.PruebasCustom
import com.anahuac.rest.api.Entity.Custom.ResponsableCustom
import com.anahuac.rest.api.Entity.Custom.SesionCustom
import com.anahuac.rest.api.Entity.Custom.SesionesAspiranteCustom
import com.anahuac.rest.api.Entity.db.Responsable
import com.anahuac.rest.api.Entity.db.ResponsableDisponible
import com.anahuac.rest.api.Entity.db.Sesion_Aspirante
import com.anahuac.rest.api.Entity.db.CatTipoPrueba
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.SimpleDateFormat

import org.apache.commons.collections4.functors.ComparatorPredicate.Criterion
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion

class SesionesDAO {
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	ResultSet rs2;

	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
	}
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true
		}
	}
	public Result getCatTipoPrueba(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby="ORDER BY ", errorlog=""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatTipoPrueba row = new CatTipoPrueba()
			List<CatTipoPrueba> rows = new ArrayList<CatTipoPrueba>();
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
				}
			}
			orderby+=object.orderby
			if(orderby.equals("ORDER BY ")) {
				orderby+="PERSISTENCEID";
			}
			orderby+=" "+object.orientation;
			String consulta = Statements.GET_CATTIPOPRUEBA;
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
					row = new CatTipoPrueba()
					
					row.setDescripcion(rs.getString("descripcion"))
					row.setPersistenceId(rs.getLong("persistenceId"))
					row.setPersistenceVersion(rs.getLong("persistenceVersion"))
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
	public Result getCatPsicologo(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby="ORDER BY ", errorlog=""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			Responsable row = new Responsable()
			List<Responsable> rows = new ArrayList<Responsable>();
			closeCon = validarConexion();
			
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					case "NOMBRE":
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
				}
			}
			orderby+=object.orderby
			if(orderby.equals("ORDER BY ")) {
				orderby+="PERSISTENCEID";
			}
			orderby+=" "+object.orientation;
			String consulta = Statements.GET_CATPSICOLOGO;
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
					row = new CatTipoPrueba()
					
					row.setNombre(rs.getString("nombre"))
					row.setPersistenceId(rs.getLong("persistenceId"))
					row.setPersistenceVersion(rs.getLong("persistenceVersion"))
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
	public Result getUserBonita(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="";
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
				group+=" AND ("
				for(Integer i=0; i<lstGrupo.size(); i++) {
					String campusMiembro=lstGrupo.get(i);
					group+="g.name='"+campusMiembro+"'"
					if(i==(lstGrupo.size()-1)) {
						group+=") "
					}
					else {
						group+=" OR "
					}
				}
				
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						case "NOMBRE":
						if(where.contains("WHERE")) {
							where+= " OR "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(firstname) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "APELLIDO":
						if(where.contains("WHERE")) {
								  where+= " OR "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(lastname) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "GRUPO":
							 if(filtro.get("operador").equals("Igual a")) {
								  group+="AND LOWER(g.name)=LOWER('[valor]')"
							 }else {
								  group+="AND LOWER(g.name) LIKE LOWER('[valor]')"
							 }
							 group = group.replace("[valor]", filtro.get("valor"))
							 break;
						case "ROL":
						if(where.contains("WHERE")) {
								  where+= " OR "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(r.name) ";
							 
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
					case "NOMBRE":
					orderby+="firstname";
					break;
					case "APELLIDO":
					orderby+="lastname";
					break;
					case "GRUPO":
					orderby+="g.name";
					break;
					case "ROL":
					orderby+="r.name";
					break;
					default:
					orderby+="u.id"
					break;
				}
				orderby+=" "+object.orientation;
				String consulta = Statements.GET_USER_BONITA;
				consulta=consulta.replace("[ROLE]", role);
				consulta=consulta.replace("[GROUP]", group);
				consulta=consulta.replace("[WHERE]", where);
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexionBonita();
				pstm = con.prepareStatement(consulta.replace("u.id, u.firstname, u.lastname, g.name as grupo, r.name as rol", "COUNT(u.id) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs= pstm.executeQuery()
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
				resultado.setError_info(errorlog)
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
	public Result insertSesion(SesionCustom sesion) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
				List<SesionCustom> rows = new ArrayList<SesionCustom>();
				closeCon = validarConexion();
				con.setAutoCommit(false)
				if(sesion.getPersistenceId()>0) {
					pstm = con.prepareStatement(Statements.UPDATE_SESION)
				}else {
					pstm = con.prepareStatement(Statements.INSERT_SESION, Statement.RETURN_GENERATED_KEYS)
				}
				pstm.setString(1, sesion.getNombre())
				pstm.setString(2, sesion.getDescripcion())
				try {
					pstm.setDate(3, convert(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:00.000'Z'").parse(sesion.getFecha_inicio())))
				}catch(Exception e) {
					pstm.setDate(3, convert(new SimpleDateFormat("yyyy-MM-dd").parse(sesion.getFecha_inicio())))
				}
				(sesion.getIsmedicina()==null)?pstm.setNull(4, java.sql.Types.NULL):pstm.setBoolean(4, sesion.getIsmedicina())
				(sesion.getBachillerato_pid()==null)?pstm.setNull(5, java.sql.Types.NULL):pstm.setLong(5, sesion.getBachillerato_pid())
				(sesion.getEstado_pid()==null)?pstm.setNull(6, java.sql.Types.NULL):pstm.setLong(6, sesion.getEstado_pid())
				(sesion.getPais_pid()==null)?pstm.setNull(7, java.sql.Types.NULL):pstm.setLong(7, sesion.getPais_pid())
				(sesion.getBorrador()==null)?pstm.setBoolean(8, true):pstm.setBoolean(8, sesion.getBorrador())
				pstm.setLong(9, sesion.getCampus_pid())
				pstm.setString(10, sesion.getTipo())
				(sesion.getCiudad_pid()==null)?pstm.setNull(11, java.sql.Types.NULL):pstm.setLong(11, sesion.getCiudad_pid())
				if(sesion.getPersistenceId()>0) {
					pstm.setLong(12, sesion.getPersistenceId())
					pstm.executeUpdate()
				}
				else {
					pstm.executeUpdate();
					rs = pstm.getGeneratedKeys();
					if(rs.next()) {
						sesion.setPersistenceId(rs.getLong("persistenceid"))
					}
				}
				
					
				for (PruebaCustom prueba: sesion.getPruebas()) {
					if(prueba.getPersistenceId()>0) {
						pstm = con.prepareStatement(Statements.UPDATE_PRUEBA)
					}else {
						pstm = con.prepareStatement(Statements.INSERT_PRUEBA, Statement.RETURN_GENERATED_KEYS)
					}
					pstm.setString(1, prueba.getNombre())
					pstm.setDate(2, convert(new SimpleDateFormat("yyyy-MM-dd").parse(prueba.getAplicacion())))
					pstm.setString(3, prueba.getEntrada())
					pstm.setString(4, prueba.getSalida())
					pstm.setInt(5, prueba.getRegistrados())
					pstm.setDate(6,convert(new SimpleDateFormat("yyyy-MM-dd").parse(prueba.getUltimo_dia_inscripcion())))
					pstm.setInt(7, prueba.getCupo())
					pstm.setString(8, prueba.getLugar())
					(prueba.getCampus_pid()==null)?pstm.setNull(9, java.sql.Types.NULL):pstm.setLong(9, prueba.getCampus_pid())
					pstm.setString(10, prueba.getCalle())
					pstm.setString(11, prueba.getNumero_int())
					pstm.setString(12, prueba.getNumero_ext())
					pstm.setString(13, prueba.getColonia())
					pstm.setString(14, prueba.getCodigo_postal())
					pstm.setString(15, prueba.getMunicipio())
					(prueba.getPais_pid()==null)?pstm.setNull(16, java.sql.Types.NULL):pstm.setLong(16, prueba.getPais_pid())
					(prueba.getEstado_pid()==null)?pstm.setNull(17, java.sql.Types.NULL):pstm.setLong(17, prueba.getEstado_pid())
					pstm.setBoolean(18, (prueba.getIseliminado()==null)?false:prueba.getIseliminado())
					pstm.setLong(19, sesion.getPersistenceId())
					pstm.setString(20, prueba.getDuracion())
					pstm.setString(21, prueba.getDescripcion())
					pstm.setLong(22, prueba.getCattipoprueba_pid())
					if(prueba.getPersistenceId()>0) {
						pstm.setLong(23, prueba.getPersistenceId())
						
						pstm.executeUpdate();
					}else {
						pstm.executeUpdate()
						rs = pstm.getGeneratedKeys();
						if(rs.next()) {
							prueba.setPersistenceId(rs.getLong("persistenceId"))
						}
					}
					
					if(prueba.getCattipoprueba_pid()==1L) {
					if(prueba.getCambioDuracion()) {
						pstm = con.prepareStatement(Statements.DELETE_IF_CAMBIO_DURACION)
						pstm.setLong(1, prueba.getPersistenceId())
					}
					
					for (ResponsableCustom responsable: prueba.getPsicologos()) {
						for (ResponsableDisponible disponible:responsable.getLstFechasDisponibles()) {
							if(disponible.getPersistenceId()>0) {
								pstm = con.prepareStatement(Statements.UPDATE_RESPONSABLEDISPONIBLE)
							}else {
								pstm = con.prepareStatement(Statements.INSERT_RESPONSABLEDISPONIBLE, Statement.RETURN_GENERATED_KEYS)
							}
							
							pstm.setString(1, disponible.getHorario())
							pstm.setBoolean(2, disponible.getDisponible())
							pstm.setLong(3, responsable.getId())
							pstm.setLong(4,prueba.getPersistenceId())
							pstm.setString(5, responsable.getLicenciaturas())
							if(disponible.getPersistenceId()>0){
								pstm.setBoolean(6, (responsable.getIseliminado()==null)?false:responsable.getIseliminado())
								pstm.setLong(7, disponible.getPersistenceId())
								pstm.executeUpdate()
							}else {
								pstm.executeUpdate()
								rs = pstm.getGeneratedKeys();
								if (rs.next()) {
									disponible.setPersistenceId(rs.getLong("persistenceId"))
									responsable.setPersistenceId(rs.getLong("persistenceId"))
								}
							}
						}
					}
					}else {
						for (ResponsableCustom responsable: prueba.getPsicologos()) {
							if(responsable.getPersistenceId()>0) {
										   
								pstm = con.prepareStatement(Statements.UPDATE_RESPONSABLEDISPONIBLE)
							}else {
								pstm = con.prepareStatement(Statements.INSERT_RESPONSABLEDISPONIBLE, Statement.RETURN_GENERATED_KEYS)
							}
							pstm.setNull(1, java.sql.Types.NULL)
							pstm.setNull(2, java.sql.Types.NULL)
							pstm.setLong(3, responsable.getId())
							pstm.setLong(4,prueba.getPersistenceId())
							pstm.setNull(5,java.sql.Types.NULL)
							if(responsable.getPersistenceId()>0){
								pstm.setBoolean(6, (responsable.getIseliminado()==null)?false:responsable.getIseliminado())
								pstm.setLong(7, responsable.getPersistenceId())
								pstm.executeUpdate()
							}else {
							pstm.executeUpdate()
							rs = pstm.getGeneratedKeys();
							if (rs.next()) {
								responsable.setLstFechasDisponibles(new ArrayList())
								responsable.getLstFechasDisponibles().add(new ResponsableDisponible())
								responsable.getLstFechasDisponibles().get(0).setPersistenceId(rs.getLong("persistenceId"))
								responsable.setPersistenceId(rs.getLong("persistenceId"))
							}
							}
							
						}
					}
					
					
				}
					
				
				con.commit();
				rows.add(sesion)
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result insertSesionAspirante(Sesion_Aspirante sesionAspirante) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
				List<Sesion_Aspirante> rows = new ArrayList<Sesion_Aspirante>();
				closeCon = validarConexion();
				con.setAutoCommit(false)
				pstm = con.prepareStatement(Statements.GET_REGISTRADOS_CUPO)
				pstm.setLong(1, sesionAspirante.getSesiones_pid())
				rs = pstm.executeQuery()
				while(rs.next()) {
					if((rs.getInt("registrados")+1)>rs.getInt("cupo")) {
						throw new Exception("No hay cupo");
					}
				}
				
				pstm = con.prepareStatement(Statements.GET_OCUPADO_RESPONSABLE_DISPONIBLE)
				pstm.setLong(1, sesionAspirante.getResponsabledisponible_pid())
				rs = pstm.executeQuery()
				if(rs.next()) {
					throw new Exception("Se encuentra ocupado")
				}
				
				pstm = con.prepareStatement(Statements.UPDATE_REGISTRADOS)
				pstm.setLong(1, sesionAspirante.getSesiones_pid())
				pstm.setLong(2, sesionAspirante.getSesiones_pid())
				pstm.executeUpdate();
				
				pstm = con.prepareStatement(Statements.UPDATE_OCUPADO_RESPONSABLE_DISPONIBLE)
				pstm.setLong(1, sesionAspirante.getResponsabledisponible_pid())
				pstm.executeUpdate();
				
				pstm = con.prepareStatement(Statements.UPDATE_REGISTRADOS_PRUEBAS)
				pstm.setLong(1, sesionAspirante.getResponsabledisponible_pid())
				pstm.setLong(2, sesionAspirante.getResponsabledisponible_pid())
				pstm.executeUpdate();
				
				pstm = con.prepareStatement(Statements.INSERT_SESION_ASPIRANTE, Statement.RETURN_GENERATED_KEYS)
				pstm.setString(1, sesionAspirante.getUsername())
				pstm.setLong(2, sesionAspirante.getSesiones_pid())
				pstm.setLong(3, sesionAspirante.getResponsabledisponible_pid())
				
				pstm.executeUpdate();
				rs = pstm.getGeneratedKeys()
				
				if(rs.next()) {
					sesionAspirante.setPersistenceId(rs.getLong("persistenceId"))
				}
				con.commit();
				rows.add(sesionAspirante)
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Result getSesionesCalendario(String fecha,String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<Calendario> lstCalendario = new ArrayList();
		Calendario calendario = new Calendario();
		String where=" WHERE (s.FECHA_INICIO between ? and  ?)", consulta =""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					case "CAMPUS":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" s.campus_pid ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor]"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
				}
			}
			Calendar calendar = dateToCalendar(new SimpleDateFormat("yyyy-MM-dd").parse(fecha));
			calendar.add(Calendar.MONTH, -1);
			Calendar calendar2 = dateToCalendar(new SimpleDateFormat("yyyy-MM-dd").parse(fecha));
			calendar2.add(Calendar.MONTH, 1);
			int lastDate = calendar2.getActualMaximum(Calendar.DATE);
			calendar2.set(Calendar.DATE, lastDate);
			closeCon = validarConexion();
			consulta = Statements.GET_SESIONES_CALENDARIO.replace("[WHERE]", where);
			
			pstm = con.prepareStatement(Statements.GET_SESIONES_CALENDARIO.replace("[WHERE]", where))
			pstm.setDate(1, convert(calendar.getTime()))
			pstm.setDate(2, convert(calendar2.getTime()))
			rs = pstm.executeQuery()
			while(rs.next()) {
				calendario = new Calendario();
				calendario.setColor(rs.getString("color"))
				calendario.setEnd_date(rs.getString("end_date"))
				calendario.setId(rs.getLong("id"))
				calendario.setStart_date(rs.getString("start_date"))
				calendario.setText(rs.getString("text"))
				lstCalendario.add(calendario)
			}
			resultado.setError_info(consulta)
			resultado.setData(lstCalendario)
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result getSesionesCalendarioAspirante(String fecha,String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<Calendario> lstCalendario = new ArrayList();
		Calendario calendario = new Calendario();
		String where=" WHERE (s.FECHA_INICIO>=now() and s.FECHA_INICIO between ? and ?) AND s.borrador=false ", consulta =""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					case "CAMPUS":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" s.campus_pid ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor]"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
					case "BACHILLERATO":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" (s.preparatoria_pid";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor] OR s.preparatoria_pid=0)"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
					case "RESIDENCIA":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" s.tipo ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor]"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
				}
			}
			Calendar calendar = dateToCalendar(new SimpleDateFormat("yyyy-MM-dd").parse(fecha));
			calendar.add(Calendar.MONTH, -1);
			Calendar calendar2 = dateToCalendar(new SimpleDateFormat("yyyy-MM-dd").parse(fecha));
			calendar2.add(Calendar.MONTH, 1);
			int lastDate = calendar2.getActualMaximum(Calendar.DATE);
			calendar2.set(Calendar.DATE, lastDate);
			closeCon = validarConexion();
			consulta = Statements.GET_SESIONES_CALENDARIO.replace("[WHERE]", where);
			
			pstm = con.prepareStatement(Statements.GET_SESIONES_CALENDARIO.replace("[WHERE]", where))
			pstm.setDate(1, convert(calendar.getTime()))
			pstm.setDate(2, convert(calendar2.getTime()))
			rs = pstm.executeQuery()
			while(rs.next()) {
				calendario = new Calendario();
				calendario.setColor(rs.getString("color"))
				calendario.setEnd_date(rs.getString("end_date"))
				calendario.setId(rs.getLong("id"))
				calendario.setStart_date(rs.getString("start_date"))
				calendario.setText(rs.getString("text"))
				lstCalendario.add(calendario)
			}
			resultado.setError_info(consulta)
			resultado.setData(lstCalendario)
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError_info(consulta)
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	


	
	
	public Result getSesion(Long persistenceId, RestAPIContext context) {


		Result resultado = new Result();
		Boolean closeCon = false;
		List<SesionCustom> lstSesion = new ArrayList();
		SesionCustom sesion = new SesionCustom()
		String where=" ", consulta =""
		try {

			closeCon = validarConexion();
	
			
			pstm = con.prepareStatement(Statements.GET_SESION_ID)
			pstm.setLong(1, persistenceId)
			rs = pstm.executeQuery()
			if(rs.next()) {
				sesion = new SesionCustom();
				sesion.setBachillerato_pid(rs.getLong("PREPARATORIA_PID"))
				sesion.setBorrador(rs.getBoolean("borrador"))
				sesion.setDescripcion(rs.getString("descripcion"))
				sesion.setEstado_pid(rs.getLong("estado_pid"))
				sesion.setFecha_inicio(rs.getString("fecha_inicio"))
				sesion.setIsmedicina(rs.getBoolean("ismedicina"))
				sesion.setNombre(rs.getString("nombre"))
				sesion.setPais_pid(rs.getLong("pais_pid"))
				sesion.setPersistenceId(rs.getLong("persistenceId"))
				sesion.setPersistenceVersion(rs.getLong("persistenceVersion"))
				sesion.setPruebas(new ArrayList())
				sesion.setCampus_pid(rs.getLong("campus_pid"))
				sesion.setTipo(rs.getString("tipo"))
				sesion.setCiudad_pid(rs.getLong("ciudad_pid"))
				pstm = con.prepareStatement(Statements.GET_PRUEBAS_SESION_PID)
				pstm.setLong(1, sesion.getPersistenceId())
				rs = pstm.executeQuery()
				while(rs.next()) {
					
					PruebaCustom p = new PruebaCustom()
					p.setAplicacion(rs.getString("aplicacion"))
					p.setCalle(rs.getString("calle"))
					p.setCampus_pid(rs.getLong("campus_pid"))
					p.setCattipoprueba_pid(rs.getLong("cattipoprueba_pid"))
					p.setCodigo_postal(rs.getString("codigo_postal"))
					p.setColonia(rs.getString("colonia"))
					p.setCupo(rs.getInt("cupo"))
					p.setDescripcion(rs.getString("descripcion"))
					p.setDuracion(rs.getString("duracion"))
					p.setEntrada(rs.getString("entrada"))
					p.setEstado_pid(rs.getLong("estado_pid"))
					p.setIseliminado(rs.getBoolean("iseliminado"))
					p.setLugar(rs.getString("lugar"))
					p.setMunicipio(rs.getString("municipio"))
					p.setNombre(rs.getString("nombre"))
					p.setNumero_ext(rs.getString("numero_ext"))
					p.setNumero_int(rs.getString("numero_int"))
					p.setPais_pid(rs.getLong("pais_pid"))
					p.setPersistenceId(rs.getLong("persistenceId"))
					p.setPersistenceVersion(rs.getLong("persistenceVersion"))
					p.setRegistrados(rs.getInt("registrados"))
					p.setSalida(rs.getString("salida"))
					p.setSesion_pid(rs.getLong("sesion_pid"))
					p.setUltimo_dia_inscripcion(rs.getString("ultimo_dia_inscripcion"))
					p.setTipo(new CatTipoPrueba())
					p.getTipo().setDescripcion(rs.getString("tipo"))
					p.setPsicologos(new ArrayList())
					
					User usr;
					UserMembership membership
					if(rs.getLong("RESPONSABLEID")>0) {
						usr = context.getApiClient().getIdentityAPI().getUser(rs.getLong("RESPONSABLEID"))
						membership=context.getApiClient().getIdentityAPI().getUserMemberships(usr.getId(), 0, 100, UserMembershipCriterion.GROUP_NAME_ASC).get(0)
					}
					
					if(sesion.getPruebas().contains(p)) {
						ResponsableCustom psi = new ResponsableCustom()
						ResponsableDisponible fd = new ResponsableDisponible()
						fd.setDisponible(rs.getBoolean("disponible"))
						fd.setOcupado(rs.getBoolean("ocupado"))
						fd.setHorario(rs.getString("horario"))
						fd.setPersistenceId(rs.getLong("rid"))
						fd.setResponsableId(rs.getLong("RESPONSABLEID"))
						psi.setLicenciaturas(rs.getString("licenciaturas"))
						psi.setPersistenceId(rs.getLong("rid"))
						
						try {
							psi.setFirstname(usr.getFirstName())
							psi.setLastname(usr.getLastName())
							psi.setGrupo(membership.groupName)
							psi.setRol(membership.roleName)
						}catch(Exception e) {
							resultado.setError_info(e.getMessage())
						}
						
						
						psi.setId(rs.getLong("RESPONSABLEID"))
					
						if(sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().contains(psi)) {
							sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().get(sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().indexOf(psi)).getLstFechasDisponibles().add(fd)
							
						}else {
							if(fd.getResponsableId()>0) {
							psi.setLstFechasDisponibles(new ArrayList())
							psi.getLstFechasDisponibles().add(fd)
							sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().add(psi)
							}
						}
					}else {
						ResponsableCustom psi = new ResponsableCustom()
						ResponsableDisponible fd = new ResponsableDisponible()
						fd.setDisponible(rs.getBoolean("disponible"))
						fd.setOcupado(rs.getBoolean("ocupado"))
						fd.setHorario(rs.getString("horario"))
						psi.setId(rs.getLong("RESPONSABLEID"))
						fd.setPersistenceId(rs.getLong("rid"))
						fd.setResponsableId(rs.getLong("RESPONSABLEID"))
						psi.setLicenciaturas(rs.getString("licenciaturas"))
						psi.setPersistenceId(rs.getLong("rid"))
						psi.setLstFechasDisponibles(new ArrayList())
						try {
							psi.setGrupo(membership.groupName)
							psi.setRol(membership.roleName)
							psi.setFirstname(usr.getFirstName())
							psi.setLastname(usr.getLastName())
							
							psi.getLstFechasDisponibles().add(fd)
							p.getPsicologos().add(psi)
						}catch(Exception e) {
							resultado.setError_info(e.getMessage())
						}
						
						
						
						sesion.getPruebas().add(p)
					}
				}
			}
			lstSesion.add(sesion)
			resultado.setError_info(consulta)
			resultado.setData(lstSesion)
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	public Result getSesionAspirante(Long persistenceId, RestAPIContext context) {
		
		
				Result resultado = new Result();
				Boolean closeCon = false;
				List<SesionCustom> lstSesion = new ArrayList();
				SesionCustom sesion = new SesionCustom()
				String where=" ", consulta =""
				try {
		
					closeCon = validarConexion();
			
					
					pstm = con.prepareStatement(Statements.GET_SESION_ID)
					pstm.setLong(1, persistenceId)
					rs = pstm.executeQuery()
					if(rs.next()) {
						sesion = new SesionCustom();
						sesion.setBachillerato_pid(rs.getLong("PREPARATORIA_PID"))
						sesion.setBorrador(rs.getBoolean("borrador"))
						sesion.setDescripcion(rs.getString("descripcion"))
						sesion.setEstado_pid(rs.getLong("estado_pid"))
						sesion.setFecha_inicio(rs.getString("fecha_inicio"))
						sesion.setIsmedicina(rs.getBoolean("ismedicina"))
						sesion.setNombre(rs.getString("nombre"))
						sesion.setPais_pid(rs.getLong("pais_pid"))
						sesion.setPersistenceId(rs.getLong("persistenceId"))
						sesion.setPersistenceVersion(rs.getLong("persistenceVersion"))
						sesion.setPruebas(new ArrayList())
						sesion.setCampus_pid(rs.getLong("campus_pid"))
						sesion.setTipo(rs.getString("tipo"))
						sesion.setCiudad_pid(rs.getLong("ciudad_pid"))
						pstm = con.prepareStatement(Statements.GET_PRUEBAS_SESION_PID_ASPIRANTE)
						pstm.setLong(1, sesion.getPersistenceId())
						rs = pstm.executeQuery()
						while(rs.next()) {
							
							PruebaCustom p = new PruebaCustom()
							p.setAplicacion(rs.getString("aplicacion"))
							p.setCalle(rs.getString("calle"))
							p.setCampus_pid(rs.getLong("campus_pid"))
							p.setCattipoprueba_pid(rs.getLong("cattipoprueba_pid"))
							p.setCodigo_postal(rs.getString("codigo_postal"))
							p.setColonia(rs.getString("colonia"))
							p.setCupo(rs.getInt("cupo"))
							p.setDescripcion(rs.getString("descripcion"))
							p.setDuracion(rs.getString("duracion"))
							p.setEntrada(rs.getString("entrada"))
							p.setEstado_pid(rs.getLong("estado_pid"))
							p.setIseliminado(rs.getBoolean("iseliminado"))
							p.setLugar(rs.getString("lugar"))
							p.setMunicipio(rs.getString("municipio"))
							p.setNombre(rs.getString("nombre"))
							p.setNumero_ext(rs.getString("numero_ext"))
							p.setNumero_int(rs.getString("numero_int"))
							p.setPais_pid(rs.getLong("pais_pid"))
							p.setPersistenceId(rs.getLong("persistenceId"))
							p.setPersistenceVersion(rs.getLong("persistenceVersion"))
							p.setRegistrados(rs.getInt("registrados"))
							p.setSalida(rs.getString("salida"))
							p.setSesion_pid(rs.getLong("sesion_pid"))
							p.setUltimo_dia_inscripcion(rs.getString("ultimo_dia_inscripcion"))
							p.setTipo(new CatTipoPrueba())
							p.getTipo().setDescripcion(rs.getString("tipo"))
							p.setPsicologos(new ArrayList())
							
							User usr;
							UserMembership membership
							if(rs.getLong("RESPONSABLEID")>0) {
								usr = context.getApiClient().getIdentityAPI().getUser(rs.getLong("RESPONSABLEID"))
								membership=context.getApiClient().getIdentityAPI().getUserMemberships(usr.getId(), 0, 100, UserMembershipCriterion.GROUP_NAME_ASC).get(0)
							}
							
							if(sesion.getPruebas().contains(p)) {
								ResponsableCustom psi = new ResponsableCustom()
								ResponsableDisponible fd = new ResponsableDisponible()
								fd.setDisponible(rs.getBoolean("disponible"))
								fd.setOcupado(rs.getBoolean("ocupado"))
								fd.setHorario(rs.getString("horario"))
								fd.setPersistenceId(rs.getLong("rid"))
								fd.setResponsableId(rs.getLong("RESPONSABLEID"))
								psi.setLicenciaturas(rs.getString("licenciaturas"))
								psi.setPersistenceId(rs.getLong("rid"))
								
								try {
									psi.setFirstname(usr.getFirstName())
									psi.setLastname(usr.getLastName())
									psi.setGrupo(membership.groupName)
									psi.setRol(membership.roleName)
								}catch(Exception e) {
									resultado.setError_info(e.getMessage())
								}
								
								
								psi.setId(rs.getLong("RESPONSABLEID"))
							
								if(sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().contains(psi)) {
									sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().get(sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().indexOf(psi)).getLstFechasDisponibles().add(fd)
									
								}else {
									if(fd.getResponsableId()>0) {
									psi.setLstFechasDisponibles(new ArrayList())
									psi.getLstFechasDisponibles().add(fd)
									sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().add(psi)
									}
								}
							}else {
								ResponsableCustom psi = new ResponsableCustom()
								ResponsableDisponible fd = new ResponsableDisponible()
								fd.setDisponible(rs.getBoolean("disponible"))
								fd.setOcupado(rs.getBoolean("ocupado"))
								fd.setHorario(rs.getString("horario"))
								psi.setId(rs.getLong("RESPONSABLEID"))
								fd.setPersistenceId(rs.getLong("rid"))
								fd.setResponsableId(rs.getLong("RESPONSABLEID"))
								psi.setLicenciaturas(rs.getString("licenciaturas"))
								psi.setPersistenceId(rs.getLong("rid"))
								psi.setLstFechasDisponibles(new ArrayList())
								try {
									psi.setGrupo(membership.groupName)
									psi.setRol(membership.roleName)
									psi.setFirstname(usr.getFirstName())
									psi.setLastname(usr.getLastName())
									
									psi.getLstFechasDisponibles().add(fd)
									p.getPsicologos().add(psi)
								}catch(Exception e) {
									resultado.setError_info(e.getMessage())
								}
								
								
								
								sesion.getPruebas().add(p)
							}
						}
					}
					pstm = con.prepareStatement(Statements.GET_PRUEBAS_SESION_ENTREVISTA_ASPIRANTE)
					pstm.setLong(1, sesion.getPersistenceId())
					pstm.setLong(2, sesion.getPersistenceId())
					rs=pstm.executeQuery()
					if(rs.next()) {
						
						PruebaCustom p = new PruebaCustom()
						p.setAplicacion(rs.getString("aplicacion"))
						p.setCalle(rs.getString("calle"))
						p.setCampus_pid(rs.getLong("campus_pid"))
						p.setCattipoprueba_pid(rs.getLong("cattipoprueba_pid"))
						p.setCodigo_postal(rs.getString("codigo_postal"))
						p.setColonia(rs.getString("colonia"))
						p.setCupo(rs.getInt("cupo"))
						p.setDescripcion(rs.getString("descripcion"))
						p.setDuracion(rs.getString("duracion"))
						p.setEntrada(rs.getString("entrada"))
						p.setEstado_pid(rs.getLong("estado_pid"))
						p.setIseliminado(rs.getBoolean("iseliminado"))
						p.setLugar(rs.getString("lugar"))
						p.setMunicipio(rs.getString("municipio"))
						p.setNombre(rs.getString("nombre"))
						p.setNumero_ext(rs.getString("numero_ext"))
						p.setNumero_int(rs.getString("numero_int"))
						p.setPais_pid(rs.getLong("pais_pid"))
						p.setPersistenceId(rs.getLong("persistenceId"))
						p.setPersistenceVersion(rs.getLong("persistenceVersion"))
						p.setRegistrados(rs.getInt("registrados"))
						p.setSalida(rs.getString("salida"))
						p.setSesion_pid(rs.getLong("sesion_pid"))
						p.setUltimo_dia_inscripcion(rs.getString("ultimo_dia_inscripcion"))
						p.setTipo(new CatTipoPrueba())
						p.getTipo().setDescripcion(rs.getString("tipo"))
						p.setPsicologos(new ArrayList())
						
						User usr;
						UserMembership membership
						if(rs.getLong("RESPONSABLEID")>0) {
							usr = context.getApiClient().getIdentityAPI().getUser(rs.getLong("RESPONSABLEID"))
							membership=context.getApiClient().getIdentityAPI().getUserMemberships(usr.getId(), 0, 100, UserMembershipCriterion.GROUP_NAME_ASC).get(0)
						}
						sesion.getPruebas().add(p)
						pstm = con.prepareStatement(Statements.GET_HORARIOS_PRUEBAS_ENTREVISTA_ASPIRANTE)
						pstm.setLong(1, p.getPersistenceId())
						rs = pstm.executeQuery()
						while(rs.next()) {
							ResponsableCustom psi = new ResponsableCustom()
							ResponsableDisponible fd = new ResponsableDisponible()
							fd.setDisponible(rs.getBoolean("disponible"))
							fd.setOcupado(rs.getBoolean("ocupado"))
							fd.setHorario(rs.getString("horario"))
							fd.setPersistenceId(rs.getLong("rid"))
							fd.setResponsableId(rs.getLong("RESPONSABLEID"))
							psi.setLicenciaturas(rs.getString("licenciaturas"))
							psi.setPersistenceId(rs.getLong("rid"))
							
							try {
								psi.setFirstname(usr.getFirstName())
								psi.setLastname(usr.getLastName())
								psi.setGrupo(membership.groupName)
								psi.setRol(membership.roleName)
							}catch(Exception e) {
								resultado.setError_info(e.getMessage())
							}
							
							
							psi.setId(rs.getLong("RESPONSABLEID"))
						
							if(sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().contains(psi)) {
								sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().get(sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().indexOf(psi)).getLstFechasDisponibles().add(fd)
								
							}else {
								if(fd.getResponsableId()>0) {
								psi.setLstFechasDisponibles(new ArrayList())
								psi.getLstFechasDisponibles().add(fd)
								sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().add(psi)
								}
							}
						}
						
					}
					lstSesion.add(sesion)
					resultado.setError_info(consulta)
					resultado.setData(lstSesion)
					resultado.setSuccess(true)
				} catch (Exception e) {
					resultado.setSuccess(false);
					resultado.setError(e.getMessage());
				}finally {
					if(closeCon) {
						new DBConnect().closeObj(con, stm, rs, pstm)
					}
				}
				return resultado;
			}
	
	
	
	
	
	
	public Result getSesionesCalendarizadas(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		List<PruebasCustom> lstSesion = new ArrayList();
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="";
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				String consulta = Statements.GET_SESIONESCALENDARIZADAS
				PruebaCustom row = new PruebaCustom();
				List<PruebasCustom> rows = new ArrayList<PruebasCustom>();
				closeCon = validarConexion();
				errorlog+="llego a filtro "+object.lstFiltro.toString()
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
					case "ID":
						where +=" AND P.persistenceid ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE":
						where +=" AND LOWER(P.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ALUMNOS GENERALES":
						where +=" AND CAST(P.cupo as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ALUMNOS ASIGNADOS":
						where +=" AND CAST(P.registrados as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
						
					case "RESIDENCIA":
						where +=" AND LOWER(S.tipo) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "FECHA":
						where +=" AND LOWER( CAST(S.fecha_inicio as varchar)) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LUGAR":
						where +=" AND LOWER(P.lugar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
						
					case "TIPO_PRUEBA":
						where +=" AND LOWER(c.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE_SESION":
						where +=" AND LOWER(S.nombre_sesion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					}
					
					
				}
					errorlog+="llego al orderby "
					switch(object.orderby) {
						
						case "ID":
						orderby+="pruebas_id";
						break;
						case "NOMBRE":
						orderby+="P.nombre";
						break;
						case "ALUMNOS ASIGNADOS":
						orderby+="P.registrados";
						break;
						case "CUPO":
						orderby+="P.cupo";
						break;
						case "RESIDENCIA":
						orderby+="S.tipo";
						break;
						case "FECHA":
						orderby+="S.fecha_inicio";
						break;
						case "LUGAR":
						orderby+="P.lugar";
						break;
						case "TIPO_PRUEBA":
						orderby+="c.descripcion";
						break;
						case "NOMBRE_SESION":
						orderby+="S.nombre";
						break;
						default:
						orderby+="S.fecha_inicio"
						break;
						
					}
					errorlog+="paso el order "
					orderby+=" "+object.orientation;
					consulta=consulta.replace("[WHERE]", where);
					consulta=consulta.replace("[FECHA]", "'"+object.fecha+"'");
					errorlog+="paso el where"
					
					pstm = con.prepareStatement(consulta.replace("DISTINCT(P.persistenceid)  as pruebas_id,   P.nombre, S.fecha_inicio, S.tipo as residencia, S.persistenceid as sesiones_id, P.lugar, P.registrados as alumnos_generales, S.nombre as nombre_sesion, c.descripcion as tipo_prueba, P.cupo, P.entrada,P.salida", "COUNT( DISTINCT(P.persistenceid)) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
					pstm.setInt(1, object.usuario)
					
					rs= pstm.executeQuery()
					if(rs.next()) {
						resultado.setTotalRegistros(rs.getInt("registros"))
					}
					consulta=consulta.replace("[ORDERBY]", orderby)
					consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
					errorlog+="conteo exitoso "
					
					pstm = con.prepareStatement(consulta)
					pstm.setInt(1, object.usuario)
					pstm.setInt(2, object.limit)
					pstm.setInt(3, object.offset)
					
					PruebasCustom Pruebas = new PruebasCustom();
					rs = pstm.executeQuery()
					errorlog+="Listado "
					while(rs.next()) {
						
						row = new PruebaCustom();
						row.setNombre(rs.getString("nombre"))
						row.setRegistrados(rs.getInt("alumnos_generales"));
						row.setLugar(rs.getString("lugar"));
						row.setPersistenceId(rs.getLong("pruebas_id"));
						row.setCupo(rs.getInt("cupo"));
						row.setTipo(new CatTipoPrueba())
						row.getTipo().setDescripcion(rs.getString("tipo_prueba"));
						row.setEntrada(rs.getString("entrada"));
						row.setSalida(rs.getString("salida"))
						
						SesionCustom row2 = new SesionCustom();
						row2.setFecha_inicio(rs.getString("fecha_inicio"));
						row2.setTipo(rs.getString("residencia"));
						row2.setPersistenceId(rs.getLong("sesiones_id"));
						row2.setNombre(rs.getString("nombre_sesion"));
						
						
						Pruebas = new PruebasCustom();
						Pruebas.setPrueba(row);
						Pruebas.setSesion(row2);
						
						rows.add(Pruebas)
					}
					
				resultado.setError_info(consulta +" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
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
	
	public Result getSesionesAspirantes(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="", orderbyUsuario="ORDER BY ";
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				String consulta = Statements.GET_SESIONESASPIRANTE
				SesionesAspiranteCustom row = new SesionesAspiranteCustom();
				List<SesionesAspiranteCustom> rows = new ArrayList<SesionesAspiranteCustom>();
				List<Map<String, Object>> aspirante = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
					case "ID BANNER":
						where +=" AND CAST(da.idbanner as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE":
						where +="  AND LOWER(concat(sda.primernombre,' ', sda.segundonombre,' ',sda.apellidopaterno,' ',sda.apellidomaterno)) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "EMAIL":
						where +=" AND LOWER(sda.correoelectronico) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROMEDIO":
                            errorlog+="PROMEDIO"
							where +=" AND  CAST(sda.PROMEDIOGENERAL as varchar) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+=" ='[valor]' "
							}else {
								where+="LIKE '%[valor]%'"
							}
                            where = where.replace("[valor]", filtro.get("valor"))
                            break;
							
					case "PREPARATORIA":
							where +=" AND LOWER(prepa.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where= where.replace("[valor]", filtro.get("valor"))
							break;
							
					case "RESIDENCIA":
						where +=" AND LOWER(R.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "SEXO":
						where +=" AND LOWER(sx.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LICENCIATURA":
						where +=" AND LOWER(gestionescolar.DESCRIPCION) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "UNIVERSIDAD":
						where +=" AND LOWER(campus.DESCRIPCION) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					}
					
					
					
					
				}
				
				errorlog+="llego al orderby "
				switch(object.orderby) {
					
					case "IDBANNER":
					orderby+="da.idbanner";
					break;
					case "NOMBRE":
					orderby+="sda.primernombre";
					break;
					case "EMAIL":
					orderby+="sda.correoelectronico";
					break;
					case "PREPARATORIA":
					orderby+="prepa.DESCRIPCION"
					break;
					case "CAMPUS":
					orderby+="campus.DESCRIPCION"
					break;
					case "RESIDENCIA":
					orderby+="R.descripcion";
					break;
					case "SEXO":
					orderby+="sx.descripcion";
					break;
					case "LICENCIATURA":
					orderby+="gestionescolar.DESCRIPCION";
					break;
					case "PROMEDIO":
					orderby+="sda.PROMEDIOGENERAL";
					break;
					case "ASISTENCIA":
					orderby+= "PL.asistencia";
					break;
					
					default:
					orderby+="SA.username"
					break;
					
				}
						
				//orderby+="SA.username"
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[WHERE]", where);
				consulta=consulta.replace("[FECHA]", "'"+object.fecha+"'");
				
				int tipo = 0;
				pstm = con.prepareStatement(Statements.GET_TIPOPRUEBA)
				pstm.setInt(1, object.prueba)
				rs= pstm.executeQuery();
				if(rs.next()) {
					tipo = (rs.getInt("tipoprueba_pid"))
				}
				if(tipo == 1) {
					consulta=consulta.replace("[ENTREVISTA]", "AND rd.persistenceid = sa.responsabledisponible_pid")
				}else {
					consulta=consulta.replace("[ENTREVISTA]", "")
				}
				errorlog+="tipo "+tipo
				pstm = con.prepareStatement(consulta.replace("SA.*,RD.responsableid,RD.prueba_pid, S.fecha_inicio, P.nombre as nombre_prueba,P.Lugar as lugar_prueba, c.descripcion as tipo_prueba, case when C.persistenceid=1 then rd.horario  else concat(p.entrada,' - ',p.salida) end as horario, c.persistenceid as tipoprueba_pid, PL.asistencia", "COUNT(SA.username) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				pstm.setInt(1, object.sesion)
				pstm.setInt(2, object.prueba)
				pstm.setInt(3, object.usuario)
				
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+="conteo exitoso "
					
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.sesion)
				pstm.setInt(2, object.prueba)
				pstm.setInt(3, object.usuario)
				pstm.setInt(4, object.limit)
				pstm.setInt(5, object.offset)
				
				rs = pstm.executeQuery()
				
				while(rs.next()) {	
					row = new SesionesAspiranteCustom();
					row.setUsername(rs.getString("username"))
					row.setSession_pid(rs.getLong("sesiones_pid"));
					row.setPersistenceid(rs.getLong("persistenceid"));
					row.setFecha(rs.getString("fecha_inicio"));
					row.setLugar_prueba(rs.getString("lugar_prueba"));
					row.setTipo_prueba(rs.getString("tipo_prueba"));
					row.setNombre_prueba(rs.getString("nombre_prueba"));
					row.setHorario(rs.getString("horario"));
					row.setTipoprueba_PID(rs.getString("tipoprueba_pid"));
					row.setEntrada(rs.getString("asistencia"))
					if(row.getEntrada() != null) {
						errorlog+="entro if "
						row.setAsistencia(rs.getBoolean("asistencia"));
					}
					
					
					pstm = con.prepareStatement(Statements.GET_ASPIRANTESDELASESION)
					pstm.setString(1, row.getUsername())
					rs2 = pstm.executeQuery()
					errorlog+="otra llamada "
					aspirante = new ArrayList<Map<String, Object>>();
					ResultSetMetaData metaData = rs2.getMetaData();
					int columnCount = metaData.getColumnCount();
					
					while(rs2.next()) {
						Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
						for (int i = 1; i <= columnCount; i++) {
							columns.put(metaData.getColumnLabel(i).toLowerCase(), rs2.getString(i));
							if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
								String encoded = "";
								try {
									for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs2.getString(i)), "fotoPasaporte", 0, 10)) {
										encoded = "../API/formsDocumentImage?document="+doc.getId();
										columns.put("fotografiab64", encoded);
									}
								}catch(Exception e) {
									columns.put("fotografiab64", "");
									errorlog+= ""+e.getMessage();
								}
							}
						}
						aspirante.add(columns);
					}
					row.setAspirantes(aspirante);
					rows.add(row);

				}
				
						
				resultado.setError_info(consulta +" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
				
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
	
	
	
	public Result getSesionesCalendarizadasPasadas(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		List<PruebasCustom> lstSesion = new ArrayList();
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="";
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				String consulta = Statements.GET_SESIONESCALENDARIZADASPASADAS
				//AND CAST(S.fecha_inicio AS DATE) < CAST([FECHA] AS DATE)
				PruebaCustom row = new PruebaCustom();
				List<PruebasCustom> rows = new ArrayList<PruebasCustom>();
				closeCon = validarConexion();
				errorlog+="llego a filtro "+object.lstFiltro.toString()
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
					case "ID":
						where +=" AND P.persistenceid ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE":
						where +=" AND LOWER(P.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ALUMNOS GENERALES":
						where +=" AND CAST(P.cupo as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ALUMNOS ASIGNADOS":
						where +=" AND CAST(P.registrados as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
						
					case "RESIDENCIA":
						where +=" AND LOWER(S.tipo) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "FECHA":
						where +=" AND LOWER( CAST(S.fecha_inicio as varchar)) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LUGAR":
						where +=" AND LOWER(P.lugar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
						
					case "TIPO PRUEBA":
						where +=" AND LOWER(c.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					}
					
					
				}
					errorlog+="llego al orderby "
					switch(object.orderby) {
						
						case "ID":
						orderby+="pruebas_id";
						break;
						case "NOMBRE":
						orderby+="P.nombre";
						break;
						case "ALUMNOS ASIGNADOS":
						orderby+="P.registrados";
						break;
						case "CUPO":
						orderby+="P.cupo";
						break;
						case "RESIDENCIA":
						orderby+="S.residencia";
						break;
						case "FECHA":
						orderby+="S.fecha_inicio";
						break;
						case "LUGAR":
						orderby+="P.lugar";
						break;
						case "TIPO_PRUEBA":
						orderby+="c.descripcion";
						break;
						case "NOMBRE_SESION":
						orderby+="S.nombre";
						break;
						default:
						orderby+="S.fecha_inicio"
						break;
						
					}
					errorlog+="paso el order "
					orderby+=" "+object.orientation;
					consulta=consulta.replace("[WHERE]", where);
					//consulta=consulta.replace("[FECHA]", "'"+object.fecha+"'");					
					errorlog+="paso el where"
					
					pstm = con.prepareStatement(consulta.replace("P.nombre, S.fecha_inicio, S.tipo as residencia, P.persistenceid as pruebas_id, S.persistenceid as sesiones_id, P.lugar, P.registrados as alumnos_generales, S.nombre as nombre_sesion, c.descripcion as tipo_prueba, P.cupo, P.entrada,P.salida", "COUNT(P.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
					
					rs= pstm.executeQuery()
					if(rs.next()) {
						resultado.setTotalRegistros(rs.getInt("registros"))
					}
					consulta=consulta.replace("[ORDERBY]", orderby)
					consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
					errorlog+="conteo exitoso "
					
					pstm = con.prepareStatement(consulta)
					pstm.setInt(1, object.limit)
					pstm.setInt(2, object.offset)
					
					PruebasCustom Pruebas = new PruebasCustom();
					rs = pstm.executeQuery()
					errorlog+="Listado "
					while(rs.next()) {
						
						row = new PruebaCustom();
						row.setNombre(rs.getString("nombre"))
						row.setRegistrados(rs.getInt("alumnos_generales"));
						row.setLugar(rs.getString("lugar"));
						row.setPersistenceId(rs.getLong("pruebas_id"));
						row.setCupo(rs.getInt("cupo"));
						row.setTipo(new CatTipoPrueba())
						row.getTipo().setDescripcion(rs.getString("tipo_prueba"));
						row.setEntrada(rs.getString("entrada"));
						row.setSalida(rs.getString("salida"))
						
						SesionCustom row2 = new SesionCustom();
						row2.setFecha_inicio(rs.getString("fecha_inicio"));
						row2.setTipo(rs.getString("residencia"));
						row2.setPersistenceId(rs.getLong("sesiones_id"));
						row2.setNombre(rs.getString("nombre_sesion"));
						
						
						Pruebas = new PruebasCustom();
						Pruebas.setPrueba(row);
						Pruebas.setSesion(row2);
						
						rows.add(Pruebas)
					}
					
				resultado.setError_info(consulta +" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
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
	public Result getDatosSesionUsername(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
		
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_DATOS_SESION_USERNAME)
				pstm.setString(1, username)
				
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
	
	
	
	public Result insertPaseLista( String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				pstm = con.prepareStatement(Statements.INSERT_PASEDELISTA, Statement.RETURN_GENERATED_KEYS)
				pstm.setLong(1, object.prueba);
				pstm.setString(2, object.username);
				pstm.setBoolean(3,object.asistencia);
				pstm.setString(4,object.fecha);
				pstm.setString(5,object.usuarioPaseLista);
				
				pstm.executeUpdate();
				
				con.commit();
				resultado.setSuccess(true)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result updatePaseLista(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				pstm = con.prepareStatement(Statements.UPDATE_PASEDELISTA)
				pstm.setBoolean(1,object.asistencia);
				pstm.setString(2,object.fecha);
				pstm.setString(3,object.usuarioPaseLista);
				pstm.setLong(4, object.prueba)
				pstm.setString(5, object.username)
				
				pstm.executeUpdate();
				
				con.commit();
				resultado.setSuccess(true)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getSesionesAspirantesPasados(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="", orderbyUsuario="ORDER BY sda.primernombre";
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				String consulta = Statements.GET_SESIONESASPIRANTEPASADAS
				//AND CAST(S.fecha_inicio AS DATE) < CAST([FECHA] AS DATE)
				SesionesAspiranteCustom row = new SesionesAspiranteCustom();
				List<SesionesAspiranteCustom> rows = new ArrayList<SesionesAspiranteCustom>();
				List<Map<String, Object>> aspirante = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
					case "ID BANNER":
					
						where +=" AND CAST(da.idbanner as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE":
						where +="  AND LOWER(concat(sda.primernombre,' ', sda.segundonombre,' ',sda.apellidopaterno,' ',sda.apellidomaterno)) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "EMAIL":
						where +=" AND LOWER(sda.correoelectronico) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROMEDIO":
							where +=" AND CAST(sda.PROMEDIOGENERAL as varchar )";
							if(filtro.get("operador").equals("Igual a")) {
								where+="='[valor]'"
							}else {
								where+="LIKE '%[valor]%'"
							}
                            where = where.replace("[valor]", filtro.get("valor"))
                            break;
							
					case "PREPARATORIA":
							where +=" AND LOWER(prepa.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where= where.replace("[valor]", filtro.get("valor"))
							break;
							
					case "RESIDENCIA":
						where +="AND  LOWER(R.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "SEXO":
						where +=" AND LOWER(sx.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LICENCIATURA":
						where +=" AND LOWER(gestionescolar.DESCRIPCION) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "UNIVERSIDAD":
						where +=" AND LOWER(campus.DESCRIPCION) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					}
					
					
					
					
				}
				
				errorlog+="llego al orderby "
				switch(object.orderby) {
					
					case "IDBANNER":
					orderby+="da.idbanner";
					break;
					case "NOMBRE":
					orderby+="sda.primernombre";
					break;
					case "EMAIL":
					orderby+="sda.correoelectronico";
					break;
					case "PREPARATORIA":
					orderby+="prepa.DESCRIPCION"
					break;
					case "CAMPUS":
					orderby+="campus.DESCRIPCION"
					break;
					case "RESIDENCIA":
					orderby+="R.descripcion";
					break;
					case "SEXO":
					orderby+="sx.descripcion";
					break;
					case "LICENCIATURA":
					orderby+="gestionescolar.DESCRIPCION";
					break;
					case "PROMEDIO":
					orderby+="sda.PROMEDIOGENERAL";
					break;
					case "ASISTENCIA":
					orderby+= "PL.asistencia";
					break;
					
					default:
					orderby+="SA.username"
					break;
					
				}
										
				//orderby+="SA.username"
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[WHERE]", where);
				//consulta=consulta.replace("[FECHA]", "'"+object.fecha+"'");
				
				int tipo = 0;
				pstm = con.prepareStatement(Statements.GET_TIPOPRUEBA)
				pstm.setInt(1, object.prueba)
				rs= pstm.executeQuery();
				if(rs.next()) {
					tipo = (rs.getInt("tipoprueba_pid"))
				}
				if(tipo == 1) {
					consulta=consulta.replace("[ENTREVISTA]", "AND rd.persistenceid = sa.responsabledisponible_pid")
				}else {
					consulta=consulta.replace("[ENTREVISTA]", "")
				}
				errorlog+="tipo "+tipo
				pstm = con.prepareStatement(consulta.replace("SA.*,RD.responsableid,RD.prueba_pid, S.fecha_inicio, P.nombre as nombre_prueba,P.Lugar as lugar_prueba, c.descripcion as tipo_prueba, case when C.persistenceid=1 then rd.horario  else concat(p.entrada,' - ',p.salida) end as horario, c.persistenceid as tipoprueba_pid, PL.asistencia", "COUNT(SA.username) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				pstm.setInt(1, object.sesion)
				pstm.setInt(2, object.prueba)
				
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+="conteo exitoso "
					
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.sesion)
				pstm.setInt(2, object.prueba)
				pstm.setInt(3, object.limit)
				pstm.setInt(4, object.offset)
				
				rs = pstm.executeQuery()
				
				while(rs.next()) {
					row = new SesionesAspiranteCustom();
					row.setUsername(rs.getString("username"))
					row.setSession_pid(rs.getLong("sesiones_pid"));
					row.setPersistenceid(rs.getLong("persistenceid"));
					row.setFecha(rs.getString("fecha_inicio"));
					row.setLugar_prueba(rs.getString("lugar_prueba"));
					row.setTipo_prueba(rs.getString("tipo_prueba"));
					row.setNombre_prueba(rs.getString("nombre_prueba"));
					row.setHorario(rs.getString("horario"));
					row.setTipoprueba_PID(rs.getString("tipoprueba_pid"));
					row.setEntrada(rs.getString("asistencia"))
					if(row.getEntrada() != null) {
						errorlog+="entro if "
						row.setAsistencia(rs.getBoolean("asistencia"));
					}
					
					
					pstm = con.prepareStatement(Statements.GET_ASPIRANTESDELASESION)
					pstm.setString(1, row.getUsername())
					rs2 = pstm.executeQuery()
					errorlog+="otra llamada "
					aspirante = new ArrayList<Map<String, Object>>();
					ResultSetMetaData metaData = rs2.getMetaData();
					int columnCount = metaData.getColumnCount();
					
					while(rs2.next()) {
						Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
						for (int i = 1; i <= columnCount; i++) {
							columns.put(metaData.getColumnLabel(i).toLowerCase(), rs2.getString(i));
							if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
								String encoded = "";
								try {
									for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs2.getString(i)), "fotoPasaporte", 0, 10)) {
										encoded = "../API/formsDocumentImage?document="+doc.getId();
										columns.put("fotografiab64", encoded);
									}
								}catch(Exception e) {
									columns.put("fotografiab64", "");
									errorlog+= ""+e.getMessage();
								}
							}
						}
						aspirante.add(columns);
					}
					row.setAspirantes(aspirante);
					rows.add(row);

				}
				
						
				resultado.setError_info(consulta +" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
				
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
	
	
	
	
	
	private static java.sql.Date convert(java.util.Date uDate) {
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}
	//Convert Date to Calendar
	private Calendar dateToCalendar(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;

	}
}
