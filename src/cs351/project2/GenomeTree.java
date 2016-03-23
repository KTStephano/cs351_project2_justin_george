package cs351.project2;

import cs351.core.Genome;
import cs351.core.Mutator;
import cs351.core.Tribe;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Implements the Tribe interface using a tree.
 */
public class GenomeTree implements Tribe
{
  private final HashSet<Genome> UNSORTED_GENOMES = new HashSet<>(2000);
  private final TreeSet<Genome> GENOME_TREE;

  {
    GENOME_TREE = new TreeSet<>((first, second) -> -1 * Double.compare(first.getFitness(), second.getFitness()));
  }

  @Override
  public Mutator getMutatorForGenome(Genome genome) throws RuntimeException
  {
    throw new RuntimeException("getMutatorForGenome() not finished");
  }

  @Override
  public void add(Genome genome)
  {
    if (!contains(genome))
    {
      UNSORTED_GENOMES.add(genome);
      GENOME_TREE.add(genome);
      //System.out.println(GENOME_TREE.first().getFitness());
    }
  }

  @Override
  public void remove(Genome genome)
  {
    if (contains(genome))
    {
      UNSORTED_GENOMES.remove(genome);
      GENOME_TREE.remove(genome);
    }
  }

  @Override
  public void clear()
  {
    UNSORTED_GENOMES.clear();
    GENOME_TREE.clear();
  }

  @Override
  public boolean contains(Genome genome)
  {
    return UNSORTED_GENOMES.contains(genome);
  }

  @Override
  public int size()
  {
    return UNSORTED_GENOMES.size();
  }

  @Override
  public Genome getBest()
  {
    return GENOME_TREE.first();
  }

  @Override
  public Collection<Genome> getGenomes()
  {
    return GENOME_TREE;
  }

  @Override
  public void recalculate()
  {
    GENOME_TREE.clear();
    GENOME_TREE.addAll(UNSORTED_GENOMES);
  }
}