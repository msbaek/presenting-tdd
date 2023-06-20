package com.example.presenting_tdd;

import groovy.lang.Tuple2;
import org.junit.jupiter.api.BeforeEach;
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

    private Map<String, GmsSpecification<Conveyor>> conveyorLines;

    @BeforeEach
    void setUp() {
        // read ConveyorLineCriteria from DB
        // create conveyorLines
        conveyorLines = new HashMap<>();
        conveyorLines.put("2", createConveyorSpecifications(
                new ConveyorLineCriteria(DeliveryType.INTERNATIONAL, false, LTE, 5, Arrays.asList(1, 2), LT, 7.0)));
        conveyorLines.put("3", createConveyorSpecifications(
                new ConveyorLineCriteria(DeliveryType.INTERNATIONAL, false, GT, 5, Arrays.asList(1, 2), LT, 7.0)));
        conveyorLines.put("4", createConveyorSpecifications(
                new ConveyorLineCriteria(DeliveryType.DOMESTIC, false, LTE, 3, Arrays.asList(1, 2), LT, 7.0)));
        conveyorLines.put("5", createConveyorSpecifications(
                new ConveyorLineCriteria(DeliveryType.DOMESTIC, false, GT, 3, Arrays.asList(1, 2), LT, 7.0)));
    }

    @Test
    public void test() {
        List<Tuple2<String, Conveyor>> conveyors = List.of(
                new Tuple2<>("2", new Conveyor(DeliveryType.INTERNATIONAL, false, 5, 1, 6.9)),
                new Tuple2<>("3", new Conveyor(DeliveryType.INTERNATIONAL, false, 6, 1, 6.9)),
                new Tuple2<>("4", new Conveyor(DeliveryType.DOMESTIC, false, 3, 1, 6.9)),
                new Tuple2<>("5", new Conveyor(DeliveryType.DOMESTIC, false, 4, 1, 6.9))
        );
        for (final Tuple2<String, Conveyor> conveyor : conveyors) {
            assertThat(matchConveyorLine(conveyor.getV2())).isEqualTo(conveyor.getV1());
        }
    }

    private String matchConveyorLine(final Conveyor conveyor5) {
        for (Map.Entry<String, GmsSpecification<Conveyor>> entry : conveyorLines.entrySet()) {
            String k = entry.getKey();
            GmsSpecification<Conveyor> v = entry.getValue();
            if(v.isSatisfiedBy(conveyor5))
                return k;
        }
        return "unmatched";
    }

    private GmsSpecification<Conveyor> createConveyorSpecifications(final ConveyorLineCriteria conveyorLineCriteria) {
        List<GmsSpecification<Conveyor>> specifications = new ArrayList<>();
        specifications.add(new ConveyorSpecification<Conveyor>(c -> c.deliveryType() == conveyorLineCriteria.deliveryType()));
        specifications.add(new ConveyorSpecification<Conveyor>(c -> c.isTube() == conveyorLineCriteria.isTube()));
        specifications.add(new ConveyorSpecification<Conveyor>(c -> conveyorLineCriteria.itemCountComparisonType().compare(c.itemCount(), conveyorLineCriteria.itemCountThreshold())));
        specifications.add(new ConveyorSpecification<Conveyor>(c -> conveyorLineCriteria.excludeBoxCodes().contains(c.boxCode())));
        specifications.add(new ConveyorSpecification<Conveyor>(c -> conveyorLineCriteria.weightComparisonType().compare(c.weight(), conveyorLineCriteria.weightThreshold())));
        return new CompositeSpecification<Conveyor>(specifications);
    }
}

record Conveyor(DeliveryType deliveryType, boolean isTube, int itemCount, int boxCode, double weight) {
}

@FunctionalInterface
interface GmsSpecification<T> {
    boolean isSatisfiedBy(T t);
}

class ConveyorSpecification<T> implements GmsSpecification<T> {

    private final Predicate<T> p;

    public ConveyorSpecification(Predicate<T> p) {
        this.p = p;
    }

    public boolean isSatisfiedBy(final T conveyor) {
        return p.test(conveyor);
    }
}

class CompositeSpecification<T> implements GmsSpecification<T> {

    private final List<GmsSpecification<T>> specifications;

    public CompositeSpecification(final List<GmsSpecification<T>> specifications) {
        this.specifications = specifications;
    }

    @Override
    public boolean isSatisfiedBy(final T t) {
        return specifications.stream().allMatch(s -> s.isSatisfiedBy(t));
    }
}