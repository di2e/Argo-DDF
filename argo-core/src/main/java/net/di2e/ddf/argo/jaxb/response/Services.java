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

package net.di2e.ddf.argo.jaxb.response;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="service" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="serviceName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="contractDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="consumability" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="ttl" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="accessPoints">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="accessPoint" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="ipAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="port" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *                                       &lt;element name="data">
 *                                         &lt;complexType>
 *                                           &lt;simpleContent>
 *                                             &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                                               &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                             &lt;/extension>
 *                                           &lt;/simpleContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                     &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="contractID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="responseID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="probeID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="responseID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="probeID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "service"
})
@XmlRootElement(name = "services")
public class Services {

    public static final String HUMAN_CONSUMABLE = "HUMAN_CONSUMABLE";
    public static final String MACHINE_CONSUMABLE = "MACHINE_CONSUMABLE";

    protected List<Services.Service> service;
    @XmlAttribute(name = "responseID")
    protected String responseID;
    @XmlAttribute(name = "probeID")
    protected String probeID;

    /**
     * Gets the value of the service property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the service property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Services.Service }
     * 
     * 
     */
    public List<Services.Service> getService() {
        if (service == null) {
            service = new ArrayList<Services.Service>();
        }
        return this.service;
    }

    /**
     * Gets the value of the responseID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseID() {
        return responseID;
    }

    /**
     * Sets the value of the responseID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseID(String value) {
        this.responseID = value;
    }

    /**
     * Gets the value of the probeID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbeID() {
        return probeID;
    }

    /**
     * Sets the value of the probeID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbeID(String value) {
        this.probeID = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="serviceName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="contractDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="consumability" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="ttl" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="accessPoints">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="accessPoint" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="ipAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="port" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
     *                             &lt;element name="data">
     *                               &lt;complexType>
     *                                 &lt;simpleContent>
     *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                                     &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                                   &lt;/extension>
     *                                 &lt;/simpleContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                           &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="contractID" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="responseID" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="probeID" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "serviceName",
        "description",
        "contractDescription",
        "consumability",
        "ttl",
        "accessPoints"
    })
    public static class Service {

        @XmlElement(required = true)
        protected String serviceName;
        @XmlElement(required = true)
        protected String description;
        @XmlElement(required = true)
        protected String contractDescription;
        @XmlElement(required = true)
        protected String consumability;
        @XmlElement(required = true)
        protected String ttl;
        @XmlElement(required = true)
        protected Services.Service.AccessPoints accessPoints;
        @XmlAttribute(name = "id")
        protected String id;
        @XmlAttribute(name = "contractID")
        protected String contractID;
        @XmlAttribute(name = "responseID")
        protected String responseID;
        @XmlAttribute(name = "probeID")
        protected String probeID;

        /**
         * Gets the value of the serviceName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getServiceName() {
            return serviceName;
        }

        /**
         * Sets the value of the serviceName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setServiceName(String value) {
            this.serviceName = value;
        }

        /**
         * Gets the value of the description property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets the value of the description property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescription(String value) {
            this.description = value;
        }

        /**
         * Gets the value of the contractDescription property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getContractDescription() {
            return contractDescription;
        }

        /**
         * Sets the value of the contractDescription property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setContractDescription(String value) {
            this.contractDescription = value;
        }

        /**
         * Gets the value of the consumability property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getConsumability() {
            return consumability;
        }

        /**
         * Sets the value of the consumability property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setConsumability(String value) {
            this.consumability = value;
        }

        /**
         * Gets the value of the ttl property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTtl() {
            return ttl;
        }

        /**
         * Sets the value of the ttl property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTtl(String value) {
            this.ttl = value;
        }

        /**
         * Gets the value of the accessPoints property.
         * 
         * @return
         *     possible object is
         *     {@link Services.Service.AccessPoints }
         *     
         */
        public Services.Service.AccessPoints getAccessPoints() {
            return accessPoints;
        }

        /**
         * Sets the value of the accessPoints property.
         * 
         * @param value
         *     allowed object is
         *     {@link Services.Service.AccessPoints }
         *     
         */
        public void setAccessPoints(Services.Service.AccessPoints value) {
            this.accessPoints = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the contractID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getContractID() {
            return contractID;
        }

        /**
         * Sets the value of the contractID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setContractID(String value) {
            this.contractID = value;
        }

        /**
         * Gets the value of the responseID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getResponseID() {
            return responseID;
        }

        /**
         * Sets the value of the responseID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setResponseID(String value) {
            this.responseID = value;
        }

        /**
         * Gets the value of the probeID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProbeID() {
            return probeID;
        }

        /**
         * Sets the value of the probeID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProbeID(String value) {
            this.probeID = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="accessPoint" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="ipAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="port" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
         *                   &lt;element name="data">
         *                     &lt;complexType>
         *                       &lt;simpleContent>
         *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *                           &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                         &lt;/extension>
         *                       &lt;/simpleContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *                 &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "accessPoint"
        })
        public static class AccessPoints {

            protected List<Services.Service.AccessPoints.AccessPoint> accessPoint;

            /**
             * Gets the value of the accessPoint property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the accessPoint property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAccessPoint().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Services.Service.AccessPoints.AccessPoint }
             * 
             * 
             */
            public List<Services.Service.AccessPoints.AccessPoint> getAccessPoint() {
                if (accessPoint == null) {
                    accessPoint = new ArrayList<Services.Service.AccessPoints.AccessPoint>();
                }
                return this.accessPoint;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="ipAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="port" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
             *         &lt;element name="data">
             *           &lt;complexType>
             *             &lt;simpleContent>
             *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
             *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
             *               &lt;/extension>
             *             &lt;/simpleContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "ipAddress",
                "port",
                "url",
                "data"
            })
            public static class AccessPoint {

                @XmlElement(required = true)
                protected String ipAddress;
                @XmlElement(required = true)
                protected String port;
                @XmlElement(required = true, type = String.class)
                @XmlJavaTypeAdapter(Adapter1 .class)
                @XmlSchemaType(name = "anyURI")
                protected URI url;
                @XmlElement(required = true)
                protected Services.Service.AccessPoints.AccessPoint.Data data;
                @XmlAttribute(name = "label")
                protected String label;

                /**
                 * Gets the value of the ipAddress property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getIpAddress() {
                    return ipAddress;
                }

                /**
                 * Sets the value of the ipAddress property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setIpAddress(String value) {
                    this.ipAddress = value;
                }

                /**
                 * Gets the value of the port property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPort() {
                    return port;
                }

                /**
                 * Sets the value of the port property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPort(String value) {
                    this.port = value;
                }

                /**
                 * Gets the value of the url property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public URI getUrl() {
                    return url;
                }

                /**
                 * Sets the value of the url property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setUrl(URI value) {
                    this.url = value;
                }

                /**
                 * Gets the value of the data property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Services.Service.AccessPoints.AccessPoint.Data }
                 *     
                 */
                public Services.Service.AccessPoints.AccessPoint.Data getData() {
                    return data;
                }

                /**
                 * Sets the value of the data property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Services.Service.AccessPoints.AccessPoint.Data }
                 *     
                 */
                public void setData(Services.Service.AccessPoints.AccessPoint.Data value) {
                    this.data = value;
                }

                /**
                 * Gets the value of the label property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getLabel() {
                    return label;
                }

                /**
                 * Sets the value of the label property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setLabel(String value) {
                    this.label = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;simpleContent>
                 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
                 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
                 *     &lt;/extension>
                 *   &lt;/simpleContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "value"
                })
                public static class Data {

                    @XmlValue
                    protected String value;
                    @XmlAttribute(name = "type")
                    protected String type;

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the type property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getType() {
                        return type;
                    }

                    /**
                     * Sets the value of the type property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setType(String value) {
                        this.type = value;
                    }

                }

            }

        }

    }

}
