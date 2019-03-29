package xfp.jmh.accumulators;

import com.upokecenter.numbers.ERational;

import xfp.java.accumulators.Accumulator;

/** Naive sum of <code>double</code> values with ERational 
 * accumulator (for testing).
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-03-29
 */
public final class ERationalAccumulator 

implements Accumulator<ERationalAccumulator> {

  private ERational _sum;

  //--------------------------------------------------------------
  // start with only immediate needs

  @Override
  public final double doubleValue () { 
    return _sum.ToDouble(); }

  @Override
  public final ERationalAccumulator clear () { 
    _sum = ERational.Zero;
    return this; }

  @Override
  public final ERationalAccumulator add (final double z) { 
    _sum = _sum.Add(ERational.FromDouble(z));
    return this; }

  @Override
  public final ERationalAccumulator addProduct (final double z0,
                                                final double z1) { 
    _sum = _sum.Add(
      ERational.FromDouble(z0)
      .Multiply(ERational.FromDouble(z1)));
    return this; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private ERationalAccumulator () { super(); clear(); }

  public static final ERationalAccumulator make () {
    return new ERationalAccumulator(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
