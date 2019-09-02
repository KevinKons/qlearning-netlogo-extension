package model

import org.nlogo.api.AnonymousReporter

class Session (var stateDef : StateDefinition = new StateDefinition, var actions : List[String] = List(),
               var rewardFunc: AnonymousReporter = null, var endEpisode: AnonymousReporter = null)

object Session {
  private var _instance : Session = null
  def instance() = {
    if (_instance == null)
      _instance = new Session()
    _instance
  }
}
