package DAO;

import java.util.Collection;

import domain.Client;

public interface IClientDAO {
	
	public Boolean create(Client client);
	
	public Client getClient(Long cpf);
	
	public Collection<Client> getAllClients();
	
	public Client update(Client client);
	
	public Boolean delete(Long cpf);
}
