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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.financial.patientfees.exemption.enums.v1.TypeOfExemptionEnum;
import riv.financial.patientfees.exemption.enums.v1.TypeOfFeeEnum;
import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesResponseType;
import riv.financial.patientfees.exemption.v1.DatePeriodType;
import riv.financial.patientfees.exemption.v1.ExemptionType;
import riv.financial.patientfees.exemption.v1.FeeExemptionType;
import riv.financial.patientfees.exemption.v1.IIType;
import riv.financial.patientfees.exemption.v1.TransactionType;
import se.skltp.agp.test.producer.TestProducerDb;

public class ExemptionStatusesTestProducerDb extends TestProducerDb {

    private static final Logger log = LoggerFactory.getLogger(ExemptionStatusesTestProducerDb.class);

    @Override
    public Object createResponse(Object... responseItems) {
        log.debug("Creates a response with {} items", responseItems);
        GetExemptionStatusesResponseType response = new GetExemptionStatusesResponseType();
        for (int i = 0; i < responseItems.length; i++) {
            response.getFeeExemption().add((FeeExemptionType)responseItems[i]);
        }
        return response;
    }

    public static final String TEST_REASON_DEFAULT = "default reason";
    public static final String TEST_REASON_UPDATED = "updated reason";

    @Override
    public Object createResponseItem(String logicalAddress, String registeredResidentId, String businessObjectId, String time) {
    	
        log.debug("Created one response item for logical-address {}, registeredResidentId {} and businessObjectId {}",
                new Object[] {logicalAddress, registeredResidentId, businessObjectId});

        FeeExemptionType response = new FeeExemptionType();
        IIType personid = new IIType();
        personid.setExtension(registeredResidentId);
        personid.setRoot("1.2.752.129.2.1.3.1");
        response.setPatientId(personid);
        
        ExemptionType e = new ExemptionType();
		response.getExemption().add(e);
		
		e.setTypeOfExemption(TypeOfExemptionEnum.CARE_VISIT);
		DatePeriodType dt = new DatePeriodType();
		dt.setEnd("20190113");
		dt.setEnd("20190119");
		e.setExemptionPeriod(dt);
		e.setHighCostProtectionPeriod(dt);
		
		TransactionType t = new TransactionType();
		response.getTransaction().add(t );
		t.setTypeOfFee(TypeOfFeeEnum.CARE_VISIT);
		
        return response;
    }
}
