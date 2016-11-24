package com.acton.springintegratevertx.model;

public class ValueObject {
	private String value;

	
	public ValueObject(String value) {
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
