package primitives

import model.Session
import org.nlogo.api.OutputDestination.Normal
import org.nlogo.api._
import org.nlogo.agent.Turtle
import org.nlogo.core.Syntax.{CommandType, ListType, NumberType, ReporterType, StringType, RepeatableType}
import org.nlogo.agent.World
import org.nlogo.core.{LogoList, Syntax}

class LearningRate extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(List(NumberType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    val optAgent : Option[model.Agent] = Session.instance().getAgent(context.getAgent)
    if(optAgent.isEmpty)
      throw new ExtensionException(
        "You should first define a state definition to this agent. Agent id: " + context.getAgent.id)
    else
      optAgent.get.learningRate = args(0).getDoubleValue
  }
}

class DiscountFactor extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(List(NumberType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    val optAgent : Option[model.Agent] = Session.instance().getAgent(context.getAgent)
    if(optAgent.isEmpty)
      throw new ExtensionException(
        "You should first define a state definition to this agent. Agent id: " + context.getAgent.id)
    else
      optAgent.get.discountFactor = args(0).getDoubleValue
  }
}

class ActionSelection extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(List(StringType, ListType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    val optAgent : Option[model.Agent] = Session.instance().getAgent(context.getAgent)
    if(optAgent.isEmpty)
      throw new ExtensionException(
        "You should first define a state definition to the agent. Agent id: " + context.getAgent.id)
    else {
      val method : String = args(0).getString.toLowerCase
      var epsilon : Double = 0
      var decreaseRate : Double = 0
      if(method.equalsIgnoreCase("e-greedy")) {
         epsilon = args(1).getList.get(0).asInstanceOf[Double]
         decreaseRate = args(1).getList.get(1).asInstanceOf[Double]
      }
      val actionSelection = optAgent.get.actionSelection
      actionSelection.method = method
      actionSelection.epsilon = epsilon
      actionSelection.decreaseRate = decreaseRate
    }
  }
}

class EndEpisode extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(right = List(ReporterType, CommandType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    val optAgent : Option[model.Agent] = Session.instance().getAgent(context.getAgent)
    if(optAgent.isEmpty)
      throw new ExtensionException(
        "You should first define a state definition to the agent. Agent id: " + context.getAgent.id)
    else {
      optAgent.get.isEndEpisode = args(0).getReporter
      optAgent.get.resetEpisode = args(1).getCommand
    }
  }
}

class Reward extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(right = List(ReporterType))

  override def perform(args: Array[Argument], context: Context): Unit = {
    val optAgent : Option[model.Agent] = Session.instance().getAgent(context.getAgent)
    if(optAgent.isEmpty)
      throw new ExtensionException(
        "You should first define an state definition to the agent. Agent id: " + context.getAgent.id)
    else
      optAgent.get.rewardFunc = args(0).getReporter
  }
}

class Actions extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(right = List(CommandType | RepeatableType), defaultOption = Some(1))

  override def perform(args: Array[Argument], context: Context): Unit = {
    val optAgent : Option[model.Agent] = Session.instance().getAgent(context.getAgent)
    if(optAgent.isEmpty)
      throw new ExtensionException(
        "You should first define an state definition to the agent. Agent id: " + context.getAgent.id)
    else {
      args.foreach(action => {
        optAgent.get.actions = optAgent.get.actions :+ action.getCommand
      })
    }
  }
}

class StateDefinition extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(right = List(ListType))

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
      val agent : model.Agent = new model.Agent(agent = context.getAgent, stateDef = stateDef)
      Session.instance().addAgent(agent)
    } else {
      throw new ExtensionException("State definition for agent " + context.getAgent.id + " is already defined. \n" +
                                    "the breed of the agent is: " + turtle.getBreed.printName)
    }
  }
}

class StateDefinitionExtra extends Command {
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

      val agent : model.Agent = new model.Agent(agent = context.getAgent, stateDef = stateDef)
      Session.instance().addAgent(agent)
    } else {
      throw new ExtensionException("State definition for agent " + context.getAgent.id + " is already defined. \n" +
        "the breed of the agent is: " + turtle.getBreed.printName)
    }
  }
}

class RandomSeed extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(right = List(NumberType))

  override def perform(args: Array[Argument], context: Context): Unit = {

    Session.instance().getAgent(context.getAgent).get.actionSelection.randomGen.setSeed(args(0).getDoubleValue.asInstanceOf[Long])
  }
}




/*
context.workspace.outputObject(
"oi", null, true, false, Normal)*/