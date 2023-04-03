package com.anahuac.rest.api.Entity.db

class Role {
	private Long tenantid;
	private Long id;
	private String name;
	private String displayname;
	private String description;
	private Long createdby;
	private Long creationdate;
	private Long lastupdate;
	private Long iconid;
	private Boolean eliminado;
	private Boolean nuevo;
	
	public Boolean getNuevo() {
		return nuevo;
	}
	public void setNuevo(Boolean nuevo) {
		this.nuevo = nuevo;
	}
	public Boolean getEliminado() {
		return eliminado;
	}
	public void setEliminado(Boolean eliminado) {
		this.eliminado = eliminado;
	}
	public Long getTenantid() {
		return tenantid;
	}
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDisplayname() {
		return displayname;
	}
	public String getDescription() {
		return description;
	}
	public Long getCreatedby() {
		return createdby;
	}
	public Long getCreationdate() {
		return creationdate;
	}
	public Long getLastupdate() {
		return lastupdate;
	}
	public Long getIconid() {
		return iconid;
	}
	public void setTenantid(Long tenantid) {
		this.tenantid = tenantid;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setCreatedby(Long createdby) {
		this.createdby = createdby;
	}
	public void setCreationdate(Long creationdate) {
		this.creationdate = creationdate;
	}
	public void setLastupdate(Long lastupdate) {
		this.lastupdate = lastupdate;
	}
	public void setIconid(Long iconid) {
		this.iconid = iconid;
	}
	
	@Override
	public boolean equals(Object arg0) {
		Boolean part1=arg0 != null;
		Boolean part2=arg0 instanceof Role;
		Boolean part3=((Role) arg0).getId().equals(this.id);
		return (part1 && part2 && part3);
	}
	
}
