package ejbinterfaces;

import entities.Record;

public interface IAdminAcountEJB {

	public abstract String retriveAdminPassword();

	public abstract void deleteRecord(Record record);
}