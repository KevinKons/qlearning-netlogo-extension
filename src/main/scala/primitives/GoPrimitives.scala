package primitives

import model.{Agent, AgentUtilities, Session}
import org.nlogo.api.OutputDestination.Normal
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.StringType


class Learning extends Reporter {
  override def getSyntax: Syntax = Syntax.reporterSyntax(ret = StringType)

  override def report(args: Array[Argument], context: Context): AnyRef = {
    val session : Session = Session.instance()
    val agent : Agent = AgentUtilities.getAgent(Session.instance().agents, context.getAgent)
    val actualState : String = agent.getState
    var actualQlist : List[Double] = null
    val optQlist : Option[List[Double]] = agent.qTable.get(actualState)

    if(optQlist.isEmpty) { //Estado não visitado anteriormente
      actualQlist = List.fill(session.actions.length)(0)
      agent.qTable += (actualState -> actualQlist)
    } else {
      actualQlist = optQlist.get
    }

    val actionActualState : AnonymousCommand = session.actionSelection.getAction(actualQlist, context)
    //var newQlist : List[Double] = actualQlist.patch(2, List(10.0), 1)
    //agent.qTable += (actualState -> newQlist)

    val params : Array[AnyRef] = Array()
    actionActualState.perform(context, params)

    val reward : Double = Session.instance().rewardFunc.report(context, params).asInstanceOf[Double]
    val newState : String = agent.getState
    val newStateBestAction : Double = agent.getBestActionExpectedReward(newState)

    //val newQvalue : Double =


    actionActualState
  }

}
