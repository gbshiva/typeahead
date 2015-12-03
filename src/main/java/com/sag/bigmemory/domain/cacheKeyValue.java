package com.sag.bigmemory.domain;

public class cacheKeyValue {
	private Object KEY;
	private Object VALUE;
	public Object getKEY() {
		return KEY;
	}
	public void setKEY(Object kEY) {
		KEY = kEY;
	}
	public Object getVALUE() {
		return VALUE;
	}
	public void setVALUE(Object vALUE) {
		VALUE = vALUE;
	}
	public cacheKeyValue(Object key, Object value){
		this.KEY = key;
		this.VALUE = value;
	}

}
