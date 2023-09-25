package com.anahuac.rest.api.Entity.db

class Sesion_Aspirante {
	private Long persistenceId;
	private Long persistenceVersion;
	private String username;
	private Long sesiones_pid;
	private Long responsabledisponible_pid;
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public Long getPersistenceVersion() {
		return persistenceVersion;
	}
	public void setPersistenceVersion(Long persistenceVersion) {
		this.persistenceVersion = persistenceVersion;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getSesiones_pid() {
		return sesiones_pid;
	}
	public void setSesiones_pid(Long sesiones_pid) {
		this.sesiones_pid = sesiones_pid;
	}
	public Long getResponsabledisponible_pid() {
		return responsabledisponible_pid;
	}
	public void setResponsabledisponible_pid(Long responsabledisponible_pid) {
		this.responsabledisponible_pid = responsabledisponible_pid;
	}
	
}
