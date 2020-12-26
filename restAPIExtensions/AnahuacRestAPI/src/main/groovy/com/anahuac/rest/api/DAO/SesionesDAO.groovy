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
import com.anahuac.rest.api.Entity.db.CatTipoPrueba
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.SimpleDateFormat
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion

class SesionesDAO {
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
				pstm = con.prepareStatement(Statements.INSERT_SESION, Statement.RETURN_GENERATED_KEYS)
				pstm.setString(1, sesion.getNombre())
				pstm.setString(2, sesion.getDescripcion())
				pstm.setDate(3, convert(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:00.000'Z'").parse(sesion.getFecha_inicio())))
				(sesion.getIsmedicina()==null)?pstm.setNull(4, java.sql.Types.NULL):pstm.setBoolean(4, sesion.getIsmedicina())
				(sesion.getBachillerato_pid()==null)?pstm.setNull(5, java.sql.Types.NULL):pstm.setLong(5, sesion.getBachillerato_pid())
				(sesion.getEstado_pid()==null)?pstm.setNull(6, java.sql.Types.NULL):pstm.setLong(6, sesion.getEstado_pid())
				(sesion.getPais_pid()==null)?pstm.setNull(7, java.sql.Types.NULL):pstm.setLong(7, sesion.getPais_pid())
				(sesion.getBorrador()==null)?pstm.setBoolean(8, true):pstm.setBoolean(8, sesion.getBorrador())
				pstm.setLong(9, sesion.getCampus_pid())
				pstm.setString(10, sesion.getTipo())
				
				pstm.executeUpdate();
                rs = pstm.getGeneratedKeys();
				if(rs.next()) {
					sesion.setPersistenceId(rs.getLong("persistenceid"))
					for (PruebaCustom prueba: sesion.getPruebas()) {
						pstm = con.prepareStatement(Statements.INSERT_PRUEBA, Statement.RETURN_GENERATED_KEYS)
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
						pstm.executeUpdate()
						rs = pstm.getGeneratedKeys();
						if(rs.next()) {
							prueba.setPersistenceId(rs.getLong("persistenceId"))
							if(prueba.getCattipoprueba_pid()==1L) {
								
							
							for (ResponsableCustom responsable: prueba.getPsicologos()) {
								for (ResponsableDisponible disponible:responsable.getLstFechasDisponibles()) {
									pstm = con.prepareStatement(Statements.INSERT_RESPONSABLEDISPONIBLE, Statement.RETURN_GENERATED_KEYS)
									pstm.setString(1, disponible.getHorario())
									pstm.setBoolean(2, disponible.getDisponible())
									pstm.setLong(3, responsable.getId())
									pstm.setLong(4,prueba.getPersistenceId())
									pstm.executeUpdate()
									rs = pstm.getGeneratedKeys();
									if (rs.next()) {
										disponible.setPersisteneceId(rs.getLong("persistenceId"))
									}
								}
							}
							}else {
								for (ResponsableCustom responsable: prueba.getPsicologos()) {
									pstm = con.prepareStatement(Statements.INSERT_RESPONSABLEDISPONIBLE, Statement.RETURN_GENERATED_KEYS)
									pstm.setNull(1, java.sql.Types.NULL)
									pstm.setNull(2, java.sql.Types.NULL)
									pstm.setLong(3, responsable.getId())
									pstm.setLong(4,prueba.getPersistenceId())
									pstm.executeUpdate()
									rs = pstm.getGeneratedKeys();
									if (rs.next()) {
										responsable.setLstFechasDisponibles(new ArrayList())
										responsable.getLstFechasDisponibles().add(new ResponsableDisponible())
										responsable.getLstFechasDisponibles().get(0).setPersisteneceId(rs.getLong("persistenceId"))
									}
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
					
					User usr = context.getApiClient().getIdentityAPI().getUser(rs.getLong("RESPONSABLEID"))
					
					if(sesion.getPruebas().contains(p)) {
						ResponsableCustom psi = new ResponsableCustom()
						ResponsableDisponible fd = new ResponsableDisponible()
						fd.setDisponible(rs.getBoolean("disponible"))
						fd.setHorario(rs.getString("horario"))
						psi.setFirstname(usr.getFirstName())
						psi.setLastname(usr.getLastName())
						
						psi.setId(rs.getLong("RESPONSABLEID"))
					
						if(sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().contains(psi)) {
							sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().get(sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().indexOf(psi)).getLstFechasDisponibles().add(fd)
							
						}else {
							psi.setLstFechasDisponibles(new ArrayList())
							psi.getLstFechasDisponibles().add(fd)
							sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().add(psi)
						}
					}else {
						ResponsableCustom psi = new ResponsableCustom()
						ResponsableDisponible fd = new ResponsableDisponible()
						fd.setDisponible(rs.getBoolean("disponible"))
						fd.setHorario(rs.getString("horario"))
						psi.setId(rs.getLong("RESPONSABLEID"))
						psi.setFirstname(usr.getFirstName())
						psi.setLastname(usr.getLastName())
						psi.setLstFechasDisponibles(new ArrayList())
						psi.getLstFechasDisponibles().add(fd)
						
						p.getPsicologos().add(psi)
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
						if(where.contains("WHERE")) {
							where+= " AND "
						}
						where +=" LOWER(pruebas_id) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE":
						if(where.contains("WHERE")) {
							where+= " AND "
						}
						where +=" LOWER(P.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ALUMNOS GENERALES":
						if(where.contains("WHERE")) {
							where+= " AND "
						}
						where +=" LOWER(alumnos_generales) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ALUMNOS ASIGNADOS":
						if(where.contains("WHERE")) {
							where+= " AND "
						}
						where +=" LOWER(alumnos_generales) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "RESIDENCIA":
						if(where.contains("WHERE")) {
							where+= " AND "
						}
						where +=" LOWER(S.tipo) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "FECHA":
						if(where.contains("WHERE")) {
							where+= " AND "
						}
						where +=" LOWER(S.fecha_inicio) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LUGAR":
						if(where.contains("WHERE")) {
							where+= " AND "
						}
						where +=" LOWER(P.lugar) ";
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
						orderby+="P.alumnos_asignados";
						break;
						case "ALUMNOS GENERALES":
						orderby+="P.alumnos_generales";
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
						default:
						orderby+="S.fecha_inicio"
						break;
						
					}
					errorlog+="paso el order "
					orderby+=" "+object.orientation;
					consulta=consulta.replace("[WHERE]", where);
					errorlog+="paso el where "+ consulta.replace("P.nombre, S.fecha_inicio, S.tipo, P.persistenceid as pruebas_id, P.lugar, P.registrados as alumnos_generales", "COUNT(P.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "")
					
					pstm = con.prepareStatement(consulta.replace("P.nombre, S.fecha_inicio, S.tipo, P.persistenceid as pruebas_id, S.persistenceid as sesiones_id, P.lugar, P.registrados as alumnos_generales", "COUNT(P.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
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
						SesionCustom row2 = new SesionCustom();
						row2.setFecha_inicio(rs.getString("fecha_inicio"));
						row2.setTipo(rs.getString("tipo"));
						row2.setPersistenceId(rs.getLong("sesiones_id"));
						
						Pruebas = new PruebasCustom();
						Pruebas.setPrueba(row);
						Pruebas.setSesion(row2);
						
						rows.add(Pruebas)
					}
					
				
			
				lstSesion.add(rows)
				resultado.setError_info(consulta +" errorLog = "+errorlog)
				resultado.setData(lstSesion)
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
		List<PruebasCustom> lstSesion = new ArrayList();
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="";
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				String consulta = Statements.GET_SESIONESASPIRANTE
				SesionesAspiranteCustom row = new SesionesAspiranteCustom();
				List<SesionesAspiranteCustom> rows = new ArrayList<SesionesAspiranteCustom>();
				List<Map<String, Object>> aspirante = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				
				orderby+="persistenceid"
				orderby+=" "+object.orientation;
					
					
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.sesion)
				
				rs = pstm.executeQuery()
				while(rs.next()) {	
					row = new PruebaCustom();
					row.setUsername(rs.getString("username"))
					row.setSession_pid(rs.getLong("session_pid"));
					row.setIseliminado(rs.getBoolean("iseliminado"));
					row.setPersistenceid(rs.getLong("persistenceid"));
					row.setFecha(rs.getString("fecha_inicio"));
					pstm = con.prepareStatement(Statements.GET_ASPIRANTESDELASESION)
					pstm.setString(1, row.getUsername())
					rs = pstm.executeQuery()
					
					aspirante = new ArrayList<Map<String, Object>>();
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();
					
					while(rs.next()) {
						Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
						for (int i = 1; i <= columnCount; i++) {
							columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
							if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
								String encoded = "";
								try {
									for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
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
						
				lstSesion.add(rows)
				resultado.setError_info(consulta +" errorLog = "+errorlog)
				resultado.setData(lstSesion)
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
