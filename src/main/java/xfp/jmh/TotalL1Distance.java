package xfp.jmh;

import xfp.java.accumulators.Accumulator;

/** <pre>
 * java -cp target\benchmarks.jar xfp.jmh.TotalL1Distance
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-07-27
 */

public class TotalL1Distance extends Base {

  @Override
  public final double[] operation (final Accumulator ac,
                                   final double[] z0,
                                   final double[] z1) {
    return new double[] 
      { ac.clear().addL1Distance(z0,z1).doubleValue() }; }

  public static final void main (final String[] args)  {
    Defaults.run("TotalL1Distance"); } }
