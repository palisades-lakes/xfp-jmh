package xfp.jmh.accumulators;

import static xfp.java.numbers.Doubles.biasedExponent;

//----------------------------------------------------------------
/** <em>NOT</em> thread safe!
 * <p>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-10
 */

public final class ZhuHayesNoBranch extends ZhuHayesGC {

  @Override
  protected final void twoSum (final double x0, 
                               final double x1) {
    // might get +/- Infinity due to overflow
    sumTwo = x0 + x1;
    final double z = sumTwo - x0;
    errTwo = (x0 - (sumTwo - z)) + (x1 - z); }

//  @Override
//  protected void twoInc (final double[] s, 
//                         final double[] e, 
//                         final int j,
//                         final double x) {
//    // might get +/- Infinity due to overflow
//    final double sj = s[j];
//    s[j] = sj + x;
//    final double z = s[j] - sj;
//    e[j] += (sj - (s[j] - z)) + (x - z); }

  @Override
  protected void twoInc (final double[] s, 
                         final double[] e, 
                         final double x) {
    // might get +/- Infinity due to overflow
    final int j = biasedExponent(x);
    final double sj = s[j];
    s[j] = sj + x;
    final double z = s[j] - sj;
    e[j] += (sj - (s[j] - z)) + (x - z); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private ZhuHayesNoBranch () { super(); }

  public static final ZhuHayesNoBranch make () {
    return new ZhuHayesNoBranch(); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
