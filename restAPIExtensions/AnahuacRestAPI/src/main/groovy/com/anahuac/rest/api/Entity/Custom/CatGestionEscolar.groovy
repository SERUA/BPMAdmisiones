package com.anahuac.rest.api.Entity.Custom

class CatGestionEscolar {
	
	private Long persistenceId;
	private Long persistenceVersion;
	private String nombre;
	private String descripcion;
	private String enlace;
	private Boolean propedeutico;
	private Boolean programaparcial;
	private Boolean matematicas;
	private String inscripcionenero;
	private String inscripcionagosto;
	private Boolean isEliminado;
	private String usuarioCreacion;
	private Date fechaCreacion;
	private String campus;
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
	public String getEnlace() {
		return enlace;
	}
	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}
	public Boolean getPropedeutico() {
		return propedeutico;
	}
	public void setPropedeutico(Boolean propedeutico) {
		this.propedeutico = propedeutico;
	}
	public Boolean getProgramaparcial() {
		return programaparcial;
	}
	public void setProgramaparcial(Boolean programaparcial) {
		this.programaparcial = programaparcial;
	}
	public Boolean getMatematicas() {
		return matematicas;
	}
	public void setMatematicas(Boolean matematicas) {
		this.matematicas = matematicas;
	}
	public String getInscripcionenero() {
		return inscripcionenero;
	}
	public void setInscripcionenero(String inscripcionenero) {
		this.inscripcionenero = inscripcionenero;
	}
	public String getInscripcionagosto() {
		return inscripcionagosto;
	}
	public void setInscripcionagosto(String inscripcionagosto) {
		this.inscripcionagosto = inscripcionagosto;
	}
	public Boolean getIsEliminado() {
		return isEliminado;
	}
	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
	}
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	
	@Override
	public String toString() {
		return "CatGestionEscolar [persistenceId=" + persistenceId + ", persistenceVersion=" + persistenceVersion
				+ ", nombre=" + nombre + ", descripcion=" + descripcion + ", enlace=" + enlace + ", propedeutico="
				+ propedeutico + ", programaparcial=" + programaparcial + ", matematicas=" + matematicas
				+ ", inscripcionenero=" + inscripcionenero + ", inscripcionagosto=" + inscripcionagosto
				+ ", isEliminado=" + isEliminado + ", usuarioCreacion=" + usuarioCreacion + ", fechaCreacion="
				+ fechaCreacion + ", campus=" + campus + ", caseId=" + caseId + "]";
	}
	
	
	
}
