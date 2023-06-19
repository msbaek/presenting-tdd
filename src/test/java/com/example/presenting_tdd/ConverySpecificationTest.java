package com.example.presenting_tdd;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Predicate;

import static com.example.presenting_tdd.ComparisonType.*;
import static org.assertj.core.api.Assertions.assertThat;

enum DeliveryType {
    DOMESTIC, INTERNATIONAL
}

enum ComparisonType {
    LT {
        @Override
        <T extends Comparable<T>> boolean compare(final T value, final T threshold) {
            return value.compareTo(threshold) < 0;
        }
    },
    LTE {
        @Override
        <T extends Comparable<T>> boolean compare(final T value, final T threshold) {
            return value.compareTo(threshold) <= 0;
        }
    },
    EQ {
        @Override
        <T extends Comparable<T>> boolean compare(final T value, final T threshold) {
            return value.compareTo(threshold) == 0;
        }
    },
    GTE {
        @Override
        <T extends Comparable<T>> boolean compare(final T value, final T threshold) {
            return value.compareTo(threshold) >= 0;
        }
    },
    GT {
        @Override
        <T extends Comparable<T>> boolean compare(final T value, final T threshold) {
            return value.compareTo(threshold) > 0;
        }
    };
    abstract <T extends Comparable<T>> boolean compare(T value, T threshold);
}

public class ConverySpecificationTest {
    @Test
    public void test() {
        final Conveyor conveyor2 = new Conveyor(DeliveryType.INTERNATIONAL, false, 5, 1, 6.9);
        final Conveyor conveyor3 = new Conveyor(DeliveryType.INTERNATIONAL, false, 6, 1, 6.9);
        final Conveyor conveyor4 = new Conveyor(DeliveryType.DOMESTIC, false, 3, 1, 6.9);
        final Conveyor conveyor5 = new Conveyor(DeliveryType.DOMESTIC, false, 4, 1, 6.9);

        assertThat(matchConveyorLine(conveyor2)).isEqualTo("2");
        assertThat(matchConveyorLine(conveyor3)).isEqualTo("3");
        assertThat(matchConveyorLine(conveyor4)).isEqualTo("4");
        assertThat(matchConveyorLine(conveyor5)).isEqualTo("5");
    }

    private String matchConveyorLine(final Conveyor conveyor5) {
        // read ConveyorLineCriteria from DB
        // create conveyorLines
        Map<String, List<GmsSpecification>> conveyorLines = new HashMap<>();
        conveyorLines.put("2", createConveyorSpecifications(
                new ConveyorLineCriteria(DeliveryType.INTERNATIONAL, false, LTE, 5, Arrays.asList(1, 2), LT, 7.0)));
        conveyorLines.put("3", createConveyorSpecifications(
                new ConveyorLineCriteria(DeliveryType.INTERNATIONAL, false, GT, 5, Arrays.asList(1, 2), LT, 7.0)));
        conveyorLines.put("4", createConveyorSpecifications(
                new ConveyorLineCriteria(DeliveryType.DOMESTIC, false, LTE, 3, Arrays.asList(1, 2), LT, 7.0)));
        conveyorLines.put("5", createConveyorSpecifications(
                new ConveyorLineCriteria(DeliveryType.DOMESTIC, false, GT, 3, Arrays.asList(1, 2), LT, 7.0)));
        for (Map.Entry<String, List<GmsSpecification>> entry : conveyorLines.entrySet()) {
            String key = entry.getKey();
            List<GmsSpecification> value = entry.getValue();
            boolean b = value.stream().allMatch(s -> s.isSatisfiedBy(conveyor5));
            if (b)
                return key;
        }
        return "unmatched";
    }

    private List<GmsSpecification> createConveyorSpecifications(final ConveyorLineCriteria conveyorLineCriteria) {
        List<GmsSpecification> specifications = new ArrayList<>();
        specifications.add(new ConveyorSpecification(c -> c.deliveryType() == conveyorLineCriteria.deliveryType()));
        specifications.add(new ConveyorSpecification(c -> c.isTube() == conveyorLineCriteria.isTube()));
        specifications.add(new ConveyorSpecification(c -> conveyorLineCriteria.itemCountComparisonType().compare(c.itemCount(), conveyorLineCriteria.itemCountThreshold())));
        specifications.add(new ConveyorSpecification(c -> conveyorLineCriteria.excludeBoxCodes().contains(c.boxCode())));
        specifications.add(new ConveyorSpecification(c -> conveyorLineCriteria.weightComparisonType().compare(c.weight(), conveyorLineCriteria.weightThreshold())));
        return specifications;
    }
}

record Conveyor(DeliveryType deliveryType, boolean isTube, int itemCount, int boxCode, double weight) {
}

@FunctionalInterface
interface GmsSpecification<T> {
    boolean isSatisfiedBy(T t);
}

class ConveyorSpecification implements GmsSpecification<Conveyor> {

    private final Predicate<Conveyor> p;

    public ConveyorSpecification(Predicate<Conveyor> p) {
        this.p = p;
    }

    public boolean isSatisfiedBy(final Conveyor conveyor) {
        return p.test(conveyor);
    }
}