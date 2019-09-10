package model

class Session (var agents : List[Agent] = List()) {

  def addAgent(agent : Agent) : Unit = agents = agents :+ agent

  def getAgent(agent : org.nlogo.api.Agent) : Option[Agent] = {
    agents.foreach(a => {
      if(a.agent == agent) {
        return Option(a)
      }
    })
    None
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
