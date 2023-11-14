import server
import client_window
import error_win

from tkinter import *
import tkinter as tk

import threading

server.create_server_socket()


def new_thread_loop():
    server.start(add_text_to_display)

def clicked(add_text_to_display):
    client_number = entry.get()

    if( client_number.isdigit() == False):
            error_win.create_error("ERROR", "Is not a digit, a number is required!")
    
    client_number = int(client_number)
    if(client_number >= 1):
        
            server.client_connect(client_number)
    

def on_close_main_window():
    server.server_disconnect()
    window.destroy()


def add_text_to_display(new_line, clear_all=False):
    if(clear_all == False):
        line = display_text.get()
        line += new_line
        display_text.set(line)
    else:
        display_text.set(new_line)


# main UI code:
window = Tk()  
window.title("Выбор клиента")  
window.geometry('960x480') # 12 колонок в длину и 33 в ширь

common_frame = Frame(window, width=300)
common_frame.pack(anchor=SE, side=BOTTOM, padx=10, pady=10)


btn = tk.Button(common_frame, text="send", command=lambda: clicked(add_text_to_display), width=10, height=1)
btn.pack(side=RIGHT, padx=10)

entry = tk.Entry(common_frame, width=300, font="Helvetica 12")
entry.pack(side=LEFT)


display_text = tk.StringVar()
display = tk.Label(window, textvariable=display_text, borderwidth=2, relief="groove", width=300, height=100, anchor=NW)
display.pack()

# server.start(window, add_text_to_display)
new_thread_loop()
window.protocol('WM_DELETE_WINDOW', on_close_main_window)
window.mainloop()