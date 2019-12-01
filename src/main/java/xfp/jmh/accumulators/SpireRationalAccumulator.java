package xfp.jmh.accumulators;

import spire.math.Rational;
import xfp.java.accumulators.ExactAccumulator;
import xfp.jmh.numbers.SpireRationals;

/** Naive sum of <code>double</code> values with Rational
 * accumulator (for testing).
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-11-30
 */
public final class SpireRationalAccumulator

extends ExactAccumulator<SpireRationalAccumulator> {

  private Rational _sum;

  //--------------------------------------------------------------

  //  private static final int loBit (final SafeLong i) {
  //    return i.GetLowBitAsSafeLong().ToInt32Checked(); }
  //
  //  private static final Rational reduce (final Rational q) {
  //    final SafeLong n0 = q.numerator();
  //    final SafeLong d0 = q.denominator();
  //    final int e = Math.min(loBit(n0),loBit(d0));
  //    final SafeLong n1 = n0.$greater$greater(e);
  //    final SafeLong d1 = d0.$greater$greater(e);
  //    final SafeLong gcd = n1.gcd(d1);
  //    //if (SafeLong.getOne().equals(gcd)) { return q; }
  //    final SafeLong n2 = n1.$div$tilde(gcd);
  //    final SafeLong d2 = d1.$div$tilde(gcd);
  //    return SpireRationals.toRational(n2,d2); }

  //--------------------------------------------------------------

  @Override
  public final boolean noOverflow () { return true; }

  @Override
  public final Object value () { return _sum; }

  @Override
  public final double doubleValue () { 
    return _sum.doubleValue(); }

  @Override
  public final SpireRationalAccumulator clear () {
    _sum = SpireRationals.ZERO;
    return this; }

  @Override
  public final SpireRationalAccumulator add (final double z) {
    //assert Double.isFinite(z);
    _sum = _sum.$plus(SpireRationals.toRational(z));
    return this; }

  @Override
  public final SpireRationalAccumulator add2 (final double z) {
    //assert Double.isFinite(z);
    final Rational zz = SpireRationals.toRational(z);
    _sum = _sum.$plus(zz.$times(zz));
    return this; }

  @Override
  public final SpireRationalAccumulator addL2 (final double z0,
                                               final double z1) {
    //assert Double.isFinite(z0);
    //assert Double.isFinite(z1);
    final Rational zz0 = SpireRationals.toRational(z0);
    final Rational zz1 = SpireRationals.toRational(z1);
    final Rational dz = zz0.$minus(zz1);
    final Rational dz2 = dz.$times(dz);
    _sum = _sum.$plus(dz2);
    return this; }

  @Override
  public final SpireRationalAccumulator addProduct (final double z0,
                                                    final double z1) {
    //assert Double.isFinite(z0);
    //assert Double.isFinite(z1);
    _sum = _sum.$plus(
      SpireRationals.toRational(z0)
      .$times(SpireRationals.toRational(z1)));
    return this; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  public static final String toHexString (final Rational q) {
    return
      "(" + q.numerator().toString()
      + " / "
      //      + "\n/\n"
      + q.denominator().toString() + ")"; }

  //--------------------------------------------------------------

  @Override
  public final String toString () { return toHexString(_sum); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private SpireRationalAccumulator () { super(); clear(); }

  public static final SpireRationalAccumulator make () {
    return new SpireRationalAccumulator(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
