package de.bassmech.findra.web.handler;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.FacesException;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ExceptionQueuedEvent;
import jakarta.faces.event.ExceptionQueuedEventContext;

public class BaseExceptionHandler extends ExceptionHandlerWrapper {

	private Logger logger = LoggerFactory.getLogger(BaseExceptionHandler.class);

	private ExceptionHandler exceptionHandler;

	public BaseExceptionHandler(ExceptionHandler wrapped) {
		exceptionHandler = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return exceptionHandler;
	}

	@Override
	public void handle() throws FacesException {
		final Iterator<ExceptionQueuedEvent> queue = getUnhandledExceptionQueuedEvents().iterator();

		while (queue.hasNext()) {
			ExceptionQueuedEvent item = queue.next();
			ExceptionQueuedEventContext exceptionQueuedEventContext = (ExceptionQueuedEventContext) item.getSource();

			try {
				Throwable throwable = exceptionQueuedEventContext.getException();
				//System.err.println("Exception: " + throwable.getMessage());
				logger.error("Exception: " + throwable.getMessage());

				FacesContext context = FacesContext.getCurrentInstance();
				Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
				NavigationHandler nav = context.getApplication().getNavigationHandler();

				requestMap.put("error-message", throwable.getMessage());
				requestMap.put("error-stack", throwable.getStackTrace());
				nav.handleNavigation(context, null, "/error");
				context.renderResponse();

			} finally {
				queue.remove();
			}
		}
	}
}
