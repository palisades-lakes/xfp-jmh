package xfp.jmh.scripts;

import xfp.java.accumulators.Accumulator;
import xfp.java.prng.Generator;
import xfp.java.test.Common;
import xfp.jmh.accumulators.ZhuHayesNoBranch;

/** Profile accumulators.
 * 
 * <pre>
 * jy --source 11 src/scripts/java/xfp/jmh/scripts/ZhuHayesProfile.java
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-10
 */
@SuppressWarnings("unchecked")
public final class ZhuHayesProfile {

  //--------------------------------------------------------------

  public static final void main (final String[] args) {
    final int dim = (32*1024*1024) + 1;
    final int trys = 128;
    final Accumulator a = ZhuHayesNoBranch.make();
    assert a.isExact();
    for (final Generator g : Common.zeroSumGenerators(dim)) {
      System.out.println();
      System.out.println(g.name());
      final double[] x = (double[]) g.next();
      final long t = System.nanoTime();
      for (int i=0;i<trys;i++) {
        a.clear();
        a.addAll(x);
        final double z = a.doubleValue();
        if (0.0 != z) {
          System.out.println(Double.toHexString(0.0) 
            + " != " + Double.toHexString(z)); } }
    System.out.printf("total secs: %8.2f\n",
      Double.valueOf((System.nanoTime()-t)*1.0e-9)); } }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------