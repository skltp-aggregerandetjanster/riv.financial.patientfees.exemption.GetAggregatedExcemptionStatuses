package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedcaredocumentation

trait CommonParameters {
  val serviceName:String     = "CareDocumentation"
  val urn:String             = "urn:riv:clinicalprocess:healthcond:description:GetCareDocumentationResponder:2"
  val responseElement:String = "GetCareDocumentationResponse"
  val responseItem:String    = "careDocumentation"
  var baseUrl:String         = if (System.getProperty("baseUrl") != null && !System.getProperty("baseUrl").isEmpty()) {
                                   System.getProperty("baseUrl")
                               } else {
                                   "http://33.33.33.33:8081/GetAggregatedCareDocumentation/service/v2"
                               }
}
