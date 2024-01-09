package com.anahuac.rest.api.Entity

class LogsTransferencias {
	private Long persistenceid;
	private String campus_origen;
	private String campus_destino;
	private String periodo_origen;
	private String periodo_destino;
	private String carrera_origen;
	private String carrera_destino;
	private String posgrado_origen;
	private String posgrado_destino;
	private String usuario;
	private String fecha_transferencia;
	
	public Long getPersistenceid() {
		return persistenceid;
	}
	public void setPersistenceid(Long persistenceid) {
		this.persistenceid = persistenceid;
	}
	public String getCampus_origen() {
		return campus_origen;
	}
	public void setCampus_origen(String campus_origen) {
		this.campus_origen = campus_origen;
	}
	public String getCampus_destino() {
		return campus_destino;
	}
	public void setCampus_destino(String campus_destino) {
		this.campus_destino = campus_destino;
	}
	public String getPeriodo_origen() {
		return periodo_origen;
	}
	public void setPeriodo_origen(String periodo_origen) {
		this.periodo_origen = periodo_origen;
	}
	public String getPeriodo_destino() {
		return periodo_destino;
	}
	public void setPeriodo_destino(String periodo_destino) {
		this.periodo_destino = periodo_destino;
	}
	public String getCarrera_origen() {
		return carrera_origen;
	}
	public void setCarrera_origen(String carrera_origen) {
		this.carrera_origen = carrera_origen;
	}
	public String getCarrera_destino() {
		return carrera_destino;
	}
	public void setCarrera_destino(String carrera_destino) {
		this.carrera_destino = carrera_destino;
	}
	public String getPosgrado_origen() {
		return posgrado_origen;
	}
	public void setPosgrado_origen(String posgrado_origen) {
		this.posgrado_origen = posgrado_origen;
	}
	public String getPosgrado_destino() {
		return posgrado_destino;
	}
	public void setPosgrado_destino(String posgrado_destino) {
		this.posgrado_destino = posgrado_destino;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getFecha_transferencia() {
		return fecha_transferencia;
	}
	public void setFecha_transferencia(String fecha_transferencia) {
		this.fecha_transferencia = fecha_transferencia;
	}
}
