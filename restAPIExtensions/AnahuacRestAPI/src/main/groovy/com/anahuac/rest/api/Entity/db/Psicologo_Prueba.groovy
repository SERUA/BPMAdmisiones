package com.anahuac.rest.api.Entity.db

class Psicologo_Prueba {
	private Long persistenceId;
	private Long persistenceversion;
	private Long psicologo_pid;
	private Long pruebas_pid;
	private Boolean iseliminado;
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public Long getPersistenceversion() {
		return persistenceversion;
	}
	public void setPersistenceversion(Long persistenceversion) {
		this.persistenceversion = persistenceversion;
	}
	public Long getPsicologo_pid() {
		return psicologo_pid;
	}
	public void setPsicologo_pid(Long psicologo_pid) {
		this.psicologo_pid = psicologo_pid;
	}
	public Long getPruebas_pid() {
		return pruebas_pid;
	}
	public void setPruebas_pid(Long pruebas_pid) {
		this.pruebas_pid = pruebas_pid;
	}
	public Boolean getIseliminado() {
		return iseliminado;
	}
	public void setIseliminado(Boolean iseliminado) {
		this.iseliminado = iseliminado;
	}

}
