package com.anahuac.rest.api.Entity

class PropertiesEntity {
	
	private String password;
	private String usuario;
	private String urlHost;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getUrlHost() {
		return urlHost;
	}
	public void setUrlHost(String urlHost) {
		this.urlHost = urlHost;
	}
	@Override
	public String toString() {
		return "Properties [password=" + password + ", usuario=" + usuario + ", urlHost=" + urlHost + "]";
	}
		
}