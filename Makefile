SHELL=/bin/bash


all: compile 

compile:
	mkdir -p bin; javac -classpath bin -sourcepath src src/GUI.java -d bin ;

run:
	java -classpath bin GUI


.PHONY: clean

clean:
	rm -rf bin
