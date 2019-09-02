import org.nlogo.api.{DefaultClassManager, PrimitiveManager}
import primitives.{Actions, Reward, StateDefinition, TesteAgentSetOrder, ActionSelection}

class Extension extends DefaultClassManager {
  def load(manager: PrimitiveManager) {
    manager.addPrimitive("state-def", new StateDefinition)
    manager.addPrimitive("agentset-order", new TesteAgentSetOrder)
    manager.addPrimitive("actions", new Actions)
    manager.addPrimitive("end-episode", new Reward)
    manager.addPrimitive("reward", new Reward)
    manager.addPrimitive("action-selection", new ActionSelection)

  }
}