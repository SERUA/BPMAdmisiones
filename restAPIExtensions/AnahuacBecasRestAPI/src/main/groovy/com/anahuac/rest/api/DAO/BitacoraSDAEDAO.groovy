package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.SimpleDateFormat

import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.bonitasoft.web.extension.rest.RestAPIContext

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result

import groovy.json.JsonSlurper

class BitacoraSDAEDAO {
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

	public Result cargarBitacora(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		ProcessInstance processInstance = null;
		ProcessAPI processAPI = null;
		Map < String, Serializable > contracto = new HashMap < String, Serializable > ();
		Map < String, Serializable > objBitacoraSDAEInput = new HashMap < String, Serializable > ();
		List < Map < String, Serializable >> lstBitacoraSDAEInput = new ArrayList < Map < String, Serializable >> ();

		String info = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			List < ProcessDeploymentInfo > lstProcessDeploymentInfo = new ArrayList < ProcessDeploymentInfo > ();
			SearchOptions searchOptionsProccess = null;
			Long processId = null;
			searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Agregar BitacoraSDAE");
			searchOptionsProccess = searchBuilderProccess.done();
			SearchResult < ProcessDeploymentInfo > SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			for (ProcessDeploymentInfo objProcessDeploymentInfo: lstProcessDeploymentInfo) {
				if (objProcessDeploymentInfo.getActivationState().toString().equals("ENABLED")) {
					processId = objProcessDeploymentInfo.getProcessId();
					info+="entro";
				}
			}


			objBitacoraSDAEInput.put("idBanner", object?.idBanner)
			objBitacoraSDAEInput.put("estatus", isNullOrBlanck(object?.estatus));
			objBitacoraSDAEInput.put("correo", object?.correo)
			objBitacoraSDAEInput.put("usuario", context.getApiSession().getUserName())
			objBitacoraSDAEInput.put("comentario", isNullOrBlanck(object?.comentario))
			objBitacoraSDAEInput.put("beca", isNullOrBlanck(object?.beca))
			objBitacoraSDAEInput.put("financiamiento", isNullOrBlanck(object?.financiamiento));
			
			lstBitacoraSDAEInput.add(objBitacoraSDAEInput);
			contracto.put("lstBitacoraSDAEInput", lstBitacoraSDAEInput);

			processAPI = context.getApiClient().getProcessAPI();

			processInstance = processAPI.startProcessWithInputs(processId, contracto);
			resultado.setSuccess(true);
		}catch(Exception e) {
			resultado.setError(e.getMessage())
			resultado.setSuccess(false);
			resultado.setError_info(info)
		}

		return resultado;
	}
	
	private String isNullOrBlanck(String text) {
		if(text == null || text.equals(null) || text.equals("null") || text.equals("") || text.length() == 0) {
			return "N/A"
		}
		
		return text;
	}

	public Result getBitacoraSDAE(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "",campus = "", orderby = "ORDER BY ", errorlog = "";
		List < String > lstGrupo = new ArrayList < String > ();

		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();

			String consulta = Statements.GET_BITACORA_SDAE
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				errorlog = consulta + " 1";
				switch (filtro.get("columna")) {

					case "ID BANNER":
						where += " AND (LOWER(IDBANNER) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					case "CORREO":
						where += " AND (LOWER(CORREO) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ESTATUS":
						where += " AND (LOWER(ESTATUS) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "COMENTARIO":
						where += " AND (LOWER(COMENTARIO) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "BECA":
						where += " AND (LOWER(BECA) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "FINANCIAMIENTO":
						where += " AND (LOWER(FINANCIAMIENTO) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "USUARIO":
						where += " AND (LOWER(USUARIOS) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "FECHA":
						where += " AND (LOWER(FECHA) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						

					default:
						break;
				}

			}
			errorlog = consulta + " 2";
			switch (object.orderby) {
				case "Id Banner":
					orderby += "IDBANNER";
					break;
				case "Correo":
					orderby += "CORREO";
					break;
				case "Estatus":
					orderby += "ESTATUS";
					break;
				case "Comentario":
					orderby += "Comentario";
					break;
				case "Beca":
					orderby += "Beca"
					break;
				case "Financiamiento":
					orderby += "Financiamiento"
					break;
				case "Usuario":
					orderby += "Usuarios"
					break;
				case "Fecha":
					orderby += "Fecha"
					break;
				default:
					orderby += "IDBANNER"
					break;
			}
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);

			pstm = con.prepareStatement(consulta.replace("idBanner,correo,estatus,comentario,beca,financiamiento,usuarios,fecha", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
			//errorlog = consultaCount + " 6";
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}

			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorlog = consulta + " 7";

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
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				rows.add(columns);
			}

			errorlog = consulta + " 9";
			resultado.setSuccess(true)
			resultado.setError_info(errorlog);
			resultado.setData(rows)

		} catch (Exception e) {
			resultado.setError_info(errorlog)
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getBitacoraSDAEByCaseId(Long caseId, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "",campus = "", orderby = "", errorlog = "";
		List < String > lstGrupo = new ArrayList < String > ();

		Long userLogged = 0L;
		Long total = 0L;
		String errorLog = "";
		
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();

			String consulta = Statements.GET_BITACORA_SDAE_CASEID;
			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, caseId);
			errorLog += consulta;
			rs = pstm.executeQuery();
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();
				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().toLowerCase().equals("fecha")) {
						String ff = rs.getString(i).split("\\.")[0];
//						SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
//						Date fecha = formatter.parse(ff);
//						String sDate = formatter.format(fecha);
						columns.put(metaData.getColumnLabel(i).toLowerCase(), ff);
					} else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
				}
				
				rows.add(columns);
			}
			
			resultado.setSuccess(true);
			resultado.setError_info(errorlog);
			resultado.setData(rows);
		} catch (Exception e) {
			resultado.setError_info(errorLog);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}

	public Result insertBitacoraSDAE(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		def jsonSlurper = new JsonSlurper();
		def objetoBitacoraSDAE = jsonSlurper.parseText(jsonData);
		String errorLog = "";
		
		try {
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.INSERT_BITACORA_SDAE);
			pstm.setString(1, objetoBitacoraSDAE.beca);
			pstm.setString(2, objetoBitacoraSDAE.comentario);
			pstm.setString(3, objetoBitacoraSDAE.correo);
			pstm.setString(4, objetoBitacoraSDAE.estatus);
			pstm.setString(5, objetoBitacoraSDAE.financiamiento);
			pstm.setString(6, objetoBitacoraSDAE.idbanner);
			pstm.setString(7, objetoBitacoraSDAE.usuarios);
			pstm.setLong(8, Long.valueOf(objetoBitacoraSDAE.caseid));
			pstm.setString(9, objetoBitacoraSDAE.tipoapoyo);
			
			pstm.execute();
			
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertBitacoraSDAE]: " + e.getMessage());
			
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
}
