package se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getexemptionstatuses;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.skltp.aggregatingservices.data.TestDataDefines.TEST_LOGICAL_ADDRESS_2;
import static se.skltp.aggregatingservices.data.TestDataDefines.TEST_RR_ID_MANY_HITS;
import static se.skltp.aggregatingservices.data.TestDataDefines.TEST_RR_ID_MANY_HITS_NO_ERRORS;

import java.util.List;
import org.apache.cxf.message.MessageContentsList;
import org.junit.jupiter.api.Test;
import riv.financial.patientfees.exemption.getexemptionstatusesresponder.v1.GetExemptionStatusesResponseType;
import se.skltp.aggregatingservices.api.AgpServiceFactory;
import se.skltp.aggregatingservices.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentResponseType;
import se.skltp.aggregatingservices.tests.CreateRequestListTest;
import se.skltp.aggregatingservices.tests.TestDataUtil;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
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