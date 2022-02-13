JC = javac
CP = sudoku
OUT = out
CFLAGS = -d out

all:
	$(JC) $(CFLAGS) $(CP)/*.java

run:
	java -cp $(OUT) sudokusolver.sudoku.App

clean:
	rm -rf $(OUT)/sudokusolver