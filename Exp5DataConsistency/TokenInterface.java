import java.rmi.*;

public interface TokenInterface extends Remote {
    public int[] getToken() throws RemoteException;

    public int[] getQueue() throws RemoteException;

    public int getOwner() throws RemoteException;

    public int getHead() throws RemoteException;

    public int getTail() throws RemoteException;

    public void setToken(int index, int value) throws RemoteException;

    public void setQueue(int[] queue) throws RemoteException;

    public void setOwner(int owner) throws RemoteException;

    public void setHead(int head) throws RemoteException;

    public void setTail(int tail) throws RemoteException;
}