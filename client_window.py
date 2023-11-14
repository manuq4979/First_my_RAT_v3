from tkinter import *
import tkinter as tk

import client
import json



        


class ClientWindow:
    
    def __init__(self, clientCode):

        self.clientCode = clientCode
        self.CLIENT_STARTED = False


    def start(self):
        self.window_client = Tk()

        self.window_client.title("Клиент "+self.clientCode.client_info)  
        self.window_client.geometry('960x480') # 12 колонок в длину и 33 в ширь

        self.common_frame_client = Frame(self.window_client, width=300)
        self.common_frame_client.pack(anchor=SE, side=BOTTOM, padx=10, pady=10)


        self.btn_client = tk.Button(self.common_frame_client, text="send", command=self.client_clicked, width=10, height=1)
        self.btn_client.pack(side=RIGHT, padx=10)

        self.entry_to_client = tk.Entry(self.common_frame_client, width=300, font="Helvetica 12")
        self.entry_to_client.pack(side=LEFT)

        
        # self.display_text = tk.StringVar() xscrollcommand=self.scroll_x.set, yscrollcommand=self.scroll_y.set
        
       
        self.common_frame_display = Frame(self.window_client, width=300)
        self.common_frame_display.pack()


        self.display_client = tk.Text(self.common_frame_display, borderwidth=2, relief="groove", width=100, height=100)
        self.display_client.pack(side=RIGHT)

        self.display_client_battery_lvl = tk.Text(self.common_frame_display, borderwidth=2, relief="groove", height=100)
        self.display_client_battery_lvl.pack(side=LEFT)
    

        # self.display_client.pack()
       
        

        self.window_client.protocol('WM_DELETE_WINDOW', lambda: self.on_close_window_client())
        self.window_client.mainloop()
       


    def on_close_window_client(self):
        self.window_client.destroy()

    def client_clicked(self):
        command = self.entry_to_client.get()

        if(self.CLIENT_STARTED == True):
            self.clientCode.setCommand(command)

        if(self.CLIENT_STARTED == False):
            self.clientCode.start_client(self.add_text_to_display, self.add_text_to_display_battery_lvl)
            self.CLIENT_STARTED = True
            
            self.clientCode.setCommand(command)
            

    def add_text_to_display(self, new_line):

        self.display_client.insert(END, "\n"+new_line+"\n")

        # line = self.display_client["text"]
        # line += new_line
        # self.display_client["text"] = line

    def add_text_to_display_battery_lvl(self, new_line):
        self.display_client_battery_lvl.delete("1.0", END)
        self.display_client_battery_lvl.insert(END, "\n"+new_line+"\n")



