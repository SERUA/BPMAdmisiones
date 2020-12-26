package com.anahuac.rest.api.Entity.Custom

import com.anahuac.catalogos.CatBachilleratos
import com.anahuac.catalogos.CatCampus
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class CatCiudadCustonFiltro {
	@XmlElement
	private Long persistenceId;
	@XmlElement
	private Long persistenceVersion;
	@XmlElement
	private String clave;
	@XmlElement
	private Long orden;
	@XmlElement
	private String pais;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String fechaCreacion;
	@XmlElement
	private String usuarioCreacion;
	@XmlElement
	private Boolean isEliminado;
	@XmlElement
	private String caseId;
	@XmlElement
	private String estado;
	@XmlElement
	private String region;
	@XmlElement
	private String isEstado;
	@XmlElement
	private CatCampus campus;
	//private CatCampus = catCampus;
	
	public String getFechaImportacion() {
		return fechaImportacion;
	}
	public void setFechaImportacion(String fechaImportacion) {
		this.fechaImportacion = fechaImportacion;
	}
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
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	public Boolean getIsEliminado() {
		return isEliminado;
	}
	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
	}
	public Long getOrden() {
		return orden;
	}
	public void setOrden(Long orden) {
		this.orden = orden;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getIsEstado() {
		return isEstado;
	}
	public void setIsEstado(String isEstado) {
		this.isEstado = isEstado;
	}
	
	public CatCampus getCatCampus() {
		return catCampus;
	}
	public void setCatCampus(CatCampus catCampus) {
		this.catCampus = catCampus;
	}
	@Override
	public String toString() {
		return "CatLicenciaturaCustom [persistenceId=" + persistenceId + ", persistenceVersion=" + persistenceVersion
				+ ", clave=" + clave + ", descripcion=" + descripcion + ", fechaCreacion=" + fechaCreacion
				+ ", usuarioCreacion=" + usuarioCreacion + ", isEliminado=" + isEliminado + " , orden="
				+ orden + " , pais=" + pais + " , caseId=" + caseId + " , region=" + region + " , estado="
				+ estado + " , isEstado=" + isEstado + " , catCampus=" + catCampus + "]";
	}
	
}
