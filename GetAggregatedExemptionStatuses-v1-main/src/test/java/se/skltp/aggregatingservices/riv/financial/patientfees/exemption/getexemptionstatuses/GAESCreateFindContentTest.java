package se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getexemptionstatuses;

import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesResponseType;
import se.skltp.aggregatingservices.api.AgpServiceFactory;
import se.skltp.aggregatingservices.tests.CreateFindContentTest;
import se.skltp.aggregatingservices.data.TestDataGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class GAESCreateFindContentTest extends CreateFindContentTest {

  private static GAESAgpServiceConfiguration configuration = new GAESAgpServiceConfiguration();
  private static AgpServiceFactory<GetExemptionStatusesResponseType> agpServiceFactory = new GAESAgpServiceFactoryImpl();
  private static ServiceTestDataGenerator testDataGenerator = new ServiceTestDataGenerator();

  public GAESCreateFindContentTest() {
    super(testDataGenerator, agpServiceFactory, configuration);
  }
}
