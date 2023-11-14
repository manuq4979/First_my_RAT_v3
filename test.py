response = "70.0%"

test = response.replace('%', '')
test = test.replace('.', '')

if(test.isdigit()):
        print("True "+response)
else:
        print(response)

