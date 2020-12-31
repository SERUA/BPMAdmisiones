package com.anahuac.rest.api.Entity.Custom

import com.anahuac.catalogos.CatEstados
import com.anahuac.catalogos.CatPais
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class CatCampusCustomFiltro {
	@XmlElement
	private Long persistenceId;
	@XmlElement
	private Long persistenceVersion;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String fechaCreacion;
	@XmlElement
	private Boolean isEliminado;
	@XmlElement
	private String usuarioBanner;
	@XmlElement
	private String fechaImplementacion;
	@XmlElement
	private String clave;
	@XmlElement
	private Boolean isEnabled;
	@XmlElement
	private long orden;
	@XmlElement
	private String grupoBonita;
	@XmlElement
	private String id;
	@XmlElement
	private String urlDatosVeridicos;
	@XmlElement
	private String urlAutorDatos;
	@XmlElement
	private String urlAvisoPrivacidad;
	@XmlElement
	private String calle;
	@XmlElement
	private String colonia;
	@XmlElement
	private String numeroExterior;
	@XmlElement
	private String numeroInterior;
	@XmlElement
	private String codigoPostal;
	@XmlElement
	private String municipio;
	@XmlElement
	private CatPais pais;
	@XmlElement
	private CatEstados estado;
	private String pais_pid;
	@XmlElement
	private String estado_pid;

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



	public Boolean getIsEliminado() {
		return isEliminado;
	}



	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
	}



	public String getUsuarioBanner() {
		return usuarioBanner;
	}



	public void setUsuarioBanner(String usuarioBanner) {
		this.usuarioBanner = usuarioBanner;
	}



	public String getFechaImplementacion() {
		return fechaImplementacion;
	}



	public void setFechaImplementacion(String fechaImplementacion) {
		this.fechaImplementacion = fechaImplementacion;
	}



	public String getClave() {
		return clave;
	}



	public void setClave(String clave) {
		this.clave = clave;
	}



	public Boolean getIsEnabled() {
		return isEnabled;
	}



	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}



	public long getOrden() {
		return orden;
	}



	public void setOrden(long orden) {
		this.orden = orden;
	}



	public String getGrupoBonita() {
		return grupoBonita;
	}



	public void setGrupoBonita(String grupoBonita) {
		this.grupoBonita = grupoBonita;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getUrlDatosVeridicos() {
		return urlDatosVeridicos;
	}



	public void setUrlDatosVeridicos(String urlDatosVeridicos) {
		this.urlDatosVeridicos = urlDatosVeridicos;
	}



	public String getUrlAutorDatos() {
		return urlAutorDatos;
	}



	public void setUrlAutorDatos(String urlAutorDatos) {
		this.urlAutorDatos = urlAutorDatos;
	}



	public String getUrlAvisoPrivacidad() {
		return urlAvisoPrivacidad;
	}



	public void setUrlAvisoPrivacidad(String urlAvisoPrivacidad) {
		this.urlAvisoPrivacidad = urlAvisoPrivacidad;
	}



	public String getCalle() {
		return calle;
	}



	public void setCalle(String calle) {
		this.calle = calle;
	}



	public String getColonia() {
		return colonia;
	}



	public void setColonia(String colonia) {
		this.colonia = colonia;
	}



	public String getNumeroExterior() {
		return numeroExterior;
	}



	public void setNumeroExterior(String numeroExterior) {
		this.numeroExterior = numeroExterior;
	}



	public String getNumeroInterior() {
		return numeroInterior;
	}



	public void setNumeroInterior(String numeroInterior) {
		this.numeroInterior = numeroInterior;
	}



	public String getCodigoPostal() {
		return codigoPostal;
	}



	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}



	public String getMunicipio() {
		return municipio;
	}



	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}



	public CatPais getPais() {
		return pais;
	}



	public void setPais(CatPais pais) {
		this.pais = pais;
	}



	public CatEstados getEstado() {
		return estado;
	}



	public void setEstado(CatEstados estado) {
		this.estado = estado;
	}



	public String getPais_pid() {
		return pais_pid;
	}



	public void setPais_pid(String pais_pid) {
		this.pais_pid = pais_pid;
	}



	public String getEstado_pid() {
		return estado_pid;
	}



	public void setEstado_pid(String estado_pid) {
		this.estado_pid = estado_pid;
	}



	@Override
	public String toString() {
		return "CatCampusCustom [persistenceId=" + persistenceId + ", persistenceVersion=" + persistenceVersion
				+ ", descripcion=" + descripcion + ", fechaCreacion=" + fechaCreacion + ", isEliminado=" + isEliminado
				+ ", usuarioBanner=" + usuarioBanner + ", fechaImplementacion=" + fechaImplementacion + ", clave="
				+ clave + ", isEnabled=" + isEnabled + ", orden =" + orden + " , grupoBonita =" + grupoBonita + ", id ="
				 + id + " , urlDatosVeridicos =" + urlDatosVeridicos + " , urlAutorDatos =" + urlAutorDatos + " , urlAvisoPrivacidad =" + urlAvisoPrivacidad + "]";
	}
	
	
}
