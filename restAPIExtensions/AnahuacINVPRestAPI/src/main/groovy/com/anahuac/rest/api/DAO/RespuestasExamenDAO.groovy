package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.DateFormat
import java.text.SimpleDateFormat
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.identity.UserNotFoundException
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.invp.RespuestasExamen
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.custom.CatPreguntasCustomFiltro

import groovy.json.JsonSlurper

class RespuestasExamenDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(RespuestasExamenDAO.class);
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
	
	public Result getCatRespuestasExamen(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "WHERE ISELIMINADO=false"
		String errorlog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			//CatPreguntasCustomFiltro row = new CatPreguntasCustomFiltro()
			//List < CatPreguntasCustomFiltro > rows = new ArrayList < CatPreguntasCustomFiltro > ();
			com.anahuac.invp.CatPreguntas row = new com.anahuac.invp.CatPreguntas();
			List < com.anahuac.invp.CatPreguntas > rows = new ArrayList < com.anahuac.invp.CatPreguntas > ();
			closeCon = validarConexion();

			List<com.anahuac.invp.RespuestasExamen> respuestaList = new ArrayList<com.anahuac.invp.RespuestasExamen>();
			def respuestaVar = new com.anahuac.invp.RespuestasExamen();
			
			errorlog += "consulta:";
			pstm = con.prepareStatement(Statements.GET_CAT_PREGUNTAS_EXAMEN.replace("[WHERE]", where));
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new com.anahuac.invp.CatPreguntas();
				row.setPersistenceId(rs.getInt("persistenceid"));
				row.setPersistenceVersion(rs.getLong("persistenceversion"));
				row.setPregunta(rs.getString("pregunta"));
				row.setCaseId(rs.getString("CASEID"));
				row.setOrden(rs.getInt("orden"));
				row.setQuestion(rs.getString("question"));
				row.setIsEliminado(rs.getBoolean("isEliminado"));
				row.setUsuarioCreacion(rs.getString("usuarioCreacion"));
				try {
					row.setFechaCreacion(rs.getString("fechacreacion"))
				} catch (Exception e) {
					LOGGER.error "[ERROR] " + e.getMessage();
					errorlog += e.getMessage()
				}
				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				rows.add(row)
				respuestaVar = new com.anahuac.invp.RespuestasExamen();
				respuestaVar.contestada = false;
				respuestaVar.objPreguntas = row;
				respuestaVar.caseId = Long.valueOf(object.caseid);
				respuestaVar.idUsuario = Long.valueOf(object.userId);
				respuestaList.add(respuestaVar);
				
			}
			
			resultado.setSuccess(true)

			resultado.setData(respuestaList)
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
	
	public Result insertRespuesta( String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Boolean terminado = false;
		String idioma = "ESP";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			pstm = con.prepareStatement(Statements.GET_EXAMEN_TERMINADO_IDIOMA);
			pstm.setString(1, object.username);
			
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				terminado = rs.getBoolean("terminado");
//				idioma = rs.getString("idioma");
			} 
			
			if(!terminado) {
				pstm = con.prepareStatement(Statements.INSERT_RESPUESTA_EXAMEN, Statement.RETURN_GENERATED_KEYS)
				pstm.setLong(1, object.pregunta);
				pstm.setBoolean(2, object.respuesta);
				pstm.setLong(3,Long.valueOf(object.caseid));
				pstm.setLong(4,Long.valueOf(object.idusuario));
				pstm.setString(5, object.username);				
				pstm.executeUpdate();
				
				con.commit();
				
				resultado.setSuccess(true);
			} else {
				String mensaje = "test_end";
				throw new Exception(mensaje);
			}
		} catch (Exception e) {
			String es = e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(es);
			con.rollback();
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result getCatRespuestasExamenbyUsuariocaso(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			com.anahuac.invp.RespuestaINVP row = new com.anahuac.invp.RespuestaINVP();
			List < com.anahuac.invp.RespuestaINVP > rows = new ArrayList < com.anahuac.invp.RespuestaINVP > ();
			closeCon = validarConexion();

			List<com.anahuac.invp.RespuestasExamen> respuestaList = new ArrayList<com.anahuac.invp.RespuestasExamen>();
			def respuestaVar = new com.anahuac.invp.RespuestasExamen();
			
			errorlog += "consulta:";
			pstm = con.prepareStatement(Statements.GET_RESPUESTAS_BY_USUARIOCASO);
			pstm.setLong(1, object.idusuario)
			pstm.setLong(2,object.caseid);
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new com.anahuac.invp.RespuestaINVP();
				row.setPersistenceId(rs.getInt("persistenceid"));
				row.setPregunta(rs.getInt("pregunta"));
				row.setCaseid(rs.getLong("caseid"));
				row.setIdusuario(rs.getLong("idusuario"));
				row.setRespuesta(rs.getBoolean("respuesta"))
				rows.add(row)

			}
			
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
	
	public Result getCaseIdbyuserid(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			com.anahuac.invp.RespuestaINVP row = new com.anahuac.invp.RespuestaINVP();
			List < com.anahuac.invp.RespuestaINVP > rows = new ArrayList < com.anahuac.invp.RespuestaINVP > ();
			closeCon = validarConexion();

			List<com.anahuac.invp.RespuestasExamen> respuestaList = new ArrayList<com.anahuac.invp.RespuestasExamen>();
			def respuestaVar = new com.anahuac.invp.RespuestasExamen();
			
			errorlog += "consulta:";
			pstm = con.prepareStatement(Statements.GET_RESPUESTAS_BY_USUARIO);
			pstm.setLong(1, Long.valueOf(object.idusuario));
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new com.anahuac.invp.RespuestaINVP();
				row.setPersistenceId(rs.getInt("persistenceid"));
				row.setPregunta(rs.getInt("pregunta"));
				row.setCaseid(rs.getLong("caseid"));
				row.setIdusuario(rs.getLong("idusuario"));
				row.setRespuesta(rs.getBoolean("respuesta"))
				rows.add(row)

			}
			
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
	
	public Result updateRespuesta( String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Boolean terminado = false;
		String idioma = "ESP";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			pstm = con.prepareStatement(Statements.GET_EXAMEN_TERMINADO_IDIOMA);
			pstm.setString(1, object.username);
			
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				terminado = rs.getBoolean("terminado");
//				idioma = rs.getString("idioma");
			}
			
			if(!terminado) {
				pstm = con.prepareStatement(Statements.UPDATE_RESPUESTAS)
				pstm.setBoolean(1, object.respuesta);
				pstm.setLong(2, object.pregunta);
				pstm.setLong(3,object.idusuario);
				pstm.setLong(4,object.caseid);
				pstm.executeUpdate();
				
				con.commit();
				
				resultado.setSuccess(true);
			} else {
				String mensaje = "test_end";
				throw new Exception(mensaje);
			}
		} catch (Exception e) {
			String es = e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(es);
			con.rollback();
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado
	}
	
	public Result insertRespuestaid( String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			Long iduser = getidusuariobyusername(object.username);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.INSERT_RESPUESTA_EXAMEN, Statement.RETURN_GENERATED_KEYS)
			pstm.setLong(1, object.pregunta);
			pstm.setBoolean(2, object.respuesta);
			pstm.setLong(3,Long.valueOf(object.caseid));
			pstm.setLong(4,Long.valueOf(iduser));
			pstm.setString(5, object.username);
			pstm.executeUpdate();
			
			con.commit();
			
			resultado.setSuccess(true);
		} catch (Exception e) {
			String es = e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(es);
			con.rollback();
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Long getidusuariobyusername(String username) {
		Long iduser = 0;
		Boolean closeCon = false;
		
		try {
		    closeCon = validarConexionBonita();
		    pstm = con.prepareStatement(Statements.GET_ID_USER_BY_USERNAME)
		    pstm.setString(1, username);
		    rs = pstm.executeQuery();
		
		    while (rs.next()) {
				iduser = rs.getLong("id");
		    }
		} catch (Exception e) {
			String es = e.getMessage();
		} finally {
		    if (closeCon) {
		      new DBConnect().closeObj(con, stm, rs, pstm)
		    }
		}
		return iduser
	}
	//
	
	
	
	public Result generarRespuestasArchivo(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String respuestas = "";
		String errorLog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			respuestas = object.respuestas;
			errorLog += "1"
			String[] respuestasArray = new String[respuestas.length()];
			for (int i = 0; i < respuestas.length(); i++) {
				respuestasArray[i] = String.valueOf(respuestas.charAt(i));
			}
			errorLog += "|2"
			
			closeCon = validarConexion();
			
			pstm = con.prepareStatement("SELECT orden, persistenceid, pregunta, question FROM CatPReguntas ORDER BY orden ASC");
			rs = pstm.executeQuery();
			errorLog += "|3"
			
			while(rs.next()) {
				errorLog += "|4[" + respuestasArray[rs.getInt("orden") - 1] + "]";
				if(!respuestasArray[rs.getInt("orden") - 1].equals("*")) {
					pstm = con.prepareStatement(Statements.INSERT_RESPUESTA_EXAMEN, Statement.RETURN_GENERATED_KEYS)
					pstm.setInt(1, rs.getInt("orden"));
					pstm.setBoolean(2, respuestasArray[rs.getInt("orden") - 1].equals("C"));
					pstm.setLong(3, Long.valueOf(object.caseid));
					pstm.setLong(4, Long.valueOf(object.idusuario));
					pstm.setString(5, object.username);
					pstm.executeUpdate();
					errorLog += "|5[" + respuestasArray[rs.getInt("orden") - 1] + "]";
				} else {
					errorLog += "|6[" + respuestasArray[rs.getInt("orden") - 1] + "]";
				}
			}
		} catch (Exception e) {
			String es = e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(es);
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	
}
