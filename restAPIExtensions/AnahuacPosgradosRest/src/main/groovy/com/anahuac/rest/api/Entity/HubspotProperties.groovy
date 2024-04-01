package com.anahuac.rest.api.Entity

class HubspotProperties {
	private Boolean hidden;
	private Boolean readOnly;
	private String label;
	private String description;
	private Long displayOrder;
	private String value;
	public Boolean getHidden() {
		return hidden;
	}
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	public Boolean getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "HubspotProperties [hidden=" + hidden + ", readOnly=" + readOnly + ", label=" + label + ", description="
				+ description + ", displayOrder=" + displayOrder + ", value=" + value + "]";
	}

}
