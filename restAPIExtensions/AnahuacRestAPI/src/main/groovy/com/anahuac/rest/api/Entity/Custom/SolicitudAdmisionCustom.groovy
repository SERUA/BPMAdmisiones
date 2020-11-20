package com.anahuac.rest.api.Entity.Custom

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class SolicitudAdmisionCustom {
	@XmlElement
	private Long persistenceId;
	@XmlElement
	private Long persistenceVersion;
	@XmlElement
	private CatLugarExamenCustom catLugarExamen;
	@XmlElement
	private CatCampusCustom catCampus;
	@XmlElement
	private CatLicenciaturaCustom catLicenciatura;
	@XmlElement
	private CatPeriodoCustom catPeriodo;
	@XmlElement
	private String primerNombre;
	@XmlElement
	private String segundoNombre;
	@XmlElement
	private String apellidoPaterno;
	@XmlElement
	private String apellidoMaterno;
	@XmlElement
	private String correoElectronico;
	@XmlElement
	private CatSexoCustom catSexo;
	@XmlElement
	private Date fechaNacimiento;
	@XmlElement
	private CatEstadoCivilCustom catEstadoCivil;
	@XmlElement
	private CatNacionalidadCustom catNacionalidad;
	@XmlElement
	private CatPresentasteEnOtroCampusCustom catPresentasteEnOtroCampus;
	@XmlElement
	private CatCampusCustom catCampusPresentadoSolicitud;
	@XmlElement
	private CatReligionCustom catReligion;
	@XmlElement
	private String curp;
	@XmlElement
	private String usuarioFacebook;
	@XmlElement
	private String usiarioTwitter;
	@XmlElement
	private String usuarioInstagram;
	@XmlElement
	private String telefonoCelular;
	@XmlElement
	private String foto;
	@XmlElement
	private String actaNacimiento;
	@XmlElement
	private String calle;
	@XmlElement
	private String codigoPostal;
	@XmlElement
	private CatPaisCustom catPais;
	@XmlElement
	private CatEstadosCustom catEstado;
	@XmlElement
	private String ciudad;
	@XmlElement
	private String calle2;
	@XmlElement
	private String numExterior;
	@XmlElement
	private String numInterior;
	@XmlElement
	private String colonia;
	@XmlElement
	private String telefono;
	@XmlElement
	private String otroTelefonoContacto;
	@XmlElement
	private String promedioGeneral;
	@XmlElement
	private String comprobanteCalificaciones;
	@XmlElement
	private CatPaisCustom catPaisExamen;
	@XmlElement
	private CatEstadosCustom catEstadoExamen;
	@XmlElement
	private String ciudadExamen;
	@XmlElement
	private Boolean avisoPrivacidad;
	@XmlElement
	private Boolean datosVeridicos;
	@XmlElement
	private Boolean aceptoAvisoPrivacidad;
	@XmlElement
	private Boolean confirmarAutorDatos;
	@XmlElement
	private Long caseId;
	@XmlElement
	private Long taskId;
	@XmlElement
	private CatBachilleratosCustom catBachilleratos;
	@XmlElement
	private String processName;
	@XmlElement
	private String processVersion;
	@XmlElement
	private String taskName;
	@XmlElement
	private CatGestionEscolar objCatGestionEscolar;
	@XmlElement
	private String fotografiaB64;
	@XmlElement
	private String updateDate;
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
	public CatLugarExamenCustom getCatLugarExamen() {
		return catLugarExamen;
	}
	public void setCatLugarExamen(CatLugarExamenCustom catLugarExamen) {
		this.catLugarExamen = catLugarExamen;
	}
	public CatCampusCustom getCatCampus() {
		return catCampus;
	}
	public void setCatCampus(CatCampusCustom catCampus) {
		this.catCampus = catCampus;
	}
	public CatLicenciaturaCustom getCatLicenciatura() {
		return catLicenciatura;
	}
	public void setCatLicenciatura(CatLicenciaturaCustom catLicenciatura) {
		this.catLicenciatura = catLicenciatura;
	}
	public CatPeriodoCustom getCatPeriodo() {
		return catPeriodo;
	}
	public void setCatPeriodo(CatPeriodoCustom catPeriodo) {
		this.catPeriodo = catPeriodo;
	}
	public String getPrimerNombre() {
		return primerNombre;
	}
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	public String getSegundoNombre() {
		return segundoNombre;
	}
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}
	public String getApellidoPaterno() {
		return apellidoPaterno;
	}
	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}
	public String getApellidoMaterno() {
		return apellidoMaterno;
	}
	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public CatSexoCustom getCatSexo() {
		return catSexo;
	}
	public void setCatSexo(CatSexoCustom catSexo) {
		this.catSexo = catSexo;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public CatEstadoCivilCustom getCatEstadoCivil() {
		return catEstadoCivil;
	}
	public void setCatEstadoCivil(CatEstadoCivilCustom catEstadoCivil) {
		this.catEstadoCivil = catEstadoCivil;
	}
	public CatNacionalidadCustom getCatNacionalidad() {
		return catNacionalidad;
	}
	public void setCatNacionalidad(CatNacionalidadCustom catNacionalidad) {
		this.catNacionalidad = catNacionalidad;
	}
	public CatPresentasteEnOtroCampusCustom getCatPresentasteEnOtroCampus() {
		return catPresentasteEnOtroCampus;
	}
	public void setCatPresentasteEnOtroCampus(CatPresentasteEnOtroCampusCustom catPresentasteEnOtroCampus) {
		this.catPresentasteEnOtroCampus = catPresentasteEnOtroCampus;
	}
	public CatCampusCustom getCatCampusPresentadoSolicitud() {
		return catCampusPresentadoSolicitud;
	}
	public void setCatCampusPresentadoSolicitud(CatCampusCustom catCampusPresentadoSolicitud) {
		this.catCampusPresentadoSolicitud = catCampusPresentadoSolicitud;
	}
	public CatReligionCustom getCatReligion() {
		return catReligion;
	}
	public void setCatReligion(CatReligionCustom catReligion) {
		this.catReligion = catReligion;
	}
	public String getCurp() {
		return curp;
	}
	public void setCurp(String curp) {
		this.curp = curp;
	}
	public String getUsuarioFacebook() {
		return usuarioFacebook;
	}
	public void setUsuarioFacebook(String usuarioFacebook) {
		this.usuarioFacebook = usuarioFacebook;
	}
	public String getUsiarioTwitter() {
		return usiarioTwitter;
	}
	public void setUsiarioTwitter(String usiarioTwitter) {
		this.usiarioTwitter = usiarioTwitter;
	}
	public String getUsuarioInstagram() {
		return usuarioInstagram;
	}
	public void setUsuarioInstagram(String usuarioInstagram) {
		this.usuarioInstagram = usuarioInstagram;
	}
	public String getTelefonoCelular() {
		return telefonoCelular;
	}
	public void setTelefonoCelular(String telefonoCelular) {
		this.telefonoCelular = telefonoCelular;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public String getActaNacimiento() {
		return actaNacimiento;
	}
	public void setActaNacimiento(String actaNacimiento) {
		this.actaNacimiento = actaNacimiento;
	}
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public CatPaisCustom getCatPais() {
		return catPais;
	}
	public void setCatPais(CatPaisCustom catPais) {
		this.catPais = catPais;
	}
	public CatEstadosCustom getCatEstado() {
		return catEstado;
	}
	public void setCatEstado(CatEstadosCustom catEstado) {
		this.catEstado = catEstado;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getCalle2() {
		return calle2;
	}
	public void setCalle2(String calle2) {
		this.calle2 = calle2;
	}
	public String getNumExterior() {
		return numExterior;
	}
	public void setNumExterior(String numExterior) {
		this.numExterior = numExterior;
	}
	public String getNumInterior() {
		return numInterior;
	}
	public void setNumInterior(String numInterior) {
		this.numInterior = numInterior;
	}
	public String getColonia() {
		return colonia;
	}
	public void setColonia(String colonia) {
		this.colonia = colonia;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getOtroTelefonoContacto() {
		return otroTelefonoContacto;
	}
	public void setOtroTelefonoContacto(String otroTelefonoContacto) {
		this.otroTelefonoContacto = otroTelefonoContacto;
	}
	public String getPromedioGeneral() {
		return promedioGeneral;
	}
	public void setPromedioGeneral(String promedioGeneral) {
		this.promedioGeneral = promedioGeneral;
	}
	public String getComprobanteCalificaciones() {
		return comprobanteCalificaciones;
	}
	public void setComprobanteCalificaciones(String comprobanteCalificaciones) {
		this.comprobanteCalificaciones = comprobanteCalificaciones;
	}
	public CatPaisCustom getCatPaisExamen() {
		return catPaisExamen;
	}
	public void setCatPaisExamen(CatPaisCustom catPaisExamen) {
		this.catPaisExamen = catPaisExamen;
	}
	public CatEstadosCustom getCatEstadoExamen() {
		return catEstadoExamen;
	}
	public void setCatEstadoExamen(CatEstadosCustom catEstadoExamen) {
		this.catEstadoExamen = catEstadoExamen;
	}
	public String getCiudadExamen() {
		return ciudadExamen;
	}
	public void setCiudadExamen(String ciudadExamen) {
		this.ciudadExamen = ciudadExamen;
	}
	public Boolean getAvisoPrivacidad() {
		return avisoPrivacidad;
	}
	public void setAvisoPrivacidad(Boolean avisoPrivacidad) {
		this.avisoPrivacidad = avisoPrivacidad;
	}
	public Boolean getDatosVeridicos() {
		return datosVeridicos;
	}
	public void setDatosVeridicos(Boolean datosVeridicos) {
		this.datosVeridicos = datosVeridicos;
	}
	public Boolean getAceptoAvisoPrivacidad() {
		return aceptoAvisoPrivacidad;
	}
	public void setAceptoAvisoPrivacidad(Boolean aceptoAvisoPrivacidad) {
		this.aceptoAvisoPrivacidad = aceptoAvisoPrivacidad;
	}
	public Boolean getConfirmarAutorDatos() {
		return confirmarAutorDatos;
	}
	public void setConfirmarAutorDatos(Boolean confirmarAutorDatos) {
		this.confirmarAutorDatos = confirmarAutorDatos;
	}
	public Long getCaseId() {
		return caseId;
	}
	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public CatBachilleratosCustom getCatBachilleratos() {
		return catBachilleratos;
	}
	public void setCatBachilleratos(CatBachilleratosCustom catBachilleratos) {
		this.catBachilleratos = catBachilleratos;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getProcessVersion() {
		return processVersion;
	}
	public void setProcessVersion(String processVersion) {
		this.processVersion = processVersion;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public CatGestionEscolar getObjCatGestionEscolar() {
		return objCatGestionEscolar;
	}
	public void setObjCatGestionEscolar(CatGestionEscolar objCatGestionEscolar) {
		this.objCatGestionEscolar = objCatGestionEscolar;
	}
	public String getFotografiaB64() {
		return fotografiaB64;
	}
	public void setFotografiaB64(String fotografiaB64) {
		this.fotografiaB64 = fotografiaB64;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	@Override
	public String toString() {
		return "SolicitudAdmisionCustom [persistenceId=" + persistenceId + ", persistenceVersion=" + persistenceVersion
				+ ", catLugarExamen=" + catLugarExamen + ", catCampus=" + catCampus + ", catLicenciatura="
				+ catLicenciatura + ", catPeriodo=" + catPeriodo + ", primerNombre=" + primerNombre + ", segundoNombre="
				+ segundoNombre + ", apellidoPaterno=" + apellidoPaterno + ", apellidoMaterno=" + apellidoMaterno
				+ ", correoElectronico=" + correoElectronico + ", catSexo=" + catSexo + ", fechaNacimiento="
				+ fechaNacimiento + ", catEstadoCivil=" + catEstadoCivil + ", catNacionalidad=" + catNacionalidad
				+ ", catPresentasteEnOtroCampus=" + catPresentasteEnOtroCampus + ", catCampusPresentadoSolicitud="
				+ catCampusPresentadoSolicitud + ", catReligion=" + catReligion + ", curp=" + curp
				+ ", usuarioFacebook=" + usuarioFacebook + ", usiarioTwitter=" + usiarioTwitter + ", usuarioInstagram="
				+ usuarioInstagram + ", telefonoCelular=" + telefonoCelular + ", foto=" + foto + ", actaNacimiento="
				+ actaNacimiento + ", calle=" + calle + ", codigoPostal=" + codigoPostal + ", catPais=" + catPais
				+ ", catEstado=" + catEstado + ", ciudad=" + ciudad + ", calle2=" + calle2 + ", numExterior="
				+ numExterior + ", numInterior=" + numInterior + ", colonia=" + colonia + ", telefono=" + telefono
				+ ", otroTelefonoContacto=" + otroTelefonoContacto + ", promedioGeneral=" + promedioGeneral
				+ ", comprobanteCalificaciones=" + comprobanteCalificaciones + ", catPaisExamen=" + catPaisExamen
				+ ", catEstadoExamen=" + catEstadoExamen + ", ciudadExamen=" + ciudadExamen + ", avisoPrivacidad="
				+ avisoPrivacidad + ", datosVeridicos=" + datosVeridicos + ", aceptoAvisoPrivacidad="
				+ aceptoAvisoPrivacidad + ", confirmarAutorDatos=" + confirmarAutorDatos + ", caseId=" + caseId
				+ ", taskId=" + taskId + ", catBachilleratos=" + catBachilleratos + ", processName=" + processName
				+ ", processVersion=" + processVersion + ", taskName=" + taskName + ", objCatGestionEscolar="
				+ objCatGestionEscolar + ", fotografiaB64=" + fotografiaB64 + ", updateDate=" + updateDate + "]";
	}

	
}
