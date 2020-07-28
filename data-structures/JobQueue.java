import java.io.*;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

/**
 * n independent threads to process the given list of
 * m jobs.
 * Threads take jobs in the order they are given in the input.
 * If there is a free thread, it immediately takes the next job from the list.
 * If a thread has started processing a job, it doesn’t interrupt or stop until it finishes processing the job.
 * If several threads try to take jobs from the list simultaneously, the thread with smaller index takes the job.
 * For each job you know exactly how long will it take any thread to process this job,
 * and this time is the same for all the threads.
 * You need to determine for each job which thread will process it and when will it start processing.
 * The second line contains 𝑚 integers
 * Ti — the times in seconds it takes to process the ith job.
 * The times are given in the same order as they are in the list from which threads take jobs.
 * Threads are indexed starting from 0.
 * Output Format. Output exactly 𝑚 lines. 𝑖-th line (0-based index is used) should contain two spaceseparated
 * integers — the 0-based index of the thread which will process the 𝑖-th job and the time
 * in seconds when it will start processing that job.
 */
public class JobQueue {
    private int numWorkers;
    private int[] jobs;

    private int[] assignedWorker;
    private long[] startTime;

    private FastScanner in;
    private PrintWriter out;
    PriorityQueue<Worker> workers = new PriorityQueue<>();
    PriorityQueue<Job> jobQueue = new PriorityQueue<>();

    public static void main(String[] args) throws IOException {
        new JobQueue().solve();
    }

    private int readData() throws IOException {
        numWorkers = in.nextInt();
        int numberofJobs = in.nextInt();
        jobs = new int[numberofJobs];
        for (int i = 0; i < numberofJobs; ++i) {
            jobs[i] = in.nextInt();
            Job job = new Job(i, jobs[i]);
            jobQueue.offer(job);
        }
        return numWorkers;
    }

    private void writeResponse() {
        for (int i = 0; i < jobs.length; ++i) {
            out.println(assignedWorker[i] + " " + startTime[i]);
        }
    }

    private void assignJobs(int numWorkers) {
        // TODO: replace this code with a faster algorithm.

        for (int i = 0; i < numWorkers; i++) {
            Worker worker = new Worker(i);
            workers.offer(worker);
        }

        int clock = 0;
        while (!jobQueue.isEmpty()) {
            Worker currentWorker = workers.poll();

            Job currentJob = jobQueue.poll();
            if(currentJob.duration)
            currentWorker.setJob(currentJob);
        }

        assignedWorker = new int[jobs.length];

        startTime = new long[jobs.length];
        long[] nextFreeTime = new long[numWorkers];
        for (int i = 0; i < jobs.length; i++) {
            int duration = jobs[i];
            int bestWorker = 0;
            for (int j = 0; j < numWorkers; ++j) {
                if (nextFreeTime[j] < nextFreeTime[bestWorker])
                    bestWorker = j;
            }
            assignedWorker[i] = bestWorker;
            startTime[i] = nextFreeTime[bestWorker];
            nextFreeTime[bestWorker] += duration;
        }
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        assignJobs(readData());
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

    static class Worker implements Comparable<Worker> {
        final int id;
        Job job;

        public Worker(int id) {
            this.id = id;
        }

        public void setJob(Job job) {
            this.job = job;
        }

        @Override
        public int compareTo(Worker worker) {
            return worker.id - id;
        }
    }

    static class Job implements Comparable<Job> {
        final int id;
        final int duration;

        public Job(int id, int duration) {
            this.id = id;
            this.duration = duration;
        }

        @Override
        public int compareTo(Job job) {

            return job.id - id;
        }
    }

    static class Priority implements Comparable<Priority> {
        final int order;
        final int time;

        public Priority(int order, int time) {
            this.order = order;
            this.time = time;
        }

        @Override
        public int compareTo(Priority o) {
            return 0;
        }
    }
}
