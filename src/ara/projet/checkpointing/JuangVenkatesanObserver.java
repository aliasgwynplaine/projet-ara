package ara.projet.checkpointing.algorithm;

import static ara.util.Constantes.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import ara.projet.mutex.NaimiTrehelAlgo.State;

import ara.projet.checkpointing.Checkpointable;
import ara.projet.checkpointing.Checkpointer;
import ara.projet.checkpointing.NodeState;
import ara.projet.checkpointing.NaimiTrehelAlgoCheckpointable;
import ara.util.Message;
import ara.util.MyRandom;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Fallible;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.edsim.EDSimulator;
import peersim.transport.Transport;



public class JuangVenkatesanObserver implements Control {
	private static final String PAR_ALG = "protocol";
	private static final String PAR_EXP = "experience";
	private static final String PAR_ALPHA = "alpha";
	private static final String PAR_GAMMA = "gamma";
	private static final String PAR_BETA  = "beta";
	
	private final int protocol_id;
	private final String experience;
	private final int alpha;
	private final int gamma;
	private final int beta;

	protected long r_time;             // time recouvrement
	protected long nb_msg;             // nb messages par recourvrement
	protected long stocka;             // stockage
	protected long ancien;             // ancienneté


	public JuangVenkatesanObserver(String prefix) {
		String tmp[] = prefix.split("\\.");
		protocol_id = Configuration.getPid(prefix + "." + PAR_ALG);
		experience = Configuration.getString(prefix + "." + PAR_EXP);
		alpha = Configuration.getInt(PAR_ALPHA);
		gamma = Configuration.getInt(PAR_GAMMA);
		beta = Configuration.getInt(PAR_BETA);
		r_time = 0;
		nb_msg = 0;
		stocka = 0;
		ancien = 0;
	}

	@Override
	public boolean execute() {

		for (int i = 0; i < Network.size(); i++) {
			Node node = Network.get(i);
			NaimiTrehelAlgoCheckpointable nha = (NaimiTrehelAlgoCheckpointable) node.getProtocol(protocol_id);

			// inspect metrics...
			// verifier
		}


		if (CommonState.getTime() == CommonState.getTime()) {
			// store metrics
		}

		return false;
	}
}
