package de.bassmech.findra.web.handler;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.FacesException;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ExceptionQueuedEvent;

public class BaseExceptionHandler extends ExceptionHandlerWrapper {

	private Logger logger = LoggerFactory.getLogger(BaseExceptionHandler.class);
	
    private ExceptionHandler wrapped;

    public BaseExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void handle() throws FacesException {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        for (Iterator<ExceptionQueuedEvent> iter = getUnhandledExceptionQueuedEvents().iterator(); iter.hasNext();) {
            Throwable exception = iter.next().getContext().getException(); // There it is!

            // Now do your thing with it. This example implementation merely prints the stack trace.
            exception.printStackTrace();
            logger.error("Error to handle", exception);
            // You could redirect to an error page (bad practice).
            // Or you could render a full error page (as OmniFaces does).
            // Or you could show a FATAL faces message.
            // Or you could trigger an oncomplete script.
            // etc..
        }

        getWrapped().handle();
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

}
