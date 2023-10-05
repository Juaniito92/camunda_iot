package org.us.etsii.camunda.services;

import static org.camunda.spin.Spin.JSON;
import org.camunda.bpm.engine.delegate.DelegateExecution;

public class StorageService {

    public StorageService() {
        super();
    }

    public void setVariable(DelegateExecution execution, String variableName, Object object, boolean printPreviousValue, boolean printLaterValue) {
        execution.setVariable(variableName, object);
    }

    public void setVariable(DelegateExecution execution, String variableName, Object object) {
        setVariable(execution, variableName, object, true, true);
    }

    public <T> T getVariableFromJson(DelegateExecution execution, String variableName, Class<T> clazz) {
        T res = null;
        if (execution.hasVariable(variableName)) {
            res = JSON(execution.getVariable(variableName)).mapTo(clazz);
        }
        return res;
    }

    public Object getVariable(DelegateExecution execution, String variableName) {
        Object res = null;
        if (execution.hasVariable(variableName)) {
            res = execution.getVariable(variableName);
        }
        return res;
    }

    public void setVariableJson(DelegateExecution execution, String variableName, Object object) {
        execution.setVariable(variableName, JSON(object));
    }

}
