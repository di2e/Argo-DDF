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
package net.di2e.ddf.argo.probe.generator;

import java.io.IOException;
import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

import net.di2e.ddf.argo.common.ArgoConstants;
import net.di2e.ddf.argo.jaxb.PayloadType;
import net.di2e.ddf.argo.jaxb.Probe;

import org.apache.commons.io.IOUtils;
import org.codice.ddf.configuration.impl.ConfigurationWatcherImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProbeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger( ProbeGenerator.class );

    private static final int NUM_OF_EXECUTOR_THREADS = 1;

    private int port = 4003;
    private String address = "230.0.0.1";
    private int ttl = 255;
    private int sendFrequency = 300;
    private String respondTo = null;

    private String probeFormatVersion = ArgoConstants.DEFAULT_PROBE_CONTRACT_ID;
    private String responseFormat = "XML";
    private List<String> serviceContractIds = null;

    private ConfigurationWatcherImpl platformConfiguration = null;

    private ProbeSender sender = new ProbeSender();
    private ScheduledFuture<?> currentTask;
    private ScheduledThreadPoolExecutor executor;

    public ProbeGenerator( String to, ConfigurationWatcherImpl config ) {
        if ( config == null ){
            throw new IllegalArgumentException( "The ConfigurationWatcherImpl parameter cannot be null for ProbeGenerator constructor" );
        }
        serviceContractIds = new ArrayList<String>();
        platformConfiguration = config;
        respondTo = to;
    }

    /**
     * Starts the timer to send out probes on a set interval.
     */
    public void start() {
        executor = new ScheduledThreadPoolExecutor( NUM_OF_EXECUTOR_THREADS );
        LOGGER.debug( "Starting ProbeGenerator to send out probe every {} seconds.", sendFrequency );
        currentTask = executor.scheduleAtFixedRate( sender, 1, sendFrequency, TimeUnit.SECONDS );
    }

    /**
     * Stops the timer and cancels any probes from being sent out.
     */
    public void stop() {
        LOGGER.debug( "Shutting down ProbeGenerator" );
        executor.shutdownNow();
    }

    public void setMulticastAddress( String multicastAddress ) {
        LOGGER.debug( "Shutting down ProbeGenerator" );
        address = multicastAddress;
    }

    public void setMulticastPort( int p ) {
        LOGGER.debug( "{} setting multicast port from '{}' to '{}'", ArgoConstants.LOG_CONFIG_UPDATE_PREFIX, this.port, p );
        this.port = p;
    }

    public void setSendFrequency( int freq ) {
        if ( this.sendFrequency != freq ) {
            LOGGER.debug( "{} setting send frequence port from '{}' to '{}' and restarting probe generator thread", ArgoConstants.LOG_CONFIG_UPDATE_PREFIX, sendFrequency, freq );
            this.sendFrequency = freq;
            currentTask.cancel( true );
            currentTask = executor.scheduleAtFixedRate( sender, 1, sendFrequency, TimeUnit.SECONDS );
        }
    }

    public void setProbeTTL( int seconds ) {
        LOGGER.debug( "{} setting probe TTL from '{}' to '{}'", ArgoConstants.LOG_CONFIG_UPDATE_PREFIX, ttl, seconds );
        this.ttl = seconds;
    }

    public void setRespondTo( String to ) {
        LOGGER.debug( "{} setting probe respondTo from '{}' to '{}'", ArgoConstants.LOG_CONFIG_UPDATE_PREFIX, respondTo, to );
        this.respondTo = to;
    }

    public void setProbeFormatVersion( String version ) {
        LOGGER.debug( "{} setting probeFormatVersion from '{}' to '{}'", ArgoConstants.LOG_CONFIG_UPDATE_PREFIX, probeFormatVersion, version );
        this.probeFormatVersion = version;
    }

    public void setResponseFormat( String format ) {
        LOGGER.debug( "{} setting probe responseFormat from '{}' to '{}'", ArgoConstants.LOG_CONFIG_UPDATE_PREFIX, responseFormat, format );
        this.responseFormat = format;
    }

    public void setServiceContractIds( List<String> ids ) {
        LOGGER.debug( "{} setting Service Contract ID from '{}' to '{}'", ArgoConstants.LOG_CONFIG_UPDATE_PREFIX, serviceContractIds, ids );
        this.serviceContractIds = ids;
    }

    public String getRespondToAddress() {
        String address = null;
        if ( respondTo != null ) {
            if ( respondTo.startsWith( "/" ) ) {
                address = platformConfiguration.getProtocol() + platformConfiguration.getHostname() + ":" + platformConfiguration.getPort() + respondTo;
            } else {
                address = respondTo;
            }
        }
        LOGGER.debug( "Setting the respondTo address in the probe to '{}' based on the respondTo configuration value '{}'", address, respondTo );
        return address;
    }

    private class ProbeSender implements Runnable {

        public void run() {
            MulticastSocket socket = null;
            if ( respondTo != null ) {
                try {
                    socket = new MulticastSocket();
                    socket.setTimeToLive( ttl );
                    Probe probe = new Probe();
                    probe.setRespondTo( getRespondToAddress() );
                    probe.setId( "urn:uuid:" + UUID.randomUUID() );
                    probe.setContractID( probeFormatVersion );
                    probe.setRespondToPayloadType( PayloadType.fromValue( responseFormat ) );
                    for ( String serviceContractId : serviceContractIds ) {
                        probe.getServiceContractID().add( serviceContractId );
                    }

                    StringWriter writer = new StringWriter();
                    JAXB.marshal( probe, writer );
                    String probeXml = writer.toString();
                    byte[] bytes = probeXml.getBytes( StandardCharsets.UTF_8 );
                    DatagramPacket packet = new DatagramPacket( bytes, bytes.length, InetAddress.getByName( address ), port );
                    if ( LOGGER.isDebugEnabled() ) {
                        LOGGER.debug( "Sending multicast packet out to {}:{}", address, port );
                        LOGGER.debug( "Sending probe message: {}", probeXml );
                    }

                    socket.send( packet );
                } catch ( IOException | DataBindingException e ) {
                    LOGGER.warn( "Encountered an error while trying to send out the probe. Could not send multicast message.", e );
                } finally {
                    IOUtils.closeQuietly( socket );
                }
            }
        }
    }

}
