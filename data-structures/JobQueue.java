import java.io.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class JobQueue {
    private int numWorkers;
    private int[] jobs;

    private int[] assignedWorker;
    private long[] startTime;

    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new JobQueue().solve();
    }

    private void readData() throws IOException {
        numWorkers = in.nextInt();
        int m = in.nextInt();
        jobs = new int[m];
        for (int i = 0; i < m; ++i) {
            jobs[i] = in.nextInt();
        }
    }

    private void writeResponse() {
        for (int i = 0; i < jobs.length; ++i) {
            out.println(assignedWorker[i] + " " + startTime[i]);
        }
    }

    private void assignJobs() {
        assignedWorker = new int[jobs.length];
        startTime = new long[jobs.length];
        Queue<Worker> workersPQ = new PriorityQueue<>(11);
        for (int i = 0; i < numWorkers; i++) {
            workersPQ.offer(new Worker(i));
        }
        for (int i = 0; i < jobs.length; i++) {
            int duration = jobs[i];
            Worker worker = workersPQ.poll();
            assignedWorker[i] = worker.id;
            startTime[i] = worker.next_free_time;
            worker.next_free_time += duration;
            workersPQ.add(worker);
        }
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        readData();
        assignJobs();
        writeResponse();
        out.close();
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    static class Worker implements Comparator<Worker> {
        public int id;
        public long next_free_time;

        Worker(int id) {
            this.id = id;
            this.next_free_time = 0;
        }

        @Override
        public int compare(Worker worker1, Worker worker2) {
            if (worker1.next_free_time ==  worker2.next_free_time) {
                return Integer.compare(worker1.id, worker2.id);
            } else return Long.compare(worker1.next_free_time, worker2.next_free_time);
        }
    }
}