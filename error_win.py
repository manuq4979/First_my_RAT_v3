from tkinter import *
import tkinter as tk

def create_error(title, txt):
	error_win = Tk()
	error_win.title(title)
	error_win.geometry('360x120')
	display_err = tk.Label(error_win, text=txt, anchor=NW)
	display_err.pack()
	error_win.mainloop()