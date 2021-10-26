import java.time.Instant;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class TimeServer extends UnicastRemoteObject implements getTime {

    public TimeServer() throws RemoteException {
        super();
    }

    public long getSystemTime() {
        long time = Instant.now().toEpochMilli();
        System.out.println("Client request received at time "+ time);
        return time;
    }

    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.createRegistry(8080);
            reg.rebind("timeServer", new TimeServer());
            System.out.println("Time Server is running..");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
