package ejbinterfaces;

import java.util.List;

import entities.Record;

public interface ILinkManagerEJB {

	public abstract String insertNewRecordIntoDB(Record record);

	public abstract String retrieveRandomLink();

	public boolean urlAlreadyExists(String value);
	
	public List<Record> retrieveRecords();

}