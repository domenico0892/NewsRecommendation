package org.rm3umf.domain;

public class Signal implements Comparable<Signal>{
	
	private User user;
	private Enrichment enrichment;
	private double[] signal;
	private String type;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Enrichment getEnrichment() {
		return enrichment;
	}
	public void setEnrichment(Enrichment enrichment) {
		this.enrichment = enrichment;
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
	
	
	public String toString(){
//		String stringSignal="["+valueSignal(signal)+"]";
		String stringSignal="["+"-signal-"+"]";
		return "[SIGNAL:"+user.getIduser()+"-"+enrichment.getName()+"-"+stringSignal+"]";
	}
	
	private String valueSignal(double[] array){
		String stringa="";
		if(array!=null){
			for(double f:array){
				stringa=stringa+" "+f;
			}
		}
		return stringa;
	}
	
	public int compareTo(Signal arg0) {
		return String.compare(enrichment.getName(),arg0.getEnrichment().getName());
	}
	
	
	

}
