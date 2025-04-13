
/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */




package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Locale;

/**
 * Represents a token for time duration values with units (e.g., ms, s, min).
 */
public class TimeDuration implements Token {

    private final long milliseconds;

    /**
     * Constructs a TimeDuration token.
     *
     * @param value The string representation of the time duration (e.g., "150ms").
     */
    public TimeDuration(String value) {
        this.milliseconds = parseMilliseconds(value);
    }

    /**
     * Returns the value of this {@code TimeDuration} object as a {@code Long}
     * representing the duration in milliseconds.
     *
     * @return the duration in milliseconds.
     */
    @Override
    public Long value() {
        return getMilliseconds();
    }

    /**
     * Returns the value in milliseconds.
     *
     * @return Duration in long.
     */
    public long getMilliseconds() {
        return this.milliseconds;
    }

    /**
     * Returns the type of this {@code TIME_DURATION} object as a {@code TokenType}
     * enum.
     *
     * @return the enumerated {@code TokenType} of this object.
     */
    @Override
    public TokenType type() {
        return TokenType.TIME_DURATION;
    }

    /**
     * Parses the input string and converts it to milliseconds.
     *
     * @param value The input duration string.
     * @return The duration in milliseconds.
     * @throws IllegalArgumentException if input is not a valid time duration string.
     */
    private long parseMilliseconds(String value) {
        String normalize = value.trim().toLowerCase(Locale.ENGLISH);
        double valueDouble = Double.parseDouble(normalize.replaceAll("[^0-9.-]", ""));
        String unit = normalize.replaceAll("[^a-zA-Z]+", "");

        if (valueDouble < 0) {
            throw new IllegalArgumentException("Negative time duration values are not allowed: " + value);
        }

        switch (unit) {
            case "ms":
                return (long) valueDouble;
            case "s":
                return (long) (valueDouble * 1000);
            case "min":
                return (long) (valueDouble * 1000 * 60);
            case "h":
                return (long) (valueDouble * 1000 * 60 * 60);
            case "d":
                return (long) (valueDouble * 1000 * 60 * 60 * 24);
            default:
                throw new IllegalArgumentException("Unsupported time unit in: " + value);
        }
    }

    /**
     * Returns the members of this {@code TIME_DURATION} object as a
     * {@code JsonElement}.
     *
     * @return Json representation of this {@code TIME_DURATION} object as
     * {@code JsonElement}
     */
    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", TokenType.TIME_DURATION.name());
        object.addProperty("value", this.milliseconds);
        return object;
    }
}

