package cs351.core;

import cs351.utility.Job;

import java.util.Collection;

/**
 * An evolution engine sets the stage for the entire process
 * of the mutation/evolution of a population. As part of its initialization,
 * it should take the file path to a target image which it will load
 * and translate the image data into a target genome. It should also
 * drive the seeding of the initial population and initialization of any
 * other critical systems to be used during the process.
 *
 * An external source should be responsible for calling its nextGeneration()
 * function, which should perform all necessary steps to move from the current
 * generation to the next and then signal the GUI when it is a good and thread-safe
 * time to update.
 */
public interface EvolutionEngine
{
  /**
   * Initializes the engine with the given image file (also calls the renderer's
   * init function at a good point during the engine's init sequence).
   *
   * @param imageFile image file
   * @param renderer renderer to use
   */
  void init(String imageFile, Renderer renderer);

  /**
   * Gets the number of generations that have passed since the engine was initialized.
   *
   * @return number of generations
   */
  int getGenerationCount();

  /**
   * The engine may have any number of jobs running as part of a generation,
   * and this is an easy way for jobs to let the engine know when they are done
   * so it knows when the generation is complete.
   *
   * @param job job that just completed
   */
  void notifyEngineOfJobCompletion(Job job);

  /**
   * Gets the main tribe managed by the engine.
   *
   * @return tribe
   */
  Tribe getTribe();

  /**
   * Returns the target genome that was generated during initialization.
   *
   * @return target genome
   */
  Genome getTarget();

  /**
   * Creates the next generation of the given population.
   */
  void nextGeneration();
}
