VICEREMOTE_VERSION=0.1.0
KICKASS_VERSION=4.16.0
MVN=docker run -t --rm -v "$$PWD":/workspace -v "$$PWD/.m2":/root/.m2 -w /workspace maven:3.5.0-jdk-8-alpine mvn
KICKASS=.m2/cml/kickass/KickAss/$(KICKASS_VERSION)/KickAss-$(KICKASS_VERSION).jar

snapshot: .m2/cml/kickass/KickAss/$(KICKASS_VERSION)/KickAss-$(KICKASS_VERSION).jar
	$(MVN) clean install

release:
	git tag $(VICEREMOTE_VERSION)
	$(MVN) versions:set -DnewVersion=$(VICEREMOTE_VERSION)
	$(MVN) clean install
	git add pom.xml
	git commit -m "Release $(VICEREMOTE_VERSION)"
	git push
	git push --tags
	$(MVN) versions:set -DnewVersion=$(VICEREMOTE_VERSION)-SNAPSHOT

test.prg: test.asm
	java -jar target/viceremote-$(VICEREMOTE_VERSION).jar "reset 1"
	java -cp target/viceremote-$(VICEREMOTE_VERSION).jar:$(KICKASS) cml.kickass.KickAssembler "$<"
	java -jar target/viceremote-$(VICEREMOTE_VERSION).jar "g 0810"

# Install KickAss.jar in local Maven repository
.m2/cml/kickass/KickAss/$(KICKASS_VERSION)/KickAss-$(KICKASS_VERSION).jar:
	$(MVN) org.apache.maven.plugins:maven-install-plugin:2.3.1:install-file \
		-Dfile=./lib/KickAss-$(KICKASS_VERSION).jar -DgroupId=cml.kickass \
		-DartifactId=KickAss -Dversion=$(KICKASS_VERSION) \
		-Dpackaging=jar -DlocalRepositoryPath=./.m2

