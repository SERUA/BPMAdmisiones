package com.anahuac.rest.api.Utilities

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.DBConnectBonita
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.AzureConfig
import com.anahuac.rest.api.Entity.Result

class LoadParametros {
    Connection conBonita;
    Statement stmBonita;
    ResultSet rsBonita;
    PreparedStatement pstmBonita;
	
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadParametros.class);

    public PropertiesEntity getParametros() throws Exception {
        PropertiesEntity objProperties = new PropertiesEntity();
        List<PropertiesEntity> lstProperties = new ArrayList<PropertiesEntity>();
        Boolean closeConBonita = false;
		Boolean closeCon = false;
        String errorLog = "";
        try {
            closeConBonita = validarConexionBonita();
			closeCon= validarConexion();
            pstmBonita = conBonita.prepareStatement(Statements.SELECT_PROPERTIES_BONITA);
            rsBonita = pstmBonita.executeQuery();
            while(rsBonita.next()) {
                if(rsBonita.getString("name").equals("password")) {
                    objProperties.setPassword(rsBonita.getString("value"));
                }
                if(rsBonita.getString("name").equals("usuario")) {
                    objProperties.setUsuario(rsBonita.getString("value"));
                }
            }
			
			pstm = con.prepareStatement(AzureConfig.GET_CONFIGURACIONES_CLAVE);
			pstm.setString(1, "urlHost");
			rs = pstm.executeQuery();
			while(rs.next()) {
				objProperties.setUrlHost(rs.getString("valor"));
			}
			
        } catch (Exception e) {
            LOGGER.error "[ERROR] " + e.getMessage();
        } finally {
            if(closeConBonita) {
				new DBConnectBonita().closeObj(con, stm, rs, pstm);
            }
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
        }
        return objProperties
    }
	
	public Boolean validarConexion() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnectBonita().getConnection();
			retorno = true
		}
		return retorno
	}
	
    public Boolean validarConexionBonita() {
        Boolean retorno=false
        if (conBonita == null || conBonita.isClosed()) {
            conBonita = new DBConnectBonita().getConnectionBonita();
            retorno=true
        }
        return retorno;
    }

}

