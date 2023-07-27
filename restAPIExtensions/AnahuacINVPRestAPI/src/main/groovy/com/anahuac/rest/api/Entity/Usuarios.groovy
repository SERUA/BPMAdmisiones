package com.anahuac.rest.api.Entity

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class Usuarios {
	
	@XmlElement
	private Long persistenceId;
	@XmlElement
	private Long persistenceVersion;
	@XmlElement
	private String nombreusuario;
	@XmlElement
	private String primernombre;
	@XmlElement
	private String segundonombre;
	@XmlElement
	private String apellidopaterno;
	@XmlElement
	private String apellidomaterno;
	@XmlElement
	private String correoelectronico;
	@XmlElement
	private String password;
	@XmlElement
	private Boolean isEliminado;
	@XmlElement
	private Boolean ayuda;
	
	
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
	
	public String getNombreusuario() {
		return nombreusuario;
	}
	public void setNombreusaurio(String nombreusuario) {
		this.nombreusaurio = nombreusuario;
	}
	
	public String getPrimernombre() {
		return primernombre;
	}
	public void setPrimernombre(String primernombre) {
		this.primernombre = primernombre;
	}
	
	public String getSegundonombre() {
		return segundonombre;
	}
	public void setSegundonombre(String segundonombre) {
		this.segundonombre = segundonombre;
	}
	
	public String getApellidopaterno() {
		return apellidopaterno;
	}
	public void setApellidopaterno(String apellidopaterno) {
		this.apellidopaterno = apellidopaterno;
	}
	
	public String getApellidomaterno() {
		return apellidomaterno;
	}
	public void setApellidomaterno(String apellidomaterno) {
		this.apellidomaterno = apellidomaterno;
	}
	
	public String getCorreoelectronico() {
		return correoelectronico;
	}
	public void setCorreoelectronico(String correoelectronico) {
		this.correoelectronico = correoelectronico;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Boolean getIsEliminado() {
		return isEliminado;
	}
	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
	}
	
	public Boolean getAyuda() {
		return ayuda;
	}
	public void setAyuda(Boolean ayuda) {
		this.ayuda = ayuda;
	}
	
	
	
	@Override
	public String toString() {
		return "CatBachilleratosCustom [persistenceId=" + persistenceId + ", persistenceVersion=" + persistenceVersion
				+ ", nombreusuario=" + nombreusuario+ ", primernombre=" + primernombre + ", segundonombre=" + segundonombre + ", apellidopaterno=" + apellidopaterno
				+ ", apellidomaterno=" + apellidomaterno + ", correoelectronico=" + correoelectronico + ", password=" + password 
				 + ", ayuda=" + ayuda + ", isEliminado=" + isEliminado + "]";
	}
	
	
}
