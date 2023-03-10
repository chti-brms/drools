/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.beliefs.bayes.assembler;

import org.drools.beliefs.bayes.JunctionTree;
import org.kie.api.internal.io.ResourceTypePackage;
import org.kie.api.io.ResourceType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BayesPackage implements ResourceTypePackage<JunctionTree> {
    private Map<String, JunctionTree> trees;
    private String namespace;

    public BayesPackage(String namespace) {
        this.trees = new HashMap<>();
        this.namespace = namespace;
    }

    public Collection<String> listJunctionTrees() {
        return trees.keySet();
    }

    public void addJunctionTree(String name, JunctionTree tree) {
        trees.put( name, tree );
    }
    public JunctionTree getJunctionTree(String name) {
        return trees.get( name );
    }

    public void removeJunctionTree(String name) {
        trees.remove( name );
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.BAYES;
    }

    @Override
    public void add(JunctionTree processedResource) {
        addJunctionTree(processedResource.getName(), processedResource);
    }

    @Override
    public Iterator<JunctionTree> iterator() {
        return trees.values().iterator();
    }
}
