TARGET_JAR = TemplateApp

TARGET_CLASS = $(TARGET_JAR)

INPUT= input.txt

OUTPUT = output
  
all:$(TARGET_JAR)
  
$(TARGET_JAR):
	javac -cp "`hadoop classpath`" *.java -d classes
	jar -cvf $(TARGET_JAR).jar -C classes ./

init:
	mkdir classes

run:
	rm -rf $(OUTPUT)
	hadoop jar $(TARGET_JAR).jar $(TARGET_CLASS) $(INPUT) $(OUTPUT)
	cat $(OUTPUT)/part-r-00000


res:
	cat $(OUTPUT)/part-r-00000

clean:   
	rm -rf *.jar
	rm -rf classes
	rm -rf $(OUTPUT)
	rm -rf *~ *class
