import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    public static void main(String args[]) throws RemoteException {
        try {
            Scanner sc = new Scanner(System.in);
            Registry reg = LocateRegistry.getRegistry("localhost", 8000);
            checkBal ob = (checkBal) reg.lookup("bankServer");
            System.out.print("\nEnter account number:");
            String acc_no = sc.nextLine();
            System.out.print("Enter password:");
            String password = sc.nextLine();
            double bal = ob.checkBalance(acc_no, password);
            if (bal == -1) {
                System.out.println("\nInvalid credentials");
                return;
            } else {
                System.out.println("\nBalance: Rs." + bal+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}