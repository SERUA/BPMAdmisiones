package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.db.Comentarios

import groovy.json.JsonSlurper

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class BitacoraDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogosDAO.class);
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
	
	
	public Result getComentariosByCaseid(Long caseId) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Comentarios comentario = new Comentarios();
		List<Comentarios> lstComentarios = new ArrayList<Comentarios>();
		String errorLog = "";
		try {
			closeCon = validarConexion();
			String consulta = Statements.GET_COMENTARIOS_BITACORA;
			pstm = con.prepareStatement(consulta);
			pstm.setLong(1, caseId);
			
			rs = pstm.executeQuery();
			errorLog += consulta;
			errorLog += " " + caseId.toString();
			while(rs.next()) {
				comentario = new Comentarios();
				comentario.setComentario(rs.getString("COMENTARIO"));
				comentario.setFechaCreacion(rs.getString("FECHACREACION"));
				comentario.setModulo(rs.getString("MODULO"));
				comentario.setUsuarioComentario(rs.getString("USUARIOCOMENTARIO"));
				comentario.setUsuario(rs.getString("USUARIO"));
				comentario.setCaseId(rs.getLong("CASEID"));
				
				lstComentarios.add(comentario);
			}

			resultado.setError(errorLog);
			resultado.setData(lstComentarios);
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError("[getCatTipoMoneda] " + e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result insertComentario(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		def jsonSlurper = new JsonSlurper();
		def objComentario = jsonSlurper.parseText(jsonData);
		String errorLog = "";
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.INSERT_COMENTARIO_SDAE);
			pstm.setString(1, objComentario.comentario);
			pstm.setString(2, objComentario.modulo);
			pstm.setString(3, objComentario.usuario);
			pstm.setString(4, objComentario.usuarioComentario);
			pstm.setString(5, Long.valueOf(objComentario.caseId));
			pstm.execute();
			con.commit();
			resultado.setSuccess(true);
				
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError("[insertarCatTipoMoneda] " + e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
}
