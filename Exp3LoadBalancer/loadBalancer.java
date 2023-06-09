import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class loadBalancer extends UnicastRemoteObject implements loadBalancerInterface{

    int noOfServers;
    int noOfRequests;
    public loadBalancer() throws RemoteException{
        noOfServers = 4;
        noOfRequests = 0;
    }

    public int getServerName() throws RemoteException{

        int serverNo = noOfRequests % noOfServers;
        return serverNo;
    }

    public checkBal getServer() throws RemoteException{
        int serverNo = noOfRequests % noOfServers;
        noOfRequests++;
        checkBal server = null;
        // return "server"+serverNo;
        try{
            String path = "bankServer" + serverNo;
            System.out.println("Redirecting request to server " +serverNo);
            Registry reg = LocateRegistry.getRegistry("localhost", 8000+serverNo);
            server = (checkBal) reg.lookup(path);
        }
        catch(NotBoundException e){
            System.out.println("Unable to connect to server, trying nextone " + e);
            server = this.getServer();
        }
        return server;
}
    public static void main(String args[]) throws RemoteException{
        loadBalancer token = new loadBalancer();
        try{
            String objPath = "loadBalancer"; //name of server location
            // TokenInterface stub = (TokenInterface)
            Registry reg = LocateRegistry.createRegistry(8081);
            reg.rebind(objPath, new loadBalancer());
            // UnicastRemoteObject.exportObject(token,0);
            // Naming.bind(objPath, token); //binding to name on rmiregistry
            System.out.println("Load balancing server is running now.");
        }
        catch(Exception e){
            System.out.println("Exception" + e);
        }
    }
}