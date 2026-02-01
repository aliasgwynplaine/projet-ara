package ara.projet.checkpointing;

import static ara.util.Constantes.log;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import ara.projet.mutex.NaimiTrehelAlgo.State;
import ara.projet.mutex.NaimiTrehelAlgoCheckpointable;

import ara.projet.checkpointing.Checkpointable;
import ara.projet.checkpointing.Checkpointer;
import ara.projet.checkpointing.NodeState;
import ara.projet.checkpointing.algorithm.JuangVenkatesanAlgo;

import ara.util.Message;
import ara.util.MyRandom;

import peersim.core.Control;
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
	private static final String PAR_TIMECHECKPOINTING = "timecheckpointing";
	private static final String PAR_CHECKPOINTER = "checkpointer";
	
	private final int protocol_id;
	private final String experience;
	private final int alpha;
	private final int gamma;
	private final int beta;
	private final long timecheckpointing;
	private final int checkpointer_id;

	protected long r_time;             // time recouvrement
	protected long nb_msg;             // nb messages par recourvrement
	protected long nb_app;             // nb messages app
	protected long stocka;             // stockage
	protected long ancienA;             // ancienneté A
	protected long ancienT;             // ancienneté T
	protected long ancienX;             // ancienneté X
	protected long nb_cs;              // nb of critical sections

	protected boolean start_rec = false;
	protected boolean last_stat = false;
	protected boolean recovering = false;
	protected boolean accumulate = false;


	public JuangVenkatesanObserver(String prefix) {
		String tmp[] = prefix.split("\\.");
		protocol_id = Configuration.getPid(prefix + "." + PAR_ALG);
		experience = Configuration.getString(prefix + "." + PAR_EXP);
		alpha = Configuration.getInt(PAR_ALPHA);
		gamma = Configuration.getInt(PAR_GAMMA);
		beta = Configuration.getInt(PAR_BETA);
		checkpointer_id = Configuration.getPid(prefix + "." + PAR_CHECKPOINTER, -1);
		timecheckpointing = Configuration.getLong(prefix + "." + PAR_TIMECHECKPOINTING);
		r_time = 0;
		nb_msg = 0;
		nb_app = 0;
		stocka = 0;
		ancienT = 0;
		ancienA = 0;
		ancienX = 0;
		nb_cs = 0;
	}

	@Override
	public boolean execute() {

		boolean indicator = false;

		for (int i = 0; i < Network.size(); i++) {
			Node node = Network.get(i);
			NaimiTrehelAlgoCheckpointable nha = (NaimiTrehelAlgoCheckpointable) node.getProtocol(protocol_id);
			JuangVenkatesanAlgo jva = (JuangVenkatesanAlgo) node.getProtocol(checkpointer_id);

			//System.out.format("%-12s%-12s%-12s%-12s%-12s%-12s%-12s%-12s\n", CommonState.getTime(), node.getID(), nha.state, nha.global_counter, nha.has_token, jva.is_recovery, jva.nb_msg_sent_during_recover, jva.getMemory());
			// inspect metrics...
			// verifier
			indicator = indicator || jva.is_recovery;
		}

		//System.out.println();

		if (indicator != last_stat) {
			recovering = !recovering;
			last_stat = indicator;

			if (stocka == 0) {
				for (int i = 0; i < Network.size(); i++) {
					Node node = Network.get(i);
					JuangVenkatesanAlgo jva = (JuangVenkatesanAlgo) node.getProtocol(checkpointer_id);
					stocka += jva.getMemory();
				}
			}
		}

		if (recovering) {
			r_time++;
		}

		if (CommonState.getTime() == CommonState.getEndTime() - 1) {
			// store metrics
			System.out.println();

			for (int i = 0; i < Network.size(); i++) {
				Node node = Network.get(i);
				NaimiTrehelAlgoCheckpointable nha = (NaimiTrehelAlgoCheckpointable) node.getProtocol(protocol_id);
				JuangVenkatesanAlgo jva = (JuangVenkatesanAlgo) node.getProtocol(checkpointer_id);
				nb_msg += jva.nb_msg_sent_during_recover;
				nb_app += nha.nb_msg_sent;
				nb_cs += nha.nb_cs;
				//System.out.format("%-12s%-12s%-12s%-12s%-12s%-12s%-12s\n", 
				//	CommonState.getTime(), node.getID(), jva.is_recovery, jva.nb_msg_sent_during_recover, 
				//	jva.getMemory(), jva.deltaT, jva.deltaA);
				ancienA += jva.deltaA;
				ancienT += jva.deltaT;
				ancienX += jva.deltaX;
			}

			//System.out.format("Nb of messages appli sent:      %d\n", nb_app);
			//System.out.format("Nb of messages appli per CS:    %d\n", nb_app/nb_cs);
			//System.out.format("Time betweencheckpointing:      %d\n", timecheckpointing);
			//System.out.format("Recovering time:                %d\n", r_time);
			//System.out.format("Nb of messages per recov:       %d\n", nb_msg);
			//System.out.format("Memory used before the crash:   %d\n", stocka);
			//System.out.format("AVG ancienneté T:               %d\n", ancienT / Network.size());
			//System.out.format("AVG ancienneté A:               %d\n", ancienA / Network.size());
			System.out.format("data: %d,%d,%d,%d,%d,%d,%d,%d\n", nb_app, nb_cs, r_time, nb_msg, stocka, ancienT / Network.size(), ancienA / Network.size(), ancienX / Network.size());
		}

		return false;
	}
}
