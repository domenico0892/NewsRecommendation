package org.rm3umf.domain;

public class Enrichment {
	
	public int numberInstance; //lo tengo?
	public String name;

	public int getNumberInstance() {
		return numberInstance;
	}

	public void setNumberInstance(int numberInstance) {
		this.numberInstance = numberInstance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String s = name.toLowerCase();
		this.name = org.apache.commons.lang3.StringUtils.stripAccents(s.trim());
	}
	
}
