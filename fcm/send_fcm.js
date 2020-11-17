var admin= require('firebase-admin');
const { pool } = require('../secret_info/db_connect');
var serviceAccount = require('/home/seokgyuhong/nodejs_practice/capstone_project1/fcm_key/my-application-eb059-firebase-adminsdk-2857l-84af43d294.json');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
    });

module.exports.send_fcm = (req,res)=>{
  let email_address = req.body.email_address;
  let fcm_target_token = ''//디비로부터 가져옴
  let fcm_message={}
  pool.getConnection().then((conn)=>{
    conn.query(`select token from User where email_address='${email_address}'`).then((data)=>{
      if(data[0]===undefined)
      {
        console.log('디비에서 토큰 가져오는 중 에러 발생');
        res.send({'key':1});
      }
      else
      {
        fcm_target_token = data[0]['token'];
        fcm_message ={ //토큰메시지 객체 
          notification:{
              title:'낙상 알림 발생 !',
              body:`장소 : location,타임 : 123:111:111`},
          token:fcm_target_token, 
        };
        admin.messaging().send(fcm_message).then((response)=>{
          console.log('Successfully sent fcm message:',response);
          res.send({'key':2})//토큰메시지 전송 성공s
        }).catch((err)=>{
          console.log('Error sending firebase message:',err);
        })
      }

    }).catch((err)=>{
      console.log('fcm 전송을위해 db token 접근시 에러'+err.code);
      res.send({'key':0,'err_code':err.code});
    })
    conn.release();
  }).catch((err)=>{
    console.log('Fcm 토큰 처리 중 디비 에러 '+err.code);
    res.send({'key':0,'err_code':err.code});
  })

}