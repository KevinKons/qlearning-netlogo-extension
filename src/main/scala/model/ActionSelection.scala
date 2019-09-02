package model

import org.nlogo.api.ExtensionException

class ActionSelection () {

  private var p_method : String = ""
  private var p_epsilon : Double = 0
  private var p_decreaseRate : Double = 0

  def method : String = p_method
  def epsilon : Double = p_epsilon
  def decreaseRate : Double = p_decreaseRate

  def method_= (m:String): Unit = {
    if(m.equalsIgnoreCase("e-greedy") || m.equalsIgnoreCase("random-normal"))
      p_method = m
    else
      throw new ExtensionException("Action selection method must be e-greedy or random-normal, " + m +
        " is not an valid action selection method")
  }

  def epsilon_= (r:Double) : Unit = {
    if(epsilon > 1 || epsilon < 0)
      throw new ExtensionException("Epsilon must be a value between 0 and 1")
    else
      p_epsilon = r
  }

  def decreaseRate_= (r:Double) : Unit = {
    if(decreaseRate > 1 || decreaseRate < 0)
      throw new ExtensionException("Decrease rate must be a value between 0 and 1")
    else
      p_decreaseRate = r
  }

}
