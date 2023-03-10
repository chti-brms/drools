/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.scenariosimulation.api.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ScesimModelDescriptorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ScesimModelDescriptor scesimModelDescriptor;
    private FactIdentifier factIdentifier;
    private ExpressionIdentifier expressionIdentifier;
    private FactIdentifier factIdentifier2;
    private ExpressionIdentifier expressionIdentifier2;

    @Before
    public void init() {
        scesimModelDescriptor = new ScesimModelDescriptor();
        factIdentifier = FactIdentifier.create("test fact", String.class.getCanonicalName());
        expressionIdentifier = ExpressionIdentifier.create("test expression", FactMappingType.EXPECT);
        factIdentifier2 = FactIdentifier.create("test fact 2", Integer.class.getCanonicalName());
        expressionIdentifier2 = ExpressionIdentifier.create("test expression 2", FactMappingType.GIVEN);
    }

    @Test
    public void getFactIdentifiers() {
        scesimModelDescriptor.addFactMapping(factIdentifier, expressionIdentifier);
        final Set<FactIdentifier> retrieved = scesimModelDescriptor.getFactIdentifiers();
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.size()).isEqualTo(1);
        assertThat(retrieved.iterator().next()).isEqualTo(factIdentifier);
    }

    @Test
    public void addFactMappingByIndexAndFactMapping() {
        FactMapping toClone = new FactMapping();
        toClone.setFactAlias("ALIAS");
        toClone.setExpressionAlias("EXPRESSION_ALIAS");
        final FactMapping cloned = scesimModelDescriptor.addFactMapping(0, toClone);
        assertThat(cloned.getFactAlias()).isEqualTo(toClone.getFactAlias());
        assertThat(cloned.getExpressionAlias()).isEqualTo(toClone.getExpressionAlias());
    }

    @Test
    public void addFactMappingByFactIdentifierAndExpressionIdentifier() {
        scesimModelDescriptor.addFactMapping(factIdentifier, expressionIdentifier);
        scesimModelDescriptor.addFactMapping(factIdentifier2, expressionIdentifier2);
        assertThat(scesimModelDescriptor.getFactMappingByIndex(0).getFactAlias()).isEqualTo(factIdentifier.getName());
        assertThat(scesimModelDescriptor.getFactMappingByIndex(0).getFactIdentifier()).isEqualTo(factIdentifier);
        assertThat(scesimModelDescriptor.getFactMappingByIndex(0).getExpressionIdentifier()).isEqualTo(expressionIdentifier);
        assertThat(scesimModelDescriptor.getFactMappingByIndex(1).getFactAlias()).isEqualTo(factIdentifier2.getName());
        assertThat(scesimModelDescriptor.getFactMappingByIndex(1).getFactIdentifier()).isEqualTo(factIdentifier2);
        assertThat(scesimModelDescriptor.getFactMappingByIndex(1).getExpressionIdentifier()).isEqualTo(expressionIdentifier2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFactMappingByFactIdentifierAndExpressionIdentifierFail() {
        scesimModelDescriptor.addFactMapping(factIdentifier, expressionIdentifier);
        // Should fail
        scesimModelDescriptor.addFactMapping(factIdentifier, expressionIdentifier);
    }

    @Test
    public void addFactMappingByIndexAndFactIdentifierAndExpressionIdentifier() {
        scesimModelDescriptor.addFactMapping(0, factIdentifier, expressionIdentifier);
        scesimModelDescriptor.addFactMapping(0, factIdentifier2, expressionIdentifier2);
        assertThat(scesimModelDescriptor.getFactMappingByIndex(1).getFactAlias()).isEqualTo(factIdentifier.getName());
        assertThat(scesimModelDescriptor.getFactMappingByIndex(1).getFactIdentifier()).isEqualTo(factIdentifier);
        assertThat(scesimModelDescriptor.getFactMappingByIndex(1).getExpressionIdentifier()).isEqualTo(expressionIdentifier);
        assertThat(scesimModelDescriptor.getFactMappingByIndex(0).getFactAlias()).isEqualTo(factIdentifier2.getName());
        assertThat(scesimModelDescriptor.getFactMappingByIndex(0).getFactIdentifier()).isEqualTo(factIdentifier2);
        assertThat(scesimModelDescriptor.getFactMappingByIndex(0).getExpressionIdentifier()).isEqualTo(expressionIdentifier2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFactMappingByIndexAndFactIdentifierAndExpressionIdentifierFail() {
        // Should fail
        scesimModelDescriptor.addFactMapping(1, factIdentifier, expressionIdentifier);
    }

    @Test
    public void removeFactMappingByIndex() {
        int testingIndex = 0;
        scesimModelDescriptor.addFactMapping(factIdentifier, expressionIdentifier);
        assertThat(scesimModelDescriptor.getFactMappingByIndex(testingIndex)).isNotNull();
        scesimModelDescriptor.removeFactMappingByIndex(testingIndex);
        expectedException.expect(IndexOutOfBoundsException.class);
        scesimModelDescriptor.getFactMappingByIndex(testingIndex);
    }

    @Test
    public void removeFactMapping() {
        FactMapping retrieved = scesimModelDescriptor.addFactMapping(factIdentifier, expressionIdentifier);
        assertThat(scesimModelDescriptor.getUnmodifiableFactMappings().contains(retrieved)).isTrue();
        scesimModelDescriptor.removeFactMapping(retrieved);
        assertThat(scesimModelDescriptor.getUnmodifiableFactMappings().contains(retrieved)).isFalse();
    }

    @Test
    public void getIndexByIdentifierTest() {
        List<FactMapping> originalFactMappings = IntStream.range(0, 2).boxed()
                .map(i -> scesimModelDescriptor
                        .addFactMapping(FactIdentifier.create("test " + i, String.class.getCanonicalName()), this.expressionIdentifier)
                ).collect(Collectors.toList());
        int indexToCheck = 0;
        int indexRetrieved = scesimModelDescriptor.getIndexByIdentifier(originalFactMappings.get(indexToCheck).getFactIdentifier(), this.expressionIdentifier);
        assertThat(indexRetrieved).isEqualTo(indexToCheck);
        indexToCheck = 1;
        indexRetrieved = scesimModelDescriptor.getIndexByIdentifier(originalFactMappings.get(indexToCheck).getFactIdentifier(), this.expressionIdentifier);
        assertThat(indexRetrieved).isEqualTo(indexToCheck);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getIndexByIdentifierTestFail() {
        IntStream.range(0, 2).forEach(i -> scesimModelDescriptor
                .addFactMapping(FactIdentifier.create("test " + i, String.class.getCanonicalName()), this.expressionIdentifier));
        FactIdentifier notExisting = new FactIdentifier();
        scesimModelDescriptor.getIndexByIdentifier(notExisting, this.expressionIdentifier);
    }

    @Test
    public void getFactMappingsByFactName() {
        IntStream.range(0, 2).forEach(i -> scesimModelDescriptor
                .addFactMapping(FactIdentifier.create("test", String.class.getCanonicalName()), ExpressionIdentifier.create("test expression " + i, FactMappingType.EXPECT)));
        scesimModelDescriptor
                .addFactMapping(FactIdentifier.create("TEST", String.class.getCanonicalName()), ExpressionIdentifier.create("test expression 2", FactMappingType.EXPECT));
        scesimModelDescriptor
                .addFactMapping(FactIdentifier.create("Test", String.class.getCanonicalName()), ExpressionIdentifier.create("test expression 3", FactMappingType.EXPECT));
        scesimModelDescriptor
                .addFactMapping(FactIdentifier.create("tEsT", String.class.getCanonicalName()), ExpressionIdentifier.create("test expression 4", FactMappingType.EXPECT));
        final Stream<FactMapping> retrieved = scesimModelDescriptor.getFactMappingsByFactName("test");
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.count()).isEqualTo(5);
    }

    @Test
    public void moveFactMappingTest() {
        ExpressionIdentifier expressionIdentifier2 = ExpressionIdentifier.create("Test expression 2", FactMappingType.GIVEN);
        ExpressionIdentifier expressionIdentifier3 = ExpressionIdentifier.create("Test expression 3", FactMappingType.GIVEN);
        FactMapping factMapping1 = scesimModelDescriptor.addFactMapping(factIdentifier, expressionIdentifier);
        FactMapping factMapping2 = scesimModelDescriptor.addFactMapping(factIdentifier, expressionIdentifier2);
        FactMapping factMapping3 = scesimModelDescriptor.addFactMapping(factIdentifier, expressionIdentifier3);
        List<FactMapping> factMappings = scesimModelDescriptor.getUnmodifiableFactMappings();

        assertThat(factMapping1).isEqualTo(factMappings.get(0));
        assertThat(factMapping2).isEqualTo(factMappings.get(1));
        assertThat(factMapping3).isEqualTo(factMappings.get(2));

        scesimModelDescriptor.moveFactMapping(0, 1);

        factMappings = scesimModelDescriptor.getUnmodifiableFactMappings();
        assertThat(factMapping2).isEqualTo(factMappings.get(0));
        assertThat(factMapping1).isEqualTo(factMappings.get(1));
        assertThat(factMapping3).isEqualTo(factMappings.get(2));

        scesimModelDescriptor.moveFactMapping(2, 1);

        factMappings = scesimModelDescriptor.getUnmodifiableFactMappings();
        assertThat(factMapping2).isEqualTo(factMappings.get(0));
        assertThat(factMapping3).isEqualTo(factMappings.get(1));
        assertThat(factMapping1).isEqualTo(factMappings.get(2));

        scesimModelDescriptor.moveFactMapping(2, 2);

        factMappings = scesimModelDescriptor.getUnmodifiableFactMappings();
        assertThat(factMapping2).isEqualTo(factMappings.get(0));
        assertThat(factMapping3).isEqualTo(factMappings.get(1));
        assertThat(factMapping1).isEqualTo(factMappings.get(2));
    }

    @Test
    public void moveFactMappingOldFailTest() {
        ExpressionIdentifier expressionIdentifier2 = ExpressionIdentifier.create("Test expression 2", FactMappingType.GIVEN);
        scesimModelDescriptor.addFactMapping(factIdentifier, expressionIdentifier);
        scesimModelDescriptor.addFactMapping(factIdentifier, expressionIdentifier2);

        muteException(() -> {
                          scesimModelDescriptor.moveFactMapping(2, 0);
                          fail("Unexpected execution path");
                      },
                      IndexOutOfBoundsException.class);

        muteException(() -> {
                          scesimModelDescriptor.moveFactMapping(-1, 0);
                          fail("Unexpected execution path");
                      },
                      IndexOutOfBoundsException.class);

        muteException(() -> {
                          scesimModelDescriptor.moveFactMapping(0, 2);
                          fail("Unexpected execution path");
                      },
                      IndexOutOfBoundsException.class);

        muteException(() -> {
                          scesimModelDescriptor.moveFactMapping(0, -1);
                          fail("Unexpected execution path");
                      },
                      IndexOutOfBoundsException.class);
    }

    private <T extends Throwable> void muteException(Runnable toBeExecuted, Class<T> expected) {
        try {
            toBeExecuted.run();
        } catch (Throwable t) {
            //noinspection NonJREEmulationClassesInClientCode
            if (!t.getClass().isAssignableFrom(expected)) {
                throw t;
            }
        }
    }
}
