var admin= require('firebase-admin');
const { pool } = require('../secret_info/db_connect');
var serviceAccount = require('../fcm_key/my-application-eb059-firebase-adminsdk-2857l-84af43d294.json');
const format = require('../format_check');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
    });
//낙상 사고 발생 으로 인한 fcm 전송 

function send_fcm(phone_number, latitude,longitude,board_nickname,data_array,fcm_target_token,alarm_time)
{
  let title1 = board_nickname+'에서 낙상사고발생'
  let body1 = board_nickname+'에서 '+alarm_time+'에 낙상사고발생'
  //console.log(latitude)
  //console.log(longitude)
  //console.log(data_array);
  data_array = JSON.stringify(data_array);
  //console.log(data_array)
  //console.log(phone_number)
  let fcm_message = {
    token : fcm_target_token,
    data:{
      title:title1,
      body:body1,
      'latitude':latitude,
      'longitude':longitude,
      'phone_number':phone_number,
      'data_array' : data_array
    },
    //data2:data_array

  }
  return new Promise((resolve,reject)=>{
    admin.messaging().send(fcm_message).then((res)=>{
      console.log('Successfully sent fcm message:',res);
      resolve(res);
    }).catch((err)=>{
      console.log('Fail to sent fcm message',err);
      reject(err);
    })

  });



}
module.exports.fall_alarm = async (req,res)=>{
  let wifi_mac_address = req.body.wifi_mac_address;
  let alarm_time = req.body.alarm_time;

  //let fcm_target_token = ''//디비로부터 가져옴
  //let fcm_message={}
  if(!format.checkValidMacAddress(wifi_mac_address))
  {
      res.send({'key':1}) //센서 양식 에러
      return;
  }

  pool.getConnection().then((conn)=>{
    conn.query(`insert into Alarm (wifi_mac_address,alarm_time) values('${wifi_mac_address}','${alarm_time}')`).then((data)=>{
      console.log('알람 데이터 삽입 성공')
      //낙상 데이터 전송을 위해 연락처 , 위도,경도,이름 필요함
      conn.query(`select email_address, board_nickname,phone_number,latitude,longitude from Sensor
      where wifi_mac_address='${wifi_mac_address}'`).then((data)=>{
        let phone_number = data[0]['phone_number']; //연락처
        let latitude = data[0]['latitude']; //위도
        let longitude = data[0]['longitude']; //경도 
        let board_nickname = data[0]['board_nickname']; //이름
        let data_array = new Array();
        let email_address = data[0]['email_address'];
        console.log(phone_number,latitude,longitude,board_nickname);
        conn.query(`select hospital_name, hospital_location,hospital_phone_num,category,emergency_room,latitude,longitude
        from Hospital where wifi_mac_address='${wifi_mac_address}'`).then((data)=>{
          for(let i =0; i<data.length; i++)
          {
            data_array.push(data[i]);
          }
          conn.query(`select token from User where email_address='${email_address}'`).then((data)=>{
            let fcm_target_token = data[0]['token'];
            console.log(fcm_target_token)
            send_fcm(phone_number,latitude,longitude,board_nickname,data_array,fcm_target_token,alarm_time).then((resolve)=>{
              console.log(resolve)
              console.log('낙상 저장 및 알림 성공 ')
              res.send({'key':2});
            }).catch((err)=>{
              console.log('낙상 저장 및 알림 실패 ')
              res.send({'key':3}) //낙상 저장 및 알림 실패
            })
          }).catch((err)=>{
            console.log('낙상 알림 전송을 위해 유저테이블 접근시 에러발생 '+err)
            res.send({'key':0,'err_code':err.code});
          })
          //return_val = await 
        }).catch((err)=>{
          console.log('낙상 알림 전송을 위해 병원테이블 접근시 에러발생'+err)
          res.send({'key':0,'err_code':err.code});
        })
      
      }).catch((err)=>{
        console.log('낙상알림 전송을 위해 Sensor 테이블 접근시 에러발생 '+err)
        res.send({'key':0,'err_code':err.code});

      })

    }).catch((err)=>{
      console.log('낙상알림 db 저장시 에러발생'+err)
      res.send({'key':0,'err_code':err.code});
    })
    conn.release();

  }).catch((err)=>{
    console.log('낙상 알림 전송 첫 단계 에러발생')
    res.send({'key':0,'err_code':err.code});
  })


}
