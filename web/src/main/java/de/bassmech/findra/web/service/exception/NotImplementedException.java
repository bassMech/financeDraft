package de.bassmech.findra.web.service.exception;

public class NotImplementedException extends FinDraMessageableRuntimeException {
	private static final long serialVersionUID = 1L;

	public NotImplementedException(String type, String value) {
		super(String.format("%s with value: %s is not implemented yet", type, value));
	}
	
}
