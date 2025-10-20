
package myPackage;

import java.util.*; 

public class Scheduler {
       List<Process> processes;
    int timeQuantum;
    int totalTime = 0;
    List<String> ganttChart = new ArrayList<>();

    public Scheduler(List<Process> processes, int timeQuantum) {
        this.processes = processes;
        this.timeQuantum = timeQuantum;
    }

    public void runPreemptiveRR() {
        Queue<Process> queue = new LinkedList<>();
        int currentTime = 0;
        List<Process> arrived = new ArrayList<>();

        while (true) {
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && !arrived.contains(p)) {
                    queue.add(p);
                    arrived.add(p);
                }
            }

            if (queue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process current = queue.poll();
            int execTime = Math.min(timeQuantum, current.remainingTime);
            for (int i = 0; i < execTime; i++) {
                ganttChart.add(current.id);
                currentTime++;
                for (Process p : processes) {
                    if (p.arrivalTime == currentTime && !arrived.contains(p)) {
                        queue.add(p);
                        arrived.add(p);
                    }
                }
            }

            current.remainingTime -= execTime;
            if (current.remainingTime > 0) {
                queue.add(current);
            } else {
                current.completionTime = currentTime;
            }

            if (arrived.size() == processes.size() && queue.isEmpty()) break;
        }

        totalTime = currentTime;
        calculateMetrics();
    }

    public void runNonPreemptiveRR() {
        Queue<Process> queue = new LinkedList<>();
        int currentTime = 0;
        List<Process> arrived = new ArrayList<>();

        while (true) {
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && !arrived.contains(p)) {
                    queue.add(p);
                    arrived.add(p);
                }
            }

            if (queue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process current = queue.poll();
            int execTime = Math.min(timeQuantum, current.remainingTime);
            for (int i = 0; i < execTime; i++) {
                ganttChart.add(current.id);
                currentTime++;
            }

            current.remainingTime -= execTime;
            if (current.remainingTime > 0) {
                queue.add(current);
            } else {
                current.completionTime = currentTime;
            }

            if (arrived.size() == processes.size() && queue.isEmpty()) break;
        }

        totalTime = currentTime;
        calculateMetrics();
    }

    private void calculateMetrics() {
        for (Process p : processes) {
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
        }
    }

    public void printResults() {
        System.out.println("\nProcess Table:");
        System.out.printf("%-5s %-10s %-10s %-10s %-10s %-10s\n", "ID", "Arrival", "Burst", "Waiting", "Turnaround", "Completion");
        for (Process p : processes) {
            System.out.printf("%-5s %-10d %-10d %-10d %-10d %-10d\n",
                    p.id, p.arrivalTime, p.burstTime, p.waitingTime, p.turnaroundTime, p.completionTime);
        }

        double awt = processes.stream().mapToInt(p -> p.waitingTime).average().orElse(0);
        double att = processes.stream().mapToInt(p -> p.turnaroundTime).average().orElse(0);
        int totalBurst = processes.stream().mapToInt(p -> p.burstTime).sum();
        double cpuUtil = ((double) totalBurst / totalTime) * 100;

        System.out.println("\nGantt Chart:");
        for (String slot : ganttChart) System.out.print("| " + slot + " ");
        System.out.println("|");

        System.out.printf("\nCPU Utilization: %.2f%%\n", cpuUtil);
        System.out.printf("Average Waiting Time: %.2f\n", awt);
        System.out.printf("Average Turnaround Time: %.2f\n", att);
    }
}