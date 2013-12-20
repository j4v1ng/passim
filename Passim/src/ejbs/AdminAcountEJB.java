package ejbs;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ejbinterfaces.IAdminAcountEJB;
import entities.Record;

@Stateless(name = "ejbs/AdminAcountEJB")
public class AdminAcountEJB implements IAdminAcountEJB {

	@PersistenceContext
	private EntityManager entityManager;

	public String retriveAdminPassword() {
		Query queryForIds = entityManager.createNamedQuery("adminpassword");
		return (String) queryForIds.getResultList().get(0);
	}

	public void deleteRecord(Record record) {
		//The record entity must be deatached from the persistence context, so the row
		//can be erased.
		//Read this carefully: http://stackoverflow.com/questions/7218715/delete-database-rows-with-ejb-3-0
		Record detachedRecord = entityManager.merge(record);
		entityManager.remove(detachedRecord);
	}
}
