package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.server.api.IUserService;
import rmi.server.exceptions.InvalidUser;

public class Client {

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("uso: java [policy] [codebase] cliente.Cliente [host] [port] [server]");
			System.exit(0);
		}

		IUserService stubServer = null;

		try {
			Registry registry = LocateRegistry.getRegistry(((Integer.valueOf(args[1]))));
			String name = "//" + args[0] + ":" + args[1] + "/" + args[2];
			stubServer = (IUserService) registry.lookup(name);
			System.out.println("* Message coming from the server: '" + stubServer.sayHello() + "'");

		} catch (Exception e) {
			System.err.println("- Exception running the client: " + e.getMessage());
			e.printStackTrace();
		}

		try {
			stubServer.registrarUsuario("Test1", "Test1");
			System.out.println("* Added user Test1");

			stubServer.registrarUsuario("Test2", "Test2");
			System.out.println("* Added user Test2");

			stubServer.registrarUsuario("Test3", "Test3");
			System.out.println("* Added user Test3");

			stubServer.registrarUsuario("Test3", "Test3");
			System.out.println("* Added user Test3");
		} catch (InvalidUser iu) {
			System.err.println("- Exception running the client: " + iu.getErrorMessage());
		} catch (Exception e) {
			System.err.println("- Exception running the client: " + e.getMessage());
		}

		try {
			System.out.println(
					"* Message coming from the server: " + stubServer.sayMessage("Test1", "Test1", "Message 1"));
			System.out.println(
					"* Message coming from the server: " + stubServer.sayMessage("Test2", "Test2", "Message 2"));
			System.out.println(
					"* Message coming from the server: " + stubServer.sayMessage("Test3", "Test3", "Message 3"));
			System.out.println(
					"* Message coming from the server: " + stubServer.sayMessage("Test3", "Test4", "Message 4"));
			System.out.println(
					"* Message coming from the server: " + stubServer.sayMessage("Test4", "Test4", "Message 5"));
		} catch (InvalidUser iu) {
			System.err.println("- Exception running the client: " + iu.getErrorMessage());
		} catch (Exception e) {
			System.err.println("- Exception running the client: " + e.getMessage());
		}

	}
}