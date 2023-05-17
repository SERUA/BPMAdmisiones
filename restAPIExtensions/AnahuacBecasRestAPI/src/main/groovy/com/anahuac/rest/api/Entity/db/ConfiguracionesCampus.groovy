package com.anahuac.rest.api.Entity.db

class ConfiguracionesCampus {
	Long persistenceId;
	String urlReglamento;
	Double porcentajeInteresFinanciamiento;
	Boolean tieneFinanciamiento;
	Boolean interesInscripcion;
	Boolean interesColegiatura;
	Boolean descuentoProntoPago;
	Long idCampus;
	Double promedioMinimo;
	String urlDocumentoCondicionesFinanciamiento;
	
	public String getUrlDocumentoCondicionesFinanciamiento() {
		return urlDocumentoCondicionesFinanciamiento;
	}
	public void setUrlDocumentoCondicionesFinanciamiento(String urlDocumentoCondicionesFinanciamiento) {
		this.urlDocumentoCondicionesFinanciamiento = urlDocumentoCondicionesFinanciamiento;
	}
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public String getUrlReglamento() {
		return urlReglamento;
	}
	public void setUrlReglamento(String urlReglamento) {
		this.urlReglamento = urlReglamento;
	}
	public Double getPorcentajeInteresFinanciamiento() {
		return porcentajeInteresFinanciamiento;
	}
	public void setPorcentajeInteresFinanciamiento(Double porcentajeInteresFinanciamiento) {
		this.porcentajeInteresFinanciamiento = porcentajeInteresFinanciamiento;
	}
	public Boolean getTieneFinanciamiento() {
		return tieneFinanciamiento;
	}
	public void setTieneFinanciamiento(Boolean tieneFinanciamiento) {
		this.tieneFinanciamiento = tieneFinanciamiento;
	}
	public Boolean getInteresInscripcion() {
		return interesInscripcion;
	}
	public void setInteresInscripcion(Boolean interesInscripcion) {
		this.interesInscripcion = interesInscripcion;
	}
	public Boolean getInteresColegiatura() {
		return interesColegiatura;
	}
	public void setInteresColegiatura(Boolean interesColegiatura) {
		this.interesColegiatura = interesColegiatura;
	}
	public Boolean getDescuentoProntoPago() {
		return descuentoProntoPago;
	}
	public void setDescuentoProntoPago(Boolean descuentoProntoPago) {
		this.descuentoProntoPago = descuentoProntoPago;
	}
	public Long getIdCampus() {
		return idCampus;
	}
	public void setIdCampus(Long idCampus) {
		this.idCampus = idCampus;
	}
	public Double getPromedioMinimo() {
		return promedioMinimo;
	}
	public void setPromedioMinimo(Double promedioMinimo) {
		this.promedioMinimo = promedioMinimo;
	}
}
