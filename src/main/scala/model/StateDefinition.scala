package model

import org.nlogo.api.AnonymousReporter


case class StateDefinition(var vars : List[String] = null, var reporterAux : AnonymousReporter = null,
                           var stringAux : String = null)
