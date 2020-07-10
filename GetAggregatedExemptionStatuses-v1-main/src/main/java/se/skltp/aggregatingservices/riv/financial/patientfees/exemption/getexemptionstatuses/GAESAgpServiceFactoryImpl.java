package se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getexemptionstatuses;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesResponseType;
import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesType;
import se.skltp.aggregatingservices.AgServiceFactoryBase;

@Log4j2
public class GAESAgpServiceFactoryImpl extends
    AgServiceFactoryBase<GetExemptionStatusesType, GetExemptionStatusesResponseType> {

  @Override
  public String getPatientId(GetExemptionStatusesType queryObject) {
    return queryObject.getPatientId().getExtension();
  }

  @Override
  public String getSourceSystemHsaId(GetExemptionStatusesType queryObject) {
    return null;
  }

  @Override
  public GetExemptionStatusesResponseType aggregateResponse(
      List<GetExemptionStatusesResponseType> aggregatedResponseList) {

    GetExemptionStatusesResponseType aggregatedResponse = new GetExemptionStatusesResponseType();

    for (Object object : aggregatedResponseList) {
      GetExemptionStatusesResponseType response = (GetExemptionStatusesResponseType) object;
      aggregatedResponse.getFeeExemption().addAll(response.getFeeExemption());
    }

    return aggregatedResponse;
  }
}

