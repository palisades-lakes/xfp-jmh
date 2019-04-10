package xfp.jmh.accumulators;

import java.util.Arrays;

import xfp.java.accumulators.Accumulator;
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

public abstract class ZhuHayes implements Accumulator<ZhuHayes> {

  //--------------------------------------------------------------

  public static final int NADDS =
    1 << (Doubles.SIGNIFICAND_BITS / 2);

  public static final int NACCUMULATORS =
    1 << Doubles.EXPONENT_BITS;

  //--------------------------------------------------------------

  protected int i;
  protected double[] a1;
  protected double[] a2;
  //  private double[] b1v;
  //  private double[] b2v;
  private final double[] ifastinput;

  //--------------------------------------------------------------

  protected double sumTwo = Double.NaN;
  protected double errTwo = Double.NaN;

  /** Update {@link #sumTwo} and {@link #errTwo} so that
   * <code>{@link #sumTwo} == x0 + x1</code> 
   * (sum rounded to nearest double), and
   * <code>rationalSum({@link #sumTwo},{@link #errTwo}) 
   * == rationalSum(x0,x1)</code> 
   * (exact sums, implemented, for example, with arbitrary
   * precision rationals)
   */

  protected abstract void twoSum (final double x0,
                                  final double x1);

  //--------------------------------------------------------------

  private final int compact () {
    //    Debug.DEBUG = true;
    //    Debug.println("compact: " + i);
    //    final double v0 = doubleValue();
    // Step 4(6)(a)

    // new arrays
    final double[] b1 = new double[NACCUMULATORS];
    final double[] b2 = new double[NACCUMULATORS];

    // preallocated
    //assert (b1v.length == NACCUMULATORS);
    //assert (b2v.length == NACCUMULATORS);
    //Arrays.fill(b1v,0.0);
    //Arrays.fill(b2v,0.0);

    // Step 4(6)(b)
    for (final double x : a1) {
      // Step 4(6)(b)(i)
      final int j = Doubles.biasedExponent(x);
      // These accesses are guaranteed to be within bounds,if:
      assert (0 <= j);
      assert (j < NACCUMULATORS);
      // Step 4(6)(b)(ii)
      twoSum(b1[j],x);
      b1[j] = sumTwo;
      // Step 4(6)(b)(iii)
      b2[j] += errTwo; }
    for (final double x : a2) {
      // Step 4(6)(b)(i)
      final int j = Doubles.biasedExponent(x);
      // These accesses are guaranteed to be within bounds,if:
      assert (0 <= j);
      assert (j < NACCUMULATORS);
      // Step 4(6)(b)(ii)
      twoSum(b1[j],x);
      b1[j] = sumTwo;
      // Step 4(6)(b)(iii)
      b2[j] += errTwo; }

    // Step 4(6)(c)
    // new arrays
    a1 = b1;
    a2 = b2;
    // preallocated
    // swap instead of gc
    //final double[] tmp1 = a1;
    //final double[] tmp2 = a2;
    //a1 = b1v;
    //a2 = b2v;
    //b1v = tmp1;
    //b2v = tmp2;
    //Arrays.fill(b1v,0.0);
    //Arrays.fill(b2v,0.0);

    //    Debug.DEBUG = false;
    //    final double v1 = doubleValue();
    //    assert v0 == v1 :
    //      "compact changed value:"
    //      + "\nbefore: " + Double.toHexString(v0)
    //      + "\nafter:  " + Double.toHexString(v1);
    //    Debug.println("\nbefore: " + Double.toHexString(v0));
    //    Debug.println("\nafter:  " + Double.toHexString(v1));

    // Step 4(6)(d)
    return 2 * NACCUMULATORS; }

  //--------------------------------------------------------------
  // Accumulator interface
  //--------------------------------------------------------------

  @Override
  public final boolean isExact () { return true; }

  @Override
  public final boolean noOverflow () { return false; }

 //--------------------------------------------------------------
  // aka zero()

  @Override
  public final ZhuHayes clear () {
    i = 0;
    Arrays.fill(a1,0.0);
    Arrays.fill(a2,0.0);
    // preallocated
    //Arrays.fill(b1v,0.0);
    //Arrays.fill(b2v,0.0); 
    Arrays.fill(ifastinput,0.0);
    return this; }

  //--------------------------------------------------------------

  @Override
  public final double doubleValue () {

    // Step 5
    // TODO: is it worth dropping zeros?
    //a.retain(|&x| x != F::zero());

    System.arraycopy(a1,0,ifastinput,0,a1.length);
    System.arraycopy(a2,0,ifastinput,a1.length,a2.length);

    // Step 6
    //return RBFAccumulator.make().addAll(ifastinput).doubleValue(); }
    return IFastSum.make().destructiveSum(ifastinput); }

  //--------------------------------------------------------------

  @Override
  public final ZhuHayes add (final double x) {

    assert Double.isFinite(x);

    // Step 4(2)
    final int j = Doubles.biasedExponent(x);
    // These accesses are guaranteed to be within bounds, because:
    assert (0 <= j);
    assert (j < NACCUMULATORS);

    // Step 4(3)
    twoSum(a1[j],x);
    a1[j] = sumTwo;

    // Step 4(4)
    a2[j] += errTwo;

    // Step 4(5)
    // This addition is guaranteed not to overflow because the
    // next step ascertains that (at this point):
    assert (i < NADDS) : i + " >= " + NADDS;
    i += 1;

    // Step 4(6)
    if (i >= NADDS) { i = compact(); } 
    return this; }

  //--------------------------------------------------------------

  @Override
  public final ZhuHayes add2 (final double x) {
    assert Double.isFinite(x);

    final double x2 = x*x;
    final double e = Math.fma(x,x,-x2);
    add(x2);
    add(e); 
    return this; }

  //--------------------------------------------------------------

  @Override
  public final ZhuHayes addProduct (final double x0,
                                    final double x1) {
    assert Double.isFinite(x0);
    assert Double.isFinite(x1);

    final double x01 = x0*x1;
    final double e = Math.fma(x0,x1,-x01);
    add(x01);
    add(e); 
    return this; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  public ZhuHayes () {
    i = 0;
    a1 = new double[NACCUMULATORS];
    a2 = new double[NACCUMULATORS];
    Arrays.fill(a1,0.0);
    Arrays.fill(a2,0.0);
    //    b1v = new double[NACCUMULATORS];
    //    b2v = new double[NACCUMULATORS];
    //    Arrays.fill(b1v,0.0);
    //    Arrays.fill(b2v,0.0);
    ifastinput = new double[2*NACCUMULATORS];
    Arrays.fill(ifastinput,0.0); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
