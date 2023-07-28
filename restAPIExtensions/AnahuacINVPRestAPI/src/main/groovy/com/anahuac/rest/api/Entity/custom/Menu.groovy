package com.anahuac.rest.api.Entity.custom

class Menu {
	private Long id;
	private Long parentid;
	private Boolean isparent;
	private String url;
	private String token;
	private String menu;
	private String displayname;
	private String parent;
	private String parenttoken;
	public String getParenttoken() {
		return parenttoken;
	}
	public void setParenttoken(String parenttoken) {
		this.parenttoken = parenttoken;
	}
	public Long getId() {
		return id;
	}
	public Long getParentid() {
		return parentid;
	}
	public Boolean getIsparent() {
		return isparent;
	}
	public String getUrl() {
		return url;
	}
	public String getToken() {
		return token;
	}
	public String getMenu() {
		return menu;
	}
	public String getDisplayname() {
		return displayname;
	}
	public String getParent() {
		return parent;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}
	public void setIsparent(Boolean isparent) {
		this.isparent = isparent;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	
}
