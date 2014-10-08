import json,httplib

who_said_app_id  = "8Oj7j7pYRXClaPfwr93VEcx4izkSDTk5dSgLzXBA"
who_said_api_key = "qUMfW8e1A0idm5zVGwpplNAD1ML9jWhr9lZDVMlW"
user_map_obj_id  = "DGBUeNQwMD" #TODO(rob) read last one in user_map.txt

# expects picture.jpg and sound.mp3 in folder
def send_pair(local_path_to_folder):
  res1 = send_picture(local_path_to_folder + "/picture.jpg")
  print res1
  picture_name = res1["name"]
  res2 = send_sound_byte(local_path_to_folder + "/sound.mp3")
  voice_name = res2["name"]
  print res2

  #get object name from end of path
  user_name = local_path_to_folder.split("/")[-1]
  print user_name
  res3 = associate_with_user_info(picture_file_name = picture_name, voice_file_name = voice_name)
  print res3
  res4 = associate_with_user_map(user_info_obj_id = res3["objectId"], user_name = user_name)

def send_picture(local_path):
  connection = httplib.HTTPSConnection('api.parse.com', 443)
  connection.connect()
  connection.request('POST', '/1/files/picture.jpg', open(local_path, 'rb').read(), {
         "X-Parse-Application-Id": who_said_app_id,
         "X-Parse-REST-API-Key": who_said_api_key,
         "Content-Type": "image/jpeg"
       })
  result = json.loads(connection.getresponse().read())
  return result

# sends voice as mp3
def send_sound_byte(local_path):
  connection = httplib.HTTPSConnection('api.parse.com', 443)
  connection.connect()
  connection.request('POST', '/1/files/sound.jpg', open(local_path, 'rb').read(), {
         "X-Parse-Application-Id": who_said_app_id,
         "X-Parse-REST-API-Key": who_said_api_key,
         "Content-Type": "audio/mpeg"
       })
  result = json.loads(connection.getresponse().read())
  return result

def associate_with_user_info(picture_file_name, voice_file_name):
  connection = httplib.HTTPSConnection('api.parse.com', 443)
  connection.connect()
  connection.request('POST', '/1/classes/UserInfo', json.dumps({
         "voiceFile": {
           "name": voice_file_name,
           "__type": "File"
         },
         "pictureFile": {
           "name": picture_file_name,
           "__type": "File"
         }
       }), {
         "X-Parse-Application-Id": who_said_app_id,
         "X-Parse-REST-API-Key": who_said_api_key,
         "Content-Type": "application/json"
       })
  result = json.loads(connection.getresponse().read())
  return result

def associate_with_user_map(user_info_obj_id, user_name):
  # read usermap from file
  json_data = open("user_map.txt").read()
  data = json.loads(json_data)
  user_map_obj_id = data["objectId"]

  connection = httplib.HTTPSConnection('api.parse.com', 443)
  connection.connect()
  connection.request('PUT', '/1/classes/UserMapObject/' + user_map_obj_id, json.dumps({
         user_name: user_info_obj_id
       }), {
         "X-Parse-Application-Id": who_said_app_id,
         "X-Parse-REST-API-Key": who_said_api_key,
         "Content-Type": "application/json"
       })
  result = json.loads(connection.getresponse().read())
  print result

# DONT CALL THIS SHIT OK?
def create_user_map():
  connection = httplib.HTTPSConnection('api.parse.com', 443)
  connection.connect()
  connection.request('POST', '/1/classes/UserMapObject', json.dumps({
       }), {
         "X-Parse-Application-Id": who_said_app_id,
         "X-Parse-REST-API-Key": who_said_api_key,
         "Content-Type": "application/json"
       })
  result = json.loads(connection.getresponse().read())
  # TODO(rob): make this append to user_map.txt
  print result

# send_pair("/Users/robertsami/Documents/dev/whosaid/server/res/troll")
# send_pair("/Users/robertsami/Documents/dev/whosaid/server/res/gun")
# send_pair("/Users/robertsami/Documents/dev/whosaid/server/res/wolf")
# create_user_map()