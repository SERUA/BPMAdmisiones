package com.anahuac.rest.api.Entity

class IdiomaExamen {
	private Long persistenceId;
	private String persistenceVersion;
	private String idioma;
	private String usuario;
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public String getPersistenceVersion() {
		return persistenceVersion;
	}
	public void setPersistenceVersion(String persistenceVersion) {
		this.persistenceVersion = persistenceVersion;
	}
	public String getIdioma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}
