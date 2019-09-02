package primitives

import model.Session
import org.nlogo.api.OutputDestination.Normal
import org.nlogo.api._
import org.nlogo.core.Syntax.{AgentsetType, ListType, ReporterType, StringType}
import org.nlogo.core.{LogoList, Syntax}
import utils.Verifications


class ActionSelection extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(List(StringType, ListType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    val method : String = args(0).getString.toLowerCase
    if(method == "e-greedy") {
      val epsilon : Double = args(0).getList.get(0).asInstanceOf[Double]
      val decreaseRate : Double = args(0).getList.get(1).asInstanceOf[Double]
      if(epsilon > 1 || epsilon < 0) {
        throw new ExtensionException("Epsilon must be a value between 0 and 1")
      } else if(decreaseRate > 1 || decreaseRate < 0) {
        throw new ExtensionException("Decrease rate must be a value between 0 and 1")
      }

    } else if (method == "random-normal") {

    }
  }
}

class EndEpisode extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(right = List(ReporterType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    Session.instance().endEpisode = args(0).getReporter
  }
}

class Reward extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(right = List(ReporterType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    Session.instance().rewardFunc = args(0).getReporter
  }
}

class Actions extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(right = List(ListType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    val actionsTemp : LogoList = args(0).getList
    var actions : List[String] = List()
    actionsTemp.indices.foreach(i => {
      actions = actions :+ actionsTemp.get(i).toString
    })
    Session.instance().actions = actions
  }
}

class TesteAgentSetOrder extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax()

  override def perform(args: Array[Argument], context: Context): Unit = {
    Session.instance().stateDef.breedVar.keys.foreach(keys => {
      context.workspace.outputObject(
        keys.printName, null, true, false, Normal)
      keys.agents.forEach(agent => {
        context.workspace.outputObject(
          agent.id.toString + " " + agent.classDisplayName , null, true, false, Normal)
      })
    })
  }

}

class StateDefinition extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(right = List(AgentsetType, ListType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    val agentset : AgentSet = args(0).getAgentSet
    val variablesTemp : LogoList = args(1).getList
    var variables : List[String] = List()
    variablesTemp.indices.foreach(i => {
      val variable : String = variablesTemp.get(i).toString.toUpperCase
      if(Verifications.isBreedOwnVariable(agentset, variable)) {
        variables = variables :+ variable
      }
    })
    Session.instance().stateDef.addBreed(agentset, variables)
  }
}
/*
context.workspace.outputObject(
"oi", null, true, false, Normal)*/