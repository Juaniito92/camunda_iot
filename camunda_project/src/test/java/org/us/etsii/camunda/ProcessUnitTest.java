package org.us.etsii.camunda;

import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.init;
import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.processEngine;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.us.etsii.camunda.ProcessConstants;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
public class ProcessUnitTest {

    static {
        LogFactory.useSlf4jLogging(); // MyBatis
    }

    @ClassRule
    @Rule
    public static ProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder.create().build();

    @Before
    public void setup() {
        init(rule.getProcessEngine());
    }

    // @Test
    @Deployment(resources = "process.bpmn")
    public void testHappyPath() {
        // Drive the process by API and assert correct behavior by camunda-bpm-assert

        ProcessInstance processInstance = processEngine().getRuntimeService()
                .startProcessInstanceByKey(ProcessConstants.PROCESS_DEFINITION_KEY);

        assertThat(processInstance).isEnded();
    }

}
