package se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getexemptionstatuses;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import riv.financial.patientfees.exemption.getexemptionstatuses.v1.rivtabp21.GetExemptionStatusesResponderInterface;
import riv.financial.patientfees.exemption.getexemptionstatuses.v1.rivtabp21.GetExemptionStatusesResponderService;
import se.skltp.aggregatingservices.config.TestProducerConfiguration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="getaggregatedexemptionstatuses.v1.teststub")
public class ServiceConfiguration extends TestProducerConfiguration {

  public static final String SCHEMA_PATH = "/schemas/financial_patientfees_exemption/interactions/GetExemptionStatusesInteraction/GetExemptionStatusesInteraction_1.0_RIVTABP21.wsdl";

  public ServiceConfiguration() {
    setProducerAddress("http://localhost:8083/vp");
    setServiceClass(GetExemptionStatusesResponderInterface.class.getName());
    setServiceNamespace("urn:riv:financial:patientfees:exemption:GetExemptionStatusesResponder:1");
    setPortName(GetExemptionStatusesResponderService.GetExemptionStatusesResponderPort.toString());
    setWsdlPath(SCHEMA_PATH);
    setTestDataGeneratorClass(ServiceTestDataGenerator.class.getName());
    setServiceTimeout(27000);
  }

}
