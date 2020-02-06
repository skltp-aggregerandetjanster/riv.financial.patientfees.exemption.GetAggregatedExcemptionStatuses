package se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getaggregatedcaredexemptionstatuses

trait CommonParameters {
  val serviceName:String     = "ExemptionStatuses"
  val urn:String             = "urn:riv:financial:patientfees:exemption:GetAggregatedExemptionStatusesResponder:1"
  val responseElement:String = "GetAggregatedExemptionStatusesResponse"
  val responseItem:String    = "exemptionStatuses"
  var baseUrl:String         = if (System.getProperty("baseUrl") != null && !System.getProperty("baseUrl").isEmpty()) {
                                   System.getProperty("baseUrl")
                               } else {
                                   "http://33.33.33.33:8081/GetAggregatedExemptionStatuses/service/v2"
                               }
}
