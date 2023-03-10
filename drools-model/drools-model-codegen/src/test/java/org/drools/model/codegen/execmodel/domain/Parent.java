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

package org.drools.model.codegen.execmodel.domain;

import java.util.StringJoiner;

public class Parent {

    private final String name;
    private final Child child;

    public Parent(String name, Child child) {
        this.name = name;
        this.child = child;
    }

    public String getName() {
        return name;
    }

    public Child getChild() {
        return child;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Parent.class.getSimpleName() + "[", "]")
                .add("child=" + child)
                .add("name='" + name + "'")
                .toString();
    }
}