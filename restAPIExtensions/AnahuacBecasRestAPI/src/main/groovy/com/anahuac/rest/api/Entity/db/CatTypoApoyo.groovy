package com.anahuac.rest.api.Entity.db

class CatTypoApoyo {
	private Long persistenceId; 
	private String persistenceId_string;
	private Long persistenceVersion; 
	private String clave; 
	private String descripcion; 
	private String fechaCreacion; 
	private String usuarioCreacion; 
	private Boolean isEliminado;
	
	//Campos de  la tabla RelacionCampusTipoApoyo
	private Boolean requiereVideo;
	private String condicionesVideo;
	private Boolean esSocioEconomico;
	private Boolean isArtistica;
	private Boolean isDeportiva;
	private Boolean isFinanciamiento;
	private Boolean isAcademica;
	private Boolean isObligatorioDoc;
	private Boolean requiereCurriculum;
	private String urlEjemploCurriculum;
	private Boolean requierePromedioMinimo;
	private Double promedioMinimo;
	
	public Boolean getIsObligatorioDoc() {
		return isObligatorioDoc;
	}
	public void setIsObligatorioDoc(Boolean isObligatorioDoc) {
		this.isObligatorioDoc = isObligatorioDoc;
	}
	public Boolean getEsSocioEconomico() {
		return esSocioEconomico;
	}
	public void setEsSocioEconomico(Boolean esSocioEconomico) {
		this.esSocioEconomico = esSocioEconomico;
	}
	public Boolean getIsArtistica() {
		return isArtistica;
	}
	public void setIsArtistica(Boolean isArtistica) {
		this.isArtistica = isArtistica;
	}
	public Boolean getIsDeportiva() {
		return isDeportiva;
	}
	public void setIsDeportiva(Boolean isDeportiva) {
		this.isDeportiva = isDeportiva;
	}
	public Boolean getIsFinanciamiento() {
		return isFinanciamiento;
	}
	public void setIsFinanciamiento(Boolean isFinanciamiento) {
		this.isFinanciamiento = isFinanciamiento;
	}
	public Boolean getIsAcademica() {
		return isAcademica;
	}
	public void setIsAcademica(Boolean isAcademica) {
		this.isAcademica = isAcademica;
	}
	public Boolean getRequiereVideo() {
		return requiereVideo;
	}
	public void setRequiereVideo(Boolean requiereVideo) {
		this.requiereVideo = requiereVideo;
	}
	public String getCondicionesVideo() {
		return condicionesVideo;
	}
	public void setCondicionesVideo(String condicionesVideo) {
		this.condicionesVideo = condicionesVideo;
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
	public String getPersistenceId_string() {
		return persistenceId_string;
	}
	public void setPersistenceId_string(String persistenceId_string) {
		this.persistenceId_string = persistenceId_string;
	}
	public Boolean getRequiereCurriculum() {
		return requiereCurriculum;
	}
	public void setRequiereCurriculum(Boolean requiereCurriculum) {
		this.requiereCurriculum = requiereCurriculum;
	}
	public String getUrlEjemploCurriculum() {
		return urlEjemploCurriculum;
	}
	public void setUrlEjemploCurriculum(String urlEjemploCurriculum) {
		this.urlEjemploCurriculum = urlEjemploCurriculum;
	}
	public Boolean getRequierePromedioMinimo() {
		return requierePromedioMinimo;
	}
	public void setRequierePromedioMinimo(Boolean requierePromedioMinimo) {
		this.requierePromedioMinimo = requierePromedioMinimo;
	}
	public Double getPromedioMinimo() {
		return promedioMinimo;
	}
	public void setPromedioMinimo(Double promedioMinimo) {
		this.promedioMinimo = promedioMinimo;
	}
}
