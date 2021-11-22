import java.rmi.*;

public interface checkBal extends Remote {
    public double checkBalance(String acc_no, String password) throws RemoteException;
    public boolean transfer(String d_acc_no, String cred_acc_no, String password, double amt) throws RemoteException;
    public void receiveRequest(int i, int n) throws RemoteException;
}