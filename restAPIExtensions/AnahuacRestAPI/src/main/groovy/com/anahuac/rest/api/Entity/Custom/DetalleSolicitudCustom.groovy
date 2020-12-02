package com.anahuac.rest.api.Entity.Custom

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class DetalleSolicitudCustom {
	@XmlElement
	private Long persistenceId;
	@XmlElement
	private Long persistenceVersion;
	@XmlElement
	private String idBanner;
	@XmlElement
	private Boolean isCurpValidado;
	@XmlElement
	private Boolean promedioCoincide;
	@XmlElement
	private Boolean revisado;
	@XmlElement
	private String tipoAlumno;
	@XmlElement
	private Float descuento;
	@XmlElement
	private String observacionesDescuento;
	@XmlElement
	private String observacionesCambio;
	@XmlElement
	private String observacionesRechazo;
	@XmlElement
	private String observacionesListaRoja;
	@XmlElement
	private String ordenPago;
	@XmlElement
	private String caseId;
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
	public String getIdBanner() {
		return idBanner;
	}
	public void setIdBanner(String idBanner) {
		this.idBanner = idBanner;
	}
	public Boolean getIsCurpValidado() {
		return isCurpValidado;
	}
	public void setIsCurpValidado(Boolean isCurpValidado) {
		this.isCurpValidado = isCurpValidado;
	}
	public Boolean getPromedioCoincide() {
		return promedioCoincide;
	}
	public void setPromedioCoincide(Boolean promedioCoincide) {
		this.promedioCoincide = promedioCoincide;
	}
	public Boolean getRevisado() {
		return revisado;
	}
	public void setRevisado(Boolean revisado) {
		this.revisado = revisado;
	}
	public String getTipoAlumno() {
		return tipoAlumno;
	}
	public void setTipoAlumno(String tipoAlumno) {
		this.tipoAlumno = tipoAlumno;
	}
	public Float getDescuento() {
		return descuento;
	}
	public void setDescuento(Float descuento) {
		this.descuento = descuento;
	}
	public String getObservacionesDescuento() {
		return observacionesDescuento;
	}
	public void setObservacionesDescuento(String observacionesDescuento) {
		this.observacionesDescuento = observacionesDescuento;
	}
	public String getObservacionesCambio() {
		return observacionesCambio;
	}
	public void setObservacionesCambio(String observacionesCambio) {
		this.observacionesCambio = observacionesCambio;
	}
	public String getObservacionesRechazo() {
		return observacionesRechazo;
	}
	public void setObservacionesRechazo(String observacionesRechazo) {
		this.observacionesRechazo = observacionesRechazo;
	}
	public String getObservacionesListaRoja() {
		return observacionesListaRoja;
	}
	public void setObservacionesListaRoja(String observacionesListaRoja) {
		this.observacionesListaRoja = observacionesListaRoja;
	}
	public String getOrdenPago() {
		return ordenPago;
	}
	public void setOrdenPago(String ordenPago) {
		this.ordenPago = ordenPago;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	@Override
	public String toString() {
		return "DetalleSolicitud [persistenceId=" + persistenceId + ", persistenceVersion=" + persistenceVersion
				+ ", idBanner=" + idBanner + ", isCurpValidado=" + isCurpValidado + ", promedioCoincide="
				+ promedioCoincide + ", revisado=" + revisado + ", tipoAlumno=" + tipoAlumno + ", descuento="
				+ descuento + ", observacionesDescuento=" + observacionesDescuento + ", observacionesCambio="
				+ observacionesCambio + ", observacionesRechazo=" + observacionesRechazo + ", observacionesListaRoja="
				+ observacionesListaRoja + ", ordenPago=" + ordenPago + ", caseId=" + caseId + "]";
	}
	
	
}
