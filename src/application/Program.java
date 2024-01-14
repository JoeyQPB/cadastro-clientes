package application;

import java.util.Collection;

import javax.swing.JOptionPane;

import DAO.ClientMapDAO;
import DAO.IClientDAO;
import domain.Client;

public class Program {
	
	private static IClientDAO clientDAO;
	private static Boolean exit = false;
	private static String title = " ## SISTEMA DE USUÁRIOS ##";
	
	public static void main(String[] args) {
		clientDAO = new ClientMapDAO();
		runProgram();
	}
	
	public static void runProgram() {
		String firstMsg = "Bem Vindo! Selecione a operação que deseja: \n\n"
				+ "1 - Cadastrar usário;\n2 - Ver todos usuários;\n3 - Ver usuário específico;\n"
				+ "4 - Alterar usuário,;\n5 - Deletar usuário;\n6 - Sair.\n\n";
		do {
			String answer = JOptionPane.showInputDialog(null, firstMsg, title, JOptionPane.INFORMATION_MESSAGE);
			checkOption(answer);
		} while (!exit);
	}
	
	public static void checkOption(String option) {
		if (!isNull(option))
			switch (option.trim()) {
			case "1":
				optionCreate();
				break;
			case "2":
				optionGetAll();
				break;
			case "3":
				optionGet();
				break;
			case "4":
				optionUpdate();
				break;
			case "5":
				optionDelete();
				break;
			case "6":
				exit();
				break;
			default:
				errorOption();
				break;
			}
	}
	
	private static <T> Boolean isNull(T var) {
		if (var == null) {
			cancelOption();
			return true;
		}
		return false;
	}

	private static void optionCreate() {
		Client newClient = formClient();
		if (isNull(newClient)) return; 
		
		Boolean isCreated = clientDAO.create(newClient);
		String msg = (isCreated != null)
				? msg = "Client: " + newClient.getName() + " - CPF: " + newClient.getCpf() + ", foi cadastrado!"
				: "Client com CPF: " + newClient.getCpf()+ " já está cadastrado! Consulte para mais informações.";

		JOptionPane.showMessageDialog(null, msg, "## CADASTRO", JOptionPane.INFORMATION_MESSAGE);
	}

	private static void optionGetAll() {
		Collection<Client> clients = clientDAO.getAllClients();
		StringBuilder msg = new StringBuilder();
		if (clients.size() < 0) msg.append("Ainda não há clientes cadastrados!");
		else {
			msg.append("Usuários Cadastrados:");
			msg.append("\n\n");
			for (Client c : clients) {
				msg.append("\n\n");
				msg.append(c.toString());
			}
		}
		JOptionPane.showMessageDialog(null, msg , title, JOptionPane.INFORMATION_MESSAGE);
	}

	private static void optionGet() {
		Long clientCpf = getCpf();
		if (isNull(clientCpf)) return;
		Client client = clientDAO.getClient(clientCpf);
		String msg = (client != null) ? client.toString() : "Cliene não encontrado";
		JOptionPane.showMessageDialog(null, msg , title, JOptionPane.INFORMATION_MESSAGE);
	}

	private static void optionUpdate() {
		//pegar cpf
//		Client client = formClientUpdate();
//		if (isNull(client)) return; 
//		String msg;
//		if (clientDAO.getClient(client.getCpf()) == null) msg = "Client não encontrado";
//		else {
//			Client clientUpdated = clientDAO.update(client);
//			msg = clientUpdated.toString();
//		}
//		JOptionPane.showMessageDialog(null, msg , title, JOptionPane.INFORMATION_MESSAGE);
	}



	private static void optionDelete() {
		Long clientCpf = getCpf();
		if (isNull(clientCpf)) return;
		Boolean isDeleted = clientDAO.delete(clientCpf);
		String msg = (isDeleted) ? "Cliente Deletado"
				: (clientDAO.getClient(clientCpf) != null) ? 
						"Client Não deletado" : "Cliente não pode ser encontrado";

		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}

	private static void exit() {
		JOptionPane.showMessageDialog(null, "Processo Finalizado", title, JOptionPane.INFORMATION_MESSAGE);
		exit = true;
		return;
	}

	private static void errorOption() {
		JOptionPane.showMessageDialog(null, "Opção Inválida", title, JOptionPane.INFORMATION_MESSAGE);
		return;
	}
	
	private static void cancelOption() {
		JOptionPane.showMessageDialog(null, "Processo Cancelado", title, JOptionPane.INFORMATION_MESSAGE);
		exit = true;
		return;
	}
	
	private static Client formClient() {
		String formMsg = "Insira os dados do cliente separados por virgula:\n"
				+ "nome, celular, cpf, endereço, número, cidade, estado\n"
				+ "(ex: "
				+ "Jhon Doe, 11999990000, 00000000000, rua dos bobos, 0, Salvador, Bahia)\n\n";
		String clientString = JOptionPane.showInputDialog(null, formMsg, title, JOptionPane.INFORMATION_MESSAGE);
		if (clientString == null) return null;
		String[] clientFields = clientString.split(",");
		for (int i = 0; i < clientFields.length; i++) {
			clientFields[i] = clientFields[i].trim();
		}

		// TO-DO: verify fields 
		return new Client(clientFields[0], Long.valueOf(clientFields[1]), Long.valueOf(clientFields[2]),
				clientFields[3], Integer.parseInt(clientFields[4]), clientFields[5], clientFields[6]);
	}
	
//	private static Client formClientUpdate() {
//		String formMsg = "Insira os dados do cliente separados por virgula:\n"
//				+ "nome, celular, cpf, endereço, número, cidade, estado\n"
//				+ "(ex: "
//				+ "Jhon Doe, 11999990000, rua dos bobos, 0, Salvador, Bahia)\n\n";
//		String clientString = JOptionPane.showInputDialog(null, formMsg, title, JOptionPane.INFORMATION_MESSAGE);
//		if (clientString == null) return null;
//		String[] clientFields = clientString.split(",");
//		for (int i = 0; i < clientFields.length; i++) {
//			clientFields[i] = clientFields[i].trim();
//		}
//
//		// TO-DO: verify fields 
//		return new Client(clientFields[0], Long.valueOf(clientFields[1]), clientFields[2],
//				Integer.parseInt(clientFields[3]), clientFields[4], clientFields[5]);
//	}
	
	private static Long getCpf() {
		String getCpfMsg = "Informe o CPF:\n"
				+ "(ex: 00000000000)";
		String cpfString = JOptionPane.showInputDialog(null, getCpfMsg, title, JOptionPane.INFORMATION_MESSAGE);
		
		// TO-DO: verify cpf
		if (cpfString == null) return null;
		
		return Long.parseLong(cpfString);
	}

}
