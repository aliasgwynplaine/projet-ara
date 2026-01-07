#!/bin/bash
rm data/*.csv
rm config/*.txt
echo "generating config files..."

betas=`python3 confgen.py`

echo "Executing case 1..."

for i in $betas; do
	./run.sh config/nt_1_$i.txt
done

cat data/nb_req_case1* > data/nb_req_case1.csv
cat data/req_time_case1* > data/req_time_case1.csv
cat data/utn_perc_case1* > data/utn_perc_case1.csv
cat data/nb_msg_apli_case1* > data/nb_msg_apli_case1.csv
cat data/nb_msg_cs_nd_case1* > data/nb_msg_cs_nd_case1.csv

echo "Executing case 2..."

for i in $betas; do
	./run.sh config/nt_2_$i.txt
done

cat data/nb_req_case2* > data/nb_req_case2.csv
cat data/req_time_case2* > data/req_time_case2.csv
cat data/utn_perc_case2* > data/utn_perc_case2.csv
cat data/nb_msg_apli_case2* > data/nb_msg_apli_case2.csv
cat data/nb_msg_cs_nd_case2* > data/nb_msg_cs_nd_case2.csv

echo "Executing case 3..."

for i in $betas; do
	./run.sh config/nt_3_$i.txt
done

cat data/nb_req_case3* > data/nb_req_case3.csv
cat data/req_time_case3* > data/req_time_case3.csv
cat data/utn_perc_case3* > data/utn_perc_case3.csv
cat data/nb_msg_apli_case3* > data/nb_msg_apli_case3.csv
cat data/nb_msg_cs_nd_case3* > data/nb_msg_cs_nd_case3.csv

python3 multi_msg_SC_plotter.py data/nb_msg_apli_case1.csv data/nb_msg_apli_case2.csv data/nb_msg_apli_case3.csv
python3 multi_msg_SC_ND_plotter.py data/nb_msg_cs_nd_case1.csv data/nb_msg_cs_nd_case2.csv data/nb_msg_cs_nd_case3.csv
python3 multi_nb_req_plotter.py data/nb_req_case1.csv data/nb_req_case2.csv data/nb_req_case3.csv 
python3 multi_req_time_plotter.py data/req_time_case1.csv data/req_time_case2.csv data/req_time_case3.csv

python3 utn_perc_plotter.py data/utn_perc_case1.csv "Perc. de Temps U, T, N - Case 1"
python3 utn_perc_plotter.py data/utn_perc_case2.csv "Perc. de Temps U, T, N - Case 2"
python3 utn_perc_plotter.py data/utn_perc_case3.csv "Perc. de Temps U, T, N - Case 3"
