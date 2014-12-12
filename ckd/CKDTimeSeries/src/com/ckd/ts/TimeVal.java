package com.ckd.ts;

import java.util.Date;

/**
 * Class to represent one Time Value object
 * @author biplap
 *
 */
public class TimeVal {
	public Date date;
	public float value;
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the value
	 */
	public float getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(float value) {
		this.value = value;
	}
	
}
