package frontendvalidation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

import ejbinterfaces.ILinkManagerEJB;

@Named("mainPageValidator")
@RequestScoped
public class MainPageValidator {

	@EJB
	private ILinkManagerEJB linkManagerEJB;

	public void validateInputedUrl(FacesContext context, UIComponent validate,
			Object value) {
		String inputFromField = (String) value;		

		
			//Regex for base URL. Found at: http://www.coderanch.com/t/382015/java/java/regex-find-url
			String simpleTextPatternText = "(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?";
			Pattern pattern = null;
			Matcher matcher = null;
			pattern = Pattern.compile(simpleTextPatternText);
			matcher = pattern.matcher(inputFromField);
			if (matcher.matches() == false) {
				// The message will be overloaded by the value given at
				// validatorMessage property in the page
				throw new ValidatorException(new FacesMessage(""));
			}
			else {
				//That url already exists in database
				if (linkManagerEJB.urlAlreadyExists(inputFromField)) {
					throw new ValidatorException(new FacesMessage(""));
				}	//Too long url
				if(inputFromField.length() > 70) {				
					throw new ValidatorException(new FacesMessage(""));
				}
			}		
	}
	
	//Validates the management page input
	public void validateBasicInputFields(FacesContext context, UIComponent validate,
			Object value) {
		String inputFromField = (String) value;
		
		String simpleTextPatternText = "^[a-zA-Z0-9]+$";
		Pattern pattern = null;
		Matcher matcher = null;
		pattern = Pattern.compile(simpleTextPatternText);
		matcher = pattern.matcher(inputFromField);
		if (matcher.matches() == false) {
			throw new ValidatorException(new FacesMessage(""));
		}
	}
}
