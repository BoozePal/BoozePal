package hu.deik.boozepal.web.session.bean;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class LocaleBean implements Serializable {

	private static final long serialVersionUID = -3618279272011850649L;
	private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
	
	public Locale getLocale() {
		return locale;
	}

	public String getLanguage() {
		return locale.getLanguage();
	}

	public void setLanguage(String language) {
		locale = new Locale(language);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}
}