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
package net.di2e.ddf.argo.probe.responselistener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import net.di2e.ddf.argo.api.ServiceMapping;
import net.di2e.ddf.argo.common.ArgoConstants;
import net.di2e.ddf.argo.jaxb.Service;
import net.di2e.ddf.argo.jaxb.Services;

import org.apache.commons.lang.StringUtils;
import org.codice.ddf.configuration.impl.ConfigurationWatcherImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ddf.catalog.source.FederatedSource;
import ddf.catalog.source.Source;
import ddf.registry.api.DynamicServiceResolver;

@Path( "/" )
public class ProbeResponseEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger( ProbeResponseEndpoint.class );

    private ConfigurationAdmin configAdmin = null;
    private DynamicServiceResolver serviceResolver = null;
    private List<ServiceMapping> serviceMappings = null;
    private List<FederatedSource> sources = null;
    private ConfigurationWatcherImpl platformConfiguration = null;

    public ProbeResponseEndpoint( ConfigurationAdmin config, ConfigurationWatcherImpl configWatcher, DynamicServiceResolver resolver, List<ServiceMapping> mappings, List<FederatedSource> sourceList ) {
        this.configAdmin = config;
        this.serviceResolver = resolver;
        this.serviceMappings = mappings;
        this.sources = sourceList;
        this.platformConfiguration = configWatcher;
    }

    @POST
    @Consumes( "application/json" )
    public void getJSONServices( String jsonResponse ) {
        LOGGER.debug( "Got a probe response in JSON format:\n{}", jsonResponse );
        JSONArray services = new JSONArray( jsonResponse );
        Set<String> createdSources = new HashSet<String>();
        for ( int i = 0; i < services.length(); i++ ) {
            JSONObject jsonService = services.getJSONObject( i );
            // determine factory pid
            String sourceId = jsonService.getString( ArgoConstants.ID_KEY );
            if ( !sourceIdExists( sourceId, createdSources ) ) {
                String serviceContractId = jsonService.getString( ArgoConstants.SERVICE_CONTRACTID_KEY );
                String serviceType = getServiceType( serviceContractId );
                if ( serviceType != null ) {
                    String pid = serviceResolver.getFactoryPid( serviceType );
                    LOGGER.debug( "Got a factory pid of '{}' for service '{}' with service contract id '{}' so attempting to create new source '{}'", pid, serviceType, serviceContractId, sourceId );
                    createSource( pid, sourceId, jsonService.getString( ArgoConstants.URL_KEY ) );
                    createdSources.add( sourceId );
                } else {
                    LOGGER.debug( "Could not find a Service Type for the Service Contract ID '{}'", serviceContractId );
                }
            } else {
                LOGGER.debug( "A Source with id '{}' already exists, so not creating a new one from probe, but checking if any values should be overridden", sourceId );
                // TODO override properties
            }
        }
    }

    @POST
    @Consumes( "text/xml" )
    public void getXMLServices( Services services ) {
        LOGGER.debug( "Got a probe response in XML format:\n{}", services );
        Set<String> createdSources = new HashSet<String>();
        for ( Service service : services.getService() ) {
            String sourceId = service.getId();
            if ( !sourceIdExists( sourceId, createdSources ) ) {
                String serviceType = getServiceType( service.getContractID() );
                if ( serviceType != null ) {
                    String pid = serviceResolver.getFactoryPid( serviceType );
                    LOGGER.debug( "Got a factory pid of '{}' for service '{}' with service contract id '{}' so attempting to create new source '{}'", pid, serviceType, service.getContractID(),
                            sourceId );
                    createSource( pid, sourceId, service.getUrl() );
                    createdSources.add( sourceId );
                } else {
                    LOGGER.debug( "Could not find a Service Type for the Service Contract ID '{}'", service.getContractID() );
                }
            } else {
                LOGGER.debug( "A Source with id '{}' already exists, so not creating a new one from probe, but checking if any values should be overridden", sourceId );
                // TODO override properties
            }
        }
    }

    private void createSource( String factoryPid, String sourceId, String url ) {
        try {
            // need to use the 2 parameter method and pass in null, otherwise we will get an error
            Configuration siteConfig = configAdmin.createFactoryConfiguration( factoryPid, null );
            Dictionary<String, Object> properties = new Hashtable<>();
            properties.put( "url", url );
            properties.put( "id", sourceId );
            LOGGER.debug( "Creating new source named '{}' that points to '{}'", sourceId, url );
            siteConfig.update( properties );
        } catch ( IOException ioe ) {
            LOGGER.warn( "Could not create new source due to error with config admin.", ioe );
        }
    }

    private String getServiceType( String serviceContractId ) {
        for ( ServiceMapping mapping : serviceMappings ) {
            if ( StringUtils.equals( mapping.getServiceContractId(), serviceContractId ) ) {
                return mapping.getServiceType();
            }
        }
        return null;
    }

    private boolean sourceIdExists( String sourceId, Set<String> newSourceIds ) {
        if ( StringUtils.equals( sourceId, platformConfiguration.getSiteName() ) ) {
            return true;
        }
        for ( Source source : sources ) {
            if ( StringUtils.equals( source.getId(), sourceId ) ) {
                return true;
            }
        }
        if ( newSourceIds.contains( sourceId ) ){
            return true;
        }
        return false;
    }

}