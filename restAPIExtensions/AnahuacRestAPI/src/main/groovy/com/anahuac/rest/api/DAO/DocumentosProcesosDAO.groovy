package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import org.slf4j.Logger

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DocumentosProcesosDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentosProcesosDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Boolean validarConexion() {
		Boolean retorno = false;
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno = true;
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
	
	private static final String GET_CASOS_BY_ESTATUS_Y_RANGO = " SELECT persistenceid, caseid, fecharegistro::TIMESTAMP, estatussolicitud  FROM solicituddeadmision WHERE fecharegistro::TIMESTAMP < TO_TIMESTAMP('01/01/2023', 'DD/MM/YYYY')  AND estatussolicitud IN ([ESTATUS]) LIMIT ? OFFSET ? ";
	private static final String GET_DOCUMENT_TO_DELETE = "SELECT tenantid, id, processinstanceid, documentid, name, description, version, index_ FROM document_mapping WHERE processinstanceid = ? AND name != 'fotoPasaporte'";
	private static final String DELETE_DOCUMENT = "DELETE FROM document WHERE id IN ([LSTDOCUMENTOID]);";
	private static final String DELETE_DOCUMENT_MAPPING = "DELETE FROM document_mapping WHERE processinstanceid = ? AND name != 'fotoPasaporte'";
//	'Aspirantes registrados sin validación de cuenta',
//	'Aspirantes registrados con validación de cuenta',
//	'Solicitud vencida',
//	'Solicitud caduca',
//	'Solicitud a modificar',
//	'Solicitud en proceso',
//	'Solicitud recibida'
	
	public Result getCasosALimpiar(String estatus, Integer offset, Integer limit) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<Long> lstCasos = new ArrayList<Long>();
		
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(GET_CASOS_BY_ESTATUS_Y_RANGO.replace("[ESTATUS]", estatus));
			pstm.setInt(1, limit);
			pstm.setInt(2, offset);
			
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				lstCasos.add(rs.getLong("caseid"));
			}
			
			con.close();
			
//			deleteDocumentBonita()
			resultado.setData(lstCasos);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result deleteDocumentBonita(Long caseId) {
		Result resultado = new Result();
		
		Boolean closeCon = false;
		
		List<Integer> lstDocumentId = new ArrayList<Integer>();
		
		try {
			closeCon = validarConexionBonita();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(GET_DOCUMENT_TO_DELETE);
			pstm.setLong(1, caseId);
			
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				lstDocumentId.add(rs.getInt("documentid"));
			}
			
			if(lstDocumentId.size() > 0) {
				pstm = con.prepareStatement(DELETE_DOCUMENT.replace("[LSTDOCUMENTOID]", lstDocumentId.join(",")));
				pstm.executeUpdate();
				
				pstm = con.prepareStatement(DELETE_DOCUMENT_MAPPING);
				pstm.setLong(1, caseId);
				pstm.executeUpdate();
			}
			
			con.commit();
			con.setAutoCommit(true);
			resultado.setSuccess(true);
		} catch (Exception e) {
			con.rollback();
			con.setAutoCommit(true);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
}
