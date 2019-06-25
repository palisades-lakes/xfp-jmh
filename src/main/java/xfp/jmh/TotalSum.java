package xfp.jmh;

import xfp.java.accumulators.Accumulator;

/** <pre>
 * java -cp target\benchmarks.jar xfp.jmh.TotalSum
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-06-13
 */

public class TotalSum extends Base {

  @Override
  public final double operation (final Accumulator ac,
                                 final double[] z0,
                                 final double[] z1) {
    return ac.clear().addAll(z0).addAll(z1).doubleValue(); }

  public static final void main (final String[] args)  {
    Defaults.run("TotalSum"); } }
