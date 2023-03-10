/*
 * Copyright (c) 2020. Red Hat, Inc. and/or its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.util;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamUtils {

    private StreamUtils() {

    }

    public static <T> Stream<T> optionalToStream(Optional<T> opt) {
        return opt.map(Stream::of).orElse(Stream.empty());
    }
    

    public static <T> List<T> optionalToList(Optional<T> opt) {
        return optionalToStream(opt).collect(Collectors.toList());
    }
}
