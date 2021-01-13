package com.anahuac.rest.api.Entity

class OrdenBitacora {
	private String noTransaccion;
	private String usuarioAspirante;
	private String fechaMovimiento;
	private String monto;
	private String medioPago;
	private String estatus;
	private String observaciones;
	private Long caseId;
	private String campus;
	private String nombrePago;
	
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getNombrePago() {
		return nombrePago;
	}
	public void setNombrePago(String nombrePago) {
		this.nombrePago = nombrePago;
	}
	public Long getCaseId() {
		return caseId;
	}
	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}
	public String getNoTransaccion() {
		return noTransaccion;
	}
	public void setNoTransaccion(String noTransaccion) {
		this.noTransaccion = noTransaccion;
	}
	public String getUsuarioAspirante() {
		return usuarioAspirante;
	}
	public void setUsuarioAspirante(String usuarioAspirante) {
		this.usuarioAspirante = usuarioAspirante;
	}
	public String getFechaMovimiento() {
		return fechaMovimiento;
	}
	public void setFechaMovimiento(String fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getMedioPago() {
		return medioPago;
	}
	public void setMedioPago(String medioPago) {
		this.medioPago = medioPago;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
}
