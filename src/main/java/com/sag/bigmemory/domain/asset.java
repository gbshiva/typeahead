package com.sag.bigmemory.domain;
import java.io.Serializable;

public class asset implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hostid;
	private String assetid;
	private String assetname;
	private String source;
	public String getHostid() {
		return hostid;
	}
	public void setHostid(String hostid) {
		this.hostid = hostid;
	}
	public String getAssetid() {
		return assetid;
	}
	public void setAssetid(String assetid) {
		this.assetid = assetid;
	}
	public String getAssetname() {
		return assetname;
	}
	public void setAssetname(String assetname) {
		this.assetname = assetname;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public asset( String hostid,String assetid,String assetname,String source){
		this.hostid = hostid;
		this.assetid = assetid;
		this.assetname = assetname;
		this.source = source;
	}
	
	
}
