package org.rm3umf.domain;

import java.util.Arrays;

public class TruthSignal {

	private int id;
	private User user;
	private double[] signal;
	private String type;

	public TruthSignal() {
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public double[] getSignal() {
		return signal;
	}
	
	public void setSignal(double[] signal) {
		this.signal = signal;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "TruthSignal [id=" + id + ", user=" + user + ", signal="
				+ Arrays.toString(signal) + ", type=" + type + "]";
	}
}
