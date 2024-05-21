package com.anahuac.rest.api.Security

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion

import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result

class SecurityFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Boolean bonitaRolFilter(RestAPIContext context, String roleName) {
		Boolean valid = false;
		List<UserMembership> uMemberships=context.apiClient.identityAPI.getUserMemberships(context.apiSession.userId, 0, 100, UserMembershipCriterion.ROLE_NAME_ASC);
		uMemberships.each{
			it ->
			if(it.roleName.toLowerCase().equals(roleName.toLowerCase()) || it.roleName.equals("ADMINISTRADOR") || it.roleName.equals("TI SERUA") || it.roleName.equals("SERUA") || it.roleName.equals("TI CAMPUS") || it.roleName.equals("PSICOLOGO") || it.roleName.equals("PSICOLOGO SUPERVISOR") || it.roleName.equals("PASE DE LISTA")) {
				valid=true
			}
		}
		
		return valid;
	} 
	
	public String getRoleList(RestAPIContext context) {
		Boolean valid = false;
		String roles = "";
		List<UserMembership> uMemberships = context.apiClient.identityAPI.getUserMemberships(context.apiSession.userId, 0, 100, UserMembershipCriterion.ROLE_NAME_ASC);
		
		int totalMemberships = uMemberships.size();
		
		uMemberships.eachWithIndex { membership, index ->
			if (roles.isEmpty()) {
				roles += "(";
			}
			
			roles += "'" + membership.roleName.toLowerCase() + "'";
			
			if (index < (totalMemberships - 1)) {
				roles += ", ";
			}
		}
		
		roles += ")";
		
		return roles;
	}
	
	
	public Result allowedUrl(RestAPIContext context, String serviceName) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String roleList = "";
		
		try {
			roleList = getRoleList(context);
			if(roleList.toLowerCase().contains("administrador") || roleList.toLowerCase().contains("ti serua")) {
				resultado.setSuccess(true);
			} else {
				closeCon = validarConexion();
				String consulta = "SELECT COUNT(*) > 0 AS tiene_permiso FROM PSGRFiltroSeguridad WHERE servicio = ? AND LOWER(rol) IN [ROLELIST]";
				
				pstm = con.prepareStatement(consulta.replace("[ROLELIST]", roleList));
				pstm.setString(1, serviceName);
				rs = pstm.executeQuery();
				if(rs.next()){
					if(rs.getBoolean("tiene_permiso") == true) {
						resultado.setSuccess(true);
					} else {
						throw  new Exception("No tienes permisos");
					}
				} else {
					throw  new Exception("No tienes permisos");
				}
			}
			
		} catch (Exception e) {
			LOGGER.error "[allowedUrl|ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError("[allowedUrl] " + e.getMessage());
		} finally {
			new DBConnect().closeObj(con, stm, rs, pstm);
		}
	
		return resultado;
	}
	
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		
		return retorno;
	}
}
