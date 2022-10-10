# Simple Makefile to compile A.java, B.java, C.java, with main()
# in Application.java
JAVAC = /usr/bin/javac
JFLAGS = -g -d $(BINDIR) -cp $(BINDIR)
.SUFFIXES: .class .java
SRCDIR = src
BINDIR = bin
SAVEFILESDIR = saved_files
OUTPUTDIR = output

JAVAFILES = src/MatrixMath.class src/Point.class src/PVector.class src/Threshold.class src/DenoiseFilterType.class src/Denoise.class src/ColourName.class src/ColourAllocater.class src/EdgeDetector.class src/FractureObject.class src/Group.class src/ConnectComponents.class src/FractureDetector.class src/Slice.class src/DataReader.class src/Rotation.class src/MorphologicalProcessor.class src/FractureSaverLoader src/VFDSModel.class src/GUI.class src/Application.class 

vpath %.java $(SRCDIR)
vpath %.class $(BINDIR)

.java.class : *.java
	$(JAVAC) $(JFLAGS) $<

CLASSES = \
	  MatrixMath.class \
	  Point.class \
	  PVector.class \
	  Threshold.class \
	  DenoiseFilterType.class \
	  Denoise.class \
	  ColourName.class \
	  ColourAllocater.class \
	  EdgeDetector.class \
	  FractureObject.class \
	  Group.class \
	  ConnectComponents.class \
	  FractureDetector.class \
	  Slice.class \
	  DataReader.class \
	  Rotation.class \
	  MorphologicalProcessor.class \
	  FractureSaverLoader.class \
	  VFDSModel.class \
	  GUI.class \
	  Application.class \


default: $(CLASSES)

# run the project
run:
	java -cp ./bin Application

# clear the class files from bin directory
clean:
	rm -f $(BINDIR)/*.class

# clear the save files from saved_files directory
cleansavedfiles:
	rm -f $(SAVEFILESDIR)/*

# clear the output files from output directory
cleanoutput:
	rm -f $(OUTPUTDIR)/*
                                                                