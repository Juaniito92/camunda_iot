package org.us.etsii.bpm.engine.delegate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import services.HttpConnectorService;
import util.Utils;
import util.VariableNames;

public abstract class AgilaServerConnectorJavaDelegate extends CustomJavaDelegate {

    protected HttpConnectorService httpConnectorService = new HttpConnectorService();

    protected HttpResponse sendPost(DelegateExecution execution, String pathUrl, String json, List<File> files) throws IOException {
        String apiUrl = Utils.getProperty("icoreproxy_url");
        String xauth = "" + this.stgService.getVariable(execution, VariableNames.XAUTH);
        return httpConnectorService.sendJsonPost(apiUrl, pathUrl, xauth, json, files);
    }

    protected HttpResponse sendGet(DelegateExecution execution, String pathUrl) throws IOException {
        String apiUrl = Utils.getProperty("icoreproxy_url");
        String xauth = "" + this.stgService.getVariable(execution, VariableNames.XAUTH);
        return httpConnectorService.sendGet(apiUrl, pathUrl, xauth);
    }

}
