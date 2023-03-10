package org.kie.pmml.models.drools.executor;

import java.util.Map;

import org.kie.api.pmml.PMML4Result;
import org.kie.pmml.api.runtime.PMMLRuntimeContext;
import org.kie.pmml.evaluator.core.executor.PMMLModelEvaluator;
import org.kie.pmml.models.drools.commons.model.KiePMMLDroolsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.kie.pmml.evaluator.core.utils.Converter.getUnwrappedParametersMap;

public abstract class DroolsModelEvaluator implements PMMLModelEvaluator<KiePMMLDroolsModel> {

    private static final Logger logger = LoggerFactory.getLogger(DroolsModelEvaluator.class.getName());

    @Override
    public PMML4Result evaluate(final KiePMMLDroolsModel model, final PMMLRuntimeContext pmmlContext) {
        final Map<String, Object> requestData =
                getUnwrappedParametersMap(pmmlContext.getRequestData().getMappedRequestParams());
        return (PMML4Result) model.evaluate(requestData, pmmlContext);
    }
}
