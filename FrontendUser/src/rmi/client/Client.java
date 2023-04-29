package rmi.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import remote.ServiceLocator;
import remote.IRemoteFacade;

public class Client {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("uso: java [policy] [codebase] cliente.Cliente [host] [port] [server]");
            System.exit(0);
        }

        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String serverName = args[2];

        String remoteUrl = String.format("//%s:%d/%s", serverIP, serverPort, serverName);
        System.out.println("Remote URL: " + remoteUrl);

        IRemoteFacade stubServer = null;
        try {
            Registry registry = LocateRegistry.getRegistry(serverPort);
            stubServer = (IRemoteFacade) registry.lookup(serverName);

            System.out.println("* Message coming from the server: '" + stubServer.sayHello() + "'");

        } catch (Exception e) {
            System.err.println("- Exception running the client: " + e.getMessage());
            e.printStackTrace();
        }

        // Agregar el resto del código de tu cliente.
    }
}
