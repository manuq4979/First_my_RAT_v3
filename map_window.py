import folium

import time
import os
import json

import sys
from PyQt5.QtCore import *
from PyQt5.QtWebEngineWidgets import *
from PyQt5.QtWidgets import *

from PyQt5.QtGui import QKeySequence
from aqt import mw


# define an instance of tkinter

class MapWindow:

    def __init__(self, user_name, user):
        self.coords = [0,0,0,"no date", "no time"]
        self.user_name = user_name
        self.user = user
        self.zoom = 20

        self.YES = True
        self.NO = False
        self.PREVIOUS_UPDATE_MAP = self.YES

        self.UPDATE_MAP = self.YES
        




    def create_and_update_map(self, arr_coord):
        # Вот как выглядит arr_coord == [str_latitude, str_longting, str_letliudid, dateText, timeText]
        print([arr_coord[0], arr_coord[1]])
        dateAndTime = arr_coord[3] + " - " + arr_coord[4]
        coord = [arr_coord[0], arr_coord[1]]

        # без location zoom_start не работает
        self.map = folium.Map(location=coord, zoom_start=self.zoom)


        folium.Marker(coord, popup="user", tooltip=dateAndTime, icon=folium.Icon(color='green')).add_to(self.map)
        print("create new maps!")
        self.map.save('C:\\Users\\user\\Desktop\\Сервер\\mymap.html')



    def update_coord(self, arr_coord):
    	    # Вот как выглядит arr_coord == [str_latitude, str_longting, str_letliudid, dateText, timeText]
    		self.coords = arr_coord

    		return self.coords





    def job(self):    

        if(self.UPDATE_MAP == self.YES):
            self.create_and_update_map(self.coords)

            print('test')
            print('Reload :%s',time.time())
            self.browser.reload() 






    def create_window_map(self):
        self.url ='file:///C:/Users/user/Desktop/Сервер/mymap.html'

        self.app =  QApplication(sys.argv)

        

        self.browser = QWebEngineView()
        self.browser.setWindowTitle(self.user_name)
        self.browser.load(QUrl(self.url))

    

        self.btn_p = QPushButton("+", self.browser)
        self.btn_p.move(0, 50)
        self.btn_p.setFixedSize(45, 50)
        self.btn_p.clicked.connect(self.zoomPlus)

        self.btn_m = QPushButton("-", self.browser)
        # self.btn_m.move(20, 20)
        self.btn_m.setFixedSize(45, 50)
        self.btn_m.clicked.connect(self.zoomMinus)
        

        self.btn_update = QPushButton(self.browser)
        self.btn_update.setStyleSheet("background-color : green")
        self.btn_update.move(0, 100)
        self.btn_update.setFixedSize(45, 50)
        self.btn_update.clicked.connect(self.control_update)


        self.btn_layout = QVBoxLayout()
        # self.btn_layout.addStretch(1)
        self.btn_layout.addWidget(self.btn_m)
        self.btn_layout.addWidget(self.btn_p)
        self.btn_layout.addWidget(self.btn_update)


        self.browser.show()

        self.timer = QTimer(interval=3000)
        self.timer.timeout.connect(self.job)
        self.timer.start()

        sys.exit(self.app.exec_())

    
    def zoomMinus(self):
        if(self.zoom != -20):
            self.zoom -= 0.5
            
            self.PREVIOUS_UPDATE_MAP = self.UPDATE_MAP
            self.UPDATE_MAP = self.YES
            self.job() # сразу обнавляем экран, чтобы не раздрадало ожидание
            self.UPDATE_MAP = self.PREVIOUS_UPDATE_MAP

    def zoomPlus(self):
        if(self.zoom != 20):
            self.zoom += 0.5

            self.PREVIOUS_UPDATE_MAP = self.UPDATE_MAP
            self.UPDATE_MAP = self.YES
            self.job() # сразу обнавляем экран, чтобы не раздрадало ожидание
            self.UPDATE_MAP = self.PREVIOUS_UPDATE_MAP

    def control_update(self):
        if(self.UPDATE_MAP == self.YES):
            self.UPDATE_MAP = self.NO
            self.btn_update.setStyleSheet("background-color : red")
        else:
            self.UPDATE_MAP = self.YES
            self.btn_update.setStyleSheet("background-color : green")

