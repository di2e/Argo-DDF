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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class loads up the default mappings for ServiceTypes to Service Contract IDs Right now that mapping jsut
 * includes the CDR Services
 */
public class DefaultServiceMappings {

    public static final String SERVICE_TYPE = "serviceType";
    public static final String SERVICE_CONTRACT_ID = "serviceContractId";

    private static final String MAPPING_PID = "argo-core-servicemapper";

    private static final Logger LOGGER = LoggerFactory.getLogger( DefaultServiceMappings.class );

    private ConfigurationAdmin configAdmin;

    private List<Configuration> configurationList = new ArrayList<>();

    public enum ServiceMap {

        CDR_REST_SEARCH_SERVICE( "CDR REST Search Service", "urn:uuid:b10b8c34-76f4-4e1d-938d-20eee3377d22" ), 
        CDR_REST_BROKERED_SEARCH_SERVICE( "CDR REST Brokered Search Service", "urn:uuid:feaf9379-970b-4bd8-ac90-b002b5f8c190" ),
        CDR_REST_DESCRIBE_SERVICE( "CDR REST Describe Service", "urn:uuid:009c0674-619d-4edc-8ed5-b709b22975ff" );
        
        private final String serviceType;
        private final String serviceContractId;

        ServiceMap( String type, String contractId ) {
            serviceType = type;
            serviceContractId = contractId;
        }

        public String getServiceType() {
            return serviceType;
        }

        public String getServiceContractId() {
            return serviceContractId;
        }

    }

    public DefaultServiceMappings( ConfigurationAdmin configurationAdmin ) {
        configAdmin = configurationAdmin;
    }

    public void init() throws IOException, InvalidSyntaxException {
        if ( !configExists() ) {
            for ( ServiceMap map : ServiceMap.values() ) {
                LOGGER.debug( "Adding default configuration values for Service Mapping with key {} value {}", map.getServiceType(), map.getServiceContractId() );
                Configuration configuration = configAdmin.createFactoryConfiguration( MAPPING_PID );
                Dictionary<String, String> properties = new Hashtable<>();
                properties.put( SERVICE_TYPE, map.getServiceType() );
                properties.put( SERVICE_CONTRACT_ID, map.getServiceContractId() );
                configuration.update( properties );
                configurationList.add( configuration );
            }
        }
    }

    private boolean configExists() throws IOException, InvalidSyntaxException {
        boolean configExists = false;
        String filter = "(" + ConfigurationAdmin.SERVICE_FACTORYPID + "=" + MAPPING_PID+ ")";
        Configuration[] config = configAdmin.listConfigurations( filter );
        if ( config != null && config.length > 0 ) {
            configExists = true;
        }
        LOGGER.debug( "The {} configuration returned {} services so return {} for configExists method", MAPPING_PID, config == null ? 0 : config.length, configExists );
        return configExists;
    }
}
