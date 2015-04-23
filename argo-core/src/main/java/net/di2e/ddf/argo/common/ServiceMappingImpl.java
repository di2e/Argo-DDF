/**
 * Copyright (C) 2014 Cohesive Integrations, LLC (info@cohesiveintegrations.com)
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

import net.di2e.ddf.argo.api.ServiceMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceMappingImpl implements ServiceMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger( ServiceMappingImpl.class );

    private String serviceContractId;

    private String serviceType;

    public ServiceMappingImpl() {
        LOGGER.debug( "Creating a new ServiceMapping configuration." );
    }

    public void setServiceType( String type ) {
        this.serviceType = type;
    }

    public void setServiceContractId( String contractId ) {
        this.serviceContractId = contractId;
    }

    @Override
    public String getServiceType() {
        return serviceType;
    }

    @Override
    public String getServiceContractId() {
        return serviceContractId;
    }

}