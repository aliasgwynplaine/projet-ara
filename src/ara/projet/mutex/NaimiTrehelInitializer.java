package ara.projet.mutex;

import java.util.ArrayList;
import java.util.List;

import peersim.core.Control;
import peersim.config.Configuration;
import peersim.core.Node;
import peersim.core.Network;

import ara.projet.mutex.NaimiTrehelAlgo;


public class NaimiTrehelInitializer implements Control {
	private static final String PAR_PROTO_TASK="task";
	private final int taskpid;


	public NaimiTrehelInitializer(String prefix) {
		taskpid=Configuration.getPid(prefix+"."+PAR_PROTO_TASK);
	}


	@Override
	public boolean execute() {
		for(int i = 0 ; i < Network.size() ; i++) {
			Node node = Network.get(i);
			NaimiTrehelAlgo nh = (NaimiTrehelAlgo) node.getProtocol(taskpid);
			nh.initialisation(node);
		}

		return false;
	}
}
