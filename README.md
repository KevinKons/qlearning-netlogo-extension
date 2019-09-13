# Q-Learning Netlogo Extension

This extension provides an easy way to use Q-Learning within Netlogo.

## Usage

The first thing you need to do is an `ask` to the breed you want to be the learners. Inside this `ask` you can run the following primitives:

* `qlearningextension:state-def ["var1" "varN"]` used to define a state representation to your learner agent, it recieves a list containing variable names that the agent that did the `ask` owns. Befero running any of the primitives below you must first run this primitive. 
* `(qlearningextension:action [action1] [action2] [actionN])` used to define what actions the learner agent can perform, it recieves the actions that the agent can perform. You can pass how many actions you want, but they must be procedures not reporters.
* `learningextension:reward [rewardFunc]` used to define a reporter that will return a number with the reward of the actual state.
* `qlearningextension:end-episode [isEndState] resetEpisode` the first parameter is a reporter that will return a boolean value informing if the actual state characterizes the end of an episode or not. The second one is a procedure that is executed after the end of an episode, this procedure must for exemple set the agent/enviroment to it's initial state.
*` qlearningextension:action-selection "type" []` used to define the action selection type, there are two types availabe: random-normal and e-greedy. The first one will select and random action if the according to the percentage passed throgth the parametes, in the type you will pass ´"random-normal"´
*`qlearningextension:learning-rate learningRate` used to inform the learning rate, it expects a value beetwen 0 and 1.
*`qlearningextension:discount-factor discountFactor` used to inform the discount factor, it expects a value beetwen 0 and 1.

Now, with everything setted up you can run the simulation. In your "go" routine inside an `ask` to the learner agent you can run the primitive `qlearningextension:learning`. This will select an action to the current state, perform the action, get the reward, update the Q-table, verify if the new state is an end state and if so will run the procedure passed to the extension in the `end-episode` primitive.
