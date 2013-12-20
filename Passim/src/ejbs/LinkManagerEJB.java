package ejbs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ejbinterfaces.ILinkManagerEJB;
import entities.Record;

/*"Stateless session beans are session beans whose instances have no 
 * conversational state. This means that all bean instances are equivalent 
 * when they are not involved in servicing a client-invoked method. 
 * The term 'stateless' signifies that an instance has no state for a 
 * specific client."*/
@Stateless(name = "ejbs/LinkManagerEJB")
public class LinkManagerEJB implements ILinkManagerEJB {

	@PersistenceContext
	private EntityManager entityManager;

	public String insertNewRecordIntoDB(Record record) {
		entityManager.persist(record);// Already validated in the Validator CDI
										// Bean
		return "thanks?faces-redirect=true";// Go to thanks page
	}

	public String retrieveRandomLink() {
		// Get the total amount of records
		Query queryForIds = entityManager.createNamedQuery("allids");
		List<Long> allIds = queryForIds.getResultList();

		// Prepare the random record
		long selectedId = allIds.get(new Random().nextInt(allIds.size()));
		Query queryRandomUrl = entityManager.createNamedQuery("singleURL");
		queryRandomUrl.setParameter("idparam", selectedId);

		return queryRandomUrl.getSingleResult().toString();		
	}
	
	@Override
	public boolean urlAlreadyExists(String value) {
		Query checkURLExists = entityManager
				.createQuery("SELECT COUNT(r.url) FROM Record r WHERE r.url=:urlparam");
		checkURLExists.setParameter("urlparam", value);
		long matchCounter = 0;
		matchCounter = (Long) checkURLExists.getSingleResult();
		if (matchCounter > 0) {
			System.out.println("COUNTER:" + matchCounter);
			return true;
		}
		return false;
	} 
	
	
	public List<Record> retrieveRecords() {
		Query allRecords = entityManager.createNamedQuery("allrecordinfo");		 
		return allRecords.getResultList();
	}
}
