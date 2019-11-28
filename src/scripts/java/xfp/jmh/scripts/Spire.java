package xfp.jmh.scripts;

import spire.math.Rational;
import spire.math.Rational$;
import spire.math.RationalAlgebra;

import spire.math.Algebraic;
import spire.math.Algebraic$;
import spire.math.AlgebraicAlgebra;

/** Experiment with calling Scala spire.
 *
 * <pre>
 * j --source 12 -ea src/scripts/java/xfp/jmh/scripts/Spire.java
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-11-27
 */
@SuppressWarnings("unchecked")
public final class Spire {


  public static final void main (final String[] args) {

    final Rational r0 = Rational$.MODULE$.apply(1.0);
    System.out.println(r0.toString());
    final Rational r1 = r0.$plus(r0);
    System.out.println(r1);
    
    final Algebraic a0 = Algebraic$.MODULE$.apply(Math.PI);
    System.out.println(r0.toString());
    final Algebraic a1 = a0.$plus(a0);
    System.out.println(a1.sqrt());
    System.out.println(Math.sqrt(Math.PI));
    
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
