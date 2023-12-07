package com.anahuac.rest.api.Entity

class INVPFiltroSeguridad {
	private Long persistenceid;
	private String rol;
	private String servicio;
	
	public Long getPersistenceid() {
		return persistenceid;
	}
	public void setPersistenceid(Long persistenceid) {
		this.persistenceid = persistenceid;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public String getServicio() {
		return servicio;
	}
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
}
