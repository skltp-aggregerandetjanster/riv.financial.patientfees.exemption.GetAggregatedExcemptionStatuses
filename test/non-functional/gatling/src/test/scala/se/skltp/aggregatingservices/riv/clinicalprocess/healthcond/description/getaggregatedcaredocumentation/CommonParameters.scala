package se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getaggregatedcaredexemptionstatuses

trait CommonParameters {
  val serviceName:String     = "ExcemptionStatuses"
  val urn:String             = "urn:riv:financial:patientfees:exemption:GetAggregatedExcemptionStatusesResponder:1"
  val responseElement:String = "GetAggregatedExcemptionStatusesResponse"
  val responseItem:String    = "exemptionStatuses"
  var baseUrl:String         = if (System.getProperty("baseUrl") != null && !System.getProperty("baseUrl").isEmpty()) {
                                   System.getProperty("baseUrl")
                               } else {
                                   "http://33.33.33.33:8081/GetAggregatedExcemptionStatuses/service/v2"
                               }
}
