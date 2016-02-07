package pl.koszolko.junit.parameterized;

import lombok.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Value
public class QuadraticEquation {

    private BigDecimal a;
    private BigDecimal b;
    private BigDecimal c;

    public List<BigDecimal> solve() {
        if (BigDecimal.ZERO.equals(a)) {
            throw new IllegalArgumentException("This is not quadratic equation. A param can not be equal 0");
        }

        List<BigDecimal> solutions = new ArrayList<BigDecimal>();

        BigDecimal discriminant = b.multiply(b).subtract(a.multiply(c).multiply(BigDecimal.valueOf(4D)));

        if (discriminant.compareTo(BigDecimal.ZERO) > 0){
            solutions.add(calculateFirstRoot(discriminant));
            solutions.add(calculateSecondRoot(discriminant));
        } else if (discriminant.compareTo(BigDecimal.ZERO) == 0) {
            solutions.add(calculateOnlyOneRoot());
        }
        //if discriminant < 0 The equation has no real root

        return solutions;
    }

    private BigDecimal calculateOnlyOneRoot() {
        return b.negate().divide(a.multiply(BigDecimal.valueOf(2D)));
    }

    private BigDecimal calculateSecondRoot(BigDecimal discriminant) {
        return (b.negate().subtract(BigDecimal.valueOf(Math.sqrt(discriminant.doubleValue()))))
                .divide(a.multiply(BigDecimal.valueOf(2D)));
    }

    private BigDecimal calculateFirstRoot(BigDecimal discriminant) {
        return (b.negate().add(BigDecimal.valueOf(Math.sqrt(discriminant.doubleValue()))))
                .divide(a.multiply(BigDecimal.valueOf(2D)));
    }
}
