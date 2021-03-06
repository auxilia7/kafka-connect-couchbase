/*
 * Copyright 2018 Couchbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.couchbase.connect.kafka.util.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationParser {
    private DurationParser() {
        throw new AssertionError("not instantiable");
    }

    private static final Pattern DURATION_PATTERN = Pattern.compile("(\\d+)([smhd])");

    private static final Map<String, TimeUnit> qualifierToTimeUnit;

    static {
        final Map<String, TimeUnit> temp = new HashMap<String, TimeUnit>();
        temp.put("s", TimeUnit.SECONDS);
        temp.put("m", TimeUnit.MINUTES);
        temp.put("h", TimeUnit.HOURS);
        temp.put("d", TimeUnit.DAYS);
        qualifierToTimeUnit = Collections.unmodifiableMap(temp);
    }

    public static long parseDurationSeconds(String s) {
        final Matcher m = DURATION_PATTERN.matcher(s.trim().toLowerCase(Locale.ROOT));
        if (!m.matches()) {
            throw new IllegalArgumentException("Unable to parse duration '" + s + "'." +
                    " Please specify an integer followed by a time unit (s = seconds, m = minutes, h = hours, d = days)." +
                    " For example, to have documents expire after 30 minutes: 30m");
        }

        final long value = Long.parseLong(m.group(1));
        final TimeUnit unit = qualifierToTimeUnit.get(m.group(2));
        return unit.toSeconds(value);
    }
}
