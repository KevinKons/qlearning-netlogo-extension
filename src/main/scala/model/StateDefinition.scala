package model

import org.nlogo.api.{AgentSet, AnonymousReporter}

import scala.collection.mutable


case class StateDefinition(var breedVar : mutable.Map[AgentSet, List[String]] = mutable.Map(),
                           var vars : List[String] = null, var reporterAux : AnonymousReporter = null,
                           var stringAux : String = null) {

  def addBreed(agentset : AgentSet, variables : List[String]): Unit = {
    breedVar += (agentset -> variables)
  }
}
