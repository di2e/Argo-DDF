/**
 * Copyright (C) 2015 Pink Summit, LLC (info@pinksummit.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.di2e.ddf.argo.common;

public class ArgoConstants {
    
    public static final String LOG_CONFIG_UPDATE_PREFIX = "ConfigUpdate:";
    public static final String DEFAULT_PROBE_CONTRACT_ID = "urn:uuid:55f1fecc-bfed-4be0-926b-b36a138a9943";

    public static final String DEFAULT_RESPOND_TO = "/services/argo/response";

    public static final String RESPONSE_ID = "responseID";
    public static final String PROBE_ID = "probeID";
    public static final String RESPONSES = "responses";

    // map keys
    public static final String ID_KEY = "id";
    public static final String SERVICE_CONTRACTID_KEY = "serviceContractID";
    public static final String URL_KEY = "url";
    public static final String IPADDRESS_KEY = "ipAddress";
    public static final String PORT_KEY = "port";
    public static final String SERVICENAME_KEY = "serviceName";
    public static final String DESCRIPTION_KEY = "description";
    public static final String CONTRACT_DESCRIPTION_KEY = "contractDescription";
    public static final String CONSUMABILITY_KEY = "consumability";

    private ArgoConstants() {

    }


}
