import java.io.*;
import java.util.Scanner;

class Bully {
    static int n;
    static int priority[] = new int[100];
    static int status[] = new int[100];
    static int co;

    public static void main(String args[]) throws IOException {
        System.out.println("Enter the number of process");
        Scanner in = new Scanner(System.in);
        n = in.nextInt();
        int i;
        for (i = 0; i < n; i++) {
            System.out.println("For process " + (i + 1) + ":");
            System.out.println("Status:");
            status[i] = in.nextInt();
            System.out.println("Priority");
            priority[i] = in.nextInt();
        }
        System.out.println("Which process will initiate election?");
        int ele = in.nextInt();
        elect(ele);
        System.out.println("Final coordinator is " + co);
    }

    static void elect(int ele) {
        ele = ele - 1;
        co = ele + 1;
        for (int i = 0; i < n; i++) {
            if (priority[ele] < priority[i]) {
                System.out.println("Election message is sent from " + (ele + 1) + " to " + (i + 1));
                if (status[i] == 1)
                    elect(i + 1);
            }
        }
    }
}