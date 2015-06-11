package com.shape100.gym.model;

import java.io.Serializable;
import java.util.Arrays;

public class Geo implements Serializable {

	/**
	 * @author yupu
	 * @date 2015年3月9日
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private Float coordinates[];

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Float[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Float coordinates[]) {
		this.coordinates = coordinates;
	}

	@Override
	public String toString() {
		return "Geo [type=" + type + ", coordinates="
				+ Arrays.toString(coordinates) + "]";
	}

}
