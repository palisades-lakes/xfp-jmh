package xfp.jmh.test.numbers;

import org.junit.jupiter.api.Test;

import com.upokecenter.numbers.EInteger;
import com.upokecenter.numbers.ERational;

import xfp.java.Debug;
import xfp.java.numbers.Doubles;
import xfp.java.prng.Generator;
import xfp.java.prng.Generators;
import xfp.java.prng.PRNG;
import xfp.java.test.Common;
import xfp.jmh.numbers.ERationals;

//----------------------------------------------------------------
/** Test desired properties of ERational. 
 * <p>
 * <pre>
 * mvn -q clean -Dtest=xfp/jmh/test/numbers/ERationalTest test > ERationalTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-21
 */

public final class ERationalTest {

  //--------------------------------------------------------------

  private static final void correctRounding (final ERational f) {
    Common.doubleRoundingTest(
      ERational::FromDouble,
      q -> ((ERational) q).ToDouble(),
      f,
      q -> ERationals.toHexString((ERational) q)); }

  //--------------------------------------------------------------

  private static final int TRYS = 8 * 1024;

  @SuppressWarnings({ "static-method" })
  @Test
  public final void roundingTest () {
    final ERational f = ERational.Create(13,11);
    correctRounding(f); }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void anotherRoundingTest () {
    Debug.DEBUG = true;
    final ERational f = 
      ERational.Create(
        EInteger.FromInt64(-0x331c0c32d0072fL),
        EInteger.FromInt64(0x1000000L));
    correctRounding(f); 
    Debug.DEBUG = false; }


  @SuppressWarnings({ "static-method" })
  @Test
  public final void longRoundingTest () {
    final ERational f = 
      ERational.Create(
        EInteger.FromInt64(0x789f09858446ad92L),
        EInteger.FromInt64(0x19513ea5d70c32eL));
    correctRounding(f); }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void fromEIntegersRoundingTest () {
    final Generator gn = 
      ERationals.eIntegerGenerator(
        PRNG.well44497b("seeds/Well44497b-2019-01-05.txt"));
    final Generator gd = 
      ERationals.nonzeroEIntegerGenerator(
        PRNG.well44497b("seeds/Well44497b-2019-01-07.txt"));
    for (int i=0;i<TRYS;i++) {
      // some longs will not be exactly representable as doubles
      final EInteger n = (EInteger) gn.next();
      final EInteger d = (EInteger) gd.next();
      final ERational f = ERational.Create(n,d);
      correctRounding(f); } }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void fromLongsRoundingTest () {
    final Generator g = 
      Generators.longGenerator(
        PRNG.well44497b("seeds/Well44497b-2019-01-05.txt"));
    for (int i=0;i<TRYS;i++) {
      // some longs will not be exactly representable as doubles
      final long n = g.nextLong();
      final long d = g.nextLong();
      final ERational f = ERational.Create(
        EInteger.FromInt64(n),
        EInteger.FromInt64(d));
      correctRounding(f); } }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void finiteDoubleRoundingTest () {
    final Generator g = 
      Doubles.finiteGenerator(
        PRNG.well44497b("seeds/Well44497b-2019-01-05.txt"));
    for (int i=0;i<TRYS;i++) {
      final double x = g.nextDouble();
      final ERational f = ERational.FromDouble(x);
      correctRounding(f); } }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void subnormalDoubleRoundingTest () {
    final Generator g = 
      Doubles.subnormalGenerator(
        PRNG.well44497b("seeds/Well44497b-2019-01-05.txt"));
    for (int i=0;i<TRYS;i++) {
      final double x = g.nextDouble();
      final ERational f = ERational.FromDouble(x);
      correctRounding(f); } }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
