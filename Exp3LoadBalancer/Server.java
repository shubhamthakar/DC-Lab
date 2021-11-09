import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements checkBal {
    public Server() throws RemoteException {
        super();
    }

    static ArrayList<Account> a = new ArrayList<Account>();

    public double checkBalance(String acc_no, String password) throws RemoteException {
        System.out.println("Request received for account number " + acc_no);
        for (int i = 0; i < a.size(); i++) {
            double bal = a.get(i).checkBalance(acc_no, password);
            if (bal != -1)
                return bal;
        }
        return -1.0;
    }


    public static void main(String[] args) {
        try {
            int no = Integer.parseInt(args[0]);
            int portNumber = 8000 + no;
            Registry reg = LocateRegistry.createRegistry(portNumber);
            reg.rebind("bankServer"+args[0], new Server());
            System.out.println("Server no "+args[0]+" is running..");
            a.add(new Account("123456", "password1", 2000.0));
            a.add(new Account("456789", "password2", 3700.50));
            a.add(new Account("234567", "password1", 2000.0));
            a.add(new Account("345678", "password2", 3700.50));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Account {
    String acc_no;
    String password;
    double balance;

    Account(String acc_no, String password, double balance) {
        this.acc_no = acc_no;
        this.password = password;
        this.balance = balance;
    }

    public double checkBalance(String acc_no, String password) {
        if (this.acc_no.equals(acc_no) && this.password.equals(password))
            return this.balance;
        else
            return -1.0;
    }
}