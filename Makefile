sources := $(wildcard src/com/gigamonkeys/boggle/*.java)

resources := classes/com/gigamonkeys/boggle/word-list.txt

all: build run

build: compile resources

compile: $(sources)
	javac -Xdiags:verbose -Xlint:deprecation -cp src:classes -d classes/ $(sources)

pretty:
	prettier --plugin ~/node_modules/prettier-plugin-java/ --write **/*.java

resources: $(resources)

run:
	java -cp classes com.gigamonkeys.boggle.Boggle

clean:
	rm -rf classes

classes/com/gigamonkeys/boggle/%.txt: %.txt
	mkdir -p $(dir $@)
	cp $< $@
