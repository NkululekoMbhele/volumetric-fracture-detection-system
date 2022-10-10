
---
"CSC3003S Capstone: Volumetric Fracture Detection System"
---

## Makefile commands:

- make: Compiles source code from *src* to the *bin* directory
- make run: Runs the **Volumetric Fracture Detection System** by running ``Application.class`` in the *bin* directory 
- make clean: Clears the *bin* directory
- make cleansavedfiles: Clears the *saved_files* directory
- make cleanoutput: Clears the *output* directory

## How to run:
- Place a folder with the desired **png** data into the *data* directory. **ensure the image names are the same as the folder followed by the number of the image in the dataset** e.g. ``data>cross>cross0.png, cross1.png, ... crossn.png``
- Image data must have equal dimensions (**n x n x n**) e.g 256 x 256 x 256 
- Run **make** to compile the program
- Run **make run** to run the application

## Program output:

The **Volumetric Fracture Detection System** program will read in data from the *data* directory and store/load in previously saved fracture data from the *saved_files* directory.

The program will also write and store human readable fracture data files, upon saving, in the *output* directory.

