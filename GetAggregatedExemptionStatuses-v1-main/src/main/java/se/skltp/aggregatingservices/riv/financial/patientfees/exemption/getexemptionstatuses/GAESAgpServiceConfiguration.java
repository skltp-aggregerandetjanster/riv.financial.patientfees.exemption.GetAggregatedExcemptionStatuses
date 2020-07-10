package se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getexemptionstatuses;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import riv.financial.patientfees.exemption.getexemptionstatuses.v1.rivtabp21.GetExemptionStatusesResponderInterface;
import riv.financial.patientfees.exemption.getexemptionstatuses.v1.rivtabp21.GetExemptionStatusesResponderService;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "getaggregatedexemptionstatuses.v1")
public class GAESAgpServiceConfiguration extends se.skltp.aggregatingservices.configuration.AgpServiceConfiguration {

public static final String SCHEMA_PATH = "/schemas/financial_patientfees_exemption/interactions/GetExemptionStatusesInteraction/GetExemptionStatusesInteraction_1.0_RIVTABP21.wsdl";

  public GAESAgpServiceConfiguration() {

    setServiceName("GetAggregatedExemptionStatuses-v1");
    setTargetNamespace("urn:riv:financial:patientfees:exemption:GetExemptionStatuses:1:rivtabp21");

    // Set inbound defaults
    setInboundServiceURL("http://localhost:8081/GetAggregatedExemptionStatuses/service/v1");
    setInboundServiceWsdl(SCHEMA_PATH);
    setInboundServiceClass(GetExemptionStatusesResponderInterface.class.getName());
    setInboundPortName(GetExemptionStatusesResponderService.GetExemptionStatusesResponderPort.toString());

    // Set outbound defaults
    setOutboundServiceWsdl(SCHEMA_PATH);
    setOutboundServiceClass(GetExemptionStatusesResponderInterface.class.getName());
    setOutboundPortName(GetExemptionStatusesResponderService.GetExemptionStatusesResponderPort.toString());

    // FindContent
    setEiServiceDomain("riv:financial:patientfees:exemption");
    setEiCategorization("fpe-es");

    // TAK
    setTakContract("urn:riv:financial:patientfees:exemption:GetExemptionStatusesResponder:1");

    // Set service factory
    setServiceFactoryClass(GAESAgpServiceFactoryImpl.class.getName());
    }


}
