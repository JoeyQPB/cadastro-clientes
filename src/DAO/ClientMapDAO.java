package DAO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import domain.Client;

public class ClientMapDAO implements IClientDAO {
	
	private Map<Long, Client> repositoryInMemory;
	
	public ClientMapDAO() {
		repositoryInMemory = new HashMap<>();
	}

	@Override
	public Boolean create(Client client) {
		if (repositoryInMemory.containsKey(client.getCpf())) {
			return false;
		}
		
		repositoryInMemory.put(client.getCpf(), client);
		return true;
	}

	@Override
	public Client getClient(Long cpf) {
		return repositoryInMemory.get(cpf);
	}

	@Override
	public Collection<Client> getAllClients() {
		return repositoryInMemory.values();
	}

	@Override
	public Client update(Client client) {
		Long cpfClient = client.getCpf();
		if (getClient(cpfClient) == null) return null;
		repositoryInMemory.put(cpfClient, client);
		return getClient(cpfClient);
	}

	@Override
	public Boolean delete(Long cpf) {
		return repositoryInMemory.remove(cpf, getClient(cpf));
	}

}
