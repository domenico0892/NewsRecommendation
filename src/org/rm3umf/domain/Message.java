package org.rm3umf.domain;

/**
 * E' una classe che mi serve solo per fare l'importing
 * @author Giulz
 *
 */

public class Message {
	
	private User user;
	private String idMessage;

	private String date;
	private String text;
	
	
	
	public Message(){
	}
	
	public String getIdMessage() {
		return idMessage;
	}
	public void setIdMessage(String idMessage) {
		this.idMessage = idMessage;
	}
	public User getUser() {
		return this.user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
//	public List<Resource> getResources(){
//		return this.resources;
//	}
//	
//	public void setResources(List<Resource> resources){
//		this.resources=resources;
//	}
//	
//	public void save() throws PersistenceException{
//		AAFacadePersistence.getInstance().messageSave(this);
//	}
//	
//	public void addResource(Resource resource) {
//		this.resources.add(resource);
//	}
	
	
	public String toString(){
		return "[MESSAGE: id="+idMessage+"("+date+")"+text+"]";
	}
	
	
	
	
	
	
	
	
	
	
}
