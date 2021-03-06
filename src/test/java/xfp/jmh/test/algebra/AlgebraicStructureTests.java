package xfp.jmh.test.algebra;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;

import xfp.java.algebra.Set;
import xfp.java.algebra.Structure;
import xfp.java.prng.PRNG;
import xfp.jmh.linear.BigDecimalsN;
import xfp.jmh.linear.BigFractionsN;
import xfp.jmh.linear.ERationalsN;
import xfp.jmh.linear.RatiosN;
import xfp.jmh.numbers.BigDecimals;
import xfp.jmh.numbers.BigFractions;
import xfp.jmh.numbers.ERationals;
import xfp.jmh.numbers.Ratios;

//----------------------------------------------------------------
/** Common code for testing sets.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-15
 */

@SuppressWarnings("unchecked")
public final class AlgebraicStructureTests {

  private static final int TRYS = 4;
  static final int SPACE_TRYS = 3;

  //--------------------------------------------------------------

  private static final void
  structureTests (final Structure s,
                  final int n) {
    SetTests.tests(s);
    final Map<Set,Supplier> generators =
      s.generators(
        ImmutableMap.of(
          Set.URP,
          PRNG.well44497b("seeds/Well44497b-2019-01-09.txt")));
    for(final Predicate law : s.laws()) {
      for (int i=0; i<n; i++) {
        final boolean result = law.test(generators);
        assertTrue(result); } } }

  //--------------------------------------------------------------

  @SuppressWarnings({ "static-method" })
  @Test
  public final void tests () {

    structureTests(ERationals.ADDITIVE_MAGMA,TRYS);
    structureTests(ERationals.MULTIPLICATIVE_MAGMA,TRYS);
    structureTests(ERationals.FIELD,TRYS);

    structureTests(BigDecimals.ADDITIVE_MAGMA,TRYS);
    structureTests(BigDecimals.MULTIPLICATIVE_MAGMA,TRYS);
    structureTests(BigDecimals.RING,TRYS);

    structureTests(BigFractions.ADDITIVE_MAGMA,TRYS);
    structureTests(BigFractions.MULTIPLICATIVE_MAGMA,TRYS);
    structureTests(BigFractions.FIELD,TRYS);

    structureTests(Ratios.ADDITIVE_MAGMA,TRYS);
    structureTests(Ratios.MULTIPLICATIVE_MAGMA,TRYS);
    structureTests(Ratios.FIELD,TRYS);

    for (final int n : new int[] { 1, 3, 127}) {
      structureTests(BigDecimalsN.group(n),SPACE_TRYS);
      structureTests(BigDecimalsN.space(n),SPACE_TRYS);

      structureTests(ERationalsN.group(n),SPACE_TRYS);
      structureTests(ERationalsN.space(n),SPACE_TRYS);

      structureTests(BigFractionsN.group(n),SPACE_TRYS);
      structureTests(BigFractionsN.space(n),SPACE_TRYS);

      structureTests(RatiosN.group(n),SPACE_TRYS);
      structureTests(RatiosN.space(n),SPACE_TRYS);
    } }


  //--------------------------------------------------------------
}
//--------------------------------------------------------------
