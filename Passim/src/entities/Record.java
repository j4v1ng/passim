package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;


/*JPA does not support random(), what should be done is:
 * 1- Return the count
 * 2- Calculate the random using the count value in the EJB
 * 3- Return the row with that index
 * */

@Entity
@NamedQueries({
		@NamedQuery(name = "allids", query = "SELECT r.id FROM Record r"),
		@NamedQuery(name = "singleURL", query = "SELECT r.url FROM Record r WHERE r.id= :idparam"),
		@NamedQuery(name = "allrecordinfo", query = "SELECT r FROM Record r") })
public class Record {

	@Id
	@GeneratedValue
	private long id;
	@Column(nullable = false)
	private String url;
	@Column(nullable = false)
	private String submitionDate;
	@Column(nullable = false)
	private boolean unnaceptableContent;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSubmitionDate() {
		return submitionDate;
	}

	public void setSubmitionDate(String submitionDate) {
		this.submitionDate = submitionDate;
	}

	public boolean isUnnaceptableContent() {
		return unnaceptableContent;
	}

	public void setUnnaceptableContent(boolean unnaceptableContent) {
		this.unnaceptableContent = unnaceptableContent;
	}
}
