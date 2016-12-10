package com.net.nky.garea.rsp;

/**
 * 
 * @author longhai
 *
 */
public interface IError {

	/**
	 * @return the code
	 */
	String getCode();

	/**
	 * @return the desc
	 */
	String getDesc();
	
	/**
	 * @return the extDesc
	 */
	String getExtDesc();

	/**
	 * 
	 * @param desc
	 */
	void setExtDesc(String desc);

}

