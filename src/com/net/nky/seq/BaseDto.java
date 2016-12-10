package com.net.nky.seq;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * <p>
 * <h2>BaseDto是所有DTO类的抽象基类</h2>
 * </p>
 * 
 * <p>
 * 实现了日志跟踪功能，实现了toString方法，实现了序列化功能（Serializable）。
 * </p>
 * 
 * <p>
 * DTO类可以包含PO对象，DTO对象不应该出现在Dao层，可以出现在Service层和其他上
 * 层，一般在Service层组装DTO对象。
 * </p>
 * 
 * <p>
 * DTO类可以传递到ActionForm类，并作为ActionForm类的属性使用，以减少
 * 不必要的get/set方法调用。
 * </p>
 * 
 *
 */
public abstract class BaseDto implements Serializable {

	//日志对象
	private transient Log log = null;

	/**构造DTO对象*/
	public BaseDto() {
		log = LogFactory.getLog(this.getClass());
//		if (SysConfig.isDebug() && getLog().isDebugEnabled()) {
//			getLog().debug("Creating " + this.getClass().getName());
//		}
	}
	
	/**释放DTO对象*/
	protected void finalize() {
//		if (SysConfig.isDebug() && getLog().isDebugEnabled()) {
//			getLog().debug("Finalizing " + this.getClass().getName());
//		}
	}
	
    /**取得日志对象*/
	protected Log getLog() {
		return log;
	}


	/**两个实例是否相等*/
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (this == o) {
			return true;
		}
		if (getClass() != o.getClass()) {
			return false;
		}
		return EqualsBuilder.reflectionEquals(this, o);
	}
	

    /**将DTO对象转换成字符串*/
	public String toString() {
    	return ToStringBuilder.reflectionToString(this,
    			ToStringStyle.MULTI_LINE_STYLE);
    }

}
