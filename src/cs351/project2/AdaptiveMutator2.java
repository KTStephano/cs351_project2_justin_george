package cs351.project2;

import cs351.core.*;
import cs351.core.Engine.EvolutionEngine;

import java.util.ArrayList;
import java.util.Random;

public class AdaptiveMutator2 implements Mutator
{
  private final Cross CROSS = new CrossMutate();
  private Genome genome = null;

  /**
   * Sets the genome. Should clear out all data maintained for the
   * previous genome if there is any.
   *
   * @param genome new genome
   */
  @Override
  public void setGenome(Genome genome)
  {
    this.genome = genome;
  }

  /**
   * At this point the mutator should decide on a mutation(s) and
   * perform them.
   *
   * @param function fitness function to use
   * @param engine   reference to the current engine
   */
  @Override
  public void mutate(FitnessFunction function, EvolutionEngine engine)
  {
    if (genome == null) throw new IllegalStateException("Genome is null");
    Genome offspring = CROSS.cross(engine, genome, genome);
    if (offspring.getFitness() > genome.getFitness())
    {
      genome.clear();
      for (float[] triangle : offspring.getTriangles()) genome.add(triangle);
      genome.setFitness(offspring.getFitness());
    }
  }
}
