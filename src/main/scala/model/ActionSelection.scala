package model

import org.nlogo.api.ExtensionException

class ActionSelection (val randomGen : scala.util.Random = scala.util.Random) {

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

  def getAction(qlist : List[Double], context : org.nlogo.api.Context) : Int = {
    var actionPos : Int = 0
    val random = randomGen.nextDouble()
    if(random <= epsilon) {
      actionPos = randomGen.nextInt(qlist.length)
    } else {
      val max : Double = qlist.max
      var maxList : List[Int] = List()
      qlist.indices.foreach(i => {
        if(qlist(i) == max)
          maxList = maxList :+ i
      })
      actionPos = maxList(randomGen.nextInt(maxList.length))
    }
    actionPos
  }
}
