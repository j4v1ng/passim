package backingbeans;

import java.io.Serializable;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named("languageChangeBB")
@SessionScoped//Needs to be Session scoped, so the locale changed lasts until the session ends
public class LanguageChangeBB implements Serializable {

	private static final long serialVersionUID = -4043846128619557776L;
	private Locale locale = FacesContext.getCurrentInstance().getViewRoot()
			.getLocale();	
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void changeToEnglish() {		
		locale = new Locale("en");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}

	public void changeToFrench() {
		locale = new Locale("fr");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);		
	}

	public void changeToGerman() {
		locale = new Locale("de");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);		
	}

	public void changeToArabic() {
		locale = new Locale("ar");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);		
	}

	public void changeToSpanish() {
		locale = new Locale("es");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);		
	}

	public void changeToItalian() {
		locale = new Locale("it");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);		
	}

	public void changeToSerbian() {
		locale = new Locale("sr");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);		
	}

	public void changeToRusian() {
		locale = new Locale("ru");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);		
	}
}
