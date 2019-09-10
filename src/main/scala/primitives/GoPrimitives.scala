package primitives

import model.{Agent, AgentUtilities, Session}
import org.nlogo.api.OutputDestination.Normal
import org.nlogo.api._
import org.nlogo.core.Syntax


class Learning extends Command {
  override def getSyntax: Syntax = Syntax.commandSyntax()

  override def perform(args: Array[Argument], context: Context): Unit = {
    val session : Session = Session.instance()
    val optAgent : Option[Agent] = Session.instance().getAgent(context.getAgent)
    if(optAgent.isEmpty) {
      throw new ExtensionException("Agent " + context.getAgent.id + " isn't a learner agent")
    } else {
      val agent : Agent = optAgent.get
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

      val print : String =
          "actual State: " + actualState + "\n" +
          "actual qlist: " + actualQlist.toString() + "\n" +
          "qValue Actual State: " + qValueActualState + "\n" +
          "reward: " + reward + "\n" +
          "new State: " + newState + "\n" +
          "new state best action: " + newStateBestAction + "\n" +
          "new Qvalue: " + newQvalue + "\n" +
          "new QList: " + newQlist + "\n-----------------------------"

      context.workspace.outputObject(
        print , null, true, false, Normal)
    }

    //val isEndState : Boolean = session.endEpisode.report(context, Array(AnyRef)).asInstanceOf[Boolean]
  }

}
