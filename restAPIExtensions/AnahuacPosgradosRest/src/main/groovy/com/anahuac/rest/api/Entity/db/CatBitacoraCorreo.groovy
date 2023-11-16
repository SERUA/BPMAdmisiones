package com.anahuac.rest.api.Entity.db

class CatBitacoraCorreo {
	public static final String GET_CATBITACORACORREO="SELECT PSGRCatBitacoraCorreos.*, PSGRRegistro.caseid FROM PSGRCatBitacoraCorreos LEFT JOIN PSGRRegistro ON PSGRCatBitacoraCorreos.para = PSGRRegistro.correo_electronico [WHERE] [ORDERBY] [LIMITOFFSET]"
	private Long persistenceId;
	private Long persistenceVersion;
	private String codigo;
	private String de;
	private String estatus;
	private String fecha_creacion;
	private String mensaje;
	private String para;
	private String campus;
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public Long getPersistenceId() {
		return persistenceId;
	}
	public Long getPersistenceVersion() {
		return persistenceVersion;
	}
	public String getCodigo() {
		return codigo;
	}
	public String getDe() {
		return de;
	}
	public String getEstatus() {
		return estatus;
	}
	public String getFecha_creacion() {
		return fecha_creacion;
	}
	public String getMensaje() {
		return mensaje;
	}
	public String getPara() {
		return para;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public void setPersistenceVersion(Long persistenceVersion) {
		this.persistenceVersion = persistenceVersion;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setDe(String de) {
		this.de = de;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public void setFecha_creacion(String fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public void setPara(String para) {
		this.para = para;
	}
	
}
