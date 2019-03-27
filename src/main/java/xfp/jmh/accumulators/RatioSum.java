package xfp.jmh.accumulators;

import static clojure.lang.Numbers.toRatio;

import java.math.BigInteger;

import clojure.lang.Numbers;
import clojure.lang.Ratio;
import xfp.java.accumulators.Accumulator;

/** Naive sum of <code>double</code> values with Ratio 
 * accumulator (for testing).
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-03-27
 */
public final class RatioSum implements Accumulator<RatioSum> {

  private static final Ratio add (final Ratio q0, 
                                  final Ratio q1) {
    return toRatio(Numbers.add(q0,q1)); } 

  private static final Ratio multiply (final Ratio q0, 
                                  final Ratio q1) {
    return toRatio(Numbers.multiply(q0,q1)); } 

  private static final Ratio ZERO = 
    new Ratio(BigInteger.ZERO,BigInteger.ONE);

  //--------------------------------------------------------------

  private Ratio _sum;

  //--------------------------------------------------------------

  @Override
  public final double doubleValue () { 
    return _sum.doubleValue(); }

  @Override
  public final RatioSum clear () { _sum = ZERO; return this; }

  @Override
  public final RatioSum add (final double z) { 
    _sum = add(_sum,toRatio(Double.valueOf(z)));
    return this; }

  @Override
  public final RatioSum addProduct (final double z0,
                                       final double z1) { 
    _sum = add(
      _sum,
      multiply(
        toRatio(Double.valueOf(z0)),
        toRatio(Double.valueOf(z1))));
    return this; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private RatioSum () { super(); clear(); }

  public static final RatioSum make () { return new RatioSum(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
