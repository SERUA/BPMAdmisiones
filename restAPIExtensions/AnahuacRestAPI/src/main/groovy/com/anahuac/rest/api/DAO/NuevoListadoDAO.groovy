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
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLType
import java.sql.Statement
import java.sql.Types
import java.text.DateFormat
import java.text.SimpleDateFormat

import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.bpm.document.Document
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
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
					
				resultado.setError_info("errorLog = "+errorlog)
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
	
	public Result contSelectUsuariosSesion(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long total = 0L;
		String where ="", orderby="ORDER BY ", errorlog="";
//		throw new Exception("No encontro datos en " + totalRegistros);
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
				int totalRegistros = 0;
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
					totalRegistros++;
					
					rows.add(columns);
				}
				
				resultado.setError_info(" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
				resultado.setTotalRegistros(totalRegistros);
				resultado.setFinalizados(finalizados);
				resultado.setProceso(proceso);
				resultado.setIniciar(iniciar);
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
				
				int limit = object.limit ?: 20;
				int offset = object.offset ?: 0;
		
				consulta += " LIMIT " + limit + " OFFSET " + offset;
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
				resultado.setLimit(limit);
				resultado.setOffset(offset);
				Result contResult = contSelectUsuariosSesion(jsonData, context);
				finalizados = contResult.getFinalizados();
				proceso = contResult.getProceso();
				iniciar = contResult.getIniciar();
				resultado.setFinalizados(finalizados);
				resultado.setProceso(proceso);
				resultado.setIniciar(iniciar);
				int totalRegistros = contResult.getTotalRegistros();
				resultado.setTotalRegistros(totalRegistros);
				List<Integer> list = [finalizados,proceso,iniciar];
				resultado.setError_info(" errorLog = "+errorlog)
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
	
	public Result selectAspirantesOVGeneralExel(String jsonData, RestAPIContext context) {
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
				
				String consulta = NuevoStatements.GET_LISTA_REPORTE_OV_EXPECIFICO_EXCEL;
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
				errorlog = consulta;

				pstm = con.prepareStatement(consulta)
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
	
	public Result getExcelFileReporteOvBusqueda(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
	
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			Result dataResult = contSelectUsuariosSesion(jsonData, context);
	
			int rowCount = 0;
			List<Object> lstParams;
			String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(type);
			CellStyle style = workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
	
			if (dataResult.success) {
				lstParams = dataResult.getData();
			} else {
				throw new Exception("No encontró datos");
			}
	
			String title = object.estatussolicitud;
			Row titleRow = sheet.createRow(++rowCount);
			Cell cellReporte = titleRow.createCell(1);
			cellReporte.setCellValue("Reporte:");
			cellReporte.setCellStyle(style);
			Cell cellTitle = titleRow.createCell(2);
			cellTitle.setCellValue("Reporte OV busqueda");
	
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -7);
			Date date = cal.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String sDate = formatter.format(date);
	
			Row blank = sheet.createRow(++rowCount);
//			Cell cellusuario = blank.createCell(4);
//			cellusuario.setCellValue("Usuario:");
//			cellusuario.setCellStyle(style);
//			Cell cellusuarioData = blank.createCell(5);
//			cellusuarioData.setCellValue("Usuario:");
			Row espacio = sheet.createRow(++rowCount);
			Row headersRow = sheet.createRow(++rowCount);
			Cell header1 = headersRow.createCell(0);
			header1.setCellValue("Id banner");
			
			Cell header2 = headersRow.createCell(1);
			header2.setCellValue("Nombre");
			
			Cell header3 = headersRow.createCell(2);
			header3.setCellValue("Email");
			
			Cell header4 = headersRow.createCell(3);
			header4.setCellValue("Programa");
			
			Cell header5 = headersRow.createCell(4);
			header5.setCellValue("Periodo");
			
			Cell header6 = headersRow.createCell(5);
			header6.setCellValue("Id sesión");
			
			Cell header7 = headersRow.createCell(6);
			header7.setCellValue("Nombre de la sesión");
			
			Cell header8 = headersRow.createCell(7);
			header8.setCellValue("Fecha de la entrevista");
			
			Cell header9 = headersRow.createCell(8);
			header9.setCellValue("Hora de la entrevista");
			
			Cell header10 = headersRow.createCell(9);
			header10.setCellValue("Psicólogo");
			
			Cell header11 = headersRow.createCell(10);
			header11.setCellValue("Estatus de admisiones");
			
			Cell header12 = headersRow.createCell(11);
			header12.setCellValue("Estatus reporte OV");
			
			Cell header13 = headersRow.createCell(12);
			header13.setCellValue("Última modificación");
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			DateFormat dformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			// Datos
			for (int i = 0; i < lstParams.size(); ++i) {
			    Row row = sheet.createRow(++rowCount);

				Cell cell1 = row.createCell(0);
				cell1.setCellValue(lstParams.get(i).get("idbanner"));
				
				Cell cell2 = row.createCell(1);
				String apellidopaterno = lstParams.get(i).get("apellidopaterno");
				String apellidomaterno = lstParams.get(i).get("apellidomaterno");
				String primernombre = lstParams.get(i).get("primernombre");
				String segundonombre = lstParams.get(i).get("segundonombre");
				cell2.setCellValue(apellidopaterno + " " + apellidomaterno + " " + primernombre + " " + segundonombre);
				
				Cell cell3 = row.createCell(2);
				cell3.setCellValue(lstParams.get(i).get("correoelectronico"));
				
				Cell cell4 = row.createCell(3);
				cell4.setCellValue(lstParams.get(i).get("licenciatura"));
				
				Cell cell5 = row.createCell(4);
				cell5.setCellValue(lstParams.get(i).get("ingreso"));
				
				Cell cell6 = row.createCell(5);
				cell6.setCellValue(lstParams.get(i).get("sesion"));
				
				Cell cell7 = row.createCell(6);
				cell7.setCellValue(lstParams.get(i).get("idsesion"));
				
				Cell cell8 = row.createCell(7);
				cell8.setCellValue(lstParams.get(i).get("fechaentrevista"));
				
				Cell cell9 = row.createCell(8);
				cell9.setCellValue(lstParams.get(i).get("horario"));
				
				Cell cell10 = row.createCell(9);
				cell10.setCellValue(lstParams.get(i).get("responsableid"));
				
				Cell cell11 = row.createCell(10);
				cell11.setCellValue(lstParams.get(i).get("estatussolicitud"));
				
				Cell cell12 = row.createCell(11);
				String finalizado = lstParams.get(i).get("finalizado");
				if ("f".equals(finalizado)) {
					finalizado = "Finalizado";
				} else if ("s".equals(finalizado)) {
					finalizado = "Sin iniciar";
				}
				cell12.setCellValue(finalizado);
				
//				Cell cell11 = row.createCell(10);
//				cell11.setCellValue(lstParams.get(i).get("fechaultimamodificacion"));
				
				String fechaRegistroString = lstParams.get(i).get("fechaultimamodificacion");
				
				if (fechaRegistroString != null) {
				    Date fechaRegistro = dfSalida.parse(fechaRegistroString);
				    String fechaFormateada = dformat.format(fechaRegistro);
				    Cell cell13 = row.createCell(12);
				    cell13.setCellValue(fechaFormateada);
				} else {
				    Cell cell13 = row.createCell(12);
				    cell13.setCellValue("N/A");
				}
				errorLog += fechaRegistroString;
			}
			
			// Ajusta el ancho de las columnas
			for (int i = 0; i <= 11; ++i) {
			    sheet.autoSizeColumn(i);
			}
	
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			
			List<Object> lstResultado = new ArrayList<Object>();
			lstResultado.add(Base64.getEncoder().encodeToString(outputStream.toByteArray()));
			resultado.setError_info(errorLog);
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
		} catch (Exception e) {
			LOGGER.error("[ERROR] " + e.getMessage());
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		}
	
		return resultado;
	}
	
	public Result getExcelFileReporteOvBusquedaAvanzada(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
	
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
	
			Result dataResult = selectAspirantesOVGeneralExel(jsonData, context);
	
			int rowCount = 0;
			List<Object> lstParams;
			String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(type);
			CellStyle style = workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
	
			if (dataResult.success) {
				lstParams = dataResult.getData();
			} else {
				throw new Exception("No encontró datos");
			}
	
			String title = object.estatussolicitud;
			Row titleRow = sheet.createRow(++rowCount);
			Cell cellReporte = titleRow.createCell(1);
			cellReporte.setCellValue("Reporte:");
			cellReporte.setCellStyle(style);
			Cell cellTitle = titleRow.createCell(2);
			cellTitle.setCellValue(title);
	
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -7);
			Date date = cal.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String sDate = formatter.format(date);
	
			Row blank = sheet.createRow(++rowCount);
			Cell cellusuario = blank.createCell(4);
			cellusuario.setCellValue("Usuario:");
			cellusuario.setCellStyle(style);
			Cell cellusuarioData = blank.createCell(5);
			cellusuarioData.setCellValue("Usuario:");
			Row espacio = sheet.createRow(++rowCount);
			Row headersRow = sheet.createRow(++rowCount);
			Cell header1 = headersRow.createCell(0);
			header1.setCellValue("Id banner");
			
			Cell header2 = headersRow.createCell(1);
			header2.setCellValue("Nombre");
			
			Cell header3 = headersRow.createCell(2);
			header3.setCellValue("Email");
			
			Cell header4 = headersRow.createCell(3);
			header4.setCellValue("CURP");
			
			Cell header5 = headersRow.createCell(4);
			header5.setCellValue("Campus");
			
			Cell header6 = headersRow.createCell(5);
			header6.setCellValue("Programa");
			
			Cell header7 = headersRow.createCell(6);
			header7.setCellValue("Período");
			
			Cell header8 = headersRow.createCell(7);
			header8.setCellValue("Procedencia");
			
			Cell header9 = headersRow.createCell(8);
			header9.setCellValue("Preparatoria");
			
			Cell header10 = headersRow.createCell(9);
			header10.setCellValue("Promedio");
			
			Cell header11 = headersRow.createCell(10);
			header11.setCellValue("Residencia");
			
			Cell header12 = headersRow.createCell(11);
			header12.setCellValue("Tipo de Admisión");
			
			Cell header13 = headersRow.createCell(12);
			header13.setCellValue("Tipo de Alumno");
			
			Cell header14 = headersRow.createCell(13);
			header14.setCellValue("Nombre de la sesión");
			
			Cell header15 = headersRow.createCell(14);
			header15.setCellValue("Id Sesión");
			
			Cell header16 = headersRow.createCell(15);
			header16.setCellValue("Fechade la entrevista");
			
			Cell header17 = headersRow.createCell(16);
			header17.setCellValue("Hora de la entrevista");
			
			Cell header18 = headersRow.createCell(17);
			header18.setCellValue("Psicólogo");
			
			Cell header19 = headersRow.createCell(18);
			header19.setCellValue("Estatus de admisiones");
			
			Cell header20 = headersRow.createCell(19);
			header20.setCellValue("Estatus reporte OV");
			
			Cell header21 = headersRow.createCell(20);
			header21.setCellValue("Última modificación");
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			DateFormat dformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			// Datos
			for (int i = 0; i < lstParams.size(); ++i) {
				Row row = sheet.createRow(++rowCount);

				Cell cell1 = row.createCell(0);
				cell1.setCellValue(lstParams.get(i).get("idbanner"));
				
				Cell cell2 = row.createCell(1);
				String nombre = lstParams.get(i).get("apellidopaterno") + " " + lstParams.get(i).get("apellidomaterno") + " " + lstParams.get(i).get("primernombre") + " " + lstParams.get(i).get("segundonombre");
				cell2.setCellValue(nombre);
				
				Cell cell3 = row.createCell(2);
				cell3.setCellValue(lstParams.get(i).get("correoelectronico"));
				
				Cell cell4 = row.createCell(3);
				cell4.setCellValue(lstParams.get(i).get("curp"));
				
				Cell cell5 = row.createCell(4);
				cell5.setCellValue(lstParams.get(i).get("campus"));
				
				Cell cell6 = row.createCell(5);
				cell6.setCellValue(lstParams.get(i).get("licenciatura"));
				
				Cell cell7 = row.createCell(6);
				cell7.setCellValue(lstParams.get(i).get("ingreso"));
				
				Cell cell8 = row.createCell(7);
				cell8.setCellValue(lstParams.get(i).get("procedencia"));
				
				Cell cell9 = row.createCell(8);
				cell9.setCellValue(lstParams.get(i).get("preparatoria"));
				
				Cell cell10 = row.createCell(9);
				cell10.setCellValue(lstParams.get(i).get("promediogeneral"));
				
				Cell cell11 = row.createCell(10);
				cell11.setCellValue(lstParams.get(i).get("residensia"));
				
				Cell cell12 = row.createCell(11);
				cell12.setCellValue(lstParams.get(i).get("tipoadmision"));
				
				Cell cell13 = row.createCell(12);
				cell13.setCellValue(lstParams.get(i).get("tipodealumno"));
				
				Cell cell14 = row.createCell(13);
				cell14.setCellValue(lstParams.get(i).get("sesion"));
				
				Cell cell15 = row.createCell(14);
				String fechaEntrevista = lstParams.get(i).get("idsesion");
				cell15.setCellValue(fechaEntrevista);
				
				Cell cell16 = row.createCell(15);
				cell16.setCellValue(lstParams.get(i).get("fechaentrevista"));
				
				Cell cell17 = row.createCell(16);
				cell17.setCellValue(lstParams.get(i).get("horario"));
				
				Cell cell18 = row.createCell(17);
				cell18.setCellValue(lstParams.get(i).get("responsableid"));
				
				Cell cell19 = row.createCell(18);
				cell19.setCellValue(lstParams.get(i).get("estatussolicitud"));
				
				Cell cell20 = row.createCell(19);
				String finalizado = lstParams.get(i).get("finalizado");
				if ("f".equals(finalizado)) {
				    finalizado = "Finalizado";
				} else if ("s".equals(finalizado)) {
				    finalizado = "Sin iniciar";
				}
				cell20.setCellValue(finalizado);
				
				Cell cell21 = row.createCell(20);
				String fechaUltimaModificacion = lstParams.get(i).get("fechaultimamodificacion");
				if (fechaUltimaModificacion != null) {
				    Date fechaRegistro = dfSalida.parse(fechaUltimaModificacion);
				    String fechaFormateada = dformat.format(fechaRegistro);
				    cell21.setCellValue(fechaFormateada);
				} else {
				    cell21.setCellValue("N/A");
				}
			}
			
			for (int i = 0; i < 19; ++i) {
			    sheet.autoSizeColumn(i);
			}
	
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			
			List<Object> lstResultado = new ArrayList<Object>();
			lstResultado.add(Base64.getEncoder().encodeToString(outputStream.toByteArray()));
			resultado.setError_info(errorLog);
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
		} catch (Exception e) {
			LOGGER.error("[ERROR] " + e.getMessage());
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		}
	
		return resultado;
	}

	
}
