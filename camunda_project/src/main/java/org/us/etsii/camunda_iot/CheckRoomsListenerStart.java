/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package org.us.etsii.camunda_iot;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.us.etsii.bpm.engine.delegate.CustomJavaDelegate;
import util.Utils;
import util.VariableNames;

public class CheckRoomsListenerStart extends CustomJavaDelegate {

    private static String API_HOST;
    private static String API_PATH;

    private void getProperties() {
        API_HOST = Utils.getProperty("apiHost");
        API_PATH = Utils.getProperty("checkRoomsPath");
        LOGGER.info("URL: " + API_HOST + API_PATH);
    }

    public void customExecute(DelegateExecution execution) throws Exception {
        this.getProperties();
        // URL
        stgService.setVariable(execution, VariableNames.URL, API_HOST + API_PATH);
        // PAYLOAD
        stgService.setVariableJson(execution, VariableNames.CHECK_ROOM_REQUEST, null);
    }
}
