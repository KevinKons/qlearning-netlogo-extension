package model

import org.nlogo.agent.{Turtle, World}
import org.nlogo.api.{AnonymousCommand, AnonymousReporter, ExtensionException, Context}

import scala.collection.mutable

class Agent (var qTable : mutable.Map[String, List[Double]] = mutable.Map(), var agent : org.nlogo.api.Agent = null,
             var stateDef : StateDefinition = null, var actions : List[AnonymousCommand] = List(),
             var rewardFunc: AnonymousReporter = null, var isEndEpisode: AnonymousReporter = null,
             var resetEpisode: AnonymousCommand = null, var actionSelection: ActionSelection = new ActionSelection,
             private var p_learningRate : Double = -1, private var p_discountFactor : Double = -1,
             var episode : Int = 0) {

  def getBestActionExpectedReward(state : String): Double = {
    val optQlist : Option[List[Double]] = qTable.get(state)
    if(optQlist.isEmpty) { //Estado não visitado anteriormente
      0
    } else {
      val qlist : List[Double] = optQlist.get
      qlist.max
    }
  }

  def getState(context : Context) : String = {
    var state : String = ""
    stateDef.vars.foreach(v => {
      val turtle : Turtle = agent.world.asInstanceOf[World].getTurtle(agent.id)
      state += turtle.getVariable(v)
    })
    if(stateDef.reporterAux == null) {
      state
    } else {
      val reporterAuxResult : String = stateDef.reporterAux.report(context, Array()).toString
      state + reporterAuxResult
    }
  }

  def discountFactor : Double = p_discountFactor
  def learningRate : Double = p_learningRate

  def discountFactor_= (f:Double): Unit = {
    if(f > 1 || f < 0) {
      throw new ExtensionException("Discount factor must be a value between 0 and 1")
    }
    p_discountFactor = f
  }

  def learningRate_= (r : Double): Unit = {
    if(r > 1 || r < 0) {
      throw new ExtensionException("Learning rate must be a value between 0 and 1")
    }
    p_learningRate = r
  }
}
