package xfp.jmh.accumulators;

import java.util.Arrays;

import xfp.java.accumulators.Accumulator;
import xfp.java.numbers.Double2;
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

public abstract class ZhuHayesOnlineExact
implements Accumulator<ZhuHayesOnlineExact> {

  //--------------------------------------------------------------

  public static final int NADDS =
    1 << (Doubles.SIGNIFICAND_BITS / 2);

  public static final int NACCUMULATORS =
    1 << Doubles.EXPONENT_BITS;

  //--------------------------------------------------------------

  protected int i;
  protected double[] a1;
  protected double[] a2;
  private double[] b1v;
  private double[] b2v;
  private final double[] ifastinput;
  protected final Double2 add2 = new Double2();

  //--------------------------------------------------------------

  public final void compact () {
    // preallocated
    // Step 4(6)(a)
    //final double[] b1v = new double[NACCUMULATORS];
    //final double[] b2v = new double[NACCUMULATORS];

    // Step 4(6)(b)
    for (final double y : a2) {
      // Step 4(6)(b)(i)
      final int j = Doubles.biasedExponent(y);
      // These accesses are guaranteed to be within bounds, because:
      //assert (b1v.length == NACCUMULATORS);
      //assert (b2v.length == NACCUMULATORS);
      //assert (j < NACCUMULATORS);
      // Step 4(6)(b)(ii)
      add2.zhuHayesBranch(b1v[j],y);
      b1v[j] = add2.z0;
      // Step 4(6)(b)(iii)
      b2v[j] += add2.z1; }

    // Step 4(6)(c)
    // swap instead of gc
    final double[] tmp1 = a1;
    final double[] tmp2 = a2;
    a1 = b1v;
    a2 = b2v;
    b1v = tmp1;
    b2v = tmp2;
    Arrays.fill(b1v,0.0);
    Arrays.fill(b2v,0.0);

    // Step 4(6)(d)
    i = 2 * NACCUMULATORS; }

  //--------------------------------------------------------------
  // Accumulator interface
  //--------------------------------------------------------------

  @Override
  public final boolean isExact () { return true; }

  //--------------------------------------------------------------
  // aka zero()

  @Override
  public final ZhuHayesOnlineExact clear () {
    i = 0;
    Arrays.fill(a1,0.0);
    Arrays.fill(a2,0.0);
    Arrays.fill(b1v,0.0);
    Arrays.fill(b2v,0.0); 
    return this; }

  //--------------------------------------------------------------

  @Override
  public final double doubleValue () {

    // Step 5
    // TODO: is it worth dropping zeros?
    //a.retain(|&x| x != F::zero());

    System.arraycopy(a1,0,ifastinput,0,NACCUMULATORS);
    System.arraycopy(a2,0,ifastinput,NACCUMULATORS,NACCUMULATORS);

    // Step 6
    return IFastSum.destructiveSum(ifastinput); }

  //--------------------------------------------------------------

  @Override
  public final ZhuHayesOnlineExact add2 (final double z) {
    final double z2 = z*z;
    final double e = Math.fma(z,z,-z2);
    add(z2);
    add(e); 
    return this; }
  
  //--------------------------------------------------------------

  @Override
  public final ZhuHayesOnlineExact addProduct (final double z0,
                                               final double z1) {
    final double z01 = z0*z1;
    final double e = Math.fma(z0,z1,-z01);
    add(z01);
    add(e); 
    return this; }
  
  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  public ZhuHayesOnlineExact () {
    i = 0;
    a1 = new double[NACCUMULATORS];
    a2 = new double[NACCUMULATORS];
    b1v = new double[NACCUMULATORS];
    b2v = new double[NACCUMULATORS];
    ifastinput = new double[2*NACCUMULATORS];
    Arrays.fill(a1,0.0);
    Arrays.fill(a2,0.0);
    Arrays.fill(b1v,0.0);
    Arrays.fill(b2v,0.0);
    Arrays.fill(ifastinput,0.0); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
