package backingbeans;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import ejbinterfaces.ILinkManagerEJB;
import entities.Record;

@Named("mainPageBB")
@RequestScoped
public class MainPageBB {

	@EJB
	private ILinkManagerEJB linkManagerEJB;
	private String inputUrl;	
	private String captchaTextInput;

	//Every time the page loads validation messages garbage should be cleared!
	public MainPageBB() {
		Iterator<FacesMessage> msgIterator = FacesContext.getCurrentInstance().getMessages();
	    while(msgIterator.hasNext())
	    {
	        msgIterator.next();
	        msgIterator.remove();
	    }
	 //   inputUrl = "Enter an URL";
	 //  captchaTextInput = "Enter the security code";
	}
	
	public String goToRandomLink() {
		String link = linkManagerEJB.retrieveRandomLink();
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("http://" + link.trim());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (link != null) {
			return link;
		}
		return null;
	}

	public String submitNewLink() {
		Record record = new Record();
		record.setUrl(inputUrl);
		record.setSubmitionDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
				.format(new Date()));		

		if (record.getUrl().contains("http://")) {
			String noHttp = record.getUrl().replace("http://", "");
			record.setUrl(noHttp);
		}		
		linkManagerEJB.insertNewRecordIntoDB(record);
		return "index.xhtml?faces-redirect=true&link=1";
	}

	// GETTERS AND SETTERS
	public String getInputUrl() {
		return inputUrl;
	}

	public void setInputUrl(String inputUrl) {
		this.inputUrl = inputUrl;
	}

	public String getCaptchaTextInput() {
		return captchaTextInput;
	}

	public void setCaptchaTextInput(String captchaTextInput) {
		this.captchaTextInput = captchaTextInput;
	}	
}
