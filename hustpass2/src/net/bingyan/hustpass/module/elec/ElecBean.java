package net.bingyan.hustpass.module.elec;

public class ElecBean {
	String remain;
	String[][] history;
	// state: notfound success
	String state;
	String datetime;

	public String getRemain() {
		return remain;
	}

	public void setRemain(String remain) {
		this.remain = remain;
	}

	public String[][] getHistory() {
		return history;
	}

	public void setHistory(String[][] history) {
		this.history = history;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

}
