package se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getexemptionstatuses;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesResponseType;
import se.skltp.aggregatingservices.api.AgpServiceFactory;
import se.skltp.aggregatingservices.tests.CreateFindContentTest;
import se.skltp.aggregatingservices.data.TestDataGenerator;


@RunWith(SpringJUnit4ClassRunner.class)
public class GAESCreateFindContentTest extends CreateFindContentTest {

  private static GAESAgpServiceConfiguration configuration = new GAESAgpServiceConfiguration();
  private static AgpServiceFactory<GetExemptionStatusesResponseType> agpServiceFactory = new GAESAgpServiceFactoryImpl();
  private static ServiceTestDataGenerator testDataGenerator = new ServiceTestDataGenerator();

  public GAESCreateFindContentTest() {
    super(testDataGenerator, agpServiceFactory, configuration);
  }
}
