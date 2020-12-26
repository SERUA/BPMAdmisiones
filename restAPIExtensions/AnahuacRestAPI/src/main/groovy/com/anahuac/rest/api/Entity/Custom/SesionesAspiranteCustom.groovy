package com.anahuac.rest.api.Entity.Custom

class SesionesAspiranteCustom {
	
	private Long session_pid;
	private String username;
	private Boolean iseliminado;
	private Long persistenceid;
	private String fecha;
	
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	private List<Map<String, Object>> Aspirantes;
	
	public Long getSession_pid() {
		return session_pid;
	}
	public void setSession_pid(Long session_pid) {
		this.session_pid = session_pid;
	}
	public String getUsername() {
		return username;
	}
	public List<Map<String, Object>> getAspirantes() {
		return Aspirantes;
	}
	public void setAspirantes(List<Map<String, Object>> aspirantes) {
		Aspirantes = aspirantes;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Boolean getIseliminado() {
		return iseliminado;
	}
	public void setIseliminado(Boolean iseliminado) {
		this.iseliminado = iseliminado;
	}
	public Long getPersistenceid() {
		return persistenceid;
	}
	public void setPersistenceid(Long persistenceid) {
		this.persistenceid = persistenceid;
	}
	
}
