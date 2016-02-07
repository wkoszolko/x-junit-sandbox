package pl.koszolko.junit.parameterized;

import org.assertj.core.util.BigDecimalComparator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class QuadraticEquationTest {

    @Parameterized.Parameter
    public BigDecimal a;
    @Parameterized.Parameter(value = 1)
    public BigDecimal b;
    @Parameterized.Parameter(value = 2)
    public BigDecimal c;
    @Parameterized.Parameter(value = 3)
    public List<BigDecimal> expectedSolutions;

    @Parameterized.Parameters(name = "Test nr {index}: ({0} * x^2 )+({1} * x)+{2}=0, solutions={3} ")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                //2 roots
                {BigDecimal.valueOf(2D), BigDecimal.valueOf(5D), BigDecimal.valueOf(-3D),
                        Arrays.asList(BigDecimal.valueOf(-3D), BigDecimal.valueOf(0.5D))},
                //2 roots
                {BigDecimal.valueOf(2D), BigDecimal.valueOf(5D), BigDecimal.valueOf(0D),
                        Arrays.asList(BigDecimal.valueOf(-2.5D), BigDecimal.valueOf(0D))},
                //only one root
                {BigDecimal.valueOf(1D), BigDecimal.valueOf(0D), BigDecimal.valueOf(0D),
                        Arrays.asList(BigDecimal.valueOf(0D))},
                //no solution
                {BigDecimal.valueOf(1D), BigDecimal.valueOf(0D), BigDecimal.valueOf(10D),
                        Collections.emptyList()}
        });
    }

    @Test
    public void testSolve() {
        //given
        QuadraticEquation equation = new QuadraticEquation(a,b,c);

        //when
        List<BigDecimal> solutions = equation.solve();

        //then
        assertThat(solutions)
                .usingElementComparator(new BigDecimalComparator())
                .containsOnly(expectedSolutions.toArray(new BigDecimal[expectedSolutions.size()]));
    }
}