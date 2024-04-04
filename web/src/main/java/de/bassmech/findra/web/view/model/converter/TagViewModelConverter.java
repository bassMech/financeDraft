package de.bassmech.findra.web.view.model.converter;

import org.springframework.stereotype.Component;

import de.bassmech.findra.web.view.model.TagViewModel;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@Component
@FacesConverter(value = "tagViewModelConverter", managed = false)
// not used
public class TagViewModelConverter implements Converter<TagViewModel> {

	@Override
	public TagViewModel getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, TagViewModel value) {
		// TODO Auto-generated method stub
		return value.getTitle();
	}

}
