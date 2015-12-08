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
package net.di2e.ddf.argo.jaxb.probe;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.di2e.ddf.argo.jaxb.probe package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.di2e.ddf.argo.jaxb.probe
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Probe }
     * 
     */
    public Probe createProbe() {
        return new Probe();
    }

    /**
     * Create an instance of {@link Probe.Ra }
     * 
     */
    public Probe.Ra createProbeRa() {
        return new Probe.Ra();
    }

    /**
     * Create an instance of {@link Probe.Scids }
     * 
     */
    public Probe.Scids createProbeScids() {
        return new Probe.Scids();
    }

    /**
     * Create an instance of {@link Probe.Siids }
     * 
     */
    public Probe.Siids createProbeSiids() {
        return new Probe.Siids();
    }

    /**
     * Create an instance of {@link Probe.Ra.RespondTo }
     * 
     */
    public Probe.Ra.RespondTo createProbeRaRespondTo() {
        return new Probe.Ra.RespondTo();
    }

}
