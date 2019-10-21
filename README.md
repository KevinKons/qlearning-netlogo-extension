# Q-Learning Netlogo Extension

This extension provides an easy way to use Q-Learning within Netlogo.

## Usage

The first thing you need to do is an `ask` to the breed you want to be the learners. Inside this `ask` you can run the following primitives:

* `qlearningextension:state-def ["var1" "varN"]` used to define a state representation to your learner agent, it recieves a list containing variable names that the agent that did the `ask` owns. Befero running any of the primitives below you must first run this primitive. Another way to define the state definition is with the following primitive: `qlearningextension:state-def ["var1" "varN"] reporter` the reporter parameter must be a reporter that returns a string, every time the extension is about to generate a state the reporter will be called and the return will be added to the state definition of that state. This other form to define the state definiton provides a way to add values to the state definition that aren't variables that the learner agent owns.
* `(qlearningextension:action [action1] [action2] [actionN])` used to define what actions the learner agent can perform, it recieves the actions that the agent can perform. You can pass how many actions you want, but they must be procedures not reporters. Obs:Do not forget the parentheses encapsulating the primitive call and the brackets surrounding each action.
* `learningextension:reward [rewardFunc]` used to define a reporter that will return a number with the reward of the actual state.
* `qlearningextension:end-episode [isEndState] resetEpisode` the first parameter is a reporter that will return a boolean value informing if the actual state characterizes the end of an episode or not. The second one is a procedure that is executed after the end of an episode, this procedure must, for exemple, set the agent/enviroment to it's initial state.
* `qlearningextension:action-selection "type" []` used to define the action selection type, there are two types availabe: random-normal and e-greedy. The first one will select and random action according to the percentage passed through the parameter. The second will select an action according to the percentage passed by the parameters, but you will pass the "decrease rate" to another parameter; in e-greedy after each episode, the percentage will be decreased by the decrease rate. Here are two examples of the action-selection primitive use: `qlearningextension:action-selection "random-normal" [0.8]` thus, 80% of the actions will be random actions; `qlearningextension:action-selection "e-greedy" [0.8 0.99995]` thus, 80% of the actions will be random actions, but after each episode this percentage is updated, the new value correspond to the current value multiplied by the decrease rate. In both cases the numbers passed must be between 0 and 1.
* `qlearningextension:learning-rate learningRate` used to inform the learning rate, it expects a value between 0 and 1.
* `qlearningextension:discount-factor discountFactor` used to inform the discount factor, it expects a value between 0 and 1.

Obs: It is advisable to call a `clear-all` in the setup procedure, if don't called it things will get duplicated.

Now, with everything setted up you can run the simulation. In your "go" routine inside an `ask` to the learner agent you can run the primitive `qlearningextension:learning`. This will select an action to the current state, perform the action, get the reward, update the Q-table, verify if the new state is an end state and if so will run the procedure passed to the extension in the `end-episode` primitive.

To help you in debugging your simulations you can call the learning primitive in a little different way: `(qlearningextension:learning true)`, calling the primitive this way will make the extension print in the console the following values: The old state representation, the old Q-list (the Q-table values of the old state), the reward of the new state, the new state representation, the expected reward of the new state and the new Q-list. Another way to debugging your simulations calling the `qlearningextension:get-qtable` primitive, this will return a string with the current Q-table.

## Example

To make it easier to understand the usage of the extension we [implemented](https://github.com/KevinKons/qlearning-netlogo-extension/blob/v0.2/cliff-walking.nlogo) the classic [cliff-waking problem](https://medium.com/@lgvaz/understanding-q-learning-the-cliff-walking-problem-80198921abbc) using the extension.
