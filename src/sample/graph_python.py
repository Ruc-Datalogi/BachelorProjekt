import sys
import matplotlib.pyplot as plt
import numpy as np
import csv
import os.path



def main():
    my_path = os.path.abspath(os.path.dirname(__file__))
    path = os.path.join(my_path, "../Data/CurrentCSV.csv")

    try:
        x = []
        y = []
        path
        with open(path) as csv_file:
            csv_reader = csv.reader(csv_file, delimiter=",")
            for row in csv_reader:
                x.append(int(float(row[0])))
                y.append(int(float(row[1])))
            csv_file.close()

        print(type(x))
        np_x = np.asarray(x)
        np_y = np.asarray(y)

        fig, ax = plt.subplots()
        ax.plot(np_x, np_y)
        plt.xlabel("Iterations")
        plt.ylabel("Energy")
        plt.show()

    except Exception as e:
        with open ("pythonLog.txt", "w") as f:
            print(str(e))
            print("uwu fucky wucky")

main()