package xfp.jmh.accumulators;

import static xfp.java.numbers.Doubles.biasedExponent;

import xfp.java.numbers.Doubles;

//----------------------------------------------------------------
/** <em>NOT</em> thread safe!
 * <p>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-10
 */

public final class ZhuHayesBranch extends ZhuHayesGC {

  @Override
  protected final void twoSum (final double x0, 
                               final double x1) {
    // might get +/- Infinity due to overflow
    sumTwo = x0 + x1;
    if (Doubles.biasedExponent(x0) > Doubles.biasedExponent(x1)) {
      errTwo = x1 - (sumTwo - x0); }
    else {
      errTwo = x0 - (sumTwo - x1); } }

//  @Override
//  protected final void twoInc (final double[] s, 
//                               final double[] e, 
//                               final int j,
//                               final double x) {
//    // might get +/- Infinity due to overflow
//    final double sj = s[j];
//    s[j] = sj + x;
//    if (Doubles.biasedExponent(sj) > Doubles.biasedExponent(x)) {
//      e[j] += x - (s[j] - sj); }
//    else {
//      e[j] += sj - (s[j] - x); } }

  @Override
  protected final void twoInc (final double[] s, 
                               final double[] e, 
                               final double x) {
    // might get +/- Infinity due to overflow
    final int j = biasedExponent(x);
    final double sj = s[j];
    s[j] = sj + x;
    if (biasedExponent(sj) > biasedExponent(x)) {
      e[j] += x - (s[j] - sj); }
    else {
      e[j] += sj - (s[j] - x); } }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private ZhuHayesBranch () { super(); }

  public static final ZhuHayesBranch make () {
    return new ZhuHayesBranch(); }

  //--------------------------------------------------------------
} // end of class
//----------------------------------------------------------------
