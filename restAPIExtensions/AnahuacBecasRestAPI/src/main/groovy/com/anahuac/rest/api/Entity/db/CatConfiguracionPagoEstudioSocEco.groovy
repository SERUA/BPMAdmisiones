package com.anahuac.rest.api.Entity.db

class CatConfiguracionPagoEstudioSocEco {
	private Long persistenceId;
	private Long idCampus
	private String clave;
	private String descripcion;
	private String instruccionesPago;
	private Double monto;
	private String fechaCreacion;
	private Boolean deshabilitarPagoEstudioSocioEconomico;
	
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
	public String getInstruccionesPago() {
		return instruccionesPago;
	}
	public void setInstruccionesPago(String instruccionesPago) {
		this.instruccionesPago = instruccionesPago;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Boolean getDeshabilitarPagoEstudioSocioEconomico() {
		return deshabilitarPagoEstudioSocioEconomico;
	}
	public void setDeshabilitarPagoEstudioSocioEconomico(Boolean deshabilitarPagoEstudioSocioEconomico) {
		this.deshabilitarPagoEstudioSocioEconomico = deshabilitarPagoEstudioSocioEconomico;
	}
}
