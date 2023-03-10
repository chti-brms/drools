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

package org.drools.mvel.expr;

import java.util.Date;

import org.drools.util.DateUtils;
import org.mvel2.ConversionHandler;

public class MVELDateCoercion implements ConversionHandler {

    public boolean canConvertFrom(Class cls) {
        return cls == String.class || cls.isAssignableFrom( Date.class );
    }

    public Object convertFrom(Object o) {
        if (o instanceof String) {
            return DateUtils.parseDate( (String) o );
        } else {
            return o;
        }
    }

}
