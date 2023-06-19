package com.example.presenting_tdd;

import java.util.List;

public record ConveyorLineCriteria(DeliveryType deliveryType, boolean isTube, ComparisonType itemCountComparisonType,
                                   int itemCountThreshold, List<Integer> excludeBoxCodes,
                                   ComparisonType weightComparisonType, double weightThreshold) {
}