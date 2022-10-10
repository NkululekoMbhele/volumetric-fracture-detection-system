import os
from PIL import Image
import sys

def convertToPNG():   
    for file in os.listdir():
        filename, extension  = os.path.splitext(file)
        if extension == ".pgm":
            new_file = "{}.png".format(filename)
            with Image.open(file) as im:
                im.save(new_file)

def movePGMS():
    for file in os.listdir():
        filename, extension  = os.path.splitext(file)
    if extension == ".pgm":
        os.remove(filename + extension)
        
movePGMS()
    