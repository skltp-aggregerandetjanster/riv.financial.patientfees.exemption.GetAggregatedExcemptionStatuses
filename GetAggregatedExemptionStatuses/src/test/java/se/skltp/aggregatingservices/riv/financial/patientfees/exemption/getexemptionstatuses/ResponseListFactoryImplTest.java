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

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesResponseType;
import riv.financial.patientfees.exemption.v1.FeeExemptionType;
import se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getexemptionstatuses.ResponseListFactoryImpl;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.service.api.QueryObject;

public class ResponseListFactoryImplTest {

    private static final JaxbUtil jaxbUtil = new JaxbUtil(GetExemptionStatusesResponseType.class, ProcessingStatusType.class);

    // FIXME - currently fails with
    //         javax.xml.bind.UnmarshalException
    @Ignore
    @Test
    public void getXmlFromAggregatedResponse(){
        FindContentType fc = new FindContentType();
        fc.setRegisteredResidentIdentification("1212121212");
        QueryObject queryObject = new QueryObject(fc, null);
        List<Object> responseList = new ArrayList<Object>(2);
        responseList.add(createGetExemptionStatusesResponseType());
        responseList.add(createGetExemptionStatusesResponseType());
        ResponseListFactoryImpl responseListFactory = new ResponseListFactoryImpl();

        String responseXML = responseListFactory.getXmlFromAggregatedResponse(queryObject, responseList);
        GetExemptionStatusesResponseType response = (GetExemptionStatusesResponseType)jaxbUtil.unmarshal(responseXML);
        assertEquals(2, response.getFeeExemption().size());
    }

    private GetExemptionStatusesResponseType createGetExemptionStatusesResponseType(){
        GetExemptionStatusesResponseType getCareDocResponse = new GetExemptionStatusesResponseType();
        getCareDocResponse.getFeeExemption().add(new FeeExemptionType());
        return getCareDocResponse;
    }
}
