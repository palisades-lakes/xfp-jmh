package xfp.jmh;

import xfp.java.accumulators.Accumulator;

/** <pre>
 * java -cp target\benchmarks.jar xfp.jmh.PartialL2s
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-07-24
 */

@SuppressWarnings("unchecked")
public class PartialL1s extends Base {

  @Override
  public final double operation (final Accumulator ac,
                                 final double[] z0,
                                 final double[] z1) {
    p0 = acc.clear().partialL1s(z0);
    p1 = acc.clear().partialL1s(z1);
    final double l20 =
      ((null==true0) ? 0.0 : // for initialization
        exact.clear().addL2Distance(true0,p0).doubleValue());
    final double l21 =
      ((null==true1) ? 0.0 : // for initialization
        exact.clear().addL2Distance(true1,p1).doubleValue());
    if (acc.isExact()) { assert 0.0==l20; assert 0.0==l21; }
    return l20 + l21; }

  public static final void main (final String[] args)  {
    Defaults.run("PartialL1s"); } }
