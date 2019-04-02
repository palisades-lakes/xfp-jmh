package xfp.jmh.accumulators;

import org.apache.commons.math3.fraction.BigFraction;

import xfp.java.accumulators.Accumulator;

/** Naive sum of <code>double</code> values with BigFraction 
 * accumulator (for testing).
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-02
 */
public final class BigFractionAccumulator 

implements Accumulator<BigFractionAccumulator> {

  private BigFraction _sum;

  //--------------------------------------------------------------
  @Override
  public final boolean isExact () { return true; }


  @Override
  public final double doubleValue () { 
    return _sum.doubleValue(); }

  @Override
  public final BigFractionAccumulator clear () { 
    _sum = BigFraction.ZERO;
    return this; }

  @Override
  public final BigFractionAccumulator add (final double z) { 
    _sum = _sum.add(new BigFraction(z));
    return this; }

  @Override
  public final BigFractionAccumulator add2 (final double z) { 
    final BigFraction zz = new BigFraction(z);
    _sum = _sum.add(zz.multiply(zz));
    return this; }

  @Override
  public final BigFractionAccumulator addProduct (final double z0,
                                                  final double z1) { 
    _sum = _sum.add(
      new BigFraction(z0)
      .multiply(
        new BigFraction(z1)));
    return this; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private BigFractionAccumulator () { super(); clear(); }

  public static final BigFractionAccumulator make () {
    return new BigFractionAccumulator(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------