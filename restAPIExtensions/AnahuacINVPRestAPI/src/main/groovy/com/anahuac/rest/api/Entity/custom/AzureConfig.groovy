package com.anahuac.rest.api.Entity.custom

class AzureConfig {
	public static final String CONFIGURACIONES="SELECT * FROM CATCONFIGURACION"
	public static final String GET_CONFIGURACIONES_CLAVE="SELECT * FROM CATCONFIGURACION WHERE clave=?"
	public static final String UPDATE_CONFIGURACIONES="UPDATE CATCONFIGURACION set valor=? where clave =?"
	public static final String INSERT_CONFIGURACIONES="INSERT INTO CATCONFIGURACION (clave,valor,descripcion,persistenceid,persistenceversion) values (?,?,?,case when (SELECT max(persistenceId)+1 from CATCONFIGURACION ) is null then 1 else (SELECT max(persistenceId)+1 from CATCONFIGURACION ) end,0)"
	private String azureAccountName;
	private String azureAccountKey;
	private String azureDefaultEndpointsProtocol;
	private String bannerToken;
	private String adminPassword;
	private String bannerMatchPerson;
	public String getBannerMatchPerson() {
		return bannerMatchPerson;
	}
	public void setBannerMatchPerson(String bannerMatchPerson) {
		this.bannerMatchPerson = bannerMatchPerson;
	}
	public String getAdminPassword() {
		return adminPassword;
	}
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	public String getAzureAccountName() {
		return azureAccountName;
	}
	public String getAzureAccountKey() {
		return azureAccountKey;
	}
	public String getAzureDefaultEndpointsProtocol() {
		return azureDefaultEndpointsProtocol;
	}
	public void setAzureAccountName(String azureAccountName) {
		this.azureAccountName = azureAccountName;
	}
	public void setAzureAccountKey(String azureAccountKey) {
		this.azureAccountKey = azureAccountKey;
	}
	public void setAzureDefaultEndpointsProtocol(String azureDefaultEndpointsProtocol) {
		this.azureDefaultEndpointsProtocol = azureDefaultEndpointsProtocol;
	}
	public String getBannerToken() {
		return bannerToken;
	}
	public void setBannerToken(String bannerToken) {
		this.bannerToken = bannerToken;
	}
	
}
