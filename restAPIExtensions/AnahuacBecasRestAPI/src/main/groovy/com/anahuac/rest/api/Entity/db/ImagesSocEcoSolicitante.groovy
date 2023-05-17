package com.anahuac.rest.api.Entity.db

class ImagesSocEcoSolicitante {
	private Long persistenceId;
	private CatImagenesSocioEconomico imagenSocioEconomico;
	private String urlImagen;
	private Long caseId;
	private Long imagenSocioEconomico_id;
	
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public CatImagenesSocioEconomico getImagenSocioEconomico() {
		return imagenSocioEconomico;
	}
	public void setImagenSocioEconomico(CatImagenesSocioEconomico imagenSocioEconomico) {
		this.imagenSocioEconomico = imagenSocioEconomico;
	}
	public String getUrlImagen() {
		return urlImagen;
	}
	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}
	public Long getCaseId() {
		return caseId;
	}
	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}
	public Long getImagenSocioEconomico_id() {
		return imagenSocioEconomico_id;
	}
	public void setImagenSocioEconomico_id(Long imagenSocioEconomico_id) {
		this.imagenSocioEconomico_id = imagenSocioEconomico_id;
	}
}
