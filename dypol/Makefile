ifeq ($(origin JAVA_HOME), undefined)
  JAVA_HOME=/usr
endif

ifeq ($(origin NETLOGO), undefined)
  NETLOGO=../..
endif

JAVAC=$(JAVA_HOME)/bin/javac
SRCS=$(wildcard src/*.java)

dypol.jar: $(SRCS) manifest.txt Makefile $(JARS)
	mkdir -p classes
	$(JAVAC) -g -deprecation -Xlint:all -Xlint:-serial -Xlint:-path -encoding us-ascii -source 1.5 -target 1.5 -classpath $(NETLOGO)/NetLogoLite.jar:$(JARSPATH) -d classes $(SRCS)
	jar cmf manifest.txt dypol.jar -C classes .

dypol.zip: dypol.jar
	rm -rf dypol
	mkdir dypol
	cp -rp dypol.jar README.md Makefile src manifest.txt dypol
	zip -rv dypol.zip dypol
	rm -rf dypol
