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

import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ONE_HIT;

import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.financial.patientfees.exemption.getexemptionstatuses.v1.rivtabp21.GetExemptionStatusesResponderInterface;
import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesResponseType;
import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesType;
import riv.financial.patientfees.exemption.v1.DatePeriodType;
import riv.financial.patientfees.exemption.v1.IIType;
import riv.financial.patientfees.exemption.v1.PersonIdType;
import se.skltp.aggregatingservices.ExemptionStatusesMuleServer;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.test.consumer.AbstractTestConsumer;
import se.skltp.agp.test.consumer.SoapHeaderCxfInterceptor;

public class ExemptionStatusesTestConsumer extends AbstractTestConsumer<GetExemptionStatusesResponderInterface>{

    private static final Logger log = LoggerFactory.getLogger(ExemptionStatusesTestConsumer.class);

    public static void main(String[] args) {
        log.info("URL: " + ExemptionStatusesMuleServer.getAddress("SERVICE_INBOUND_URL"));
        String serviceAddress = ExemptionStatusesMuleServer.getAddress("SERVICE_INBOUND_URL");
        String personnummer = TEST_RR_ID_ONE_HIT;

        ExemptionStatusesTestConsumer consumer = new ExemptionStatusesTestConsumer(serviceAddress, SAMPLE_SENDER_ID, SAMPLE_ORIGINAL_CONSUMER_HSAID, SAMPLE_CORRELATION_ID);
        Holder<GetExemptionStatusesResponseType> responseHolder = new Holder<GetExemptionStatusesResponseType>();
        Holder<ProcessingStatusType> processingStatusHolder = new Holder<ProcessingStatusType>();
        long now = System.currentTimeMillis();
        consumer.callService("logical-adress", personnummer, null, processingStatusHolder, responseHolder);
        log.info("Returned #care documentation = " + responseHolder.value.getFeeExemption().size() + " in " + (System.currentTimeMillis() - now) + " ms.");
    }

    public ExemptionStatusesTestConsumer(String serviceAddress, String senderId, String originalConsumerHsaId, String correlationId) {
        // Setup a web service proxy for communication using HTTPS with Mutual Authentication
        super(GetExemptionStatusesResponderInterface.class, serviceAddress, senderId, originalConsumerHsaId, correlationId);
    }

    public void callService(String logicalAddress, String id, DatePeriodType datePeriod, Holder<ProcessingStatusType> processingStatusHolder, Holder<GetExemptionStatusesResponseType> responseHolder) {
        log.debug("Calling GetRequestActivities-soap-service with id = {}", id);

        GetExemptionStatusesType request = new GetExemptionStatusesType();
        
        IIType patientId = new IIType();
        patientId.setExtension(id);
        request.setPatientId(patientId);

        GetExemptionStatusesResponseType response = _service.getExemptionStatuses(logicalAddress, request);
        responseHolder.value = response;

        processingStatusHolder.value = SoapHeaderCxfInterceptor.getLastFoundProcessingStatus();
    }
}
