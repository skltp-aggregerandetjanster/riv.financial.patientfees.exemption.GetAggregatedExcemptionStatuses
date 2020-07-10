package se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getexemptionstatuses;

import lombok.extern.log4j.Log4j2;
import org.apache.cxf.message.MessageContentsList;
import org.springframework.stereotype.Service;
import riv.financial.patientfees.exemption.enums.v1.TypeOfExemptionEnum;
import riv.financial.patientfees.exemption.enums.v1.TypeOfFeeEnum;
import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesResponseType;
import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesType;
import riv.financial.patientfees.exemption.v1.DatePeriodType;
import riv.financial.patientfees.exemption.v1.ExemptionType;
import riv.financial.patientfees.exemption.v1.FeeExemptionType;
import riv.financial.patientfees.exemption.v1.IIType;
import riv.financial.patientfees.exemption.v1.TransactionType;
import se.skltp.aggregatingservices.data.TestDataGenerator;

@Log4j2
@Service
public class ServiceTestDataGenerator extends TestDataGenerator {

  @Override
  public String getPatientId(MessageContentsList messageContentsList) {
    GetExemptionStatusesType request = (GetExemptionStatusesType) messageContentsList.get(1);
    return request.getPatientId().getExtension();
  }

  @Override
  public Object createResponse(Object... responseItems) {
    log.info("Creating a response with {} items", responseItems.length);
    GetExemptionStatusesResponseType response = new GetExemptionStatusesResponseType();
    for (int i = 0; i < responseItems.length; i++) {
      response.getFeeExemption().add((FeeExemptionType) responseItems[i]);
    }
    log.info("response.toString:" + response.toString());
    return response;
  }

  @Override
  public Object createResponseItem(String logicalAddress, String registeredResidentId,
      String businessObjectId, String time) {
    log.debug(
        "Created ResponseItem for logical-address {}, registeredResidentId {} and businessObjectId {}",
        new Object[]{logicalAddress, registeredResidentId, businessObjectId});

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
    response.getTransaction().add(t);
    t.setTypeOfFee(TypeOfFeeEnum.CARE_VISIT);
    return response;
  }

  public Object createRequest(String patientId, String sourceSystemHSAId) {
    GetExemptionStatusesType request = new GetExemptionStatusesType();
    IIType patient = new IIType();
    patient.setExtension(patientId);
    request.setPatientId(patient);
    return request;
  }
}
