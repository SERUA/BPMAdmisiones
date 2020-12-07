package com.anahuac.rest.api.DAO

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement

class SolicitudUsuarioDAO {
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
	public Result getIdbanner(Integer parameterP, Integer parameterC, String idbanner, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_IDBANNER_BY_IDBANNER)
				pstm.setString(1, idbanner)

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
			
				resultado.setError_info("error al obtener GET_IDBANNER_BY_IDBANNER")
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
