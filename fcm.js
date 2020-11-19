const mysql = require('mysql');
const config = require('./db_config.json');

var admin= require('firebase-admin');
var serviceAccount = require('/home/seokgyuhong/nodejs_practice/capstone_project1/fcm_key/my-application-eb059-firebase-adminsdk-2857l-84af43d294.json'); // my key로 수정
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
    });

const pool = mysql.createPool(config);

// 낙상 알림 전송 fcm
function send_fcm_fall(email_addr, location, res){
    let fcm_target_token = ''//디비로부터 가져옴
    let fcm_message={}
    conn.query(`select token from User where email_address='${email_addr}'`).then((data)=>{
        if(data[0]===undefined)
        {
          console.log('디비에서 토큰 가져오는 중 에러 발생');
          res.send({'key':-1}); // 토큰 가져오기 에러 
        }
        else
        {
          fcm_target_token = data[0]['token'];
          fcm_message = { //토큰메시지 객체 
            notification:{
                title:'낙상 알림 발생 !',
                body:`장소 : '${location}'`},
            token:fcm_target_token
          };
          admin.messaging().send(fcm_message).then((response)=>{
            console.log('Successfully sent fcm message:',response);
            res.send({'key':1})//토큰메시지 전송 성공
          }).catch((err)=>{
            console.log('Error sending firebase message:',err);
          })
        }
  
      }).catch((err)=>{
        console.log('fcm 전송을위해 db token 접근시 에러'+err.code);
        res.send({'key':-2,'err_code':err.code});
      })
      conn.release();
}

// 센서 연결 끊김 알림 전송 fcm
function send_fcm_connect(email_addr, location, nickname, res){
    let fcm_target_token = ''//디비로부터 가져옴
    let fcm_message={}
    conn.query(`select token from User where email_address='${email_addr}'`).then((data)=>{
        if(data[0]===undefined)
        {
          console.log('디비에서 토큰 가져오는 중 에러 발생');
          res.send({'key':-1}); // 토큰 가져오기 에러 
        }
        else
        {
          fcm_target_token = data[0]['token'];
          fcm_message ={ //토큰메시지 객체 
            notification:{
                title:'센서 연결이 끊어졌습니다 !',
                body:`장소 : '${location}', 닉네임 : '${nickname}`},
            token:fcm_target_token, 
          };
          admin.messaging().send(fcm_message).then((response)=>{
            console.log('Successfully sent fcm message:',response);
            res.send({'key':1})//토큰메시지 전송 성공
          }).catch((err)=>{
            console.log('Error sending firebase message:',err);
          })
        }
      }).catch((err)=>{
        console.log('fcm 전송을위해 db token 접근시 에러'+err.code);
        res.send({'key':-2,'err_code':err.code});
      })
      conn.release();
}

// mac address로 역할에 따른 fcm 주기
module.exports.send_fcm_byMac = (mac_addr, res, flag)=>{
//   let email_address = req.body.email_address;
  pool.getConnection().then((conn)=>{
      conn.query(`select * from Sensor where mac_address='${mac_addr}'`).then((data)=>{
          if(data[0]===undefined){
            console.log('sensor table에서 user email 가져오는 중 에러 발생');
            res.send({'key':-1}); // email 가져오기 에러
          }
          else{
              if(flag === 'fall'){ // fall 알람 주는 역할
                  send_fcm_fall(data[0]['email_address'], data[0]['location'], res); 
              }
              else if(flag === 'connect'){ // connect 알람 주는 역할
                  send_fcm_connect(data[0]['email_address'], data[0]['location'], data[0]['board_nickname'], res);
              }

          }
      })
  }).catch((err)=>{
    console.log('Fcm 토큰 처리 중 디비 에러 '+err.code);
    res.send({'key':-3,'err_code':err.code});
  })
}