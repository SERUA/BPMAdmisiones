package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import com.anahuac.posgrados.catalog.PSGRCatDocumentosTextos
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result

class DocumentosTextosDAO {
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	public static final String GET="SELECT * FROM CATDOCUMENTOSTEXTOS WHERE campus_pid=?"
	public static final String INSERT="INSERT INTO CATDOCUMENTOSTEXTOS  (NOSABES ,TIPSCB ,URLGUIAEXAMENCB , URLTESTVOCACIONAL ,persistenceid,persistenceversion, actividadingreso1, actividadingreso2, cancelarsegurogastosmedicos, ciudadcarta, correodirectoradmisiones, costosgm, cursomatematicas1, cursomatematicas2, directoradmisiones, documentosentregar, documentosentregarextranjero, educaciongarantizada, estadocarta, instruccionespagobanco, notasdocumentos, parrafoespanol1, parrafoespanol2, parrafoespanol3, parrafomatematicas1, parrafomatematicas2, parrafomatematicas3, telefonodirectoradmisiones, titulodirectoradmisiones,instruccionespagocaja, campus_pid) values (?,?,?,?,case when (SELECT max(persistenceId)+1 from CATDOCUMENTOSTEXTOS  ) is null then 1 else (SELECT max(persistenceId)+1 from CATDOCUMENTOSTEXTOS) end,0,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?);"
	public static final String UPDATE="UPDATE CATDOCUMENTOSTEXTOS SET NOSABES=?, TIPSCB=?, URLGUIAEXAMENCB=?, URLTESTVOCACIONAL=?,actividadingreso1=?, actividadingreso2=?, cancelarsegurogastosmedicos=?, ciudadcarta=?, correodirectoradmisiones=?, costosgm=?, cursomatematicas1=?, cursomatematicas2=?, directoradmisiones=?, documentosentregar=?, documentosentregarextranjero=?, educaciongarantizada=?, estadocarta=?, instruccionespagobanco=?, notasdocumentos=?, parrafoespanol1=?, parrafoespanol2=?, parrafoespanol3=?, parrafomatematicas1=?, parrafomatematicas2=?, parrafomatematicas3=?, telefonodirectoradmisiones=?, titulodirectoradmisiones=?, instruccionespagocaja=? where campus_pid=?;"
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		return retorno
	}
	public Result getDocumentosTextos(Long campus_pid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {

			PSGRCatDocumentosTextos row = new PSGRCatDocumentosTextos()
			List<PSGRCatDocumentosTextos> rows = new ArrayList<PSGRCatDocumentosTextos>();
			closeCon = validarConexion();
			String consulta = GET
			pstm = con.prepareStatement(consulta)
			pstm.setLong(1, campus_pid)
			rs = pstm.executeQuery()
				while(rs.next()) {
					row = new PSGRCatDocumentosTextos()
					row.setNoSabes(rs.getString("noSabes"))
					row.setTipsCB(rs.getString("tipsCB"))
					row.setUrlGuiaExamenCB(rs.getString("urlGuiaExamenCB"))
					row.setUrlTestVocacional(rs.getString("urlTestVocacional"))
					row.setCiudadCarta(rs.getString("ciudadCarta"))
					row.setEstadoCarta(rs.getString("estadoCarta"))
					row.setDocumentosEntregar(rs.getString("documentosEntregar"))
					row.setDocumentosEntregarExtranjero(rs.getString("documentosEntregarExtranjero"))
					row.setNotasDocumentos(rs.getString("notasDocumentos"))
					row.setParrafoMatematicas1(rs.getString("parrafoMatematicas1"))
					row.setParrafoMatematicas2(rs.getString("parrafoMatematicas2"))
					row.setParrafoMatematicas3(rs.getString("parrafoMatematicas3"))
					row.setParrafoEspanol1(rs.getString("parrafoEspanol1"))
					row.setParrafoEspanol2(rs.getString("parrafoEspanol2"))
					row.setParrafoEspanol3(rs.getString("parrafoEspanol3"))
					row.setDirectorAdmisiones(rs.getString("directorAdmisiones"))
					row.setTituloDirectorAdmisiones(rs.getString("tituloDirectorAdmisiones"))
					row.setCorreoDirectorAdmisiones(rs.getString("correoDirectorAdmisiones"))
					row.setTelefonoDirectorAdmisiones(rs.getString("telefonoDirectorAdmisiones"))
					row.setActividadIngreso1(rs.getString("actividadIngreso1"))
					row.setActividadIngreso2(rs.getString("actividadIngreso2"))
					row.setCostoSGM(rs.getString("costoSGM"))
					row.setEducacionGarantizada(rs.getString("educacionGarantizada"))
					row.setInstruccionesPagoBanco(rs.getString("instruccionesPagoBanco"))
					row.setCancelarSeguroGastosMedicos(rs.getString("cancelarSeguroGastosMedicos"))
					row.setCursoMatematicas1(rs.getString("cursoMatematicas1"))
					row.setCursoMatematicas2(rs.getString("cursoMatematicas2"))
					row.setInstruccionesPagoCaja(rs.getString("instruccionesPagoCaja"))
					row.setCampus_pid(rs.getLong("campus_pid"))
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
	public Result insertDocumentosTextos(PSGRCatDocumentosTextos row) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			List<PSGRCatDocumentosTextos> rows = new ArrayList<PSGRCatDocumentosTextos>();
			closeCon = validarConexion();
			String consulta = GET
			pstm = con.prepareStatement(consulta)
			pstm.setLong(1, row.getCampus_pid())
			rs = pstm.executeQuery()
					pstm = con.prepareStatement(rs.next()?UPDATE:INSERT)
					pstm.setString(1, row.getNoSabes())
					pstm.setString(2, row.getTipsCB())
					pstm.setString(3, row.getUrlGuiaExamenCB())
					pstm.setString(4, row.getUrlTestVocacional())
					pstm.setString(5,row.actividadIngreso1);//1
					pstm.setString(6,row.actividadIngreso2);//2
					pstm.setString(7,row.cancelarSeguroGastosMedicos);//3
					pstm.setString(8,row.ciudadCarta);//4
					pstm.setString(9,row.correoDirectorAdmisiones);//5
					pstm.setString(10,row.costoSGM);//6
					pstm.setString(11,row.cursoMatematicas1);//7
					pstm.setString(12,row.cursoMatematicas2);//8
					pstm.setString(13,row.directorAdmisiones);//9
					pstm.setString(14,row.documentosEntregar);//10
					pstm.setString(15,row.documentosEntregarExtranjero);//11
					pstm.setString(16,row.educacionGarantizada);//12
					pstm.setString(17,row.estadoCarta);//13
					pstm.setString(18,row.instruccionesPagoBanco);//14
					pstm.setString(19,row.notasDocumentos);//15
					pstm.setString(20,row.parrafoEspanol1);//16
					pstm.setString(21,row.parrafoEspanol2);//17
					pstm.setString(22,row.parrafoEspanol3);//18
					pstm.setString(23,row.parrafoMatematicas1);//19
					pstm.setString(24,row.parrafoMatematicas2);//20
					pstm.setString(25,row.parrafoMatematicas3);//21
					pstm.setString(26,row.telefonoDirectorAdmisiones);//22
					pstm.setString(27,row.tituloDirectorAdmisiones);//23
					pstm.setString(28,row.instruccionesPagoCaja);//23
					pstm.setLong(29, row.getCampus_pid())
					pstm.execute()
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
	
}
