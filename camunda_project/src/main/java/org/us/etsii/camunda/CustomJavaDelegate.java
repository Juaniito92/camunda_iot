package org.us.etsii.camunda;

import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.us.etsii.camunda.services.StorageService;

public abstract class CustomJavaDelegate implements JavaDelegate {

    protected final Logger LOGGER = Logger.getLogger(this.getClass().getSimpleName());
    protected StorageService stgService;
    public static final String ERROR_CODE = "java.lang.Exception";

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            this.stgService = new StorageService();
            this.customExecute(execution);
        } catch (Exception e) {
            LOGGER.info("\n\n  ... LoggerDelegate invoked by processDefinitionId=" + execution.getProcessDefinitionId()
                    + ",\n activtyId=" + execution.getCurrentActivityId()
                    + ",\n activtyName='" + execution.getCurrentActivityName() + "'"
                    + ",\n processInstanceId=" + execution.getProcessInstanceId()
                    + ",\n businessKey=" + execution.getProcessBusinessKey()
                    + ",\n executionId=" + execution.getId()
                    + ",\n exception=" + ExceptionUtils.getStackTrace(e)
                    + "\n\n");
            String errorMessage = StringUtils.substring(ExceptionUtils.getStackTrace(e), 0, 3999).replace("\n", ",").replace("\r", "");
            throw new BpmnError(ERROR_CODE, errorMessage);
        }
    }

    protected abstract void customExecute(DelegateExecution execution) throws Exception;


}
