package org.drools.model.codegen.execmodel;

import java.time.Instant;
import java.util.Date;

import org.drools.model.codegen.execmodel.domain.Result;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.Assertions.assertThat;

public class MaterializedLambdaTest extends BaseModelTest {

    public MaterializedLambdaTest(RUN_TYPE testRunType) {
        super(testRunType);
    }

    @Test
    public void testMaterializeLambda() {
        String str =

                "import " + DataType.class.getCanonicalName() + ";\n" +
                "import " + Result.class.getCanonicalName() + ";\n" +
                "global Result result;\n" +
                "rule \"rule1\"\n" +
                "when " + DataType.class.getCanonicalName() + " (\n" +
                "        field1 == \"FF\"\n" +
                "        , field2 == \"BBB\"\n" +
                ")\n" +
                "then\n" +
                "    result.setValue(0);\n" +
                "end\n" +
                "rule \"rule2\"\n" +
                "when " + DataType.class.getCanonicalName() + " (\n" +
                "        field2 == \"BBB\"\n" +
                "        , fieldDate >= \"27-Oct-2019\"\n" +
                ")\n" +
                "then\n" +
                "    result.setValue(0);\n" +
                "end\n";

        KieSession ksession = getKieSession(str);

        DataType st = new DataType("FF", "BBB");
        DataType st2 = new DataType("FF", "CCC");
        ksession.insert(st);
        ksession.insert(st2);

        DataType st3 = new DataType("AA", "CCC", Date.from(Instant.parse("2018-11-30T18:35:24Z")));
        ksession.insert(st3);

        Result r = new Result();
        ksession.setGlobal("result", r);
        assertThat(ksession.fireAllRules()).isEqualTo(1);
        assertThat(r.getValue()).isEqualTo(0);
    }

    public static class Executor {

        public static void execute(Runnable r) {
            r.run();
        }
    }

    // DROOLS-4858
    @Test
    public void testMaterializeLambdaWithNested() {
        String str =
                "import " + Executor.class.getCanonicalName() + ";\n" +
                "import " + Result.class.getCanonicalName() + ";\n" +
                "global Result result;\n" +
                "rule LambdaProblem when\n" +
                "  $i :Integer()\n" +
                "then\n" +
                "    System.out.println($i);\n" +
                "    Executor.execute(() -> { " +
                "       System.out.println(\"Integer is \" + $i);" +
                "        result.setValue($i);" +
                "    });" +
                "end";

        KieSession ksession = getKieSession(str);

        ksession.insert(42);

        Result r = new Result();
        ksession.setGlobal("result", r);

        int actual = ksession.fireAllRules();
        assertThat(actual).isEqualTo(1);
        assertThat(r.getValue()).isEqualTo(42);
    }
}
