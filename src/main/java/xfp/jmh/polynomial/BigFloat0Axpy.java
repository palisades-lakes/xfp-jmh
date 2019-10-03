package xfp.jmh.polynomial;

import xfp.java.polynomial.Axpy;
import xfp.jmh.AxpyBench;
import xfp.jmh.numbers.BigFloat0;

/** Exact {@link AxpyBench} using {@link BigFloat0} for the exact 
 * values.
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-03
 */

@SuppressWarnings("unchecked")
public final class BigFloat0Axpy implements Axpy<BigFloat0> {

  @Override
  public final boolean isExact () { return true; }

  @Override
  public final BigFloat0 axpy (final double a,
                               final double x,
                               final double y) {
    return axpy(BigFloat0.valueOf(a),x,y); }
  //  return BigFloat0.axpy(a,x,y); }

  @Override
  public final BigFloat0[] axpy (final double[] a,
                                 final double[] x,
                                 final double[] y) {
    return BigFloat0.axpy(a,x,y); }

  @Override
  public final BigFloat0 axpy (final BigFloat0 a,
                               final double x,
                               final double y) {
    return BigFloat0.axpy(a,x,y); }

  @Override
  public final BigFloat0[] axpy (final BigFloat0[] a,
                                 final double[] x,
                                 final double[] y) {
    return BigFloat0.axpy(a,x,y); }

  @Override
  public final double daxpy (final double a,
                             final double x,
                             final double y) {
    return BigFloat0.axpy(a,x,y).doubleValue(); }

  @Override
  public final double[] daxpy (final double[] a,
                               final double[] x,
                               final double[] y) {    
    final int n = a.length;
    assert n==x.length;
    assert n==y.length;
    final double[] z = new double[n];
    for (int i=0;i<n;i++) { z[i] = daxpy(a[i],x[i],y[i]); }
    return z; }

  public static final BigFloat0Axpy make () {
    return new BigFloat0Axpy(); }

  //--------------------------------------------------------------
}
