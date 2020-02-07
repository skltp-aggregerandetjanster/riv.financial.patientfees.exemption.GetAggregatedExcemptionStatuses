/**
 * Copyright (c) 2014 Inera AB, <http://inera.se/>
 *
 * This file is part of SKLTP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getexemptionstatuses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

import riv.financial.patientfees.exemption.v1.IIType;
import se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getexemptionstatuses.QueryObjectFactoryImpl;
import se.skltp.agp.service.api.QueryObject;
import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesType;
import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.ObjectFactory;

public class QueryObjectFactoryImplTest {

    private static final String CATEGORIZATION = "fpe-es";
    private static final String SERVICE_DOMAIN = "riv:financial:patientfees:exemption";
    private static final String RR_ID = "1212121212";
    private static final String SOURCE_SYSTEM = null ; //"SS1";

    QueryObjectFactoryImpl objectFactory = new QueryObjectFactoryImpl();

    @Before
    public void setup(){
        objectFactory.setEiCategorization(CATEGORIZATION);
        objectFactory.setEiServiceDomain(SERVICE_DOMAIN);
    }

    @Test
    public void createQueryObject() throws Exception{
    	GetExemptionStatusesType get = new GetExemptionStatusesType();
        IIType patientId = new IIType();
        patientId.setExtension(RR_ID);
    	get.setPatientId(patientId);

        Document doc = createDocument(get);
        QueryObject queryObj = objectFactory.createQueryObject(doc);

        assertNotNull(queryObj.getFindContent());
        assertEquals(SERVICE_DOMAIN, queryObj.getFindContent().getServiceDomain());
        assertEquals(CATEGORIZATION, queryObj.getFindContent().getCategorization());
        assertEquals(RR_ID, queryObj.getFindContent().getRegisteredResidentIdentification());
        assertNull(queryObj.getFindContent().getSourceSystem());
    }

    //@Ignore("not used here")
    @Test
    public void createQueryObject_with_source_system() throws Exception{
    	GetExemptionStatusesType get = new GetExemptionStatusesType();
        IIType patientId = new IIType();
        patientId.setExtension(RR_ID);
    	get.setPatientId(patientId);
        Document doc = createDocument(get);
        QueryObject queryObj = objectFactory.createQueryObject(doc);

        assertNotNull(queryObj.getFindContent());
        assertEquals(SOURCE_SYSTEM, queryObj.getFindContent().getSourceSystem());
    }

    private Document createDocument(GetExemptionStatusesType exemption) throws Exception {
    	ObjectFactory of = new ObjectFactory();
        JAXBContext jaxbContext = JAXBContext.newInstance(GetExemptionStatusesType.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().newDocument();
        marshaller.marshal(of.createGetExemptionStatuses(exemption), doc);
        return doc;
    }

}
