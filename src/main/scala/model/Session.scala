package model

import org.nlogo.api.{AnonymousCommand, AnonymousReporter, ExtensionException}

class Session (var stateDef : StateDefinition = new StateDefinition, var actions : List[AnonymousCommand] = List(),
               var rewardFunc: AnonymousReporter = null, var endEpisode: AnonymousReporter = null,
               var actionSelection: ActionSelection = new ActionSelection, private var p_learningRate : Double = 0,
               private var p_discountFactor : Double = 0, var agents : List[Agent] = List()) {

  def addAgent(agent : org.nlogo.api.Agent) : Unit = {
    agents = agents :+ new Agent(agent = agent)
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

object Session {
  private var _instance : Session = null
  def instance() = {
    if (_instance == null)
      _instance = new Session()
    _instance
  }
}
