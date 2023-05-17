package com.anahuac.rest.api.Entity.db

class CatImagenesSocioEconomico {
	private Long persistenceId;
	private String descripcion;
	private String urlAzure;
	private Boolean isEliminado;
	private String usuarioCreacion;
	private Long idCampus;
	private Long idTipoApoyo;
	
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getUrlAzure() {
		return urlAzure;
	}
	public void setUrlAzure(String urlAzure) {
		this.urlAzure = urlAzure;
	}
	public Boolean getIsEliminado() {
		return isEliminado;
	}
	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
	}
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public Long getIdCampus() {
		return idCampus;
	}
	public void setIdCampus(Long idCampus) {
		this.idCampus = idCampus;
	}
	public Long getIdTipoApoyo() {
		return idTipoApoyo;
	}
	public void setIdTipoApoyo(Long idTipoApoyo) {
		this.idTipoApoyo = idTipoApoyo;
	}
	
}

