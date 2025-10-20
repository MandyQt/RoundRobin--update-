
package myPackage;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Process> processes = new ArrayList<>();

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.println("Enter details for Process " + (i + 1) + ":");
            System.out.print("Process ID: ");
            String id = sc.next();
            System.out.print("Arrival Time: ");
            int at = sc.nextInt();
            System.out.print("Burst Time: ");
            int bt = sc.nextInt();
            processes.add(new Process(id, at, bt));
        }

        System.out.print("Enter Time Quantum: ");
        int tq = sc.nextInt();

        System.out.println("Choose Scheduling Type:\n1. Preemptive Round Robin\n2. Non-Preemptive Round Robin");
        int choice = sc.nextInt();

        Scheduler scheduler = new Scheduler(processes, tq);
        if (choice == 1) scheduler.runPreemptiveRR();
        else scheduler.runNonPreemptiveRR();

        scheduler.printResults();
    }
}