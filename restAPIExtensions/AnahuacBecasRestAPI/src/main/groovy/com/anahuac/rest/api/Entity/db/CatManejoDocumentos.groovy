package com.anahuac.rest.api.Entity.db

class CatManejoDocumentos {
	private Long persistenceId;
	private String persistenceId_string;
	private Long idCampus;
	private Long idTipoApoyo;
	private String isObligatorio;
	private String nombreDocumento;
	private String urlDocumentoAzure;
	private Boolean isEliminado;
	private String fechaCreacion;
	private String usuarioCreacion;
	private String descripcionDocumento;
	private Boolean isObligatorioDoc;
	private Boolean requiereEjemplo;
	private Boolean isAval;
	
	public String getDescripcionDocumento() {
		return descripcionDocumento;
	}
	public void setDescripcionDocumento(String descripcionDocumento) {
		this.descripcionDocumento = descripcionDocumento;
	}
	public String getPersistenceId_string() {
		return persistenceId_string;
	}
	public void setPersistenceId_string(String persistenceId_string) {
		this.persistenceId_string = persistenceId_string;
	}
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
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
	public String getNombreDocumento() {
		return nombreDocumento;
	}
	public void setNombreDocumento(String nombreDocumento) {
		this.nombreDocumento = nombreDocumento;
	}
	public String getUrlDocumentoAzure() {
		return urlDocumentoAzure;
	}
	public void setUrlDocumentoAzure(String urlDocumentoAzure) {
		this.urlDocumentoAzure = urlDocumentoAzure;
	}
	public Boolean getIsEliminado() {
		return isEliminado;
	}
	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public Boolean getIsObligatorioDoc() {
		return isObligatorioDoc;
	}
	public void setIsObligatorioDoc(Boolean isObligatorioDoc) {
		this.isObligatorioDoc = isObligatorioDoc;
	}
	public String getIsObligatorio() {
		return isObligatorio;
	}
	public void setIsObligatorio(String isObligatorio) {
		this.isObligatorio = isObligatorio;
	}
	public Boolean getRequiereEjemplo() {
		return requiereEjemplo;
	}
	public void setRequiereEjemplo(Boolean requiereEjemplo) {
		this.requiereEjemplo = requiereEjemplo;
	}
	public Boolean getIsAval() {
		return isAval;
	}
	public void setIsAval(Boolean isAval) {
		this.isAval = isAval;
	}
	
}
