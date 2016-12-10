package com.net.jfinal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 *  model绑定数据库表注解
 * 
 * huilet 2013-3-20
 * @author yuanc
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TableBind {
	/**表名*/
	String name() default "";
	/**主键名*/
	String pk() default "id";
}