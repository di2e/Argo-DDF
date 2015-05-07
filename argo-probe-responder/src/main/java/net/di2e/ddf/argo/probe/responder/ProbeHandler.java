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
import java.io.StringReader;
import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXB;

import net.di2e.ddf.argo.api.ServiceMapping;
import net.di2e.ddf.argo.common.ArgoConstants;
import net.di2e.ddf.argo.jaxb.Consumability;
import net.di2e.ddf.argo.jaxb.PayloadType;
import net.di2e.ddf.argo.jaxb.Probe;
import net.di2e.ddf.argo.jaxb.Service;
import net.di2e.ddf.argo.jaxb.Services;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codice.ddf.configuration.impl.ConfigurationWatcherImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ddf.registry.service.ServiceInfo;
import ddf.registry.service.ServiceRegistry;

public class ProbeHandler implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger( ProbeHandler.class );

    private MulticastSocket socket;
    private boolean isCanceled = false;
    private List<String> ignoreProbesList = null;

    private ConfigurationWatcherImpl platformConfiguration = null;
    private List<ServiceMapping> serviceMappings = null;
    private ServiceRegistry serviceRegistry = null;

    public ProbeHandler( MulticastSocket socket, ConfigurationWatcherImpl config, ServiceRegistry registry, List<ServiceMapping> mappings, List<String> ignoreList ) {
        this.socket = socket;
        this.platformConfiguration = config;
        this.ignoreProbesList = ignoreList;
        this.serviceRegistry = registry;
        this.serviceMappings = mappings;
    }

    public void cancel() {
        isCanceled = true;
    }

    @Override
    public void run() {
        LOGGER.debug( "Listening for any multicast packets." );
        while ( !isCanceled ) {
            try {
                byte buf[] = new byte[1024];
                DatagramPacket pack = new DatagramPacket( buf, buf.length );
                socket.receive( pack );
                String data = new String( pack.getData(), pack.getOffset(), pack.getLength() );
                LOGGER.debug( "Packet received with the following payload: {}.", data );
                Probe probe = JAXB.unmarshal( new StringReader( data ), Probe.class );
                String respondTo = probe.getRespondTo();

                boolean ignoreProbe = false;
                if ( ignoreProbesList != null && !ignoreProbesList.isEmpty() ) {
                    for ( String ignoreProbeString : ignoreProbesList ) {
                        String updatedIgnoreString = exapndRespondToAddress( ignoreProbeString );
                        // TODO cache the request ID and use that instead of the local hostname
                        if ( StringUtils.equalsIgnoreCase( updatedIgnoreString, respondTo ) ) {
                            LOGGER.debug( "Configuration is set to ignore probes that have a respondTo of '{}'. Incoming probe contains respondTo of '{}'. IGNORING PROBE.", ignoreProbeString,
                                    respondTo );
                            ignoreProbe = true;
                        }
                    }
                }
                if ( !ignoreProbe ) {
                    List<String> contractIDs = probe.getServiceContractID();
                    // TODO handle the different contractID
                    // URI contractId = probe.getContractID();
                    String probeId = probe.getId();
                    String response = generateServiceResponse( probe.getRespondToPayloadType(), contractIDs, probeId );

                    if ( StringUtils.isNotBlank( response ) ) {
                        LOGGER.debug( "Returning back to {} with the following response:\n{}", probe.getRespondTo(), response );
                        sendResponse( respondTo, response, probe.getRespondToPayloadType() );
                    } else {
                        LOGGER.debug( "No services found that match the incoming contract IDs, not sending a response." );
                    }
                }
            } catch ( SocketTimeoutException ste ) {
                LOGGER.trace( "Received timeout on socket, resetting socket to check for cancellation." );
            } catch ( IOException ioe ) {
                if ( !isCanceled ) {
                    LOGGER.warn( "Error while trying to receive a packet, shutting down listener", ioe );
                }
                break;
            }
        }
        if ( isCanceled ) {
            LOGGER.debug( "Listener was canceled, not receiving any more multicast packets." );
        }

    }

    private String generateServiceResponse( PayloadType payloadType, List<String> contractIDs, String probeId ) {
        String response = null;

        List<Service> responseServiceList = new ArrayList<Service>( getServiceList() );
        if ( !contractIDs.isEmpty() ) {
            Iterator<Service> serviceIterator = responseServiceList.iterator();
            while ( serviceIterator.hasNext() ) {
                Service curService = serviceIterator.next();
                if ( !contractIDs.contains( curService.getContractID() ) ) {
                    serviceIterator.remove();
                }
            }
            if ( responseServiceList.isEmpty() ) {
                // no sites were found that match, return empty response
                LOGGER.debug( "No Service matches were found for, so not sending a response for contractIDs {} and probeId {}", contractIDs, probeId );
                return null;
            }
        }

        switch ( payloadType ) {
        case XML:
            response = getXMLServices( responseServiceList, probeId );
            break;
        case JSON:
            response = getJSONServices( responseServiceList, probeId );
            break;
        default:
            response = getXMLServices( responseServiceList, probeId );
            break;
        }
        return response;
    }

    /**
     * Generates a XML formatted list of services.
     *
     * @return XML formatted string
     */
    private String getXMLServices( List<Service> serviceList, String probeId ) {
        LOGGER.debug( "Generating an XML-formatted list of services." );

        Services services = new Services();
        services.setProbeID( probeId );
        services.setResponseID( "urn:uuid:" + UUID.randomUUID() );
        services.getService().addAll( serviceList );

        StringWriter writer = new StringWriter();
        JAXB.marshal( services, writer );
        return writer.toString();
    }

    /**
     * Generates a JSON-formatted list of services.
     *
     * @return JSON list of services.
     */
    private String getJSONServices( List<Service> serviceInfoList, String probeId ) {
        LOGGER.debug( "Generating a JSON-formatted list of services." );

        JSONObject jsonObject = new JSONObject();
        jsonObject.put( ArgoConstants.RESPONSE_ID, "urn:uuid:" + UUID.randomUUID() );
        jsonObject.put( ArgoConstants.PROBE_ID, probeId );

        JSONArray servicesArray = new JSONArray();
        for ( Service service : serviceInfoList ) {
            Map<String, Object> serviceMap = new HashMap<String, Object>();
            serviceMap.put( ArgoConstants.SERVICE_CONTRACTID_KEY, service.getContractID() );
            serviceMap.put( ArgoConstants.ID_KEY, service.getId() );

            serviceMap.put( ArgoConstants.URL_KEY, service.getUrl() );
            serviceMap.put( ArgoConstants.IPADDRESS_KEY, service.getIpAddress() );
            serviceMap.put( ArgoConstants.PORT_KEY, service.getPort() );
            serviceMap.put( ArgoConstants.SERVICENAME_KEY, service.getServiceName() );
            serviceMap.put( ArgoConstants.DESCRIPTION_KEY, service.getDescription() );
            serviceMap.put( ArgoConstants.CONTRACT_DESCRIPTION_KEY, service.getContractDescription() );
            serviceMap.put( ArgoConstants.CONSUMABILITY_KEY, service.getConsumability().value() );
            servicesArray.put( serviceMap );
        }
        jsonObject.put( ArgoConstants.RESPONSES, servicesArray );
        return jsonObject.toString();
    }

    /**
     * Sends the response using an HTTP POST
     *
     * @param endpoint
     *            URI of the service to send the POST to
     * @param responseStr
     *            Content for the response message
     * @param payloadType
     *            Encoding type of the response, should be either XML or JSON.
     */
    private void sendResponse( String endpoint, String responseStr, PayloadType payloadType ) {
        ContentType contentType;
        switch ( payloadType ) {
        case XML:
            contentType = ContentType.TEXT_XML;
            break;
        case JSON:
            contentType = ContentType.APPLICATION_JSON;
            break;
        default:
            // it shouldn't get here, but send back as xml
            contentType = ContentType.TEXT_XML;
            break;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost( endpoint );
        httpPost.setEntity( new ByteArrayEntity( responseStr.getBytes(), contentType ) );

        try {
            CloseableHttpResponse response = httpClient.execute( httpPost );
            LOGGER.debug( "Response sent." );
            response.close();
        } catch ( Exception e ) {
            LOGGER.warn( "Could not send message to server at " + endpoint.toString(), e );
        }
    }

    public List<Service> getServiceList() {
        List<Service> serviceList = new ArrayList<Service>();
        if ( serviceRegistry != null && serviceRegistry.getServices() != null ) {
            for ( ServiceInfo serviceInfo : serviceRegistry.getServices() ) {
                String contractId = getContractId( serviceInfo );
                if ( contractId != null ) {
                    Service service = new Service();
                    service.setContractID( contractId );
                    service.setId( platformConfiguration.getSiteName() );

                    service.setUrl( platformConfiguration.getProtocol() + platformConfiguration.getHostname() + ":" + platformConfiguration.getPort() + serviceInfo.getServiceRelativeUrl() );
                    service.setIpAddress( platformConfiguration.getHostname() );
                    service.setPort( platformConfiguration.getPort() );
                    service.setServiceName( platformConfiguration.getSiteName() );
                    service.setDescription( getServiceDescription( serviceInfo ) );
                    service.setContractDescription( serviceInfo.getServiceType() );
                    service.setConsumability( Consumability.MACHINE_CONSUMABLE );
                    serviceList.add( service );

                } else {
                    LOGGER.debug( "Could not find a Service ContractId for Service Type '{}'", serviceInfo.getServiceType() );
                }
            }
        } else {
            LOGGER.info( "Service Registry is empty, meaning no services exist, so cannot create any services from Argo response" );
        }
        return serviceList;
    }

    private String getServiceDescription( ServiceInfo serviceInfo ) {
        StringBuilder sb = new StringBuilder();
        sb.append( platformConfiguration.getSiteName() + " " + serviceInfo.getServiceType() + " Implementation" );
        String desc = serviceInfo.getServiceDescription();
        if ( desc != null ) {
            sb.append( " - " + desc );
        }
        return sb.toString();
    }

    private String getContractId( ServiceInfo serviceInfo ) {
        String serviceType = serviceInfo.getServiceType();
        if ( serviceType != null ){
            for ( ServiceMapping mapping : serviceMappings ) {
                if ( StringUtils.equals( serviceType, mapping.getServiceType() ) ) {
                    return mapping.getServiceContractId();
                }
            }
        }
        return null;
    }

    // TODO this is a duplicate method of the one in ProbeGenerator
    public String exapndRespondToAddress( String respondTo ) {
        String address = null;
        if ( respondTo != null ) {
            if ( respondTo.startsWith( "/" ) ) {
                address = platformConfiguration.getProtocol() + platformConfiguration.getHostname() + ":" + platformConfiguration.getPort() + respondTo;
            } else {
                address = respondTo;
            }
        }
        LOGGER.debug( "Exapnding the respondTo address in the probe to '{}' based on the respondTo configuration value '{}'", address, respondTo );
        return address;
    }

}
