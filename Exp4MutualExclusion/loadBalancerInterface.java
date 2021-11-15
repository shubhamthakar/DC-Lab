import java.rmi.*;

public interface loadBalancerInterface extends Remote {

    public checkBal getServer() throws RemoteException;
    public int getServerName() throws RemoteException;

}