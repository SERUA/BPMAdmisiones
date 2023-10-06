package com.anahuac.rest.api.Entity.db

import com.anahuac.rest.api.Entity.db.PSGRCatEstado
import com.anahuac.rest.api.Entity.db.CatPaisCustomFiltro
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class CatCampusCustomFiltro {
	@XmlElement
	private Long persistenceId;
	@XmlElement
	private Long persistenceVersion;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String fecha_creacion;
	@XmlElement
	private Boolean eliminado;
	@XmlElement
	private String usuario_banner;
	@XmlElement
	private String fecha_implementacion;
	@XmlElement
	private String clave;
	@XmlElement
	private Boolean activado;
	@XmlElement
	private long orden;
	@XmlElement
	private String grupo_bonita;
	@XmlElement
	private String id;
	@XmlElement
	private String url_datos_veridicos;
	@XmlElement
	private String url_autor_datos;
	@XmlElement
	private String url_aviso_privacidad;
	@XmlElement
	private String calle;
	@XmlElement
	private String colonia;
	@XmlElement
	private String numero_ext;
	@XmlElement
	private String numero_int;
	@XmlElement
	private String codigo_postal;
	@XmlElement
	private String municipio;
	@XmlElement
	private String url_imagen;
	@XmlElement
	private String email;
	@XmlElement
	private CatPaisCustomFiltro pais;
	@XmlElement
	private PSGRCatEstado estado;
	@XmlElement
	private String pais_pid;
	@XmlElement
	private String estado_pid;
	
	@XmlElement
	private String paises_relacion_pid;
	@XmlElement
	private String estados_relacion_pid;
	
	private PSGRCatEstado estados;
	
	
	
	
	public PSGRCatEstado getEstados() {
		return estados;
	}

	public void setEstados(PSGRCatEstado estados) {
		this.estados = estados;
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



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public String getFechaCreacion() {
		return fecha_creacion;
	}



	public void setFechaCreacion(String fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}



	public Boolean getIsEliminado() {
		return eliminado;
	}



	public void setIsEliminado(Boolean eliminado) {
		this.eliminado = eliminado;
	}



	public String getUsuarioBanner() {
		return usuario_banner;
	}



	public void setUsuarioBanner(String usuario_banner) {
		this.usuario_banner = usuario_banner;
	}



	public String getFechaImplementacion() {
		return fecha_implementacion;
	}



	public void setFechaImplementacion(String fecha_implementacion) {
		this.fecha_implementacion = fecha_implementacion;
	}



	public String getClave() {
		return clave;
	}



	public void setClave(String clave) {
		this.clave = clave;
	}



	public Boolean getIsEnabled() {
		return activado;
	}



	public void setIsEnabled(Boolean activado) {
		this.activado = activado;
	}



	public long getOrden() {
		return orden;
	}



	public void setOrden(long orden) {
		this.orden = orden;
	}



	public String getGrupoBonita() {
		return grupo_bonita;
	}



	public void setGrupoBonita(String grupo_bonita) {
		this.grupo_bonita = grupo_bonita;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getUrlDatosVeridicos() {
		return url_datos_veridicos;
	}



	public void setUrlDatosVeridicos(String url_datos_veridicos) {
		this.url_datos_veridicos = url_datos_veridicos;
	}



	public String getUrlAutorDatos() {
		return url_autor_datos;
	}



	public void setUrlAutorDatos(String url_autor_datos) {
		this.url_autor_datos = url_autor_datos;
	}



	public String getUrlAvisoPrivacidad() {
		return url_aviso_privacidad;
	}



	public void setUrlAvisoPrivacidad(String url_aviso_privacidad) {
		this.url_aviso_privacidad = url_aviso_privacidad;
	}



	public String getCalle() {
		return calle;
	}



	public void setCalle(String calle) {
		this.calle = calle;
	}



	public String getColonia() {
		return colonia;
	}



	public void setColonia(String colonia) {
		this.colonia = colonia;
	}



	public String getNumeroExterior() {
		return numero_ext;
	}



	public void setNumeroExterior(String numero_ext) {
		this.numero_ext = numero_ext;
	}



	public String getNumeroInterior() {
		return numero_int;
	}



	public void setNumeroInterior(String numero_int) {
		this.numero_int = numero_int;
	}



	public String getCodigoPostal() {
		return codigo_postal;
	}



	public void setCodigoPostal(String codigo_postal) {
		this.codigo_postal = codigo_postal;
	}



	public String getMunicipio() {
		return municipio;
	}



	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}



	public CatPaisCustomFiltro getPais() {
		return pais;
	}



	public void setPais(CatPaisCustomFiltro pais) {
		this.pais = pais;
	}



	public PSGRCatEstado getEstado() {
		return estado;
	}



	public void setEstado(PSGRCatEstado estado) {
		this.estado = estado;
	}



	public String getPais_pid() {
		return pais_pid;
	}



	public void setPais_pid(String pais_pid) {
		this.pais_pid = pais_pid;
	}
	
	
	public String getPaisesRelacionPid() {
		return paises_relacion_pid;
	}



	public void setPaisesRelacionPid(String paises_relacion_pid) {
		this.paises_relacion_pid = paises_relacion_pid;
	}



	public String getEstado_pid() {
		return estado_pid;
	}



	public void setEstado_pid(String estado_pid) {
		this.estado_pid = estado_pid;
	}
	
	
	public String getEstadosRelacionPid() {
		return estados_relacion_pid;
	}



	public void setEstadosRelacionPid(String estados_relacion_pid) {
		this.estados_relacion_pid = estados_relacion_pid;
	}
	
	public String getUrlImagen() {
		return url_imagen;
	}

	public void setUrlImagen(String url_imagen) {
		this.url_imagen = url_imagen;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
