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
package se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getexemptionstatuses.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static se.skltp.aggregatingservices.ExemptionStatusesMuleServer.getAddress;
import static se.skltp.agp.riv.interoperability.headers.v1.CausingAgentEnum.VIRTUALIZATION_PLATFORM;
import static se.skltp.agp.test.consumer.AbstractTestConsumer.SAMPLE_CORRELATION_ID;
import static se.skltp.agp.test.consumer.AbstractTestConsumer.SAMPLE_ORIGINAL_CONSUMER_HSAID;
import static se.skltp.agp.test.consumer.AbstractTestConsumer.SAMPLE_SENDER_ID;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_BO_ID_MANY_HITS_1;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_BO_ID_MANY_HITS_2;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_BO_ID_MANY_HITS_3;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_BO_ID_ONE_HIT;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_LOGICAL_ADDRESS_1;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_LOGICAL_ADDRESS_2;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_LOGICAL_ADDRESS_3;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_EJ_SAMVERKAN_I_TAK;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_FAULT_INVALID_ID;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_MANY_HITS;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ONE_HIT;
import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ZERO_HITS;

import java.util.List;

import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.soitoolkit.commons.mule.util.RecursiveResourceBundle;

import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesResponseType;
import riv.financial.patientfees.exemption.v1.DatePeriodType;
import riv.financial.patientfees.exemption.v1.FeeExemptionType;
import se.skltp.agp.cache.TakCacheBean;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusRecordType;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.test.consumer.AbstractAggregateIntegrationTest;
import se.skltp.agp.test.consumer.ExpectedTestData;
import se.skltp.agp.test.producer.EngagemangsindexTestProducerLogger;
import se.skltp.agp.test.producer.TestProducerLogger;

public class ExemptionStatusesIntegrationTest extends AbstractAggregateIntegrationTest {

    
    private static final RecursiveResourceBundle rb = new RecursiveResourceBundle("GetAggregatedExemptionStatuses-config");
    private static final String SKLTP_HSA_ID = rb.getString("SKLTP_HSA_ID");

    private static final String LOGICAL_ADDRESS = "logical-address";
    private static final String EXPECTED_ERR_TIMEOUT_MSG = "Read timed out";
    private static final String EXPECTED_ERR_INVALID_ID_MSG = "Invalid Id: " + TEST_RR_ID_FAULT_INVALID_ID;;
    private static final String DEFAULT_SERVICE_ADDRESS = getAddress("SERVICE_INBOUND_URL");

    protected String getConfigResources() {
        return
                "soitoolkit-mule-jms-connector-activemq-embedded.xml," +
                "GetAggregatedExemptionStatuses-common.xml," +
                //			"aggregating-services-common.xml," +
                //			"aggregating-service.xml," +
                "teststub-services/engagemangsindex-teststub-service.xml," +
                "teststub-services/service-producer-teststub-service.xml," +
                "teststub-non-default-services/tak-teststub-service.xml";
    }

    @Before
    public void loadTakCache() throws Exception {
        ExemptionStatusesTestConsumer consumer = new ExemptionStatusesTestConsumer(DEFAULT_SERVICE_ADDRESS, "", "", "");
        final TakCacheBean takCache = (TakCacheBean) muleContext.getRegistry().lookupObject("takCacheBean");
        takCache.updateCache();
    }
    
    /**
     * Perform a test that is expected to return zero hits
     */
    @Test
    public void test_ok_zero_hits() {
        doTest(TEST_RR_ID_ZERO_HITS, 0);
    }
    
    /**
     * Perform a test that is expected to return zero hits even for those who 
     * do not have permission to access. 
     */
    @Test
    public void test_no_authorization_zero_hits_no_vp007_error() {
        doTest(TEST_RR_ID_EJ_SAMVERKAN_I_TAK, 0);
    }

    /**
	 * Perform a test that is expected to return an exception due to missing mandatory http headers (sender-id and original-consumer-id)
	 */
    @Test
    public void test_fault_missing_http_headers() {
    	try {
			doTest(TEST_RR_ID_ZERO_HITS, null, SAMPLE_ORIGINAL_CONSUMER_HSAID, SAMPLE_CORRELATION_ID, null, 0);
			fail("This one should fail on missing http header");
		} catch (SOAPFaultException e) {
			assertEquals("Mandatory HTTP header x-vp-sender-id is missing", e.getMessage());
		}

    	try {
	    	doTest(TEST_RR_ID_ZERO_HITS, SAMPLE_SENDER_ID, null, SAMPLE_CORRELATION_ID, null, 0);
	       	fail("This one should fail on missing http header");
		} catch (SOAPFaultException e) {
			assertEquals("\nMandatory HTTP header x-rivta-original-serviceconsumer-hsaid is missing", e.getMessage());
		}

    	try {
	       	doTest(TEST_RR_ID_ZERO_HITS, null, null, null, null, 0);
	       	fail("This one should fail on missing http header");
		} catch (SOAPFaultException e) {
		    assertEquals("Mandatory HTTP header x-vp-sender-id is missing\nMandatory HTTP header x-rivta-original-serviceconsumer-hsaid is missing\nMandatory HTTP header x-skltp-correlation-id is missing", e.getMessage());
		}
    }

    /**
     * Perform a test that is expected to return one hit with data from one source system
     */
    @Test
    public void test_ok_one_hit() {
        List<ProcessingStatusRecordType> statusList = doTest(TEST_RR_ID_ONE_HIT, 2, new ExpectedTestData(TEST_BO_ID_ONE_HIT, TEST_LOGICAL_ADDRESS_1));
        assertProcessingStatusDataFromSource(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
    }

    /**
     * Perform a test that is expected to return three hit with data from two source systems and one source system that cause a timeout
     */
    @Test
    public void test_ok_many_hits_with_partial_timeout() {

        // Setup call and verify the response, expect one booking from source #1, two from source #2 and a timeout from source #3
        List<ProcessingStatusRecordType> statusList = doTest(TEST_RR_ID_MANY_HITS, 3,
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_1, TEST_LOGICAL_ADDRESS_1),
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_2, TEST_LOGICAL_ADDRESS_2),
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_3, TEST_LOGICAL_ADDRESS_2));

        // Verify the Processing Status, expect ok from source system #1 and #2 but a timeout from #3
        assertProcessingStatusDataFromSource(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
        assertProcessingStatusDataFromSource(statusList.get(1), TEST_LOGICAL_ADDRESS_2);
        assertProcessingStatusNoDataSynchFailed(statusList.get(2), TEST_LOGICAL_ADDRESS_3, VIRTUALIZATION_PLATFORM, EXPECTED_ERR_TIMEOUT_MSG);
    }

    /**
     * Perform a test that is expected to casue the source system to fail with its processing
     */
    @Test
    public void test_fault_invalidInput() throws Exception {

        List<ProcessingStatusRecordType> statusList = doTest(TEST_RR_ID_FAULT_INVALID_ID, 1);

        // Verify the Processing Status, expect a processing failure from the source system
        assertProcessingStatusNoDataSynchFailed(statusList.get(0), TEST_LOGICAL_ADDRESS_1, VIRTUALIZATION_PLATFORM, EXPECTED_ERR_INVALID_ID_MSG);
    }

    @Ignore @Test // FIXME
    public void test_ok_caching() {
        long   expectedProcessingTime = getTestDb().getProcessingTime(TEST_LOGICAL_ADDRESS_1);

        long ts = System.currentTimeMillis();

        List<ProcessingStatusRecordType> statusList = doTest(TEST_RR_ID_ONE_HIT, SAMPLE_SENDER_ID,"consumer1", SAMPLE_CORRELATION_ID, null, 1, new ExpectedTestData(TEST_BO_ID_ONE_HIT, TEST_LOGICAL_ADDRESS_1));
        ts = System.currentTimeMillis() - ts;
        assertProcessingStatusDataFromSource(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
        assertTrue("Expected a long processing time (i.e. a non cached response)", ts > expectedProcessingTime);

        ts = System.currentTimeMillis();
        statusList = doTest(TEST_RR_ID_ONE_HIT, SAMPLE_SENDER_ID, "consumer1", SAMPLE_CORRELATION_ID, null, 1, new ExpectedTestData(TEST_BO_ID_ONE_HIT, TEST_LOGICAL_ADDRESS_1));
        ts = System.currentTimeMillis() - ts;
        assertProcessingStatusDataFromCache(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
        assertTrue("Expected a short processing time (i.e. a cached response)", ts < expectedProcessingTime);

        ts = System.currentTimeMillis();
        statusList = doTest(TEST_RR_ID_ONE_HIT, SAMPLE_SENDER_ID, "consumer2", SAMPLE_CORRELATION_ID, null, 1, new ExpectedTestData(TEST_BO_ID_ONE_HIT, TEST_LOGICAL_ADDRESS_1));
        ts = System.currentTimeMillis() - ts;
        assertProcessingStatusDataFromSource(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
        assertTrue("Expected a long processing time (i.e. a non cached response)", ts > expectedProcessingTime);
    }


    @Ignore @Test // FIXME
    public void test_ok_caching_many_hits() {
        long   expectedProcessingTime = getTestDb().getProcessingTime(TEST_LOGICAL_ADDRESS_1);

        long ts = System.currentTimeMillis();


        List<ProcessingStatusRecordType> statusList = doTest(TEST_RR_ID_MANY_HITS, 3,
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_1, TEST_LOGICAL_ADDRESS_1),
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_2, TEST_LOGICAL_ADDRESS_2),
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_3, TEST_LOGICAL_ADDRESS_2));

        // Verify the Processing Status, expect ok from source system #1 and #2 but a timeout from #3
        assertProcessingStatusDataFromSource(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
        assertProcessingStatusDataFromSource(statusList.get(1), TEST_LOGICAL_ADDRESS_2);
        assertProcessingStatusNoDataSynchFailed(statusList.get(2), TEST_LOGICAL_ADDRESS_3, VIRTUALIZATION_PLATFORM, EXPECTED_ERR_TIMEOUT_MSG);
        ts = System.currentTimeMillis() - ts;
        assertTrue("Expected a long processing time (i.e. a non cached response)", ts > expectedProcessingTime);

        ts = System.currentTimeMillis();
        statusList = doTest(TEST_RR_ID_MANY_HITS, 3,
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_1, TEST_LOGICAL_ADDRESS_1),
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_2, TEST_LOGICAL_ADDRESS_2),
                new ExpectedTestData(TEST_BO_ID_MANY_HITS_3, TEST_LOGICAL_ADDRESS_2));
        ts = System.currentTimeMillis() - ts;
        assertProcessingStatusDataFromCache(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
        assertProcessingStatusDataFromCache(statusList.get(1), TEST_LOGICAL_ADDRESS_2);
        assertProcessingStatusNoDataSynchFailed(statusList.get(2), TEST_LOGICAL_ADDRESS_3, VIRTUALIZATION_PLATFORM, EXPECTED_ERR_TIMEOUT_MSG);
        assertTrue("Expected a short processing time (i.e. a cached response)", ts < expectedProcessingTime);
    }

    @Ignore @Test // FIXME
    public void test_non_cachable() {
        DatePeriodType datePeriod = new DatePeriodType();
        datePeriod.setStart("20110101");
        datePeriod.setEnd("20130201");

        List<ProcessingStatusRecordType> statusList = doTest(TEST_RR_ID_ONE_HIT, SAMPLE_SENDER_ID, "consumer1", SAMPLE_CORRELATION_ID, datePeriod, 1, new ExpectedTestData(TEST_BO_ID_ONE_HIT, TEST_LOGICAL_ADDRESS_1));
        assertProcessingStatusDataFromSource(statusList.get(0), TEST_LOGICAL_ADDRESS_1);

        statusList = doTest(TEST_RR_ID_ONE_HIT, SAMPLE_SENDER_ID, "consumer1", SAMPLE_CORRELATION_ID, datePeriod, 1, new ExpectedTestData(TEST_BO_ID_ONE_HIT, TEST_LOGICAL_ADDRESS_1));
        assertProcessingStatusDataFromSource(statusList.get(0), TEST_LOGICAL_ADDRESS_1);
    }

    /**
     * Helper method for performing a call to the aggregating service and perform some common validations of the result
     *
     * @param registeredResidentId
     * @param expectedProcessingStatusSize
     * @param ExpectedTestData
     * @return
     */
    private List<ProcessingStatusRecordType> doTest(String registeredResidentId, int expectedProcessingStatusSize, ExpectedTestData... testData) {
        return doTest(registeredResidentId, SAMPLE_SENDER_ID, SAMPLE_ORIGINAL_CONSUMER_HSAID, SAMPLE_CORRELATION_ID, null, expectedProcessingStatusSize, testData);
    }

    /**
     * Helper method for performing a call to the aggregating service and perform some common validations of the result
     *
     * @param registeredResidentId
     * @param senderId
     * @param originalConsumerHsaId
     * @param expectedProcessingStatusSize
     * @param testData
     * @return
     */
    private List<ProcessingStatusRecordType> doTest(String registeredResidentId, String senderId, String originalConsumerHsaId, String correlationId, DatePeriodType datePeriod,int expectedProcessingStatusSize, ExpectedTestData... testData) {

        // Setup and perform the call to the web service
        ExemptionStatusesTestConsumer consumer = new ExemptionStatusesTestConsumer(DEFAULT_SERVICE_ADDRESS, senderId, originalConsumerHsaId, correlationId);
        Holder<GetExemptionStatusesResponseType> responseHolder = new Holder<GetExemptionStatusesResponseType>();
        Holder<ProcessingStatusType> processingStatusHolder = new Holder<ProcessingStatusType>();
        consumer.callService(LOGICAL_ADDRESS, registeredResidentId, datePeriod, processingStatusHolder, responseHolder);

        // Verify the response size and content
        GetExemptionStatusesResponseType response = responseHolder.value;
        int expextedResponseSize = testData.length;
        assertEquals(expextedResponseSize, response.getFeeExemption().size());

        for (int i = 0; i < testData.length; i++) {
            FeeExemptionType responseElement = response.getFeeExemption().get(i);
            assertEquals(registeredResidentId, responseElement.getPatientId().getExtension());
        }

        // Verify the size of the processing status and return it for further analysis
 		ProcessingStatusType statusList = processingStatusHolder.value;
 		assertEquals(expectedProcessingStatusSize, statusList.getProcessingStatusList().size());

 	 	// Verify that correct "x-vp-sender-id" http header was passed to the engagement index
 		assertEquals(SKLTP_HSA_ID, EngagemangsindexTestProducerLogger.getLastSenderId());

 	 	// Verify that correct "x-rivta-original-serviceconsumer-hsaid" http header was passed to the engagement index
 		assertEquals(SAMPLE_ORIGINAL_CONSUMER_HSAID, EngagemangsindexTestProducerLogger.getLastOriginalConsumer());

        // Verify that correct "x-vp-sender-id" and "x-rivta-original-serviceconsumer-hsaid" http header was passed to the service producer,
 		// given that a service producer was called
 		if (expectedProcessingStatusSize > 0) {
 			assertEquals(SAMPLE_SENDER_ID, TestProducerLogger.getLastSenderId());
 			assertEquals(SAMPLE_ORIGINAL_CONSUMER_HSAID, TestProducerLogger.getLastOriginalConsumer());
            assertEquals(SAMPLE_CORRELATION_ID, TestProducerLogger.getLastCorrelationId());
 		}

        return statusList.getProcessingStatusList();
    }

}
