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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.financial.patientfees.exemption.getexemptionstatuses.v1.rivtabp21.GetExemptionStatusesResponderInterface;
import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesResponseType;
import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesType;
import se.skltp.agp.test.producer.TestProducerDb;

@WebService(serviceName = "GetExemptionStatusesResponderService", portName = "GetExemptionStatusesResponderPort", targetNamespace = "urn:riv:ehr:patientsummary:GetExemptionStatusesResponder:2:rivtabp21", name = "GetExemptionStatusesInteraction")
public class ExemptionStatusesTestProducer implements GetExemptionStatusesResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(ExemptionStatusesTestProducer.class);

    private TestProducerDb testDb;

    public void setTestDb(TestProducerDb testDb) {
        this.testDb = testDb;
    }

    @Override
    @WebResult(name = "GetExemptionStatusesResponse", targetNamespace = "urn:riv:financial:patientfees:exemption:GetExemptionStatusesResponder:1", partName = "parameters")
    @WebMethod(operationName = "GetExemptionStatuses", action = "urn:riv:financial:patientfees:exemption:GetExemptionStatusesResponder:1:GetExemptionStatuses")
    public GetExemptionStatusesResponseType getExemptionStatuses(
            @WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String logicalAddress,
            @WebParam(partName = "parameters", name = "GetExemptionStatuses", targetNamespace = "urn:riv:financial:patientfees:exemption:GetExemptionStatusesResponder:1") GetExemptionStatusesType request) {

    	log.info("### Virtual service for GetExemptionStatuses call the source system with logical address: {} and patientId: {}", logicalAddress, request.getPatientId().getExtension());

        testDb.refreshDb();
        GetExemptionStatusesResponseType response = (GetExemptionStatusesResponseType)testDb.processRequest(logicalAddress, request.getPatientId().getExtension());
        if (response == null) {
            // Return an empty response object instead of null if nothing is found
            response = new GetExemptionStatusesResponseType();
        }

        log.info("### Virtual service got {} documents in the reply from the source system with logical address: {} and patientId: {}", new Object[] {response.getFeeExemption().size(), logicalAddress, request.getPatientId().getExtension()});

        // We are done
        return response;
    }

}
