package com.example.demo.error;

import java.time.LocalDateTime;

public class ErrorResponse {
    private LocalDateTime  timestamp;
    private int status;
    private String error;
	private String context;

	public LocalDateTime  getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime  timestamp) {
		this.timestamp = timestamp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
}
