JAVAC=javac
JAVA=java
PEERSIM_BIN=bin
SRC_DIR=src
DATA_DIR=data
BUILD_DIR=build
CONFIG_DIR=config
CIBLE=run.sh
JAR=output.jar
CLASSPATH=${PEERSIM_BIN}/*
SOURCES=$(shell find $(SRC_DIR) -name "*.java")
CTX_FILE=${CONFIG_DIR}/ctx.txt

all: ${CIBLE}

${CIBLE}: ${JAR}
	echo '#!/bin/bash' > $@
	echo 'if [ $$# -lt 1 ]; then' >> $@
	echo '  echo "Usage: $$0 <config file>"' >> $@
	echo '  exit 2' >> $@
	echo 'fi' >> $@
	echo 'java -cp "${JAR}:${CLASSPATH}" peersim.Simulator $$1' >> $@
	chmod +x ${CIBLE}

${JAR}: ${BUILD_DIR} compile
	jar cvf $@ -C ${BUILD_DIR} .

compile:
	${JAVAC} -cp "${CLASSPATH}" -d ${BUILD_DIR} ${SOURCES}

${BUILD_DIR}:
	mkdir -p ${BUILD_DIR}

clean:
	rm -rf ${BUILD_DIR} ${CIBLE} ${JAR} ${DATA_DIR}/*.csv ${CONFIG}/*.txt
