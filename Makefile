JAVAC=javac
JAVA=java
PEERSIM_BIN=bin
SRC_DIR=src
BUILD_DIR=build
CIBLE=run.sh
JAR=output.jar
CLASSPATH=${PEERSIM_BIN}/*
SOURCES=$(shell find $(SRC_DIR) -name "*.java")
CTX_FILE=${SRC_DIR}/ctx.txt

all: ${CIBLE}

${CIBLE}: ${BUILD_DIR}/${JAR}
	echo '#!/bin/bash' >> $@
	echo 'java -cp "${JAR}:${CLASSPATH}" peersim.Simulator ${CTX_FILE}' >> $@
	chmod +x ${CIBLE}

${BUILD_DIR}/${JAR}: ${BUILD_DIR} compile
	jar cvf $@ -C ${BUILD_DIR} .

compile:
	${JAVAC} -cp "${CLASSPATH}" -d ${BUILD_DIR} ${SOURCES}

${BUILD_DIR}:
	mkdir -p ${BUILD_DIR}

clean:
	rm -rf ${BUILD_DIR} ${CIBLE}
