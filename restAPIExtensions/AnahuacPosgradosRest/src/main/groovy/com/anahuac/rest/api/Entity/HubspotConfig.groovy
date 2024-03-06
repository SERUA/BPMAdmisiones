package com.anahuac.rest.api.Entity

class HubspotConfig {
	public static final String CONFIGURACIONES="SELECT * FROM CATCONFIGURACION"
	public static final String GET_CONFIGURACIONES_CLAVE="SELECT * FROM CATCONFIGURACION WHERE clave=?"
	public static final String UPDATE_CONFIGURACIONES="UPDATE CATCONFIGURACION set valor=? where clave =?"
	public static final String INSERT_CONFIGURACIONES="INSERT INTO CATCONFIGURACION (clave,valor,descripcion,persistenceid,persistenceversion) values (?,?,?,case when (SELECT max(persistenceId)+1 from CATCONFIGURACION ) is null then 1 else (SELECT max(persistenceId)+1 from CATCONFIGURACION ) end,0)"
	
	private String emailHubspotRegistro;
	private String emailHubspotEnviada;
	private String emailHubspotModificar;
	private String emailHubspotValidar;
	private String emailHubspotRechazoLRoja;
	private String emailHubspotRestaurarRechazoLRoja;
	private String emailHubspotPago;
	private String emailHubspotAutodescripcion;
	private String emailHubspotGenerarCredencial;
	private String emailHubspotEsperaResultado;
	private String emailHubspotNoAsistioPruebas;
	private String emailHubspotSeleccionoFechaExamen;
	private String emailHubspotUsuarioRegistrado;
	private String emailHubspotTransferirAspirante;
	public String getEmailHubspotRegistro() {
		return emailHubspotRegistro;
	}
	public void setEmailHubspotRegistro(String emailHubspotRegistro) {
		this.emailHubspotRegistro = emailHubspotRegistro;
	}
	public String getEmailHubspotEnviada() {
		return emailHubspotEnviada;
	}
	public void setEmailHubspotEnviada(String emailHubspotEnviada) {
		this.emailHubspotEnviada = emailHubspotEnviada;
	}
	public String getEmailHubspotModificar() {
		return emailHubspotModificar;
	}
	public void setEmailHubspotModificar(String emailHubspotModificar) {
		this.emailHubspotModificar = emailHubspotModificar;
	}
	public String getEmailHubspotValidar() {
		return emailHubspotValidar;
	}
	public void setEmailHubspotValidar(String emailHubspotValidar) {
		this.emailHubspotValidar = emailHubspotValidar;
	}
	public String getEmailHubspotRechazoLRoja() {
		return emailHubspotRechazoLRoja;
	}
	public void setEmailHubspotRechazoLRoja(String emailHubspotRechazoLRoja) {
		this.emailHubspotRechazoLRoja = emailHubspotRechazoLRoja;
	}
	public String getEmailHubspotRestaurarRechazoLRoja() {
		return emailHubspotRestaurarRechazoLRoja;
	}
	public void setEmailHubspotRestaurarRechazoLRoja(String emailHubspotRestaurarRechazoLRoja) {
		this.emailHubspotRestaurarRechazoLRoja = emailHubspotRestaurarRechazoLRoja;
	}
	public String getEmailHubspotPago() {
		return emailHubspotPago;
	}
	public void setEmailHubspotPago(String emailHubspotPago) {
		this.emailHubspotPago = emailHubspotPago;
	}
	public String getEmailHubspotAutodescripcion() {
		return emailHubspotAutodescripcion;
	}
	public void setEmailHubspotAutodescripcion(String emailHubspotAutodescripcion) {
		this.emailHubspotAutodescripcion = emailHubspotAutodescripcion;
	}
	public String getEmailHubspotGenerarCredencial() {
		return emailHubspotGenerarCredencial;
	}
	public void setEmailHubspotGenerarCredencial(String emailHubspotGenerarCredencial) {
		this.emailHubspotGenerarCredencial = emailHubspotGenerarCredencial;
	}
	public String getEmailHubspotEsperaResultado() {
		return emailHubspotEsperaResultado;
	}
	public void setEmailHubspotEsperaResultado(String emailHubspotEsperaResultado) {
		this.emailHubspotEsperaResultado = emailHubspotEsperaResultado;
	}
	public String getEmailHubspotNoAsistioPruebas() {
		return emailHubspotNoAsistioPruebas;
	}
	public void setEmailHubspotNoAsistioPruebas(String emailHubspotNoAsistioPruebas) {
		this.emailHubspotNoAsistioPruebas = emailHubspotNoAsistioPruebas;
	}
	public String getEmailHubspotSeleccionoFechaExamen() {
		return emailHubspotSeleccionoFechaExamen;
	}
	public void setEmailHubspotSeleccionoFechaExamen(String emailHubspotSeleccionoFechaExamen) {
		this.emailHubspotSeleccionoFechaExamen = emailHubspotSeleccionoFechaExamen;
	}
	public String getEmailHubspotUsuarioRegistrado() {
		return emailHubspotUsuarioRegistrado;
	}
	public void setEmailHubspotUsuarioRegistrado(String emailHubspotUsuarioRegistrado) {
		this.emailHubspotUsuarioRegistrado = emailHubspotUsuarioRegistrado;
	}
	public String getEmailHubspotTransferirAspirante() {
		return emailHubspotTransferirAspirante;
	}
	public void setEmailHubspotTransferirAspirante(String emailHubspotTransferirAspirante) {
		this.emailHubspotTransferirAspirante = emailHubspotTransferirAspirante;
	}
	
}

