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

package org.drools.decisiontable;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.drools.drl.extensions.DecisionTableFactory;
import org.drools.kiesession.rulebase.InternalKnowledgeBase;
import org.drools.kiesession.rulebase.KnowledgeBaseFactory;
import org.junit.Test;
import org.kie.api.command.Command;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.DecisionTableConfiguration;
import org.kie.internal.builder.DecisionTableInputType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.command.CommandFactory;
import org.kie.internal.io.ResourceFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class UnicodeInXLSTest {

	@Test
    public void testUnicodeXLSDecisionTable() throws FileNotFoundException {

        DecisionTableConfiguration dtconf = KnowledgeBuilderFactory.newDecisionTableConfiguration();
        dtconf.setInputType(DecisionTableInputType.XLS);
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("unicode.drl.xls", getClass()), ResourceType.DTABLE, dtconf);
        if (kbuilder.hasErrors()) {
            System.out.println(kbuilder.getErrors().toString());
            System.out.println(DecisionTableFactory.loadFromInputStream(getClass().getResourceAsStream("unicode.drl.xls"), dtconf));
            fail("Cannot build XLS decision table containing utf-8 characters\n" + kbuilder.getErrors().toString() );
        }
        InternalKnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addPackages(kbuilder.getKnowledgePackages());
        
        KieSession ksession = kbase.newKieSession();
        
        List<Command<?>> commands = new ArrayList<Command<?>>();
        List<??lov??k> dosp??l?? = new ArrayList<??lov??k>();
        commands.add(CommandFactory.newSetGlobal("dosp??l??", dosp??l??));
        ??lov??k ??eho?? = new ??lov??k();
        ??eho??.setV??k(30);
        ??eho??.setJm??no("??eho??");
        commands.add(CommandFactory.newInsert(??eho??));
        commands.add(CommandFactory.newFireAllRules());

        ksession.execute(CommandFactory.newBatchExecution(commands));

        // people with age greater than 18 should be added to list of adults
        assertThat(kbase.getRule("org.drools.decisiontable", "p??idej k dosp??l??m")).isNotNull();
        assertThat(5).isEqualTo(dosp??l??.size());
        assertThat("??eho??").isEqualTo(dosp??l??.iterator().next().getJm??no());

        assertThat(kbase.getRule("org.drools.decisiontable", "???????????? ??????")).isNotNull();
        assertThat(kbase.getRule("org.drools.decisiontable", "????????????"));
        assertThat(kbase.getRule("org.drools.decisiontable", "hall?? v??rlden")).isNotNull();
        assertThat(kbase.getRule("org.drools.decisiontable", "?????????? ????????????")).isNotNull();

        ksession.dispose();
    }
	
    public static class ??lov??k {

        private int v??k;
        private String jm??no;

        public void setV??k(int v??k) {
            this.v??k = v??k;
        }

        public int getV??k() {
            return v??k;
        }

        public void setJm??no(String jm??no) {
            this.jm??no = jm??no;
        }

        public String getJm??no() {
            return jm??no;
        }
    }
}
