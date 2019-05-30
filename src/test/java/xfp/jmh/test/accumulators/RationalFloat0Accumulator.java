package xfp.jmh.test.accumulators;

import xfp.java.accumulators.Accumulator;
import xfp.jmh.numbers.RationalFloat0;

/** Naive sum of <code>double</code> values with a Rational-valued
 * accumulator (for testing).
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-05-23
 */
public final class RationalFloat0Accumulator

implements Accumulator<RationalFloat0Accumulator> {

  private RationalFloat0 _sum;

  //--------------------------------------------------------------

  @Override
  public final boolean isExact () { return true; }

  @Override
  public final boolean noOverflow () { return true; }

  @Override
  public final Object value () { return _sum; }

  @Override
  public final double doubleValue () {
    return _sum.doubleValue(); }

  @Override
  public final float floatValue () {
    return _sum.floatValue(); }

  @Override
  public final RationalFloat0Accumulator clear () {
    _sum = RationalFloat0.ZERO;
    return this; }

  @Override
  public final RationalFloat0Accumulator add (final double z) {
    assert Double.isFinite(z);
    _sum = _sum.add(z);
    return this; }

  @Override
  public final RationalFloat0Accumulator add2 (final double z) {
    assert Double.isFinite(z);
    _sum = _sum.add2(z);
    return this; }

  @Override
  public final RationalFloat0Accumulator addProduct (final double z0,
                                                    final double z1) {
    assert Double.isFinite(z0);
    assert Double.isFinite(z1);
    _sum = _sum.addProduct(z0,z1);
    return this; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private RationalFloat0Accumulator () { super(); clear(); }

  public static final RationalFloat0Accumulator make () {
    return new RationalFloat0Accumulator(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
