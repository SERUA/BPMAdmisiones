package com.anahuac.rest.api.Entity.custom

class AspiranteSesionCustom {
	private Long idBpm;
	private String idBanner;
	private String nombre;
	private String uni;
	private String correoElectronico;
	private String telefono;
	private String celular;
	private Integer preguntas;
	private Integer contestadas;
	private String inicio;
	private String termino; 
	private String tiempo;
	private String estatus;
	private Boolean bloqueado;
	private String idioma;
	private Boolean terminado;
	private String caseidINVP;
	private String estatusINVP;
	private Boolean examenReiniciado;
	private Boolean usuarioBloqueado;
	private String tempprueba;
	private String tempentrada;
	private String tempsalida;
	private String tempfecha;
	private String temptoleranciaentrada;
	private String temptoleranciaSalida;
	private Boolean isTemporal;
	private Boolean resultadoEnviado;
	
	public Boolean getUsuarioBloqueado() {
		return usuarioBloqueado;
	}
	public Boolean getResultadoEnviado() {
		return resultadoEnviado;
	}
	public void setResultadoEnviado(Boolean resultadoEnviado) {
		this.resultadoEnviado = resultadoEnviado;
	}
	public void setUsuarioBloqueado(Boolean usuarioBloqueado) {
		this.usuarioBloqueado = usuarioBloqueado;
	}
	public Boolean getExamenReiniciado() {
		return examenReiniciado;
	}
	public void setExamenReiniciado(Boolean examenReiniciado) {
		this.examenReiniciado = examenReiniciado;
	}
	public String getCaseidINVP() {
		return caseidINVP;
	}
	public void setCaseidINVP(String caseidINVP) {
		this.caseidINVP = caseidINVP;
	}
	public String getEstatusINVP() {
		return estatusINVP;
	}
	public void setEstatusINVP(String estatusINVP) {
		this.estatusINVP = estatusINVP;
	}
	public Boolean getTerminado() {
		return terminado;
	}
	public void setTerminado(Boolean terminado) {
		this.terminado = terminado;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public Long getIdBpm() {
		return idBpm;
	}
	public void setIdBpm(Long idBpm) {
		this.idBpm = idBpm;
	}
	public String getIdBanner() {
		return idBanner;
	}
	public void setIdBanner(String idBanner) {
		this.idBanner = idBanner;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUni() {
		return uni;
	}
	public void setUni(String uni) {
		this.uni = uni;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public Integer getPreguntas() {
		return preguntas;
	}
	public void setPreguntas(Integer preguntas) {
		this.preguntas = preguntas;
	}
	public Integer getContestadas() {
		return contestadas;
	}
	public void setContestadas(Integer contestadas) {
		this.contestadas = contestadas;
	}
	public String getInicio() {
		return inicio;
	}
	public void setInicio(String inicio) {
		this.inicio = inicio;
	}
	public String getTermino() {
		return termino;
	}
	public void setTermino(String termino) {
		this.termino = termino;
	}
	public String getTiempo() {
		return tiempo;
	}
	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public Boolean getBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	public String getIdioma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	public String getTempprueba() {
		return tempprueba;
	}
	public void setTempprueba(String tempprueba) {
		this.tempprueba = tempprueba;
	}
	public String getTempentrada() {
		return tempentrada;
	}
	public void setTempentrada(String tempentrada) {
		this.tempentrada = tempentrada;
	}
	public String getTempsalida() {
		return tempsalida;
	}
	public void setTempsalida(String tempsalida) {
		this.tempsalida = tempsalida;
	}
	public String getTempfecha() {
		return tempfecha;
	}
	public void setTempfecha(String tempfecha) {
		this.tempfecha = tempfecha;
	}
	public String getTemptoleranciaentrada() {
		return temptoleranciaentrada;
	}
	public void setTemptoleranciaentrada(String temptoleranciaentrada) {
		this.temptoleranciaentrada = temptoleranciaentrada;
	}
	public String getTemptoleranciaSalida() {
		return temptoleranciaSalida;
	}
	public void setTemptoleranciaSalida(String temptoleranciaSalida) {
		this.temptoleranciaSalida = temptoleranciaSalida;
	}
	public Boolean getIsTemporal() {
		return isTemporal;
	}
	public void setIsTemporal(Boolean isTemporal) {
		this.isTemporal = isTemporal;
	}
	
}
