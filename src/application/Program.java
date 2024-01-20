package application;

import java.util.Collection;

import javax.swing.JOptionPane;

import DAO.ClientMapDAO;
import DAO.ClientSetDAO;
import DAO.IClientDAO;
import domain.Client;

public class Program {
	
	private static IClientDAO clientDAO;
	private static Boolean exit = false;
	private static String title = " ## SISTEMA DE USUÁRIOS ##";
	
	public static void main(String[] args) {
//		clientDAO = new ClientSetDAO();
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
				? "Client: " + newClient.getName() + " - CPF: " + newClient.getCpf() + ", foi cadastrado!"
				: "Client com CPF: " + newClient.getCpf()+ " já está cadastrado! Consulte para mais informações.";

		JOptionPane.showMessageDialog(null, msg, "## CADASTRO", JOptionPane.INFORMATION_MESSAGE);
	}

	private static void optionGetAll() {
		Collection<Client> clients = clientDAO.getAllClients();
		StringBuilder msg = new StringBuilder();
		if (clients.size() < 0) msg.append("Ainda não há clientes cadastrados!");
		else {
			msg.append("Usuários Cadastrados:");
			msg.append("\n");
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
		Long clientCpf = getCpf();
		if (isNull(clientCpf)) return;
		Client client = clientDAO.getClient(clientCpf);
		String msg = (client != null) ? client.toString() + " esse é o cliente que deseja atualizar?\n(1 - sim | 2 - não)" : "Cliene não encontrado";
		String answer = JOptionPane.showInputDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
		if (isNull(answer)) return; 
		else if (answer.trim().equals("1")) {
			Client updateData = formUpdateClient(clientCpf);
			if (isNull(updateData)) return; 
			Client clientUpdated = clientDAO.update(updateData);
			msg = (clientUpdated != null) ? clientUpdated.toString() : "Algo deu errado";
			JOptionPane.showMessageDialog(null, msg, "## CADASTRO", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		else if (answer.trim().equals("2")) {
			optionUpdate();
			return;
		}
		else {
			JOptionPane.showMessageDialog(null, "Resposta inválida!" , title, JOptionPane.INFORMATION_MESSAGE);
			return;
		}
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
		JOptionPane.showMessageDialog(null, "Algo não ocorreu como esperado.\nProcesso cancelado!", title, JOptionPane.INFORMATION_MESSAGE);
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
		if (clientFields.length != 7) return null;
		
		for (int i = 0; i < clientFields.length; i++) {
			clientFields[i] = clientFields[i].trim();
		}
		
		Long celNum = Long.valueOf(clientFields[1]);
		Long numCPF = Long.valueOf(clientFields[2]);
		Integer numEnd = Integer.valueOf(clientFields[4]);
		
		if (!verifyType(clientFields[0], String.class)) return null;
		if (!verifyType(celNum, Long.class)) return null;
		if (!verifyType(numCPF, Long.class)) return null;
		if (!verifyType(clientFields[3], String.class)) return null;
		if (!verifyType(numEnd, Integer.class)) return null;
		if (!verifyType(clientFields[5], String.class)) return null;
		if (!verifyType(clientFields[6], String.class)) return null;
		
		return new Client(clientFields[0], celNum, numCPF, clientFields[3], numEnd,
						  clientFields[5], clientFields[6]);
	}
	
	private static Client formUpdateClient(Long cpf) {
		String formMsg = "Insira os novos dados para o cliente separados por virgula:\n"
				+ "nome, celular, endereço, número, cidade, estado\n"
				+ "(ex: "
				+ "Jhon Doe, 11999990000, rua dos bobos, 0, Salvador, Bahia)\n\n";
		String clientString = JOptionPane.showInputDialog(null, formMsg, title, JOptionPane.INFORMATION_MESSAGE);
		if (clientString == null) return null;
		String[] clientFields = clientString.split(",");
		if (clientFields.length != 6) return null;
		
		for (int i = 0; i < clientFields.length; i++) {
			clientFields[i] = clientFields[i].trim();
		}
		
		Long celNum = Long.valueOf(clientFields[1]);
		Integer numEnd = Integer.valueOf(clientFields[3]);
		
		if (!verifyType(clientFields[0], String.class)) return null;
		if (!verifyType(celNum, Long.class)) return null;
		if (!verifyType(clientFields[2], String.class)) return null;
		if (!verifyType(numEnd, Integer.class)) return null;
		if (!verifyType(clientFields[4], String.class)) return null;
		if (!verifyType(clientFields[5], String.class)) return null;
		
		return new Client(clientFields[0], celNum, cpf, clientFields[2],
						  numEnd, clientFields[4], clientFields[5]);
	}
	
	private static Long getCpf() {
		String getCpfMsg = "Informe o CPF:\n"
				+ "(ex: 00000000000)";
		String cpfString = JOptionPane.showInputDialog(null, getCpfMsg, title, JOptionPane.INFORMATION_MESSAGE);
		return stringToLong(cpfString);
	}
	
    public static Long stringToLong(String input) {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter para Long: " + e.getMessage());
            return null;
        }
    }
    
    public static <T> boolean verifyType(Object var, Class<T> typeOfVar) {
    	if (isNull(var)) return false;
        return typeOfVar.isInstance(var);
    }
}
