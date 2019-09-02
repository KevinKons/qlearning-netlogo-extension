package utils

import org.nlogo.agent.{Turtle, World}
import org.nlogo.api.{AgentSet, ExtensionException}

import scala.util.control.Breaks._

object Verifications {
  def isBreedOwnVariable(agentset : AgentSet, variable : String) : Boolean = {
    var turtle : Turtle = null
    breakable {
      agentset.agents.forEach(agent => {
        val world : World = agent.world.asInstanceOf[World]
        turtle = world.getTurtle(agent.id)
        break
      })
    }
    try {
      turtle.variables.indices.foreach(i => {
        if (turtle.variableName(i) == variable) {
          return true
        }
      })
      throw new ExtensionException("Breed " + agentset.printName + " doesn't own " + variable)
    } catch {
      case _ : NullPointerException =>
        throw new ExtensionException(
          "An agent of breed " + agentset.printName + " must be created before using this reporter")
    }
  }
}


