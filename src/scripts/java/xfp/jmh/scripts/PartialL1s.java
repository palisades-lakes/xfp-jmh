package xfp.jmh.scripts;

import xfp.java.accumulators.Accumulator;
import xfp.java.prng.Generator;
import xfp.java.prng.Generators;
import xfp.jmh.accumulators.BigFloatAccumulator0;

/** Benchmark partial L1 norms
 *
 * <pre>
 * jy --source 12 src/scripts/java/xfp/jmh/scripts/PartialL1s.java
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-09-09
 */
@SuppressWarnings("unchecked")
public final class PartialL1s {

  public static final void main (final String[] args) {
    final int dim = (1*1024*1024) - 1;
    final int trys = 8 * 1024;
    //final Generator g = Generators.make("exponential",dim);
    //final Generator g = Generators.make("finite",dim);
    final Generator g = Generators.make("gaussian",dim);
    //final Generator g = Generators.make("laplace",dim);
    //final Generator g = Generators.make("uniform",dim);
    final Accumulator a = BigFloatAccumulator0.make();
    assert a.isExact();
    for (int i=0;i<trys;i++) {
      final double[] x = (double[]) g.next();
      final double[] z = a.partialL1s(x); 
      assert ! Double.isNaN(z[dim-1]);} }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
