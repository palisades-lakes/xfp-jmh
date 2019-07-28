package xfp.jmh;

import xfp.java.accumulators.Accumulator;

/** <pre>
 * java -cp target\benchmarks.jar xfp.jmh.PartialL1Distances
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-07-27
 */

@SuppressWarnings("unchecked")
public class PartialL2Distances extends Base {

  @Override
  public final double[] operation (final Accumulator ac,
                                   final double[] z0,
                                   final double[] z1) {
    return ac.clear().partialL2Distances(z0,z1); }

  public static final void main (final String[] args)  {
    Defaults.run("PartialL1Distances"); } }
