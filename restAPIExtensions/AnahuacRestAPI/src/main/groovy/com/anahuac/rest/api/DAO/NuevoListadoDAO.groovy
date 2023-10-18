package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.catalogos.CatTerapiaDAO
import com.anahuac.rest.api.DAO.BannerDAO
import com.anahuac.model.Autodescripcion
import com.anahuac.model.AutodescripcionDAO
import com.anahuac.model.TestPsicometrico
import com.anahuac.model.TestPsicometricoDAO
import com.anahuac.model.TestPsicometricoRasgos
import com.anahuac.model.TestPsicometricoRasgosDAO
import com.anahuac.model.TestPsicometricoRelativosDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.NuevoStatements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utilities.FileDownload
import com.bonitasoft.web.extension.rest.RestAPIContext
import com.anahuac.rest.api.DAO.SesionesDAO;

import groovy.json.JsonSlurper
import groovy.sql.Sql

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLType
import java.sql.Statement
import java.sql.Types
import java.text.SimpleDateFormat

import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.api.ProcessAPI
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class NuevoListadoDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(PsicometricoDAO.class);
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
		  return retorno
	}
	 
	public Result selectReporteOV(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", campus = "",  orderby = "ORDER BY ", errorlog = "";
		List < String > lstGrupo = new ArrayList < String > ();
		List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
		Map < String, String > objGrupoCampus = new HashMap < String, String > ();
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);

			List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999);
			userLogged = context.getApiSession().getUserId();
			List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for (UserMembership objUserMembership: lstUserMembership) {
				for (CatCampus rowGrupo: lstCatCampus) {
					if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}

			assert object instanceof Map;
			if (object.campus != null) {
				where += " AND LOWER(campus.grupoBonita) = LOWER('" + object.campus + "') "
			}
			where += " "
			
			if (lstGrupo.size() > 0) {
				campus += " AND ("
			}
			for (Integer i = 0; i < lstGrupo.size(); i++) {
				String campusMiembro = lstGrupo.get(i);
				campus += "campus.descripcion='" + campusMiembro + "'"
				if (i == (lstGrupo.size() - 1)) {
					campus += ") "
				} else {
					campus += " OR "
				}
			}
			errorlog = "1";
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			
			String consulta = NuevoStatements.GET_LISTA_REPORTE_OV;
			String conteo = NuevoStatements.GET_CONTEO_REPORTE_OV;
		
			where += Where(object.lstFiltro,where);
			where  = NuevoStatements.WHERE_LISTA_REPORTE_OV.replace("[EXTRA]",where);
			errorlog = "2";
			orderby += Orden(object.orderby) + " " + object.orientation;
			errorlog = "3";
			
			consulta = consulta.replace("[WHERE]", where);
			consulta = consulta.replace("[CAMPUS]", campus);
			
			conteo = conteo.replace("[WHERE]", where);
			conteo = conteo.replace("[CAMPUS]", campus);
			
			errorlog = "4" + conteo;
			pstm = con.prepareStatement(conteo);
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorlog = "5 "+ consulta;
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)
			rs = pstm.executeQuery();
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			errorlog = consulta + " 8";
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("finalizado")){
						String info = rs.getString(i);
						if(info.equals("null") || info == null){
							columns.put(metaData.getColumnLabel(i).toLowerCase(), "s");
						}else{
							columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						}

					} else if(metaData.getColumnLabel(i).toLowerCase().equals("responsableid")) {
						User usr;
						String responsables = rs.getString(i);
						String nombres= "";
						if(!responsables.equals("null") && responsables != null) {
							String[] arrOfStr = responsables.split(",");
							for (String a: arrOfStr) {
								if(Long.parseLong(a)>0) {
									usr = context.getApiClient().getIdentityAPI().getUser(Long.parseLong(a))
									nombres+=(nombres.length()>1?", ":"")+usr.getFirstName()+" "+usr.getLastName()
								}
							}
						}
						columns.put(metaData.getColumnLabel(i).toLowerCase(), nombres);
					}else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
				}

				rows.add(columns);
			}
			errorlog = consulta + " 9";
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorlog);

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setError_info(errorlog);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result selectReporteOV_by_SESION(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long total = 0L;
		String where ="", orderby="ORDER BY ", errorlog="", role="", campus="", group="",registrados="";
		Boolean isPsicologo = false;
		List<String> lstGrupo = new ArrayList<String>();
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				String consulta = NuevoStatements.GET_LISTA_REPORTE_OV_BY_SESION;
				
				closeCon = validarConexion();
				
				def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
				List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
				
				userLogged = context.getApiSession().getUserId();
				
				List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
				for(UserMembership objUserMembership : lstUserMembership) {
					for(CatCampus rowGrupo : lstCatCampus) {
						if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita()) && !lstGrupo.contains(rowGrupo.getDescripcion()) ) {
							lstGrupo.add(rowGrupo.getDescripcion());
							break;
						}
					}
				}
				
				if(lstGrupo.size()>0) {
					campus+=" AND ("
				}
				for(Integer i=0; i<lstGrupo.size(); i++) {
					String campusMiembro=lstGrupo.get(i);
					campus+="campus.descripcion='"+campusMiembro+"'"
					if(i==(lstGrupo.size()-1)) {
						campus+=") "
					}
					else {
						campus+=" OR "
					}
				}
				
				if(object.campus != null) {
					campus +=" AND LOWER(campus.grupobonita) = LOWER('"+object.campus+"')";
					where +=" AND LOWER(campus.grupobonita)  = LOWER('"+object.campus+"')";
				}
			
				List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();

				for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
					if(filtro.get("columna") != "ALUMNOS REGISTRADOS" && filtro.get("columna") != "CAMPUS" ) {
						where = WhereIndividual(filtro.get("columna"),filtro.get("valor"),where);						
					}else if(filtro.get("columna") == "ALUMNOS REGISTRADOS") {
						registrados = WhereIndividual(filtro.get("columna"),filtro.get("valor"),registrados);
					}else if(filtro.get("columna") == "PSICOLOGO") {
						isPsicologo = true;
					}
				}
				
				errorlog = "2";
				orderby += Orden(object.orderby,"Pruebas.aplicacion") + " " + object.orientation;
				errorlog = "3";
				
				consulta=consulta.replace("[CAMPUS]", campus)
				consulta=consulta.replace("[WHERE]", where);
				String consulta_EXT = NuevoStatements.EXT_LISTA_REPORTE_OV_BY_SESION;
				
				
				consulta=consulta.replace("[REGISTRADOS]", registrados);
				
				consulta=consulta.replace("[COUNTASPIRANTES]", consulta_EXT);
				String groupBy = NuevoStatements.ORDERBY_LISTA_REPORTE_OV_BY_SESION;
				
				consulta=consulta.replace("[GROUPBY]", groupBy)
				
				errorlog+="consulta: "+ consulta.replace("* FROM (", "count(*) registros from ( ").replace("[LIMITOFFSET]", "").replace("[ORDERBY]","");
				pstm = con.prepareStatement(consulta.replace("* FROM (", "count(*) registros from ( ").replace("[LIMITOFFSET]", "").replace("[ORDERBY]",""));
				rs= pstm.executeQuery();
				while(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				
				errorlog+="conteo exitoso "
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				errorlog = consulta;
				rs = pstm.executeQuery()
				rows = new ArrayList < Map < String, Object >> ();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				errorlog = consulta + " 8";
				while (rs.next()) {
					Map < String, Object > columns = new LinkedHashMap < String, Object > ();
					for (int i = 1; i <= columnCount; i++) {
						if(metaData.getColumnLabel(i).toLowerCase().equals("responsables")) {
							User usr;
							String responsables = rs.getString(i);
							String nombres= "";
							if(!responsables.equals("null") && responsables != null) {
								String[] arrOfStr = responsables.split(",");
								for (String a: arrOfStr) {
									if(Long.parseLong(a)>0) {
										usr = context.getApiClient().getIdentityAPI().getUser(Long.parseLong(a))
										nombres+=(nombres.length()>1?", ":"")+usr.getFirstName()+" "+usr.getLastName();
									}
								}
							}
							columns.put(metaData.getColumnLabel(i).toLowerCase(), nombres);
						}else {							
							columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						}
					}
					rows.add(columns);
				}
					
				resultado.setError_info(consulta+" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
			} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				resultado.setError_info(" errorLog = "+errorlog);
			}finally {
				if(closeCon) {
					new DBConnect().closeObj(con, stm, rs, pstm)
				}
			}
		return resultado
	}
	
	public Result selectUsuariosSesion(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long total = 0L;
		String where ="", orderby="ORDER BY ", errorlog="";
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				String consulta = NuevoStatements.GET_LISTA_REPORTE_OV_USUARIOS_BY_SESION;
				List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
				for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
					where = WhereIndividual(filtro.get("columna"),filtro.get("valor"),where);
				}
				orderby += Orden(object.orderby,"") + " " + object.orientation;
				
				closeCon = validarConexion();
				consulta=consulta.replace("[WHERE]", where);				
				consulta=consulta.replace("[ORDERBY]", orderby);
				pstm = con.prepareStatement(consulta)

				errorlog = consulta;
				rs = pstm.executeQuery()
				rows = new ArrayList < Map < String, Object >> ();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				errorlog = consulta + " 8";
				int finalizados = 0,proceso = 0,iniciar = 0;
				while (rs.next()) {
					Map < String, Object > columns = new LinkedHashMap < String, Object > ();
					for (int i = 1; i <= columnCount; i++) {
						if(metaData.getColumnLabel(i).toLowerCase().equals("finalizado")){
							String info = rs.getString(i);
							if(info.equals("null") || info == null){
								iniciar++;
								columns.put(metaData.getColumnLabel(i).toLowerCase(), "s");
							}else{
								if(info.equals("t")){
									finalizados++;
								}else{
									proceso++;
								}
								columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
							}

						}else if(metaData.getColumnLabel(i).toLowerCase().equals("responsableid")) {
							User usr;
							String responsables = rs.getString(i);
							String nombres= "";
							if(!responsables.equals("null") && responsables != null) {
								String[] arrOfStr = responsables.split(",");
								for (String a: arrOfStr) {
									if(Long.parseLong(a)>0) {
										usr = context.getApiClient().getIdentityAPI().getUser(Long.parseLong(a))
										nombres+=(nombres.length()>1?", ":"")+usr.getFirstName()+" "+usr.getLastName()
									}
								}
							}
							columns.put(metaData.getColumnLabel(i).toLowerCase(), nombres);
						}else {
							columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						}
					}
					rows.add(columns);
				}
				
				List<Integer> list = [finalizados,proceso,iniciar];
				resultado.setError_info(consulta+" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
				resultado.setAdditional_data(list);		
			} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				resultado.setError_info(" errorLog = "+errorlog);
			}finally {
				if(closeCon) {
					new DBConnect().closeObj(con, stm, rs, pstm)
				}
			}
		return resultado
	}

	public Result selectAspirantesOVGeneral(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long total = 0L;
		String where ="", orderby="ORDER BY ", errorlog="", campus = "";
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				List < String > lstGrupo = new ArrayList < String > ();
				List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
				Map < String, String > objGrupoCampus = new HashMap < String, String > ();
				def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
				
				String consulta = NuevoStatements.GET_LISTA_REPORTE_OV_EXPECIFICO;
				List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();

				List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999);
				userLogged = context.getApiSession().getUserId();

				List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
				for (UserMembership objUserMembership: lstUserMembership) {
					for (CatCampus rowGrupo: lstCatCampus) {
						if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
							lstGrupo.add(rowGrupo.getDescripcion());
							break;
						}
					}
				}

				if (object.campus != null) {
					where += " AND LOWER(campus.grupoBonita) = LOWER('" + object.campus + "') "
				}
		
				if (lstGrupo.size() > 0) {
					campus += " AND ("
				}

				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					campus += "campus.descripcion='" + campusMiembro + "'"
					if (i == (lstGrupo.size() - 1)) {
						campus += ") "
					} else {
						campus += " OR "
					}
				}

				

				for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
					where = WhereIndividual(filtro.get("columna"),filtro.get("valor"),where);
				}
				orderby += Orden(object.orderby,"") + " " + object.orientation;
				
				closeCon = validarConexion();

				String SSA = "";
				pstm = con.prepareStatement(NuevoStatements.CONFIGURACIONESSSA)
				rs = pstm.executeQuery();
				if (rs.next()) {
					SSA = rs.getString("valor")
				}
				String conteo = NuevoStatements.COUNT_LISTA_REPORTE_OV_EXPECIFICO;
				conteo=conteo.replace("[WHERE]", where);
				conteo=conteo.replace("[CAMPUS]", campus);
				errorlog = conteo;
				pstm = con.prepareStatement(conteo);
				rs= pstm.executeQuery();
				while(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}

				consulta=consulta.replace("[CAMPUS]", campus);		
				consulta=consulta.replace("[WHERE]", where);				
				consulta=consulta.replace("[ORDERBY]", orderby);				
				consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")

				errorlog = consulta;

				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()

				rows = new ArrayList < Map < String, Object >> ();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				errorlog = consulta + " 8";

				while (rs.next()) {
					Map < String, Object > columns = new LinkedHashMap < String, Object > ();
					for (int i = 1; i <= columnCount; i++) {
						
						 if(metaData.getColumnLabel(i).toLowerCase().equals("finalizado")){
							String info = rs.getString(i);
							if(info.equals("null") || info == null){
								columns.put(metaData.getColumnLabel(i).toLowerCase(), "s");
							}else{
								columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
							}

						} else if(metaData.getColumnLabel(i).toLowerCase().equals("responsableid")) {
							User usr;
							String responsables = rs.getString(i);
							String nombres= "";
							if(!responsables.equals("null") && responsables != null) {
								String[] arrOfStr = responsables.split(",");
								for (String a: arrOfStr) {
									if(Long.parseLong(a)>0) {
										usr = context.getApiClient().getIdentityAPI().getUser(Long.parseLong(a))
										nombres+=(nombres.length()>1?", ":"")+usr.getFirstName()+" "+usr.getLastName()
									}
								}
							}
							columns.put(metaData.getColumnLabel(i).toLowerCase(), nombres);
						}else if(metaData.getColumnLabel(i).toLowerCase().equals("urlfoto")) {
							String encoded = "";
							boolean noAzure = false;
							try {
								String urlFoto = rs.getString("urlfoto");
								if (urlFoto != null && !urlFoto.isEmpty()) {
									columns.put("fotografiab64", base64Imagen((rs.getString("urlfoto") + SSA)) );
								}
							} catch (Exception e) {
								columns.put("fotografiabpm", "");
								if(noAzure){
									columns.put("fotografiab64", "");
								}
							}
						}else {
							columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						}
					}
					rows.add(columns);
				}
				
				resultado.setError_info(" errorLog = "+errorlog);
				resultado.setData(rows);
				resultado.setSuccess(true);
			} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				resultado.setError_info(" errorLog = "+errorlog);
			}finally {
				if(closeCon) {
					new DBConnect().closeObj(con, stm, rs, pstm)
				}
			}
		return resultado
	}

	public Result getFoto(String caseId, Long intentos,RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long total = 0L;
		String where ="", orderby="ORDER BY ", errorlog="";
		try {				
				closeCon = validarConexion();
				String SSA = "";
				pstm = con.prepareStatement(NuevoStatements.CONFIGURACIONESSSA)
				rs = pstm.executeQuery();
				if (rs.next()) {
					SSA = rs.getString("valor")
				}
				String consulta = NuevoStatements.GET_FOTO_BY_CASEID_INTENTO+" WHERE CASEID =${caseId} ";
				//pstm.setString(1, caseId);
				pstm = con.prepareStatement(consulta);
				rs = pstm.executeQuery()

				List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();

				while (rs.next()) {
					Map < String, Object > columns = new LinkedHashMap < String, Object > ();
					String encoded = "";
					boolean noAzure = false;
					try {
						String urlFoto = rs.getString("urlfoto");
						if (urlFoto != null && !urlFoto.isEmpty()) {
							columns.put("fotografiab64", base64Imagen((rs.getString("urlfoto") + SSA)) );
						}
					} catch (Exception e) {
						columns.put("fotografiabpm", "");
						if(noAzure){
							columns.put("fotografiab64", "");
						}
					}
					rows.add(columns);
				}
				resultado.setInfo(consulta);			
				resultado.setData(rows)
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
	
	public Result getCarreraByCampus(String Campus,RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "",campus="";
		List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
		try {
			
			if(Campus == null || Campus.equals("null") || Campus.length()<1 || Campus.equals("undefined") ){
				errorlog ="1";
				List < String > lstGrupo = new ArrayList < String > ();
				List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
				Map < String, String > objGrupoCampus = new HashMap < String, String > ();
				def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
				List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999);
				Long userLogged = 0L;
				
				userLogged = context.getApiSession().getUserId();
				List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
				for (UserMembership objUserMembership: lstUserMembership) {
					for (CatCampus rowGrupo: lstCatCampus) {
						if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
							lstGrupo.add(rowGrupo.getGrupoBonita());
							break;
						}
					}
				}
				if (lstGrupo.size() > 0) {
					campus += " ("
				}
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					campus += "campus ='" + campusMiembro + "'"
					if (i == (lstGrupo.size() - 1)) {
						campus += ") "
					} else {
						campus += " OR "
					}
				}
				
				errorlog ="3";
				closeCon = validarConexion();
				String consulta = NuevoStatements.GET_CARRERAS_BY_CAMPUSMULTIPLE.replace("[CAMPUS]", campus);
				errorlog ="4: "+ consulta;
				pstm = con.prepareStatement(consulta);
				rs = pstm.executeQuery()
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				errorlog = consulta + " 8";
				while (rs.next()) {
					Map < String, Object > columns = new LinkedHashMap < String, Object > ();
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
					rows.add(columns);
				}
			}else {
				closeCon = validarConexion();
				String consulta = NuevoStatements.GET_CARRERAS_BY_CAMPUS;
				pstm = con.prepareStatement(consulta);
				pstm.setString(1, Campus);
				rs = pstm.executeQuery()
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				errorlog = consulta + " 8";
				while (rs.next()) {
					Map < String, Object > columns = new LinkedHashMap < String, Object > ();
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
					rows.add(columns);
				}
			}
			
			resultado.setData(rows)
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog);
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result getPeriodo() {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		try {
			closeCon = validarConexion();
			String consulta = NuevoStatements.GET_PERIODOS;
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery()
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			errorlog = consulta + " 8";
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();
				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				rows.add(columns);
			}
			resultado.setData(rows)
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
	
	public String Where(def lstFiltro,String where,String specific = "") {
		
		for (Map < String, Object > filtro: (List < Map < String, Object >> ) lstFiltro) {
			
			switch (filtro.get("columna")) {
				case "NOMBRE,EMAIL":
					where += " AND "
					where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))

					where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))

					break;
				case "CORREO":
					where += " AND "
					where += "LOWER(sda.correoelectronico) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				
				case "ULTIMA MODIFICACION":
					where += " AND "
					where += " (LOWER(sda.fechaultimamodificacion) ";
					if (filtro.get("operador").equals("Igual a")) {
						where += "=LOWER('[valor]')"
					} else {
						where += "LIKE LOWER('%[valor]%')"
					}
					where += " OR to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'DD-MM-YYYYTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') ";
					where += "LIKE LOWER('%[valor]%'))";

					where = where.replace("[valor]", filtro.get("valor"))
					break;
					//filtrado utilizado en lista roja y rechazado

				case "ESTATUS":
					where += " AND "
					where += " LOWER(sda.ESTATUSSOLICITUD) ";
					if (filtro.get("operador").equals("Igual a")) {
						where += "=LOWER('[valor]')"
					} else {
						where += "LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				case "ID BANNER":
					where += " AND "
					where += " LOWER(da.idbanner) ";
					if (filtro.get("operador").equals("Igual a")) {
						where += "=LOWER('[valor]')"
					} else {
						where += "LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				
				case "CAMPUS VPD":
					where += " AND "
					where += " LOWER(campusEstudio.descripcion) ";
					if (filtro.get("operador").equals("Igual a")) {
						where += "=LOWER('[valor]')"
					} else {
						where += "LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				
				case "CAMPUS DE LA SESIÓN":
					where += " AND "
					where += " LOWER(campus.descripcion) ";
					if (filtro.get("operador").equals("Igual a")) {
						where += "=LOWER('[valor]')"
					} else {
						where += "LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					

				case "SESIÓN,ID SESIÓN,FECHA ENTREVISTA": 
					where += " AND "
					where += " ( LOWER(Sesion.nombre) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					where += " OR LOWER(CAST(Sesion.persistenceid AS varchar)) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					where += " OR LOWER( CAST(TO_CHAR(Pruebas.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				case "FECHA DE LA ENTREVISTA":
					where += " AND "
					where += " LOWER( CAST(TO_CHAR(Pruebas.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				case "ID SESIÓN":
					where += " AND "
					where += "  LOWER(CAST(Sesion.persistenceid AS varchar)) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				case "ID_SESIÓN":
					where += " AND "
					where += "  Sesion.persistenceid  == %[valor]% ";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "NOMBRE DE LA SESIÓN":
					where += " AND "
					where += " LOWER(Sesion.nombre) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				case "ID DE LA PRUEBA":
					where +=" AND CAST(Pruebas.persistenceid as varchar) ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="='[valor]'"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
				case "FECHA, LUGAR":
					where +=" AND  ( LOWER( CAST(TO_CHAR(Pruebas.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') ";
					where += "OR LOWER(Pruebas.entrada) LIKE LOWER('%[valor]%') "
					where += "OR LOWER(Pruebas.salida) LIKE LOWER('%[valor]%') "
					where = where.replace("[valor]", filtro.get("valor"))

					where +=" OR LOWER(Pruebas.lugar) LIKE LOWER('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
					break;

				case "NOMBRE DE LA PRUEBA":
					where +=" AND LOWER(Pruebas.nombre) ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=LOWER('[valor]')"
					}else {
						where+="LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;

				case "CUPO DE LA PRUEBA":
					where +=" AND CAST(Pruebas.cupo as varchar) ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="='[valor]'"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;

				case "ALUMNOS REGISTRADOS":
					where += " WHERE "
					where +=" CAST(registrados as varchar) ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="='[valor]'"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;


					default:
					break;
			}
			
			if(!filtro.get("columna").equals(specific) && specific != ""){
				where = "";
			}

			if(filtro.get("columna").equals(specific)){
				if(where.length() > 0){
					break;
				}
			}	
		}
		return where;
	}
	
	public String WhereIndividual(String filtro,String valor,String where,String specific = "") {
		if(specific.equals("") && !filtro.equals("PSICOLOGO")) {
			where += " AND "
		}
		switch (filtro) {
			case "NOMBRE,EMAIL":
				where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
				where = where.replace("[valor]", valor)
				where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
				where = where.replace("[valor]", valor)
				break;

			case "CORREO":
				where += " LOWER(sda.correoelectronico) like lower('%[valor]%') ";
				where = where.replace("[valor]", valor)
				break;

			case "NOMBRE DEL ALUMNO":
				where += " LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
				where = where.replace("[valor]", valor)
				break;
			
			case "CARRERA":
				where += " LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
				where = where.replace("[valor]", valor);
				break;
				
			case "CAMPUS VPD":
				where += " LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
				where = where.replace("[valor]", valor);
				break;

			case "PERIODO":
				where += " LOWER(periodo.DESCRIPCION) like lower('%[valor]%') ";
				where = where.replace("[valor]", valor)
				break;
				
			case "CAMPUS":
				where += "  LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
				where = where.replace("[valor]", valor)	
				break;
				
			case "CAMPUS DE LA SESIÓN":
				where += "  LOWER(campus.descripcion) like lower('%[valor]%') ";
				where = where.replace("[valor]", valor)
				break;
			
			case "ULTIMA MODIFICACION":
				where += " (LOWER(sda.fechaultimamodificacion) LIKE LOWER('%[valor]%')";
				where += " OR to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'DD-MM-YYYYTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') ";
				where += "LIKE LOWER('%[valor]%'))";
				where = where.replace("[valor]", valor)
				break;
				//filtrado utilizado en lista roja y rechazado

			case "ESTATUS":
				where += " LOWER(sda.ESTATUSSOLICITUD) LIKE LOWER('%[valor]%')";
				where = where.replace("[valor]", valor)
				break;
			case "ID BANNER":
				where += " LOWER(da.idbanner) LIKE LOWER('%[valor]%')";
				where = where.replace("[valor]", valor)
				break;

			case "SESIÓN,ID SESIÓN,FECHA ENTREVISTA":
				
				where += " ( LOWER(Sesion.nombre) like lower('%[valor]%') ";
				where = where.replace("[valor]", valor)
				where += " OR LOWER(CAST(Sesion.persistenceid AS varchar)) like lower('%[valor]%') ";
				where = where.replace("[valor]", valor)
				where += " OR LOWER( CAST(TO_CHAR(Pruebas.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') )";
				where = where.replace("[valor]", valor)
				break;
				
			case "FECHA DE LA ENTREVISTA":
				where += " LOWER( CAST(TO_CHAR(Pruebas.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') ";
				where = where.replace("[valor]", valor)
				break;
				
			case "ID SESIÓN":
				where += "  LOWER(CAST(Sesion.persistenceid AS varchar)) like lower('%[valor]%') ";
				where = where.replace("[valor]", valor)
				break;
			case "ID_SESIÓN":
				where += "  Sesion.persistenceid  = [valor] ";
				where = where.replace("[valor]", valor)
				break;
			case "NOMBRE DE LA SESIÓN":
				where += " LOWER(Sesion.nombre) like lower('%[valor]%') ";
				where = where.replace("[valor]", valor)
				break;
			case "ID DE LA PRUEBA":
				where +=" CAST(Pruebas.persistenceid as varchar) LIKE LOWER('%[valor]%') ";
				where = where.replace("[valor]", valor)
				break;
			case "FECHA, LUGAR":
				where +=" ( LOWER( CAST(TO_CHAR(Pruebas.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') ";
				where += "OR LOWER(Pruebas.entrada) LIKE LOWER('%[valor]%') "
				where += "OR LOWER(Pruebas.salida) LIKE LOWER('%[valor]%') "
				where = where.replace("[valor]", valor)
				where +=" OR LOWER(Pruebas.lugar) LIKE LOWER('%[valor]%') )";
				where = where.replace("[valor]", valor)
				break;

			case "NOMBRE DE LA PRUEBA":
				where +=" LOWER(Pruebas.nombre) LIKE LOWER('%[valor]%')";
				where = where.replace("[valor]", valor)
				break;

			case "CUPO DE LA PRUEBA":
				where +=" CAST(Pruebas.cupo as varchar) LIKE LOWER('%[valor]%') ";
				where = where.replace("[valor]", valor)
				break;

			case "ALUMNOS REGISTRADOS":
				where += " WHERE "
				where +=" CAST(registrados as varchar) LIKE LOWER('%[valor]%') ";
				where = where.replace("[valor]", valor)
				break;
				
				default:
				break;
		}
		
		return where;
	}
	
	
	public String Orden(String order,String Default = "") {
		String orderby = " ";
		if(!Default.equals("") && order?.trim()?.equals("")) {
			return Default;
		}
		switch (order) {

			case "FECHAULTIMAMODIFICACION":
				orderby += "sda.fechaultimamodificacion";
				break;
			case "NOMBRE":
				orderby += "sda.apellidopaterno";
				break;
			case "EMAIL":
				orderby += "sda.correoelectronico";
				break;
			case "CAMPUS":
				orderby += "campus.DESCRIPCION"
				break;
			case "CAMPUSVPD":
				orderby += "campusEstudio.DESCRIPCION"
				break;
			case "ESTATUS":
				orderby += "sda.ESTATUSSOLICITUD";
				break;
			case "IDBANNER":
				orderby += "da.idbanner";
				break;
			case "SOLICITUD":
				orderby += "sda.caseid::INTEGER";
				break;
			case "PROGRAMA":
				orderby += "gestionescolar.NOMBRE"
				break;
			case "INGRESO":
				orderby += "periodo.DESCRIPCION"
				break;
			case "SESIÓN":
				orderby += "Sesion.nombre";
				break;
			case "FECHA ENTREVISTA":
				orderby += "Pruebas.APLICACION";
				break;
			case "ID SESIÓN":
				orderby += "Sesion.persistenceid";
				break;
			case "ID PRUEBA":
				orderby+="pruebas_id";
				break;
			case "ALUMNOS REGISTRADOS":
				orderby+="registrados";
				break;
			case "CUPO PRUEBA":
				orderby+="Pruebas.cupo";
				break;
			case "FECHA PRUEBA":
				orderby+="Pruebas.aplicacion";
				break;
			case "LUGAR PRUEBA":
				orderby+="Pruebas.lugar";
				break;
			case "NOMBRE PRUEBA":
				orderby+="Pruebas.nombre";
				break;
			case "LUGAR":
				orderby+="Pruebas.lugar";
				break;
				
			default:
				orderby += "sda.persistenceid"
				break;
		}
		
		return orderby;
		
	}
	
	public String base64Imagen(String url)  throws Exception {
		String b64 = "";
		if(url.toLowerCase().contains(".jpeg")) {
				b64 = ( "data:image/jpeg;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".png")) {
				b64 = ( "data:image/png;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".jpg")) {
				b64 = ( "data:image/jpg;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".jfif")) {
				b64 = ( "data:image/jfif;base64, "+(new FileDownload().b64Url(url)));
			}
		return  b64
	}

	
}
