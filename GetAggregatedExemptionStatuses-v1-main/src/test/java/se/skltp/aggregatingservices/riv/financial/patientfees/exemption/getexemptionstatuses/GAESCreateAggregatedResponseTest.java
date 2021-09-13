package se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getexemptionstatuses;


import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesResponseType;
import se.skltp.aggregatingservices.api.AgpServiceFactory;
import se.skltp.aggregatingservices.tests.CreateAggregatedResponseTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class GAESCreateAggregatedResponseTest extends CreateAggregatedResponseTest {

  private static GAESAgpServiceConfiguration configuration = new GAESAgpServiceConfiguration();
  private static AgpServiceFactory<GetExemptionStatusesResponseType> agpServiceFactory = new GAESAgpServiceFactoryImpl();
  private static ServiceTestDataGenerator testDataGenerator = new ServiceTestDataGenerator();

  public GAESCreateAggregatedResponseTest() {
    super(testDataGenerator, agpServiceFactory, configuration);
  }

  @Override
  public int getResponseSize(Object response) {
    GetExemptionStatusesResponseType responseType = (GetExemptionStatusesResponseType) response;
    return responseType.getFeeExemption().size();
  }
}