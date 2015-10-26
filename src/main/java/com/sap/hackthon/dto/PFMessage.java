package com.sap.hackthon.dto;

public class PFMessage {

    private String name;
    private String length;
    private String startTime;
    private String endTime;
    


	public PFMessage(String name, String length, String startTime,
			String endTime) {
		this.name = name;
		this.length = length;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getName() {
        return name;
    }

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setName(String name) {
		this.name = name;
	}

}
