package ara.projet.checkpointing;

import java.util.ArrayList;
import java.util.List;

import peersim.core.Control;
import peersim.config.Configuration;
import peersim.core.Node;
import peersim.core.Network;

import ara.projet.checkpointing.algorithm.JuangVenkatesanAlgo;


public class JuangVenkatesanAlgoInitializer implements Control {
	private static final String PAR_PROTO_TASK="task";
	private final int taskpid;


	public JuangVenkatesanAlgoInitializer(String prefix) {
		taskpid=Configuration.getPid(prefix+"."+PAR_PROTO_TASK);
	}


	@Override
	public boolean execute() {
		for(int i = 0 ; i < Network.size() ; i++) {
			Node node = Network.get(i);
			JuangVenkatesanAlgo jva = (JuangVenkatesanAlgo) node.getProtocol(taskpid);
			jva.createCheckpoint(node);
		}

		return false;
	}
}