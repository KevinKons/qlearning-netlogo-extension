import org.nlogo.api.{DefaultClassManager, PrimitiveManager}
import primitives._

class Extension extends DefaultClassManager {
  def load(manager: PrimitiveManager) {
    manager.addPrimitive("state-def", new StateDefinition)
    manager.addPrimitive("state-def-extra", new StateDefinitionExtra)
//    manager.addPrimitive("agentset-order", new TesteAgentSetOrder)
    manager.addPrimitive("action", new Actions)
    manager.addPrimitive("end-episode", new EndEpisode)
    manager.addPrimitive("reward", new Reward)
    manager.addPrimitive("action-selection", new ActionSelection)
    manager.addPrimitive("learning-rate", new LearningRate)
    manager.addPrimitive("discount-factor", new DiscountFactor)
    manager.addPrimitive("learning", new Learning)
  }
}