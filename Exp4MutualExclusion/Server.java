import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements checkBal {
    public Server(int serverNo) throws RemoteException {
        super();
        RN = new int[3];
        no_of_requests = 0;
        critical = false;
        this.serverNo = serverNo;
        try {
            // token = (TokenInterface) Naming.lookup("Token");
            Registry reg = LocateRegistry.getRegistry("localhost", 8082);
            TokenInterface token = (TokenInterface) reg.lookup("tokenServer");
            this.token = token;
        } catch (Exception e) {
            System.out.println("Exception occurred : " + e.getMessage());
        }
    }

    static ArrayList<Account> a = new ArrayList<Account>() {
        {
            add(new Account("123456", "password1", 2000.0));
            add(new Account("456789", "password2", 3000.0));
            add(new Account("234567", "password3", 4000.0));
            add(new Account("345678", "password4", 5000.0));
        }
    };
    // int serverNo;
    // Clock clock;
    int RN[];
    boolean critical;
    int no_of_requests;
    TokenInterface token;
    int serverNo;

    public double checkBalance(String acc_no, String password) throws RemoteException {
        System.out.println("Request received for account number " + acc_no);
        for (int i = 0; i < a.size(); i++) {
            double bal = a.get(i).checkBalance(acc_no, password);
            if (bal != -1)
                return bal;
        }
        return -1.0;
    }

    public boolean transfer(String d_acc_no, String cred_acc_no, String password, double amt) throws RemoteException {
        System.out.println("Request received for account number " + d_acc_no);
        System.out.println("Credit account number " + cred_acc_no);
        boolean isValid = false;
        for (int i = 0; i < a.size(); i++) {
            isValid = a.get(i).checkValid(d_acc_no, password);
            if (isValid) {
                break;
            }
        }
        if (!isValid) {
            return false;
        } else {
            // Sync clocks ??
            if (token.getOwner() == -1) {
                token.setOwner(serverNo);
                System.out.println("No owner");
                no_of_requests++;
                RN[serverNo]++;
            } else {
                sendRequest();
            }
            while (token.getOwner() != serverNo)
                ;
            System.out.println("Got token");
            critical = true;
            boolean b = critical_section(d_acc_no, cred_acc_no, password, amt);
            critical = false;
            releaseToken();
            System.out.println("Updated Balance:"+checkBalance(d_acc_no, password));
            return b;
        }

    }

    public void sendRequest() throws RemoteException {
        no_of_requests++;
        for (int i = 0; i < 1; i++) {
            try {
                // checkBal server = (checkBal) Naming.lookup("server" + i);
                Registry reg = LocateRegistry.getRegistry("localhost", 8000+i);
                checkBal server = (checkBal) reg.lookup("bankServer"+i);
                server.receiveRequest(serverNo, no_of_requests);
            } catch (Exception e) {
                System.out.println("Exception occurred : " + e.getMessage());
            }
        }
    }

    public boolean critical_section(String d_acc_no, String cred_acc_no, String password, double amt) {
        // Account deb = new Account();
        // Account cred = new Account();
        int deb_ind = 0;
        int cred_ind = 0;
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).acc_no.equals(d_acc_no) && a.get(i).password.equals(password)) {
                deb_ind = i;
                System.out.println("Got deb ind");
            }
            if (a.get(i).acc_no.equals(cred_acc_no)) {
                cred_ind = i;
                System.out.println("Got cred ind");
            }
        }
        if (a.get(deb_ind).balance < amt)
            return false;
        else {
            a.get(deb_ind).balance -= amt;
            a.get(cred_ind).balance += amt;
            System.out.println("Debit acct:"+a.get(deb_ind).balance);
            System.out.println("Credit acct:"+a.get(cred_ind).balance);
            return true;
        }
    }

    public void receiveRequest(int i, int n) throws RemoteException {
        System.out.println("Recieved request from " + i);
        if (RN[i] <= n) {
            RN[i] = n;
            if (token.getToken()[i] + 1 == RN[i]) {
                if (token.getOwner() == serverNo) {
                    if (critical) {
                        // token.queue = i;
                        System.out.println("Add to queue");
                        token.getQueue()[token.getTail()] = i;
                        token.setTail(token.getTail() + 1);
                    } else {
                        System.out.println("Queue empty, setting owner");
                        token.setOwner(i);
                    }
                }
            }
        }
    }

    public void releaseToken() throws RemoteException {
        token.setToken(serverNo, RN[serverNo]);
        if (token.getHead() != token.getTail()) {
            System.out.println("Release token");
            token.setOwner(token.getQueue()[token.getHead()]);
            System.out.println("New owner" + token.getOwner());
            token.setHead(token.getHead() + 1);
        }
    }

    public static void main(String[] args) {
        try {
            int no = Integer.parseInt(args[0]);
            int portNumber = 8000 + no;
            Registry reg = LocateRegistry.createRegistry(portNumber);
            reg.rebind("bankServer" + args[0], new Server(no));
            System.out.println("Server no " + args[0] + " is running..");
            // a.add(new Account("123456", "password1", 2000.0));
            // a.add(new Account("456789", "password2", 3000.0));
            // a.add(new Account("234567", "password3", 4000.0));
            // a.add(new Account("345678", "password4", 5000.0));
            System.out.println("no of acc:"+a.size());
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

    Account()
    {
        this.acc_no = "";
        this.password = "";
        this.balance = 0.0;
    }

    public double checkBalance(String acc_no, String password) {
        if (this.acc_no.equals(acc_no) && this.password.equals(password))
            return this.balance;
        else
            return -1.0;
    }

    public boolean checkValid(String acc_no, String password) {
        if (this.acc_no.equals(acc_no) && this.password.equals(password))
            return true;
        else
            return false;
    }
}