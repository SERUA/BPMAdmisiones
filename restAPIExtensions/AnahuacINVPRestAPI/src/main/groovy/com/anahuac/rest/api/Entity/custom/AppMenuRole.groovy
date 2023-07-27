package com.anahuac.rest.api.Entity.custom

import com.anahuac.rest.api.Entity.db.BusinessAppMenu
import com.anahuac.rest.api.Entity.db.Role

class AppMenuRole extends BusinessAppMenu{
	public static String GET="SELECT b.tenantid, b.id, b.displayname, b.applicationid, b.applicationpageid, b.parentid, b.index_, r.id   AS roleid, r.name AS rolename FROM business_app_menu b LEFT JOIN app_menu_role amr ON amr.business_app_menuid=b.displayname LEFT JOIN business_app ba ON b.applicationid=ba.id LEFT JOIN role r ON r.id=amr.roleid WHERE ba.displayname='Administrativo INVP' ORDER BY index_"
	public static String INSERT="INSERT INTO app_menu_role (business_app_menuid, roleid ) VALUES (?, ? )";
	public static String DELETE ="DELETE FROM app_menu_role WHERE business_app_menuid=? AND roleid=? "
	public static String CREATE ="CREATE TABLE public.app_menu_role (id bigserial NOT NULL, business_app_menuid varchar(255) NOT NULL, roleid bigint NOT NULL, PRIMARY KEY (id) );"
	private List<Role> roles;

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
