package com.anahuac.rest.api.DAO

import static org.junit.Assert.format

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.document.DocumentValue
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessDefinition
import org.bonitasoft.engine.identity.ContactDataCreator
import org.bonitasoft.engine.identity.ContactDataUpdater
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserCreator
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.identity.UserUpdater
import org.bonitasoft.engine.profile.Profile
import org.bonitasoft.engine.profile.ProfileMemberCreator
import org.bonitasoft.engine.search.Order
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.bonitasoft.engine.bpm.contract.FileInputValue
import org.apache.commons.codec.binary.Base64;

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Menu
import com.anahuac.rest.api.Entity.MenuParent
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.custom.AppMenuRole
import com.anahuac.rest.api.Utilities.FileDownload
import com.anahuac.rest.api.Utilities.LoadParametros
import groovy.json.JsonSlurper

class UsuariosDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(UsuariosDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;

	public Boolean bonitaRolFilter(RestAPIContext context,String groupName) {
		Boolean valid = false; 
		List<UserMembership> uMemberships=context.apiClient.identityAPI.getUserMemberships(context.apiSession.userId, 0, 100, UserMembershipCriterion.ROLE_NAME_ASC);
		uMemberships.each{
			it ->
			if(it.groupName.toLowerCase().equals(groupName.toLowerCase()) ) {
				valid=true
			}
		}
		
		return valid;
	}
	
	public Result getMenuAdministrativo(RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			MenuParent row = new MenuParent()
			List<MenuParent> rows = new ArrayList<MenuParent>();
			closeCon = validarConexionBonita();
			pstm = con.prepareStatement(MenuParent.GET);
			pstm.setLong(1,context.apiSession.userId);
			rs = pstm.executeQuery();
			rows = new ArrayList<MenuParent>();

			while(rs.next()) {
				row = new MenuParent();
				row.setId(rs.getLong("id"));
				row.setIsparent(rs.getBoolean("isparent"));
				row.setUrl(rs.getString("url"));
				row.setToken(rs.getString("token"));
				row.setMenu(rs.getString("menu"));
				row.setDisplayname(rs.getString("Displayname"));
				row.setParent(rs.getString("parent"));
				row.setParentid(rs.getLong("parentid"));
				row.setParenttoken(rs.getString("parenttoken"));
				if(rs.getBoolean("isparent")) {
					row = new MenuParent();
					row.setId(rs.getLong("id"));
					row.setIsparent(rs.getBoolean("isparent"));
					row.setUrl(rs.getString("url"));
					row.setToken(rs.getString("token"));
					row.setMenu(rs.getString("menu"));
					row.setDisplayname(rs.getString("Displayname"));
					row.setParent(rs.getString("parent"));
					row.setParentid(rs.getLong("parentid"));
					row.setParenttoken(rs.getString("parenttoken"));
					row.setChild(new ArrayList<Menu>());
					rows.add(row);
				}else {
					Menu menu = new Menu();
					menu.setId(rs.getLong("id"));
					menu.setIsparent(rs.getBoolean("isparent"));
					menu.setUrl(rs.getString("url"));
					menu.setToken(rs.getString("token"));
					menu.setMenu(rs.getString("menu"));
					menu.setDisplayname(rs.getString("Displayname"));
					menu.setParent(rs.getString("parent"));
					menu.setParentid(rs.getLong("parentid"));
					menu.setParenttoken(rs.getString("parenttoken"));
					if(rows.contains(row)) {
						rows.get(rows.indexOf(row)).getChild().add(menu);
					}
				}
			}
			resultado.setSuccess(true)
			
			resultado.setData(rows)
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			if(e.getMessage().contains("\"app_menu_role\" does not exist") || e.getMessage().contains("Table \"APP_MENU_ROLE\" not found")) {
				try {
					pstm = con.prepareStatement(AppMenuRole.CREATE)
					pstm.execute();
					resultado.setError("La tabla app_menu_role no existía, y ya fue creada, favor de ejecutar la consulta de nuevo.")
				} catch (Exception e2) {
					LOGGER.error "[ERROR] " + e2.getMessage();
					resultado.setError("falló al crear tabla "+e2.getMessage());
				}
				
			} else {
				resultado.setError("No entró al crear tabla "+e.getMessage());
			}
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		return retorno;
	}
	
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true;
		}
		return retorno;
	}
}