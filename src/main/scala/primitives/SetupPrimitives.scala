package primitives

import model.Session
import org.nlogo.api.OutputDestination.Normal
import org.nlogo.api._
import org.nlogo.agent.Turtle
import org.nlogo.core.Syntax.{AgentsetType, CommandType, ListType, NumberType, ReporterType, StringType, WildcardType}
import org.nlogo.agent.World
import org.nlogo.core.{LogoList, Syntax}

/*class Learner extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax()

  override def perform(args: Array[Argument], context: Context): Unit = {
    Session.instance().addAgent(context.getAgent)
  }
}*/

class LearningRate extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(List(NumberType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    Session.instance().learningRate = args(0).getDoubleValue
  }
}

class DiscountFactor extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(List(NumberType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    Session.instance().discountFactor = args(0).getDoubleValue
  }
}

class ActionSelection extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(List(StringType, ListType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    val method : String = args(0).getString.toLowerCase
    var epsilon : Double = 0
    var decreaseRate : Double = 0
    if(method.equalsIgnoreCase("e-greedy")) {
       epsilon = args(1).getList.get(0).asInstanceOf[Double]
       decreaseRate = args(1).getList.get(1).asInstanceOf[Double]
    }
    val actionSelection = Session.instance().actionSelection
    actionSelection.method = method
    actionSelection.epsilon = epsilon
    actionSelection.decreaseRate = decreaseRate
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
  override def getSyntax: Syntax = Syntax.commandSyntax(right = List(CommandType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    val action : AnonymousCommand = args(0).getCommand
    /*val actionsTemp : LogoList = args(0).getList
    var actions : List[String] = List()
    actionsTemp.indices.foreach(i => {
      actions = actions :+ actionsTemp.get(i).toString
    })*/
    Session.instance().actions = Session.instance().actions :+ action
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
  override def getSyntax: Syntax = Syntax.commandSyntax(right = List(ListType, WildcardType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    val optAgent : Option[model.Agent] = Session.instance().getAgent(context.getAgent)
    val turtle : Turtle = context.world.asInstanceOf[World].getTurtle(context.getAgent.id)
    if(optAgent.isEmpty) {
      val variablesTemp : LogoList = args(0).getList
      var variables : List[String] = List()
      variablesTemp.indices.foreach(i => {
        val variable : String = variablesTemp.get(i).toString.toUpperCase
        if(turtle.ownsVariable(variable)) {
          variables = variables :+ variable
        } else {
          throw new ExtensionException("Breed " + turtle.getBreed.printName + " doesn't own " + variable)
        }
      })

      val stateDef : model.StateDefinition = model.StateDefinition(vars = variables)
      try {
        stateDef.reporterAux = args(1).getReporter
      } catch {
        case _ : ExtensionException =>
          stateDef.stringAux = args(1).getString
      }

      val agent : model.Agent = new model.Agent(agent = context.getAgent, stateDef = stateDef)
      Session.instance().addAgent(agent)
    } else {
      throw new ExtensionException("State definition for agent " + context.getAgent.id + " is already defined. \n" +
                                    "the breed of the agent is: " + turtle.getBreed.printName)
    }

    val optAgentt : Option[model.Agent] = Session.instance().getAgent(context.getAgent)
    context.workspace.outputObject(
      optAgentt.get.stateDef.toString() , null, true, false, Normal)

  }
}

class StateDefinition1 extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(right = List(ListType, ReporterType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    val optAgent : Option[model.Agent] = Session.instance().getAgent(context.getAgent)
    val turtle : Turtle = context.world.asInstanceOf[World].getTurtle(context.getAgent.id)
    if(optAgent.isEmpty) {
      val variablesTemp : LogoList = args(0).getList
      var variables : List[String] = List()
      variablesTemp.indices.foreach(i => {
        val variable : String = variablesTemp.get(i).toString.toUpperCase
        if(turtle.ownsVariable(variable)) {
          variables = variables :+ variable
        } else {
          throw new ExtensionException("Breed " + turtle.getBreed.printName + " doesn't own " + variable)
        }
      })

      val stateDef : model.StateDefinition = model.StateDefinition(vars = variables)

        stateDef.reporterAux = args(1).getReporter

      context.workspace.outputObject(
        stateDef.reporterAux.report(context, Array()) , null, true, false, Normal)

      val agent : model.Agent = new model.Agent(agent = context.getAgent, stateDef = stateDef)
      Session.instance().addAgent(agent)
    } else {
      throw new ExtensionException("State definition for agent " + context.getAgent.id + " is already defined. \n" +
        "the breed of the agent is: " + turtle.getBreed.printName)
    }



    val optAgentt : Option[model.Agent] = Session.instance().getAgent(context.getAgent)
    context.workspace.outputObject(
      optAgentt.get.stateDef.toString() , null, true, false, Normal)

  }
}


/*
context.workspace.outputObject(
"oi", null, true, false, Normal)*/