package com.anahuac.rest.api.Entity.custom

class ModuloUsuario {
	public static String GET_USUARIOS_CUSTOM="Select user_.enabled, user_.firstname, user_.lastname, user_.creationdate, user_.userName, ul.lastconnection last_connection,user_.id, STRING_AGG(role.name || ' en ' || group_.name, ',' order by role.name ) membresia from user_ user_ left join user_membership membership on membership.userid=user_.id left join group_ group_ on group_.id=membership.groupid  left join role role on role.id=membership.roleid left join user_login ul on ul.id=user_.id  [WHERE] GROUP BY user_.enabled, user_.firstname, user_.lastname, user_.creationdate, user_.userName, ul.lastconnection,user_.id [ORDERBY] [LIMITOFFSET]"
	private String firstname;
	private String lastname;
	private String userName;
	private Long last_connection;
	private Long creation_date;
	private Long id;
	private String membresia;
	private Boolean enabled;
	
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Long getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Long creation_date) {
		this.creation_date = creation_date;
	}
	public String getFirstname() {
		return firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public Long getLast_connection() {
		return last_connection;
	}
	public Long getId() {
		return id;
	}
	public String getMembresia() {
		return membresia;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public void setLast_connection(Long last_connection) {
		this.last_connection = last_connection;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setMembresia(String membresia) {
		this.membresia = membresia;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
