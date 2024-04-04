package de.bassmech.findra.web.service.exception;

import java.util.Locale;

import de.bassmech.findra.core.exception.FinDraRuntimeException;
import de.bassmech.findra.web.handler.FacesMessageHandler;
import de.bassmech.findra.web.util.LocalizationUtil;
import jakarta.faces.application.FacesMessage;

public class FinDraMessageableRuntimeException extends FinDraRuntimeException {
	
	public FinDraMessageableRuntimeException(String message) {
		super(message);
		FacesMessageHandler.addMessage(FacesMessage.SEVERITY_ERROR
				, LocalizationUtil.getMessage("error.occured", Locale.getDefault()));
	}
}
