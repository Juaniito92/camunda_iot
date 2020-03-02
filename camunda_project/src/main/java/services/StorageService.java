package services;

import static org.camunda.spin.Spin.JSON;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import util.JSON;
import util.Utils;

public class StorageService {

    private final Logger LOGGER = Logger.getLogger(this.getClass().getSimpleName());

    private static final boolean PRINT_LOGGERS = Boolean.valueOf(Utils.getProperty("print_loggers"));

    public StorageService() {
        super();
    }

    /**
     * Save object in scope, you decide print or not the previous-later value.
     * 
     * @param execution {@link DelegateExecution}
     * @param variableName {@link String}
     * @param object {@link Object}
     * @param printPreviousValue {@code boolean} -> true: print / false: not print
     * @param printLaterValue {@code boolean} -> true: print / false: not print
     */
    public void setVariable(DelegateExecution execution, String variableName, Object object, boolean printPreviousValue, boolean printLaterValue) {
        String className = getClassName();
        if (PRINT_LOGGERS && execution.hasVariable(variableName) && printPreviousValue) {
            String str = JSON.writeValueAsString(execution.getVariable(variableName));
            LOGGER.info("Previous value of " + variableName + " in class " + className + ": " + str);
        }
        execution.setVariable(variableName, object);
        if (PRINT_LOGGERS && printLaterValue) {
            String strAfter = JSON.writeValueAsString(execution.getVariable(variableName));
            LOGGER.info("After save " + variableName + " in class " + className + ": " + strAfter);
        }
    }

    /**
     * Save object in scope, always print previous-later value.
     * 
     * @param execution {@link DelegateExecution}
     * @param variableName {@link String}
     * @param object {@link Object}
     */
    public void setVariable(DelegateExecution execution, String variableName, Object object) {
        setVariable(execution, variableName, object, true, true);
    }

    /**
     * Get object from scope of type {@code clazz} Use this for all our ICT models - previous you used {@link #setVariableJson(DelegateExecution, String, Object)} function to save the
     * variable
     * 
     * @param execution {@link DelegateExecution}
     * @param variableName {@link String}
     * @param clazz {@link Class}
     * @return Instance of same object that belongs to {@code clazz} param.
     */
    public <T> T getVariableFromJson(DelegateExecution execution, String variableName, Class<T> clazz) {
        T res = null;
        if (execution.hasVariable(variableName)) {
            res = JSON(execution.getVariable(variableName)).mapTo(clazz);
            nextLogger(execution, variableName);
        }
        return res;
    }

    /**
     * Get generic Object from scope.
     * 
     * @param execution {@link DelegateExecution}
     * @param variableName {@link String}
     * @return {@link Object}
     */
    public Object getVariable(DelegateExecution execution, String variableName) {
        Object res = null;
        if (execution.hasVariable(variableName)) {
            res = execution.getVariable(variableName);
            nextLogger(execution, variableName);
        }
        return res;
    }

    /**
     * Set object in scope as JSON - use this for all our ICT models (prevent error of deserialization using default camunda mapping deserialization).
     * 
     * @param execution {@link DelegateExecution}
     * @param variableName {@link String}
     * @param object {@link Object}
     */
    public void setVariableJson(DelegateExecution execution, String variableName, Object object) {
        previousLogger(execution, variableName);
        execution.setVariable(variableName, JSON(object));
        nextLogger(execution, variableName);
    }

    private String getClassName() {
        StackTraceElement[] s = Thread.currentThread().getStackTrace();
        List<String> res = new ArrayList<>();
        for (int i = 1; i < 6 && i < s.length; i++) {
            res.add(s[i].getClassName());
        }
        return res.stream().filter(c -> StringUtils.contains(c, "FastClaims")).collect(Collectors.joining(" -> "));
    }

    private void nextLogger(DelegateExecution execution, String variableName) {
        String className = getClassName();
        if (PRINT_LOGGERS) {
            String strAfter = JSON.writeValueAsString(execution.getVariable(variableName));
            LOGGER.info("Value of " + variableName + " in class " + className + ": " + strAfter);
        }
    }

    private void previousLogger(DelegateExecution execution, String variableName) {
        String className = getClassName();
        if (PRINT_LOGGERS && execution.hasVariable(variableName)) {
            String str = JSON.writeValueAsString(execution.getVariable(variableName));
            LOGGER.info("Previous value of " + variableName + " in class " + className + ": " + str);
        }
    }
}
