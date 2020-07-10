package se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getexemptionstatuses;

import static org.junit.Assert.assertEquals;
import static se.skltp.aggregatingservices.data.TestDataDefines.TEST_LOGICAL_ADDRESS_2;
import static se.skltp.aggregatingservices.data.TestDataDefines.TEST_RR_ID_MANY_HITS;
import static se.skltp.aggregatingservices.data.TestDataDefines.TEST_RR_ID_MANY_HITS_NO_ERRORS;

import java.util.List;
import org.apache.cxf.message.MessageContentsList;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesResponseType;
import se.skltp.aggregatingservices.api.AgpServiceFactory;
import se.skltp.aggregatingservices.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentResponseType;
import se.skltp.aggregatingservices.tests.CreateFindContentTest;
import se.skltp.aggregatingservices.data.TestDataGenerator;
import se.skltp.aggregatingservices.tests.CreateRequestListTest;
import se.skltp.aggregatingservices.tests.TestDataUtil;
import se.skltp.aggregatingservices.utility.RequestListUtil;

@RunWith(SpringJUnit4ClassRunner.class)
public class GAESCreateRequestListTest extends CreateRequestListTest {

  private static GAESAgpServiceConfiguration configuration = new GAESAgpServiceConfiguration();
  private static AgpServiceFactory<GetExemptionStatusesResponseType> agpServiceFactory = new GAESAgpServiceFactoryImpl();
  private static ServiceTestDataGenerator testDataGenerator = new ServiceTestDataGenerator();

    public GAESCreateRequestListTest() {
    super(testDataGenerator, agpServiceFactory, configuration);
  }

  @Test
  public void testCreateRequestListSomeFilteredBySourceSystem(){
    MessageContentsList messageContentsList = TestDataUtil.createRequest(LOGISK_ADRESS, testDataGenerator.createRequest(
        TEST_RR_ID_MANY_HITS, TEST_LOGICAL_ADDRESS_2));
    FindContentResponseType eiResponse = eiResponseDataHelper.getResponseForPatient(TEST_RR_ID_MANY_HITS);
    List<MessageContentsList> requestList = agpServiceFactory.createRequestList(messageContentsList, eiResponse);
    assertEquals(3, requestList.size());
  }

  @Test
  public void testCreateRequestListAllFilteredBySourceSystem(){
    MessageContentsList messageContentsList = TestDataUtil.createRequest(LOGISK_ADRESS, testDataGenerator.createRequest(
        TEST_RR_ID_MANY_HITS_NO_ERRORS, TEST_LOGICAL_ADDRESS_2));
    FindContentResponseType eiResponse = eiResponseDataHelper.getResponseForPatient(TEST_RR_ID_MANY_HITS_NO_ERRORS);
    List<MessageContentsList> requestList = agpServiceFactory.createRequestList(messageContentsList, eiResponse);
    assertEquals(3, requestList.size());
  }
}