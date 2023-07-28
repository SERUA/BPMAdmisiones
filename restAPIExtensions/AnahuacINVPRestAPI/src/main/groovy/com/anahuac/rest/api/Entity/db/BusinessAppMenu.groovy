package com.anahuac.rest.api.Entity.db

class BusinessAppMenu {
	public static String GET="SELECT tenantid, id, displayname, applicationid, applicationpageid, parentid, index_ FROM business_app_menu where parentid is null order by index_"
	private Long tenantid;
	private Long id;
	private String displayname;
	private Long applicationid;
	private Long applicationpageid;
	private Long parentid;
	private Integer index_;
	public Long getTenantid() {
		return tenantid;
	}
	public Long getId() {
		return id;
	}
	public String getDisplayname() {
		return displayname;
	}
	public Long getApplicationid() {
		return applicationid;
	}
	public Long getApplicationpageid() {
		return applicationpageid;
	}
	public Long getParentid() {
		return parentid;
	}
	public Integer getIndex_() {
		return index_;
	}
	public void setTenantid(Long tenantid) {
		this.tenantid = tenantid;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public void setApplicationid(Long applicationid) {
		this.applicationid = applicationid;
	}
	public void setApplicationpageid(Long applicationpageid) {
		this.applicationpageid = applicationpageid;
	}
	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}
	public void setIndex_(Integer index_) {
		this.index_ = index_;
	}
	
	@Override
	public boolean equals(Object arg0) {
		Boolean part1=arg0 != null;
		Boolean part2=arg0 instanceof BusinessAppMenu;
		Boolean part3=((BusinessAppMenu) arg0).getId().equals(this.id);
		return (part1 && part2 && part3);
	}
}
