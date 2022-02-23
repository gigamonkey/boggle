all: compile run

compile:
	javac -Xlint:deprecation -cp src:classes -d classes/ src/com/gigamonkeys/boggle/Main.java

run:
	java -cp classes com.gigamonkeys.boggle.Main
