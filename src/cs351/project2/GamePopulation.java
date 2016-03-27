package cs351.project2;

import cs351.core.*;
import cs351.core.Engine.EvolutionEngine;
import cs351.core.Engine.GUI;
import cs351.core.Engine.Population;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * GamePopulation is called by EvolutionLoop. This class is used to manage the tribes, which in turn manages objects
 * that fall within its hierarchy.
 */
public class GamePopulation implements Population
{
  private int numTribes = 16; // Default value of tribes in a population
  private int numGenomes = 2000;  // Default value of starting genomes per tribe
  private int numTriangles = 200; // Default value of starting triangles per genome

  private Random numGenerator; // initialized once to be used when creating initial triangle vertices
  private Collection<Tribe> tribesCollection; // holds tribes that make up the population


  /**
   * Gets the fitness function that the population wants to be used
   * for all genomes.
   *
   * @return active fitness function
   */
  @Override
  public FitnessFunction getFitnessFunction()
  {
    return null;
  }

  /**
   * Gets the active cross object being used for genomes that are
   * apart of the given population.
   *
   * @return Cross object
   */
  @Override
  public Cross getCrossObject()
  {
    return null;
  }

  /**
   * Returns the selector that is being used to perform random genome selections.
   *
   * @return selector object
   */
  @Override
  public Selector getSelector()
  {
    return null;
  }

  /**
   * Returns the working tribes for the population.
   *
   * @return working tribe
   */
  @Override
  public Collection<Tribe> getTribes()
  {
    return tribesCollection;
  }

  /**
   * This function should clear the existing tribe and reinitialize it
   * to some starting state. Along with this, a fitness function should be generated
   * in order to be used not only by this class, but by all other classes that
   * want to create genomes.
   *
   * @param engine    engine object for callbacks
   * @param numTribes number of tribes to initialize
   */
  @Override
  public void generateStartingState(EvolutionEngine engine, int numTribes)
  {
    this.numTribes = numTribes;
    tribesCollection = new ArrayList<>();
    numGenerator = new Random();


    // Initialize tribes, and then add it to the tribe collection
    for(int i = 0; i < numTribes; i++)
    {
      Tribe tribe = new GenomeTree();
      System.out.println(">>>>>>>>>>>>> Tribe: " + i);

      // For each tribe, initialize and add specified number of genomes to that tribe
      for(int j = 0; j < numGenomes; j++)
      {
        // Initialize genome, and give it a specified amount of new triangles. Remember that a triangle is a part of a genome.
        // A genome will also have a fitness level, however that will be calculated within the recalulate method
        // within tribe.
        Genome genome = new Genome();
        for(int k = 0; k < numTriangles; k++)
        {
          Triangle triangle = new GameTriangle();
          triangle.init(numGenerator, engine.getGUI());
          genome.add(triangle);
        }


        // Add completed genome to tribe
        tribe.add(genome);
      }

      // Once current tribe has had all of its genomes added, organize them by fitness
      tribe.recalculate();

      // Tribe is good to go, and now should be added to the tribe collection
      tribesCollection.add(tribe);
    }
  }

  /**
   * This should only ever be called by an evolution engine. The purpose is to allow
   * the population to make sure that the Tribe it maintains does not contain
   * genomes that the population doesn't know about. This will only happen is another
   * part of the simulation directly added to the population's tribe without
   * running it through the population first.
   * <p>
   * The reason this is important is because the population won't be able to assign
   * mutator objects to genomes it doesn't know about.
   *
   * @throws IllegalStateException thrown if the Tribe and population have fallen out of sync
   */
  @Override
  public void validatePopulationData() throws IllegalStateException
  {

  }
}
