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

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import riv.financial.patientfees.exemption.v1.DatePeriodType;
import riv.financial.patientfees.exemption.v1.FeeExemptionType;
import riv.financial.patientfees.exemption.v1.IIType;
import se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getexemptionstatuses.RequestListFactoryImpl;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentResponseType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.riv.itintegration.engagementindex.v1.EngagementType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.test.producer.TestProducerDb;

public class RequestListFactoryImplTest {

    private static final String CATEGORIZATION = "voo";
    private static final String SERVICE_DOMAIN = "riv:financial:patientfees:exemption";
    private static final String RR_ID = "1212121212";

    private static final String SOURCE_SYSTEM_1 = "SS1";
    private static final String SOURCE_SYSTEM_2 = "SS2";

    @Test
    public void createRequestList(){
        RequestListFactoryImpl requestFactory = new RequestListFactoryImpl();
        FindContentType fc = createFindContent(RR_ID);
        FeeExemptionType feeExcemption = createFeeExemption(RR_ID, Collections.<String> emptyList());
        QueryObject queryObject = new QueryObject(fc, feeExcemption);
        FindContentResponseType findContentResponse = createFindContentResponse(TestProducerDb.TEST_LOGICAL_ADDRESS_1, TestProducerDb.TEST_LOGICAL_ADDRESS_2);
        List<Object[]> requestList =  requestFactory.createRequestList(queryObject, findContentResponse);
        assertEquals(2, requestList.size());

        assertEquals(TestProducerDb.TEST_LOGICAL_ADDRESS_2, requestList.get(0)[0]);
        FeeExemptionType request1 = (FeeExemptionType)requestList.get(0)[1];
        assertEquals(RR_ID, request1.getPatientId().getExtension());

        assertEquals(TestProducerDb.TEST_LOGICAL_ADDRESS_1, requestList.get(1)[0]);
        FeeExemptionType request2 = (FeeExemptionType)requestList.get(1)[1];
        assertEquals(RR_ID, request2.getPatientId().getExtension());
    }

    @Test
    public void createRequestList_one_careUnit(){
        RequestListFactoryImpl requestFactory = new RequestListFactoryImpl();
        FindContentType fc = createFindContent(RR_ID);
        FeeExemptionType feeExemption = createFeeExemption(RR_ID, Collections.singletonList(TestProducerDb.TEST_LOGICAL_ADDRESS_1));
        QueryObject queryObject = new QueryObject(fc, feeExemption);
        FindContentResponseType findContentResponse = createFindContentResponse(TestProducerDb.TEST_LOGICAL_ADDRESS_1, TestProducerDb.TEST_LOGICAL_ADDRESS_1);
        List<Object[]> requestList =  requestFactory.createRequestList(queryObject, findContentResponse);
        assertEquals(1, requestList.size());
        assertEquals(TestProducerDb.TEST_LOGICAL_ADDRESS_1, requestList.get(0)[0]);

        FeeExemptionType request = (FeeExemptionType)requestList.get(0)[1];
        assertEquals(RR_ID, request.getPatientId().getExtension());
    }

    @Test @Ignore("Ej relevant")
    public void createRequestList_different_sourceSystems(){
        RequestListFactoryImpl requestFactory = new RequestListFactoryImpl();
        FindContentType fc = createFindContent(RR_ID);
        FeeExemptionType feeExcemption = createFeeExemption(RR_ID, Collections.singletonList(TestProducerDb.TEST_LOGICAL_ADDRESS_1));
        QueryObject queryObject = new QueryObject(fc, feeExcemption);
        FindContentResponseType findContentResponse = createFindContentResponse(TestProducerDb.TEST_LOGICAL_ADDRESS_1, TestProducerDb.TEST_LOGICAL_ADDRESS_1);
        findContentResponse.getEngagement().get(0).setSourceSystem(SOURCE_SYSTEM_1);
        findContentResponse.getEngagement().get(1).setSourceSystem(SOURCE_SYSTEM_2);

        List<Object[]> requestList =  requestFactory.createRequestList(queryObject, findContentResponse);
        assertEquals(2, requestList.size());

        assertEquals(SOURCE_SYSTEM_1, requestList.get(0)[0]);
        FeeExemptionType request1 = (FeeExemptionType)requestList.get(0)[1];
        assertEquals(RR_ID, request1.getPatientId().getExtension());

        assertEquals(SOURCE_SYSTEM_2, requestList.get(1)[0]);
        FeeExemptionType request2 = (FeeExemptionType)requestList.get(1)[1];
        assertEquals(RR_ID, request2.getPatientId().getExtension());
    }

    @Test
    public void createRequestList_different_careUnits_one_sourceSystem(){
        RequestListFactoryImpl requestFactory = new RequestListFactoryImpl();
        FindContentType fc = createFindContent(RR_ID);
        FeeExemptionType feeExemption = createFeeExemption(RR_ID, Collections.<String> emptyList());
        QueryObject queryObject = new QueryObject(fc, feeExemption);
        FindContentResponseType findContentResponse = createFindContentResponse(TestProducerDb.TEST_LOGICAL_ADDRESS_1, TestProducerDb.TEST_LOGICAL_ADDRESS_2);
        findContentResponse.getEngagement().get(0).setSourceSystem(SOURCE_SYSTEM_1);
        findContentResponse.getEngagement().get(1).setSourceSystem(SOURCE_SYSTEM_1);
        List<Object[]> requestList =  requestFactory.createRequestList(queryObject, findContentResponse);
        assertEquals(1, requestList.size());
        assertEquals(SOURCE_SYSTEM_1, requestList.get(0)[0]);

        FeeExemptionType request = (FeeExemptionType)requestList.get(0)[1];
        assertEquals(RR_ID, request.getPatientId().getExtension());
    }

    @Test
    public void createRequestList_one_careUnit_one_sourceSystem(){
        RequestListFactoryImpl requestFactory = new RequestListFactoryImpl();
        FindContentType fc = createFindContent(RR_ID);
        FeeExemptionType feeExcemption = createFeeExemption(RR_ID, Collections.<String> emptyList());
        QueryObject queryObject = new QueryObject(fc, feeExcemption);
        FindContentResponseType findContentResponse = createFindContentResponse(TestProducerDb.TEST_LOGICAL_ADDRESS_1, TestProducerDb.TEST_LOGICAL_ADDRESS_1);
        findContentResponse.getEngagement().get(0).setSourceSystem(SOURCE_SYSTEM_1);
        findContentResponse.getEngagement().get(1).setSourceSystem(SOURCE_SYSTEM_1);
        List<Object[]> requestList =  requestFactory.createRequestList(queryObject, findContentResponse);
        assertEquals(1, requestList.size());
        assertEquals(SOURCE_SYSTEM_1, requestList.get(0)[0]);

        FeeExemptionType request = (FeeExemptionType)requestList.get(0)[1];
        assertEquals(RR_ID, request.getPatientId().getExtension());
    }


    @Ignore @Test // Not in use in this service domain
    public void createRequestList_timePeriod(){
        RequestListFactoryImpl requestFactory = new RequestListFactoryImpl();
        FindContentType fc = createFindContent(RR_ID);
        FeeExemptionType feeExcemption = createFeeExemption(RR_ID, Collections.<String> emptyList());
        DatePeriodType timePeriod = new DatePeriodType();
        timePeriod.setStart("20110101");
        timePeriod.setEnd("20110201");
        // feeExcemption.setTimePeriod(timePeriod);
        QueryObject queryObject = new QueryObject(fc, feeExcemption);
        FindContentResponseType findContentResponse = createFindContentResponse(TestProducerDb.TEST_LOGICAL_ADDRESS_1, TestProducerDb.TEST_LOGICAL_ADDRESS_2);
        findContentResponse.getEngagement().get(0).setMostRecentContent("20110101120101");
        findContentResponse.getEngagement().get(1).setMostRecentContent("20110301120101");

        List<Object[]> requestList =  requestFactory.createRequestList(queryObject, findContentResponse);
        assertEquals(1, requestList.size());

        assertEquals(TestProducerDb.TEST_LOGICAL_ADDRESS_1, requestList.get(0)[0]);
        FeeExemptionType request = (FeeExemptionType)requestList.get(0)[1];
        assertEquals(RR_ID, request.getPatientId().getExtension());
    }

    private FindContentResponseType createFindContentResponse(String... logicalAddresses){
        FindContentResponseType findContentResponse = new FindContentResponseType();
        for(String logicalAddress: logicalAddresses){
            findContentResponse.getEngagement().add(createEngagement(logicalAddress, logicalAddress));
        }
        return findContentResponse;
    }

    private FindContentType createFindContent(String id){
        FindContentType fc = new FindContentType();
        fc.setRegisteredResidentIdentification(id);
        fc.setServiceDomain(SERVICE_DOMAIN);
        fc.setCategorization(CATEGORIZATION);
        return fc;
    }

    private FeeExemptionType createFeeExemption(String id, List<String> careUnits){
        FeeExemptionType feeExcemption = new FeeExemptionType();
        IIType patientId = new IIType();
        patientId.setExtension(RR_ID);
        feeExcemption.setPatientId(patientId);
        // feeExcemption.getCareUnitHSAid().addAll(careUnits);
        return feeExcemption;
    }

    private EngagementType createEngagement(String logicalAddress, String sourceSystem){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        EngagementType engagement = new EngagementType();
        engagement.setCategorization(CATEGORIZATION);
        engagement.setServiceDomain(SERVICE_DOMAIN);
        engagement.setLogicalAddress(logicalAddress);
        engagement.setSourceSystem(sourceSystem);
        engagement.setMostRecentContent(df.format(new Date()));
        return engagement;
    }
}
