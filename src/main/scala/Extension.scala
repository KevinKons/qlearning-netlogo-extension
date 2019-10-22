import org.nlogo.api.{DefaultClassManager, PrimitiveManager}
import primitives._

class Extension extends DefaultClassManager {
  def load(manager: PrimitiveManager) {
    manager.addPrimitive("state-def", new StateDefinition)
    manager.addPrimitive("state-def-extra", new StateDefinitionExtra)
    manager.addPrimitive("actions", new Actions)
    manager.addPrimitive("end-episode", new EndEpisode)
    manager.addPrimitive("reward", new Reward)
    manager.addPrimitive("action-selection", new ActionSelection)
    manager.addPrimitive("learning-rate", new LearningRate)
    manager.addPrimitive("discount-factor", new DiscountFactor)
    manager.addPrimitive("learning", new Learning)
    manager.addPrimitive("episode", new GetEpisode)
    manager.addPrimitive("get-qtable", new GetQTable)
    manager.addPrimitive("random-seed", new RandomSeed)
    manager.addPrimitive("decay-epsilon", new DecayEpsilon)
  }
}