package com.anahuac.rest.api.Entity.db

class Prueba {
    private Long persistenceId;
    private Long persistenceVersion;
    private String nombre;
    private String aplicacion;
    private String entrada;
    private String salida;
    private Integer registrados;
    private String ultimo_dia_inscripcion;
    private Integer cupo;
    private String lugar;
    private Long campus_pid;
    private String calle;
    private String numero_int;
    private String numero_ext;
    private String colonia;
    private String codigo_postal;
    private String municipio;
    private Long pais_pid;
    private Long estado_pid;
    private Boolean iseliminado;
    private Long sesion_pid;
	private Long cattipoprueba_pid;
	private String duracion;
	private String descripcion;
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDuracion() {
		return duracion;
	}
	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}
	public Long getCattipoprueba_pid() {
		return cattipoprueba_pid;
	}
	public void setCattipoprueba_pid(Long cattipoprueba_pid) {
		this.cattipoprueba_pid = cattipoprueba_pid;
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
	public String getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	public String getEntrada() {
		return entrada;
	}
	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}
	public String getSalida() {
		return salida;
	}
	public void setSalida(String salida) {
		this.salida = salida;
	}
	public Integer getRegistrados() {
		return registrados;
	}
	public void setRegistrados(Integer registrados) {
		this.registrados = registrados;
	}
	public String getUltimo_dia_inscripcion() {
		return ultimo_dia_inscripcion;
	}
	public void setUltimo_dia_inscripcion(String ultimo_dia_inscripcion) {
		this.ultimo_dia_inscripcion = ultimo_dia_inscripcion;
	}
	public Integer getCupo() {
		return cupo;
	}
	public void setCupo(Integer cupo) {
		this.cupo = cupo;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public Long getCampus_pid() {
		return campus_pid;
	}
	public void setCampus_pid(Long campus_pid) {
		this.campus_pid = campus_pid;
	}
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getNumero_int() {
		return numero_int;
	}
	public void setNumero_int(String numero_int) {
		this.numero_int = numero_int;
	}
	public String getNumero_ext() {
		return numero_ext;
	}
	public void setNumero_ext(String numero_ext) {
		this.numero_ext = numero_ext;
	}
	public String getColonia() {
		return colonia;
	}
	public void setColonia(String colonia) {
		this.colonia = colonia;
	}
	public String getCodigo_postal() {
		return codigo_postal;
	}
	public void setCodigo_postal(String codigo_postal) {
		this.codigo_postal = codigo_postal;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public Long getPais_pid() {
		return pais_pid;
	}
	public void setPais_pid(Long pais_pid) {
		this.pais_pid = pais_pid;
	}
	public Long getEstado_pid() {
		return estado_pid;
	}
	public void setEstado_pid(Long estado_pid) {
		this.estado_pid = estado_pid;
	}
	public Boolean getIseliminado() {
		return iseliminado;
	}
	public void setIseliminado(Boolean iseliminado) {
		this.iseliminado = iseliminado;
	}
	public Long getSesion_pid() {
		return sesion_pid;
	}
	public void setSesion_pid(Long sesion_pid) {
		this.sesion_pid = sesion_pid;
	}
	
}

