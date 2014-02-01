package com.bbytes.config.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Cloud Config property type
 * 
 * @author Thanneer
 * 
 */
public class CCProperty implements Serializable {

	private static final long serialVersionUID = 4283008852216866053L;

	private String propertyName;

	private Object propertyValue;

	private Date creationDate;

	private Date updateDate;

	private DataType dataType;

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName
	 *            the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate
	 *            the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the propertyValue
	 */
	public Object getPropertyValue() {
		return propertyValue;
	}

	/**
	 * @param propertyValue
	 *            the propertyValue to set
	 */
	public void setPropertyValue(Object propertyValue) {
		this.propertyValue = propertyValue;
	}

	/**
	 * @return the dataType
	 */
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * @param dataType
	 *            the dataType to set
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String toString() {
		return "PropertyName:" + propertyName + ",  PropertyValue:"
				+ propertyValue + ",  UpdateDate:"
				+ updateDate.toLocaleString();
	}

}
