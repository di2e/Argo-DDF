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
package net.di2e.ddf.argo.probe.responder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.di2e.ddf.argo.api.ServiceMapping;
import net.di2e.ddf.argo.common.ArgoConstants;

import org.apache.commons.lang.StringUtils;
import org.codice.ddf.configuration.impl.ConfigurationWatcherImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ddf.registry.service.ServiceRegistry;

public class ProbeResponder {

    private static final String MULTICAST_ADDRESS = "multicastAddress";
    private static final String MULTICAST_PORT = "multicastPort";
    private static final String IGNORE_PROBES_LIST = "ignoreProbesList";

    private static final Logger LOGGER = LoggerFactory.getLogger( ProbeResponder.class );

    private ServiceRegistry serviceRegistry;
    private List<ServiceMapping> serviceMappings;
    private MulticastSocket socket;
    private ProbeHandler handler;

    private ConfigurationWatcherImpl platformConfiguration = null;

    private int port = 4003;
    private String address = "230.0.0.1";
    private List<String> ignoreProbesList = null;

    public ProbeResponder( ServiceRegistry registry, ConfigurationWatcherImpl config, List<ServiceMapping> mappings ) {
        this.serviceRegistry = registry;
        this.platformConfiguration = config;
        this.serviceMappings = mappings;
        ignoreProbesList = new ArrayList<String>();
        ignoreProbesList.add( ArgoConstants.DEFAULT_RESPOND_TO );
    }

    public void init() throws IOException {
        startSocket( port, address );
        handler = new ProbeHandler( socket, platformConfiguration, serviceRegistry, serviceMappings, ignoreProbesList );
        Thread listenerThread = new Thread( handler );
        listenerThread.start();
    }

    public void destroy() {
        if ( handler != null ) {
            handler.cancel();
        }
        closeSocket( address );
    }
    
    public void setIgnoreProbesList( List<String> ignoreList ) {
        LOGGER.debug( "ConfigUpdate: Updating the IgnoreProbesList values from {} to {}", ignoreProbesList, ignoreList );
        ignoreProbesList = ignoreList;
    }

    public void setMulticastPort( Integer p ) {
        if ( p != null ) {
            LOGGER.debug( "ConfigUpdate: Updating the probe responder listen port value from {} to {}", port, p );
            port = p;
        }
    }

    public void setMulticastAddress( String a ) {
        if ( StringUtils.isNotBlank( a ) ) {
            LOGGER.debug( "ConfigUpdate: Updating the probe responder multicast address value from {} to {}", this.address, a );
            address = a;
        }
    }

    public void updateConfiguration( Map<String, Object> properties ) {
        LOGGER.debug( "{} ProbeResponder updateConfiguration called with properties: {}", ArgoConstants.LOG_CONFIG_UPDATE_PREFIX, properties );
        if ( properties != null ) {
            destroy();

            address = (String) properties.get( MULTICAST_ADDRESS );
            Object portValue = properties.get( MULTICAST_PORT );
            if ( portValue != null ){
                try{
                    port = Integer.parseInt( portValue.toString() );
                } catch ( NumberFormatException e ) {
                    LOGGER.warn( "Could not set port value because it is not an integer: " + e.getMessage(), e );
                }
            }
            String[] ignoreArray = (String[]) properties.get( IGNORE_PROBES_LIST );
            setIgnoreProbesList( ignoreArray == null ? new ArrayList<String>() : Arrays.asList( ignoreArray ) );

            try {
                init();
            } catch ( IOException e ) {
                LOGGER.warn( "Could not restart Probe Responder multicast listener: " + e.getMessage(), e );
            }
        }
        
    }

    private void startSocket( int port, String address ) throws IOException {
        if ( socket != null && socket.isConnected() ) {
            LOGGER.debug( "Cannot try to connect an already connected socket." );
            return;
        } else {
            LOGGER.debug( "Starting up multicast socket listener at {}:{}", address, port );
            socket = new MulticastSocket( port );
            socket.joinGroup( InetAddress.getByName( address ) );
        }
    }

    private void closeSocket( String address ) {
        if ( socket != null ) {
            try {
                LOGGER.debug( "Shutting down multicast socket listener at {}", address );
                socket.leaveGroup( InetAddress.getByName( address ) );
                socket.close();
            } catch ( UnknownHostException uhe ) {
                LOGGER.debug( "Error while trying to leave the multicast group.", uhe );
            } catch ( IOException ioe ) {
                LOGGER.debug( "Error while trying to leave the multicast group.", ioe );
            } catch ( RuntimeException e ) {
                LOGGER.warn( "Error while trying to leave the multicast group.", e );
            }
        }
    }

}
