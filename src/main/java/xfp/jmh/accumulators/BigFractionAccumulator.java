package xfp.jmh.accumulators;

import org.apache.commons.math3.fraction.BigFraction;

import xfp.java.accumulators.Accumulator;

/** Naive sum of <code>double</code> values with BigFraction 
 * accumulator (for testing).
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-03-29
 */
public final class BigFractionAccumulator 

implements Accumulator<BigFractionAccumulator> {

  private BigFraction _sum;

  //--------------------------------------------------------------
  // start with only immediate needs

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

//  @Override
//  public final BigFractionAccumulator addAll (final double[] z)  {
//    for (final double zi : z) { 
//      _sum = _sum.add(new BigFraction(zi)); }
//    return this; } 

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
