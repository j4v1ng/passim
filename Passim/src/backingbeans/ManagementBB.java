package backingbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import ejbinterfaces.IAdminAcountEJB;
import ejbinterfaces.ILinkManagerEJB;
import entities.Record;

@Named("managementBB")
@SessionScoped
public class ManagementBB implements Serializable{
	
	private static final long serialVersionUID = -5672767293737585714L;
	@EJB
	private ILinkManagerEJB linkManagerEJB;
	@EJB
	private IAdminAcountEJB managementEJB;
	
	private String input;
	
	private boolean authorized;
		
	public String seeWhatsUp() {
		if(input.equals(managementEJB.retriveAdminPassword())) {
			authorized = true;
			return "manage?faces-redirect=true;";
		}
		input = "";
		return "index?faces-redirect=true;";
	}
	
	public String exit() {
		input = "";
		authorized = false;
		return "index?faces-redirect=true;";
	}
	
	public String deleteRow(Record record) {		
		managementEJB.deleteRecord(record);
		return "manage?faces-redirect=true;";
	}
	
	public String goToLink(String clickedLink) {
		String link = clickedLink;
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
	
	
	//GET SET METHODS
	public List<Record> retrieveRecords() {		
			return linkManagerEJB.retrieveRecords();
	}

	public boolean isAuthorized() {
		return authorized;
	}

	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}	
}
