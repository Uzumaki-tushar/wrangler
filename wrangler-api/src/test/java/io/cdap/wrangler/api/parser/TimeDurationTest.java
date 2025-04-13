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
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cdap.wrangler.api.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link TimeDuration}.
 */
public class TimeDurationTest {

  @Test
  public void testValidDurations() {
    Assert.assertEquals(150, new TimeDuration("150ms").getMilliseconds());
    Assert.assertEquals(10000, new TimeDuration("10s").getMilliseconds()); // seconds to ms
    Assert.assertEquals(300000, new TimeDuration("5min").getMilliseconds()); // minutes to ms
    Assert.assertEquals(7200000, new TimeDuration("2h").getMilliseconds()); // hours to ms
    Assert.assertEquals(86400000, new TimeDuration("1d").getMilliseconds()); // days to ms
  }

  @Test
  public void testDecimalDurations() {
    Assert.assertEquals(1500, new TimeDuration("1.5s").getMilliseconds()); // fractional seconds to ms
    Assert.assertEquals(5400000, new TimeDuration("1.5h").getMilliseconds()); // fractional hours to ms
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidUnit() {
    new TimeDuration("50weeks"); // unsupported unit
  }

  @Test(expected = NumberFormatException.class)
  public void testMalformedNumber() {
    new TimeDuration("abcms"); // non-numeric input
  }

  @Test
  public void testWhitespaceHandling() {
    Assert.assertEquals(60000, new TimeDuration(" 1min ").getMilliseconds()); // trims extra spaces
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeValue() {
    new TimeDuration("-10ms"); // negative input
  }
}
