#!/bin/bash

function thread_ctrl() {
  while [ $(jobs | wc -l ) -gt 5 ]; do
    sleep 1
  done
}

rm data/*.csv
rm config/*.txt
echo "generating config files..."

python3 jv_confgen.py

for confile in $(ls config/); do
  for i in $(seq 1 200); do
      (./run.sh config/$confile 2>/dev/null | grep data | cut -d " " -f 2 >> data/$confile-dat.csv) &
      thread_ctrl
      echo $confile-$i done!
  done
done
