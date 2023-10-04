package com.anahuac.rest.api.Entity.custom

class CatGestionEscolar {
	private Long persistenceId;
	private Long persistenceVersion;
	private String nombre;
	private String descripcion;
	private String enlace;
	private Boolean propedeutico;
	private Boolean programa_parcial;
	private Boolean matematicas;
	private String inscripcion_enero;
	private String inscripcion_agosto;
	private Boolean is_eliminado;
	private String usuario_creacion;
	private Date fecha_creacion;
	private String campus;
	private String case_id;
	private String Tipo_licenciatura;
	private String clave;
	private String tipo_centro_estudio;
	private String inscripcion_mayo;
	private String inscripcion_septiembre;
	private String url_img_licenciatura;
	private Boolean is_medicina;
	private String idioma;
	private Long campus_referencia_pid;
	
	
	
	
	public Long getCampusReferenciaPid() {
		return campus_referencia_pid;
	}
	public void setCampusReferenciaPid(Long campus_referencia_pid) {
		this.campus_referencia_pid = campus_referencia_pid;
	}
	
	public String getIdioma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	
	public Boolean getIsMedicina() {
		return is_medicina;
	}
	public void setIsMedicina(Boolean is_medicina) {
		this.is_medicina = is_medicina;
	}
	public String getUrlImgLicenciatura() {
		return url_img_licenciatura;
	}
	public void setUrlImgLicenciatura(String url_img_licenciatura) {
		this.url_img_licenciatura = url_img_licenciatura;
	}
	public String getTipoCentroEstudio() {
		return tipo_centro_estudio;
	}
	public void setTipoCentroEstudio(String tipo_centro_estudio) {
		this.tipo_centro_estudio = tipo_centro_estudio;
	}
	public String getInscripcionMayo() {
		return inscripcion_mayo;
	}
	public void setInscripcionMayo(String inscripcion_mayo) {
		this.inscripcion_mayo = inscripcion_mayo;
	}
	public String getInscripcionSeptiembre() {
		return inscripcion_septiembre;
	}
	public void setInscripcionSeptiembre(String inscripcion_septiembre) {
		this.inscripcion_septiembre = inscripcion_septiembre;
	}
	
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getTipoLicenciatura() {
		return Tipo_licenciatura;
	}
	public void setTipoLicenciatura(String tipo_licenciatura) {
		Tipo_licenciatura = tipo_licenciatura;
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
		return programa_parcial;
	}
	public void setProgramaparcial(Boolean programa_parcial) {
		this.programa_parcial = programa_parcial;
	}
	public Boolean getMatematicas() {
		return matematicas;
	}
	public void setMatematicas(Boolean matematicas) {
		this.matematicas = matematicas;
	}
	public String getInscripcionenero() {
		return inscripcion_enero;
	}
	public void setInscripcionenero(String inscripcion_enero) {
		this.inscripcion_enero = inscripcion_enero;
	}
	public String getInscripcionagosto() {
		return inscripcion_agosto;
	}
	public void setInscripcionagosto(String inscripcion_agosto) {
		this.inscripcion_agosto = inscripcion_agosto;
	}
	public Boolean getIsEliminado() {
		return is_eliminado;
	}
	public void setIsEliminado(Boolean is_eliminado) {
		this.is_eliminado = is_eliminado;
	}
	public String getUsuarioCreacion() {
		return usuario_creacion;
	}
	public void setUsuarioCreacion(String usuario_creacion) {
		this.usuario_creacion = usuario_creacion;
	}
	public Date getFechaCreacion() {
		return fecha_creacion;
	}
	public void setFechaCreacion(Date fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getCaseId() {
		return case_id;
	}
	public void setCaseId(String case_id) {
		this.case_id = case_id;
	}
}
