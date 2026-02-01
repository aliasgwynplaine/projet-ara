package ara.projet.mutex;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import peersim.core.Control;
import peersim.core.CommonState;
import peersim.config.Configuration;
import peersim.core.Node;
import peersim.core.Network;

import ara.projet.mutex.NaimiTrehelAlgo;
import ara.projet.mutex.NaimiTrehelAlgo.State;

import static ara.util.Constantes.log;

public class NaimiTrehelObserver implements Control {
	private static final String PAR_ALG = "protocol";
	private static final String PAR_EXP = "experience";
	private static final String PAR_ALPHA = "alpha";
	private static final String PAR_GAMMA = "gamma";
	private static final String PAR_BETA  = "beta";

	// identifiant du protocole Naimi-Trehel
	private final int protocol_id;
	private final String experience;
	private final int alpha;
	private final int gamma;
	private final int beta;

	// vars for the statistics
	protected long r_time[];        // time in requesting state
	protected long u_time;          // Utilisé
	protected long t_time;          // en Transit
	protected long n_time;          // Non utilisé

	public NaimiTrehelObserver(String prefix) {
		String tmp[] = prefix.split("\\.");
		protocol_id = Configuration.getPid(prefix + "." + PAR_ALG);
		experience = Configuration.getString(prefix + "." + PAR_EXP);
		alpha = Configuration.getInt(PAR_ALPHA);
		gamma = Configuration.getInt(PAR_GAMMA);
		beta = Configuration.getInt(PAR_BETA);
		int sz = Network.size();
		r_time = new long[sz];
		u_time = 0;
		t_time = 0;
		n_time = 0;
	}

	@Override
	public boolean execute() {
		boolean token_in_transit = true;
		int verifier = 0;

		for (int i = 0; i < Network.size(); i++) {
			Node node = Network.get(i);
			NaimiTrehelAlgo nh = (NaimiTrehelAlgo) node.getProtocol(protocol_id);
			
			if (nh.has_token) {
				verifier++;
				token_in_transit = false;

				if (verifier > 1) {
					log.severe("More than one token detected in the system!");
				}

				if (nh.state == State.tranquil) {
					n_time++;
				}
			}

			if (nh.state == State.requesting) {
				r_time[i]++;
			}

			if (nh.state == State.inCS) {
				u_time++;
				token_in_transit = false;

				if (!nh.has_token) {
					log.severe("Node "+ node.getID() +" is in CS but does not have the token!");
				}
			}

			//System.out.format("%-12s%-12s%-12s%-12s%-12s%-12s%-12s\n", CommonState.getTime(), node.getID(), nh.state, nh.nb_cs, nh.global_counter, r_time[i], nh.has_token);
		}
		//System.out.println("\n");

		if (token_in_transit) {
			t_time++;
		}

		// compute statistics at the last cycle of the simulation
		if (CommonState.getTime() == CommonState.getEndTime() - 1) {
			long total_r_time = 0;
			long total_r_time_2 = 0;
			long total_nb_requests = 0;
			long total_nb_requests_2 = 0;
			long total_nb_msg = 0;
			long total_nb_msg_2 = 0;
			long total_nb_msg_cs_nd = 0;
			long total_nb_msg_cs_nd_2 = 0;
			long nb_cs = 0;
			int max_global_counter = -1;
			int nb_req[] = new int[Network.size()];
			int nb_msg[] = new int[Network.size()];
			int nb_msg_cs_nd[] = new int[Network.size()];

			if (u_time + t_time + n_time != CommonState.getEndTime()) {
				log.severe("Inconsistent time accounting!");
			}

			//System.out.format("%-12s%-12s%-12s%-12s%-12s%-12s%-12s%-12s%-12s\n", "Time", "NodeID", "state", "nb_cs", "global_c", "nb_req","r_time", "nb_msg_sent", "has_token");

			for (int i = 0; i < Network.size(); i++) {
				Node node = Network.get(i);
				NaimiTrehelAlgo nh = (NaimiTrehelAlgo) node.getProtocol(protocol_id);
				
				//System.out.format("%-12s%-12s%-12s%-12s%-12s%-12s%-12s%-12s%-12s\n",CommonState.getTime(), node.getID(), nh.state, nh.nb_cs, nh.global_counter, nh.nb_requests, r_time[i], nh.nb_msg_sent, nh.has_token);
				
				total_r_time  += r_time[i];
				total_r_time_2 += r_time[i] * r_time[i];
				
				nb_cs += nh.nb_cs;
				nb_req[i] = nh.nb_requests;
				nb_msg[i] = nh.nb_msg_sent;
				nb_msg_cs_nd[i] = nh.nb_msg_sent / nh.nb_cs;

				total_nb_requests   += nh.nb_requests;
				total_nb_requests_2 += nh.nb_requests * nh.nb_requests;

				total_nb_msg   += nh.nb_msg_sent;
				total_nb_msg_2 += nh.nb_msg_sent * nh.nb_msg_sent;

				total_nb_msg_cs_nd += nb_msg_cs_nd[i];
				total_nb_msg_cs_nd_2 += nb_msg_cs_nd[i] * nb_msg_cs_nd[i];

				if (nh.global_counter > max_global_counter) {
					max_global_counter = nh.global_counter;
				}
			}

			for (int i = 0; i < Network.size(); i++) {
				Node node = Network.get(i);
				NaimiTrehelAlgo nh = (NaimiTrehelAlgo) node.getProtocol(protocol_id);
				
				r_time[i] = r_time[i] / nh.nb_cs;
			}

			double avg_r_time = (double) total_r_time / Network.size();
			double std_r_time = Math.sqrt((double) total_r_time_2 / Network.size() - avg_r_time * avg_r_time); 
			
			double avg_nb_requests = (double) total_nb_requests / Network.size();
			double std_nb_requests = Math.sqrt((double) total_nb_requests_2 / Network.size() - avg_nb_requests * avg_nb_requests);
			
			double nb_msg_cs  = (double) total_nb_msg / nb_cs;

			double avg_nb_msg_cs_nd = (double) total_nb_msg_cs_nd / Network.size();
			double std_nb_msg_cs_nd = Math.sqrt((double) total_nb_msg_cs_nd_2 / Network.size() - avg_nb_msg_cs_nd * avg_nb_msg_cs_nd);

			double avg_nb_msg = (double) total_nb_msg / Network.size();
			double std_nb_msg = Math.sqrt((double) total_nb_msg_2 / Network.size() - avg_nb_msg * avg_nb_msg);

			Double rho = (double)(alpha + gamma) / beta;
			//System.out.println("alpha:"+ alpha);
			//System.out.println("gamma:"+ gamma);
			//System.out.println("beta:"+ beta);
			//System.out.println("rho: "+ rho);
			//System.out.println("");
			//System.out.format("Max global counter:       %10s\n", max_global_counter);
			//System.out.format("Nb messages per CS:       %10s\n", nb_msg_cs);
			//System.out.format("AVG Nb msg per CS per N:  %10s\n", avg_nb_msg_cs_nd);
			//System.out.format("STD Nb msg per CS per N:  %10s\n", std_nb_msg_cs_nd);
			//System.out.format("AVG number of messages:   %10s\n", avg_nb_msg);
			//System.out.format("STD number of messages:   %10s\n", std_nb_msg);
			//System.out.format("AVG requesting time:      %10.3f\n", avg_r_time);
			//System.out.format("STD requesting time:      %10.3f\n", std_r_time);
			//System.out.format("AVG number of requests:   %10.3f\n", avg_nb_requests);
			//System.out.format("STD number of requests:   %10.3f\n", std_nb_requests);
			//System.out.format("Total number of requests: %10s\n", total_nb_requests);
			//System.out.format("Token utilized time:      %10s -> %-5s%% \n", u_time, u_time / (double)CommonState.getTime() * 100);
			//System.out.format("Token in transit time:    %10s -> %-5s%% \n", t_time, t_time / (double)CommonState.getTime() * 100);
			//System.out.format("Token non utilized time:  %10s -> %-5s%% \n", n_time, n_time / (double)CommonState.getTime() * 100);
			
			String nb_req_out_name   = "data/nb_req_"+ experience +".csv";
			String req_time_out_name = "data/req_time_"+ experience +".csv";
			String utn_perc_out_name = "data/utn_perc_"+ experience +".csv";
			String nb_msg_apli_name  = "data/nb_msg_apli_"+ experience +".csv";

			String nb_msg_cs_nd_name = "data/nb_msg_cs_nd_"+ experience +".csv";

			System.out.println("Writing csv files...");

			Path nb_req_path = Path.of(nb_req_out_name);
			Path req_time_path = Path.of(req_time_out_name);
			Path utn_perc_out_path = Path.of(utn_perc_out_name);
			Path nb_msg_apli_path = Path.of(nb_msg_apli_name);
			
			Path nb_msg_cs_nd_path = Path.of(nb_msg_cs_nd_name);

			String nb_req_line = Arrays.stream(nb_req)
				.mapToObj(String::valueOf).collect(Collectors.joining(","));

			String req_time_line = Arrays.stream(r_time)
				.mapToObj(String::valueOf).collect(Collectors.joining(","));

			String utn_perc_line = u_time / (double)CommonState.getTime() * 100 
				+","+ t_time / (double)CommonState.getTime() * 100 
				+","+ n_time / (double)CommonState.getTime() * 100;

			String nb_msg_line = Arrays.stream(nb_msg)
				.mapToObj(String::valueOf).collect(Collectors.joining(","));
			
			String nb_msg_nd_cs_line = Arrays.stream(nb_msg_cs_nd)
				.mapToObj(String::valueOf).collect(Collectors.joining(","));

			try {
				Files.writeString(nb_req_path, rho + ","+ Network.size() +"\n" +nb_req_line+"\n");
				Files.writeString(req_time_path, rho + "\n" +req_time_line+"\n");
				Files.writeString(utn_perc_out_path, rho + "\n" +utn_perc_line+"\n");
				Files.writeString(nb_msg_apli_path, rho + ","+nb_cs+"\n"+ nb_msg_line+"\n");

				Files.writeString(nb_msg_cs_nd_path, rho +"\n"+ nb_msg_nd_cs_line+"\n");
			} catch (IOException e) {
				System.err.println("fuck you");
			}
		}

		return false;
	}
}