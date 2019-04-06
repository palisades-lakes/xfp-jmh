package xfp.jmh.accumulators;

import java.util.Arrays;

import xfp.java.numbers.Double2;
import xfp.java.numbers.Doubles;

//----------------------------------------------------------------
/** Iterative-refinement fast correctly rounded sums: exact real
 * sum rounded to nearest double.
 * <p>
 * Primary references:
 * <p>
 * <a href="http://dl.acm.org/citation.cfm?id=1824815">
 * Yong Kang Zhu and Wayne B. Hayes,
 * "Algorithm 908: Online Exact Summation of Floating-Point Streams",
 * ACM TOMS Volume 37 Issue 3, September 2010</a>
 * <p>
 * <a href="https://macsphere.mcmaster.ca/bitstream/11375/9258/1/fulltext.pdf">
 *  Yuhang Zhao, "Some Highly Accurate Basic Linear Algebra Subroutines",
 *  MS Thesis, McMaster U , Computing and Software, 2010.</a>S
 * <p>
 * <a href="http://epubs.siam.org/doi/abs/10.1137/070710020?journalCode=sjoce3" >
 * Yong Kang Zhu and Wayne B. Hayes,
 * "Correct Rounding and a Hybrid Approach to Exact Floating-Point Summation,"
 * SIAM J. Sci. Comput.,  31 (4), p 2981–3001, Jun 2009. (21 pages)</a>
 * <p>
 * This implementation based on:
 * <p>
 * <a href="https://github.com/bsteinb/accurate/blob/master/src/sum/ifastsum.rs">
 * Benedikt Steinbusch,
 * "(More or less) accurate floating point algorithms"</a>
 * (Apache 2.0 or MIT license, visited 2017-05-01)
 * <p>
 * Other implementations:
 * <p>
 * <a href="https://github.com/Jeffrey-Sarnoff/IFastSum.jl" >
 * Jeffery Sarnoff,
 * IFastSum.jl</a> (visited 2017-05-01, MIT License)
 * <p>
 * <a href="https://github.com/aseldawy/sumn" >
 *  Ahmed Eldawy, Sumn
 * </a>
 * (visited 2017-05-01, Apache 2.0 License)
 * <p>
 * <em>NOT</em> thread safe!
 * <p>
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-06
 */

public final class IFastSum {

  //--------------------------------------------------------------

  private static final double destructiveSum (final double[] x,
                                              final int[] n,
                                              final boolean recurse) {
    // assert 1 == n.length;

    // Step 1
    double s = 0.0;
    final Double2 add2 = new Double2();

    // Step 2
    // The following accesses are guaranteed to be inside bounds, because:
    //assert (n[0] <= x.length);
    for (int i=0;i<n[0]; i++) {
      //      System.out.println(i + ": " + Double.toHexString(s) + ", "
      //        + Double.toHexString(x[i]));
      add2.zhuHayesNoBranch(s,x[i]); s = add2.z0; x[i] = add2.z1;
      //      System.out.println(i + ": " + Double.toHexString(s) + ", "
      //        + Double.toHexString(x[i]));
    }
    //System.out.println("step 2: " + s);
    // Step 3
    while (true) {
      //for (int iloop=0;true;iloop++) {
      // Step 3(1)
      int count = 0; // slices are indexed from 0
      double st = 0.0;
      double sm = 0.0;
      // Step 3(2)
      // The following accesses are guaranteed to be inside bounds, because:
      //assert (n[0] <= x.length);
      for (int i=0;i<n[0];i++) {
        // Step 3(2)(a)
        add2.zhuHayesNoBranch(st, x[i]);
        st = add2.z0;
        final double b = add2.z1;
        // Step 3(2)(b)
        if (0.0 != b) {
          // The following access is guaranteed to be inside bounds, because:
          //assert (count < x.length);
          x[count] = b;
          // Step 3(2)(b)(i)
          // count += 1;
          // The following addition is guaranteed not to overflow:
          count = Math.addExact(count,1);
          // Step 3(2)(b)(ii)
          sm = Math.max(sm,Math.abs(st)); } }
      // Step 3(3)
      //      System.out.println("Step 3(3): " + iloop +
      //                         ", count=" + count);
      final double dcount = count;
      // test count exact as double
      //assert count == (int) dcount;
      final double em = dcount * Doubles.halfUlp(sm);
      // Step 3(4)
      add2.zhuHayesNoBranch(s, st);
      s = add2.z0;
      st = add2.z1;
      // The following access is guaranteed to be inside bounds, because:
      //assert (count < x.length) : count + ">=" + x.length;
      x[count] = st;
      // The following addition is guaranteed not to overflow, because:
      //assert (count < Integer.MAX_VALUE);
      // and thus:
      //debug_assert!(count.checked_add(1).is_some());
      n[0] = Math.addExact(count,1);
      // Step 3(5)
      //      System.out.println("Step 3(5): "
      //      + "em=" + Double.toHexString(em)
      //      + ", s=" + Double.toHexString(s)
      //      + ", halfUlp=" + Double.toHexString(Doubles.halfUlp(s)));

      if ((em == 0.0) || (em < Doubles.halfUlp(s))) {
        // Step 3(5)(a)
        if (! recurse) { return s; }
        // Step 3(5)(b)
        add2.zhuHayesNoBranch(st, em);
        final double w1 = add2.z0;
        final double e1 = add2.z1;
        // Step 3(5)(c)
        add2.zhuHayesNoBranch(st, -em);
        final double w2 = add2.z0;
        final double e2 = add2.z1;
        // Step 3(5)(d)
        if (((w1 + s) != s)
          || ((w2 + s) != s)
          || (Doubles.round3(s, w1, e1) != s)
          || (Doubles.round3(s, w2, e2) != s)) {
          // Step 3(5)(d)(i)
          double s1 = destructiveSum(x, n, false);
          // Step 3(5)(d)(ii)
          add2.zhuHayesNoBranch(s, s1);
          s = add2.z0;
          s1 = add2.z1;
          // Step 3(5)(d)(iii)
          final double s2 = destructiveSum(x, n, false);
          // Step 3(5)(d)(iv)
          s = Doubles.round3(s, s1, s2); }
        // Step 3(5)(e)
        // System.out.println("step 3(5)e: " + s);
        return s; } } }

  //--------------------------------------------------------------

  public static final double destructiveSum (final double[] x) {
    final int[] n = new int[1];
    n[0] = x.length;
    return destructiveSum(x,n,true); }

  //--------------------------------------------------------------

  public static final double sum (final double[] x) {
    return destructiveSum(Arrays.copyOf(x,x.length)); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private IFastSum () {
    throw new UnsupportedOperationException("can't instantiate" +
      getClass()); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
