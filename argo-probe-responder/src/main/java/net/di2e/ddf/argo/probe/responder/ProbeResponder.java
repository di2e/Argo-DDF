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
package net.di2e.ddf.argo.probe.responder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import net.di2e.ddf.argo.api.ServiceMapping;

import org.codice.ddf.configuration.impl.ConfigurationWatcherImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ddf.registry.service.ServiceRegistry;

public class ProbeResponder {

    private static final String MULTICAST_ADDRESS = "multicastAddress";
    private static final String MULTICAST_PORT = "multicastPort";
    private static final String IGNORE_LOCAL_RESPONDTO = "ignoreLocalRespondTo";

    private static final Logger LOGGER = LoggerFactory.getLogger( ProbeResponder.class );

    private ServiceRegistry serviceRegistry;
    private List<ServiceMapping> serviceMappings;
    private MulticastSocket socket;
    private ProbeHandler handler;

    private ConfigurationWatcherImpl platformConfiguration = null;

    private int port = 4003;
    private String address = "230.0.0.1";
    private boolean ignoreLocalProbes = true;

    public ProbeResponder( ServiceRegistry registry, ConfigurationWatcherImpl config, List<ServiceMapping> mappings ) {
        this.serviceRegistry = registry;
        this.platformConfiguration = config;
        this.serviceMappings = mappings;
    }

    public void init() throws IOException {
        startSocket( port, address );
        handler = new ProbeHandler( socket, platformConfiguration, serviceRegistry, serviceMappings, ignoreLocalProbes );
        Thread listenerThread = new Thread( handler );
        listenerThread.start();
    }

    public void destroy() {
        if ( handler != null ) {
            handler.cancel();
        }
        closeSocket( address );
    }
    
    public void updateConfiguration( Map<String, String> properties ) {
        LOGGER.debug( "ProbeResponder updateConfiguratoin called with properties: {}", properties );
        if ( properties != null ) {
            destroy();

            address = properties.get( MULTICAST_ADDRESS );
            Object portValue = properties.get( MULTICAST_PORT );
            if ( portValue != null ){
                try{
                    port = Integer.parseInt( portValue.toString() );
                } catch ( NumberFormatException e ) {
                    LOGGER.warn( "Could not set port value because it is not an integer: " + e.getMessage(), e );
                }
            }

            ignoreLocalProbes = Boolean.TRUE.equals( properties.get( IGNORE_LOCAL_RESPONDTO ) );

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
