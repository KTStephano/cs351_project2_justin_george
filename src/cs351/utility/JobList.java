package cs351.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Note :: This class is not safe to be used by multiple threads at once. It is
 *         better to create segregated lists for each thread and let the job
 *         system deal with managing jobs concurrently.
 *
 * This class can be used to manage local copies of a job list
 * before submitting them to the job system. The benefit to this is
 * that you can build up a list of jobs to be executed but have strict
 * control over when the job system gets to see them.
 *
 * Each list is capable of managing various layers of different-priority job lists.
 *
 * Keep in mind that once you tell the list to submit the jobs it is managing,
 * it will submit all jobs to the job system for immediate execution and clear
 * its local list. This includes all job lists for all priorities.
 */
public class JobList
{
  private final ParallelJobSystem JOB_SYSTEM;
  private final HashMap<Integer, LinkedList<Job>> JOBS;
  private final ArrayList<AtomicInteger> ACTIVE_COUNTERS;
  private int size = 0;

  public JobList(ParallelJobSystem jobSystem)
  {
    JOB_SYSTEM = jobSystem;
    JOBS = new HashMap<>();
    ACTIVE_COUNTERS = new ArrayList<>(10);
  }

  /**
   * Adds a job of the given priority to the list. A job list will separate its jobs
   * by priority, but when submit is called, all jobs of all priorities are submitted
   * and the job system is notified of which job lists are of what priority.
   *
   * @param job job to submit
   * @param priority priority of the job (determines the list it goes in)
   */
  public void add(Job job, int priority)
  {
    if (!JOBS.containsKey(priority)) JOBS.put(priority, new LinkedList<>());
    JOBS.get(priority).add(job);
    size++;
  }

  /**
   * Removes a job from the list. This should only be called before submitting since
   * once submit is called, all job lists are wiped.
   *
   * @param job job to remove
   * @param priority its priority (used to find it)
   */
  public void remove(Job job, int priority)
  {
    if (!JOBS.containsKey(priority)) return;
    JOBS.get(priority).remove(job);
    size--;
  }

  /**
   * Gets the size of all job lists that it is maintaining.
   *
   * @return size of all job lists combined
   */
  public int size()
  {
    return size;
  }

  /**
   * Submits all jobs to the given job system. All lists are wiped afterwards
   * so future calls to submitJobs will have no effect unless jobs are
   * re-added.
   */
  public void submitJobs()
  {
    LinkedList<LinkedList<Job>> pendingDeletion = new LinkedList<>();
    for (Map.Entry<Integer, LinkedList<Job>> entry : JOBS.entrySet())
    {
      ACTIVE_COUNTERS.add(JOB_SYSTEM.submit(entry.getValue(), entry.getKey()));
      pendingDeletion.add(entry.getValue());
    }
    // Clear out each list
    for (LinkedList<Job> jobList : pendingDeletion) jobList.clear();
    // Reset the size
    size = 0;
    //System.out.println("Here I am");
  }

  /**
   * Returns only when the job system has completed all jobs that were associated with
   * this list before being submitted.
   */
  public void waitForCompletion()
  {
    try
    {
      boolean isComplete = false;
      while (!isComplete)
      {
        isComplete = true;
        for (AtomicInteger counter : ACTIVE_COUNTERS)
        {
          if (counter.get() > 0)
          {
            isComplete = false;
            Thread.sleep(1);
            break;
          }
        }
      }
    }
    catch (InterruptedException e)
    {
      // Do nothing
    }
  }
}