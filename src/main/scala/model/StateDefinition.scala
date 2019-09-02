package model

import org.nlogo.api.AgentSet

import scala.collection.mutable


case class StateDefinition(var breedVar : mutable.Map[AgentSet, List[String]] = mutable.Map()) {
  def addBreed(agentset : AgentSet, variables : List[String]): Unit = {
    breedVar += (agentset -> variables)
  }
}
