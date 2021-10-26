import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.time.*;

public class Client {
    public static void main(String args[]) throws RemoteException {
        try {
            Scanner sc = new Scanner(System.in);
            Registry reg = LocateRegistry.getRegistry("localhost", 8000);
            checkBal obj_bal = (checkBal) reg.lookup("bankServer");
            System.out.print("\nEnter account number:");
            String acc_no = sc.nextLine();
            System.out.print("Enter password:");
            String password = sc.nextLine();
            Clock client_time = Clock.systemUTC();
            Registry reg_time = LocateRegistry.getRegistry("localhost",8080);
            getTime obj = (getTime) reg_time.lookup("timeServer");
            long start = Instant.now().toEpochMilli();
            long serverTime = obj.getSystemTime(); 
            System.out.println("Server time "+ serverTime);
            long end = Instant.now().toEpochMilli();
            long rtt = (end-start)/2;
            System.out.println("Round Trip Time " + rtt);
            long updatedTime = serverTime + rtt;
            client_time = Clock.offset(client_time, Duration.ofMillis(updatedTime - client_time.instant().toEpochMilli()));
            System.out.println("New Client time " + client_time.instant().toEpochMilli());            
            double bal = obj_bal.checkBalance(acc_no, password);
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