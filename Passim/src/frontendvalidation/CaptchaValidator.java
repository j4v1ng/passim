package frontendvalidation;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import servlets.MyCaptcha;

@Named("captchaValidator")
@RequestScoped
public class CaptchaValidator {

	// We inject this managed bean because we need it
	@Inject
	private MainPageValidator mainPageValidator;

	public void validateCaptcha(FacesContext context, UIComponent validate,
			Object value) {
		// This first line just checks against forbiden characters in the
		// captcha inputtext field
		mainPageValidator.validateBasicInputFields(context, validate, value);

		String inputFromField = (String) value;
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		Boolean isResponseCorrect = Boolean.FALSE;
		javax.servlet.http.HttpSession session = request.getSession();

		String parm = inputFromField;
		String captchaCheck = (String) session
				.getAttribute(MyCaptcha.CAPTCHA_KEY);

		if (!parm.equals(captchaCheck)) {
			throw new ValidatorException(new FacesMessage(""));
		}
	}
}
