package com.baeldung.examples.fleetly.dto;

import java.io.Serializable;
import java.util.Objects;

public class Fault implements Serializable {
    private final String message;

    public String getMessage() {
        return message;
    }

    public Fault(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fault entity = (Fault) o;
        return Objects.equals(this.message, entity.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(message = " + message + ")";
    }
}
