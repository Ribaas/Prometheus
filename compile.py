import os
import subprocess
from shutil import copyfile
import traceback
import sys


PATH = "/home/prometheus/Documents/Dev/newws/{}".format(sys.argv[1])
LIB_PATH = "/home/prometheus/Documents/Dev/JavaLibs/javafx-sdk-11.0.2/lib/"

p = subprocess.Popen(["rm", "-rf", "{}/bin".format(PATH)])
out, err = p.communicate()

try:
	command = 'javac --module-path "{}" -Xlint:deprecation -d "{}/bin" $(find "{}/src/" -name "*.java")'.format(LIB_PATH, PATH, PATH)
	os.system(command)
except Exception as e:
	print(traceback.format_exc())

for root, dirs, files in os.walk("{}/src/".format(PATH)):
	for file in files:
		if ".fxml" in file:
			absolutePath = "{}/{}".format(root, file)
			copyfile(absolutePath, absolutePath.replace('src', 'bin'))
