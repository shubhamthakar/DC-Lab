import java.rmi.*;

public interface getTime extends Remote {
    long getSystemTime() throws RemoteException;
}
