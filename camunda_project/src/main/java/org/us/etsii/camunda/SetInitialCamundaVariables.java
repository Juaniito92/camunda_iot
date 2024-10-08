/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package org.us.etsii.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.us.etsii.camunda.utils.Utils;
import org.us.etsii.camunda.utils.VariableNames;

public class SetInitialCamundaVariables extends CustomJavaDelegate {

    public void customExecute(DelegateExecution execution) throws Exception {
        stgService.setVariable(execution, VariableNames.OBSERVATION_TIMER, Utils.getProperty("observationTimer"));
    }
}
