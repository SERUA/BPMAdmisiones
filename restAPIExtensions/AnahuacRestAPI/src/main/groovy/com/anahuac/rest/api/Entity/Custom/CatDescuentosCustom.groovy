package com.anahuac.rest.api.Entity.Custom

import java.time.LocalDateTime

import com.anahuac.catalogos.CatBachilleratos

class CatDescuentosCustom {
	private Long persistenceId;
	private Long persistenceVersion;
	private CatBachilleratos catBachilleratos;
	private String ciudad;
	private String campana;
	private String documento;
	private String convenioDescuento;
	private String inicioVigencia;
	private String finVigencia;
	private String tipo;
	private Integer descuento;
	private String usuarioCreacion;
	private Boolean isEliminado;
	private String campus;
	private String bachillerato;
	
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
	public CatBachilleratos getCatBachilleratos() {
		return catBachilleratos;
	}
	public void setCatBachilleratos(CatBachilleratos catBachilleratos) {
		this.catBachilleratos = catBachilleratos;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getCampana() {
		return campana;
	}
	public void setCampana(String campana) {
		this.campana = campana;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getConvenioDescuento() {
		return convenioDescuento;
	}
	public void setConvenioDescuento(String convenioDescuento) {
		this.convenioDescuento = convenioDescuento;
	}
	
	
	
	public String getInicioVigencia() {
		return inicioVigencia;
	}
	public void setInicioVigencia(String inicioVigencia) {
		this.inicioVigencia = inicioVigencia;
	}
	public String getFinVigencia() {
		return finVigencia;
	}
	public void setFinVigencia(String finVigencia) {
		this.finVigencia = finVigencia;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Integer getDescuento() {
		return descuento;
	}
	public void setDescuento(Integer descuento) {
		this.descuento = descuento;
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
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getBachillerato() {
		return bachillerato;
	}
	public void setBachillerato(String bachillerato) {
		this.bachillerato = bachillerato;
	}
	
	
	
}
