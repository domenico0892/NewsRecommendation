package org.rm3umf.domain;
import java.util.List;




public class UserModel {

	private User user;
	private List<Signal> signals;
	private TruthSignal truthSignal;


	public UserModel(User user){
		this.user=user;
	}

	public UserModel(User user,List<Signal> listSignal){
		this.user=user;
		this.signals=listSignal;
	}

	public UserModel(User user,TruthSignal truthSignal){
		this.user=user;
		this.truthSignal=truthSignal;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Signal> getSignals() {
		return signals;
	}

	public void setSignals(List<Signal> signals) {
		this.signals = signals;
	}


	public TruthSignal getTruthSignal() {
		return truthSignal;
	}

	public void setTruthSignal(TruthSignal truthSignal) {
		this.truthSignal = truthSignal;
	}

	public boolean equals(Object um){
		return this.user.getIduser()==(((UserModel)um).getUser().getIduser() );
	}


	public String toString(){
		String s = "[USER MODEL :"+user.getIduser();
		if(signals != null) {
			s += " numSignal="+signals.size();
		}
		s +="]";
		return s;
	}




}
