package com.example.presenting_tdd.refactoring;

import java.util.List;
import java.util.Map;

class GetSequence {
    Long sequence(final String key) {
        final Long sequence = execute(List.of(key));
        if (isEmpty(sequence)) {
            final String maxSequence = getMaxSequence();
            opsForValue().put(key, maxSequence);
            return sequence(key);
        }
        return sequence;
    }

    private Map<String, String> opsForValue() {
        throw new UnsupportedOperationException("GetSequence::opsForValue not implemented yet");
    }

    private Long execute(List<String> key) {
        throw new UnsupportedOperationException("GetSequence::execute not implemented yet");
    }

    private String getMaxSequence() {
        throw new UnsupportedOperationException("GetSequence::getMaxSequence not implemented yet");
    }

    private Boolean isEmpty(final Long sequence) {
        throw new UnsupportedOperationException("GetSequence::isEmpty not implemented yet");
    }
}

public class InvertIf {
}