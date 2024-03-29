package DAO;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import domain.Client;

public class ClientSetDAO implements IClientDAO {
	
	private Set<Client> repositoryInMemory;
	
	public ClientSetDAO() {
		repositoryInMemory = new HashSet<>();
	}

	@Override
	public Boolean create(Client client) {
		return repositoryInMemory.add(client);
	}

	@Override
	public Client getClient(Long cpf) {
		return repositoryInMemory.stream().filter((el) -> el.getCpf().equals(cpf)).findFirst().orElse(null);
	}

	@Override
	public Collection<Client> getAllClients() {
		return repositoryInMemory;
	}

	@Override
	public Client update(Client client){
		Long cpfClient = client.getCpf();
		if (getClient(cpfClient) == null) return null;
		for (Client el : repositoryInMemory) {
			if (el.getCpf().equals(cpfClient)) {
				el.setCel(client.getCel());
				el.setCidade(client.getCidade());
				el.setEnd(client.getEnd());
				el.setEstado(client.getEstado());
				el.setName(client.getName());
				el.setNumero(client.getNumero());
				break;
			};
		}
		return getClient(cpfClient);
	}

	@Override
	public Boolean delete(Long cpf) {
		repositoryInMemory.stream().forEach((el) -> {
			if (el.getCpf().equals(cpf)) repositoryInMemory.remove(el); 
		});
		return null;
	}

}
