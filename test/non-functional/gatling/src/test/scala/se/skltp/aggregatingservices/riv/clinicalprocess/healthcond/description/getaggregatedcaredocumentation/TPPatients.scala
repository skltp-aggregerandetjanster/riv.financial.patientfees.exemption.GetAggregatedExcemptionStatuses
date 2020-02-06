package se.skltp.aggregatingservices.riv.financial.patientfees.exemption.getaggregatedcaredexemptionstatuses

import se.skltp.agp.testnonfunctional.TPPatientsAbstract

/**
 * Test GetAggregatedExcemptionStatuses using test cases defined in patients.csv (or patients-override.csv)
 */
class TPPatients extends TPPatientsAbstract with CommonParameters {
  setUp(setUpAbstract(serviceName, urn, responseElement, responseItem, baseUrl))
}
