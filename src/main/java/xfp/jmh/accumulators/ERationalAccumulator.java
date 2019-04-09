package xfp.jmh.accumulators;

import com.upokecenter.numbers.ERational;

import xfp.java.accumulators.Accumulator;

/** Naive sum of <code>double</code> values with ERational 
 * accumulator (for testing).
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-09
 */
public final class ERationalAccumulator 

implements Accumulator<ERationalAccumulator> {

  private ERational _sum;

  //--------------------------------------------------------------

  @Override
  public final boolean isExact () { return true; }

  @Override
  public final boolean noOverflow () { return true; }

  @Override
  public final double doubleValue () { 
    return _sum.ToDouble(); }

  @Override
  public final ERationalAccumulator clear () { 
    _sum = ERational.Zero;
    return this; }

  @Override
  public final ERationalAccumulator add (final double z) { 
    assert Double.isFinite(z);
    _sum = _sum.Add(ERational.FromDouble(z));
    return this; }

  @Override
  public final ERationalAccumulator add2 (final double z) {
    assert Double.isFinite(z);
    final ERational zz = ERational.FromDouble(z);
    _sum = _sum.Add(zz.Multiply(zz));
    return this; }

  @Override
  public final ERationalAccumulator addProduct (final double z0,
                                                final double z1) { 
    assert Double.isFinite(z0);
    assert Double.isFinite(z1);
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
