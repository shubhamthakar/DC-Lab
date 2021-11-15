import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class Token extends UnicastRemoteObject implements TokenInterface {
    int token[];
    int queue[];
    int owner;
    int head;
    int tail;

    public Token() throws RemoteException {
        token = new int[3];
        queue = new int[100];
        owner = -1;
        head = 0;
        tail = 0;
    }

    public int[] getToken() throws RemoteException {
        return token;
    }

    public int[] getQueue() throws RemoteException {
        return queue;
    }

    public int getOwner() throws RemoteException {
        return owner;
    }

    public int getHead() throws RemoteException {
        return head;
    }

    public int getTail() throws RemoteException {
        return tail;
    }

    public void setToken(int index, int value) throws RemoteException {
        this.token[index] = value;
    }

    public void setQueue(int[] queue) throws RemoteException {
        this.queue = queue;
    }

    public void setOwner(int owner) throws RemoteException {
        this.owner = owner;
    }

    public void setHead(int head) throws RemoteException {
        this.head = head;
    }

    public void setTail(int tail) throws RemoteException {
        this.tail = tail;
    }

    public static void main(String args[]) throws RemoteException {
        try {
            // String objPath = "Token"; // name of server location
            // Naming.bind(objPath, token); // binding to name on rmiregistry
            // System.out.println("Remote Server is running now.");
            // int no = Integer.parseInt(args[0]);
            // int portNumber = 8000 + no;
            Registry reg = LocateRegistry.createRegistry(8082);
            reg.rebind("tokenServer", new Token());
            System.out.println("Token server is running..");
        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
    }
}