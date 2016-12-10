package com.net.nky.seq;

import java.sql.Timestamp;

public class DateLable {

	private static Timestamp timeLable = new Timestamp(System.currentTimeMillis());

	private static final DateLable dateLableInstance = new DateLable();

	private DateLable() {

	}

	public static DateLable getdateLableInst() {
		return dateLableInstance;
	}

	public Timestamp getTimeLable() {
		if (timeLable != null) {
			return timeLable;
		} else {
			timeLable = new Timestamp(System.currentTimeMillis());
			return timeLable;
		}
	}
}
