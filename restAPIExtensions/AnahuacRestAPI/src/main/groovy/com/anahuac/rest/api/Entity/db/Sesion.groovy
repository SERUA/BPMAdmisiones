package com.anahuac.rest.api.Entity.db

class Sesion {
	private Long persistenceId;
    private Long persistenceVersion;
    private String nombre;
    private String descripcion;
    private String fecha_inicio;
    private Boolean ismedicina;
    private Long estado_pid;
    private Long pais_pid;
	private Long bachillerato_pid;
    private String borrador;
	private String tipo;
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Long getBachillerato_pid() {
		return bachillerato_pid;
	}
	public void setBachillerato_pid(Long bachillerato_pid) {
		this.bachillerato_pid = bachillerato_pid;
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
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getFecha_inicio() {
		return fecha_inicio;
	}
	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}
	public Boolean getIsmedicina() {
		return ismedicina;
	}
	public void setIsmedicina(Boolean ismedicina) {
		this.ismedicina = ismedicina;
	}
	public Long getEstado_pid() {
		return estado_pid;
	}
	public void setEstado_pid(Long estado_pid) {
		this.estado_pid = estado_pid;
	}
	public Long getPais_pid() {
		return pais_pid;
	}
	public void setPais_pid(Long pais_pid) {
		this.pais_pid = pais_pid;
	}
	public String getBorrador() {
		return borrador;
	}
	public void setBorrador(String borrador) {
		this.borrador = borrador;
	}
	
}
