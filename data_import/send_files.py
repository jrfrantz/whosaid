import json,httplib

who_said_app_id  = "8Oj7j7pYRXClaPfwr93VEcx4izkSDTk5dSgLzXBA"
who_said_api_key = "qUMfW8e1A0idm5zVGwpplNAD1ML9jWhr9lZDVMlW"

# expects picture.jpg and sound.mp3 in folder
def send_pair(local_path_to_folder):
  res1 = send_picture(local_path_to_folder + "/picture.jpg")
  print res1
  picture_name = res1["name"]
  res2 = send_sound_byte(local_path_to_folder + "/sound.mp3")
  picture_name = res2["name"]
  print res2

  #get object name from end of path
  obj_name = local_path_to_folder.split("/")[-1]
  print obj_name

def send_picture(local_path):
  connection = httplib.HTTPSConnection('api.parse.com', 443)
  connection.connect()
  connection.request('POST', '/1/files/pic.jpg', open(local_path, 'rb').read(), {
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
  connection.request('POST', '/1/files/pic.jpg', open(local_path, 'rb').read(), {
         "X-Parse-Application-Id": who_said_app_id,
         "X-Parse-REST-API-Key": who_said_api_key,
         "Content-Type": "audio/mpeg"
       })
  result = json.loads(connection.getresponse().read())
  return result

def associate_with_parse_obj():
  connection = httplib.HTTPSConnection('api.parse.com', 443)
  connection.connect()
  connection.request('POST', '/1/classes/PlayerProfile', json.dumps({
         "name": "Andrew",
         "picture": {
           "name": "...profile.png",
           "__type": "File"
         }
       }), {
         "X-Parse-Application-Id": who_said_app_id,
         "X-Parse-REST-API-Key": who_said_api_key,
         "Content-Type": "application/json"
       })
  result = json.loads(connection.getresponse().read())
  return result

send_pair("/Users/robertsami/Documents/dev/whosaid/server/res/troll")