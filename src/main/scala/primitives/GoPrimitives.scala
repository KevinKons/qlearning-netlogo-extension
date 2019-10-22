package primitives

import model.{Agent, Session}
import org.nlogo.api.OutputDestination.Normal
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.{BooleanType, NumberType, StringType, RepeatableType}
import org.nlogo.api.ScalaConversions._

import scala.collection.mutable

class Learning extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax(
    right = List(BooleanType | RepeatableType),
    defaultOption = Some(0),
    minimumOption = Some(0)
  )

  override def perform(args: Array[Argument], context: Context): Unit = {
    val optAgent : Option[Agent] = Session.instance().getAgent(context.getAgent)
    if(optAgent.isEmpty) {
      throw new ExtensionException("Agent " + context.getAgent.id + " isn't a learner agent")
    } else {
      val agent : Agent = optAgent.get

      if(agent.actions.isEmpty)
        throw new ExtensionException("No action has been defined for agent " + context.getAgent.id)

      if(agent.learningRate == -1)
        throw new ExtensionException("No learning rate has been defined for agent " + context.getAgent.id)

      if(agent.discountFactor == -1)
        throw new ExtensionException("No discount factor has been defined for agent " + context.getAgent.id)

      if(agent.actionSelection.method == "")
        throw new ExtensionException("No action selection method has been defined for agent " + context.getAgent.id)

      val actualState : String = agent.getState(context)
      var actualQlist : List[Double] = null
      val optQlist : Option[List[Double]] = agent.qTable.get(actualState)

      if(optQlist.isEmpty) { //Estado não visitado anteriormente
        actualQlist = List.fill(agent.actions.length)(0)
        agent.qTable += (actualState -> actualQlist)
      } else {
        actualQlist = optQlist.get
      }

      val actionActualState : Int = agent.actionSelection.getAction(actualQlist, context)

      //val params : Array[AnyRef] = Array()
      agent.actions(actionActualState).perform(context, Array(AnyRef))

      val qValueActualState : Double = actualQlist(actionActualState)
      val reward : Double = try agent.rewardFunc.report(context, Array(AnyRef)).asInstanceOf[Double]
      catch {
        case _ : NullPointerException =>
          throw new ExtensionException("No reward function for agent " + context.getAgent.id + " was defined")
      }
      val newState : String = agent.getState(context)
      val newStateBestAction : Double = agent.getBestActionExpectedReward(newState)

      val newQvalue : Double =
        qValueActualState + (agent.learningRate * (reward + (agent.discountFactor * newStateBestAction) - qValueActualState))

      val newQlist : List[Double] = actualQlist.patch(actionActualState, List(newQvalue), 1)
      agent.qTable += (actualState -> newQlist)

      if(args.length > 0 && args(0).getBooleanValue) {
        val print : String =
            "Old state: " + actualState + "\n" +
            "Old qlist: " + actualQlist.toString() + "\n" +
            "new state reward: " + reward + "\n" +
            "new State: " + newState + "\n" +
            "new state best action: " + newStateBestAction + "\n" +
            "new QList: " + newQlist +
            "epsilon: " + agent.actionSelection.epsilon +
            "\n-----------------------------"


        context.workspace.outputObject(
          print , null, true, false, Normal)
      }

      val isEndEpisode : Boolean = agent.isEndEpisode.report(context, Array()).asInstanceOf[Boolean]
      if(isEndEpisode) {
        agent.episode = agent.episode + 1
        agent.actionSelection.epsilon = agent.actionSelection.epsilon * agent.actionSelection.decreaseRate
//        if(agent.actionSelection.method.equalsIgnoreCase("e-greedy")) {
//          if (agent.actionSelection.epsilon * agent.actionSelection.decreaseRate < 0)
//            agent.actionSelection.epsilon = 0
//          else if (agent.actionSelection.epsilon * agent.actionSelection.decreaseRate > 0)
//        }
        agent.resetEpisode.perform(context, Array())
      }
    }
  }
}

class GetEpisode extends Reporter {
  override def getSyntax: Syntax = Syntax.reporterSyntax(ret = NumberType)

  override def report(args: Array[Argument], context: Context): AnyRef = {
    Session.instance().getAgent(context.getAgent).get.episode.toLogoObject
  }
}

class GetQTable extends Reporter {
  override def getSyntax: Syntax = Syntax.reporterSyntax(ret = StringType)

  override def report(args: Array[Argument], context: Context): AnyRef = {
    val qTable : mutable.Map[String, List[Double]] = Session.instance().getAgent(context.getAgent).get.qTable
    var ret : String = "--------- Q-Table --------- \n"
    for ((k,v) <- qTable) {
      ret = ret + k + " -> "
      v.foreach(e => {
        ret = ret + e + " / "
      })
      ret = ret.substring(0, ret.length - 3) + "\n"
    }
    ret + "------------------------------" + "\n"
  }
}

class DecayEpsilon extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax()

  override def perform(args: Array[Argument], context: Context): Unit = {
    val optAgent : Option[Agent] = Session.instance().getAgent(context.getAgent)
    if(optAgent.isEmpty) {
      throw new ExtensionException("Agent " + context.getAgent.id + " isn't a learner agent")
    } else {
      val agent : Agent = optAgent.get
      agent.actionSelection.epsilon = agent.actionSelection.epsilon * agent.actionSelection.decreaseRate
    }
  }
}


