package com.shape100.gym.protocol.oauth;


public class Parameter implements Comparable<Parameter> {


	private final String key;
	private final String value;

	public Parameter(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey(){
		return key;
	}
	
	public String asUrlEncodedPair() {
		// return
		// OAuthEncoder.encode(key).concat("=").concat(OAuthEncoder.encode(value));
		return key.concat("=").concat(SafeEncoder.encode(value));
	}

	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Parameter))
			return false;

		Parameter otherParam = (Parameter) other;
		return otherParam.key.equals(key) && otherParam.value.equals(value);
	}

	public int hashCode() {
		return key.hashCode() + value.hashCode();
	}

	public int compareTo(Parameter parameter) {
		int keyDiff = key.compareTo(parameter.key);

		return keyDiff != 0 ? keyDiff : value.compareTo(parameter.value);
	}

}
