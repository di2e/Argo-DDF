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

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.di2e.ddf.argo.jaxb.response package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.di2e.ddf.argo.jaxb.response
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Services }
     * 
     */
    public Services createServices() {
        return new Services();
    }

    /**
     * Create an instance of {@link Services.Service }
     * 
     */
    public Services.Service createServicesService() {
        return new Services.Service();
    }

    /**
     * Create an instance of {@link Services.Service.AccessPoints }
     * 
     */
    public Services.Service.AccessPoints createServicesServiceAccessPoints() {
        return new Services.Service.AccessPoints();
    }

    /**
     * Create an instance of {@link Services.Service.AccessPoints.AccessPoint }
     * 
     */
    public Services.Service.AccessPoints.AccessPoint createServicesServiceAccessPointsAccessPoint() {
        return new Services.Service.AccessPoints.AccessPoint();
    }

    /**
     * Create an instance of {@link Services.Service.AccessPoints.AccessPoint.Data }
     * 
     */
    public Services.Service.AccessPoints.AccessPoint.Data createServicesServiceAccessPointsAccessPointData() {
        return new Services.Service.AccessPoints.AccessPoint.Data();
    }

}
