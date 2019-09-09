package model

import org.nlogo.agent.{Turtle, World}
import org.nlogo.api.ExtensionException

import scala.collection.mutable

class Agent (var qTable : mutable.Map[String, List[Double]] = mutable.Map(), var agent : org.nlogo.api.Agent = null,
             var stateDef : StateDefinition = null) {

  def getBestActionExpectedReward(state : String): Double = {
    val optQlist : Option[List[Double]] = qTable.get(state)
    if(optQlist.isEmpty) { //Estado não visitado anteriormente
      0
    } else {
      val qlist : List[Double] = optQlist.get
      qlist.max
    }
  }

  def getState : String = {
    var state : String = ""
    for ((agentset, fields) <- Session.instance().stateDef.breedVar) {
      agentset.agents.forEach(a => {
        fields.foreach(f => {
          val world : World = a.world.asInstanceOf[World]
          val turtle : Turtle = world.getTurtle(a.id)
          val fieldValue = turtle.getVariable(f)
          state += fieldValue
        })
      })
    }
    state
  }
}

object AgentUtilities {
  def getAgent(agents : List[Agent], agent : org.nlogo.api.Agent) : Agent = {
    Session.instance().agents.foreach(a => {
      if(a.agent == agent) {
        return a
      }
    })
    throw new ExtensionException(agent + " is not a learner agent")
  }
}
