default: clear clean build run

clear:
	clear

clean:
	rm -rf bin/*

build:
	javac -sourcepath src -d bin src/ca/ariselab/myhhw/*.java

run:
	DISPLAY=:0 java -cp bin/ ca.ariselab.myhhw.Tailer logs/output.20120413.log

#test:
#	DISPLAY=:0 java -cp bin/ ca.ariselab.myhhw.TestControl
