package xfp.jmh.accumulators;

import xfp.java.numbers.Doubles;

//----------------------------------------------------------------
/** Fast exact online summation. Basic idea is to use a separate
 * accumulator for each (biased) exponent value.
 * <p>
 * Primary reference:
 * <p>
 * <a href="http://dl.acm.org/citation.cfm?id=1824815" >
 * Yong Kang Zhu and Wayne B. Hayes,
 * "Algorithm 908: Online Exact Summation of Floating-Point Streams",
 * ACM TOMS Volume 37 Issue 3, September 2010</a>
 * <p>
 * Also see:
 * <p>
 * <a href="https://macsphere.mcmaster.ca/bitstream/11375/9258/1/fulltext.pdf" >
 * Yuhang Zhao, "Some Highly Accurate Basic Linear Algebra Subroutines",
 * MS Thesis, McMaster U , Computing and Software, 2010.</a>
 * <p>
 * <a href="http://epubs.siam.org/doi/abs/10.1137/070710020?journalCode=sjoce3" >
 * Yong Kang Zhu and Wayne B. Hayes,
 * "Correct Rounding and a Hybrid Approach to Exact Floating-Point Summation,"
 * SIAM J. Sci. Comput.,  31(4), p 2981–3001, Jun 2009. (21 pages)</a>
 * <p>
 * This implementation based on the code with TOMS 908 and:
 * <p>
 * <a href="https://github.com/bsteinb/accurate/blob/master/src/sum/onlineexactsum.rs">
 * Benedikt Steinbusch,
 * "(More or less) accurate floating point algorithms"</a>
 * (Apache 2.0 or MIT license, visited 2017-05-01)
 * <p>
 * <em>NOT</em> thread safe!
 * <p>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-06
 */

public final class ZhuHayesNoBranch
extends ZhuHayes {

  //--------------------------------------------------------------
  // Accumulator interface
  //--------------------------------------------------------------

  @Override
  public final ZhuHayesNoBranch add (final double z) {
    // Step 4(2)
    final int j = Doubles.biasedExponent(z);
    // These accesses are guaranteed to be within bounds, because:
    // assert (a1.length == NACCUMULATORS);
    // assert (a2.length == NACCUMULATORS);
    // assert (j < NACCUMULATORS);
    // Step 4(3)
    add2.zhuHayesNoBranch(a1[j], z);
    a1[j] = add2.z0;
    // Step 4(4)
    a2[j] += add2.z1;

    // Step 4(5)
    // This addition is guaranteed not to overflow because the
    // next step ascertains that (at this point):
    assert (i < (NADDS/2)) : i + " < " + NADDS/2;
    // and (for `f32` and `f64`) we have:
    assert ((NADDS/2) < Integer.MAX_VALUE);
    // thus we can assume:
    assert ((i+1) <= Integer.MAX_VALUE);
    i += 1;

    // Step 4(6)
    if (i >= NADDS) { compact(); } 
    return this; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private ZhuHayesNoBranch () { super(); }

  public static final ZhuHayesNoBranch make () {
    return new ZhuHayesNoBranch(); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
