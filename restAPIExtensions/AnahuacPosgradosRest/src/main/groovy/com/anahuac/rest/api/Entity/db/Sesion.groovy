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
    private Boolean borrador;
	private String tipo;
	private Long campus_pid;
	private Long ciudad_pid;
	private String ultimo_dia_inscripcion;
	private Boolean isEliminado;
	private Long periodo_pid;
	private String usuarios_lst_id;
	private String estado_preparatoria;
	
	
	public String getEstado_preparatoria() {
		return estado_preparatoria;
	}
	public void setEstado_preparatoria(String estado_preparatoria) {
		this.estado_preparatoria = estado_preparatoria;
	}
	public String getUsuarios_lst_id() {
		return usuarios_lst_id;
	}
	public void setUsuarios_lst_id(String usuarios_lst_id) {
		this.usuarios_lst_id = usuarios_lst_id;
	}
	public Long getPeriodo_pid() {
		return periodo_pid;
	}
	public void setPeriodo_pid(Long periodo_pid) {
		this.periodo_pid = periodo_pid;
	}
	public Boolean getIsEliminado() {
		return isEliminado;
	}
	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
	}
	public String getUltimo_dia_inscripcion() {
		return ultimo_dia_inscripcion;
	}
	public void setUltimo_dia_inscripcion(String ultimo_dia_inscripcion) {
		this.ultimo_dia_inscripcion = ultimo_dia_inscripcion;
	}
	public Long getCiudad_pid() {
		return ciudad_pid;
	}
	public void setCiudad_pid(Long ciudad_pid) {
		this.ciudad_pid = ciudad_pid;
	}
	public Long getCampus_pid() {
		return campus_pid;
	}
	public void setCampus_pid(Long campus_pid) {
		this.campus_pid = campus_pid;
	}
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
	public Boolean getBorrador() {
		return borrador;
	}
	public void setBorrador(Boolean borrador) {
		this.borrador = borrador;
	}

	
}
