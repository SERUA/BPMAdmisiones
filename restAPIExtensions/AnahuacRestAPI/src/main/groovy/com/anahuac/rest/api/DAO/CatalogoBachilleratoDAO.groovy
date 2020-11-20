package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement

import com.anahuac.catalogos.CatBachilleratos
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.StatementsBachillerato
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class CatalogoBachilleratoDAO {
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	public Result insert(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			CatBachilleratos catBachillerado = new CatBachilleratos();
			List<CatBachilleratos> data =  new ArrayList<CatBachilleratos>();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
				closeCon = validarConexion();
				pstm = con.prepareStatement(StatementsBachillerato.INSERT)
                pstm.setString(1, object.ciudad)
                pstm.setString(2, object.clave)
                pstm.setString(3, object.descripcion)
                pstm.setString(4, object.estado)
                pstm.setString(5, object.pais)
                pstm.setString(6, "")
                pstm.setString(7, object.usuarioBanner)
				
				pstm.execute()
				resultado.setSuccess(true)
				data.add(catBachillerado)
				resultado.setData(data)
			
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
	public Result get(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(StatementsBachillerato.GET)
				
				
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
	public Result update(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			CatBachilleratos catBachillerado = new CatBachilleratos();
			List<CatBachilleratos> data =  new ArrayList<CatBachilleratos>();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
				closeCon = validarConexion();
				pstm = con.prepareStatement(StatementsBachillerato.UPDATE)
				pstm.setString(1, object.ciudad)
                pstm.setString(2, object.clave)
                pstm.setString(3, object.descripcion)
                pstm.setString(4, object.estado)
                pstm.setString(5, object.pais)
                pstm.setString(6, object.usuariobanner)
                pstm.setBoolean(7, object.iseliminado)
                pstm.setBoolean(8, object.isenabled)
                pstm.setLong(9, object.persistenceId)
				
				pstm.execute()
				resultado.setSuccess(true)
				data.add(catBachillerado)
				resultado.setData(data)
			
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
	public void validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
	}
}
