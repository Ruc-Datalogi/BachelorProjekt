import sys
import matplotlib.pyplot as plt
import numpy as np

arg1 = sys.argv[1]
arg2 = sys.argv[2]

def Convert(s):
    li = list(s.split(","))
    li[0] = li[0].strip("[")
    li[-1] = li[-1].strip("]")
    float_map = map(float, li)
    float_list = list(float_map)
    return float_list


def input(x , y):
    # plot
    fig, ax = plt.subplots()
    ax.scatter(x, y)
    plt.xlabel("Iterations")
    plt.ylabel("Area")
    plt.show()

input(Convert(arg1), Convert(arg2))