resources := classes/com/gigamonkeys/boggle/word-list.txt


all: build run

build: compile resources

compile:
	javac -Xlint:deprecation -cp src:classes -d classes/ src/com/gigamonkeys/boggle/Main.java


resources: $(resources)


run:
	java -cp classes com.gigamonkeys.boggle.Main


clean:
	rm -rf classes


classes/com/gigamonkeys/boggle/%.txt: %.txt
	mkdir -p $(dir $@)
	cp $< $@
