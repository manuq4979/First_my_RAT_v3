# 1 буква - это 1 байт

# Сервер не может работать с несколькими клиентами
# Он отключается если единственный пользователь отключился

from client import *
from client_window import *

import socket
from threading import *

import time
import sys
import os




# если будет совпадения устройства клиента с уже существующем в списке, то вернет false
# также будет обнавлять сокет user если клиент снова запросил соединение, хотя ранее соеденялся
def client_list_check(client_info, user):
	global arr_client_info
	global dict_client_index_by_client_info

	if(client_info == ""):
		return False
	if(client_info == "[][][]"):
		return False

	if(len(arr_client_info) != 0):
		for ci in arr_client_info:
			if(ci == client_info):
				print("\n"+str(dict_client_index_by_client_info)+"\n")

				clientCode_index = dict_client_index_by_client_info[client_info]
				clientCode = arr_clientCode[clientCode_index]
				clientCode.setUserSocket(user)
				print("connect update!")
				return False
	
	return True


def getSystemProperty(user):
	bufferSize = 1024*200
	msg = json.dumps({'header' : 'SystemProperty'})
	user.send(bytes(msg,'UTF-8'))
	client_info = user.recv(bufferSize)
	print(client_info)
	return client_info.decode('utf-8')








# main():
server = 0

def create_server_socket(client_info="server"):
	global server

	
	try:
		server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

		server.bind(("127.0.0.1", 123)) # тут возникает исключение если клиент отключился, а сокет клиента не был отвязан ОС от хоста и порта

		server.listen(10) # тут можно указать размер максимальный для очереди из клиентов

		print("socket server created complite!")

	except Exception as e:
		print(str(e))
		# удалить клиента из списков и сообщить в клиентском окне о том, что клиент отключился
		global connect_number
		global arr_client_window
		global arr_clientCode
		global arr_client_info
		global dict_client_index_by_client_info
		global arr_display_client_text

		global global_add_text_to_display

		connect_number -= 1
		client_index = dict_client_index_by_client_info[client_info]

		arr_client_window.remove(arr_client_window[client_index])
		arr_clientCode.remove(arr_clientCode[client_index])
		arr_client_info.remove(arr_client_info[client_index])
		arr_display_client_text.remove(arr_display_client_text[client_index])

		del dict_client_index_by_client_info[client_info]
	
		
		client_list = ""
		for clt in arr_display_client_text:
			client_list += clt
		global_add_text_to_display_server(client_list, True)
		error_win.create_error("WARNING", "Client disconnect! The client window is no longer valid!")



connect_number = 0
arr_client_window = []
arr_clientCode = []
arr_client_info = []
dict_client_index_by_client_info = dict() # пока что нужно лишь для того, чтобы обновить сокет user у конретного экземпляра кода клиента
arr_display_client_text = [] # так легче удалять клиента текст с экрана, просто по индексу

global_add_text_to_display_server = 0

def while_true(add_text_to_display):

	add_text_to_display('Working...\n')
	while (True):
		
		global global_add_text_to_display_server

		global_add_text_to_display_server = add_text_to_display

		global arr_client_window
		global arr_clientCode
		global arr_client_info
		global connect_number
		global server

		try:
			user, adres = server.accept()
		except Exception as ex:
			print(str(ex))

		client_info = getSystemProperty(user)
		print("Hekkp kek")
		print(client_info+"\n")

		# если клиент новый, вернет True
		if(client_list_check(client_info, user)):

			print(client_info+"\n")
			arr_client_info.append(client_info)

			# client_info = " System property: " + client_info + "\n"

			# связывание экземпляра клиента с экземпляром окна:
			clientCode = ClientCode(user, client_info)
			# thread = Thread(target=ClientWindow, args=(clientCode, ))
			clientWindow = ClientWindow(clientCode)

			arr_client_window.append(clientWindow)
			arr_clientCode.append(clientCode)

			connect_number += 1
			client_number = "client number: " + str(connect_number)
			dict_client_index_by_client_info[client_info] = connect_number-1


			# add_text_to_display(client_info)
			
			print(str(arr_client_window)+"\n")
			arr_display_client_text.append("New connect: " + client_number + ".	" + client_info + '\n')

			# Код ниже просто обьеденяет весь код из служебного массива клиентов и делает одно строку, для того чтобы передать в метод для добавления текста на экран, но с особенным флагом, который означает стереть все, нужно, чтобы клиенты не дублировались
			client_list = ""
			for clt in arr_display_client_text:
				client_list += clt
			add_text_to_display(client_list, True)
			



def client_connect(client_number):
	global arr_clientCode
	global arr_client_window
	# global global_add_text_to_display_server

	# global_add_text_to_display_server("connect: "+client_info)

	client_number = int(client_number)-1
	print(client_number)

	# проверяю, не указан ли номер не существующего клиента
	size_arr_client = len(arr_clientCode)
	if(client_number < size_arr_client):
		clientWindow = arr_client_window[client_number]
		clientCode = arr_clientCode[client_number]
		clientCode.setWindow(clientWindow)
		clientWindow.start()
	else:
		error_win.create_error("ERROR", "Invalid number client entered!")


def server_disconnect():
	global server

	server.close()

def start(add_text_to_display):
	thread = Thread(target=while_true, args=(add_text_to_display, ))
	thread.start()