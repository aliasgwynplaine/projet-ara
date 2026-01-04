package ara.projet.mutex;

import java.util.ArrayList;
import java.util.List;

import peersim.core.Control;
import peersim.core.CommonState;
import peersim.config.Configuration;
import peersim.core.Node;
import peersim.core.Network;

import ara.projet.mutex.NaimiTrehelAlgo;
import ara.projet.mutex.NaimiTrehelAlgo.State;

import static ara.util.Constantes.log;

public class NaimiTrehelObserver implements Control {
        private static final String PAR_ALGO = "protocol";

        // identifiant du protocole Naimi-Trehel
        private final int protocol_id;
        protected long r_time[];        // time in requesting state
        protected long u_time;          // Utilisé
        protected long t_time;          // en Transit
        protected long n_time;          // Non utilisé

        public NaimiTrehelObserver(String prefix) {
                String tmp[] = prefix.split("\\.");
                protocol_id = Configuration.getPid(prefix + "." + PAR_ALGO);
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

                if (CommonState.getTime() == CommonState.getEndTime() - 1) {
                        long total_r_time = 0;

                        for (int i = 0; i < Network.size(); i++) {
                                Node node = Network.get(i);
                                NaimiTrehelAlgo nh = (NaimiTrehelAlgo) node.getProtocol(protocol_id);
                                System.out.format("%-12s%-12s%-12s%-12s%-12s%-12s%-12s\n",CommonState.getTime(), node.getID(), nh.state, nh.nb_cs, nh.global_counter, r_time[i], nh.has_token);
                                total_r_time += r_time[i];
                        }

                        double avg_r_time = (double) total_r_time / Network.size();
                        System.out.format("Average requesting time: %10s\n", avg_r_time);
                        System.out.format("Token utilized time:     %10s\n", u_time);
                        System.out.format("Token in transit time:   %10s\n", t_time);
                        System.out.format("Token non utilized time: %10s\n", n_time);
                }

                return false;
        }
}